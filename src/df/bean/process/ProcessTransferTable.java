/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.process;

import df.bean.db.conn.DBConn;
import df.bean.obj.util.FindDate;
import df.bean.obj.util.JDate;

import java.sql.SQLException;

/**
 *
 * @author USER
 */
public class ProcessTransferTable {
    private String hpCode = "";
    private String mm = "";
    private String yyyy="";
    private String typeGuarantee = "";
    private static final int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    private String getHpCode() {
        return hpCode;
    }

    private String getMm() {
        return mm;
    }

    private String getYyyy() {
        return yyyy;
    }

    private void setHpCode(String hpCode) {
        this.hpCode = hpCode;
    }

    private void setMm(String mm) {
        this.mm = mm;
    }

    private void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }
    private String getTypeGuarantee() {
        return typeGuarantee;
    }
    private void setTypeGuarantee(String typeGuarantee) {
        this.typeGuarantee = typeGuarantee;
    }
    private boolean isValid(int m, int d, int y) {
        if (m < 1 || m > 12)      return false;
        if (d < 1 || d > DAYS[m]) return false;
        if (m == 2 && d == 29 && !isLeapYear(y)) return false;
        return true;
    }
    private boolean isLeapYear(int y) {
        if (y % 400 == 0) return true;
        if (y % 100 == 0) return false;
        return (y % 4 == 0);
    }
    private String next(String dt) {
        int day = Integer.parseInt(dt);
        int m = Integer.parseInt(this.getMm());
        int y = Integer.parseInt(this.getYyyy());
        
        if (isValid( m , day + 1, y)){
            return y +""+ (m < 10 ? "0" + m : m) + "" + ((day+1) < 10 ? "0" + (day+1) : (day+1));
        }
        else if (isValid(m + 1, 1, y)){
            return y +""+ ((m+1) < 10 ? "0" + (m+1) : (m+1)) + "01";
        }
        else return (y+1) +""+ "01" + "01";
    }
    private String StartDate(String dt){
        if(dt.length()==7 || dt.length()==1 ) dt = "0" + dt;
        return this.getYyyy()+ "" + this.getMm() + "" + dt;
    }
    private String EndDate(String starttime, String endtime, String date){
        //String dt = this.getYyyy()+ "" + this.getMm() + "" + this.DAYS[Integer.parseInt(this.getMm())];
        String dt = this.getYyyy()+ "" + this.getMm() + "" + JDate.getEndMonthDate(this.getYyyy(), this.getMm());
        if("DLY".equalsIgnoreCase(this.getTypeGuarantee()) || "MLY".equalsIgnoreCase(this.getTypeGuarantee())){
            dt = "" + Integer.parseInt(starttime) + " : " + Integer.parseInt(endtime);
            if(Integer.parseInt(starttime) > Integer.parseInt(endtime)){
                dt = this.next(date);
            }else{
                dt = this.StartDate(date);
            }
        }      
        return dt;
    }
    private boolean rollbackProcess(String h, String m, String y){
        DBConn conn = new DBConn();  
        boolean status = true;
        try {
            conn.setStatement();
            conn.insert("DELETE FROM STP_GUARANTEE WHERE HOSPITAL_CODE = '"+h+"' AND YYYY = '"+y+"' AND MM = '"+m+"' AND USER_ID = 'TRANSFER'");
            conn.commitDB();
        } catch (SQLException ex) {
        	conn.rollDB();
        	status = false;
        }
    	return status;
    }
    
    public boolean ProcessMain(String h,String m, String y){
        this.setHpCode(h);
        this.setMm(m);
        this.setYyyy(y);
        String[][] arr = null;
        DBConn conn = new DBConn();
        FindDate fn = new FindDate();
        
        try {
            conn.setStatement();
        } catch (SQLException ex) {
        }

        String SQLCommand = "";
        //System.out.println("Delete SQLCommand : " + SQLCommand);
        try{
            /*
             DLY -> DLY : 20090403120000
             MLY -> MLY : 200904
             MLA -> MLY : 200904
             */
        	if(!this.rollbackProcess(h, m, y)){
        		System.out.println("Rollback Guarantee Transfer Error");
        	}  
        	String guarantee_code = "";       

            String select = "SELECT TT.HOSPITAL_CODE," + 
            		"TT.DOCTOR_CODE," + 
            		"TT.GUARANTEE_TYPE_CODE," + 
            		"TT.DAY," + 
            		"TT.START_TIME," + 
            		"TT.END_TIME," + 
            		"TT.GUARANTEE_AMOUNT," + 
            		"TT.GUARANTEE_EXCLUDE_AMOUNT," + 
            		"DG.IN_GUARANTEE_PCT," + 
            		"DG.OVER_GUARANTEE_PCT," + 
            		"TT.GUARANTEE_FIX_AMOUNT," + 
            		"TT.GUARANTEE_INCLUDE_AMOUNT," + 
            		"DR.DEPARTMENT_CODE," + 
            		"TT.ADMISSION_TYPE_CODE  " + 
            		"FROM STP_MASTER_TIME_TABLE TT  " + 
            		"LEFT OUTER JOIN DOCTOR DR ON TT.HOSPITAL_CODE = DR.HOSPITAL_CODE AND TT.DOCTOR_CODE = DR.CODE  " + 
            		"RIGHT OUTER JOIN DOCTOR_GUARANTEE DG ON TT.HOSPITAL_CODE=DG.HOSPITAL_CODE AND TT.DOCTOR_CODE=DG.GUARANTEE_DR_CODE AND TT.GUARANTEE_TYPE_CODE=DG.GUARANTEE_TYPE_CODE " + 
            		"RIGHT OUTER JOIN BATCH B ON DG.HOSPITAL_CODE=B.HOSPITAL_CODE  " + 
            		"WHERE TT.HOSPITAL_CODE='"+this.hpCode+"' AND TT.ACTIVE='1' AND TT.IS_TB_GUARANTEE='Y' " + 
            		"AND ( B.CLOSE_DATE IS NULL OR B.CLOSE_DATE ='')  " + 
            		"AND B.BATCH_NO BETWEEN SUBSTRING(DG.GUARANTEE_START_DATE,1,6) AND SUBSTRING(DG.GUARANTEE_EXPIRE_DATE,1,6)";
            arr = conn.query(select);
            String dt = "";
            String sqlInsert = "";
            for(int i=0; i < arr.length ; i++){
                dt = "";
                this.setTypeGuarantee(arr[i][2]);

                if(this.getTypeGuarantee().equals("DLY")){
                    dt = fn.FindDateAll(arr[i][3], this.getMm(), this.getYyyy());
                    String[] arrDt = dt.split("\\|");
                    for(int k = 0 ; k < arrDt.length ; k++){            
                        guarantee_code = this.StartDate(arrDt[k] + arr[i][4]);
                        sqlInsert = 
                        "INSERT INTO STP_GUARANTEE(GUARANTEE_ALLOCATE_PCT, OVER_ALLOCATE_PCT, HOSPITAL_CODE, IS_GUARANTEE_DAILY, USER_ID, MM, YYYY, GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE, GUARANTEE_TYPE_CODE, START_DATE, START_TIME, END_DATE, END_TIME, GUARANTEE_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT, GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, GL_DEPARTMENT_CODE, INCLUDE_OF_TIME, INCLUDE_PER_TIME, TAX_TYPE_CODE, AMOUNT_DIFF_TIME, IS_INCLUDE_LOCATION, AMOUNT_PER_TIME, UPDATE_DATE, UPDATE_TIME) " +
                        "VALUES('"+arr[i][8]+"','"+arr[i][9]+"','"+ this.getHpCode() +"','Y','TRANSFER','"+this.getMm()+"','"+ this.getYyyy() +"', '"+ arr[i][1] +"','"+ guarantee_code +"','"+arr[i][13]+"','"+ arr[i][2] +"','"+ this.StartDate(arrDt[k]) +"','"+arr[i][4]+"','"+ this.EndDate(arr[i][4], arr[i][5], arrDt[k]) +"','"+ arr[i][5] +"','"+arr[i][6]+"','"+arr[i][7]+"', '"+arr[i][10]+"', '"+arr[i][11]+"','"+arr[i][12]+"',0,0,'',0,'',0,'"+JDate.getDate()+"','"+JDate.getTime()+"')";
                        try{
                        	conn.insert(sqlInsert);
                        	conn.commitDB();
                        }catch(Exception e){
                        	conn.rollDB();
                        	System.out.println("Error Insert Daily Guarantee : "+e+"<Statement>"+sqlInsert);                      	
                        }
                    }
                }else{
                    dt = " Monthly in STP_GUARANTEE ";
                    guarantee_code = this.getYyyy() + "" + this.getMm();
                    if(this.getTypeGuarantee().equals("MLY")){
                    	//Monthly fix Day
                        dt = fn.FindDateAll(arr[i][3], this.getMm(), this.getYyyy());
                        String[] arrDt = dt.split("\\|");
                        for(int k = 0 ; k < arrDt.length ; k++){
                            sqlInsert = "INSERT INTO STP_GUARANTEE(HOSPITAL_CODE, USER_ID, MM, YYYY, GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE, GUARANTEE_TYPE_CODE, IS_GUARANTEE_DAILY, START_DATE, START_TIME, END_DATE, END_TIME, GUARANTEE_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT, GUARANTEE_ALLOCATE_PCT, OVER_ALLOCATE_PCT, GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, GL_DEPARTMENT_CODE, INCLUDE_OF_TIME, INCLUDE_PER_TIME, TAX_TYPE_CODE, AMOUNT_DIFF_TIME, IS_INCLUDE_LOCATION, AMOUNT_PER_TIME, UPDATE_DATE, UPDATE_TIME) " +
                            "VALUES('"+ this.getHpCode() +"','TRANSFER','"+this.getMm()+"','"+ this.getYyyy() +"', '"+ arr[i][1] +"','"+ guarantee_code +"','"+arr[i][13]+"','MLY','N','"+ this.StartDate(arrDt[k]) +"','"+arr[i][4]+"','"+ this.StartDate(arrDt[k]) +"','"+ arr[i][5] +"','"+arr[i][6]+"','"+arr[i][7]+"',"+arr[i][8]+","+arr[i][9]+", '"+arr[i][10]+"', '"+arr[i][11]+"','"+arr[i][12]+"',0,0,'',0,'',0,'"+JDate.getDate()+"','"+JDate.getTime()+"')";
                            try{
                            	conn.insert(sqlInsert);
                            	conn.commitDB();
                            }catch(Exception e){
                            	conn.rollDB();
                                System.out.println("Error Insert Month fix days Guarantee : "+e+"<Statement>"+sqlInsert);                         	
                            }
                        }
                    }else if(this.getTypeGuarantee().equals("MLD")){
                    	//Daily to Monthly
                        dt = fn.FindDateAll(arr[i][3], this.getMm(), this.getYyyy());
                        String[] arrDt = dt.split("\\|");
                        for(int k = 0 ; k < arrDt.length ; k++){
                            sqlInsert = "INSERT INTO STP_GUARANTEE(HOSPITAL_CODE, USER_ID, MM, YYYY, GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE, GUARANTEE_TYPE_CODE, IS_GUARANTEE_DAILY, START_DATE, START_TIME, END_DATE, END_TIME, GUARANTEE_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT, GUARANTEE_ALLOCATE_PCT, OVER_ALLOCATE_PCT, GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, GL_DEPARTMENT_CODE, INCLUDE_OF_TIME, INCLUDE_PER_TIME, TAX_TYPE_CODE, AMOUNT_DIFF_TIME, IS_INCLUDE_LOCATION, AMOUNT_PER_TIME, UPDATE_DATE, UPDATE_TIME) " +
                            "VALUES('"+ this.getHpCode() +"','TRANSFER','"+this.getMm()+"','"+ this.getYyyy() +"', '"+ arr[i][1] +"','"+ guarantee_code +"','"+arr[i][13]+"','MLD','Y','"+ this.StartDate(arrDt[k]) +"','"+arr[i][4]+"','"+ this.StartDate(arrDt[k]) +"','"+ arr[i][5] +"','"+arr[i][6]+"','"+arr[i][7]+"',"+arr[i][8]+","+arr[i][9]+", '"+arr[i][10]+"', '"+arr[i][11]+"','"+arr[i][12]+"',0,0,'',0,'',0,'"+JDate.getDate()+"','"+JDate.getTime()+"')";
                            try{
                            	conn.insert(sqlInsert);
                            	conn.commitDB();
                            }catch(Exception e){
                            	conn.rollDB();
                                System.out.println("Error Insert Day to Month Guarantee : "+e+"<Statement>"+sqlInsert);                         	
                            }
                        }
                    }else if(this.getTypeGuarantee().equals("MLA")){
	                    	sqlInsert = "INSERT INTO STP_GUARANTEE(HOSPITAL_CODE, USER_ID, MM, YYYY, GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE, GUARANTEE_TYPE_CODE, IS_GUARANTEE_DAILY, START_DATE, START_TIME, END_DATE, END_TIME, GUARANTEE_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT, GUARANTEE_ALLOCATE_PCT, OVER_ALLOCATE_PCT, GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, GL_DEPARTMENT_CODE, INCLUDE_OF_TIME, INCLUDE_PER_TIME, TAX_TYPE_CODE, AMOUNT_DIFF_TIME, IS_INCLUDE_LOCATION, AMOUNT_PER_TIME, UPDATE_DATE, UPDATE_TIME) " +
	                    	"VALUES('"+ this.getHpCode() +"','TRANSFER','"+this.getMm()+"','"+ this.getYyyy() +"', '"+ arr[i][1] +"','"+ guarantee_code +"','"+arr[i][13]+"','MLY','N','"+ this.StartDate("01") +"','"+arr[i][4]+"','"+ this.EndDate(arr[i][4], arr[i][5], "") +"','"+ arr[i][5] +"','"+arr[i][6]+"','"+arr[i][7]+"',"+arr[i][8]+","+arr[i][9]+", '"+arr[i][10]+"', '"+arr[i][11]+"','"+arr[i][12]+"',0,0,'',0,'',0,'"+JDate.getDate()+"','"+JDate.getTime()+"')";
	                    	try{
	                        	conn.insert(sqlInsert);
	                        	conn.commitDB();
	                        }catch(Exception e){
	                        	conn.rollDB();
	                        	System.out.println("Error Insert Monthly/Monthly : "+e+"<Statement>"+sqlInsert);                         	
	                        }
                    }else if(this.getTypeGuarantee().equals("MMY")){
                    		sqlInsert = "INSERT INTO STP_GUARANTEE(HOSPITAL_CODE, USER_ID, MM, YYYY, GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE, GUARANTEE_TYPE_CODE, IS_GUARANTEE_DAILY, START_DATE, START_TIME, END_DATE, END_TIME, GUARANTEE_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT, GUARANTEE_ALLOCATE_PCT, OVER_ALLOCATE_PCT, GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, GL_DEPARTMENT_CODE,INCLUDE_OF_TIME, INCLUDE_PER_TIME, TAX_TYPE_CODE, AMOUNT_DIFF_TIME, IS_INCLUDE_LOCATION, AMOUNT_PER_TIME, UPDATE_DATE, UPDATE_TIME) " +
                    		"VALUES('"+ this.getHpCode() +"','TRANSFER','"+this.getMm()+"','"+ this.getYyyy() +"', '"+ arr[i][1] +"','"+ guarantee_code +"','"+arr[i][13]+"','MMY','N','"+ this.StartDate("01") +"','"+arr[i][4]+"','"+ this.EndDate(arr[i][4], arr[i][5], "") +"','"+ arr[i][5] +"','"+arr[i][6]+"','"+arr[i][7]+"',"+arr[i][8]+","+arr[i][9]+", '"+arr[i][10]+"', '"+arr[i][11]+"','"+arr[i][12]+"',0,0,'',0,'',0,'"+JDate.getDate()+"','"+JDate.getTime()+"')";
		                	try{
		                    	conn.insert(sqlInsert);
		                    	conn.commitDB();
		                    }catch(Exception e){
		                    	conn.rollDB();
		                    	System.out.println("Error Insert Monthly/Monthly : "+e+"<Statement>"+sqlInsert);                         	
		                    }
                    }else if(this.getTypeGuarantee().equals("STP")){
	                    sqlInsert = "INSERT INTO STP_GUARANTEE(HOSPITAL_CODE, USER_ID, MM, YYYY, GUARANTEE_DR_CODE, GUARANTEE_CODE, ADMISSION_TYPE_CODE, GUARANTEE_TYPE_CODE, IS_GUARANTEE_DAILY, START_DATE, START_TIME, END_DATE, END_TIME, GUARANTEE_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT, GUARANTEE_ALLOCATE_PCT, OVER_ALLOCATE_PCT, GUARANTEE_FIX_AMOUNT, GUARANTEE_INCLUDE_AMOUNT, GL_DEPARTMENT_CODE,INCLUDE_OF_TIME, INCLUDE_PER_TIME, TAX_TYPE_CODE, AMOUNT_DIFF_TIME, IS_INCLUDE_LOCATION, AMOUNT_PER_TIME, UPDATE_DATE, UPDATE_TIME) " +
                        "VALUES('"+ this.getHpCode() +"','TRANSFER','"+this.getMm()+"','"+ this.getYyyy() +"', '"+ arr[i][1] +"','"+ guarantee_code +"','"+arr[i][13]+"','STP','N','"+ this.StartDate("01") +"','"+arr[i][4]+"','"+ this.EndDate(arr[i][4], arr[i][5], "") +"','"+ arr[i][5] +"','"+arr[i][6]+"','"+arr[i][7]+"',"+arr[i][8]+","+arr[i][9]+", '"+arr[i][10]+"', '"+arr[i][11]+"','"+arr[i][12]+"',0,0,'',0,'',0,'"+JDate.getDate()+"','"+JDate.getTime()+"')";
                    	try{
                        	conn.insert(sqlInsert);
                        	conn.commitDB();
                        }catch(Exception e){
                        	conn.rollDB();
                        	System.out.println("Error Insert Monthly/Step Guarantee : "+e+"<Statement>"+sqlInsert);                         	
                        }
                    }
                }
            }
            conn.closeStatement("");
            conn.closeDB("");
            return true;
        }catch(Exception err){
        	System.out.println("Process Transfer timetable Error : "+err);        	
            return false;
        }finally{
        	System.out.println("Process Transfer timetable Finish on Time : "+JDate.getTime());        	
        }
    }
//    public static void main(String[] arg){
//        ProcressMatchTable p = new ProcressMatchTable();
//        p.ProcessMain("00001","04","2009");
//    }
}