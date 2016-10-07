package df.bean.process.summary;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;

public class SummaryMonthly extends Summary {

	protected String endDate = null;

	protected DBConnection objConn = null;

	public ArrayList<Map<String, String>> data = null;

	public SummaryMonthly(String hospitalCode, String yyyy, String mm,
			String term, String payDate) {

		this.hospitalCode = hospitalCode;
		this.yyyy = yyyy;
		this.mm = mm;
		this.term = term;
		this.payDate = payDate;

		if (!"".equals(this.payDate)) {

			if (this.payDate.substring(0, 6).equals(this.yyyy + this.mm)) {
				this.term = "1";
				this.endDate = "15";
			} else {
				this.term = "2";
				this.endDate = "31";
			}

		}
		this.objConn = new DBConnection();
		this.objConn.connectToLocal();

	}

	/**
	 * @return SQL summary DF amount
	 */
	public String getSQLSummaryDF() {
		return "SELECT '2' AS PAYMENT_TERM, TRN_DAILY.YYYY, TRN_DAILY.MM, TRN_DAILY.HOSPITAL_CODE, TRN_DAILY.DOCTOR_CODE, '04' AS PAYMENT_TYPE, "
				+ "SUM(TRN_DAILY.AMOUNT_AFT_DISCOUNT) AS AMOUNT_AFT_DISCOUNT, "
				+ "SUM(TRN_DAILY.AMOUNT_OF_DISCOUNT) AS AMOUNT_OF_DISCOUNT, "
				+ "SUM(TRN_DAILY.DR_AMT) AS DR_AMT, SUM(TRN_DAILY.HP_AMT) AS HP_AMT, "
				+ "SUM(TRN_DAILY.DR_AMT) AS DR_NET_PAID_AMT, "
				+ "SUM(CASE WHEN TRN_DAILY.PAY_BY_AR != 'Y' THEN TRN_DAILY.DR_AMT ELSE 0 END) AS SUM_PAY_BY_CASH, "
				+ "SUM(CASE WHEN TRN_DAILY.PAY_BY_AR = 'Y' AND (TRN_DAILY.RECEIPT_NO NOT LIKE 'AdvanceAR%' AND TRN_DAILY.IS_PARTIAL != 'Y' ) THEN TRN_DAILY.DR_AMT ELSE 0 END) AS SUM_PAY_BY_AR,"
				+ "SUM(CASE WHEN TRN_DAILY.PAY_BY_AR = 'Y' AND (TRN_DAILY.RECEIPT_NO LIKE 'AdvanceAR%' OR TRN_DAILY.IS_PARTIAL = 'Y') THEN TRN_DAILY.DR_AMT ELSE 0 END ) AS SUM_PAY_BY_PATIENT, "
				+ "SUM(CASE WHEN TRN_DAILY.GUARANTEE_CODE != '' AND TRN_DAILY.TRANSACTION_DATE LIKE '"
				+ this.yyyy
				+ this.mm
				+ "%' AND GUARANTEE_NOTE NOT LIKE '%EXTRA%' THEN DR_AMT ELSE 0 END) AS SUM_DR_IN_GUA,"
				+ "SUM(CASE WHEN TRN_DAILY.GUARANTEE_CODE != '' AND TRN_DAILY.TRANSACTION_DATE LIKE '"
				+ this.yyyy
				+ this.mm
				+ "%' AND GUARANTEE_NOTE LIKE '%EXTRA%' THEN DR_AMT ELSE 0 END) AS SUM_DR_IN_EXT,"
				+ "SUM(CASE WHEN TRN_DAILY.TAX_TYPE_CODE = '400' THEN DR_TAX_400 ELSE 0 END) AS SUM_TAX_400, "
				+ "SUM(CASE WHEN TRN_DAILY.TAX_TYPE_CODE = '401' THEN DR_TAX_401 ELSE 0 END) AS SUM_TAX_401, "
				+ "SUM(CASE WHEN TRN_DAILY.TAX_TYPE_CODE = '402' THEN DR_TAX_402 ELSE 0 END) AS SUM_TAX_402, "
				+ "SUM(CASE WHEN TRN_DAILY.TAX_TYPE_CODE = '406' THEN DR_TAX_406 ELSE 0 END) AS SUM_TAX_406,"
				+ "'"
				+ JDate.getDate()
				+ "' AS CREATE_DATE, '"
				+ JDate.getTime()
				+ "' AS CREATE_TIME, '' AS CREATE_USER_ID, "
				+ "DOCTOR.PAYMENT_MODE_CODE, DOCTOR.BANK_ACCOUNT_NO AS REF_PAID_NO, "
				+ "'20140210' AS PAYMENT_DATE, "
				+ "0 AS EXDR_AMT, "
				+ "0 AS EXCR_AMT, "
				+ "0 AS EXDR_400,"
				+ "0 AS EXDR_401, "
				+ "0 AS EXDR_402, "
				+ "0 AS EXDR_406, "
				+ "0 AS EXCR_400,"
				+ "0 AS EXCR_401,"
				+ "0 AS EXCR_402,"
				+ "0 AS EXCR_406,"
				+ "DOCTOR.IS_HOLD AS IS_HOLD, 0 AS SALARY_AMT, 0 AS POSITION_AMT, 0 AS GUARANTEE_AMOUNT,"
				+ "0 AS ABSORB_AMT, "
				+ "0 AS EXTRA_AMT "
				+

				// this for condition
				this.monthlyProcess(this.endDate)
				+

				/*
				 * "FROM "+ "DOCTOR LEFT OUTER JOIN TRN_DAILY "+
				 * "ON DOCTOR.CODE = TRN_DAILY.DOCTOR_CODE AND DOCTOR.HOSPITAL_CODE = TRN_DAILY.HOSPITAL_CODE "
				 * + "LEFT OUTER JOIN ORDER_ITEM "+
				 * "ON TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE AND TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "
				 * + "WHERE "+ "DOCTOR.HOSPITAL_CODE = '"+ this.hospitalCode
				 * +"' AND BATCH_NO = '' "+
				 * "AND ((TRN_DAILY.TRANSACTION_DATE BETWEEN '"
				 * +this.yyyy+this.mm+
				 * "01' AND '"+this.yyyy+this.mm+"31') OR ("+
				 * "TRN_DAILY.RECEIPT_DATE BETWEEN '" + this.yyyy+this.mm +
				 * "01' AND '"+this.yyyy+this.mm+"31'))"+
				 * "AND TRN_DAILY.GUARANTEE_TERM_YYYY = '' AND DOCTOR.PAYMENT_TIME = '2' AND ORDER_ITEM.PAYMENT_TIME = '2'"
				 * + "AND TRN_DAILY.YYYY = '"+this.yyyy
				 * +"' AND TRN_DAILY.MM = '"
				 * +this.mm+"' AND TRN_DAILY.ACTIVE = '1' AND DOCTOR.ACTIVE = '1' "
				 * +
				 */

				"GROUP BY TRN_DAILY.YYYY, TRN_DAILY.MM, TRN_DAILY.HOSPITAL_CODE, TRN_DAILY.DOCTOR_CODE,"
				+ "DOCTOR.PAYMENT_MODE_CODE, DOCTOR.BANK_ACCOUNT_NO, "
				+ "DOCTOR.IS_HOLD ";
	}

