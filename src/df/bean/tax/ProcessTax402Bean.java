package df.bean.tax;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

import df.bean.db.conn.DBConn;
import df.bean.expense.ExpenseSummaryBean;
import df.bean.obj.util.JDate;
import df.bean.obj.util.JNumber;
import df.bean.obj.util.Utils;
import df.bean.obj.util.Variables;

public class ProcessTax402Bean {
    DBConn cdb;
    String[][] tax_rate = null;
    String[][] temp_table = null;
    String previous_month = "";
    String previous_year = "";
    String next_month = "";
    String next_year = "";
    String month = "";
    String year = "";
    String hospital = "";
    String result = "";
    String userId ="";
    
    
    public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	boolean status = false;
    //private double incomeTurnTotal, firstTaxTurn, taxTurn;//for hospital paid tax
    private double incomeNormalTotal, firstTaxNormal, taxNormal, taxNormalAccu;//for doctor paid tax by self
    private String[][] sumAll;
    private ExpenseSummaryBean esb;
    //1. do new constructor with ProcessTax402Bean(parameter)
    //2. do initial tax data with provideProcess()
    //3. use servlet to run process tax calculate each doctor with processTax(parameter)
    
	public ProcessTax402Bean(String hospital, String month, String year){
		try{
			cdb = new DBConn();
			cdb.setStatement();
            this.tax_rate = this.getTaxRate();
		}catch(Exception e){			
		}
		setTerm(hospital,month,year);
	}
    private String[][] getTaxRate(){
    	String[][] tax_rate = null;
        String sqNormal = "SELECT * FROM STP_TAX_RATE";
		try{
			tax_rate = cdb.query(sqNormal);
		} catch (Exception e){
		    System.out.println(e);
		}
		return tax_rate;
    }
    //Import Income TO Summary_tax_402 Table
    public boolean provideProcess(){
        boolean status = true;
        String term_tax = "";
        if(this.month.equals("12")){
        	term_tax = this.next_year;
        }else{
        	term_tax = this.year;
        }
	    String stm =	"INSERT INTO SUMMARY_TAX_402(HOSPITAL_CODE, DOCTOR_CODE, TURN_OUT_AMT, OTHER_AMT, GUARANTEE_AMT, POSITION_AMT, " +
	                    "YYYY, MM, TAX_TERM, BATCH_NO,UPDATE_DATE,UPDATE_TIME,USER_ID, TAX_402_METHOD, IS_LEGAL_ENTITY) "+
	                    "SELECT HOSPITAL_CODE, DOCTOR_TAX_402_CODE AS DOCTOR_CODE, "+
	                    "SUM((SUM_TAX_402+EXDR_402)-EXCR_402) AS SUM_TAX_402, 0, 0, 0, "+
	                    "'"+this.year+"' AS YEAR, '"+this.month+"' AS MONTH, '"+term_tax+"' AS TERM, "+
	                    "'' AS BATCH_NO ,'"+JDate.getDate()+"' AS UPDATE_DATE, '"+JDate.getTime()+"' AS UPDATE_TIME, '"+this.userId+"'AS USER_ID, "+
	                    "TAX_402_METHOD, IS_LEGAL_ENTITY "+
	                    "FROM "+
	                    "( SELECT TRN_DAILY.HOSPITAL_CODE, CASE WHEN DOCTOR.DOCTOR_TAX_402_CODE <> '' THEN DOCTOR.DOCTOR_TAX_402_CODE ELSE DOCTOR.CODE END AS DOCTOR_TAX_402_CODE, "+
//	                    "TRN_DAILY.DOCTOR_CODE, "+
	                    "DOCTOR.TAX_402_METHOD, DOCTOR.IS_LEGAL_ENTITY, "+
	                    "SUM(CASE WHEN TRN_DAILY.TAX_TYPE_CODE = '402' THEN DR_TAX_402 ELSE 0 END) AS SUM_TAX_402, "+
	                    "0 AS EXDR_402, "+
	                    "0 AS EXCR_402 "+
	                    "FROM "+
	                    "DOCTOR LEFT OUTER JOIN TRN_DAILY "+
	                    "ON DOCTOR.CODE = TRN_DAILY.DOCTOR_CODE AND DOCTOR.HOSPITAL_CODE = TRN_DAILY.HOSPITAL_CODE "+
	                    "LEFT OUTER JOIN ORDER_ITEM "+
	                    "ON TRN_DAILY.ORDER_ITEM_CODE = ORDER_ITEM.CODE AND TRN_DAILY.HOSPITAL_CODE = ORDER_ITEM.HOSPITAL_CODE "+
	                    "WHERE "+ 
	                    "DOCTOR.HOSPITAL_CODE = '"+this.hospital+"' "+
	                    "AND TRN_DAILY.YYYY = '"+this.year+"' AND TRN_DAILY.MM = '"+this.month+"' "+
	                    "AND (TRN_DAILY.BATCH_NO = '' OR TRN_DAILY.BATCH_NO = '"+this.year+this.month+"') "+
	                    "AND TRN_DAILY.IS_PAID = 'Y' AND TRN_DAILY.INVOICE_TYPE <> 'ORDER' "+
	                    "AND TRN_DAILY.ORDER_ITEM_ACTIVE = '1' AND TRN_DAILY.ACTIVE = '1' "+
	                    "GROUP BY TRN_DAILY.HOSPITAL_CODE, CASE WHEN DOCTOR.DOCTOR_TAX_402_CODE <> '' THEN DOCTOR.DOCTOR_TAX_402_CODE ELSE DOCTOR.CODE END, "+
//	                    "TRN_DAILY.DOCTOR_CODE, "+
	                    "DOCTOR.TAX_402_METHOD, DOCTOR.IS_LEGAL_ENTITY "+
	                    "UNION "+
	                    "SELECT AJ.HOSPITAL_CODE, CASE WHEN DOCTOR.DOCTOR_TAX_402_CODE <> '' THEN DOCTOR.DOCTOR_TAX_402_CODE ELSE DOCTOR.CODE END AS DOCTOR_TAX_402_CODE, "+
//	                    "AJ.DOCTOR_CODE, "+
	                    "DOCTOR.TAX_402_METHOD, DOCTOR.IS_LEGAL_ENTITY, "+
	                    "0 AS SUM_TAX_402, "+
	                    "SUM(CASE WHEN AJ.EXPENSE_SIGN = '1' AND AJ.TAX_TYPE_CODE = '402' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXDR_402, "+
	                    "SUM(CASE WHEN AJ.EXPENSE_SIGN = '-1' AND AJ.TAX_TYPE_CODE = '402' THEN AJ.TAX_AMOUNT ELSE 0 END) AS EXCR_402 "+
	                    "FROM DOCTOR "+
	                    "LEFT OUTER JOIN TRN_EXPENSE_DETAIL AS AJ "+
	                    "ON DOCTOR.CODE = AJ.DOCTOR_CODE AND DOCTOR.HOSPITAL_CODE = AJ.HOSPITAL_CODE "+
	                    "LEFT OUTER JOIN EXPENSE AS EX "+
	                    "ON AJ.EXPENSE_CODE = EX.CODE AND AJ.HOSPITAL_CODE = EX.HOSPITAL_CODE "+
	                    "WHERE DOCTOR.HOSPITAL_CODE = '"+this.hospital+"' "+
	                    "AND DOCTOR.PAYMENT_MODE_CODE NOT IN ('U','') AND DOCTOR.ACTIVE = '1' "+
	                    "AND AJ.YYYY = '"+this.year+"' AND AJ.MM = '"+this.month+"' AND (AJ.BATCH_NO IS NULL OR AJ.BATCH_NO = '' OR AJ.BATCH_NO = '"+this.year+this.month+"') "+
	                    "GROUP BY AJ.HOSPITAL_CODE, CASE WHEN DOCTOR.DOCTOR_TAX_402_CODE <> '' THEN DOCTOR.DOCTOR_TAX_402_CODE ELSE DOCTOR.CODE END, "+
//	                    "AJ.DOCTOR_CODE, "+
	                    "DOCTOR.TAX_402_METHOD, DOCTOR.IS_LEGAL_ENTITY "+
	                    ") Q "+
	                    //"WHERE SUM_TAX_402+EXDR_402-EXCR_402 > 0 "+
	                    "GROUP BY HOSPITAL_CODE, DOCTOR_TAX_402_CODE, TAX_402_METHOD, IS_LEGAL_ENTITY ";
	                    //"HAVING SUM((SUM_TAX_402+EXDR_402)-EXCR_402) > 0 ";	                    
        try {
        	if(Variables.IS_TEST){
            	System.out.println(stm);        		
        	}
            cdb.insert(stm);
            cdb.commitDB();
        } catch (Exception e) {
            System.out.println("Error insert income to summary_tax_402 : "+e);
            status = false;
            cdb.rollDB();
        }
        return status;
		//return status = setRevenueToPayment();
	}
    public String processTax(String hospital,String doctor, String month, String year){
    	this.setTerm(hospital, month, year);
        String inform = "Complete";
        String stm = "SELECT 0, 0, 0, 0, 0, " +//0-4
                            "TURN_OUT_AMT AS TAX402, "+//5
                            "PM.DOCTOR_CODE, CASE WHEN DR.DOCTOR_PROFILE_CODE=DR.CODE THEN 'N' ELSE 'Y' END AS HUMAN, " +//6-7
                            "DR.PAY_TAX_402_BY "+//8
                            "FROM SUMMARY_TAX_402 PM LEFT OUTER JOIN DOCTOR DR " +
                            "ON PM.DOCTOR_CODE = DR.CODE AND PM.HOSPITAL_CODE=DR.HOSPITAL_CODE "+
                            "WHERE PM.HOSPITAL_CODE='"+this.hospital+"' AND PM.YYYY = '"+this.year+"' AND " +
                            "PM.MM = '"+this.month+"' AND " +
                            "PM.DOCTOR_CODE = '"+doctor+"'";
        try {   
                //แพทย์เวรทางโรงพยาบาลเป็นคนจ่ายภาษีให้ รายได้นอกเหนือจากนั้นแพทย์จ่ายภาษีเอง
                //หากเลือก "ภาษี ร.พ.จ่าย" หน้าจอในการตั้งค่าแพทย์ จะมีค่าเป็น "1" อยู่ในฟิลด์ PAY_TAX_402_BY table DOCTOR
                //ทางโรงพยาบาลจะออกภาษีให้ทั้งหมดไม่ว่ารายได้จะไปตกในส่วนที่ไม่ใช่แพทย์เวร
        		
                sumAll = cdb.query(stm);
                if(Variables.IS_TEST){
                	//System.out.println(stm); 
                    //System.out.println("Query length : "+sumAll.length);                	
                }
                initialValue();
                
                if( sumAll[0][8].equals("1")){
                    //Hospital paid tax all revenue
                	/*
                    incomeTurnTotal = Double.parseDouble(sumAll[0][0])+Double.parseDouble(sumAll[0][1])+Double.parseDouble(sumAll[0][2])+Double.parseDouble(sumAll[0][3])+Double.parseDouble(sumAll[0][4]);
                    firstTaxTurn = hospitalTax(""+(incomeTurnTotal+getTotalIncome("T",doctor,year)),sumAll[0][7]);
                    taxTurn = firstTaxTurn - getTotalTax("T",doctor,year);
                    insertTaxNormal(doctor);
                    */
                }else{
                    //Doctor paid tax all revenue by self
                	//All revenue of this month
                    incomeNormalTotal = Double.parseDouble(sumAll[0][5]);
                    //calculate tax in month from accumulate tax of year
                    firstTaxNormal = calculateMonthTax(""+( new BigDecimal(incomeNormalTotal+getTotalYearIncome("N",doctor,year)) ),sumAll[0][7],sumAll[0][6]);
                    taxNormalAccu = incomeNormalTotal+getTotalYearIncome("N",doctor,year);
                    //calculate accu tax year
                    taxNormal = firstTaxNormal - getTotalYearTax("N",doctor,year);
                    taxNormal = taxNormal < 0 ? 0 : taxNormal;
                    if(saveMonthTax(doctor)){
                        insertExpenseTax(doctor);
                    }
                }
        } catch (Exception f) {
        	System.out.println("Exception from ProcessTax402 : "+f);
        	System.out.println(stm);
            inform = "False"+f;
        }
        return inform;
    }
    public String[][] getDoctor(String hospital,String month, String year){
    	
    	String sql= "SELECT DISTINCT SUMMARY_TAX_402.HOSPITAL_CODE,SUMMARY_TAX_402.DOCTOR_CODE,SUMMARY_TAX_402.YYYY,SUMMARY_TAX_402.MM "
    			+ ",DR.TAX_402_METHOD "
    			+ " FROM SUMMARY_TAX_402  "
    			+ " INNER JOIN DOCTOR DR ON  DR.CODE = SUMMARY_TAX_402.DOCTOR_CODE "
    			+ " WHERE SUMMARY_TAX_402.HOSPITAL_CODE='"+hospital+"' "
    			+ " AND SUMMARY_TAX_402.YYYY='"+year+"' AND SUMMARY_TAX_402.MM='"+month+"'";
    	
    	String[][] data = null;
    	try{
    		data = cdb.query(sql);
    		cdb.closeStatement("");
    		cdb.closeDB("");
    	}catch(Exception err){
    		System.out.println(err.getMessage());
    	}
    	return data;
    }
    private boolean saveMonthTax(String doctor){
    	boolean status = false;
        try {
            String s = "UPDATE SUMMARY_TAX_402 SET " +
            "ACCU_NORMAL_TAX_MONTH = '"+new BigDecimal(JNumber.setFormat(taxNormalAccu, "0.00")).setScale(2, RoundingMode.HALF_UP)+"' "+
            //"ACCU_NORMAL_TAX_MONTH = '"+new BigDecimal(JNumber.setFormat(taxNormalAccu, "0.00"))+"' "+   //ACCRU REVENUE
            ",SUM_NORMAL_TAX_AMT = '"+JNumber.setFormat(incomeNormalTotal, "0.00")+"' "+ //REVENUE OF THIS MONTH
            ",NORMAL_TAX_MONTH = '"+JNumber.setFormat(firstTaxNormal, "0.00") +"' "+ 	  //TAX FROM REVENUE IN MONTH
            ",TEXT_NET_TAX_MONTH = '"+Utils.toThaiMoney(JNumber.setFormat(taxNormal,"0.00"))+"' "+
            ",NET_TAX_MONTH = '"+JNumber.setFormat(taxNormal, "0.00") +"' "+
            "WHERE DOCTOR_CODE = '"+doctor+"' " +
            "AND HOSPITAL_CODE = '"+this.hospital+"' "+
            "AND YYYY = '"+this.year+"' " +
            "AND MM = '"+this.month+"'";
            cdb.insert(s);  
            cdb.commitDB();
            status = true;			 
        } catch (Exception e) {
        	cdb.rollDB();
            System.out.println("Error while insert income to summary_tax_402 : "+e);
        }
        return status;
    }
    private boolean insertExpenseTax(String doctor){
    	String s = "INSERT INTO TRN_EXPENSE_DETAIL (" +
		"HOSPITAL_CODE,DOCTOR_CODE,DOC_NO," +
		"LINE_NO, DOC_DATE,AMOUNT,TAX_AMOUNT,EXPENSE_SIGN," +
		"EXPENSE_ACCOUNT_CODE,EXPENSE_CODE,TAX_TYPE_CODE," +
		"YYYY,MM,NOTE,USER_ID) " +
		"SELECT SM.HOSPITAL_CODE, SM.DOCTOR_CODE, " +
		"'TAX 40(2)','40201',SM.YYYY+SM.MM+'01', SM.NET_TAX_MONTH,'0','-1', "+
		"EX.ACCOUNT_CODE, EX.CODE, EX.TAX_TYPE_CODE, SM.YYYY, SM.MM, 'Expense Tax of Month', 'ProcessTax' "+
		"FROM SUMMARY_TAX_402 SM LEFT OUTER JOIN EXPENSE EX ON SM.HOSPITAL_CODE = EX.HOSPITAL_CODE " +
		"WHERE SM.HOSPITAL_CODE = '"+this.hospital+"' AND " +
		"EX.ADJUST_TYPE = 'TX' AND "+
		"SM.DOCTOR_CODE = '"+doctor+"' AND "+
    	"SM.NORMAL_TAX_MONTH > 0 AND SM.YYYY = '"+this.year+"' AND SM.MM = '"+this.month+"'";
    	//System.out.println(s);
    	try{
    		cdb.insert(s);
    		cdb.commitDB();
    	}catch (Exception e){
    		cdb.rollDB();
    	}
    	return true;
    }
    private void setTerm(String hospital, String month, String year){
        this.month = month;
        this.year = year;
        this.hospital = hospital;
        if(this.month.equals("12")){
            this.next_month = "01";
            this.next_year = ""+(Integer.parseInt(this.year)+1); 
        }else{
            this.next_month = ""+(Integer.parseInt(this.month)+1);
                if(this.next_month.length()==1){
                    this.next_month = "0"+this.next_month;
                }
            this.next_year = this.year;
        }
        if(this.month.equals("01")){
            this.previous_month = "12";
            this.previous_year = ""+(Integer.parseInt(this.year)-1); 
        }else{
            this.previous_month = ""+(Integer.parseInt(this.month)-1);
                if(this.previous_month.length()==1){
                    this.previous_month = "0"+this.previous_month;
                }
            this.previous_year = this.year;
        }
    }
    private void initialValue(){
        incomeNormalTotal = 0;
        firstTaxNormal = 0;
        taxNormal = 0;
    }
    private double getTotalYearIncome(String inType, String doctor, String year){
        double income = 0;
        String incomeType = "";
        String[][] temp = null;
        String term_tax = "";
        if(this.month.equals("12")){
        	term_tax = this.next_year;
        }else{
        	term_tax = this.year;
        }
        if(inType.equals("N")){
            incomeType = "SUM_NORMAL_TAX_AMT";
        }else{
            incomeType = "SUM_TURN_TAX_AMT";
        }
        String sqNormal = "SELECT SUM("+incomeType+") FROM SUMMARY_TAX_402 " +
        "WHERE HOSPITAL_CODE= '"+ this.hospital +"' AND TAX_TERM = '"+term_tax+"' " +
        "AND DOCTOR_CODE = '"+doctor+"' AND ACTIVE = '1' AND MM <> '13' ";
        try{
            temp = cdb.query(sqNormal);
            if(temp.length != 0){
                income = Double.parseDouble(""+temp[0][0]);                
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return income;
    }
    private double getTotalYearTax(String inType, String doctor, String year){
        double taxAmount = 0;
        String incomeType = "";
        String[][] temp = null;
        String term_tax = "";

        if(this.month.equals("12")){
        	term_tax = this.next_year;
        }else{
        	term_tax = this.year;
        }
        if(inType.equals("N")){
            incomeType = "NET_TAX_MONTH";
        }else{
            incomeType = "NET_TAX_MONTH";
        }
        String sqNormal = "SELECT SUM("+incomeType+") FROM SUMMARY_TAX_402 " +
        		"WHERE HOSPITAL_CODE= '"+ this.hospital +"' AND TAX_TERM = '"+term_tax+"' " +
        		"AND NET_TAX_MONTH > 0 AND DOCTOR_CODE = '"+doctor+"' AND ACTIVE = '1'";
        try{
            temp = cdb.query(sqNormal);
        } catch (Exception e){
            System.out.println(e);
        }
        if(temp.length == 0){
            taxAmount = 0;
        }else{
            if(temp[0][0]==null){
                taxAmount = 0;
            }else{
                taxAmount = Double.parseDouble(""+temp[0][0]);
            }
        }
        return taxAmount;
    }
    private double calculateMonthTax(String income, String humanGroup, String doctor_code){ //การคำนวณแบบปกติ
    	String reduce01_sql = "SELECT TOP 1 A08+A081 FROM STP_TAX_REDUCE WHERE "
    		+ "HOSPITAL_CODE = '"+this.hospital+"' AND STATUS = '1' AND DOCTOR_CODE = '"+doctor_code+"' ORDER BY YYYY+MM DESC";
    	String reduce02_sql = "SELECT TOP 1 A10+A101 FROM STP_TAX_REDUCE WHERE "
    		+ "HOSPITAL_CODE = '"+this.hospital+"' AND STATUS = '1' AND DOCTOR_CODE = '"+doctor_code+"' ORDER BY YYYY+MM DESC";
    	String reduce03_sql = "SELECT TOP 1 B07+B071 FROM STP_TAX_REDUCE WHERE "
    		+ "HOSPITAL_CODE = '"+this.hospital+"' AND STATUS = '1' AND DOCTOR_CODE = '"+doctor_code+"' ORDER BY YYYY+MM DESC";
    	String reduce041_sql = "SELECT TOP 1 C01+C02+C03_02+C03_04"
    		+ "+C04_02+C04_04+C04_06+C04_08+C05+C051+C06_05+C06_051 FROM STP_TAX_REDUCE WHERE "
    		+ "HOSPITAL_CODE = '"+this.hospital+"' AND STATUS = '1' AND DOCTOR_CODE = '"+doctor_code+"' ORDER BY YYYY+MM DESC";
    	String reduce042_sql = "SELECT SUM(AMOUNT_1+AMOUNT_2) FROM STP_TAX_REDUCE_DETAIL "
    		+ "WHERE DOCTOR_CODE = '"+doctor_code+"' AND YYYY+MM = (SELECT MAX(YYYY+MM) "
    		+ "FROM STP_TAX_REDUCE_DETAIL WHERE DOCTOR_CODE = '"+doctor_code+"')";

        double total_income = Double.parseDouble(income); //for test
        double humanExpense = 0;
        double temp_income = 0;
        double expense = 0;
        double reduce01 = 0;
        double reduce02 = 0;
        double reduce03 = 0;
        double reduce041 = 0;
        double reduce042 = 0;
        double donate01 = 0;
        double donate02 = 0;

        try{
            reduce01 = Double.parseDouble(cdb.getSingleData(reduce01_sql));
        }catch(Exception e){}
        try{
            reduce02 = Double.parseDouble(cdb.getSingleData(reduce02_sql));
        }catch(Exception e){}
        try{
            reduce03 = Double.parseDouble(cdb.getSingleData(reduce03_sql));
        }catch(Exception e){}
        try{
            reduce041 = Double.parseDouble(cdb.getSingleData(reduce041_sql));
        }catch(Exception e){}
        try{
            reduce042 = Double.parseDouble(cdb.getSingleData(reduce042_sql));
        }catch(Exception e){}        
		
        if(reduce041 == 0){//if reduce not setup, check from human expense = 0
            expense = total_income * 0.5 > 100000 ? 100000 : total_income * 0.5;
            if(humanGroup.equals("Y")){
                humanExpense = 60000;
                temp_income = (total_income - expense) - humanExpense;
            }else{
                humanExpense = 60000;
                temp_income = (total_income - expense) - humanExpense;
            }
        }else{
            temp_income = total_income - reduce03;//step 1
            //System.out.println(temp_income+" = "+total_income+" - "+reduce03+" step 1");
            expense = total_income * 0.5 > 100000 ? 100000 : total_income * 0.5;//step 2
            //System.out.println("expense = "+expense+" step 2");
            temp_income = temp_income - expense;//step 3
            //System.out.println("temp_income = "+temp_income+" step 3");
            temp_income = temp_income - (reduce041+reduce042);//step 4
            //System.out.println("temp_income = "+temp_income+" reduce04 = "+reduce041+":"+reduce042+" step 4");
            donate01 = temp_income * 0.1 > reduce01 ? reduce01 : temp_income * 0.1;//step 5
            //System.out.println("donate01 = "+donate01+" step 5");
            temp_income = temp_income - donate01;//step 6
            //System.out.println("temp_income = "+temp_income+" step 6");
            donate02 = temp_income * 0.1 > reduce02 ? reduce02 : temp_income * 0.1;//step 7
            //System.out.println("donate01 = "+donate01+" step 7");
            temp_income = temp_income - donate02;//step 8
            //System.out.println("temp_income = "+temp_income+" step 8");
        }
        //ยอดภาษีที่ต้องนำไปคำนวณ = เงินได้ทั้งหมด - ค่าใช้จ่าย 40% - ลดหย่อนส่วนตัว 30000
        return compareTax(temp_income);		
    }
    
    private double compareTax(double revenue){
    	String[][] table_tax_rate = null;
    	/*
        String sqNormal = "SELECT * FROM STP_TAX_RATE";
		try{
			table_tax_rate = cdb.query(sqNormal);
		} catch (Exception e){
		    System.out.println(e);
		}
		*/
        double tax_amount = 0;
        if(this.tax_rate.equals(null) || this.tax_rate.length < 2){
        	this.tax_rate = this.getTaxRate();
        }
        table_tax_rate = this.tax_rate;
        if(revenue < Integer.parseInt(table_tax_rate[0][1])+1){//< 150000+1
        	//if revenue < tax rate step 1
        	tax_amount = 0;
        }else if(revenue < Integer.parseInt(table_tax_rate[1][1])+1){//< 300000+1
        	//if revenue < tax rate step 2
        	tax_amount = stepOne(revenue, Integer.parseInt(table_tax_rate[0][1]), Double.parseDouble(table_tax_rate[0][2]), table_tax_rate);
        }else if(revenue < Integer.parseInt(table_tax_rate[2][1])+1){//< 500000+1
        	//revenue < tax rate step 3
        	tax_amount = stepTwo(revenue, Integer.parseInt(table_tax_rate[1][1]), Double.parseDouble(table_tax_rate[1][2]), table_tax_rate);
        }else if(revenue < Double.parseDouble(table_tax_rate[3][1])+1){//< 750000+1
        	//revenue < tax rate step 4
        	tax_amount = stepThree(revenue, Integer.parseInt(table_tax_rate[2][1]), Double.parseDouble(table_tax_rate[2][2]), table_tax_rate);
        }else if(revenue < Double.parseDouble(table_tax_rate[4][1])+1){//< 1000000+1
        	//revenue < tax rate step 5
        	tax_amount = stepFour(revenue, Integer.parseInt(table_tax_rate[3][1]), Double.parseDouble(table_tax_rate[3][2]), table_tax_rate);
        }else if(revenue < Double.parseDouble(table_tax_rate[5][1])+1){//< 2000000+1
        	//revenue < tax rate step 6
        	tax_amount = stepFive(revenue, Integer.parseInt(table_tax_rate[4][1]), Double.parseDouble(table_tax_rate[4][2]), table_tax_rate);
        }else if(revenue < Double.parseDouble(table_tax_rate[6][1])+1){//< 4000000+1
        	//revenue < tax rate step 7
        	tax_amount = stepSix(revenue, Integer.parseInt(table_tax_rate[5][1]), Double.parseDouble(table_tax_rate[5][2]), table_tax_rate);
        }else{
        	//revenue > tax rate step 8
        	tax_amount = stepSeven(revenue, Integer.parseInt(table_tax_rate[6][1]), Double.parseDouble(table_tax_rate[6][2]), table_tax_rate);
        }
        return tax_amount;
    }
    
    private double stepOne(double revenue, int amount_rate, double rate, String[][] tm){
        double money = 0;
        money = revenue - amount_rate;
        money  = money * rate;
        return money;
    }
    private double stepTwo(double revenue, int amount_rate, double rate, String[][] tm){
        double money = 0;
        money = revenue - amount_rate;
        money = (money * rate) + stepOne(amount_rate,Integer.parseInt(tm[0][1]),Double.parseDouble(tm[0][2]),tm);
        return money;
    }
    private double stepThree(double revenue, int amount_rate, double rate, String[][] tm){
        double money = 0;
        money = revenue - amount_rate;
        money = (money * rate) + stepTwo(amount_rate,Integer.parseInt(tm[1][1]),Double.parseDouble(tm[1][2]),tm);        
        return money;
    }
    private double stepFour(double revenue, int amount_rate, double rate, String[][] tm){
        double money = 0;
        money = revenue - amount_rate;
        money = (money * rate) + stepThree(amount_rate,Integer.parseInt(tm[2][1]),Double.parseDouble(tm[2][2]),tm);
        return money;
    }
    private double stepFive(double revenue, int amount_rate, double rate, String[][] tm){
        double money = 0;
        money = revenue - amount_rate;
        money = (money * rate) + stepFour(amount_rate,Integer.parseInt(tm[3][1]),Double.parseDouble(tm[3][2]),tm);
        return money;
    }
    private double stepSix(double revenue, int amount_rate, double rate, String[][] tm){
        double money = 0;
        money = revenue - amount_rate;
        money = (money * rate) + stepFive(amount_rate,Integer.parseInt(tm[4][1]),Double.parseDouble(tm[4][2]),tm);
        return money;
    }
    private double stepSeven(double revenue, int amount_rate, double rate, String[][] tm){
        double money = 0;
        money = revenue - amount_rate;
        money = (money * rate) + stepFour(amount_rate,Integer.parseInt(tm[5][1]),Double.parseDouble(tm[5][2]),tm);
        return money;
    }

    public boolean rollbackTax402(String hospital, String year, String month){
    	boolean status = true;
    	String s = "DELETE SUMMARY_TAX_402 WHERE YYYY = '"+year+"' AND MM = '"+month+"' AND HOSPITAL_CODE = '"+hospital+"'";
    	String e = "DELETE FROM TRN_EXPENSE_DETAIL WHERE EXPENSE_CODE IN (SELECT CODE FROM EXPENSE WHERE ADJUST_TYPE = 'TX' AND HOSPITAL_CODE = '"+hospital+"') AND USER_ID = 'ProcessTax'"+
    		"AND YYYY = '"+year+"' AND MM = '"+month+"' AND HOSPITAL_CODE = '"+hospital+"'";
    	try{
    		cdb.insert(s);
    		System.out.println("Roll back Tax 402 Processing.");
    		cdb.insert(e);
    		System.out.println("Roll back Expense for Tax 402 Processing.");
    		cdb.commitDB();
    		System.out.println("Roll back Tax 402 Complete.");
    	}catch(Exception ee){
    		cdb.rollDB();
    		status = false;
    	}finally{
    		cdb.closeStatement("");
    		cdb.closeDB("");
    	}
    	return status;
    }
}
