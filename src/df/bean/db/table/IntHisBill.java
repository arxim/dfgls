/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.db.table;

import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class IntHisBill extends ABSTable {
    public IntHisBill(DBConnection conn) {
        super();
        this.setDBConnection(conn);
    }
        
    public boolean rollBackDelete(String hospitalCode, String startDate, String endDate) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql = "DELETE FROM INT_HIS_BILL " 
                    + " WHERE BILL_DATE >= '" + startDate + "' AND BILL_DATE <= '" + endDate + "'"
                    + " AND HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql );
        System.out.println(sql);
        ret = super.rollBack(sqlCommand);
        return ret;
    }    
}
