package df.bean.tax;
import df.bean.db.conn.DBConn;
import df.bean.obj.util.JDate;
public class Tax402Bean_Bak {
	//)))))))))))))))))))))))
	//**********************
    DBConn cdb;
    String[][] temp_table = null;
    String previous_month = "";
    String previous_year = "";
    String next_month = "";
    String next_year = "";
    String month = "";
    String year = "";
    String hospital = "";
    String result = "";

    private double incomeNormalTotal, incomeTurnTotal, firstTaxTurn, firstTaxNormal, taxTurn, taxNormal, taxNormalAccu;
    private String[][] sumAll;
    //Table PAYMENT_MONTHLY 
    //Field POSITION_AMT = จำนวนเงินค่าประจำตำแหน่ง
    //Field PAYMENT_TYPE = '01'
    
    public Tax402Bean_Bak(){
    	try{
    		cdb = new DBConn();
    		cdb.setStatement();
    	}catch(Exception err){
    		System.out.println(err.getMessage());
    	}
    }
    
    public boolean setTerm(String hospital_code, String month, String year){
    	//this method use from servlet
        this.month = month;
        this.year = year;
        this.hospital = hospital_code;
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
        return insertTransaction();//copy revenue from PAYMENT_MONTHLY to SUMMARY_TAX_402
    }
    
