package df.bean.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import df.bean.db.dao.StpMethodAllocateDAO;
import df.bean.db.dao.TrnDailyDAO;

public class ProcessBasicAllocateBean {
	TrnDailyDAO trn = null; //DAO for prepare statement select
	TrnDailyDAO upTrn = null; //DAO for prepare statement update
	StpMethodAllocateDAO mth = null;
	List<Map<String, Object>> methodStepDoctorList = null;
	List<Map<String,Object>> trnDailyList = null;
	List<String> orderConditionList = null;
	List<Map<String,Object>> trnsaction = null;
	String userID = null;
	
	//----- Initial -----
	public ProcessBasicAllocateBean(){
		methodStepDoctorList = new ArrayList<Map<String, Object>>();
		trnDailyList = new ArrayList<Map<String, Object>>();
		orderConditionList = new ArrayList<String>();
		trn = new TrnDailyDAO();
		upTrn = new TrnDailyDAO();
		mth = new StpMethodAllocateDAO();
	}
	private void prepareCondition(){
		// define condition
		orderConditionList.add("DOCTOR_CATEGORY");
		orderConditionList.add("PAYOR_CATEGORY");
		orderConditionList.add("PAYOR");
		orderConditionList.add("DOCTOR");
		orderConditionList.add("DOCTOR_TREATMENT");
		orderConditionList.add("PRIVATE_DOCTOR");
		orderConditionList.add("ADMISSION_TYPE");
		orderConditionList.add("IS_PROCEDURE");
		orderConditionList.add("ORDER_ITEM_CATEGORY");
		orderConditionList.add("ORDER_ITEM");
	}
	
	//----- Functional -----
	public String processRequest(String MAX_ROW, String curRow, String HOSPITAL_CODE, String START_DATE, String END_DATE, String INVOICE_NO, String LINE_NO, String TRANSACTION_DATE, String userId) {
		this.userID = userId;
		if(curRow.equals("0")){
			trn.prepareSelectCalculate();
			upTrn.prepareUpdateCalculate();
			methodStepDoctorList = mth.getBasicAllocateMethod(HOSPITAL_CODE);
    		prepareCondition();
    	}
		trnDailyList = this.trn.getPsTrnDailyForBasicCalculate(HOSPITAL_CODE, START_DATE, END_DATE, INVOICE_NO, LINE_NO); // PrepareStatement
		//trnDailyList = this.trn.getTrnDailyForBasicCalculate(HOSPITAL_CODE, START_DATE, END_DATE, INVOICE_NO, LINE_NO); // Statement
    	return this.calculateBasicAllocate();
	}
	
