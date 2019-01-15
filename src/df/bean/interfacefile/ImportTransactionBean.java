package df.bean.interfacefile;

import df.bean.db.conn.DBConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import df.bean.db.conn.DBConn;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;
/**
 *
 * @author arxim
 */
public class ImportTransactionBean extends InterfaceTextFileBean {
    private ResultSet rs;
    private Statement stm;
    private String transaction_date;
    private String user_id;
    private String hospital_code;
    
    @Override
    public boolean insertData(String fn, DBConnection d) {
        boolean status = true;
        ArrayList a = null;
        String[] sub_data = null;
        String temp = "";
        String message = "";
        int insert_count = 0;

        try {
            setConn(d);//connect database
            setFileName(fn);//set filename read
            copyDataFile();//copy data from file to superclass arraylist member
            a = getData();//copy data from superclass arraylist member
            stm = getConn().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        	this.getConn().setUserID(this.getUserName());
        	this.getConn().setHospitalCode(this.hospital_code);
        	System.out.println("Start Select "+JDate.getTime());
            //rs = stm.executeQuery("SELECT * FROM INT_HIS_BILL");
        	rs = stm.executeQuery("SELECT * FROM INT_HIS_BILL WHERE 0<>0");
            System.out.println("End Select "+JDate.getTime());
            for(int i = 0; i<a.size(); i++){
                if(i>0){
                    this.setMessage("File Transaction Error Line : "+i);
                    temp = (String)a.get(i);
                    sub_data = temp.split("[|]");
                    System.out.println("data size : " + sub_data.length);
                     try{
                        rs.moveToInsertRow();
                        message = "Invoice No = "+sub_data[2]+" Invoice Date = "+sub_data[3]
                                  +" Line No: = "+sub_data[34].trim()+":"
                                  +new String(sub_data[6].trim().getBytes(),"TIS-620");
                        
                      //   System.out.println("Info : " +  message);
                        this.setMessage(message);
                        rs.updateString("HOSPITAL_CODE",sub_data[0].trim());
                        rs.updateString("EPISODE_TYPE",sub_data[1].trim());
                        rs.updateString("BILL_NO",sub_data[2].trim());
                        rs.updateString("BILL_DATE",sub_data[3].trim());
                        rs.updateString("RECEIPT_TYPE_CODE",sub_data[4].trim());
                        
                        if(sub_data[4].trim().equals("AR")){
                            rs.updateString("TRANSACTION_TYPE","INV");
                        }else{
                            rs.updateString("TRANSACTION_TYPE","REV");
                        }
                        
                        rs.updateString("HN_NO", sub_data[5].trim());
                        //rs.updateString("PATIENT_NAME",sub_data[6].trim());
                        rs.updateString("PATIENT_NAME",new String(sub_data[6].trim().getBytes(),"TIS-620"));
                        rs.updateString("EPISODE_NO",sub_data[7].trim());
                        rs.updateString("PAYOR_CODE",sub_data[8].trim().toString().replace("#", "_"));
                        rs.updateString("PAYOR_NAME",new String(sub_data[9].trim().getBytes(),"TIS-620"));
                        rs.updateString("PAYOR_CATEGORY_CODE",sub_data[10].trim());
                        rs.updateString("PAYOR_CATEGORY_DESC",new String(sub_data[11].trim().getBytes(),"TIS-620"));
                       
                        if(sub_data[12].trim().equals("I")){
                            rs.updateString("ADMISSION_TYPE_CODE",sub_data[12].trim());
                        }else{
                            rs.updateString("ADMISSION_TYPE_CODE","O");
                        }
                        
                        rs.updateString("ORDER_ITEM_CODE",sub_data[13].trim());
                        rs.updateString("ORDER_ITEM_DESCRIPTION",new String(sub_data[14].trim().getBytes(),"TIS-620"));
                        rs.updateString("DOCTOR_PROFILE_CODE",sub_data[15].trim());
                        rs.updateString("DOCTOR_PROFILE_NAME",new String(sub_data[16].trim().getBytes(),"TIS-620"));
                    
                        if(sub_data[17].length()<4){
                            rs.updateString("DOCTOR_CODE","99999999");
                        }else{
                            rs.updateString("DOCTOR_CODE",sub_data[17].trim());
                        }
                        
                        rs.updateString("DOCTOR_NAME",new String(sub_data[18].trim().getBytes(),"TIS-620"));
                        
                        //  IS Private Doctor 
                        rs.updateString("DOCTOR_PRIVATE",sub_data[19].trim());
                        
                        rs.updateDouble("AMOUNT_BEF_DISCOUNT",Double.parseDouble(""+sub_data[20].trim()));
                        rs.updateDouble("AMOUNT_OF_DISCOUNT",Double.parseDouble(""+sub_data[21].trim()));
                        rs.updateString("ORDERED_DATE",sub_data[22].trim());
                        rs.updateString("ORDERED_TIME",sub_data[23].trim());
                        rs.updateString("NATIONALITY_CODE",sub_data[24].trim());
                        rs.updateString("NATIONALITY_DESCRIPTION",new String(sub_data[25].trim().getBytes(),"TIS-620"));
                        
                        rs.updateString("PATIENT_LOCATION_CODE",sub_data[26].trim());
                        rs.updateString("PATIENT_LOCATION_DESC",new String(sub_data[27].trim().getBytes(),"TIS-620"));
                        rs.updateString("PATIENT_LOCATION_DEPT_CODE",sub_data[28].trim());
                        rs.updateString("PATIENT_LOCATION_DEPT_DESC",new String(sub_data[29].trim().getBytes(),"TIS-620"));
                        
                        rs.updateString("RECEIVING_LOCATION_CODE",sub_data[30].trim());
                        rs.updateString("RECEIVING_LOCATION_DESC",new String(sub_data[31].trim().getBytes(),"TIS-620"));
                        rs.updateString("RECEIVING_LOCATION_DEPT_CODE",sub_data[32].trim());
                        rs.updateString("RECEIVING_LOCATION_DEPT_DESC",new String(sub_data[33].trim().getBytes(),"TIS-620"));
                       
                        if(sub_data[34].trim().equals("") || sub_data[34].trim().equals("null")){
                            rs.updateString("LINE_NO",(""+JDate.getTimeInMillis()).substring(6)+"/"+i);
                        }else{
                            rs.updateString("LINE_NO",sub_data[34].trim());                            
                        }
                        
                        if(sub_data[35].trim().length() < 4){
                            rs.updateString("VERIFIED_DATE","");                                                        
                        }else{
                            //if(Integer.parseInt(sub_data[34].trim()) < Integer.parseInt(sub_data[3].trim())){
                                //rs.updateString("VERIFIED_DATE",""+sub_data[3].trim());                            
                            //}else{
                                rs.updateString("VERIFIED_DATE",""+sub_data[35].trim());                            
                            //}
                        }
                        rs.updateString("VERIFIED_TIME",sub_data[36].trim());
                        rs.updateString("BILL_TOTAL_AMOUNT",sub_data[37].trim());
                        if(this.transaction_date.length()< 2){
                        	rs.updateString("TRANSACTION_DATE",sub_data[3].trim());                        	
                        }else{
                        	rs.updateString("TRANSACTION_DATE",this.transaction_date);
                        }
                        /*
                        if(sub_data[34].trim().length()<4 || 
                        Integer.parseInt(sub_data[34].trim()) < Integer.parseInt(sub_data[3].trim())){
                            rs.updateString("TRANSACTION_DATE",sub_data[3].trim());
                        }else{
                            rs.updateString("TRANSACTION_DATE",sub_data[34].trim());
                        }
                        */
                        if(sub_data[35].trim().equals("")){
                            rs.updateString("INVOICE_TYPE","ORDER");
                        }else{
                            rs.updateString("INVOICE_TYPE","EXECUTE");
                        }
                        // RUN TEST
                        //rs.updateString("DOCTOR_PRIVATE"  , "");
                        
                        rs.insertRow();
                        insert_count++;
                     }catch(Exception e){
                        TRN_Error.writeErrorLog(this.getConn(), "InterfaceTransaction",  this.getMessage(), e.toString(), message,"");
                    }
                }
            }
            if(insert_count < a.size()-1){
                setMessage("Error : "+ ""+(a.size()- (insert_count+1))+"/"+(a.size()-1)+" records complete : "+insert_count+" records.");
            }else{
                setMessage("Complete "+(a.size()-1)+"/"+insert_count+" records.");
            }
            setBillDate(sub_data[3].trim());
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
    
    /**
     * This method interface file by  HOSPITAL.DOCTOR_PRIVATE 
     * @param fn
     * @param d
     * @return
     */
    public boolean insertDataNoDoctorPrivate(String fn , DBConnection d) {
    	 boolean status = true;
         ArrayList a = null;
         String[] sub_data = null;
         String temp = "";
         String message = "";
         int insert_count = 0;

         try {
        	 
             setConn(d);				//connect database
             setFileName(fn);			//set filename read
             copyDataFile();			//copy data from file to superclass arraylist member
             a = getData();				//copy data from superclass arraylist member
             stm = getConn().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
         	
             this.getConn().setUserID(this.getUserName());
         	 this.getConn().setHospitalCode(this.hospital_code);
         	 System.out.println("Start Select "+JDate.getTime());
         	 rs = stm.executeQuery("SELECT * FROM INT_HIS_BILL WHERE 0<>0");
             System.out.println("End Select "+JDate.getTime());
             
             for(int i = 0; i<a.size(); i++){
                 if(i>0){
                    
                	 this.setMessage("File Transaction Error Line : "+i);
                     temp = (String)a.get(i);
                     sub_data = temp.split("[|]");
                     System.out.println("data size : " + sub_data.length);
                     
                     try{
                         rs.moveToInsertRow();
                         message = "Invoice No = "+sub_data[2]+" Invoice Date = "+sub_data[3]
                                   +" Line No = "+sub_data[33].trim()+":"
                                   +new String(sub_data[6].trim().getBytes(),"TIS-620");
                         
                         this.setMessage(message);
                     
                         rs.updateString("HOSPITAL_CODE",sub_data[0].trim());
                         rs.updateString("EPISODE_TYPE",sub_data[1].trim());
                         rs.updateString("BILL_NO",sub_data[2].trim());
                         rs.updateString("BILL_DATE",sub_data[3].trim());
                         rs.updateString("RECEIPT_TYPE_CODE",sub_data[4].trim());
                         
                         if(sub_data[4].trim().equals("AR")){
                             rs.updateString("TRANSACTION_TYPE","INV");
                         }else{
                             rs.updateString("TRANSACTION_TYPE","REV");
                         }
                         
                         rs.updateString("HN_NO", sub_data[5].trim());
                         rs.updateString("PATIENT_NAME",new String(sub_data[6].trim().getBytes(),"TIS-620"));
                         rs.updateString("EPISODE_NO",sub_data[7].trim());
                         rs.updateString("PAYOR_CODE",sub_data[8].trim().toString().replace("#", "_"));
                         rs.updateString("PAYOR_NAME",new String(sub_data[9].trim().getBytes(),"TIS-620"));
                         rs.updateString("PAYOR_CATEGORY_CODE",sub_data[10].trim());
                         rs.updateString("PAYOR_CATEGORY_DESC",new String(sub_data[11].trim().getBytes(),"TIS-620"));
                        
                         if(sub_data[12].trim().equals("I")){
                             rs.updateString("ADMISSION_TYPE_CODE",sub_data[12].trim());
                         }else{
                             rs.updateString("ADMISSION_TYPE_CODE","O");
                         }
                         
                         rs.updateString("ORDER_ITEM_CODE",sub_data[13].trim());
                         rs.updateString("ORDER_ITEM_DESCRIPTION",new String(sub_data[14].trim().getBytes(),"TIS-620"));
                         rs.updateString("DOCTOR_PROFILE_CODE",sub_data[15].trim());
                         rs.updateString("DOCTOR_PROFILE_NAME",new String(sub_data[16].trim().getBytes(),"TIS-620"));
                     
                         if(sub_data[17].length()<4){
                             rs.updateString("DOCTOR_CODE","99999999");
                         }else{
                             rs.updateString("DOCTOR_CODE",sub_data[17].trim());
                         }
                         
                         rs.updateString("DOCTOR_NAME",new String(sub_data[18].trim().getBytes(),"TIS-620"));
                         
                         //  IS Private Doctor 
                         rs.updateString("DOCTOR_PRIVATE", "" );
                         
                         rs.updateDouble("AMOUNT_BEF_DISCOUNT",Double.parseDouble(""+sub_data[19].trim()));
                         rs.updateDouble("AMOUNT_OF_DISCOUNT",Double.parseDouble(""+sub_data[20].trim()));
                         rs.updateString("ORDERED_DATE",sub_data[21].trim());
                         rs.updateString("ORDERED_TIME",sub_data[22].trim());
                         rs.updateString("NATIONALITY_CODE",sub_data[23].trim());
                         rs.updateString("NATIONALITY_DESCRIPTION",new String(sub_data[24].trim().getBytes(),"TIS-620"));
                         
                         rs.updateString("PATIENT_LOCATION_CODE",sub_data[25].trim());
                         rs.updateString("PATIENT_LOCATION_DESC",new String(sub_data[26].trim().getBytes(),"TIS-620"));
                         rs.updateString("PATIENT_LOCATION_DEPT_CODE",sub_data[27].trim());
                         rs.updateString("PATIENT_LOCATION_DEPT_DESC",new String(sub_data[28].trim().getBytes(),"TIS-620"));
                         
                         rs.updateString("RECEIVING_LOCATION_CODE",sub_data[29].trim());
                         rs.updateString("RECEIVING_LOCATION_DESC",new String(sub_data[30].trim().getBytes(),"TIS-620"));
                         rs.updateString("RECEIVING_LOCATION_DEPT_CODE",sub_data[31].trim());
                         rs.updateString("RECEIVING_LOCATION_DEPT_DESC",new String(sub_data[32].trim().getBytes(),"TIS-620"));
                        
                         if(sub_data[33].trim().equals("") || sub_data[33].trim().equals("null")){
                             rs.updateString("LINE_NO",(""+JDate.getTimeInMillis()).substring(6)+"/"+i);
                         }else{
                             rs.updateString("LINE_NO",sub_data[33].trim());                            
                         }
                         
                         if(sub_data[34].trim().length() < 4){
                             rs.updateString("VERIFIED_DATE","");                                                        
                         }else{
                                 rs.updateString("VERIFIED_DATE",""+sub_data[34].trim());                            
                          }
                         
                         rs.updateString("VERIFIED_TIME",sub_data[35].trim());
                         rs.updateString("BILL_TOTAL_AMOUNT",sub_data[36].trim());
                         
                         if(this.transaction_date.length()< 2){
                         	rs.updateString("TRANSACTION_DATE",sub_data[3].trim());                        	
                         }else{
                         	rs.updateString("TRANSACTION_DATE",this.transaction_date);
                         }
                       
                         if(sub_data[35].trim().equals("")){
                             rs.updateString("INVOICE_TYPE","ORDER");
                         }else{
                             rs.updateString("INVOICE_TYPE","EXECUTE");
                         }
                         
                         rs.insertRow();
                         insert_count++;
                      }catch(Exception e){
                         TRN_Error.writeErrorLog(this.getConn(), "InterfaceTransaction",  this.getMessage(), e.toString(), message,"");
                     }
                 }
             }
             if(insert_count < a.size()-1){
                 setMessage("Error : "+ ""+(a.size()- (insert_count+1))+"/"+(a.size()-1)+" records complete : "+insert_count+" records.");
             }else{
                 setMessage("Complete "+(a.size()-1)+"/"+insert_count+" records.");
             }
             setBillDate(sub_data[3].trim());
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
