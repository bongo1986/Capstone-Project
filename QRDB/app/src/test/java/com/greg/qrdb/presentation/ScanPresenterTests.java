package com.greg.qrdb.presentation;


import com.greg.domain.FirebaseService;
import com.greg.domain.QrCode;
import com.greg.domain.QrCodeService;
import com.greg.presentation.ScanPresenter;
import com.greg.presentation.ScanView;
import com.greg.qrdb.R;
import com.greg.utils.StringRetreiver;
import com.greg.utils.ThreadProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.util.UUID;

import rx.Scheduler;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Greg on 13-11-2016.
 */
public class ScanPresenterTests {
    private QrCodeService mockService;
    private ScanView mockView;
    private StringRetreiver strRetreiver;
    private FirebaseService mFirebaseService;
    private ScanPresenter presenterToTest;

    private final class MockThreadProvider implements ThreadProvider {

        @Override
        public Scheduler mainThread() {
            return Schedulers.immediate();
        }

        @Override
        public Scheduler newThread() {
            return Schedulers.immediate();
        }
    }

    @Before
    public void setup() {
        mockService = mock(QrCodeService.class);
        mockView = mock(ScanView.class);
        mFirebaseService = mock(FirebaseService.class);
        strRetreiver = mock(StringRetreiver.class);
        when(strRetreiver.getString(R.string.text_code_not_found)).thenReturn("not found");
        when(strRetreiver.getString(R.string.text_existing_qr)).thenReturn("existing");
        when(strRetreiver.getString(R.string.text_new_qr_code_saved)).thenReturn("saved");

        presenterToTest = new ScanPresenter(mFirebaseService, mockService, strRetreiver, new MockThreadProvider());
    }

    @Test
    public void codeNotFound_NotFoundMessageShown() {

        presenterToTest.setView(mockView);
        //QrCode newQrCode = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1}, false);
        presenterToTest.qrCodeScanned("Test str");
        verify(mockView, times(1)).ShowErrorMessage("not found");

    }
    @Test
    public void scanningExistingCode_RedirectToQrCodeListAfterScanningCalled() {
        UUID uuid = UUID.randomUUID();
        presenterToTest.setView(mockView);
        QrCode qr = new QrCode("aaa","", uuid,new byte[]{0,1}, true, 1);
        when(mockService.GetQrCodeForUuid(Matchers.any(UUID.class))).thenReturn(qr);
        when(mFirebaseService.findQrCodeByUuid(Matchers.anyString())).thenReturn(qr);

        //QrCode newQrCode = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1}, false);
        presenterToTest.qrCodeScanned(uuid.toString());
        verify(mockView, times(1)).RedirectToQrCodeListAfterScanning(qr, "existing");

    }
    @Test
    public void scanningExistingCode_ExistingQrCodeUpdated() {
        UUID uuid = UUID.randomUUID();
        presenterToTest.setView(mockView);
        QrCode qr = new QrCode("aaa","", uuid,new byte[]{0,1}, true, 1);
        when(mockService.GetQrCodeForUuid(Matchers.any(UUID.class))).thenReturn(qr);
        when(mFirebaseService.findQrCodeByUuid(Matchers.anyString())).thenReturn(qr);

        //QrCode newQrCode = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1}, false);
        presenterToTest.qrCodeScanned(uuid.toString());
        verify(mockService, times(1)).UpdateQrCodeSync(Matchers.eq(qr), anyBoolean());

    }

    @Test
    public void scanningNewCode_RedirectToQrCodeListAfterScanningCalled() {
        UUID uuid = UUID.randomUUID();
        presenterToTest.setView(mockView);
        QrCode qr = new QrCode("aaa","", uuid,new byte[]{0,1}, true, 1);
        when(mockService.GetQrCodeForUuid(Matchers.any(UUID.class))).thenReturn(null);
        when(mFirebaseService.findQrCodeByUuid(Matchers.anyString())).thenReturn(qr);

        //QrCode newQrCode = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1}, false);
        presenterToTest.qrCodeScanned(uuid.toString());
        verify(mockView, times(1)).RedirectToQrCodeListAfterScanning(qr, "saved");

    }
    @Test
    public void scanningNewCode_ExistingQrCodeUpdated() {
        UUID uuid = UUID.randomUUID();
        presenterToTest.setView(mockView);
        QrCode qr = new QrCode("aaa","", uuid,new byte[]{0,1}, true, 1);
        when(mockService.GetQrCodeForUuid(Matchers.any(UUID.class))).thenReturn(null);
        when(mFirebaseService.findQrCodeByUuid(Matchers.anyString())).thenReturn(qr);

        //QrCode newQrCode = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1}, false);
        presenterToTest.qrCodeScanned(uuid.toString());
        verify(mockService, times(1)).InsertQrCodeSync(Matchers.eq(qr), Matchers.eq(true), anyBoolean());

    }
}
