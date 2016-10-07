package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;

public class ReceiptHeader extends ABSTable{
    private String receiptNo = ""; 
    private String invoiceNo = "";
    private String hospitalCode = "";
    private String invoiceNoInside = "";
    private String receiptDate = "";
    private String invoiceDate = "";
    private String cashierLocationCode = "";
    private String titleName = "";
    private String firstName = "";
    private String lastName = "";
    private String hospitalNo = "";
    private String visitNo = "";
    private String admissionType = "";
    private Double amountBefDiscount = 0d;
    private Double amountOfDiscount = 0d;
    private String receiptModule = "";
    private String updateDate = "";
    private String updateTime = "";
    private String userId = "";
    private String ACTIVE = "1";

    public ReceiptHeader() {
        super();
    }
    public ReceiptHeader(DBConnection conn) {
        super();
        this.setDBConnection(conn);
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getInvoiceNoInside() {
        return invoiceNoInside;
    }

    public void setInvoiceNoInside(String invoiceNoInside) {
        this.invoiceNoInside = invoiceNoInside;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCashierLocationCode() {
        return cashierLocationCode;
    }

    public void setCashierLocationCode(String cashierLocationCode) {
        this.cashierLocationCode = cashierLocationCode;
    }

    public String getTitleName() {
        if (titleName == null) this.titleName = "";
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getFirstName() {
        if (firstName == null) this.firstName = "";
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        if (lastName == null) this.lastName = "";
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHospitalNo() {
        if (hospitalNo == null) { hospitalNo = ""; }
        return hospitalNo;
    }

    public void setHospitalNo(String hospitalNo) {
        this.hospitalNo = hospitalNo;
    }

    public String getVisitNo() {
        return visitNo;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }

    public String getAdmissionType() {
        return admissionType;
    }

    public void setAdmissionType(String admissionType) {
        this.admissionType = admissionType;
    }

    public Double getAmountBefDiscount() {
        return amountBefDiscount;
    }

    public void setAmountBefDiscount(Double amountBefDiscount) {
        this.amountBefDiscount = amountBefDiscount;
    }

    public Double getAmountOfDiscount() {
        return amountOfDiscount;
    }

    public void setAmountOfDiscount(Double amountOfDiscount) {
        this.amountOfDiscount = amountOfDiscount;
    }

    public String getReceiptModule() {
        return receiptModule;
    }

    public void setReceiptModule(String receiptModule) {
        this.receiptModule = receiptModule;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActive() {
        return ACTIVE;
    }

    public void setActive(String ACTIVE) {
        this.ACTIVE = ACTIVE;
    }
    
    
    
    public ReceiptHeader(String hospitalCode, String receiptNo, String receiptDate, String invoiceNo, DBConnection conn) {
        this.setDBConnection(conn);
        ResultSet rs = this.getDBConnection().executeQuery("select * from Receipt_Header where HOSPITAL_CODE='" + hospitalCode + "'" +
                                                    " and RECEIPT_NO = '" + receiptNo + "'" +
                                                    " and RECEIPT_DATE = '" + receiptDate + "'" +
                                                    " and INVOICE_NO = '" + invoiceNo + "'"); 

        try {
            while (rs.next()) {
                this.receiptNo = rs.getString("RECEIPT_NO");; 
                this.invoiceNo = rs.getString("INVOICE_NO");
                this.hospitalCode = rs.getString("HOSPITAL_CODE");
                this.invoiceNoInside = rs.getString("Invoice_No_Inside");
                this.receiptDate = rs.getString("RECEIPT_DATE");
                this.invoiceDate = rs.getString("Invoice_Date");
                this.cashierLocationCode = rs.getString("Cashier_Location_Code");
                this.titleName = rs.getString("TITLE_NAME");
                this.firstName = rs.getString("FIRST_NAME");
                this.lastName = rs.getString("LAST_NAME");
                this.hospitalNo = rs.getString("RT.");
                this.visitNo = rs.getString("Visit_No");
                this.admissionType = rs.getString("ADMISSION_TYPE_CODE");
                this.amountBefDiscount = rs.getDouble("AMOUNT_BEF_DISCOUNT");
                this.amountOfDiscount = rs.getDouble("AMOUNT_OF_DISCOUNT");
                this.receiptModule = rs.getString("Receipt_Module");
                this.updateDate = rs.getString("UPDATE_DATE");
                this.updateTime = rs.getString("Update_Time");
                this.userId = rs.getString("USER_ID");
                this.ACTIVE = rs.getString("ACTIVE");
            }
        } catch (SQLException e) {
            // TODO
            e.printStackTrace();
        } finally {
               //Clean up resources, close the connection.
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
     
    }
    
    
    public boolean rollBackUpdate(String hospitalCode, String yyyy, String mm, String dd) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "update receipt_header set UPDATE_DATE = '', UPDATE_TIME=''";
                sql1 = sql1 + " where RECEIPT_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    } 
    
    public boolean rollBackDelete(String hospitalCode, String yyyy, String mm, String dd) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "delete from receipt_header ";
                sql1 = sql1 + " where RECEIPT_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }        
    
    public boolean rollBackUpdateByDateAndDoctorCode(String hospitalCode, String sYYYY, String sMM, String sDD, String eYYYY, String eMM, String eDD) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "update receipt_header set UPDATE_DATE = '" + JDate.getDate() + "', UPDATE_TIME='" + JDate.getTime() + "'";
                sql1 = sql1 + " where (RECEIPT_DATE >= '" + sYYYY.concat(sMM).concat(sDD) + "'";
                sql1 = sql1 + " and RECEIPT_DATE <= '" + eYYYY.concat(eMM).concat(eDD) + "')";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
    
    
}
