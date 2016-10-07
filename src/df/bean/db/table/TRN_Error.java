/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.db.table;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author admin
 */
public class TRN_Error {
    
    static private Connection conn=null;
    static public String PROCESS_ROLLBACK = "Rollback";
    static public String PROCESS_DAILY = "Daily Calculate";
    static public String PROCESS_IMPORT_BILL = "Import Bill";
    static public String PROCESS_BANK_TMB_PAYMENT_MONTHLY = "Export to bank";
    static public String PROCESS_SUMMARY_MONTHLY = "Monthly Calculate";
    static public String PROCESS_PAYMENT_MONTHLY = "DF Payment";
    static public String PROCESS_SALARY_PAYMENT = "Salary Payment";
    static public String PROCESS_RECEIPT_BY_AR = "Import Receipt";
    static public String PROCESS_RECEIPT_BY_DOCTOR = "Receipt By Doctor";
    static public String PROCESS_RECEIPT_BY_PAYOR = "Receipt By Payor";
    static public String PROCESS_BANK_PAYMENT_MONTHLY_402 = "Bank Payment Monthly 4(2)";
    static public String PROCESS_COMPUTE_TAX_406 = "Compute tax 40(6)";
    
    static public String ERROR_TYPE_NO_CATEGOTY_CODE = "NO_CATEGORY_CODE";
    static public String ERROR_TYPE_NO_DOCTOR_CODE = "NO_DOCTOR_CODE";
    static public String ERROR_TYPE_METHOD_ALLOCATION_IS_NULL = "METHOD_ALLOCATION_IS_NULL";
    static public String ERROR_TYPE_NOT_SETUP_CONDITION = "NOT_SETUP_CONDITION";
    static public String ERROR_TYPE_COMPUTE_METHOD_ALLOCATE_ERROR = "COMPUTE_METHOD_ALLOCATE_ERROR";
    static public String ERROR_TYPE_NO_ORDER_ITEM = "NO_ORDER_ITEM";
    static public String ERROR_TYPE_COMPUTE_TAX_ERROR = "COMPUTE_TAX_ERROR";
    static public String ERROR_TYPE_COMPUTE_PREMIUM_ERROR = "COMPUTE_PREMIUM_ERROR";
    static public String ERROR_TYPE_UPDATE_TRNASACTION_ERROR = "UPDATE_TRNASACTION_ERROR";
    static public String ERROR_TYPE_COMPUTE_DAILY_ERROR = "COMPUTE_DAILY_ERROR";
    
    // import bill
    static public String ERROR_TYPE_IMPORT_BILL_ERROR = "IMPORT_BILL_ERROR";
    
    static public String RunDate = "";
    static public String RunTime = "";
//    static private DBConnection DBconn=null;
    static private String user_name = "";
    static private String hospital_code = "";
    
	public static void setHospital_code(String hospitalCode) {
		hospital_code = hospitalCode;
	}
	public static String getUser_name() {
		return user_name;
	}
	public static void setUser_name(String userName) {
		user_name = userName;
	}
	static public void setRunDate(String date) {
        RunDate = date;
    }    
    static public void setRunTime(String time) {
        RunTime = time;
    }
    
    static public Connection getConnection() {
        return conn;
    }

    public static void setConnection(Connection conn) {
        TRN_Error.conn = conn;
    }
    
    static public boolean writeErrorLog(Connection connect, String hospitalCode, String userID, String processName, String remark, String systemMessage, String sqlCommand, String errorType) {
        return writeErrorLog(connect, hospitalCode, userID, processName, remark, systemMessage, "", "");
    }
    static public boolean writeErrorLog(Connection connection, String hospitalCode, String userID, String processName, String remark, String systemMessage, String sqlCommand) {
        return writeErrorLog(connection, hospitalCode, userID, processName, remark, systemMessage, sqlCommand, "");    
    }
    static public boolean writeErrorLog(Connection connect, String processName, String remark, String systemMessage) {
        return writeErrorLog(connect, processName, remark, systemMessage, "", "");
    }
    static public boolean writeErrorLog(Connection connect, String processName, String remark, String systemMessage, String sqlCommand) {
        return writeErrorLog(connect, processName, remark, systemMessage, sqlCommand, "");    
    }
    static public boolean writeErrorLog(DBConnection connect, String processName, String remark, String systemMessage) {
        return writeErrorLog(connect, processName, remark, systemMessage, "", "");
    }
    static public boolean writeErrorLog(DBConnection connection, String processName, String remark, String systemMessage, String sqlCommand) {
        return writeErrorLog(connection, processName, remark, systemMessage, sqlCommand, "");    
    }

