package eu.ttbox.nfcparser.emv;


import java.util.HashMap;

import eu.ttbox.nfcparser.model.RecvTag;

/**
 * http://www.eftlab.co.uk/index.php/site-map/knowledge-base/145-emv-nfc-tags
 * <p/>
 * EMV_v4.3_Book_3_Application_Specification_20120607062110791.pdf
 * Table 33: Data Elements Dictionary
 */
public enum Emv41TagEnum {

    CARD_Issuer_Identification_Number_IIN("42", "Issuer Identification Number (IIN)", "The number that identifies the major industry and the card issuer and that forms the first part of the Primary Account Number (PAN)", "Card", "n 6", "'BF0C' or '73'", "3", "3"),
    CARD_Application_Identifier_ADF_Name("4F", "Application Identifier (ADF Name)", "The ADF Name identifies the application as described in [ISO 7816-5]. The AID is made up of the Registered Application Provider Identifier (RID) and the Proprietary Identifier Extension (PIX).", "Card", "binary 40-128", "'61'", "5", "16"),
    CARD_Application_Label("50", "Application Label", "Mnemonic associated with the AID according to ISO/IEC 7816-5", "Card", "ans with the special character limited to space", "'61' or 'A5'", "1", "16"),


    CARD_Command_to_perform("52", "Command to perform", "", "Card", "H", "", "", ""),
    Track_1_Data("56", "Track 1 Data", "Track 1 Data contains the data objects of the track 1 according to [ISO/IEC 7813] Structure B, excluding start sentinel, end sentinel and LRC. The Track 1 Data may be present in the file read using the READ RECORD command during a mag-stripe mode transaction.", "", "ans", "", "0", "76"),
    CARD_Track_2_Equivalent_Data("57", "Track 2 Equivalent Data", "Contains the data objects of the track 2, in accordance with [ISO/IEC 7813], excluding start sentinel, end sentinel, and LRC.", "Card", "binary", "'70' or '77'", "0", "19"),


    CARD_Application_Primary_Account_Number_PAN("5A", "Application Primary Account Number (PAN)", "Valid cardholder account number", "Card", "cn variable up to 19", "'70' or '77'", "0", "10"),


    TERMINAL_Deleted_see_9D("5D", "Deleted (see 9D)", "", "Terminal", "H", "", "", ""),
    CARD_Cardholder_Name("5F20", "Cardholder Name", "Indicates cardholder name according to ISO 7813", "Card", "ans 2-26", "'70' or '77'", "2", "26"),


    CARD_Application_Expiration_Date("5F24", "Application Expiration Date", "Date after which application expires. The date is expressed in the YYMMDD format. For MasterCard applications, if the value of YY ranges from '00' to '49' the date reads 20YYMMDD. If the value of YY ranges from '50' to '99' the date reads 19YYMMDD.", "Card", "n 6 (YYMMDD)", "'70' or '77'", "3", "3"),


    CARD_Application_Effective_Date("5F25", "Application Effective Date", "Date from which the application may be used. The date is expressed in the YYMMDD format. For MasterCard branded applications if the value of YY ranges from '00' to '49' the date reads 20YYMMDD. If the value of YY ranges from '50' to '99', the date reads 19YYMMDD.", "Card", "n 6 (YYMMDD)", "'70' or '77'", "3", "3"),


    CARD_Issuer_Country_Code("5F28", "Issuer Country Code", "Indicates the country of the issuer according to ISO 3166-1", "Card", "n 3", "'70' or '77'", "2", "2"),


    TERMINAL_Transaction_Currency_Code("5F2A", "Transaction Currency Code", "Indicates the currency code of the transaction according to ISO 4217", "Terminal", "n 3", "", "2", "2"),


    CARD_Language_Preference("5F2D", "Language Preference", "1-4 languages stored in order of preference, each represented by 2 alphabetical characters according to ISO 639", "Card", "an 2", "'A5'", "2", "8"),


    CARD_Service_Code("5F30", "Service Code", "Service code as defined in ISO/IEC 7813 for Track 1 and Track 2", "Card", "n 3", "'70' or '77'", "2", "2"),

    CARD_Application_Primary_Account_Number_PAN_Sequence_Number_PSN("5F34", "Application Primary Account Number (PAN) Sequence Number (PSN)", "Identifies and differentiates cards with the same Application PAN", "Card", "n 2", "'70' or '77'", "1", "1"),


    TERMINAL_Transaction_Currency_Exponent("5F36", "Transaction Currency Exponent", "Identifies the decimal point position from the right of the transaction amount accordin to ISO 4217", "Terminal", "n 1", "", "1", "1"),

    TERMINAL_Transaction_Reference_Currency_Code("5F3C", "Transaction Reference Currency Code", "Identifies the common currency used by the terminal", "Terminal", "binary 2", "", "1", "1"),
    TERMINAL_Transaction_Reference_Currency_Exponent("5F3D", "Transaction Reference Currency Exponent", "Identifies the decimal point position from the right of the terminal common currency", "Terminal", "n 1", "", "1", "1"),
    CARD_Issuer_URL("5F50", "Issuer URL", "The URL provides the location of the Issuer's Library Server on the Internet.", "Card", "ans", "'BF0C' or '73'", "var.", "var."),
    CARD_International_Bank_Account_Number_IBAN("5F53", "International Bank Account Number (IBAN)", "Uniquely identifies the account of a customer at a financial institution as defined in ISO 13616.", "Card", "variable", "'BF0C' or '73'", "0", "34"),
    CARD_Bank_Identifier_Code_BIC("5F54", "Bank Identifier Code (BIC)", "Uniquely identifies a bank as defined in ISO 9362.", "Card", "variable", "'BF0C' or '73'", "8 or 11", "8 or 11"),
    CARD_Issuer_Country_Code_alpha2_format("5F55", "Issuer Country Code (alpha2 format)", "Indicates the country of the issuer as defined in ISO 3166 (using a 2 character alphabetic code)", "Card", "a 2", "'BF0C' or '73'", "2", "2"),
    CARD_Issuer_Country_Code_alpha3_format("5F56", "Issuer Country Code (alpha3 format)", "Indicates the country of the issuer as defined in ISO 3166 (using a 3 character alphabetic code)", "Card", "a 3", "'BF0C' or '73'", "3", "3"),
    TERMINAL_Account_Type("5F57", "Account Type", "Indicates the type of account selected on the terminal, coded as specified in Annex G", "Terminal", "n 2", "", "1", "1"),
    CARD_Application_Template("61", "Application Template", "Template containing one or more data objects relevant to an application directory entry according to [ISO 7816-5].", "Card", "binary", "'70'", "var. up to 252", "var. up to 252"),

    CARD_File_Control_Parameters_FCP_Template("62", "File Control Parameters (FCP) Template", "Identifies the FCP template according to ISO/IEC 7816-4", "Card", "variable", "", "var. up to 252", "var. up to 252"),
    CARD_File_Control_Information_FCI_Template("6F", "File Control Information (FCI) Template", "Identifies the FCI template according to ISO/IEC 7816-4", "Card", "variable", "", "0", "252"),


    CARD_READ_RECORD_Response_Message_Template("70", "READ RECORD Response Message Template", "Template containing the data objects returned by the Card in response to a READ RECORD command. Contains the contents of the record read. (Mandatory for SFIs 1-10. Response messages for SFIs 11-30 are outside the scope of EMV, but may use template '70')", "Card", "variable", "", "0", "255"),

    ISSUER_Issuer_Script_Template_1("71", "Issuer Script Template 1", "Contains proprietary issuer data for transmission to the ICC before the second GENERATE AC command", "Issuer", "binary", "", "var.", "var."),

    ISSUER_Issuer_Script_Template_2("72", "Issuer Script Template 2", "Contains proprietary issuer data for transmission to the ICC after the second GENERATE AC command", "Issuer", "binary", "", "var.", "var."),

    CARD_Directory_Discretionary_Template("73", "Directory Discretionary Template", "Issuer discretionary part of the directory according to ISO/IEC 7816-5", "Card", "variable", "'61'", "0", "252"),
    CARD_Response_Message_Template_Format_2("77", "Response Message Template Format 2", "Contains the data objects (with tags and lengths) returned by the ICC in response to a command", "Card", "variable", "", "var.", "var."),


    CARD_Response_Message_Template_Format_1("80", "Response Message Template Format 1", "Contains the data objects (without tags and lengths) returned by the ICC in response to a command", "Card", "variable", "", "var.", "var."),


    TERMINAL_Amount_Authorised_Binary("81", "Amount, Authorised (Binary)", "Authorised amount of the transaction (excluding adjustments)", "Terminal", "binary", "", "4", "4"),
    CARD_Application_Interchange_Profile_AIP("82", "Application Interchange Profile (AIP)", "Indicates the capabilities of the card to support specific functions in the application", "Card", "binary", "'77' or '80'", "2", "2"),


