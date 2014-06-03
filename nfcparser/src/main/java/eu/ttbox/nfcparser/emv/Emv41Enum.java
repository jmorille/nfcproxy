package eu.ttbox.nfcparser.emv;


import java.util.HashMap;

import eu.ttbox.nfcparser.model.RecvTag;
import eu.ttbox.nfcparser.utils.ISOUtil;
import eu.ttbox.nfcparser.utils.NumUtil;


/**
 * http://www.eftlab.co.uk/index.php/site-map/knowledge-base/145-emv-nfc-tags
 *
 * EMV_v4.3_Book_3_Application_Specification_20120607062110791.pdf
 *   Table 33: Data Elements Dictionary
 */
public enum Emv41Enum {

    // TLV
    BIC(0x5F54,  Emv41TypeEnum.TLV),
    IBAN(0x5F53,  Emv41TypeEnum.TLV),

    AFL(0x94,  Emv41TypeEnum.UNNKOWN),

    Application_Primary_Account_Number_PAN(0x5A, Emv41TypeEnum.CN),
    Application_Expiration_Date(0x5F24, Emv41TypeEnum.YYMMDD),
    Application_Usage_Control(0x9F07, Emv41TypeEnum.UNNKOWN),

    READ_RECORD_Response_Message_Template(0x70,  Emv41TypeEnum.TLV),
    Response_Message_Template_Format_1(0x80,  Emv41TypeEnum.TLV),
    Response_Message_Template_Format_2(0x77,  Emv41TypeEnum.TLV),

    Log_Entry_SFI(0x9F4D, Emv41TypeEnum.UNNKOWN),

    // TODO find ref
    PDOL(0x9F38, Emv41TypeEnum.UNNKOWN),
    FCI_Issuer_Discretionary_DATA(0xBF0C, Emv41TypeEnum.TLV),
    KERNEL_IDENTIFIER(0x9F2A,  Emv41TypeEnum.UNNKOWN),

    // refer to EMV 4.1 Book 1 - Section 11.3.4
    DF_FCI(0x6F, Emv41TypeEnum.TLV),
    DF_FCI_PROPRIETARY(0xA5, Emv41TypeEnum.TLV),
    DF_FCI_SFI(0x88, Emv41TypeEnum.UNNKOWN),
    DF_FCI_NAME(0x84, Emv41TypeEnum.STRING),
    DF_FCI_LANG(0x5F2D, Emv41TypeEnum.STRING),

    PSE_ENTRY(0x61, Emv41TypeEnum.TLV),

    // refer to EMV 4.1 Book 1 - Section 12.2.3
    DF_ADF_NAME(0x4F, Emv41TypeEnum.UNNKOWN),
    DF_ADF_LABEL(0x50, Emv41TypeEnum.STRING),
    DF_ADF_PREFERRED_NAME(0x9F12, Emv41TypeEnum.STRING),
    DF_ADF_PRIORITY(0x87, Emv41TypeEnum.UNNKOWN),

    // refer to EMV 4.1 Book 3 - Annex A - Data Elements Dictionary
    TRACK2_EQUIV_DATA(0x57, Emv41TypeEnum.UNNKOWN),
    CARDHOLDER_NAME(0x5F20, Emv41TypeEnum.STRING);


    public final int tagId;

    public final Emv41TypeEnum type;



    Emv41Enum(int  tag, Emv41TypeEnum type) {
        this.tagId = Integer.valueOf(tag);
        this.type = type;
    }

    public byte[] getTagIdAsBytes() {
       return NumUtil.intToBytes(tagId);
    }

    public String toString( byte[] value) {
        return type.toString(value);
    }


    private static   HashMap<Integer, Emv41Enum> byTag;

    static {
        HashMap<Integer, Emv41Enum> localByTag = new HashMap<Integer, Emv41Enum>();
        for (Emv41Enum emv : Emv41Enum.values()) {
            localByTag.put(Integer.valueOf( emv.tagId), emv);
        }
        byTag = localByTag;
    }

    public static  Emv41Enum getByTag(int tagId) {
        return getByTag(Integer.valueOf(tagId));
    }

    public static  Emv41Enum getByTag(Integer tagId) {
        return byTag.get(tagId);
    }

    @Deprecated
    public static  Emv41Enum getByTag(RecvTag tag) {
        return getByTag( ISOUtil.byte2int(tag.key));
    }

}
