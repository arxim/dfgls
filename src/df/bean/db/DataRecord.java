/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.db;

import java.util.Vector;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import df.bean.db.conn.DBConnection;

/**
 *
 * @author Pong
 */
public class DataRecord {

    protected String table;
    protected Vector<DataField> fields;
    
    public DataRecord() {
        this(null);
    }
    
    public DataRecord(String table) {
        this.table = table;
        this.fields = new Vector<DataField>();
    }
    
    public boolean addField(String field, int type, String value) {
        return this.addField(field, type, value, false);
    }
    
    public boolean addField(String field, int type, String value, boolean pk) {
        return DBMgr.isValidType(type) ? fields.add(new DataField(field, type, value, pk)) : false;
    }
    
    public DataField getField(String field) {
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            if (fields.elementAt(i).name.equalsIgnoreCase(field)) {
                return fields.elementAt(i);
            }
        }
        return new DataField();
    }
/*    
    public boolean retrieve() throws Exception {
        if (this.fields.size() <= 0 || !this.fields.firstElement().name.equalsIgnoreCase("CODE"))
            return false;
        
        DBConnection con = new DBConnection();
        con.connectToServer();
        
        DataField field = this.fields.firstElement();
        
        String value = field.value;
        if (field.type == DBMgr.TYPE_STRING) {
            value = "'" + DBMgr.toSQLString(field.value) + "'";
        }
        //else if (field.type == DBMgr.TYPE_DOUBLE) {
        //    value = field.value;
        //}
        
        String query = String.format("SELECT * FROM %1$s WHERE %2$s = %3$s", this.table, field.name, value);
        
        ResultSet rs = con.executeQuery(query);
        boolean result;
        if (rs.next()) {
            // TODO
            this.mode = DBMgr.MODE_QUERY;
            result = true;
        } 
        else {
            this.fields.removeAllElements();
            result = false;
        }
        rs.close();
        con.freeConnection();
        return result;
    }
*/    
    /*
    public boolean isExist() throws Exception {
        DBConnection con = new DBConnection();
        con.connectToServer();
        
        String query = String.format("SELECT CODE FROM %1$s WHERE CODE = '%2$s'", this.table, DBMgr.toSQLString(this.code));
        
        ResultSet rs = con.executeQuery(query);
        boolean result = rs.next();
        rs.close();
        con.freeConnection();
        return result;
    }
    
    public boolean insert() throws Exception {
        if (this.isExist()) {
            return false;
        }
            
        DBConnection con = new DBConnection();
        con.connectToServer();
        
        String query = String.format("SELECT * FROM %1$s WHERE 0 = 1", this.table);
        ResultSet rs = con.getStatementForInsert().executeQuery(query);
        rs.moveToInsertRow();
        
        rs.updateString("CODE", this.code);
        int size = this.fields.size();
        for (int i = 0; i < size; i++) {
            DataField f = this.fields.elementAt(i);
            switch (f.type) {
                case DBMgr.TYPE_STRING :
                    rs.updateString(f.name, f.value);
                    break;
                case DBMgr.TYPE_DOUBLE :
                    rs.updateDouble(f.name, Double.parseDouble(f.value));
                    break;
            }
        }
        rs.insertRow();
        
        rs.close();
        con.freeConnection();
        
        return true;
    }
    
    public boolean update() throws Exception {
        if (!this.isExist()) {
            return false;
        }
            
        DBConnection con = new DBConnection();
        con.connectToServer();
        
        String query = String.format("SELECT * FROM %1$s WHERE 0 = 1", this.table);
        ResultSet rs = con.getStatementForInsert().executeQuery(query);
        rs.moveToInsertRow();
        
        rs.updateString("CODE", this.code);
        int size = this.fields.size();
        for (int i = 0; i < size; i++) {
            DataField f = this.fields.elementAt(i);
            switch (f.type) {
                case DBMgr.TYPE_STRING :
                    rs.updateString(f.name, f.value);
                    break;
                case DBMgr.TYPE_DOUBLE :
                    rs.updateDouble(f.name, Double.parseDouble(f.value));
                    break;
            }
        }
        rs.insertRow();
        
        rs.close();
        con.freeConnection();
        
        return true;
    }
     * */
}
