package eu.ttbox.nfcproxy.service.nfc.reader.emv41;


import eu.ttbox.nfcparser.emv.Emv41Enum;
import eu.ttbox.nfcparser.emv.parser.EmvTLVList;

public class PseDirectory {

    public String dfName;

    public String lang;

    public int fsi;

    public final EmvTLVList parsedRecv;

    public PseDirectory(EmvTLVList parsedRecv) {
        this.parsedRecv = parsedRecv;
    }

    public byte[] getAid() {
        // AID = A0000000031010 ==> Visa credit or debit
        return parsedRecv.getTlvValue(Emv41Enum.DF_ADF_NAME);
    }

    public Byte getSfi() {
        Byte sfiByte = null;
        byte[] fciSfi = parsedRecv.getTlvValue(Emv41Enum.DF_FCI_SFI);
        if (fciSfi != null) {
            int sfi = fciSfi[0];
            sfiByte = (byte) ((fsi << 3) | 4);
        }
        return sfiByte;
    }

    @Override
    public String toString() {
        return "PseDirectory{" +
                "dfName='" + dfName + '\'' +
                ", lang='" + lang + '\'' +
                ", fsi=" + fsi +
                '}';
    }
}
