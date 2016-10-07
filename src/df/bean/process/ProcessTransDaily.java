package df.bean.process;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.db.table.TRN_Error;
import df.bean.db.table.TrnDaily;
import df.bean.obj.Item.DrMethodAllocation;
import df.bean.obj.doctor.*;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;

public class ProcessTransDaily extends Process {   
    private DrMethodAllocation drMethodAllocation = null;
    String startDate = "";
    String endDate = "";
    private DoctorList drList = null;
    String batch = "";
    
    public ProcessTransDaily(DBConnection conn) {
        super(conn);
        this.setDBConnection(conn);
    }
    public ProcessTransDaily() {
        super();
    }
    public void setBatch(String b){
        this.batch = b;
    }

    public void setDrMedthodAlloction(DrMethodAllocation drMedthodAllocation) {
        this.drMethodAllocation = drMedthodAllocation;
    }
    
    // used     
    public boolean Calculate(String startDate, String endDate, String hospitalCode, String invoiceNo, String lineNo ) {
        boolean ret = false;
        String doctorCode = "";
        CareProvider careProvider = null;
        int iCount = 0;
        if (drMethodAllocation == null) { drMethodAllocation = new DrMethodAllocation(this.getDBConnection(), Variables.getHospitalCode()); }
        TrnDaily tDaily = new TrnDaily(this.getDBConnection());
            try {
                ret = true;
                String sql = TrnDaily.getSQL_TRN_DAILY(startDate, endDate, hospitalCode, invoiceNo, lineNo);
                tDaily.OpenResultSet(sql);

                while (tDaily.MoveNext()) {
                    doctorCode = tDaily.getDoctorCode();
                    careProvider = this.getDoctorList().getDoctor(doctorCode);
                    if (careProvider != null) {
                        careProvider.setTrnDaily(tDaily);
                        careProvider.setDrMethodAllocation(this.drMethodAllocation);
                        if (careProvider.computeTransDaily()) {   
                            iCount++;
                            ret = true;    } 
                        else {  
                            ret = false;
                            break;   
                        }
                    }
                }
                if (ret) { ret = tDaily.updateCompute(startDate, endDate, hospitalCode, invoiceNo, lineNo); }
            } catch (Exception e) {
                // TODO
                e.printStackTrace();
                TRN_Error.writeErrorLog(this.getConn().getConnection(), TRN_Error.PROCESS_DAILY, "", e.getMessage());
                ret=false;
            } finally {
               //Clean up resources, close the connection.
                try {
                    tDaily = null;
                } catch (Exception ex) { 
                    ex.printStackTrace();  
                    TRN_Error.writeErrorLog(this.getConn().getConnection(), TRN_Error.PROCESS_DAILY, "", ex.getMessage());
                    ret = false; 
                } 
            }

        return ret;
    }
}
