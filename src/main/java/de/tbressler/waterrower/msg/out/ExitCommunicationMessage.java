package de.tbressler.waterrower.msg.out;

import de.tbressler.waterrower.msg.AbstractMessage;

/**
 * Application is exiting (PC -> S4/S5).
 *
 * Any application wishing to normally terminate (close) is required to send this packet to stop
 * the automatic packets being sent to the PC.
 *
 * [E][XIT] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class ExitCommunicationMessage extends AbstractMessage {}
