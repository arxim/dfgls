package df.bean.db.conn;

import df.bean.obj.util.DialogBox;
import java.io.InputStream;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBConnection {
    private Connection conn=null;
    private Statement stmt=null;
    private String userName;
    private String password;
    private String URL;
    private boolean autoCommit;
    private Statement stmtForInsert=null;
    private ResultSet rsForInsert = null;
	private String userID = "";
    private String hospitalCode = "";
    
    //private Savepoint savePoint;
    //static public final String CONN_URL = "jdbc:oracle:thin:@area51:1521:DF";  //area51
    //static public final String CONN_USER = "df"; //area51
    //static public final String CONN_PASS = "WELCOME"; //area51
    
    //static public final String CONN_URL = "jdbc:oracle:thin:@nb72:1521:XE";  //local
    //static public final String CONN_USER = "doctor";   
    //static public final String CONN_PASS = "welcome";   
    //static public final String CONN_CLASS = "oracle.jdbc.driver.OracleDriver"; //oracle
    
    // sql server 2000
    //static public String CONN_URL = "jdbc:microsoft:sqlserver://scap:1433;databasename=DFDEV";
    //static public final String CONN_CLASS = "com.microsoft.jdbc.sqlserver.SQLServerDriver"; // SQL Server
    //public String CONN_USER = "sa"; //server
    //public String CONN_PASS = "welcome"; //server
    // sql server 2005
    //public String CONN_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; 
    //public String CONN_URL = "jdbc:sqlserver://127.0.0.1:1434;databaseName=DFDEV";
    
    // Connection Pool
    public String CONN_USER;
    public String CONN_PASS;
    public String CONN_CLASS;
    public String CONN_URL;
    private DBConnectionManager connMgr;

    public DBConnection() {
    	//System.out.println("New Connect DB");
        InputStream is = getClass().getResourceAsStream("db.properties");
        
        Properties dbProps = new Properties();
        try {
            dbProps.load(is);
            this.CONN_URL = dbProps.getProperty("url");
            this.CONN_CLASS = dbProps.getProperty("drivers");
            this.CONN_USER = dbProps.getProperty("user");
            this.CONN_PASS = dbProps.getProperty("password");
            
            this.URL = dbProps.getProperty("url");
            this.setUserName(dbProps.getProperty("user"));
            this.setPassword(dbProps.getProperty("password"));
      
        } catch (Exception e) { System.out.println(e); e.printStackTrace(); }
        
        conn = null;
        connMgr = null;
        
    }
    public DBConnection(String hopital_code) {
        InputStream is = getClass().getResourceAsStream("db.properties");
        Properties dbProps = new Properties();
        this.setHospitalCode(hopital_code);
        try {
            dbProps.load(is);
            this.CONN_URL = dbProps.getProperty("url");
            this.CONN_CLASS = dbProps.getProperty("drivers");
            this.CONN_USER = dbProps.getProperty("user");
            this.CONN_PASS = dbProps.getProperty("password");
            
            this.URL = dbProps.getProperty("url");
            this.setUserName(dbProps.getProperty("user"));
            this.setPassword(dbProps.getProperty("password"));
      
        } catch (Exception e) { System.out.println(e); e.printStackTrace(); }
        
        conn = null;
        connMgr = null;
        
    }
    public void freeConnection() {
        connMgr.freeConnection("idb", conn);
    }
    
    protected void destroy() {
        if (this.IsOpened()) {
            this.Close();
        }
        connMgr.release();
        conn = null;
        stmt = null;
    }
    public void setUserName(String userName) { 
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Connection getConnection() {
        return conn;
    }
    public Connection getConnectionFormConnectionManager() {
        conn = connMgr.getConnection("idb");
        return conn;
    }
    public void setConnection(Connection connect) {
        this.conn = connect;
    }
    public Statement getStatement() {
        return stmt;
    }
    public void setStatement(Statement statement) {
        stmt = statement;
    }
    public boolean connectToSCAP() {
        return Connect();
    }
    
    public boolean connectToLocal() {
        return Connect();
    }
    
    public boolean connectToServer() {
        return ConnectToPool();
    }

    public DBConnectionManager getConnectionManager() {
        return connMgr;
    }

	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
    
    //connection to database 
    public boolean Connect() {
        boolean ret=false;
        if ((this.getUserName().equals("")) && (this.getPassword().equals(""))) {
            return ret;
        } 
        
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            
            Class.forName(getCONN_CLASS()); 
//            connMgr = DBConnectionManager.getInstance();
//            conn = connMgr.getConnection("idb");
            
            conn = DriverManager.getConnection(getCONN_URL(), this.userName, this.password);
            stmt = conn.createStatement();
             
             
            this.stmtForInsert = this.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                
            ret=true;
        } catch (SQLException e) { System.out.println(e); e.printStackTrace(); 
        } catch(ClassNotFoundException e) { System.out.println(e); e.printStackTrace();   } 
        finally {
               //Clean up resources, close the connection.
        }
        return ret;
    }

    //connection to database 
    public boolean ConnectToPool() {
        boolean ret=false;
        if ((this.getUserName().equals("")) && (this.getPassword().equals(""))) {
            return ret;
        } 
        
        try {
             DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            
            Class.forName(getCONN_CLASS()); 
            connMgr = DBConnectionManager.getInstance();
            conn = connMgr.getConnection("idb");
            
            conn = DriverManager.getConnection(getCONN_URL(), this.userName, this.password);
            stmt = conn.createStatement();
             
             
            this.stmtForInsert = this.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                
            ret=true;
        } catch (SQLException e) { e.printStackTrace(); 
        } catch(ClassNotFoundException e) { e.printStackTrace();   } 
        finally {
               //Clean up resources, close the connection.
        }
        return ret;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getCONN_CLASS() {
        return CONN_CLASS;
    }

    public String getCONN_PASS() {
        return CONN_PASS;
    }

    public String getCONN_URL() {
        return CONN_URL;
    }

    public String getCONN_USER() {
        return CONN_USER;
    }

    // Close Connection
    public boolean Close() {
        boolean ret = false;
        if (this.getConnection() != null) {
            try {
                if (this.rsForInsert != null) { this.rsForInsert.close(); }
                if (this.stmtForInsert != null) { this.stmtForInsert.close(); }
                if (this.stmt != null) { this.stmt.close(); }
                if (this.conn != null) { this.conn.close(); }
                
                this.rsForInsert = null;
                this.stmt = null;
                this.stmtForInsert = null;
                this.conn = null;
                ret = true;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    
    public boolean IsClosed() {
        boolean ret = false;
        if (conn != null) {
            try {
                ret = this.conn.isClosed();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    
    public boolean IsOpened() {
        boolean ret = false;
        if (conn != null) {
            try {
                ret = !this.conn.isClosed();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    
    public ResultSet executeQuery(String sqlCommand) {
        ResultSet ret = null;

        if (this.getConnection() != null) {
            try {
                if (stmt == null) {
                    stmt = conn.createStatement();
                }
                ret = stmt.executeQuery(sqlCommand);
            } catch (SQLException e) {
            	System.out.println(e);
                //e.printStackTrace();
            }
        }
        return ret;
    }
    public int executeUpdate(String sqlCommand) {
        int ret = 0;
        if (this.getConnection() != null) {
            try {
                if (stmt == null) {
                    stmt = conn.createStatement();
                }
                ret = stmt.executeUpdate(sqlCommand);
            } catch (SQLException e) {
                ret = -1;
                e.printStackTrace();
                System.out.print(sqlCommand);
                System.out.print(e.getMessage());
            }
        }
        return ret;
    }
    
    public String executeQueryString(String sqlCommand) {
        String ret = null;
        ResultSet rs = null;
        Statement  st = null;
        if (conn != null) {
            try {
                st = conn.createStatement();
                rs = st.executeQuery(sqlCommand);
                while (rs.next()) {
                    ret = rs.getString(1);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
              try {
                if (rs != null) { 
                    rs.close();
                    rs = null;
                }
                if (st != null) {
                    st.close();
                    st = null;
                }
              }
              catch (SQLException ex)  {
                System.out.println("A SQLException error has occured in DBConnection.IsFound() \n" + ex.getMessage());
                ex.printStackTrace();
              }
            }
            
        }

        return ret;
    }    

    public boolean IsFound(String sqlCommand) {
        boolean ret = false;
        ResultSet rs = null;
        Statement st = null;
        if (conn != null) {
            try {
                st = conn.createStatement();
                rs = st.executeQuery(sqlCommand);
                while (rs.next()) {
                    ret = true;
                }
            } catch (SQLException e) {
                System.out.println("Error in DBConnection.IsFound() \n" + e.getMessage());
                e.printStackTrace();
            }
            
            finally {
              try {
                if (rs != null) { 
                    rs.close();
                    rs = null;
                }
                if (st != null) {
                    st.close();
                    st = null;
                }
              }
              catch (SQLException ex)  {
                System.out.println("A SQLException error has occured in DBConnection.IsFound() \n" + ex.getMessage());
                ex.printStackTrace();
              }
            }
     
        }
        return ret; 
    }  
    
    public boolean insert(String tableName, String[] Fields, String[] Values) {
        boolean ret = false;
        ResultSet rs=null;
        Statement stmt1 = null;
        try {
            // Create an updatable result set
            stmt1 = getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            setStatement(stmt1);
            rs = executeQuery("SELECT " +  getColumnNamesLine(Fields) + " FROM " + tableName + " WHERE 0=1");
            
            // Move cursor to the "insert row"
            rs.moveToInsertRow();
            
            // Set values for the new row.
            for (int i=0; i<Fields.length; i++) {
                rs.updateString(Fields[i].toString(), Values[i]);
            }

            // Insert the row
            rs.insertRow();
            ret = true;
        } catch (SQLException e) {
            try {
                rs.cancelRowUpdates();
            } catch (SQLException f) {
                f.printStackTrace();
            }
            e.printStackTrace();
        }
        finally {
          try {
            if (rs != null) { 
                rs.close();
                rs = null;
            }
            if (stmt1 != null) {
                stmt1.close();
                stmt1 = null;
            }
          }
          catch (SQLException ex)  {
            System.out.println("A SQLException error has occured in DBConnection.insert() \n" + ex.getMessage());
            ex.printStackTrace();
            ret = false;
          }
        }
        return ret;
    }
    
    public String[] getColumnNames(String tableName) {
        String[] columnNames=null;
        Statement stmt1 = null;
        ResultSet rs = null;
        try {
            // Create a result set
            stmt1 = getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            rs = stmt1.executeQuery("SELECT * FROM " + tableName + " WHERE 1=0");
            // Get result set meta data
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            columnNames = new String[numColumns];
            // Get the column names; column indices start from 1
            for (int i=1; i<numColumns+1; i++) {
                columnNames[i-1] = rsmd.getColumnName(i);
        
                // Get the name of the column's table name
    //                String tableName = rsmd.getTableName(i);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }        
        finally {
          try {
            if (rs != null) { 
                rs.close();
                rs = null;
            }
            if (stmt1 != null) {
                stmt1.close();
                stmt1 = null;
            }
            
          }
          catch (SQLException ex)  {
            System.out.println("A SQLException error has occured in DBConnection.getColumnNames() \n" + ex.getMessage());
            ex.printStackTrace();
          }
        }
        return columnNames;
    }
    
    public String getColumnNamesLine(String[] columnNames) {
        String ret = "";
        for (int i=0; i<columnNames.length; i++) {
            ret = ret + columnNames[i];
            if (i<columnNames.length-1) {
                ret = ret + ",";
            }
        }
        return ret;
    }
    
    public List getValues(String tableName, String[] fields, String whereCause) {
        List values=new ArrayList();
        String sql = null;
        Statement stmt1 = null;
        ResultSet rs = null;
        try {
            // Create a result set
            sql = "select " + getColumnNamesLine(fields) + " from " + tableName;
            if (whereCause.equals("")) { 
                sql = sql + " where " + whereCause; 
            }
            
            stmt1 = getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            rs = stmt1.executeQuery(sql);

            // Get result set meta data
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
//            values = new String[numColumns];
                
                // Get the column names; column indices start from 1
                 rs.next();
            for (int i=1; i<numColumns+1; i++) {

                String type = rsmd.getColumnTypeName(i);
                if (type.equalsIgnoreCase("VARCHAR2")) {
                    values.add(rs.getString(fields[i-1]));
                }
                else if (type.equalsIgnoreCase("DATE")) {
                    values.add((Date)rs.getDate(fields[i-1]));
                }
//                else if (type.equalsIgnoreCase("NUMBER")) {
//                    values.add(rs.getInt(fields[i-1]));
//                }
//                else if (type.equalsIgnoreCase("Double")) {
//                    values.add(rs.getDouble(fields[i-1]));
//                }
                else if (type.equalsIgnoreCase("Timestamp")) {
                    values.add(rs.getTimestamp(fields[i-1]));
                }
//                else if (type.equalsIgnoreCase("Float")) {
//                    values.add(rs.getFloat(fields[i-1]));
//                }
                else {
                    values.add(rs.getString(fields[i-1]));
                }
            }
            rs.close();
            stmt1.close();
            rs = null;
            stmt1 = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return values;
    }
    
    public void beginTrans() {
        try {
            if (!getConnection().isClosed()) {
                autoCommit = getConnection().getAutoCommit();
                getConnection().setAutoCommit(false);
//                savePoint = getConnection().setSavepoint();
            }// else { DialogBox.ShowWarning("Database is not connect."); }
        } catch (SQLException e) {
            // TODO
//            DialogBox.ShowError("Error in DBConnection.beginTrans. \n" + e.getMessage());
        }
    }
    
    public void commitTrans() {
        try {
            getConnection().commit();
            getConnection().setAutoCommit(autoCommit);
        } catch (SQLException e) {
            // TODO
//            DialogBox.ShowError("Error in DBConnection.commitTrans. \n" + e.getMessage());
        }
    }
    
    public void rollBackTrans() {
        try {
            getConnection().rollback();
//            getConnection().rollback(savePoint);
//            getConnection().releaseSavepoint(savePoint); 
            getConnection().setAutoCommit(autoCommit);
        } catch (SQLException e) {
            // TODO
//            DialogBox.ShowError("Error in DBConnection.rollBackTrans. \n" + e.getMessage());
        }
    }
    
    public Statement getStatementForInsert() {
        try {
            if (this.stmtForInsert == null) {
                this.stmtForInsert = this.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            }
        } catch (SQLException e) {
            // TODO
            System.out.print("create statement for insert error.");
        }
        return this.stmtForInsert;
    }
    public ResultSet getRsForInsert(String sql) {
        try {
            if (this.rsForInsert == null) {
                this.rsForInsert = this.stmtForInsert.executeQuery(sql);
            }
        } catch (SQLException e) {
            // TODO
            System.out.print("create resultset for insert error.");
        }
        return this.rsForInsert;
    }
    public void closeStatementForInsert() {
        try {
            if (this.stmtForInsert != null) {
                this.stmtForInsert.close();
                this.stmtForInsert = null;
            }
        } catch (SQLException e) {
            // TODO
            System.out.print("create statement for insert error.");
        }
    }
    public void closeRsForInsert() {
        try {
            if (this.rsForInsert != null) {
                this.rsForInsert.close();
                this.rsForInsert = null;
            }
        } catch (SQLException e) {
            // TODO
            System.out.print("create statement for insert error.");
        }
    }
    
    
    // get data from database and add in ArrayList
    public List getValueList(String sqlCommand) {
        ResultSet rs = null;
        Statement  st = null;
        
        List values = new ArrayList();
        
        if (conn != null) {
            try {
                st = this.getConnection().createStatement();
                rs = st.executeQuery(sqlCommand);
                while (rs.next()) {
                    values.add(rs.getString(1));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
              try {
                if (rs != null) { 
                    rs.close();
                    rs = null;
                }
                if (st != null) {
                    st.close();
                    st = null;
                }
              }
              catch (SQLException ex)  {
                System.out.println("A SQLException error has occured in getValueList() \n" + ex.getMessage());
                ex.printStackTrace();
              }
            }
        }

        return values;
    }    

    
}
