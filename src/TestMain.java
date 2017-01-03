import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.BankTMBMediaClearing;
import df.bean.db.table.Batch;
import df.bean.obj.doctor.DoctorList;
import df.bean.obj.util.DialogBox;
// import df.bean.obj.util.JDate;
import df.bean.obj.util.JNumber;
import df.bean.obj.util.Utils;
import df.bean.obj.util.Variables;
import df.bean.process.ProcessAllocateMonthlyExpenseBean;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import df.bean.process.ProcessBankTMBPaymentMonthly;
import df.bean.process.ProcessImport;
import df.bean.process.ProcessMaster;
import df.bean.process.ProcessReceipt;
import df.bean.process.ProcessRollBack;
import df.bean.process.ProcessSummaryMonthlyDF;
import df.bean.process.ProcessTax406;
import df.bean.process.ProcessTransDaily;
import df.bean.process.ProcessXrayManagementBean;
import df.bean.process.summary.Summary;
import df.bean.process.summary.SummaryCreater;

/**
 *
 * @author admin
 */
public class TestMain {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
    	Map<String, String> team1 = new HashMap<String, String>();
    	team1.put("HOSPITAL_CODE", "00001");
    	team1.put("INVOICE_NO", "INV0001");
    	System.out.println(team1.size()+">>"+team1);
    	//System.out.println(Utils.toThaiMoney("12345.66"));
    	//System.out.println(Double.parseDouble("9000000.00")+Double.parseDouble("15000000.000"));
    	//System.out.println(JNumber.showDouble(Double.parseDouble("9000000.00")+Double.parseDouble("15000000.00"),2));
    	//Double t = (Double.parseDouble("140000")*
		//		(((Double.parseDouble(JDate.getEndMonthDate("2013", "09")))-((Double.parseDouble("20130916".substring(6, 8)))-1))*100)
		//		/Double.parseDouble(JDate.getEndMonthDate("2013", "09"))/100);
    	//System.out.println(((Double.parseDouble("20130916".substring(6, 8)))-1)+"<>"+t);
    	//ProcessXrayManagementBean p = new ProcessXrayManagementBean("011","2013","11");
    	//p.doProcess();
    	/*
    	DBConn d = new DBConn();
    	HashMap<String,String> hm = new HashMap<String,String>();
        ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>();
    	try { d.setStatement(); } catch (SQLException e) {}
		al = d.getMultiData("SELECT * FROM TRN_DAILY WHERE INVOICE_NO = '01-CI13002270'");
		for(int i = 0; i < al.size(); i++){
			hm = al.get(i);
			al.get(i).put("LINE_NO", hm.get("LINE_NO")+"G");			
		}

		System.out.println(d.addData(al, "TRN_DAILY"));
		*/
    	//ProcessSummaryMonthlyDF pm; 
    	//pm = new ProcessSummaryMonthlyDF("011", "25/06/2013", "", "");
    	//pm.doRollback();
    	//pm.doProcess();
    	/*
    	System.out.println(""+JNumber.getSaveMoney(1600*85/100));
		*/
    	//hm = al.get(1);
		//hm.put("INVOICE_NO", "1");
		//System.out.println(al.size());
		//System.out.println(hm.get("LINE_NO"));
    	//System.out.println(""+JDate.GetDiffMonth(10,2013,06,2013));

    	//System.out.println(JNumber.getShowMoney(120450.3554));
    	//System.out.println(JNumber.getSaveMoney("120,450.35"));
    	//System.out.println(JNumber.getSaveMoney(450.5524));
        // TODO code application logic here
        //DBConnection conn = new DBConnection();
        //conn.connectToLocal();
        //Variables.setHospitalCode("00001");
        //Variables.setUserID("sys");
        //Variables.setPassword("321");
        //Batch batch = new Batch(Variables.getHospitalCode(), conn);        
        //String startTime = JDate.getTime();

        //Compute Daily
        //System.out.println("Start Daily Calculate");
        //DoctorList drList = new DoctorList(Variables.getHospitalCode(), conn);
        //drList.newDoctorForComputeDaily("20120131", "20120131", "00001");
        //System.out.println("List");

        //ProcessTransDaily pd = new ProcessTransDaily(conn);
        //System.out.println("New Process");

        //pd.setDoctorList(drList);
        //System.out.println("Set List");

        //System.out.println(pd.Calculate("20120131", "20120131", "00001", "01-CI12001736", "10334915/3") ? "Success" : "Fail");
        //pd = null; 
        //drList = null;

        // Rollback ComputeDaily
//        ProcessRollBack rb = new ProcessRollBack(conn);
//        if (rb.rollBackTrnDaily(Variables.getHospitalCode(), "20080201", "20080232")) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");

        
        // Update Receipt
//        ProcessReceipt pr = new ProcessReceipt(conn);
//        if (pr.run("2008", "02", Variables.getHospitalCode())) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
//        pr = null;
        
        // Rollback Receipt AR
//        ProcessRollBack rb = new ProcessRollBack(conn);
//        if (rb.rollBackReceiptByAR(Variables.getHospitalCode(), "2008", "02")) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
                
