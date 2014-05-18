package eu.ttbox.nfcparser.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import eu.ttbox.nfcparser.model.RecvTag;
import eu.ttbox.nfcparser.utils.NumUtil;

//http://stackoverflow.com/questions/11473974/is-there-a-java-parser-for-ber-tlv
// http://www.emvlab.org/tlvutils/
public class TLVParser {

    public static byte[] getData(byte[] recv) {
        int recvSize = recv.length;
        if (recvSize >= 2) {
            return Arrays.copyOfRange(recv, 0, recvSize - 2);
        }
        return new byte[0];
    }



    public static HashMap<RecvTag, byte[]> parseTVL(byte[] tlv) {
        return parseTVL(tlv, null);
    }


    // http://stackoverflow.com/questions/11473974/is-there-a-java-parser-for-ber-tlv
    public static HashMap<RecvTag, byte[]> parseTVL(byte[] tlv, HashMap<RecvTag, byte[]> presult) {
        HashMap<RecvTag, byte[]> result = presult != null ? presult : new HashMap<RecvTag, byte[]>();

        int tlvSize = tlv != null ? tlv.length : 0;
        for (int i = 0; i < tlvSize; ) {
            byte[] key = new byte[]{tlv[i++]};
            if ((key[0] & 0x1F) == 0x1F) {
                key = new byte[]{key[0], tlv[i++]};
            }
            byte len = tlv[i++];
            int length = len;
            byte[] val = Arrays.copyOfRange(tlv, i, i = i + length);
            // System.out.println("parseTVL key " + NumUtil.hex2String(key) + "("+ length + ") ==> " + NumUtil.toHexString(val));
            RecvTag keyTag = new RecvTag(key, length);
            result.put(keyTag, val);
        }

        return result;
    }

    /**
     * http://books.google.fr/books?id=IhnUSceC0lcC&pg=PA173&lpg=PA173&dq=afl+pdol&source=bl&ots=B2gBB6tYNu&sig=hICWfr4KOajHDHhBs6GYYnsGL5c&hl=fr&sa=X&ei=cItnU_HJJMmP0AWF5oHIAw&ved=0CEoQ6AEwAg#v=onepage&q=afl%20pdol&f=false
     *
     * @param pdol
     * @return
     */
    public static ArrayList<RecvTag> parseDataObjectList(byte[] pdol) {
        ArrayList<RecvTag> result = new ArrayList<RecvTag>();
        int tlvSize = pdol != null ? pdol.length : 0;
        for (int i = 0; i < tlvSize; ) {
            byte[] key = new byte[]{pdol[i++]};
            if ((key[0] & 0x1F) == 0x1F) {
                key = new byte[]{key[0], pdol[i++]};
            }
            byte len = pdol[i++];
            int length = len;
            RecvTag keyTag = new RecvTag(key, length);
            result.add(keyTag);
        }
        return result;
    }


    public static byte[] getTlvValue(HashMap<RecvTag, byte[]> parsed, String key) {
        return getTlvValue(parsed, NumUtil.hex2ByteNoSpace(key));
    }

    public static byte[] getTlvValue(HashMap<RecvTag, byte[]> parsed, byte[] key) {
        RecvTag keyMap = new RecvTag(key);
        return getTlvValue(parsed, keyMap);
    }


    public static byte[] getTlvValue(HashMap<RecvTag, byte[]> parsed, RecvTag keyMap) {
        return parsed.get(keyMap);
    }

}