    TERMINAL_Command_Template("83", "Command Template", "Identifies the data field of a command message", "Terminal", "binary", "", "var.", "var."),
    CARD_Dedicated_File_DF_Name("84", "Dedicated File (DF) Name", "Identifies the name of the DF as described in ISO/IEC 7816-4", "Card", "binary", "'6F'", "5", "16"),


    ISSUER_Issuer_Script_Command("86", "Issuer Script Command", "Contains a command for transmission to the ICC", "Issuer", "binary", "'71' or '72'", "var. up to 125", "var. up to 125"),
    CARD_Application_Priority_Indicator("87", "Application Priority Indicator", "Indicates the priority of a given application or group of applications in a directory", "Card", "binary", "'61' or 'A5'", "1", "1"),


    CARD_Short_File_Identifier_SFI("88", "Short File Identifier (SFI)", "Identifies the AEF referenced in commands related to a given ADF or DDF. It is a binary data object having a value in the range 1 to 30 and with the three high order bits set to zero.", "Card", "binary", "'A5'", "1", "1"),

    ISSUER_Authorisation_Code("89", "Authorisation Code", "Nonzero value generated by the issuer for an approved transaction.", "Issuer", "ans 6 (special characters limited to spaces)", "", "6", "6"),

    ISSUER_TERMINAL_Authorisation_Response_Code_ARC("8A", "Authorisation Response Code (ARC)", "Indicates the transaction disposition of the transaction received from the issuer for online authorisations.", "Issuer/Terminal", "an 2", "", "2", "2"),


    CARD_Card_Risk_Management_Data_Object_List_1_CDOL1("8C", "Card Risk Management Data Object List 1 (CDOL1)", "List of data objects (tag and length) to be passed to the ICC in the first GENERATE AC command", "Card", "binary", "'70' or '77'", "0", "252"),


    CARD_Card_Risk_Management_Data_Object_List_2_CDOL2("8D", "Card Risk Management Data Object List 2 (CDOL2)", "List of data objects (tag and length) to be passed to the ICC in the second GENERATE AC command", "Card", "binary", "'70' or '77'", "var. up to 64", "var. up to 64"),

    CARD_Cardholder_Verification_Method_CVM_List("8E", "Cardholder Verification Method (CVM) List", "Identifies a method of verification of the cardholder supported by the application", "Card", "binary", "'70' or '77'", "10", "252"),


    CARD_Certification_Authority_Public_Key_Index_PKI("8F", "Certification Authority Public Key Index (PKI)", "Identifies the certification authority's public key in conjunction with the RID", "Card", "binary", "'70' or '77'", "1", "1"),


    CARD_Issuer_Public_Key_Certificate("90", "Issuer Public Key Certificate", "Issuer public key certified by a certification authority", "Card", "binary", "'70' or '77'", "var. (NCA)", "var. (NCA)"),


    ISSUER_Issuer_Authentication_Data("91", "Issuer Authentication Data", "Data sent to the ICC for online Issuer Authentication", "Issuer", "binary 64-128", "", "8", "16"),


    CARD_Issuer_Public_Key_Remainder("92", "Issuer Public Key Remainder", "Remaining digits of the Issuer Public Key Modulus", "Card", "binary", "'70' or '77'", "var. (NI - NCA + 36)", "var. (NI - NCA + 36)"),


    CARD_Signed_Application_Data("93", "Signed Application Data", "Digital signature on critical application parameters that is used in static data authentication (SDA).", "Card", "binary 512-1984", "'70' or '77'", "64", "248"),
    CARD_Application_File_Locator_AFL("94", "Application File Locator (AFL)", "Indicates the location (SFI range of records) of the Application Elementary Files associated with a particular AID, and read by the Kernel during a transaction.", "Card", "binary, var.; multiple of 4 between 4 and 252", "'77' or '80'", "4", "252"),


    TERMINAL_Terminal_Verification_Results_TVR("95", "Terminal Verification Results (TVR)", "Status of the different functions as seen from the terminal", "Terminal", "", "", "6", "6"),


    CARD_Transaction_Certificate_Data_Object_List_TDOL("97", "Transaction Certificate Data Object List (TDOL)", "List of data objects (tag and length) to be used by the terminal in generating the TC Hash Value", "Card", "binary", "'70' or '77'", "0", "252"),
    TERMINAL_Transaction_Certificate_TC_Hash_Value("98", "Transaction Certificate (TC) Hash Value", "Result of a hash function specified in Book 2, Annex B3.1", "Terminal", "binary", "", "20", "20"),
    TERMINAL_Transaction_Personal_Identification_Number_PIN_Data("99", "Transaction Personal Identification Number (PIN) Data", "Data entered by the cardholder for the purpose of the PIN verification", "Terminal", "binary", "", "var.", "var."),
    TERMINAL_Transaction_Date("9A", "Transaction Date", "Local date that the transaction was authorised", "Terminal", "n 6 (YYMMDD)", "", "3", "3"),


    TERMINAL_Transaction_Status_Information("9B", "Transaction Status Information", "Indicates the functions performed in a transaction", "Terminal", "binary", "", "2", "2"),
    TERMINAL_Transaction_Type("9C", "Transaction Type", "Indicates the type of financial transaction, represented by the first two digits of the ISO 8583:1987 Processing Code. The actual values to be used for the Transaction Type data element are defined by the relevant payment system", "Terminal", "n 2", "", "1", "1"),


    CARD_Directory_Definition_File_DDF_Name("9D", "Directory Definition File (DDF) Name", "Identifies the name of a DF associated with a directory", "Card", "binary", "'61'", "5", "16"),
    TERMINAL_Acquirer_Identifier("9F01", "Acquirer Identifier", "Uniquely identifies the acquirer within each payment system", "Terminal", "n 6-11", "", "6", "11"),

    TERMINAL_Amount_Authorised_Numeric("9F02", "Amount, Authorised (Numeric)", "Authorised amount of the transaction (excluding adjustments)", "Terminal", "n 12", "", "6", "6"),


    TERMINAL_Amount_Other_Numeric("9F03", "Amount, Other (Numeric)", "Secondary amount associated with the transaction representing a cashback amount", "Terminal", "n 12", "", "6", "6"),


    TERMINAL_Amount_Other_Binary("9F04", "Amount, Other (Binary)", "Secondary amount associated with the transaction representing a cashback amount", "Terminal", "binary", "", "4", "4"),
    CARD_Application_Discretionary_Data("9F05", "Application Discretionary Data", "Issuer or payment system specified data relating to the application", "Card", "binary", "'70' or '77'", "1", "32"),
    TERMINAL_Application_Identifier_AID_terminal("9F06", "Application Identifier (AID) - terminal", "Identifies the application as described in ISO/IEC 7816-5", "Terminal", "binary", "", "5", "16"),

    CARD_Application_Usage_Control_AUC("9F07", "Application Usage Control (AUC)", "Indicates issuer's specified restrictions on the geographic usage and services allowed for the application", "Card", "binary", "'70' or '77'", "2", "2"),


    CARD_Application_Version_Number("9F08", "Application Version Number", "Version number assigned by the payment system for the application in the Card", "Card", "binary", "'70' or '77'", "2", "2"),

    TERMINAL_Application_Version_Number_9F09("9F09", "Application Version Number", "Version number assigned by the payment system for the Kernel application", "Terminal", "binary", "", "2", "2"),
    CARD_Cardholder_Name_Extended("9F0B", "Cardholder Name Extended", "Indicates the whole cardholder name when greater than 26 characters using the same coding convention as in ISO 7813", "Card", "ans 27-45", "'70' or '77'", "27", "45"),
    CARD_Issuer_Action_Code_Default("9F0D", "Issuer Action Code - Default", "Specifies the issuer's conditions that cause a transaction to be rejected if it might have been approved online, but the terminal is unable to process the transaction online", "Card", "binary", "'70' or '77'", "5", "5"),


    CARD_Issuer_Action_Code_Denial("9F0E", "Issuer Action Code - Denial", "Specifies the issuer's conditions that cause the denial of a transaction without attempt to go online", "Card", "binary", "'70' or '77'", "5", "5"),


    CARD_Issuer_Action_Code_Online("9F0F", "Issuer Action Code - Online", "Specifies the issuer's conditions that cause a transaction to be transmitted online", "Card", "binary", "'70' or '77'", "5", "5"),


    CARD_Issuer_Application_Data_IAD("9F10", "Issuer Application Data (IAD)", "Contains proprietary application data for transmission to the issuer in an online transaction.", "Card", "binary", "'77' or '80'", "0", "32"),