	/**
	 * @return SQL summary Expense
	 */
	public String getSQLSummaryExpense() {
		return " SELECT '2' AS PAYMENT_TERM, AJ.YYYY, AJ.MM, AJ.HOSPITAL_CODE, AJ.DOCTOR_CODE, '04' AS PAYMENT_TYPE , "
				+ " 0 AS AMOUNT_AFT_DISCOUNT, "
				+ " 0 AS AMOUNT_OF_DISCOUNT, "
				+ " 0 AS DR_AMT, "
				+ " 0 AS HP_AMT, "
				+ " SUM(AJ.AMOUNT*AJ.EXPENSE_SIGN) AS DR_NET_PAID_AMT, "
				+ " 0 AS SUM_PAY_BY_CASH, "
				+ " 0 AS SUM_PAY_BY_AR, "
				+ " 0 AS SUM_PAY_BY_PATIENT, "
				+ " 0 AS SUM_DR_IN_GUA,"
				+ " 0 AS SUM_DR_IN_EXT, "
				+ " 0 AS SUM_TAX_400, "
				+ " 0 AS SUM_TAX_401, "
				+ " 0 AS SUM_TAX_402, "
				+ " 0 AS SUM_TAX_406, "
				+ "'TODATE' AS CREATE_DATE, 'TOTIME' AS CREATE_TIME, '' AS CREATE_USER_ID, DOCTOR.PAYMENT_MODE_CODE, "
				+ "DOCTOR.BANK_ACCOUNT_NO AS REF_PAID_NO, '20140210' AS PAYMENT_DATE, "
				+ "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' THEN AJ.AMOUNT ELSE 0 END) AS EXDR_AMT, "
				+ "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' THEN AJ.AMOUNT ELSE 0 END) AS EXCR_AMT, "
				+ "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' AND AJ.TAX_TYPE_CODE = '400' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXDR_400,"
				+ "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' AND AJ.TAX_TYPE_CODE = '401' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXDR_401, "
				+ "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' AND AJ.TAX_TYPE_CODE = '402' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXDR_402, "
				+ "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' AND AJ.TAX_TYPE_CODE = '406' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXDR_406, "
				+ "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' AND AJ.TAX_TYPE_CODE = '400' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXCR_400, "
				+ "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' AND AJ.TAX_TYPE_CODE = '401' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXCR_401, "
				+ "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' AND AJ.TAX_TYPE_CODE = '402' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXCR_402, "
				+ "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' AND AJ.TAX_TYPE_CODE = '406' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXCR_406, "
				+ "DOCTOR.IS_HOLD AS IS_HOLD, 0 AS SALARY_AMT, 0 AS POSITION_AMT, 0 AS GUARANTEE_AMOUNT, "
				+ "SUM(CASE WHEN EX.ADJUST_TYPE = 'HP' THEN AJ.AMOUNT ELSE 0 END) AS ABSORB_AMT, "
				+ "SUM(CASE WHEN EX.ADJUST_TYPE = 'EX' THEN AJ.AMOUNT ELSE 0 END) AS EXTRA_AMT "
				+ "FROM DOCTOR  "
				+ "LEFT OUTER JOIN TRN_EXPENSE_DETAIL AS AJ "
				+ "ON DOCTOR.CODE = AJ.DOCTOR_CODE AND DOCTOR.HOSPITAL_CODE = AJ.HOSPITAL_CODE "
				+ "LEFT OUTER JOIN EXPENSE AS EX "
				+ "ON AJ.EXPENSE_CODE = EX.CODE AND AJ.HOSPITAL_CODE = EX.HOSPITAL_CODE "
				+ "WHERE DOCTOR.HOSPITAL_CODE = '"+this.hospitalCode+"' "
				+ "AND DOCTOR.PAYMENT_MODE_CODE NOT IN ('U','') AND DOCTOR.ACTIVE = '1' "
				+ "AND AJ.YYYY = '"+this.yyyy+"' AND AJ.MM = '"+this.mm+"' AND AJ.BATCH_NO = '' "
				+ "GROUP BY AJ.YYYY, AJ.MM, AJ.HOSPITAL_CODE, AJ.DOCTOR_CODE, "
				+ "DOCTOR.PAYMENT_MODE_CODE, DOCTOR.BANK_ACCOUNT_NO, "
				+ "DOCTOR.IS_HOLD ";

	}

