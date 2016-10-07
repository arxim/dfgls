package df.servlet.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.TrnDaily;
import df.bean.obj.Item.DrMethodAllocation;
import df.bean.obj.doctor.CareProvider;
import df.bean.obj.doctor.DoctorList;
import df.bean.obj.util.JDate;

public class ProcessDailySrvl extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private DoctorList drList = null;
    private DBConnection connect = null;
	private DBConnection conn = null;
	private DBConn dbConn = null;
    private DrMethodAllocation drMethodAllocation =null;
    private int countNum = 0;

	public void processRequest(HttpServletRequest request, HttpServletResponse response){
    	String mode = request.getParameter("mode");
    	String hospitalCode = request.getParameter("hospitalCode");
    	String startDate = request.getParameter("startDate");
    	String endDate = request.getParameter("endDate");
		String replaceStartDeta = JDate.saveDate(startDate);
		String replaceEndDate = JDate.saveDate(endDate);
    	
    	if(request.getParameter("mode") != null){
    		if(mode.equals("getInvoice")){
    			dbConn = new DBConn();
    			HashMap<String, String> hashData = new HashMap<String, String>();
    			String bothJson= "{ \"listInvoice\" : [";
                String sql = "SELECT DISTINCT INVOICE_NO "+
                			" FROM TRN_DAILY " +
                            " WHERE HOSPITAL_CODE = '" + hospitalCode + "'" +
                            " AND (TRANSACTION_DATE >= '" + replaceStartDeta + "'" +     // #20071123# this.getStartComputeDate()
                            " AND TRANSACTION_DATE <= '" + replaceEndDate + "')" +  // #20071123# this.getEndComputeDate()
                            " AND (BATCH_NO IS NULL OR BATCH_NO = '')" +
                            " AND ACTIVE = '1' " +
                            " AND AMOUNT_AFT_DISCOUNT <> 0 " +
                            " AND (COMPUTE_DAILY_DATE IS NULL OR COMPUTE_DAILY_DATE = '') ";
                sql = sql + " ORDER BY INVOICE_NO";
                ArrayList<HashMap<String, String>> arrData = dbConn.listQueryData(sql);
                dbConn.closeDB("");
    	        if(arrData.size() > 0){
    	        	for(int i=0;i<arrData.size();i++){
    	        		hashData = arrData.get(i);
    	        		bothJson += "{\"inviceNo\" : \""+hashData.get("INVOICE_NO")+"\"}, ";
    	        	}
        			bothJson = bothJson.substring(0, bothJson.length()-2);
    				bothJson += "]}";
    	        }else{
    	        	bothJson ="";
    	        }
    	        arrData = null; //add 20120902
    	        hashData = null; //add 20120902
    			try {
    		        response.setContentType("application/json"); 
    				response.setCharacterEncoding("utf-8"); 
    				response.getWriter().write(bothJson);
    			} catch (IOException e) {
    				System.out.println("Error ====> "+e.getMessage());
    				e.printStackTrace();
    			}
    		}else if(mode.equals("calculate")){
    	    	String inviceNo = request.getParameter("inviceNo");
    	    	int count = Integer.parseInt(request.getParameter("count"));
		    	int maxSize = Integer.parseInt(request.getParameter("maxSize"))-1;
    			String bothJson= "";
    	    	if(count == 0){
    	    		this.conn = new DBConnection();
    	    		this.conn.connectToLocal(); //add 20120902
                    this.connect = new DBConnection(); //add 20120902
                    this.connect.connectToLocal(); //add 20120902
                    this.drList = new DoctorList(hospitalCode, conn); //add 20120902
	                drList.newAllDoctor(hospitalCode);
	                drMethodAllocation = new DrMethodAllocation(this.connect, hospitalCode);
    	    	}
    	    	
    	    	if(!processCalculate(replaceStartDeta, replaceEndDate, hospitalCode,inviceNo)){this.countNum++;}
    	    	else{this.countNum++;}
                bothJson = "{\"count\":\""+this.countNum+"\",\"lastData\":\"N\"}";
                //System.out.println("countNum = " + countNum);
    	    	if(count == maxSize){
                    bothJson = "{\"count\":\""+this.countNum+"\",\"lastData\":\"Y\"}";
    	    		this.connect.Close();
    	    		this.conn.Close();
    	    		this.countNum = 0;
	                System.out.println("Close Connection");
    	    	}
    	    	
    	    	try {
    		        response.setContentType("application/json"); 
    				response.setCharacterEncoding("utf-8"); 
    				response.getWriter().write(bothJson);
    			} catch (IOException e) {
    				System.out.println("IOException ====> "+e.getMessage());
    				e.printStackTrace();
    			}
    		}
            
    	}else{
    		System.out.println("Mode is Null Cannot Process!!!");
    	}
	}
	public boolean processCalculate(String startDate, String endDate, String hospitalCode, String invoiceNo) {
        boolean ret = false;
        String doctorCode = "";
        CareProvider careProvider = null;
		DBConnection conn = new DBConnection();
		TrnDaily tDaily = null;
	    
	        if (drMethodAllocation == null){
	        	drMethodAllocation = new DrMethodAllocation(this.connect, hospitalCode); 
	        }
        	tDaily = new TrnDaily(this.connect);
            try {
                ret = true;
                String sql = TrnDaily.getSQL_TRN_DAILY(startDate, endDate, hospitalCode, invoiceNo);
                tDaily.OpenResultSet(sql);
            	
                while (tDaily.MoveNext()) {
                	System.out.println("Process Calculate");
                    doctorCode = tDaily.getDoctorCode();
                    careProvider = this.drList.getDoctor(doctorCode);
                    if (careProvider != null) {
                        careProvider.setTrnDaily(tDaily);
                        careProvider.setDrMethodAllocation(this.drMethodAllocation);
                        if (careProvider.computeTransDaily()) { 
                            ret = true;
                        } else {  
                            ret = false;
                        }
                    } else {
                        ret = false;
                    }
                }
            } catch (Exception e) {
            	System.out.println("Exception == > "+e.getMessage());
                ret=false;
            } finally {
               //Clean up resources, close the connection.
                try {
                    tDaily.setStatement(null);
                    tDaily.setResultSet(null);
                    tDaily = null;
                    conn.Close();
                } catch (Exception ex) { 
                	System.out.println("Exception == > "+ex.getMessage());
                } 
            }
        return ret;
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
}