    public void setTime(String hospital, String month, String year){
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
    
    public String processTax(String hospital,String doctor, String month, String year){

    	this.setTime(hospital, month, year);
    	
        String inform = "Complete";
        String stm = "SELECT PM.CASH_402+PM.AR_402, PM.POSITION_AMT, PM.EX_402, PM.GDR_402, PM.ABCR_402, " +
                            "PM.CASH_402+PM.AR_402+PM.EX_402+PM.GDR_402+PM.ABCR_402 AS TAX402, "+
                            "PM.DOCTOR_CODE, CASE WHEN DR.DOCTOR_PROFILE_CODE=DR.CODE THEN 'N' ELSE 'Y' END AS HUMAN, " +
                            "DR.PAY_TAX_402_BY "+
                            "FROM VW_ALL_SUMMARY_MONTHLY PM LEFT OUTER JOIN DOCTOR DR " +
                            //"FROM PAYMENT_MONTHLY PM LEFT OUTER JOIN DOCTOR DR " +
                            "ON PM.DOCTOR_CODE = DR.CODE AND PM.HOSPITAL_CODE=DR.HOSPITAL_CODE "+
                            "WHERE PM.HOSPITAL_CODE='"+this.hospital+"'  AND PM.YYYY = '"+this.year+"' AND " +
                            "PM.MM = '"+this.month+"' AND " +
                            //"PM.MM = CASE WHEN PM.PAYMENT_TYPE='04' THEN '"+this.month+"' " + FOR POSITION&SALARY
                            //"ELSE '"+this.previous_month+"' END AND "+
                            "PM.DOCTOR_CODE = '"+doctor+"'";
        try {   
                //แพทย์เวรทางโรงพยาบาลเป็นคนจ่ายภาษีให้ รายได้นอกเหนือจากนั้นแพทย์จ่ายภาษีเอง
                //หากเลือก "ภาษี ร.พ.จ่าย" หน้าจอในการตั้งค่าแพทย์ จะมีค่าเป็น "1" อยู่ในฟิลด์ Doctor_Tax_Type_Code
                //ทางโรงพยาบาลจะออกภาษีให้ทั้งหมดไม่ว่ารายได้จะไปตกในส่วนที่ไม่ใช่แพทย์เวร
        		
                sumAll = cdb.query(stm);
                System.out.println(sumAll.length);
                clearValue();
                
                if( sumAll[0][8].equals("1")){
                	//****If want to use this condition pls. verify again
                    //Hospital paid tax all revenue
                    incomeTurnTotal = Double.parseDouble(sumAll[0][0])+Double.parseDouble(sumAll[0][1])+Double.parseDouble(sumAll[0][2])+Double.parseDouble(sumAll[0][3])+Double.parseDouble(sumAll[0][4]);
                    firstTaxTurn = hospitalTax(""+(incomeTurnTotal+getTotalIncome("T",doctor,year)),sumAll[0][7]);
                    taxTurn = firstTaxTurn - getTotalTax("T",doctor,year);
                    insertTaxNormal(doctor);
                }else{
                    //Doctor paid tax all revenue
                    //incomeNormalTotal = Double.parseDouble(sumAll[0][0])+Double.parseDouble(sumAll[0][1])+Double.parseDouble(sumAll[0][2])+Double.parseDouble(sumAll[0][3])+Double.parseDouble(sumAll[0][4]);
                    //firstTaxNormal = normalTax(""+incomeNormalTotal,sumAll[0][7]);
                    incomeNormalTotal = Double.parseDouble(sumAll[0][5]);			//revenue of this month
                    firstTaxNormal = normalTax(""+(incomeNormalTotal+
                					getTotalIncome("N",doctor,year)),sumAll[0][7]);	//calculate tax accumulate of year
                    taxNormalAccu = incomeNormalTotal+getTotalIncome("N",doctor,year);
                    taxNormal = firstTaxNormal - getTotalTaxAccu("N",doctor,year); 	//calculate accu tax year
                    taxNormal = taxNormal < 0 ? 0 : taxNormal;
                    /*
                    if(doctor.equals("0291168")){
                    	System.out.println("firstTaxNormal : "+firstTaxNormal+" | taxAccu : "+getTotalTaxAccu("N",doctor,year));
                    }
                    */
                    insertTaxNormal(doctor);
                }
                cdb.closeStatement("");
                cdb.closeDB("");
        } catch (Exception f) {
        	System.out.println(f.getMessage());
            inform = "False"+f;
        }
        return inform;
    }
    private boolean insertExpenseTax(String doctor){
    	String s = "INSERT INTO TRN_EXPENSE_DETAIL (" +
		"HOSPITAL_CODE,DOCTOR_CODE,DOC_NO," +
		"LINE_NO,DOC_DATE,AMOUNT,TAX_AMOUNT,EXPENSE_SIGN," +
		"EXPENSE_ACCOUNT_CODE,EXPENSE_CODE,TAX_TYPE_CODE," +
		"YYYY,MM,NOTE,USER_ID) " +
		"SELECT TX.HOSPITAL_CODE, TX.DOCTOR_CODE" +
		"'TAX 40(2)', '40201', TX.YYYY+TX.MM+'01', TX.NET_TAX_MONTH, '0', EX.SIGN, "+
		"EX.ACCOUNT_CODE,EX.CODE,EX.TAX_TYPE_CODE,TX.YYYY,TX.MM,'Reduce Revenue from Tax','ProcessTax' "+
		"FROM SUMMARY_TAX_402 TX LEFT OUTER JOIN EXPENSE EX ON TX.HOSPITAL_CODE = EX.HOSPITAL_CODE " +
		"WHERE TX.HOSPITAL_CODE = '"+this.hospital+"' AND " +
		"TX.DOCTOR_CODE = '"+doctor+"' AND EX.ADJUST_TYPE = 'TX' AND "+
    	"TX.NORMAL_TAX_MONTH > 0 AND TX.YYYY = '"+this.year+"' AND TX.MM = '"+this.month+"'";
    	//System.out.println(s);
    	try{
    		cdb.insert(s);
    		cdb.commitDB();
    	}catch (Exception e){
    		cdb.rollDB();
    	}
    	return true;
    }
    
    private void insertTaxNormal(String doctor){
        try {
            String s = "UPDATE SUMMARY_TAX_402 SET " +
            "ACCU_NORMAL_TAX_MONTH = '"+taxNormalAccu+"' "+   //ACCRU REVENUE
            ",SUM_NORMAL_TAX_AMT = '"+incomeNormalTotal+"' "+ //REVENUE OF THIS MONTH
            ",NORMAL_TAX_MONTH = '"+firstTaxNormal+"' "+ 	  //TAX FROM REVENUE IN MONTH
            ",NET_TAX_MONTH = '"+taxNormal+"' "+
            "WHERE DOCTOR_CODE = '"+doctor+"' " +
            "AND HOSPITAL_CODE = '"+this.hospital+"' "+
            "AND YYYY = '"+this.year+"' " +
            "AND MM = '"+this.month+"'";
            //System.out.println(s);
            cdb.insert(s);  
            cdb.commitDB();
            insertExpenseTax(doctor);
			 
        } catch (Exception e) {
        	cdb.rollDB();
            System.out.println("Error while insert income to summary_tax_402 : "+e);
        }
    }
    
    private boolean insertTransaction(){
        boolean status = true;
        String term_tax = "";
        if(this.month.equals("12")){
        	term_tax = this.next_year;
        }else{
        	term_tax = this.year;
        }
        //System.out.println("Insert Tax Transaction");
        //Import Income from Doctor for Case Doctor have no DF 
        String stm = "INSERT INTO SUMMARY_TAX_402(HOSPITAL_CODE, TURN_OUT_AMT, OTHER_AMT, GUARANTEE_AMT, POSITION_AMT, " +
                     "DOCTOR_CODE, YYYY, MM, TAX_TERM) "+
                     "SELECT '"+ this.hospital +"',CASH_402+AR_402, EX_402, GDR_402, 0, DOCTOR_CODE, '" +
                     this.year+"', '"+this.month+"', '"+term_tax+"' "+
                     "FROM VW_ALL_SUMMARY_MONTHLY "+
                     //"FROM PAYMENT_MONTHLY " +
                     "WHERE YYYY = '"+this.year+"' AND " +
                     "HOSPITAL_CODE = '"+this.hospital+"' AND "+
                     "CASH_402+AR_402+EX_402+GDR_402 > 0 AND "+
                     "MM = '"+this.month+"'";
                     //"MM = CASE WHEN PAYMENT_TYPE='04' THEN '"+this.month+"' ELSE '"+this.previous_month+"' END";
        try {
            cdb.setStatement();
            cdb.insert(stm);
            cdb.commitDB();
            cdb.closeStatement("");
            cdb.closeDB("");
        } catch (Exception e) {
            System.out.println("Error insert income to summary_tax_402 : "+e);
            status = false;
            cdb.rollDB();
            cdb.closeStatement("");
            cdb.closeDB("");            
        }
        return status;
    }
    
    private void clearValue(){
        incomeNormalTotal = 0;
        firstTaxNormal = 0;
        taxNormal = 0;
        incomeTurnTotal = 0;
        firstTaxTurn = 0;
        taxTurn = 0;
    }
    
    public double getIncomeNormalTotal(){
        return incomeNormalTotal;
    }
    public double getFirstTaxNormal(){
        return firstTaxNormal;
    }
    public double getTaxNormal(){
        return taxNormal;
    }
    public double getIncomeTurnTotal(){
        return incomeTurnTotal;
    }
    public double getFirstTaxTurn(){
        return firstTaxTurn;
    }
    public double getTaxTurn(){
        return taxTurn;
    }
    
    private double getTotalIncome(String inType, String doctor, String year){
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
        "WHERE HOSPITAL_CODE="+ this.hospital +" AND TAX_TERM = '"+
        term_tax+"' AND DOCTOR_CODE = '"+doctor+"'";
        try{
            temp = cdb.query(sqNormal);
        } catch (Exception e){
            System.out.println(e);
        }
        if(temp.length == 0){
            income = 0;
        }else{
            if(temp[0][0]==null){
                income = 0;
            }else{
                income = Double.parseDouble(""+temp[0][0]);
            }
        }
        return income;
    }
    private double getTotalTax(String inType, String doctor, String year){
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
            incomeType = "NORMAL_TAX_MONTH";
        }else{
            incomeType = "HOSPITAL_TAX_MONTH";
        }
        String sqNormal = "SELECT SUM("+incomeType+") FROM SUMMARY_TAX_402 " +
        		"WHERE HOSPITAL_CODE="+ this.hospital +" AND TAX_TERM = '"+
        		term_tax+"' AND DOCTOR_CODE = '"+doctor+"'";
        //System.out.println("query : "+sqNormal);
        try{
            temp = cdb.query(sqNormal);
            //c.closeConnect();
        } catch (Exception e){
            System.out.println(e);
        }
        if(temp.length == 0){
            taxAmount = 0;
        }else{
            //System.out.println("test : "+temp[0].length);
            if(temp[0][0]==null){
                taxAmount = 0;
            }else{
                taxAmount = Double.parseDouble(""+temp[0][0]);
            }
        }
        return taxAmount;
    }
    private double getTotalTaxAccu(String inType, String doctor, String year){
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
        		"WHERE HOSPITAL_CODE="+ this.hospital +" AND TAX_TERM = '"+
        		term_tax+"' AND DOCTOR_CODE = '"+doctor+"'";
        //System.out.println("query : "+sqNormal);
        try{
            temp = cdb.query(sqNormal);
            //c.closeConnect();
        } catch (Exception e){
            System.out.println(e);
        }
        if(temp.length == 0){
            taxAmount = 0;
        }else{
            //System.out.println("test : "+temp[0].length);
            if(temp[0][0]==null){
                taxAmount = 0;
            }else{
                taxAmount = Double.parseDouble(""+temp[0][0]);
            }
        }
        return taxAmount;
    }
    
