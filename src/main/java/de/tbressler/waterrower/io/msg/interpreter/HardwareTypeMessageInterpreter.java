package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessageInterpreter;
import de.tbressler.waterrower.io.msg.in.HardwareTypeMessage;

/**
 * Interpreter for:
 *
 * Hardware Type (S4/S5 -> PC).
 *
 * The Water Rower will reply with this packet when it receives a "USB" packet and will then
 * proceed to send other packets accordingly until it switch’s off or the application issues an
 * exit packet.
 *
 * [_][WR_] + 0x0D0A
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class HardwareTypeMessageInterpreter extends AbstractMessageInterpreter<HardwareTypeMessage> {

    @Override
    public String getMessageIdentifier() {
        return "_";
    }

    @Override
    public Class<HardwareTypeMessage> getMessageType() {
        return HardwareTypeMessage.class;
    }

    @Override
    public HardwareTypeMessage decode(String msg) {
        boolean isWaterRower = msg.startsWith("_WR_");
        return new HardwareTypeMessage(isWaterRower);
    }

    @Override
    public String encode(HardwareTypeMessage msg) {
        throw new IllegalStateException("This type of message can not be send to the Water Rower S4/S5 monitor.");
    }

}
