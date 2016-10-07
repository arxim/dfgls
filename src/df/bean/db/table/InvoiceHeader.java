package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;


import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;

public class InvoiceHeader extends ABSTable {

    private String invoiceNo;
    private String invoiceDate;
    private String titleName;
    private String firstName;
    private String lastName;
    private String hospitalNo;
    private String visitNo;
    private String building;
    private String floor;
    private String coPayment;
    private Double amountBefDiscount;
    private Double amountOfDiscount;
    private Double percentOfDiscount;
    private Double amountAftDiscount;
    private String hospitalCode;
    private String admissionTypeCode;
    private String locationCode;
    private String payorOfficeCode;
    private String payorName;
    private String transactionDate;
    private String transactionTime;
    private String userID;
    private String updateDate;
    private String updateTime;
    private String ACTIVE;
    private String statusModify;
    private String batchNo;
    private String invoiceType;

    public InvoiceHeader(DBConnection conn) {
        super();
        this.setDBConnection(conn);
    }

    public String getAdmissionTypeCode() {
        return this.admissionTypeCode;
    }

    public Double getAmountAftDiscount() {
        return this.amountAftDiscount;
    }

    public Double getAmountBefDiscount() {
        return this.amountBefDiscount;
    }

    public Double getAmountOfDiscount() {
        return this.amountOfDiscount;
    }

    public String getBuilding() {
        if (this.building == null) { 
            this.building = ""; 
        }
        return this.building;
    }

    public String getCoPayment() {
        if (this.coPayment == null) { 
            this.coPayment = ""; 
        }
        return this.coPayment;
    }

    public String getFirstName() {
        if (this.firstName == null) {
            this.firstName = "";
        }
        return this.firstName;
    }

