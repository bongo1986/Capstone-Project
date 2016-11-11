package com.greg.qrdb.presentation;

import com.greg.domain.QrCode;
import com.greg.domain.QrCodeService;
import com.greg.presentation.CRUDQrCodePresenter;
import com.greg.presentation.CRUDQrCodeView;
import com.greg.qrdb.R;
import com.greg.utils.StringRetreiver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.UUID;

import rx.Observable;
import rx.schedulers.TestScheduler;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static rx.Observable.just;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CRUDQrCodePresenterTests {
    private QrCodeService mockService;
    private CRUDQrCodeView mockView;
    private StringRetreiver strRetreiver;

    @Before
    public void setup() {
        mockService = mock(QrCodeService.class);
        mockView = mock(CRUDQrCodeView.class);
        strRetreiver = mock(StringRetreiver.class);
        when(strRetreiver.getString(R.string.text_too_long_message)).thenReturn("%d");
        when(strRetreiver.getString(R.string.text_qr_code_not_saved)).thenReturn("not saved");
    }

    @Test
    public void create_emptyTitle_showTitleErrorCalledOnView() {

        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);
        QrCode newQrCode = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1});
        presenterToTest.CreateNewQrCode(newQrCode);
        verify(mockView, times(1)).showTitleError(anyString());

    }
    @Test
    public void update_emptyTitle_showTitleErrorCalledOnView() {

        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);
        QrCode newQrCode = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1});
        presenterToTest.UpdateQrCode(newQrCode);
        verify(mockView, times(1)).showTitleError(anyString());

    }
    @Test
    public void create_emptyDescription_showDescriptionErrorCalledOnView() {

        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);

        QrCode newQrCode = new QrCode("","aaa", UUID.randomUUID(),new byte[]{0,1});
        presenterToTest.CreateNewQrCode(newQrCode);
        verify(mockView, times(1)).showDescriptionError(anyString());

    }
    @Test
    public void update_emptyDescription_showDescriptionErrorCalledOnView() {

        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);

        QrCode newQrCode = new QrCode("","aaa", UUID.randomUUID(),new byte[]{0,1});
        presenterToTest.UpdateQrCode(newQrCode);
        verify(mockView, times(1)).showDescriptionError(anyString());

    }
    @Test
    public void create_titleToLong_showTitleErrorCalledOnView() {

        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);

        String title = "";

        for(int i = 0; i < 32; i++){
            title += "a";
        }

        QrCode newQrCode = new QrCode("desc",title, UUID.randomUUID(),new byte[]{0,1});
        presenterToTest.CreateNewQrCode(newQrCode);
        verify(mockView, times(1)).showTitleError("30");

    }
    @Test
    public void update_titleToLong_showTitleErrorCalledOnView() {

        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);

        String title = "";

        for(int i = 0; i < 32; i++){
            title += "a";
        }

        QrCode newQrCode = new QrCode("desc",title, UUID.randomUUID(),new byte[]{0,1});
        presenterToTest.UpdateQrCode(newQrCode);
        verify(mockView, times(1)).showTitleError("30");

    }
    @Test
    public void create_descriptionToLong_showTitleErrorCalledOnView() {

        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);

        String description = "";

        for(int i = 0; i < 302; i++){
            description += "a";
        }

        QrCode newQrCode = new QrCode(description,"title", UUID.randomUUID(),new byte[]{0,1});
        presenterToTest.CreateNewQrCode(newQrCode);
        verify(mockView, times(1)).showDescriptionError("300");

    }
    @Test
    public void update_descriptionToLong_showTitleErrorCalledOnView() {

        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);

        String description = "";

        for(int i = 0; i < 302; i++){
            description += "a";
        }

        QrCode newQrCode = new QrCode(description,"title", UUID.randomUUID(),new byte[]{0,1});
        presenterToTest.UpdateQrCode(newQrCode);
        verify(mockView, times(1)).showDescriptionError("300");

    }
    @Test
    public void create_savingQrCode_waitDialogShown() {
        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);
        TestScheduler testScheduler = new TestScheduler();
        Observable<Long> result = just((long)1).subscribeOn(testScheduler);
        QrCode newQrCode = new QrCode("description","title", UUID.randomUUID(),new byte[]{0,1});
        when(mockService.InsertQRcode(Mockito.eq(newQrCode), anyBoolean())).thenReturn(result);

        presenterToTest.CreateNewQrCode(newQrCode);
        verify(mockView, times(1)).showWaitDialog();
    }
    @Test
    public void update_savingQrCode_waitDialogShown() {
        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);
        TestScheduler testScheduler = new TestScheduler();
        Observable<Integer> result = just(1).subscribeOn(testScheduler);
        QrCode newQrCode = new QrCode("description","title", UUID.randomUUID(),new byte[]{0,1});
        when(mockService.UpdateQrCode(Mockito.eq(newQrCode))).thenReturn(result);

        presenterToTest.UpdateQrCode(newQrCode);
        verify(mockView, times(1)).showWaitDialog();
    }
    @Test
    public void create_savingQrCodeSuccessful_waitDialogHidden() {
        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);
        TestScheduler testScheduler = new TestScheduler();
        Observable<Long> result = just((long)1).subscribeOn(testScheduler);
        QrCode newQrCode = new QrCode("description","title", UUID.randomUUID(),new byte[]{0,1});
        when(mockService.InsertQRcode(Matchers.any(QrCode.class), anyBoolean())).thenReturn(result);
        presenterToTest.CreateNewQrCode(newQrCode);
        testScheduler.triggerActions();
        verify(mockView, times(1)).hideWaitDialog();
    }
    @Test
    public void update_savingQrCodeSuccessful_waitDialogHidden() {
        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);
        TestScheduler testScheduler = new TestScheduler();
        Observable<Integer> result = just(1).subscribeOn(testScheduler);
        QrCode newQrCode = new QrCode("description","title", UUID.randomUUID(),new byte[]{0,1});
        when(mockService.UpdateQrCode(Matchers.any(QrCode.class))).thenReturn(result);
        presenterToTest.UpdateQrCode(newQrCode);
        testScheduler.triggerActions();
        verify(mockView, times(1)).hideWaitDialog();
    }
    @Test
    public void create_savingQrCodeSuccessful_redirectingToQrCode() {
        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);
        TestScheduler testScheduler = new TestScheduler();
        Observable<Long> result = just((long)1).subscribeOn(testScheduler);
        QrCode newQrCode = new QrCode("description","title", UUID.randomUUID(),new byte[]{0,1});
        when(mockService.InsertQRcode(Mockito.eq(newQrCode), anyBoolean())).thenReturn(result);
        presenterToTest.CreateNewQrCode(newQrCode);
        testScheduler.triggerActions();
        verify(mockView, times(1)).redirectToMyQrCodesAfterCreate();
    }
    @Test
    public void update_savingQrCodeSuccessful_redirectingToQrCode() {
        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);
        TestScheduler testScheduler = new TestScheduler();
        Observable<Integer> result = just(1).subscribeOn(testScheduler);
        QrCode newQrCode = new QrCode("description","title", UUID.randomUUID(),new byte[]{0,1});
        when(mockService.UpdateQrCode(Mockito.eq(newQrCode))).thenReturn(result);
        presenterToTest.UpdateQrCode(newQrCode);
        testScheduler.triggerActions();
        verify(mockView, times(1)).redirectToMyQrCodesAfterUpdate();
    }

    @Test
    public void create_qrCodeNotSaved_waitDialogHidden() {
        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);
        TestScheduler testScheduler = new TestScheduler();
        Observable<Long> result = just((long)-1).subscribeOn(testScheduler);
        QrCode newQrCode = new QrCode("description","title", UUID.randomUUID(),new byte[]{0,1});
        when(mockService.InsertQRcode(Matchers.any(QrCode.class), anyBoolean())).thenReturn(result);
        presenterToTest.CreateNewQrCode(newQrCode);
        testScheduler.triggerActions();
        verify(mockView, times(1)).hideWaitDialog();
    }
    @Test
    public void update_qrCodeNotSaved_waitDialogHidden() {
        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);
        TestScheduler testScheduler = new TestScheduler();
        Observable<Integer> result = just(0).subscribeOn(testScheduler);
        QrCode newQrCode = new QrCode("description","title", UUID.randomUUID(),new byte[]{0,1});
        when(mockService.UpdateQrCode(Matchers.any(QrCode.class))).thenReturn(result);
        presenterToTest.UpdateQrCode(newQrCode);
        testScheduler.triggerActions();
        verify(mockView, times(1)).hideWaitDialog();
    }
    @Test
    public void qrCodeNotSaved_snackBarShown() {
        CRUDQrCodePresenter presenterToTest = new CRUDQrCodePresenter(mockService, strRetreiver);
        presenterToTest.setView(mockView);
        TestScheduler testScheduler = new TestScheduler();
        Observable<Long> result = just((long)-1).subscribeOn(testScheduler);
        QrCode newQrCode = new QrCode("description","title", UUID.randomUUID(),new byte[]{0,1});
        when(mockService.InsertQRcode(Matchers.any(QrCode.class), anyBoolean())).thenReturn(result);
        presenterToTest.CreateNewQrCode(newQrCode);
        testScheduler.triggerActions();
        verify(mockView, times(1)).showErrorMessageSnackBar("not saved");
    }

}