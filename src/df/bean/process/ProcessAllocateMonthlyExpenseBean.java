package df.bean.process;

import java.util.HashMap;
import java.util.Iterator;

import df.bean.db.conn.DBConnection;
import df.bean.process.summary.Summary;
import df.bean.process.summary.SummaryCreater;

public class ProcessAllocateMonthlyExpenseBean extends ProcessMasterMonthly {

	
	private Iterator<HashMap<String, String>> inIterator = null;
	
	private DBConnection objConn = null;
	
	
	public ProcessAllocateMonthlyExpenseBean(String hospitalCode , String yyyy  , String mm , String term , String payDate){
		
		setHospitalCode(hospitalCode);
		setYyyy(yyyy);
		setMm(mm);
		setTerm(term);
		setPayDate(payDate);
		
		SummaryCreater create = new SummaryCreater(this.hospitalCode , this.yyyy , this.mm  , this.term , this.payDate);
    	Summary  summary  = create.createSummary("monthly");
    	summary.setRevenueType("all");
    	
    	inIterator = summary.getData().iterator();
    	
    	objConn = new DBConnection();
    	objConn.connectToLocal();
    	
	}

	@Override
	public boolean doProcess() {
	
		System.out.println("SystemProcess.....");
	
    	
    	while(inIterator.hasNext())
    	{
    		HashMap<String, String>  unitData  = inIterator.next();
    	
    		if(Double.parseDouble(unitData.get("EXCR_AMT")) > 0){
    			
    			checkExpense(unitData.get("DOCTOR_CODE"));
    			
    		}
    	
    	
    	}
    	
		return false;
	
	}

	@Override
	public boolean doRollback() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doBatchClose() {
		return false;
	}
	
	private String checkExpense(String DrCode) {
		System.out.println(" DATA : "+ DrCode);
		
		String sqlExpense  =  "SELECT AMOUNT FROM TRN_EXPENSE_DETAIL "
				+ " WHERE HOSPITAL_CODE =  '" + this.hospitalCode + "'"
				+ " AND YYYY+MM = '" + this.yyyy+this.mm + "' "
				+ " AND DOCTOR_CODE = '" + DrCode + "' "
				+ " AND SIGN = '-1' ";
		
		
		
		return null;
	}

	@Override
	protected void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	@Override
	protected void setYyyy(String yyyy) {
		this.yyyy  = yyyy;
	}

	@Override
	protected void setMm(String mm) {
		this.mm = mm;
	}

	@Override
	protected void setTerm(String term) {
		this.term = term;
	}

	@Override
	protected void setPayDate(String payDate) {
		this.payDate  =  payDate;
		
	}

	@Override
	protected boolean initMonthly() {
		return false;
	}
	

}
