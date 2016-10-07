package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.process.ProcessAllocateManagementFeeBean;

public class ProcessAllocateManagementFeeSrvl extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
 	
    public ProcessAllocateManagementFeeSrvl() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		JSONObject objJson = new JSONObject();
		PrintWriter out = response.getWriter();
		
		String hospitalCode = request.getParameter("hospitalCode");
		String paymentTerm  = request.getParameter("payment");
		
		double amount = (request.getParameter("txtPercenter") == null ? 0.0 : Double.parseDouble(request.getParameter("txtPercenter").toString())) ;
		DBConnection objConn = new DBConnection();
		objConn.connectToLocal();
				
		Batch objBatch = new Batch(hospitalCode, objConn);
        
		ProcessAllocateManagementFeeBean objManagementFee;
		objManagementFee = new  ProcessAllocateManagementFeeBean(objBatch.getMm(), objBatch.getYyyy() , hospitalCode , paymentTerm);
		objManagementFee.setAmount(amount);
		
		// This action process
		if(objManagementFee.doProcess()){
			System.out.println("Management Fee Process complete.");
			objJson.put("status", "SUCCESS");
		} else  { 
			objJson.put("status" , "FAIL");
		}
		
		out.print(objJson.toJSONString());
		            
	}
	
}
