package df.bean.process;

import java.util.List;
import java.util.Map;
import df.bean.db.dao.StpMethodAllocSetupDAO;
import df.bean.db.dao.TrnDailyDAO;

public class ProcessStepCalculate implements ProcessMaster{
	//Attribute Zone
	private String hospitalCode;
	private String doctorCode;
	private String doctorCategory;
	private String month;
	private String year;
	private String userId;
	private String taxType;
	private String taxSource;
	private String admissionType;
	
	//Getter Setter Method Zone
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getDoctorCode() {
		return doctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}
	public String getDoctorCategory() {
		return doctorCategory;
	}
	public void setDoctorCategory(String doctorCategory) {
		this.doctorCategory = doctorCategory;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public String getTaxSource() {
		return taxSource;
	}
	public void setTaxSource(String taxSource) {
		this.taxSource = taxSource;
	}
	public String getAdmissionType() {
		return admissionType;
	}
	public void setAdmissionType(String admissionType) {
		this.admissionType = admissionType;
	}

	//Functional Zone
	private List<Map<String, Object>> getTransaction(String hospital, String admissionType, String doctorCategory, String doctor, String month, String year){
		TrnDailyDAO td = new TrnDailyDAO();
		return td.getTrnDailyForStepCalByDoctor(hospital, admissionType, doctorCategory, doctor, month, year);
	}
	private List<Map<String, Object>> getStepMethod(String hospital, String doctorProfile){
		StpMethodAllocSetupDAO methodSetup = new StpMethodAllocSetupDAO();
		return methodSetup.getStpMethodAllocSetup(this.hospitalCode, this.doctorCode);
	}
	
	//Core Functional Zone
	@Override
	public boolean doProcess(){
		System.out.println("Start Process Calculate");
		List<Map<String, Object>> methodStepDoctor = this.getStepMethod(this.hospitalCode, this.doctorCode);
		if(methodStepDoctor.size()>0){
			this.taxType = methodStepDoctor.get(0).get("TAX_TYPE_CODE").toString();
			this.taxSource = methodStepDoctor.get(0).get("TAX_SOURCE").toString();
			this.admissionType = methodStepDoctor.get(0).get("ADMISSION_TYPE_CODE").toString();
		}
		List<Map<String,Object>> transactionDF = this.getTransaction(this.hospitalCode, this.admissionType, this.doctorCategory, this.doctorCode, this.month, this.year);
		TrnDailyDAO td = new TrnDailyDAO(false);//Prepare Statement
		double minRate = 0, maxRate = 0, allocateRate = 0;
		double amountPrice = 0, sumTrnAmount = 0, previousSumTrnAmount = 0, accuAllocateAmount = 0, sharingAmount = 0;
		
		//Begin Calculate Step
		for(int i = 0; i < transactionDF.size(); i++ ){
			Map<String, Object> trnSeq = transactionDF.get(i);
			amountPrice = Double.parseDouble(trnSeq.get("AMOUNT_AFT_DISCOUNT").toString());
			sumTrnAmount = sumTrnAmount+amountPrice;
			if( methodStepDoctor.size() > 0 ){
				try{
					accuAllocateAmount = 0;
					for( int x = 0; x < methodStepDoctor.size(); x++ ){
						//Initial Value
						minRate = Double.parseDouble(methodStepDoctor.get(x).get("AMOUNT_START").toString());
						maxRate = Double.parseDouble(methodStepDoctor.get(x).get("AMOUNT_END").toString());
						allocateRate = Double.parseDouble(methodStepDoctor.get(x).get("ALLOCATE_PCT").toString());

						//Allocate
						if( sumTrnAmount <= maxRate && sumTrnAmount > minRate ){//in step rate
							accuAllocateAmount = accuAllocateAmount + (((sumTrnAmount-previousSumTrnAmount) * allocateRate) / 100);
							td.setAllocatePct(allocateRate);
						}else if( sumTrnAmount > maxRate && previousSumTrnAmount < maxRate ){//value between 2 step
							if(x+1 == methodStepDoctor.size()){//some in rate last step and over last step
								accuAllocateAmount = accuAllocateAmount + (((sumTrnAmount-previousSumTrnAmount) * allocateRate) / 100);
								td.setAllocatePct(allocateRate);
							}else{//some in current rate and some in next step rate
								accuAllocateAmount = accuAllocateAmount+(((maxRate-previousSumTrnAmount) * allocateRate) / 100);
								previousSumTrnAmount = previousSumTrnAmount + (maxRate-previousSumTrnAmount);
								td.setAllocatePct(allocateRate);
							}
						}else if( sumTrnAmount < minRate && x == 0){//not in first step rate 
							accuAllocateAmount = accuAllocateAmount + (((sumTrnAmount-previousSumTrnAmount) * allocateRate) / 100);								
							td.setAllocatePct(allocateRate);
						}else if( x+1 == methodStepDoctor.size() && sumTrnAmount > maxRate){//over last step (use last step rate)
							accuAllocateAmount = accuAllocateAmount + (((sumTrnAmount-previousSumTrnAmount) * allocateRate) / 100);								
							td.setAllocatePct(allocateRate);
						}
					}
					
					sharingAmount = amountPrice > accuAllocateAmount ? amountPrice - accuAllocateAmount : 0;
					//System.out.println(JDate.getTime()+" No."+i+" Amount = "+amountPrice+" Allocate = "+accuAllocateAmount+" Summary = "+sumTrnAmount);
				}catch(Exception e){ System.out.println("Step Allocate Error Exception : "+e); }
			}else{}
			previousSumTrnAmount = sumTrnAmount;
			td.setTaxType(this.taxType);
			td.setDoctorCategory(this.doctorCategory);
			td.setDrAmt(accuAllocateAmount);
			td.setHpAmt(sharingAmount);
			td.setSeq(""+(i+1));
			td.setTaxSource(taxSource);
			td.setUserId(this.getUserId());
			td.setAllocateAmt(previousSumTrnAmount);
			//td.updateStepCalculate(trnSeq);
			td.updatePrepareStepCalculate(trnSeq);
		}//End Calculate Step
		return true;
	}
	@Override
	public boolean doRollback(){
		System.out.println("Rollback");
		TrnDailyDAO td = new TrnDailyDAO(false);//Prepare Statement
		td.setHospital(this.getHospitalCode());
		td.setMonth(this.getMonth());
		td.setYear(this.getYear());
		return td.rollbackStepCalculate();
	}
	@Override
	public boolean doBatchClose(){
		//do not implement this method
		return true;
	}
	
	//Test Zone
	public static void main(String[] args) {
		/*
		psc.setDoctorCode("11900174");//11900174/11931973
		psc.setHospitalCode("011");
		psc.setMonth("05");
		psc.setYear("2015");
		psc.setUserId("");
		System.out.println("Step Calculate end with : "+psc.doProcess());
		*/
	}
}