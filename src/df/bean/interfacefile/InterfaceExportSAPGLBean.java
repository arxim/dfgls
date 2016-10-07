package df.bean.interfacefile;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;
import java.sql.ResultSet;

public class InterfaceExportSAPGLBean {
    private DBConnection dbconn;
    private DBConn conn;
    private String YYYY;
    private String MM;
    private String Hospital_code;
    private String TypeData;
    private int count_insert;

    public InterfaceExportSAPGLBean(String yyyy, String mm, String term, String hospital_code){
        conn = new DBConn();
        YYYY = yyyy;
        MM = mm;
        Hospital_code = hospital_code;
        try{
        	System.out.println("GL Process Class : "+this.getClass()+" term : "+term);
            conn.setStatement();
            //System.out.println(this.processAccu());
        }catch(Exception e){
            System.out.println(e);
        }
    }
    //<editor-fold default state="collapsed" description="Accrued">
    private String accuDfDeditNoGuanrantee(){//Cost DF
    	String stm = "";
    	stm =   "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE, PATIENT_DEPARTMENT_CODE," +
                "AMOUNT_AFT_DISCOUNT, ACCOUNT_CODE, RECEIPT_LOCATION, PATIENT_LOCATION, NATIONALITY_CODE," +
                "AMOUNT_SIGN, MM, YYYY, HOSPITAL_CODE, PROCESS) " +
                "SELECT T.LINE_NO+'|'+T.INVOICE_NO, T.DOCTOR_CODE, T.ADMISSION_TYPE_CODE, " +
                //Department
                "CASE WHEN (T.PATIENT_DEPARTMENT_CODE = '' OR T.PATIENT_DEPARTMENT_CODE IS NULL) " +
                "THEN T.DOCTOR_DEPARTMENT_CODE ELSE T.PATIENT_DEPARTMENT_CODE END AS PATIENT_DEPARTMENT_CODE, " +

                //Amount Aft Discount
                //"CASE WHEN T.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' " +
	            //"THEN " +
	            //	"CASE WHEN T.LINE_NO LIKE '%ADV' THEN T.GUARANTEE_PAID_AMT " +
	            //	"ELSE T.AMOUNT_AFT_DISCOUNT - T.GUARANTEE_PAID_AMT END " +
	            //"ELSE T.AMOUNT_AFT_DISCOUNT " +
	            //"END AS AMOUNT_AFT_DISCOUNT, "+
                "T.AMOUNT_AFT_DISCOUNT AS AMOUNT_AFT_DISCOUNT, "+

                "OI.ACCOUNT_CODE, T.RECEIPT_LOCATION_CODE, T.PATIENT_LOCATION_CODE,'TH','+' ,'"
	            +this.MM+"','"+this.YYYY+"','"+ this.Hospital_code +"', 'AC' " +
                "FROM TRN_DAILY T " +
                "LEFT OUTER JOIN DOCTOR D ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN ORDER_ITEM OI ON (T.ORDER_ITEM_CODE = OI.CODE AND T.HOSPITAL_CODE = OI.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN HOSPITAL H ON (T.HOSPITAL_CODE = H.CODE) "+
                "WHERE (T.BATCH_NO = '' OR T.BATCH_NO = '"+this.YYYY+this.MM+"') AND " +
                "OI.ACCOUNT_CODE NOT LIKE '0%' "+
                "AND T.AMOUNT_AFT_DISCOUNT > 0 AND T.DOCTOR_CODE NOT LIKE '99999%' "+
                "AND T.INVOICE_TYPE <> 'ORDER' AND D.ACTIVE = '1' AND T.ACTIVE = '1' "+
                "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' AND T.IS_PAID <> 'N' " +
                "AND T.ORDER_ITEM_ACTIVE = '1' AND D.DOCTOR_TYPE_CODE NOT LIKE 'G%' ";
    	//System.out.println("accuDfDeditNoGuanrantee Process : "+stm);
        return stm;
    }    
    private String accuDfDebitGuatantee(){//Cost DF Guarantee
    	String stm = "";
    	stm = 	"INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT, ACCOUNT_CODE,RECEIPT_LOCATION, PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT T.LINE_NO+'|'+T.INVOICE_NO, T.DOCTOR_CODE, T.ADMISSION_TYPE_CODE, " +
                //Department
                "CASE WHEN (T.PATIENT_DEPARTMENT_CODE = '' OR T.PATIENT_DEPARTMENT_CODE IS NULL) " +
        		"THEN T.DOCTOR_DEPARTMENT_CODE ELSE T.PATIENT_DEPARTMENT_CODE END AS PATIENT_DEPARTMENT_CODE, " +

        		//Amount Aft Discount
                //"CASE WHEN T.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' " +
	            //"THEN " +
	            //	"CASE WHEN T.LINE_NO LIKE '%ADV' THEN T.GUARANTEE_PAID_AMT " +
	            //	"ELSE T.AMOUNT_AFT_DISCOUNT - T.GUARANTEE_PAID_AMT END " +
	            //"ELSE T.AMOUNT_AFT_DISCOUNT " +
	            //"END AS AMOUNT_AFT_DISCOUNT, "+
                "T.AMOUNT_AFT_DISCOUNT AS AMOUNT_AFT_DISCOUNT, "+

                "H.EARNING_ACCOUNT AS ACCOUNT_CODE, T.RECEIPT_LOCATION_CODE, T.PATIENT_LOCATION_CODE,'TH', '+', " +
                "'"+this.MM+"', '"+this.YYYY+"', '"+ this.Hospital_code +"', 'AC' " +
                "FROM TRN_DAILY T " +
                "LEFT OUTER JOIN DOCTOR D ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN ORDER_ITEM OI ON (T.ORDER_ITEM_CODE = OI.CODE AND T.HOSPITAL_CODE = OI.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN HOSPITAL H ON (T.HOSPITAL_CODE = H.CODE) "+
                "WHERE (T.BATCH_NO = '' OR T.BATCH_NO = '"+this.YYYY+this.MM+"') AND " +
                "OI.ACCOUNT_CODE NOT LIKE '0%' "+
                "AND T.AMOUNT_AFT_DISCOUNT > 0 AND T.DOCTOR_CODE NOT LIKE '99999%' "+
                "AND T.INVOICE_TYPE <> 'ORDER' AND D.ACTIVE = '1' AND T.ACTIVE = '1' "+
                "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' AND T.IS_PAID <> 'N' "+
                "AND T.ORDER_ITEM_ACTIVE = '1' AND D.DOCTOR_TYPE_CODE LIKE 'G%'";
    	//System.out.println("accu602105DebitGuatantee Process : "+stm);
        return stm;
    }
    private String accuDfCreditSharing(){ //Sharing
    	String stm = "";
        stm = 	"INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE," +
                "AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN, MM, YYYY, HOSPITAL_CODE, PROCESS) " +
                "SELECT T.LINE_NO+'|'+T.INVOICE_NO, T.DOCTOR_CODE, T.ADMISSION_TYPE_CODE, " +
                //Department
                "CASE WHEN (T.PATIENT_DEPARTMENT_CODE = '' OR T.PATIENT_DEPARTMENT_CODE IS NULL) " +
            	"THEN T.DOCTOR_DEPARTMENT_CODE ELSE T.PATIENT_DEPARTMENT_CODE END AS PATIENT_DEPARTMENT_CODE, " +

	            //Sharing Amount
                //"CASE WHEN T.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' " +
	            //"THEN " +
	            //	"CASE WHEN T.LINE_NO LIKE '%ADV' THEN 0 " +
	            //	"ELSE (T.AMOUNT_AFT_DISCOUNT - T.DR_AMT) - T.GUARANTEE_PAID_AMT END " +
	            //"ELSE T.AMOUNT_AFT_DISCOUNT - T.DR_AMT " +
	            //"END AS AMOUNT_AFT_DISCOUNT, "+
            	"T.AMOUNT_AFT_DISCOUNT - T.DR_AMT AS AMOUNT_AFT_DISCOUNT, "+

                "H.SHARING_ACCOUNT AS ACCOUNT_CODE, T.RECEIPT_LOCATION_CODE, T.PATIENT_LOCATION_CODE,'TH','-','"
	            +this.MM+"','"+this.YYYY+"','"+ this.Hospital_code +"', 'AC' " +
                "FROM TRN_DAILY T " +
                "LEFT OUTER JOIN DOCTOR D ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN ORDER_ITEM OI ON (T.ORDER_ITEM_CODE = OI.CODE AND T.HOSPITAL_CODE = OI.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN HOSPITAL H ON T.HOSPITAL_CODE = H.CODE "+
                "WHERE (T.BATCH_NO = '' OR T.BATCH_NO = '"+this.YYYY+this.MM+"') AND " +
                "OI.ACCOUNT_CODE NOT LIKE '0%' "+
                "AND T.AMOUNT_AFT_DISCOUNT > 0 AND T.DOCTOR_CODE NOT LIKE '99999%' "+
                "AND T.INVOICE_TYPE <> 'ORDER' AND D.ACTIVE = '1' AND T.ACTIVE = '1' "+
                "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' AND T.IS_PAID <> 'N' "+
                "AND T.ORDER_ITEM_ACTIVE = '1'";
    	//System.out.println("accu602103Credit Process : "+stm);
        return stm;
    }
    private String accuDfCredit(){//Accrue DF
    	String stm = "";
    	stm = 	"INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE, PATIENT_DEPARTMENT_CODE," +
                "AMOUNT_AFT_DISCOUNT, ACCOUNT_CODE, RECEIPT_LOCATION, PATIENT_LOCATION, NATIONALITY_CODE," +
                "AMOUNT_SIGN, MM, YYYY, HOSPITAL_CODE, PROCESS) " +
                "SELECT T.LINE_NO+'|'+T.INVOICE_NO, T.DOCTOR_CODE, T.ADMISSION_TYPE_CODE, " +
                //Department
                "CASE WHEN (T.PATIENT_DEPARTMENT_CODE = '' OR T.PATIENT_DEPARTMENT_CODE IS NULL) " +
    			"THEN T.DOCTOR_DEPARTMENT_CODE ELSE T.PATIENT_DEPARTMENT_CODE END AS PATIENT_DEPARTMENT_CODE, " +
    			//DF Amount
	            "T.DR_AMT, H.AC_ACCOUNT_CODE AS ACCOUNT_CODE, " +
                "T.RECEIPT_LOCATION_CODE, T.PATIENT_LOCATION_CODE, " +
	            //"'', '', "+
                "'TH','-','"+this.MM+"','"+this.YYYY+"','"+ this.Hospital_code +"', 'AC' " +
                "FROM TRN_DAILY T " +
                "LEFT OUTER JOIN DOCTOR D ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN ORDER_ITEM OI ON (T.ORDER_ITEM_CODE = OI.CODE AND T.HOSPITAL_CODE = OI.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN HOSPITAL H ON T.HOSPITAL_CODE = H.CODE "+
                "WHERE (T.BATCH_NO = '' OR T.BATCH_NO = '"+this.YYYY+this.MM+"') AND " +
                "OI.ACCOUNT_CODE NOT LIKE '0%' "+
                "AND T.AMOUNT_AFT_DISCOUNT > 0 AND T.DOCTOR_CODE NOT LIKE '99999%' "+
                "AND T.INVOICE_TYPE <> 'ORDER' AND T.ACTIVE = '1' AND D.ACTIVE = '1' " +
                "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' AND T.IS_PAID <> 'N' "+
                "AND T.ORDER_ITEM_ACTIVE = '1'";
    	//System.out.println("accu203104Credit Process : "+stm);
        return stm;
    }
    private String accuCostExpense(){//Cost Adjust
    	String stm = "";
    	stm = 	"INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
               	",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
               	"AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
               	"SELECT T.LINE_NO, T.DOCTOR_CODE, 'O', " +
               	"CASE WHEN (T.DEPARTMENT_CODE = '' OR T.DEPARTMENT_CODE IS NULL) " +
               	"THEN D.DEPARTMENT_CODE ELSE T.DEPARTMENT_CODE END AS DEPARTMENT_CODE, " +
               	"T.AMOUNT, T.EXPENSE_ACCOUNT_CODE, T.LOCATION_CODE, T.LOCATION_CODE, 'TH', " +
               	"CASE WHEN T.EXPENSE_SIGN = '-1' THEN '%' ELSE '*' END AS EXPENSE_SIGN, "+
               	"'"+this.MM+"', " +
               	"'"+this.YYYY+"', " +
               	"'"+ this.Hospital_code +"', 'AC' " +
               	"FROM TRN_EXPENSE_DETAIL T " +
               	"LEFT OUTER JOIN DOCTOR D ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
               	"LEFT OUTER JOIN EXPENSE EX ON (T.EXPENSE_CODE = EX.CODE AND T.HOSPITAL_CODE = EX.HOSPITAL_CODE) "+
               	"WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               	"AND T.AMOUNT > 0 "+
               	"AND D.ACTIVE = '1' AND EX.AC_INTERFACE = 'Y' "+ //Update date 08/07/2009
               	"AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' ";
    	//System.out.println("accuExpense Process : "+stm);
        return stm;
    }
    private String accuExpense(){//Accrue Adjust
    	String stm = "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
               ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
               "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
               "SELECT T.LINE_NO, T.DOCTOR_CODE, 'O', " +
               "CASE WHEN (T.DEPARTMENT_CODE = '' OR T.DEPARTMENT_CODE IS NULL) " +
               "THEN D.DEPARTMENT_CODE ELSE T.DEPARTMENT_CODE END AS RECEIPT_LOCATION, " +
               "T.AMOUNT, H.AC_ACCOUNT_CODE, " +
               "T.LOCATION_CODE, T.LOCATION_CODE, " +
               "'TH', " +
               "CASE WHEN T.EXPENSE_SIGN = '-1' THEN '*' ELSE '%' END AS EXPENSE_SIGN, "+
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"', 'AC' " +
               "FROM TRN_EXPENSE_DETAIL T " +
               "LEFT OUTER JOIN DOCTOR D ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
               "LEFT OUTER JOIN EXPENSE EX ON (T.EXPENSE_CODE = EX.CODE AND T.HOSPITAL_CODE = EX.HOSPITAL_CODE) "+
               "LEFT OUTER JOIN HOSPITAL H ON T.HOSPITAL_CODE = H.CODE "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               "AND D.ACTIVE = '1' AND EX.AC_INTERFACE = 'Y' "+ //Update date 08/07/2009
               "AND T.AMOUNT > 0 "+
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' ";
    	//System.out.println("accu203104Expense Process : "+stm);
        return stm;
    }