    //Implement Method
    static public boolean writeErrorLog(Connection connect, String processName, String remark, String systemMessage, String sqlCommand, String errorType) {
      if (systemMessage == null) { return false; }
      Statement stmt=null;
      String sql;
      boolean ret = false;
      setConnection(connect);
      remark = remark.replace("'", " ");
      remark = remark.replace(",", " ");
      processName = processName.replace(" ", "");
      systemMessage = systemMessage.replace("'", " ");
      systemMessage = systemMessage.replace(",", " ");
      RunDate = JDate.getDate();
      RunTime = JDate.getTime();
      
      sql = "insert into TRN_ERROR (HOSPITAL_CODE, PROCESS_NAME, ERROR_DATE, ERROR_TIME, REMARK, SYSTEM_MESSAGE, SQLCOMMAND, ERROR_TYPE, USER_ID)" +
                  //" VALUES ('" + Variables.getHospitalCode() + "'" +
      			" VALUES ('" + hospital_code + "'" +
                  " ,'" + processName + "'" +
                  " ,'" + RunDate + "'" +
                  " ,'" + RunTime + "'" +
                  " ,'" + remark + "'" +
                  " ,'" + systemMessage + "'" +
                  " ,'" + sqlCommand.replace("'", "|").replace(",", "!") + "'" +
                  " ,'" + errorType + "'" +
                  " ,'" + user_name + "')";
                  //" ,'" + Variables.getUserID() + "')";
      if (getConnection() != null) {
          try {
              if (stmt == null) {
                  stmt = conn.createStatement();
              }
              if (stmt.executeUpdate(sql) > -1) { ret = true; }
          } catch (SQLException e) {
              ret = false;
              e.printStackTrace();
              System.out.print(sql);
              System.out.print(e.getMessage());
          }
      }else{
    	  System.out.println(sql);
      }
      return ret;
    }

    static public boolean writeErrorLog(DBConnection connect, String processName, String remark, String systemMessage, String sqlCommand, String errorType) {
      if (systemMessage == null) { return false; }
      Statement stmt=null;
      String sql;
      boolean ret = false;
      remark = remark.replace("'", " ");
      remark = remark.replace(",", " ");
      processName = processName.replace(" ", "");
      systemMessage = systemMessage.replace("'", " ");
      systemMessage = systemMessage.replace(",", " ");
      RunDate = JDate.getDate();
      RunTime = JDate.getTime();
      
      sql = "insert into TRN_ERROR (HOSPITAL_CODE, PROCESS_NAME, ERROR_DATE, ERROR_TIME, REMARK, SYSTEM_MESSAGE, SQLCOMMAND, ERROR_TYPE, USER_ID)" +
                  " VALUES ('" + connect.getHospitalCode() + "'" +
                  " ,'" + processName + "'" +
                  " ,'" + RunDate + "'" +
                  " ,'" + RunTime + "'" +
                  " ,'" + remark + "'" +
                  " ,'" + systemMessage + "'" +
                  " ,'" + sqlCommand.replace("'", "|").replace(",", "!") + "'" +
                  " ,'" + errorType + "'" +
                  " ,'" + connect.getUserID() + "')";
      if (connect.getConnection() != null) {
          try {
              if (stmt == null) {
                  stmt = connect.getConnection().createStatement();
              }
              if (stmt.executeUpdate(sql) > -1) { ret = true; }
              connect.commitTrans();
          } catch (SQLException e) {
              ret = false;
          }
      }
      return ret;
	}
    
}