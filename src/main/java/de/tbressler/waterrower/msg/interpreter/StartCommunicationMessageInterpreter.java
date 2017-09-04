package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.msg.out.StartCommunicationMessage;

/**
 * Interpreter for:
 *
 * Application starting communication's (PC -> S4/S5).
 *
 * This is the very first packet sent by an application once the COM port is opened, this will
 * tell the rowing computer to reply with its hardware type packet.
 *
 * [U][SB] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class StartCommunicationMessageInterpreter extends AbstractMessageInterpreter<StartCommunicationMessage> {

    @Override
    public String getMessageTypeChar() {
        return null;
    }

    @Override
    public Class<StartCommunicationMessage> getMessageType() {
        return StartCommunicationMessage.class;
    }

    @Override
    public StartCommunicationMessage decode(byte[] bytes) {
        throw new IllegalStateException("This type of message should not be send by Water Rower S4/S5 monitor to the PC.");
    }

    @Override
    public byte[] encode(StartCommunicationMessage msg) {
        return new String("USB").getBytes();
    }

}
