/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.db.conn;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import df.bean.db.table.PayorOffice;
import df.bean.obj.util.JDate;
import df.bean.obj.util.Variables;


public class DBConn {
	final static Logger logger = Logger.getLogger(DBConn.class);
	
    private Connection conn;
    private String conn_class;
    private String conn_url;
    private String conn_user;
    private String conn_password;
    
    private Statement stm;
    private PreparedStatement pstm;
    private String[] title_name;

    public DBConn() {
        InputStream is = getClass().getResourceAsStream("db.properties");
        Properties dbProps = new Properties();
        try {
            //mysqlDb();
            //sqlServerDb();
            //sqlServerDbLocal();
            dbProps.load(is);
            this.conn_url = dbProps.getProperty("url");
            this.conn_class = dbProps.getProperty("drivers");
            this.conn_user = dbProps.getProperty("user");
            this.conn_password = dbProps.getProperty("password");      
            Class.forName(conn_class);
            conn = DriverManager.getConnection(conn_url,conn_user,conn_password);
            conn.setAutoCommit(false);
            //System.out.println("Connection Complete");
        } catch (ClassNotFoundException e) {
    		logger.error("DBConn() : Connect DB class error : "+e);
        } catch (SQLException e) {
    		logger.error("DBConn() : Connect DB sql error : "+e);
        } catch (Exception e) { 
    		logger.error("DBConn() : Connect DB others error : "+e);
        }
    }

    public DBConn(boolean con) {
        InputStream is = getClass().getResourceAsStream("db.properties");
        Properties dbProps = new Properties();
        try {
            dbProps.load(is);
            this.conn_url = dbProps.getProperty("url");
            this.conn_class = dbProps.getProperty("drivers");
            this.conn_user = dbProps.getProperty("user");
            this.conn_password = dbProps.getProperty("password");      
            Class.forName(conn_class);
            conn = DriverManager.getConnection(conn_url,conn_user,conn_password);
            conn.setAutoCommit(con);
        } catch (ClassNotFoundException e) {
    		logger.error("DBConn(param) : Connect DB class error : "+e);
        } catch (SQLException e) {
    		logger.error("DBConn(param) : Connect DB sql error : "+e);
        } catch (Exception e) { 
    		logger.error("DBConn(param) : Connect DB others error : "+e);
        }
    }

    public DBConn(Connection dbcp) {
        try {
            this.conn = dbcp;
            this.conn.setAutoCommit(false);
        } catch (Exception e) {
    		logger.error("DBConn(param) : Connect DB others error : "+e);
        }
    }
    
    private void mysqlDb(){
        this.conn_class = "com.mysql.jdbc.Driver";
        this.conn_url = "jdbc:mysql://scap:3306/DFDEV";
        this.conn_user = "root";
        this.conn_password = "welcome";
    }
    
    private void sqlServerDb(){
        this.conn_class = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        this.conn_url = "jdbc:sqlserver://192.168.10.68:1433;DatabaseName=DFDEV";
        this.conn_user = "dfdevsa";
        this.conn_password = "dfsa";
    }
    private void sqlServerDbLocal(){
        this.conn_class = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        this.conn_url = "jdbc:sqlserver://localhost:1433;DatabaseName=DFDEV";
        this.conn_user = "sa";
        this.conn_password = "welcome";
    }
    
