/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.interfacefile;

import df.bean.db.conn.DBConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import df.bean.db.conn.DBConn;
import df.bean.db.table.TRN_Error;

/**
 *
 * @author nopphadon
 */
public class ImportGuaranteeBean extends InterfaceTextFileBean {
    private ResultSet rs;
    private Statement stm;

    @Override
    public boolean insertData(String fn, DBConnection d) {
        boolean status = true;
        ArrayList a = null;
        String[] sub_data = null;
        String temp = "";
        int insert_count = 0;
        try {
            setConn(d);//connect database
            setFileName(fn);//set filename read
            copyDataFile();//copy data from file to superclass arraylist member
            a = getData();//copy data from superclass arraylist member
            getConn().beginTrans();
            stm = getConn().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stm.executeQuery("SELECT * FROM STP_GUARANTEE");
            setMessage("Interface Guarantee Table Complete");
            for(int i = 0; i<a.size(); i++){
                if(i>0){
                    temp = (String)a.get(i);
                    sub_data = temp.split("[|]");
                    rs.moveToInsertRow();
                    setMessage("Error on Doctor = "+sub_data[1]+" Guarantee Code = "+sub_data[2]+""+sub_data[3].trim());
                    rs.updateString("HOSPITAL_CODE",sub_data[0].trim());
                    rs.updateString("GUARANTEE_DR_CODE",sub_data[1].trim());
                    rs.updateString("YYYY", sub_data[2].trim().substring(0, 4));
                    rs.updateString("MM", sub_data[2].trim().substring(4, 6));
                    rs.updateString("START_DATE",sub_data[2].trim());
                    rs.updateString("START_TIME",sub_data[3].trim());
                    rs.updateString("END_DATE",sub_data[4].trim());
                    rs.updateString("END_TIME",sub_data[5].trim());
                    rs.updateString("ADMISSION_TYPE_CODE",sub_data[6].trim());
                    rs.updateString("GUARANTEE_LOCATION",sub_data[7].trim());
                    rs.updateString("GUARANTEE_CODE",sub_data[2].trim()+""+sub_data[3].trim());
                    rs.updateString("GUARANTEE_TYPE_CODE","DLY");
                    rs.updateString("ACTIVE","0");
                    try{
                        rs.insertRow();
                        insert_count++;
                    }catch(Exception e){
                        TRN_Error.writeErrorLog(this.getConn().getConnection(), "Import Guarantee", "Duplicate Data Line : "+i, e.getMessage(), this.getMessage());
                    }
                }
            }
            if(insert_count < a.size()-1){
                setMessage("Error : "+ ""+(a.size()- (insert_count+1))+"/"+(a.size()-1)+" records complete : "+insert_count+" records.");
            }else{
                setMessage("Complete "+(a.size()-1)+"/"+insert_count+" records.");
            }
            getConn().commitTrans();
            status = true;
            rs.close();
        } catch (Exception f) {
            setInfo(f.toString());
            status = false;
            //getConn().commitTrans();
            getConn().rollBackTrans();
        } finally {
            try {
                if (rs != null) { 
                    rs.close();
                    rs = null;
                }
                if (stm != null) {
                    stm.close();
                    stm = null;
                }
            }
            catch (Exception ex)  {
                System.out.println(ex);
            }
            
        }
        return status;
    }

    @Override
    public boolean exportData(String fn, String hp, String type, String year, String month, DBConn d, String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	@Override
	public boolean exportData(String fn, String hp_code, String type, String year, String month, DBConn d, String path,
			String filing_type) {
		// TODO Auto-generated method stub
		return false;
	}
}
