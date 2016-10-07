package df.bean.db.table;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;

public class SummaryTax402 extends ABSTable {

    private String yyyy;
    private String mm;
    private String doctorCode;
    private Double positionAmt;
    private Double turnInAmt;
    private Double turnOutAmt;
    private Double guaranteeAmt;
    private Double otherAmt;
    private Double sumNormalTaxAmt;
    private Double sumTurnTaxAmt;
    private Double normalTaxMonth;
    private Double accuNormalTaxMonth;
    private Double hospitalTaxMonth;
    private Double accuHospitalTaxMonth;
    private Double netTaxMonth;
    private String textNetTaxMonth;
    private String hospitalCode;
    private String updateDate;
    private String updateTime;
    private String userID;
    private String active;
    private String lineNumber;

	public SummaryTax402(DBConnection conn) {
        super();
        this.setDBConnection(conn);
        this.setPositionAmt(0d);
        this.setTurnInAmt(0d);
        this.setTurnOutAmt(0d);
        this.setGuaranteeAmt(0d);
        this.setOtherAmt(0d);
        this.setSumNormalTaxAmt(0d);
        this.setSumTurnTaxAmt(0d);
        this.setNormalTaxMonth(0d);
        this.setAccuNormalTaxMonth(0d);
        this.setHospitalTaxMonth(0d);
        this.setAccuNormalTaxMonth(0d);
        this.setNetTaxMonth(0d);
        this.setHospitalCode("");
    }
	
    public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

    public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public Double getAccuHospitalTaxMonth() {
        return this.accuHospitalTaxMonth;
    }

    public Double getAccuNormalTaxMonth() {
        return this.accuNormalTaxMonth;
    }

    public String getDoctorCode() {
        return this.doctorCode;
    }

    public Double getGuaranteeAmt() {
        return this.guaranteeAmt;
    }

    public Double getHospitalTaxMonth() {
        return this.hospitalTaxMonth;
    }

    public String getMm() {
        return this.mm;
    }

    public Double getNetTaxMonth() {
        return this.netTaxMonth;
    }

    public Double getNormalTaxMonth() {
        return this.normalTaxMonth;
    }

    public Double getOtherAmt() {
        return this.otherAmt;
    }

    public Double getPositionAmt() {
        return this.positionAmt;
    }

    public Double getSumNormalTaxAmt() {
        return this.sumNormalTaxAmt;
    }

    public Double getSumTurnTaxAmt() {
        return this.sumTurnTaxAmt;
    }

    public String getTextNetTaxMonth() {
        return this.textNetTaxMonth;
    }

    public Double getTurnInAmt() {
        return this.turnInAmt;
    }

    public Double getTurnOutAmt() {
        return this.turnOutAmt;
    }

    public String getYyyy() {
        return this.yyyy;
    }

    public void setAccuHospitalTaxMonth(Double accuHospitalTaxMonth) {
        this.accuHospitalTaxMonth = accuHospitalTaxMonth;
    }