    public void insert(String dt) throws SQLException {
        stm.executeUpdate(dt);
    }
    public boolean addData(ArrayList<HashMap<String,String>> dt, String tableName){
    	String[] dataField = null;
    	String[] dataValue = null;
    	String messageInfo = "";
        ResultSet rs;
        Statement sttm;
    	String dataTemp = "";
        HashMap<String,String> dataMap = new HashMap<String,String>();
        try {
			sttm = this.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
	        rs = sttm.executeQuery("SELECT * FROM "+tableName+" WHERE 0<>0");
	        for(int i = 0; i<dt.size(); i++ ){
	        	dataMap = dt.get(i);
	        	dataTemp = dataMap.entrySet().toString().replace("[", "");
	        	dataTemp = dataTemp.replace("]", "");
	        	dataField = dataTemp.split(",");
	            for(int x = 0; x < dataField.length; x++){
		        	dataValue = dataField[x].split("=");
		            rs.moveToInsertRow();
		        	if(dataValue.length>1){
		        		messageInfo = "Table "+tableName+" Field Name : "+dataValue[0].trim()+" VALUES : "+dataValue[1].trim();
			            rs.updateString(dataValue[0].trim(), dataValue[1].trim());
		        	}
	            }
                rs.insertRow();
	        }
	        this.getConnection().commit();
	        sttm.close();
	        rs.close();
		} catch (Exception e) {
    		logger.error("DBConn : addData method error : "+e);
    		logger.error("DBConn : addData method info : "+messageInfo);
		}
    	return true;
    }
    public void setPrepareStatement(String sql){
    	try {
			pstm = conn.prepareStatement(sql);
		} catch (SQLException e) {}
    }
    public PreparedStatement getPrepareStatement(){
    	return pstm;
    }
    public void setStatement() throws SQLException {
        stm = conn.createStatement();
    }
    public void setStatement(Statement s) throws SQLException {
        stm = s;
    }
    public Statement getStatement() throws SQLException {
        return this.stm;
        //stm = conn.createStatement();
    }
    public Connection getConnection(){
        return conn;
    }
    public void closeDB(String met){
        if (conn != null) {
            try {
                conn.close();
            }catch (Exception e) {}
        }
    }
    
    public void closeStatement(String method_name){
        if (this.stm != null) {
            try {
                this.stm.close();
            }catch (Exception e) {}
        }
    }
    
    public void commitDB(){
        try {
            conn.commit();
        } catch (SQLException e) {
            // TODO
        }
    }
    
    public void rollDB(){
        try {
            conn.rollback();
        } catch (SQLException e) {
            // TODO
        }
    }
    
