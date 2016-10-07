/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.report;
import df.bean.db.conn.DBConn;
import java.util.*;
import net.sf.jasperreports.engine.*;
import java.net.URL;
import java.sql.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.export.JRXlsExporter;
/**
 * How to show font and output PDF format
 * import lib itext-1.3.1.jar from iReport Librarie
 * copy font to folder : build\web\WEB-INF\classes
 * @author nopphadon
 */
public class GenerateReportBean {
Statement stmt1;
    JRResultSetDataSource obj;
    ResultSet rs3;
    String i, j;
    String salarys, salarye, butValue1;
    String pdftext, csvtext, htmltext, xmltext, exceltext, wordtext, path;
    String error_message = "";
    DBConn cdb;
    String uploadPath;
   
    public String getErrMesg(){
        return this.error_message;
    }
    public void setPath(String s){
    	uploadPath=s;
    }
    public boolean exportReportPDF(String file_save, String reportFile, Map parameters) {
        cdb = new DBConn();
        boolean status = true;
        URL inFilename = null;

        try { 
            inFilename = new URL(reportFile);
            JasperReport jasperReport = ( JasperReport ) JRLoader.loadObject( inFilename );
            Connection jdbcConnection = cdb.getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jdbcConnection);
            JasperExportManager.exportReportToPdfFile(jasperPrint, uploadPath+file_save+".pdf");
        }catch(Exception e){
        	System.out.println("Test->"+e);
            error_message = e.getMessage();
            status = false;
        }finally{
            cdb.closeDB("Report PDF Export");
        }
        return status;
    }
    public boolean exportReportExcel(String file_save, String reportFile, Map parameters, DBConn conn) {
        boolean status = true;
        URL inFilename = null;

         try { 
             JRXlsExporter exporter = new JRXlsExporter();
             inFilename = new URL(reportFile);
             JasperReport jasperReport = ( JasperReport ) JRLoader.loadObject( inFilename );
             Connection jdbcConnection = conn.getConnection();
             JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jdbcConnection);
             exporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrint );
             exporter.setParameter( JRExporterParameter.OUTPUT_FILE_NAME, uploadPath+file_save+".xls");
             exporter.exportReport();
         }catch(Exception e){
            error_message = e.getMessage();
            status = false;
         }
        return status;
    }
}
