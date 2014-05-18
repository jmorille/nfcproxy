package eu.ttbox.nfcparser.utils;

import org.junit.Assert;
import org.junit.Test;

public class AscciHelperTest {


    // ===========================================================
    // Format A & AN
    // ===========================================================

    @Test
    public void testFormatAN() {
        testEncodeDecodeToByte(AscciHelper.DECODE_TABLE_AN);
    }

    @Test
    public void testFormatA() {
        testEncodeDecodeToByte(AscciHelper.DECODE_TABLE_A);
    }

    public void testEncodeDecodeToByte(char[] alphabet) {
        for (byte i = 0; i < alphabet.length; i++) {
            char c = alphabet[i];
            byte encodeChar = AscciHelper.encodeFormatAN(c);
            char decodeByte = AscciHelper.decodeFormatAN(encodeChar);

            System.out.println("" + i + " : " + c + " == encode ==> 0x" + NumUtil.byte2Hex(new byte[]{encodeChar}) + " == decode ==> " + decodeByte);
            //
            Assert.assertEquals(i, encodeChar);
            Assert.assertEquals(c, decodeByte);
        }
    }


    // ===========================================================
    // Other
    // ===========================================================


    @Test
    public void testAscci2Hex() {
        String fileName = "1PAY.SYS.DDF01";
        byte[] text = AscciHelper.toAsciiString2Bytes(fileName);
        String texthex = NumUtil.byte2Hex(text);
        Assert.assertEquals("315041592E5359532E4444463031", texthex);
        System.out.println("" + fileName + " ==> " + texthex);
    }

    @Test
    public void testHex2Ascci() {
        String hexString = "315041592E5359532E4444463031";
        byte[] data = NumUtil.hex2ByteNoSpace(hexString);
        String text = AscciHelper.toAsciiByte2String(data);

        Assert.assertEquals("1PAY.SYS.DDF01", text);
        System.out.println("" + hexString + " ==> " + text);
    }


    // ===========================================================
    // Test
    // ===========================================================

    public byte[] encodeFormatBinary(String name){

        byte[] result = null;

        return result;

    }


   // @Test
    public void testEncodeDFName() {
        String fileName = "1PAY.SYS.DDF01";
        // Encode
        byte[] bytes = encodeFormatBinary(fileName);
        String bytesHex = NumUtil.byte2HexNoSpace(bytes);
        System.out.println("" + fileName + " ==> " + bytesHex);
        // Assert
        Assert.assertEquals("315041592E5359532E4444463031", bytesHex);
    }

    //    @Test
    public void testDecodeDFName() {
        String hexString = "315041592E5359532E4444463031";
        byte[] data = NumUtil.hex2ByteNoSpace(hexString);
        String text = AscciHelper.decodeFormatAN(data);

        Assert.assertEquals("1PAY.SYS.DDF01", text);
        System.out.println("" + hexString + " ==> " + text);
    }



    // ===========================================================
    // Other
    // ===========================================================


    @Test
    public void testNavigoAscci2Hex() {
        String fileName = "1PAY.SYS.DDF01";
        byte[] text = AscciHelper.toAsciiString2Bytes(fileName);
        String texthex = NumUtil.byte2HexNoSpace(text);
        Assert.assertEquals("315041592E5359532E4444463031", texthex);
        System.out.println("" + fileName + " ==> " + texthex);
    }

    @Test
    public void testNavigoHex2Ascci() {
        String hexString = "2000";
        byte[] data = NumUtil.hex2ByteNoSpace(hexString);
        String text = AscciHelper.toAsciiByte2String(data);

        Assert.assertEquals("1PAY.SYS.DDF01", text);
        System.out.println("" + hexString + " ==> " + text);
    }

}
