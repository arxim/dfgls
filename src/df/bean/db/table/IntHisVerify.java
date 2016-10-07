/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.db.table;

import df.bean.db.conn.DBConnection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class IntHisVerify extends ABSTable {
    public IntHisVerify(DBConnection conn) {
        super();
        this.setDBConnection(conn);
    }    
    public boolean rollBackDelete(String hospitalCode, String startDate, String endDate) {
        List sqlCommand = new ArrayList();
        boolean ret = false;
        String sql = "DELETE FROM INT_HIS_VERIFY " 
                    + " WHERE TRANSACTION_DATE >= '" + startDate + "' AND TRANSACTION_DATE <= '" + endDate + "'"
                    + " AND HOSPITAL_CODE = '" + hospitalCode + "'";
        sqlCommand.add( sql );
        ret = super.rollBack(sqlCommand);
        return ret;
    }    
}
