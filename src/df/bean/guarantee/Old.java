package df.bean.guarantee;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import df.bean.db.conn.DBConn;

/**
 *
 * @author arxim
 * 
 */
public class Old {
    DBConn cdb;
    String month = "";
    String year = "";
    String hospital = "";
    
    public Old(DBConn cdb){
        try { this.cdb = cdb; } catch (Exception ex) { System.out.println(ex); }
    }
    
    public boolean processRollback(String month, String year, String hospital_code){
        boolean message = true;
        this.month = month;
        this.year = year;
        this.hospital = hospital_code;
        try {
            if(rollbackTransaction()){
                if(rollbackSummary())
                    message = true;
            }else{
                message = false;
            }
        } catch (Exception ex) {
            
        }
        return message;
    }
    
    private boolean rollbackTransaction(){
        boolean status = true;
        String sql_stm = "UPDATE TRN_DAILY SET GUARANTEE_TERM_MM = '', "+
                "GUARANTEE_AMT = '0', "+
                "GUARANTEE_DR_CODE = '', "+
                "GUARANTEE_TYPE = '', "+
                "GUARANTEE_DATE_TIME = '', "+
                "GUARANTEE_TERM_YYYY = '' " +
                "WHERE GUARANTEE_TERM_MM = '"+this.month+"' "+
                "AND GUARANTEE_TERM_YYYY = '"+this.year+"' AND HOSPITAL_CODE = '"+this.hospital+"'";
        try {
            this.cdb.insert(sql_stm);
            this.cdb.commitDB();
        } catch (SQLException ex) {
            System.out.println(ex);
            status = false;
            this.cdb.rollDB();
        }
        return status;
    }
    private boolean rollbackSummary(){
        String sql_stm = "DELETE FROM SUMMARY_GUARANTEE WHERE MM = '"+this.month+"' AND YYYY = '"+
                         this.year+"' AND HOSPITAL_CODE = '"+this.hospital+"'";
        String sql_stm2 = "DELETE FROM STP_SUM_GUARANTEE WHERE MM = '"+this.month+"' AND YYYY = '"+
                         this.year+"' AND HOSPITAL_CODE = '"+this.hospital+"'";
        boolean status = true;
        try {
            this.cdb.insert(sql_stm2);
            this.cdb.insert(sql_stm);
            this.cdb.commitDB();
        } catch (SQLException ex) {
            status = false;
            this.cdb.rollDB();
        }
        return status;
    }

}
