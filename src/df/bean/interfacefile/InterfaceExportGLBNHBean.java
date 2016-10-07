package df.bean.interfacefile;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;
import java.sql.ResultSet;

public class InterfaceExportGLBNHBean {
    private DBConnection dbconn;
    private DBConn conn;
    private String YYYY;
    private String MM;
    private String Hospital_code;
    private String TypeData;
    private int count_insert;

    public InterfaceExportGLBNHBean(String yyyy, String mm, String hospital_code){
        conn = new DBConn();
        YYYY = yyyy;
        MM = mm;
        Hospital_code = hospital_code;
        try{
            conn.setStatement();
        }catch(Exception e){
            System.out.println(e);
        }
    }
  //<editor-fold default state="collapsed" description="Accrued">
    private String accuDfDeditNoGuanrantee(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE," +
                "AMOUNT_AFT_DISCOUNT, ACCOUNT_CODE, RECEIPT_LOCATION, PATIENT_LOCATION," +
                "NATIONALITY_CODE,AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT T.LINE_NO, T.DOCTOR_CODE, T.ADMISSION_TYPE_CODE, " +
                "CASE WHEN (T.PATIENT_DEPARTMENT_CODE = '' OR T.PATIENT_DEPARTMENT_CODE IS NULL) " +
                	"THEN T.DOCTOR_DEPARTMENT_CODE " +
                	"ELSE T.PATIENT_DEPARTMENT_CODE " +
                "END AS PATIENT_DEPARTMENT_CODE, " +
                
                "T.AMOUNT_AFT_DISCOUNT, " +
	            "OI.ACCOUNT_CODE,  " +
	            "'','','TH',"+
                "'+' ," +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"', 'AC' " +
                "FROM TRN_DAILY T " +
                "LEFT OUTER JOIN DOCTOR D ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN ORDER_ITEM OI ON (T.ORDER_ITEM_CODE = OI.CODE AND T.HOSPITAL_CODE = OI.HOSPITAL_CODE) "+
                "WHERE (T.BATCH_NO = '' OR T.BATCH_NO IS NULL) "+
                "AND T.INVOICE_TYPE <> 'ORDER' AND D.ACTIVE = '1' AND T.ACTIVE = '1' "+
                "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' AND T.IS_PAID <> 'N' " +
                "AND T.ORDER_ITEM_ACTIVE = '1' AND D.DOCTOR_TYPE_CODE NOT LIKE 'G%' ";
    }    
    private String accu602103Credit(){ //transaction hp_amt
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT T.LINE_NO, T.DOCTOR_CODE, T.ADMISSION_TYPE_CODE, " +
                "CASE WHEN (T.PATIENT_DEPARTMENT_CODE = '' OR T.PATIENT_DEPARTMENT_CODE IS NULL) " +
            		"THEN T.DOCTOR_DEPARTMENT_CODE " +
            		"ELSE T.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            
	            //"T.PATIENT_DEPARTMENT_CODE, " +
	            "CASE WHEN T.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' " +
	            	"THEN T.AMOUNT_AFT_DISCOUNT - (T.DR_AMT+T.GUARANTEE_PAID_AMT) " +
	            	"ELSE T.AMOUNT_AFT_DISCOUNT - T.DR_AMT END AS HP_AMT, "+
                //"T.HP_AMT, " +

                "602103 AS ACCOUNT_CODE,  " +
                "'','','TH',"+              
                "'-', " +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"', 'AC' " +
                "FROM TRN_DAILY T " +
                "LEFT OUTER JOIN DOCTOR D ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
                "WHERE (T.BATCH_NO = '' OR T.BATCH_NO IS NULL) "+
                "AND T.INVOICE_TYPE <> 'ORDER' AND D.ACTIVE = '1' AND T.ACTIVE = '1' "+
                "AND T.ORDER_ITEM_ACTIVE = '1' "+
                "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' AND T.IS_PAID <> 'N'";
    }
    private String accu602105DebitGuatantee(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT T.LINE_NO, T.DOCTOR_CODE, T.ADMISSION_TYPE_CODE, " +
                "CASE WHEN (T.PATIENT_DEPARTMENT_CODE = '' OR T.PATIENT_DEPARTMENT_CODE IS NULL) " +
        			"THEN T.DOCTOR_DEPARTMENT_CODE " +
        			"ELSE T.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +
	            /*nop comment 2011-02-07
	            "CASE WHEN T.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE' " +
	            	"THEN T.AMOUNT_AFT_DISCOUNT - T.GUARANTEE_PAID_AMT " +
	            	"ELSE T.AMOUNT_AFT_DISCOUNT END AS AMOUNT_AFT_DISCOUNT, "+
                */
                "T.AMOUNT_AFT_DISCOUNT, " +
                "602105 AS ACCOUNT_CODE,  " +
                "'','','TH',"+
                "'+', " +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"', 'AC' " +
                "FROM TRN_DAILY T LEFT OUTER JOIN DOCTOR D ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
                "WHERE (T.BATCH_NO = '' OR T.BATCH_NO IS NULL) "+
                "AND T.INVOICE_TYPE <> 'ORDER' AND D.ACTIVE = '1' AND T.ACTIVE = '1' "+
                "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' AND T.IS_PAID <> 'N' "+
                "AND T.ORDER_ITEM_ACTIVE = '1' " +
                "AND D.DOCTOR_TYPE_CODE LIKE 'G%'";
    } 
    private String accu203104Credit(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE," +
                "AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT T.LINE_NO, T.DOCTOR_CODE, T.ADMISSION_TYPE_CODE, " +
                
                "CASE WHEN (T.PATIENT_DEPARTMENT_CODE = '' OR T.PATIENT_DEPARTMENT_CODE IS NULL) " +
    				"THEN T.DOCTOR_DEPARTMENT_CODE " +
    				"ELSE T.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            
	            //"T.PATIENT_DEPARTMENT_CODE, " +
                "T.DR_AMT+T.GUARANTEE_PAID_AMT, 203104 AS ACCOUNT_CODE,  " +
                "'','','TH',"+
                "'-', " +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"', 'AC' " +
                "FROM TRN_DAILY T LEFT OUTER JOIN DOCTOR D "+
                "ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
                "WHERE (T.BATCH_NO = '' OR T.BATCH_NO IS NULL) " +
                "AND T.INVOICE_TYPE <> 'ORDER' " +
                "AND T.ACTIVE = '1' AND D.ACTIVE = '1'" +
                "AND T.ORDER_ITEM_ACTIVE = '1' "+
                "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' "+
                "AND T.IS_PAID <> 'N'";
    }
    private String accuExpense(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
               ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
               "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
               "SELECT T.LINE_NO, T.DOCTOR_CODE, 'O', " +
               //"T.DEPARTMENT_CODE, "+
               "CASE WHEN (T.DEPARTMENT_CODE = '' OR T.DEPARTMENT_CODE IS NULL) " +
               		"THEN D.DEPARTMENT_CODE ELSE T.DEPARTMENT_CODE END AS DEPARTMENT_CODE, " +
               "T.AMOUNT, T.EXPENSE_ACCOUNT_CODE, '', '', " +
               "'TH', " +
               "CASE WHEN T.EXPENSE_SIGN = '-1' THEN '%' ELSE '*' END AS EXPENSE_SIGN, "+
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"', 'AC' " +
               "FROM TRN_EXPENSE_DETAIL T " +
               "LEFT OUTER JOIN DOCTOR D ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
               "LEFT OUTER JOIN EXPENSE EX ON (T.EXPENSE_CODE = EX.CODE AND T.HOSPITAL_CODE = EX.HOSPITAL_CODE) "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               //"AND D.ACTIVE = '1' AND T.EXPENSE_ACCOUNT_CODE <> '101201' "+//Comment date 08/07/2009
               "AND D.ACTIVE = '1' AND EX.AC_INTERFACE = 'Y' "+ //Update date 08/07/2009
               "AND EX.ADJUST_TYPE <> 'DR' "+//Update by nop 2011-02-07
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' ";
    }
    private String accu203104Expense(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
               ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
               "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
               "SELECT T.LINE_NO, T.DOCTOR_CODE, 'O', " +
               //"T.DEPARTMENT_CODE, "+
               "CASE WHEN (T.DEPARTMENT_CODE = '' OR T.DEPARTMENT_CODE IS NULL) " +
               		"THEN D.DEPARTMENT_CODE ELSE T.DEPARTMENT_CODE END AS RECEIPT_LOCATION, " +
               //"T.AMOUNT, T.EXPENSE_ACCOUNT_CODE, T.LOCATION_CODE, T.LOCATION_CODE, " +
               "T.AMOUNT, '203104', '', '', 'TH', " +
               "CASE WHEN T.EXPENSE_SIGN = '-1' THEN '*' ELSE '%' END AS EXPENSE_SIGN, "+
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"', 'AC' " +
               "FROM TRN_EXPENSE_DETAIL T " +
               "LEFT OUTER JOIN DOCTOR D ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
               "LEFT OUTER JOIN EXPENSE EX ON (T.EXPENSE_CODE = EX.CODE AND T.HOSPITAL_CODE = EX.HOSPITAL_CODE) "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               //"AND D.ACTIVE = '1' AND T.EXPENSE_ACCOUNT_CODE <> '101201' "+ //Comment date 08/07/2009
               "AND D.ACTIVE = '1' AND EX.AC_INTERFACE = 'Y' "+ //Update date 08/07/2009
               "AND EX.ADJUST_TYPE <> 'DR' "+//Update by nop 2011-02-07
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' ";
    }

