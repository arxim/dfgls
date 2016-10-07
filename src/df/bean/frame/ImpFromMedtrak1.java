package df.bean.frame;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import javax.swing.JSeparator;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;

public class ImpFromMedtrak1 extends JInternalFrame {
    private JButton jButton1 = new JButton();
    private JButton jButton2 = new JButton();
    public DBConnection conn = new DBConnection();
    public String FIELD1 = "DOCTOR_CODE";
    public String FIELD2 = "RECEIPT_MODE_CODE";
    public String FIELD3 = "INVDATE";
    public String FIELD4 = "REVDATE";
    public String FIELD5 = "HN";
    public String FIELD6 = "PTNAME";
    public String FIELD7 = "ORDER_ITEM_CODE";
    public String FIELD8 = "ORDER_ITEM_DESCRIPTION";
    public String FIELD9 = "AMOUNT";
    public String FIELD10 = "RECEIPT_TYPE_CODE";
    public String FIELD11 = "DISCOUNT";
    public String FIELD12 = "EPISODE";
    public String FIELD13 = "BILLNO";
    public String FIELD14 = "ISLOADED";
    public String FIELD15 = "INVNO";
    public String FIELD16 = "LINE_NO"; //#LINENO
    public String FIELD17 = "ADMISSION_TYPE_CODE";
    public String FIELD18 = "LOCATION_CODE";
    public String FIELD19 = "TITLE_NAME";
    public String FIELD20 = "LAST_NAME";
    private JLabel lblMessage = new JLabel();
    private JLabel lblInvHeader = new JLabel();
    private JLabel lblInvDetail = new JLabel();
    private JLabel lblRec = new JLabel();
    private final String PAYMENT_MODULE_AR = "AR";
    private final String PAYMENT_MODULE_CASH = "CASH";
    private JLabel lblRecInv = new JLabel();
    private JSeparator jSeparator1 = new JSeparator();
    private JComboBox jcboYear = new JComboBox();
    private JComboBox jcboMonth = new JComboBox();
    private JComboBox jcboDay = new JComboBox();
    private JLabel jLabel2 = new JLabel();

