package df.bean.process.summary;

public class SummaryCreater {

	private String hospitalCode = null;

	private String mm = null;

	private String yyyy = null;

	private String term = null;

	private String payDate  = null;
	
	
	public SummaryCreater(String hospitalCode , String yyyy , String mm , String term , String payDate){ 
		
		this.hospitalCode = hospitalCode;
		this.yyyy = yyyy;
		this.mm = mm;
		this.term  = term;
		this.payDate = payDate;
		
	}
		
	public Summary createSummary(String  type){
		if(type.equals("monthly")){
			return new SummaryMonthly(hospitalCode, yyyy, mm, term, payDate);
		} return null;
	}
}
