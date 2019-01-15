package df.bean.interfacefile;

import df.bean.db.conn.DBConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import df.bean.db.conn.DBConn;
import df.bean.db.table.TRN_Error;
/**
 * @author arxim
 */
public class ImportTransactionResultBean extends InterfaceTextFileBean {
    private ResultSet rs;
    private Statement stm;
    private String user_id;
    private String hospital_code;

    @Override
    public boolean insertData(String fn, DBConnection da) {
        DBConnection d = new DBConnection();
        d.connectToLocal();
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
        	this.getConn().setUserID(this.getUserName());
        	this.getConn().setHospitalCode(this.hospital_code);
            rs = stm.executeQuery("SELECT * FROM INT_HIS_VERIFY WHERE 0<>0");
            for(int i = 0; i<a.size(); i++){
                if(i>0){
                    temp = (String)a.get(i)+" ";
                    sub_data = temp.split("[|]");
                    rs.moveToInsertRow();
                    setMessage("Bill No = "+sub_data[1]+" Line No = "+sub_data[3]+" Verify Date = "+sub_data[4]+" No."+i);

                    rs.updateString("HOSPITAL_CODE",sub_data[0].trim());
                    rs.updateString("BILL_NO",sub_data[1].trim());
                    rs.updateString("LINE_NO",sub_data[3].trim());
                    rs.updateString("DOCTOR_CODE",sub_data[2].trim());
                    //rs.updateDouble("AMOUNT_BEF_DISCOUNT",Double.parseDouble(""+sub_data[19].trim()));
                    rs.updateString("VERIFIED_DATE",""+sub_data[4].trim());
                    rs.updateString("VERIFIED_TIME",sub_data[5].trim());
                    String[]  date_file =  fn.split("\\.");
                    
                    //System.out.println("filename >>>>>>"+date_file[date_file.length - 1]);
                    rs.updateString("TRANSACTION_DATE",date_file[date_file.length - 1]);
                    //rs.updateString("TRANSACTION_DATE",sub_data[4].trim());
                   
                    try{
                        rs.insertRow();
                        insert_count++;
                    }catch(Exception e){
                        TRN_Error.writeErrorLog(this.getConn(), "InterfaceVerifyResult",  this.getMessage(), e.toString(), "","");
                        this.setMessage(""+e);
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
            this.setMessage(""+f);
            System.out.println("Import Result Error : "+f);
            status = false;
            getConn().rollBackTrans();
        }
        finally {
            try {
                if (rs != null) { 
                    rs.close();
                    rs = null;
                }
                if (stm != null) {
                    stm.close();
                    stm = null;
                }
                d.Close();
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
    
    public void setUserId(String u){
    	this.user_id = u;
    }
	public void setHospitalCode(String hospitalCode) {
		hospital_code = hospitalCode;
	}

	@Override
	public boolean exportData(String fn, String hp_code, String type, String year, String month, DBConn d, String path,
			String filing_type) {
		// TODO Auto-generated method stub
		return false;
	}
}