    //<editor-fold default state="collapsed" description="GL Account.">
    private String glDfDebitNoGuatantee(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION," +
                "NATIONALITY_CODE,AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT SD.LINE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
	            "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
	            	"THEN SD.DOCTOR_DEPARTMENT_CODE " +
	            	"ELSE SD.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            
	            "SD.AMOUNT_AFT_DISCOUNT, " +
	            "OI.ACCOUNT_CODE,  " +
	            "'','','TH',"+
                "'+' ," +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"', 'GL' " +
                " FROM TRN_DAILY SD " +
                "LEFT OUTER JOIN DOCTOR DR ON (SD.DOCTOR_CODE = DR.CODE AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE) "
                +"LEFT OUTER JOIN ORDER_ITEM OI ON (SD.ORDER_ITEM_CODE = OI.CODE AND SD.HOSPITAL_CODE = OI.HOSPITAL_CODE) "
                + " WHERE SD.YYYY='"+YYYY+"'"
                + " 	AND INVOICE_TYPE <> 'ORDER'"
                + " 	AND SD.MM='"+MM+"'"
                + " 	AND SD.HOSPITAL_CODE = '"+this.Hospital_code+"'"
                + " 	AND DR.DOCTOR_CATEGORY_CODE LIKE '%'"
                + " 	AND SD.DOCTOR_CODE NOT LIKE '99999%'"
                //--UPDATE 13/10/2009 FOR INTERFACE GL ALL PAYMENT
                + "     AND DR.PAYMENT_MODE_CODE <> 'U' " 
                + "		AND DR.PAYMENT_MODE_CODE <> '' " 
                + "		AND DR.PAYMENT_MODE_CODE IS NOT NULL "
                + "     AND DR.IS_HOLD <> 'Y' "
                //--END UPDATE 13/10/2009
                + " 	AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1' "
                + " 	AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != ''"
                + " 	AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y' )"
                + " 	AND SD.IS_PAID = 'Y'"
                + " 	AND DR.DOCTOR_TYPE_CODE NOT LIKE 'G%'";
    }
    private String glDfDebitGuarantee(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT SD.LINE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
                "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
        			"THEN SD.DOCTOR_DEPARTMENT_CODE " +
        			"ELSE SD.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            
	            
                "SD.AMOUNT_AFT_DISCOUNT, " +
                "602105 AS ACCOUNT_CODE,  " +
	            "'','','TH',"+
                "'+', " +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"','GL' " +
                "FROM TRN_DAILY SD LEFT OUTER JOIN DOCTOR DR "+
                "ON (SD.DOCTOR_CODE = DR.CODE AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
                " WHERE SD.YYYY='"+ this.YYYY +"'"+
                " 	AND INVOICE_TYPE <> 'ORDER'"+
                " 	AND SD.MM LIKE '"+ this.MM +"'"+
                " 	AND SD.HOSPITAL_CODE = '"+this.Hospital_code+"'"+
                " 	AND DR.DOCTOR_CATEGORY_CODE LIKE '%'"+
                " 	AND SD.DOCTOR_CODE NOT LIKE '99999%'"+
                //--UPDATE 13/10/2009 FOR INTERFACE GL ALL PAYMENT
                "   AND DR.PAYMENT_MODE_CODE <> 'U' AND DR.PAYMENT_MODE_CODE <> '' AND DR.PAYMENT_MODE_CODE IS NOT NULL "+
                "   AND DR.IS_HOLD <> 'Y' "+
                //--END UPDATE 13/10/2009
                " 	AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1' "+
                " 	AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != ''"+
                " 	AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y' )"+
                " 	AND SD.IS_PAID = 'Y'"+
                " AND DR.DOCTOR_TYPE_CODE LIKE 'G%'";
                //"   AND '"+this.YYYY+this.MM+"01' BETWEEN DR.GUARANTEE_START_DATE AND DR.GUARANTEE_EXPIRE_DATE";
    }
    /*
    private String gl602105DebitAdvanceSomeGuarantee(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT SD.LINE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
                "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
        			"THEN SD.DOCTOR_DEPARTMENT_CODE " +
        			"ELSE SD.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            
	            
                //"SD.AMOUNT_AFT_DISCOUNT, " +
                "SD.GUARANTEE_PAID_AMT, "+
	            "602105 AS ACCOUNT_CODE,  " +
	            "'','','TH',"+
                "'+', " +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"','GL' " +
                "FROM TRN_DAILY SD LEFT OUTER JOIN DOCTOR DR "+
                "ON (SD.DOCTOR_CODE = DR.CODE AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
                " WHERE SD.GUARANTEE_TERM_YYYY='"+ this.YYYY +"'"+
                " 	AND SD.GUARANTEE_TERM_MM LIKE '"+ this.MM +"'"+
                //--UPDATE 05/08/2010
                "   AND SD.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE'"+
                //--END UPDATE 05/08/2010
                " 	AND INVOICE_TYPE <> 'ORDER'"+
                " 	AND DR.DOCTOR_CATEGORY_CODE LIKE '%'"+
                " 	AND SD.DOCTOR_CODE NOT LIKE '99999%'"+
                " 	AND SD.HOSPITAL_CODE = '"+this.Hospital_code+"'"+
                //--UPDATE 13/10/2009 FOR INTERFACE GL ALL PAYMENT
                "   AND DR.PAYMENT_MODE_CODE <> 'U' AND DR.PAYMENT_MODE_CODE <> '' AND DR.PAYMENT_MODE_CODE IS NOT NULL "+
                "   AND DR.IS_HOLD <> 'Y' "+
                //--END UPDATE 13/10/2009
                " 	AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1' "+
                " 	AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != ''"+
                //--comment line below by nop 20100909 (absorb some is unpaid transaction)
                //" 	AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y' )"+
                " 	AND SD.IS_PAID = 'Y'";
    }
    */
    private String gl602103CreditSharing(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE,ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT SD.LINE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
                "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
            		"THEN SD.DOCTOR_DEPARTMENT_CODE " +
            		"ELSE SD.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            
	            
                "SD.AMOUNT_AFT_DISCOUNT - SD.DR_AMT AS HP_AMT, "+
                //"T.HP_AMT, " +

                "602103 AS ACCOUNT_CODE,  " +
	            "'','','TH',"+
                "'-', " +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"','GL' " +
                " FROM TRN_DAILY SD " +
                "LEFT OUTER JOIN DOCTOR DR ON (SD.DOCTOR_CODE = DR.CODE AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE)"+
                " WHERE SD.YYYY LIKE '"+ this.YYYY +"'"+
                " 	AND INVOICE_TYPE <> 'ORDER'"+
                " 	AND SD.MM LIKE '"+ this.MM +"'"+
                " 	AND SD.HOSPITAL_CODE = '"+this.Hospital_code+"'"+
                " 	AND DR.DOCTOR_CATEGORY_CODE LIKE '%'"+
                " 	AND SD.DOCTOR_CODE NOT LIKE '99999%'"+
                //--UPDATE 13/10/2009 FOR INTERFACE GL ALL PAYMENT
                "   AND DR.PAYMENT_MODE_CODE <> 'U' AND DR.PAYMENT_MODE_CODE <> '' AND DR.PAYMENT_MODE_CODE IS NOT NULL "+
                "   AND DR.IS_HOLD <> 'Y' "+
                //--END UPDATE 13/10/2009
                " 	AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1'"+
                " 	AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != ''"+
                " 	AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y' )"+
                " 	AND SD.IS_PAID = 'Y'";
    }
    /*
    private String gl101201CreditAdvanceSomeGuarantee(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT SD.LINE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
                
                "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
    				"THEN SD.DOCTOR_DEPARTMENT_CODE " +
    				"ELSE SD.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            

                "SD.GUARANTEE_PAID_AMT, 101201 AS ACCOUNT_CODE,  " +
	            "'','','TH',"+
                "'-', " +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"','GL' " +
                " FROM TRN_DAILY SD LEFT OUTER JOIN DOCTOR DR ON (SD.DOCTOR_CODE = DR.CODE AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE)"+
                " WHERE SD.GUARANTEE_TERM_YYYY='"+ this.YYYY +"'"+
                " 	AND SD.GUARANTEE_TERM_MM LIKE '"+ this.MM +"'"+
                //--UPDATE 05/08/2010
                "   AND SD.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE'"+
                //--END UPDATE 05/08/2010
                " 	AND SD.INVOICE_TYPE <> 'ORDER'"+
                " 	AND SD.HOSPITAL_CODE='"+this.Hospital_code+"'"+
                " 	AND DR.DOCTOR_CATEGORY_CODE LIKE '%'"+
                " 	AND SD.DOCTOR_CODE NOT LIKE '99999%'"+
                //--UPDATE 13/10/2009 FOR INTERFACE GL ALL PAYMENT
                "   AND DR.PAYMENT_MODE_CODE <> 'U' AND DR.PAYMENT_MODE_CODE <> '' AND DR.PAYMENT_MODE_CODE IS NOT NULL "+
                "   AND DR.IS_HOLD <> 'Y' "+
                //--END UPDATE 13/10/2009
                " 	AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1'"+
                " 	AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != ''"+
                //--comment line below by nop 20100909 (absorb some is unpaid transaction)
                //" 	AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y' )"+
                " 	AND SD.IS_PAID = 'Y'";
    }
    */
    private String gl101201Credit(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT SD.LINE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
                
                "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
    				"THEN SD.DOCTOR_DEPARTMENT_CODE " +
    				"ELSE SD.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            

                "SD.DR_AMT, 101201 AS ACCOUNT_CODE,  " +
	            "'','','TH',"+
                "'-', " +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"','GL' " +
                " FROM TRN_DAILY SD LEFT OUTER JOIN DOCTOR DR ON (SD.DOCTOR_CODE = DR.CODE AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE)"+
                " WHERE SD.YYYY LIKE '"+this.YYYY+"'"+
                " 	AND SD.INVOICE_TYPE <> 'ORDER'"+
                " 	AND SD.MM LIKE '"+this.MM+"'"+
                " 	AND SD.HOSPITAL_CODE='"+this.Hospital_code+"'"+
                " 	AND DR.DOCTOR_CATEGORY_CODE LIKE '%'"+
                " 	AND SD.DOCTOR_CODE NOT LIKE '99999%'"+
                //--UPDATE 13/10/2009 FOR INTERFACE GL ALL PAYMENT
                "   AND DR.PAYMENT_MODE_CODE <> 'U' AND DR.PAYMENT_MODE_CODE <> '' AND DR.PAYMENT_MODE_CODE IS NOT NULL "+
                "   AND DR.IS_HOLD <> 'Y' "+
                //--END UPDATE 13/10/2009
                " 	AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1'"+
                " 	AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != ''"+
                " 	AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y' )"+
                " 	AND SD.IS_PAID = 'Y'";
    }
    private String glExpense(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
               ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
               "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
               "SELECT T.LINE_NO, T.DOCTOR_CODE, 'O', " +
               //"T.DEPARTMENT_CODE, "+
               "CASE WHEN (T.DEPARTMENT_CODE = '' OR T.DEPARTMENT_CODE IS NULL) " +
               		"THEN DR.DEPARTMENT_CODE ELSE T.DEPARTMENT_CODE END AS RECEIPT_LOCATION, " +
               //"T.AMOUNT, T.EXPENSE_ACCOUNT_CODE, T.LOCATION_CODE, T.LOCATION_CODE, " +
               "T.AMOUNT, T.EXPENSE_ACCOUNT_CODE, '', '', " +
               "'TH', " +
               "CASE WHEN T.EXPENSE_SIGN = '-1' THEN '%' ELSE '*' END AS EXPENSE_SIGN, "+
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"','GL' " +
               "FROM TRN_EXPENSE_DETAIL T " +
               "LEFT OUTER JOIN DOCTOR DR ON (T.DOCTOR_CODE = DR.CODE AND T.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
               "LEFT OUTER JOIN EXPENSE EX ON (T.EXPENSE_CODE = EX.CODE AND T.HOSPITAL_CODE = EX.HOSPITAL_CODE) "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               //--UPDATE 13/10/2009 FOR INTERFACE GL ALL PAYMENT
               "AND DR.PAYMENT_MODE_CODE <> 'U' AND DR.PAYMENT_MODE_CODE <> '' AND DR.PAYMENT_MODE_CODE IS NOT NULL "+
               "AND DR.IS_HOLD <> 'Y' "+
               "AND DR.ACTIVE = '1' AND EX.GL_INTERFACE = 'Y' "+ //Update date 13/10/2010
               //--END UPDATE 13/10/2009
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' ";
    }
    private String gl101201Expense(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
               ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
               "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
               "SELECT T.LINE_NO, T.DOCTOR_CODE, 'O', " +
               //"T.DEPARTMENT_CODE, "+
               "CASE WHEN (T.DEPARTMENT_CODE = '' OR T.DEPARTMENT_CODE IS NULL) " +
               		"THEN DR.DEPARTMENT_CODE ELSE T.DEPARTMENT_CODE END AS RECEIPT_LOCATION, " +
               //"T.AMOUNT, T.EXPENSE_ACCOUNT_CODE, T.LOCATION_CODE, T.LOCATION_CODE, " +
               "T.AMOUNT, '101201', '', '', " +
               "'TH', " +
               "CASE WHEN T.EXPENSE_SIGN = '-1' THEN '*' ELSE '%' END AS EXPENSE_SIGN, "+
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"','GL' " +
               "FROM TRN_EXPENSE_DETAIL T " +
               "LEFT OUTER JOIN DOCTOR DR ON (T.DOCTOR_CODE = DR.CODE AND T.HOSPITAL_CODE = DR.HOSPITAL_CODE) "+
               "LEFT OUTER JOIN EXPENSE EX ON (T.EXPENSE_CODE = EX.CODE AND T.HOSPITAL_CODE = EX.HOSPITAL_CODE) "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               //--UPDATE 13/10/2009 FOR INTERFACE GL ALL PAYMENT
               "AND DR.PAYMENT_MODE_CODE <> 'U' AND DR.PAYMENT_MODE_CODE <> '' AND DR.PAYMENT_MODE_CODE IS NOT NULL "+
               "AND DR.IS_HOLD <> 'Y' "+
               "AND DR.ACTIVE = '1' AND EX.GL_INTERFACE = 'Y' "+ //Update date 13/10/2010
               //--END UPDATE 13/10/2009
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' ";
    }

