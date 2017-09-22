package de.tbressler.waterrower.io.codec;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
import static de.tbressler.waterrower.io.utils.ByteUtils.bufferToString;
import static de.tbressler.waterrower.log.Log.MESSAGES;
import static de.tbressler.waterrower.log.Log.SERIAL;
import static java.util.Objects.requireNonNull;

/**
 * Decodes messages (byte > msg).
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class RxtxMessageFrameDecoder extends ByteToMessageDecoder {

    /* The message parser. */
    private final RxtxMessageParser parser;


    /**
     * Constructor.
     *
     * @param parser The message parser, must not be null.
     */
    public RxtxMessageFrameDecoder(RxtxMessageParser parser) {
        this.parser = requireNonNull(parser);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        Log.debug(MESSAGES, "Decoder received new message buffer:\n" +
                " Buffer: " + bufferToString(in));

        int numberOfBytes = in.readableBytes();

        // Check if bytes are available and this is not an empty frame.
        if (numberOfBytes == 0) {
            Log.warn(SERIAL, "No bytes in message buffer! Skipping frame.");
            return;
        }

        byte [] byteArray = new byte[numberOfBytes];

        in.readBytes(byteArray, 0, numberOfBytes);

        Log.debug(SERIAL, "Message buffer decoded to: >" + new String(byteArray, UTF_8) + "<");

        // Decode the message.
         AbstractMessage decodedMessage = parser.decode(byteArray);
         if (decodedMessage == null) {
             Log.debug(SERIAL, "Couldn't decode bytes to message! Skipping it.");
             return;
         }

         out.add(decodedMessage);

         return;
        }

}
