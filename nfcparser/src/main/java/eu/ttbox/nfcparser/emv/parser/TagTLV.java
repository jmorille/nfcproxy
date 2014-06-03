package eu.ttbox.nfcparser.emv.parser;


import java.util.ArrayList;
import java.util.Collection;

import eu.ttbox.nfcparser.utils.NumUtil;

public class TagTLV {

    public int tagId;

    public int tagValueSize;

    public byte[] tagValue;

    // Hierarchy
    public TagTLV parentKey;

    private ArrayList<TagTLV> childKeys;


    // ===========================================================
    // Constructor
    // ===========================================================

    public TagTLV(int tagId) {
        this(tagId, 0, null);
    }

    public TagTLV(int tagId, byte[] tagValue) {
        this(tagId, tagValue != null ? tagValue.length : 0, tagValue);
    }

    public TagTLV(int tagId, int tagValueSize, byte[] tagValue) {
        this.tagId = tagId;
        this.tagValueSize = tagValueSize;
        this.tagValue = tagValue;
    }


    // ===========================================================
    // Tag Id
    // ===========================================================

    public Integer getTagIdAsInteger() {
        return Integer.valueOf(tagId);
    }

    public String getTagIdAsHexString() {
        String hexVal = Integer.toHexString(tagId);
        return hexVal;
    }

    public byte[] getTagIdAsByte() {
        return NumUtil.intToBytes(tagId);
    }


    // ===========================================================
    // parent / Child
    // ===========================================================


    public ArrayList<TagTLV> getChildKeys() {
        return childKeys;
    }

    private ArrayList<TagTLV> createChildKeys() {
        if (this.childKeys == null) {
            this.childKeys = new ArrayList<TagTLV>();
        }
        return this.childKeys;
    }

    public void addChildKey(TagTLV childKey) {
        if (this.childKeys == null) {
            createChildKeys();
        }
        this.childKeys.add(childKey);
    }

    public void addAllChildKey(Collection<TagTLV> toAddChilds) {
        if (childKeys != null) {
            if (this.childKeys == null) {
                createChildKeys();
            }
            this.childKeys.addAll(toAddChilds);
        }
    }

    // ===========================================================
    // Override
    // ===========================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagTLV tagTLV = (TagTLV) o;

        if (tagId != tagTLV.tagId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return tagId;
    }

    @Override
    public String toString() {
        return "TagTLV{" + getTagIdAsHexString() +
                " (" + tagValueSize +
                ") =" + NumUtil.byte2Hex(tagValue) +
                '}';
    }


    // ===========================================================
    // Other
    // ===========================================================

}
