package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import df.bean.db.conn.DBConn;
import df.bean.guarantee.ProcessGuaranteeBeanNew;
import df.bean.process.ProcessTimeTableCase;

/**
 * Servlet implementation class ProcessRunTimeTableCaseSrvl
 */
public class ProcessRunTimeTableCaseSrvl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessRunTimeTableCaseSrvl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException {
		        response.setContentType("text/xml; charset=UTF-8");
		        PrintWriter out = response.getWriter();
		        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		        request.setCharacterEncoding("UTF-8");
		        
		        String hospitalCode = request.getSession().getAttribute("HOSPITAL_CODE").toString();
		        String userId = request.getParameter("USER");
		        String yyyy = request.getParameter("YYYY");
		        String mm = request.getParameter("MM");
		        
		        ProcessTimeTableCase pttc = null;
		        boolean st = true;
	            try {
	                pttc = new ProcessTimeTableCase();
	                pttc.initProcessMappingCase(hospitalCode, userId);
	                st = pttc.runTimeTableCase(yyyy,mm);
	                out.print("<RESULT><SUCCESS>"+st+"</SUCCESS></RESULT>");
	            }catch (Exception  e) {
	            	st = false;
	                out.print("<RESULT><SUCCESS>"+st+"</SUCCESS></RESULT>");
	                e.printStackTrace(out);
	            }finally {
		            out.close();
		        }
		    } 

}
