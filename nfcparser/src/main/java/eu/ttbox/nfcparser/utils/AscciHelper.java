package eu.ttbox.nfcparser.utils;


import java.io.UnsupportedEncodingException;

public class AscciHelper {
    //


    public static final char[] STANDARD_ENCODE_TABLE_A = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static final char[] DECODE_TABLE_A = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    public static final char[] DECODE_TABLE_AN = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    // ans ==> Common Character Set table in Annex B of Book 4.

    // ===========================================================
    // Format A & AN
    // ===========================================================


    public static String decodeFormatAN(byte[] digits) {
        int size = digits == null ? 0 : digits.length;
        char[] result = new char[size];
        for (int i = 0; i < size; i++) {
            result[i] = decodeFormatAN(digits[i]);
        }
        return new String(result);
    }


    public static byte[] encodeFormatAN(String text) {
        int size = text == null ? 0 : text.length();
        byte[] result = new byte[size];
        for (int i = 0; i < size; i++) {
            result[i] = encodeFormatAN(text.charAt(i));
        }
        return result;
    }

    public static char decodeFormatAN(byte digit) {
        if (digit < 26) {
            return (char) ('a' + digit);
            //return Character.toChars('a' + digit)[0];
        } else if (digit < 52) {
            return (char) ('A' + (digit - 26));
        } else if (digit < 62) {
            return (char) ('0' + (digit - 52));
        }
        return '\0';
    }


    public static byte encodeFormatAN(char digit) {
        if (digit >= 'a') {
            return (byte) (digit - 'a');
        } else if (digit >= 'A') {
            return (byte) ((digit - 'A') + 26);
        }
        return (byte) (digit - '0' + 52);

    }

    // ===========================================================
    // Other
    // ===========================================================

 //   private static final String ASCCII_ENCODING =  "US-ASCII" ; // 7-bit data
     private static final String ASCCII_ENCODING = "ISO-8859-1" ; // 8-bit data

    public static String toAsciiByte2String(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            return  new String(bytes, ASCCII_ENCODING);
        } catch (UnsupportedEncodingException e) {
            return "Error in reading : " + NumUtil.byte2Hex(bytes);
        }
//        StringBuilder sb = new StringBuilder();
//        for (byte b : bytes) {
//            char[] result = Character.toChars(b);
//            sb.append(result);
//
//        }
//        return sb.toString();
    }

    public static byte[] toAsciiString2Bytes(String ascci) {
        byte[] bytes = new byte[0];
        try {
            bytes = ascci.getBytes(ASCCII_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return bytes;

    }

    // ===========================================================
    // Other
    // ===========================================================


}
