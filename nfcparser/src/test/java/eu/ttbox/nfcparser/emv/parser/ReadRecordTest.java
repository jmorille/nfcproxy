package eu.ttbox.nfcparser.emv.parser;

import org.jpos.iso.ISOException;
import org.jpos.tlv.TLVList;
import org.jpos.tlv.TLVMsg;
import org.junit.Assert;
import org.junit.Test;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import eu.ttbox.nfcparser.emv.Emv41Enum;
import eu.ttbox.nfcparser.model.RecvTag;
import eu.ttbox.nfcparser.utils.NumUtil;

public class ReadRecordTest {

    @Test
    public void testReadRecord() throws ISOException {
        byte[] recv = NumUtil.hex2Byte("70 81 93 90 81 90 03 A7 8D 72 8C 0D 7F D7 60 2B C6 69 36 13 70 17 AA 48 9D D9 B6 57 79 3F 5B 84 F9 00 1A A0 CA 7C E7 9C 80 DB AC 4C F8 CD B0 3E D1 5B 50 1F 2E CC 4C A7 13 8B 30 99 04 6B C4 32 67 D1 62 19 46 0D B3 4A 45 15 34 A8 66 34 04 09 A3 89 3F 4E D1 6B 2D 2A 75 64 25 E5 CA 28 89 B3 EC 8A DA 8B 59 29 81 C7 8B A0 D7 A2 67 26 4C 12 C5 ED 82 41 F7 24 8E BF 6B C8 31 AA BD C8 79 6F 69 A8 88 A9 8F D1 7D 89 F8 F3 EC 2E EE A9 67 4D E4 D0 9D 9E 99 83 90 00");
        TLVList tlvList = new TLVList();
        tlvList.unpack(recv);

        TLVMsg tmp =  tlvList.find(0x70);
        TLVList tlvListSub = new TLVList();
        tlvListSub.unpack( tmp.getValue());



        System.out.println();
        HashMap<RecvTag, byte[]> parsedRecv = EmvTLVParser.parsePayCardTVLInDept(recv);
        Assert.assertTrue(parsedRecv != null);

        print("Read record", tlvList);
        print("Read sub record", tlvListSub);
        print("Read record", parsedRecv);
    }


    // ===========================================================
    // Print map
    // ===========================================================
    private void print(String prefix,    TLVList tlvList ) {
        System.out.println("---------------------------------------------------------------------");
        System.out.println("--- " + prefix);
        System.out.println("---------------------------------------------------------------------");
        for(TLVMsg tlv  : tlvList.getTags()) {
            System.out.println(tlv.toString());
        }
        System.out.println("---------------------------------------------------------------------");
    }

    private void print(String prefix,  HashMap<RecvTag, byte[]> parsedRecv) {
        if (parsedRecv==null) {
            return;
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("--- " + prefix);
        System.out.println("---------------------------------------------------------------------");
        for (Map.Entry<RecvTag, byte[]> entry : parsedRecv.entrySet()){
            RecvTag tag = entry.getKey();
            Emv41Enum emv = Emv41Enum.getByTag(tag);
            String emvLabel = emv==null ? "???" : emv.name() + "()";

            System.out.println( " " +prefix+
                    " ===> Key " + emvLabel +
                    "  " + NumUtil.byte2Hex(tag.key) + " = " +  NumUtil.byte2Hex(entry.getValue()));

        }
        System.out.println("---------------------------------------------------------------------");
    }

}
