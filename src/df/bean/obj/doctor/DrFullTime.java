package df.bean.obj.doctor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.db.table.PaymentMode;
import df.bean.db.table.StpMethodAllocItem;
import df.bean.db.table.StepSharing;
import df.bean.db.table.SummaryMonthlyStep;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;
import df.bean.obj.variable.SummaryMonthlyVar;


public class DrFullTime extends CareProvider {
    List stepSharingList = null;
//    public static SummaryMonthlyVar smVar = null;
    public static SummaryMonthlyVar smVarStep = null;
    
    public DrFullTime() {
        super();
    }
    
    public DrFullTime(String doctorCode, String hospitalCode, DBConnection conn) {
        super(doctorCode, hospitalCode, conn);
    }
    
    @Override
    protected void finalize() {
        stepSharingList.clear();
        stepSharingList = null;
    }
    public void newStepSharing() {
        String Code = this.getDBConnection().executeQueryString("select DOCTOR_CATEGORY.DOCTOR_CLASS_CODE from DOCTOR,DOCTOR_CATEGORY where DOCTOR.CODE='" + this.getDoctorCode() + "' and " + "DOCTOR.DOCTOR_CATEGORY_CODE=DOCTOR_CATEGORY.CODE");
        String sqlCommand = "select * from STEP_SHARING where DOCTOR_CLASS_CODE='" + Code + "' order by SORDER";
        ResultSet rs = null;
        setStepSharingList(new ArrayList());
        Statement stmt = null;
        try {
            stmt = this.getDBConnection().getConnection().createStatement();
            rs = stmt.executeQuery(sqlCommand);
            while (rs.next()) {
                getStepSharingList().add(new StepSharing(rs.getString("Code"), rs.getString("DOCTOR_CLASS_CODE"), this.getDBConnection()));
            }    
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(),
                    this.getClass().getName(), "", e.getMessage());
        }
        rs = null;
        stmt = null;        
    }
    public List<StepSharing> getStepSharingList() {
        return stepSharingList;
    }
    public void setStepSharingList(List stepSharingList) {
        this.stepSharingList = stepSharingList;
    }
    @Override
    public boolean computePremium() {
        super.computePremium();
        if (!this.lExcludeTreatment.equals(StpMethodAllocItem.EXCLUDE_YES)) {
            this.lHpPremium = Double.parseDouble((new java.text.DecimalFormat("0.00")).format(this.lTotPremiumAmt));
            this.lDrPremium = 0.00;
        }
        return true;
    }
    public boolean computeTransMonthly() {
         return true;
     }
}


