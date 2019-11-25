package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import df.bean.db.conn.DBConn;
import df.bean.obj.util.JDate;

/**
 * Servlet implementation class ProcessGroupSharingRevenueSrvl
 */
public class ProcessGroupSharingRevenueSrvl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	HttpSession session;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}
	
	public void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/xml; charset=UTF-8");
        session = request.getSession(true);
        PrintWriter out = response.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        request.setCharacterEncoding("UTF-8");
        
        try {
        	String hospitalCode = request.getParameter("HOSPITAL_CODE");
            String start_date = request.getParameter("START_DATE");
            String end_date = request.getParameter("END_DATE");
            String user_id = session.getAttribute("USER_ID").toString();
            boolean st = true;
            
            st = Process(hospitalCode,start_date,end_date,user_id);
            
            out.print("<RESULT><SUCCESS>" + st + "</SUCCESS></RESULT>");

        } finally {
            out.close();
        }
	}
	
	public boolean Process(String hospital_code,String start_date, String end_date ,String user_id) {
		boolean status = true;
		
        String yyyy = start_date.substring(0,4);
        String mm = end_date.substring(4,6);
        
        if (deleteExpenseDetail(yyyy,mm,hospital_code) && rollbackTransaction(hospital_code,start_date,end_date) && updateTransaction(hospital_code,start_date,end_date) && updateExpenseDetail(hospital_code,start_date,end_date)) {
        	status = true;
		} else {
			status = false;
		}
        
		return status;
	}
	
	private boolean deleteExpenseDetail(String yyyy, String mm, String hospital_code){
        boolean status = true;
        DBConn cdb = null;
        String stm ="DELETE FROM TRN_EXPENSE_DETAIL " + 
        			" WHERE YYYY = '"+yyyy+"' AND MM = '"+mm+"' AND HOSPITAL_CODE = '"+hospital_code+"' AND BATCH_NO = '' AND EXPENSE_CODE IN (select CODE from EXPENSE " + 
        			"where ADJUST_TYPE = 'SR')";
//        System.out.println(stm);
        try {
        	cdb = new DBConn();
        	cdb.setStatement();
            cdb.insert(stm);
            cdb.commitDB();
        } catch (Exception ex) {
            status = false;
            cdb.closeDB("");
            System.out.println("Delete Expense Detail (SHARING_REVENUE CODE) Error : " + ex);
        }
        
        System.out.println("<Step 1> Delete Expense Detail (SHARING_REVENUE CODE) status : " + status);
        System.out.println("<------------------------------------------------>");
        return status;
    }
	
	private boolean rollbackTransaction(String hospital_code,String start_date, String end_date) {
		boolean status = true;
		
		DBConn cdb = null;
        String stm ="UPDATE TRN_DAILY\r\n" + 
        		"SET NOR_ALLOCATE_PCT = '0',\r\n" + 
        		"	DR_AMT = '0',\r\n" + 
        		"	GUARANTEE_NOTE = '',\r\n" + 
        		"	NOTE = '',\r\n" + 
        		"	IS_PAID = 'Y'\r\n" + 
        		"\r\n" + 
        		"FROM \r\n" + 
        		"(\r\n" + 
        		"	SELECT '45' AS PCT, 'PT|'+SSR.GROUP_CODE AS G_NOTE,'WEEKEND' AS NOTE, TD.INVOICE_NO,TD.LINE_NO,PM.ORDER_ITEM_CODE AS STT, TD.ORDER_ITEM_CODE ,SSR.HOSPITAL_CODE, SSR.GROUP_CODE,SSR.TYPE, TD.DOCTOR_CODE, TD.VERIFY_DATE, TD.AMOUNT_AFT_DISCOUNT, TD.AMOUNT_AFT_DISCOUNT*0.45 AS PCT_AMOUNT\r\n" + 
        		"	FROM TRN_DAILY TD LEFT JOIN STP_SHARING_REVENUE SSR  \r\n" + 
        		"	ON TD.HOSPITAL_CODE = SSR.HOSPITAL_CODE AND TD.DOCTOR_CODE = SSR.DOCTOR_CODE and SSR.TYPE = 'DF'\r\n" + 
        		"	LEFT JOIN STP_PROMOTION_ITEM_MAPPING PM \r\n" + 
        		"	ON TD.HOSPITAL_CODE = PM.HOSPITAL_CODE AND TD.ORDER_ITEM_CODE = PM.ORDER_ITEM_CODE\r\n" + 
        		"	LEFT JOIN STP_SPECIAL_HOLIDAY SH\r\n" + 
        		"	ON TD.HOSPITAL_CODE = SH.HOSPITAL_CODE AND TD.VERIFY_DATE = SH.HOLIDAY\r\n" + 
        		"	WHERE ((TD.VERIFY_DATE BETWEEN '"+start_date+"' AND '"+end_date+"' AND DATEPART(WEEKDAY,TD.VERIFY_DATE) IN ('1','7'))  OR SH.HOLIDAY IS NOT NULL) AND SSR.HOSPITAL_CODE = '"+hospital_code+"'\r\n" + 
        		"	AND PM.ORDER_ITEM_CODE IS NULL\r\n" + 
        		"\r\n" + 
        		"	union all\r\n" + 
        		"\r\n" + 
        		"	SELECT '100' AS PCT, 'PT|'+SSR.GROUP_CODE AS G_NOTE,'WEEKDAY' AS NOTE, TD.INVOICE_NO,TD.LINE_NO,PM.ORDER_ITEM_CODE AS STT,TD.ORDER_ITEM_CODE,SSR.HOSPITAL_CODE, SSR.GROUP_CODE,SSR.TYPE, TD.DOCTOR_CODE, TD.VERIFY_DATE, TD.AMOUNT_AFT_DISCOUNT, TD.AMOUNT_AFT_DISCOUNT*1 AS PCT_AMOUNT\r\n" + 
        		"	FROM TRN_DAILY TD LEFT JOIN STP_SHARING_REVENUE SSR  \r\n" + 
        		"	ON TD.HOSPITAL_CODE = SSR.HOSPITAL_CODE AND TD.DOCTOR_CODE = SSR.DOCTOR_CODE and SSR.TYPE = 'DF'\r\n" + 
        		"	LEFT JOIN STP_PROMOTION_ITEM_MAPPING PM \r\n" + 
        		"	ON TD.HOSPITAL_CODE = PM.HOSPITAL_CODE AND TD.ORDER_ITEM_CODE = PM.ORDER_ITEM_CODE\r\n" + 
        		"	LEFT JOIN DOCTOR D\r\n" + 
        		"	ON TD.HOSPITAL_CODE = D.HOSPITAL_CODE AND TD.DOCTOR_CODE = D.CODE\r\n" + 
        		"	LEFT JOIN STP_SPECIAL_HOLIDAY SH\r\n" + 
        		"	ON TD.HOSPITAL_CODE = SH.HOSPITAL_CODE AND TD.VERIFY_DATE = SH.HOLIDAY\r\n" + 
        		"	WHERE TD.VERIFY_DATE BETWEEN '"+start_date+"' AND '"+end_date+"' AND DATEPART(WEEKDAY,TD.VERIFY_DATE) BETWEEN '2' AND '6' AND SH.HOLIDAY IS NULL AND SSR.HOSPITAL_CODE = '"+hospital_code+"'\r\n" + 
        		"	AND PM.ORDER_ITEM_CODE IS NULL\r\n" + 
        		"\r\n" + 
        		"	union all\r\n" + 
        		"\r\n" + 
        		"	SELECT '35' AS PCT, 'PT|'+SSR.GROUP_CODE AS G_NOTE,'PROMO' AS NOTE, TD.INVOICE_NO,TD.LINE_NO,PM.ORDER_ITEM_CODE AS STT,TD.ORDER_ITEM_CODE,SSR.HOSPITAL_CODE, SSR.GROUP_CODE,SSR.TYPE, TD.DOCTOR_CODE, TD.VERIFY_DATE, TD.AMOUNT_AFT_DISCOUNT, TD.AMOUNT_AFT_DISCOUNT*0.35 AS PCT_AMOUNT\r\n" + 
        		"	FROM TRN_DAILY TD LEFT JOIN STP_SHARING_REVENUE SSR  \r\n" + 
        		"	ON TD.HOSPITAL_CODE = SSR.HOSPITAL_CODE AND TD.DOCTOR_CODE = SSR.DOCTOR_CODE and SSR.TYPE = 'DF'\r\n" + 
        		"	INNER JOIN STP_PROMOTION_ITEM_MAPPING PM \r\n" + 
        		"	ON TD.HOSPITAL_CODE = PM.HOSPITAL_CODE AND TD.ORDER_ITEM_CODE = PM.ORDER_ITEM_CODE\r\n" + 
        		"	WHERE TD.VERIFY_DATE BETWEEN '"+start_date+"' AND '"+end_date+"' AND SSR.HOSPITAL_CODE = '"+hospital_code+"'\r\n" + 
        		") TB\r\n" + 
        		"\r\n" + 
        		"WHERE TRN_DAILY.HOSPITAL_CODE = '"+hospital_code+"' AND TRN_DAILY.DOCTOR_CODE = TB.DOCTOR_CODE AND TRN_DAILY.VERIFY_DATE BETWEEN '"+start_date+"' AND '"+end_date+"'\r\n" + 
        		"AND TRN_DAILY.INVOICE_NO = TB.INVOICE_NO AND TRN_DAILY.LINE_NO = TB.LINE_NO";
//        System.out.println(stm);
		
        try {
        	cdb = new DBConn();
        	cdb.setStatement();
            cdb.insert(stm);
            cdb.commitDB();
        } catch (Exception ex) {
            status = false;
            cdb.closeDB("");
            System.out.println("Rollack Transaction Error : " + ex);
        }
        
		System.out.println("<Step 2> Rollback Transaction status : " + status);
        System.out.println("<------------------------------------------------>");

		return status;
	}
	
	private boolean updateTransaction(String hospital_code,String start_date, String end_date) {
		boolean status = true;
		
		DBConn cdb = null;
		String stm ="UPDATE TRN_DAILY\r\n" + 
        		"SET NOR_ALLOCATE_PCT = TB.PCT,\r\n" + 
        		"	DR_AMT = TB.PCT_AMOUNT,\r\n" + 
        		"	GUARANTEE_NOTE = TB.G_NOTE,\r\n" + 
        		"	NOTE = TB.NOTE,\r\n" + 
        		"	IS_PAID = 'N'\r\n" + 
        		"\r\n" + 
        		"FROM \r\n" + 
        		"(\r\n" + 
        		"	SELECT '45' AS PCT, 'PT|'+SSR.GROUP_CODE AS G_NOTE,'WEEKEND' AS NOTE, TD.INVOICE_NO,TD.LINE_NO,PM.ORDER_ITEM_CODE AS STT, TD.ORDER_ITEM_CODE ,SSR.HOSPITAL_CODE, SSR.GROUP_CODE,SSR.TYPE, TD.DOCTOR_CODE, TD.VERIFY_DATE, TD.AMOUNT_AFT_DISCOUNT, TD.AMOUNT_AFT_DISCOUNT*0.45 AS PCT_AMOUNT\r\n" + 
        		"	FROM TRN_DAILY TD LEFT JOIN STP_SHARING_REVENUE SSR  \r\n" + 
        		"	ON TD.HOSPITAL_CODE = SSR.HOSPITAL_CODE AND TD.DOCTOR_CODE = SSR.DOCTOR_CODE and SSR.TYPE = 'DF'\r\n" + 
        		"	LEFT JOIN STP_PROMOTION_ITEM_MAPPING PM \r\n" + 
        		"	ON TD.HOSPITAL_CODE = PM.HOSPITAL_CODE AND TD.ORDER_ITEM_CODE = PM.ORDER_ITEM_CODE\r\n" + 
        		"	LEFT JOIN STP_SPECIAL_HOLIDAY SH\r\n" + 
        		"	ON TD.HOSPITAL_CODE = SH.HOSPITAL_CODE AND TD.VERIFY_DATE = SH.HOLIDAY\r\n" + 
        		"	WHERE ((TD.VERIFY_DATE BETWEEN '"+start_date+"' AND '"+end_date+"' AND DATEPART(WEEKDAY,TD.VERIFY_DATE) IN ('1','7'))  OR SH.HOLIDAY IS NOT NULL) AND SSR.HOSPITAL_CODE = '"+hospital_code+"'\r\n" + 
        		"	AND PM.ORDER_ITEM_CODE IS NULL\r\n" + 
        		"\r\n" + 
        		"	union all\r\n" + 
        		"\r\n" + 
        		"	SELECT '100' AS PCT, 'PT|'+SSR.GROUP_CODE AS G_NOTE,'WEEKDAY' AS NOTE, TD.INVOICE_NO,TD.LINE_NO,PM.ORDER_ITEM_CODE AS STT,TD.ORDER_ITEM_CODE,SSR.HOSPITAL_CODE, SSR.GROUP_CODE,SSR.TYPE, TD.DOCTOR_CODE, TD.VERIFY_DATE, TD.AMOUNT_AFT_DISCOUNT, TD.AMOUNT_AFT_DISCOUNT*1 AS PCT_AMOUNT\r\n" + 
        		"	FROM TRN_DAILY TD LEFT JOIN STP_SHARING_REVENUE SSR  \r\n" + 
        		"	ON TD.HOSPITAL_CODE = SSR.HOSPITAL_CODE AND TD.DOCTOR_CODE = SSR.DOCTOR_CODE and SSR.TYPE = 'DF'\r\n" + 
        		"	LEFT JOIN STP_PROMOTION_ITEM_MAPPING PM \r\n" + 
        		"	ON TD.HOSPITAL_CODE = PM.HOSPITAL_CODE AND TD.ORDER_ITEM_CODE = PM.ORDER_ITEM_CODE\r\n" + 
        		"	LEFT JOIN DOCTOR D\r\n" + 
        		"	ON TD.HOSPITAL_CODE = D.HOSPITAL_CODE AND TD.DOCTOR_CODE = D.CODE\r\n" + 
        		"	LEFT JOIN STP_SPECIAL_HOLIDAY SH\r\n" + 
        		"	ON TD.HOSPITAL_CODE = SH.HOSPITAL_CODE AND TD.VERIFY_DATE = SH.HOLIDAY\r\n" + 
        		"	WHERE TD.VERIFY_DATE BETWEEN '"+start_date+"' AND '"+end_date+"' AND DATEPART(WEEKDAY,TD.VERIFY_DATE) BETWEEN '2' AND '6' AND SH.HOLIDAY IS NULL AND SSR.HOSPITAL_CODE = '"+hospital_code+"'\r\n" + 
        		"	AND PM.ORDER_ITEM_CODE IS NULL\r\n" + 
        		"\r\n" + 
        		"	union all\r\n" + 
        		"\r\n" + 
        		"	SELECT '35' AS PCT, 'PT|'+SSR.GROUP_CODE AS G_NOTE,'PROMO' AS NOTE, TD.INVOICE_NO,TD.LINE_NO,PM.ORDER_ITEM_CODE AS STT,TD.ORDER_ITEM_CODE,SSR.HOSPITAL_CODE, SSR.GROUP_CODE,SSR.TYPE, TD.DOCTOR_CODE, TD.VERIFY_DATE, TD.AMOUNT_AFT_DISCOUNT, TD.AMOUNT_AFT_DISCOUNT*0.35 AS PCT_AMOUNT\r\n" + 
        		"	FROM TRN_DAILY TD LEFT JOIN STP_SHARING_REVENUE SSR  \r\n" + 
        		"	ON TD.HOSPITAL_CODE = SSR.HOSPITAL_CODE AND TD.DOCTOR_CODE = SSR.DOCTOR_CODE and SSR.TYPE = 'DF'\r\n" + 
        		"	INNER JOIN STP_PROMOTION_ITEM_MAPPING PM \r\n" + 
        		"	ON TD.HOSPITAL_CODE = PM.HOSPITAL_CODE AND TD.ORDER_ITEM_CODE = PM.ORDER_ITEM_CODE\r\n" + 
        		"	WHERE TD.VERIFY_DATE BETWEEN '"+start_date+"' AND '"+end_date+"' AND SSR.HOSPITAL_CODE = '"+hospital_code+"'\r\n" + 
        		") TB\r\n" + 
        		"\r\n" + 
        		"WHERE TRN_DAILY.HOSPITAL_CODE = '"+hospital_code+"' AND TRN_DAILY.DOCTOR_CODE = TB.DOCTOR_CODE AND TRN_DAILY.VERIFY_DATE BETWEEN '"+start_date+"' AND '"+end_date+"'\r\n" + 
        		"AND TRN_DAILY.INVOICE_NO = TB.INVOICE_NO AND TRN_DAILY.LINE_NO = TB.LINE_NO";
//        System.out.println(stm);
		
        try {
        	cdb = new DBConn();
        	cdb.setStatement();
            cdb.insert(stm);
            cdb.commitDB();
        } catch (Exception ex) {
            status = false;
            cdb.closeDB("");
            System.out.println("Update Transaction Error : " + ex);
        }
		
		System.out.println("<Step 3> Update Transaction status : " + status);
        System.out.println("<------------------------------------------------>");

		return status;
	}
	
	private boolean updateExpenseDetail(String hospital_code,String start_date, String end_date) {
		boolean status = true;
		
		// TODO
		
		DBConn cdb = new DBConn();
		DBConn cdb_up = new DBConn();
		ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>();
    	HashMap<String,String> hm = new HashMap<String,String>();
        
        try { 
        	cdb.setStatement(); 
        	cdb_up.setStatement(); 
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        
        String stm = "SELECT TD.DOCTOR_CODE,\r\n" + 
        		"SUM(CASE WHEN NOR_ALLOCATE_PCT <> 100 THEN DR_AMT ELSE 0 END) + ((SUM(CASE WHEN NOR_ALLOCATE_PCT = 100 THEN DR_AMT ELSE 0 END) - SUM(SALARY)) * 0.2) AS SUMMARY,\r\n" + 
        		"SSR.PERCENT_DF, SUBSTRING(GUARANTEE_NOTE, 4, 5) AS GROUP_CODE , E.CODE, E.SIGN, E.ACCOUNT_CODE,E.TAX_TYPE_CODE\r\n" + 
        		",'' AS PAID\r\n" + 
        		"FROM TRN_DAILY TD LEFT OUTER JOIN DOCTOR D ON TD.HOSPITAL_CODE = D.HOSPITAL_CODE AND TD.DOCTOR_CODE = D.CODE\r\n" + 
        		"LEFT OUTER JOIN STP_SHARING_REVENUE SSR ON TD.HOSPITAL_CODE = SSR.HOSPITAL_CODE AND TD.DOCTOR_CODE = SSR.DOCTOR_CODE\r\n" + 
        		"LEFT OUTER JOIN EXPENSE E ON TD.HOSPITAL_CODE = E.HOSPITAL_CODE AND E.ADJUST_TYPE = 'SR'\r\n" + 
        		"WHERE SUBSTRING(GUARANTEE_NOTE, 1, 2)  = 'PT' AND TD.HOSPITAL_CODE = '"+hospital_code+"' AND TD.VERIFY_DATE BETWEEN '"+start_date+"' AND '"+end_date+"'\r\n" + 
        		"GROUP BY TD.DOCTOR_CODE, TD.DOCTOR_CATEGORY_CODE, SSR.PERCENT_DF, SSR.GROUP_CODE,TD.GUARANTEE_NOTE,E.CODE,E.SIGN, E.ACCOUNT_CODE,E.TAX_TYPE_CODE\r\n" + 
        		"ORDER BY SSR.GROUP_CODE\r\n" + 
        		"";
        
        al = cdb.getMultiData(stm);
        cdb.closeDB(stm);
        
//        System.out.println("updateExpenseDetail stm : " + stm);
        System.out.println("------------------------------------------------");
        System.out.println("al.size() : " + al.size());

        ArrayList<String> arr = new ArrayList<String>();
        for(int i = 0; i < al.size(); i++){
			hm = al.get(i);
        	System.out.print(hm.get("DOCTOR_CODE"));
        	System.out.print(" " + hm.get("SUMMARY"));
        	System.out.print(" " + hm.get("PERCENT_DF"));
			System.out.print(" " + hm.get("GROUP_CODE"));
			System.out.print(" " + hm.get("PAID"));
			System.out.print(" " + hm.get("CODE"));
			System.out.print(" " + hm.get("SIGN"));
			System.out.print(" " + hm.get("ACCOUNT_CODED"));
			System.out.println(" " + hm.get("TAX_TYPE_CODE"));
			
			if(i == 0) {
				arr.add(hm.get("GROUP_CODE"));
			} else {
				if (hm.get("GROUP_CODE").equals(al.get(i-1).get("GROUP_CODE"))) {
					
				} else {
					arr.add(hm.get("GROUP_CODE"));
				}
			}	
        }

        ArrayList<Double> arr_amount = new ArrayList<Double>();
        for (int i = 0; i < arr.size(); i++) {
        	double sum_ratio = 0;
        	double sum_amount = 0;
        	for (int j = 0; j < al.size(); j++) {
        		hm = al.get(j);
        		double ratio = Double.parseDouble(hm.get("PERCENT_DF"));
    			double amount = Double.parseDouble(hm.get("SUMMARY"));
    			if (hm.get("GROUP_CODE").equals(arr.get(i))) {
    				sum_ratio += ratio;
    				sum_amount += amount;
				}
			}
        	System.out.println("------------------------------------------------");
        	System.out.println("sum_ratio : " + sum_ratio);
        	System.out.println("sum_amount : " + sum_amount);

        	arr_amount.add(sum_amount/sum_ratio);
        }
      
        for (int i = 0; i < arr.size(); i++) {
        	for (int j = 0; j < al.size(); j++) {
        		hm = al.get(j);
        		if (hm.get("GROUP_CODE").equals(arr.get(i))) {
        			hm.put("PAID", ""+(arr_amount.get(i)*Double.parseDouble(hm.get("PERCENT_DF"))));	
        		} 
        	}
        }
//        al.add(hm);   
        
        System.out.println("------------------------------------------------");
        System.out.println("al.size() : " + al.size());
        System.out.println("------------------------------------------------");
        for(int i = 0; i < al.size(); i++){
			hm = al.get(i);
        	System.out.print(hm.get("DOCTOR_CODE"));
        	System.out.print(" " + hm.get("SUMMARY"));
        	System.out.print(" " + hm.get("PERCENT_DF"));
        	System.out.print(" " + hm.get("GROUP_CODE"));
			System.out.print(" " + hm.get("PAID"));
			System.out.print(" " + hm.get("CODE"));
			System.out.print(" " + hm.get("SIGN"));
			System.out.print(" " + hm.get("ACCOUNT_CODE"));
			System.out.println(" " + hm.get("TAX_TYPE_CODE"));
        }
        
        // UPDATE to TRN_EXPENSE_DETAIL
        
        String yyyy = start_date.substring(0,4);
        String mm = end_date.substring(4,6);
        
        String stm_2 = "";
        for(int i = 0; i < al.size(); i++){
			hm = al.get(i);
	        stm_2 = "INSERT INTO TRN_EXPENSE_DETAIL(YYYY, MM, DOCTOR_CODE, HOSPITAL_CODE, LINE_NO, EXPENSE_CODE, "
    				+ "EXPENSE_SIGN, EXPENSE_ACCOUNT_CODE, AMOUNT, TAX_AMOUNT, UPDATE_DATE, UPDATE_TIME, TAX_TYPE_CODE )\r\n" + 
    		
    				"VALUES ('"+yyyy+"', '"+mm+"', '"+hm.get("DOCTOR_CODE")+"', '"+hospital_code+"', '"+yyyy+mm+i+"', '"+hm.get("CODE")+"',"
    				+ " '"+hm.get("SIGN")+"', '"+hm.get("ACCOUNT_CODE")+"', '"+hm.get("PAID")+"', '"+hm.get("PAID")+"', '"+JDate.getDate()+"', '"+JDate.getTime()+"', '"+hm.get("TAX_TYPE_CODE")+"' )";
        
	        try {
	        	cdb_up.setStatement();
	            cdb_up.insert(stm_2);
	            cdb_up.commitDB();
	        } catch (Exception ex) {
	        	System.out.println("Update Expense Detail (SHARING_REVENUE CODE) Error : " + ex);
	        }
        
        }
        
        //System.out.println("stm_2 >> " + stm_2);
        
        cdb_up.closeDB("");
        
		System.out.println("<Step 4> Update ExpenseDetail status : " + status);
        System.out.println("<------------------------------------------------>");

		return status;
	}

	
}

