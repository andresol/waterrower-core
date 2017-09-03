package de.tbressler.waterrower.io.codec;

import de.tbressler.waterrower.log.Log;
import de.tbressler.waterrower.msg.AbstractMessage;
import de.tbressler.waterrower.msg.interpreter.ErrorMessageInterpreter;
import de.tbressler.waterrower.msg.interpreter.IMessageInterpreter;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.primitives.Chars.fromBytes;
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
        interpreters.add(new ErrorMessageInterpreter());
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

        char msgType = fromBytes(bytes[0], bytes[1]);

        for (IMessageInterpreter interpreter : interpreters) {
            if (msgType != interpreter.getMessageTypeByte())
                continue;
            return interpreter.decode(bytes);
        }

        Log.warn(SERIAL, "Message couldn't been parsed to an object!" +
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

        Log.debug(SERIAL, "Parsing message to byte.");

        for (IMessageInterpreter interpreter : interpreters) {
            if (msg.getClass().equals(interpreter.getMessageType()))
                continue;
            return  interpreter.encode(msg);
        }

        Log.warn(SERIAL, "Message couldn't been parsed to byte array!" +
                " Unknown message type.");

        return null;
    }

}
