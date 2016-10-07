package df.bean.db.table;

import java.sql.SQLException;
import df.bean.db.conn.DBConnection;

public class StepSharing extends ABSTable {

    private String code;
    private String description;
    private Double beginAmount;
    private Double endAmount;
    private Double drPercent;
    private Double hpPercent;
    private Integer SORDER;
    private String doctorClassCode;

    public StepSharing() {
        super();
    }

    public Double getBeginAmount() {
        return this.beginAmount;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDoctorClassCode() {
        return this.doctorClassCode;
    }

    public Double getDrPercent() {
        return this.drPercent;
    }

    public Double getEndAmount() {
        return this.endAmount;
    }

    public Double getHpPercent() {
        return this.hpPercent;
    }

    public Integer getSorder() {
        return this.SORDER;
    }

    public void setBeginAmount(Double beginAmount) {
        this.beginAmount = beginAmount;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDoctorClassCode(String doctorClassCode) {
        this.doctorClassCode = doctorClassCode;
    }

    public void setDrPercent(Double drPercent) {
        this.drPercent = drPercent;
    }

    public void setEndAmount(Double endAmount) {
        this.endAmount = endAmount;
    }

    public void setHpPercent(Double hpPercent) {
        this.hpPercent = hpPercent;
    }

    public void setSorder(Integer SORDER) {
        this.SORDER = SORDER;
    }
    
    public StepSharing(String code, String doctorClassCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from STEP_SHARING where CODE='" + code + "' and DOCTOR_CLASS_CODE='" + doctorClassCode + "'"));

        try {
            while (this.getResultSet().next()) {
                this.code = this.getResultSet().getString("Code");
                this.description = this.getResultSet().getString("Description");
                this.beginAmount = this.getResultSet().getDouble("Begin_Amount");
                this.endAmount = this.getResultSet().getDouble("End_Amount");
                this.drPercent = this.getResultSet().getDouble("Dr_Percent");
                this.hpPercent = this.getResultSet().getDouble("Hp_Percent");
                this.SORDER = this.getResultSet().getInt("SORDER");
                this.doctorClassCode = this.getResultSet().getString("DOCTOR_CLASS_CODE");
            }
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        } finally {
               //Clean up resources, close the connection.
               if(this.getResultSet() != null) {
                  try {
                     this.getResultSet().close();
                     this.setResultSet(null);
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
    }
    
}