    CARD_Issuer_Code_Table_Index("9F11", "Issuer Code Table Index", "Indicates the code table according to ISO/IEC 8859 for displaying the Application Preferred Name", "Card", "n 2", "'A5'", "1", "1"),


    CARD_Application_Preferred_Name("9F12", "Application Preferred Name", "Preferred mnemonic associated with the AID", "Card", "ans", "'61' or 'A5'", "1", "16"),


    CARD_Last_Online_Application_Transaction_Counter_ATC_Register("9F13", "Last Online Application Transaction Counter (ATC) Register", "ATC value of the last transaction that went online", "Card", "binary", "", "2", "2"),
    CARD_Lower_Consecutive_Offline_Limit("9F14", "Lower Consecutive Offline Limit", "Issuer-specified preference for the maximum number of consecutive offline transactions for this ICC application allowed in a terminal with online capability", "Card", "binary", "'70' or '77'", "1", "1"),
    TERMINAL_Merchant_Category_Code("9F15", "Merchant Category Code", "Classifies the type of business being done by the merchant, represented according to ISO 8583:1993 for Card Acceptor Business Code", "Terminal", "n 4", "", "2", "2"),

    TERMINAL_Merchant_Identifier("9F16", "Merchant Identifier", "When concatenated with the Acquirer Identifier, uniquely identifies a given merchant", "Terminal", "ans 15", "", "15", "15"),
    CARD_Personal_Identification_Number_PIN_Try_Counter("9F17", "Personal Identification Number (PIN) Try Counter", "Number of PIN tries remaining", "Card", "binary", "", "1", "1"),
    ISSUER_Issuer_Script_Identifier("9F18", "Issuer Script Identifier", "May be sent in authorisation response from issuer when response contains Issuer Script. Assigned by the issuer to uniquely identify the Issuer Script.", "Issuer", "binary 32", "'71' or '72'", "4", "4"),

    Deleted_see_9F49("9F19", "Deleted (see 9F49)", "", "", "H", "", "", ""),
    TERMINAL_Terminal_Country_Code("9F1A", "Terminal Country Code", "Indicates the country of the terminal, represented according to ISO 3166", "Terminal", "n 3", "", "2", "2"),


    TERMINAL_Terminal_Floor_Limit("9F1B", "Terminal Floor Limit", "Indicates the floor limit in the terminal in conjunction with the AID", "Terminal", "binary 32", "", "4", "4"),

    TERMINAL_Terminal_Identification("9F1C", "Terminal Identification", "Designates the unique location of a Terminal at a merchant", "Terminal", "an 8", "", "8", "8"),
    TERMINAL_Terminal_Risk_Management_Data("9F1D", "Terminal Risk Management Data", "Application-specific value used by the card for risk management purposes", "Terminal", "binary", "", "1", "8"),
    TERMINAL_Interface_Device_IFD_Serial_Number("9F1E", "Interface Device (IFD) Serial Number", "Unique and permanent serial number assigned to the IFD by the manufacturer", "Terminal", "an 8", "", "8", "8"),
    CARD_Track_1_Discretionary_Data("9F1F", "Track 1 Discretionary Data", "Discretionary part of track 1 according to ISO/IEC 7813", "Card", "ans", "'70' or '77'", "var.", "var."),


    CARD_Track_2_Discretionary_Data("9F20", "Track 2 Discretionary Data", "Discretionary part of track 2 according to ISO/IEC 7813", "Card", "cn", "'70' or '77'", "var.", "var."),
    TERMINAL_Transaction_Time("9F21", "Transaction Time", "Local time at which the transaction was performed.", "Terminal", "n 6 (HHMMSS)", "", "3", "3"),

    TERMINAL_Certification_Authority_Public_Key_Index_PKI_9F22("9F22", "Certification Authority Public Key Index (PKI)", "Identifies the Certificate Authority’s public key in conjunction with the RID for use in offline static and dynamic data authentication.", "Terminal", "binary 8", "", "1", "1"),
    CARD_Upper_Consecutive_Offline_Limit("9F23", "Upper Consecutive Offline Limit", "Issuer-specified preference for the maximum number of consecutive offline transactions for this ICC application allowed in a terminal without online capability", "Card", "binary", "'70' or '77'", "1", "1"),
    CARD_Application_Cryptogram_AC("9F26", "Application Cryptogram (AC)", "Cryptogram returned by the ICC in response of the GENERATE AC or RECOVER AC command", "Card", "binary", "'77' or '80'", "8", "8"),


    CARD_Cryptogram_Information_Data_CID("9F27", "Cryptogram Information Data (CID)", "Indicates the type of cryptogram and the actions to be performed by the terminal", "Card", "binary", "'77' or '80'", "1", "1"),


    CARD_Extended_Selection("9F29", "Extended Selection", "The value to be appended to the ADF Name in the data field of the SELECT command, if the Extended Selection Support flag is present and set to 1. Content is payment system proprietary.", "Card", "binary", "'61'", "var.", "var."),
    CARD_Kernel_Identifier("9F2A", "Kernel Identifier", "Indicates the card’s preference for the kernel on which the contactless application can be processed.", "Card", "binary", "'61'", "1", "1"),
    CARD_Integrated_Circuit_Card_ICC_PIN_Encipherment_Public_Key_Certificate("9F2D", "Integrated Circuit Card (ICC) PIN Encipherment Public Key Certificate", "ICC PIN Encipherment Public Key certified by the issuer", "Card", "binary", "'70' or '77'", "var. (NI)", "var. (NI)"),
    CARD_Integrated_Circuit_Card_ICC_PIN_Encipherment_Public_Key_Exponent("9F2E", "Integrated Circuit Card (ICC) PIN Encipherment Public Key Exponent", "ICC PIN Encipherment Public Key Exponent used for PIN encipherment", "Card", "binary", "'70' or '77'", "1 or 3", "1 or 3"),
    CARD_Integrated_Circuit_Card_ICC_PIN_Encipherment_Public_Key_Remainder("9F2F", "Integrated Circuit Card (ICC) PIN Encipherment Public Key Remainder", "Remaining digits of the ICC PIN Encipherment Public Key Modulus", "Card", "binary", "'70' or '77'", "var. (NPE - NI + 42)", "var. (NPE - NI + 42)"),
    CARD_Issuer_Public_Key_Exponent("9F32", "Issuer Public Key Exponent", "Issuer public key exponent used for the verification of the Signed Static Application Data and the ICC Public Key Certificate", "Card", "binary", "'70' or '77'", "1", "3"),


    TERMINAL_Terminal_Capabilities("9F33", "Terminal Capabilities", "Indicates the card data input, CVM, and security capabilities of the Terminal and Reader. The CVM capability (Byte 2) is instantiated with values depending on the transaction amount. The Terminal Capabilities is coded according to Annex A.2 of [EMV Book 4].", "Terminal", "binary", "", "3", "3"),

    TERMINAL_Cardholder_Verification_Method_CVM_Results("9F34", "Cardholder Verification Method (CVM) Results", "Indicates the results of the last CVM performed", "Terminal", "binary", "", "3", "3"),
    TERMINAL_Terminal_Type("9F35", "Terminal Type", "Indicates the environment of the terminal, its communications capability, and its operational control", "Terminal", "n 2", "", "1", "1"),


    CARD_Application_Transaction_Counter_ATC("9F36", "Application Transaction Counter (ATC)", "Counter maintained by the application in the ICC (incrementing the ATC is managed by the ICC)", "Card", "binary", "'77' or '80'", "2", "2"),


    TERMINAL_Unpredictable_Number_UN("9F37", "Unpredictable Number (UN)", "Value to provide variability and uniqueness to the generation of a cryptogram", "Terminal", "binary", "", "4", "4"),


    CARD_Processing_Options_Data_Object_List_PDOL("9F38", "Processing Options Data Object List (PDOL)", "Contains a list of terminal resident data objects (tags and lengths) needed by the ICC in processing the GET PROCESSING OPTIONS command", "Card", "binary", "'A5'", "var.", "var."),


