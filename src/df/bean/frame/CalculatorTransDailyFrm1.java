package df.bean.frame;

import df.bean.process.ProcessTransDaily;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

//my import
import javax.swing.table.DefaultTableModel;
import df.bean.db.table.Status;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.JDate;
import df.bean.obj.util.passParameter;

public class CalculatorTransDailyFrm1 extends CalculatorFrm1 {
    private JSeparator jSeparator4 = new JSeparator();
    private JLabel jLabel1 = new JLabel();
    private JSeparator jSeparator5 = new JSeparator();
    private String menuName;
    private JComboBox jcboYear = new JComboBox();
    private JComboBox jcboMonth = new JComboBox();
    private JComboBox jcboDay = new JComboBox();
    private JLabel jLabel2 = new JLabel();

    public CalculatorTransDailyFrm1() {
        try {
//            if (connect())
                jbInit();
        } catch (Exception e) {
            DialogBox.ShowError("Error in CalculatorTransDailyFrm\n"+e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void finalize() {
        tblMessage = null;
    }

    private void jbInit() throws Exception {
        this.setTitle("Invoice Daily Calculator");
        this.setSize(new Dimension(746, 469));
        menuName = passParameter.menuName;
        jPanelLayout.setLayout(null);
        getLstDoctorGroupCode().setBounds(new Rectangle(5, 80, 200, 330));
        statusBar.setText( "Status :" );
        this.jLabel1.setIcon(imagePhyathai);


        start.setIcon(imageStart);
        start.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        start_actionPerformed(e);
                    }
                });
        stop.setIcon(imageStop);
        stop.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        stop_actionPerformed(e);
                    }
                });
        close.setIcon(imageClose);
        close.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        close_actionPerformed(e);
                    }
                });
        
        getLstDoctorGroupCode().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        
        scrollPane = new JScrollPane(getLstDoctorGroupCode());
        scrollPane.setBounds(new Rectangle(10, 180, 80, 215));
        jPanelLayout.add(getLblDoctor(), null);
        jPanelLayout.add(getLblHospital(), null);
        
        // scroll pane for hospital
        scrollPaneHospitalCode = new JScrollPane(getLstHospitalCode());
        scrollPaneHospitalCode.setBounds(new Rectangle(10, 85, 80, 65));
       
        //Table Scroll Pane
         String[] columnNames={ "YYYY", "MM", "DD", "Hospital", "Inv No",
                         "Inv Date","Bill No","Rev Date","Department", "Location", "HN#", "PTName", "Order Item Code", "Billing Sub Group No",
                         "Rec Mode", "Rec Type", "Payment Module", "Doctor", "Inv Amt", "INV_DISCOUNT_AMT", 
                         "Rec Amt", "Rec Premium Amt", "Nor Allocate_Amt", "Nor Allocate Pct", 
                         "Dr Amt", "Dr Amt 402", "Dr Amt 406", "Dr Premium", "Dr TaxBase", "Tax Type", 
                         "HP_AMT", "HP Premium", "HP_TAX", "Status_Code", "Admission Type", 
                         "Dr Treatment Code", "Premium Pct", "Premium Vat", "Order Date", "Category", "Ex_Treatment", "Premium Rec Amt"};
        tableModel = new DefaultTableModel(columnNames, row);
        tblMessage = new JTable(tableModel);
        scrollPaneMessage = new JScrollPane(tblMessage); 
        scrollPaneMessage.setBounds(new Rectangle(95, 65, 635, 330));
        jPanelLayout.add(jLabel2, null);
        jPanelLayout.add(jcboDay, null);
        jPanelLayout.add(jcboMonth, null);
        jPanelLayout.add(jcboYear, null);
        jPanelLayout.add(jSeparator5, null);
        jPanelLayout.add(jLabel1, null);
        jPanelLayout.add(jSeparator4, null);
        jPanelLayout.add(close, null);
        jPanelLayout.add(stop, null);
        jPanelLayout.add(start, null);
        jPanelLayout.add(scrollPaneMessage, BorderLayout.CENTER);
        jPanelLayout.add(scrollPaneHospitalCode, null);
        jPanelLayout.add(scrollPane, null);
        tblMessage.setAutoscrolls(true);
        tblMessage.setAutoResizeMode(0);


        //        start.setText("Start");
        start.setBounds(new Rectangle(30, 10, 55, 35));
        start.setActionCommand("Start");
        start.setToolTipText("Start");
        //        stop.setText("Stop");
        stop.setBounds(new Rectangle(95, 10, 55, 35));
        stop.setActionCommand("Stop");
        stop.setToolTipText("Stop");
        //        close.setText("Close");
        close.setBounds(new Rectangle(160, 10, 55, 35));
        close.setActionCommand("Close");
        close.setToolTipText("Close");
        jSeparator4.setBounds(new Rectangle(10, 50, 720, 2));
        jLabel1.setSize(new Dimension(134, 41));
        jLabel1.setBounds(new Rectangle(590, 5, 135, 40));
        jSeparator5.setBounds(new Rectangle(10, 410, 720, 2));


        jcboYear.setBounds(new Rectangle(455, 10, 85, 30));
        jcboYear.setSelectedItem(JDate.getYear());
        jcboYear.setToolTipText("YYYY");
        jcboMonth.setBounds(new Rectangle(405, 10, 50, 30));
        jcboMonth.setSelectedItem(JDate.getMonth());
        jcboMonth.setToolTipText("MM");
        jcboDay.setBounds(new Rectangle(355, 10, 50, 30));
        jcboDay.setToolTipText("DD");
        jLabel2.setText("�ѹ��� :");
        jLabel2.setBounds(new Rectangle(300, 10, 40, 30));
        getLstHospitalCode().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        addHospitalCodeToList();
                
        getLblHospital().setText("Hospital");
        getLblHospital().setBounds(new Rectangle(10, 60, 70, 25));
        getLblDoctor().setText("Invoice No.");
