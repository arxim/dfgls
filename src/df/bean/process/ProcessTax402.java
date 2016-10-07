
package df.bean.process;

import java.sql.ResultSet;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.SummaryTax402;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Utils;
import df.bean.obj.util.Variables;

public class ProcessTax402 extends Process{
    
    public ProcessTax402(DBConnection conn) {
        super(conn);
        this.setDBConnection(conn);
    }
    
    // Insert Summary_Tax_402
    /*
    public boolean insertSummaryTax402(String hospitalCode, String yyyy, String doctorCode) {
        boolean result = true;
        String sql = "INSERT INTO YYYY,MM,TAX_TERM,DOCTOR_CODE,HOSPITAL_CODE,POSITION_AMT " +
        				",TURN_IN_AMT,TURN_OUT_AMT,GUARANTEE_AMT,OTHER_AMT,SUM_NORMAL_TAX_AMT " +
        				",SUM_TURN_TAX_AMT,NORMAL_TAX_MONTH,ACCU_NORMAL_TAX_MONTH,HOSPITAL_TAX_MONTH " +
        				",ACCU_HOSPITAL_TAX_MONTH,NET_TAX_MONTH,TEXT_NET_TAX_MONTH,BATCH_NO,UPDATE_DATE " +
        				",UPDATE_TIME,USER_ID,STATUS_MODIFY,ACTIVE " +
        	 " (SELECT TAX_TERM, '13',TAX_TERM,DOCTOR_CODE,HOSPITAL_CODE,POSITION_AMT " +
        				",TURN_IN_AMT,TURN_OUT_AMT,GUARANTEE_AMT,OTHER_AMT,SUM_NORMAL_TAX_AMT " +
        				",SUM_TURN_TAX_AMT,NORMAL_TAX_MONTH,ACCU_NORMAL_TAX_MONTH,HOSPITAL_TAX_MONTH " +
        				",ACCU_HOSPITAL_TAX_MONTH,NET_TAX_MONTH,TEXT_NET_TAX_MONTH,BATCH_NO,UPDATE_DATE " +
        				",UPDATE_TIME,USER_ID,STATUS_MODIFY,ACTIVE " + 
         	 "    	FROM SUMMARY_TAX_402 " +
             " 		WHERE HOSPITAL_CODE = '" + hospitalCode + "'" +
             "  	AND DOCTOR_CODE = '" + doctorCode + "'" +
             " 		AND TAX_TERM = '" + yyyy + "')";

        try {
//          conn.beginTrans();
            if (this.getDBConnection().executeUpdate(sql) >= 0) { result = true; } else { result = false; }
        } catch (Exception ex) {
            System.out.println("Import Bill : " + sql);
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), TRN_Error.PROCESS_IMPORT_BILL, 
                    "Import data error", ex.getMessage(), sql);
            
        } finally {
//            if (result) { conn.commitTrans(); }
//            if (!result) { conn.rollBackTrans(); }
        }
        
        return result;    
    }    
	*/
    public boolean CalculateTax402(String hospitalCode, String yyyy) {
        SummaryTax402 st = new SummaryTax402(this.getDBConnection());
        ResultSet rs = null;
        
        try {
        	this.getDBConnection().executeUpdate("DELETE SUMMARY_TAX_402 WHERE HOSPITAL_CODE='" + hospitalCode 
        						+ "' AND YYYY = '" + yyyy + "' AND MM = '13'");
        	
            String sql = "SELECT HOSPITAL_CODE, TAX_TERM, DOCTOR_CODE, SUM(SUM_NORMAL_TAX_AMT) as S1," +
               				" SUM(NET_TAX_MONTH) as S2, RIGHT('0000'+ CONVERT(VARCHAR,ROW_NUMBER() OVER(ORDER BY TAX_TERM)),4) AS LINE_NO" +
               				" FROM SUMMARY_TAX_402 " + 
               				" WHERE HOSPITAL_CODE = '" + hospitalCode + "'" +
               				" AND TAX_TERM = '" + yyyy + "'" + //nop update 05/01/2010
               				//" AND YYYY = '" + yyyy + "'" +
               				" AND ACTIVE = '1' " +
               				" GROUP BY HOSPITAL_CODE, TAX_TERM, DOCTOR_CODE";

                if (this.getStatement() == null) { this.setStatement(this.getDBConnection().getConnection().createStatement()); }
                rs = this.getStatement().executeQuery(sql);
                
                while (rs.next()) {
                	// values.clear();
                    // call for compute summary monthly
                    
                    // insert into SUMMARY_MONTHLY table
                    st.setHospitalCode(rs.getString("HOSPITAL_CODE"));
                    st.setDoctorCode(rs.getString("DOCTOR_CODE"));
                    st.setYyyy(rs.getString("TAX_TERM"));
                    st.setMm("13");
                    st.setPositionAmt(0d);
                    st.setTurnInAmt(0d);
                    st.setTurnOutAmt(0d);
                    st.setGuaranteeAmt(0d);
                    st.setOtherAmt(0d);
                    st.setSumNormalTaxAmt(rs.getDouble("S1"));
                    st.setSumTurnTaxAmt(0d);
                    st.setNormalTaxMonth(0d);
                    st.setAccuNormalTaxMonth(rs.getDouble("S1"));
                    st.setHospitalTaxMonth(0d);
                    st.setAccuHospitalTaxMonth(0d);
                    st.setNetTaxMonth(rs.getDouble("S2"));
                    st.setTextNetTaxMonth(Utils.toThaiMoney(rs.getDouble("S2")));
                    st.setLineNumber(rs.getString("LINE_NO"));
                    st.setUpdateDate(JDate.getDate());
                    st.setUpdateTime(JDate.getTime());
                    st.setUserID(this.getDBConnection().getUserID());
                    //st.setUserID(Variables.getUserID());
                    st.setActive("1");

                    if (!st.insert()) { st = null;   return false;  }

                }
        } catch (Exception e) {
            e.printStackTrace();
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                    TRN_Error.PROCESS_DAILY, "Calculate tax 40(2) is error.", e.getMessage());
            return false;
        } finally {
               //Clean up resources, close the connection.
            st = null;
                try {
                    if(rs != null) {
                        rs.close();
                        rs = null;
                    }
                } catch (Exception ignored) { ignored.printStackTrace();   }
            }

        return true;
    }

}