    public ImpFromMedtrak1() {
        try {
            jbInit();
            this.lblMessage.setText("");
            this.lblInvHeader.setText("");
            this.lblInvDetail.setText("");
            this.lblRec.setText("");
            this.lblRecInv.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void destroy() {
        conn.Close();
        conn = null;
    }
    
    private void jbInit() throws Exception {
        this.getContentPane().setLayout( null );
        this.setSize(new Dimension(536, 322));
        this.setTitle( "Import data from Medtrak" );
        jButton1.setText("Import");
        jButton1.setBounds(new Rectangle(20, 15, 90, 30));
        jButton1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jButton1_actionPerformed(e);
                    }
                });
        jButton2.setText("Close");
        jButton2.setBounds(new Rectangle(115, 15, 90, 30));
        jButton2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jButton2_actionPerformed(e);
                    }
                });
        lblMessage.setBounds(new Rectangle(60, 190, 360, 35));
        lblMessage.setText("lblMessage");
        lblInvHeader.setText("lblInvHeader");
        lblInvHeader.setBounds(new Rectangle(60, 90, 370, 20));
        lblInvDetail.setText("lblInvDetail");
        lblInvDetail.setBounds(new Rectangle(60, 120, 370, 20));
        lblRec.setText("lblRec");
        lblRec.setBounds(new Rectangle(60, 150, 370, 20));
        lblRecInv.setText("lblRecInv");
        lblRecInv.setBounds(new Rectangle(60, 175, 370, 20));
        jSeparator1.setBounds(new Rectangle(15, 60, 495, 10));
        jcboYear.setBounds(new Rectangle(410, 15, 85, 30));
        jcboYear.setSelectedItem(JDate.getYear());
        jcboYear.setToolTipText("YYYY");
        jcboYear.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        jcboYear_actionPerformed(e);
                    }
                });
        jcboMonth.setBounds(new Rectangle(360, 15, 50, 30));
        jcboMonth.setSelectedItem(JDate.getMonth());
        jcboMonth.setToolTipText("MM");
        jcboDay.setBounds(new Rectangle(310, 15, 50, 30));
        jcboDay.setToolTipText("DD");
        jLabel2.setText("�ѹ��� :");
        jLabel2.setBounds(new Rectangle(265, 15, 40, 30));
        this.getContentPane().add(jLabel2, null);
        this.getContentPane().add(jcboDay, null);
        this.getContentPane().add(jcboMonth, null);
        this.getContentPane().add(jcboYear, null);
        this.getContentPane().add(jSeparator1, null);
        this.getContentPane().add(lblRecInv, null);
        this.getContentPane().add(lblRec, null);
        this.getContentPane().add(lblInvDetail, null);
        this.getContentPane().add(lblInvHeader, null);
        this.getContentPane().add(lblMessage, null);
        this.getContentPane().add(jButton2, null);
        this.getContentPane().add(jButton1, null);

                for (int i=1; i<32; i++) {
                    if (i<10) {  jcboDay.addItem("0" + i); }
                    else { jcboDay.addItem(i); }
                }
                for (int i=1; i<13; i++) {
                    if (i<10) jcboMonth.addItem("0"+i);
                    else jcboMonth.addItem(i);
                }
                for (int i=-1; i<2; i++) {
                    jcboYear.addItem(""+(Integer.parseInt(JDate.getYear())+i));
                }        
    }

    public String getComputeDate() {
        return this.jcboYear.getSelectedItem().toString() + this.jcboMonth.getSelectedItem().toString() + 
                this.jcboDay.getSelectedItem().toString();
    }
    
    private void jButton2_actionPerformed(ActionEvent e) {
        this.setVisible(false);
    }

    private void jButton1_actionPerformed(ActionEvent e) {
        boolean result = false;
        if (connect()) {
            this.getDBConnection().beginTrans();

            if (this.ImpToInvoiceHeader()) {
                if (this.ImpToInvoiceDetail()) {
                    if (this.ImpToReceiptFromAR_Header()) {
                        if (this.ImpToReceiptFromAR_Detail())  {
                            if (this.UpdateStatus()) {
                                if (this.UpdateLoaded()) {
                                    if (this.UpdateReceipFromARHeadLoaded()) {
                                        if (this.updateDoctorCodeToHosp()) {
                                            if (this.UpdateReceipFromARDetailLoaded()) {
                                                result = true; 
                                            }
                                        }   // UpdateReceipFromARDetailLoaded
                                    }   // UpdateReceipFromARHeadLoaded
                                }   // UpdateLoaded
                            }   // UpdateStatus
                        }   // ImpToReceiptFromAR_Detail
                    }   // ImpToReceiptFromAR_Header
                }   // ImpToInvoiceDetail
            }   // ImpToInvoiceHeader
            
            if (result) this.getDBConnection().commitTrans();
            if (!result) this.getDBConnection().rollBackTrans();
            if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
            if (result) DialogBox.ShowInfo("Import data are completed.");
            if (!result) DialogBox.ShowError("Import data are failed.");
        }
    }
    
    public void setDBConnection(DBConnection conn) {
        this.conn = conn;
    }
    public DBConnection getDBConnection() {
        return this.conn;
    }
    
    public boolean connect() {
        boolean ret = false;
        try {
            // connect to databse
    //            conn = new DBConnection();
            lblMessage.setText("Open Connection.");
//            conn.setUserName(DBConnection.CONN_USER);
//            conn.setPassword(DBConnection.CONN_PASS);
            if (conn.Connect()) {
                lblMessage.setText("Connected.");
                ret = true;
            } else {
                DialogBox.ShowError("ImpFromMedtrak.connect() : Connect to database failed. !!!!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Connection failed.");
            DialogBox.ShowError("ImpFromMedtrak.connect() : Database connection failed.");
        }

        return ret;
    }    
    
    public boolean ImpToInvoiceHeader() {
        String sql = "", groupBy = "", sql1 = "";
        sql = "insert into TC_INVOICE_HEADER (INVOICE_NO, INVOICE_DATE, TITLE_NAME, ";
        sql = sql + "FIRST_NAME, LAST_NAME, ";
        sql = sql + "RT., visit_no, ADMISSION_TYPE_CODE, ";
        sql = sql + "AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, AMOUNT_AFT_DISCOUNT )";
        
        sql1 = sql1 + FIELD15;                // INVOICE_NO
        sql1 = sql1 + "," + FIELD3;           // INVOICE_DATE
        sql1 = sql1 + "," + FIELD19;          // TITLE_NAME 
        sql1 = sql1 + "," + FIELD6;           // FIRST_NAME
        sql1 = sql1 + "," + FIELD20;          // LAST_NAME
        sql1 = sql1 + "," + FIELD5;           // RT.
        sql1 = sql1 + "," + FIELD12;          // visit_no
        sql1 = sql1 + "," + FIELD17;              // ADMISSION_TYPE_CODE
        groupBy = sql1;
        sql1 = sql1 + ",sum(" + FIELD9 + ")";     // AMOUNT_BEF_DISCOUNT
        sql1 = sql1 + ",sum(" + FIELD11 + ")";    // AMOUNT_OF_DISCOUNT
        sql1 = sql1 + ",sum(" + FIELD9 + ")-sum(" + FIELD11 + ")"; // AMOUNT_AFT_DISCOUNT
//        sql = sql + ",0";                       // percent_of_discount
//        sql = sql + ",'1'";                   // HOSPITAL_CODE
//        sql = sql + ",''";                    // LOCATION_CODE
//        sql = sql + ",''";                    // payor_office_code
//        sql = sql + ",''";                    // payor_name
//        sql = sql + ",'20061215'";              // UPDATE_DATE
//        sql = sql + ",''";                      // UPDATE_TIME
//        sql = sql + ",'sys'";                   // USER_ID
//        sql = sql + ",'20061215'";              // TRANSACTION_DATE
//        sql = sql + ",'N'";                     // isloaded
        sql = sql + " select " + sql1 + " from IMP_INVOICE_MEDTRAK where ISLOADED <> 'Y'";
        sql = sql + " and INVDATE ='" + this.getComputeDate() + "'";
//        sql = sql + " and InvNo not in (select INVOICE_NO from TC_INVOICE_HEADER)";
        sql = sql + " group by ";
        sql = sql + groupBy;
        lblMessage.setText("Import Invoice Header.");
        lblInvHeader.setText("Insert into Invoice Header ... ");
        int rows = conn.executeUpdate(sql);
        if (rows >-1) { 
            lblMessage.setText("Import Invoice Header successful.");
            lblInvHeader.setText("Insert into Invoice Header ... " + rows + " rows success");
            lblInvHeader.setForeground(Color.BLACK);
            return true; 
        }
        lblMessage.setText("Import Invoice Header failed.");
        lblInvHeader.setText("Insert into Invoice Header ... " + rows + " rows fail");
        lblInvHeader.setForeground(Color.RED);
        return false;
    }
    
    public boolean ImpToInvoiceDetail() {
        String sql = "";
        sql = "insert into TC_INVOICE_DETAIL (Billing_sub_group_no, order_date, order_time, doctor_order_code, ";
        sql = sql + "update_order_date, update_order_time, doctor_result_code, update_result_date, update_result_time, ";
        sql = sql + "order_note, DEPARTMENT_CODE, LOCATION_CODE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, ";
        sql = sql + "percent_of_discount, AMOUNT_AFT_DISCOUNT, INVOICE_NO, HOSPITAL_CODE, ORDER_ITEM_CODE, ";
        sql = sql + "INVOICE_DATE, doctor_cure_code, OLD_DOCTOR_CODE, update_cure_date, update_cure_time, isloaded, Billing_suffix_No)";
        
        sql = sql + " select " + FIELD16;   // Billing_sub_group_no
        sql = sql + ",''";                  // order_date
        sql = sql + ",''";                  // order_time
        sql = sql + ",''";                  // doctor_order_code
        sql = sql + ",''";                  // update_order_date
        sql = sql + ",''";                  // update_order_time
        sql = sql + ",''";                  // doctor_result_code
        sql = sql + ",''";                  // update_result_date
        sql = sql + ",''";                  // update_result_time
        sql = sql + ",''";                  // order_note
        sql = sql + ",'999'";                  // DEPARTMENT_CODE
        sql = sql + "," + FIELD18;          // LOCATION_CODE
        sql = sql + "," + FIELD9;           // AMOUNT_BEF_DISCOUNT
        sql = sql + "," + FIELD11;          // AMOUNT_OF_DISCOUNT
        sql = sql + ",0";                   // percent_of_discount
        sql = sql + "," + FIELD9 + "-" + FIELD11;   // AMOUNT_AFT_DISCOUNT
        sql = sql + "," + FIELD15;                  // INVOICE_NO
        sql = sql + ",'" + Variables.getHospitalCode() + "'";   // HOSPITAL_CODE
        sql = sql + "," + FIELD7;           // ORDER_ITEM_CODE
        sql = sql + "," + FIELD3;           // INVOICE_DATE
        sql = sql + "," + FIELD1;           // doctor_cure_code
         sql = sql + "," + FIELD1;           // OLD_DOCTOR_CODE
        sql = sql + "," + FIELD3;           // update_cure_date
        sql = sql + ",''";                  // update_cure_time
        sql = sql + ",'N'";                 // isloaded
         sql = sql + "," + FIELD16;         // billing_suffix_no
        sql = sql + " from IMP_INVOICE_MEDTRAK where ISLOADED <> 'Y'";
        sql = sql + " and INVDATE ='" + this.getComputeDate() + "'";

        lblMessage.setText("Import Invoice Detail.");
        lblInvDetail.setText("Insert into Invoice Detail ... ");
        int rows = conn.executeUpdate(sql);
        if (rows >-1) { 
            lblMessage.setText("Import Invoice Detail successful.");
            lblInvDetail.setText("Insert into Invoice Detail ... " + rows + " rows success");
            lblInvDetail.setForeground(Color.BLACK);
            return true; 
        }
        lblMessage.setText("Import Invoice Detail failed.");
        lblInvDetail.setText("Insert into Invoice Detail ... " + rows + " rows success");
        lblInvDetail.setForeground(Color.RED);
        return false;
    }
    /*
    public boolean ImpToReceipt() {
        String sql = "";
        sql = "insert into RECEIPT (HOSPITAL_CODE, RECEIPT_NO, INVOICE_NO, RECEIPT_DATE, ";
        sql = sql + "cashier_location_code, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, with_holding_tax, ";
        sql = sql + "receipt_from, UPDATE_DATE, UPDATE_TIME, USER_ID, TRANSACTION_DATE, transaction_time, ";
        sql = sql + "ACTIVE, RECEIPT_TYPE_CODE, payment_module, line_no)";
        
        sql = sql + " select '" + Variables.getHospitalCode() + "'";
        sql = sql + "," + FIELD13;
        sql = sql + "," + FIELD15;
        sql = sql + "," + FIELD4;
        sql = sql + "," + FIELD18;
        sql = sql + "," + FIELD9;
        sql = sql + "," + FIELD11;
        sql = sql + ",'0'";
        sql = sql + "," + FIELD2;
        sql = sql + ",'" + JDate.getDate() + "'";
        sql = sql + ",'" + JDate.getTime() + "'";
        sql = sql + ",'" + Variables.getUserID() + "'";
        sql = sql + ",'" + JDate.getDate() + "'";
        sql = sql + ",'" + JDate.getTime() + "'";
        sql = sql + ",'1'";
        sql = sql + "," + FIELD10;
        sql = sql + ",'" + PAYMENT_MODULE_CASH + "'";
        sql = sql + "," + FIELD16;
        sql = sql + " from IMP_INVOICE_MEDTRAK where ISLOADED <> 'Y' and (BILLNO is not null or BILLNO <> '')"; // #Oracle
//        sql = sql + " from IMP_INVOICE_MEDTRAK where ISLOADED <> 'Y' and (BILLNO is not null and BILLNO <> '')"; // #sql
        
        lblMessage.setText("Import Receipt.");
        lblRec.setText("Insert into Receipt ... ");
        int rows = conn.executeUpdate(sql);
        if (rows > -1) { 
            lblMessage.setText("Import Receipt successful.");
            lblRec.setText("Insert into Receipt ... " + rows + " rows success");
            lblRec.setForeground(Color.BLACK);
            return true; 
        }
        lblMessage.setText("Import Receipt failed.");
        lblRec.setText("Insert into Receipt ... " + rows + " rows success");
        lblRec.setForeground(Color.RED);
        return false;
    }
    */
    public boolean UpdateLoaded() {
        lblMessage.setText("Update load status.");
        int rows = conn.executeUpdate("update imp_invoice_medtrak set isloaded='Y' where isloaded <> 'Y' and INVDATE ='" + this.getComputeDate() + "'");
        if (rows > -1) { 
            lblMessage.setText("Update load status ... " + rows + " rows successful");
            return true;
        }
        lblMessage.setText("Update load status ... " + rows + " rows failed.");
        lblMessage.setForeground(Color.RED);
        return false;
    }
    
    public boolean UpdateStatus() {
        lblMessage.setText("Update TC_Invoice_Header status ...");
        int rows = conn.executeUpdate("update tc_invoice_header set UPDATE_DATE='" + JDate.getDate() + "', UPDATE_TIME='" + JDate.getTime() + "', HOSPITAL_CODE='" + Variables.getHospitalCode() + "', isloaded='N' where isloaded <> 'Y' and TRANSACTION_DATE ='" + this.getComputeDate() + "'");
        if (rows > -1) { 
            lblMessage.setText("Update tc_invoice_header ... " + rows + " rows successful");
            lblMessage.setForeground(Color.BLACK);
            return true;
        }
        lblMessage.setText("Update tc_invoice_header ... " + rows + " rows failed.");
        lblMessage.setForeground(Color.RED);
        return false;
    }
    
    
    public boolean UpdateReceipFromARHeadLoaded() {
        lblMessage.setText("Update Receipt From AR Loaded status.");
        int rows = conn.executeUpdate("update imp_invoice_Head set isloaded='Y' where isloaded <> 'Y' and REVANUE_DATE ='" + this.getComputeDate() + "'");
        if (rows > -1) { 
            lblMessage.setText("Update load status ... " + rows + " rows successful");
            return true;
        }
        lblMessage.setText("Update load status ... " + rows + " rows failed.");
        lblMessage.setForeground(Color.RED);
        return false;
    }
    public boolean UpdateReceipFromARDetailLoaded() {
        lblMessage.setText("Update Receipt From AR Load status.");
        int rows = conn.executeUpdate("update imp_invoice_Detail set isloaded='Y' where isloaded <> 'Y' and REVANUE_DATE ='" + this.getComputeDate() + "'");
        if (rows > -1) { 
            lblMessage.setText("Update Receipt From AR Load status ... " + rows + " rows successful");
            return true;
        }
        lblMessage.setText("Update Receipt From AR Load status ... " + rows + " rows failed.");
        lblMessage.setForeground(Color.RED);
        return false;
    }
    
    
    
    
    // Import receipt from Imp_Invoice_Head To Receipt_Header
    public boolean ImpToReceiptFromAR_Header() {
        String sql = "";
        sql = "insert into RECEIPT_HEADER (HOSPITAL_CODE, RECEIPT_NO, INVOICE_NO, invoice_no_inside, RECEIPT_DATE, INVOICE_DATE, ";
        sql = sql + "TITLE_NAME, FIRST_NAME, LAST_NAME, RT., Visit_No, ADMISSION_TYPE_CODE, ";
        sql = sql + "cashier_location_code, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, ";
        sql = sql + "Receipt_Module, UPDATE_DATE, UPDATE_TIME, USER_ID, ACTIVE)";

        sql = sql + " select distinct '" + Variables.getHospitalCode() + "'";
        sql = sql + ",IMP_INVOICE_HEAD.INVOICE_NO";     // RECEIPT_NO
        sql = sql + ",IMP_INVOICE_HEAD.INVOICE_NO";     // INVOICE_NO
        sql = sql + ",IMP_INVOICE_HEAD.INVOICE_NO";     // invoice_no_inside
        sql = sql + ",IMP_INVOICE_HEAD.REVANUE_DATE";   // RECEIPT_DATE
        sql = sql + ",IMP_INVOICE_HEAD.INVOICE_DATE";   // INVOICE_DATE
        sql = sql + ",''";  // TITLE_NAME
        sql = sql + ",IMP_INVOICE_HEAD.PATIENT_NAME";  // FIRST_NAME
        sql = sql + ",''";  // LAST_NAME
        sql = sql + ",IMP_INVOICE_HEAD.PATIENT_NO";  // RT.
        sql = sql + ",''";  // Visit_No
        sql = sql + ",IMP_INVOICE_HEAD.ADMISSION_TYPE_CODE"; // ADMISSION_TYPE_CODE
        sql = sql + ",IMP_INVOICE_HEAD.INVOICE_LOCATION_ID";
        sql = sql + ",0";
        sql = sql + ",0";
        sql = sql + ",IMP_INVOICE_DETAIL.PAYMENT_MODULE";
        sql = sql + ",'" + JDate.getDate() + "'";
        sql = sql + ",'" + JDate.getTime() + "'";
        sql = sql + ",'" + Variables.getUserID() + "'";
        sql = sql + ",'1'";
        sql = sql + " from IMP_INVOICE_HEAD, IMP_INVOICE_DETAIL where IMP_INVOICE_DETAIL.ISLOADED <> 'Y' ";
        sql = sql + " and IMP_INVOICE_HEAD.ISLOADED <> 'Y' ";
        sql = sql + " and IMP_INVOICE_HEAD.HOSPITAL_CODE=IMP_INVOICE_DETAIL.HOSPITAL_CODE ";
        sql = sql + " and IMP_INVOICE_HEAD.INVOICE_NO=IMP_INVOICE_DETAIL.INVOICE_NO ";
        //sql = sql + " and (IMP_INVOICE_DETAIL.RECEIPT_TYPE_CODE <> '' or IMP_INVOICE_DETAIL.RECEIPT_TYPE_CODE is not null)";
        sql = sql + " and IMP_INVOICE_DETAIL.REVANUE_DATE ='" + this.getComputeDate() + "'";
        sql = sql + " and IMP_INVOICE_HEAD.REVANUE_DATE = '" + this.getComputeDate() + "'";
        sql = sql + " and IMP_INVOICE_DETAIL.REVANUE_DATE = IMP_INVOICE_HEAD.REVANUE_DATE";
        
        lblMessage.setText("Import Receipt Header From AR.");
        lblRecInv.setText("Insert into Receipt Header from AR ... success");
        int rows = conn.executeUpdate(sql);
        if (rows > -1) { 
            lblMessage.setText("Import Receipt Header from AR successful.");
            lblRecInv.setText("Insert into Receipt Header from AR ... " + rows + " rows success");
            lblRecInv.setForeground(Color.BLACK);
            return true; 
        }
        lblMessage.setText("Import Receipt Header from AR failed.");
        lblRecInv.setText("Insert into Receipt Header from AR... " + rows + " rows success");
        lblRecInv.setForeground(Color.RED);
        return false;
    }
    
    
    // Import receipt from Imp_Invoice_Detail
    public boolean ImpToReceiptFromAR_Detail() {
        String sql = "";
        sql = "insert into RECEIPT_DETAIL (HOSPITAL_CODE, RECEIPT_NO, INVOICE_NO, invoice_no_inside, RECEIPT_DATE, ";
        sql = sql + "INVOICE_DATE, Billing_Sub_Group_No, ORDER_ITEM_CODE, DOCTOR_CODE, ";
        sql = sql + "Department_Code, LOCATION_CODE, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, ";
        sql = sql + "Percent_Of_Discount, AMOUNT_AFT_DISCOUNT, ";
        sql = sql + "Receipt_Type_Code, BILLING_SUFFIX_NO, UPDATE_DATE, UPDATE_TIME, USER_ID, Receipt_Module,";
        sql = sql + "YYYY, MM, DD, BATCH_NO, STATUS_MODIFY, OLD_DOCTOR_CODE)";
        
        sql = sql + " select distinct '" + Variables.getHospitalCode() + "'";
        sql = sql + ",IMP_INVOICE_HEAD.INVOICE_NO";     // RECEIPT_NO
        sql = sql + ",IMP_INVOICE_HEAD.INVOICE_NO";     // INVOICE_NO
        sql = sql + ",IMP_INVOICE_HEAD.INVOICE_NO";     // invoice_no_inside
        sql = sql + ",IMP_INVOICE_HEAD.REVANUE_DATE";   // RECEIPT_DATE
        sql = sql + ",IMP_INVOICE_HEAD.INVOICE_DATE, IMP_INVOICE_DETAIL.LINE_NO";       // INVOICE_DATE , BILLING_SUB_GROUP_NO
        sql = sql + ",IMP_INVOICE_DETAIL.ORDER_ITEM_CODE";      // ORDER_ITEM_CODE
        sql = sql + ",IMP_INVOICE_DETAIL.DOCTOR_CODE";          // DOCTOR_CODE
        sql = sql + ",IMP_INVOICE_DETAIL.DEPARTMENT_CODE";      // DEPARTMENT CODE
        sql = sql + ",substr(IMP_INVOICE_HEAD.INVOICE_NO,3,1)";   // LOCATION_CODE
        sql = sql + ",IMP_INVOICE_DETAIL.AMOUNT";
        sql = sql + ",IMP_INVOICE_DETAIL.DISCOUNT_AMT";
        sql = sql + ",0,IMP_INVOICE_DETAIL.AMOUNT-IMP_INVOICE_DETAIL.DISCOUNT_AMT";     // PERCENT_OF_DISCOUNT, AMOUNT_AFT_DISCOUNT
        sql = sql + ",IMP_INVOICE_DETAIL.RECEIPT_TYPE_CODE";
        sql = sql + ",IMP_INVOICE_DETAIL.LINE_NO";       // billing_suffix_no
        sql = sql + ",'" + JDate.getDate() + "'";
        sql = sql + ",'" + JDate.getTime() + "'";
        sql = sql + ",'" + Variables.getUserID() + "'";
        sql = sql + ",IMP_INVOICE_DETAIL.PAYMENT_MODULE";
        sql = sql + ",''";
        sql = sql + ",''";
        sql = sql + ",''";
        sql = sql + ",''";
        sql = sql + ",'I'";
        sql = sql + ",IMP_INVOICE_DETAIL.DOCTOR_CODE";          // DOCTOR_CODE
        sql = sql + " from IMP_INVOICE_HEAD, IMP_INVOICE_DETAIL where IMP_INVOICE_DETAIL.ISLOADED <> 'Y'";
        sql = sql + " and IMP_INVOICE_HEAD.HOSPITAL_CODE=IMP_INVOICE_DETAIL.HOSPITAL_CODE ";
        sql = sql + " and IMP_INVOICE_HEAD.INVOICE_NO=IMP_INVOICE_DETAIL.INVOICE_NO";
//        sql = sql + " and (IMP_INVOICE_DETAIL.RECEIPT_TYPE_CODE <> '' or IMP_INVOICE_DETAIL.RECEIPT_TYPE_CODE is not null)";
        sql = sql + " and IMP_INVOICE_DETAIL.REVANUE_DATE ='" + this.getComputeDate() + "'";
        sql = sql + " and IMP_INVOICE_HEAD.REVANUE_DATE ='" + this.getComputeDate() + "'";
        sql = sql + " and IMP_INVOICE_DETAIL.REVANUE_DATE = IMP_INVOICE_HEAD.REVANUE_DATE";
        
        lblMessage.setText("Import Receipt Detail From AR.");
        lblRecInv.setText("Insert into Receipt Detail from AR ... success");
        int rows = conn.executeUpdate(sql);
        if (rows > -1) { 
            lblMessage.setText("Import Receipt Detail from AR successful.");
            lblRecInv.setText("Insert into Receipt Detail from AR ... " + rows + " rows success");
            lblRecInv.setForeground(Color.BLACK);
            return true; 
        }
        lblMessage.setText("Import Receipt Detail from AR failed.");
        lblRecInv.setText("Insert into Receipt Detail from AR... " + rows + " rows success");
        lblRecInv.setForeground(Color.RED);
        return false;
    }
    
    public boolean updateDoctorCodeToHosp() {
        String sql = "";
        sql = "update RECEIPT_DETAIL set DOCTOR_CODE='HOSP' where RECEIPT_DATE = '" + this.getComputeDate() + "'";
        sql = sql + " and RECEIPT_NO in (select RECEIPT_NO from receipt_header";
        sql = sql + " where receipt_header.RECEIPT_DATE ='" + this.getComputeDate() + "'";
        sql = sql + " and receipt_header.CASHIER_LOCATION_CODE='01LDC2')";
        
//        lblMessage.setText("update Cashier_location_code '01LDC2' from Receipt Detail of AR.");
//        lblRecInv.setText("update from Receipt Detail of AR ... success");
        int rows = conn.executeUpdate(sql);
        if (rows > -1) { 
//            lblMessage.setText("update Cashier_location_code '01LDC2' from Receipt Detail of AR successful.");
//            lblRecInv.setText("update from Receipt Detail of AR ... " + rows + " rows success");
//            lblRecInv.setForeground(Color.BLACK);
            return true; 
        }
        lblMessage.setText("update Cashier_location_code '01LDC2' from Receipt Detail of AR failed.");
        lblRecInv.setText("update from Receipt Detail of AR... " + rows + " rows success");
        lblRecInv.setForeground(Color.RED);
        return false;
    }


    private void jcboYear_actionPerformed(ActionEvent e) {
    }
}
