/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.db;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.obj.util.Variables;
import df.jsp.Util;
import java.io.*;
import java.sql.*;

/**
 *
 * @author Pong
 */
public class DBMgr {
    
    public static final byte MODE_QUERY = 0;
    public static final byte MODE_INSERT = 1;
    public static final byte MODE_UPDATE = 2;
    public static final byte MODE_DELETE = 3;
    
    public static boolean isValidType(int type) {
        switch (type) {
            case Types.VARCHAR :
            case Types.CHAR :
            case Types.DOUBLE :
            case Types.NUMERIC :
                return true;
        }
        return false;
    }
    
    public static boolean isValidMode(byte mode) {
            return mode == DBMgr.MODE_QUERY || mode == DBMgr.MODE_INSERT || mode == DBMgr.MODE_UPDATE || mode == DBMgr.MODE_DELETE;
    }
    
    public static String toSQLString(String input) {
        if (input != null)
            return input.replace("'", "''");
        return "";
    }
    
    protected static String toSQLValue(DataField f) {
        switch (f.type) {
            case Types.CHAR :
            case Types.VARCHAR :
                return "'" + DBMgr.toSQLString(f.value) + "'";
            default :
                return f.value;
        }
    }
    
    protected static void updateResultSet(ResultSet rs, DataRecord record) throws IOException {
        int size = record.fields.size();
        String fieldName = null;
        String str = "";
        DataField field;

        try {
            for (int i = 0; i < size; i++) {
                field = record.fields.elementAt(i);
                fieldName = field.name;
                switch (field.type) {
                    case Types.CHAR :
                    case Types.VARCHAR :
                    	//System.out.println(Variables.IS_TEST ? "field name="+field.name+" value="+field.value : "");
                        if (field.value == null || field.value.equalsIgnoreCase("")) {
                            rs.updateString(field.name, "");
                            
                        }
                        else {
                            rs.updateString(field.name, field.value.trim());
                        }
                        break;
                    /*case Types.NCHAR :
                    case Types.NVARCHAR :
                        rs.updateNString(field.name, field.value);
                        break;*/
                    case Types.DOUBLE :
                    case Types.NUMERIC :
                        if (field.value == null || field.value.equalsIgnoreCase("")) {
                            rs.updateDouble(field.name, 0);
                        }
                        else {
                            rs.updateDouble(field.name, Double.parseDouble(field.value.trim()));
                        }
                        break;
                }
            }
        }
        catch (Exception e) {
        	System.out.println("Update Result Set Error : "+e);
        }
    }
    
    protected static String generatePKCondition(DataRecord record) {
        int size = record.fields.size();
        String cond = "";
        for (int i = 0; i < size; i++) {
            DataField f = record.fields.elementAt(i);
            if (f.pk) {
                if (cond.length() != 0) {
                    cond += " AND ";
                }
                cond += f.name + " = " + DBMgr.toSQLValue(f);
            }
        }
        return cond;
    }
    
    public static boolean isExist(DataRecord record) throws Exception {
        String query = String.format("SELECT COUNT(*) AS NUM_REC FROM %1$s WHERE %2$s", record.table, DBMgr.generatePKCondition(record));
        boolean result = false;
        
        try {
            DBConnection con = new DBConnection();
            con.connectToServer();

            ResultSet rs = con.executeQuery(query);
            rs.next();
            result = rs.getInt("NUM_REC") > 0 ? true : false;
            rs.close();
            con.freeConnection();
        }
        catch (Exception e) {
            e.printStackTrace();            
        }
        return result;
    }
    
