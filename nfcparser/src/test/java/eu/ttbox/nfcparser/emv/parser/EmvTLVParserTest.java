package eu.ttbox.nfcparser.emv.parser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import eu.ttbox.nfcparser.emv.Emv41Enum;
import eu.ttbox.nfcparser.utils.NumUtil;

public class EmvTLVParserTest {


    // ===========================================================
    // 2PAY.SYS.DDF01 // ADF
    // ===========================================================


    @Test
    public void parseSelectPseDirectory2Pay() {
        byte[] recv = NumUtil.hex2Byte("6F 57 84 0E 32 50 41 59 2E 53 59 53 2E 44 44 46 30 31 A5 45 BF 0C 42 61 1B 4F 07 A0 00 00 00 42 10 10 87 01 01 50 02 43 42 9F 2A 08 03 00 00 00 00 00 00 00 61 23 4F 07 A0 00 00 00 03 10 10 87 01 02 50 0A 56 49 53 41 20 44 45 42 49 54 9F 2A 08 03 00 00 00 00 00 00 00");
        EmvTLVList parsedRecv = new EmvTLVList(recv);
        Assert.assertTrue(parsedRecv != null);

        print("2PAY.SYS.DDF01", parsedRecv);
        Assert.assertTrue(parsedRecv.containsKey(Emv41Enum.DF_FCI_NAME));
        Assert.assertTrue(parsedRecv.containsKey(Emv41Enum.DF_ADF_PRIORITY));
        Assert.assertTrue(parsedRecv.containsKey(Emv41Enum.DF_ADF_NAME)); // =AID = RID + PIX
        Assert.assertTrue(parsedRecv.containsKey(Emv41Enum.DF_ADF_LABEL));

        Assert.assertTrue(parsedRecv.containsKey(Emv41Enum.KERNEL_IDENTIFIER));
    }

    // ===========================================================
    // 1PAY.SYS.DDF01 //PSE  == >'6A82' (‘File not found’)
    // ===========================================================


    @Test
    public void parseSelectPseDirectory1Pay() {
        byte[] recv = NumUtil.hex2Byte("6F 1E 84 0E 31 50 41 59 2E 53 59 53 2E 44 44 46 30 31 A5 0C 88 01 01 5F 2D 02 66 72 9F 11 01 01");
        EmvTLVList parsedRecv = new EmvTLVList(recv);
        Assert.assertTrue(parsedRecv != null);
        // Assert.assertEquals(2, parsedRecv.size());
        print("1PAY.SYS.DDF01", parsedRecv);
        Assert.assertTrue(parsedRecv.containsKey(Emv41Enum.DF_FCI_NAME));

        Assert.assertTrue(parsedRecv.containsKey(Emv41Enum.DF_FCI_SFI));
        Assert.assertTrue(parsedRecv.containsKey(Emv41Enum.DF_FCI_LANG));

    }
    // ===========================================================
    //Other
    // ===========================================================


    //  @Test
    public void parseSelectApplication() {
        byte[] recv = NumUtil.hex2Byte("6F 53 84 07 A0 00 00 00 42 10 10 A5 48 50 02 43 42 87 01 01 9F 38 18 9F 66 04 9F 02 06 9F 03 06 9F 1A 02 95 05 5F 2A 02 9A 03 9C 01 9F 37 04 5F 2D 02 66 72 9F 11 01 01 9F 12 0E 54 52 41 4E 53 41 43 54 49 4F 4E 20 43 42 BF 0C 09 DF 61 01 03 9F 4D 02 11 32 90 00");
        EmvTLVList parsedRecv = new EmvTLVList(recv);
        Assert.assertTrue(parsedRecv != null);
        // Assert.assertEquals(2, parsedRecv.size());
        print("parseSelectApplication", parsedRecv);

    }



    // ===========================================================
    // read record SFI 2 record #1
    // ===========================================================

    @Test
    public void readRecord() {
        byte[] recv = NumUtil.hex2Byte("70 81 93 90 81 90 03 A7 8D 72 8C 0D 7F D7 60 2B C6 69 36 13 70 17 AA 48 9D D9 B6 57 79 3F 5B 84 F9 00 1A A0 CA 7C E7 9C 80 DB AC 4C F8 CD B0 3E D1 5B 50 1F 2E CC 4C A7 13 8B 30 99 04 6B C4 32 67 D1 62 19 46 0D B3 4A 45 15 34 A8 66 34 04 09 A3 89 3F 4E D1 6B 2D 2A 75 64 25 E5 CA 28 89 B3 EC 8A DA 8B 59 29 81 C7 8B A0 D7 A2 67 26 4C 12 C5 ED 82 41 F7 24 8E BF 6B C8 31 AA BD C8 79 6F 69 A8 88 A9 8F D1 7D 89 F8 F3 EC 2E EE A9 67 4D E4 D0 9D 9E 99 83");
        EmvTLVList parsedRecv = new EmvTLVList(recv);
     //   HashMap<RecvTag, byte[]> parsedRecv = TLVParser.parseTVL(recv, null);
        Assert.assertTrue(parsedRecv != null);
        // Assert.assertEquals(2, parsedRecv.size());
        print("Read Record sf2 record 1", parsedRecv);

    }

    // ===========================================================
    // Print map
    // ===========================================================


    private void print(String prefix,   EmvTLVList parsedRecv) {
        if (parsedRecv==null) {
            return;
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("--- " + prefix);
        System.out.println("---------------------------------------------------------------------");
        for (Map.Entry<Integer, TagTLV> entry : parsedRecv.getMapTagsEntrySet() ){
            TagTLV tag = entry.getValue();
            Emv41Enum emv = Emv41Enum.getByTag(tag);
            String emvLabel = emv==null ? "???" : emv.name() + "()";

            System.out.println( " " +prefix+
                    " ===> Key " + emvLabel +
                    "  " +tag.toString());

        }
        System.out.println("---------------------------------------------------------------------");
    }



}
