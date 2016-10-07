package df.servlet.process;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import df.bean.process.ProcessDischargeSummary;
import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.expense.ExpenseSummaryBean;
import df.bean.obj.doctor.CareProvider;
import df.bean.obj.doctor.DoctorList;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;
import df.bean.process.ProcessBankTMBPaymentMonthly;
import df.bean.process.ProcessSummaryMonthlyDF;

public class ProcessAllMonthlySrvl extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private DoctorList doctorList = null;
    private DoctorList drList = null;
    private ExpenseSummaryBean  esb;
    private DBConnection conn = null;
    private int countNum = 0;
    private DBConn cdb = null;
    private String[] strArray =null;
    private String d = "";
    private String mm = "";
    private String year = "";
    private String bothJson= "";
    private Batch batch = null;
    private ProcessBankTMBPaymentMonthly pbm = null;
    
    public void doProcess(HttpServletRequest request, HttpServletResponse response){
    	String mode = request.getParameter("mode");
    	String term = request.getParameter("term");
    	String date = request.getParameter("date");
    	String user = request.getParameter("user");
		String hospitalCode = request.getParameter("hospitalCode");
		System.out.println(term+"<>"+mode);
    	if(request.getParameter("mode") != null){
    		if(request.getParameter("date") != null){
	        	this.strArray = date.split("\\/");
	        	this.d = this.strArray[0];
	        	this.mm = this.strArray[1];
	        	this.year = this.strArray[2];
	        	
	        	if(this.mm.equals("01")){
	        		this.mm = "12";
	        		this.year =  Integer.toString(Integer.parseInt(this.year)-1);
	        	}else{
	        		if(this.mm.substring(0,1).equals("0")){
		        		this.mm = "0"+Integer.toString(Integer.parseInt(this.mm)-1);
	        		}else{
		        		this.mm = Integer.toString(Integer.parseInt(this.mm)-1);
		        		if(this.mm.length()==1){
		        			this.mm = "0"+this.mm;
		        		}
	        		}
	        	}
    		}
	    	if(mode.equals("getDoctor")){
	    		System.out.println("mode = " + mode);
				DBConn dbConn = new DBConn();
				String bothJson= "{ \"listDoctorCode\" : [";
				String sql = "";
				HashMap<String, String> hashData = new HashMap<String, String>();
		        sql="SELECT CODE FROM DOCTOR WHERE HOSPITAL_CODE = '"+hospitalCode+"'";
		        System.out.println("Get Doctor");
		        ArrayList<HashMap<String, String>> arrData = dbConn.listQueryData(sql);
		        dbConn.closeDB("");
		        if(arrData.size() > 0){
		        	for(int i=0;i<arrData.size();i++){
		        		hashData = arrData.get(i);
		        		bothJson += "{\"doctorCode\" : \""+hashData.get("CODE")+"\"}, ";
		        	}
		        }
	    		try{
			       //write Json	
	    			bothJson = bothJson.substring(0, bothJson.length()-2);
					bothJson += "]}";
			        response.setContentType("application/json"); 
					response.setCharacterEncoding("utf-8"); 
					response.getWriter().write(bothJson);
				}catch(IOException e){
					System.out.println("Error Message => " + e.getMessage());
				}
			
	    	}else if(mode.equals("processMonthly")){ // ====== Mouthly Calulate ======
	    		System.out.println("process mode : "+mode+" term : "+term);
	    		try {
	    			 if(term.equals("2")){
		    			 new ProcessSummaryMonthlyDF(hospitalCode, date,"","",user).doProcess();
	    			 }else{
		    			 new ProcessSummaryMonthlyDF(hospitalCode, date,"","",user).doProcess();	    				 
	    			 }
	    			 bothJson = "{\"count\" : \"1\"}";	    			
				} catch (Exception e1) {
					 bothJson = "{\"count\" : \"0\"}";
				}
				try {
			        response.setContentType("application/json"); 
					response.setCharacterEncoding("utf-8"); 
					response.getWriter().write(bothJson);
				} catch (IOException e) {
					System.out.println("Error Message = > " + e.getMessage());
				}
			}else if( mode.equals("processDischarge") && term.equals("2") ){
				ProcessDischargeSummary PD=new ProcessDischargeSummary();
				try {
					if(PD.doProcessDischange(hospitalCode,this.year,this.mm)){
						bothJson = "{\"count\" : \"1\"}";						
					}else{
						bothJson = "{\"count\" : \"0\"}";
					}
				} catch (Exception e) {
					bothJson = "{\"count\" : \"0\"}";
				}
				try {
			        response.setContentType("application/json"); 
					response.setCharacterEncoding("utf-8"); 
					response.getWriter().write(bothJson);
				} catch (IOException e) {
					System.out.println("Error Message = > " + e.getMessage());
				}
			}else{
				try {
					bothJson = "{\"count\" : \"1\"}";						
			        response.setContentType("application/json"); 
					response.setCharacterEncoding("utf-8"); 
					response.getWriter().write(bothJson);
				} catch (IOException e) {
					System.out.println("Error Message = > " + e.getMessage());
				}
			}
    	}else{
    		System.out.println("Mode is Null Cannot Process!!!");
    	}
    }
    
    private void newObject(String hos) {
        batch = new Batch(hos, conn);
        pbm = new ProcessBankTMBPaymentMonthly(conn);
    }
    public void iniExpenseCalulate(){
    	this.conn = new DBConnection();
    	this.conn.connectToLocal();
    	this.cdb = new DBConn(this.conn.getConnection());
        try {
        	this.cdb.setStatement();
        	this.esb = new ExpenseSummaryBean(this.cdb);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }    
    private DoctorList getDoctorList() {
        return doctorList;
    }

    private void setDoctorList(DoctorList doctorList) {
        this.doctorList = doctorList;
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}
}