    public double normalTax(String income, String humanGroup){ //การคำนวณแบบปกติ
        double totalIncome = Double.parseDouble(income); //for test
        double humanExpense = 0;
        double incomeForTax = 0;
        double expense;
        expense = totalIncome * 0.4;
        if(expense > 60000){
            expense = 60000;
        }
        if(humanGroup.equals("Y")){
            humanExpense = 60000;
        }else{
            humanExpense = 30000;
        }
        
        //totalIncome = Double.parseDouble(income);

        //ยอดภาษีที่ต้องนำไปคำนวณ = เงินได้ทั้งหมด - ค่าใช้จ่าย 40% - ลดหย่อนส่วนตัว 30000
        incomeForTax = (totalIncome - expense) - humanExpense;
        /*
        System.out.println("Expense : "+expense);
        System.out.println("Income : "+income);
        System.out.println("HumanExpense : "+humanExpense);
        System.out.println("Total Income : "+incomeForTax);
		*/
        return compareTax(incomeForTax);
    }
             
    public double hospitalTax(String income, String humanGroup){ //การคำนวณ แบบโรงพยาบาลออกให้
        double totalIncome = Double.parseDouble(income); //for test
        double humanExpense = 0;
        double incomeForTax = 0;
        double taxTemp = 1;
        double tax = 0;
        double expense;
        expense = totalIncome * 0.4;
        if(expense>60000){
            expense = 60000;
        }
        if(humanGroup.equals("Y")){
            humanExpense = 60000;
        }else{
            humanExpense = 30000;
        }
        //ยอดภาษีที่ต้องนำไปคำนวณ = เงินได้ทั้งหมด - ค่าใช้จ่าย 40% - ลดหย่อนส่วนตัว 30000
        incomeForTax = (totalIncome - expense) - humanExpense;
        for(;taxTemp != tax; ){
            taxTemp = tax;
            tax = compareTax(incomeForTax+taxTemp);
        }
        return taxTemp;
    }
             