    TERMINAL_Point_of_Service_POS_Entry_Mode("9F39", "Point-of-Service (POS) Entry Mode", "Indicates the method by which the PAN was entered, according to the first two digits of the ISO 8583:1987 POS Entry Mode", "Terminal", "n 2", "", "1", "1"),
    TERMINAL_Amount_Reference_Currency("9F3A", "Amount, Reference Currency", "Authorised amount expressed in the reference currency", "Terminal", "binary", "", "4", "4"),
    CARD_Application_Reference_Currency("9F3B", "Application Reference Currency", "1-4 currency codes used between the terminal and the ICC when the Transaction Currency Code is different from the Application Currency Code; each code is 3 digits according to ISO 4217", "Card", "n 3", "'70' or '77'", "2", "8"),
    TERMINAL_Transaction_Reference_Currency_Code_9F3C("9F3C", "Transaction Reference Currency Code", "Code defining the common currency used by the terminal in case the Transaction Currency Code is different from the Application Currency Code", "Terminal", "n 3", "", "2", "2"),
    TERMINAL_Transaction_Reference_Currency_Exponent_9F3D("9F3D", "Transaction Reference Currency Exponent", "Indicates the implied position of the decimal point from the right of the transaction amount, with the Transaction Reference Currency Code represented according to ISO 4217", "Terminal", "n 1", "", "1", "1"),
    TERMINAL_Additional_Terminal_Capabilities("9F40", "Additional Terminal Capabilities", "Indicates the data input and output capabilities of the Terminal and Reader. The Additional Terminal Capabilities is coded according to Annex A.3 of [EMV Book 4].", "Terminal", "binary", "", "5", "5"),

    TERMINAL_Transaction_Sequence_Counter("9F41", "Transaction Sequence Counter", "Counter maintained by the terminal that is incremented by one for each transaction", "Terminal", "n 4-8", "", "2", "4"),
    CARD_Application_Currency_Code("9F42", "Application Currency Code", "Indicates the currency in which the account is managed according to ISO 4217", "Card", "n 3", "'70' or '77'", "2", "2"),


    CARD_Application_Reference_Currency_Exponent("9F43", "Application Reference Currency Exponent", "Indicates the implied position of the decimal point from the right of the amount, for each of the 1-4 reference currencies represented according to ISO 4217", "Card", "n 1", "'70' or '77'", "1", "4"),
    CARD_Application_Currency_Exponent("9F44", "Application Currency Exponent", "Indicates the implied position of the decimal point from the right of the amount represented according to ISO 4217", "Card", "n 1", "'70' or '77'", "1", "1"),
    CARD_Data_Authentication_Code("9F45", "Data Authentication Code", "An issuer assigned value that is retained by the terminal during the verification process of the Signed Static Application Data", "Card", "binary", "", "2", "2"),
    CARD_Integrated_Circuit_Card_ICC_Public_Key_Certificate("9F46", "Integrated Circuit Card (ICC) Public Key Certificate", "ICC Public Key certified by the issuer", "Card", "binary", "'70' or '77'", "var. (NI)", "var. (NI)"),


    CARD_Integrated_Circuit_Card_ICC_Public_Key_Exponent("9F47", "Integrated Circuit Card (ICC) Public Key Exponent", "Exponent ICC Public Key Exponent used for the verification of the Signed Dynamic Application Data", "Card", "binary", "'70' or '77'", "1", "3"),


    CARD_Integrated_Circuit_Card_ICC_Public_Key_Remainder("9F48", "Integrated Circuit Card (ICC) Public Key Remainder", "Remaining digits of the ICC Public Key Modulus", "Card", "binary", "'70' or '77'", "var. (NIC - NI + 42)", "var. (NIC - NI + 42)"),


    CARD_Dynamic_Data_Authentication_Data_Object_List_DDOL("9F49", "Dynamic Data Authentication Data Object List (DDOL)", "List of data objects (tag and length) to be passed to the ICC in the INTERNAL AUTHENTICATE command", "Card", "binary", "'70' or '77'", "0", "252"),
    CARD_Static_Data_Authentication_Tag_List_SDA("9F4A", "Static Data Authentication Tag List (SDA)", "List of tags of primitive data objects defined in this specification whose value fields are to be included in the Signed Static or Dynamic Application Data", "Card", "-", "'70' or '77'", "var.", "var."),


    CARD_Signed_Dynamic_Application_Data_SDAD("9F4B", "Signed Dynamic Application Data (SDAD)", "Digital signature on critical application parameters for CDA", "Card", "binary", "'77' or '80'", "var. (NIC)", "var. (NIC)"),


    CARD_ICC_Dynamic_Number("9F4C", "ICC Dynamic Number", "Time-variant number generated by the ICC, to be captured by the terminal", "Card", "binary", "", "2", "8"),
    CARD_Log_Entry("9F4D", "Log Entry", "Provides the SFI of the Transaction Log file and its number of records", "Card", "binary", "'BF0C' or '73'", "2", "2"),

    TERMINAL_Merchant_Name_and_Location("9F4E", "Merchant Name and Location", "Indicates the name and location of the merchant", "Terminal", "ans", "", "var.", "var."),


    CARD_Log_Format("9F4F", "Log Format", "List (in tag and length format) of data objects representing the logged data elements that are passed to the terminal when a transaction log record is read", "Card", "binary", "", "var.", "var."),
    Offline_Accumulator_Balance("9F50", "Offline Accumulator Balance", "Represents the amount of offline spending available in the Card. The Offline Accumulator Balance is retrievable by the GET DATA command, if allowed by the Card configuration.", "", "n 12", "", "6", "6"),

    CARD_Application_Currency_Code_9F51("9F51", "Application Currency Code", "", "Card", "binary", "", "var.", "var."),

    CARD_Application_Default_Action_ADA("9F52", "Application Default Action (ADA)", "", "Card", "binary", "", "var.", "var."),

    CARD_Consecutive_Transaction_Counter_International_Limit_CTCIL("9F53", "Consecutive Transaction Counter International Limit (CTCIL)", "", "Card", "binary", "", "var.", "var."),


    CARD_Cumulative_Total_Transaction_Amount_Limit_CTTAL("9F54", "Cumulative Total Transaction Amount Limit (CTTAL)", "", "Card", "binary", "", "var.", "var."),

    CARD_Geographic_Indicator("9F55", "Geographic Indicator", "", "Card", "H", "", "", ""),
    CARD_Issuer_Authentication_Indicator("9F56", "Issuer Authentication Indicator", "", "Card", "H", "", "", ""),
    CARD_Issuer_Country_Code_9F57("9F57", "Issuer Country Code", "", "Card", "binary", "", "var.", "var."),
    CARD_Consecutive_Transaction_Counter_Limit_CTCL("9F58", "Consecutive Transaction Counter Limit (CTCL)", "", "Card", "binary", "", "var.", "var."),
    CARD_Consecutive_Transaction_Counter_Upper_Limit_CTCUL("9F59", "Consecutive Transaction Counter Upper Limit (CTCUL)", "", "Card", "binary", "", "var.", "var."),
    CARD_Application_Program_Identifier_Program_ID("9F5A", "Application Program Identifier (Program ID)", "Payment system proprietary data element identifying the Application Program ID of the card application. When personalised, the Application Program ID is returned in the FCI Issuer Discretionary Data of the SELECT response (Tag ‘BF0C’). EMV mode readers that support Dynamic Reader Limits (DRL) functionality examine the Application Program ID to determine the Reader Limit Set to apply.", "Card", "binary", "", "1", "16"),

    TERMINAL_Issuer_Script_Results("9F5B", "Issuer Script Results", "Indicates the results of Issuer Script processing. When the reader/terminal transmits this data element to the acquirer, in this version of Kernel 3, it is acceptable that only byte 1 is transmitted, although it is preferable for all five bytes to be transmitted.", "Terminal", "binary", "", "var.", "var."),

    CARD_Cumulative_Total_Transaction_Amount_Upper_Limit_CTTAUL("9F5C", "Cumulative Total Transaction Amount Upper Limit (CTTAUL)", "Visa proprietary data element specifying the maximum total amount of offline transactions in the designated currency or designated and secondary currency allowed for the card application before a transaction is declined after an online transaction is unable to be performed.", "Card", "n 12", "", "6", "6"),


    CARD_Available_Offline_Spending_Amount_AOSA("9F5D", "Available Offline Spending Amount (AOSA)", "Kernel 3 proprietary data element indicating the remaining amount available to be spent offline. The AOSA is a calculated field used to allow the reader to print or display the amount of offline spend that is available on the card.", "Card", "n 12", "", "6", "6"),

    CARD_Consecutive_Transaction_International_Upper_Limit_CTIUL("9F5E", "Consecutive Transaction International Upper Limit (CTIUL)", "", "Card", "binary", "", "var.", "var."),

    CARD_DS_Slot_Availability("9F5F", "DS Slot Availability", "Contains the Card indication, obtained in the response to the GET PROCESSING OPTIONS command, about the slot type(s) available for data storage.", "Card", "binary", "", "1", "1"),

    CARD_CVC3_Track1("9F60", "CVC3 (Track1)", "The CVC3 (Track1) is a 2-byte cryptogram returned by the Card in the response to the COMPUTE CRYPTOGRAPHIC CHECKSUM command.", "Card", "binary", "", "2", "2"),


