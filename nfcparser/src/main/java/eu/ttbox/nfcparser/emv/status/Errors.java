package eu.ttbox.nfcparser.emv.status;

import java.util.ArrayList;


public class Errors {

    private static ArrayList<Err> ERRORS = getErrors();

    public static final String ERR_SW2_DATASIZE = "XX";
    public static final String ERR_SW2_FX = "FX";
    public static final String ERR_SW2_NOT = "--";
    public static final String ERR_SW2_CX = "CX";
    public static final String ERR_SW2_START9 = "9-";

    public static boolean isSw2Data(String sw2String) {
        if (sw2String == null || sw2String.isEmpty()) {
            return false;
        } else if (sw2String.equalsIgnoreCase(ERR_SW2_DATASIZE)) {
            return false;
        } else if (sw2String.equalsIgnoreCase(ERR_SW2_FX)) {
            return false;
        } else if (sw2String.equalsIgnoreCase(ERR_SW2_NOT)) {
            return false;
        } else if (sw2String.equalsIgnoreCase(ERR_SW2_CX)) {
            return false;
        } else if (sw2String.equalsIgnoreCase(ERR_SW2_START9)) {
            return false;
        }

        return true;
    }

    public static ArrayList<Err> getError(byte[] recv) {
        ArrayList<Err> result = null;
        int recvSize = recv.length;
        if (recvSize >= 2) {
            byte sw1 = recv[recvSize - 2];
            byte sw2 = recv[recvSize - 1];
            result = getError(sw1, sw2);
        }
        return result;
    }

    public static ArrayList<Err> getError(byte sw1, byte sw2) {
        Err result = null;
        ArrayList<Err> sw1Filter = new ArrayList<Err>();
        for (Err err : ERRORS) {
            if (err.sw1Byte == sw1) {
                if (err.sw2Byte != null) {
                    if (err.sw2Byte.byteValue() == sw2) {
                        sw1Filter.clear();
                        sw1Filter.add(err);
                        return sw1Filter;
                    }
                } else {
                    sw1Filter.add(err);
                }
            }
        }


        return sw1Filter;
    }