	public String getSQLSummaryGuanrantee() {
		return " SELECT '2' AS PAYMENT_TERM, GT.YYYY, GT.MM, GT.HOSPITAL_CODE, GT.GUARANTEE_DR_CODE, '04' AS PAYMENT_TYPE, "
				+ " 0 AS AMOUNT_AFT_DISCOUNT, "
				+ " 0 AS AMOUNT_OF_DISCOUNT, "
				+ " 0 AS DR_AMT, "
				+ " 0 AS HP_AMT, "
				+ " 0 AS DR_NET_PAID_AMT, "
				+ " 0 AS SUM_PAY_BY_CASH, "
				+ " 0 AS SUM_PAY_BY_AR, "
				+ " 0 AS SUM_PAY_BY_PATIENT, "
				+ " 0 AS SUM_DR_IN_GUA, "
				+ " 0 AS SUM_DR_IN_EXT, "
				+ " 0 AS SUM_TAX_400, "
				+ " 0 AS SUM_TAX_401, "
				+ " 0 AS SUM_TAX_402, "
				+ " 0 AS SUM_TAX_406, "
				+ " 'TODATE' AS CREATE_DATE, 'TOTIME' AS CREATE_TIME, '' AS CREATE_USER_ID, DOCTOR.PAYMENT_MODE_CODE, "
				+ " DOCTOR.BANK_ACCOUNT_NO AS REF_PAID_NO, '"
				+ this.payDate
				+ "' AS PAYMENT_DATE, "
				+ " 0 AS EXDR_AMT, "
				+ " 0 AS EXCR_AMT, "
				+ " 0 AS EXDR_400, "
				+ " 0 AS EXDR_401, "
				+ " 0 AS EXDR_402, "
				+ " 0 AS EXDR_406, "
				+ " 0 AS EXCR_400, "
				+ " 0 AS EXCR_401, "
				+ " 0 AS EXCR_402, "
				+ " 0 AS EXCR_406, "
				+ " DOCTOR.IS_HOLD AS IS_HOLD, 0 AS SALARY_AMT, 0 AS POSITION_AMT, "
				+ " SUM(GT.GUARANTEE_AMOUNT) AS GUARANTEE_AMOUNT, "
				+ " 0 AS ABSORB_AMT, "
				+ " 0 AS EXTRA_AMT"
				+ " FROM DOCTOR  "
				+ " LEFT OUTER JOIN STP_GUARANTEE AS GT "
				+ " ON DOCTOR.CODE = GT.GUARANTEE_DR_CODE AND DOCTOR.HOSPITAL_CODE = GT.HOSPITAL_CODE "
				+ " WHERE GT.HOSPITAL_CODE = '"
				+ this.hospitalCode
				+ "' "
				+ " AND DOCTOR.PAYMENT_MODE_CODE NOT IN ('U','') AND DOCTOR.ACTIVE = '1' "
				+ " AND GT.YYYY = '"
				+ this.yyyy
				+ "' AND GT.MM = '"
				+ this.mm
				+ "' AND GT.ACTIVE = '1' "
				+ " GROUP BY GT.YYYY, GT.MM, GT.HOSPITAL_CODE, GT.GUARANTEE_DR_CODE, "
				+ " DOCTOR.PAYMENT_MODE_CODE, DOCTOR.BANK_ACCOUNT_NO, "
				+ " DOCTOR.IS_HOLD ";
	}

