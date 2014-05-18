package eu.ttbox.nfcparser.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import eu.ttbox.nfcparser.model.RecvTag;
import eu.ttbox.nfcparser.utils.AscciHelper;
import eu.ttbox.nfcparser.utils.NumUtil;
import eu.ttbox.nfcparser.utils.NumUtil;

import static org.junit.Assert.assertEquals;

public class TLVParserTest {

    @Test
    public void parseSelectPseDirectory() {
        byte[] recv = NumUtil.hex2Byte("6F 1E 84 0E 31 50 41 59 2E 53 59 53 2E 44 44 46 30 31 A5 0C 88 01 01 5F 2D 02 66 72 9F 11 01 01 90 00");
        HashMap<RecvTag, byte[]> parsedRecv = TLVParser.parseTVL(recv);
        Assert.assertTrue(parsedRecv != null);
        Assert.assertEquals(2, parsedRecv.size());
        print("parseSelectPseDirectory", parsedRecv);

        byte[] fciTemplate = TLVParser.getTlvValue(parsedRecv, "6F");
        Assert.assertTrue(fciTemplate != null);
    }

    private void print(String prefix, HashMap<RecvTag, byte[]> parsedRecv) {
        if (parsedRecv == null) {
            return;
        }
        for (Map.Entry<RecvTag, byte[]> entry : parsedRecv.entrySet()) {
            RecvTag tag = entry.getKey();
            System.out.println(" " + prefix +
                    " ===> Key " + NumUtil.byte2Hex(tag.key) + " = " + NumUtil.byte2Hex(entry.getValue()));

        }
    }

    @Test
    public void parseTLVTest() {
        byte[] recv = NumUtil.hex2Byte("6F 1A 84 0E 31 50 41 59 2E 53 59 53 2E 44 44 46 30 31 A5 08 88 01 01 5F 2D 02 65 6E");
        //   TLV tlv = new TLV(recv);

        //  System.out.print(   tlv.valueAsString()  );

        // Parse One
        HashMap<RecvTag, byte[]> parseOne = TLVParser.parseTVL(recv);
        byte[] valOne = TLVParser.getTlvValue(parseOne, "6F");
        Assert.assertTrue(valOne != null);
        Assert.assertEquals(26, valOne.length);

        // Parse Two
        HashMap<RecvTag, byte[]> parseTwo = TLVParser.parseTVL(valOne);
        byte[] valTow = TLVParser.getTlvValue(parseTwo, "84");
        System.out.println("===> Key " + "84" + " : " + NumUtil.byte2Hex(valTow));
        Assert.assertTrue(valTow != null);
        Assert.assertEquals(14, valTow.length);
    }

    @Test
    public void testParse2() {
        byte[] recv = NumUtil.hex2Byte("84 0E 31 50 41 59 2E 53 59 53 2E 44 44 46 30 31 A5 0C 88 01 01 5F 2D 02 66 72 9F 11 01 01");
        HashMap<RecvTag, byte[]> parseOne = TLVParser.parseTVL(recv);
        byte[] valOne = TLVParser.getTlvValue(parseOne, "A5");

        Assert.assertTrue(valOne != null);
        Assert.assertEquals(12, valOne.length);

        System.out.println("Key " + "A5" + " = " + NumUtil.byte2Hex(valOne));
    }

    @Test
    public void testKeyDouble() {
        byte[] recv = NumUtil.hex2Byte("88 01 01 5F 2D 02 65 6E");
        HashMap<RecvTag, byte[]> parseOne = TLVParser.parseTVL(recv);

        byte[] valOne = TLVParser.getTlvValue(parseOne, "88");
        Assert.assertTrue(valOne != null);
        Assert.assertEquals(1, valOne.length);

        byte[] valTow = TLVParser.getTlvValue(parseOne, "5F2D");
        Assert.assertTrue(valTow != null);
        Assert.assertEquals(2, valTow.length);
        String text = AscciHelper.toAsciiByte2String(valTow);
        Assert.assertEquals("en", text);


        System.out.println(NumUtil.byte2Hex(valTow) + " = " + text);

        for (RecvTag key : parseOne.keySet()) {
            System.out.println("testKeyDouble ===> Key " + NumUtil.byte2Hex(key.key) + " : " + NumUtil.byte2Hex(parseOne.get(key)));
        }
    }

    // @Test
    public void testParseReadRecord() {
        byte[] recv = NumUtil.hex2Byte("70 23 61 21 4F 07 A0 00 00 00 42 10 10 50 02 43 42 9F 12 0E 54 52 41 4E 53 41 43 54 49 4F 4E 20 43 42 87 01 01 90 00");
        byte[] recvRead = Arrays.copyOfRange(recv, 2, recv[1]);
        System.out.println(" ==> " + NumUtil.byte2Hex(recvRead));
        byte[] recvReadApp = Arrays.copyOfRange(recvRead, 2, recvRead.length);
        System.out.println(" ==> " + NumUtil.byte2Hex(recvReadApp));

        HashMap<RecvTag, byte[]> parseOne = TLVParser.parseTVL(recvReadApp);
    }


    @Test
    public void testParseDataObjectList() {
        byte[] pdol = NumUtil.hex2Byte("9F 66 04 9F 02 06 9F 03 06 9F 1A 02 95 05 5F 2A 02 9A 03 9C 01 9F 37 04");
        ArrayList<RecvTag> parsePdol = TLVParser.parseDataObjectList(pdol);
        Assert.assertTrue(parsePdol != null);
        Assert.assertEquals(9, parsePdol.size());

        int pdolSize = 0;
        for (RecvTag tag : parsePdol) {
            System.out.println("testParseDataObjectList ===>  Key " + NumUtil.byte2Hex(tag.key) + " : Size of " + tag.valueSize);
            pdolSize += tag.valueSize;
        }
        System.out.println("testParseDataObjectList ===>  Total Size " + pdolSize + " ==> hex 0x" + NumUtil.byte2Hex(new byte[]{(byte) pdolSize}));
        System.out.println("testParseDataObjectList ===>  Byte number " + pdol.length + " ==> hex 0x" + NumUtil.byte2Hex(new byte[]{(byte) pdol.length}));


    }

    @Test
    public void testSelectApplicationPDOL() {
        byte[] pdol = NumUtil.hex2Byte("9F 66 04 9F 02 06 9F 03 06 9F 1A 02 95 05 5F 2A 02 9A 03 9C 01 9F 37 04");
        ArrayList<RecvTag> parsedPdol = TLVParser.parseDataObjectList(pdol);
        assertEquals(9, parsedPdol.size());
    }


}