package df.bean.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import df.bean.obj.util.JDate;
import df.bean.obj.util.Utils;
import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;

public class ProcessHolidayCalculate {

	private static final ArrayList<TreeMap<Integer, String>>DATA_STP_HOLIDAY = new ArrayList<TreeMap<Integer, String>>();
	private String msg;
	public String getMsg() {
		return msg;
	}
	/**
	 * @param mm 
	 * @param yyyy 
	 * @param args
	 */	
	public void processHoliday(String hospitalcode, String yyyy, String mm){
		DBConnection con =new DBConnection();
		con.connectToLocal();
		DBConnection connsb=new DBConnection();
		connsb.connectToLocal(); 	
		connsb.beginTrans();
		Batch b = new Batch(hospitalcode, con);
		String yearMonth = b.getBatchNo();
		String messageIn = "";
		String Command="SELECT * FROM STP_HOLIDAY WHERE HOSPITAL_CODE = '"+hospitalcode+"' AND YYYY='"+yyyy+"' AND MM='"+mm+"' AND ACTIVE='1' AND INCLUDE LIKE '%' ORDER BY NOR_ALLOCATE_PCT ";
		ResultSet rsMethod=con.executeQuery(Command);
		
		try {
			int a=rsMethod.getMetaData().getColumnCount();
			while(rsMethod.next()){
				ArrayList<String> conditions = new ArrayList<String>();
				if( !rsMethod.getString("ADMISSION_TYPE_CODE").equals("")){
					conditions.add(" ADMISSION_TYPE_CODE "+(rsMethod.getString("ADMISSION_TYPE_CODE").equals("ALL")?" IN('I','O') ":"='"+rsMethod.getString("ADMISSION_TYPE_CODE")+"'"));
				}
				if( !rsMethod.getString("DOCTOR_CATEGORY_CODE").equals("")){
					conditions.add(" DOC.DOCTOR_CATEGORY_CODE='"+rsMethod.getString("DOCTOR_CATEGORY_CODE")+"'");
				}
				if( !rsMethod.getString("ORDER_ITEM_CATEGORY_CODE").equals("")){
					conditions.add(" OI.ORDER_ITEM_CATEGORY_CODE='"+rsMethod.getString("ORDER_ITEM_CATEGORY_CODE")+"'");
				}
				if( !rsMethod.getString("DOCTOR_CODE").equals("")){
					conditions.add(" DOCTOR_CODE='"+rsMethod.getString("DOCTOR_CODE")+"'");
				}
				if( !rsMethod.getString("ORDER_ITEM_CODE").equals("")){
					conditions.add(" ORDER_ITEM_CODE='"+rsMethod.getString("ORDER_ITEM_CODE")+"'");
				}
				if( !rsMethod.getString("PATIENT_DEPARTMENT_CODE").equals("")){
					conditions.add(" PATIENT_DEPARTMENT_CODE='"+rsMethod.getString("PATIENT_DEPARTMENT_CODE")+"'");
				}
				double NOR_ALLOCATE_PCT=0.00;
				if(!rsMethod.getString("NOR_ALLOCATE_PCT").equals("0.00")){
					NOR_ALLOCATE_PCT=rsMethod.getDouble("NOR_ALLOCATE_PCT")/100;
				}
				String INCLUDE="";
				if( !rsMethod.getString("INCLUDE").equals("")){
					INCLUDE=rsMethod.getString("INCLUDE");
				}
				String sqlqu=
					"INSERT INTO HIS_TRN_DAILY "
					+"SELECT TD.*,'"+INCLUDE+"' AS INCLUDE, 'HOLIDAY' AS TAG "
					+"FROM TRN_DAILY AS TD "
					//+"WITH (index (discharge_index)) " //change verify date 20180222
					+"LEFT OUTER JOIN ORDER_ITEM AS OI ON TD.ORDER_ITEM_CODE=OI.CODE AND TD.HOSPITAL_CODE=OI.HOSPITAL_CODE "
					+"LEFT OUTER JOIN DOCTOR AS DOC ON TD.DOCTOR_CODE=DOC.CODE AND TD.HOSPITAL_CODE=DOC.HOSPITAL_CODE "
					+"WHERE "
					+"TD.LINE_NO+TD.INVOICE_NO+TD.TRANSACTION_DATE NOT IN(SELECT LINE_NO+INVOICE_NO+TRANSACTION_DATE FROM HIS_TRN_DAILY WHERE TAG='HOLIDAY' AND HOSPITAL_CODE='"+hospitalcode+"') "
					+"AND TD.HOSPITAL_CODE='"+hospitalcode+"' "
					+"AND (TD.VERIFY_DATE = '"+rsMethod.getString("YYYY")+rsMethod.getString("MM")+rsMethod.getString("DD")+"' "
					+"AND TD.TRANSACTION_DATE LIKE '"+yearMonth+"%') " //change verify date 20180222
					+"AND (TD.BATCH_NO = '' OR TD.BATCH_NO IS NULL) "//change verify date 20180222
					+"AND TD.ACTIVE='1' "
					+"AND TD.DR_AMT > 0 "
					+"AND "+Utils.Join(conditions, " AND ");
				System.out.println(sqlqu);
				connsb.executeUpdate(sqlqu);
			}
			if(rsMethod.getString("INCLUDE").equals("Y")){
				DBConnection con2 =new DBConnection();
				con2.connectToLocal();
				String Command2="SELECT * FROM STP_HOLIDAY WHERE YYYY='"+yyyy+"' AND MM='"+mm+"' AND ACTIVE='1' AND INCLUDE='Y' ORDER BY INCLUDE ASC,DOCTOR_CATEGORY_CODE ASC,ORDER_ITEM_CATEGORY_CODE ASC,DOCTOR_CODE ASC,ORDER_ITEM_CODE ASC ,ADMISSION_TYPE_CODE DESC";
				ResultSet rsMethod2=con2.executeQuery(Command2);
				while(rsMethod2.next()){
					String ADTYPE="";
					if(rsMethod2.getString("ADMISSION_TYPE_CODE").equals("ALL")){
						ADTYPE=(" TRN_DAILY.ADMISSION_TYPE_CODE LIKE '%' ");
					}else{
						ADTYPE=(" TRN_DAILY.ADMISSION_TYPE_CODE = '"+rsMethod2.getString("ADMISSION_TYPE_CODE")+"'");
					}
					if(!rsMethod2.getString("PATIENT_DEPARTMENT_CODE").equals("")){
						ADTYPE = ADTYPE+" AND TRN_DAILY.PATIENT_DEPARTMENT_CODE = '"+rsMethod2.getString("PATIENT_DEPARTMENT_CODE")+"'";
					}
					
					String sqlcommand =
							"UPDATE TRN_DAILY SET "
							+"TRN_DAILY.COMPUTE_DAILY_USER_ID = 'LONGWEEKEND', "
							+"TRN_DAILY.NOR_ALLOCATE_PCT ="+rsMethod2.getDouble("NOR_ALLOCATE_PCT")+", "
							+"TRN_DAILY.NOR_ALLOCATE_AMT =0.00, "
							+"TRN_DAILY.DR_AMT = (TRN_DAILY.AMOUNT_AFT_DISCOUNT/100)*("+rsMethod2.getDouble("NOR_ALLOCATE_PCT")+"), "
							+"TRN_DAILY.DR_TAX_406 = (TRN_DAILY.AMOUNT_AFT_DISCOUNT/100)*("+rsMethod2.getDouble("NOR_ALLOCATE_PCT")+"), "
							+"TRN_DAILY.HP_AMT = TRN_DAILY.AMOUNT_AFT_DISCOUNT-((TRN_DAILY.AMOUNT_AFT_DISCOUNT/100)*("+rsMethod2.getDouble("NOR_ALLOCATE_PCT")+")) "
							
							+"FROM "
							+"TRN_DAILY INNER JOIN HIS_TRN_DAILY ON "
							+"TRN_DAILY.LINE_NO = HIS_TRN_DAILY.LINE_NO AND "
							+"TRN_DAILY.HOSPITAL_CODE = HIS_TRN_DAILY.HOSPITAL_CODE AND "
							+"TRN_DAILY.INVOICE_NO = HIS_TRN_DAILY.INVOICE_NO "
							
							+"WHERE "
							+"HIS_TRN_DAILY.TAG='HOLIDAY' AND "
							+"TRN_DAILY.HOSPITAL_CODE='"+hospitalcode+"' AND "
							+"(TRN_DAILY.VERIFY_DATE = '"+rsMethod2.getString("YYYY")+rsMethod2.getString("MM")+rsMethod2.getString("DD")+"' AND "
							+"TRN_DAILY.TRANSACTION_DATE LIKE '"+yearMonth+"%') AND " //change verify date 20180222
							+"(TRN_DAILY.BATCH_NO = '' OR TRN_DAILY.BATCH_NO IS NULL) AND "//change verify date 20180222
							+""+ADTYPE+" AND "
							+"TRN_DAILY.DR_AMT > 0  AND "
							+"HIS_TRN_DAILY.INCLUDE='Y'";
					messageIn = sqlcommand;
					System.out.println("LongWeekend Allocate : "+messageIn);
				    connsb.executeUpdate(sqlcommand);
				}
				con2.Close();	
			}
			connsb.commitTrans();
			connsb.Close();
			con.Close();
			msg="1";
		} catch (SQLException e) {
			System.out.println("Exception Process LongWeekend : "+e);
			System.out.println("With Statement : "+messageIn);
			msg="0";
		}
	}
	public boolean dataRollBack(String hospitalcode,String yyyy,String mm){
		boolean status = true;
		DBConn dbconn = new DBConn();
		try {
			dbconn.setStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		String sqlRollbackTrnDaily = 
			"UPDATE TRN_DAILY SET " +
			"TRN_DAILY.COMPUTE_DAILY_USER_ID = 'LWN', "+
			"TRN_DAILY.NOR_ALLOCATE_PCT = HIS_TRN_DAILY.NOR_ALLOCATE_PCT, "+
			"TRN_DAILY.NOR_ALLOCATE_AMT = HIS_TRN_DAILY.NOR_ALLOCATE_AMT, "+
			"TRN_DAILY.DR_AMT = HIS_TRN_DAILY.DR_AMT, "+
			"TRN_DAILY.HP_AMT = HIS_TRN_DAILY.HP_AMT "+
			"FROM " +
			"TRN_DAILY INNER JOIN HIS_TRN_DAILY ON " +			
			"TRN_DAILY.INVOICE_NO = HIS_TRN_DAILY.INVOICE_NO AND " +
			"TRN_DAILY.LINE_NO = HIS_TRN_DAILY.LINE_NO AND "+
			"TRN_DAILY.HOSPITAL_CODE = HIS_TRN_DAILY.HOSPITAL_CODE "+
			"WHERE " +
			"HIS_TRN_DAILY.INCLUDE='Y' AND " +
			"HIS_TRN_DAILY.TAG='HOLIDAY' AND " +
			"HIS_TRN_DAILY.HOSPITAL_CODE='"+hospitalcode+"' AND " +
			"HIS_TRN_DAILY.TRANSACTION_DATE LIKE '"+yyyy+mm+"%'";
		
		String sqlDeleteHisTrnDaily =
			"DELETE FROM HIS_TRN_DAILY WHERE TAG='HOLIDAY' AND HOSPITAL_CODE='"+hospitalcode+"' AND TRANSACTION_DATE LIKE '"+yyyy+mm+"%'";
		System.out.println("\nStart Rollback LongWeekend : ->");
		try{
			dbconn.insert(sqlRollbackTrnDaily);
			System.out.print("Transaction LongWeekend Rollback Completed : ->");
			dbconn.insert(sqlDeleteHisTrnDaily);
			System.out.print("History LongWeekend Delete Completed\n");
			System.out.println("LongWeekend Rollback Complete");
			dbconn.commitDB();
		}catch(Exception e){
			System.out.println("LongWeekend Rollback In Complete : "+e);
			dbconn.rollDB();
			status = false;
		}
		dbconn.closeDB("");
		return status;
	}
}