package df.bean.frame;

import df.bean.process.Process;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;

//my import
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import df.bean.db.conn.DBConnection;
import df.bean.obj.doctor.*;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.Variables;

public class CalculatorFrm1 extends JInternalFrame {
    protected JPanel jPanelLayout = new JPanel();
    protected JList lstDoctorGroupCode = new JList();
    protected JTable tblMessage = null; //new JTable();
    protected JLabel statusBar = new JLabel();
    
    protected JScrollPane scrollPane;// = new JScrollPane();
    protected JScrollPane scrollPaneMessage;// = new JScrollPane();
    
    protected JList lstHospitalCode = new JList();
    protected JScrollPane scrollPaneHospitalCode;
    protected JLabel lblHospital = new JLabel();
    protected JLabel lblDoctor = new JLabel();

    protected int row = 0;
    protected ImageIcon imageStart = new ImageIcon(CalculatorFrm1.class.getResource("start.gif"));
    protected ImageIcon imageStop = new ImageIcon(CalculatorFrm1.class.getResource("stop.gif"));
    protected ImageIcon imageClose = new ImageIcon(CalculatorFrm1.class.getResource("close.gif"));
    protected ImageIcon imagePhyathai = new ImageIcon(CalculatorFrm1.class.getResource("logo2.gif"));
    
    protected JButton start = new JButton();
    protected JButton stop = new JButton();
    protected JButton close = new JButton();
    private JSeparator jSeparator4 = new JSeparator();
    private JLabel jLabel1 = new JLabel();
    private JSeparator jSeparator5 = new JSeparator();
    
    public DBConnection conn = new DBConnection();
        
    protected String hospitalCode;
    protected Process process = null;
    protected DefaultTableModel tableModel;
//    private String[] columnNames;
    
