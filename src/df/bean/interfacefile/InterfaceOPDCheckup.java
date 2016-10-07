package df.bean.interfacefile;

import java.util.ArrayList;
import java.util.HashMap;

import df.bean.interfacefile.InterfaceFile;
import df.bean.obj.util.FileConn;
import df.bean.obj.util.JDate;
import df.bean.db.dao.IntHisBillDAO;

public class InterfaceOPDCheckup implements InterfaceFile {
	IntHisBillDAO inh = null;
	ArrayList<HashMap<String,String>> dataSource = null;
	String transactionDate = "";
	String hospitalCode = "";
	String userID = "";
	String message = "";

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}

	private boolean dataMapping() {
		HashMap<String, String> dataToInsert = new HashMap<String, String>();
		int columnComplete = 0;
		for(int t = 1; t<dataSource.size(); t++){
			try{
				dataToInsert.put("HOSPITAL_CODE", dataSource.get(t).get("BusinessUnit"));
				dataToInsert.put("EPISODE_TYPE", dataSource.get(t).get("EpisodeType"));
				dataToInsert.put("BILL_NO", dataSource.get(t).get("PatientNumber(HN)")+dataSource.get(t).get("EpisodeNumber"));
				dataToInsert.put("BILL_DATE", this.transactionDate.length() > 8 ? JDate.saveDate(this.getTransactionDate()) : this.transactionDate);
				dataToInsert.put("RECEIPT_TYPE_CODE", "A");
				dataToInsert.put("TRANSACTION_TYPE", dataSource.get(t).get("ReceiptType").equals("AR")? "INV" : "REV");
				dataToInsert.put("HN_NO", dataSource.get(t).get("PatientNumber(HN)"));
				dataToInsert.put("PATIENT_NAME", new String(dataSource.get(t).get("PatientName").trim().getBytes(),"TIS-620"));
				dataToInsert.put("EPISODE_NO", dataSource.get(t).get("EpisodeNumber"));
				dataToInsert.put("PAYOR_CODE", dataSource.get(t).get("PayorOfficeCode"));
				dataToInsert.put("PAYOR_NAME", new String(dataSource.get(t).get("PayorDescription").trim().getBytes(),"TIS-620"));
				dataToInsert.put("PAYOR_CATEGORY_CODE", "");//dataSource.get(t).get("PayorOfficeCategory"));
				dataToInsert.put("PAYOR_CATEGORY_DESC", "");//dataSource.get(t).get("PayorOfficeCategory Desc"));
				dataToInsert.put("ADMISSION_TYPE_CODE", dataSource.get(t).get("Charge Type"));
				dataToInsert.put("ORDER_ITEM_CODE", dataSource.get(t).get("Order Item Code"));
				dataToInsert.put("ORDER_ITEM_DESCRIPTION", new String(dataSource.get(t).get("Order Item Description").trim().getBytes(),"TIS-620"));
				dataToInsert.put("DOCTOR_PROFILE_CODE", dataSource.get(t).get("Care Provider Group Code"));
				dataToInsert.put("DOCTOR_PROFILE_NAME", new String(dataSource.get(t).get("Care Provider Group Desc").trim().getBytes(),"TIS-620"));
				dataToInsert.put("DOCTOR_CODE", dataSource.get(t).get("Care Provider Code").equals("") ? "99999999" : dataSource.get(t).get("Care Provider Code"));
				dataToInsert.put("DOCTOR_NAME", new String(dataSource.get(t).get("Care Provider Desc").trim().getBytes(),"TIS-620"));
				dataToInsert.put("AMOUNT_BEF_DISCOUNT", dataSource.get(t).get("Standard Charged Amount"));
				dataToInsert.put("AMOUNT_OF_DISCOUNT", dataSource.get(t).get("Discount Amount"));
				dataToInsert.put("ORDERED_DATE", dataSource.get(t).get("Date Ordered"));
				dataToInsert.put("ORDERED_TIME", dataSource.get(t).get("Time Ordered"));
				dataToInsert.put("NATIONALITY_CODE", dataSource.get(t).get("Nationality Code"));
				dataToInsert.put("NATIONALITY_DESCRIPTION", new String(dataSource.get(t).get("Nationality Description").trim().getBytes(),"TIS-620"));
				dataToInsert.put("PATIENT_LOCATION_CODE", dataSource.get(t).get("Patient Location Code"));
				dataToInsert.put("PATIENT_LOCATION_DESC", dataSource.get(t).get("Patient Location Desc"));
				dataToInsert.put("PATIENT_LOCATION_DEPT_CODE", dataSource.get(t).get("Patient Location Dept Code"));
				dataToInsert.put("PATIENT_LOCATION_DEPT_DESC", dataSource.get(t).get("Patient Location Dept Desc"));
				dataToInsert.put("RECEIVING_LOCATION_CODE", dataSource.get(t).get("Receiving Location Code"));
				dataToInsert.put("RECEIVING_LOCATION_DESC", dataSource.get(t).get("Receiving Location Desc"));
				dataToInsert.put("RECEIVING_LOCATION_DEPT_CODE", dataSource.get(t).get("Receiving Location Dept Code"));
				dataToInsert.put("RECEIVING_LOCATION_DEPT_DESC", dataSource.get(t).get("Receiving Location Dept Desc"));
				dataToInsert.put("LINE_NO", dataSource.get(t).get("Accession Number"));
				dataToInsert.put("VERIFIED_DATE", dataSource.get(t).get("Verified Date"));
				dataToInsert.put("VERIFIED_TIME", dataSource.get(t).get("Verified Time"));
				dataToInsert.put("BILL_TOTAL_AMOUNT", dataSource.get(t).get("Invoice Total Amount"));
				dataToInsert.put("TRANSACTION_DATE", this.transactionDate.length() > 8 ? JDate.saveDate(this.getTransactionDate()) : this.transactionDate);
				dataToInsert.put("INVOICE_TYPE", dataSource.get(t).get("Verified Date").equals("")? "ORDER" : "EXECUTE");
				dataToInsert.put("IS_ONWARD", "N");
				dataToInsert.put("OLD_AMOUNT", dataSource.get(t).get("Standard Charged Amount"));
				dataToInsert.put("DOCTOR_PRIVATE", dataSource.get(t).get("Private Doctor"));
				inh.createTransaction(dataToInsert);
			}catch (Exception e){
				System.out.println("Error While Mapping Running : "+e);
			}
            columnComplete++;
		}
        if(columnComplete < dataSource.size()-1){
            setMessage("Error : "+ ""+(dataSource.size()- (columnComplete+1))+"/"+(dataSource.size()-1)+" records complete : "+columnComplete+" records.");
        }else{
            setMessage("Complete "+(dataSource.size()-1)+"/"+columnComplete+" records.");
        }		
		return false;
	}
	@Override
	public boolean doProcess(ArrayList<HashMap<String,String>> dts){
		inh = new IntHisBillDAO();
		inh.prepareCreateTransaction();
		dataSource = dts;
		this.dataMapping();
		return true;
	}
	@Override
	public boolean doRollback() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean doBatchClose() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static void main(String[] args){
		String sourceFile = "E:/DFPRJ015.20151001";
		InterfaceOPDCheckup rf = new InterfaceOPDCheckup();
		FileConn fc = new FileConn();
		rf.doProcess(fc.getDataFromFile(sourceFile));
		//rf.doProcess(fc.getDataFromFile(psourceFileSMB,"","")); //SMB
	}
}