	private String calculateBasicAllocate(){
		double amountStart=0.00, amountEnd=0.00, amountAftDiscount = 0.00;
		double allocatePct = 0.00, allocateAmt = 0.00, taxRate = 0.00, taxAmt = 0.00;
		String status = "0";
		this.prepareCondition();
		for(int i = 0; i<trnDailyList.size(); i++){
			boolean match = false;
			methodStepDoctor:
			for(int x = 0; x<methodStepDoctorList.size(); x++){
				for(int icon = 0; icon<orderConditionList.size(); icon++){ //check for each field in condition
					if(!methodStepDoctorList.get(x).get(orderConditionList.get(icon).toString()).equals("")){
						if(!methodStepDoctorList.get(x).get(orderConditionList.get(icon).toString()).equals(trnDailyList.get(i).get(orderConditionList.get(icon).toString()))){
							continue methodStepDoctor;
						}else{
							//match = true;
							//check set time range (new condition)
							try{
								//System.out.println(methodStepDoctorList.get(x).get("TIME_START"));
								if( methodStepDoctorList.get(x).get("TIME_START") == null || methodStepDoctorList.get(x).get("TIME_START").equals("")){
									//if not set time range
									match = true;
								}else{
									int timeStart = Integer.parseInt(methodStepDoctorList.get(x).get("TIME_START").toString());
									int timeEnd = Integer.parseInt(methodStepDoctorList.get(x).get("TIME_END").toString());
									int tranTime = Integer.parseInt( trnDailyList.get(i).get("VERIFY_TIME").toString());
									if( timeStart < timeEnd || timeStart == timeEnd ){
										if( tranTime > timeStart && tranTime < timeEnd ){
											match = true;
										}else{
											continue methodStepDoctor;
										}
									}else{
										if( tranTime > timeStart || tranTime < timeEnd ){
											match = true;
										}else{
											continue methodStepDoctor;
										}
									}
								}
							}catch (Exception e){
								match = true;
							}
							//end new condition
						}
					}else{
						match = true;
					}
				}
				//allocate in condition
				amountStart = Double.parseDouble(methodStepDoctorList.get(x).get("AMOUNT_START").toString());
				amountEnd = Double.parseDouble(methodStepDoctorList.get(x).get("AMOUNT_END").toString());
				amountAftDiscount = Double.parseDouble(trnDailyList.get(i).get("AMOUNT_AFT_DISCOUNT").toString());
				allocateAmt = Double.parseDouble(methodStepDoctorList.get(x).get("NORMAL_ALLOCATE_AMT").toString());
				allocatePct = Double.parseDouble(methodStepDoctorList.get(x).get("NORMAL_ALLOCATE_PCT").toString());
				if(match){
					double df = 0.00;
					if( (amountStart == 0 && amountEnd == 0) || (amountStart <= amountAftDiscount && amountEnd >= amountAftDiscount) ){
						if(allocateAmt > 0.00){
						//allocate amount
							if(allocateAmt > amountAftDiscount){
								df = amountAftDiscount;
							}else{
								df = allocateAmt;
							}
						}else{
						// allocate pencentage
							df = ( Double.parseDouble(trnDailyList.get(i).get("AMOUNT_AFT_DISCOUNT").toString()) * allocatePct ) / 100;
						}
						taxRate = Double.parseDouble(methodStepDoctorList.get(x).get("TAX_RATE").toString());
						taxAmt = methodStepDoctorList.get(x).get("TAX_SOURCE").toString().equals("BF") ? amountAftDiscount*taxRate : df*taxRate ;
					}else if(amountStart == amountAftDiscount){ 
						// allocate fix price
						df = allocateAmt;
						taxRate = Double.parseDouble(methodStepDoctorList.get(x).get("TAX_RATE").toString());
						taxAmt = methodStepDoctorList.get(x).get("TAX_SOURCE").toString().equals("BF") ? amountAftDiscount*taxRate : df*taxRate ;
					}else{
						continue methodStepDoctor;
					}
					trnDailyList.get(i).put("SEQ_STEP", 0.00);
					trnDailyList.get(i).put("COMPUTE_DAILY_USER_ID", this.userID);
					trnDailyList.get(i).put("NOR_ALLOCATE_AMT", allocateAmt);
					trnDailyList.get(i).put("NOR_ALLOCATE_PCT", allocatePct);
					trnDailyList.get(i).put("DR_AMT", df);
					trnDailyList.get(i).put("HP_AMT", amountAftDiscount - df);
					trnDailyList.get(i).put("TAX_TYPE_CODE", methodStepDoctorList.get(x).get("TAX_TYPE_CODE").toString());
					trnDailyList.get(i).put("DR_TAX_400", methodStepDoctorList.get(x).get("TAX_TYPE_CODE").toString().equals("400") ? taxAmt : "0" );
					trnDailyList.get(i).put("DR_TAX_401", methodStepDoctorList.get(x).get("TAX_TYPE_CODE").toString().equals("401") ? taxAmt : "0" );
					trnDailyList.get(i).put("DR_TAX_402", methodStepDoctorList.get(x).get("TAX_TYPE_CODE").toString().equals("402") ? taxAmt : "0" );
					trnDailyList.get(i).put("DR_TAX_406", methodStepDoctorList.get(x).get("TAX_TYPE_CODE").toString().equals("406") ? taxAmt : "0" );
					trnDailyList.get(i).put("IS_GUARANTEE_FROM_ALLOC", methodStepDoctorList.get(x).get("GUARANTEE_SOURCE").toString().equals("") ? "" : methodStepDoctorList.get(x).get("GUARANTEE_SOURCE").toString().equals("BF") ? "N" : "Y");
					trnDailyList.get(i).put("TAX_FROM_ALLOCATE", methodStepDoctorList.get(x).get("TAX_SOURCE").toString());
					upTrn.updatePrepareCalculate(trnDailyList.get(i));
					status = "1";
					break methodStepDoctor;

				}else{
					continue methodStepDoctor;
				}
			} // End For Loop Method Basic Allocate
		} // End For Loop Transaction
		return status;
	} // End Method Basic Allocate
	
	public boolean doRecalculate(String hospitalCode, String invoiceNo, String lineNo, String transactionDate, String userId){
		boolean status = true;
		status = trn.rollbackBasicCalculateByLine(hospitalCode, transactionDate, invoiceNo, lineNo);
		if(status){
			this.processRequest("0", "0", hospitalCode, transactionDate, transactionDate, invoiceNo, lineNo, transactionDate, userId);
		}
		return status;
	}
}