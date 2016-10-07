package df.bean.interfacefile;
import java.io.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.*;

import df.bean.db.conn.DBConnection;
import java.sql.SQLException;
import df.bean.db.conn.DBConn;
import df.bean.process.ProcessUtil;
import df.bean.db.table.Batch;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;

public class ImportDischargeExcelBean extends InterfaceTextFileBean{
	private String hospital_code;
	DBConn cdb;
	DBConnection cc = new DBConnection();
	String result = "";
	String user_name=this.getUserName();
	Batch batch;
	HSSFSheet sheet;
	String year="", month="", completeDate="", invoiceDate="";
	private String remarkMessage="", sqlMessage="";
	
	public void setHospital(String s){
    	this.hospital_code = s;
    }

    @Override
    public boolean insertData(String fn, DBConnection da) {
    	boolean status = true;
    	try {
    		cc.connectToLocal();
    		cdb = new DBConn();
            if (cdb.getStatement() == null) {
                cdb.setStatement();
            }
            batch = new Batch(hospital_code, cc);
            this.year = batch.getYyyy();
            this.month = batch.getMm();
            cc.Close();
     	   	InputStream myxls2003 = new FileInputStream(fn);
     	   	HSSFWorkbook wb2003 = new HSSFWorkbook(myxls2003);
        	HSSFSheet sheet = wb2003.getSheetAt(0);
    	
	        for(int i=1;i<sheet.getPhysicalNumberOfRows();i++){
	        	HSSFRow row2003 = sheet.getRow(i);
	           	completeDate = row2003.getCell((short)3)==null ? "" :""+JDate.saveDate(row2003.getCell((short)3).toString().trim());
	           	invoiceDate = row2003.getCell((short)1)==null ? "" :""+JDate.saveDate(row2003.getCell((short)1).toString().trim());
	           	String sql_insert="INSERT INTO INT_HIS_DISCHARGE (" +
		  		"HOSPITAL_CODE, INVOICE_DATE, INVOICE_NO, COMPLETE_DATE, " +
		  		"HN_NO, EPISODE_NO, DOCTOR_CODE, ORDER_ITEM_CODE, LINE_NO, " +
	           	"PAYMENT_STATUS, YYYY, MM) VALUES ('"+
				row2003.getCell((short)0)+"','"+invoiceDate+"', '"+row2003.getCell((short)2)+"', '"+
				this.completeDate+"', '"+row2003.getCell((short)4)+"', '"+row2003.getCell((short)5)+"', '"+
				row2003.getCell((short)6)+"', '"+row2003.getCell((short)7)+"', '"+row2003.getCell((short)8)+"', '"+
				row2003.getCell((short)9)+"', '"+this.year+"', '"+this.month+"')";
	           	//System.out.println("> : "+sql_insert);
	           	
	           	try{
		           	if(row2003.getCell((short)9).toString().equals("N")){
		        	   if(JDate.saveDate(row2003.getCell((short)1).toString().trim()).substring(0, 6).equals(this.year+this.month)){
		        		   cdb.insert(sql_insert);
		        	   }
		           	}
		           	if(row2003.getCell((short)9).toString().equals("Y")){
		        	   if(!JDate.saveDate(row2003.getCell((short)1).toString().trim()).substring(0, 6).equals(this.year+this.month)
		        	   && !JDate.saveDate(row2003.getCell((short)1).toString().trim()).equals(completeDate)){ 
		        		   cdb.insert(sql_insert);
		        	   }
		           	}
	           	}catch(Exception e){
		        	System.out.println(e);
		        }
	        }
	    	cdb.commitDB();
        }catch (Exception ex) {
        	status = false;
        	cdb.rollDB();
            this.result = ""+ex;
            this.setMessage(this.result);
            System.out.println(ex);
        }
    	cdb.closeDB("");
    	return status;
    }

    @Override
	public boolean exportData(String fn, String hpCode, String type,
			String year, String month, DBConn d, String path) {
		// TODO Auto-generated method stub
		return false;
	}
}