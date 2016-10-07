package df.bean.interfacefile;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;

/**
 *
 * @author 
 */
public class ImportNewMasterBean {
    private DBConn cdb;
    private String invoice_date;
    private String hospital_code;
    private String message;
    
    public boolean ImportNewMaster(String invoice_date, String hospital_code, DBConn dc){
        boolean status = true;
        try {
            this.cdb = dc;
            this.invoice_date = invoice_date;
            this.hospital_code = hospital_code;
            if (this.cdb.getStatement() == null) {
                this.cdb.setStatement();
            }else{
                System.out.print( Variables.IS_TEST ? "Connection not null : "+this.cdb.getConnection()+"\n" : "" );
                System.out.print( Variables.IS_TEST ? "Statement not null : "+this.cdb.getStatement()+"\n" : "" );
            }
            try{//insert order item into master
                this.cdb.insert(insertOrderItem());
                this.cdb.commitDB();
            }catch(Exception e){
                this.message = this.message+" : Order Item"+e;
                this.cdb.rollDB();
            }
            
            try{//insert payor office category into master
                this.cdb.insert(insertPayorCategory());
                this.cdb.commitDB();
            }catch(Exception e){
                this.cdb.rollDB();
                this.message = this.message+" : Payor Category"+e;
            }
            
            try{//insert payor office into master
                this.cdb.insert(insertPayor());
                this.cdb.commitDB();
            }catch(Exception e){
                this.cdb.rollDB();
                this.message = this.message+" : Payor"+e;
            }
            try{//insert Receipt Type into master
                this.cdb.insert(insertReceiptType());
                this.cdb.commitDB();
            }catch(Exception e){
                this.cdb.rollDB();
                this.message = this.message+" : Receipt Type"+e;
            }
            
        } catch (SQLException ex) {
            this.message = "Can't insert Order Item, Payor Category and Payor Office\n"+ex.getMessage();
            status = false;
        }
        return status;
    }
    
    public String getMessage(){
        return this.message;
    }
    
