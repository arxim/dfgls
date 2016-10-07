package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import df.bean.process.ProcessXrayManagementBean;

/**
 * Servlet implementation class ProcessXrayManagementController
 */
public class ProcessXrayManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessXrayManagementController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request , response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request , response);
	}

	public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{	
				response.setContentType("text/xml; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				request.setCharacterEncoding("UTF-8");
			 
				String hospitalCode =  request.getParameter("HOSPITAL_CODE");
				String yyyy = request.getParameter("YYYY");
				String mm = request.getParameter("MM");
			
				ProcessXrayManagementBean processXrayManagementBean = new ProcessXrayManagementBean(hospitalCode , yyyy , mm);
				
				try {
		                
					Thread.sleep(20);
		            
					if(processXrayManagementBean.doProcess()){
		                 out.print("<RESULT><SUCCESS>SUCCESS</SUCCESS></RESULT>");
		              }else{
		                 out.print("<RESULT><SUCCESS>false</SUCCESS></RESULT>");
		              }
		                
		         } catch (Exception  e) {
		                out.print("<RESULT><SUCCESS>Error</SUCCESS></RESULT>");
		                e.printStackTrace(out);
		         }
				
		}catch (Exception ex){
			System.out.println(ex.toString());
		}
	}
	
}
