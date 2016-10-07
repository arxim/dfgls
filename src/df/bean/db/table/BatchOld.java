package df.bean.db.table;

import java.sql.SQLException;
import java.sql.Statement;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;

public class BatchOld  extends ABSTable {

    static private String batchNo;
    static private String hospitalCode;
    static private String yyyy;
    static private String mm;
    private String createDate;
    private String createTime;
    private String closeDate;
    private String closeTime;
    static private String paymentDate;
    private String createByUserId;
    private String closeByUserId;

    public BatchOld() {
        super();
        this.setHospitalCode(hospitalCode);
    }

    static public String getBatchNo() {
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

    static public String getHospitalCode() {
        return hospitalCode;
    }

    static public String getMm() {
        return mm;
    }

    static public String getYyyy() {
        return yyyy;
    }

    @SuppressWarnings("static-access")
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    @SuppressWarnings("static-access")
    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    @SuppressWarnings("static-access")
    public void setMm(String mm) {
        this.mm = mm;
    }

    @SuppressWarnings("static-access")
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

    static public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        BatchOld.paymentDate = paymentDate;
    }
    
    public BatchOld(String hospitalCode, DBConnection conn) {
        this.setDBConnection(conn);
        this.setResultSet(this.getDBConnection().executeQuery("select * from BATCH where HOSPITAL_CODE ='" + hospitalCode + "'" +
                                                " and (CLOSE_DATE is null or CLOSE_DATE = '')"));
        
        
        try {
            while (this.getResultSet().next()) {
                this.setBatchNo(this.getResultSet().getString("BATCH_NO"));
                this.setCreateDate(this.getResultSet().getString("Create_Date"));
                this.setCreateTime(this.getResultSet().getString("Create_Time"));
                this.setCloseDate(this.getResultSet().getString("CLOSE_DATE"));
                this.setCloseTime(this.getResultSet().getString("CLOSE_TIME"));
                this.setHospitalCode(this.getResultSet().getString("HOSPITAL_CODE"));
                this.setMm(this.getResultSet().getString("MM"));
                this.setYyyy(this.getResultSet().getString("YYYY"));
                this.setCreateByUserId(this.getResultSet().getString("Create_By_User_Id"));
                this.setCloseByUserId(this.getResultSet().getString("CLOSE_BY_USER_ID"));
                this.setPaymentDate(this.getResultSet().getString("PAYMENT_DATE"));
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
    
    
    public boolean createBATCH() {
        boolean ret = false;
        Statement stmt = null;
        
        try {
            // Create an updatable result set
/*            String tableName = "BATCH";
            String[] ss = this.getDBConnection().getColumnNames(tableName);
            String s1 = this.getDBConnection().getColumnNamesLine(ss);
            stmt = this.getDBConnection().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = this.getDBConnection().executeQuery("SELECT " + s1 + " FROM " + tableName + " where MM = '0'");
                
            // Move cursor to the "insert row"
            rs.moveToInsertRow();
            
            // Set values for the new row.
            rs.updateString("BATCH_NO", JDate.getDate());
            rs.updateString("Create_Date", JDate.getDate());
            rs.updateString("Create_Time", JDate.getTime());
            rs.updateString("CLOSE_DATE", "");
            rs.updateString("CLOSE_TIME", "");
            rs.updateString("HOSPITAL_CODE", this.getHospitalCode());
            rs.updateString("MM", JDate.getMonth());
            rs.updateString("YYYY", JDate.getYear());
            rs.updateString("Create_By_User_Id", Variables.getUserID());
            rs.updateString("CLOSE_BY_USER_ID", "");

            // Insert the row
            rs.insertRow(); */
            String sql = "insert into BATCH (BATCH_NO,Create_date,Create_Time,CLOSE_DATE,CLOSE_TIME,HOSPITAL_CODE, MM, YYYY,Create_By_User_ID, CLOSE_BY_USER_ID, PAYMENT_DATE) values (";
            sql = sql + "'" + JDate.getYear() + JDate.getMonth() + "',";
            sql = sql + "'" + JDate.getDate() + "',";
            sql = sql + "'" + JDate.getTime() + "',";
            sql = sql + "'','',";
            sql = sql + "'" + getHospitalCode() + "',";
            sql = sql + "'" + JDate.getMonth() + "',";
            sql = sql + "'" + JDate.getYear() + "',";
            sql = sql + "'" + Variables.getUserID() + "',";
            sql = sql + "'', ";
            sql = sql + "'" + newBatch() + "')";
            int updateCount = this.getDBConnection().getStatement().executeUpdate(sql);
            if (updateCount > -1) { ret=true; }
        } catch (SQLException e) {
            e.printStackTrace();
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(),
                    this.getClass().getName(), "", e.getMessage());
        }
/*        finally {
            try {
                if (rs != null) { 
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                } 
            }
            catch (SQLException ex)  {
                System.out.println("A SQLException error has occured in BATCH.insert() \n" + ex.getMessage());
                ex.printStackTrace();
            } 
        } */
        return ret;
    }
    
    public boolean closeBATCH() {
        boolean ret = false;
        try {
            // Create an updatable result set
            String tableName = "BATCH";
//            String[] ss = this.getDBConnection().getColumnNames(tableName);
//            String s1 = this.getDBConnection().getColumnNamesLine(ss);
            // Create an updatable result set
//            Statement stmt = this.getDBConnection().getConnection().createStatement(
//                ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

//            this.getDBConnection().setStatement(this.getDBConnection().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE));
/*            ResultSet rs = stmt.executeQuery("select * from " + tableName + 
                                                " where HOSPITAL_CODE ='" + this.hospitalCode + "'" +
                                                " and BATCH_NO in " +
                                                "(select max(BATCH_NO) as bno from BATCH " +
                                                    " where HOSPITAL_CODE ='" + this.hospitalCode + "')");
            */
                // Prepare a statement to update a record
                String sql = "UPDATE " + tableName + " SET CLOSE_DATE='" + JDate.getDate() + "'" +
                                " , CLOSE_TIME='" + JDate.getTime() + "'" +
                                " , CLOSE_BY_USER_ID='" + Variables.getUserID() + "'" + 
                                " where HOSPITAL_CODE ='" + getHospitalCode() + "'" +
                                " and BATCH_NO in " +
                                "(select max(BATCH_NO) as bno from BATCH " +
                                    " where HOSPITAL_CODE ='" + getHospitalCode() + "')";
                                
                // Execute the insert statement
                int updateCount = this.getDBConnection().getStatement().executeUpdate(sql);
                // updateCount contains the number of updated rows


            // Move cursor to the "insert row"
/*            rs.first();
            
            // Set values for the new row.
//            rs.updateString("BATCH_NO", this.getBATCHNo());
//            rs.updateString("Begin_Date", this.getBeginDate());
//            rs.updateString("Begin_Time", this.getBeginTime());
            rs.updateString("CLOSE_DATE", JDate.getDate());
            rs.updateString("CLOSE_TIME", JDate.getTime());
//            rs.updateString("HOSPITAL_CODE", this.getHospitalCode());
//            rs.updateString("MM", this.getMm());
//            rs.updateString("YYYY", this.getYyyy());
//            rs.updateString("Create_By_User_Id", this.getCreateByUserId());
            rs.updateString("CLOSE_BY_USER_ID", this.getCloseByUserId());
            
            // Insert the row
            rs.updateRow();  
            rs = null; */
            if (updateCount > -1) { ret=true; }
        } catch (SQLException e) {
            e.printStackTrace();
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(),
                    this.getClass().getName(), "", e.getMessage());
        }
        return ret;
    }

    private String newBatch() {
        String ret = "";
        Double mm1 = Double.parseDouble(getMm());
        Double yyyy1 = Double.parseDouble(getYyyy());
        String sMM = "", sYYYY = "";
        mm1 = mm1 + 1;
        if (mm1 == 13d) { mm1 = 1d; yyyy1 = yyyy1 + 1d; }
        
        sMM = mm1.toString();
        sYYYY = yyyy1.toString();
        if (sMM.length() == 1) {
            sMM = "0" + sMM;
        }
        ret = sYYYY.concat(sMM.concat("10"));
        return ret;
    }
}
