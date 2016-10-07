package df.bean.interfacefile;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;
import java.sql.ResultSet;

public class InterfaceExportGLBPHBean {
    private DBConnection dbconn;
    private DBConn conn;
    private String YYYY;
    private String MM;
    private String Hospital_code;
    private String TypeData;
    private int count_insert;

    public InterfaceExportGLBPHBean(String yyyy, String mm, String hospital_code){
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

    private String accuDebitDf(){ //transaction amount_aft_discount Not Guarantee
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT T.LINE_NO, T.DOCTOR_CODE, T.ADMISSION_TYPE_CODE, " +
                "CASE WHEN (T.PATIENT_DEPARTMENT_CODE = '' OR T.PATIENT_DEPARTMENT_CODE IS NULL) " +
                	"THEN T.DOCTOR_DEPARTMENT_CODE " +
                	"ELSE T.PATIENT_DEPARTMENT_CODE " +
                "END AS PATIENT_DEPARTMENT_CODE, " +
                "CASE WHEN T.AMOUNT_AFT_DISCOUNT < T.DR_AMT THEN T.DR_AMT ELSE T.AMOUNT_AFT_DISCOUNT END AS AMOUNT_AFT, " +
	            "OI.ACCOUNT_CODE AS ACCOUNT_CODE,  " +
	            "'','','TH',"+
                "'+' ," +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"','AC' " +
                "FROM TRN_DAILY T " +
                "LEFT OUTER JOIN DOCTOR D ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
                "LEFT OUTER JOIN ORDER_ITEM OI ON (T.ORDER_ITEM_CODE = OI.CODE AND T.HOSPITAL_CODE = OI.HOSPITAL_CODE) "+
                "WHERE (T.BATCH_NO = '' OR T.BATCH_NO IS NULL) "+
                "AND T.INVOICE_TYPE <> 'ORDER' AND D.ACTIVE = '1' AND T.ACTIVE = '1' "+
                "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' AND T.IS_PAID <> 'N' " +
                "AND D.DOCTOR_TYPE_CODE NOT LIKE 'G%' " +
                "AND T.ORDER_ITEM_ACTIVE = '1'";
    }    
    private String accu602105DebitGuatantee(){ //transaction amount_aft_discount Guarantee Only
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT T.LINE_NO, T.DOCTOR_CODE, T.ADMISSION_TYPE_CODE, " +
                "CASE WHEN (T.PATIENT_DEPARTMENT_CODE = '' OR T.PATIENT_DEPARTMENT_CODE IS NULL) " +
        			"THEN T.DOCTOR_DEPARTMENT_CODE " +
        			"ELSE T.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            	            
                "T.AMOUNT_AFT_DISCOUNT, " +
                "602105 AS ACCOUNT_CODE,  " +
                "'','','TH',"+
                "'+', " +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"', 'AC' " +
                "FROM TRN_DAILY T LEFT OUTER JOIN DOCTOR D "+
                "ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
                "WHERE (T.BATCH_NO = '' OR T.BATCH_NO IS NULL) "+
                "AND T.INVOICE_TYPE <> 'ORDER' AND D.ACTIVE = '1' AND T.ACTIVE = '1' "+
                "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' AND T.IS_PAID <> 'N' "+
                "AND T.ORDER_ITEM_ACTIVE = '1' " +
                "AND D.DOCTOR_TYPE_CODE LIKE 'G%' ";                
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
                "CASE WHEN T.AMOUNT_AFT_DISCOUNT - T.DR_AMT < 0 " +
                	"THEN 0 ELSE T.AMOUNT_AFT_DISCOUNT - T.DR_AMT END AS HP_AMT, "+
                "602103 AS ACCOUNT_CODE,  " +
                "'','','TH',"+              
                "'-', " +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"','AC' " +
                "FROM TRN_DAILY T LEFT OUTER JOIN DOCTOR D "+
                "ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
                "WHERE (T.BATCH_NO = '' OR T.BATCH_NO IS NULL) "+
                "AND T.INVOICE_TYPE <> 'ORDER' AND D.ACTIVE = '1' AND T.ACTIVE = '1' "+
                "AND T.ORDER_ITEM_ACTIVE = '1' "+
                "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' AND T.IS_PAID <> 'N'";
    }    
    private String accu203104Credit(){ //transaction dr_amt
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT T.LINE_NO, T.DOCTOR_CODE, T.ADMISSION_TYPE_CODE, " +
                "CASE WHEN (T.PATIENT_DEPARTMENT_CODE = '' OR T.PATIENT_DEPARTMENT_CODE IS NULL) " +
    				"THEN T.DOCTOR_DEPARTMENT_CODE " +
    				"ELSE T.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            
                "T.DR_AMT, 203104 AS ACCOUNT_CODE,  " +
                "'','','TH',"+
                "'-', " +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"','AC' " +
                "FROM TRN_DAILY T LEFT OUTER JOIN DOCTOR D "+
                "ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
                "WHERE (T.BATCH_NO = '' OR T.BATCH_NO IS NULL) " +
                "AND T.INVOICE_TYPE <> 'ORDER' " +
                "AND T.ACTIVE = '1' AND D.ACTIVE = '1'" +
                "AND T.ORDER_ITEM_ACTIVE = '1' "+
                "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' "+
                "AND T.IS_PAID <> 'N'";
    }
    private String accuAbsorbDebitGuatantee(){
        return "INSERT INTO TEMP_GL(ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
               ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION," +
               "NATIONALITY_CODE,AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
               "SELECT 'O', "+
               "D.DEPARTMENT_CODE, "+
               //"T.DF406_HOLD_AMOUNT+T.HP402_ABSORB_AMOUNT AS ABSORB, "+
               //--UPDATE 23/08/2010
               "T.SUM_TAX_402 AS ABSORB, "+
               //--END UPDATE 23/08/2010

               "602106 AS ACCOUNT_CODE, "+
               "'','','TH',"+
               "'+', " +
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"','AC' " +
               "FROM SUMMARY_GUARANTEE T LEFT OUTER JOIN DOCTOR D "+
               "ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               "AND D.ACTIVE = '1' "+
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' "+
               "AND T.SUM_TAX_402 > 0";
    }
    private String accuAbsorbCreditGuatantee(){
        return "INSERT INTO TEMP_GL(ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
               ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION," +
               "NATIONALITY_CODE,AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
               "SELECT 'O', "+
               "D.DEPARTMENT_CODE, "+
               //"T.SUM_TAX_406+T.SUM_TAX_402 AS ABSORB, "+
               //--UPDATE 23/08/2010
               "T.SUM_TAX_402 AS ABSORB, "+
               //--END UPDATE 23/08/2010

               "203104 AS ACCOUNT_CODE, "+
               "'','','TH','-',"+
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"','AC' " +
               "FROM SUMMARY_GUARANTEE T LEFT OUTER JOIN DOCTOR D "+
               "ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               "AND D.ACTIVE = '1' "+
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' "+
               "AND T.SUM_TAX_402 > 0";
    }
    private String accuExpense203104(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
               ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
               "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
               "SELECT T.LINE_NO, T.DOCTOR_CODE, 'O', " +
               //"T.DEPARTMENT_CODE, "+
               "CASE WHEN (T.DEPARTMENT_CODE = '' OR T.DEPARTMENT_CODE IS NULL) " +
               		"THEN D.DEPARTMENT_CODE ELSE T.DEPARTMENT_CODE END AS RECEIPT_LOCATION, " +
               //"T.AMOUNT, T.EXPENSE_ACCOUNT_CODE, T.LOCATION_CODE, T.LOCATION_CODE, " +
               "T.AMOUNT, '203104', '', '', " +
               "'TH', " +
               "CASE WHEN T.EXPENSE_SIGN = '-1' THEN '*' ELSE '%' END AS EXPENSE_SIGN, "+
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"','AC' " +
               "FROM TRN_EXPENSE_DETAIL T LEFT OUTER JOIN DOCTOR D "+
               "ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               //"AND D.ACTIVE = '1' AND T.EXPENSE_ACCOUNT_CODE <> '101201' "+ //Comment date 08/07/2009
               //"AND D.ACTIVE = '1' AND T.EXPENSE_ACCOUNT_CODE LIKE '6%' "+ //Update date 08/07/2009
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' ";
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
               "'"+ this.Hospital_code +"','AC' " +
               "FROM TRN_EXPENSE_DETAIL T LEFT OUTER JOIN DOCTOR D "+
               "ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               //"AND D.ACTIVE = '1' AND T.EXPENSE_ACCOUNT_CODE <> '101201' "+//Comment date 08/07/2009
               //"AND D.ACTIVE = '1' AND T.EXPENSE_ACCOUNT_CODE LIKE '6%' "+ //Update date 08/07/2009
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' ";
    }

    //<editor-fold default state="collapsed" description="GL Account.">

    private String glDebitDf(){ //transaction amount_aft_discount
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION," +
                "NATIONALITY_CODE,AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT SD.LINE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
	            "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
	            	"THEN SD.DOCTOR_DEPARTMENT_CODE " +
	            	"ELSE SD.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +
                "CASE WHEN SD.AMOUNT_AFT_DISCOUNT < SD.DR_AMT " +
                	"THEN SD.DR_AMT " +
                	"ELSE SD.AMOUNT_AFT_DISCOUNT " +
                "END AS AMOUNT_AFT_DISCOUNT, " +
	            "CASE WHEN OI.ACCOUNT_CODE = '' OR OI.ACCOUNT_CODE IS NULL THEN OI.CODE ELSE OI.ACCOUNT_CODE END AS ACCOUNT_CODE,  " +
	            "'','','TH',"+
                "'+' ," +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"','GL' " +
                " FROM TRN_DAILY SD " +
                "LEFT OUTER JOIN DOCTOR DR ON (SD.DOCTOR_CODE = DR.CODE AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE) "
                +"LEFT OUTER JOIN ORDER_ITEM OI ON (SD.ORDER_ITEM_CODE = OI.CODE AND SD.HOSPITAL_CODE = OI.HOSPITAL_CODE) "
                + " WHERE SD.YYYY='"+YYYY+"'"
                + " 	AND INVOICE_TYPE <> 'ORDER'"
                + " 	AND SD.MM='"+MM+"'"
                + " 	AND SD.HOSPITAL_CODE = '"+this.Hospital_code+"'"
                + " 	AND DR.DOCTOR_CATEGORY_CODE LIKE '%'"
                + " 	AND SD.DOCTOR_CODE NOT LIKE '99999%'"
                + " 	AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1' AND DR.ACTIVE = '1' "
                + " 	AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != ''"
                + " 	AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y' )"
                + " 	AND SD.IS_PAID = 'Y'"
                + " 	AND DR.PAYMENT_MODE_CODE != 'U'"
                + "		AND DR.DOCTOR_TYPE_CODE NOT LIKE 'G%'"
                ;
    }
    private String glAccount602103Credit(){ // transaction hp_amt
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT SD.LINE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
                "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
            		"THEN SD.DOCTOR_DEPARTMENT_CODE " +
            		"ELSE SD.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            
                "CASE WHEN SD.AMOUNT_AFT_DISCOUNT - SD.DR_AMT < 0 " +
            		"THEN 0 " +
            		"ELSE SD.AMOUNT_AFT_DISCOUNT - SD.DR_AMT " +
            	"END AS HP_AMT, "+
                //"SD.AMOUNT_AFT_DISCOUNT - SD.DR_AMT AS HP_AMT, "+
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
                " 	AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1' AND DR.ACTIVE = '1' "+
                " 	AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != ''"+
                " 	AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y' )"+
                " 	AND SD.IS_PAID = 'Y'"+
                " 	AND DR.PAYMENT_MODE_CODE != 'U'";
    }
    private String glAccount602105DebitGuarantee(){
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
                "FROM TRN_DAILY SD LEFT OUTER JOIN DOCTOR D "+
                "ON (SD.DOCTOR_CODE = D.CODE AND SD.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
                " WHERE SD.YYYY='"+ this.YYYY +"'"+
                " 	AND INVOICE_TYPE <> 'ORDER'"+
                " 	AND SD.MM LIKE '"+ this.MM +"'"+
                " 	AND SD.HOSPITAL_CODE = '"+this.Hospital_code+"'"+
                " 	AND D.DOCTOR_CATEGORY_CODE LIKE '%'"+
                " 	AND SD.DOCTOR_CODE NOT LIKE '99999%'"+
                " 	AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1' AND D.ACTIVE = '1' "+
                " 	AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != ''"+
                " 	AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y' )"+
                " 	AND SD.IS_PAID = 'Y'"+
                " 	AND D.PAYMENT_MODE_CODE != 'U'"+
                "   AND D.DOCTOR_TYPE_CODE LIKE 'G%'";
                //" AND '"+this.YYYY+this.MM+"01' BETWEEN D.GUARANTEE_START_DATE AND D.GUARANTEE_EXPIRE_DATE";
    }
    private String glAccount602105DebitAdvanceSomeGuarantee(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT SD.LINE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
                "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
        			"THEN SD.DOCTOR_DEPARTMENT_CODE " +
        			"ELSE SD.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            
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
                "   AND SD.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE'"+
                " 	AND INVOICE_TYPE <> 'ORDER'"+
                " 	AND DR.DOCTOR_CATEGORY_CODE LIKE '%'"+
                " 	AND SD.DOCTOR_CODE NOT LIKE '99999%'"+
                " 	AND SD.HOSPITAL_CODE = '"+this.Hospital_code+"'"+
                "   AND (DR.PAYMENT_MODE_CODE <> 'U' OR DR.PAYMENT_MODE_CODE <> '' OR DR.PAYMENT_MODE_CODE IS NOT NULL) "+
                "   AND DR.IS_HOLD <> 'Y' "+
                " 	AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1'"+
                " 	AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != ''"+
                //" 	AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y' )"+
                " 	AND SD.IS_PAID = 'Y'";
                //" AND DR.DOCTOR_TYPE_CODE LIKE 'G%'";
    }
    private String glAbsorbDebitGuatantee(){
        return "INSERT INTO TEMP_GL(ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE, " +
               "AMOUNT_AFT_DISCOUNT, ACCOUNT_CODE, RECEIPT_LOCATION, " +
               "PATIENT_LOCATION, NATIONALITY_CODE, AMOUNT_SIGN, MM, YYYY, HOSPITAL_CODE,PROCESS) " +
               "SELECT 'O', "+
               "D.DEPARTMENT_CODE, "+
               "T.SUM_TAX_402 AS ABSORB, "+
               "602106 AS ACCOUNT_CODE, "+
               "'','',"+
               "'TH' AS NATIONALITY_CODE,'+', "+
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"','GL' " +
               "FROM SUMMARY_GUARANTEE T LEFT OUTER JOIN DOCTOR D "+
               "ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               "AND D.ACTIVE = '1' AND D.PAYMENT_MODE_CODE != 'U' "+
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' "+
               "AND T.SUM_TAX_402 > 0";
    }
    private String glAbsorbCreditGuatantee(){
        return "INSERT INTO TEMP_GL(ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
               ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION," +
               "NATIONALITY_CODE,AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
               "SELECT 'O', "+
               "D.DEPARTMENT_CODE, "+
               "T.SUM_TAX_402 AS ABSORB, "+
               "101202 AS ACCOUNT_CODE, "+
               "'','',"+
               "'TH' AS NATIONALITY_CODE,'-', "+
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"','GL' " +
               "FROM SUMMARY_GUARANTEE T LEFT OUTER JOIN DOCTOR D "+
               "ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               "AND D.ACTIVE = '1' AND D.PAYMENT_MODE_CODE != 'U' "+
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' "+
               "AND T.SUM_TAX_402 > 0";
    }
    private String glAccount101202CreditAdvanceSomeGuarantee(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT SD.LINE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
                
                "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
    				"THEN SD.DOCTOR_DEPARTMENT_CODE " +
    				"ELSE SD.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            

                "SD.GUARANTEE_PAID_AMT, 101202 AS ACCOUNT_CODE,  " +
	            "'','','TH',"+
                "'-', " +
                "'"+this.MM+"', " +
                "'"+this.YYYY+"', " +
                "'"+ this.Hospital_code +"','GL' " +
                " FROM TRN_DAILY SD LEFT OUTER JOIN DOCTOR DR ON (SD.DOCTOR_CODE = DR.CODE AND SD.HOSPITAL_CODE = DR.HOSPITAL_CODE)"+
                " WHERE SD.GUARANTEE_TERM_YYYY='"+ this.YYYY +"'"+
                " 	AND SD.GUARANTEE_TERM_MM LIKE '"+ this.MM +"'"+
                "   AND SD.GUARANTEE_NOTE = 'ABSORB SOME GUARANTEE'"+
                " 	AND SD.INVOICE_TYPE <> 'ORDER'"+
                " 	AND SD.HOSPITAL_CODE='"+this.Hospital_code+"'"+
                " 	AND DR.DOCTOR_CATEGORY_CODE LIKE '%'"+
                " 	AND SD.DOCTOR_CODE NOT LIKE '99999%'"+
                "   AND (DR.PAYMENT_MODE_CODE <> 'U' OR DR.PAYMENT_MODE_CODE <> '' OR DR.PAYMENT_MODE_CODE IS NOT NULL) "+
                "   AND DR.IS_HOLD <> 'Y' "+
                " 	AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1'"+
                " 	AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != ''"+
                //" 	AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y' )"+
                " 	AND SD.IS_PAID = 'Y'";
    }
    private String glAccount101202Credit(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
                ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
                "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
                "SELECT SD.LINE_NO, SD.DOCTOR_CODE, SD.ADMISSION_TYPE_CODE, " +
                
                "CASE WHEN (SD.PATIENT_DEPARTMENT_CODE = '' OR SD.PATIENT_DEPARTMENT_CODE IS NULL) " +
    				"THEN SD.DOCTOR_DEPARTMENT_CODE " +
    				"ELSE SD.PATIENT_DEPARTMENT_CODE " +
	            "END AS PATIENT_DEPARTMENT_CODE, " +	            

                "SD.DR_AMT, 101202 AS ACCOUNT_CODE,  " +
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
                "   AND DR.PAYMENT_MODE_CODE != 'U'"+
                " 	AND SD.ACTIVE = '1' AND SD.ORDER_ITEM_ACTIVE = '1' AND DR.ACTIVE = '1' "+
                " 	AND SD.COMPUTE_DAILY_DATE is not null AND SD.COMPUTE_DAILY_DATE != ''"+
                " 	AND (SD.PAY_BY_CASH='Y' OR SD.PAY_BY_AR='Y' OR SD.PAY_BY_DOCTOR='Y' OR SD.PAY_BY_PAYOR='Y' OR SD.PAY_BY_CASH_AR='Y' )"+
                " 	AND SD.IS_PAID = 'Y'";
    }
    private String glExpense(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE" +
               ",AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
               "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
               "SELECT T.LINE_NO, T.DOCTOR_CODE, 'O', " +
               "CASE WHEN (T.DEPARTMENT_CODE = '' OR T.DEPARTMENT_CODE IS NULL) " +
               		"THEN D.DEPARTMENT_CODE ELSE T.DEPARTMENT_CODE END AS RECEIPT_LOCATION, " +
               "T.AMOUNT, T.EXPENSE_ACCOUNT_CODE, '', '', " +
               "'TH', " +
               "CASE WHEN T.EXPENSE_SIGN = '-1' THEN '%' ELSE '*' END AS EXPENSE_SIGN, "+
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"','GL' " +
               "FROM TRN_EXPENSE_DETAIL T LEFT OUTER JOIN DOCTOR D "+
               "ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               "AND D.ACTIVE = '1' AND D.PAYMENT_MODE_CODE != 'U' "+
               "AND T.HOSPITAL_CODE = '"+this.Hospital_code+"' ";
    }
    private String glExpense101202(){
        return "INSERT INTO TEMP_GL(LINE_NO, DOCTOR_CODE, ADMISSION_TYPE_CODE,PATIENT_DEPARTMENT_CODE," +
               "AMOUNT_AFT_DISCOUNT,ACCOUNT_CODE,RECEIPT_LOCATION,PATIENT_LOCATION,NATIONALITY_CODE," +
               "AMOUNT_SIGN,MM,YYYY,HOSPITAL_CODE,PROCESS) " +
               "SELECT T.LINE_NO, T.DOCTOR_CODE, 'O', " +
               "CASE WHEN (T.DEPARTMENT_CODE = '' OR T.DEPARTMENT_CODE IS NULL) " +
               		"THEN D.DEPARTMENT_CODE ELSE T.DEPARTMENT_CODE END AS RECEIPT_LOCATION, " +
               "T.AMOUNT, '101202', '', '', " +
               "'TH', " +
               "CASE WHEN T.EXPENSE_SIGN = '-1' THEN '*' ELSE '%' END AS EXPENSE_SIGN, "+
               "'"+this.MM+"', " +
               "'"+this.YYYY+"', " +
               "'"+ this.Hospital_code +"','GL' " +
               "FROM TRN_EXPENSE_DETAIL T LEFT OUTER JOIN DOCTOR D "+
               "ON (T.DOCTOR_CODE = D.CODE AND T.HOSPITAL_CODE = D.HOSPITAL_CODE) "+
               "WHERE T.YYYY = '"+this.YYYY+"' AND T.MM = '"+this.MM+"' "+
               "AND D.ACTIVE = '1' AND D.PAYMENT_MODE_CODE != 'U' "+
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
        //String Accounting_DT = JDate.getDay() + "/" + JDate.getMonth() + "/" + JDate.getYear();
        String Accounting_DT = JDate.getEndMonthDate(this.YYYY, this.MM)+ "/" + this.MM + "/" + this.YYYY;
        //String Accounting_DT = JDate.getEndMonthDate(this.YYYY, this.MM)+ "/" + JDate.getMonth() + "/" + JDate.getYear();
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
                    Type = "IPD";                }else if("O".equalsIgnoreCase(rs.getString("ADMISSION_TYPE_CODE"))){
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
                //System.out.println("Interface GL : "+sqlInsert);
                try{
                    //conn.setStatement();
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
        System.out.println(" Insert into INT_ERP_GL : " + count_insert + "");
    }

    public String processAccu(){
        String countdata[][] = null;
        this.TypeData = "AC";
        try{
            conn.setStatement();
            conn.insert("DELETE TEMP_GL WHERE YYYY='"+this.YYYY+"' AND HOSPITAL_CODE = '"+this.Hospital_code+"' AND PROCESS = 'AC'");
            conn.insert("DELETE INT_ERP_GL WHERE TYPE='AC' AND YYYY='"+this.YYYY+"' AND MM='"+this.MM+"' AND HOSPITAL_CODE = '"+this.Hospital_code+"'");

            conn.insert(this.accu602103Credit());
            conn.insert(this.accuDebitDf());
            conn.insert(this.accu602105DebitGuatantee());
            conn.insert(this.accuAbsorbDebitGuatantee());
            conn.insert(this.accuAbsorbCreditGuatantee());
            conn.insert(this.accu203104Credit());
            conn.insert(this.accuExpense203104());
            conn.insert(this.accuExpense());

            conn.commitDB();
            countdata = conn.query("SELECT count(*) as count_table FROM TEMP_GL WHERE HOSPITAL_CODE = '"+this.Hospital_code+"'");

            System.out.println("Data ac : " + countdata[0][0] + " record.");
        }catch(Exception err){
            System.out.println("On method processAccu() : "+err);
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
            System.out.println("Hospital_code -> " + this.Hospital_code);
            System.out.println("YYYY -> " + this.YYYY);
            System.out.println("MM -> " + this.MM);
            conn.setStatement();
            conn.insert("DELETE TEMP_GL WHERE YYYY='"+this.YYYY+"' AND HOSPITAL_CODE = '"+this.Hospital_code+"' AND PROCESS = 'GL'");
            conn.insert("DELETE INT_ERP_GL WHERE TYPE='GL' AND YYYY='"+this.YYYY+"' AND MM='"+this.MM+"' AND HOSPITAL_CODE = '"+this.Hospital_code+"'");
            
            message = this.glDebitDf();
            conn.insert(this.glDebitDf());
            message = this.glAccount602103Credit();
            conn.insert(this.glAccount602103Credit());
            conn.insert(this.glAccount602105DebitGuarantee());
            conn.insert(this.glAccount602105DebitAdvanceSomeGuarantee());
            conn.insert(this.glAbsorbDebitGuatantee());
            conn.insert(this.glAbsorbCreditGuatantee()); 
            conn.insert(this.glAccount101202Credit());
            conn.insert(this.glAccount101202CreditAdvanceSomeGuarantee());
            conn.insert(this.glExpense());
            conn.insert(this.glExpense101202());
                        
            conn.commitDB();
            countdata = conn.query("SELECT count(*) as count_table FROM TEMP_GL WHERE HOSPITAL_CODE = '"+this.Hospital_code+"' AND YYYY='"+this.YYYY+"' AND MM='"+this.MM+"'");
            System.out.println("Data gl : " + countdata.length + " record.");
        }catch(Exception err){
            System.out.println("On method processGlAccount() : "+err);
            System.out.println("on sql "+message);
            conn.rollDB();
        }finally{
            insertIntoIntErpGl();
            conn.closeDB("");
            return countdata + "/" + count_insert;
        }
    }
}