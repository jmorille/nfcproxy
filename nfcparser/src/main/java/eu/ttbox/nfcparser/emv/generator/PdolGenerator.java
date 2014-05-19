package eu.ttbox.nfcparser.emv.generator;


import java.util.ArrayList;

import eu.ttbox.nfcparser.model.RecvTag;
import eu.ttbox.nfcparser.parser.TLVParser;
import eu.ttbox.nfcparser.utils.NumUtil;

public class PdolGenerator {

    public byte[] pdol = null;

    public PdolGenerator(byte[] pdol) {
        this.pdol = pdol;
    }

    /**
     * 9F02 - 06 : amount authorized
     * 9F03 - 06 : amount other
     * 9F1A - 02 : terminal country code
     * 95 - 05	 : terminal verification results
     * 5F2A - 02 : transactin country code
     * 9A - 03	 : transaction date
     * 9C - 03	 : transaction Type
     * 9F37 - 04 : unpredictable number
     * 9F66 - 04 : Terminal Transaction Qualifiers
     * 9F7A - 01 : VLP Terminal Support Indicator
     * @return
     */
    public byte[] generatePdolRequestData() {
        byte[] result = null;
        if (this.pdol != null) {
            ArrayList<RecvTag> parsedPdol = TLVParser.parseDataObjectList(this.pdol);
            int pdolSize = 0;
            for (RecvTag recvTag : parsedPdol) {
                pdolSize += recvTag.valueSize;
            }
            // Create result
            byte[] dest = new byte[pdolSize];
            int index = 0;
            for (RecvTag tag : parsedPdol) {
                System.out.println("generatePdolRequestData ===>  Request " + tag);
                // Copy Key
                String key = tag.getKeyHex2String();
                // http://www.eftlab.co.uk/index.php/site-map/knowledge-base/145-emv-nfc-tags
                //  PDOL (4 + 6 + 6 + 2 + 5 + 2 + 3 + 1 + 4)
                // 80 A8 00 00 23 83 ==> 21
                //
                if ("9F66".equals(key)) {
                    // 9F66	 Terminal transaction Qualifiers	 4 octets
                    // binary 32
                     // 32 00 00 00
                    writeRecvTag(dest, index, tag,"32 00 00 00");
                } else if ("9F02".equals(key)) {
                    // 9F02	 Amount, Authorized (Numeric)	 6 octets
                    // n 12 ==> 00 00 00 01 00 00
                    // 00 00 00 00 00 00
//                    writeRecvTag(dest, index, tag,"00 00 00 00 01 00"); // 1 euros
                    writeRecvTag(dest, index, tag,"00 00 00 00 00 01"); // 1 centime
 //                   writeRecvTag(dest, index, tag,"00 00 00 00 00 00");
                } else if ("9F03".equals(key)) {
                    //9F03	 Amount, Other (Numeric)	 6 octets
                    // n 12 ==> Always '00 00 00 00 00 00'
                    // 00 00 00 00 00 00
                 } else if ("9F1A".equals(key)) {
                    // 9F1A	 Terminal Country Code	 2 octets
                    // n 3 ==> Indicates the country of the terminal, represented according to ISO 3166-1
                    // http://fr.wikipedia.org/wiki/ISO_3166-1
                    // 02 50
                    writeRecvTag(dest, index, tag,"02 50");
                 } else if ("9505".equals(key)) {
                    // 9505	 Terminal Verification Results	 5 octets
                    // 00 00 00 00 00
                 } else if ("5F2A".equals(key)) {
                    // 5F2A	 Transaction Currency Code	 2 octets
                    // n 3 ==> Indicates the currency code of the transaction according to ISO 4217 ==> EUR :  0978
                    // 09 78
                    writeRecvTag(dest, index, tag,"09 78");
                 } else if ("9A".equals(key)) {
                    // 9A   Transaction Date	 3 octets
                    // n 6 (YYMMDD) ==> Local date that the transaction was authorised
                    // 12 12 31
 // TODO                   Calendar now = Calendar.getInstance();
                     writeRecvTag(dest, index, tag, "14 06 28");
                 } else if ("9C".equals(key)) {
                    // 9C   Transaction Type	 1 octet
                    // n 2 ==> Always '00'
                    // indicates the type of financial transaction, represented by the first two digits of the ISO 8583:1993 Processing Code. The actual values to be used for the Transaction Type data element are defined by the relevant payment system.
                    // 00
                    writeRecvTag(dest, index, tag, "00");
                } else if ("9F37".equals(key)) {
                    // 9F37	 Unpredictable Number	 4 octets
                    // binary ==> Value to provide variability and uniqueness to the generation of a cryptogram
                    // E4 EC 9E 52 00
                    writeRecvTag(dest, index, tag, "E4 EC 9E 52");
                } else {
                    new RuntimeException("Not manage Tag : " + tag);
                }
                // Copy Values
                // Rest  00
                index += tag.valueSize;
                System.out.println("generatePdolRequestData ===>  Request " + NumUtil.byte2Hex(dest) + " : Size of " + dest.length + " ==> hex 0x" + NumUtil.byte2Hex(new byte[]{(byte) dest.length}));

            }
            //Check Total Size
            if (index!= dest.length) {
                throw new RuntimeException("Bad PDOL value Size");
            }
            result = dest;
        }
        return result;
    }

    private void writeRecvTag( byte[] dest , int index, RecvTag tag, String value) {
        byte[] src = NumUtil.hex2Byte(value);
        writeRecvTag(dest, index, tag, src);
    }


    private void writeRecvTag( byte[] dest , int index, RecvTag tag, byte[] value) {
        // Check Validity
        int maxSize = tag.valueSize;
        int copySize = value.length;
        if (copySize>maxSize) {
            throw new IllegalArgumentException("Error Copy Tag " + tag + " for the value " +  NumUtil.byte2Hex(value) );
        }
        // Do Copy
        System.arraycopy(value, 0, dest, index, copySize);
    }

}



