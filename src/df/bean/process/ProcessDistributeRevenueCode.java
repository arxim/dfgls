package df.bean.process;

import java.util.HashMap;
import df.bean.db.conn.DBConn;

/**
 * @author Sarunyoo Keawsopa
 * @TODO Process run change doctor code.
 * 
 */
public class ProcessDistributeRevenueCode implements ProcessMaster {

	private DBConn conn = null;
	private String[][] Data = null; // เก็บข้อมูล Data transaction
	private String[][] DataCondition = null; // เก็บข้อมูล Condition data
											 // ว่ามีการแพทย์กำหนดเงือนไข
											 // ไว้อย่างไรบ้าง
	
	private HashMap<String, String> hmCondition = null;

	/**
	 * @TODO is set init data
	 * @param conn
	 *            [ DBConn ]
	 * @param dataCondition
	 *            เมื่อมีการใช้งาน บังครับ ให้ TD นำหน้า ฟิลที่ต้องการ ตัวอย่าง
	 *            TD.HOSPITAL_CODE = '[DATA]'
	 */
	public ProcessDistributeRevenueCode(DBConn conn, HashMap<String, String> condition) {

		// set Init
		this.conn = conn;
		this.hmCondition = condition;
	}

	/**
	 * @TODO เป็นการกำหนด Data สำหรับที่ต้องการเอาข้อมูลไปแสดงผลการทำงาน Ext.
	 *       <pre> obj = new ProcessDistributeRevenueCode; </pre>
	 *       <pre> obj.doProcess(); </pre>
	 *       <pre> String [][] arrData = obj.getData(); </pre>
	 */
	public boolean doProcess() {
			// set data transaction
			System.out.println(sqlCommand(this.hmCondition));
			setData(this.conn.query(sqlCommand(this.hmCondition)));

			// set data condition
			setDataCondition(this.conn.query(sqlCommandGetCondition(this.hmCondition)));
			
			//System.out.println( "sql condition  : " + sqlCommandGetCondition(this.hmCondition));

			if(this.hmCondition.get("ACTION").equals("PROCESS")){ 
				this.actionProcess();
			} else { 
				this.actionSelect();
			}	
			
		return false;
	}

	private void actionSelect() {
		try {
			for (int i = 0; i <= (getData().length - 1); i++) {
				getData()[i][4]  = getData()[i][3];
			}
		} catch (Exception e) {
			
		}
	}

	private void actionProcess() {
		
		System.out.println("Process Distribute Revenue Doctor Code");
		
		try {
			double sumDataTrn = 0.0;
			double dataCondition = 0.0;

			int index = 0;
		
				for (int i = 0; i <= (getData().length - 1); i++) {
	
					dataCondition = Double.parseDouble(getDataCondition()[index][1]);
					sumDataTrn += Double.parseDouble(getData()[i][5]);
		
					if (sumDataTrn < dataCondition) {
						getData()[i][4] = getDataCondition()[index][0];
					} else {
						
						if (index != (getDataCondition().length - 1)) {
							++index;
						}
						
						sumDataTrn = Double.parseDouble(getData()[i][5]);
						getData()[i][4] = getDataCondition()[index][0];
						dataCondition = Double.parseDouble(getDataCondition()[index][1]);
					}
					//System.out.println( i + "|" + sumDataTrn + "|" + dataCondition);
				}
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
		}
	}

	public String[][] getData() {
		return Data;
	}

	public void setData(String[][] data) {
		Data = data;
	}

	public String[][] getDataCondition() {
		return DataCondition;
	}

	public void setDataCondition(String[][] dataCondition) {
			DataCondition = dataCondition;
	}

