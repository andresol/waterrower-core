package de.tbressler.waterrower.msg.interpreter;

import de.tbressler.waterrower.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.msg.out.ResetMessage;

/**
 * Interpreter for:
 *
 * Request the rowing computer to reset (PC -> S4/S5).
 *
 * Request the rowing computer to perform a reset; this will be identical to the user performing
 * this with the power button. Used prior to configuring the rowing computer from a PC.
 * Interactive mode will be disabled on a reset.
 *
 * [R][ESET] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class ResetMessageInterpreter extends AbstractMessageInterpreter<ResetMessage> {

    @Override
    public String getMessageTypeChar() {
        return null;
    }

    @Override
    public Class<ResetMessage> getMessageType() {
        return ResetMessage.class;
    }

    @Override
    public ResetMessage decode(byte[] bytes) {
        throw new IllegalStateException("This type of message should not be send by Water Rower S4/S5 monitor to the PC.");
    }

    @Override
    public byte[] encode(ResetMessage msg) {
        return new String("RESET").getBytes();
    }

}
