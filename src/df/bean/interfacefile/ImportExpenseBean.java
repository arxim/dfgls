package df.bean.interfacefile;

import df.bean.db.conn.DBConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import df.bean.db.conn.DBConn;
import df.bean.db.table.Batch;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;
/**
 * @author arxim
 */
public class ImportExpenseBean extends InterfaceTextFileBean {
    private ResultSet rs;
    private Statement stm;
    private String expenseCode = "";
    private String expenseName = "";
    private String accountCode = "";
    private String taxType = "";
    private String hospitalCode = "";
    private String departmentCode = "";
    
    @Override
    public boolean insertData(String fn, DBConnection da) {
        DBConnection d = new DBConnection();
        d.connectToLocal();
        boolean status = true;
        ArrayList a = null;
        String[] sub_data = null;
        String temp = "";
        int insert_count = 0;
        this.getExpenseCode(hospitalCode);
        Batch b = new Batch(hospitalCode, d);
        try {
            setConn(d);//connect database
            setFileName(fn);//set filename read
            copyDataFile();//copy data from file to superclass arraylist member
            a = getData();//copy data from superclass arraylist member
            getConn().beginTrans();
            stm = getConn().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stm.executeQuery("SELECT * FROM TRN_EXPENSE_DETAIL WHERE 1=0");
            insert_count = 0;
            for(int i = 0; i<a.size(); i++){
                if(i>0){
                    temp = (String)a.get(i);
                    sub_data = temp.split("[|]");
                    System.out.println(sub_data.length);
                    rs.moveToInsertRow();
                    setMessage("Employee ID = "+sub_data[1]+" Doc No = "+sub_data[2]+" Date = "+sub_data[3].trim());
                    rs.updateString("HOSPITAL_CODE",sub_data[0].trim());
                    rs.updateString("DOCTOR_CODE",getDoctorCode(sub_data[1].trim(),sub_data[0].trim()));
                    rs.updateString("EMPLOYEE_ID",sub_data[1].trim());
                    rs.updateString("DOC_NO",sub_data[2].trim());
                    rs.updateString("LINE_NO", ""+fn.substring(fn.length()-8, fn.length())+i);
                    rs.updateString("DOC_DATE",sub_data[3].trim());
                    rs.updateDouble("AMOUNT",Double.parseDouble(""+sub_data[4].trim()));                    
                    rs.updateString("EXPENSE_SIGN","-1");
                    rs.updateString("EXPENSE_ACCOUNT_CODE", this.accountCode);
                    rs.updateString("EXPENSE_CODE",this.expenseCode);
                    rs.updateString("TAX_TYPE_CODE", this.taxType);
                    rs.updateString("YYYY",b.getYyyy());
                    rs.updateString("MM",b.getMm());
                    try{
                    	rs.updateString("NOTE","CO ค่ารักษาพยาบาล :"+new String(sub_data[7].trim().getBytes(),"TIS-620")+"  HN:"+sub_data[5].trim()+
                    	", Episode:"+sub_data[6].trim());
                    }catch(Exception ee){
                    	System.out.println("Error Interface Expense Note : "+ee);
                    	rs.updateString("NOTE","CO ค่ารักษาพยาบาล");
                    }
                    //rs.updateString("NOTE",new String(sub_data[5].trim().getBytes(),"TIS-620"));                    
                    rs.updateString("USER_ID","INTERFACE");
                    rs.updateString("UPDATE_DATE",JDate.getDate());
                    rs.updateString("UPDATE_TIME", JDate.getTime());
                    try  { 
                    	 rs.updateString("DEPARTMENT_CODE" ,this.getDepartmentCode(sub_data[0].trim().toString(), sub_data[9].trim().toString()));
                    }catch(Exception ex)  {
                    	System.out.println("Interface CO error : "+ex);
                    	rs.updateString("DEPARTMENT_CODE" ,"");
                    }
                                        
                    try{
                        rs.insertRow();
                        insert_count++;
                    }catch(Exception e){
                        System.out.println(e);
                        TRN_Error.writeErrorLog(this.getConn().getConnection(), "Import Expense", "File Interface Error Line : "+i, ""+e.getMessage(), this.getMessage());
                    }
                }
            }
            if(insert_count < a.size()-1){
                setMessage("Error : "+ ""+(a.size()- (insert_count+1))+"/"+(a.size()-1)+" records complete : "+insert_count+" records.");
            }else{
                setMessage("Complete "+(a.size()-1)+"/"+insert_count+" records.");
            }
            getConn().commitTrans();
            status = true;
            rs.close();
        } catch (Exception f) {
            status = false;
            getConn().rollBackTrans();
        }
        finally {
            try {
                if (rs != null) { 
                    rs.close();
                    rs = null;
                }
                if (stm != null) {
                    stm.close();
                    stm = null;
                }
                d.Close();
            }
            catch (Exception ex)  {
                System.out.println(ex);
            }
            
        }
        return status;
    }
    public void setHospital(String hp){
    	this.hospitalCode = hp;
    }
    private String getDoctorCode(String emp_id, String hospital_code){
        String employee = "";
        String stm = "SELECT CODE FROM DOCTOR_PROFILE WHERE EMPLOYEE_ID = '"+emp_id+"'"+
        " AND HOSPITAL_CODE = '"+hospital_code+"'";
        try{
            employee = getConn().executeQueryString(stm);
            if(employee.equals("null")||employee.equals(null)||employee.equals("")){
                employee = "99999999";
            }
        }catch(Exception e){
            System.out.println(e);
            employee = "99999999";
        }
        return employee;
    }
    private String getExpenseCode(String hospital_code){
    	DBConn d = new DBConn();
    	String a[][] = null;
        String employee = "";
        String stm = "SELECT CODE, ACCOUNT_CODE, TAX_TYPE_CODE, DESCRIPTION FROM EXPENSE " +
        			 "WHERE HOSPITAL_CODE = '"+hospital_code+"' AND ADJUST_TYPE = 'CO'";
        try{
        	d.setStatement();
            a = d.query(stm);
            this.accountCode = a[0][1];
            this.expenseCode = a[0][0];
            this.taxType = a[0][2];
            this.expenseName = a[0][3];
        }catch(Exception e){
            System.out.println("Get Expense COde : "+e);
        }
        return employee;
    }
    
    private String getDepartmentCode(String hospital_code, String department_code){
    	DBConn d = new DBConn();
    	String a[][] = null;
        String dept = "";
        String stm = "SELECT NEW_DEPT_CODE FROM TB_MAPPING_DEPT " +
        			 "WHERE HOSPITAL_CODE = '"+hospital_code+"' " +
        			 "AND OLD_DEPT_CODE = '"+department_code+"'";
        try{
        	System.out.println(stm);
        	d.setStatement();
            a = d.query(stm);
            dept = a[0][0];
        }catch(Exception e){
            System.out.println(e);
        }
        System.out.println(dept);
        return dept;
    }


    @Override
    public boolean exportData(String fn, String hp, String type, String year, String month, DBConn d, String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