	public boolean doRollback() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean doBatchClose() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @param hmCondtion
	 *            1 TRN_START_DATE , TRN_END_DATE 2 DOCTOR_PROFILE_CODE 3
	 *            PAYOR_OFFICE_CODE 4 ORDER_ITEM_CODE 5 HN_NO 6 HOSPITAL_CODE
	 * 
	 * @return String Sql Command [ index colum 8]
	 * 
	 * @Description มีการเรียงลำดับ Colum แล้ว ห้าม เปลี่ยนแปลง verify date |
	 *              invoice no | line no | doctor cocd [old] | doctor code [new]
	 *              | amount after discount | dr amt
	 */
	private String sqlCommand(HashMap<String, String> hmCondition) {
		return "SELECT  DISTINCT "
				+ " TD.VERIFY_DATE , TD.INVOICE_NO ,  TD.LINE_NO , TD.DOCTOR_CODE , '' AS NEW_DOCTOR  ,  "
				+ " TD.AMOUNT_AFT_DISCOUNT , TD.DR_AMT  "
				+ " FROM TRN_DAILY TD "
				+ " INNER JOIN DOCTOR DR ON TD.DOCTOR_CODE  =  DR.CODE "
				+ " AND TD.HOSPITAL_CODE = DR.HOSPITAL_CODE AND  DR.ACTIVE = 1  "
				+ " INNER JOIN STP_DISTRIBUTE_REVENUE SDR "
				+ " ON DR.DOCTOR_PROFILE_CODE =  SDR.DOCTOR_PROFILE_CODE "
				+ " AND TD.HOSPITAL_CODE = SDR.HOSPITAL_CODE "
				+ " INNER JOIN ORDER_ITEM OI ON TD.ORDER_ITEM_CODE = OI.CODE AND TD.HOSPITAL_CODE = OI.HOSPITAL_CODE "
				+ " WHERE TD.IS_ONWARD <> 'Y' AND TD.ACTIVE = 1 " 
				// + " AND TD.COMPUTE_DAILY_DATE = '' "
				+ " AND TD.HOSPITAL_CODE = '" 
				+ hmCondition.get("HOSPITAL_CODE")
				+ "'"
				+ " AND TD.YYYY = '"
				+ hmCondition.get("YYYY")
				+ "'"
				+ " AND TD.MM = '"
				+ hmCondition.get("MM")
				+ "' "
				//+ " AND TD.TRANSACTION_DATE BETWEEN  '"
				//+ hmCondition.get("TRN_START_DATE")
				//+ "'  AND  '"
				//+ hmCondition.get("TRN_END_DATE")
				//+ "' "
				+ " AND DR.DOCTOR_PROFILE_CODE  =  '"
				+ hmCondition.get("DOCTOR_PROFILE_CODE")
				+ "' "
				+ " AND TD.PAYOR_OFFICE_CODE LIKE '%"
				+ hmCondition.get("PAYOR_OFFICE_CODE")
				+ "%' "
				+ " AND TD.ORDER_ITEM_CODE LIKE '%"
				+ hmCondition.get("ORDER_ITEM_CODE")
				+ "%' "
				+ " AND OI.ORDER_ITEM_CATEGORY_CODE LIKE '%"
				+ hmCondition.get("ORDER_ITEM_CATEGORY_CODE")
				+ "%'"
				+ " ORDER BY  TD.DOCTOR_CODE  ,  TD.INVOICE_NO ,  TD.LINE_NO ASC ";
	}

	/**
	 * 
	 * @param hmCondition
	 *            1 TRN_START_DATE , TRN_END_DATE 2 DOCTOR_PROFILE_CODE 3
	 *            PAYOR_OFFICE_CODE 4 ORDER_ITEM_CODE 5 HN_NO 6 HOSPITAL_CODE
	 * 
	 * @return sql command [ ดึงเงือนไข ที่มีการกำหนดไว้ ]
	 */
	private String sqlCommandGetCondition(HashMap<String, String> hmCondition) {
		return "SELECT  DRR.CODE ,  "
				+ " CASE WHEN SDR.DISTRIBUTE_PERCENT > 0 THEN " + " ((  "
				+
				// " SELECT SUM(DR_AMT) AS DATA FROM TRN_DAILY TDS " +
				" SELECT SUM(AMOUNT_AFT_DISCOUNT) AS DATA FROM TRN_DAILY TDS "
				+ " INNER JOIN DOCTOR DRS ON TDS.DOCTOR_CODE  =  DRS.CODE AND  DRS.HOSPITAL_CODE = '"
				+ hmCondition.get("HOSPITAL_CODE")
				+ "'"
				+ " WHERE " 
				+ " TDS.MM = '" + hmCondition.get("MM")  + "' AND TDS.YYYY = '" + hmCondition.get("YYYY") + "' AND TDS.IS_ONWARD <> 'Y' AND TDS.ACTIVE = 1 " 
				// + " AND TDS.COMPUTE_DAILY_DATE = '' " 
				+ " AND TDS.HOSPITAL_CODE = '"
				+ hmCondition.get("HOSPITAL_CODE")
				+ "' AND DRS.DOCTOR_PROFILE_CODE = DRR.DOCTOR_PROFILE_CODE "
				+ " GROUP BY DRS.DOCTOR_PROFILE_CODE "
				+ " ) * DISTRIBUTE_PERCENT ) / 100 "
				+ " ELSE "
				+ " SDR.DISTRIBUTE_AMOUNT "
				+ " END  AS RESULT "
				+ " FROM DOCTOR DRR "
				+ " INNER JOIN STP_DISTRIBUTE_REVENUE SDR ON DRR.CODE = SDR.DOCTOR_CODE AND SDR.HOSPITAL_CODE = DRR.HOSPITAL_CODE "
				+ " WHERE DRR.HOSPITAL_CODE = '"
				+ hmCondition.get("HOSPITAL_CODE")
				+ "'"
				+ " AND DRR.DOCTOR_PROFILE_CODE = '"
				+ hmCondition.get("DOCTOR_PROFILE_CODE")
				+ "'"
				+ " ORDER BY  DRR.CODE ";
	}
}
