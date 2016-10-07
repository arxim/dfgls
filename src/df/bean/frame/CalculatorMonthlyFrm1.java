package df.bean.frame;

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

import df.bean.obj.util.DialogBox;
import df.bean.obj.util.JDate;
import df.bean.obj.util.passParameter;

public class CalculatorMonthlyFrm1 extends CalculatorFrm1 {
    private JSeparator jSeparator4 = new JSeparator();
    private JLabel jLabel1 = new JLabel();
    private JSeparator jSeparator5 = new JSeparator();
    private String menuName;
    private JLabel lblPaymentDate = new JLabel();
    private JComboBox jcboDay = new JComboBox();
    private JComboBox jcboMonth = new JComboBox();
    private JComboBox jcboYear = new JComboBox();
    //    private JLabel jLabel2 = new JLabel();
//    private JComboBox jComboBox1 = new JComboBox();
//    private JComboBox jComboBox2 = new JComboBox();
//    private JLabel jLabel3 = new JLabel();
//    private JComboBox jcboDay = new JComboBox();
//    private JComboBox jComboBox4 = new JComboBox();
//    private JComboBox jComboBox5 = new JComboBox();

    public CalculatorMonthlyFrm1() { 
        try {
//            if (connect())
                jbInit();
        } catch (Exception e) {
            e.printStackTrace();
            DialogBox.ShowError("Error in CalculatorMonthlyFrm\n"+e.getMessage());
        }
    }
    
    @Override
    public void finalize() {
        tblMessage = null;
    }

    private void jbInit() throws Exception {
        this.setTitle("DF Monthly Calculator");
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
        String[] columnNames={ "HOSPITAL_CODE", "DOCTOR_CODE", "MM", "YYYY",
                                "Transaction_Date", "SUM_AMT", "Sum_Disc_Amt", 
                                "Sum_Patient_Paid_Amt", "Sum_Premium_Amt", 
                                "DR_SUM_AMT", "Dr_Tax_40_2", "DR_TAX_40_6", "Dr_Premium_Amt", 
                                "HP_SUM_AMT", "Hp_Premium_Amt", "HP_TAX", "Guaruntee_Amt", "Guaruntee_Tax", 
                                "Dr_Credit_Amt", "Dr_Debit_Amt", "DR_NET_PAID_AMT", "Hp_Disc_Amt", "Dr_Disc_Amt",  
                                "UPDATE_DATE", "Update_Time", "USER_ID", "STATUS_MODIFY",
                                "Ref_Paid_No", "Remark", "Payment_Mode_Code", "PAY_DATE", "Payment_Term_Date", "Premium Rec Amt" };
        tableModel = new DefaultTableModel(columnNames, row);
        tblMessage = new JTable(tableModel);
        scrollPaneMessage = new JScrollPane(tblMessage);
        scrollPaneMessage.setBounds(new Rectangle(95, 65, 635, 330));

        jPanelLayout.add(jcboYear, null);
        jPanelLayout.add(jcboMonth, null);
        jPanelLayout.add(jcboDay, null);
        jPanelLayout.add(lblPaymentDate, null);
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

        start.setBounds(new Rectangle(30, 10, 55, 35));
        start.setActionCommand("Start");
        start.setToolTipText("Start");
        stop.setBounds(new Rectangle(95, 10, 55, 35));
        stop.setActionCommand("Stop");
        stop.setToolTipText("Stop");
        close.setBounds(new Rectangle(160, 10, 55, 35));
        close.setActionCommand("Close");
        close.setToolTipText("Close");
        jSeparator4.setBounds(new Rectangle(10, 50, 720, 2));
        jLabel1.setSize(new Dimension(134, 41));
        jLabel1.setBounds(new Rectangle(590, 5, 135, 40));
        jSeparator5.setBounds(new Rectangle(10, 410, 720, 2));

        lblPaymentDate.setText("Payment Date:");
        lblPaymentDate.setBounds(new Rectangle(260, 25, 85, 25));
        jcboDay.setBounds(new Rectangle(355, 15, 55, 30));
        jcboMonth.setBounds(new Rectangle(410, 15, 55, 30));
        jcboYear.setBounds(new Rectangle(465, 15, 75, 30));
        getLstHospitalCode().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        addHospitalCodeToList();
        
        getLblHospital().setText("Hospital");
        getLblHospital().setBounds(new Rectangle(10, 60, 70, 25));
        getLblDoctor().setText("Doctor");
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
        jcboMonth.setSelectedItem(JDate.getMonth());
        jcboMonth.setToolTipText("MM");
        jcboYear.setSelectedItem(JDate.getYear());
        jcboYear.setToolTipText("YYYY");
    }
    
    @Override
    public DefaultListModel elementAdd(String hospitalCode) {
        DefaultListModel r = new DefaultListModel();
        String sql = "select distinct CODE FROM DOCTOR where ACTIVE = '1' and PAYMENT_MODE_CODE <> 'U' order by CODE";

        ResultSet rs = conn.executeQuery(sql);
        try {
            r.clear();
            while (rs.next()) {
                r.addElement(rs.getString("code"));
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
        return r;
    }
    public void close_actionPerformed(ActionEvent e) {
        this.setVisible(false);
        //dispose();
    }

    public void start_actionPerformed(ActionEvent e) {
        if (connect()) {
            this.addHospitalCodeToList();    
//            setProcess(new ProcessMonthly(this.conn));
//            this.getProcess().setCalculatorFrm(this);
            this.getProcess().running = true;
            this.getProcess().working = true;
            this.setRun();
//            this.getProcess().start();
        } else { 
            DialogBox.ShowError("�������ö�Դ��͡Ѻ�ҹ��������");
        }
    }
    
    public void stop_actionPerformed(ActionEvent e) {
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
    
    public String getEffectiveDate() {
        return this.jcboDay.getSelectedItem().toString() + this.jcboMonth.getSelectedItem().toString() + 
                this.jcboYear.getSelectedItem().toString();
    }
    
    public String getPaymentTermDate() {
        return this.jcboYear.getSelectedItem().toString() + this.jcboMonth.getSelectedItem().toString() +
                this.jcboDay.getSelectedItem().toString();
    }
}
                    