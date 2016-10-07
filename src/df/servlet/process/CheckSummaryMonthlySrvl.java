package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import df.bean.db.conn.DBConn;

public class CheckSummaryMonthlySrvl extends HttpServlet  {
    /** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
     
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/xml; charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        request.setCharacterEncoding("UTF-8");
        
        String hospital_code = request.getSession().getAttribute("HOSPITAL_CODE").toString();
        String date_input = request.getParameter("DATE_INPUT");
        String[] _arr = date_input.split("/");
        
        DBConn conn = null;
        String numAffRec = "NO";
        try {
        	conn = new DBConn();
        	conn.setStatement();
        	        	
        	String qCheck = "SELECT MM FROM SUMMARY_MONTHLY WHERE HOSPITAL_CODE='"+ hospital_code +"' AND YYYY='"+ _arr[2] +"' AND MM='"+ _arr[1] +"' GROUP BY MM;";
        	String[][] result = conn.query(qCheck);
        	
        	String qCheckG = "SELECT MM FROM SUMMARY_GUARANTEE WHERE HOSPITAL_CODE='"+ hospital_code +"' AND YYYY='"+ _arr[2] +"' AND MM='"+ _arr[1] +"' GROUP BY MM;";
        	String[][] resultG = conn.query(qCheckG);
        	
        	//if(resultG.length > 0 || result.length > 0 || resultBatch.length >0 ){
        	if(resultG.length > 0 || result.length > 0 ){
        		numAffRec = "NO";
        	}else{
        		numAffRec = "YES";
        	}
            Thread.sleep(20);
            out.print("<RESULT><STATUS>" + numAffRec + "</STATUS></RESULT>");
        }
        catch (Exception  e) {
        	conn.closeDB("");
        	System.out.println("Exception Check Guarantee : "+e);
            e.printStackTrace(out);
        }
        finally {
        	conn.closeDB("");
            out.close();
        }        
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
    * Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
    * Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
    * Returns a short description of the servlet.
    */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}