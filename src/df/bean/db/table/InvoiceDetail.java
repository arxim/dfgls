package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;

//import java.util.Date;

import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;

public class InvoiceDetail extends ABSTable {

    private String lineNo;
    private String orderDate;
    private String orderTime;
    private String doctorCureCode;
    private String updateCureDate;
    private String updateCureTime;
    private String doctorOrderCode;
    private String updateOrderDate;
    private String updateOrderTime;
    private String doctorResultCode;
    private String updateResultDate;
    private String updateResultTime;
    private String orderNote;
    private String departmentGroupCode;
    private String locationCode;
    private Double amountBefDiscount;
    private Double amountOfDiscount;
    private Double percentOfDiscount;
    private Double amountAftDiscount;
    private String invoiceNo;
    private String hospitalCode;
    private String orderItemCode;
    private String invoiceDate;
    private String transactionDate;
    private String transactionTime;
    private String ACTIVE;
    private String statusModify;
    private String batchNo;
    private String updateDate;
    private String updateTime;
    private String oldDoctorCode;
    private String billingSuffixNo;
    
    public InvoiceDetail(DBConnection conn) {
        super();
        this.setDBConnection(conn);
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

    public String getlineNo() {
        return this.lineNo;
    }

    public String getDepartmentGroupCode() {
        return this.departmentGroupCode;
    }

    public String getDoctorCureCode() {
        return this.doctorCureCode;
    }

    public String getDoctorOrderCode() {
        return this.doctorOrderCode;
    }

    public String getDoctorResultCode() {
        return this.doctorResultCode;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public String getLocationCode() {
        return this.locationCode;
    }

    public String getOrderDate() {
        return this.orderDate;
    }

    public String getOrderItemCode() {
        return this.orderItemCode;
    }

    public String getOrderNote() {
        return this.orderNote;
    }

    public String getOrderTime() {
        return this.orderTime;
    }

    public Double getPercentOfDiscount() {
        return this.percentOfDiscount;
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public String getUpdateOrderDate() {
        return this.updateOrderDate;
    }

    public String getUpdateOrderTime() {
        return this.updateOrderTime;
    }

    public String getUpdateResultDate() {
        return this.updateResultDate;
    }

    public String getUpdateResultTime() {
        return this.updateResultTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
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

    public void setlineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public void setDepartmentGroupCode(String departmentGroupCode) {
        this.departmentGroupCode = departmentGroupCode;
    }

    public void setDoctorCureCode(String doctorCureCode) {
        this.doctorCureCode = doctorCureCode;
    }

    public void setDoctorOrderCode(String doctorOrderCode) {
        this.doctorOrderCode = doctorOrderCode;
    }

    public void setDoctorResultCode(String doctorResultCode) {
        this.doctorResultCode = doctorResultCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setOrderItemCode(String orderItemCode) {
        this.orderItemCode = orderItemCode;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public void setPercentOfDiscount(Double percentOfDiscount) {
        this.percentOfDiscount = percentOfDiscount;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setUpdateOrderDate(String updateOrderDate) {
        this.updateOrderDate = updateOrderDate;
    }

    public void setUpdateOrderTime(String updateOrderTime) {
        this.updateOrderTime = updateOrderTime;
    }

    public void setUpdateResultDate(String updateResultDate) {
        this.updateResultDate = updateResultDate;
    }

    public void setUpdateResultTime(String updateResultTime) {
        this.updateResultTime = updateResultTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public InvoiceDetail(String hospitalCode, String invoiceNo, String orderItemCode, String lineNo, DBConnection conn) {
        this.setDBConnection(conn);
        ResultSet rs = this.getDBConnection().executeQuery("select * from INVOICE_DETAIL where Billing_Sub_Group_No='" + lineNo + "'"+
                                                    " and hospital_Code='"+hospitalCode+"'"+
                                                    " and ORDER_ITEM_CODE='"+orderItemCode+"'"+
                                                    " and INVOICE_NO='" + invoiceNo + "'");

        try {
            while (rs.next()) {
                this.lineNo = rs.getString("Billing_Sub_Group_No");
                this.orderDate = rs.getString("Order_Date");
                this.orderTime = rs.getString("Order_Time");
                this.doctorCureCode = rs.getString("Doctor_Cure_Code");
                this.setUpdateCureDate(rs.getString("Update_Cure_Date"));
                this.setUpdateCureTime(rs.getString("Update_Cure_Time"));
                this.doctorOrderCode = rs.getString("Doctor_Order_Code");
                this.updateOrderDate = rs.getString("Update_Order_Date");
                this.updateOrderTime = rs.getString("Update_Order_Time");
                this.doctorResultCode = rs.getString("Doctor_Result_Code");
                this.updateResultDate = rs.getString("Update_Result_Date");
                this.updateResultTime = rs.getString("Update_Result_Time");
                this.orderNote = rs.getString("Order_Note");
                this.departmentGroupCode = rs.getString("Department_Group_Code");
                this.locationCode = rs.getString("LOCATION_CODE");
                this.amountBefDiscount = rs.getDouble("AMOUNT_BEF_DISCOUNT");
                this.amountOfDiscount = rs.getDouble("AMOUNT_OF_DISCOUNT");
                this.percentOfDiscount = rs.getDouble("Percent_Of_Discount");
                this.amountAftDiscount = rs.getDouble("AMOUNT_AFT_DISCOUNT");
                this.invoiceNo = rs.getString("INVOICE_NO");
                this.hospitalCode = rs.getString("HOSPITAL_CODE");
                this.orderItemCode = rs.getString("ORDER_ITEM_CODE");
                this.invoiceDate = rs.getString("Invoice_Date");
                this.transactionDate = rs.getString("Transaction_Date");
                this.transactionTime = rs.getString("Transaction_Time");
                this.updateDate = rs.getString("UPDATE_DATE");
                this.updateTime = rs.getString("Update_Time");
                this.ACTIVE = rs.getString("ACTIVE");
                this.statusModify = rs.getString("STATUS_MODIFY");
                this.batchNo = rs.getString("BATCH_NO");
                this.oldDoctorCode = rs.getString("OLD_DOCTOR_CODE");
                this.billingSuffixNo = rs.getString("Billing_Suffix_No");
                
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
            System.out.println("A SQLException error has occured in InvoiceDetail.InvoiceDetail() \n" + ex.getMessage());
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
                String tableName = "INVOICE_DETAIL";
                String[] ss = this.getDBConnection().getColumnNames("INVOICE_DETAIL");
                String s1 = this.getDBConnection().getColumnNamesLine(ss);
                stmt = this.getDBConnection().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rs = stmt.executeQuery("SELECT " + s1 + " FROM " + tableName);
                
                // Move cursor to the "insert row"
                rs.moveToInsertRow();
            
                // Set values for the new row.
                rs.updateString("billing_Sub_Group_No", this.getlineNo());
                rs.updateString("order_Date", this.getOrderDate());
                rs.updateString("order_Time", this.getOrderTime());
                rs.updateString("doctor_Cure_Code", this.getDoctorCureCode());
                rs.updateString("update_Cure_Date", this.getUpdateCureDate());
                rs.updateString("update_Cure_Time", this.getUpdateCureTime());
                rs.updateString("doctor_Order_Code", this.getDoctorOrderCode());
                rs.updateString("update_Order_Date", this.getUpdateOrderDate());
                rs.updateString("update_Order_Time", this.getUpdateOrderTime());
                rs.updateString("doctor_Result_Code", this.getDoctorResultCode());
                rs.updateString("update_Result_Date", this.getUpdateResultDate());
                rs.updateString("update_Result_Time", this.getUpdateResultTime());
                rs.updateString("order_Note", this.getOrderNote());
                rs.updateString("department_Group_Code", this.getDepartmentGroupCode());
                rs.updateString("LOCATION_CODE", this.getLocationCode());
                rs.updateDouble("AMOUNT_BEF_DISCOUNT", this.getAmountBefDiscount());
                rs.updateDouble("AMOUNT_OF_DISCOUNT", this.getAmountOfDiscount());
                rs.updateDouble("percent_Of_Discount", this.getPercentOfDiscount());
                rs.updateDouble("AMOUNT_AFT_DISCOUNT", this.getAmountAftDiscount());
                rs.updateString("INVOICE_NO", this.getInvoiceNo());
                rs.updateString("hospital_Code", this.getHospitalCode());
                rs.updateString("order_Item_Code", this.getOrderItemCode());
                rs.updateString("invoice_Date", this.getInvoiceDate());
                rs.updateString("UPDATE_DATE", this.getUpdateCureDate());
                rs.updateString("update_Time", this.getUpdateCureTime());
                rs.updateString("Transaction_Date", this.getTransactionDate());
                rs.updateString("Transaction_Time", this.getTransactionTime());
                rs.updateString("UPDATE_DATE", this.getUpdateDate());
                rs.updateString("update_Time", this.getUpdateTime());
                rs.updateString("ACTIVE", this.getActive());
                rs.updateString("STATUS_MODIFY", this.getStatusModify());
                rs.updateString("BATCH_NO", this.getBatchNo());
                rs.updateString("OLD_DOCTOR_CODE", this.getOldDoctorCode());
                rs.updateString("Billing_Suffix_No", this.getBillingSuffixNo());
                
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
                System.out.println("A SQLException error has occured in InvoiceDetail.insert() \n" + ex.getMessage());
                ex.printStackTrace();
                ret = false;
              }
        }
        
        
        return ret;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
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

    public String getUpdateCureDate() {
        return updateCureDate;
    }

    public void setUpdateCureDate(String updateCureDate) {
        this.updateCureDate = updateCureDate;
    }

    public String getUpdateCureTime() {
        return updateCureTime;
    }

    public void setUpdateCureTime(String updateCureTime) {
        this.updateCureTime = updateCureTime;
    }
    
    public boolean rollBackUpdate(String hospitalCode, String yyyy, String mm, String dd) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "update INVOICE_DETAIL set STATUS_MODIFY = 'R', UPDATE_DATE = '" + JDate.getDate() + "', UPDATE_TIME='" + JDate.getTime() + "', yyyy='', mm='', dd=''";
                sql1 = sql1 + " where INVOICE_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
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
        String sql1 = "delete from INVOICE_DETAIL ";
                sql1 = sql1 + " where INVOICE_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
    
    public boolean rollBackUpdateByDateAndDoctorCode(String hospitalCode, String sYYYY, String sMM, String sDD, String eYYYY, String eMM, String eDD, String doctorCode) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "update INVOICE_DETAIL set STATUS_MODIFY = 'R', UPDATE_DATE = '" + JDate.getDate() + "', UPDATE_TIME='" + JDate.getTime() + "', yyyy='', mm='', dd=''";
                sql1 = sql1 + " where (INVOICE_DATE >= '" + sYYYY.concat(sMM).concat(sDD) + "'";
                sql1 = sql1 + " and INVOICE_DATE <= '" + eYYYY.concat(eMM).concat(eDD) + "')";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
                sql1 = sql1 + " and (yyyy >= '" + sYYYY + "'";
                sql1 = sql1 + " AND MM >= '" + sMM + "'";
                sql1 = sql1 + " and dd >= '" + sDD + "')";
                sql1 = sql1 + " and (yyyy <= '" + eYYYY + "'";
                sql1 = sql1 + " AND MM <= '" + eMM + "'";
                sql1 = sql1 + " and dd <= '" + eDD + "')";
                sql1 = sql1 + " and doctor_cure_code = '" + doctorCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }

    public String getOldDoctorCode() {
        return oldDoctorCode;
    }

    public void setOldDoctorCode(String oldDoctorCode) {
        this.oldDoctorCode = oldDoctorCode;
    }

    public String getBillingSuffixNo() {
        return billingSuffixNo;
    }

    public void setBillingSuffixNo(String billingSuffixNo) {
        this.billingSuffixNo = billingSuffixNo;
    }
}
