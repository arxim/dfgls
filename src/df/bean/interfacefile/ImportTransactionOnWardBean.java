package df.bean.interfacefile;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;

public class ImportTransactionOnWardBean extends InterfaceTextFileBean{
    private ResultSet rs;
    private Statement stm;
    private Statement stm2;
    private String transaction_date;
    private String user_id;
    private String hospital_code;
    private ResultSet rshosp;
	@Override
	public boolean insertData(String fn, DBConnection d) {
		boolean status = false;
		ArrayList arrayList = new ArrayList();
        String[] sub_data = null;
        String message = "",rsHospital = "";
        int insert_count = 0;
        DBConnection c = new DBConnection();
        c.connectToLocal();
        Batch b = new Batch(hospital_code.toString(), c);
        c.Close();
        String date = "",month = "",year = "";
        String billDate="";
        System.out.println("File = "+fn);
        try {
            setConn(d);//connect database
            setFileName(fn);//set filename read
            copyDataFile();//copy data from file to superclass arraylist member
            arrayList = getData();//copy data from superclass arraylist member
            stm = getConn().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stm2 = getConn().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        	this.getConn().setUserID(this.getUserName());
        	this.getConn().setHospitalCode(this.hospital_code);
            rs = stm.executeQuery("SELECT * FROM INT_HIS_BILL WHERE 0<>0");
        	month = b.getMm();
        	year =  b.getYyyy();
        	date = JDate.getEndMonthDate(year, month);
        	billDate = year+month+date;
        	System.out.println("Interface : "+arrayList.size());
        	//check doctor_private
            String sqlHospital = "SELECT DOCTOR_PRIVATE FROM HOSPITAL WHERE CODE = '"  + this.hospital_code + "' AND ACTIVE = '1'";
            rshosp = stm2.executeQuery(sqlHospital);
            rshosp.next();
        	System.out.println(sqlHospital+"   "+rshosp.getString("DOCTOR_PRIVATE").toString());
 		
        	for(int i = 0; i<arrayList.size(); i++){
        		if(i>0){
	        		sub_data = arrayList.get(i).toString().split("\\|");
	    			this.setMessage("File Transaction Error Line : "+i);
	        		try{
	        			rs.moveToInsertRow();
	        	        message = "Invoice No = "+sub_data[5].trim()+sub_data[7].trim()+" Invoice Date = "+sub_data[21].trim()
	        	                  +" Line No = "+sub_data[34].trim()+":"
	        	                  +new String(sub_data[6].trim().getBytes(),"TIS-620");
	        	        this.setMessage(message);
	        	        if(rshosp.getString("DOCTOR_PRIVATE").toString().equals("Y")){
	        	        	privateDoctor(rs,sub_data,billDate);
	        	        	rs.insertRow();
			                insert_count++;
	        	        }else{
	        	        	noPrivateDoctor(rs, sub_data,billDate);
	        	        	rs.insertRow();
			                insert_count++;
	        	        }
	        		}catch (Exception e){
	        			System.out.println(e+" <> "+message);
	                    TRN_Error.writeErrorLog(this.getConn(), "onward",  this.getMessage(), e.toString(), message,"");
					}
	        	}
        	}
            if(insert_count < arrayList.size()-1){
                setMessage("Error : "+ ""+(arrayList.size()-(insert_count+1))+"/"+(arrayList.size()-1)+" records complete : "+insert_count+" records.");
            }else{
                setMessage("Complete "+(arrayList.size()-1)+"/"+insert_count+" records.");
            }
            setBillDate(billDate);
            status = true;
            this.getConn().commitTrans();
            rs.close();
		} catch (Exception f) {
            setInfo(f.toString());
            status = false;
            this.getConn().rollBackTrans();
		}
        finally {
            try {
                if(this.rs != null){ 
                    this.rs.close();
                    this.rs = null;
                }
                if(this.stm != null){
                	this.stm.close();
                	this.stm = null;
                }
            }
            catch (Exception ex)  {
                System.out.println(ex);
            }
            
        }
		return status;
	}
	
