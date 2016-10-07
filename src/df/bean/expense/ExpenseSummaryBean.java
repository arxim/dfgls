/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.expense;
import df.bean.db.conn.DBConn;
/**
 *
 * @author nopphadon
 */
public class ExpenseSummaryBean {
    DBConn cdb;
    String[][] temp_table = null;
    String month = "";
    String year = "";
    String hospital = "";
    String result = "";
    public ExpenseSummaryBean(DBConn cdb){
        try {
            this.cdb = cdb;
            if (this.cdb.getStatement() == null) {
                this.cdb.setStatement();
            }
        } catch (Exception ex) {
            this.result = ""+ex;
        }
    }
    
    public String getMessage(){
        return this.result;
    }
    public boolean summaryProcess(String month, String year, String hospital, String process_type){
        boolean status = true;
        try {
            //cdb = new ConnectionDB();
            //cdb.setStatement();
            this.month = month;
            this.year = year;
            this.hospital = hospital;
            
            if(process_type.equals("Remove Expense Transaction")){
                System.out.println("Remove Expense Transaction Process ");
                status = deleteExpense();
            }
            if(process_type.equals("Summary Expense Transaction")){
                System.out.println("Summary Expense Transaction Process ");
                status = summaryExpense();
            }
        
        } catch (Exception ex) {
            System.out.println(ex);
            status = false;
        }
        return status;
    }
    
    private boolean deleteExpense(){
        boolean status = true;
        String stm ="DELETE FROM TRN_EXPENSE_HEADER "+
                    "WHERE HOSPITAL_CODE = '"+this.hospital+"' AND YYYY = '"+this.year+"' AND MM = '"+this.month+"'";
        System.out.println(stm);
        try {
            cdb.insert(stm);
            cdb.commitDB();
        } catch (Exception ex) {
            cdb.rollDB();
            status = false;
            System.out.println("Delete Expense Header Error : "+ex);
        }
        return status;
    }
    
    public boolean summaryExpense(){
        String[][] tmp = null;
        String temp = "";
        boolean status = true;
        String stm = "";
            //Select from Transaction and Sum Amount of Guarantee Seperate by Tax Type, Cash, Credit and Hold
            stm = "INSERT INTO TRN_EXPENSE_HEADER " +
                  "(HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, "+
                  "CREDIT_TOTAL_AMOUNT, DEBIT_TOTAL_AMOUNT, DEBIT_406_AMOUNT, "+
                  "CREDIT_406_AMOUNT, DEBIT_402_AMOUNT, CREDIT_402_AMOUNT, "+
                  "DEBIT_401_AMOUNT, CREDIT_401_AMOUNT, DEBIT_400_AMOUNT, "+
                  "CREDIT_400_AMOUNT) "+
                    
                  "SELECT HOSPITAL_CODE, YYYY, MM , DOCTOR_CODE, "+
                  
                  "SUM(CASE WHEN EXPENSE_SIGN = '-1' THEN AMOUNT ELSE '0' END) "+
                  "AS CREDIT_AMOUNT, "+
                  "SUM(CASE WHEN EXPENSE_SIGN = '1' THEN AMOUNT ELSE '0' END) "+
                  "AS DEBIT_AMOUNT, "+
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='406' AND EXPENSE_SIGN = '1') " +
                  "THEN TAX_AMOUNT ELSE '0' END) AS DEBIT_406_AMOUNT, "+
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='406' AND EXPENSE_SIGN = '-1') " +
                  "THEN TAX_AMOUNT ELSE '0' END) AS CREDIT_406_AMOUNT, "+
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='402' AND EXPENSE_SIGN = '1') " +
                  "THEN TAX_AMOUNT ELSE '0' END) AS DEBIT_402_AMOUNT, "+
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='402' AND EXPENSE_SIGN = '-1') " +
                  "THEN TAX_AMOUNT ELSE '0' END) AS CREDIT_402_AMOUNT, "+
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='401' AND EXPENSE_SIGN = '1') " +
                  "THEN TAX_AMOUNT ELSE '0' END) AS DEBIT_401_AMOUNT, "+
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='401' AND EXPENSE_SIGN = '-1') " +
                  "THEN TAX_AMOUNT ELSE '0' END) AS CREDIT_401_AMOUNT, "+
                  
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='400' AND EXPENSE_SIGN = '1') " +
                  "THEN TAX_AMOUNT ELSE '0' END) AS DEBIT_400_AMOUNT, "+
                  "SUM(CASE WHEN (TAX_TYPE_CODE ='400' AND EXPENSE_SIGN = '-1') " +
                  "THEN TAX_AMOUNT ELSE '0' END) AS CREDIT_400_AMOUNT "+
                  
                  "FROM TRN_EXPENSE_DETAIL "+
                  "WHERE YYYY = '"+this.year+"' AND MM = '"+this.month+"' AND " +
                  "HOSPITAL_CODE = '"+this.hospital+"'"+
                  "GROUP BY HOSPITAL_CODE, YYYY, MM , DOCTOR_CODE";
        try {
            System.out.println("\nExpense Summary : "+stm);
            cdb.insert(stm);
            cdb.commitDB();
        } catch (Exception ex) {
            System.out.println("\nExpense Summary Fail : "+ex);
            status = false;
            result = "Insert Expense Header error : \n"+ex+
                     "\nCause "+temp;
            cdb.rollDB();
        }
        return status;
    }
}
