/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.interfacefile;

import df.bean.db.conn.DBConnection;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.Statement;

import df.bean.db.conn.DBConn;
import df.bean.obj.util.*;

/**
 *
 * @author nopphadon
 */
public class ExportDFToBankBean extends InterfaceTextFileBean {
    private ResultSet rs;
    private Statement stm;
    private String payment_date = "";
    private String transaction_date = "";
    private String sum_dr_amount = "";
    private String message = "";
    private String yyyy;
    private String mm;
    private String date;

    @Override
    public boolean insertData(String fn, DBConnection d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public String getMessage(){
    	return this.message;
    }
    public void setPaymentDate(String s){
    	this.payment_date = s;
    }
    public void setTransactionDate(String s){
    	this.transaction_date = s;
    }
	private String getYyyy() {
		return yyyy;
	}
	private void setYyyy(String yyyy) {
		this.yyyy = yyyy;
	}
	private String getMm() {
		return mm;
	}
	private void setMm(String mm) {
		this.mm = mm;
	}

    @Override
    public boolean exportData(String fn, String hp, String type, String year, String month, DBConn d, String path) {
    	
        String qy = this.getOwnerBank();
        this.setYyyy(year);
        this.setMm(month);

        String dat_bay = "SELECT FILE_TYPE, RECORD_TYPE, BATCH_NUMBER, RECEIVING_BANK_CODE," + //0-3
        "RECEIVING_BRANCH_CODE, RECEIVING_ACCOUNT_NO, SENDING_BANK_CODE, SENDING_BRANCH_CODE," + //4-7
        "SENDING_ACCOUNT_NO, EFFECTIVE_DATE, SERVICE_TYPE, CLEARING_HOUSE_CODE,"+ //8-11
        "TRANSFER_AMOUNT, RECEIVER_INFORMATION, SENDER_INFORMATION, OTHER_INFORMATION,"+ //12-15
        "REFERENCE_RUNNING_NO, SPACE, COMPANY_CODE, SEQUENCE_NUMBER "+ //16-19
        "FROM BANK_TMB_MEDIA_CLEARING WHERE (BATCH_NO IS NULL OR BATCH_NO = '') AND "+
        "YYYY LIKE '"+year+"' AND MM LIKE '"+month+"' AND SERVICE_TYPE LIKE '"+type+"' AND "+
        "HOSPITAL_CODE LIKE '"+hp+"' AND RECEIVING_BANK_CODE = '"+qy+"'";
        
//        String dat_scb = getDataSCB(hp, year, month,  JDate.saveDate(this.payment_date));
//        String dat_scb = getSCBDataNew(hp, year, month,  JDate.saveDate(this.payment_date));
        String dat_scb = "EXEC [dbo].[getSCBData] @hosid = '"+hp+"', @year = '"+year+"',  @month = '"+month+"',@paymentDate = '"+JDate.saveDate(this.payment_date)+"'";
        System.out.println(dat_scb);

//        String dat_scb = "SELECT FILE_TYPE, RECORD_TYPE, BATCH_NUMBER, RECEIVING_BANK_CODE," + //0-3
//        "RECEIVING_BRANCH_CODE, RECEIVING_ACCOUNT_NO, SENDING_BANK_CODE, SENDING_BRANCH_CODE," + //4-7
//        "SENDING_ACCOUNT_NO, EFFECTIVE_DATE, SERVICE_TYPE, CLEARING_HOUSE_CODE,"+ //8-11
//        "CONVERT(INT,BANK_TMB_MEDIA_CLEARING.TRANSFER_AMOUNT) / 100.00 AS AMOUNT, " + //12
//       "RECEIVER_INFORMATION, SENDER_INFORMATION, OTHER_INFORMATION,"+ //13-15
//        "REFERENCE_RUNNING_NO, SPACE, COMPANY_CODE, SEQUENCE_NUMBER "+ //16-19
//        "FROM BANK_TMB_MEDIA_CLEARING WHERE (BATCH_NO IS NULL OR BATCH_NO = '') AND "+
//        "YYYY LIKE '"+year+"' AND MM LIKE '"+month+"' AND SERVICE_TYPE LIKE '"+type+"' AND "+
//        "HOSPITAL_CODE LIKE '"+hp+"' AND RECEIVING_BANK_CODE LIKE '"+qy+"'";

//        String dat_citi = "SELECT B.FILE_TYPE, B.RECORD_TYPE, B.BATCH_NUMBER, B.RECEIVING_BANK_CODE, " + //0-3
//        "CASE WHEN T.BANK_BRANCH_CODE = '' OR T.BANK_BRANCH_CODE IS NULL THEN " +
//        "B.RECEIVING_BRANCH_CODE ELSE T.BANK_BRANCH_CODE END AS RECEIVING_BRANCH_CODE, " +//4
//        "B.RECEIVING_ACCOUNT_NO, B.SENDING_BANK_CODE, B.SENDING_BRANCH_CODE, " + //5-7
//        "B.SENDING_ACCOUNT_NO, B.EFFECTIVE_DATE, B.SERVICE_TYPE, B.CLEARING_HOUSE_CODE, "+ //8-11
//        "CONVERT(INT,B.TRANSFER_AMOUNT) / 100.00 AS AMOUNT, B.RECEIVER_INFORMATION, B.SENDER_INFORMATION, " +//12-14
//        "B.OTHER_INFORMATION, B.REFERENCE_RUNNING_NO, SPACE, B.COMPANY_CODE, B.SEQUENCE_NUMBER, "+ //15-19
//        "H.CITI_PARTY_NAME, H.CITI_PARTY_ID, H.CITI_PARTY_AC, DR.EMAIL "+ //20-23
//        "FROM BANK_TMB_MEDIA_CLEARING B LEFT OUTER JOIN HOSPITAL H ON B.HOSPITAL_CODE = H.CODE " +
//        "LEFT OUTER JOIN DOCTOR DR ON B.DOCTOR_CODE = DR.CODE AND B.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
//        "LEFT OUTER JOIN TB_MAPPING_BANK_BRANCH T ON B.RECEIVING_BANK_CODE = T.BANK_CODE "+
//        "WHERE (B.BATCH_NO IS NULL OR B.BATCH_NO = '') AND "+
//        "B.YYYY LIKE '"+year+"' AND B.MM LIKE '"+month+"' AND B.SERVICE_TYPE LIKE '"+type+"' AND "+
//        "B.HOSPITAL_CODE LIKE '"+hp+"'";
//        
        
        /**
         * Mr.sarunyoo
		 * เธชเน�เธงเธ�เธ�เธตเน� เธญเธฒเธ�เธ�เธฐเธกเธตเธ�เธฒเธฃ FIX เธ�เน�เธญเธ�เธ�เน�เธฒเธ�เธกเธฒเธ�  เธกเธตเธ�เธฒเธฃ FIX  เธ�เน�เธฒเธ� SQL command 
         */
        String dat_citi = " SELECT "  +  
        				  " '10' AS FILE_TYPE , '2' AS RECORD_TYPE , '000001' AS BATCH_NUMBER , " +  	// 0-2
        				  " DR.BANK_CODE AS RECEIVING_BANK_CODE , " + // 3
        				  " ( CASE WHEN TMBB.BANK_BRANCH_CODE = '' OR TMBB.BANK_BRANCH_CODE IS NULL THEN DR.BANK_BRANCH_CODE ELSE TMBB.BANK_BRANCH_CODE END	) AS RECEIVING_BRANCH_CODE , " +  // 4
        				  " DR.BANK_ACCOUNT_NO AS RECEIVING_ACCOUNT_NO , H.BANK_CODE AS SENDING_BANK_CODE ,  "  +   // 5-6
        				  " '0000' AS SENDING_BRANCH_CODE , H.ACCOUNT_NO AS SENDING_ACCOUNT_NO , "  +  // 7-8
        				  " SUBSTRING(SP.PAYMENT_DATE , 7 ,8) + SUBSTRING(SP.PAYMENT_DATE , 5 ,2) + SUBSTRING(SP.PAYMENT_DATE , 1 ,4) AS EFFECTIVE_DATE , "  + //  9
        				  "  '04' AS SERVICE_TYPE , '00' AS CLEARING_HOUSE_CODE , SP.DR_NET_PAID_AMT AS AMOUNT , " + // 10 - 12
        				  " CASE WHEN DR.BANK_ACCOUNT_NAME = '' OR DR.BANK_ACCOUNT_NAME IS NULL THEN DR.NAME_THAI ELSE DR.BANK_ACCOUNT_NAME   END  AS RECEIVER_INFORMATION " +  // 13
        				  " ,  H.DESCRIPTION_THAI AS SENDER_INFORMATION , "  + // 14
        				  " '' AS OTHER_INFORMATION , 'RUNING NO' AS  REFERENCE_RUNNING_NO , " + //15 - 16
        				  " '' AS SPACE , '0515' AS  COMPANY_CODE , 'RUNING NO' AS SEQUENCE_NUMBER ,  " +  // 17 - 19 
        				  " H.CITI_PARTY_NAME , H.CITI_PARTY_ID , H.CITI_PARTY_AC , "  + // 20 - 22 
        				  " DR.EMAIL AS EMAIL " +  // 23 
        				  " FROM SUMMARY_PAYMENT SP " +
        				  " INNER JOIN DOCTOR DR ON SP.DOCTOR_CODE = DR.CODE AND SP.HOSPITAL_CODE = DR.HOSPITAL_CODE " + 
        				  " INNER JOIN HOSPITAL H ON SP.HOSPITAL_CODE = H.CODE  " +
        				  " LEFT OUTER JOIN TB_MAPPING_BANK_BRANCH TMBB ON DR.BANK_CODE = TMBB.BANK_CODE " +
        				  " WHERE SP.PAYMENT_DATE  = '"+ JDate.saveDate(this.payment_date) +"' " + 
        				  " AND SP.YYYY = '"+ this.yyyy +"' AND SP.MM = '" + this.mm + "'" + 
        				  " AND SP.HOSPITAL_CODE = '" + hp + "' AND SP.PAYMENT_MODE_CODE = 'B'" +
        				  " AND (SP.BATCH_NO IS NULL OR SP.BATCH_NO = '') " + 
        				  " AND SP.DR_NET_PAID_AMT > 0"+
        				  " ORDER BY DR.DOCTOR_PROFILE_CODE, DR.CODE DESC";
        
        String dat_tmb = "SELECT T.FILE_TYPE, T.RECORD_TYPE, T.BATCH_NUMBER, T.RECEIVING_BANK_CODE," + //0-3
        "T.RECEIVING_BRANCH_CODE, T.RECEIVING_ACCOUNT_NO, T.SENDING_BANK_CODE, T.SENDING_BRANCH_CODE," + //5-7
        "T.SENDING_ACCOUNT_NO, T.EFFECTIVE_DATE, T.SERVICE_TYPE, T.CLEARING_HOUSE_CODE,"+ //8-11
        "T.TRANSFER_AMOUNT, T.RECEIVER_INFORMATION, T.SENDER_INFORMATION, " + //12-14
        "T.OTHER_INFORMATION, T.REFERENCE_RUNNING_NO, " + //15-16
        "T.SPACE, T.COMPANY_CODE, T.SEQUENCE_NUMBER "+ //17-19
        "FROM BANK_TMB_MEDIA_CLEARING T WHERE (T.BATCH_NO IS NULL OR T.BATCH_NO = '') AND "+
        "T.YYYY LIKE '"+year+"' AND T.MM LIKE '"+month+"' AND T.SERVICE_TYPE LIKE '"+type+"' AND "+
        "T.HOSPITAL_CODE LIKE '"+hp+"' AND T.RECEIVING_BANK_CODE = '011'";
        
        String dat_other = "SELECT T.FILE_TYPE, T.RECORD_TYPE, T.BATCH_NUMBER, T.RECEIVING_BANK_CODE," + //0-3
        "T.RECEIVING_BRANCH_CODE, T.RECEIVING_ACCOUNT_NO, T.SENDING_BANK_CODE, T.SENDING_BRANCH_CODE," + //5-7
        "T.SENDING_ACCOUNT_NO, T.EFFECTIVE_DATE, T.SERVICE_TYPE, T.CLEARING_HOUSE_CODE,"+ //8-11
        "T.TRANSFER_AMOUNT, T.RECEIVER_INFORMATION, T.SENDER_INFORMATION, " + //12-14
        "T.OTHER_INFORMATION, T.REFERENCE_RUNNING_NO, " + //15-16
        "T.SPACE, T.COMPANY_CODE, T.SEQUENCE_NUMBER "+ //17-19
        "FROM BANK_TMB_MEDIA_CLEARING T WHERE (T.BATCH_NO IS NULL OR T.BATCH_NO = '') AND "+
        "T.YYYY LIKE '"+year+"' AND T.MM LIKE '"+month+"' AND T.SERVICE_TYPE LIKE '"+type+"' AND "+
        "T.HOSPITAL_CODE LIKE '"+hp+"' AND T.RECEIVING_BANK_CODE NOT LIKE '011'";

        String dat_sum = "SELECT sum(CONVERT(INT,BANK_TMB_MEDIA_CLEARING.TRANSFER_AMOUNT) / 100.00) AS AMOUNT " + //0
        "FROM BANK_TMB_MEDIA_CLEARING WHERE (BATCH_NO IS NULL OR BATCH_NO = '') AND "+
        "YYYY LIKE '"+year+"' AND MM LIKE '"+month+"' AND SERVICE_TYPE LIKE '"+type+"' AND "+
        "HOSPITAL_CODE LIKE '"+hp+"' AND RECEIVING_BANK_CODE like '"+qy+"'";
        
        if (this.getOwnerBank().equals("0025")){
        	//BAY Bank to Non BAY (SMART)
        	qy = "025";
        }
        
        String dat_smart_pattaya = "SELECT FILE_TYPE, RECORD_TYPE, BATCH_NUMBER, RECEIVING_BANK_CODE," + //0-3
        "RECEIVING_BRANCH_CODE, RECEIVING_ACCOUNT_NO, SENDING_BANK_CODE, SENDING_BRANCH_CODE," + //4-7
        "SENDING_ACCOUNT_NO, EFFECTIVE_DATE, SERVICE_TYPE, CLEARING_HOUSE_CODE,"+ //8-11
        "TRANSFER_AMOUNT, RECEIVER_INFORMATION, SENDER_INFORMATION, OTHER_INFORMATION,"+ //12-15
        "REFERENCE_RUNNING_NO, SPACE, COMPANY_CODE, SEQUENCE_NUMBER "+ //16-19
        "FROM BANK_TMB_MEDIA_CLEARING WHERE (BATCH_NO IS NULL OR BATCH_NO = '') AND "+
        "YYYY LIKE '"+year+"' AND MM LIKE '"+month+"' AND SERVICE_TYPE LIKE '"+type+"' AND "+
        "HOSPITAL_CODE LIKE '"+hp+"' AND RECEIVING_BANK_CODE NOT LIKE '"+qy+"'";
        
        boolean status = true;
        String[][] revenue_data = null;
        String[][] temp_data = null;
        String[][] temp_sum_amount = null;
        setFileName(path);//set filename read
        if(this.getOwnerBank().equals("014")){ 
        	//SCB Bank -----------------------
            /*if(Variables.IS_TEST){
                System.out.println("Get Data SCB : "+dat_sum);     	
            }
            temp_sum_amount = d.query(dat_sum);
        	sum_dr_amount = temp_sum_amount[0][0];*/
        	revenue_data = d.query(dat_scb);
        	writeFileNew(generateFileScbBank(revenue_data));
        	batEncrypt(path, fn.replace("\\", "\\\\"));
        }else if (this.getOwnerBank().equals("011")){
        	//TMB Bank ------------------------------
            if(Variables.IS_TEST){
                System.out.println("Get Data TMB : "+dat_tmb);     	
            }
        	revenue_data = d.query(dat_tmb);
            writeFileNew(bankTMB(revenue_data));
        }else if (this.getOwnerBank().equals("025")){
        	//BAY Bank ------------------------------
        	revenue_data = d.query(dat_bay);
        	writeFileNew(bankBAY(revenue_data));
        }else if (this.getOwnerBank().equals("017")){
        	
        	//CITI Bank -----------------------------
            if(Variables.IS_TEST){
                System.out.println("Get Data CITY : "+dat_citi);     	
            }
            
        	revenue_data = d.query(dat_citi);
            
        	if(revenue_data==null || revenue_data.equals(null) || revenue_data.length<1){
                this.message = "There is "+revenue_data+" Citi Bank data.";                	
            }
        	
        	writeFileNew(bankCITYBank(revenue_data));
        	
        }else if (this.getOwnerBank().equals("0011")){
        	//TMB Bank to Non TMB ------------------
            if(Variables.IS_TEST){
                System.out.println("Get Data UOB : "+dat_other);     	
            }
        	revenue_data = d.query(dat_other);
        	writeFileNew(bankTMB(revenue_data));
        }else{//0025
        	//BAY Bank to Non BAY (SMART)
            if(Variables.IS_TEST){
                System.out.println("Get Data BAY SMART : "+dat_smart_pattaya);     	
            }
        	revenue_data = d.query(dat_smart_pattaya);
        	writeFileNew(smartPay(revenue_data));
        }
        return status;
    }

	public boolean exportDataDirect(String fn, String hp_code, String type, String year, String month, DBConn d, String path,String transaction_date) {
		String TBank_Direct = "SELECT '' AS blank,'M' AS TERM_PAYMENT ,'062006' AS COMPANY_CODE ,"+//1-3
				"'VICHAIYUT' AS COMPANY_NAME,'D' AS APPLY_CODE,'A' AS STATUS ,"+//4-6
				"'"+transaction_date+"' EFFECTIVE_DATE,convert(nvarchar(255),COUNT(*)) AS TOTAL_RECORD,replace(convert(nvarchar(255),SUM(DR_NET_PAID_AMT)),'.','') AS TOTAL_AMOUNT ,"+//7-9
				"'I' AS MEDIA_TYPE, '5802410000' AS HASH_VALUE "+//10-11
				"FROM SUMMARY_PAYMENT P "+
				"INNER JOIN DOCTOR D ON D.CODE = P.DOCTOR_CODE AND D.HOSPITAL_CODE = P.HOSPITAL_CODE "+
				"LEFT JOIN BANK B ON B.CODE = D.BANK_CODE "+ 
				"WHERE P.HOSPITAL_CODE='"+hp_code+"' AND D.PAYMENT_MODE_CODE='B' AND B.CODE = '065' AND YYYY='"+year+"' AND MM='"+month+"' "+
				"AND P.PAYMENT_DATE='"+JDate.saveDate(this.payment_date)+"' P.DR_NET_PAID_AMT > 0 "+
				" UNION ALL "+
				"SELECT D.BANK_ACCOUNT_NO AS ACCOUNT_NO, '"+transaction_date+"' AS EFFECTIVE_DATE,'0' AS AMOUNT_SIGN ,"+//1-3
				"replace(convert(nvarchar(255),P.DR_NET_PAID_AMT),'.','') AS AMOUNT,ISNULL(D.BANK_ACCOUNT_NAME,'') AS NAME,'' AS STATUS_RETURN ,"+//4-6
				"'' AS blank,'','','',''"+//7
				" FROM SUMMARY_PAYMENT P "+
				"INNER JOIN DOCTOR D ON D.CODE = P.DOCTOR_CODE AND D.HOSPITAL_CODE = P.HOSPITAL_CODE "+
				"INNER JOIN HOSPITAL H ON H.CODE = P.HOSPITAL_CODE AND H.CODE='"+hp_code+"' "+
				"LEFT JOIN BANK B ON B.CODE = D.BANK_CODE "+
				"WHERE P.HOSPITAL_CODE='"+hp_code+"' AND D.PAYMENT_MODE_CODE='B' AND B.CODE ='065' AND YYYY='"+year+"' AND MM='"+month+"' "+
				"AND P.PAYMENT_DATE='"+JDate.saveDate(this.payment_date)+"' P.DR_NET_PAID_AMT > 0 ";
		System.out.println(payment_date);
		boolean status = true;
		String[][] revenue_data_direct= null;
		setFileName(path);
		System.out.println("Query Tbank direct "+TBank_Direct);
		revenue_data_direct = d.query(TBank_Direct);
		System.out.println(revenue_data_direct.toString());
		writeFileNew(TBANK(revenue_data_direct));
		return status;
	}
	public boolean exportDataSmart(String fn, String hp_code, String type, String year, String month, DBConn d, String path,String transaction_date,String com_code){
		this.setYyyy(year);
        this.setMm(month);
		String TBank_Smart = "SELECT '10' AS File_Type,'1' AS Record_Type,'000001' AS Batch_Number,"+//1-3
				"'065' AS  Sending_Bank_Code,'000' AS Number_of_Transfer_Orders,replace(convert(nvarchar(255),SUM(P.DR_NET_PAID_AMT)),'.','')  AS DR_NET_PAID_AMT,"+//4-6
				"'"+transaction_date+"' AS Effective_Date,'C' AS Kind_of_Transaction,' ' AS Blank,"+//6-9
				"convert(nvarchar(255),COUNT(*)) AS Total_Records,'"+com_code+"' AS Com_Code,'VICHAIYUT' AS Com_Name,"+ //10-12
				"' ' AS Blank2,'' AS Blank3,'','','','','','','0' AS Running_Number,'','',''"+//13
				"FROM SUMMARY_PAYMENT P "+
				"INNER JOIN DOCTOR D ON D.CODE = P.DOCTOR_CODE AND D.HOSPITAL_CODE = P.HOSPITAL_CODE "+
				"LEFT JOIN BANK B ON B.CODE = D.BANK_CODE "+
				"WHERE P.HOSPITAL_CODE='"+hp_code+"' AND D.PAYMENT_MODE_CODE='B' AND B.CODE <>'065'AND YYYY='"+year+"' AND MM='"+month+"' AND P.PAYMENT_DATE='"+JDate.saveDate(this.payment_date)+"' P.DR_NET_PAID_AMT > 0 "+
				// End of header
				" UNION ALL "+
				//start detail
				"SELECT '10' AS File_Type,'2' AS Record_Type,'000001' AS Batch_Number,"+//1-3
				"D.BANK_CODE AS Receiving_Bank_Code,D.BANK_BRANCH_CODE AS Receiving_Branch_Code,D.BANK_ACCOUNT_NO AS Receiving_Bank_AC_No,"+//4-6
				"H.BANK_CODE AS Sending_Bank_Code,H.BANK_BRANCH_CODE AS Sending_Branch_Code,H.ACCOUNT_NO AS Sending_Bank_AC_No,"+//7-9
				"'"+transaction_date+"' AS Effective_Date_Transfer,'01' AS Service_Type_Code,'00' AS Clearing_House_Code,"+//10-12
				"replace(convert(nvarchar(255),P.DR_NET_PAID_AMT),'.','') AS Transfer_Amount,'' AS Information_About_Receiver,'"+com_code+"' AS Com_Code,'VICHAIYUT' AS Com_Name,"+//13-15
				"'' AS Mobile_Phone_Receiver,"+//16
				"' ' AS Blank1,"+//17
				"'0000000000' AS Other_Information,ISNULL(D.BANK_ACCOUNT_NAME,'') AS Receiving_Bank_AC_Name,ROW_NUMBER() OVER(ORDER BY P.DOCTOR_CODE) AS Running_Number, "+//18-20
				"' ' AS Blank2,' ' AS Return_Code,' ' AS Blank3 "+//21-23
				"FROM SUMMARY_PAYMENT P "+
				"INNER JOIN DOCTOR D ON D.CODE = P.DOCTOR_CODE AND D.HOSPITAL_CODE = P.HOSPITAL_CODE "+
				"INNER JOIN HOSPITAL H ON H.CODE = P.HOSPITAL_CODE AND H.CODE='"+hp_code+"'" +
				"LEFT JOIN BANK B ON B.CODE = D.BANK_CODE "+
				"WHERE P.HOSPITAL_CODE='"+hp_code+"' AND D.PAYMENT_MODE_CODE='B' AND B.CODE <>'065'AND YYYY='"+year+"' AND MM='"+month+"' AND P.PAYMENT_DATE='"+JDate.saveDate(this.payment_date)+"' P.DR_NET_PAID_AMT > 0 ";
			boolean status = true;
			String[][] revenue_data = null;
			setFileName(path);
			System.out.println(TBank_Smart);
			revenue_data = d.query(TBank_Smart);
			writeFileNew(TBANK_SMART(revenue_data));
		System.out.print(TBANK_SMART(revenue_data));
		return status;
	}
	
    private String[] bankBAY(String[][] t){
    	String[] dt = new String[t.length + 1];
    	String dtBankApprove = this.payment_date.replaceAll("/", "");
    	long sum = 0;
    	String item_no = "";
    	for(int i = 1; i<=(t.length); i++){
    		sum = sum + Integer.parseInt(t[i-1][12]);
    		if(i==1){
    			item_no = String.valueOf(i) + this.getMm() + this.getYyyy().substring(2, 4);
    			//item_no = String.valueOf(i) + "" + "".substring(2, 4);
    		}
        	// DETAIL RECORD********************************************************
    		dt[i] = checkString( "000" ,3,"","b");//3 BRANCH CODE
    		dt[i] = dt[i] + checkString( "001" ,3,"","b");//3 COMPANY ID.
    		dt[i] = dt[i] + checkString( t[i-1][5].substring(1, t[i-1][5].length()) ,10,"","b");//10 ACCOUNT NO.
    		dt[i] = dt[i] + checkString( t[i-1][13].substring(0, ( (t[i-1][13].length()) > 20 ? 20 : t[i-1][13].length() )) ,20," ","b");//20 ACCOUNT NAME
    		dt[i] = dt[i] + checkString( t[i-1][12].substring(1, t[i-1][12].length()) ,11," ","f");//11 AMOUNT
    		dt[i] = dt[i] + checkString( t[i-1][19] ,5," ","b");//5 SEQUENCE NO.
    		dt[i] = dt[i] + checkString( "" ,21," ","b");//21 FILLER
    		dt[i] = dt[i] + checkString( String.valueOf(i) + this.getMm() + this.getYyyy().substring(2, 4) ,7,"0","f");//7 BATCH NO.
    		//dt[i] = dt[i] + checkString( String.valueOf(i) + "" + "".substring(2, 4) ,7,"0","f");//7 BATCH NO.
    		dt[i] = dt[i] + checkString( "" ,48," ","b");//48 FILLER       		
    	}
    	//dt[t.length+1] = "";
    	
    	// CONTROL RECORD********************************************************
    	dt[0] = checkString( "000" ,3,"","b");//3	BRANCH CODE
    	dt[0] = dt[0] + checkString( "001" ,3,"","b");//3	COMPANY ID.
    	dt[0] = dt[0] + checkString( dtBankApprove.substring(0, 4) + dtBankApprove.substring(6, 8) ,6,"","b");//6	POST DATE , t[1][9]
    	dt[0] = dt[0] + checkString( t[1][8].substring(1, t[1][8].length()) ,30," ","b");//30 COMPANY NAME / DEBIT ACCOUNT
    	dt[0] = dt[0] + checkString( "712" ,3,"","b");//3	TRANSACTION CODE
    	dt[0] = dt[0] + checkString( "" ,2," ","b");//2	FILLER
    	dt[0] = dt[0] + checkString( "" ,5," ","b");//5	SEQUENCE NO.
    	dt[0] = dt[0] + checkString( "" ,20," ","b");//20 FILLER
    	dt[0] = dt[0] + checkString( "A" ,1,"","b");//1 BATCH TYPE
    	dt[0] = dt[0] + checkString( item_no ,7,"0","f");//7 BATCH NO.
    	dt[0] = dt[0] + checkString( String.valueOf(t.length) ,7,"0","f");//7 ITEM NO.
    	dt[0] = dt[0] + checkString( String.valueOf(sum) ,15,"0","f");//15 TOTAL AMOUNT
    	dt[0] = dt[0] + checkString( "" ,26," ","b");//26 FILLER 	
    	return dt;
    }
    private String[] bankTMB(String[][] t){
    	String[] dt = new String[t.length];
    	for(int i = 0; i<t.length; i++){
            dt[i] = "TXN";  //Write Default Value of "File Type"
            dt[i] = dt[i]+checkString(t[i][14],120," ","b"); //---------------(02)Payor Name
            dt[i] = dt[i]+checkString(t[i][13],130," ","b"); //---------------(03)Receive Name
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(04)Mail to Name
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(05)Address 1
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(06)Address 2
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(07)Address 3
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(08)Address 4
            dt[i] = dt[i]+checkString("",10," ","b");        //---------------(09)Zip Code
            dt[i] = dt[i]+checkString("",16," ","b");        //---------------(10)Cust. Reference
            dt[i] = dt[i]+checkString(t[i][9],8," ","b");    //---------------(11)Effective Date
            dt[i] = dt[i]+checkString("",8," ","b");         //---------------(12)Pickup Date
            dt[i] = dt[i]+checkString("THB",3," ","b");      //---------------(13)Payment Currency
            dt[i] = dt[i]+checkString("",15," ","b");        //---------------(14)Comcode
            dt[i] = dt[i]+checkString("",15," ","b");        //---------------(15)Run Date
            dt[i] = dt[i]+checkString("",20," ","b");        //---------------(16)Vendor Code
            dt[i] = dt[i]+checkString(t[i][8],20," ","b");   //---------------(17)Debit Amount Number
            
            //String amount = ""+JNumber.setFormat(Double.parseDouble(t[i][12]) /100.00,"0.00");
            //dt[i] = dt[i]+checkString(amount,15," ","f");    //---------------(18)Payment Amount
            dt[i] = dt[i]+checkString(JNumber.getMoneyFormat(t[i][12]),15," ","f");//(18)Payment Amount
            dt[i] = dt[i]+checkString(t[i][3]+t[i][4],16," ","b");//----------(19)Bank+Branch Code
            dt[i] = dt[i]+checkString(t[i][5],20," ","b");   //---------------(20)Bank Account
            dt[i] = dt[i]+checkString(t[i][10],2," ","b");   //---------------(21)Pay type "Smart" 04
            dt[i] = dt[i]+checkString("00",2," ","b");       //---------------(22)Baht net "Default 00"
            dt[i] = dt[i]+checkString("",2," ","b");         //---------------(23)Delivery Method
            dt[i] = dt[i]+checkString("",20," ","b");        //---------------(24)Pay Check Location
            dt[i] = dt[i]+checkString("EMAIL",5," ","b");    //---------------(25)Advise Mode
            dt[i] = dt[i]+checkString("",50," ","b");        //---------------(26)Fax No.
            dt[i] = dt[i]+checkString("",50," ","b");        //---------------(27)Email Address
            dt[i] = dt[i]+checkString("",50," ","b");        //---------------(28)Mobile
            dt[i] = dt[i]+checkString("BEN",13," ","b");     //---------------(29)Fee Charge
            if(t[i][3].equals("011")){
            	dt[i] = dt[i]+checkString("DCB",3," ","b");  //---------------(30)Transaction Type
            }else{
            	dt[i] = dt[i]+checkString("MCL",3," ","b");  //---------------(30)Transaction Type
            }
            dt[i] = dt[i]+checkString("",5," ","b");         //---------------(31)Blank
            dt[i] = dt[i]+checkString("",4," ","b");         //---------------(32)Blank
            dt[i] = dt[i]+checkString("",30," ","b");        //---------------(33)Blank
            dt[i] = dt[i]+checkString("",105," ","b");       //---------------(34)Blank
            dt[i] = dt[i]+checkString("",70," ","b");        //---------------(35)Blank
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(36)Blank
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(37)Blank
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(38)Blank
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(39)Blank
            dt[i] = dt[i]+checkString("",16," ","b");        //---------------(40)Blank
            dt[i] = dt[i]+checkString("",7," ","b");         //---------------(41)Blank
            dt[i] = dt[i]+checkString("",2," ","b");         //---------------(42)Blank
            dt[i] = dt[i]+checkString("",40," ","b");        //---------------(43)Blank
            dt[i] = dt[i]+checkString("END",3," ","b");      //---------------(44)Blank
        }
    	return dt;
    }
    private String[] bankUOB(String[][] temp_data){
    	String[] sub_data = new String[temp_data.length]; 
        for(int i = 0; i<temp_data.length; i++){
            sub_data[i] = "102000001";  //Write Default Value of "File Type"
            
            for(int x = 0; x<temp_data[i].length; x++){
                //System.out.println(temp_data[i].length+" in column : "+x);
                if(x == 3){        //---------------------------------------Receive Bank Code
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],3," ","b");
                }else if(x == 4){  //---------------------------------------Receive Bank Branch
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],4,"0","f");
                }else if(x == 5){  //---------------------------------------Receive Account No.
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],11,"0","f");
                }else if(x == 6){  //---------------------------------------Send Bank Code
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],3," ","b");
                }else if(x == 7){  //---------------------------------------Send Bank Branch
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],4,"0","f");
                }else if(x == 8){  //---------------------------------------Send Account No.
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],11,"0","f");
                }else if(x == 9){  //---------------------------------------Effective Date
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],8," ","b");
                }else if(x == 10){  //--------------------------------------Service Type
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],2," ","b");
                }else if(x == 11){  //--------------------------------------Clearing House Code
                    sub_data[i] = sub_data[i] + "00";
                }else if(x == 12){  //--------------------------------------Transfer Amount
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],12,"0","f");
                }else if(x == 13){  //--------------------------------------Receive Information
                    //new String(sub_data[11].trim().getBytes(),"TIS-620")
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],60," ","b");
                }else if(x == 14){  //--------------------------------------Sender Information
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],60," ","b");
                }else if(x == 15){  //--------------------------------------Other Information
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],100," ","b");
                }else if(x == 16){  //--------------------------------------Running No.
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],6,"0","f");
                    //sub_data[i] = sub_data[i] + checkString(""+i,6,"0","f"); //Running from this method
                }else if(x == 17){  //--------------------------------------Space
                    sub_data[i] = sub_data[i] + checkString("",16," ","f");
                }else if(x == 18){  //--------------------------------------Company Code
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],4," ","b");
                }else if(x == 19){  //--------------------------------------Filler
                    sub_data[i] = sub_data[i] + checkString(temp_data[i][x],5,"0","f");
                }else{}
            }
        }
        return sub_data;
    }
    private String[] generateFileScbBank(String[][] temp_data2){
    	String[] sub_data = new String[temp_data2.length]; 
        for(int i = 0; i<(temp_data2.length); i++){
        	sub_data[i] = "";  //Write Default Value of "File Type"
//        	String[] temps = null;
//        	String keep_doubles = "";
//        	keep_doubles = ""+ temp_data2[i-1][12];
//          	temps = keep_doubles.split("[.]");
//          	if(temps.length>1){
//          		//System.out.println(keep_double);
//              	keep_doubles = temps[1];
//              	if(keep_doubles.length()<2){
//              		temps[1] = temps[1]+"0";
//              	}else if(keep_doubles.length()>2){
//              		temps[1] = keep_doubles.substring(0,2);
//              	}else{}
//              	keep_doubles = temps[0]+temps[1];
//              	//sum1 = Double.parseDouble(keep_double);
//          	}else{
//          	}
            for(int x = 0; x<temp_data2[i].length; x++){
//            	if(x == 5){  //---------------------------------------Account No.
//            		if(temp_data2[i-1][x].length()>10){
//                    sub_data[i] = sub_data[i] + checkString(temp_data2[i-1][x].substring(1, 11),10,"","b");
//            		}
//            		else{
//            			sub_data[i] = sub_data[i] + checkString(temp_data2[i-1][x],10,"","b");
//            		}		
//                }else if(x == 9){  //---------------------------------------Effective Date
//                    sub_data[i] = sub_data[i]+ checkString(temp_data2[i-1][9].substring(0,4)+temp_data2[i-1][9].substring(6,8),6,"0","f");
//                }else if(x == 11){  //--------------------------------------SIGN
//                    sub_data[i] = sub_data[i] + checkString("",1,"0","f");
//                }else if(x == 12){  //--------------------------------------Amount
//                	sub_data[i] = sub_data[i] + checkString(keep_doubles,9,"0","f");
//                	//System.out.println(keep_doubles);
//                }else if(x == 13){  //--------------------------------------SPACE
//                    sub_data[i] = sub_data[i] + checkString("",52," ","f");
//                }
//                else{}
            	if(x == 3 && !temp_data2[i][0].equals("002")){
            		sub_data[i] += "";
            	}
            	else if(x == 12){
            		sub_data[i] += "";
            	}
            	else{
            		sub_data[i] += temp_data2[i][x];
            	}
            }
            	
        }
//        String[] temp = null;
//        String keep_double = "";
//        keep_double = ""+ sum_dr_amount;
//    	temp = keep_double.split("[.]");
//    	if(temp.length>1){
//    		//System.out.println(keep_double);
//        	keep_double = temp[1];
//        	if(keep_double.length()<2){
//        		temp[1] = temp[1]+"0";
//        	}else if(keep_double.length()>2){
//        		temp[1] = keep_double.substring(0,2);
//        	}else{}
//        	keep_double = temp[0]+temp[1];
        	//sum1 = Double.parseDouble(keep_double);
//    	}else{
//    	}
    	
//        sub_data[0] = checkString("",9," ","f");//------------------------------------SPACE
//        sub_data[0]	= sub_data[0]+ checkString("",1,"M","b");//-----------------------TERM-PAYMENT
//        sub_data[0] = sub_data[0]+ checkString(temp_data2[0][18],6,"0","f"); //--------COMPANY-CODE
//        sub_data[0] = sub_data[0]+ checkString(temp_data2[0][14],30," ","b");//-------COMPANY-NAME
//        sub_data[0] = sub_data[0]+ checkString( "",1,"D","b");//----------------------APPLY-CODE
//        sub_data[0] = sub_data[0]+ checkString( "",1,"A","b");//----------------------STATUS
//        sub_data[0] = sub_data[0]+ checkString(temp_data2[0][9].substring(0,4)+temp_data2[0][9].substring(6,8),6,"0","f");//----------Effective Date
//        sub_data[0] = sub_data[0]+ checkString(String.valueOf(temp_data2.length),5,"0","f");//-- TOTAL-RECORD
//        sub_data[0] = sub_data[0]+ checkString( String.valueOf(keep_double),10,"0","f");//--TOTAL-AMOUNT
//        sub_data[0]	= sub_data[0]+ checkString("",1,"I","b");//-----------------------MEDIA-TYPE
//        sub_data[0] = sub_data[0]+ checkString("",8," ","f");//----------------------- SPACE
        
//        System.out.println("SCB lenght sub data: "+sub_data.length);
//        System.out.println(Arrays.toString(sub_data));
        return sub_data;
    }  
    private String[] bankCITYBank(String[][] t){
    	String[] dt = new String[t.length];
    	for(int i = 0; i<t.length; i++){
    		dt[i] = "";  //Write Default Value of "File Type"
    		  String[] temps = null;
              String keep_doubles = "";
              keep_doubles = ""+ t[i][12];
          	temps = keep_doubles.split("[.]");
          	if(temps.length>1){
          		//System.out.println(keep_double);
              	keep_doubles = temps[1];
              	if(keep_doubles.length()<2){
              		temps[1] = temps[1]+"0";
              	}else if(keep_doubles.length()>2){
              		temps[1] = keep_doubles.substring(0,2);
              	}else{}
              	keep_doubles = temps[0]+"."+temps[1];
              	//sum1 = Double.parseDouble(keep_double);
          	}else{
          	}
            dt[i] = dt[i]+checkString("PTA",3," ","b");//---------------(01)
            dt[i] = dt[i]+checkString("@",1,"","b");
            dt[i] = dt[i]+checkString("TH",2," ","b");//---------------(02)
            dt[i] = dt[i]+checkString("@",1,"","b");
            dt[i] = dt[i]+checkString(t[i][22],t[i][22].length()," ","b");//---------------(03)
            dt[i] = dt[i]+checkString("@",1,"","b");
            dt[i] = dt[i]+checkString("THB",3," ","b");//---------------(04)
            dt[i] = dt[i]+checkString("@",1,"","b");
            dt[i] = dt[i]+checkString(keep_doubles,keep_doubles.length()," ","b");//---------------(05)
            dt[i] = dt[i]+checkString("@@",2,"","b");//---------------(06)@06@
            dt[i] = dt[i]+checkString(t[i][9].substring(4)+t[i][9].substring(2,4)+t[i][9].substring(0,2),8," ","b");//---------------(07)
            dt[i] = dt[i]+checkString("@@@@@@",6,"","b");//---------------(08-12)@08@09@10@11@12@
            //"H.CITI_PARTY_NAME, H.CITI_PARTY_ID, H.CITI_DEBIT_AC "+ //20-22
            if(t[i][21].length()>34){
                dt[i] = dt[i]+checkString(t[i][21],34," ","b");//---------------(13)
            }else{
                dt[i] = dt[i]+checkString(t[i][21],t[i][21].length()," ","b");//---------------(13)
            }
            
            dt[i] = dt[i]+checkString("@",1,"","b");
            if(t[i][20].length()>35){
            	dt[i] = dt[i]+checkString(t[i][20],35," ","b");//--------------- (14)
            }else{
            	dt[i] = dt[i]+checkString(t[i][20],t[i][20].length()," ","b");//--------------- (14)            }
            }
            dt[i] = dt[i]+checkString("@@@@@@",6,"","b");//---------------(15-19)@15@16@17@18@19@
            dt[i] = dt[i]+checkString(t[i][13],t[i][13].length()," ","b");//---------------(20)
            dt[i] = dt[i]+checkString("@@@@@",5," ","b");      //---------------(21-24)@21@22@23@24@
            dt[i] = dt[i]+checkString(t[i][5],t[i][5].length() ,"0","f");//---------------(25)  เธ�เธญเธข
            dt[i] = dt[i]+checkString("@@@@@@@",7," ","b");//---------------(26-31)@26@27@28@29@30@31@
            dt[i] = dt[i]+checkString(t[i][3]+t[i][4],(t[i][3]+t[i][4]).length()," ","b");        //---------------(32)
            //---------------(33-70)@33@34@35@36@37@38@39@40@41@42@43@44@45@46@47@48@49@50@51@52@53@54@55@56@57@58@59@60@61@62@63@64@65@66@67@68@69@70@
            dt[i] = dt[i]+checkString("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",39," ","b");
            if(t[i][13].length()>35){
            	dt[i] = dt[i]+checkString(t[i][13],35," ","b");//--------------- (71)
            }else{
            	dt[i] = dt[i]+checkString(t[i][13],t[i][13].length()," ","b");//--------------- (71)            }
            }
            //dt[i] = dt[i]+checkString(t[i][13],35," ","b");//---------------(71)
            dt[i] = dt[i]+checkString("@@@@@",5," ","b");      //---------------(72-75)@72@73@74@75@
            if(t[i][23].length()>4){
                dt[i] = dt[i]+checkString(t[i][23],t[i][23].length()," ","b");//Email and Domain            	
            }else{
                dt[i] = dt[i]+checkString("",5,"","b");//EMAIL---------------(76)
                dt[i] = dt[i]+checkString("@",1,"","b");
                dt[i] = dt[i]+checkString("",6,"","b");//Domain---------------(77)
            }
            dt[i] = dt[i]+checkString("@@",2,"","b");//---------------(78)@78@
            dt[i] = dt[i]+checkString("04",2," ","b");//---------------(79)
            dt[i] = dt[i]+checkString("@@@@@@@@@@@@@@@@@@",18," ","b");      //---------------(80-96)@80@81@82@83@84@85@86@87@88@89@90@91@92@93@94@95@96@
            dt[i] = dt[i]+checkString(keep_doubles,keep_doubles.length()," ","b");//---------------(97)
            dt[i] = dt[i]+checkString("@@@@@@@@@@@@@@@@",16," ","b");      //---------------(98-113)@98@99@100@101@102@103@104@105@106@107@108@109@110@111@112@113
        }
    	return dt;
    }
    private String[] smartPay(String[][] t){
    	//fix for BPH
    	//String dtBankApprove = "10"+JDate.getMonth()+JDate.getYear() ;
    	String dtBankApprove = this.payment_date.replaceAll("/", "");
    	String[] dt = new String[t.length + 2];
    	long sum = 0;
    	for(int i = 1; i<(t.length+1); i++){ 
    		sum = sum + Integer.parseInt(t[i-1][12]);
        	// detail ********************************************************
	    	//2 เน�เธ�เน�เธกเธ�เน�เธญเธกเธนเธฅ IN-PUT เน�เธ�เน�เธซเธกเธฒเธขเน€เธฅเธ� "10" เธ•เธฅเธญเธ”
    		dt[i] = "10";
	    	//1 เธ�เธฃเธฐเน€เธ เธ—เธ�เน�เธญเธกเธนเธฅ (BATCH) เน�เธ�เน�เธซเธกเธฒเธขเน€เธฅเธ� "2" เธ•เธฅเธญเธ”
    		dt[i] = dt[i]+"2";
	    	//6 เธซเธกเธฒเธขเน€เธฅเธ�เธ�เธฃเธฐเธ�เธณ BATCH เน€เธฃเธดเน�เธกเธ•เธฑเน�เธ�เน�เธ•เน�เธซเธกเธฒเธขเน€เธฅเธ� 000001  เน€เธฃเธตเธขเธ�เธฅเธณเธ”เธฑเธ�เธ เธฒเธขเน�เธ•เน�เธงเธฑเธ�เธ—เธตเน�เธฃเธฒเธขเธ�เธฒเธฃเธกเธตเธ�เธฅเน€เธ”เธตเธขเธงเธ�เธฑเธ�
    		dt[i] = dt[i]+checkString( t[i-1][19] ,6,"0","f");
	    	//3 เธฃเธซเธฑเธชเธ�เธ�เธฒเธ�เธฒเธฃเธ—เธตเน�เน€เธ�เน�เธ�เธชเธกเธฒเธ�เธดเธ�เธฃเธฐเธ�เธ�Media Clearing (เน�เธ�เธฃเธ”เธ”เธนเน€เธญเธ�เธชเธฒเธฃเน�เธ�เธ� 1)
    		dt[i] = dt[i]+checkString( t[i-1][3] ,3,"0","f");
	    	//4 เธฃเธซเธฑเธชเธชเธฒเธ�เธฒเธ�เธ�เธฒเธ�เธฒเธฃเธ�เธนเน�เธฃเธฑเธ�เน�เธญเธ�(เธ•เน�เธญเธ�เน€เธ•เธดเธก "0" เน€เธ�เธดเน�เธกเธ�เน�เธฒเธ�เธซเธ�เน�เธฒเธฃเธซเธฑเธชเธชเธฒเธ�เธฒ) RECEIVING BANK CODE
    		dt[i] = dt[i]+checkString( t[i-1][4] ,4,"0","f");
	    	//11 เน€เธฅเธ�เธ—เธตเน�เธ�เธฑเธ�เธ�เธตเธ�เธ�เธฒเธ�เธฒเธฃเธ�เธนเน�เธฃเธฑเธ�เน�เธญเธ�(เธ•เน�เธญเธ�เน€เธ•เธดเธก "0" เน€เธ�เธดเน�เธกเธ�เน�เธฒเธ�เธซเธ�เน�เธฒเน€เธฅเธ�เธ—เธตเน�เธ�เธฑเธ�เธ�เธต)
    		dt[i] = dt[i]+checkString( t[i-1][5] ,11,"0","f");
	    	//3 เธฃเธซเธฑเธชเธ�เธ�เธฒเธ�เธฒเธฃเธ�เธนเน�เธชเน�เธ�เธฃเธฒเธขเธ�เธฒเธฃ เน�เธ�เน�เธซเธกเธฒเธขเน€เธฅเธ� "025" เธ•เธฅเธญเธ”
    		dt[i] = dt[i]+checkString( "025" ,3,"0","f");
	    	//4 เธฃเธซเธฑเธชเธชเธฒเธ�เธฒเธ�เธ�เธฒเธ�เธฒเธฃเธ�เธฃเธธเธ�เธจเธฃเธตเธฏ (เธ•เน�เธญเธ�เน€เธ•เธดเธก "0" เน€เธ�เธดเน�เธกเธ�เน�เธฒเธ�เธซเธ�เน�เธฒเธฃเธซเธฑเธชเธชเธฒเธ�เธฒ)
    		dt[i] = dt[i]+checkString( t[i-1][7] ,4,"0","f");
	    	//11 เน€เธฅเธ�เธ—เธตเน�เธ�เธฑเธ�เธ�เธตเธ�เธฃเธดเธฉเธฑเธ—เธ�เธนเน�เธชเน�เธ�เธฃเธฒเธขเธ�เธฒเธฃ(เธ•เน�เธญเธ�เน€เธ•เธดเธก "0" เน€เธ�เธดเน�เธกเธ�เน�เธฒเธ�เธซเธ�เน�เธฒเน€เธฅเธ�เธ—เธตเน�เธ�เธฑเธ�เธ�เธต)
    		dt[i] = dt[i]+checkString( t[i-1][8] ,11,"0","f");
	    	//8 เธงเธฑเธ�เธ—เธตเน�เธฃเธฒเธขเธ�เธฒเธฃเธกเธตเธ�เธฅ(เธงเธฑเธ�เน€เธ”เธทเธญเธ�เธ�เธต เธ�.เธจ.)
    		dt[i] = dt[i]+checkString( dtBankApprove ,8,"","f");//t[i-1][9]
	    	//2 เธฃเธซเธฑเธชเธ�เธฃเธฐเน€เธ เธ—เธฃเธฒเธขเธ�เธฒเธฃเน€เธ�เน�เธฒเธ�เธฑเธ�เธ�เธต(เน�เธ�เธฃเธ”เธ”เธนเน€เธญเธ�เธชเธฒเธฃเน�เธ�เธ� 2)
    		dt[i] = dt[i]+checkString( "01" ,2,"","b");
	    	//2 เธฃเธซเธฑเธชเธ�เธทเน�เธ�เธ—เธตเน�เน�เธ�เน€เธ�เธ•เธชเธณเธ�เธฑเธ�เน€เธ�เน�เธฒเธ�เธฑเธ�เธ�เธตเธ�เธฃเธธเธ�เน€เธ—เธ�เธซเธฃเธทเธญเธชเน�เธงเธ�เธ เธนเธกเธดเธ เธฒเธ� 00 = เธ�เธทเน�เธ�เธ—เธตเน�เน�เธ�เน€เธ�เธ•เธ�เธฃเธธเธ�เน€เธ—เธ�เน�เธฅเธฐ เธ�เธฃเธดเธกเธ“เธ‘เธฅ 01 = เธ�เธทเน�เธ�เธ—เธตเน�เน�เธ�เธชเน�เธงเธ�เธ เธนเธกเธดเธ เธฒเธ�
    		dt[i] = dt[i]+checkString( "01" ,2,"","b");
	    	//12 เธ�เธณเธ�เธงเธ�เน€เธ�เธดเธ�เธ—เธตเน�เธ•เน�เธญเธ�เธ�เธฒเธฃเน�เธญเธ�เน€เธ�เธดเธ� (10 เธ�เธณเธ�เธงเธ�เน€เธ•เน�เธก 2 เธ—เธจเธ�เธดเธขเธก )
    		dt[i] = dt[i]+checkString( t[i-1][12] ,12,"0","f");
	    	//60 เธ�เธทเน�เธญเธ�เธฑเธ�เธ�เธตเธ�เธนเน�เธฃเธฑเธ�เน�เธญเธ�
    		dt[i] = dt[i]+checkString( t[i-1][13] ,60," ","b");
	    	//60 เธ�เธทเน�เธญเธ�เธฃเธดเธฉเธฑเธ—เธ�เธนเน�เธชเน�เธ�เธฃเธฒเธขเธ�เธฒเธฃ
    		dt[i] = dt[i]+checkString( t[i-1][14] ,60," ","b");
	    	//10 เธ�เน�เธญเธกเธนเธฅเธญเน�เธฒเธ�เธญเธดเธ�เธญเธฑเธ�เน€เธ�เน�เธ�เธ�เธฃเธฐเน�เธขเธ�เธ�เน�เธ•เน�เธญเธ�เธฃเธดเธฉเธฑเธ—
    		dt[i] = dt[i]+checkString( "" ,10," ","b");
	    	//20 เธ�เน�เธญเธกเธนเธฅเธญเน�เธฒเธ�เธญเธดเธ�เธญเธฑเธ�เน€เธ�เน�เธ�เธ�เธฃเธฐเน�เธขเธ�เธ�เน�เธ•เน�เธญเธ�เธฃเธดเธฉเธฑเธ—
    		dt[i] = dt[i]+checkString( "" ,20," ","b");
	    	//8 เธ�เน�เธญเธกเธนเธฅเธญเน�เธฒเธ�เธญเธดเธ�เธญเธฑเธ�เน€เธ�เน�เธ�เธ�เธฃเธฐเน�เธขเธ�เธ�เน�เธ•เน�เธญเธ�เธฃเธดเธฉเธฑเธ—
    		dt[i] = dt[i]+checkString( "" ,8," ","b");
	    	//20 เธ�เน�เธญเธกเธนเธฅเธญเน�เธฒเธ�เธญเธดเธ�เธญเธฑเธ�เน€เธ�เน�เธ�เธ�เธฃเธฐเน�เธขเธ�เธ�เน�เธ•เน�เธญเธ�เธฃเธดเธฉเธฑเธ—
    		dt[i] = dt[i]+checkString( "" ,20," ","b");
	    	//20 เธ�เน�เธญเธกเธนเธฅเธญเน�เธฒเธ�เธญเธดเธ�เธญเธฑเธ�เน€เธ�เน�เธ�เธ�เธฃเธฐเน�เธขเธ�เธ�เน�เธ•เน�เธญเธ�เธฃเธดเธฉเธฑเธ—
    		dt[i] = dt[i]+checkString( "" ,20," ","b"); 
	    	//22 เธ�เน�เธญเธกเธนเธฅเธญเธฑเธ�เน€เธ�เน�เธ�เธ�เธฃเธฐเน�เธขเธ�เธ�เน�เธ•เน�เธญเธ�เธ�เธฒเธ�เธฒเธฃ(เธ�เน�เธญเธ�เธงเน�เธฒเธ�)
    		dt[i] = dt[i]+checkString( "" ,22," ","b");
	    	//6  เธฅเธณเธ”เธฑเธ�เธ—เธตเน�เธญเน�เธฒเธ�เธญเธดเธ�เธชเธณเธซเธฃเธฑเธ�เธ�เธ�เธฒเธ�เธฒเธฃ
    		dt[i] = dt[i]+checkString( "000000" ,6,"","b");
	    	//25 เธ�เน�เธญเธ�เธงเน�เธฒเธ�    	
    		dt[i] = dt[i]+checkString( "" ,25," ","b");
    	}
    	// header ********************************************************
    	//2 เน�เธ�เน�เธกเธ�เน�เธญเธกเธนเธฅ IN-PUT เน�เธ�เน�เธซเธกเธฒเธขเน€เธฅเธ� "10" เธ•เธฅเธญเธ”
    	dt[0] = checkString( "10" ,2,"","b");
    	//1 เธ�เธฃเธฐเน€เธ เธ—เธ�เน�เธญเธกเธนเธฅ (BATCH) เน�เธ�เน�เธซเธกเธฒเธขเน€เธฅเธ� "1" เธ•เธฅเธญเธ”
    	dt[0] = dt[0]+checkString( "1" ,1,"","b");
    	//6 เธซเธกเธฒเธขเน€เธฅเธ�เธ�เธฃเธฐเธ�เธณ BATCH เน€เธฃเธดเน�เธกเธ•เธฑเน�เธ�เน�เธ•เน�เธซเธกเธฒเธขเน€เธฅเธ� 000001 เน€เธฃเธตเธขเธ�เธฅเธณเธ”เธฑเธ�เธ เธฒเธขเน�เธ•เน�เธงเธฑเธ�เธ—เธตเน�เธฃเธฒเธขเธ�เธฒเธฃเธกเธตเธ�เธฅเน€เธ”เธตเธขเธงเธ�เธฑเธ�
    	dt[0] = dt[0]+checkString( "000001" ,6,"","b");
    	//3 เธฃเธซเธฑเธชเธ�เธ�เธฒเธ�เธฒเธฃเธ�เธนเน�เธชเน�เธ�เธฃเธฒเธขเธ�เธฒเธฃ เน�เธ�เน�เธซเธกเธฒเธขเน€เธฅเธ� "025" เธ•เธฅเธญเธ”
    	dt[0] = dt[0]+checkString( "025" ,3,"","b");
    	//10 เธขเธญเธ”เธฃเธงเธกเธ�เธณเธ�เธงเธ�เธฃเธฒเธขเธ�เธฒเธฃเธ�เน�เธญเธกเธนเธฅ เธ�เธถเน�เธ�เธกเธตเธชเธนเธ�เธชเธธเธ”เน�เธ”เน� 9, 999,999,999 เธฃเธฒเธขเธ�เธฒเธฃ เธ เธฒเธขเน�เธ•เน� BATCH NO. เน€เธ”เธตเธขเธงเธ�เธฑเธ�
    	dt[0] = dt[0]+checkString( String.valueOf(t.length) ,10,"0","f");
    	//15  เธขเธญเธ”เธฃเธงเธกเธ�เธณเธ�เธงเธ�เน€เธ�เธดเธ�เธ�เธญเธ�เธฃเธฒเธขเธ�เธฒเธฃเธ�เน�เธญเธกเธนเธฅเธ เธฒเธขเน�เธ•เน� BATCH NO. เน€เธ”เธตเธขเธงเธ�เธฑเธ� (13 เธ�เธณเธ�เธงเธ�เน€เธ•เน�เธก 2 เธ—เธจเธ�เธดเธขเธก)
    	dt[0] = dt[0]+checkString( String.valueOf(sum) ,15,"0","f");
    	//8 เธงเธฑเธ�เธ—เธตเน�เธฃเธฒเธขเธ�เธฒเธฃเธกเธตเธ�เธฅ(เธงเธฑเธ�เน€เธ”เธทเธญเธ�เธ�เธต เธ�.เธจ.)
    	dt[0] = dt[0]+checkString( dtBankApprove ,8," ","b");//t[0][9]
    	//1 เธ�เธฃเธฐเน€เธ เธ—เธ�เธญเธ�เธฃเธฒเธขเธ�เธฒเธฃเธ�เน�เธญเธกเธนเธฅ C= CREDIT TRANSFER
    	dt[0] = dt[0]+checkString( "C" ,1,"","b");
    	//274 เธ�เน�เธญเธ�เธงเน�เธฒเธ�
    	dt[0] = dt[0]+checkString( "" ,274," ","b");
    	
    	// footer ********************************************************
    	//2 เน�เธ�เน�เธกเธ�เน�เธญเธกเธนเธฅ IN-PUT เน�เธ�เน�เธซเธกเธฒเธขเน€เธฅเธ� "10" เธ•เธฅเธญเธ”
    	dt[t.length+1] = checkString( "10" ,2,"","b");
    	//1 เธ�เธฃเธฐเน€เธ เธ—เธ�เน�เธญเธกเธนเธฅ เน�เธ�เน�เธซเธกเธฒเธขเน€เธฅเธ� "3" เธ•เธฅเธญเธ”
    	dt[t.length+1] = dt[t.length+1]+checkString( "3" ,1,"","b");
    	//6 เธขเธญเธ”เธฃเธงเธก BATCH CONTROL เน�เธ�เน� "000001" เธ•เธฅเธญเธ”
    	dt[t.length+1] = dt[t.length+1]+checkString( "000001" ,6,"0","f");
    	//11 เธขเธญเธ”เธฃเธงเธกเธ�เธณเธ�เธงเธ�เธฃเธฒเธขเธ�เธฒเธฃเน�เธ� BATCH NO. เน€เธ”เธตเธขเธงเธ�เธฑเธ�
    	dt[t.length+1] = dt[t.length+1]+checkString( String.valueOf(t.length) ,11,"0","f");
    	//300 เธ�เน�เธญเธ�เธงเน�เธฒเธ�
    	dt[t.length+1] = dt[t.length+1]+checkString( "" ,300," ","b");
    	
    	return dt;
    }
	private String[] TBANK(String[][] t) {
		String[] dt = new String[t.length];
		String dtBankApprove = t[0][6].replaceAll("/", "");
		try {
			dt[0] = checkString("",8," ","b");//8 Space
			dt[0] = dt[0] + t[0][1];//1 TERM - PAYMENT
			dt[0] = dt[0] + t[0][2];//6 COMPANY - CODE
			dt[0] = dt[0] + checkString( t[0][3].substring(0, t[0][3].length()) ,30," ","b");//30 COMPANY - NAME
			dt[0] = dt[0] + t[0][4];//1 APPLY-CODE
			dt[0] = dt[0] + t[0][5];//1 STATUS
			dt[0] = dt[0] + checkString( dtBankApprove.substring(0, 4) + dtBankApprove.substring(6, 8),6,"","b");//6 EFFECTIVE-DATE
			dt[0] = dt[0] + checkString( t[0][7].substring(0, t[0][7].length()),5,"0","f");//5 TOTAL-RECORD
			dt[0] = dt[0] + checkString( t[0][8].substring(0, t[0][8].length()),11,"0","f");//11 TOTAL-AMOUNT
			dt[0] = dt[0] + t[0][9];//1 MEDIA-TYPE
			dt[0] = dt[0] + t[0][10];//10 HASH-VALUE
			for(int i=1;i<(t.length);i++) {
				dt[i] = t[i][0];//10 ACCOUNT NO.
				dt[i] = dt[i] + checkString( dtBankApprove.substring(0, 4) + dtBankApprove.substring(6, 8) ,6,"","b");//6 EFFECTIVE-DATE
				dt[i] = dt[i] + t[i][2];//1 AMOUNT-SIGN
				dt[i] = dt[i] + checkString( t[i][3].substring(0, t[i][3].length()),11,"0","f");//11 AMOUNT
				//dt[i] = dt[i] + checkString( t[i][4].substring(0, t[i][4].length()),30," ","l");//30 NAME
				try {
					dt[i] = dt[i] + checkString( new String(t[i][4].trim().getBytes(),"TIS-620").substring(0, t[i][4].length()),30," ","l");
				} catch (Exception e) {
					dt[i] = dt[i] + checkString( new String(t[i][4].trim().getBytes(),"TIS-620"),30," ","l");

					System.out.println("Write File Bank : "+e+"  "+t[i][4]);
				}//30 NAME
				
				dt[i] = dt[i] + checkString("",1," ","b");//1 STATUS-RETURN
				dt[i] = dt[i] + checkString("",21," ","b");//21 SPACE
			}
		}catch (Exception e) {
			System.out.println("TBANK in for : "+e);
		}
		
	return dt;
	}
	private String[] TBANK_SMART(String[][] t) {
		String dtBankApprove = t[0][6].replaceAll("/", "");
		String[] dt = new String[t.length];
		// header
		dt[0] = t[0][0];//2 File Type
		dt[0] = dt[0] + t[0][1];//1 Record Type
		dt[0] = dt[0] + t[0][2];//6 Batch Number
		dt[0] = dt[0] + t[0][3];//3 Sending Bank Code
		dt[0] = dt[0] + t[0][4];//3 Number of Transfer Orders in The Batch
		dt[0] = dt[0] + checkString( t[0][5].substring(0, t[0][5].length()) ,15,"0","f");//15 Grand Total of Amounts of Transfer Orders
		dt[0] = dt[0] + dtBankApprove;//8 Effective Date of Transfer Batch Number
		dt[0] = dt[0] + t[0][7];//1 Kind of Transaction
		dt[0] = dt[0] + checkString("",1," ","b");//1 Space
		dt[0] = dt[0] + checkString( t[0][9].substring(0, t[0][9].length()) ,6,"0","f");//6 Total Records
		dt[0] = dt[0] + t[0][10];//6 Company Code
		dt[0] = dt[0] + checkString( t[0][11].substring(0, t[0][11].length()) ,30," ","l");//30 Company Name
		dt[0] = dt[0] + checkString("",238," ","b");//238 Space
		for(int i = 1; i<(t.length); i++){// Detail
			dt[i] = t[i][0];//2 File Type
			dt[i] = dt[i] + t[i][1];//1 Record Type
    		dt[i] = dt[i] + t[i][2];//6 Batch Number
    		dt[i] = dt[i] + t[i][3];//3 Receiving Bank Code
    		dt[i] = dt[i] + t[i][4];//4 Receiving Branch Code
    		dt[i] = dt[i] + checkString( t[i][5].substring(0, t[i][5].length()) ,11,"0","f");//11 Receiving Bank's A/C No.
    		dt[i] = dt[i] + t[i][6];//3 Sending Bank Code
    		dt[i] = dt[i] + t[i][7];//4 Sending Branch Code
    		dt[i] = dt[i] + checkString( t[i][8].substring(0, t[i][8].length()) ,11,"0","f");//11 Sending Bank's A/C No.
    		dt[i] = dt[i] + dtBankApprove;//8 Effective Date of Transfer
    		dt[i] = dt[i] + t[i][10];//2 Service Type Code
    		dt[i] = dt[i] + t[i][11];//2 Clearing House Code
    		dt[i] = dt[i] + checkString( t[i][12].substring(0, t[i][12].length()) ,12,"0","f");//12 Transfer Amount
    		dt[i] = dt[i] + checkString("",60," ","b");//60 Information About The Receiver
    		dt[i] = dt[i] + t[i][14];//6 Information About The Sender Code
    		dt[i] = dt[i] + checkString( t[i][15].substring(0, t[i][15].length()) ,30," ","");//30 Information About The Sender Name
    		dt[i] = dt[i] + checkString("",10," ","b");//10 Mobile Phone of Receiver
    		dt[i] = dt[i] + checkString("",14," ","b");//14 Space
    		dt[i] = dt[i] + t[i][18];//10 Any Other Information
    		dt[i] = dt[i] + checkString( t[i][19].substring(0, t[i][19].length()) ,90," ","l");//90 Receiving Bank's A/C Name
    		dt[i] = dt[i] + checkString( t[i][20].substring(0, t[i][20].length()) ,6,"0","f");//6 Reference Running Number Given by Bank
    		dt[i] = dt[i] + checkString("",6," ","b");//6 Space
    		dt[i] = dt[i] + checkString("",2," ","b");//2 Return Code
    		dt[i] = dt[i] + checkString("",17," ","b");//17 Space	
		}
		return dt;
	}
    
    private void batEncrypt(String source, String target) {
        try {
            final File batchFile = new File("C:\\SCBHash\\HashModule\\SCBHashProgram.bat");
            final ProcessBuilder processBuilder = new ProcessBuilder(batchFile.getAbsolutePath(), source, target);
            final Process process = processBuilder.start();
            final int exitStatus = process.waitFor();
            System.out.println("Processed finished with status: " + exitStatus);

        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.toString());
        }
    }
	@Override
	public boolean exportData(String fn, String hp_code, String type, String year, String month, DBConn d, String path,
			String filing_type) {
		// TODO Auto-generated method stub
		return false;
	}
}