	public ResultSet privateDoctor(ResultSet rs, String[] sub_data,String billDate){
		System.out.println("privateDoctor");
		 try {
			 	rs.updateString("HOSPITAL_CODE",sub_data[0].trim());
				rs.updateString("EPISODE_TYPE", sub_data[1].trim());
				rs.updateString("BILL_NO", sub_data[5].trim()+sub_data[7].trim()+sub_data[8].trim());
		        rs.updateString("BILL_DATE", billDate);
		        rs.updateString("RECEIPT_TYPE_CODE", "AR");
		        rs.updateString("TRANSACTION_TYPE", "INV");
		        rs.updateString("HN_NO", sub_data[5].trim());
		        rs.updateString("PATIENT_NAME", new String(sub_data[6].trim().getBytes(),"TIS-620"));
		        rs.updateString("EPISODE_NO", sub_data[7].trim());
		        rs.updateString("PAYOR_CODE", sub_data[8].trim());
		        rs.updateString("PAYOR_NAME", new String(sub_data[9].trim().getBytes(),"TIS-620"));
		        rs.updateString("PAYOR_CATEGORY_CODE", sub_data[10].trim());
		        rs.updateString("PAYOR_CATEGORY_DESC", new String(sub_data[11].trim().getBytes(),"TIS-620"));
		        rs.updateString("ADMISSION_TYPE_CODE", sub_data[12].trim());
		        rs.updateString("ORDER_ITEM_CODE", sub_data[13].trim());
		        rs.updateString("ORDER_ITEM_DESCRIPTION", new String(sub_data[14].trim().getBytes(),"TIS-620"));
		        rs.updateString("DOCTOR_CODE", sub_data[17].trim()); 
		        rs.updateString("DOCTOR_NAME", new String(sub_data[18].trim().getBytes(),"TIS-620"));
		        rs.updateString("DOCTOR_PROFILE_CODE", sub_data[15].trim());
		        rs.updateString("DOCTOR_PROFILE_NAME", new String(sub_data[16].trim().getBytes(),"TIS-620"));
		        rs.updateString("DOCTOR_PRIVATE",sub_data[19].trim() );
		        rs.updateDouble("AMOUNT_BEF_DISCOUNT", Double.parseDouble(sub_data[20].trim()));
		        rs.updateDouble("AMOUNT_OF_DISCOUNT", Double.parseDouble(sub_data[21].trim()));
		        rs.updateString("ORDERED_DATE", sub_data[22].trim());
		        rs.updateString("ORDERED_TIME", sub_data[23].trim());
		        rs.updateString("NATIONALITY_CODE", sub_data[24].trim());
		        rs.updateString("NATIONALITY_DESCRIPTION", new String(sub_data[25].trim().getBytes(),"TIS-620"));
		        rs.updateString("PATIENT_LOCATION_CODE", sub_data[26].trim());
		        rs.updateString("PATIENT_LOCATION_DESC", new String(sub_data[27].trim().getBytes(),"TIS-620"));
		        rs.updateString("PATIENT_LOCATION_DEPT_CODE", sub_data[28].trim());
		        rs.updateString("PATIENT_LOCATION_DEPT_DESC",new String( sub_data[29].trim().getBytes(),"TIS-620"));
		        rs.updateString("RECEIVING_LOCATION_CODE", sub_data[30].trim());
		        rs.updateString("RECEIVING_LOCATION_DESC", new String(sub_data[31].trim().getBytes(),"TIS-620"));
		        rs.updateString("RECEIVING_LOCATION_DEPT_CODE", sub_data[32].trim());
		        rs.updateString("RECEIVING_LOCATION_DEPT_DESC", new String(sub_data[33].trim().getBytes(),"TIS-620"));
		        rs.updateString("LINE_NO", sub_data[34].trim());
		        rs.updateString("VERIFIED_DATE", sub_data[35].trim());
		        rs.updateString("VERIFIED_TIME", sub_data[36].trim());
		        rs.updateDouble("BILL_TOTAL_AMOUNT", Double.parseDouble(sub_data[37].trim()));
		        rs.updateString("TRANSACTION_DATE", billDate);
		        rs.updateString("INVOICE_TYPE", "EXECUTE");
		        rs.updateString("IS_ONWARD", "Y");
			} catch (SQLException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	        
		return  rs;
	}
	
	public ResultSet noPrivateDoctor(ResultSet rs, String[] sub_data,String billDate){
		System.out.println("noPrivateDoctor");
        try {
        	rs.updateString("HOSPITAL_CODE",sub_data[0].trim());
			rs.updateString("EPISODE_TYPE", sub_data[1].trim());
			rs.updateString("BILL_NO", sub_data[5].trim()+sub_data[7].trim());
	        rs.updateString("BILL_DATE", billDate);
	        rs.updateString("RECEIPT_TYPE_CODE", "AR");
	        rs.updateString("TRANSACTION_TYPE", "INV");
	        rs.updateString("HN_NO", sub_data[5].trim());
	        rs.updateString("PATIENT_NAME", new String(sub_data[6].trim().getBytes(),"TIS-620"));
	        rs.updateString("EPISODE_NO", sub_data[7].trim());
	        rs.updateString("PAYOR_CODE", sub_data[8].trim());
	        rs.updateString("PAYOR_NAME", new String(sub_data[9].trim().getBytes(),"TIS-620"));
	        rs.updateString("PAYOR_CATEGORY_CODE", sub_data[10].trim());
	        rs.updateString("PAYOR_CATEGORY_DESC", new String(sub_data[11].trim().getBytes(),"TIS-620"));
	        rs.updateString("ADMISSION_TYPE_CODE", sub_data[12].trim());
	        rs.updateString("ORDER_ITEM_CODE", sub_data[13].trim());
	        rs.updateString("ORDER_ITEM_DESCRIPTION", new String(sub_data[14].trim().getBytes(),"TIS-620"));
	        rs.updateString("DOCTOR_CODE", sub_data[17].trim()); 
	        rs.updateString("DOCTOR_NAME", new String(sub_data[18].trim().getBytes(),"TIS-620"));
	        rs.updateString("DOCTOR_PROFILE_CODE", sub_data[15].trim());
	        rs.updateString("DOCTOR_PROFILE_NAME", new String(sub_data[16].trim().getBytes(),"TIS-620"));
	        rs.updateString("DOCTOR_PRIVATE", "");
	        rs.updateDouble("AMOUNT_BEF_DISCOUNT", Double.parseDouble(sub_data[19].trim()));
	        rs.updateDouble("AMOUNT_OF_DISCOUNT", Double.parseDouble(sub_data[20].trim()));
	        rs.updateString("ORDERED_DATE", sub_data[21].trim());
	        rs.updateString("ORDERED_TIME", sub_data[22].trim());
	        rs.updateString("NATIONALITY_CODE", sub_data[23].trim());
	        rs.updateString("NATIONALITY_DESCRIPTION", new String(sub_data[24].trim().getBytes(),"TIS-620"));
	        rs.updateString("PATIENT_LOCATION_CODE", sub_data[25].trim());
	        rs.updateString("PATIENT_LOCATION_DESC", new String(sub_data[26].trim().getBytes(),"TIS-620"));
	        rs.updateString("PATIENT_LOCATION_DEPT_CODE", sub_data[27].trim());
	        rs.updateString("PATIENT_LOCATION_DEPT_DESC",new String( sub_data[28].trim().getBytes(),"TIS-620"));
	        rs.updateString("RECEIVING_LOCATION_CODE", sub_data[29].trim());
	        rs.updateString("RECEIVING_LOCATION_DESC", new String(sub_data[30].trim().getBytes(),"TIS-620"));
	        rs.updateString("RECEIVING_LOCATION_DEPT_CODE", sub_data[31].trim());
	        rs.updateString("RECEIVING_LOCATION_DEPT_DESC", new String(sub_data[32].trim().getBytes(),"TIS-620"));
	        rs.updateString("LINE_NO", sub_data[33].trim());
	        rs.updateString("VERIFIED_DATE", sub_data[34].trim());
	        rs.updateString("VERIFIED_TIME", sub_data[35].trim());
	        rs.updateDouble("BILL_TOTAL_AMOUNT", Double.parseDouble(sub_data[36].trim()));
	        rs.updateString("TRANSACTION_DATE", billDate);
	        rs.updateString("INVOICE_TYPE", "EXECUTE");
	        rs.updateString("IS_ONWARD", "Y");
		} catch (SQLException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return rs;
		
	}
	
	
	@Override
	public boolean exportData(String fn, String hpCode, String type,
			String year, String month, DBConn d, String path) {
		// TODO Auto-generated method stub
		return false;
	}
    
    public void setDate(String d){
    	this.transaction_date = JDate.saveDate(d);
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