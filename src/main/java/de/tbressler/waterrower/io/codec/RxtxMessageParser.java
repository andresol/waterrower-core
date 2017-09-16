package de.tbressler.waterrower.io.codec;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.IMessageInterpreter;
import de.tbressler.waterrower.io.msg.interpreter.*;
import de.tbressler.waterrower.log.Log;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
import static de.tbressler.waterrower.log.Log.SERIAL;

/**
 * Decodes and encodes messages received from or sent to the Water Rower S4/S5 monitor.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RxtxMessageParser {

    /* List of message interpreters. */
    private List<IMessageInterpreter> interpreters = new ArrayList<>();


    /**
     * Constructor.
     */
    public RxtxMessageParser() {
        createAndAddMessageInterpreters();
    }

    /* Adds message interpreters to this parser. */
    private void createAndAddMessageInterpreters() {

        // Incoming communication:
        interpreters.add(new AcknowledgeMessageInterpreter());
        interpreters.add(new ErrorMessageInterpreter());
        interpreters.add(new HardwareTypeMessageInterpreter());
        interpreters.add(new ModelInformationMessageInterpreter());
        interpreters.add(new PingMessageInterpreter());
        interpreters.add(new PulseCountMessageInterpreter());
        interpreters.add(new StrokeMessageInterpreter());

        // Outgoing communication:
        interpreters.add(new ExitCommunicationMessageInterpreter());
        interpreters.add(new RequestModelInformationMessageInterpreter());
        interpreters.add(new ResetMessageInterpreter());
        interpreters.add(new StartCommunicationMessageInterpreter());
    }


    /**
     * Decodes the given byte array to a message object. Returns null if the message
     * couldn't be decoded.
     *
     * @param bytes The byte array.
     * @return The message object or null.
     */
    public AbstractMessage decode(byte[] bytes) {

        Log.debug(SERIAL, "Parsing message to object.");

        String msgType = new String(bytes, 0, 1, UTF_8);

        for (IMessageInterpreter interpreter : interpreters) {
            if (interpreter.getMessageTypeChar() == null)
                continue;
            if (msgType.equals(interpreter.getMessageTypeChar()))
                continue;
            return interpreter.decode(bytes);
        }

        Log.warn(SERIAL, "Message couldn't be parsed to an object!" +
                " Unknown message type '"+ msgType +"'.");

        return null;
    }


    /**
     * Encodes the given message to a byte array. Returns null if the message
     * couldn't be encoded.
     *
     * @param msg The message.
     * @return The byte array or null.
     */
    public byte[] encode(AbstractMessage msg) {

        Log.debug(SERIAL, "Parsing message '"+msg.toString()+"' to bytes.");

        for (IMessageInterpreter interpreter : interpreters) {
            if (msg.getClass().equals(interpreter.getMessageType()))
                continue;
            return  interpreter.encode(msg);
        }

        Log.warn(SERIAL, "Message couldn't be parsed to a byte array!" +
                " Unknown message type.");

        return null;
    }

}
