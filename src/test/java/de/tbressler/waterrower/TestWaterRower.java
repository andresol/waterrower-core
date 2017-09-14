package de.tbressler.waterrower;

import de.tbressler.waterrower.io.IRxtxConnectionListener;
import de.tbressler.waterrower.io.RxtxCommunicationService;
import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.out.ExitCommunicationMessage;
import de.tbressler.waterrower.io.msg.out.ResetMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;
import static de.tbressler.waterrower.model.ErrorCode.ERROR_COMMUNICATION_FAILED;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

/**
 * Tests for class WaterRower.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWaterRower {

    /* Class under test. */
    private WaterRower waterRower;

    /* Direct executor service, so that the test can be executed in one thread only. */
    private ExecutorService directExecutorService = newDirectExecutorService();

    // Mocks:
    private RxtxCommunicationService communicationService = mock(RxtxCommunicationService.class, "communicationService");
    private RxtxDeviceAddress address = mock(RxtxDeviceAddress.class, "address");
    private IWaterRowerListener waterRowerListener = mock(IWaterRowerListener.class, "waterRowerListener");
    private AbstractMessage message = mock(AbstractMessage.class, "message");

    // Capture:
    private ArgumentCaptor<IRxtxConnectionListener> callback = forClass(IRxtxConnectionListener.class);
    private IRxtxConnectionListener rxtxConnectionListener;


    @Before
    public void setUp() {
        waterRower = new WaterRower(communicationService, directExecutorService);
        waterRower.addWaterRowerListener(waterRowerListener);

        captureRxtxConnectionListener();
    }

    /* Captures the internal RXTX connection listener. */
    private void captureRxtxConnectionListener() {
        verify(communicationService, times(1)).addRxtxConnectionListener(callback.capture());
        rxtxConnectionListener = callback.getValue();
    }


    /**
     * Checks if an exception is thrown, when communicationService is null.
     */
    @Test(expected = NullPointerException.class)
    public void new_withNullCommunicationService_throwsException() {
       new WaterRower(null, directExecutorService);
    }

    /**
     * Checks if an exception is thrown, when executorService is null.
     */
    @Test(expected = NullPointerException.class)
    public void new_withNullExecutorService_throwsException() {
        new WaterRower(communicationService, null);
    }


    /**
     * Checks if an exception is thrown, when address is null.
     */
    @Test(expected = NullPointerException.class)
    public void connect_withNullAddress_throwsException() throws Exception {
        waterRower.connect(null);
    }

    /**
     * Checks if connect() throws an IOException when already connected.
     */
    @Test(expected = IOException.class)
    public void connect_whenConnected_throwsIOException() throws IOException {
        when(communicationService.isConnected()).thenReturn(true);
        waterRower.connect(address);
    }

    /**
     * Checks if connect() opens the connection on the communication service, if not yet connected.
     */
    @Test
    public void connect_whenNotConnectedYet_opensConnection() throws IOException {
        when(communicationService.isConnected()).thenReturn(false);

        waterRower.connect(address);

        verify(communicationService, times(1)).open(address);
    }

    /**
     * Checks if listener is called, if connection fails due to an exception.
     */
    @Test
    public void connect_whenConnectionFails_notifiesListeners() throws IOException {
        when(communicationService.isConnected()).thenReturn(false);
        doThrow(new IOException("a mocked exception!")).when(communicationService).open(address);

        waterRower.connect(address);

        verify(communicationService, times(1)).open(address);
        verify(waterRowerListener, times(1)).onError(ERROR_COMMUNICATION_FAILED);
    }


    @Test(expected = NullPointerException.class)
    public void sendAsync_withNullMessage_throwsException() throws Exception {
        waterRower.sendMessageAsync(null);
    }


    @Test(expected = IOException.class)
    public void sendAsync_whenNotConnected_throwsException() throws Exception {
        when(communicationService.isConnected()).thenReturn(false);
        waterRower.sendMessageAsync(message);
    }


    @Test
    public void sendAsync_withValidMessage_sendsMessage() throws Exception {
        when(communicationService.isConnected()).thenReturn(true);

        waterRower.sendMessageAsync(message);

        verify(communicationService, times(1)).send(message);
    }


    /**
     * Checks if disconnect() throws an IOException when not connected yet.
     */
    @Test(expected = IOException.class)
    public void disconnect_whenNotConnected_throwsIOException() throws IOException {
        when(communicationService.isConnected()).thenReturn(false);
        waterRower.disconnect();
    }

    /**
     * Checks if disconnect() closes the connection on the communication service, if connected.
     */
    @Test
    public void disconnect_whenConnected_closesConnection() throws IOException {
        when(communicationService.isConnected()).thenReturn(true);

        waterRower.disconnect();

        verify(communicationService, times(1)).send(any(ExitCommunicationMessage.class));
        verify(communicationService, times(1)).close();
    }


    /**
     * Checks if listener is called, if send "goodbye" fails due to an exception.
     */
    @Test
    public void disconnect_whenSendGoodbyeFails_notifiesListeners() throws IOException {
        when(communicationService.isConnected()).thenReturn(true);
        doThrow(new IOException("a mocked exception!")).when(communicationService).send(any(ExitCommunicationMessage.class));

        waterRower.disconnect();

        verify(communicationService, times(1)).send(any(ExitCommunicationMessage.class));
        verify(waterRowerListener, times(1)).onError(ERROR_COMMUNICATION_FAILED);
    }

    /**
     * Checks if listener is called, if connection fails due to an exception.
     */
    @Test
    public void disconnect_whenDisconnectionFails_notifiesListeners() throws IOException {
        when(communicationService.isConnected()).thenReturn(true);
        doThrow(new IOException("a mocked exception!")).when(communicationService).close();

        waterRower.disconnect();

        verify(communicationService, times(1)).send(any(ExitCommunicationMessage.class));
        verify(communicationService, times(1)).close();
        verify(waterRowerListener, times(1)).onError(ERROR_COMMUNICATION_FAILED);
    }


    @Test(expected = IOException.class)
    public void performReset_whenNotConnected_throwsException() throws Exception {
        when(communicationService.isConnected()).thenReturn(false);
        waterRower.performReset();
    }

    @Test
    public void performReset_whenConnected_sendsResetMessage() throws Exception {
        when(communicationService.isConnected()).thenReturn(true);

        waterRower.performReset();

        verify(communicationService, times(1)).send(any(ResetMessage.class));
    }


    @Test
    public void rxtxListenerOnConnected_sendsStartCommunicationMessage() throws Exception {
        when(communicationService.isConnected()).thenReturn(true);
        rxtxConnectionListener.onConnected();
        verify(communicationService, times(1)).send(any(StartCommunicationMessage.class));
    }

    @Test
    public void rxtxListenerOnConnected_whenSendStartCommunicationMessageFails_notifiesListenerAboutError() throws Exception {
        when(communicationService.isConnected()).thenReturn(true);
        doThrow(new IOException("a mocked exception!")).when(communicationService).send(any(StartCommunicationMessage.class));

        rxtxConnectionListener.onConnected();

        verify(waterRowerListener, times(1)).onError(ERROR_COMMUNICATION_FAILED);
    }

    @Test
    public void rxtxListenerOnConnected_whenNotConnected_throwsException() throws Exception {
        when(communicationService.isConnected()).thenReturn(false);

        rxtxConnectionListener.onConnected();

        verify(waterRowerListener, times(1)).onError(ERROR_COMMUNICATION_FAILED);
    }

    /**
     * Checks if IWaterRowerListener is notified, when IRxtxConnectionListener is called.
     */
    @Test
    public void rxtxListenerOnError_notificationOfListeners_whenOnErrorIsCalled() {
        rxtxConnectionListener.onError();
        verify(waterRowerListener, times(1)).onError(ERROR_COMMUNICATION_FAILED);
    }

    /**
     * Checks if IWaterRowerListener is notified, when IRxtxConnectionListener is called.
     */
    @Test
    public void rxtxListenerOnDisconnected_notificationOfListeners_whenOnDisconnectedIsCalled() {
        rxtxConnectionListener.onDisconnected();
        verify(waterRowerListener, times(1)).onDisconnected();
    }


    /**
     * Checks if an exception is thrown when listener is null.
     */
    @Test(expected = NullPointerException.class)
    public void addWaterRowerListener_withNull_throwsException() {
        waterRower.addWaterRowerListener(null);
    }

    /**
     * Checks if an exception is thrown when listener is null.
     */
    @Test(expected = NullPointerException.class)
    public void removeWaterRowerListener_withNull_throwsException() {
        waterRower.removeWaterRowerListener(null);
    }

}