    public void setAccuNormalTaxMonth(Double accuNormalTaxMonth) {
        this.accuNormalTaxMonth = accuNormalTaxMonth;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public void setGuaranteeAmt(Double guaranteeAmt) {
        this.guaranteeAmt = guaranteeAmt;
    }

    public void setHospitalTaxMonth(Double hospitalTaxMonth) {
        this.hospitalTaxMonth = hospitalTaxMonth;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public void setNetTaxMonth(Double netTaxMonth) {
        this.netTaxMonth = netTaxMonth;
    }

    public void setNormalTaxMonth(Double normalTaxMonth) {
        this.normalTaxMonth = normalTaxMonth;
    }

    public void setOtherAmt(Double otherAmt) {
        this.otherAmt = otherAmt;
    }

    public void setPositionAmt(Double positionAmt) {
        this.positionAmt = positionAmt;
    }

    public void setSumNormalTaxAmt(Double sumNormalTaxAmt) {
        this.sumNormalTaxAmt = sumNormalTaxAmt;
    }

    public void setSumTurnTaxAmt(Double sumTurnTaxAmt) {
        this.sumTurnTaxAmt = sumTurnTaxAmt;
    }

    public void setTextNetTaxMonth(String textNetTaxMonth) {
        this.textNetTaxMonth = textNetTaxMonth;
    }

    public void setTurnInAmt(Double turnInAmt) {
        this.turnInAmt = turnInAmt;
    }

    public void setTurnOutAmt(Double turnOutAmt) {
        this.turnOutAmt = turnOutAmt;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }
    
    public boolean update() {
        boolean ret = false;
        String tableName = "SUMMARY_TAX_402";
//        try {
                // Prepare a statement to update a record
                String sql = "UPDATE " + tableName + " set SUM_NORMAL_TAX_AMT = '" + this.getSumNormalTaxAmt() + "'";
                sql = sql + " ,SUM_TURN_TAX_AMT = " + this.getSumTurnTaxAmt() + "";
                sql = sql + " ,NORMAL_TAX_MONTH = " + this.getNormalTaxMonth() + "";
                sql = sql + " ,ACCU_NORMAL_TAX_MONTH = " + this.getAccuNormalTaxMonth() + "";
                sql = sql + " ,HOSPITAL_TAX_MONTH = " + this.getHospitalTaxMonth() + "";
                sql = sql + " ,ACCU_HOSPITAL_TAX_MONTH = " + this.getAccuHospitalTaxMonth() + "";
                sql = sql + " ,NET_TAX_MONTH = " + this.getNetTaxMonth() + "";
                sql = sql + " ,TEXT_NET_TAX_MONTH = '" + this.getTextNetTaxMonth() + "'";
                sql = sql + " ,UPDATE_DATE = '" + JDate.getDate() + "'";
                sql = sql + " ,UPDATE_TIME = '" + JDate.getTime() + "'";
                sql = sql + " ,USER_ID = '" + this.getUserID() + "'";
                sql = sql + " ,STATUS_MODIFY = '" + Status.STATUS_CALCULATED + "'";
                sql = sql + " WHERE TAX_TERM ='" + this.getYyyy() + "'";
                //sql = sql + " WHERE YYYY='" + this.getYyyy() + "'";
                //sql = sql + " AND MM = '" + this.getMm() + "'";
                sql = sql + " AND DOCTOR_CODE='" + this.getDoctorCode() + "'";
                sql = sql + " AND HOSPITAL_CODE='" + this.getHospitalCode() + "'";
                sql = sql + " AND BATCH_NO is null";
                                    
                // Execute the insert statement
                int updateCount = this.getDBConnection().executeUpdate(sql);
//                int updateCount = this.getDBConnection().getStatementForInsert().executeUpdate(sql);
                // updateCount contains the number of updated rows
        
                if (updateCount > -1) { ret=true; }
/*            } catch (SQLException e) {
                e.printStackTrace();
                TRN_Error.writeErrorLog(this.getDBConnection().getConnection(),
                    this.getClass().getName(), "", e.getMessage());
            } */
        return ret;
    }
    public boolean insert() {
        boolean ret = false;
        // Prepare a statement to update a record
        String sql = "INSERT INTO SUMMARY_TAX_402 (YYYY,MM,TAX_TERM,DOCTOR_CODE,HOSPITAL_CODE,POSITION_AMT " +
        				",TURN_IN_AMT,TURN_OUT_AMT,GUARANTEE_AMT,OTHER_AMT,SUM_NORMAL_TAX_AMT " +
        				",SUM_TURN_TAX_AMT,NORMAL_TAX_MONTH,ACCU_NORMAL_TAX_MONTH,HOSPITAL_TAX_MONTH " +
        				",ACCU_HOSPITAL_TAX_MONTH,NET_TAX_MONTH,TEXT_NET_TAX_MONTH,BATCH_NO,UPDATE_DATE " +
        				",UPDATE_TIME,USER_ID,ACTIVE,LINE_NO) " +
        	 " VALUES (" +
        	 "'" + this.getYyyy() + "'" +
        	 ",'" + this.getMm() + "'" +
        	 ",'" + this.getYyyy() + "'" +
        	 ",'" + this.getDoctorCode() + "'" +
        	 ",'" + this.getHospitalCode() + "'" +
        	 "," + this.getPositionAmt() + 
        	 "," + this.getTurnInAmt() +
        	 "," + this.getTurnOutAmt() +
        	 "," + this.getGuaranteeAmt() +
        	 "," + this.getOtherAmt() +
        	 "," + this.getSumNormalTaxAmt() +
        	 "," + this.getSumTurnTaxAmt() +
        	 "," + this.getNormalTaxMonth() +
        	 "," + this.getAccuNormalTaxMonth() +
        	 "," + this.getHospitalTaxMonth() +
        	 "," + this.getAccuHospitalTaxMonth() +
        	 "," + this.getNetTaxMonth() +
        	 ",'" + this.getTextNetTaxMonth() + "'" +
        	 ",''" +
        	 ",'" + this.getUpdateDate() + "'" +
        	 ",'" + this.getUpdateTime() + "'" +
        	 ",'" + this.getUserID() + "'" +
        	 ",'" + this.getActive() + "'" +
        	 ",'" + this.getLineNumber() + "')";
                                    
        // Execute the insert statement
        int updateCount = this.getDBConnection().executeUpdate(sql);        
        if (updateCount > -1) { ret=true; }

        return ret;
    }
    
    
    
}