    public String getFloor() {
        if (this.floor == null) { 
            this.floor = ""; 
        }
        return this.floor;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public String getHospitalNo() {
        return this.hospitalNo;
    }

    public String getInvoiceDate() {
        return this.invoiceDate;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public String getLastName() {
        if (this.lastName == null) {
            this.lastName = "";
        }
        return this.lastName;
    }

    public String getLocationCode() {
        if (this.locationCode == null) {
            this.locationCode = "";
        }
        return this.locationCode;
    }

    public String getPayorName() {
        if (this.payorName == null) {
            this.payorName = "";
        }
        return this.payorName;
    }

    public String getPayorOfficeCode() {
        if (this.payorOfficeCode == null) {
            this.payorOfficeCode = "";
        }
        return this.payorOfficeCode;
    }

    public Double getPercentOfDiscount() {
        return this.percentOfDiscount;
    }

    public String getTitleName() {
        if (this.titleName == null) {
            this.titleName = "";
        }
        return this.titleName;
    }

    public String getVisitNo() {
        if (this.visitNo == null) {
            this.visitNo = "";
        }
        return this.visitNo;
    }

    public void setAdmissionTypeCode(String admissionTypeCode) {
        this.admissionTypeCode = admissionTypeCode;
    }

    public void setAmountAftDiscount(Double amountAftDiscount) {
        this.amountAftDiscount = amountAftDiscount;
    }

    public void setAmountBefDiscount(Double amountBefDiscount) {
        this.amountBefDiscount = amountBefDiscount;
    }

    public void setAmountOfDiscount(Double amountOfDiscount) {
        this.amountOfDiscount = amountOfDiscount;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setCoPayment(String coPayment) {
        this.coPayment = coPayment;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setHospitalNo(String hospitalNo) {
        this.hospitalNo = hospitalNo;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public void setPayorName(String payorName) {
        this.payorName = payorName;
    }

    public void setPayorOfficeCode(String payorOfficeCode) {
        this.payorOfficeCode = payorOfficeCode;
    }

    public void setPercentOfDiscount(Double percentOfDiscount) {
        this.percentOfDiscount = percentOfDiscount;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }
    
    public InvoiceHeader(String invoiceNo, String hospitalCode, DBConnection conn) {
        this.setDBConnection(conn);
        ResultSet rs = this.getDBConnection().executeQuery("select * from Invoice_Header where INVOICE_NO='" + invoiceNo + "' and HOSPITAL_CODE='" + hospitalCode + "'");

        try {
            while (rs.next()) {
                this.invoiceNo = rs.getString("INVOICE_NO");
                this.invoiceDate = rs.getString("Invoice_Date");
                this.titleName = rs.getString("TITLE_NAME");
                this.firstName = rs.getString("FIRST_NAME");
                this.lastName = rs.getString("LAST_NAME");
                this.hospitalNo = rs.getString("RT.");
                this.visitNo = rs.getString("Visit_No");
                this.building = rs.getString("Building");
                this.floor = rs.getString("Floor");
                this.coPayment = rs.getString("Co_Payment");
                this.amountBefDiscount = rs.getDouble("AMOUNT_BEF_DISCOUNT");
                this.amountOfDiscount = rs.getDouble("AMOUNT_OF_DISCOUNT");
                this.percentOfDiscount = rs.getDouble("Percent_Of_Discount");
                this.amountAftDiscount = rs.getDouble("AMOUNT_AFT_DISCOUNT");
                this.hospitalCode = rs.getString("HOSPITAL_CODE");
                this.admissionTypeCode = rs.getString("ADMISSION_TYPE_CODE");
                this.locationCode = rs.getString("LOCATION_CODE");
                this.payorOfficeCode = rs.getString("Payor_Office_Code");
                this.payorName = rs.getString("Payor_Name");
                this.transactionDate = rs.getString("Transaction_Date");
                this.setTransactionTime(rs.getString("Transaction_Time"));
                this.setUserID(rs.getString("USER_ID"));
                this.setUpdateDate(rs.getString("UPDATE_DATE"));
                this.setUpdateTime(rs.getString("Update_Time"));
                this.setActive(rs.getString("ACTIVE"));
                this.setStatusModify(rs.getString("STATUS_MODIFY"));
                this.setBatchNo(rs.getString("BATCH_NO"));
                this.setInvoiceType(rs.getString("INVOICE_TYPE"));
            }
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        }
        finally {
          try {
            if (rs != null) { 
                rs.close();
                rs = null;
            }
          }
          catch (SQLException ex)  {
            System.out.println("A SQLException error has occured in InvoiceHeader.InvoiceHeader() \n" + ex.getMessage());
            ex.printStackTrace();
          }
        }
    }
    
    public boolean insert() {
        boolean ret = false;
        ResultSet rs = null;
        Statement stmt = null;
        try {
                // Create an updatable result set
                String[] ss = this.getDBConnection().getColumnNames("Invoice_Header");
                String s1 = this.getDBConnection().getColumnNamesLine(ss);
                stmt = this.getDBConnection().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rs = stmt.executeQuery("SELECT " + s1 + " FROM Invoice_Header");

                // Move cursor to the "insert row"
                rs.moveToInsertRow();
            
                // Set values for the new row.
                rs.updateString("INVOICE_NO", this.getInvoiceNo());
                rs.updateString("invoice_Date", this.getInvoiceDate());
                rs.updateString("TITLE_NAME", this.getTitleName());
                rs.updateString("FIRST_NAME", this.getFirstName());
                rs.updateString("LAST_NAME", this.getLastName()); 
                rs.updateString("RT.", this.getHospitalNo());
                rs.updateString("visit_No", this.getVisitNo());
                rs.updateString("building", this.getBuilding());
                rs.updateString("floor", this.getFloor());
                rs.updateString("co_Payment", this.getCoPayment());
                rs.updateDouble("AMOUNT_BEF_DISCOUNT", this.getAmountBefDiscount());
                rs.updateDouble("AMOUNT_OF_DISCOUNT", this.getAmountBefDiscount());
                rs.updateDouble("percent_Of_Discount", this.getPercentOfDiscount());
                rs.updateDouble("AMOUNT_AFT_DISCOUNT", this.getAmountAftDiscount());
                rs.updateString("hospital_Code",this.getHospitalCode());
                rs.updateString("ADMISSION_TYPE_CODE", this.getAdmissionTypeCode());
                rs.updateString("LOCATION_CODE", this.getLocationCode());
                rs.updateString("payor_Office_Code", this.getPayorOfficeCode());
                rs.updateString("payor_Name", this.getPayorName());
                rs.updateString("TRANSACTION_DATE", this.getTransactionDate());
                rs.updateString("Transaction_Time", this.getTransactionTime());
                rs.updateString("USER_ID", this.getUserID());
                rs.updateString("UPDATE_DATE", this.getUpdateDate());
                rs.updateString("Update_Time", this.getUpdateTime());
                rs.updateString("ACTIVE", this.getActive());
                rs.updateString("STATUS_MODIFY", this.getStatusModify());
                rs.updateString("BATCH_NO", this.getBatchNo());
                rs.updateString("INVOICE_TYPE", this.getInvoiceType());
                
                // Insert the row
                rs.insertRow();
                ret = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
            finally {
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
                System.out.println("A SQLException error has occured in InvoiceHeader.insert() \n" + ex.getMessage());
                ex.printStackTrace();
              }
            }

        return ret;

    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getActive() {
        return ACTIVE;
    }

    public void setActive(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }

    public String getStatusModify() {
        return statusModify;
    }

    public void setStatusModify(String statusModify) {
        this.statusModify = statusModify;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    
    public boolean rollBackUpdate(String hospitalCode, String yyyy, String mm, String dd) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "update invoice_header set STATUS_MODIFY = 'R', UPDATE_DATE = '', UPDATE_TIME=''";
                sql1 = sql1 + " where INVOICE_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    } 
    
    public boolean rollBackDelete(String hospitalCode, String yyyy, String mm, String dd) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "delete from invoice_header ";
                sql1 = sql1 + " where INVOICE_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }        
    
    public boolean rollBackUpdateByDateAndDoctorCode(String hospitalCode, String sYYYY, String sMM, String sDD, String eYYYY, String eMM, String eDD) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "update invoice_header set STATUS_MODIFY = 'R', UPDATE_DATE = '" + JDate.getDate() + "', UPDATE_TIME='" + JDate.getTime() + "'";
                sql1 = sql1 + " where (INVOICE_DATE >= '" + sYYYY.concat(sMM).concat(sDD) + "'";
                sql1 = sql1 + " and INVOICE_DATE <= '" + eYYYY.concat(eMM).concat(eDD) + "')";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }
}
