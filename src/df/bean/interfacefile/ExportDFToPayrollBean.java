/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.interfacefile;

import df.bean.db.conn.DBConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import df.bean.db.conn.DBConn;
import df.bean.obj.util.JDate;

/**
 *
 * @author nopphadon
 */
public class ExportDFToPayrollBean extends InterfaceTextFileBean {
    private ResultSet rs;
    private Statement stm;
    
    @Override
    public boolean insertData(String fn, DBConnection d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean exportData(String fn, String hp, String type, String year, String month, DBConn d, String path) {
        String[] sub_data = null;
        boolean status = true;
        String[][] temp_data = null;
        String dat = "SELECT PM.HOSPITAL_CODE, PM.DOCTOR_CODE, PM.DR_NET_PAID_AMT, " +
                     "PM.PAYMENT_TERM_DATE, DR.DEPARTMENT_CODE "+
                     "FROM PAYMENT_MONTHLY PM LEFT OUTER JOIN DOCTOR DR ON PM.DOCTOR_CODE = DR.CODE "+
                     "WHERE (PM.BATCH_NO IS NULL OR PM.BATCH_NO = '') AND "+
                     "PM.YYYY LIKE '"+year+"' AND PM.MM LIKE '"+month+"' AND PM.PAYMENT_TYPE = '01' AND "+
                     "PM.HOSPITAL_CODE LIKE '"+hp+"'";
        
        try {
            setFileName(path);//set filename read
            temp_data = d.query(dat);
            sub_data = new String[temp_data.length];            
            for(int i = 0; i<temp_data.length; i++){
                sub_data[i] = temp_data[i][1]+","+
                              "EDF,"+
                              temp_data[i][4]+",,,,,,"+
                              Double.parseDouble(temp_data[i][2])+","+
                              "U";                              
            }
        }catch(Exception e){
            System.out.println(e);
        }
        writeFileNew(sub_data);
        return status;
    }

	@Override
	public boolean exportData(String fn, String hp_code, String type, String year, String month, DBConn d, String path,
			String filing_type) {
		// TODO Auto-generated method stub
		return false;
	}

}