    public CalculatorFrm1() {
        try {
//            if (connect())
                jbInit();
        } catch (Exception e) {
            DialogBox.ShowError("Error in CalculatorFrm\n"+e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void setVariables(String userId, String password, String hospitalCode) {
        Variables.setUserID(userId);
        Variables.setPassword(password);
        Variables.setHospitalCode(hospitalCode);
    }
    
    @Override
    public void finalize() {
        if (conn.IsOpened()) {          
            conn.Close();
        }
        conn = null;
    }
    
    public boolean connect() {
        boolean ret = false;
        try {
            // connect to databse
//            conn = new DBConnection();
//            conn.setUserName(DBConnection.CONN_USER);
//            conn.setPassword(DBConnection.CONN_PASS);
            if (conn.Connect()) {
                ret = true;
            } else {
                DialogBox.ShowError("CalculatorFrm.connect() : Connect to database fail. !!!!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            DialogBox.ShowError("CalculatorFrm.connect() : Error in calculateFRM");
        }

        return ret;
    }
    
    private void jbInit() throws Exception {
//        this.setMaximizable(true);
//        this.setResizable(true);
         this.setTitle("Daily Calculator");
         this.setSize(new Dimension(746, 469));
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
         
         scrollPane.setVisible(false);
         getLblDoctor().setVisible(false);
         
         // scroll pane for hospital
         scrollPaneHospitalCode = new JScrollPane(getLstHospitalCode());
         scrollPaneHospitalCode.setBounds(new Rectangle(10, 85, 80, 65));
         
         //Table Scroll Pane
          String[] columnNames={ "ID", "YYYY", "MM", "DD", "Hospital", "Inv No",
                          "Inv Date","Bill No","Rev Date","Department", "Location", "HN#", "PTName", "Order Item Code", "Billing Sub Group No",
                          "Rec Mode", "Rec Type", "Payment Module", "Doctor", "Inv Amt", "INV_DISCOUNT_AMT", 
                          "Rec Amt", "Rec Premium Amt", "Nor Allocate_Amt", "Nor Allocate Pct", 
                          "Dr Amt", "Dr Amt 402", "Dr Amt 406", "Dr Premium", "Dr TaxBase", "Tax Type", 
                          "HP_AMT", "HP Premium", "HP_TAX", "Status_Code", "Admission Type", 
                          "Dr Treatment Code", "Premium Pct", "Premium Vat", "Order Date" };
         tableModel = new DefaultTableModel(columnNames, row);
         tblMessage = new JTable(tableModel);
         scrollPaneMessage = new JScrollPane(tblMessage); 
         scrollPaneMessage.setBounds(new Rectangle(95, 65, 635, 330));
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
         
         
         getLstHospitalCode().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//         addHospitalCodeToList();
                 
         getLblHospital().setText("Hospital");
         getLblHospital().setBounds(new Rectangle(10, 60, 70, 25));
         getLblDoctor().setText("Invoice No.");
         //        getLblDoctor().setText("Doctor");
         getLblDoctor().setBounds(new Rectangle(10, 155, 70, 25));
                 
         jPanelLayout.add(scrollPane, null);
         getContentPane().add(jPanelLayout, BorderLayout.CENTER);
        
         this.setHospitalCode("null");
         this.setSelected(true);
    }
    
    public void addDoctorGroupToList(String hospitalCode) {
        this.getLstDoctorGroupCode().removeAll();
        DefaultListModel model = elementAdd(hospitalCode);
        this.getLstDoctorGroupCode().setModel(model);
    }
    
    public DefaultListModel elementAdd(String hospitalCode) {
        DefaultListModel r = new DefaultListModel();
        String sql = "SELECT CODE FROM doctor where HOSPITAL_CODE = '" + hospitalCode + "'";
        sql = sql + " order by CODE";
        ResultSet rs = conn.executeQuery(sql);
        try {
            r.clear();
            while (rs.next()) {
                r.addElement(rs.getString("code"));
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
    
    public void setTableValue(List lst) {
        if (this.tableModel.getRowCount() > 4000) { this.tableModel.removeRow(0);  }
        Object[] object = new String[lst.size()];
        for (int i=0; i<lst.size(); i++) {
            if (lst.get(i) != null)
                object[i] = lst.get(i).toString();
            else 
                object[i] = " ";
        }
        tableModel.addRow(object);
    }
    public void setErrorMessage(List lst) {
        if (this.tableModel.getRowCount() > 2000) this.tableModel.removeRow(0);
        Object[] object = new String[lst.size()];
        for (int i=0; i<lst.size(); i++) {
            if (lst.get(i) != null)
                object[i] = lst.get(i).toString();
            else 
                object[i] = " ";
        }
        
        tableModel.addRow(object);  
        tblMessage.setBackground(Color.RED);
//        tblMessage.setSelectionBackground(Color.RED);
    }

    private void close_actionPerformed(ActionEvent e) {
    }

    private void start_actionPerformed(ActionEvent e) {
        if (this.conn.IsOpened()) { this.connect();  addHospitalCodeToList();  }
    }
    
    private CareProvider newCareProvider(String className) {
        CareProvider cpv = null;
        try {
            Class careProviderClass = Class.forName( className, true, ClassLoader.getSystemClassLoader() );
            cpv = (CareProvider)careProviderClass.newInstance();
        } catch( Exception err ) {
            err.printStackTrace();
        }
        return cpv;
    }
    

    private void stop_actionPerformed(ActionEvent e) {
        
    }


    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public JList getLstDoctorGroupCode() {
        return lstDoctorGroupCode;
    }

    public void setLstDoctorGroupCode(JList lstDoctorGroupCode) {
        this.lstDoctorGroupCode = lstDoctorGroupCode;
    }

    public JList getLstHospitalCode() {
        return lstHospitalCode;
    }

    public void setLstHospitalCode(JList lstHospitalCode) {
        this.lstHospitalCode = lstHospitalCode;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }
    
    // hospital list
    public void addHospitalCodeToList() {
        DefaultListModel model = elementAddToHospitalCodeList();
        this.getLstHospitalCode().setModel(model);
    }
    
    public DefaultListModel elementAddToHospitalCodeList() {
        DefaultListModel r = new DefaultListModel();
        String sql;
        sql = "SELECT CODE FROM hospital where code like '" + Variables.getHospitalCode() + "%' order by CODE"; 
        
        ResultSet rs = conn.executeQuery(sql);
        try {
            while (rs.next()) {
                r.addElement(rs.getString("code"));
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

    public JLabel getLblHospital() {
        return lblHospital;
    }

    public void setLblHospital(JLabel lblHospital) {
        this.lblHospital = lblHospital;
    }

    public JLabel getLblDoctor() {
        return lblDoctor;
    }

    public void setLblDoctor(JLabel lblDoctor) {
        this.lblDoctor = lblDoctor;
    }
    public void startProcess() {
        
    }
    public void stopProcess() {
        if (this.getProcess() != null) {
            this.getProcess().running = false;
            this.getProcess().working = false;
            this.setStop();     
        }
    }
    public void setRun() {
        start.setEnabled(false);
        stop.setEnabled(true);
    }
    public void setStop() {
        start.setEnabled(true);
        stop.setEnabled(false);        
    }
/*
    private void this_componentResized(ComponentEvent e) {
//        scrollPaneMessage.setSize(jPanelLayout.getWidth()-110, jPanelLayout.getHeight()-60);
//        this.scrollPane.setSize(this.scrollPane.getWidth(), jPanelLayout.getHeight()-185);
//        jToolBarDFCalculator.setSize(jPanelLayout.getWidth(), jToolBarDFCalculator.getHeight());
    }    */
}