    private String insertDoctorProfile(){
        String stm = "INSERT INTO DOCTOR_PROFILE (HOSPITAL_CODE, CODE, NAME_THAI, ACTIVE, " +
                     "UPDATE_DATE, USER_ID) "+
                
                     "SELECT DISTINCT HOSPITAL_CODE, DOCTOR_PROFILE_CODE, DOCTOR_PROFILE_NAME, '0', " +
                     "'"+this.invoice_date+"', 'Interface' "+
                     "FROM INT_HIS_BILL WHERE DOCTOR_PROFILE_CODE NOT IN (SELECT CODE FROM DOCTOR_PROFILE" +
                     " WHERE HOSPITAL_CODE = '"+this.hospital_code+"') AND " +
                     "BILL_DATE = '"+this.invoice_date+"' AND DOCTOR_PROFILE_CODE <> '' "+
                     "AND HOSPITAL_CODE = '"+this.hospital_code+"'";
        System.out.print( Variables.IS_TEST ? "Insert DP : "+stm+"\n" : "" );
        return stm;
    }
    private String insertDoctor(){
        String stm = "INSERT INTO DOCTOR (HOSPITAL_CODE, DOCTOR_PROFILE_CODE, " +
        			 "CODE, NAME_THAI, ACTIVE, UPDATE_DATE, USER_ID) "+
                
                     "SELECT DISTINCT HOSPITAL_CODE, DOCTOR_PROFILE_CODE, DOCTOR_CODE, DOCTOR_NAME, '0', " +
                     "'"+this.invoice_date+"', 'Interface' "+
                     "FROM INT_HIS_BILL WHERE DOCTOR_CODE NOT IN (SELECT CODE FROM DOCTOR" +
                     " WHERE HOSPITAL_CODE = '"+this.hospital_code+"') AND " +
                     "BILL_DATE = '"+this.invoice_date+"' AND DOCTOR_PROFILE_CODE <> '' AND DOCTOR_CODE <> '' " +
                     "AND HOSPITAL_CODE = '"+this.hospital_code+"'";
        System.out.print( Variables.IS_TEST ? "Insert DR : "+stm+"\n" : "" );
        return stm;
    }
    private String insertOrderItem(){
        String stm = "INSERT INTO ORDER_ITEM (HOSPITAL_CODE, CODE, DESCRIPTION_THAI, ACTIVE, " +
                     "DESCRIPTION_ENG, IS_COMPUTE, IS_ALLOC_FULL_TAX, IS_GUARANTEE, "+
                     "EXCLUDE_TREATMENT, TAX_TYPE_CODE, UPDATE_DATE, USER_ID) "+
                     
                     "SELECT DISTINCT HOSPITAL_CODE, ORDER_ITEM_CODE, ORDER_ITEM_DESCRIPTION, '2', " +
                     "ORDER_ITEM_DESCRIPTION, 'Y','Y', 'Y', 'N', '406', "+
                     "'"+this.invoice_date+"', 'Interface' "+
                     "FROM INT_HIS_BILL " +
                     "WHERE BILL_DATE = '"+this.invoice_date+"' AND " +
                     "HOSPITAL_CODE = '"+this.hospital_code+"' AND "+
                     "ORDER_ITEM_CODE NOT IN (SELECT CODE FROM ORDER_ITEM " +
                     "WHERE HOSPITAL_CODE = '"+this.hospital_code+"') AND " +
                     "ORDER_ITEM_CODE IS NOT NULL";
        System.out.print( Variables.IS_TEST ? "Insert Order Item : "+stm+"\n" : "" );
        return stm;
    }
    private String insertPayor(){
        String stm = "INSERT INTO PAYOR_OFFICE (HOSPITAL_CODE, CODE, NAME_THAI, ACTIVE, " +
                     "UPDATE_DATE, USER_ID) "+
                
                     "SELECT DISTINCT HOSPITAL_CODE, PAYOR_CODE, PAYOR_NAME, '2', " +
                     "'"+this.invoice_date+"', 'Interface' "+
                     "FROM INT_HIS_BILL " +
                     "WHERE BILL_DATE = '"+this.invoice_date+"' AND " +
                     "HOSPITAL_CODE = '"+this.hospital_code+"' AND "+
                     "PAYOR_CODE NOT IN (SELECT CODE FROM PAYOR_OFFICE " +
                     "WHERE HOSPITAL_CODE = '"+this.hospital_code+"') AND " +
                     "PAYOR_CODE IS NOT NULL";
        System.out.print( Variables.IS_TEST ? "Insert Payor : "+stm+"\n" : "" );
        return stm;
    }
    private String insertPayorCategory(){
        String stm = "INSERT INTO PAYOR_OFFICE_CATEGORY (HOSPITAL_CODE, CODE, " +
        			 "NAME_THAI, NAME_ENG, ACTIVE, UPDATE_DATE, USER_ID) "+
                
                     "SELECT DISTINCT HOSPITAL_CODE, PAYOR_CATEGORY_CODE, PAYOR_CATEGORY_DESC, " +
                     "PAYOR_CATEGORY_DESC, '1', '"+this.invoice_date+"', 'Interface' "+
                     "FROM INT_HIS_BILL " +
                     "WHERE PAYOR_CATEGORY_CODE NOT IN (SELECT CODE FROM PAYOR_OFFICE_CATEGORY " +
                     "WHERE HOSPITAL_CODE = '"+this.hospital_code+"') AND " +
                     "BILL_DATE = '"+this.invoice_date+"' AND HOSPITAL_CODE = '"+this.hospital_code+
                     "' AND PAYOR_CATEGORY_CODE IS NOT NULL";
        System.out.print( Variables.IS_TEST ? "Insert Payor Cat. : "+stm+"\n" : "" );
        return stm;
    }
    private String insertReceiptType(){
        String stm = "INSERT INTO RECEIPT_TYPE (CODE, DESCRIPTION_THAI, DESCRIPTION_ENG, " +
        			 "IS_CHARGE, PERCENT_OF_CHARGE, ACTIVE, UPDATE_DATE, UPDATE_TIME, " +
        			 "USER_ID, BANK_CODE, HOSPITAL_CODE, RECEIPT_MODE_CODE) "+
                
                     "SELECT DISTINCT RECEIPT_TYPE_CODE, RECEIPT_TYPE_CODE, RECEIPT_TYPE_CODE, '0', 0.00, '1'" +
                     ", '"+JDate.getDate()+"', '"+JDate.getTime()+"', 'auto' , '', HOSPITAL_CODE, 'CS' "+
                     "FROM INT_HIS_BILL " +
                     "WHERE RECEIPT_TYPE_CODE NOT IN (SELECT CODE FROM RECEIPT_TYPE " +
                     "WHERE HOSPITAL_CODE = '"+this.hospital_code+"') AND " +
                     "BILL_DATE = '"+this.invoice_date+"' AND " +
                     "HOSPITAL_CODE = '"+this.hospital_code+"'AND " +
                     "RECEIPT_TYPE_CODE IS NOT NULL";
        System.out.print( Variables.IS_TEST ? "Insert ReceiptType : "+stm+"\n" : "" );
        return stm;
    }
}