    CARD_CVC3_Track2("9F61", "CVC3 (Track2)", "The CVC3 (Track2) is a 2-byte cryptogram returned by the Card in the response to the COMPUTE CRYPTOGRAPHIC CHECKSUM command.", "Card", "binary", "", "2", "2"),
    PCVC3_Track1("9F62", "PCVC3 (Track1)", "PCVC3(Track1) indicates to the Kernel the positions in the discretionary data field of the Track 1 Data where the CVC3 (Track1) digits must be copied.", "", "binary", "", "6", "6"),

    CARD_Offline_Counter_Initial_Value("9F63", "Offline Counter Initial Value", "", "Card", "binary", "", "var.", "var."),

    CARD_NATC_Track1("9F64", "NATC (Track1)", "The value of NATC(Track1) represents the number of digits of the Application Transaction Counter to be included in the discretionary data field of Track 1 Data.", "Card", "binary", "", "1", "1"),
    PCVC3_Track2("9F65", "PCVC3 (Track2)", "PCVC3(Track2) indicates to the Kernel the positions in the discretionary data field of the Track 2 Data where the CVC3 (Track2) digits must be copied.", "", "binary", "", "2", "2"),
    TERMINAL_Terminal_Transaction_Qualifiers_TTQ("9F66", "Terminal Transaction Qualifiers (TTQ)", "Indicates reader capabilities, requirements, and preferences to the card. TTQ byte 2 bits 8-7 are transient values, and reset to zero at the beginning of the transaction. All other TTQ bits are static values, and not modified based on transaction conditions. TTQ byte 3 bit 7 shall be set by the acquirer-merchant to 1b.", "Terminal", "binary 32", "", "4", "4"),

    CARD_MSD_Offset("9F67", "MSD Offset", "", "Card", "binary", "", "var.", "var."),

    CARD_Card_Additional_Processes("9F68", "Card Additional Processes", "", "Card", "binary", "", "var.", "var."),
    CARD_Card_Authentication_Related_Data("9F69", "Card Authentication Related Data", "Contains the fDDA Version Number, Card Unpredictable Number, and Card Transaction Qualifiers. For transactions where fDDA is performed, the Card Authentication Related Data is returned in the last record specified by the Application File Locator for that transaction.", "Card", "binary", "", "5", "16"),

    CARD_Unpredictable_Number_Numeric("9F6A", "Unpredictable Number (Numeric)", "Unpredictable number generated by the Kernel during a mag-stripe mode transaction. The Unpredictable Number (Numeric) is passed to the Card in the data field of the COMPUTE CRYPTOGRAPHIC CHECKSUM command.", "Card", "n 8", "", "4", "4"),

    CARD_Card_CVM_Limit("9F6B", "Card CVM Limit", "", "Card", "binary", "", "var.", "var."),

    CARD_Card_Transaction_Qualifiers_CTQ("9F6C", "Card Transaction Qualifiers (CTQ)", "In this version of the specification, used to indicate to the device the card CVM requirements, issuer preferences, and card capabilities.", "Card", "binary 16", "", "2", "2"),
    CARD_VLP_Reset_Threshold("9F6D", "VLP Reset Threshold", "", "Card", "binary", "", "var.", "var."),


    CARD_Third_Party_Data("9F6E", "Third Party Data", "The Third Party Data contains various information, possibly including information from a third party. If present in the Card, the Third Party Data must be returned in a file read using the READ RECORD command or in the File Control Information Template. 'Device Type' is present when the most significant bit of byte 1 of 'Unique Identifier' is set to 0b. In this case, the maximum length of 'Proprietary Data' is 26 bytes. Otherwise it is 28 bytes.", "Card", "binary", "", "5", "32"),


    CARD_DS_Slot_Management_Control("9F6F", "DS Slot Management Control", "Contains the Card indication, obtained in the response to the GET PROCESSING OPTIONS command, about the status of the slot containing data associated to the DS Requested Operator ID.", "Card", "binary", "", "1", "1"),
    Protected_Data_Envelope_1("9F70", "Protected Data Envelope 1", "The Protected Data Envelopes contain proprietary information from the issuer, payment system or third party. The Protected Data Envelope can be retrieved with the GET DATA command. Updating the Protected Data Envelope with the PUT DATA command requires secure messaging and is outside the scope of this specification.", "", "binary", "", "0", "192"),

    Protected_Data_Envelope_2("9F71", "Protected Data Envelope 2", "Same as Protected Data Envelope 1.", "", "binary", "", "0", "192"),

    Protected_Data_Envelope_3("9F72", "Protected Data Envelope 3", "Same as Protected Data Envelope 1.", "", "binary", "", "0", "192"),

    Protected_Data_Envelope_4("9F73", "Protected Data Envelope 4", "Same as Protected Data Envelope 1.", "", "binary", "", "0", "192"),

    Protected_Data_Envelope_5("9F74", "Protected Data Envelope 5", "Same as Protected Data Envelope 1.", "", "binary", "", "0", "192"),

    CARD_Unprotected_Data_Envelope_1("9F75", "Unprotected Data Envelope 1", "The Unprotected Data Envelopes contain proprietary information from the issuer, payment system or third party. Unprotected Data Envelopes can be retrieved with the GET DATA command and can be updated with the PUT DATA (CLA='80') command without secure messaging.", "Card", "binary", "", "0", "192"),

    CARD_Unprotected_Data_Envelope_2("9F76", "Unprotected Data Envelope 2", "Same as Unprotected Data Envelope 1.", "Card", "binary", "", "0", "192"),

    CARD_Unprotected_Data_Envelope_3("9F77", "Unprotected Data Envelope 3", "Same as Unprotected Data Envelope 1.", "Card", "binary", "", "0", "192"),
    CARD_Unprotected_Data_Envelope_4("9F78", "Unprotected Data Envelope 4", "Same as Unprotected Data Envelope 1.", "Card", "binary", "", "0", "192"),
    CARD_Unprotected_Data_Envelope_5("9F79", "Unprotected Data Envelope 5", "Same as Unprotected Data Envelope 1.", "Card", "binary", "", "0", "192"),


    TERMINAL_VLP_Terminal_Support_Indicator("9F7A", "VLP Terminal Support Indicator", "If present indicates offline and/or online support. If absent indicates online only support", "Terminal", "n 1", "", "1", "1"),
    TERMINAL_VLP_Terminal_Transaction_Limit("9F7B", "VLP Terminal Transaction Limit", "", "Terminal", "H", "", "", ""),
    CARD_Customer_Exclusive_Data_CED("9F7C", "Customer Exclusive Data (CED)", "Contains data for transmission to the issuer.", "Card", "binary", "", "var. up to 32", "var. up to 32"),

    CARD_DS_Summary_1("9F7D", "DS Summary 1", "Contains the Card indication, obtained in the response to the GET PROCESSING OPTIONS command, about either the stored summary associated with DS ODS Card if present, or about a default zero-filled summary if DS ODS Card is not present and DS Unpredictable Number is present.", "Card", "binary", "", "8", "16"),

    CARD_Mobile_Support_Indicator("9F7E", "Mobile Support Indicator", "The Mobile Support Indicator informs the Card that the Kernel supports extensions for mobile and requires on device cardholder verification.", "Card", "binary", "", "1", "1"),

    CARD_DS_Unpredictable_Number("9F7F", "DS Unpredictable Number", "Contains the Card challenge (random), obtained in the response to the GET PROCESSING OPTIONS command, to be used by the Terminal in the summary calculation when providing DS ODS Term.", "Card", "binary", "", "4", "4"),

    CARD_File_Control_Information_FCI_Proprietary_Template("A5", "File Control Information (FCI) Proprietary Template", "Identifies the data object proprietary to this specification in the FCI template according to ISO/IEC 7816-4", "Card", "variable", "'6F'", "var.", "var."),


    CARD_File_Control_Information_FCI_Issuer_Discretionary_Data("BF0C", "File Control Information (FCI) Issuer Discretionary Data", "Issuer discretionary part of the File Control Information Proprietary Template.", "Card", "variable", "'A5'", "0", "222"),