    private double compareTax(double ct){
        double test = 0;
        if(ct < 150001){
            test = 0;
        }else if(ct < 500001){
            test = stepOne(ct);
        }else if(ct < 1000001){
            test = stepTwo(ct);
        }else if(ct < 4000001){
            test = stepThree(ct);
        }else{
            test = stepFour(ct);
        }
        return test;
    }
    
    private double stepOne(double ct){
        double money = 0;
        money = ct - 150000;
        //System.out.println("1Money : "+money+" | "+ct+" - 150000");
        money  = money * 0.1;
        //System.out.println("1Tax : "+money);
        return money;
    }
    private double stepTwo(double ct){
        double money = 0;
        money = ct - 500000;
        //System.out.println("2Money : "+money+" | "+ct+" - 500000");
        money = (money * 0.2) + stepOne(500000);
        //System.out.println("2Tax : "+money);
        return money;
    }
    private double stepThree(double ct){
        double money = 0;
        money = ct - 1000000;
        //System.out.println("3Money : "+money);
        money = (money * 0.3) + stepTwo(1000000);
        //System.out.println("3Tax : "+money);
        return money;
    }
    private double stepFour(double ct){
        double money = 0;
        money = ct - 4000000;
        money = (money * 0.37) + stepThree(4000000);
        return money;
    }
    
    public String[][] getTax402(String hospital,String month, String year){
    	
    	String sql= "SELECT DISTINCT HOSPITAL_CODE,DOCTOR_CODE,YYYY,MM FROM SUMMARY_TAX_402 " +
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
    //public static void main(String[] arg){
    //	Tax402Bean tax402 = new Tax402Bean();
    //	tax402.processTax("00001","0100196", "03", "2009");
    //}
    public boolean rollbackTax402(String hospital, String year, String month){
    	boolean status = true;
    	String s = "DELETE SUMMARY_TAX_402 WHERE YYYY = '"+year+"' AND MM = '"+month+"' AND HOSPITAL_CODE = '"+hospital+"'";
    	String e = "DELETE FROM TRN_EXPENSE_DETAIL WHERE EXPENSE_CODE = 'EXTAX402' AND USER_ID = 'ProcessTax'"+
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