package df.bean.process;

import java.sql.ResultSet;
import java.sql.SQLException;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;

public class ProcessEnableTaxReportBean {
	String hospitalCode, year, term, printDate, userID, taxType;
	DBConnection conn;
	private String tax402Batch;
	private String tax406Term;
	private String tax406Year;

	public int processEnableTaxReport(String hospitalCode, String printDate, String year, String term, String userID, String taxType) {
		conn = new DBConnection();
		conn.connectToLocal();
		this.hospitalCode = hospitalCode;
		this.printDate = printDate.length() > 8 ? JDate.saveDate(printDate) : printDate;
		System.out.println("testTax : "+this.printDate);
		this.year = year;
		this.term = term;
		this.printDate = printDate;
		this.userID = userID;
		this.taxType = taxType;
		System.out.println(doEnableTaxReport());
		int isComplete = conn.executeUpdate(doEnableTaxReport());
		System.out.println(isComplete);
		conn.Close();
		return isComplete;
//		return 1;
	}

	private String doEnableTaxReport() {
		String sql = null;
		if (this.taxType.equals("Tax406")) {
			sql = "UPDATE SUMMARY_TAX_406 SET PRINT_DATE = '"+this.printDate+"', "
				+ "UPDATE_DATE = '"+JDate.getDate()+"', UPDATE_TIME = '"+JDate.getTime()+"', USER_ID = '"+this.userID+"' "
				+ "WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' "
				+ "AND YYYY = '"+this.year+"' AND MM = '"+this.term+"' "
				+ "AND PRINT_DATE = ''";
		} else if (this.taxType.equals("Tax402")) {
			sql = "UPDATE SUMMARY_TAX_402 SET PRINT_DATE = '"+this.printDate+"', BATCH_NO = '"+this.year+this.term+"',"
				+ "UPDATE_DATE = '"+JDate.getDate()+"', UPDATE_TIME = '"+JDate.getTime()+"', USER_ID = '"+this.userID+"' "
				+ "WHERE HOSPITAL_CODE = '"+this.hospitalCode+"' "
				+ "AND YYYY = '"+this.year+"' AND MM = '"+this.term+"' "
				+ "AND BATCH_NO = ''";
		}
		return sql;
	}
	
	public void getCurrentTaxTerm(String hospitalCode, String taxType) {
		ResultSet resultSet = null;
		String sql;
		if(taxType.equals("Tax402")) {
			sql = "SELECT TOP 1  YYYY FROM SUMMARY_TAX_402 WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND BATCH_NO = ''";
			System.out.println(sql);
			resultSet = conn.executeQuery(sql);
			try {
				while(resultSet.next()) {
					this.setTax402Batch(resultSet.getString("YYYY"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(taxType.equals("Tax402")) {
			sql = "SELECT TOP 1  YYYY, MM FROM SUMMARY_TAX_406 WHERE HOSPITAL_CODE = '"+hospitalCode+"' AND PRINT_DATE = ''";
			System.out.println(sql);
			resultSet = conn.executeQuery(sql);
			try {
				while(resultSet.next()) {
					this.setTax406Year(resultSet.getString("YYYY"));
					this.setTax406Term(resultSet.getString("MM"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
//		return "";
	}
	
	public String getTax402Batch() {
		return this.tax402Batch;
	}
	
	public void setTax402Batch(String batch) {
		this.tax402Batch = batch;
	}
	
	public String getTax406Term() {
		return this.tax406Term;
	}
	
	public void setTax406Term(String term) {
		this.tax406Term = term;
	}
	
	public String getTax406Year() {
		return this.tax406Year;
	}
	
	public void setTax406Year(String year) {
		this.tax406Year = year;
	}
}