    CARD_Visa_Fleet_CDO("BF50", "Visa Fleet - CDO", "", "Card", "H", "", "", ""),
    DATA_EXCHANGE_Integrated_Data_Storage_Record_Update_Template("BF60", "Integrated Data Storage Record Update Template", "Part of the command data for the EXTENDED GET PROCESSING OPTIONS command. The IDS Record Update Template contains data to be updated in one or more IDS Records.", "Data Exchange", "binary", "", "var.", "var."),
    CARD_Card_issuer_action_code_decline("C3", "Card issuer action code -decline", "", "Card", "H", "", "", ""),
    CARD_Card_issuer_action_code_default("C4", "Card issuer action code -default", "", "Card", "H", "", "", ""),
    CARD_Card_issuer_action_code_online("C5", "Card issuer action code online", "", "Card", "H", "", "", ""),
    CARD_PIN_Try_Limit("C6", "PIN Try Limit", "", "Card", "H", "", "", ""),
    CARD_CDOL_1_Related_Data_Length("C7", "CDOL 1 Related Data Length", "", "Card", "H", "", "", ""),
    CARD_Card_risk_management_country_code("C8", "Card risk management country code", "", "Card", "H", "", "", ""),
    CARD_Card_risk_management_currency_code("C9", "Card risk management currency code", "", "Card", "H", "", "", ""),
    CARD_Lower_cummulative_offline_transaction_amount("CA", "Lower cummulative offline transaction amount", "", "Card", "H", "", "", ""),
    CARD_Upper_cumulative_offline_transaction_amount("CB", "Upper cumulative offline transaction amount", "", "Card", "H", "", "", ""),
    CARD_Card_Issuer_Action_Code_PayPass_Default("CD", "Card Issuer Action Code (PayPass) – Default", "", "Card", "H", "", "3", "3"),
    CARD_Card_Issuer_Action_Code_PayPass_Online("CE", "Card Issuer Action Code (PayPass) – Online", "", "Card", "H", "", "3", "3"),
    CARD_Card_Issuer_Action_Code_PayPass_Decline("CF", "Card Issuer Action Code (PayPass) – Decline", "", "Card", "H", "", "3", "3"),
    CARD_Currency_conversion_table("D1", "Currency conversion table", "", "Card", "H", "", "", ""),
    CARD_Integrated_Data_Storage_Directory_IDSD("D2", "Integrated Data Storage Directory (IDSD)", "Directory of Integrated Data Storage records on the card.", "Card", "binary", "", "var.", "var."),
    CARD_Additional_check_table("D3", "Additional check table", "", "Card", "H", "", "", ""),
    CARD_Application_Control("D5", "Application Control", "", "Card", "H", "", "", ""),
    CARD_Default_ARPC_response_code("D6", "Default ARPC response code", "", "Card", "H", "", "", ""),
    CARD_Application_Control_PayPass("D7", "Application Control (PayPass)", "", "Card", "H", "", "3", "3"),
    CARD_AIP_PayPass("D8", "AIP (PayPass)", "", "Card", "H", "", "2", "2"),
    CARD_AFL_PayPass("D9", "AFL (PayPass)", "", "Card", "H", "", "12", "16"),
    CARD_Static_CVC3_TRACK1("DA", "Static CVC3-TRACK1", "", "Card", "H", "", "2", "2"),
    CARD_Static_CVC3_TRACK2("DB", "Static CVC3-TRACK2", "", "Card", "H", "", "2", "2"),
    CARD_IVCVC3_TRACK1("DC", "IVCVC3-TRACK1", "", "Card", "H", "", "2", "2"),
    CARD_IVCVC3_TRACK2("DD", "IVCVC3-TRACK2", "", "Card", "H", "", "2", "2"),
    CARD_Encrypted_PIN_Block_in_Tag_9F62_ISO_95641_Format_0("DF01", "Encrypted PIN Block in Tag 9F62 – ISO 95641 Format 0", "", "Card", "H", "", "", ""),
    CARD_PEK_Version_Number("DF02", "PEK Version Number", "", "Card", "H", "", "", ""),
    CARD_PIN_Try_Limit_DF03("DF03", "PIN Try Limit", "", "Card", "H", "", "", ""),
    CARD_PIN_Try_Counter_VSDC_Application("DF04", "PIN Try Counter (VSDC Application)", "", "Card", "H", "", "", ""),
    CARD_AIP_For_VISA_Contactless("DF05", "AIP - For VISA Contactless", "", "Card", "H", "", "", ""),
    CARD_Products_permitted("DF06", "Products permitted", "", "Card", "H", "", "", ""),
    CARD_Offline_checks_mandated("DF07", "Offline checks mandated", "", "Card", "H", "", "", ""),
    CARD_UDKmac("DF08", "UDKmac", "", "Card", "H", "", "", ""),
    CARD_UDKenc("DF09", "UDKenc", "", "Card", "H", "", "", ""),
    CARD_Retries_Permitted_Limit("DF0B", "Retries Permitted Limit", "", "Card", "H", "", "", ""),
    CARD_Script_Message_Update("DF0C", "Script Message Update", "", "Card", "H", "", "", ""),
    CARD_Fleet_Issuer_Action_Code_Default("DF0D", "Fleet Issuer Action Code - Default", "", "Card", "H", "", "", ""),
    CARD_Fleet_Issuer_Action_Code_Denial("DF0E", "Fleet Issuer Action Code - Denial", "", "Card", "H", "", "", ""),
    CARD_Fleet_Issuer_Action_Code_Online("DF0F", "Fleet Issuer Action Code - Online", "", "Card", "H", "", "", ""),
    CARD_Vehicle_Registration_Number("DF12", "Vehicle Registration Number", "", "Card", "A", "", "", ""),
    CARD_DDA_Public_Modulus("DF13", "DDA Public Modulus", "", "Card", "H", "", "", ""),
    CARD_Driver_Name("DF14", "Driver Name", "", "Card", "A", "", "", ""),
    CARD_Driver_ID("DF15", "Driver ID", "", "Card", "A", "", "", ""),
    CARD_Max_Fill_Volume("DF16", "Max Fill Volume", "", "Card", "H", "", "", ""),
    CARD_DDA_Public_Modulus_Length("DF17", "DDA Public Modulus Length", "", "Card", "H", "", "", ""),
    CARD_Mileage("DF18", "Mileage", "", "Card", "H", "", "", ""),
    CARD_Issuer_Proprietary_Bitmap_IPB("DF20", "Issuer Proprietary Bitmap (IPB)", "", "Card", "H", "", "", ""),
    CARD_Internet_Authentication_Flag_IAF("DF21", "Internet Authentication Flag (IAF)", "", "Card", "H", "", "", ""),
    CARD_Encrypted_PEK_RFU("DF22", "Encrypted PEK - RFU", "", "Card", "H", "", "", ""),
    CARD_PEK_Key_Check_Value_RFU("DF23", "PEK Key Check Value - RFU", "", "Card", "H", "", "", ""),
    CARD_MDK_Key_derivation_Index("DF24", "MDK - Key derivation Index", "", "Card", "H", "", "", ""),
    CARD_VISA_DPA_MDK_Key_derivation_Index("DF25", "VISA DPA – MDK - Key derivation Index", "", "Card", "H", "", "", ""),
    CARD_Encrypted_PIN_Block_ISO_9564_1_Format_1_PIN_Block_Thales_P3_Format_05("DF26", "Encrypted PIN Block – ISO 9564-1 Format 1 PIN Block (Thales P3 Format 05)", "", "Card", "H", "", "", ""),
    CARD_qVSDC_AIP("DF40", "qVSDC AIP", "", "Card", "H", "", "", ""),
    CARD_VSDC_AIP("DF41", "VSDC AIP", "", "Card", "H", "", "", ""),
    CARD_UDKac("DF42", "UDKac", "", "Card", "H", "", "", ""),
    CARD_UDKmac_DF43("DF43", "UDKmac", "", "Card", "H", "", "", ""),
    CARD_UDKenc_DF44("DF44", "UDKenc", "", "Card", "H", "", "", ""),
    CARD_UDKcvc("DF47", "UDKcvc", "", "Card", "H", "", "", ""),
    CARD_UDKac_KCV("DF48", "UDKac KCV", "", "Card", "H", "", "", ""),
    CARD_UDKmac_KCV("DF49", "UDKmac KCV", "", "Card", "H", "", "", ""),
    CARD_UDKenc_KCV("DF4A", "UDKenc KCV", "", "Card", "H", "", "", ""),
    CARD_UDKcvc_KCV("DF4B", "UDKcvc KCV", "", "Card", "H", "", "", ""),

