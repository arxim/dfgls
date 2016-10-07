package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;

public class ReceiptDetail  extends ABSTable{

        private String hospitalCode = "";
        private String invoiceNo = "";
        private String invoiceNoInside = "";
        private String receiptNo = "";
        private String receiptDate = "";
        private String invoiceDate = "";
        private String lineNo = "";
        private String orderItemCode = "";
        private String doctorCode = "";
        private String departmentCode = "";
        private String locationCode = "";
        private Double amountBefDiscount = 0d;
        private Double amountOfDiscount = 0d;
        private Double percentOfDiscount = 0d;
        private Double amountAftDiscount = 0d;
        private String receiptTypeCode = "";
        private String receiptModule = "";
        private String billingSuffixNo = "";
        private String yyyy = "";
        private String mm = "";
        private String dd = "";
        private String batchNo = "";
        private String statusModify = "";
        private String updateDate = "";
        private String updateTime = "";
        private String userId = "";


    public ReceiptDetail() {
        super();
    }
    public ReceiptDetail(DBConnection conn) {
        super();
        this.setDBConnection(conn);
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceNoInside() {
        return invoiceNoInside;
    }

    public void setInvoiceNoInside(String invoiceNoInside) {
        this.invoiceNoInside = invoiceNoInside;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
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

    public String getBillingSubGroup() {
        return lineNo;
    }

    public void setBillingSubGroup(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getOrderItemCode() {
        return orderItemCode;
    }

    public void setOrderItemCode(String orderItemCode) {
        this.orderItemCode = orderItemCode;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getLocationCode() {
        if (locationCode == null) locationCode = "";
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
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

    public Double getPercentOfDiscount() {
        return percentOfDiscount;
    }

    public void setPercentOfDiscount(Double percentOfDiscount) {
        this.percentOfDiscount = percentOfDiscount;
    }

    public Double getAmountAftDiscount() {
        return amountAftDiscount;
    }

    public void setAmountAftDiscount(Double amountAftDiscount) {
        this.amountAftDiscount = amountAftDiscount;
    }

    public String getReceiptTypeCode() {
        return receiptTypeCode;
    }

    public void setReceiptTypeCode(String receiptTypeCode) {
        this.receiptTypeCode = receiptTypeCode;
    }

    public String getReceiptModule() {
        return receiptModule;
    }

    public void setReceiptModule(String receiptModule) {
        this.receiptModule = receiptModule;
    }

    public String getBillingSuffixNo() {
        return billingSuffixNo;
    }

    public void setBillingSuffixNo(String billingSuffixNo) {
        this.billingSuffixNo = billingSuffixNo;
    }

    public String getYyyy() {
        return yyyy;
    }

    public void setYyyy(String yyyy) {
        this.yyyy = yyyy;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getStatusModify() {
        return statusModify;
    }

    public void setStatusModify(String statusModify) {
        this.statusModify = statusModify;
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
    
    
    
    public ReceiptDetail(String hospitalCode, String receiptNo, String receiptDate, String invoiceNo, String lineNo, DBConnection conn) {
        this.setDBConnection(conn);
        ResultSet rs = this.getDBConnection().executeQuery("select * from RECEIPT_DETAIL where HOSPITAL_CODE='" + hospitalCode + "'" +
                                                    " and RECEIPT_NO = '" + receiptNo + "'" +
                                                    " and RECEIPT_DATE = '" + receiptDate + "'" + 
                                                    " and INVOICE_NO = '" + invoiceNo + "'" + 
                                                    " and Billing_Sub_Group_No = '" + lineNo + "'"); 

        try {
            while (rs.next()) {                
                this.hospitalCode = rs.getString("HOSPITAL_CODE");
                this.invoiceNo = rs.getString("INVOICE_NO");
                this.invoiceNoInside = rs.getString("Invoice_No_Inside");
                this.receiptNo = rs.getString("RECEIPT_NO");
                this.receiptDate = rs.getString("RECEIPT_DATE");
                this.invoiceDate = rs.getString("Invoice_Date");
                this.lineNo = rs.getString("billing_Sub_Group_No");
                this.orderItemCode = rs.getString("order_Item_Code");
                this.doctorCode = rs.getString("DOCTOR_CODE");
                this.departmentCode = rs.getString("department_Code");
                this.locationCode = rs.getString("LOCATION_CODE");
                this.amountBefDiscount = rs.getDouble("AMOUNT_BEF_DISCOUNT");
                this.amountOfDiscount = rs.getDouble("AMOUNT_OF_DISCOUNT");
                this.percentOfDiscount = rs.getDouble("percent_Of_Discount");
                this.amountAftDiscount = rs.getDouble("AMOUNT_AFT_DISCOUNT");
                this.receiptTypeCode = rs.getString("receipt_Type_Code");
                this.receiptModule = rs.getString("receipt_Module");
                this.billingSuffixNo = rs.getString("billing_Suffix_No");
                this.yyyy = rs.getString("YYYY");
                this.mm = rs.getString("MM");
                this.dd = rs.getString("DD");
                this.batchNo = rs.getString("BATCH_NO");
                this.statusModify = rs.getString("STATUS_MODIFY");
                this.updateDate = rs.getString("UPDATE_DATE");
                this.updateTime = rs.getString("Update_Time");
                this.userId = rs.getString("USER_ID");                
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
        String sql1 = "update RECEIPT_DETAIL set STATUS_MODIFY = 'R', UPDATE_DATE = '" + JDate.getDate() + "', UPDATE_TIME='" + JDate.getTime() + "', yyyy='', mm='', dd=''";
                sql1 = sql1 + " where RECEIPT_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
                sql1 = sql1 + " and yyyy='" + yyyy + "'";
                sql1 = sql1 + " AND MM='" + mm + "'";
                sql1 = sql1 + " and dd='" + dd + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
    
    public boolean rollBackDelete(String hospitalCode, String yyyy, String mm, String dd) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "delete from RECEIPT_DETAIL ";
                sql1 = sql1 + " where RECEIPT_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
    
    public boolean rollBackUpdateByDateAndDoctorCode(String hospitalCode, String sYYYY, String sMM, String sDD, String eYYYY, String eMM, String eDD, String doctorCode) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "update RECEIPT_DETAIL set STATUS_MODIFY = 'R', UPDATE_DATE = '" + JDate.getDate() + "', UPDATE_TIME='" + JDate.getTime() + "', yyyy='', mm='', dd=''";
                sql1 = sql1 + " where (RECEIPT_DATE >= '" + sYYYY.concat(sMM).concat(sDD) + "'";
                sql1 = sql1 + " and RECEIPT_DATE <= '" + eYYYY.concat(eMM).concat(eDD) + "')";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
                sql1 = sql1 + " and (yyyy >= '" + sYYYY + "'";
                sql1 = sql1 + " AND MM >= '" + sMM + "'";
                sql1 = sql1 + " and dd >= '" + sDD + "')";
                sql1 = sql1 + " and (yyyy <= '" + eYYYY + "'";
                sql1 = sql1 + " AND MM <= '" + eMM + "'";
                sql1 = sql1 + " and dd <= '" + eDD + "')";
                sql1 = sql1 + " and DOCTOR_CODE = '" + doctorCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
    
    
    
    
}
