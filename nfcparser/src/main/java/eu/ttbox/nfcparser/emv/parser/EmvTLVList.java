package eu.ttbox.nfcparser.emv.parser;


import org.jpos.emv.EMVStandardTagType;
import org.jpos.emv.UnknownTagNumberException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;
import org.jpos.tlv.TLVList;
import org.jpos.tlv.TLVMsg;

import java.math.BigInteger;
import java.util.HashMap;

import eu.ttbox.nfcparser.model.RecvTag;

public class EmvTLVList {

    private static HashMap<RecvTag, byte[]> parseTLVListLInDept(byte[] tlv, RecvTag parentKey, HashMap<RecvTag, byte[]> presult) {
        HashMap<RecvTag, byte[]> result = presult == null ? new HashMap<RecvTag, byte[]>() : presult;
        TLVList tlvList = new TLVList();
        try {
            tlvList.unpack(tlv);
            // Read
            for ( TLVMsg tlvMsg :  tlvList.getTags()){
                int tag =  tlvMsg.getTag();
                byte[] tagValue =  tlvMsg.getValue();

                // Tag

                EMVStandardTagType emvTag = null;
                try {
                    emvTag = EMVStandardTagType.forCode(tag);

                } catch (UnknownTagNumberException noTag) {

                }
                EmvKey recvTag = new EmvKey(tlvMsg);

            };
        } catch (ISOException e){

        }
        return result;
    }



}