    public static ArrayList<Err> getErrors() {
        ArrayList<Err> error = new ArrayList<Err>();

        error.add(new Err("6", "", "E", "Class not supported."));
        error.add(new Err("61", "--", "I", "Response bytes still available"));
        error.add(new Err("61", "XX", "I", "Command successfully executed; 'XX' bytes of data are available and can be requested using GET RESPONSE."));
        error.add(new Err("62", "--", "W", "State of non-volatile memory unchanged"));
        error.add(new Err("62", "0", "W", "No information given (NV-Ram not changed)"));
        error.add(new Err("62", "1", "W", "NV-Ram not changed 1."));
        error.add(new Err("62", "81", "W", "Part of returned data may be corrupted"));
        error.add(new Err("62", "82", "W", "End of file/record reached before reading Le bytes"));
        error.add(new Err("62", "83", "W", "Selected file invalidated"));
        error.add(new Err("62", "84", "W", "Selected file is not valid. FCI not formated according to ISO"));
        error.add(new Err("62", "85", "W", "No Purse Engine enslaved for R3bc"));
        error.add(new Err("62", "A2", "W", "Wrong R-MAC"));
        error.add(new Err("62", "A4", "W", "Card locked (during reset( ))"));
        error.add(new Err("62", "CX", "W", "Counter with value x (command dependent)"));
        error.add(new Err("62", "F1", "W", "Wrong C-MAC"));
        error.add(new Err("62", "F3", "W", "Internal reset"));
        error.add(new Err("62", "F5", "W", "Default agent locked"));
        error.add(new Err("62", "F7", "W", "Cardholder locked"));
        error.add(new Err("62", "F8", "W", "Basement is current agent"));
        error.add(new Err("62", "F9", "W", "CALC Key Set not unblocked"));
        error.add(new Err("62", "FX", "W", "-"));
        error.add(new Err("62", "XX", "W", "RFU"));
        error.add(new Err("63", "--", "W", "State of non-volatile memory changed"));
        error.add(new Err("63", "0", "W", "No information given (NV-Ram changed)"));
        error.add(new Err("63", "81", "W", "File filled up by the last write. Loading/updating is not allowed."));
        error.add(new Err("63", "82", "W", "Card key not supported."));
        error.add(new Err("63", "83", "W", "Reader key not supported."));
        error.add(new Err("63", "84", "W", "Plaintext transmission not supported."));
        error.add(new Err("63", "85", "W", "Secured transmission not supported."));
        error.add(new Err("63", "86", "W", "Volatile memory is not available."));
        error.add(new Err("63", "87", "W", "Non-volatile memory is not available."));
        error.add(new Err("63", "88", "W", "Key number not valid."));
        error.add(new Err("63", "89", "W", "Key length is not correct."));
        error.add(new Err("63", "C0", "W", "Verify fail, no try left."));
        error.add(new Err("63", "C1", "W", "Verify fail, 1 try left."));
        error.add(new Err("63", "C2", "W", "Verify fail, 2 tries left."));
        error.add(new Err("63", "C3", "W", "Verify fail, 3 tries left."));
        error.add(new Err("63", "CX", "W", "Counter with value x (command dependent)"));
        error.add(new Err("63", "FX", "W", "-"));
        error.add(new Err("63", "XX", "W", "RFU"));
        error.add(new Err("64", "--", "E", "State of non-volatile memory unchanged"));
        error.add(new Err("64", "0", "E", "No information given (NV-Ram not changed)"));
        error.add(new Err("64", "1", "E", "Command timeout."));
        error.add(new Err("64", "XX", "E", "RFU"));
        error.add(new Err("65", "--", "E", "State of non-volatile memory changed"));
        error.add(new Err("65", "0", "E", "No information given"));
        error.add(new Err("65", "1", "E", "Write error. Memory failure. There have been problems in writing or reading the EEPROM. Other hardware problems may also bring this error."));
        error.add(new Err("65", "81", "E", "Memory failure"));
        error.add(new Err("65", "FX", "E", "-"));
        error.add(new Err("65", "XX", "E", "RFU"));
        error.add(new Err("66", "--", "S", ""));
        error.add(new Err("66", "69", "S", "Incorrect Encryption/Decryption Padding"));
        error.add(new Err("66", "XX", "S", "-"));
        error.add(new Err("67", "--", "E", ""));
        error.add(new Err("67", "0", "E", "Wrong length"));
        error.add(new Err("67", "XX", "E", "length incorrect (procedure)(ISO 7816-3)"));
        error.add(new Err("68", "--", "E", "Functions in CLA not supported"));
        error.add(new Err("68", "0", "E", "No information given (The request function is not supported by the card)"));
        error.add(new Err("68", "81", "E", "Logical channel not supported"));
        error.add(new Err("68", "82", "E", "Secure messaging not supported"));
        error.add(new Err("68", "83", "E", "Last command of the chain expected"));
        error.add(new Err("68", "84", "E", "Command chaining not supported"));
        error.add(new Err("68", "FX", "E", "-"));
        error.add(new Err("68", "XX", "E", "RFU"));
        error.add(new Err("69", "--", "E", "Command not allowed"));
        error.add(new Err("69", "0", "E", "No information given (Command not allowed)"));
        error.add(new Err("69", "81", "E", "Command incompatible with file structure"));
        error.add(new Err("69", "82", "E", "Security condition not satisfied."));
        error.add(new Err("69", "83", "E", "Authentication method blocked"));
        error.add(new Err("69", "84", "E", "Referenced data reversibly blocked (invalidated)"));
        error.add(new Err("69", "85", "E", "Conditions of use not satisfied"));
        error.add(new Err("69", "86", "E", "Command not allowed (no current EF)"));
        error.add(new Err("69", "87", "E", "Expected secure messaging (SM) object missing"));
        error.add(new Err("69", "88", "E", "Incorrect secure messaging (SM) data object"));
        error.add(new Err("69", "96", "E", "Data must be updated again"));
        error.add(new Err("69", "F0", "E", "Permission Denied"));
        error.add(new Err("69", "F1", "E", "Permission Denied - Missing Privilege"));
        error.add(new Err("69", "FX", "E", "-"));
        error.add(new Err("69", "XX", "E", "RFU"));
        error.add(new Err("6A", "--", "E", "Wrong parameter(s) P1-P2"));
        error.add(new Err("6A", "0", "E", "No information given (Bytes P1 and/or P2 are incorrect)"));
        error.add(new Err("6A", "80", "E", "The parameters in the data field are incorrect."));
        error.add(new Err("6A", "81", "E", "Function not supported"));
        error.add(new Err("6A", "82", "E", "File not found"));
        error.add(new Err("6A", "83", "E", "Record not found"));
        error.add(new Err("6A", "84", "E", "There is insufficient memory space in record or file"));
        error.add(new Err("6A", "85", "E", "Lc inconsistent with TLV structure"));
        error.add(new Err("6A", "86", "E", "Incorrect P1 or P2 parameter."));
        error.add(new Err("6A", "87", "E", "Lc inconsistent with P1-P2"));
        error.add(new Err("6A", "88", "E", "Referenced data not found"));
        error.add(new Err("6A", "89", "E", "File already exists"));
        error.add(new Err("6A", "8A", "E", "DF name already exists."));
        error.add(new Err("6A", "F0", "E", "Wrong parameter value"));
        error.add(new Err("6A", "FX", "E", "-"));
        error.add(new Err("6A", "XX", "E", "RFU"));
        error.add(new Err("6B", "--", "E", ""));
        error.add(new Err("6B", "0", "E", "Wrong parameter(s) P1-P2"));
        error.add(new Err("6B", "XX", "E", "Reference incorrect (procedure byte), (ISO 7816-3)"));
        error.add(new Err("6C", "--", "E", "Wrong length Le"));
        error.add(new Err("6C", "0", "E", "Incorrect P3 length."));
        error.add(new Err("6C", "XX", "E", "xx = exact Le"));
        error.add(new Err("6D", "--", "E", ""));
        error.add(new Err("6D", "0", "E", "Instruction code not supported or invalid"));
        error.add(new Err("6D", "XX", "E", "Instruction code not programmed or invalid (procedure byte), (ISO 7816-3)"));
        error.add(new Err("6E", "--", "E", ""));
        error.add(new Err("6E", "0", "E", "Class not supported"));
        error.add(new Err("6E", "XX", "E", "Instruction class not supported (procedure byte), (ISO 7816-3)"));
        error.add(new Err("6F", "--", "E", "Internal exception"));
        error.add(new Err("6F", "0", "E", "Command aborted - more exact diagnosis not possible (e.g., operating system error)."));
        error.add(new Err("6F", "FF", "E", "Card dead (overuse, …)"));
        error.add(new Err("6F", "XX", "E", "No precise diagnosis (procedure byte), (ISO 7816-3)"));
        // error.add(new Err("9-", "--", "", ""));
        error.add(new Err("90", "0", "I", "Command successfully executed (OK)."));
        error.add(new Err("90", "4", "W", "PIN not succesfully verified, 3 or more PIN tries left"));
        error.add(new Err("90", "8", "", "Key/file not found"));
        error.add(new Err("90", "80", "W", "Unblock Try Counter has reached zero"));
        error.add(new Err("91", "1", "", "States.activity, States.lock Status or States.lockable has wrong value"));
        error.add(new Err("91", "2", "", "Transaction number reached its limit"));
        error.add(new Err("92", "10", "E", "No more storage available."));
        error.add(new Err("93", "1", "", "Integrity error"));
        error.add(new Err("93", "2", "", "Candidate S2 invalid"));
        error.add(new Err("94", "1", "", "Candidate currency code does not match purse currency"));
        error.add(new Err("94", "2", "", "Candidate amount too high"));
        error.add(new Err("94", "3", "", "Candidate amount too low"));
        error.add(new Err("94", "5", "", "Problems in the data field"));
        error.add(new Err("94", "7", "", "Bad currency : purse engine has no slot with R3bc currency"));
        error.add(new Err("94", "8", "", "R3bc currency not supported in purse engine"));
        error.add(new Err("95", "80", "", "Bad sequence"));
        error.add(new Err("96", "81", "", "Slave not found"));
        error.add(new Err("97", "0", "", "PIN blocked and Unblock Try Counter is 1 or 2"));
        error.add(new Err("97", "2", "", "Main keys are blocked"));
        error.add(new Err("97", "4", "", "PIN not succesfully verified, 3 or more PIN tries left"));
        error.add(new Err("97", "84", "", "Base key"));
        error.add(new Err("97", "85", "", "Limit exceeded - C-MAC key"));
        error.add(new Err("97", "86", "", "SM error - Limit exceeded - R-MAC key"));
        error.add(new Err("97", "87", "", "Limit exceeded - sequence counter"));
        error.add(new Err("97", "88", "", "Limit exceeded - R-MAC length"));
        error.add(new Err("97", "89", "", "Service not available"));
        error.add(new Err("98", "4", "E", "Access conditions not satisfied."));
        error.add(new Err("99", "0", "", "1 PIN try left"));
        error.add(new Err("99", "4", "", "PIN not succesfully verified, 1 PIN try left"));
        error.add(new Err("99", "85", "", "Wrong status - Cardholder lock"));
        error.add(new Err("99", "86", "E", "Missing privilege"));
        error.add(new Err("99", "87", "", "PIN is not installed"));
        error.add(new Err("99", "88", "", "Wrong status - R-MAC state"));
        error.add(new Err("9A", "0", "", "2 PIN try left"));
        error.add(new Err("9A", "4", "", "PIN not succesfully verified, 2 PIN try left"));
        error.add(new Err("9A", "71", "", "Wrong parameter value - Double agent AID"));
        error.add(new Err("9A", "72", "", "Wrong parameter value - Double agent Type"));
        error.add(new Err("9D", "5", "E", "Incorrect certificate type"));
        error.add(new Err("9D", "7", "E", "Incorrect session data size"));
        error.add(new Err("9D", "8", "E", "Incorrect DIR file record size"));
        error.add(new Err("9D", "9", "E", "Incorrect FCI record size"));
        error.add(new Err("9D", "0A", "E", "Incorrect code size"));
        error.add(new Err("9D", "10", "E", "Insufficient memory to load application"));
        error.add(new Err("9D", "11", "E", "Invalid AID"));
        error.add(new Err("9D", "12", "E", "Duplicate AID"));
        error.add(new Err("9D", "13", "E", "Application previously loaded"));
        error.add(new Err("9D", "14", "E", "Application history list full"));
        error.add(new Err("9D", "15", "E", "Application not open"));
        error.add(new Err("9D", "17", "E", "Invalid offset"));
        error.add(new Err("9D", "18", "E", "Application already loaded"));
        error.add(new Err("9D", "19", "E", "Invalid certificate"));
        error.add(new Err("9D", "1A", "E", "Invalid signature"));
        error.add(new Err("9D", "1B", "E", "Invalid KTU"));
        error.add(new Err("9D", "1D", "E", "MSM controls not set"));
        error.add(new Err("9D", "1E", "E", "Application signature does not exist"));
        error.add(new Err("9D", "1F", "E", "KTU does not exist"));
        error.add(new Err("9D", "20", "E", "Application not loaded"));
        error.add(new Err("9D", "21", "E", "Invalid Open command data length"));
        error.add(new Err("9D", "30", "E", "Check data parameter is incorrect (invalid start address)"));
        error.add(new Err("9D", "31", "E", "Check data parameter is incorrect (invalid length)"));
        error.add(new Err("9D", "32", "E", "Check data parameter is incorrect (illegal memory check area)"));
        error.add(new Err("9D", "40", "E", "Invalid MSM Controls ciphertext"));
        error.add(new Err("9D", "41", "E", "MSM controls already set"));
        error.add(new Err("9D", "42", "E", "Set MSM Controls data length less than 2 bytes"));
        error.add(new Err("9D", "43", "E", "Invalid MSM Controls data length"));
        error.add(new Err("9D", "44", "E", "Excess MSM Controls ciphertext"));
        error.add(new Err("9D", "45", "E", "Verification of MSM Controls data failed"));
        error.add(new Err("9D", "50", "E", "Invalid MCD Issuer production ID"));
        error.add(new Err("9D", "51", "E", "Invalid MCD Issuer ID"));
        error.add(new Err("9D", "52", "E", "Invalid set MSM controls data date"));
        error.add(new Err("9D", "53", "E", "Invalid MCD number"));
        error.add(new Err("9D", "54", "E", "Reserved field error"));
        error.add(new Err("9D", "55", "E", "Reserved field error"));
        error.add(new Err("9D", "56", "E", "Reserved field error"));
        error.add(new Err("9D", "57", "E", "Reserved field error"));
        error.add(new Err("9D", "60", "E", "MAC verification failed"));
        error.add(new Err("9D", "61", "E", "Maximum number of unblocks reached"));
        error.add(new Err("9D", "62", "E", "Card was not blocked"));
        error.add(new Err("9D", "63", "E", "Crypto functions not available"));
        error.add(new Err("9D", "64", "E", "No application loaded"));
        error.add(new Err("9E", "0", "", "PIN not installed"));
        error.add(new Err("9E", "4", "", "PIN not succesfully verified, PIN not installed"));
        error.add(new Err("9F", "0", "", "PIN blocked and Unblock Try Counter is 3"));
        error.add(new Err("9F", "4", "", "PIN not succesfully verified, PIN blocked and Unblock Try Counter is 3"));
        // error.add(new Err("9x", "XX", "", "Application related status, (ISO 7816-3)"));

        return error;
    }
}
