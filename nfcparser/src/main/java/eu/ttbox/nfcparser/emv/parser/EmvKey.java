package eu.ttbox.nfcparser.emv.parser;


import org.jpos.tlv.TLVMsg;

public class EmvKey {

    private int tag;

    private int valueSize;

    public EmvKey(TLVMsg tlvMsg) {
      this(tlvMsg.getTag(), tlvMsg.getValue().length);
    }


    public EmvKey(int tag, int valueSize) {
        this.tag = tag;
        this.valueSize = valueSize;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmvKey)) return false;

        EmvKey emvKey = (EmvKey) o;

        if (tag != emvKey.tag) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return tag;
    }


}

