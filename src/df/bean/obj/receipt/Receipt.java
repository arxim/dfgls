package df.bean.obj.receipt;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Hospital;
import df.bean.db.table.ReceiptDetail;
import df.bean.db.table.ReceiptHeader;
import df.bean.db.table.Status;
import df.bean.db.table.TRN_Error;
import df.bean.frame.CalculatorFrm1;
import df.bean.obj.location.Location;

public class Receipt {
    ReceiptHeader receiptHeader;
    ReceiptDetail receiptDetail;
    Location location;
    df.bean.db.table.Hospital hospital;
    df.bean.obj.receipt.ReceiptType receiptType;

    private String hospitalCode;
    private String invoiceNo;
    public DBConnection conn = null;
    
    public Receipt() {
        // this.newReceipt();
    }
    public Receipt(DBConnection conn) {
        this.conn = conn;
    }
    /*
    public Receipt(String hospitalCode, String invoiceNo, DBConnection conn) {
        this.setHospitalCode(hospitalCode);
        this.setInvoiceNo(invoiceNo);
        this.setDBConnection(conn);
        
//        this.newLocation();
        this.newReceiptType();
        this.newReceipt(hospitalCode, invoiceNo, receiptType.getReceiptType().getCode());
//        this.newHospital();
    } */
    
    public Receipt(String hospitalCode, String receiptNo, String receiptDate, String invoiceNo, String lineNo, DBConnection conn) {
         this.setHospitalCode(hospitalCode);
         this.setInvoiceNo(invoiceNo);
         this.setDBConnection(conn);
         
     //        this.newLocation();
         this.newReceiptType(hospitalCode, receiptNo, receiptDate, invoiceNo, lineNo);
         this.newReceiptHeader(hospitalCode,receiptNo, receiptDate, invoiceNo);
         this.newReceiptDetail(hospitalCode, receiptNo, receiptDate, invoiceNo, lineNo);
     //        this.newHospital();
     }    
     
     public void setDBConnection(DBConnection conn) {
         this.conn = conn;
     }
     public DBConnection getDBConnection() {
         return this.conn;
     }
/*    public void newReceipt(String hospitalCode, String invoiceNo, String receiptTypeCode) {
        this.setReceipt(new df.bean.db.table.ReceiptHeader(hospitalCode, invoiceNo, receiptTypeCode, this.getDBConnection()));
    } */
    public void newReceiptHeader(String hospitalCode, String receiptNo, String receiptDate, String invoiceNo) {
        this.setReceiptHeader(new ReceiptHeader(hospitalCode, receiptNo, receiptDate, invoiceNo, this.getDBConnection()));
    }
    
    public void newReceiptDetail(String hospitalCode, String receiptNo, String receiptDate, String invoiceNo, String lineNo) {
        this.setReceiptDetail(new ReceiptDetail(hospitalCode, receiptNo, receiptDate, invoiceNo, lineNo, this.getDBConnection()));
    }
    
    public void newLocation() {
        String code = this.getDBConnection().executeQueryString("select Cashier_Location_Code from Receipt where INVOICE_NO='" +this.getInvoiceNo() + 
                "'" + " and HOSPITAL_CODE = '" + this.getHospitalCode() + "'");
        this.setLocation(new Location(code, this.getHospitalCode(), this.getDBConnection()));
    }
    
    public void newReceiptType() {
        String sql = "select Receipt_Type_Code, max(PERCENT_OF_CHARGE) as mPercent_of_charge " +
                     " from Receipt, RECEIPT_TYPE " +
                     " where INVOICE_NO='" +this.getInvoiceNo() + "'" + 
                     " and Receipt.HOSPITAL_CODE = '" + this.getHospitalCode() + "'";
        sql = sql + " and Receipt.Receipt_Type_Code = Receipt_Type.CODE ";
        sql = sql + " group by Receipt_Type_Code ";
        sql = sql + " order by mPercent_of_charge ";
        String code = this.getDBConnection().executeQueryString(sql);
        this.setReceiptType(new ReceiptType(this.getHospitalCode(), code, this.getDBConnection()));
    }
    
