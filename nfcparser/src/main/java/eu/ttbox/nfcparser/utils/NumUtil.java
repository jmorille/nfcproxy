/*
 * Copyright (c) 2005 Chang Sau Sheong, Thomas Tarpin-Lyonnet.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.ttbox.nfcparser.utils;

import java.nio.ByteBuffer;

/**
 * A utility class mainly used for display of bytes to string and vice versa
 *
 */
public class NumUtil {



    // table to convert a nibble to a hex char.
    private static String hexits = "0123456789abcdef";
    private static final char[] hexChar = "0123456789ABCDEF".toCharArray();

    // ===========================================================
    // HexString conversion to Bytes
    // ===========================================================

    /**
     * Convert hexString in byte Arrays
     *
     * @param "00 04 00 00 0E"
     * @return new byte[]{ .... }
     */
    public static byte[] hex2Byte(String hexstr) {
        String[] hexbytes = hexstr.split("\\s");
        byte[] bytes = new byte[hexbytes.length];
        for (int i = 0; i < hexbytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hexbytes[i], 16);
        }
        return bytes;
    }


    /**
     * Convert a String (hexadecimal representation of a bytes) into a byte array.
     * Example: "112233AA", the returned byte[] will be: {0x11,0x22,0x33,0xAA}
     * @param s the String to convert
     * @return The String representation of the bytes
     */
    public static byte[] hex2ByteNoSpace(String s){
        s = s.toLowerCase();

        if((s.length() % 2) != 0)
            s = "0" + s;

        byte[] b = new byte[s.length() / 2];
        int j = 0;
        int h;
        int nybble = -1;
        for (int i = 0; i < s.length(); ++i) {
            h = hexits.indexOf(s.charAt(i));
            if (h >= 0) {
                if (nybble < 0) {
                    nybble = h;
                } else {
                    b[j++] = (byte) ((nybble << 4) + h);
                    nybble = -1;
                }
            }
        }
        if (nybble >= 0) {
            b[j++] = (byte) (nybble << 4);
        }
        if (j < b.length) {
            byte[] b2 = new byte[j];
            System.arraycopy(b, 0, b2, 0, j);
            b = b2;
        }
        return b;
    }

    // ===========================================================
    // Byte conversion to HexString
    // ===========================================================

    /**
     * Convert a byte array into its hexadecimal string representation.
     *
     * @param b = {0x00,0x11,0x22,0xAA}
     * @return The returned String will be "001122AA"
     */
    public static String byte2HexNoSpace(byte[] b) {
        return byte2Hex(b, "");
    }

    /**
     * Convert a byte array into its hexadecimal string representation.
     *
     * @param b = {0x00,0x11,0x22,0xAA}
     * @return The returned String will be "00 11 22 AA"
     */
    public static String byte2Hex(byte[] b) {
        return byte2Hex(b, " ");
    }

    public static String byte2Hex(byte[] b, String separator) {
        if (b == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer(b.length * 3);
        for (int i = 0; i < b.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            byte2Hex(b[i], sb);
        }
        return sb.toString().toUpperCase().trim();
    }

    /**
     * Convert a byte array into its hexadecimal string representation.
     * No space
     *
     * @param b = 0xAA
     * @return the returned String will be "AA"
     */
    public static String byte2Hex(byte b) {
        return byte2Hex(b, new StringBuffer(2)).toString();
    }

    public static StringBuffer byte2Hex(byte b, StringBuffer dest) {
        // look up high nibble char
        dest.append(hexChar[(b & 0xf0) >>> 4]);
        // look up low nibble char
        dest.append(hexChar[b & 0x0f]);
        // V2 result.append(String.format("%02X", inputbyte));
        return dest;
    }


    // ===========================================================
    // Other
    // ===========================================================

    /**
     * Get the unsigned value of a byte.
     * @param b
     * @return  An unsigned integer of the value
     */
    public static int getUnsignedValue(byte b){
        return (int)(b & 0x00FF);
    }

    /**
     * Converts a byte array of hex into an integer
     *
     * @param arr bytes
     * @return integer representation of bytes
     */
    public static int getIntWith2Bytes(byte[] arr, int off) {
        return arr[off]<<8 &0xFF00 | arr[off+1]&0xFF;
    }


}

