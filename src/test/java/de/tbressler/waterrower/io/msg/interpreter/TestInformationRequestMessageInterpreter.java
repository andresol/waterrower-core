package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.InformationRequestMessage;
import de.tbressler.waterrower.io.msg.Memory;
import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.in.ModelInformationMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import de.tbressler.waterrower.io.msg.out.RequestModelInformationMessage;
import de.tbressler.waterrower.model.MonitorType;
import org.junit.Before;
import org.junit.Test;

import static de.tbressler.waterrower.io.msg.Memory.*;
import static de.tbressler.waterrower.model.MonitorType.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for class InformationRequestMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestInformationRequestMessageInterpreter {

    /* Class under test. */
    private InformationRequestMessageInterpreter informationRequestMessageInterpreter;


    @Before
    public void setUp() {
        informationRequestMessageInterpreter = new InformationRequestMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsI() {
        assertEquals("I", informationRequestMessageInterpreter.getMessageIdentifier());
    }


    @Test
    public void getMessageType_returnsInformationRequestMessageClass() {
        assertEquals(InformationRequestMessage.class, informationRequestMessageInterpreter.getMessageType());
    }

    // Decode model information message:

    @Test
    public void decode_withIV40200_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IV40200");

        assertNotNull(msg);
        assertModelInformationMessage(msg, WATER_ROWER_S4, "02.00");
    }

    @Test
    public void decode_withIV50300_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IV50300");

        assertNotNull(msg);
        assertModelInformationMessage(msg, WATER_ROWER_S5, "03.00");
    }

    @Test
    public void decode_withIV10300_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IV10200");

        assertNotNull(msg);
        assertModelInformationMessage(msg, UNKNOWN_MONITOR_TYPE, "02.00");
    }

    @Test
    public void decode_withIV41234_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IV41234");

        assertNotNull(msg);
        assertModelInformationMessage(msg, WATER_ROWER_S4, "12.34");
    }

    // Decode single memory locations:

    @Test
    public void decode_withIDS00101_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IDS00101");

        assertNotNull(msg);
        assertDataMemoryMessage(msg, SINGLE_MEMORY, 1, -1, -1, 1);
    }

    @Test
    public void decode_withIDSFFF00_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IDSFFF00");

        assertNotNull(msg);
        assertDataMemoryMessage(msg, SINGLE_MEMORY, 4095, -1, -1, 0);
    }

    @Test
    public void decode_withIDS000FF_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IDS000FF");

        assertNotNull(msg);
        assertDataMemoryMessage(msg, SINGLE_MEMORY, 0, -1, -1, 255);
    }

    // Decode double memory locations:

    @Test
    public void decode_withIDD0010101_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IDD0010101");

        assertNotNull(msg);
        assertDataMemoryMessage(msg, DOUBLE_MEMORY, 1, -1, 1, 1);
    }

    @Test
    public void decode_withIDDFFF0101_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IDDFFF0000");

        assertNotNull(msg);
        assertDataMemoryMessage(msg, DOUBLE_MEMORY, 4095, -1, 0, 0);
    }

    @Test
    public void decode_withIDD000FFFF_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IDD000FFFF");

        assertNotNull(msg);
        assertDataMemoryMessage(msg, DOUBLE_MEMORY, 0, -1, 255, 255);
    }

    @Test
    public void decode_withIDD0000102_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IDD0000102");

        assertNotNull(msg);
        assertDataMemoryMessage(msg, DOUBLE_MEMORY, 0, -1, 1, 2);
    }

    // Decode triple memory locations:

    @Test
    public void decode_withIDT001010101_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IDT001010101");

        assertNotNull(msg);
        assertDataMemoryMessage(msg, TRIPLE_MEMORY, 1, 1, 1, 1);
    }

    @Test
    public void decode_withIDTFFF000000_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IDTFFF000000");

        assertNotNull(msg);
        assertDataMemoryMessage(msg, TRIPLE_MEMORY, 4095, 0, 0, 0);
    }

    @Test
    public void decode_withIDT000FFFFFF_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IDT000FFFFFF");

        assertNotNull(msg);
        assertDataMemoryMessage(msg, TRIPLE_MEMORY, 0, 255, 255, 255);
    }

    @Test
    public void decode_withIDT000010203_returnsValidMessage() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("IDT000010203");

        assertNotNull(msg);
        assertDataMemoryMessage(msg, TRIPLE_MEMORY, 0, 1, 2, 3);
    }

    // Decode invalid messages:

    @Test
    public void decode_withInvalidMessage_returnsNull() {
        InformationRequestMessage msg = informationRequestMessageInterpreter.decode("INVALID");
        assertNull(msg);
    }

    // Encode single memory location messages:

    @Test
    public void encode_withSingleMemoryLocation001_returnsIRS001() {
        ReadMemoryMessage msg = new ReadMemoryMessage(SINGLE_MEMORY, 0x001);

        String result = informationRequestMessageInterpreter.encode(msg);

        assertNotNull(result);
        assertEquals("IRS001", result);
    }

    @Test
    public void encode_withSingleMemoryLocationFFF_returnsIRSFFF() {
        ReadMemoryMessage msg = new ReadMemoryMessage(SINGLE_MEMORY, 0xFFF);

        String result = informationRequestMessageInterpreter.encode(msg);

        assertNotNull(result);
        assertEquals("IRSFFF", result);
    }

    // Encode double memory location messages:

    @Test
    public void encode_withDoubleMemoryLocation001_returnsIRD001() {
        ReadMemoryMessage msg = new ReadMemoryMessage(DOUBLE_MEMORY, 0x001);

        String result = informationRequestMessageInterpreter.encode(msg);

        assertNotNull(result);
        assertEquals("IRD001", result);
    }

    @Test
    public void encode_withDoubleMemoryLocationFFF_returnsIRDFFF() {
        ReadMemoryMessage msg = new ReadMemoryMessage(DOUBLE_MEMORY, 0xFFF);

        String result = informationRequestMessageInterpreter.encode(msg);

        assertNotNull(result);
        assertEquals("IRDFFF", result);
    }

    // Encode triple memory location messages:

    @Test
    public void encode_withTripleMemoryLocation001_returnsIRT001() {
        ReadMemoryMessage msg = new ReadMemoryMessage(TRIPLE_MEMORY, 0x001);

        String result = informationRequestMessageInterpreter.encode(msg);

        assertNotNull(result);
        assertEquals("IRT001", result);
    }

    @Test
    public void encode_withTripleMemoryLocationFFF_returnsIRTFFF() {
        ReadMemoryMessage msg = new ReadMemoryMessage(TRIPLE_MEMORY, 0xFFF);

        String result = informationRequestMessageInterpreter.encode(msg);

        assertNotNull(result);
        assertEquals("IRTFFF", result);
    }

    // Encode poll model information message:

    @Test
    public void encode_withRequestModelInformationMessage_returnsIV() {
        RequestModelInformationMessage msg = new RequestModelInformationMessage();

        String result = informationRequestMessageInterpreter.encode(msg);

        assertNotNull(result);
        assertEquals("IV?", result);
    }

    // Encode poll model information message:

    @Test
    public void encode_withInvalidMessage_returnsNull() {
        InformationRequestMessage msg = mock(InformationRequestMessage.class, "msg");

        String result = informationRequestMessageInterpreter.encode(msg);

        assertNull(result);
    }

    /* Assert the content of the DataMemoryMessage. */
    private void assertDataMemoryMessage(InformationRequestMessage msg, Memory expectedMemory, int expectedLocation, int expectedValue3, int expectedValue2, int expectedValue1) {
        assertEquals(DataMemoryMessage.class, msg.getClass());
        DataMemoryMessage dataMemoryMessage = (DataMemoryMessage) msg;
        assertEquals(expectedLocation, dataMemoryMessage.getLocation());
        assertEquals(expectedMemory, dataMemoryMessage.getMemory() );
        assertEquals(expectedValue3, dataMemoryMessage.getValue3());
        assertEquals(expectedValue2, dataMemoryMessage.getValue2());
        assertEquals(expectedValue1, dataMemoryMessage.getValue1());
    }

    /* Assert the content of the ModelInformationMessage. */
    private void assertModelInformationMessage(InformationRequestMessage msg, MonitorType expectedMonitorType, String expectedFirmwareVersion) {
        assertEquals(ModelInformationMessage.class, msg.getClass());
        ModelInformationMessage modelInformationMessage = (ModelInformationMessage) msg;
        assertNotNull(modelInformationMessage.getModelInformation());
        assertEquals(expectedMonitorType, modelInformationMessage.getModelInformation().getMonitorType());
        assertEquals(expectedFirmwareVersion, modelInformationMessage.getModelInformation().getFirmwareVersion());
    }

}