//        getLblDoctor().setText("Doctor");
        getLblDoctor().setBounds(new Rectangle(10, 155, 70, 25));
                
        jPanelLayout.add(scrollPane, null);
        getContentPane().add(jPanelLayout, BorderLayout.CENTER);

        this.setHospitalCode("null");
        this.setSelected(true);

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
    
    @Override
    public DefaultListModel elementAdd(String hospitalCode) {
        DefaultListModel r = new DefaultListModel();

        // ���͡�Ţ��� invoice �ҡ��¡�� invoice � table INVOICE_DETAIL ������͡੾����¡�÷���ѧ���ӹǳ�ҤԴ
        // ��Ƿ��١�ӹǳ����Ǩ����١���Ҥӹǳ�ա ���ͧ�ҡ �Ѵ੾�� STATUS_MODIFY <> 'C'
       String sql = "select distinct INVOICE_NO " +
                    " from TRN_DAILY " +
                    " where HOSPITAL_CODE = '" + hospitalCode + "'" +
                    " and (STATUS_MODIFY <> '" + Status.STATUS_CALCULATED + "'" +
                    " or STATUS_MODIFY is null)" +
                    " and (BATCH_NO is null or BATCH_NO = '')" +
                    " and ACTIVE = '1' " +
                    " and AMOUNT_AFT_DISCOUNT <> 0 " +
                    " and TRANSACTION_DATE = '" + this.getComputeDate() + "'" +     // #20071123#
                    " and COMPUTE_DAILY_DATE is null " +
//                    " and (RECEIPT_DATE = '" + this.getComputeDate() + "'" +
//                    " or INVOICE_DATE = '" + this.getComputeDate() + "')" +
//                    " and  (DR_AMT <= 0 or DR_AMT is null) " +
                    " and DOCTOR_CODE <> '" + Status.DOCTOR_CODE_UNREAD + "'";
        sql = sql + " order by INVOICE_NO";

        ResultSet rs = conn.executeQuery(sql);
        try {
            r.clear();
            while (rs.next()) {
                r.addElement(rs.getString("INVOICE_NO"));
            }
        } catch (SQLException e) {
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
        return r;
    }
    
    private void close_actionPerformed(ActionEvent e) {
        this.setVisible(false);
        //dispose();
    }

    private void start_actionPerformed(ActionEvent e) {
        if (connect()) {
            addHospitalCodeToList();
            this.setProcess(new ProcessTransDaily(this.conn));
//            this.getProcess().setCalculatorFrm(this);
            this.getProcess().running = true;
            this.getProcess().working = true;
            this.setRun();
//            this.getProcess().start();
        } else { 
            DialogBox.ShowError("�������ö�Դ��͡Ѻ�ҹ��������");
        }        
    }
    private void stop_actionPerformed(ActionEvent e) {
        this.stopProcess();
    }
    @Override
    public void setRun() {
        start.setEnabled(false);
        stop.setEnabled(true);
    }
    @Override
    public void setStop() {
        start.setEnabled(true);
        stop.setEnabled(false);        
    }    
/*
    private void this_componentResized(ComponentEvent e) {
        scrollPaneMessage.setSize(jPanelLayout.getWidth()-110, jPanelLayout.getHeight()-60);
        this.scrollPane.setSize(this.scrollPane.getWidth(), jPanelLayout.getHeight()-185);
        jToolBarDFCalculator.setSize(jPanelLayout.getWidth(), jToolBarDFCalculator.getHeight());
    } */
     public String getComputeDate() {
         return this.jcboYear.getSelectedItem().toString() + this.jcboMonth.getSelectedItem().toString() + 
                 this.jcboDay.getSelectedItem().toString();
     }


}
