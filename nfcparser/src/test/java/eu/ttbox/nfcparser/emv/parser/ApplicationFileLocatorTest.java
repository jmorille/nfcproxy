package eu.ttbox.nfcparser.emv.parser;

import org.junit.Assert;
import org.junit.Test;

import eu.ttbox.nfcparser.model.RecvTag;
import eu.ttbox.nfcparser.parser.TLVParser;
import eu.ttbox.nfcparser.utils.NumUtil;

import static org.junit.Assert.*;

import eu.ttbox.nfcparser.emv.parser.ApplicationFileLocator.AflRecord;
public class ApplicationFileLocatorTest {

    @Test
    public void testParsing() {
        byte[] recv = NumUtil.hex2Byte("77 81 BE 82 02 20 00 94 0C 18 01 01 01 10 03 03 00 10 01 02 00 9F 6C 02 10 00 9F 5D 06 00 00 00 02 79 25 9F 4B 81 80 51 59 10 53 7D DD EB BA 84 EA 57 AB 5A 97 F6 8F D3 1F 27 EF 1B D8 BC 21 54 A1 91 C2 FB 3A 79 07 7F BF 2F 6C 90 45 CF 43 76 03 1B 97 69 08 05 22 42 7C 55 D9 15 D2 8A 07 AE EA A7 5C 7E 70 86 6B F1 DE 53 7D E9 D5 A6 0C 8D BD 93 AF E8 D0 E9 3C A3 BA A5 05 92 A4 98 23 E3 DE B6 E4 71 57 E2 7D 5B 01 A4 2F 54 31 B3 76 19 6B E4 3F D6 C1 35 17 27 03 E0 CE 17 56 BE 14 1C B2 C2 E4 24 F6 45 B8 9F 26 08 6C 70 68 2D 65 51 9D DB 9F 36 02 01 C3 9F 10 07 06 05 0A 03 90 00 00");
        System.out.println(NumUtil.byte2HexNoSpace(recv));
        ApplicationFileLocator afl = new ApplicationFileLocator(recv);
// http://www.emvlab.org/tlvutils/?data=7781BE82022000940C1801010110030300100102009F6C0210009F5D060000000279259F4B8180515910537DDDEBBA84EA57AB5A97F68FD31F27EF1BD8BC2154A191C2FB3A79077FBF2F6C9045CF4376031B9769080522427C55D915D28A07AEEAA75C7E70866BF1DE537DE9D5A60C8DBD93AFE8D0E93CA3BAA50592A49823E3DEB6E47157E27D5B01A42F5431B376196BE43FD6C135172703E0CE1756BE141CB2C2E424F645B89F26086C70682D65519DDB9F360201C39F100706050A03900000%0D%0A

        RecvTag tag =  TLVParser.getFirstTVLKey(recv, 0);
        System.out.println(tag + " / " + recv.length  +" ==> " );



    }

//    @Test
    public void parseSelectPseDirectory() {
        byte[] recv = NumUtil.hex2Byte("80 0E 7C 00 08 01 01 00 10 01 05 00 18 01 02 01 90 00");
        ApplicationFileLocator afl =  new ApplicationFileLocator(recv);

        Assert.assertEquals(3, afl.records.size());
        AflRecord rc1 = afl.records.get(0);
        AflRecord rc2 = afl.records.get(1);
        AflRecord rc3 = afl.records.get(2);
        // Fsi
        Assert.assertEquals(1, rc1.sfi);
        Assert.assertEquals(2, rc2.sfi);
        Assert.assertEquals(3, rc3.sfi);
        // Record 1
        Assert.assertEquals(1, rc1.recordNumberBegin);
        Assert.assertEquals(1, rc1.recordNumberEnd);
        // Record 2
        Assert.assertEquals(1, rc2.recordNumberBegin);
        Assert.assertEquals(5, rc2.recordNumberEnd);
        // Record 3
        Assert.assertEquals(1, rc3.recordNumberBegin);
        Assert.assertEquals(2, rc3.recordNumberEnd);

    }
}