    public static boolean isExist(String query) throws Exception {
        boolean result = false;
        try {
            DBConnection con = new DBConnection();
            con.connectToServer();

            ResultSet rs = con.executeQuery(query);
            if (rs.next()) {
                result = true;
            }

            rs.close();
            con.freeConnection();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }

    public static int executeUpdate(String cmd) throws SQLException {
        int result = 0;
        try {
            DBConnection con = new DBConnection();
            con.connectToLocal();
            //con.connectToServer();

            result = con.executeUpdate(cmd);
            con.Close();
            //con.freeConnection();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public static String generateDropDownList(String id, String cssClass, String query, String textField, String valueField, String selectedValue) throws SQLException {
        return DBMgr.generateDropDownList(id, cssClass, "", query, textField, valueField, selectedValue);
    }
    
    public static String generateDropDownList(String id, String cssClass, String cssClassOptionInActive, String query, String textField, String valueField, String selectedValue) throws SQLException {
        String str = String.format("<select id=\"%1$s\" name=\"%1$s\" class=\"%2$s\">", id, cssClass);
        str += "<option value=\"\">--- Select ---</option>";
        str += DBMgr.generateOptionList(query, textField, valueField, selectedValue);
        str += "</select>";
        
        return str;
    }
    
    public static String generateDropDownList(String id, String cssClass, String [] value, String [] text, String selectedValue) {
        String str = String.format("<select id=\"%1$s\" name=\"%1$s\" class=\"%2$s\">", id, cssClass);
        selectedValue = selectedValue == null ? "" : selectedValue;
        if (value != null && text != null && value.length == text.length) {
            for (int i = 0; i < value.length; i++) {
                if (value[i].equalsIgnoreCase(selectedValue)) {
                    str += "<option value=\"" + value[i] + "\" selected=\"selected\">" + text[i] + "</option>";
                }
                else {
                    str += "<option value=\"" + value[i] + "\">" + text[i] + "</option>";
                }
            }
        }
        str += "</select>";
        return str;
    }
    
    public static String generateOptionList(String query, String textField, String valueField, String selectedValue) throws SQLException {
        return DBMgr.generateOptionList("", query, textField, valueField, selectedValue);
    }
    
    public static String generateOptionList(String cssClassOptionInActive, String query, String textField, String valueField, String selectedValue) throws SQLException {
        String str = "";
        selectedValue = selectedValue == null ? "" : selectedValue;

        DBConnection con = new DBConnection();
        con.connectToLocal();

        ResultSet rs = null;
        rs = con.executeQuery(query);
        
        String attrClassInActive;
        while (rs.next()) {
            attrClassInActive = !cssClassOptionInActive.equalsIgnoreCase("") && rs.getString("ACTIVE").equalsIgnoreCase("1") ? "" : " class=\"" + cssClassOptionInActive + "\"";
            if (rs.getString(valueField).compareTo(selectedValue) == 0) {
                str += String.format("<option value=\"%1$s\"%2$s selected=\"selected\">%3$s</option>", rs.getString(valueField), attrClassInActive, Util.formatHTMLString(rs.getString(textField), true));
            }
            else {
                str += String.format("<option value=\"%1$s\"%2$s>%3$s</option>", rs.getString(valueField), attrClassInActive, Util.formatHTMLString(rs.getString(textField), true));
            }
        }
 
        if (rs != null) {
            rs.close();
        }
        con.Close();
         
        return str;
    }
    //create by T.
    public static String generateOptionList(String cssClassOptionInActive, String query, String textField, String valueField, String selectedValue,boolean chktype) throws SQLException {
        String str = "";
        selectedValue = selectedValue == null ? "" : selectedValue;

        DBConnection con = new DBConnection();
        con.connectToLocal();

        ResultSet rs = null;
        rs = con.executeQuery(query);
        
        String attrClassInActive;
        while (rs.next()) {
            attrClassInActive = !cssClassOptionInActive.equalsIgnoreCase("") && rs.getString("ACTIVE").equalsIgnoreCase("1") ? "" : " class=\"" + cssClassOptionInActive + "\"";
            if (selectedValue.equals(rs.getString(valueField).toString().substring(0, 1))) {
                str += String.format("<option value=\"%1$s\"%2$s selected>%3$s</option>", rs.getString(valueField), attrClassInActive, Util.formatHTMLString(rs.getString(textField), true));
            }
            else {
                str += String.format("<option value=\"%1$s\"%2$s>%3$s</option>", rs.getString(valueField), attrClassInActive, Util.formatHTMLString(rs.getString(textField), true));
            }
        }
 
        if (rs != null) {
            rs.close();
        }
        con.Close();
         
        return str;
    }
  
    public static boolean insertRecord(DataRecord record) throws Exception {
        boolean result = false;
        if (record.table == null || DBMgr.isExist(record)) {
            return result;
        }
            
        DBConnection con = new DBConnection();
        ResultSet rs = null;
        try {
            con.connectToLocal();

            String query = String.format("SELECT * FROM %1$s WHERE 0 = 1", record.table);
            System.out.println(Variables.IS_TEST ? "Insert Table From DBMgr : "+query : "");
            rs = con.getStatementForInsert().executeQuery(query);
            rs.moveToInsertRow();

            DBMgr.updateResultSet(rs, record);

            rs.insertRow();
            result = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (con.IsOpened()) {
                con.Close();
            }
        }
        
        return result;
    }
    
    public static boolean updateRecord(DataRecord record) throws Exception {
        boolean result = false;
        
        if (record.table == null || !DBMgr.isExist(record)) {
            return result;
        }
            
        DBConnection con = new DBConnection();
        ResultSet rs = null;
        try {
            con.connectToLocal();

            String query = String.format("SELECT * FROM %1$s WHERE %2$s", record.table, DBMgr.generatePKCondition(record));
            System.out.println(Variables.IS_TEST ? "Update Table From DBMgr : "+query : "");
            rs = con.getStatementForInsert().executeQuery(query);
            while (rs.next()) {
                DBMgr.updateResultSet(rs, record);
                rs.updateRow();
            }
            result = true;
        }
        catch (Exception e) {
           System.out.println("Error for Update DBMgr : "+e);
           e.printStackTrace(); 
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (con.IsOpened()) {
                con.Close();
            }
        }
      
        rs.close();
        con.Close();
       
        return result;
    }

    public static DataRecord getRecord(String query) throws Exception {
        DataRecord record = null;
        DBConnection con = new DBConnection();
        ResultSet rs = null;
        //System.out.print(Variables.IS_TEST? "Get record from DBMgr : "+query : "");
        try {
            con.connectToLocal();

            rs = con.executeQuery(query);
            	//System.out.println("TEST :"+query);
            if (rs.next()) {
                record = new DataRecord();
                ResultSetMetaData rsmd = rs.getMetaData();

                int numCol = rsmd.getColumnCount();
                int colType;

                for (int i = 1; i < numCol + 1 ; i++) {
                    colType = rsmd.getColumnType(i);
                    switch (colType) {
                        case Types.CHAR :
                        case Types.VARCHAR :
                        case Types.NVARCHAR:
                            record.addField(rsmd.getColumnName(i), Types.VARCHAR, rs.getString(i));
                            break;
                        case Types.DOUBLE :
                        case Types.NUMERIC :
                            record.addField(rsmd.getColumnName(i), Types.DOUBLE, rs.getString(i));
                            break;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            con.Close();
        }
        
        return record;
    }

    public static boolean deleteRecord(DataRecord record){
        DBConn conn = new DBConn();
        try {
            conn.setStatement();
        } catch (SQLException ex) {
        }

        String tbl = record.table;

        int size = record.fields.size();

        DataField field;
        String SQLCommand = "";
        String tbl_condition = "";

        for (int i = 0; i < size; i++) {
            field = record.fields.elementAt(i);
            if(field.pk){
                tbl_condition += field.name + "='" + field.value + "' and ";
            }
        }
        tbl_condition = tbl_condition.substring(0,tbl_condition.length() - 4);

        SQLCommand = "delete " + tbl + " where "+ tbl_condition +" ";
        try{
            conn.insert(SQLCommand);
            conn.commitDB();
            conn.closeStatement("");
            conn.closeDB("");
            return true;
        }catch(Exception err){
            conn.rollDB();
            System.out.println(SQLCommand + err);
            return false;
        }
    }
    
    public static String getRecordValue(DataRecord rec, String fieldName) {
        return rec == null ? "" : rec.getField(fieldName).getValue();
    }

}