	private String monthlyProcess(String endDate) {
		String sql = "";
		String trnCon = "";
		String termCon = "";
		if (this.term.equals("1")) {
			trnCon = "AND TRN_DAILY.GUARANTEE_TERM_YYYY = '' ";
			termCon = "AND DOCTOR.PAYMENT_TIME = '2' AND ORDER_ITEM.PAYMENT_TIME = '2' ";
		} else { /* empty value */
		}
		sql = "FROM "
				+ "DOCTOR LEFT OUTER JOIN TRN_DAILY "
				+ "ON DOCTOR.CODE = TRN_DAILY.DOCTOR_CODE AND DOCTOR.HOSPITAL_CODE = TRN_DAILY.HOSPITAL_CODE "
				+ "LEFT OUTER JOIN ORDER_ITEM "
				+ "ON TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE AND TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "
				+ "WHERE " + "DOCTOR.HOSPITAL_CODE = '"
				+ this.hospitalCode
				+ "' AND BATCH_NO = '' "
				+ "AND ((TRN_DAILY.TRANSACTION_DATE BETWEEN '"
				+ this.yyyy
				+ this.mm
				+ "01' AND '"
				+ this.yyyy
				+ this.mm
				+ endDate
				+ "') OR ("
				+ "TRN_DAILY.RECEIPT_DATE BETWEEN '"
				+ this.yyyy
				+ this.mm
				+ "01' AND '"
				+ this.yyyy
				+ this.mm
				+ endDate
				+ "'))"
				+ trnCon
				+ termCon
				+ "AND TRN_DAILY.YYYY = '"
				+ this.yyyy
				+ "' AND TRN_DAILY.MM = '"
				+ this.mm
				+ "' AND TRN_DAILY.ACTIVE = '1' AND DOCTOR.ACTIVE = '1' ";
		return sql;
	}

