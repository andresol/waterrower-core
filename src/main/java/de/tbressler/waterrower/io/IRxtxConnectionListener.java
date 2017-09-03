package de.tbressler.waterrower.io;

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
     * Called if connection was closed.
     */
    void onDisconnected();

    /**
     * Called if a connection error occurred.
     */
    void onError();

}
