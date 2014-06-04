package eu.ttbox.nfcparser.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import eu.ttbox.nfcparser.emv.parser.ITag;
import eu.ttbox.nfcparser.utils.NumUtil;

@Deprecated
public class RecvTag implements ITag {

    public final byte[] key;

    public final int valueSize;

    public RecvTag parentKey;
    private ArrayList<RecvTag> childKeys;



    // ===========================================================
    // Constructor
    // ===========================================================


    public RecvTag(String key) {
        this( NumUtil.hex2ByteNoSpace(key));
    }

    public RecvTag(byte[] key) {
        this(key,0);
    }

    public RecvTag(byte[] key, int valueSize) {
        this.key = key;
        this.valueSize = valueSize;
    }

    // ===========================================================
    // Accessor
    // ===========================================================


    public ArrayList<RecvTag> getChildKeys() {
        return childKeys;
    }

    private  ArrayList<RecvTag> createChildKeys() {
        if (this.childKeys == null) {
            this.childKeys = new ArrayList<RecvTag>();
        }
        return this.childKeys;
    }

    public void addChildKey( RecvTag  childKey ) {
        if (this.childKeys == null) {
          createChildKeys();
        }
        this.childKeys.add(childKey);
    }

    public void addAllChildKey( Collection< RecvTag> toAddChilds ) {
        if (childKeys!=null) {
            if (this.childKeys == null) {
                 createChildKeys();
            }
            this.childKeys.addAll(toAddChilds);
        }
    }

    public byte[] getValueSizeAsBytes(){
        return new byte[] {(byte)valueSize};
    }

    /**
     * @return  returned String will be "00 11 22 AA"
     */
    public String getKeytoHexString(){
       return  NumUtil.byte2Hex(key);
    }

    /**
     *
     * @return  returned String will be "001122AA"
     */
    public String getKeyHex2String(){
        return  NumUtil.byte2HexNoSpace(key);
    }

    @Override
    public Integer getTagIdAsInteger() {
        return NumUtil.bytesToInt(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        byte[] otherKey = null;
        if ((o instanceof RecvTag)) {
            RecvTag tag = (RecvTag) o;
            otherKey = tag.key;
        }
        // Compare
        if (otherKey!=null) {
            if (!Arrays.equals(key, otherKey)) return false;
        } else {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(key);
    }


    @Override
    public String toString() {
        return "RecvTag{" +
                "key=" + NumUtil.byte2HexNoSpace(key)   +
                ", valueSize=" + valueSize +
                '}';
    }


}