        //         Rollback Receipt By Payor
//        ProcessRollBack rb = new ProcessRollBack(conn);
//        if (rb.rollBackReceiptByPayor(Variables.getHospitalCode(), "2008", "02")) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
        //         Rollback Receipt
//        ProcessRollBack rb = new ProcessRollBack(conn);
//        if (rb.rollBackReceiptByDoctor(Variables.getHospitalCode(), "2008", "02")) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
        
        
        // Summary Monthly
//        drList.newAllDoctor (Variables.getHospitalCode());
//        ProcessTransMonthly pm = new ProcessTransMonthly(conn);
//        pm.setDoctorList(drList);
//        if (pm.run("2008", "02", Variables.getHospitalCode())) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
//        pm = null;
        
//         Rollback Summary Monthly
//        ProcessRollBack rb = new ProcessRollBack(conn);
//        if (rb.rollBackSummaryMonthly(Variables.getHospitalCode(), "2008", "02")) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
        
        
        // Compute Payment monthly for DF
        /*
        ProcessPaymentMonthly ppm = new ProcessPaymentMonthly(conn);
        if (ppm.run("2008", "02", Variables.getHospitalCode())) {
            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
        }else DialogBox.ShowInfo("Fail!!!");
        ppm = null;
        */
        
        // Rollback Payment Monthly for DF
//        ProcessRollBack rb = new ProcessRollBack(conn);
//        if (rb.rollBackPaymentMonthly(Variables.getHospitalCode(), "2008", "02", PaymentMonthly.PAYMENT_TYPE_DF)) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
          
        // Export to Bank for DF
//        ProcessBankTMBPaymentMonthly pb = new ProcessBankTMBPaymentMonthly(conn);
//        if (pb.run("2008", "02", "20080310", PaymentMonthly.PAYMENT_TYPE_DF, Variables.getHospitalCode())) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
//        pb = null;
        // Rollback Export to Bank for DF
//        ProcessRollBack rb = new ProcessRollBack(conn);
//        if (rb.rollBackMediaClearing(Variables.getHospitalCode(), "2008", "02", BankTMBMediaClearing.SERVICE_TYPE_PAYMENT)) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
        
          
        // Compute Summary Tax406
//        drList.newAllDoctor(Variables.getHospitalCode());
//        ProcessTax406 pt = new ProcessTax406(conn);
//        pt.setDoctorList(drList);
//        if (pt.run("2008", "06", Variables.getHospitalCode())) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
//        pt = null;
        
        // Rollback Summary Tax406
//        ProcessRollBack rb = new ProcessRollBack(conn);
//        if (rb.rollBackSummaryTax406(Variables.getHospitalCode(), "2008", "06")) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
        
        // Compute Payment monthly for Salary
//        ProcessPaymentMonthly ppm1 = new ProcessPaymentMonthly(conn);
//        if (ppm1.runForSalary("20080225", Variables.getHospitalCode())) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
//        ppm1 = null;
        // Rollback Payment monthly for Salary
//        ProcessRollBack rb = new ProcessRollBack(conn);
//        if (rb.rollBackPaymentMonthly(Variables.getHospitalCode(), "2008", "02", PaymentMonthly.PAYMENT_TYPE_SALARY)) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");  
        
        
//        // Export to Bank for Salary
//        ProcessBankTMBPaymentMonthly pb1 = new ProcessBankTMBPaymentMonthly(conn);
//        if (pb1.run("2008", "02", "20080226", PaymentMonthly.PAYMENT_TYPE_SALARY, Variables.getHospitalCode())) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
//        pb1 = null;
        // Rollback Export to Bank for Salary
//        ProcessRollBack rb = new ProcessRollBack(conn);
//        if (rb.rollBackMediaClearing(Variables.getHospitalCode(), "2008", "02", BankTMBMediaClearing.SERVICE_TYPE_PAYROLL)) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
        
        
        //////// Import ///////////
//        ProcessImport pi = new ProcessImport(conn);
//        ProcessReceipt pr = new ProcessReceipt(conn);
//        if (pi.importBill(Variables.getHospitalCode(), "20081005", "20081005") && 
//                pr.CalculateReceiptByCash(Batch.getYyyy(), Batch.getMm(), "20081005", "20081005", "201")) {
//        if (pi.importVerifyInMonth(Variables.getHospitalCode(), "20081005", "20081005")) {
//        if (pi.importVerifyRecOverMonth(Variables.getHospitalCode(), "20081005", "20081005")) {
//        if (pi.importVerifyNotRec(Variables.getHospitalCode(), "20081005", "20081005")) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");
        
        // Rollback Export to Bank for Salary
//        ProcessRollBack rb = new ProcessRollBack(conn);
//        if (rb.rollBackImportBill(Variables.getHospitalCode(), "20081005", "20081005") && 
//                rb.rollBackImportVerify(Variables.getHospitalCode(), "20081005", "20081005")) {
//            DialogBox.ShowInfo("Success!!!" + startTime + " ---> " + JDate.getTime());
//        }else DialogBox.ShowInfo("Fail!!!");

        //batch = null;
        //if (conn.IsOpened()) { conn.Close(); }
    	
    	
    	// Test Monthly summary
    	
//    	SummaryCreater create = new SummaryCreater("011", "2014", "01", "2" , "20140210");
//    	Summary  summary  = create.createSummary("monthly");
//    	summary.setRevenueType("all");
//    	summary.getData();
    	
    	
    	
//    	ProcessMaster expProcess =  new  ProcessAllocateMonthlyExpenseBean("011", "2014", "01", "2");
//    	expProcess.doProcess();
    	
    }
}
