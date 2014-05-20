package eu.ttbox.nfcparser.emv.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import eu.ttbox.nfcparser.model.RecvTag;
import eu.ttbox.nfcparser.parser.TLVParser;
import eu.ttbox.nfcparser.utils.NumUtil;

/**
 * AFL
 */
public class ApplicationFileLocator {

    private byte[] recv;

    // Parse 1
    public byte[] featuresSupported;
    public byte[] afls;

    // Parse 2
    public ArrayList<AflRecord> records = new ArrayList<AflRecord>();


    public ApplicationFileLocator(byte[] recv) {
        this.recv = recv;
        init(recv);
    }


    private void init(byte[] recv) {
       // parse Recv
        System.out.println("recv : " + NumUtil.byte2Hex(recv));

        HashMap<RecvTag, byte[]> parsedRecv = TLVParser.parseTVL(recv);
        boolean isDone = parseResponseMessageTemplateFormat1(parsedRecv);
        isDone = parseResponseMessageTemplateFormat2(recv);
        // 71
        // 77
    }

    /**
     * Contains the data objects (with tags and lengths) returned by the ICC in response to a command
     * Book 3 : 6.5.5.4 Data Field Returned in the Response Message
     */
    private boolean parseResponseMessageTemplateFormat2 (byte[] recv) {
// TODO org.jpos.emv.EMVStandardTagType
        if (recv[0]!= ((byte)0X77)) {
            return false;
        }
        byte[] fixedSizeOf2 = Arrays.copyOfRange(recv, 1, 3);
        byte[] data = Arrays.copyOfRange(recv, 3, recv.length);
        System.out.println("Fixed Size Of 2 : " + NumUtil.byte2Hex(fixedSizeOf2) + " / " + data.length);
        System.out.println("Fixed Size Of 2 : " + NumUtil.getIntWith2Bytes(fixedSizeOf2, 0) + " / " + data.length);

        System.out.println("Data  : " + NumUtil.byte2Hex(data));
        return true;
    }



    /**
     * Contains the data objects (without tags and lengths) returned by the ICC in response to a command
     */
    private boolean parseResponseMessageTemplateFormat1 (HashMap<RecvTag, byte[]> parsedRecv) {
        byte[] raw = TLVParser.getTlvValue(parsedRecv, "80");
        if (raw==null) {
            return false;
        }
        int rawSize = raw.length;
        this.featuresSupported = Arrays.copyOfRange(raw, 0, 2); // CID
        this.afls = Arrays.copyOfRange(raw, 2, rawSize); // ATC


        // System.out.println( "features Supported = " +  NumUtil.toHexString(getAFL.featuresSupported));
        // System.out.println( "AFL = " +  NumUtil.toHexString(getAFL.afls));
        int aflSize = this.afls.length;
        // Check group 4 bytes
        if (aflSize % 4 != 0) {
            throw new RuntimeException("AFL not in group of 4 : " + NumUtil.byte2Hex(this.afls));
        }
        // Split in group
        ArrayList<byte[]> aflGroups = new ArrayList<byte[]>();
        for (int i = 0; i < aflSize; i = i + 4) {
            byte[] group = Arrays.copyOfRange(this.afls, i, i + 4);
            aflGroups.add(group);
        }
        // Print Group
        for (byte[] group : aflGroups) {
            System.out.println("AFL Group = " + NumUtil.byte2Hex(group));
            byte sfi = group[0];
            byte recordBegin = group[1];
            byte recordEnd = group[2];
            byte numberRecordForAuthentification = group[3];

            AflRecord record = new AflRecord();
            record.sfi = (sfi >> 3); //The five most significant bits indicate the SFI
            record.recordNumberBegin = recordBegin;
            record.recordNumberEnd = recordEnd;
            record.recordNumberForAuthentification = numberRecordForAuthentification;
            this.records.add(record);

        }
        return true;
    }


    public class AflRecord {

        public int sfi;

        public int recordNumberBegin;

        public int recordNumberEnd;

        public int recordNumberForAuthentification;


        public boolean isAuthentifcation() {
            return recordNumberForAuthentification > 0;
        }

        @Override
        public String toString() {
            return "AflRecord{" +
                    "sfi=" + sfi +
                    ", record[ " + recordNumberBegin +
                    "-->  " + recordNumberEnd + "]" +
                    ", record authentification=" + recordNumberForAuthentification +
                    '}';
        }


    }


}
