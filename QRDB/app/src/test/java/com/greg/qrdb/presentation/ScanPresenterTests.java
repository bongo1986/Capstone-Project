package com.greg.qrdb.presentation;

import com.greg.domain.DataRetreiverService;
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
    private DataRetreiverService mDataRetreiverService;
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
        mDataRetreiverService = mock(DataRetreiverService.class);
        strRetreiver = mock(StringRetreiver.class);
        when(strRetreiver.getString(R.string.text_code_not_found)).thenReturn("not found");
        when(strRetreiver.getString(R.string.text_existing_qr)).thenReturn("existing");
        when(strRetreiver.getString(R.string.text_new_qr_code_saved)).thenReturn("saved");

        presenterToTest = new ScanPresenter(mDataRetreiverService, mockService, strRetreiver, new MockThreadProvider());
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

        presenterToTest.setView(mockView);
        QrCode qr = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1}, true);
        when(mockService.GetQrCodeForUuid(Matchers.any(UUID.class))).thenReturn(qr);
        when(mDataRetreiverService.findScannedCode(Matchers.anyString())).thenReturn(qr);

        //QrCode newQrCode = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1}, false);
        presenterToTest.qrCodeScanned("Test str");
        verify(mockView, times(1)).RedirectToQrCodeListAfterScanning(qr, "existing");

    }
    @Test
    public void scanningExistingCode_ExistingQrCodeUpdated() {

        presenterToTest.setView(mockView);
        QrCode qr = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1}, true);
        when(mockService.GetQrCodeForUuid(Matchers.any(UUID.class))).thenReturn(qr);
        when(mDataRetreiverService.findScannedCode(Matchers.anyString())).thenReturn(qr);

        //QrCode newQrCode = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1}, false);
        presenterToTest.qrCodeScanned("Test str");
        verify(mockService, times(1)).UpdateQrCodeSync(qr);

    }

    @Test
    public void scanningNewCode_RedirectToQrCodeListAfterScanningCalled() {

        presenterToTest.setView(mockView);
        QrCode qr = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1}, true);
        when(mockService.GetQrCodeForUuid(Matchers.any(UUID.class))).thenReturn(null);
        when(mDataRetreiverService.findScannedCode(Matchers.anyString())).thenReturn(qr);

        //QrCode newQrCode = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1}, false);
        presenterToTest.qrCodeScanned("Test str");
        verify(mockView, times(1)).RedirectToQrCodeListAfterScanning(qr, "saved");

    }
    @Test
    public void scanningNewCode_ExistingQrCodeUpdated() {

        presenterToTest.setView(mockView);
        QrCode qr = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1}, true);
        when(mockService.GetQrCodeForUuid(Matchers.any(UUID.class))).thenReturn(null);
        when(mDataRetreiverService.findScannedCode(Matchers.anyString())).thenReturn(qr);

        //QrCode newQrCode = new QrCode("aaa","", UUID.randomUUID(),new byte[]{0,1}, false);
        presenterToTest.qrCodeScanned("Test str");
        verify(mockService, times(1)).InsertQrCodeSync(qr, true);

    }
}