    private void insertIntoIntErpGl(){
        /*
        Hospital_Code	10000
        Seq_No	counter
        Accounting_DT	11/2/2008
        Accounting_Time	11.22.23
        Type	AC/GL
        Account	ACCOUNT_CODE
        Dept_ID	PATIENT_DEPARTMENT_CODE
        Product	ADMISSION TYPE CODE
        Monetary_Amount	AMOUNT_AFT_DISCOUNT
        Class_Fld	NATIONALITY_CODE
        Chartfield2	RECEIPT_LOCATION
        Chartfield3	PATIENT_LOCATION
         */
    	String Accounting_Time = JDate.getHour() + "." + JDate.getMinutes() + "." + JDate.getSeconds();
        String Accounting_DT = JDate.getEndMonthDate(this.YYYY, this.MM)+ "/" + this.MM + "/" + this.YYYY;
    	String account_date_gl = "10/" + this.MM + "/" + this.YYYY;
    	String[][] d = null;
    	
    	try{
            conn.setStatement();
            if(TypeData.equals("GL")){
            	Accounting_DT = "10/" + this.MM + "/" + this.YYYY;
        		d = conn.query("SELECT MAX(DISTINCT PAYMENT_TERM_DATE) FROM PAYMENT_MONTHLY WHERE HOSPITAL_CODE='"+this.Hospital_code+"'");
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
    public String processAccu(){
        String countdata[][] = null;
        this.TypeData = "AC";
        try{
            conn.setStatement();
            conn.insert("DELETE TEMP_GL WHERE YYYY='"+this.YYYY+"' AND HOSPITAL_CODE = '"+this.Hospital_code+"' AND PROCESS = 'AC'");
            conn.insert("DELETE INT_ERP_GL WHERE TYPE='AC' AND YYYY='"+this.YYYY+"' AND MM='"+this.MM+"' AND HOSPITAL_CODE = '"+this.Hospital_code+"'");
            conn.insert(this.accuDfDeditNoGuanrantee());
            conn.insert(this.accu602103Credit());
            conn.insert(this.accu602105DebitGuatantee());
            /*
             * Comment because process guarantee insert 
             * data to expense transaction
            conn.insert(this.accuAbsorbDebitGuatantee());
            conn.insert(this.accuAbsorbCreditGuatantee());
            */
            conn.insert(this.accu203104Credit());
            conn.insert(this.accu203104Expense());
            conn.insert(this.accuExpense());

            conn.commitDB();
            //countdata = conn.query("SELECT count(*) as count_table FROM TEMP_GL WHERE HOSPITAL_CODE = '"+this.Hospital_code+"'");
            //System.out.println("Data from AC : " + countdata[0][0] + " record.");
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
    	String message = "";
        String countdata[][] = null;
        this.TypeData = "GL";
        try{
            conn.setStatement();
            conn.insert("DELETE TEMP_GL WHERE YYYY='"+this.YYYY+"' AND HOSPITAL_CODE = '"+this.Hospital_code+"' AND PROCESS = 'GL'");
            conn.insert("DELETE INT_ERP_GL WHERE TYPE='GL' AND YYYY='"+this.YYYY+"' AND MM='"+this.MM+"' AND HOSPITAL_CODE = '"+this.Hospital_code+"'");

            conn.insert(this.glDfDebitNoGuatantee());
            conn.insert(this.gl602103CreditSharing());
            conn.insert(this.glDfDebitGuarantee());
            conn.insert(this.gl101201Credit());
            conn.insert(this.glExpense());
            conn.insert(this.gl101201Expense());
            /* nopp comment 20101021
             * Comment because process guarantee insert 
             * data to expense transaction
            conn.insert(this.glAbsorbDebitGuatantee());
            conn.insert(this.glAbsorbCreditGuatantee()); 
            */
            //conn.insert(this.gl602105DebitAdvanceSomeGuarantee());
            //conn.insert(this.gl101201CreditAdvanceSomeGuarantee());
      
            conn.commitDB();
            //countdata = conn.query("SELECT count(*) as count_table FROM TEMP_GL WHERE HOSPITAL_CODE = '"+this.Hospital_code+"' AND YYYY='"+this.YYYY+"' AND MM='"+this.MM+"'");
            //System.out.println("Data from accu : " + countdata.length + " record.");
        }catch(Exception err){
            System.out.println("Process GL : "+err);
            conn.rollDB();
        }finally{
            insertIntoIntErpGl();
            conn.closeDB("");
            return countdata + "/" + count_insert;
        }
    }
}