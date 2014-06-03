package eu.ttbox.nfcparser.emv.parser;


import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import eu.ttbox.nfcparser.emv.Emv41Enum;
import eu.ttbox.nfcparser.utils.ISOUtil;

public class EmvTLVList {

    private List<TagTLV> tags = new ArrayList<TagTLV>();

    private LinkedHashMap<Integer, TagTLV> mapTags = new LinkedHashMap<Integer, TagTLV>();


    // ===========================================================
    // Constructor
    // ===========================================================


    public EmvTLVList(byte[] buf) {
        this(buf, 0);
    }

    public EmvTLVList(byte[] buf, int offset) {
        super();
        unpack(buf, offset);
    }


    // ===========================================================
    // Parser
    // ===========================================================


    /**
     * unpack a message with a starting offset
     *
     * @param buf    - raw message
     * @param offset
     */
    public void unpack(byte[] buf, int offset) throws RuntimeException {
        ByteBuffer buffer = ByteBuffer.wrap(buf, offset, buf.length - offset);
        parseTLVListLInDept(buffer, null);
    }

    private List<TagTLV> parseTLVListLInDept(byte[] buf, TagTLV parentKey) {
        ByteBuffer buffer = ByteBuffer.wrap(buf, 0, buf.length);
        return parseTLVListLInDept(buffer, parentKey);
    }

    private List<TagTLV> parseTLVListLInDept(ByteBuffer buffer, TagTLV parentKey) {
        List<TagTLV> levelTags = new ArrayList<TagTLV>();
        while (hasNext(buffer)) {
            TagTLV currentNode = getTLVMsg(buffer);    // null is returned if no tag found (trailing padding)
            if (currentNode != null)
                // Parent
                currentNode.parentKey = parentKey;
            // Search type
            // TODO
            Emv41Enum emv = Emv41Enum.getByTag(currentNode.getTagIdAsInteger());
            if (emv == null) {
                // Not found add
                append(currentNode);
                // System.out.println("Not found type : " +currentNode );
            } else {
                System.out.println("Found type : " + emv);
                switch (emv.type) {
                    case TLV: {
                        List<TagTLV> sub = parseTLVListLInDept(currentNode.tagValue, null);
                        currentNode.addAllChildKey(sub);
                        // Save it
                        levelTags.add(currentNode);
                        append(currentNode);
                    }
                    break;
                    default: {
                        // Save it
                        levelTags.add(currentNode);
                        append(currentNode);
                    }
                }
            }
        }
        return levelTags;
    }

    /**
     * Append TLVMsg to the TLVList
     */
    public void append(TagTLV tlvToAppend) {
        tags.add(tlvToAppend);
        mapTags.put(tlvToAppend.getTagIdAsInteger(), tlvToAppend);
    }


    // ===========================================================
    // Static
    // ===========================================================


    /**
     * Read next TLV Message from stream and return it
     *
     * @param buffer
     * @return TLVMsg
     */
    private static TagTLV getTLVMsg(ByteBuffer buffer) throws RuntimeException {
        int tag = getTAG(buffer);  // tag = 0 if tag not found
        if (tag == 0)
            return null;

        // Get Length if buffer remains!
        if (!buffer.hasRemaining())
            throw new RuntimeException(String.format("BAD TLV FORMAT - tag (%x)"
                    + " without length or value", tag));

        int length = getValueLength(buffer);
        if (length > buffer.remaining())
            throw new RuntimeException(String.format("BAD TLV FORMAT - tag (%x)"
                    + " length (%d) exceeds available data.", tag, length));

        byte[] arrValue = new byte[length];
        buffer.get(arrValue);

        return getTLVMsg(tag, arrValue);
    }

    private static TagTLV getTLVMsg(int tag, byte[] arrValue) {
        return new TagTLV(tag, arrValue);
    }


    /**
     * Read length bytes and return the int value
     *
     * @param buffer
     * @return value length
     */
    private static int getValueLength(ByteBuffer buffer) {
        byte b = buffer.get();
        int count = b & 0x7f;
        // check first byte for more bytes to follow
        if ((b & 0x80) == 0 || count == 0)
            return count;

        //fetch rest of bytes
        byte[] bb = new byte[count];
        buffer.get(bb);
        //adjust buffer if first bit is turn on
        //important for BigInteger representation
        if ((bb[0] & 0x80) > 0)
            bb = ISOUtil.concat(new byte[1], bb);
        return new BigInteger(bb).intValue();
    }

    /**
     * Return the next TAG
     *
     * @return tag
     */
    public static int getTAG(ByteBuffer buffer) {
        int b;
        int tag;
        b = buffer.get() & 0xff;
        // Skip padding chars
        if (b == 0xFF || b == 0x00) {
            do {
                b = buffer.get() & 0xff;
            } while ((b == 0xFF || b == 0x00) && hasNext(buffer));
        }
        // Get first byte of Tag Identifier
        tag = b;
        // Get rest of Tag identifier if required
        if ((b & 0x1F) == 0x1F) {
            do {
                tag <<= 8;
                b = buffer.get();
                tag |= b & 0xFF;

            } while ((b & 0x80) == 0x80);
        }
        return tag;
    }

    /**
     * Check Existance of next TLV Field
     *
     * @param buffer ByteBuffer containing TLV data
     */
    private static boolean hasNext(ByteBuffer buffer) {
        return buffer.hasRemaining();
    }


}
