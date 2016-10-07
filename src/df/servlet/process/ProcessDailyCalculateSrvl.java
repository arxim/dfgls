package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.TrnDaily;
import df.bean.obj.Item.DrMethodAllocation;
import df.bean.obj.doctor.CareProvider;
import df.bean.obj.doctor.DoctorList;
import df.bean.obj.util.JDate;

/**
 * Servlet implementation class ProcessDailyCalculateSrvl
 */
public class ProcessDailyCalculateSrvl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessDailyCalculateSrvl() {
        super();
    }
	public void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		response.setContentType("text/xml; charset=UTF-8");
	    PrintWriter out = response.getWriter();
	    out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");  
	    
	    request.setCharacterEncoding("UTF-8");
    	String hospitalCode = request.getParameter("HOSPITAL_CODE");
    	String startDate = request.getParameter("START_DATE");
    	String endDate = request.getParameter("END_DATE");
		String invoiceNo = request.getParameter("INVOICE_NO");

		System.out.println(invoiceNo+" : "+hospitalCode+" : "+startDate+endDate+"<>"+(DoctorList)request.getAttribute("DR_LIST"));
		int numAffRec = 1;
		try {
            Thread.sleep(10);
            out.print("<RESULT><SUCCESS>" + numAffRec + "</SUCCESS></RESULT>");
        }catch (Exception  e) {
            e.printStackTrace(out);
        }
	}
	/*
	public boolean processCalculate(String startDate, String endDate, String hospitalCode, String invoiceNo) {
        boolean ret = false;
        String doctorCode = "";
        CareProvider careProvider = null;
		DBConnection conn = new DBConnection();
		conn.connectToLocal();
		TrnDaily tDaily = null;

	        if (drMethodAllocation == null){
	        	drMethodAllocation = new DrMethodAllocation(this.connect, hospitalCode); 
	        }

        	tDaily = new TrnDaily(conn);
            try {
                ret = true;
                String sql = TrnDaily.getSQL_TRN_DAILY(startDate, endDate, hospitalCode, invoiceNo);
                tDaily.OpenResultSet(sql);
            	
                while (tDaily.MoveNext()) {
                    doctorCode = tDaily.getDoctorCode();
                    careProvider = this.drList.getDoctor(doctorCode);
                    if (careProvider != null) {
                        careProvider.setTrnDaily(tDaily);
                        careProvider.setDrMethodAllocation(this.drMethodAllocation);
                        if (careProvider.computeTransDaily()) { 
                            ret = true;
                        }else {  
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
    */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
