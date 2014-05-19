package eu.ttbox.nfcparser.utils;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class NumUtilTest {

    @Test
    public void hex2Byte() {
        assertEquals((byte) 0x6F, NumUtil.hex2Byte("6F")[0]);
        assertTrue(Arrays.equals(new byte[]{0x6F}, NumUtil.hex2Byte("6F")));
        assertTrue(Arrays.equals(new byte[]{0x5F, 0x54}, NumUtil.hex2Byte("5F 54")));
    }

    @Test
    public void hex2ByteNoSpace() {
        assertEquals((byte) 0x6F, NumUtil.hex2ByteNoSpace("6F")[0]);
        assertTrue(Arrays.equals(new byte[]{0x6F}, NumUtil.hex2ByteNoSpace("6F")));
        assertTrue(Arrays.equals(new byte[]{0x5F, 0x54}, NumUtil.hex2ByteNoSpace("5F54")));
    }

    @Test
    public void hex2ByteLong() throws Exception {
        byte[] recv = NumUtil.hex2Byte("6F 53 84 07 A0 00 00 00 42 10 10 A5 48 50 02 43 42 87 01 01 9F 38 18 9F 66 04 9F 02 06 9F 03 06 9F 1A 02 95 05 5F 2A 02 9A 03 9C 01 9F 37 04 5F 2D 02 66 72 9F 11 01 01 9F 12 0E 54 52 41 4E 53 41 43 54 49 4F 4E 20 43 42 BF 0C 09 DF 61 01 03 9F 4D 02 11 32 90 00");
        assertNotNull(recv);
        assertEquals(87, recv.length);
    }


    @Test
    public void testHex2Byte() throws Exception {
        String toTest = "6F 53 84 07 A0 00 00 00 42 10 10 A5 48 50 02 43 42 87 01 01 9F 38 18 9F 66 04 9F 02 06 9F 03 06 9F 1A 02 95 05 5F 2A 02 9A 03 9C 01 9F 37 04 5F 2D 02 66 72 9F 11 01 01 9F 12 0E 54 52 41 4E 53 41 43 54 49 4F 4E 20 43 42 BF 0C 09 DF 61 01 03 9F 4D 02 11 32 90 00";
        byte[] recv = NumUtil.hex2Byte(toTest);
        String hexString = NumUtil.byte2Hex(recv);

        System.out.println(" ==> " + hexString);
        assertEquals(toTest, hexString);
    }


}