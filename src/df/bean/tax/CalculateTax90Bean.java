package df.bean.tax;
import df.bean.db.conn.DBConn;
import df.bean.obj.util.Variables;

public class CalculateTax90Bean {
    DBConn cdb;
    String[][] temp_table = null;
    String previous_month = "";
    String previous_year = "";
    String next_month = "";
    String next_year = "";
    String month = "12";
    String year = "";
    String hospital = "";
    String result = "";
    boolean status = false;
    private double firstTaxNormal, taxNormal;//for doctor paid tax by self
    private String[][] sumAll;
	public CalculateTax90Bean(String hospital, String year){
		try{
			cdb = new DBConn();
			cdb.setStatement();
			this.hospital = hospital;
			this.year = year;
			provideProcess();
		}catch(Exception e){
			
		}
	}
	private boolean provideProcess(){
        boolean status = true;
        String term_tax = this.year;
        String stm_tem = "INSERT INTO TEMP_SUMMARY_TAX_90(HOSPITAL_CODE, REVENUE_TERM, "+
        				 "DOCTOR_CODE, AMOUNT, TAX_AMOUNT) "+
        				 "SELECT '"+this.hospital +"', YYYY+'12', DOCTOR_CODE, SUM_TAX_DR_AMT, 0 "+
        				 "FROM SUMMARY_TAX_406 "+
                         "WHERE YYYY = '"+this.year+"' AND " +
                         "HOSPITAL_CODE = '"+this.hospital+"' AND "+
                         "MM = '12' "+
                         "UNION "+
						 "SELECT '"+this.hospital +"', YYYY+'12', DOCTOR_CODE, SUM_NORMAL_TAX_AMT, "+
						 "NET_TAX_MONTH FROM SUMMARY_TAX_402 "+
				         "WHERE YYYY = '"+this.year+"' AND " +
				         "HOSPITAL_CODE = '"+this.hospital+"' AND "+
				         "MM = '13'";

        //Import Income from Doctor for Case Doctor have no DF
        String stm = "INSERT INTO SUMMARY_TAX_90(HOSPITAL_CODE, DOCTOR_CODE, REVENUE_TERM, " +
        			 "REVENUE_AMOUNT, OLD_TAX_AMOUNT) "+
                     "SELECT HOSPITAL_CODE, DOCTOR_CODE, REVENUE_TERM, " +
                     "SUM(AMOUNT), SUM(TAX_AMOUNT) "+
                     "FROM TEMP_SUMMARY_TAX_90 "+
                     "WHERE REVENUE_TERM = '"+this.year+this.month+"' AND " +
                     "HOSPITAL_CODE = '"+this.hospital+"' "+
                     "GROUP BY HOSPITAL_CODE, DOCTOR_CODE, REVENUE_TERM";
        try {
        	System.out.println(stm_tem);
            cdb.insert(stm_tem);
            cdb.commitDB();
        } catch (Exception e) {
            System.out.println("Error insert temp revenue : "+e);
            status = false;
            cdb.rollDB();
        }
        try {
        	System.out.println(stm);
            cdb.insert(stm);
            cdb.commitDB();
        } catch (Exception e) {
            System.out.println("Error insert revenue : "+e);
            status = false;
            cdb.rollDB();
        }

        return status;
	}
    public boolean processTax(String hospital, String year){
        boolean inform = true;
        String stm = "SELECT PM.HOSPITAL_CODE, PM.DOCTOR_CODE, PM.REVENUE_AMOUNT, PM.OLD_TAX_AMOUNT, "+
        			 "CASE WHEN DR.DOCTOR_PROFILE_CODE=DR.CODE THEN 'N' ELSE 'Y' END AS HUMAN " +
                     "FROM SUMMARY_TAX_90 PM LEFT OUTER JOIN DOCTOR DR " +
                     "ON PM.DOCTOR_CODE = DR.CODE AND PM.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
                     "WHERE PM.HOSPITAL_CODE='"+hospital+"' AND " +
                     "PM.REVENUE_TERM = '"+year+this.month+"' ORDER BY PM.DOCTOR_CODE";
    	sumAll = cdb.query(stm);
    	try{
	        if(sumAll.length>0){
	        	for(int i = 0; i<sumAll.length; i++){
	                initialValue();
	                firstTaxNormal = calculateMonthTax(sumAll[0][2],sumAll[0][4],sumAll[0][1]);	            
	                taxNormal = firstTaxNormal - Double.parseDouble(sumAll[0][3]);
	                taxNormal = taxNormal < 0 ? 0 : taxNormal;
	                saveMonthTax(sumAll[0][1]);
	        	}
	        }
    	}catch(Exception f){
        	System.out.println("Exception from ProcessTax402 : "+f);
            inform = false;
        }
        return inform;
    }
    public String[][] getDoctor(String hospital,String month, String year){
    	
    	String sql= "SELECT DISTINCT HOSPITAL_CODE, DOCTOR_CODE, YYYY, MM FROM SUMMARY_TAX_402 " +
    			"WHERE HOSPITAL_CODE='"+ hospital +"' " +
    			"AND YYYY='"+ year +"' " +
    			"AND MM='"+ month +"'";
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
    	String text_tax = "test";
        try {
            String s = "UPDATE SUMMARY_TAX_90 SET " +
            "TAX_AMOUNT = '"+taxNormal+"', "+   
            "TAX_AMOUNT_TEXT = '"+text_tax+"' "+
            "WHERE DOCTOR_CODE = '"+doctor+"' " +
            "AND HOSPITAL_CODE = '"+this.hospital+"' "+
            "AND REVENUE_TERM = '"+this.year+this.month+"'";
            cdb.insert(s);  
            cdb.commitDB();
            status = true;			 
        } catch (Exception e) {
        	cdb.rollDB();
            System.out.println("Error while insert income to summary_tax_90 : "+e);
        }
        return status;
    }
    private void initialValue(){
        firstTaxNormal = 0;
        taxNormal = 0;
    }
    private double calculateMonthTax(String income, String humanGroup, String doctor_code){
    	//STP_TAX_REDUCE = FIX REDUCE
    	//STP_TAX_REDUCE_DETAIL = DYNAMIC REDUCT BY SETUP
    	//FIELD A08 = HUMAN
    	//FIELD A081 = SPOUSE
    	String reduce01_sql = "SELECT TOP 1 A08+A081 FROM STP_TAX_REDUCE WHERE DOCTOR_CODE = '"
    		+doctor_code+"' ORDER BY YYYY+MM DESC";
    	String reduce02_sql = "SELECT TOP 1 A10+A101 FROM STP_TAX_REDUCE WHERE DOCTOR_CODE = '"
    		+doctor_code+"' ORDER BY YYYY+MM DESC";
    	String reduce03_sql = "SELECT TOP 1 B07+B071 FROM STP_TAX_REDUCE WHERE DOCTOR_CODE = '"
    		+doctor_code+"' ORDER BY YYYY+MM DESC";
    	String reduce041_sql = "SELECT TOP 1 C01+C02+(C03_01*C03_02)+(C03_03*C03_04)" +
    			"+C04_02+C04_04+C04_06+C04_08+C05+C051+C06_05+C06_051 " +
    			"FROM STP_TAX_REDUCE WHERE DOCTOR_CODE = '"+doctor_code+"' ORDER BY YYYY+MM DESC";
    	String reduce042_sql = "SELECT SUM(AMOUNT_1+AMOUNT_2) FROM STP_TAX_REDUCE_DETAIL " +
    			"WHERE DOCTOR_CODE = '"+doctor_code+"' AND YYYY+MM = (SELECT MAX(YYYY+MM) " +
    			"FROM STP_TAX_REDUCE_DETAIL WHERE DOCTOR_CODE = '"+doctor_code+"')";

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
            if(humanGroup.equals("Y")){
                humanExpense = 60000;
                temp_income = (total_income - expense) - humanExpense;
            }else{
                humanExpense = 30000;
                temp_income = (total_income - expense) - humanExpense;
            }
        }else{
            temp_income = total_income - reduce03;//step 1
            System.out.println(temp_income+" = "+total_income+" - "+reduce03+" step 1");
            expense = total_income * 0.6;//step 2
            System.out.println("expense = "+expense+" step 2");
            temp_income = temp_income - expense;//step 3
            System.out.println("temp_income = "+temp_income+" step 3");
            temp_income = temp_income - (reduce041+reduce042);//step 4
            System.out.println("temp_income = "+temp_income+" reduce04 = "+reduce041+":"+reduce042+" step 4");
            donate01 = temp_income * 0.1 > reduce01 ? reduce01 : temp_income * 0.1;//step 5
            System.out.println("donate01 = "+donate01+" step 5");
            temp_income = temp_income - donate01;//step 6
            System.out.println("temp_income = "+temp_income+" step 6");
            donate02 = temp_income * 0.1 > reduce02 ? reduce02 : temp_income * 0.1;//step 7
            System.out.println("donate01 = "+donate01+" step 7");
            temp_income = temp_income - donate02;//step 8
            System.out.println("temp_income = "+temp_income+" step 8");
        }
        //ยอดภาษีที่ต้องนำไปคำนวณ = เงินได้ทั้งหมด - ค่าใช้จ่าย 40% - ลดหย่อนส่วนตัว 30000
        //System.out.println("Expense : "+expense);
        //System.out.println("Income : "+income);
        //System.out.println("HumanExpense : "+humanExpense);
        //System.out.println("Total Income : "+incomeForTax);
        return compareTax(temp_income);		
    }
    private double compareTax(double revenue){
    	String[][] table_tax_rate = null;
        String sqNormal = "SELECT * FROM STP_TAX_RATE";
		try{
			table_tax_rate = cdb.query(sqNormal);
		} catch (Exception e){
		    System.out.println(e);
		}

        double test = 0;
        if(revenue < Integer.parseInt(table_tax_rate[0][1])+1){//< 150000+1
        	//if revenue < 150001
            test = 0;
        }else if(revenue < Integer.parseInt(table_tax_rate[1][1])+1){//< 500000+1
        	//if revenue < 500001
        	//stepOne(revenue,150000, 0.1)
            test = stepOne(revenue, Integer.parseInt(table_tax_rate[0][1]), Double.parseDouble(table_tax_rate[0][2]), table_tax_rate);
        }else if(revenue < Integer.parseInt(table_tax_rate[2][1])+1){//< 1000000+1
        	//revenue < 1000001
            test = stepTwo(revenue, Integer.parseInt(table_tax_rate[1][1]), Double.parseDouble(table_tax_rate[1][2]), table_tax_rate);
        }else if(revenue < Double.parseDouble(table_tax_rate[3][1])+1){//< 4000000+1
        	//revenue < 4000001
        	test = stepThree(revenue, Integer.parseInt(table_tax_rate[2][1]), Double.parseDouble(table_tax_rate[2][2]), table_tax_rate);
        }else{
        	//revenue > 4000000
            test = stepFour(revenue, Integer.parseInt(table_tax_rate[3][1]), Double.parseDouble(table_tax_rate[3][2]), table_tax_rate);
        }
        return test;
    }
    
    private double stepOne(double revenue, int amount_rate, double rate, String[][] tm){
        double money = 0;
        //money = revenue - 150000;
        //money  = money * 0.1;

        money = revenue - amount_rate;
        money  = money * rate;

        return money;
    }
    private double stepTwo(double revenue, int amount_rate, double rate, String[][] tm){
        double money = 0;
        //money = revenue - 500000;
        //money = (money * 0.2) + stepOne(500000);

        money = revenue - amount_rate;
        money = (money * rate) + stepOne(amount_rate,Integer.parseInt(tm[0][1]),Double.parseDouble(tm[0][2]),tm);

        return money;
    }
    private double stepThree(double revenue, int amount_rate, double rate, String[][] tm){
        double money = 0;
        //money = revenue - 1000000;
        //money = (money * 0.3) + stepTwo(1000000);

        money = revenue - amount_rate;
        money = (money * rate) + stepTwo(amount_rate,Integer.parseInt(tm[1][1]),Double.parseDouble(tm[1][2]),tm);
        
        return money;
    }
    private double stepFour(double revenue, int amount_rate, double rate, String[][] tm){
        double money = 0;
        //money = revenue - 4000000;
        //money = (money * 0.37) + stepThree(4000000);
        
        money = revenue - amount_rate;
        money = (money * rate) + stepThree(amount_rate,Integer.parseInt(tm[2][1]),Double.parseDouble(tm[2][2]),tm);

        return money;
    }
}
