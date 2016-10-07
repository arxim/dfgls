package df.bean.process;

public abstract class ProcessMasterMonthly  implements ProcessMaster{


	/**
	 *@description this hospital code for application
	 */
	protected String hospitalCode = null;
	
	/**
	 *@description this Year on accounting system doctor fee 	 
	 */
	protected String yyyy = null;
	
	
	/**
	 *@description this month  on accounting system doctor fee
	 */
	protected String mm = null;
	
	/**
	 *@description this term on accounting system doctor fee
	 */
	protected String term = null;
	
	/**
	 * @description this payment to doctor 
	 * 
	 */
	protected String payDate =  null;

	
	protected abstract void setHospitalCode(String hospitalCode);

	protected abstract void setYyyy(String yyyy);

	protected abstract void setMm(String mm);

	protected abstract void setTerm(String term);

	protected abstract void setPayDate(String payDate);
	
	protected abstract boolean initMonthly();
	
	
	// protected abstract void set
	
	
	
}
