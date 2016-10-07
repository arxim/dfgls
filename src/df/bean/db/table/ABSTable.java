package df.bean.db.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;

import df.bean.db.conn.DBConnection;

public class ABSTable {

    DBConnection conn=null;
    protected ResultSet resultSet = null;
    protected Statement statement = null;
    
    public ABSTable() {
    }

    public DBConnection getDBConnection() {
        return conn;
    }

    public void setDBConnection(DBConnection conn) {
        this.conn = conn;
    }
    
    public ResultSet getResultSet() {
        return resultSet;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement aStmt) {
        statement = aStmt;
    }

    public void setResultSet(ResultSet aResultSet) {
        resultSet = aResultSet;
    }
    
    public boolean rollBack(List sql) {
        Statement stmt = null;
        List sqlCommand = sql;
        boolean ret = false;
        int rows = 0;

//        sqlCommand.add( sql1 );
        
        try {
            for (int i=0; i<sqlCommand.size(); i++) {
                stmt = this.getDBConnection().getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                rows = stmt.executeUpdate(sqlCommand.get(i).toString());
                if (rows < 0 ) {
                    ret = false;
                    TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), TRN_Error.PROCESS_ROLLBACK, "Rollback fail.", "", sqlCommand.get(i).toString());
                    break;
                } else {
                    ret = true;
                    TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), TRN_Error.PROCESS_ROLLBACK, "Rollback Complete.", "", sqlCommand.get(i).toString());
                }
            }
            
        } catch (Exception ex) {
            ret = false;
            TRN_Error.writeErrorLog(this.getDBConnection().getConnection(), TRN_Error.PROCESS_ROLLBACK, "Rollback fail.", ex.getMessage());
            System.out.println(ex.getMessage());
        } finally {
            sqlCommand = null;
            try {
              if (stmt != null) {
                  stmt.close();
                  stmt = null;
              }
            }
            catch (SQLException ex)  {
              System.out.println("A SQLException error has occured in ABSTable.rollBack() \n" + ex.getMessage());
              ex.printStackTrace();
            }
        }
        return ret;
    }
}
