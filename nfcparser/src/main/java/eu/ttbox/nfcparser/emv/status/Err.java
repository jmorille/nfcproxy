package eu.ttbox.nfcparser.emv.status;


import eu.ttbox.nfcparser.utils.NumUtil;

public class Err {



    public String sw1;

    public byte sw1Byte;

    public String sw2;

    public Byte sw2Byte;

    public String type;

    public String desc;


    public Err(String sw1, String sw2, String type, String desc) {
        this.sw1 = sw1;
        this.sw2 = sw2;
        this.type = type;
        this.desc = desc;
        // Convert byte
        this.sw1Byte = NumUtil.hex2Byte(sw1)[0];
        this.sw2Byte = Errors.isSw2Data(sw2) ?  NumUtil.hex2Byte(sw2)[0] : null;

    }

    @Override
    public String toString() {
        return "Err{" +  sw1 + " " + sw2 +
                " = (" + type + ')' +
                " " + desc +  '}';
    }
}