    CARD_Grand_Parent_AC("DF51", "Grand Parent AC", "", "Card", "H", "", "", ""),
    CARD_Parent_AC("DF52", "Parent AC", "", "Card", "H", "", "", ""),
    CARD_Grand_Parent_MAC("DF53", "Grand Parent MAC", "", "Card", "H", "", "", ""),
    CARD_Parent_MAC("DF54", "Parent MAC", "", "Card", "H", "", "", ""),
    CARD_Grand_Parent_ENC("DF55", "Grand Parent ENC", "", "Card", "H", "", "", ""),
    CARD_TERMINAL_Parent_ENC_Terminal_Action_Code_Default("DF56", "Parent ENC/Terminal Action Code - Default", "", "Card/Terminal", "H", "", "", ""),
    TERMINAL_Terminal_Action_Code_Decline("DF57", "Terminal Action Code - Decline", "", "Terminal", "H", "", "", ""),
    CARD_DS_Input_Card("DF60", "DS Input (Card)", "Contains Terminal provided data if permanent data storage in the Card was applicable (DS Slot Management Control[8]=1b), remains applicable, or becomes applicable (DS ODS Info[8]=1b). Otherwise this data item is a filler to be supplied by the Kernel. The data is forwarded to the Card with the GENERATE AC command, as per DSDOL formatting.", "Card", "binary", "", "8", "8"),

    CARD_DDA_Component_Q("DF61", "DDA Component Q", "", "Card", "H", "", "", ""),

    CARD_DS_ODS_Info("DF62", "DS ODS Info", "Contains Terminal provided data to be forwarded to the Card with the GENERATE AC command, as per DSDOL formatting.", "Card", "binary", "", "1", "1"),

    CARD_DDA_Component_D2("DF63", "DDA Component D2", "", "Card", "H", "", "", ""),

    CARD_DDA_Component_Q_Minus_1_Mod_P("DF64", "DDA Component Q Minus 1 Mod P", "", "Card", "H", "", "", ""),
    CARD_DDA_Private_Exponent("DF65", "DDA Private Exponent", "", "Card", "H", "", "", ""),
    CARD_Paypass_Contactless("DF6B", "Paypass Contactless", "", "Card", "H", "", "", ""),
    CARD_Dynamic_Data_Authentication_Keys("DF79", "Dynamic Data Authentication Keys", "", "Card", "H", "", "", ""),
    CARD_DS_Summary_2("DF8101", "DS Summary 2", "This data allows the Kernel to check the consistency between DS Summary 1 and DS Summary 2, and so to ensure that DS ODS Card is provided by a genuine Card. It is located in the ICC Dynamic Data recovered from the Signed Dynamic Application Data.", "Card", "binary", "", "8", "16"),
    CARD_DS_Summary_3("DF8102", "DS Summary 3", "This data allows the Kernel to check whether the Card has seen the same transaction data as were sent by the Terminal/Kernel. It is located in the ICC Dynamic Data recovered from the Signed Dynamic Application Data.", "Card", "binary", "", "8", "16"),
    Balance_Read_Before_Gen_AC("DF8104", "Balance Read Before Gen AC", "The presence of Balance Read Before Gen AC in the TLV Database is an indication to the Kernel to read the offline balance from the Card before the GENERATE AC command. The Kernel stores the offline balance read from the Card in Balance Read Before Gen AC.", "", "n 12", "", "6", "6"),
    Balance_Read_After_Gen_AC("DF8105", "Balance Read After Gen AC", "The presence of Balance Read After Gen AC in the TLV Database is an indication to the Kernel to read the offline balance from the Card after the GENERATE AC command. The Kernel stores the offline balance read from the Card in Balance Read After Gen AC.", "", "n 12", "", "6", "6"),
    CARD_Data_Needed("DF8106", "Data Needed", "List of tags included in the DEK signal to request information from the Terminal.", "Card", "binary", "", "var.", "var."),
    CARD_CDOL1_Related_Data("DF8107", "CDOL1 Related Data", "Command data field of the GENERATE AC command, coded according to CDOL1.", "Card", "binary", "", "var.", "var."),
    CARD_DS_AC_Type("DF8108", "DS AC Type", "Contains the AC type indicated by the Terminal for which IDS data must be stored in the Card.", "Card", "binary", "", "1", "1"),
    CARD_DS_Input_Term("DF8109", "DS Input (Term)", "Contains Terminal provided data if permanent data storage in the Card was applicable (DS Slot Management Control[8]=1b), remains applicable or becomes applicable (DS ODS Info[8]=1b). DS Input (Term) is used by the Kernel as input to calculate DS Digest H.", "Card", "binary", "", "8", "8"),
    CARD_DS_ODS_Info_For_Reader("DF810A", "DS ODS Info For Reader", "Contains instructions from the Terminal on how to proceed with the transaction if:", "Card", "binary", "", "1", "1"),


    CARD_DS_Summary_Status("DF810B", "DS Summary Status", "Information reported by the Kernel to the Terminal about:", "Card", "binary", "", "1", "1"),


    Kernel_ID("DF810C", "Kernel ID", "Contains a value that uniquely identifies each Kernel. There is one occurrence of this data object for each Kernel in the Reader.", "", "binary", "", "1", "1"),
    CARD_DSVN_Term("DF810D", "DSVN Term", "Integrated data storage support by the Kernel depends on the presence of this data object. If it is absent, or is present with a length of zero, integrated data storage is not supported. Its value is '02' for this version of data storage functionality. This variable length data item has an initial byte that defines the maximum version number supported by the Terminal and a variable number of subsequent bytes that define how the Terminal supports earlier versions of the specification. As this is the first version, no legacy support is described and no additional bytes are present.", "Card", "binary", "", "var.", "var."),
    Post_Gen_AC_Put_Data_Status("DF810E", "Post-Gen AC Put Data Status", "Information reported by the Kernel to the Terminal, about the processing of PUT DATA commands after processing the GENERATE AC command. Possible values are 'completed' or 'not completed'. In the latter case, this status is not specific about which of the PUT DATA commands failed, or about how many of these commands have failed or succeeded. This data object is part of the Discretionary Data provided by the Kernel to the Terminal.", "", "binary", "", "1", "1"),
    Pre_Gen_AC_Put_Data_Status("DF810F", "Pre-Gen AC Put Data Status", "Information reported by the Kernel to the Terminal, about the processing of PUT DATA commands before sending the GENERATE AC command. Possible values are 'completed' or 'not completed'. In the latter case, this status is not specific about which of the PUT DATA commands failed, or about how many of these commands have failed or succeeded. This data object is part of the Discretionary Data provided by the Kernel to the Terminal.", "", "binary", "", "1", "1"),
    Proceed_To_First_Write_Flag("DF8110", "Proceed To First Write Flag", "Indicates that the Terminal will send no more requests to read data other than as indicated in Tags To Read. This data item indicates the point at which the Kernel shifts from the Card reading phase to the Card writing phase.", "", "binary", "", "1", "1"),


    PDOL_Related_Data("DF8111", "PDOL Related Data", "Command data field of the GET PROCESSING OPTIONS command, coded according to PDOL.", "", "binary", "", "var.", "var."),
    Tags_To_Read("DF8112", "Tags To Read", "List of tags indicating the data the Terminal has requested to be read. This data item is present if the Terminal wants any data back from the Card before the Data Record. This could be in the context of SDS, or for non data storage usage reasons, for example the PAN. This data item may contain configured data.", "", "binary", "", "var.", "var."),

