package eu.ttbox.nfcparser.emv;


import eu.ttbox.nfcparser.utils.AscciHelper;
import eu.ttbox.nfcparser.utils.NumUtil;

// https://github.com/jpos/jPOS/blob/master/jpos/src/main/java/org/jpos/tlv/TLVDataFormat.java
public enum Emv41TypeEnum {

//    CONSTRUCTED,
//    BINARY,
//    COMPRESSED_NUMERIC,
//    NUMERIC,
//    DATE_YYMMDD,
//    ALPHA,
//    ALPHA_NUMERIC,
//    ALPHA_NUMERIC_SPACE,
//    ALPHA_NUMERIC_SPECIAL,
//    CARD_NUMBER,
//    TIME_HHMMSS,
//    PROPRIETARY,

    UNNKOWN,
    TLV,
    YYMMDD(new Emv41TypeToString() {
        public String toString(byte[] value) {
            String dateString = NumUtil.byte2Hex(value);
            String[] split = dateString.split(" ");
            String dateFormat = split[2] + "/" + split[1] + "/20"+split[0];
            return dateFormat;
        }
    }),
    CN(new Emv41TypeToString() {
        public String toString(byte[] value) {
            return NumUtil.byte2HexNoSpace(value);
        }
    }),
    STRING(new Emv41TypeToString() {
        public String toString(byte[] value) {
            return AscciHelper.toAsciiByte2String(value);
        }
    });

    Emv41TypeEnum() {
        this(null);
    }

    Emv41TypeEnum(Emv41TypeToString typeToString) {
        this.typeToString = typeToString;
    }

    Emv41TypeToString typeToString;

    public String toString(byte[] value) {
        if (typeToString != null) {
            try {
                return typeToString.toString(value);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error in " + name() + " toString of : " + NumUtil.byte2Hex(value);
            }
        }
        return NumUtil.byte2Hex(value);
    }

}
