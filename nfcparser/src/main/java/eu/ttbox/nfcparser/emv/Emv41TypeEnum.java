package eu.ttbox.nfcparser.emv;


import eu.ttbox.nfcparser.utils.AscciHelper;
import eu.ttbox.nfcparser.utils.NumUtil;

public enum Emv41TypeEnum {

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