    //<editor-fold default state="collapsed" description="Payment>
    private String glDfDebitNoGuatantee(){
    	return 	"INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE, PATIENT_DEPARTMENT_CODE, " +
                "AMOUNT_AFT_DISCOUNT, ACCOUNT_CODE, RECEIPT_LOCATION, PATIENT_LOCATION, NATIONALITY_CODE, " +
                "AMOUNT_SIGN, MM, YYYY, HOSPITAL_CODE, PROCESS) " +
                "SELECT SD.LINE_NO+'|'+SD.INVOICE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
                //Department
	            "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
	            "THEN SD.DOCTOR_DEPARTMENT_CODE ELSE SD.PATIENT_DEPARTMENT_CODE END AS PATIENT_DEPARTMENT_CODE, " +	            

	            //Amount Aft Discount
	            /*
                "CASE WHEN SD.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' " +
	            "THEN " +
	            	"CASE WHEN SD.LINE_NO LIKE '%ADV' THEN SD.GUARANTEE_PAID_AMT " +
	            	"ELSE SD.AMOUNT_AFT_DISCOUNT - SD.GUARANTEE_PAID_AMT END " +
	            "ELSE SD.AMOUNT_AFT_DISCOUNT " +
	            "END AS AMOUNT_AFT_DISCOUNT, "+
	            */
            	"SD.AMOUNT_AFT_DISCOUNT AS AMOUNT_AFT_DISCOUNT, "+

	            "OI.ACCOUNT_CODE, SD.RECEIPT_LOCATION_CODE, SD.PATIENT_LOCATION_CODE,'TH', '+' ," +
                "'"+this.MM+"', '"+this.YYYY+"', '"+ this.Hospital_code +"', 'GL' " +
                "FROM TRN_PAYMENT SD " +
                "LEFT OUTER JOIN DOCTOR DR ON (SD.DOCTOR_CODE = DR.CODE AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN ORDER_ITEM OI ON (SD.ORDER_ITEM_CODE = OI.CODE AND SD.HOSPITAL_CODE = OI.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN HOSPITAL H ON SD.HOSPITAL_CODE = H.CODE "+
                "WHERE SD.YYYY='"+YYYY+"' AND SD.MM='"+MM+"' AND INVOICE_TYPE <> 'ORDER' "+
                "AND SD.HOSPITAL_CODE = '"+this.Hospital_code+"' " +
                //"AND DR.DOCTOR_CATEGORY_CODE LIKE '%'"+
                //"AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y') "+
                //"AND SD.DOCTOR_CODE NOT LIKE '99999%' " +
                "AND DR.PAYMENT_MODE_CODE NOT IN ('U','') AND DR.PAYMENT_MODE_CODE IS NOT NULL " +
                "AND DR.IS_HOLD <> 'Y' AND DR.ACTIVE = '1' "+
                "AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1' AND OI.ACCOUNT_CODE NOT LIKE '0%' "+
                "AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != '' AND SD.AMOUNT_AFT_DISCOUNT > 0 "+
                "AND SD.IS_PAID = 'Y' AND DR.DOCTOR_TYPE_CODE NOT LIKE 'G%'";
    }
    private String glDfDebitGuarantee(){
        return 	"INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE, PATIENT_DEPARTMENT_CODE, " +
                "AMOUNT_AFT_DISCOUNT, ACCOUNT_CODE, RECEIPT_LOCATION, PATIENT_LOCATION, NATIONALITY_CODE, " +
                "AMOUNT_SIGN, MM, YYYY, HOSPITAL_CODE, PROCESS) " +
                "SELECT SD.LINE_NO+'|'+SD.INVOICE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +

                "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
        		"THEN SD.DOCTOR_DEPARTMENT_CODE ELSE SD.PATIENT_DEPARTMENT_CODE END AS PATIENT_DEPARTMENT_CODE, " +

        		//Amount Aft Discount
        		/*
                "CASE WHEN SD.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' " +
	            "THEN " +
	            	"CASE WHEN SD.LINE_NO LIKE '%ADV' THEN SD.GUARANTEE_PAID_AMT " +
	            	"ELSE SD.AMOUNT_AFT_DISCOUNT - SD.GUARANTEE_PAID_AMT END " +
	            "ELSE SD.AMOUNT_AFT_DISCOUNT " +
	            "END AS AMOUNT_AFT_DISCOUNT, "+
	            */
            	"SD.AMOUNT_AFT_DISCOUNT AS AMOUNT_AFT_DISCOUNT, "+

	            "H.EARNING_ACCOUNT AS ACCOUNT_CODE, SD.RECEIPT_LOCATION_CODE, SD.PATIENT_LOCATION_CODE,'TH', '+', " +
                "'"+this.MM+"', '"+this.YYYY+"', '"+ this.Hospital_code +"', 'GL' " +
 
                "FROM TRN_PAYMENT SD " +
                "LEFT OUTER JOIN DOCTOR DR ON (SD.DOCTOR_CODE = DR.CODE AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN ORDER_ITEM OI ON (SD.ORDER_ITEM_CODE = OI.CODE AND SD.HOSPITAL_CODE = OI.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN HOSPITAL H ON SD.HOSPITAL_CODE = H.CODE "+
                "WHERE SD.YYYY='"+YYYY+"' AND SD.MM='"+MM+"' AND INVOICE_TYPE <> 'ORDER' "+
                "AND SD.HOSPITAL_CODE = '"+this.Hospital_code+"' " +
                //"AND DR.DOCTOR_CATEGORY_CODE LIKE '%'"+
                //"AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y') "+
                //"AND SD.DOCTOR_CODE NOT LIKE '99999%' " +
                "AND DR.PAYMENT_MODE_CODE NOT IN ('U','') AND DR.PAYMENT_MODE_CODE IS NOT NULL " +
                "AND DR.IS_HOLD <> 'Y' AND DR.ACTIVE = '1' "+
                "AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1' AND OI.ACCOUNT_CODE NOT LIKE '0%' "+
                "AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != '' AND SD.AMOUNT_AFT_DISCOUNT > 0 "+
                "AND SD.IS_PAID = 'Y' AND DR.DOCTOR_TYPE_CODE LIKE 'G%'";
    }
    private String glDfCreditSharing(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE,ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT SD.LINE_NO+'|'+SD.INVOICE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
                //Department
                "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
            	"THEN SD.DOCTOR_DEPARTMENT_CODE ELSE SD.PATIENT_DEPARTMENT_CODE END AS PATIENT_DEPARTMENT_CODE, " +
				
				//Sharing Amount Exclude Chiengmai Change 
				/* 
				   "CASE WHEN SD.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' " + 
				"THEN " + 
				 "CASE WHEN SD.LINE_NO LIKE '%ADV' THEN 0 " + 
				 "ELSE SD.AMOUNT_AFT_DISCOUNT - (SD.DR_AMT+SD.GUARANTEE_PAID_AMT) END " + 
				"ELSE SD.AMOUNT_AFT_DISCOUNT - SD.DR_AMT " + 
				"END AS AMOUNT_AFT_DISCOUNT, "+ 
				*/ 
				"SD.AMOUNT_AFT_DISCOUNT - SD.DR_AMT AS AMOUNT_AFT_DISCOUNT, "+

	            "H.SHARING_ACCOUNT AS ACCOUNT_CODE, SD.RECEIPT_LOCATION_CODE, SD.PATIENT_LOCATION_CODE,'TH','-','"
	            +this.MM+"','"+this.YYYY+"','"+ this.Hospital_code +"', 'GL' " +
                "FROM TRN_PAYMENT SD " +
                "LEFT OUTER JOIN DOCTOR DR ON (SD.DOCTOR_CODE = DR.CODE AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN ORDER_ITEM OI ON (SD.ORDER_ITEM_CODE = OI.CODE AND SD.HOSPITAL_CODE = OI.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN HOSPITAL H ON SD.HOSPITAL_CODE = H.CODE "+
                "WHERE SD.YYYY='"+YYYY+"' AND SD.MM='"+MM+"' AND INVOICE_TYPE <> 'ORDER' "+
                "AND SD.HOSPITAL_CODE = '"+this.Hospital_code+"' " +
                //"AND DR.DOCTOR_CATEGORY_CODE LIKE '%'"+
                //"AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y') "+
                //"AND SD.DOCTOR_CODE NOT LIKE '99999%' " +
                "AND DR.PAYMENT_MODE_CODE NOT IN ('U','') AND DR.PAYMENT_MODE_CODE IS NOT NULL " +
                "AND DR.IS_HOLD <> 'Y' AND DR.ACTIVE = '1' "+
                "AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1' AND OI.ACCOUNT_CODE NOT LIKE '0%' "+
                "AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != '' AND SD.AMOUNT_AFT_DISCOUNT > 0 "+
                "AND SD.IS_PAID = 'Y'";
    }
    private String glDfCredit(){
        return 	"INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE, PATIENT_DEPARTMENT_CODE, " +
                "AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE, RECEIPT_LOCATION, PATIENT_LOCATION, NATIONALITY_CODE," +
                "AMOUNT_SIGN, MM, YYYY, HOSPITAL_CODE, PROCESS) " +
                "SELECT SD.LINE_NO+'|'+SD.INVOICE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
                //Department
                "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
    			"THEN SD.DOCTOR_DEPARTMENT_CODE ELSE SD.PATIENT_DEPARTMENT_CODE END AS PATIENT_DEPARTMENT_CODE, " +

    			//DF Amount
                "SD.DR_AMT, " +
                "H.GL_ACCOUNT_CODE AS ACCOUNT_CODE, SD.RECEIPT_LOCATION_CODE, SD.PATIENT_LOCATION_CODE,'TH','-', '"
	            +this.MM+"','"+this.YYYY+"','"+ this.Hospital_code +"', 'GL' " +
                "FROM TRN_PAYMENT SD " +
                "LEFT OUTER JOIN DOCTOR DR ON (SD.DOCTOR_CODE = DR.CODE AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN ORDER_ITEM OI ON (SD.ORDER_ITEM_CODE = OI.CODE AND SD.HOSPITAL_CODE = OI.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN HOSPITAL H ON SD.HOSPITAL_CODE = H.CODE "+
                "WHERE SD.YYYY='"+YYYY+"' AND SD.MM='"+MM+"' AND INVOICE_TYPE <> 'ORDER' "+
                "AND SD.HOSPITAL_CODE = '"+this.Hospital_code+"' " +
                //"AND DR.DOCTOR_CATEGORY_CODE LIKE '%'"+
                //"AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y') "+
                //"AND SD.DOCTOR_CODE NOT LIKE '99999%' " +
                "AND DR.PAYMENT_MODE_CODE NOT IN ('U','') AND DR.PAYMENT_MODE_CODE IS NOT NULL " +
                "AND DR.IS_HOLD <> 'Y' AND DR.ACTIVE = '1' "+
                "AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1' AND OI.ACCOUNT_CODE NOT LIKE '0%' "+
                "AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != '' AND SD.AMOUNT_AFT_DISCOUNT > 0 "+
                "AND SD.IS_PAID = 'Y'";
    }
    private String glCostExpense(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE, " +
               "AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE, " +
               "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
               "SELECT CASE WHEN EX.ADJUST_TYPE = 'CO' THEN 'COSAP|'+T.DOC_NO+'|'+T.DOC_DATE+'|'+"+
               "T.NOTE "+
               //"RTRIM(SUBSTRING(T.NOTE, CHARINDEX(':',T.NOTE)+1, CHARINDEX(' HN',T.NOTE)-CHARINDEX(':',T.NOTE))) "+
               "ELSE T.LINE_NO END AS LINE_NO, "+
               "T.DOCTOR_CODE, 'O', " +
               "CASE WHEN (T.DEPARTMENT_CODE = '' OR T.DEPARTMENT_CODE IS NULL) " +
               "THEN DR.DEPARTMENT_CODE ELSE T.DEPARTMENT_CODE END AS DEPARTMENT_CODE, " +
               "T.AMOUNT, T.EXPENSE_ACCOUNT_CODE, T.LOCATION_CODE, T.LOCATION_CODE, "+
               "CASE WHEN EX.ADJUST_TYPE = 'CO' THEN '00' ELSE 'TH' END, " +
               "CASE WHEN T.EXPENSE_SIGN = '-1' THEN '%' ELSE '*' END AS EXPENSE_SIGN, "+
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"','GL' " +
               "FROM TRN_EXPENSE_DETAIL T " +
               "LEFT OUTER JOIN DOCTOR DR ON (T.DOCTOR_CODE = DR.CODE AND T.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
               "LEFT OUTER JOIN EXPENSE EX ON (T.EXPENSE_CODE = EX.CODE AND T.HOSPITAL_CODE = EX.HOSPITAL_CODE) "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               "AND DR.PAYMENT_MODE_CODE NOT IN ('U','') AND DR.PAYMENT_MODE_CODE IS NOT NULL "+
               "AND DR.IS_HOLD <> 'Y' AND T.AMOUNT > 0 "+
               "AND DR.ACTIVE = '1' AND EX.GL_INTERFACE = 'Y' "+
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' ";
    }
    private String glExpense(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE, PATIENT_DEPARTMENT_CODE, " +
               "AMOUNT_AFT_DISCOUNT, ACCOUNT_CODE, RECEIPT_LOCATION, PATIENT_LOCATION, NATIONALITY_CODE, " +
               "AMOUNT_SIGN, MM, YYYY, HOSPITAL_CODE, PROCESS) " +
               "SELECT CASE WHEN EX.ADJUST_TYPE = 'CO' THEN 'COSAP|'+T.DOC_NO+'|'+T.DOC_DATE+'|'+"+
               "T.NOTE "+
               //"RTRIM(SUBSTRING(T.NOTE, CHARINDEX(':',T.NOTE)+1, CHARINDEX(' HN',T.NOTE)-CHARINDEX(':',T.NOTE))) "+
               "ELSE T.LINE_NO END AS LINE_NO, "+
               "T.DOCTOR_CODE, 'O', " +
               "CASE WHEN (T.DEPARTMENT_CODE = '' OR T.DEPARTMENT_CODE IS NULL) " +
               "THEN DR.DEPARTMENT_CODE ELSE T.DEPARTMENT_CODE END AS DEPARTMENT_CODE, " +
               "T.AMOUNT, H.GL_ACCOUNT_CODE, T.LOCATION_CODE, T.LOCATION_CODE, "+
               "CASE WHEN EX.ADJUST_TYPE = 'CO' THEN '00' ELSE 'TH' END, " +
               "CASE WHEN T.EXPENSE_SIGN = '-1' THEN '*' ELSE '%' END AS EXPENSE_SIGN, "+
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"','GL' " +
               "FROM TRN_EXPENSE_DETAIL T " +
               "LEFT OUTER JOIN DOCTOR DR ON (T.DOCTOR_CODE = DR.CODE AND T.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
               "LEFT OUTER JOIN EXPENSE EX ON (T.EXPENSE_CODE = EX.CODE AND T.HOSPITAL_CODE = EX.HOSPITAL_CODE) "+
               "LEFT OUTER JOIN HOSPITAL H ON T.HOSPITAL_CODE = H.CODE "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               "AND DR.PAYMENT_MODE_CODE <> 'U' AND DR.PAYMENT_MODE_CODE <> '' AND DR.PAYMENT_MODE_CODE IS NOT NULL "+
               "AND DR.IS_HOLD <> 'Y' AND T.AMOUNT > 0 "+
               "AND DR.ACTIVE = '1' AND EX.GL_INTERFACE = 'Y' "+ //Update date 13/10/2010
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' ";
    }

