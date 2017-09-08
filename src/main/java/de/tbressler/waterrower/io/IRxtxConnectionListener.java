package de.tbressler.waterrower.io;

import de.tbressler.waterrower.msg.AbstractMessage;

/**
 * Listener for RXTX connections.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public interface IRxtxConnectionListener {

    /**
     * Called if connection was established.
     */
    void onConnected();

    /**
     * Called if a message was received.
     *
     * @param msg The received message.
     */
    void onMessageReceived(AbstractMessage msg);

    /**
     * Called if connection was closed.
     */
    void onDisconnected();

    /**
     * Called if a connection error occurred.
     */
    void onError();

}
