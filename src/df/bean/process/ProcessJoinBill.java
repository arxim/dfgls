package df.bean.process;

/**
 *
 * @author T.
 */
import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class ProcessJoinBill {
	final static Logger logger = Logger.getLogger(ProcessJoinBill.class);

    private String startDate = "";
    private String toDate = "";
    private String hospital_code = "";
    private DBConnection con;
    private DBConn dbconn;

    private String getStartDate() {
        return startDate;
    }

    private String getToDate() {
        return toDate;
    }

    private void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    private void setToDate(String toDate) {
        this.toDate = toDate;
    }

    private ResultSet getLineNO(){
        String sql = "SELECT " +
                "LINE_NO, COUNT(LINE_NO) " +
                "FROM INT_HIS_BILL " +
                "WHERE " +
                "AMOUNT_BEF_DISCOUNT > 0 " +
                //"AND IS_ONWARD = 'N' " +
                "AND HOSPITAL_CODE = '"+this.hospital_code+"' "+
                "AND BILL_DATE BETWEEN '"+this.getStartDate()+"' AND '"+this.getToDate()+"' " +
                "GROUP BY LINE_NO HAVING COUNT(LINE_NO) > 1;";
        DBConnection con = new DBConnection();
        con.connectToLocal();
        ResultSet rs = null;
        try{
            rs = con.executeQuery(sql);
        }catch(Exception err){
        	logger.error(err.getMessage());
        }
        return rs;
    }
    private void getBillByLineNO(String idRef, String startDate, String endDate){
        String sql = "SELECT BILL_NO, LINE_NO, AMOUNT_BEF_DISCOUNT, TRANSACTION_TYPE " +
                	 "FROM INT_HIS_BILL WHERE HOSPITAL_CODE = '"+this.hospital_code+"' " +
                	 "AND (TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"') "+
                	 //"AND IS_ONWARD = 'N' "+
                	 "AND LINE_NO = '"+ idRef +"'";
        Double temp_sum = 0.0;
        String temp_line = "";
        String temp_bill = "";
        String sql_insert = "";

        DBConnection con = new DBConnection();
        con.connectToLocal();
        ResultSet rs = con.executeQuery(sql);
        try{

            int iCount =0;
            while(rs.next()){
                if("INV".equalsIgnoreCase(rs.getString("TRANSACTION_TYPE")) || iCount==0){
                    temp_line = rs.getString("LINE_NO");
                    temp_bill = rs.getString("BILL_NO");
                    iCount++;
                }
                temp_sum = temp_sum + Double.parseDouble(rs.getString("AMOUNT_BEF_DISCOUNT"));                
            }
            sql_insert += "update INT_HIS_BILL SET AMOUNT_BEF_DISCOUNT='0.0' WHERE HOSPITAL_CODE = '"+
            			  this.hospital_code+"' AND (TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"') AND LINE_NO='"+ temp_line +"'; ";
            sql_insert += "update INT_HIS_BILL SET AMOUNT_BEF_DISCOUNT='"+ temp_sum +"' WHERE HOSPITAL_CODE = '"+
            			  this.hospital_code+"' AND (TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+endDate+"') AND LINE_NO='"+ temp_line +"' AND BILL_NO='"+ temp_bill +"'";            
            con.executeUpdate(sql_insert);
        }catch(Exception err){
            logger.error("Error getbillbylineno"+err.getMessage());
            logger.error("Statement " + sql_insert);
        }finally{
            try{
                con.Close();
            }catch(Exception err){
                //
            }
        }
        //ResultSet rs =
    }
    public boolean ProcessJoinBill(String hospital_code, String startDate, String toDate){
        this.con = new DBConnection();
        this.con.connectToLocal();
        this.dbconn = new DBConn();
        try{ this.dbconn.setStatement(); } catch (Exception e){}
        this.setStartDate(startDate);
        this.setToDate(toDate);
        this.hospital_code = hospital_code;
        boolean status = true;
        con.executeUpdate("UPDATE INT_HIS_BILL SET OLD_AMOUNT = AMOUNT_BEF_DISCOUNT " +
	            "WHERE HOSPITAL_CODE = '"+hospital_code+"' " +
	            "AND TRANSACTION_DATE BETWEEN '"+startDate+"' AND '"+toDate+"'");
        con.Close();
        if(dbconn.getSingleData("SELECT IS_JOIN_BILL FROM HOSPITAL WHERE CODE = '"+hospital_code+"'").equals("Y")){
        	logger.info("Join Bill Process");
	        ResultSet rs = this.getLineNO();
	        int count = 0;
	        try{
	            while(rs.next()){
	                this.getBillByLineNO(rs.getString("LINE_NO"), startDate, toDate);
	                count++;
	            }
	        }catch(Exception err){
	            logger.error("Error main join bill : "+err.getMessage());
	            status = false;
	        }finally{
	            try { rs.close(); } catch (SQLException ex) {}
	        }
        }else{
        	logger.info("Not Join Bill");
        }
        dbconn.closeDB("Close Connection from JoinBill Process");
        return status;
    }
}