    // select RECEIPT_TYPE by lineNo
    public void newReceiptType(String hospitalCode, String receiptNo, String receiptDate, String invoiceNo, String lineNo) {
        String sql = "select Receipt_Type_Code, max(PERCENT_OF_CHARGE) as mPercent_of_charge " +
                     " from RECEIPT_DETAIL, RECEIPT_TYPE " +
                     " where INVOICE_NO='" + invoiceNo + "'" + 
                     " and RECEIPT_DETAIL.RECEIPT_NO ='" + receiptNo + "'" +
                     " and RECEIPT_DETAIL.HOSPITAL_CODE = '" + hospitalCode + "'" +
                     " and RECEIPT_DETAIL.BILLING_SUB_GROUP_NO = '" + lineNo + "'";
        sql = sql + " and RECEIPT_DETAIL.Receipt_Type_Code = Receipt_Type.CODE ";
        sql = sql + " and RECEIPT_DETAIL.RECEIPT_DATE ='" + receiptDate + "'";
        sql = sql + " group by Receipt_Type_Code ";
        String code = this.getDBConnection().executeQueryString(sql);
        this.setReceiptType(new ReceiptType(this.getHospitalCode(), code, this.getDBConnection()));
    }    
    
    public void newHospital() {
        setHospital(new Hospital(this.getHospitalCode(), this.getDBConnection()));
    }
/*    public void newReceipt() {
        this.setReceipt(new df.bean.db.table.Receipt());
    } */
    public void newLocation(String locationCode) {
        this.setLocation(new Location(locationCode, this.getHospitalCode(), this.getDBConnection()));
    }
    public void newHospital(String hospitalCode) {
        setHospital(new Hospital(hospitalCode, this.getDBConnection()));
    }
    
    public df.bean.db.table.ReceiptDetail getReceiptDetail() {
        return this.receiptDetail;
    }

