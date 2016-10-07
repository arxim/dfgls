package df.bean.guarantee;

import java.sql.SQLException;
import df.bean.db.conn.DBConn;

public class GuaranteeSummaryBean {
    DBConn cdb;
    String[][] temp_table = null;
    String month = "";
    String year = "";
    String hospital = "";
    String result = "";

    public GuaranteeSummaryBean(DBConn cdb){
        try {
            this.cdb = cdb;
            if (this.cdb.getStatement() == null) {
                this.cdb.setStatement();
            }
        } catch (SQLException ex) {
            this.result = ""+ex;
        }
    }
    
    public String getMessage(){
        return this.result;
    }
    
    public boolean summaryProcess(String month, String year, String hospital){
        boolean status = true;
        try {
            //cdb = new ConnectionDB();
            //cdb.setStatement();
            this.month = month;
            this.year = year;
            this.hospital = hospital;
            
            if(sumAmountGuarantee()){
                if(writeSumGuarantee()){
                    if(adjustTax()){
                        if(updateGuaranteeCode()){
                            if(sumMonthGuarantee()){
                                result = "Summary Guarantee Complete\n";
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            status = false;
        }
        return status;
    }
    
    private boolean sumAmountGuarantee(){
        String[][] tmp = null;
        String temp = "";
        boolean status = true;
        String stm = "";
            //Select from Transaction and Sum Amount of Guarantee Seperate by Tax Type, Cash, Credit and Hold
            stm = "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, "+
                  //"ADMISSION_TYPE_CODE, PATIENT_LOCATION_CODE, "+
                  "GUARANTEE_TYPE, "+
                  //tax 406 index 5-7
                  //เกิดจากรายการที่เป็นใบเสร็จส่งมาจากระบบหน้างาน
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='406' AND TRANSACTION_TYPE = 'REV' AND TRANSACTION_MODULE = 'TR' "+
                  "AND YYYY IS NOT NULL) THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_406_CASH, "+
                  
                  //เกิดจากรายการที่เป็นใบแจ้งหนี้แต่มีการรับชำระภายในเดือน
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='406' AND TRANSACTION_TYPE = 'INV' " +
                  "AND (TRANSACTION_MODULE = 'TR' OR (TRANSACTION_MODULE = 'AR' AND PAY_BY_CASH_AR = 'Y')) " +
                  "AND YYYY IS NOT NULL) THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_406_CREDIT, " +
                  
                  //เกิดจากรายการที่เป็นใบแจ้งหนี้และยังไม่ได้รับชำระ
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='406' AND TRANSACTION_TYPE = 'INV' " +
                  "AND TRANSACTION_MODULE = 'TR' AND YYYY IS NULL) " +
                  "THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_406_HOLD, "+
                  
                  //tax 402 index 8-10
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='402' AND TRANSACTION_TYPE = 'REV' AND TRANSACTION_MODULE = 'TR' "+
                  "AND YYYY IS NOT NULL) THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_402_CASH, "+
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='402' AND TRANSACTION_TYPE = 'INV' " +
                  "AND (TRANSACTION_MODULE = 'TR' OR (TRANSACTION_MODULE = 'AR' AND PAY_BY_CASH_AR = 'Y')) " +
                  "AND YYYY IS NOT NULL) THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_402_CREDIT, " +
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='402' AND " +
                  "TRANSACTION_TYPE = 'INV' AND TRANSACTION_MODULE = 'TR' AND YYYY IS NULL) " +
                  "THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_402_HOLD, "+
                  //no tax index 11-13
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='400' AND TRANSACTION_TYPE = 'REV' AND TRANSACTION_MODULE = 'TR' "+
                  "AND YYYY IS NOT NULL) THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_400_CASH, "+
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='400' AND TRANSACTION_TYPE = 'INV' " +
                  "AND (TRANSACTION_MODULE = 'TR' OR (TRANSACTION_MODULE = 'AR' AND PAY_BY_CASH_AR = 'Y')) " +
                  "AND YYYY IS NOT NULL) THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_400_CREDIT, " +
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='400' AND " +
                  "TRANSACTION_TYPE = 'INV' AND TRANSACTION_MODULE = 'TR' AND YYYY IS NULL) " +
                  "THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_400_HOLD "+
                  
                  "FROM TRN_DAILY "+
                  "WHERE GUARANTEE_TERM_YYYY = '"+this.year+"' AND GUARANTEE_TERM_MM = '"+this.month+"' AND " +
                  "HOSPITAL_CODE = '"+this.hospital+"' AND ACTIVE = '1' "+
                  "GROUP BY HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, "+
                  //"ADMISSION_TYPE_CODE, PATIENT_LOCATION_CODE";
                  "GUARANTEE_TYPE";
            //System.out.println(stm);
        tmp = cdb.query(stm);
        try {
            for(int i = 0; i<tmp.length; i++){
                int dfcash = Integer.parseInt(tmp[i][5])+Integer.parseInt(tmp[i][6])+
                             Integer.parseInt(tmp[i][8])+Integer.parseInt(tmp[i][9])+
                             Integer.parseInt(tmp[i][11])+Integer.parseInt(tmp[i][12]);
                int dfhold = Integer.parseInt(tmp[i][7])+
                             Integer.parseInt(tmp[i][10])+
                             Integer.parseInt(tmp[i][13]);
                int dfamount = dfcash+dfhold;
                //Return Message
                temp = "Update STP_GUARANTEE by GUARANTEE_DR_CODE="+tmp[i][1]+"' AND "+
                       "GUARANTEE_CODE="+tmp[i][2];
                //When Select Guarantee Amount finish
                //Update Guarantee Amount in to Guarantee Setup Table
                stm = "UPDATE STP_GUARANTEE SET " +
                      "DF406_CASH_AMOUNT = '"+tmp[i][5]+"', "+
                      "DF406_CREDIT_AMOUNT = '"+tmp[i][6]+"', "+
                      "DF406_HOLD_AMOUNT = '"+tmp[i][7]+"', "+
                      "DF402_CASH_AMOUNT = '"+tmp[i][8]+"', "+
                      "DF402_CREDIT_AMOUNT = '"+tmp[i][9]+"', "+
                      "DF402_HOLD_AMOUNT = '"+tmp[i][10]+"', "+
                      "DF400_CASH_AMOUNT = '"+tmp[i][11]+"', "+
                      "DF400_CREDIT_AMOUNT = '"+tmp[i][12]+"', "+
                      "DF400_HOLD_AMOUNT = '"+tmp[i][13]+"', "+
                      "DF_CASH_AMOUNT = '"+dfcash+"', "+
                      "DF_HOLD_AMOUNT = '"+dfhold+"', "+
                      //Prepare Absorb Amount by Tax Type
                      //DF406_ABSORB_AMOUNT = หาก (เงินการันตี - การันตีรวมเงิน) - DFที่เป็นเงินสด น้อยกว่า 0 DF406_ABSORB_AMOUNT จะเท่ากับ 0
                      //หากมากกว่า 0 และ (เงินการันตี - การันตีรวมเงิน) - (DFที่เป็นเงินสด + DFที่เป็น Hold)
                      //หากน้อยกว่า 0 จะเท่ากับ เงินการันตี - การันตีรวมเงิน - DFที่เป็นเงินสด
                      "DF406_ABSORB_AMOUNT = (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT) - "+dfcash+") <= 0 "+
                      "THEN '0' ELSE (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT) - ("+dfcash+"+"+Integer.parseInt(tmp[i][7])+")) <= 0 "+
                      "THEN (GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT) - DF_CASH_AMOUNT ELSE "+tmp[i][7]+" END) END), "+
                      //
                      "DF402_ABSORB_AMOUNT = (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT)-(DF_CASH_AMOUNT+DF406_ABSORB_AMOUNT)) <=0 " +
                      "THEN '0' ELSE (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT)-(DF_CASH_AMOUNT+"+Integer.parseInt(tmp[i][10])+"+"+
                      "DF406_ABSORB_AMOUNT)) <=0 THEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT)-(DF_CASH_AMOUNT+DF406_ABSORB_AMOUNT)) ELSE "+
                      Integer.parseInt(tmp[i][10])+" END) END), "+
                      //
                      "DF400_ABSORB_AMOUNT = (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT)-(DF_CASH_AMOUNT+DF406_ABSORB_AMOUNT+DF402_ABSORB_AMOUNT)) <=0 "+
                      "THEN 0 ELSE (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT)-(DF_CASH_AMOUNT+DF406_ABSORB_AMOUNT+DF402_ABSORB_AMOUNT+"+tmp[i][13]+"))<=0 " +
                      "THEN (GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT)-(DF_CASH_AMOUNT+DF406_ABSORB_AMOUNT+DF402_ABSORB_AMOUNT) ELSE "+tmp[i][13]+" END) END), "+
                      //
                      "DF_ABSORB_AMOUNT = DF400_ABSORB_AMOUNT+DF402_ABSORB_AMOUNT+DF406_ABSORB_AMOUNT, "+
                      "HP402_ABSORB_AMOUNT = (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT) - "+dfamount+") < 0 " +
                      "THEN '0' ELSE (GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT) - "+dfamount+" END), "+
                      //"SUM_TAX_406 = DF406_ABSORB_AMOUNT+GUARANTEE_FIX_AMOUNT, "+
                      //"SUM_TAX_402 = DF402_ABSORB_AMOUNT+HP402_ABSORB_AMOUNT+GUARANTEE_INCLUDE_AMOUNT+GUARANTEE_EXCLUDE_AMOUNT, "+
                      "SUM_TAX_400 = DF400_ABSORB_AMOUNT "+
                      "WHERE HOSPITAL_CODE = '"+tmp[i][0]+"' AND "+
                      "GUARANTEE_DR_CODE = '"+tmp[i][1]+"' AND "+
                      "GUARANTEE_CODE = '"+tmp[i][2]+"' AND "+
                      "GUARANTEE_TYPE = '"+tmp[i][3]+"'";
                      //"ADMISSION_TYPE_CODE = '"+tmp[i][3]+"' AND "+
                      //"GUARANTEE_LOCATION = '"+tmp[i][4]+"'";
                //System.out.println(stm);
                cdb.insert(stm);
            }
            cdb.commitDB();
        } catch (Exception ex) {
            status = false;
            result = "Update calculate guarantee amount error : \n"+ex+
                     "\nCause "+temp;
            cdb.rollDB();
        }
        return status;
    }
    
    private boolean writeSumGuarantee(){
        //When Set up Guarantee 1 Doctor Personal for 2 Doctor Code
        //need to separate tax for 2 Doctor
        String temp = "";
        String stm = "";
        String upd = "";
        String stm2 = "";
        boolean status = true;
        String[][] tb = null;
                  //Insert first  of transaction
            stm = "INSERT INTO STP_SUM_GUARANTEE (HOSPITAL_CODE, GUARANTEE_DR_CODE, YYYY, MM, "+
                  "GUARANTEE_CODE, DOCTOR_CODE, GUARANTEE_TYPE, " +
                  //"ADMISSION_TYPE_CODE, GUARANTEE_LOCATION, "+
                  "DF406_CASH_AMOUNT, DF406_CREDIT_AMOUNT, DF406_HOLD_AMOUNT, "+
                  "DF402_CASH_AMOUNT, DF402_CREDIT_AMOUNT, DF402_HOLD_AMOUNT, "+
                  "DF400_CASH_AMOUNT, DF400_CREDIT_AMOUNT, DF400_HOLD_AMOUNT) "+
                  
                  "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_TERM_YYYY, GUARANTEE_TERM_MM, "+
                  "GUARANTEE_CODE, DOCTOR_CODE, " +
                  //"ADMISSION_TYPE_CODE, PATIENT_LOCATION_CODE, "+
                  
                  //tax 406
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='406' AND TRANSACTION_TYPE = 'REV' AND TRANSACTION_MODULE = 'TR' "+
                  "AND YYYY IS NOT NULL) THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_406_CASH, "+
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='406' AND TRANSACTION_TYPE = 'INV' " +
                  "AND (TRANSACTION_MODULE = 'TR' OR (TRANSACTION_MODULE = 'AR' AND PAY_BY_CASH_AR = 'Y')) " +
                  "AND YYYY IS NOT NULL) THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_406_CREDIT, " +
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='406' AND TRANSACTION_TYPE = 'INV' " +
                  "AND TRANSACTION_MODULE = 'TR' AND YYYY IS NULL) " +
                  "THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_406_HOLD, "+
                  
                  //tax 402
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='402' AND TRANSACTION_TYPE = 'REV' AND TRANSACTION_MODULE = 'TR' "+
                  "AND YYYY IS NOT NULL) THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_402_CASH, "+
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='402' AND TRANSACTION_TYPE = 'INV' " +
                  "AND (TRANSACTION_MODULE = 'TR' OR (TRANSACTION_MODULE = 'AR' AND PAY_BY_CASH_AR = 'Y')) " +
                  "AND YYYY IS NOT NULL) THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_402_CREDIT, " +
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='402' AND " +
                  "TRANSACTION_TYPE = 'INV' AND TRANSACTION_MODULE = 'TR' AND YYYY IS NULL) " +
                  "THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_402_HOLD, "+
                  
                  //no tax
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='400' AND TRANSACTION_TYPE = 'REV' AND TRANSACTION_MODULE = 'TR' "+
                  "AND YYYY IS NOT NULL) THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_400_CASH, "+
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='400' AND TRANSACTION_TYPE = 'INV' " +
                  "AND (TRANSACTION_MODULE = 'TR' OR (TRANSACTION_MODULE = 'AR' AND PAY_BY_CASH_AR = 'Y')) " +
                  "AND YYYY IS NOT NULL) THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_400_CREDIT, " +
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='400' AND " +
                  "TRANSACTION_TYPE = 'INV' AND TRANSACTION_MODULE = 'TR' AND YYYY IS NULL) " +
                  "THEN GUARANTEE_AMT ELSE '0' END) AS SUM_TAX_400_HOLD "+
                  
                  "FROM TRN_DAILY "+
                  "WHERE GUARANTEE_TERM_YYYY = '"+this.year+"' AND GUARANTEE_TERM_MM = '"+this.month+"' AND " +
                  "HOSPITAL_CODE = '"+this.hospital+"' AND ACTIVE = '1' "+
                  "GROUP BY HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_TERM_YYYY, GUARANTEE_TERM_MM, "+
                  "GUARANTEE_CODE, DOCTOR_CODE, GUARANTEE_TYPE";
                  //"ADMISSION_TYPE_CODE, PATIENT_LOCATION_CODE";
            
            try {
                //System.out.println(stm);
                cdb.insert(stm);
                cdb.commitDB();
                
                //Update Setup From STP_GUARANTEE To STP_SUM_GUARANTEE
                stm2= "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, YYYY, MM, "+
                      "GUARANTEE_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, " +
                      "GUARANTEE_FIX_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT, "+
                      "GUARANTEE_TYPE "+
                      "FROM STP_GUARANTEE WHERE YYYY = '"+this.year+"' AND MM = '"+this.month+"'";
                tb = cdb.query(stm2);
                for(int i = 0; i<tb.length; i++){
                    temp = "Guarantee_Dr_Code="+tb[i][1]+" AND Guarantee_Code="+tb[i][2];
                    String s = "UPDATE STP_SUM_GUARANTEE SET GUARANTEE_AMOUNT = '"+tb[i][5]+"', "+
                               "GUARANTEE_INCLUDE_AMOUNT = '"+tb[i][6]+"', "+
                               "GUARANTEE_FIX_AMOUNT = '" +tb[i][7]+"', "+
                               "GUARANTEE_EXCLUDE_AMOUNT = '"+tb[i][8]+"' "+
                               "WHERE HOSPITAL_CODE = '"+tb[i][0]+"' AND GUARANTEE_DR_CODE = '"+tb[i][1]+"' "+
                               "AND GUARANTEE_CODE = '"+tb[i][2]+"' AND YYYY = '"+tb[i][3]+"' "+
                               "AND MM = '"+tb[i][4]+"' AND GUARANTEE_TYPE = '"+tb[i][9]+"'";
                    cdb.insert(s);
                }
                cdb.commitDB();
                //----------------------------------------------------
            } catch (SQLException ex) {
                result = "Update setup guarantee amount error : \n"+ex+
                         "\nCause "+temp;
                status = false;
                cdb.rollDB();
            }
            
            upd = "UPDATE STP_SUM_GUARANTEE SET "+
                  "DF_CASH_AMOUNT = DF406_CASH_AMOUNT+DF406_CREDIT_AMOUNT+DF402_CASH_AMOUNT+" +
                  "DF402_CREDIT_AMOUNT+DF400_CASH_AMOUNT+DF400_CREDIT_AMOUNT, "+
                  //
                  "DF_HOLD_AMOUNT = DF406_HOLD_AMOUNT+DF402_HOLD_AMOUNT+DF400_HOLD_AMOUNT, "+
                  //
                  "DF406_ABSORB_AMOUNT = (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT) - DF_CASH_AMOUNT) <= 0 "+
                  "THEN '0' ELSE (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT) - (DF_CASH_AMOUNT+DF406_HOLD_AMOUNT)) <= 0 "+
                  "THEN (GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT) - DF_CASH_AMOUNT ELSE DF406_HOLD_AMOUNT END) END), "+
                  //
                  "DF402_ABSORB_AMOUNT = (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT)-(DF_CASH_AMOUNT+DF406_ABSORB_AMOUNT)) <=0 " +
                  "THEN '0' ELSE (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT)-(DF_CASH_AMOUNT+DF402_HOLD_AMOUNT+"+
                  "DF406_ABSORB_AMOUNT)) <=0 THEN (GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT)-(DF_CASH_AMOUNT+DF406_ABSORB_AMOUNT) ELSE "+
                  "DF402_HOLD_AMOUNT END) END), "+
                  //
                  "DF400_ABSORB_AMOUNT = (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT)-(DF_CASH_AMOUNT+DF406_ABSORB_AMOUNT+DF402_ABSORB_AMOUNT)) <=0 "+
                  "THEN 0 ELSE (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT)-(DF_CASH_AMOUNT+DF406_ABSORB_AMOUNT+DF402_ABSORB_AMOUNT+DF400_HOLD_AMOUNT))<=0 " +
                  "THEN (GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT)-(DF_CASH_AMOUNT+DF406_ABSORB_AMOUNT+DF402_ABSORB_AMOUNT) ELSE DF400_HOLD_AMOUNT END) END), "+
                  //
                  "DF_ABSORB_AMOUNT = DF400_ABSORB_AMOUNT+DF402_ABSORB_AMOUNT+DF406_ABSORB_AMOUNT, "+
                  //
                  "HP402_ABSORB_AMOUNT = (CASE WHEN ((GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT) - (DF_CASH_AMOUNT+DF_HOLD_AMOUNT)) < 0 " +
                  "THEN '0' ELSE (GUARANTEE_AMOUNT-GUARANTEE_INCLUDE_AMOUNT) - (DF_CASH_AMOUNT+DF_HOLD_AMOUNT) END), "+
                  //
                  //"SUM_TAX_406 = DF406_ABSORB_AMOUNT+GUARANTEE_FIX_AMOUNT, "+
                  //"SUM_TAX_402 = DF402_ABSORB_AMOUNT+HP402_ABSORB_AMOUNT+GUARANTEE_INCLUDE_AMOUNT+GUARANTEE_EXCLUDE_AMOUNT, "+
                  "SUM_TAX_400 = DF400_ABSORB_AMOUNT "+
                  "WHERE HOSPITAL_CODE = '"+this.hospital+"' AND YYYY = '"+this.year+"' AND MM = '"+this.month+"'";
        try {
            //System.out.println(stm);
            cdb.insert(upd);
            cdb.commitDB();
        } catch (SQLException ex) {
            result = "Update process tax condition error : \n"+ex;
            status = false;
            cdb.rollDB();
        }
        return status;
    }

    private boolean adjustTax(){
        boolean status = true;
        String temp = "";
        //Adjust Tax
        String stm = "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, YYYY, MM, "+ //index 0-4
                     "SUM_TAX_406, SUM_TAX_402, SUM_TAX_400, HP402_ABSORB_AMOUNT, " + //index 5-8
                     "DF406_ABSORB_AMOUNT, DF402_ABSORB_AMOUNT, DF400_ABSORB_AMOUNT, "+ //index 9-11
                     "DF406_HOLD_AMOUNT, DF402_HOLD_AMOUNT, DF400_HOLD_AMOUNT, "+ //index 12-14
                     "GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT "+ //index 15-17
                     "FROM STP_GUARANTEE WHERE YYYY = '"+this.year+"' AND MM = '"+this.month+"' "+
                     "ORDER BY GUARANTEE_DR_CODE";
        String sum = "SELECT HOSPITAL_CODE, GUARANTEE_DR_CODE, GUARANTEE_CODE, YYYY, MM, "+ //index 0-4
                     "DOCTOR_CODE, SUM_TAX_406, SUM_TAX_402, SUM_TAX_400, HP402_ABSORB_AMOUNT, "+ //index 5-9
                     "DF406_ABSORB_AMOUNT, DF402_ABSORB_AMOUNT, DF400_ABSORB_AMOUNT, "+ //index 10-12
                     "DF406_HOLD_AMOUNT, DF402_HOLD_AMOUNT, DF400_HOLD_AMOUNT, "+ //index 13-15
                     "GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT "+ //index 16-18
                     "FROM STP_SUM_GUARANTEE WHERE YYYY = '"+this.year+"' AND MM = '"+this.month+"' "+
                     "ORDER BY GUARANTEE_DR_CODE, DOCTOR_CODE";
                     //GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT
                     //"SUM_TAX_406 = DF406_ABSORB_AMOUNT+GUARANTEE_FIX_AMOUNT, "+
                     //"SUM_TAX_402 = DF402_ABSORB_AMOUNT+HP402_ABSORB_AMOUNT+GUARANTEE_INCLUDE_AMOUNT+GUARANTEE_EXCLUDE_AMOUNT, "+
                     //"SUM_TAX_400 = DF400_ABSORB_AMOUNT "+
        String up = "";
        String[][] tb_stp = null;
        String[][] tb_sum = null;
        tb_stp = cdb.query(stm);
        tb_sum = cdb.query(sum);
        try {
            for(int i = 0; i<tb_stp.length; i++){
                for(int x = 0; x<tb_sum.length; x++){
           
                    //if dr_guarantee setup and dr_guarantee sum is equal
                    //ทำการปรับรายการภาษีตามรายแพทย์
                    if(tb_stp[i][1].equals(tb_sum[x][1])){
                        
                        //Set DF406_ABSORB_AMOUNT
                        //if df hold 406 in sum greater than 0
                        if(Double.parseDouble(tb_sum[x][13]) > 0){
                            //if df absorb 406 in sum greater than df absorb 406 in setup
                            if(Double.parseDouble(tb_sum[x][10]) > Double.parseDouble(tb_stp[i][9])){
                                //assign df absorb in sum with df absorb in setup
                                tb_sum[x][10] = tb_stp[i][9];
                                //clear df absorb in setup = 0
                                tb_stp[i][9] = "0";
                            }
                        }else{
                            tb_sum[x][10] = "0";
                        }
                        //Set DF402_ABSORB_AMOUNT
                        //if df hold 402 in sum greater than 0
                        if(Double.parseDouble(tb_sum[x][14]) > 0){
                            //if df absorb 402 in sum greater than df absorb 402 in setup
                            if(Double.parseDouble(tb_sum[x][11]) > Double.parseDouble(tb_stp[i][10])){
                                tb_sum[x][11] = tb_stp[i][10];
                                tb_stp[i][10] = "0";
                            }
                        }else{
                            tb_sum[x][11] = "0";
                        }
                        //Set DF400_ABSORB_AMOUNT
                        //if df hold 400 in sum greater than 0
                        if(Double.parseDouble(tb_sum[x][15]) > 0){
                            //if df absorb 400 in sum greater than df absorb 400 in setup
                            if(Double.parseDouble(tb_sum[x][12]) > Double.parseDouble(tb_stp[i][11])){
                                tb_sum[x][12] = tb_stp[i][11];
                                tb_stp[i][11] = "0";
                            }
                        }else{
                            tb_sum[x][12] = "0";
                        }
                        
                        //Adjust Tax
                        //if dr code in sum equal dr guarantee code in setup
                        if(tb_sum[x][5].equals(tb_stp[i][1])){
                            //HP402_ABSORB_AMOUNT in sum = HP402_ABSORB_AMOUNT in setup
                            tb_sum[x][9] = tb_stp[i][8];
                            //TAX406 = DF406_ABSORB_AMOUNT + GUARANTEE_FIX_AMOUNT
                            tb_sum[x][6] = ""+Double.parseDouble(tb_sum[x][10])+Double.parseDouble(tb_sum[x][16]);
                            //TAX402 = DF402_ABSORB_AMOUNT + HP402_ABSORB_AMOUNT + GUARANTEE_INCLUDE_AMOUNT + GUARANTEE_EXCLUDE_AMOUNT
                            tb_sum[x][7] = ""+Double.parseDouble(tb_sum[x][11])+Double.parseDouble(tb_sum[x][9])+
                                              Double.parseDouble(tb_sum[x][17])+Double.parseDouble(tb_sum[x][18]);
                            //TAX400 = DF400_ABSORB_AMOUNT
                            tb_sum[x][8] = ""+Double.parseDouble(tb_sum[x][12]);
                        }else{
                            tb_sum[x][9] = "0";
                            //TAX406 = DF406_ABSORB_AMOUNT
                            tb_sum[x][6] = ""+Double.parseDouble(tb_sum[x][10]);
                            //TAX402 = DF402_ABSORB_AMOUNT
                            tb_sum[x][7] = ""+Double.parseDouble(tb_sum[x][11]);
                            //TAX400 = DF400_ABSORB_AMOUNT
                            tb_sum[x][8] = ""+Double.parseDouble(tb_sum[x][12]);
                        }
                        //end Master of code           
                    }
                    //Message Detail Error
                    temp = "Guarantee_Dr_Code="+tb_sum[x][1]+" And Doctor_Code="+tb_sum[x][5];
                    up = "UPDATE STP_SUM_GUARANTEE SET " +
                         "SUM_TAX_406 = '"+tb_sum[x][6]+"', "+
                         "SUM_TAX_402 = '"+tb_sum[x][7]+"', " +
                         "SUM_TAX_400 = '"+tb_sum[x][8]+"', "+
                         "HP402_ABSORB_AMOUNT = '"+tb_sum[x][9]+"', "+
                         "DF406_ABSORB_AMOUNT = '"+tb_sum[x][10]+"', "+
                         "DF402_ABSORB_AMOUNT = '"+tb_sum[x][11]+"', "+
                         "DF400_ABSORB_AMOUNT = '"+tb_sum[x][12]+"', "+
                         "DF_ABSORB_AMOUNT = DF406_ABSORB_AMOUNT+DF402_ABSORB_AMOUNT+DF400_ABSORB_AMOUNT "+
                         "WHERE HOSPITAL_CODE = '"+tb_sum[x][0]+"' AND GUARANTEE_DR_CODE = '"+tb_sum[x][1]+"' "+
                         "AND GUARANTEE_CODE = '"+tb_sum[x][2]+"' AND YYYY = '"+tb_sum[x][3]+"' "+
                         "AND MM = '"+tb_sum[x][4]+"' AND DOCTOR_CODE = '"+tb_sum[x][5]+"'";
                    //System.out.println(up);
                    cdb.insert(up);
                }
            }
            cdb.commitDB();
        } catch (Exception ex) {
            cdb.rollDB();
            status = false;
            result = "Update Adjust Tax error : \n"+ex+
                     "\nCause "+temp;
        }
        return status;
    }
    
    private boolean updateGuaranteeCode(){
        boolean status = true;
        
        String stm = "INSERT INTO STP_SUM_GUARANTEE"+
                     "(HOSPITAL_CODE, GUARANTEE_DR_CODE, DOCTOR_CODE, GUARANTEE_CODE, "+
                     "ADMISSION_TYPE_CODE, GUARANTEE_LOCATION, YYYY, MM, GUARANTEE_AMOUNT, "+
                     "GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT, "+
                     "DF406_CASH_AMOUNT, DF406_CREDIT_AMOUNT, DF406_HOLD_AMOUNT, "+
                     "DF402_CASH_AMOUNT, DF402_CREDIT_AMOUNT, DF402_HOLD_AMOUNT, "+
                     "DF400_CASH_AMOUNT, DF400_CREDIT_AMOUNT, DF400_HOLD_AMOUNT, "+
                     "DF_CASH_AMOUNT, DF_HOLD_AMOUNT, DF406_ABSORB_AMOUNT, DF402_ABSORB_AMOUNT, "+
                     "DF400_ABSORB_AMOUNT, DF_ABSORB_AMOUNT, HP402_ABSORB_AMOUNT," +
                     "SUM_HP_OVER_AMOUNT, GUARANTEE_PAID_AMOUNT, "+
                     "SUM_TAX_406, SUM_TAX_402, SUM_TAX_400) "+
                     
                     "SELECT STP.HOSPITAL_CODE, STP.GUARANTEE_DR_CODE, STP.GUARANTEE_DR_CODE, STP.GUARANTEE_CODE, "+
                     "STP.ADMISSION_TYPE_CODE, STP.GUARANTEE_LOCATION, STP.YYYY, STP.MM, STP.GUARANTEE_AMOUNT, "+
                     "STP.GUARANTEE_FIX_AMOUNT, STP.GUARANTEE_INCLUDE_AMOUNT, STP.GUARANTEE_EXCLUDE_AMOUNT, "+
                     "STP.DF406_CASH_AMOUNT, STP.DF406_CREDIT_AMOUNT, STP.DF406_HOLD_AMOUNT, "+
                     "STP.DF402_CASH_AMOUNT, STP.DF402_CREDIT_AMOUNT, STP.DF402_HOLD_AMOUNT, "+
                     "STP.DF400_CASH_AMOUNT, STP.DF400_CREDIT_AMOUNT, STP.DF400_HOLD_AMOUNT, "+
                     "STP.DF_CASH_AMOUNT, STP.DF_HOLD_AMOUNT, 0, 0, 0, "+
                     "STP.DF_ABSORB_AMOUNT, STP.HP402_ABSORB_AMOUNT," +
                     "STP.SUM_HP_OVER_AMOUNT, STP.GUARANTEE_PAID_AMOUNT, "+
                     "STP.GUARANTEE_FIX_AMOUNT, STP.HP402_ABSORB_AMOUNT+STP.GUARANTEE_INCLUDE_AMOUNT+" +
                     "STP.GUARANTEE_EXCLUDE_AMOUNT, '0' "+
                     "FROM STP_GUARANTEE STP WHERE STP.GUARANTEE_DR_CODE NOT IN" +
                     "(SELECT GUARANTEE_DR_CODE FROM STP_SUM_GUARANTEE " +
                     "WHERE GUARANTEE_DR_CODE = DOCTOR_CODE) "+
                     "AND STP.HOSPITAL_CODE = '"+this.hospital+"' AND STP.YYYY = '"+this.year+"' AND STP.MM = '"+this.month+"' ";
        try {
            cdb.insert(stm);
        } catch (SQLException ex) {
            cdb.rollDB();
            status = false;
            System.out.println("Update Guarantee Code : "+ex);
        }
        return status;
    }
    
    private boolean sumMonthGuarantee(){
        boolean status = true;
        String stm ="INSERT INTO SUMMARY_GUARANTEE " +
                    "(HOSPITAL_CODE, DOCTOR_CODE, YYYY, MM, "+
                    //"ADMISSION_TYPE_CODE, GUARANTEE_LOCATION, GUARANTEE_AMOUNT, "+
                    //"GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT, "+
                    //"DF406_CASH_AMOUNT, DF406_CREDIT_AMOUNT, DF406_HOLD_AMOUNT, "+
                    //"DF402_CASH_AMOUNT, DF402_CREDIT_AMOUNT, DF402_HOLD_AMOUNT, "+
                    //"DF400_CASH_AMOUNT, DF400_CREDIT_AMOUNT, DF400_HOLD_AMOUNT, "+
                    //"DF_CASH_AMOUNT, DF_HOLD_AMOUNT, "+
                    "DF406_ABSORB_AMOUNT, DF402_ABSORB_AMOUNT, DF400_ABSORB_AMOUNT, " +
                    //"DF_ABSORB_AMOUNT, HP402_ABSORB_AMOUNT," +
                    "SUM_HP_OVER_AMOUNT, "+//GUARANTEE_PAID_AMOUNT, "+
                    "SUM_TAX_406, SUM_TAX_402, SUM_TAX_400) "+
                     
                    "SELECT HOSPITAL_CODE, DOCTOR_CODE, YYYY, MM, "+
                    //"SUM(DF406_CASH_AMOUNT), SUM(DF406_CREDIT_AMOUNT), SUM(DF406_HOLD_AMOUNT), "+
                    //"SUM(DF402_CASH_AMOUNT), SUM(DF402_CREDIT_AMOUNT), SUM(DF402_HOLD_AMOUNT), "+
                    //"SUM(DF400_CASH_AMOUNT), SUM(DF400_CREDIT_AMOUNT), SUM(DF400_HOLD_AMOUNT), "+
                    //"SUM(DF_CASH_AMOUNT), SUM(DF_HOLD_AMOUNT), " +
                    "SUM(DF406_ABSORB_AMOUNT), SUM(DF402_ABSORB_AMOUNT), SUM(DF400_ABSORB_AMOUNT), " +
                    //"SUM(DF_ABSORB_AMOUNT), SUM(HP402_ABSORB_AMOUNT)," +
                    "SUM(SUM_HP_OVER_AMOUNT), "+ //SUM(GUARANTEE_PAID_AMOUNT), "+
                    "SUM(SUM_TAX_406), SUM(SUM_TAX_402), SUM(SUM_TAX_400) " +
                    "FROM STP_SUM_GUARANTEE "+
                    "WHERE HOSPITAL_CODE = '"+this.hospital+"' AND YYYY = '"+this.year+"' AND MM = '"+this.month+"' "+
                    "GROUP BY HOSPITAL_CODE, DOCTOR_CODE, YYYY, MM";
        try {
            cdb.insert(stm);
        } catch (SQLException ex) {
            cdb.rollDB();
            status = false;
            System.out.println("Update Summary Month Guarantee : "+ex);
        }
        return status;
    }
}

