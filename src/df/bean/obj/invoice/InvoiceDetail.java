package df.bean.obj.invoice;

import java.util.List;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.OrderItem;

public class InvoiceDetail {
//    OrderItem orderItem = null;
    Invoice invoice = null;
    df.bean.db.table.InvoiceDetail invoiceDetail = null;
    
    private String hospitalCode;
    private String orderItemCode;
    private String invoiceNo;
    private String lineNo;
    public DBConnection conn = null;
    
    public InvoiceDetail(String hospitalCode, String orderItemCode, String invoiceNo, String lineNo, DBConnection conn) {
        this.hospitalCode = hospitalCode;
        this.setOrderItemCode(orderItemCode);
        this.invoiceNo = invoiceNo;
        this.lineNo = lineNo;
        
        this.conn = conn;
//        this.newOrderItem();
        this.newInvoiceDetail();
        this.newInvoice();
    }
    protected void finalize() {
//        orderItem = null;
        invoice = null;
        invoiceDetail = null;
    }
    public void setDBConnection(DBConnection conn) {
        this.conn = conn;
    }
    public DBConnection getDBConnection() {
        return this.conn;
    }
/*    private void newOrderItem() {
        setOrderItem(new OrderItem(this.orderItemCode, this.hospitalCode, this.getDBConnection()));
    } */
    private void newInvoiceDetail() {
        setInvoiceDetail(new df.bean.db.table.InvoiceDetail(this.hospitalCode, this.invoiceNo, this.orderItemCode, this.lineNo, this.getDBConnection()));
    }
    private void newInvoice() {
        setInvoice(new Invoice(this.invoiceNo, this.hospitalCode, this.getDBConnection()));
    }
/*
    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    } */

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public df.bean.db.table.InvoiceDetail getInvoiceDetail() {
        return invoiceDetail;
    }

    public void setInvoiceDetail(df.bean.db.table.InvoiceDetail invoiceDetail) {
        this.invoiceDetail = invoiceDetail;
    }
    /*
    // don't used
    public boolean setStatusCalculated() {
        boolean ret = false;
        String sql = "update INVOICE_DETAIL set STATUS_MODIFY='" + Status.STATUS_CALCULATED + "'";
        sql = sql + " ,UPDATE_DATE='" + JDate.getDate() + "'";
        sql = sql + " ,Update_Time='" + JDate.getTime() + "'";
        sql = sql + " where HOSPITAL_CODE='" + this.hospitalCode + "'";
        sql = sql + " and ORDER_ITEM_CODE='" + this.orderItemCode + "'";
        sql = sql + " and INVOICE_NO='" +  this.invoiceNo + "'";
        sql = sql + " and billing_Sub_Group_No='" + this.lineNo + "'";
        if (this.conn.executeUpdate(sql)>-1) {
            ret = true;
        }
        return ret;
    } */

    public String getOrderItemCode() {
        return orderItemCode;
    }

    public void setOrderItemCode(String orderItemCode) {
        this.orderItemCode = orderItemCode;
    }
}
