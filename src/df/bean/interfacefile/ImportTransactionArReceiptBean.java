package df.bean.interfacefile;

import df.bean.db.conn.DBConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import df.bean.db.conn.DBConn;
import df.bean.db.table.Batch;
import df.bean.db.table.TRN_Error;
/**
 * @author arxim
 */
public class ImportTransactionArReceiptBean extends InterfaceTextFileBean {
	final static Logger logger = Logger.getLogger(ImportTransactionArReceiptBean.class);
    private ResultSet rs;
    private Statement stm, stm2;
    private String hospital_code;

    @Override
    public boolean insertData(String fn, DBConnection d) {
    	Batch b = null;
        boolean status = false;
        ArrayList a = null;
        String[] sub_data = null;
        String[] sub_date = null;
        String temp = "";
        double pay_amount = 0;
        int insert_count = 0;
        String isLastReceipt = "";
        try {
            setConn(d);//connect database
            setFileName(fn);//set filename read
            copyDataFile();//copy data from file to superclass arraylist member
            a = getData();//copy data from superclass arraylist member
            getConn().beginTrans();
            this.getConn().setUserID(this.getUserName());
        	this.getConn().setHospitalCode(this.hospital_code);
            stm = getConn().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stm2 = getConn().getConnection().createStatement();
            rs = stm.executeQuery("SELECT * FROM INT_ERP_AR_RECEIPT WHERE 1 <> 0");
            for(int i = 0; i<a.size(); i++){
                if(i>0 && a.get(i).toString().length()> 10){
                    temp = (String)a.get(i);
                    sub_data = temp.split("[|]");
                    sub_date = fn.split("[.]");
                    isLastReceipt = sub_data[11].trim();
                    double totalPay = 0;
                    rs.moveToInsertRow();
                    setMessage("Bill No = "+sub_data[1]+" Line No = "+i);
                    // rs.updateString("HOSPITAL_CODE",sub_data[0].trim().length() == 5 ? sub_data[0].trim().substring(2, 5) : sub_data[0].trim() );
                    rs.updateString("HOSPITAL_CODE" , this.hospital_code);
                    rs.updateString("BILL_NO",sub_data[1].trim());
                    rs.updateString("RECEIPT_NO",new String(sub_data[2].trim().getBytes(),"TIS-620").replaceAll("'", "_"));
                    rs.updateString("RECEIPT_DATE",sub_data[4].trim());
                    rs.updateString("RECEIPT_TYPE_CODE",sub_data[3].trim());
                    rs.updateDouble("BILL_AMOUNT",Double.parseDouble(""+sub_data[5].trim()));
                    rs.updateDouble("CREDIT_NOTE_AMOUNT",Double.parseDouble(""+sub_data[6].trim()));
                    rs.updateDouble("DEBIT_NOTE_AMOUNT",Double.parseDouble(""+sub_data[7].trim()));
                
                    try{
                    	//get previous payment amount before write off
                    	if(sub_data[10].trim().equals("W") && sub_data[11].trim().equals("Y") && Double.parseDouble(""+sub_data[8].trim()) == 0){
                    		String q = "SELECT ISNULL(SUM(PAYMENT_AMOUNT),0) AS PAYMENT_AMOUNT FROM INT_ERP_AR_RECEIPT WHERE HOSPITAL_CODE= '"+this.hospital_code+"' AND BILL_NO = '"+sub_data[1].trim()+"' AND IS_LAST_RECEIPT = 'N'";
	                    	ResultSet totalPayment = stm2.executeQuery(q);
	                    	while(totalPayment.next()){
	                    		totalPay = Double.parseDouble(totalPayment.getString("PAYMENT_AMOUNT").toString());
	                        }
	                    }
                    	//if bill amount equals to write off amount plus previous payment amount then set pay amount to 0
                    	if(Double.parseDouble(""+sub_data[5].trim()) == Double.parseDouble(""+sub_data[9].trim())+totalPay) {
                    		pay_amount = 0;
                    	}
                    	//if doc type is write off and it is last receipt of document and payment amount from flat file is 0 
                    	//then find payment amount for this transaction by using bill amount minus write off amount
                    	else if(sub_data[10].trim().equals("W") && sub_data[11].trim().equals("Y") && Double.parseDouble(""+sub_data[8].trim()) == 0){
                    		if(Double.parseDouble(""+sub_data[5].trim())-Double.parseDouble(""+sub_data[9].trim())>1){
	                    		//pay_amount = Double.parseDouble(""+sub_data[5].trim())-Double.parseDouble(""+sub_data[9].trim());
	                    		pay_amount = Double.parseDouble(""+sub_data[5].trim()) - ( Double.parseDouble(""+sub_data[9].trim())+totalPay );
	                    	}else{
	                    		pay_amount = 0;
	                    	}
	                    }
                    	//other conditions set pay amount = payment amount from flat file 
                    	else{
	                    	pay_amount = Double.parseDouble(""+sub_data[8].trim());
	                    }
                    }catch(Exception e){
                    	logger.error("Receipt type code is incorrect data in module Import WriteOff");
                    	rs.updateDouble("PAYMENT_AMOUNT",Double.parseDouble(""+sub_data[8].trim()));
                    }
                    
                    //if doc type is write off and it is not last receipt of document 
                    //but bill amount is equals to write off amount then isLastReceipt = "Y"
                    if(sub_data[10].trim().equals("W") && sub_data[11].trim().equals("N") && Double.parseDouble(""+sub_data[5].trim()) == Double.parseDouble(""+sub_data[9].trim())){
                    	isLastReceipt = "Y";
                    }
	                //if doc type is Receipt and it is not last receipt of document
	                //but payment amount is more or equals to bill amount then isLastReceipt = "Y"
                    else if(sub_data[10].trim().equals("R") && sub_data[11].trim().equals("N") && Double.parseDouble(""+sub_data[8].trim()) >= Double.parseDouble(""+sub_data[5].trim())){
                    	isLastReceipt = "Y";
                    }
                  //other conditions set isLastReceipt = isLastReceipt from flat file
                    else {
                    	isLastReceipt = sub_data[11].trim();
                    }

                    rs.updateDouble("PAYMENT_AMOUNT",pay_amount);
                    rs.updateDouble("WRITE_OFF_AMOUNT",Double.parseDouble(""+sub_data[9].trim())+totalPay);
                    rs.updateString("DOC_TYPE",""+sub_data[10].trim());
                    rs.updateString("IS_LAST_RECEIPT", isLastReceipt);
                    rs.updateString("TRANSACTION_DATE",sub_date[1]);                    	
                    /*
                    String fileName = fn.substring(fn.length()-14, fn.length());
                    if(fileName.length() > 15){
                        //ARDF00001.20111231 = not browse file
                        rs.updateString("TRANSACTION_DATE",fileName.substring(fn.length()-8, fileName.length()));                   	
                    }else{
                        //ARDF201112.txt = browse file
                        rs.updateString("TRANSACTION_DATE",fn.substring(fn.length()-10, fn.length()-4)+"01");                    	
                    }
                    */
                    rs.updateString("IS_LOADED", "N");
                    try{
                    	rs.insertRow();
                    	getConn().commitTrans();
	                    insert_count++;
                    }catch(Exception e){
                        TRN_Error.writeErrorLog(this.getConn(), "InterfaceArReceipt",  this.getMessage(), e.toString(), "","");
                    }
                }
            }
            if(insert_count < a.size()-1){
                setMessage("Error : "+ ""+(a.size()- (insert_count+1))+"/"+(a.size()-1)+" records complete : "+insert_count+" records.");
            }else{
                setMessage("Complete "+(a.size()-1)+"/"+insert_count+" records.");
            }
            
            status = true;
            rs.close();
        } catch (Exception f) {
            logger.error(f);
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
                if (stm2 != null) {
                    stm2.close();
                    stm2 = null;
                }  
            } catch (Exception ex)  {
                logger.error(ex);
            }   
        }
        return status;
    }

    @Override
    public boolean exportData(String fn, String hp, String type, String year, String month, DBConn d, String path) {
        throw new UnsupportedOperationException("Not supported yet.");
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