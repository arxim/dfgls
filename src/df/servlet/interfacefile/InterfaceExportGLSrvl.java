/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.servlet.interfacefile;

import df.bean.interfacefile.InterfaceExportGLSVNHBean;
import df.bean.interfacefile.InterfaceExportSAPGLBean;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author USER
 */
public class InterfaceExportGLSrvl extends HttpServlet {
   HttpSession session;
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        session = request.getSession(true);
        response.setContentType("text/xml; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        String TYPE = request.getParameter("TYPE");
        String Term = request.getParameter("TERM");
        String MM = request.getParameter("mm");
        String YYYY = request.getParameter("yyyy");
        String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();
        //InterfaceExportGLSVNHBean gl = new InterfaceExportGLSVNHBean(YYYY, MM, Term, hospital_code);
        InterfaceExportSAPGLBean gl = new InterfaceExportSAPGLBean(YYYY, MM, Term, hospital_code);
        System.out.println(TYPE);
        String countdata = "0";
        try {
            if("AC".equalsIgnoreCase(TYPE)){
            	countdata = gl.processAccu();
            }else{
            	countdata = gl.processGlAccount();
            }
            try {
                Thread.sleep(20);
                out.print("<RESULT><SUCCESS>" + countdata + "</SUCCESS></RESULT>");
            }catch (Exception  e) {
                out.print("<RESULT><SUCCESS>Error</SUCCESS></RESULT>");
                e.printStackTrace(out);
            }finally {
                out.close();
            }
        } finally { 
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