    CARD_DRDOL_Related_Data("DF8113", "DRDOL Related Data", "Command data field of the RECOVER AC command, coded according to DRDOL.", "Card", "binary", "", "var.", "var."),
    Reference_Control_Parameter("DF8114", "Reference Control Parameter", "Working variable to store the reference control parameter of the GENERATE AC command.", "", "binary", "", "1", "1"),
    CARD_Error_Indication("DF8115", "Error Indication", "Contains information regarding the nature of the error that has been encountered during the transaction processing. This data object is part of the Discretionary Data.", "Card", "binary", "", "6", "6"),
    User_Interface_Request_Data("DF8116", "User Interface Request Data", "Combines all parameters to be sent with the MSG signal.", "", "binary", "", "22", "22"),
    CARD_Card_Data_Input_Capability("DF8117", "Card Data Input Capability", "Indicates the card data input capability of the Terminal and Reader. The Card Data Input Capability is coded according to Annex A.2 of [EMV Book 4].", "Card", "binary", "", "1", "1"),
    CARD_CVM_Capability_CVM_Required("DF8118", "CVM Capability – CVM Required", "Indicates the CVM capability of the Terminal and Reader when the transaction amount is greater than the Reader CVM Required Limit. The CVM Capability – CVM Required is coded according to Annex A.2 of [EMV Book 4].", "Card", "binary", "", "1", "1"),
    CARD_CVM_Capability_No_CVM_Required("DF8119", "CVM Capability – No CVM Required", "Indicates the CVM capability of the Terminal and Reader when the transaction amount is less than or equal to the Reader CVM Required Limit. The CVM Capability – No CVM Required is coded according to Annex A.2 of [EMV Book 4].", "Card", "binary", "", "1", "1"),
    CARD_Default_UDOL("DF811A", "Default UDOL", "The Default UDOL is the UDOL to be used for constructing the value field of the COMPUTE CRYPTOGRAPHIC CHECKSUM command if the UDOL in the Card is not present. The Default UDOL must contain as its only entry the tag and length of the Unpredictable Number (Numeric) and has the value: '9F6A04'.", "Card", "binary", "", "3", "3"),
    Kernel_Configuration("DF811B", "Kernel Configuration", "Indicates the Kernel configuration options.", "", "binary", "", "1", "1"),
    Max_Lifetime_of_Torn_Transaction_Log_Record("DF811C", "Max Lifetime of Torn Transaction Log Record", "Maximum time, in seconds, that a record can remain in the Torn Transaction Log.", "", "binary", "", "2", "2"),
    Max_Number_of_Torn_Transaction_Log_Records("DF811D", "Max Number of Torn Transaction Log Records", "Indicates the maximum number of records that can be stored in the Torn Transaction Log.", "", "binary", "", "1", "1"),
    Mag_stripe_CVM_Capability_CVM_Required("DF811E", "Mag-stripe CVM Capability – CVM Required", "Indicates the CVM capability of the Terminal/Reader in the case of a mag-stripe mode transaction when the Amount, Authorized (Numeric) is greater than the Reader CVM Required Limit.", "", "binary", "", "1", "1"),
    Security_Capability("DF811F", "Security Capability", "Indicates the security capability of the Kernel. The Security Capability is coded according to Annex A.2 of [EMV Book 4].", "", "binary", "", "1", "1"),
    Terminal_Action_Code_Default("DF8120", "Terminal Action Code – Default", "Specifies the acquirer's conditions that cause a transaction to be rejected on an offline only Terminal.", "", "binary", "", "5", "5"),
    Terminal_Action_Code_Denial("DF8121", "Terminal Action Code – Denial", "Specifies the acquirer's conditions that cause the denial of a transaction without attempting to go online.", "", "binary", "", "5", "5"),
    Terminal_Action_Code_Online("DF8122", "Terminal Action Code – Online", "Specifies the acquirer's conditions that cause a transaction to be transmitted online on an online capable Terminal.", "", "binary", "", "5", "5"),
    Reader_Contactless_Floor_Limit("DF8123", "Reader Contactless Floor Limit", "Indicates the transaction amount above which transactions must be authorized online.", "", "n 12", "", "6", "6"),
    Reader_Contactless_Transaction_Limit_No_On_device_CVM("DF8124", "Reader Contactless Transaction Limit (No On-device CVM)", "Indicates the transaction amount above which the transaction is not allowed, when on device cardholder verification is not supported.", "", "n 12", "", "6", "6"),
    Reader_Contactless_Transaction_Limit_On_device_CVM("DF8125", "Reader Contactless Transaction Limit (On-device CVM)", "Indicates the transaction amount above which the transaction is not allowed, when on device cardholder verification is supported.", "", "n 12", "", "6", "6"),
    Reader_CVM_Required_Limit("DF8126", "Reader CVM Required Limit", "Indicates the transaction amount above which the Kernel instantiates the CVM capabilities field in Terminal Capabilities with CVM Capability – CVM Required.", "", "n 12", "", "6", "6"),
    Time_Out_Value("DF8127", "Time Out Value", "Defines the time in ms before the timer generates a TIMEOUT signal.", "", "binary", "", "2", "2"),
    IDS_Status("DF8128", "IDS Status", "Indicates if the transaction performs an IDS read and/or write.", "", "binary", "", "1", "1"),
    Outcome_Parameter_Set("DF8129", "Outcome Parameter Set", "This data object is used to indicate to the Terminal the outcome of the transaction processing by the Kernel. Its value is an accumulation of results about applicable parts of the transaction.", "", "binary", "", "8", "8"),
    CARD_DD_Card_Track1("DF812A", "DD Card (Track1)", "If Track 1 Data is present, then DD Card (Track1) contains a copy of the discretionary data field of Track 1 Data as returned by the Card in the file read using the READ RECORD command during a mag-stripe mode transaction (i.e. without Unpredictable Number (Numeric), Application Transaction Counter, CVC3 (Track1) and nUN included).", "Card", "ans", "", "0", "56"),
    CARD_DD_Card_Track2("DF812B", "DD Card (Track2)", "DD Card (Track2) contains a copy of the discretionary data field of Track 2 Data as returned by the Card in the file read using the READ RECORD command during a mag-stripe mode transaction (i.e. without Unpredictable Number (Numeric), Application Transaction Counter, CVC3 (Track2) and nUN included).", "Card", "cn", "", "0", "8"),
    CARD_Mag_stripe_CVM_Capability_No_CVM_Required("DF812C", "Mag-stripe CVM Capability – No CVM Required", "Indicates the CVM capability of the Terminal/Reader in the case of a mag-stripe mode transaction when the Amount, Authorized (Numeric) is less than or equal to the Reader CVM Required Limit.", "Card", "binary", "", "1", "1"),
    Message_Hold_Time("DF812D", "Message Hold Time", "Indicates the default delay for the processing of the next MSG signal. The Message Hold Time is an integer in units of 100ms.", "", "n 6", "", "3", "3"),
    Hold_Time_Value("DF8130", "Hold Time Value", "Indicates the time that the field is to be turned off after the transaction is completed if requested to do so by the cardholder device. The Hold Time Value is in units of 100ms.", "", "binary", "", "1", "1"),
    Phone_Message_Table("DF8131", "Phone Message Table", "The Phone Message Table is a variable length list of entries of eight bytes each, and defines for the selected AID the message and status identifiers as a function of the POS Cardholder Interaction Information. Each entry in the Phone Message Table contains the fields shown in the table below.", "", "binary", "", "var.", "var."),

    CARD_Visa_International("FF60", "Visa International", "", "Card", "H", "", "", ""),
    CARD_Visa_Magnetic_Stripe("FF62", "Visa Magnetic Stripe", "", "Card", "H", "", "", ""),
    CARD_Visa_Quick_VSDC("FF63", "Visa Quick VSDC", "", "Card", "H", "", "", ""),
    Torn_Record("FF8101", "Torn Record", "A copy of a record from the Torn Transaction Log that is expired. Torn Record is sent to the Terminal as part of the Discretionary Data.", "", "binary", "", "var.", "var."),
    Tags_To_Write_Before_Gen_AC("FF8102", "Tags To Write Before Gen AC", "List of data objects indicating the Terminal data writing requests to be sent to the Card before processing the GENERATE AC command or the RECOVER AC command. This data object may be provided several times by the Terminal in a DET signal. Therefore, these values must be accumulated in Tags To Write Yet Before Gen AC buffer.", "", "binary", "", "var.", "var."),
    Tags_To_Write_After_Gen_AC("FF8103", "Tags To Write After Gen AC", "Contains the Terminal data writing requests to be sent to the Card after processing the GENERATE AC command or the RECOVER AC command. The value of this data object is composed of a series of TLVs. This data object may be provided several times by the Terminal in a DET signal. Therefore, these values must be accumulated in Tags To Write Yet After Gen AC.", "", "binary", "", "var.", "var."),
    CARD_Data_To_Send("FF8104", "Data To Send", "List of data objects that contains the accumulated data sent by the Kernel to the Terminal in a DEK signal. These data may correspond to Terminal reading requests, obtained from the Card by means of GET DATA or READ RECORD commands, or may correspond to data that the Kernel posts to the Terminal as part of its own processing.", "Card", "binary", "", "var.", "var."),
    CARD_Data_Record("FF8105", "Data Record", "The Data Record is a list of TLV encoded data objects returned with the Outcome Parameter Set on the completion of transaction processing.", "Card", "binary", "", "var.", "var."),
    CARD_Discretionary_Data("FF8106", "Discretionary Data", "The Discretionary Data is a list of Kernel-specific data objects sent to the Terminal as a separate field in the OUT signal.", "Card", "binary", "", "var.", "var.");
    private static HashMap<RecvTag, Emv41TagEnum> byTag;

    static {
        HashMap<RecvTag, Emv41TagEnum> localByTag = new HashMap<RecvTag, Emv41TagEnum>();
        for (Emv41TagEnum emv : Emv41TagEnum.values()) {
            localByTag.put(emv.tag, emv);
        }
        byTag = localByTag;
    }

    public final RecvTag tag;

    //    public String toString( byte[] value) {
//        return type.toString(value);
//    }
    //public final Emv41TypeEnum type;
    public final String name;

    Emv41TagEnum(String tag, String name, String Description, String Source, String Format, String Template, String min, String max) {
        this.tag = new RecvTag(tag);
        this.name = name;
    }

    public static Emv41TagEnum getByTag(RecvTag tag) {
        return byTag.get(tag);
    }

}
