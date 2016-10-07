/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.servlet.service;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import df.bean.db.conn.DBConnection;
import df.jsp.Util;

/**
 *
 * @author Pong
 */
public class RetrieveData extends HttpServlet {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	DBConnection con;
    
    public void init() throws ServletException {
        super.init();
    }
    
    @Override
    public void destroy(){
        super.destroy();
        con.freeConnection();
    }
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


        //
        // Process request
        //
        
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("TABLE") == null) {
            out.print("<RECORDS></RECORDS>");
            return;
        }

        String str = "<RECORDS>";
        String table = request.getParameter("TABLE");
        String cond = request.getParameter("COND");
        
        try {
            //DBConnection con = new DBConnection();
            //con.connectToServer();
            con = new DBConnection();
            con.connectToLocal();
            
            String query = String.format("SELECT * FROM %1$s", table);
            if (cond != null) {
                query += " WHERE " + cond;
            }

            String[] listFields = con.getColumnNames(table);
            
            ResultSet rs = null;
            rs = con.executeQuery(query);
            while (rs != null && rs.next()) {
                str += "<" + table + ">";
                for (int i = 0; i < listFields.length; i++) {
                    //str += String.format("<%1$s>%2$s</%1$s>", listFields[i], rs.getString(listFields[i]).replace("&", "&amp;"));
                    str += String.format("<%1$s>%2$s</%1$s>", listFields[i], Util.formatHTMLString(rs.getString(listFields[i]), false));
                }
                str += "</" + table + ">";
            }

            if (rs != null) {
                rs.close();
            }
            //con.freeConnection();
        }
        catch (Exception e) {
            //e.printStackTrace(out);
        }
        str += "</RECORDS>";
        out.print(str);
        /*response.setContentType("text/html;charset=UTF-8");
        /*PrintWriter out = response.getWriter();
        try {
             TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RetrieveData</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RetrieveData at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
            
        } finally { 
            out.close();
        }
        */
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
    }
    // </editor-fold>
}
