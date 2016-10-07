package df.bean.db.table;

public class TrnExpenseDetail {
	String doctorCode;
	double amount;
	String lineNumber;
	int expense_sign;
	
	
	

	public TrnExpenseDetail(String doctorCode, double amount, String lineNumber,int expense_sign) {
	
		this.doctorCode = doctorCode;
		this.amount = amount;
		this.lineNumber = lineNumber;
		this.expense_sign = expense_sign;
	}

	public String getDoctorCode() {
		return doctorCode;
	}

	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}
	

	public int getExpense_sign() {
		return expense_sign;
	}

	public void setExpense_sign(int expense_sign) {
		this.expense_sign = expense_sign;
	}

	

	
	
	
	
	
}
