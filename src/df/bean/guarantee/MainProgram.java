/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.guarantee;

import df.bean.db.conn.DBConnection;
import df.bean.interfacefile.ExportDFToBankBean_no_ciitbank;
import df.bean.interfacefile.ImportGuaranteeBean;
import df.bean.interfacefile.ImportTransactionResultBean;
import df.bean.interfacefile.ImportTransactionBean;
import df.bean.interfacefile.ImportNewMasterBean;
import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.interfacefile.ExportDFToAPBean;
import df.bean.interfacefile.ExportDFToPayrollBean;
import df.bean.report.GenerateReportBean;
import java.util.Calendar;

/**
 *
 * @author arxim
 */

public class MainProgram {
    public static void main(String[] arg){
        //Servlet Process
        //DBConnection conn = new DBConnection();
        //conn.connectToServer();
        //DBConn cdb = new DBConn(conn.getConnection());
        //DBConnection conn = new DBConnection();
        try {
            //cdb.setStatement();
        } catch (Exception ex) {
            //System.out.println(ex);
        }
        
        //String month = "12";
        //String year = "2008";
        //String hospital = "00001";
        
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int dow = cal.get(Calendar.DAY_OF_WEEK);
        int dom = cal.get(Calendar.DAY_OF_MONTH);
        int doy = cal.get(Calendar.DAY_OF_YEAR);
 
        System.out.println("Current Date: " + cal.getTime());
        System.out.println("Day: " + day);
        System.out.println("Month: " + month);
        System.out.println("Year: " + year);
        System.out.println("Day of Week: " + dow);
        System.out.println("Day of Month: " + dom);
        System.out.println("Day of Year: " + doy);

        
        //uaranteeNewPrepareBean gpb = new GuaranteeNewPrepareBean(cdb);
        //System.out.println("Result of prepare Guarantee = "+gpb.prepareProcess(month, year, hospital, null));
        
        //GENERATE FILE REPORT
        /*
        GenerateReportBean grb = new GenerateReportBean();
        grb.exportReportExcel("C:/ex.xls", "BankPayment.jasper", null, cdb);
        grb.exportReportPDF("C:/ex.pdf", "BankPayment.jasper", null, cdb);
        */
        //--------------------
        
        //TAX 402 PROCESS
        /*
        Tax402Bean tax402 = new Tax402Bean();
        if(tax402.setTerm(month, year, cdb)){
            System.out.println("\n"+tax402.processTax("DC-00287-00", hospital, month, year));
            System.out.println(tax402.getTaxNormal());
        }
        */
        //---------------

        //GUARANTEE PROCESS
        
        //Remove previous Data
        //GuaranteeRollbackBean gr = new GuaranteeRollbackBean(cdb);
        //System.out.println("Result of rollback Guarantee = "+gr.processRollback(month, year, hospital));
        //Prepare data Guarantee
        //GuaranteePrepareBean gpb = new GuaranteePrepareBean(cdb);
        //System.out.println("Result of prepare Guarantee = "+gpb.prepareProcess(month, year, hospital));
        //Summary Guarantee
        //GuaranteeSummaryBean gs = new GuaranteeSummaryBean(cdb);
        ///System.out.println("Result of Summary Guarantee = "+gs.summaryProcess(month, year,hospital));
        //System.out.println(gs.getMessage());
        //GuaranteeAbsorbAccuBean ga = new GuaranteeAbsorbAccuBean(cdb);
        //System.out.println("\nResult of Guarantee Absorb = "+ga.summaryAbsorbAccu(month, year,hospital));
        
        //--------------------------------------
        
        //INTERFACE FILE PROCESS EXPORT
        //ExportDFToBankBean edtb = new ExportDFToBankBean();
        //System.out.println(""+edtb.exportData("test","00001","04","2008","12", cdb, "null"));
        //ExportDFToAPBean edfap = new ExportDFToAPBean();
        //System.out.println(edfap.exportData("test", "201", null, "2008", "02", cdb));
        //ExportDFToPayrollBean edfp = new ExportDFToPayrollBean();
        //System.out.println(edfp.exportData("43", "201", null, "2008", "02", cdb));
        
        
        //INTERFACE FILE PROCESS IMPORT
        /*
        ImportTransactionBean it = new ImportTransactionBean();
        ImportTransactionResultBean idr = new ImportTransactionResultBean();
        ImportGuaranteeBean ig = new ImportGuaranteeBean();
        if(it.insertData("C:/DF00001.20081005",conn)){
            idr.insertData("C:/DFVerify00001.20081005",conn);
            ig.insertData("C:/DFGT001.20081005",conn);            
        }else{
            idr.insertData("C:/DFVerify00001.20081005",conn);
            ig.insertData("C:/DFGT001.20081005",conn);            
        }
        InsertNewMasterBean indr = new InsertNewMasterBean("20081005",cdb);
        //idr.insertData("/home/arxim/Desktop/DFVerify00001.20081005",conn);
        //it.insertData("/home/arxim/Desktop/DF00001.20081005",conn);
        System.out.println(indr.getMessage());
        System.out.println(it.getMessage());
        System.out.println(idr.getMessage());
        System.out.println(ig.getMessage());
        */
        //--------------------------------------
        
        //Implement in destroy method of servlet
        //conn.freeConnection();
        //cdb.closeStatement("null");
        //cdb.closeDB("null");
        //conn.Close();
        //conn = null;
        //--------------------------------------
    }
}
