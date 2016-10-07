package df.bean.process;

import df.bean.db.conn.DBConnection;
import df.bean.db.table.Batch;
import df.bean.db.table.TRN_Error;
import df.bean.obj.doctor.DoctorList;
import df.bean.obj.util.DialogBox;
import df.bean.obj.util.Variables;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Process extends Thread {
    private DBConnection conn = null;
    public boolean running = true;
    public boolean working = true;
    public String title;
    public boolean result = true;
    private List valueList = null;
//    private ProcessServlet processServlet = null;
    private DoctorList doctorList = null;
    private Statement statement = null;
    public List values = new ArrayList();
    
    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }
    
    public Process() { 
        super();
        this.valueList = new ArrayList();
        doctorList = null;
    }
    
    public Process(DBConnection conn) {
        super();
        this.valueList = new ArrayList();
        this.conn = conn;
        title = "DF Daily Calculator";
    }
    
    public boolean connectToDB() {
        if (getConn() == null) {
            setConn(new DBConnection());
        }
        
        return getConn().connectToLocal();
    }
    
    @Override
    protected void finalize() {
    }
    public void setDBConnection(DBConnection conn) {
        this.setConn(conn);
    }
    public DBConnection getDBConnection() {
        return this.getConn();
    }
   
    public boolean run(String startDate, String endDate, String hospitalCode) {
        if (this.getDBConnection().IsClosed()) {  
            if (!this.getDBConnection().Connect()) //{ DialogBox.ShowError(" Error "); return false;}
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(),
                    this.getClass().getName(), "", "Connection fail.");
        }
        
        this.getDBConnection().beginTrans();
        
        result = this.Calculate(startDate, endDate, hospitalCode);
//        super.run();    // call super 
            if (this.result) {
                this.getDBConnection().commitTrans();
//                if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
//                DialogBox.ShowInfo("      completed         ");
            } else {
                this.getDBConnection().rollBackTrans();
//                if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
//                DialogBox.ShowError("Error in ProcessTransDaily.Calculate !!!!! \nTransactions are rollback.");
            } 
//        } else { 
//            this.getDBConnection().rollBackTrans();
//            if (this.getDBConnection().IsOpened()) { this.getDBConnection().Close(); }
//        } 
        
        return result;
    }
    
    public boolean Calculate(String value, String hospitalCode) {
        boolean ret = false;
        return ret;
    }
    public boolean Calculate(String startDate, String endDate, String hospitalCode) {
        boolean ret = false;
        return ret;
    }

    public DBConnection getConn() {
        return conn;
    }

    public void setConn(DBConnection conn) {
        this.conn = conn;
    }

    public List getValueList() {
        return valueList;
    }

    public void setValueList(List valueList) {
        this.valueList = valueList;
    }

//    public ProcessServlet getProcessServlet() {
//        return processServlet;
//    }
//
//    public void setProcessServlet(ProcessServlet processServlet) {
//        this.processServlet = processServlet;
//    }

    public DoctorList getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(DoctorList doctorList) {
        this.doctorList = doctorList;
    }
    
    
    
    
}
