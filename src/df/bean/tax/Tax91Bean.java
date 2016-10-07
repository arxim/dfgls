package df.bean.tax;

import java.sql.SQLException;
import df.bean.db.conn.DBConn;
import df.bean.obj.util.JDate;
import df.bean.db.table.TRN_Error;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tax91Bean {
    DBConn cdb;
    
    String result_show = "";
    String year = "";
    String hospital_code = "";
    
    public Tax91Bean(DBConn cdb){
        try {
            this.cdb = cdb;
            if (this.cdb.getStatement() == null) {
                this.cdb.setStatement();
            }
        } catch (SQLException ex) {
            this.result_show = ""+ex;
            System.out.println(ex);
        }
    }
    
    public String getMessage(){
        return this.result_show;
    }
    
    public boolean prepareProcess(String year, String hospital_code, String process_type){
        boolean status = true;
        this.year = year;
        this.hospital_code = hospital_code;
        System.out.println("START RUN PROCESS Calculate Tax91");
          if(process_type.equals("Calculate Tax91"))
	        {
	        	if(DeleteDataTax91())
	        	{
	        		if(!(CalculateTax()))
	        		{
	        			status=false;
	        		}
	        	}
	        }
          else
          {
        	  System.out.println("no process");
          }
         return status;
    }

    private boolean DeleteDataTax91(){
        //Step 1:ลบข้อมูลออกจากตาราง SUMMARY_MONTHLY ตามเงื่อนไข HOSPITAL_CODE,YYYY,MM, EXPENSE_CODE IN (AD406,DD,406,AD402,DD402)
    	System.out.println("START STEP 1 : DELETE DATA IN TABLE TAX_91");
    	String sql_statement1 = "";
    	boolean status_delete=true;
        
       	sql_statement1 = "DELETE FROM TAX_91 WHERE HOSPITAL_CODE='"+hospital_code+"' AND YYYY='"+year+"' ";
       	//System.out.println("DELETE Tax91="+sql_statement1);
        
        try
        {
            cdb.insert(sql_statement1);
            System.out.println("Step 1 : Delete TAX_91 complete");
            cdb.commitDB();
                
         }
       	 catch(Exception e)
       	 {
            System.out.println("Step 1 Delete TRN_EXPENSE_DETAIL EXP_DIS Exclude : "+e+" QUERY="+sql_statement1);
            status_delete = false;
         }
       	System.out.println("FINISH STEP 1 : DELETE DATA IN TABLE TAX91");
        return status_delete;
    }
    
    
    
    private String[][] GetDoctorTax()
    {
        String sql="";
        String[][] DoctorTaxArr = null;
        
        sql = "SELECT T.DOCTOR_CODE, D.DOCTOR_PROFILE_CODE, T.SUM_NORMAL_TAX_AMT, T.NET_TAX_MONTH "
        	  +" FROM SUMMARY_TAX_402 T, DOCTOR D "
        	  +" WHERE T.DOCTOR_CODE=D.CODE "
        	  +" AND T.HOSPITAL_CODE='"+hospital_code+"' "
        	  +" AND T.YYYY='"+year+"' "
        	  +" AND T.MM='13' "
        	  +" ORDER BY T.DOCTOR_CODE ";
        	  
           	
        	//System.out.println("Query DOCTOR  : "+sql);
        	
       	try
        {   
       		DoctorTaxArr = cdb.query(sql);
        }
        catch(Exception e)
        {
            System.out.println("Excepiton Query Data Table SUMMARY_TAX_402 : "+e+" query="+sql);
        }
       	
        return DoctorTaxArr;
    } 
    private boolean CalculateTax()
    {
    	System.out.println("Calculate");
        boolean status = true;
        String sql= "",startMonth="",endMonth="";
        String[][] RevenueArr = null;
        //query revenue of month/year
     	System.out.println("ค้นหา doctor code");
       	RevenueArr=this.GetDoctorTax();
       	//RevenueArr=this.GetRevenue(startMonth,endMonth,"","","3");
        System.out.println("วนลูปรายได้ของ doctor code ที่จะคำนวณภาษี");
        System.out.println("RevenueArr.length="+RevenueArr.length);
        boolean status_doctor=true;
        
        if(RevenueArr.length !=0)
        {
	       for(int i=0; i<RevenueArr.length;i++)
	       {
	    	   String doctorCode="", doctorProfileCode="";
	           double totalAmount=0,totalTax=0, a06c01=0, result=0;
	           int a14_01=0;
	           double a02=0, a03=0, a04=0, a05=0, a07=0, a08=0, a09=0, a10=0, a11=0, a12=0, a14=0;
	           double tax_gov_def = 60000;
	    	   //รหัส doctor code
	    	   doctorCode=RevenueArr[i][0];
	    	   doctorProfileCode=RevenueArr[i][1];
	    	   //เงินได้ ข้อที่ ก.1, ก.3.คงเหลือ (1.-2.)
	    	   totalAmount=Double.parseDouble(RevenueArr[i][2]);
	    	   //ภาษีหัก ณ ที่จ่าย ก.13
	    	   totalTax=Double.parseDouble(RevenueArr[i][3]);
	    	   
	    	   System.out.println("Doctor Code="+doctorCode);
	    	   System.out.println("Doctor Profile Code="+doctorProfileCode);
	    	   System.out.println("totalAmount="+totalAmount);
	    	   System.out.println("totalTax="+totalTax);
	    	   
	    	   //ก.6 หัก ค่าลดหย่อนฯ (ยกมาจาก ค 14.)
	    	   if(doctorCode.equals(doctorProfileCode))
	    	   {   a06c01=30000;   	   }
	    	   else
	    	   {   a06c01=60000;   	   }
	    	   //ก.3.คงเหลือ (1.-2.)
	    	   a03 = totalAmount-a02;
	    	   if(a03<0) {a03=0;}
	    	   //ก.4.หัก ค่าใช้จ่าย(ร้อยละ 40 ของ 3. แต่ไม่เกินที่กฎหมายกำหนด)
	    	   a04 = (a03*40)/100 ;
	           if(a04 > tax_gov_def) { a04 = tax_gov_def; }
	           //ก.5 คงเหลือ(3.-4.)
	           a05 = a03 - a04;
	           if(a05<0) { a05=0;}
	           //ก.7.คงเหลือ(5.-6.)
	           a07 = a05 - a06c01;
	           if(a07<0){ a07=0;}
	    	   //8.หัก เงินบริจาคสนับสนุนทางการศึกษา  (2 เท่าของจำนวนเงินที่ได้จ่ายไปจริง แต่ไม่เกินร้อยละ 10 ของ 7.)  
	           /*double ma_def = 10;
	           double ma_chk = (a07 * ma_def) / 100;
	           double total_a08 = a08 * 2;
	   		   if(total_a08 > ma_chk) { a08 = ma_chk;	}
	   		   if(total_a08<0){a08=0;}*/
	    	   //ก.9.คงเหลือ(7.-8.) 
	   		   a09 = a07 - a08;
	   		   if(a09<0){ a09=0;}
	    	   //ก.10.หัก เงินบริจาค(ไม่เกินร้อยละ 10 ของ 9.) 
	    	   //ก.11.เงินได้สุทธิ(9.-10.) 
	   		   a11 = a09 - a10;
	   		   if(a11<0) { a11=0; }
	   		   //ก.12.ภาษีคำนวณจากเงินได้สุทธิตาม 11. 
		   		if(a11 < 150001){
		            a12 = 0;
		        }else if(a11 < 500001){
		        	a12 = stepOne(a11);
		        }else if(a11 < 1000001){
		        	a12 = stepTwo(a11);
		        }else if(a11 < 4000001){
		        	a12 = stepThree(a11);
		        }else{
		        	a12 = stepFour(a11);
		        }
	    	   //ก.14.คงเหลือ ภาษีที่ ชำระเพิ่มเติม  ชำระเกินไว้  
		   		if(a12 > totalTax)
		   		{
		        	//alert('a12 > a13');
		    		result = a12 - totalTax;
		    		if(result<0) { result=0;}
		    		a14 = result;
		    		a14_01=1;//ชำระเพิ่มเติม
		    	}
		   		else if(a12 < totalTax)
		   		{
		    		//alert('a12 < a13');
		    		result = totalTax - a12;
		    		if(result<0) { result=0;}
		    		a14 = result;
		    		a14_01 = 2;//ชำระเกินไว้
		    	}
	    	   
	    	   //Insert Data
		   		String sql_insert_tax="INSERT INTO TAX_91("
					+" HOSPITAL_CODE, YYYY, DOCTOR_CODE, A01, A02, A03, A04, A05, "
					+" A06, A07, A08, A09, A10, A11, A12, A13, A14_03, A14_01,C01, "
					+" CREATE_DATE, CREATE_TIME, UPDATE_DATE,UPDATE_TIME,"
					+" DOCTOR_TEL_HOME, DOCTOR_TEL_OFFICE, SPOUSE_BIRTHDAY, SPOUSE_NO,"
					+" SPOUSE_TAX_NO, SPOUSE_NAME, PHALANX_01_01, PHALANX_02_01,"
					+" A14_02, A15, A16, A17, A18_02, A19, A20_02,"
					+" B01, B02, B03, B04, B05, B06, B07,"
					+" C02,C03_01, C03_02, C03_03, C03_04, C04_01, C04_02, C04_03, C04_04, C04_05, C04_06, C04_07, C04_08,"
					+" C05, C06_01, C06_02, C06_03, C06_04, C06_05, C07, C08, C09, C10, C11, C12, C13, C14_01, C14_02)"
					+" VALUES('"+hospital_code+"', '"+year+"', '"+doctorCode+"',"
					+totalAmount+","+a02+","+a03+","+a04+","+a05+","+a06c01+","+a07+","+a08+","
					+a09+","+a10+","+a11+","+a12+","+totalTax+","+a14+",'"+a14_01+"',"+a06c01+","
					+" '"+JDate.getDate()+"','"+JDate.getTime()+"',"
		   			+" '"+JDate.getDate()+"','"+JDate.getTime()+"',"
		   			+" '','','','',"
		   			+" '','','','',"
		   			+" 0, 0, 0, 0, 0, 0, 0,"
		   			+" 0, 0, 0, 0, 0, 0, 0,"
		   			+" 0, 0, 0, 0, 0, '', 0, '', 0, '', 0, '', 0,"
		   			+" 0, '', '', '', '', 0, 0, 0, 0, 0, 0, 0, 0, "+a06c01+", 0)";
					System.out.println("sql_insert_tax="+sql_insert_tax);
					try
                   	{
                   		cdb.insert(sql_insert_tax);
                   		cdb.commitDB();
                   	}
                   	catch(Exception e)
        			{
        				System.out.println("sql_insert_tax Excepiton : "+e+"query="+sql_insert_tax);
        				cdb.rollDB();
        				status_doctor=false;
        			}
	       }
        }
        else
        {
        	status_doctor=false;
        }
        return status_doctor;
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
        
}