	/**
	 * for SELECT Multiple Data command
	 * 
	 * @param sql
	 * @param args
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> queryList(String sql, Object... args) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		Connection connection = null;
		
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

			pstmt = conn.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				pstmt.setObject(i + 1, args[i]);
			}

			rs = pstmt.executeQuery();
			ResultSetMetaData md = rs.getMetaData();

			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();

				for (int i = 1; i <= md.getColumnCount(); i++) {
					map.put(md.getColumnLabel(i), rs.getObject(i));
				}

				list.add(map);
			}

			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ArrayList<HashMap<String,String>> getMultiData(String sqlCommand) {
		//Implement By Tor
		ArrayList<HashMap<String,String>> lsQueryData = new ArrayList<HashMap<String,String>>();
		ResultSet rs = null;
		try {
			rs = this.stm.executeQuery(sqlCommand);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			while (rs.next()) {
				HashMap<String, String> rtnData = new HashMap<String, String>();
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					String value = "";
					if (rs.getString(i) != null && !rs.getString(i).equals("")) {
						value = rs.getString(i);
						rtnData.put(rsMetaData.getColumnName(i), value);
					} else {
						rtnData.put(rsMetaData.getColumnName(i), value);
					}
				}
				lsQueryData.add(rtnData);
			}
		} catch (Exception e) {
    		logger.error("DBConn : getMultiData method error : "+e);
		} finally {
			try {
				if (rs != null) { 
					rs.close();
				}
			} catch (SQLException e) {}
		}
		return lsQueryData;
	}
	
	public List<Map<String, Object>> queryPsList() {
		ResultSet rs = null;		
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			rs = pstm.executeQuery();
			ResultSetMetaData md = rs.getMetaData();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= md.getColumnCount(); i++) {
					map.put(md.getColumnLabel(i), rs.getObject(i));
				}
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
    public ArrayList<HashMap<String,String>> listQueryData(String sql) {
		ArrayList<HashMap<String,String>> lsQueryData = new ArrayList<HashMap<String,String>>();
		ResultSet rs = null;
		try {
			this.setStatement();
			rs = this.getStatement().executeQuery(sql);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			while (rs.next()) {
				HashMap<String, String> rtnData = new HashMap<String, String>();
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					String value = "";
					if (rs.getString(i) != null && !rs.getString(i).equals("")) {
						value = rs.getString(i);
						rtnData.put(rsMetaData.getColumnName(i), value);
					} else {
						rtnData.put(rsMetaData.getColumnName(i), value);
					}
				}
				lsQueryData.add(rtnData);
			}
		} catch (Exception e) {
    		logger.error("DBConn : listQueryData method error : "+e);
		} finally {
			try {
				if (rs != null) { 
					rs.close();
				}
				if (this.getStatement() != null) {
					this.closeStatement("");
				}
			} catch (SQLException e) {}
		}
		return lsQueryData;
	}
    
    public void setDualStatement(String sql){
    	try {
    		if(sql.equals("")){
    			stm = conn.createStatement();
    		}else{
    			pstm = conn.prepareStatement(sql);
    		}
		} catch (SQLException e) {
    		logger.error("DBConn : setDualStatement method error : "+e);
		}
    }
    
    public String[][] query(String sqlCommand) {
        PreparedStatement pstmt1 = null;
        ResultSet res = null;
        int numColumns = 0;
        int numLines = 0;
        //int numLines = countRow(sqlCommand);
        //System.out.println("line is : "+numLines);
        String[][] dt = null;
        try {
        	pstmt1 = conn.prepareStatement(sqlCommand,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);//new
        	res = pstmt1.executeQuery();//new
            res.last();//new
            numLines = res.getRow();//new
            res.beforeFirst();//new
            ResultSetMetaData rsmd = res.getMetaData();
            numColumns = rsmd.getColumnCount();
            int lines = 0;
            dt = new String[numLines][numColumns];
            while(res.next()){
                for(int i = 0; i<numColumns; i++){
                    dt[lines][i] = res.getString(i+1);
                }
                lines++;
            }
        } catch (SQLException e) {
    		logger.error("DBConn : query method error : "+e);
    		logger.error("DBConn : sql command : "+sqlCommand);
        } finally {
            try {
                res.close();
				pstmt1.close();
			} catch (Exception e) {}
        }
        return dt;
    }
	public ArrayList<String> getSingleDataIsArrayList(String sql, String columnName) {
		ArrayList<String> lsQueryData = new ArrayList<String>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			lsQueryData.add("");
			while(rs.next()) {
				if(rs.getString(columnName) != null && !rs.getString(columnName).equals("")){
					lsQueryData.add(rs.getString(columnName));
				}
			}
		} catch (Exception e) {
    		logger.error("DBConn : getSingleDataIsArrayList method error : "+e);
		} finally {
			try {
				if (rs != null) { 
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lsQueryData;
	}
	
    public int countColumn(String sqlCommand) {
        ResultSet res = null;
        title_name = null;
        int numColumns = 0;
        try {
            res = stm.executeQuery(sqlCommand);
            ResultSetMetaData rsmd = res.getMetaData();
            numColumns = rsmd.getColumnCount();
            title_name = new String[numColumns];
            for(int i = 0;i<numColumns;i++){
                title_name[i] = rsmd.getColumnName(i+1);           
            }
            res.close();
        } catch (SQLException e) {
    		logger.error("DBConn : countColumn method error : "+e);
        } 
        return numColumns;
    }
    
    public int countRow(String sqlCommand) {
        ResultSet res_count = null;
        int numLines = 0;
        try {
            res_count = stm.executeQuery(sqlCommand);
            numLines = 0;
            while(res_count.next()){
                numLines++;
            }
            res_count.close();
        } catch (SQLException e) {
    		logger.error("DBConn : countRow method error : "+e);
        } 
        return numLines;
    }

    public String getSingleData(String sqlCommand) {
        ResultSet res_count = null;
        String data = "";
        try {
            res_count = stm.executeQuery(sqlCommand);
            while(res_count.next()){
            	data = res_count.getString(1);
            }
            res_count.close();
        } catch (SQLException e) {
    		logger.error("DBConn : countRow method error : "+e);
        } 
        return data;
    }
    public String[] getTitleName(){
        return this.title_name;
    }
}
