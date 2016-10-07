package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;

public class Batch {

    private String batchNo;
    private String hospitalCode;
    private String yyyy;
    private String mm;
    private String createDate;
    private String createTime;
    private String closeDate;
    private String closeTime;
    private String paymentDate;
    private String createByUserId;
    private String closeByUserId;
    private ResultSet resultSet = null;
    private DBConnection con = null;

    public Batch() {
        super();
        this.setHospitalCode(hospitalCode);
    }

    public String getBatchNo() {
        return batchNo;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public String getCloseDate() {
        return this.closeDate;
    }

    public String getCloseTime() {
        return this.closeTime;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public String getMm() {
        return mm;
    }
    public String getYyyy() {
        return yyyy;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }

    public String getCreateByUserId() {
        return createByUserId;
    }

    public void setCreateByUserId(String createByUserId) {
        this.createByUserId = createByUserId;
    }

    public String getCloseByUserId() {
        return closeByUserId;
    }

    public void setCloseByUserId(String closeByUserId) {
        this.closeByUserId = closeByUserId;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public Batch(String hospitalCode, DBConnection conn) {
    	this.con = conn;
        this.resultSet = conn.executeQuery("select * from BATCH where HOSPITAL_CODE ='" + hospitalCode + "'" +
                                                " and (CLOSE_DATE is null or CLOSE_DATE = '')");
        
        try {
            while (this.resultSet.next()) {
                this.setBatchNo(this.resultSet.getString("BATCH_NO"));
                this.setCreateDate(this.resultSet.getString("CREATE_DATE"));
                this.setCreateTime(this.resultSet.getString("CREATE_TIME"));
                this.setCloseDate(this.resultSet.getString("CLOSE_DATE"));
                this.setCloseTime(this.resultSet.getString("CLOSE_TIME"));
                this.setHospitalCode(this.resultSet.getString("HOSPITAL_CODE"));
                this.setMm(this.resultSet.getString("MM"));
                this.setYyyy(this.resultSet.getString("YYYY"));
                this.setCreateByUserId(this.resultSet.getString("CREATE_BY_USER_ID"));
                this.setCloseByUserId(this.resultSet.getString("CLOSE_BY_USER_ID"));
                this.setPaymentDate(this.resultSet.getString("PAYMENT_DATE"));
            }
        } catch (SQLException e) {
            // TODO
        	System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
               //Clean up resources, close the connection.
               if(this.resultSet != null) {
                  try {
                     this.resultSet.close();
                     this.resultSet = null;
                  }catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
    }
    
    
    public boolean createBATCH() {
        boolean ret = false;
        try {
        	// Create an updatable result set
            String sql = 
            "INSERT INTO BATCH (BATCH_NO, CREATE_DATE, CREATE_TIME, CLOSE_DATE, " +
            "CLOSE_TIME, HOSPITAL_CODE, MM, YYYY, CREATE_BY_USER_ID, " +
            "CLOSE_BY_USER_ID, PAYMENT_DATE) VALUES ('" +
            JDate.getYear() + JDate.getMonth() + "','"+JDate.getDate() + "','" +
            JDate.getTime() + "','','','" + getHospitalCode() + "','" + JDate.getMonth() + "','" + 
            JDate.getYear() + "','" + this.getCloseByUserId() + "','','" + newBatch() + "')";
            
            int updateCount = this.con.getStatement().executeUpdate(sql);
            
            if (updateCount > -1) { ret=true; }
        
        } catch (SQLException e) {
            e.printStackTrace();
            TRN_Error.writeErrorLog(this.con.getConnection(),
                    this.getClass().getName(), "", e.getMessage());
        }
        return ret;
    }
    
    public boolean closeBATCH() {
        boolean ret = false;
        try {
            String tableName = "BATCH";
                // Prepare a statement to update a record
                String sql = "UPDATE " + tableName + " SET CLOSE_DATE='" + JDate.getDate() + "'" +
                             " , CLOSE_TIME='" + JDate.getTime() + "'" +
                             " , CLOSE_BY_USER_ID='" + this.getCloseByUserId() + "'" + 
                             " where HOSPITAL_CODE ='" + getHospitalCode() + "'" +
                             " and BATCH_NO in " +
                             "(select max(BATCH_NO) as bno from BATCH " +
                             " where HOSPITAL_CODE ='" + getHospitalCode() + "')";
                int updateCount = this.con.getStatement().executeUpdate(sql);
            if (updateCount > -1) { ret=true; }
        } catch (SQLException e) {
            e.printStackTrace();
            TRN_Error.writeErrorLog(this.con.getConnection(), this.getClass().getName(), "", e.getMessage());
        }
        return ret;
    }

    private String newBatch() {
    	
        String ret = "";
        int mm1 = Integer.parseInt(this.getMm());
        int yyyy1 =  Integer.parseInt(this.getYyyy());
        
        String sMM = "", sYYYY = "";
        
        mm1 = mm1 + 1;
       
        if (mm1 == 13) { 
        	mm1 = 1; 
        	yyyy1 = yyyy1 + 1; 
        }
        
        sMM = ""+mm1;
        sYYYY = ""+yyyy1;
        
        if (sMM.length() == 1) {
            sMM = "0" + sMM;
        }
        
        ret = sYYYY.concat(sMM.concat("10"));
        return ret;
        
    }
}
