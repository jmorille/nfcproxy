package eu.ttbox.nfcparser.emv.status;


import eu.ttbox.nfcparser.utils.NumUtil;

public class Emv41SWLabelItem {


    public String sw1String;
    public byte sw1;

    public String sw2String;
    public Byte sw2;

    public String type;
    public String desc;


    public Emv41SWLabelItem(String sw1, String sw2, String type, String desc) {
        this.sw1String = sw1;
        this.sw2String = sw2;
        this.type = type;
        this.desc = desc;
        // Convert byte
        this.sw1 = NumUtil.hex2Byte(sw1)[0];
        this.sw2 = Emv41SWLabel.isSw2Data(sw2) ?  NumUtil.hex2Byte(sw2)[0] : null;

    }

    @Override
    public String toString() {
        return "Emv41SWLabelItem{" + sw1String + " " + sw2String +
                " = (" + type + ')' +
                " " + desc +  '}';
    }
}
