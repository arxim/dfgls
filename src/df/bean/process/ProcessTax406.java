
package df.bean.process;

import df.bean.db.conn.DBConnection;
import df.bean.obj.doctor.CareProvider;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.JDate;


public class ProcessTax406 extends Process{
    
    public ProcessTax406(DBConnection conn) {
        super(conn);
        this.setDBConnection(conn);
    }

    @Override
    public boolean run(String YYYY, String MM, String hospitalCode) {
        if (this.getDBConnection().IsClosed()) {  
            if (!this.getDBConnection().Connect()) { DialogBox.ShowError(" Error "); return false;}
        }
        
//        if (drMethodAllocation == null) {
//            drMethodAllocation = new DrMethodAllocation(this.getDBConnection(), Variables.getHospitalCode());
//        }
        
        this.getDBConnection().beginTrans();
        
        result = this.Calculate(YYYY, MM, hospitalCode);
//        super.run();    // call super 
//        if (this.calculatorFrm.getLstDoctorGroupCode().getModel().getSize() == this.calculatorFrm.getLstDoctorGroupCode().getSelectedIndex()+1) {
            if (this.result) {
                this.getDBConnection().commitTrans();
//                if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
//                DialogBox.ShowInfo("      completed         ");
            } else {
                this.getDBConnection().rollBackTrans();
//                if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
//                DialogBox.ShowError("Error in ProcessTransDaily.Calculate !!!!! \nTransactions are rollback.");
            } 
//        } else { 
//            this.getDBConnection().rollBackTrans();
//            if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
//        } 
        
        return result;
    }
    
    
    @Override
    public boolean Calculate(String YYYY, String MM, String hospitalCode) {
        boolean ret = false;        
        String dd = "";
        CareProvider careProvider = null;
        int iCount = 0;
        ret = true;        
        
        if (MM.equals("06")) { dd = "30"; } else { dd = "31"; }
        
        for (int i=0; i<this.getDoctorList().getDoctorLst().size(); i++) {
            careProvider = this.getDoctorList().getDoctorIDX(i);
            if (careProvider != null) {
                if (careProvider.computeTax406(dd, MM, YYYY)) {
                    iCount++;
                    ret = true;    
                } 
                else {  return false;   }
            }
        }
        
        return ret;
    } 
    
    // Used here
    public boolean Calculate(String startDate, String endDate, String hospitalCode, String doctorCode, String MM, String YYYY) {
        boolean ret = false;        
        String dd = ""; //, MM = "";
        CareProvider careProvider = null;
        int iCount = 0;
        ret = true;   
//        MM = endDate.substring(0, 2);
        if (MM.equalsIgnoreCase("04") || MM.equalsIgnoreCase("06") || MM.equalsIgnoreCase("09") || MM.equalsIgnoreCase("11")) 
        { dd = "30"; } else { dd = "31"; }
        startDate = JDate.saveDate("01/" + startDate);
        endDate = JDate.saveDate(dd + "/" + endDate);
        System.out.println("Start Date : "+startDate+":"+endDate);
//        for (int i=0; i<this.getDoctorList().getDoctorLst().size(); i++) {
            careProvider = this.getDoctorList().getDoctor(doctorCode);
            if (careProvider != null) {
                if (careProvider.computeTax406(startDate, endDate, MM, YYYY, hospitalCode)) {
                    iCount++;
                    ret = true;    
                } 
                else {  return false;   }
            }
//        }
        
        return ret;
    } 
}
