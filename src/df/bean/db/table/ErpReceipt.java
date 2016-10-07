package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;

public class ErpReceipt extends ABSTable {

    private String hospitalCode;
    private String receiptNo;
    private String invoiceNo;
    private String invoiceNoInside;
    private String receiptDate;
    private String invoiceDate;
    private String cashierLocationCode;
    private String titleName;
    private String firstName;
    private String lastName;
    private String hospitalNo;
    private String visitNo;
    private String admissionTypeCode;
    private Double amountBefDiscount;
    private Double amountOfDiscount;
    private String receiptTypeCode;
    private String receiptModule;
    private Double invAmtBefDiscount;
    private String invAmtOfDiscount;
    private Integer isVoid;
    private String docType;
    private String updateDate;
    private String updateTime;
    private String userId;
    private String isloaded;


    public ErpReceipt(DBConnection conn) {
        super();
        this.setDBConnection(conn);
    }

    public String getAdmissionTypeCode() {
        return this.admissionTypeCode;
    }

    public Double getAmountBefDiscount() {
        return this.amountBefDiscount;
    }

    public Double getAmountOfDiscount() {
        return this.amountOfDiscount;
    }

    public String getCashierLocationCode() {
        return this.cashierLocationCode;
    }

    public String getDocType() {
        return this.docType;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getHospitalCode() {
        return this.hospitalCode;
    }

    public String getHospitalNo() {
        return this.hospitalNo;
    }

    public Double getInvAmtBefDiscount() {
        return this.invAmtBefDiscount;
    }

    public String getInvAmtOfDiscount() {
        return this.invAmtOfDiscount;
    }

    public String getInvoiceDate() {
        return this.invoiceDate;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public String getInvoiceNoInside() {
        return this.invoiceNoInside;
    }

    public Integer getIsVoid() {
        return this.isVoid;
    }

    public String getIsloaded() {
        return this.isloaded;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getReceiptDate() {
        return this.receiptDate;
    }

    public String getReceiptModule() {
        return this.receiptModule;
    }

    public String getReceiptNo() {
        return this.receiptNo;
    }

    public String getReceiptTypeCode() {
        return this.receiptTypeCode;
    }

    public String getTitleName() {
        return this.titleName;
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getVisitNo() {
        return this.visitNo;
    }

    public void setAdmissionTypeCode(String admissionTypeCode) {
        this.admissionTypeCode = admissionTypeCode;
    }

    public void setAmountBefDiscount(Double amountBefDiscount) {
        this.amountBefDiscount = amountBefDiscount;
    }

    public void setAmountOfDiscount(Double amountOfDiscount) {
        this.amountOfDiscount = amountOfDiscount;
    }

    public void setCashierLocationCode(String cashierLocationCode) {
        this.cashierLocationCode = cashierLocationCode;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public void setHospitalNo(String hospitalNo) {
        this.hospitalNo = hospitalNo;
    }

    public void setInvAmtBefDiscount(Double invAmtBefDiscount) {
        this.invAmtBefDiscount = invAmtBefDiscount;
    }

    public void setInvAmtOfDiscount(String invAmtOfDiscount) {
        this.invAmtOfDiscount = invAmtOfDiscount;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public void setInvoiceNoInside(String invoiceNoInside) {
        this.invoiceNoInside = invoiceNoInside;
    }

    public void setIsVoid(Integer isVoid) {
        this.isVoid = isVoid;
    }

    public void setIsloaded(String isloaded) {
        this.isloaded = isloaded;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public void setReceiptModule(String receiptModule) {
        this.receiptModule = receiptModule;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public void setReceiptTypeCode(String receiptTypeCode) {
        this.receiptTypeCode = receiptTypeCode;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
    }


        
    public boolean rollBackUpdate(String hospitalCode, String yyyy, String mm, String dd) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "update ERP_Receipt set ISLOADED = 'N', UPDATE_DATE = '" + JDate.getDate() + "', UPDATE_TIME='" + JDate.getTime() + "'";
                sql1 = sql1 + " where RECEIPT_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }
        
    public boolean rollBackDelete(String hospitalCode, String yyyy, String mm, String dd) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql1 = "delete from ERP_Receipt ";
                sql1 = sql1 + " where RECEIPT_DATE = '" + yyyy.concat(mm).concat(dd) + "'";
                sql1 = sql1 + " and HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql1 );
        ret = super.rollBack(sqlCommand);
        return ret;
    }

    // Import to Receipt_Header
    public int ImpToReceiptHeader(String computeDate) {
        int rows = -1;
        try {
            String sql = "";
            sql = "insert into Receipt_Header (INVOICE_NO, INVOICE_DATE, hospital_Code,";
            sql = sql + "RECEIPT_NO, RECEIPT_DATE, invoice_no_inside, cashier_location_code,";
            sql = sql + "TITLE_NAME, FIRST_NAME, LAST_NAME, ";
            sql = sql + "RT., visit_no, ADMISSION_TYPE_CODE, AMOUNT_BEF_DISCOUNT,";
            sql = sql + "AMOUNT_OF_DISCOUNT, RECEIPT_MODULE, UPDATE_DATE, UPDATE_TIME,";
            sql = sql + "USER_ID, ACTIVE, inv_amt_bef_discount, inv_amt_of_discount,";
            sql = sql + "is_void, doc_type)";
        
            sql = sql + " select INVOICE_NO, INVOICE_DATE, hospital_Code,";
            sql = sql + "RECEIPT_NO, RECEIPT_DATE, invoice_no_inside, cashier_location_code,";
            sql = sql + "TITLE_NAME, FIRST_NAME, LAST_NAME, ";
            sql = sql + "RT., visit_no, ADMISSION_TYPE_CODE, AMOUNT_BEF_DISCOUNT,";
            sql = sql + "AMOUNT_OF_DISCOUNT, RECEIPT_MODULE, to_char(sysdate,'yyyymmdd'), to_char(sysdate,'HH24MISS'),";
            sql = sql + "USER_ID, '1', inv_amt_bef_discount, inv_amt_of_discount,";
            sql = sql + "is_void, doc_type from ERP_Receipt where ISLOADED <> 'Y'";
            sql = sql + " and RECEIPT_DATE ='" + computeDate + "'";
                
            rows = conn.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
    
    // Import to RECEIPT_DETAIL
    public int ImpToReceiptDetail(String computeDate) {
        int rows = -1;
        try {
            String sql = "";
            sql = "insert into RECEIPT_DETAIL (INVOICE_NO, INVOICE_DATE, hospital_Code,";
            sql = sql + "RECEIPT_NO, RECEIPT_DATE, OLD_DOCTOR_CODE, BILLING_SUB_GROUP_NO, ";
            sql = sql + "billing_suffix_no, ORDER_ITEM_CODE, invoice_no_inside, ";
            sql = sql + "DOCTOR_CODE, department_code, LOCATION_CODE,";
            sql = sql + "AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, percent_of_discount, ";
            sql = sql + "AMOUNT_AFT_DISCOUNT, RECEIPT_TYPE_CODE, RECEIPT_MODULE, ";
            sql = sql + "yyyy, mm, dd, BATCH_NO, STATUS_MODIFY, UPDATE_DATE, UPDATE_TIME, ";
            sql = sql + "USER_ID, doc_type)";
    
            sql = sql + " select er.INVOICE_NO, er.INVOICE_DATE, er.hospital_Code,";
            sql = sql + "er.RECEIPT_NO, er.RECEIPT_DATE, id.OLD_DOCTOR_CODE, id.BILLING_SUB_GROUP_NO, ";
            sql = sql + "id.billing_suffix_no, id.ORDER_ITEM_CODE, er.invoice_no_inside, ";
            sql = sql + "id.doctor_cure_code, id.DEPARTMENT_CODE, id.LOCATION_CODE,";
            sql = sql + "er.AMOUNT_BEF_DISCOUNT, er.AMOUNT_OF_DISCOUNT, id.percent_of_discount, ";
            sql = sql + "er.AMOUNT_BEF_DISCOUNT-er.AMOUNT_OF_DISCOUNT as AMOUNT_AFT_DISCOUNT, er.RECEIPT_TYPE_CODE, er.RECEIPT_MODULE, ";
            sql = sql + "'', '', '', '', id.STATUS_MODIFY, to_char(sysdate,'yyyymmdd'), to_char(sysdate,'HH24MISS'), ";
            sql = sql + "er.USER_ID, er.doc_type ";
            sql = sql + " from ERP_Receipt er, INVOICE_DETAIL id where er.ISLOADED <> 'Y'";
            sql = sql + " and er.RECEIPT_DATE ='" + computeDate + "'";
            sql = sql + " and er.HOSPITAL_CODE = id.HOSPITAL_CODE";
            sql = sql + " and er.INVOICE_NO = id.INVOICE_NO";
            sql = sql + " and er.INVOICE_DATE = id.INVOICE_DATE";
            
            rows = conn.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
    
    public int UpdateLoaded(String computeDate) {
        int rows = -1;
        try {
            rows = conn.executeUpdate("update ERP_Receipt set isloaded='Y' where isloaded <> 'Y' and RECEIPT_DATE ='" + computeDate + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
    
    public int ImpToTransactionHeader(String computeDate) {
        int rows = 0;
//        String sql = "select RECEIPT_NO, RECEIPT_DATE, doc_type, is_void, INVOICE_NO, INVOICE_DATE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT from erp_receipt where RECEIPT_DATE='" + computeDate + "' and isloaded <> 'Y'";// and is_void = '" + Status.RECEIPT_IS_NOT_VOID + "'";
        String sql = "select RECEIPT_NO, RECEIPT_DATE, INVOICE_NO, INVOICE_DATE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, doc_type, is_void, inv_amt_bef_discount, inv_amt_of_discount " + 
        "from erp_receipt " + 
        "where  INVOICE_NO = 'N50-012366' and RECEIPT_DATE='" + computeDate + "' and isloaded = 'Y' and (RECEIPT_NO || RECEIPT_DATE || INVOICE_NO || INVOICE_DATE || AMOUNT_BEF_DISCOUNT || AMOUNT_OF_DISCOUNT || doc_type || '1') " + 
        "in (select RECEIPT_NO || RECEIPT_DATE || INVOICE_NO || INVOICE_DATE || AMOUNT_BEF_DISCOUNT || AMOUNT_OF_DISCOUNT || doc_type || count(RECEIPT_NO || RECEIPT_DATE || INVOICE_NO || INVOICE_DATE || AMOUNT_BEF_DISCOUNT || AMOUNT_OF_DISCOUNT || doc_type) " + 
        "   from erp_receipt where RECEIPT_DATE='" + computeDate + "' and isloaded = 'Y'" + 
        "   group by (RECEIPT_NO || RECEIPT_DATE || INVOICE_NO || INVOICE_DATE || AMOUNT_BEF_DISCOUNT || AMOUNT_OF_DISCOUNT || doc_type)" + 
        "   having count(RECEIPT_NO || RECEIPT_DATE || INVOICE_NO || INVOICE_DATE || AMOUNT_BEF_DISCOUNT || AMOUNT_OF_DISCOUNT || doc_type) = 1) " +
        "   order by INVOICE_NO, RECEIPT_DATE";
        
        ResultSet rs = this.getDBConnection().executeQuery(sql);
        Statement stmt = this.getDBConnection().getStatementForInsert();
        try {
            while (rs.next()) {
//                if (rs.getString("is_void").equals("0")) {      // ��� void
                    sql = "update TRANSACTION_HEADER set RECEIPT_NO = '" + rs.getString("RECEIPT_NO") + "'";
                    sql = sql + ", RECEIPT_DATE = '" + rs.getString("RECEIPT_DATE") + "'";
                    sql = sql + ", RECEIPT_TYPE = '" + rs.getString("doc_type") + "'";
                    sql = sql + ", REC_IS_VOID = '" + rs.getString("is_void") + "'";
                    sql = sql + ", REC_AMOUNT_BEF_DISCOUNT = " + (rs.getDouble("inv_amt_bef_discount"));
                    sql = sql + ", REC_AMOUNT_OF_DISCOUNT = " + ((rs.getDouble("inv_amt_bef_discount") - rs.getDouble("inv_amt_of_discount")) - (rs.getDouble("AMOUNT_BEF_DISCOUNT") - rs.getDouble("AMOUNT_OF_DISCOUNT")));
                    sql = sql + ", UPDATE_DATE = '" + JDate.getDate() + "'";
                    sql = sql + ", UPDATE_TIME = '" + JDate.getTime() + "'";
                    sql = sql + ", RECEIPT_MODULE = 'AR'";
                    sql = sql + " where (STATUS_MODIFY <> '" + Status.STATUS_CALCULATED + "'";
                    sql = sql + " or STATUS_MODIFY is null)";
                    sql = sql + " and RECEIPT_NO = '" + Status.RECEIPT_NO_DEFAULT + "'";
                    sql = sql + " and INVOICE_NO = '" + rs.getString("INVOICE_NO") + "'";
                    sql = sql + " and INVOICE_DATE = '" + rs.getString("INVOICE_DATE") + "'"; 
//                }
                
                rows = rows + stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            rows = -1;
        } finally {
               //Clean up resources, close the connection.
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                     stmt = null;
                    }
                  catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
        return rows;
    }
    
    public int ImpToTransactionDetail(String computeDate) {
        int rows = 0;
//        String sql = "select RECEIPT_NO, RECEIPT_DATE, doc_type, is_void, INVOICE_NO, INVOICE_DATE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, RECEIPT_TYPE_CODE from erp_receipt where RECEIPT_DATE='" + computeDate + "' and isloaded <> 'Y' and is_void = '" + Status.RECEIPT_IS_NOT_VOID + "'";
        String sql = "select RECEIPT_NO, RECEIPT_DATE, doc_type, INVOICE_NO, INVOICE_DATE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, RECEIPT_TYPE_CODE, inv_amt_bef_discount, inv_amt_of_discount, is_void " + 
             " from erp_receipt " + 
             " where RECEIPT_DATE='" + computeDate + "' and isloaded <> 'Y' " + 
             " and (RECEIPT_NO || RECEIPT_DATE || doc_type || INVOICE_NO || INVOICE_DATE || AMOUNT_BEF_DISCOUNT || AMOUNT_OF_DISCOUNT || RECEIPT_TYPE_CODE || '1') " + 
             " in (select RECEIPT_NO || RECEIPT_DATE || doc_type || INVOICE_NO || INVOICE_DATE || AMOUNT_BEF_DISCOUNT || AMOUNT_OF_DISCOUNT || RECEIPT_TYPE_CODE || count(RECEIPT_NO || RECEIPT_DATE || doc_type || INVOICE_NO || INVOICE_DATE || AMOUNT_BEF_DISCOUNT || AMOUNT_OF_DISCOUNT || RECEIPT_TYPE_CODE) " + 
             "   from erp_receipt where RECEIPT_DATE='" + computeDate + "' and isloaded <> 'Y'" + 
             "   group by (RECEIPT_NO || RECEIPT_DATE || doc_type || INVOICE_NO || INVOICE_DATE || AMOUNT_BEF_DISCOUNT || AMOUNT_OF_DISCOUNT || RECEIPT_TYPE_CODE)" + 
             "   having count(RECEIPT_NO || RECEIPT_DATE || doc_type || INVOICE_NO || INVOICE_DATE || AMOUNT_BEF_DISCOUNT || AMOUNT_OF_DISCOUNT || RECEIPT_TYPE_CODE) = 1) " +
             "   order by INVOICE_NO, RECEIPT_DATE";
 
        ResultSet rs = this.getDBConnection().executeQuery(sql);
        Statement stmt = this.getDBConnection().getStatementForInsert();
        try {
            while (rs.next()) {
//                if (rs.getString("is_void").equals("0")) {  // ��� void
                    sql = "update TRN_DAILY set RECEIPT_NO = '" + rs.getString("RECEIPT_NO") + "'";
                    sql = sql + ", RECEIPT_DATE = '" + rs.getString("RECEIPT_DATE") + "'";
                    sql = sql + ", RECEIPT_TYPE = '" + rs.getString("doc_type") + "'";
                    sql = sql + ", REC_IS_VOID = '" + rs.getString("is_void") + "'";
                    sql = sql + ", REC_AMOUNT_BEF_DISCOUNT = " + (rs.getDouble("inv_amt_bef_discount"));
                    sql = sql + ", REC_AMOUNT_OF_DISCOUNT = " + ((rs.getDouble("inv_amt_bef_discount") - rs.getDouble("inv_amt_of_discount")) - (rs.getDouble("AMOUNT_BEF_DISCOUNT") - rs.getDouble("AMOUNT_OF_DISCOUNT")));
                    sql = sql + ", RECEIPT_TYPE_CODE = '" + rs.getString("RECEIPT_TYPE_CODE") + "'";
                    sql = sql + ", UPDATE_DATE = '" + JDate.getDate() + "'";
                    sql = sql + ", UPDATE_TIME = '" + JDate.getTime() + "'";
                    sql = sql + ", RECEIPT_MODULE = 'AR'";
                    // �������� update ��Ƿ���¤ӹǳ������� 
                    if (!rs.getString("RECEIPT_DATE").equals(rs.getString("INVOICE_DATE"))) {
                        sql = sql + ", YYYY = '" + rs.getString("RECEIPT_DATE").substring(0,4) + "'";
                        sql = sql + ", MM = '" + rs.getString("RECEIPT_DATE").substring(4,6) + "'";
                        sql = sql + ", dd = '" + rs.getString("RECEIPT_DATE").substring(6,8) + "'";
                        sql = sql + ", COMPUTE_DAILY_DATE = '" + JDate.getDate() + "'";
                        sql = sql + ", COMPUTE_DAILY_TIME = '" + JDate.getTime() + "'";
                        sql = sql + ", COMPUTE_DAILY_USER_ID = '" + Variables.getUserID() + "'";
                        sql = sql + ", STATUS_MODIFY = '" + Status.STATUS_CALCULATED + "'";
                    }
//                    sql = sql + " where (STATUS_MODIFY <> '" + Status.STATUS_CALCULATED + "' or STATUS_MODIFY is null)";
                    sql = sql + " where RECEIPT_NO = '" + Status.RECEIPT_NO_DEFAULT + "'";
                    sql = sql + " and INVOICE_NO = '" + rs.getString("INVOICE_NO") + "'";
                    sql = sql + " and INVOICE_DATE = '" + rs.getString("INVOICE_DATE") + "'";
                    sql = sql + " and (COMPUTE_DAILY_DATE is null or COMPUTE_DAILY_DATE = '')";
//                } 
                rows = rows + stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            rows = -1;
        } finally {
               //Clean up resources, close the connection.
               if(rs != null) {
                  try {
                     rs.close();
                     rs = null;
                     stmt = null;
                }
                catch (Exception ignored) { ignored.printStackTrace();   }
            }
        }
        return rows;
    }
    
    // insert if is void
    public int ImpToTransactionHeaderByInsert(String computeDate) {
        int rows = 0;
        String sql = "";
        Statement stmt = this.getDBConnection().getStatementForInsert();
        try {
            sql = "insert into TRANSACTION_HEADER (INVOICE_NO, INVOICE_DATE, hospital_Code,";
            sql = sql + "TITLE_NAME, FIRST_NAME, LAST_NAME, ";
            sql = sql + "RT., visit_no, co_payment, ";
            sql = sql + "AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, percent_of_discount, ";
            sql = sql + "AMOUNT_AFT_DISCOUNT, ADMISSION_TYPE_CODE, LOCATION_CODE, ";
            sql = sql + "payor_office_code, payor_name, TRANSACTION_DATE, transaction_time, ";
            sql = sql + "USER_ID, UPDATE_DATE, UPDATE_TIME, ACTIVE, STATUS_MODIFY, INVOICE_TYPE, ";
            sql = sql + "INV_IS_VOID, RECEIPT_NO, RECEIPT_DATE, RECEIPT_TYPE, REC_IS_VOID,";
            sql = sql + "REC_AMOUNT_BEF_DISCOUNT, REC_AMOUNT_OF_DISCOUNT)";
            
            sql = sql + " select distinct rc.INVOICE_NO, rc.INVOICE_DATE, rc.hospital_Code,"; // ����¹ INVOICE_DATE -> RECEIPT_DATE
            sql = sql + "rc.TITLE_NAME, rc.FIRST_NAME, rc.LAST_NAME, ";
            sql = sql + "rc.RT., rc.visit_no, HD.co_payment,";
            sql = sql + "HD.AMOUNT_BEF_DISCOUNT, HD.AMOUNT_OF_DISCOUNT, HD.percent_of_discount, ";
            sql = sql + "HD.AMOUNT_AFT_DISCOUNT, HD.ADMISSION_TYPE_CODE, HD.LOCATION_CODE,";
            sql = sql + "HD.payor_office_code, HD.payor_name, rc.RECEIPT_DATE, to_char(sysdate,'HH24MISS'),";
            sql = sql + "HD.USER_ID, to_char(sysdate,'yyyymmdd'), to_char(sysdate,'HH24MISS'), HD.ACTIVE, HD.STATUS_MODIFY, '" + Status.INVOICE_TYPE_IS_RESULT + "', "; //.INVOICE_TYPE, ";
            sql = sql + "HD.INV_IS_VOID, rc.RECEIPT_NO, rc.RECEIPT_DATE, HD.RECEIPT_TYPE, HD.REC_IS_VOID, "; 
            sql = sql + "rc.inv_amt_bef_discount, rc.inv_amt_bef_discount - rc.inv_amt_of_discount - (rc.AMOUNT_BEF_DISCOUNT - rc.AMOUNT_OF_DISCOUNT) ";
            
            sql = sql + " from Erp_receipt rc, TRANSACTION_HEADER hd where rc.ISLOADED <> 'Y'";
            sql = sql + " and rc.INVOICE_NO = HD.INVOICE_NO and rc.INVOICE_DATE = HD.INVOICE_DATE ";
            sql = sql + " and rc.is_void = '" + Status.RECEIPT_IS_VOID + "'";
            //                    sql = sql + " and rc.RT. = HD.RT. and rc.visit_no = HD.visit_no ";
            sql = sql + " and rc.RECEIPT_DATE ='" + computeDate + "'";      // �� RECEIPT_DATE �� result_date
            //                    sql = sql + " and HD.RECEIPT_DATE <> '" + computeDate + "'";
            // ������������
            sql = sql + " and (rc.RECEIPT_NO || rc.RECEIPT_DATE || rc.INVOICE_NO || rc.INVOICE_DATE || rc.AMOUNT_BEF_DISCOUNT || rc.AMOUNT_OF_DISCOUNT || rc.doc_type || '1') " + 
                     " in (select er.RECEIPT_NO || er.RECEIPT_DATE || er.INVOICE_NO || er.INVOICE_DATE || er.AMOUNT_BEF_DISCOUNT || er.AMOUNT_OF_DISCOUNT || er.doc_type || count(er.RECEIPT_NO || er.RECEIPT_DATE || er.INVOICE_NO || er.INVOICE_DATE || er.AMOUNT_BEF_DISCOUNT || er.AMOUNT_OF_DISCOUNT || er.doc_type) " + 
                     "   from erp_receipt er where er.RECEIPT_DATE='" + computeDate + "' and er.isloaded <> 'Y'" + 
                     "   group by (er.RECEIPT_NO || er.RECEIPT_DATE || er.INVOICE_NO || er.INVOICE_DATE || er.AMOUNT_BEF_DISCOUNT || er.AMOUNT_OF_DISCOUNT || er.doc_type)" + 
                     "   having count(er.RECEIPT_NO || er.RECEIPT_DATE || er.INVOICE_NO || er.INVOICE_DATE || er.AMOUNT_BEF_DISCOUNT || er.AMOUNT_OF_DISCOUNT || er.doc_type) = 1) " +
                     "   order by rc.INVOICE_NO, rc.RECEIPT_DATE";
                     
            rows = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            rows = -1;
        } finally {
               //Clean up resources, close the connection.
                stmt = null;
        }
        return rows;
    }    
    
    public int ImpToTransactionDetailByInsert(String computeDate) {
        int rows = 0;
        String sql = "";
        Statement stmt = this.getDBConnection().getStatementForInsert();
        try {
            sql = "insert into TRN_DAILY (HOSPITAL_CODE, INVOICE_NO, INVOICE_DATE, "; 
            sql = sql + "BILLING_SUB_GROUP_NO, ORDER_ITEM_CODE, order_date, ";
            sql = sql + "DOCTOR_CODE, DEPARTMENT_CODE, LOCATION_CODE, ADMISSION_TYPE_CODE, ";
            sql = sql + "AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, AMOUNT_AFT_DISCOUNT, old_amount, ";
            sql = sql + "ACTIVE, STATUS_MODIFY, BATCH_NO, UPDATE_DATE, UPDATE_TIME, USER_ID, OLD_DOCTOR_CODE,";
            sql = sql + "INV_IS_VOID, yyyy, mm, dd, RECEIPT_NO, RECEIPT_DATE, RECEIPT_MODULE,";
            sql = sql + "RECEIPT_TYPE_CODE, RECEIPT_MODE_CODE, REC_IS_VOID, REC_AMOUNT_BEF_DISCOUNT, REC_AMOUNT_OF_DISCOUNT,";
            sql = sql + "REC_PREMIUM_AMT, NOR_ALLOCATE_AMT, NOR_ALLOCATE_PCT, DR_AMT, DR_AMT_402, DR_AMT_406,";
            sql = sql + "DR_PREMIUM, DR_TAX_BASE, HP_AMT, HP_PREMIUM, HP_TAX, TAX_TYPE_CODE,";
            sql = sql + "DOCTOR_TREATMENT_CODE, COMPUTE_DAILY_DATE, COMPUTE_DAILY_TIME, COMPUTE_DAILY_USER_ID,";
            sql = sql + "PREMIUM_CHARGE_PCT, PREMIUM_VAT_PCT, TRANSACTION_DATE, DOCTOR_CATEGORY_CODE,";
            sql = sql + "EXCLUDE_TREATMENT, ";
            sql = sql + "PREMIUM_REC_AMT, INVOICE_TYPE, treatment_type)";

            sql = sql + " select distinct DTL.HOSPITAL_CODE, DTL.INVOICE_NO, DTL.INVOICE_DATE, ";   // ����¹ INVOICE_DATE -> RECEIPT_DATE
            sql = sql + "DTL.REC_IS_VOID||DTL.BILLING_SUB_GROUP_NO, DTL.ORDER_ITEM_CODE, DTL.order_date, ";
            sql = sql + "DTL.DOCTOR_CODE, DTL.DEPARTMENT_CODE, DTL.LOCATION_CODE, DTL.ADMISSION_TYPE_CODE, ";
            sql = sql + "DTL.AMOUNT_BEF_DISCOUNT, DTL.AMOUNT_OF_DISCOUNT, DTL.AMOUNT_AFT_DISCOUNT, DTL.old_amount, ";
            sql = sql + "DTL.ACTIVE, DTL.STATUS_MODIFY, '', to_char(sysdate,'yyyymmdd'), to_char(sysdate,'HH24MISS'),'" + Variables.getUserID() + "', DTL.OLD_DOCTOR_CODE, ";
            sql = sql + "DTL.INV_IS_VOID, '','','', rc.RECEIPT_NO, rc.RECEIPT_DATE, rc.RECEIPT_MODULE, ";
            sql = sql + "rc.RECEIPT_TYPE_CODE, DTL.RECEIPT_MODE_CODE, DTL.REC_IS_VOID, rc.inv_amt_bef_discount, rc.inv_amt_bef_discount - rc.inv_amt_bef_discount - rc.AMOUNT_BEF_DISCOUNT - rc.AMOUNT_OF_DISCOUNT, ";
            sql = sql + "DTL.REC_PREMIUM_AMT, DTL.NOR_ALLOCATE_AMT, DTL.NOR_ALLOCATE_PCT, DTL.DR_AMT, DTL.DR_AMT_402, DTL.DR_AMT_406,";
            sql = sql + "DTL.DR_PREMIUM, DTL.DR_TAX_BASE, DTL.HP_AMT, DTL.HP_PREMIUM, DTL.HP_TAX, DTL.TAX_TYPE_CODE,";
            sql = sql + "DTL.DOCTOR_TREATMENT_CODE, DTL.COMPUTE_DAILY_DATE, DTL.COMPUTE_DAILY_TIME, DTL.COMPUTE_DAILY_USER_ID,";
            sql = sql + "DTL.PREMIUM_CHARGE_PCT, DTL.PREMIUM_VAT_PCT, rc.RECEIPT_DATE, DTL.DOCTOR_CATEGORY_CODE,";
            sql = sql + "DTL.EXCLUDE_TREATMENT, ";
            sql = sql + "DTL.PREMIUM_REC_AMT, DTL.INVOICE_TYPE, DTL.treatment_type ";
            sql = sql + " from TRN_DAILY dtl left join ERP_receipt rc ";
            sql = sql + " ON rc.INVOICE_NO = DTL.INVOICE_NO AND rc.INVOICE_DATE = DTL.INVOICE_DATE ";
            sql = sql + " where rc.ISLOADED <> 'Y'";  // ����������� join ����Թ�ӹǹ������ record
            sql = sql + " and rc.INVOICE_NO = DTL.INVOICE_NO and rc.INVOICE_DATE = DTL.INVOICE_DATE ";
            sql = sql + " and rc.is_void = '" + Status.RECEIPT_IS_VOID + "'";
            sql = sql + " and rc.RECEIPT_DATE ='" + computeDate + "'";      // �� RECEIPT_DATE �� result_date
            sql = sql + " and STATUS_MODIFY <> '" + Status.STATUS_CALCULATED + "'";


             // ������������
             sql = sql + " and (rc.RECEIPT_NO || rc.RECEIPT_DATE || rc.INVOICE_NO || rc.INVOICE_DATE || rc.AMOUNT_BEF_DISCOUNT || rc.AMOUNT_OF_DISCOUNT || rc.doc_type || '1') " + 
                      " in (select er.RECEIPT_NO || er.RECEIPT_DATE || er.INVOICE_NO || er.INVOICE_DATE || er.AMOUNT_BEF_DISCOUNT || er.AMOUNT_OF_DISCOUNT || er.doc_type || count(er.RECEIPT_NO || er.RECEIPT_DATE || er.INVOICE_NO || er.INVOICE_DATE || er.AMOUNT_BEF_DISCOUNT || er.AMOUNT_OF_DISCOUNT || er.doc_type) " + 
                      "   from erp_receipt er where er.RECEIPT_DATE='" + computeDate + "' and er.isloaded <> 'Y'" + 
                      "   group by (er.RECEIPT_NO || er.RECEIPT_DATE || er.INVOICE_NO || er.INVOICE_DATE || er.AMOUNT_BEF_DISCOUNT || er.AMOUNT_OF_DISCOUNT || er.doc_type)" + 
                      "   having count(er.RECEIPT_NO || er.RECEIPT_DATE || er.INVOICE_NO || er.INVOICE_DATE || er.AMOUNT_BEF_DISCOUNT || er.AMOUNT_OF_DISCOUNT || er.doc_type) = 1) " +
                      "   order by INVOICE_NO, RECEIPT_DATE";

            rows = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            rows = -1;
        } finally {
               //Clean up resources, close the connection.
                stmt = null;
        }
        return rows;
    }    
    
    // Import data to Erp_receipt from integration
    public int ImportFromIntegration(String computeDate) {
        int rows = -1;
        try {
            String sql = "";
            sql = "copy from drfconnect/drfread@drfcon to doctor/welcome@dfpyt2 insert ERP_Receipt " +
            " (HOSPITAL_CODE, RECEIPT_NO, INVOICE_NO, INVOICE_NO_INSIDE, RECEIPT_DATE, INVOICE_DATE, " +
            " CASHIER_LOCATION_CODE, TITLE_NAME, FIRST_NAME, LAST_NAME, RT., VISIT_NO, ADMISSION_TYPE_CODE, " +
            " AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, RECEIPT_TYPE_CODE, RECEIPT_MODULE, INV_AMT_BEF_DISCOUNT, " +
            " INV_AMT_OF_DISCOUNT, IS_VOID, DOC_TYPE, UPDATE_DATE, UPDATE_TIME, USER_ID, ISLOADED) " +
            " using  select distinct HospitalCode, ReceiptNo, InvoiceNo, InvoiceNoInside, ReceiptDate, InvoiceDate, " +
            " CashierLocationCode, TitleName, FirstName, LastName, HospitalNo, VisitNo, AdmissionTypeCode, " +
            " AmountBefDiscount, AmountOfDiscount, ReceiptTypeCode, ReceiptModule, InvAmtBefDiscount, InvAmtOfDiscount, " +
            " IsVoid, DocType, to_char(sysdate,'yyyymmdd'), to_char(sysdate,'HH24MISS'), 'int', 'N' " +
            " from integration.erp2ss_drf2dd_rcpt_out where intflag='0' and receiptdate = '" + computeDate + "'";
            
            rows = conn.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
    
    public int updateFlag(String computeDate) {
        int rows = -1;
        try {
            rows = conn.executeUpdate("update integration.erp2ss_drf2dd_rcpt_out set intflag = '1' " +
                            " where receiptdate ='" + computeDate + "'");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }    
}