    public void setReceiptDetail(df.bean.db.table.ReceiptDetail receipt) {
        this.receiptDetail = receipt;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
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

    public ReceiptType getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(ReceiptType receiptType) {
        this.receiptType = receiptType;
    }

    public ReceiptHeader getReceiptHeader() {
        return receiptHeader;
    }

    public void setReceiptHeader(ReceiptHeader receiptHeader) {
        this.receiptHeader = receiptHeader;
    }
    
    public boolean IsNotFoundDoctorCode(CalculatorFrm1 cf, String receiptDate) {
        boolean ret = true;
        String sql = "select HOSPITAL_CODE, INVOICE_NO, DOCTOR_CODE from RECEIPT_DETAIL where RECEIPT_DATE = '" + receiptDate + "'";
                sql = sql + " and DOCTOR_CODE not in (SELECT CODE FROM DOCTOR WHERE ACTIVE = '1') order by INVOICE_NO, DOCTOR_CODE";
                
        ResultSet rs = this.getDBConnection().executeQuery(sql);
        List values = new ArrayList();
            try {
                while (rs.next()) {
                    values.add(receiptDate);
                    values.add(rs.getString("INVOICE_NO"));
                    values.add(rs.getString("DOCTOR_CODE") + " is not found.");
                    cf.setTableValue(values);
                    values.clear();
                }
            } catch (SQLException e) {
                // TODO
                e.printStackTrace();
            } finally {
               //Clean up resources, close the connection.
               values = null;
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
        return ret;
    }
    
    
    
    public boolean IsNotFoundOrderItemCode(CalculatorFrm1 cf, String receiptDate) {
        boolean ret = true;
        String sql = "select RECEIPT_DETAIL.INVOICE_NO, ORDER_ITEM_CODE, RT. from RECEIPT_DETAIL,receipt_header where ";
        sql = sql + " RECEIPT_DETAIL.RECEIPT_NO = receipt_header.RECEIPT_NO";
        sql = sql + " and RECEIPT_DETAIL.RECEIPT_DATE = '" + receiptDate + "'";
        sql = sql + " and receipt_header.RECEIPT_DATE = '" + receiptDate + "'";
        sql = sql + " and ORDER_ITEM_CODE not in (SELECT CODE FROM ORDER_ITEM) order by RECEIPT_DETAIL.INVOICE_NO, ORDER_ITEM_CODE, RT.";
        
        ResultSet rs = this.getDBConnection().executeQuery(sql);
        List values = new ArrayList();
            try {
                while (rs.next()) {
                    values.add(receiptDate);
                    values.add(rs.getString("RECEIPT_DETAIL.INVOICE_NO"));
                    values.add("Error: " + rs.getString("ORDER_ITEM_CODE") + " is not found." + " HN:" + rs.getString("RT."));
                    cf.setTableValue(values);
                    values.clear();
                }
            } catch (SQLException e) {
                // TODO
                e.printStackTrace();
            } finally {
               //Clean up resources, close the connection.
               values = null;
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
        return ret;
    }    
    
    public boolean IsNotFoundDepartmentCode(CalculatorFrm1 cf, String receiptDate) {
        boolean ret = true;
        String sql = "select HOSPITAL_CODE, INVOICE_NO, department_code from RECEIPT_DETAIL where RECEIPT_DATE = '" + receiptDate + "'";
                sql = sql + " and department_code not in (SELECT CODE FROM DEPARTMENT_GROUP) order by INVOICE_NO, department_code";
                
        ResultSet rs = this.getDBConnection().executeQuery(sql);
        List values = new ArrayList();
            try {
                while (rs.next()) {
                    values.add(receiptDate);
                    values.add(rs.getString("INVOICE_NO"));
                    values.add(rs.getString("ORDER_ITEM_CODE") + " is not found.");
                    cf.setTableValue(values);
                    values.clear();
                }
            } catch (SQLException e) {
                // TODO
                e.printStackTrace();
            } finally {
               //Clean up resources, close the connection.
               values = null;
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
        return ret;
    }    

    
    public boolean IsNotFoundReceiptTypeCode(CalculatorFrm1 cf, String receiptDate) {
        boolean ret = true;
        String sql = "select HOSPITAL_CODE, INVOICE_NO, RECEIPT_TYPE_CODE from RECEIPT_DETAIL where RECEIPT_DATE = '" + receiptDate + "'";
                sql = sql + " and RECEIPT_TYPE_CODE not in (SELECT CODE FROM RECEIPT_TYPE) order by INVOICE_NO, RECEIPT_TYPE_CODE";
                
        ResultSet rs = this.getDBConnection().executeQuery(sql);
        List values = new ArrayList();
            try {
                while (rs.next()) {
                    values.add(receiptDate);
                    values.add(rs.getString("INVOICE_NO"));
                    values.add(rs.getString("RECEIPT_TYPE_CODE") + " is not found.");
                    cf.setTableValue(values);
                    values.clear();
                }
            } catch (SQLException e) {
                // TODO
                e.printStackTrace();
            } finally {
               //Clean up resources, close the connection.
               values = null;
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
        return ret;
    }    
    
    public boolean IsNotFoundMedthodAllocation(CalculatorFrm1 cf, String receiptDate) {
        boolean ret = true;
        String sql = "SELECT DISTINCT RECEIPT_DETAIL.hospital_Code as hc, RECEIPT_DETAIL.ORDER_ITEM_CODE as oi_code, RECEIPT_DETAIL.INVOICE_NO as inv_no, " + 
        " METHOD_ALLOCATION.DOCTOR_CATEGORY_CODE , DOCTOR.DOCTOR_CATEGORY_CODE " +
        " FROM         RECEIPT_DETAIL INNER JOIN " +
        " DOCTOR ON RECEIPT_DETAIL.DOCTOR_CODE = DOCTOR.CODE LEFT OUTER JOIN " +
        "              METHOD_ALLOCATION ON RECEIPT_DETAIL.ORDER_ITEM_CODE = METHOD_ALLOCATION.ORDER_ITEM_CODE AND " +
                      " DOCTOR.DOCTOR_CATEGORY_CODE = METHOD_ALLOCATION.DOCTOR_CATEGORY_CODE " +
        " WHERE     (METHOD_ALLOCATION.DOCTOR_CATEGORY_CODE IS NULL) ";
        sql = sql +  " and RECEIPT_DETAIL.RECEIPT_DATE = '" + receiptDate + "'";
        sql = sql + " and DOCTOR.PAYMENT_MODE_CODE <> 'U'";
        
        ResultSet rs = this.getDBConnection().executeQuery(sql);
        List values = new ArrayList();
            try {
                while (rs.next()) {
                    values.add(rs.getString("hc"));
                    values.add(rs.getString("inv_no"));
                    values.add(rs.getString("oi_code") + " in table method_allocation is not set");
                    cf.setTableValue(values);
                    values.clear();
                }
            } catch (SQLException e) {
                // TODO
                e.printStackTrace();
            } finally {
               //Clean up resources, close the connection.
               values = null;
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
        return ret;
    }    
    
    // Receipt Header
    public boolean IsNotFoundAdmissionTypeCode(CalculatorFrm1 cf, String receiptDate) {
        boolean ret = true;
        String sql = "select HOSPITAL_CODE, INVOICE_NO, ADMISSION_TYPE_CODE from RECEIPT_DETAIL where RECEIPT_DATE = '" + receiptDate + "'";
                sql = sql + " and ADMISSION_TYPE_CODE not in (SELECT CODE FROM ADMISSION_TYPE) order by INVOICE_NO, ADMISSION_TYPE_CODE";
                
        ResultSet rs = this.getDBConnection().executeQuery(sql);
        List values = new ArrayList();
            try {
                while (rs.next()) {
                    values.add(receiptDate);
                    values.add(rs.getString("INVOICE_NO"));
                    values.add(rs.getString("ADMISSION_TYPE_CODE") + " is not found.");
                    cf.setTableValue(values);
                    values.clear();
                }
            } catch (SQLException e) {
                // TODO
                e.printStackTrace();
            } finally {
               //Clean up resources, close the connection.
               values = null;
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
        return ret;
    }        
    
    
    //update receipt by CASH
    public int updateReceipt(String YYYY, String MM, String hospitalCode, String tableName) {
        int rows = -1;
        String sql = "";
        try {
            sql = "UPDATE " + tableName + " SET PAY_BY_CASH = 'Y' "
                    + " ,YYYY = '" + YYYY + "'"
                    + " ,MM = '" + MM + "'"
                    + " ,RECEIPT_NO = INVOICE_NO "
                    + " ,RECEIPT_DATE = INVOICE_DATE "
                    + " WHERE PAY_BY_CASH = 'N' "
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND TRANSACTION_TYPE = '" + Status.TRANSACTION_TYPE_RECEIPT + "'"
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND (TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')";
            rows = this.getDBConnection().executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    } 
    
    //update receipt by CASH
    public int updateReceipt(String YYYY, String MM, String startDate, String endDate, String hospitalCode, String tableName) {
        int rows = -1;
        String sql = "";
        try {
            sql = "UPDATE " + tableName + " SET PAY_BY_CASH = 'Y' "
                    + " ,YYYY = '" + YYYY + "'"
                    + " ,MM = '" + MM + "'"
                    + " ,RECEIPT_NO = INVOICE_NO "
                    + " ,RECEIPT_DATE = INVOICE_DATE "
                    + " WHERE PAY_BY_CASH = 'N' "
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND TRANSACTION_TYPE = '" + Status.TRANSACTION_TYPE_RECEIPT + "'"
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND (TRANSACTION_DATE BETWEEN '" + startDate + "' AND '" + endDate + "')";
            rows = this.getDBConnection().executeUpdate(sql);

        } catch (Exception e) {
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), 
                        TRN_Error.PROCESS_IMPORT_BILL, "Update receipt by cash fail.", e.getMessage(), sql);
            e.printStackTrace();
        }
        return rows;
    } 
    
    public boolean rollBackUpdate(String YYYY, String MM, String hospitalCode, String tableName) {
        boolean ret = false;
        String sql1 = "UPDATE " + tableName + " SET "
                    + " YYYY = ''"
                    + " ,MM = ''"
                    + " ,TRANSACTION_MODULE = 'TR'"
                    + " ,RECEIPT_NO = '' "
                    + " ,RECEIPT_DATE = '' "
                    + " WHERE PAY_BY_CASH = 'Y' "
                    + " AND PAY_BY_AR <> 'Y' AND PAY_BY_CASH_AR <> 'Y' AND PAY_BY_DOCTOR <> 'Y' AND PAY_BY_PAYOR <> 'Y'"
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND TRANSACTION_TYPE = '" + Status.TRANSACTION_TYPE_RECEIPT + "'"
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND (TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')";

        String sql2 = "UPDATE " + tableName + " SET PAY_BY_CASH = 'N' "
                    + " WHERE PAY_BY_CASH = 'Y' "
                    + " AND (BATCH_NO IS NULL OR BATCH_NO = '') "
                    + " AND TRANSACTION_TYPE = '" + Status.TRANSACTION_TYPE_RECEIPT + "'"
                    + " AND HOSPITAL_CODE='" + hospitalCode + "'" 
                    + " AND (TRANSACTION_DATE BETWEEN '" + YYYY + MM + "00' AND '" + YYYY + MM + "31')";
        int rows = this.getDBConnection().executeUpdate(sql1);
        if (rows > -1) rows = this.getDBConnection().executeUpdate(sql1);
        if (rows > -1) ret = true;
        return ret;
    }
    
}
