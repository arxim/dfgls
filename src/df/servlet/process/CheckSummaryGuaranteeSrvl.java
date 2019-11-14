package df.servlet.process;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.obj.util.JDate;

/**
 * Servlet implementation class CheckSummaryGuaranteeSrvl
 */
public class CheckSummaryGuaranteeSrvl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckSummaryGuaranteeSrvl() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/xml; charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        request.setCharacterEncoding("UTF-8");
        
        String hospital_code = request.getSession().getAttribute("HOSPITAL_CODE").toString();
        String month = request.getParameter("MM");
        String year = request.getParameter("YYYY");
        Batch getBatch = null;
        System.out.println("TEST  "  +month+"   "+year);
        if(request.getParameter("FORM").equals("invoice") && request.getParameter("DATE_INPUT").toString().length()>4){
        	month = JDate.saveDate(request.getParameter("DATE_INPUT").toString()).substring(4, 6);
        	year = JDate.saveDate(request.getParameter("DATE_INPUT").toString()).substring(0, 4);
        }
        DBConn conn = null;
        DBConnection dbConn = null;
        String numAffRec = "NO";
        try {
        	conn = new DBConn();
        	conn.setStatement();
        	dbConn = new DBConnection();
        	dbConn.connectToLocal();
        	getBatch = new Batch(hospital_code, dbConn);
        	        	
        	String qCheck = "SELECT MM FROM SUMMARY_GUARANTEE WHERE HOSPITAL_CODE='"+ hospital_code +"' AND YYYY='"+ year +"' AND MM='"+ month +"' GROUP BY MM;";
        	String[][] result = conn.query(qCheck);
        	
        	String qCheckBatch = "SELECT MM FROM BATCH WHERE CLOSE_DATE <>'' AND HOSPITAL_CODE='"+ hospital_code +"' AND YYYY='"+ year +"' AND MM='"+ month +"' GROUP BY MM;";
        	String[][] resultBatch = conn.query(qCheckBatch);
        	
        	if(result.length > 0 || resultBatch.length >0 ){
        		//System.out.println("Is Process Guarantee Ready");
        		numAffRec = "YES";
        	}else{
        		if(year+month != getBatch.getBatchNo() ){
            		//System.out.println("Term Payment Does't Match In Month :"+year+month+" and Batch : "+getBatch.getBatchNo());
            		numAffRec = "NO";
        		}
        	}
        	dbConn.Close();
            //System.out.println(year+month+numAffRec);
            out.print("<RESULT><STATUS>" + numAffRec + "</STATUS></RESULT>");
        }
        catch (Exception  e) {
        	conn.closeDB("");
            e.printStackTrace(out);
        }
        finally {
        	conn.closeDB("");
            out.close();
        }        
    } 

}