    public String processAccu(){
        String countdata[][] = null;
        this.TypeData = "AC";
        try{
            conn.setStatement();
            conn.insert("DELETE TEMP_GL WHERE YYYY='"+this.YYYY+"' AND HOSPITAL_CODE = '"+this.Hospital_code+"' AND PROCESS = 'AC'");
            System.out.println("Delete Temp");
            conn.insert("DELETE INT_ERP_GL WHERE TYPE='AC' AND YYYY='"+this.YYYY+"' AND MM='"+this.MM+"' AND HOSPITAL_CODE = '"+this.Hospital_code+"'");
            System.out.println("Delete Int ERP");
            //System.out.print(Variables.IS_TEST ? "\nDelete AC complete : "+this.Hospital_code : "");
            conn.insert(this.accuDfDeditNoGuanrantee());
            System.out.println("accuDfDeditNoGuanrantee");
            conn.insert(this.accuDfCreditSharing());
            System.out.println("accuDfCreditSharing");
            conn.insert(this.accuDfDebitGuatantee());
            System.out.println("accuDfDebitGuatantee");
            conn.insert(this.accuDfCredit());
            System.out.println("accuDfCredit");
            conn.insert(this.accuCostExpense());
            System.out.println("accuCostExpense");
            conn.insert(this.accuExpense());
            System.out.println("accuExpense");
            conn.commitDB();
            System.out.print(Variables.IS_TEST ? "\nCommit Process Accu : "+this.Hospital_code : "");
        }catch(Exception err){
            System.out.println("Process Accu : "+err);
            conn.rollDB();
        }finally{
            insertIntoIntErpGl();
            conn.closeDB("");
        }
        return countdata + "/" + count_insert;
    }
    public String processGlAccount(){
        String countdata[][] = null;
        this.TypeData = "GL";
        try{
            conn.setStatement();
            conn.insert("DELETE TEMP_GL WHERE YYYY='"+this.YYYY+"' AND HOSPITAL_CODE = '"+this.Hospital_code+"' AND PROCESS = 'GL'");
            conn.insert("DELETE INT_ERP_GL WHERE TYPE='GL' AND YYYY='"+this.YYYY+"' AND MM='"+this.MM+"' AND HOSPITAL_CODE = '"+this.Hospital_code+"'");
            conn.commitDB();
            System.out.println(this.glDfDebitNoGuatantee());
            conn.insert(this.glDfDebitNoGuatantee());
            System.out.println(this.glDfDebitGuarantee());
            conn.insert(this.glDfDebitGuarantee());
            conn.insert(this.glDfCreditSharing());
            conn.insert(this.glDfCredit());
            System.out.println(this.glCostExpense());
            conn.insert(this.glCostExpense());
            System.out.println(this.glExpense());
            conn.insert(this.glExpense());
            conn.commitDB();
            System.out.print(Variables.IS_TEST ? "\nCommit Process GL : "+this.Hospital_code : "");
        }catch(Exception err){
            System.out.println("Process GL : "+err);
            conn.rollDB();
        }
        insertIntoIntErpGl();
        conn.closeDB("");
        return countdata + "/" + count_insert;
    }
    private void insertIntoIntErpGl(){
        /*
        Hospital_Code:	10000
        Seq_No:	counter
        Accounting_DT:	11/2/2008
        Accounting_Time:	11.22.23
        Type:	AC/GL
        Account:	ACCOUNT_CODE
        Dept_ID:	PATIENT_DEPARTMENT_CODE
        Product:	ADMISSION TYPE CODE
        Monetary_Amount:	AMOUNT_AFT_DISCOUNT
        Class_Fld:	NATIONALITY_CODE
        Chartfield2:	RECEIPT_LOCATION
        Chartfield3:	PATIENT_LOCATION
         */
    	String Accounting_Time = JDate.getHour() + "." + JDate.getMinutes() + "." + JDate.getSeconds();
        String Accounting_DT = JDate.getEndMonthDate(this.YYYY, this.MM)+ "/" + this.MM + "/" + this.YYYY;
    	String account_date_gl = "10/" + this.MM + "/" + this.YYYY;
    	String[][] d = null;
    	
    	try{
            conn.setStatement();
            if(TypeData.equals("GL")){
            	Accounting_DT = "10/" + this.MM + "/" + this.YYYY;
        		d = conn.query("SELECT MAX(DISTINCT PAYMENT_DATE) FROM SUMMARY_PAYMENT WHERE HOSPITAL_CODE='"+this.Hospital_code+"' AND PAYMENT_TERM = '2'");
        		//d = conn.query("SELECT MAX(DISTINCT PAYMENT_TERM_DATE) FROM PAYMENT_MONTHLY WHERE HOSPITAL_CODE='"+this.Hospital_code+"'");
        		account_date_gl = d[0][0];
        		Accounting_DT = account_date_gl.substring(6, 8)+"/"+account_date_gl.substring(4, 6)+"/"+account_date_gl.substring(0, 4);
        	}
        }catch(Exception err){
            err.getMessage();
        }
        int Seq_No = 1;
        String sql = "SELECT HOSPITAL_CODE, ADMISSION_TYPE_CODE, PATIENT_DEPARTMENT_CODE, SUM(AMOUNT_AFT_DISCOUNT) as 'AMOUNT_AFT_DISCOUNT', " +
                "ACCOUNT_CODE, RECEIPT_LOCATION, PATIENT_LOCATION, NATIONALITY_CODE, AMOUNT_SIGN " +
                "FROM TEMP_GL WHERE ADMISSION_TYPE_CODE <>'U' AND MM='"+this.MM+"' AND YYYY='"+this.YYYY+"' " +
                "AND HOSPITAL_CODE='"+this.Hospital_code+"' AND PROCESS = '"+this.TypeData+"' "+
                "GROUP BY HOSPITAL_CODE, ADMISSION_TYPE_CODE, PATIENT_DEPARTMENT_CODE, " +
                "ACCOUNT_CODE, RECEIPT_LOCATION, PATIENT_LOCATION, NATIONALITY_CODE, AMOUNT_SIGN ";
        String sqlInsert = "";
        String Type = "";

        if(dbconn==null){
            dbconn = new DBConnection();
            dbconn.connectToLocal();
        }
        ResultSet rs = dbconn.executeQuery(sql);

        try{
            while(rs.next()){
                if("I".equalsIgnoreCase(rs.getString("ADMISSION_TYPE_CODE"))){
                    Type = "IPD";
                }else if("O".equalsIgnoreCase(rs.getString("ADMISSION_TYPE_CODE"))){
                    Type = "OPD";
                }else{
                    Type = "OPD";
                }

                sqlInsert = "INSERT INTO INT_ERP_GL(HOSPITAL_CODE, SEQ_NO, ACCOUNTING_DT, ACCOUNTING_TIME, " +
                        "TYPE, ACCOUNT, DEPT_ID, PRODUCT, MONETARY_AMOUNT, " +
                        "CLASS_FLD, CHARTFIELD2, CHARTFIELD3, YYYY, MM, AMOUNT_SIGN) " +
                        "VALUES('"+ rs.getString("HOSPITAL_CODE") +"','"+ Seq_No +"','"+ Accounting_DT +"','"+ Accounting_Time +"'," +
                        "'"+TypeData+"','"+rs.getString("ACCOUNT_CODE")+"','"+rs.getString("PATIENT_DEPARTMENT_CODE")+"','"+Type+"'," +
                        "'"+rs.getString("AMOUNT_AFT_DISCOUNT")+"','"+rs.getString("NATIONALITY_CODE")+"','"+rs.getString("RECEIPT_LOCATION")+"'," +
                        "'"+rs.getString("PATIENT_LOCATION")+"','"+YYYY+"','"+MM+"','"+rs.getString("AMOUNT_SIGN")+"'); ";

                try{
                    conn.insert(sqlInsert);
                    conn.commitDB();
                }catch(Exception err){
                    conn.rollDB();
                    System.out.println("Insert : "+err);
                    err.getMessage();
                }
                Seq_No++;
            }
        }catch(Exception err){
            System.out.println(err.getMessage());
        }finally{
           conn.closeDB("");
        }
        count_insert = Seq_No-1;
    }
}