	public String getSQLSummaryAll() {
		return "SELECT PAYMENT_TERM, YYYY, MM, HOSPITAL_CODE, DOCTOR_CODE, PAYMENT_TYPE, "
				+ "SUM(AMOUNT_AFT_DISCOUNT) AS AMOUNT_AFT_DISCOUNT, "
				+ "SUM(AMOUNT_OF_DISCOUNT) AS AMOUNT_OF_DISCOUNT, "
				+ "SUM(DR_AMT) AS DR_AMT, SUM(HP_AMT) AS HP_AMT, "
				+ "SUM(DR_NET_PAID_AMT) AS DR_NET_PAID_AMT, "
				+ "SUM(SUM_PAY_BY_CASH) AS SUM_PAY_BY_CASH, "
				+ "SUM(SUM_PAY_BY_AR) AS SUM_PAY_BY_AR, "
				+ "SUM(SUM_PAY_BY_PATIENT) AS SUM_PAY_BY_PATIENT, "
				+ "SUM(SUM_DR_IN_GUA) AS SUM_DR_IN_GUA, "
				+ "SUM(SUM_DR_IN_EXT) AS SUM_DR_IN_EXT, "
				+ "SUM(SUM_TAX_400) AS SUM_TAX_400, "
				+ "SUM(SUM_TAX_401) AS SUM_TAX_401, "
				+ "SUM(SUM_TAX_402) AS SUM_TAX_402, "
				+ "SUM(SUM_TAX_406) AS SUM_TAX_406, "
				+ "CREATE_DATE, CREATE_TIME, CREATE_USER_ID, PAYMENT_MODE_CODE, REF_PAID_NO, "
				+ "PAYMENT_DATE, "
				+ "SUM(EXDR_AMT) AS EXDR_AMT, "
				+ "SUM(EXCR_AMT) AS EXCR_AMT, "
				+ "SUM(EXDR_400) AS EXDR_400, "
				+ "SUM(EXDR_401) AS EXDR_401, "
				+ "SUM(EXDR_402) AS EXDR_402, "
				+ "SUM(EXDR_406) AS EXDR_406, "
				+ "SUM(EXCR_400) AS EXCR_400, "
				+ "SUM(EXCR_401) AS EXCR_401, "
				+ "SUM(EXCR_402) AS EXCR_402, "
				+ "SUM(EXCR_406) AS EXCR_406, "
				+ "IS_HOLD, SALARY_AMT, POSITION_AMT,  "
				+ "SUM(GUARANTEE_AMOUNT) AS GUARANTEE_AMOUNT, "
				+ "SUM(ABSORB_AMT) AS ABSORB_AMT, "
				+ "SUM(EXTRA_AMT) AS EXTRA_AMT "
				+ "FROM "
				+ " ( "
				+ this.getSQLSummaryDF()
				+ " UNION "
				+ this.getSQLSummaryExpense()
				+ " UNION "
				+ this.getSQLSummaryGuanrantee()
				+ "  ) Q"
				+

				" GROUP BY PAYMENT_TERM, YYYY, MM, HOSPITAL_CODE, DOCTOR_CODE, "
				+ " PAYMENT_TYPE, CREATE_DATE, CREATE_TIME , CREATE_USER_ID, "
				+ " PAYMENT_MODE_CODE, REF_PAID_NO, "
				+ " PAYMENT_DATE, "
				+ " IS_HOLD, SALARY_AMT, POSITION_AMT";

	}

	public ArrayList<HashMap<String, String>> getData() {

		ArrayList<HashMap<String, String>> lsQueryData = new ArrayList<HashMap<String, String>>();
		ResultSet rs = null;
		try {
			rs = this.rsExcute();
			ResultSetMetaData rsMetaData = rs.getMetaData();
			while (rs.next()) {
				HashMap<String, String> rtnData = new HashMap<String, String>();
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					String value = "";
					if (rs.getString(i) != null && !rs.getString(i).equals("")) {
						value = rs.getString(i);
						rtnData.put(rsMetaData.getColumnName(i), value);
					} else {
						rtnData.put(rsMetaData.getColumnName(i), value);
					}
				}
				lsQueryData.add(rtnData);
			}
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("SIZE : " + lsQueryData.size());
		return lsQueryData;

	}

	private ResultSet rsExcute() {
		if (getRevenueType().equals("df")) {
			System.out.println("Query DF : "+getSQLSummaryDF());
			return objConn.executeQuery(getSQLSummaryDF());
		} else if (getRevenueType().equals("ex")) {
			System.out.println("Query DF : "+getSQLSummaryExpense());
			return objConn.executeQuery(getSQLSummaryExpense());
		} else if (getRevenueType().equals("gt")) {
			System.out.println("Query DF : "+getSQLSummaryGuanrantee());
			return objConn.executeQuery(getSQLSummaryGuanrantee());
		} else if (getRevenueType().equals("all")) {
			System.out.println("Query DF : "+getSQLSummaryAll());
			return objConn.executeQuery(getSQLSummaryAll());
		} else
			return null;
	}

	public String getRevenueType() {
		return revenueType;
	}

	/**
	 * @param revenueType
	 *            ( "df" , "ex" , "gt" , "all" )
	 * @description Monthly can you select type revenue data.
	 */
	public void setRevenueType(String revenueType) {
		this.revenueType = revenueType;
	}

}
