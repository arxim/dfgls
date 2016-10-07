/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.interfacefile;
import df.bean.db.conn.DBConnection;
import java.sql.ResultSet;
import java.sql.Statement;
import df.bean.db.conn.DBConn;
public class ExportDFToGLBean extends InterfaceTextFileBean{
    private ResultSet rs;
    private Statement stm;

    @Override
    public boolean insertData(String fn, DBConnection d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    private String ship(String text, char c, int lengthStr, String positon){
        int lengthText = text.length();
        if(positon==null) positon = "LEFT";
        if(lengthText==lengthStr){
            return text;
        }else if(lengthText > lengthStr){
            return "Error";
        }else{
            String str = "";
            int dif = lengthStr - lengthText;
            for(int i = 0; i < dif ; i++){
                str+= c + "";
            }
            if("LEFT".equalsIgnoreCase(positon)){
                return text+str;
            }else{
                return str+text;
            }
        }
    }

    @Override
    public boolean exportData(String fn, String hp, String type, String year, String month, DBConn dInMethod, String path) {
        boolean status = true;
        DBConnection conn = new DBConnection();
        conn.connectToLocal();
        
        String[] data_export = null;
        String data = "select HOSPITAL_CODE, SEQ_NO, ACCOUNTING_DT, ACCOUNTING_TIME, TYPE, " +
                "ACCOUNT, DEPT_ID, PRODUCT, MONETARY_AMOUNT, CLASS_FLD, CHARTFIELD2, CHARTFIELD3, " +
                "CASE WHEN (AMOUNT_SIGN = '*' OR AMOUNT_SIGN = '+') THEN '+' ELSE '-' END AS AMOUNT_SIGN " +
                //"CHARTFIELD2, CHARTFIELD3, AMOUNT_SIGN " +
                "from INT_ERP_GL " +
                "where YYYY = '"+year+"' and MM='"+month+"' AND HOSPITAL_CODE = '"+hp+"' and TYPE='"+type+"';";
        String countRowSQL = "select count(*) " +
                "from INT_ERP_GL " +
                "where YYYY = '"+year+"' and MM='"+month+"' AND HOSPITAL_CODE = '"+hp+"' and TYPE='"+type+"';";
        try {
            setFileName(path);
            //temp_data = dInMethod.query(data);

            int countRow = Integer.parseInt(conn.executeQueryString(countRowSQL));
            rs = conn.executeQuery(data);
            System.out.println(data);
            data_export = new String[countRow];
            int iCount = 0;
            while(rs.next()){
                data_export[iCount] = ""
                        + ship(rs.getString("HOSPITAL_CODE"),' ',5,"LEFT")
                        + ship(rs.getString("SEQ_NO"),'0',9,"RIGHT")
                        + ship(rs.getString("ACCOUNTING_DT"),' ',10,"LEFT")
                        //+ ship("DOCTORFEE",' ',10,"LEFT")
                        + ship("FRONTEND",' ',10,"LEFT")
                        + ship(rs.getString("ACCOUNT"),' ',6,"LEFT")
                        + ship(rs.getString("DEPT_ID"),' ',10,"LEFT")
                        + ship(rs.getString("PRODUCT"),' ',6,"LEFT")
                        + ship("",' ',15,"LEFT")
                        + ship("THB",' ',3,"LEFT")
                        + ship("",' ',3,"LEFT")
                        + ship("000000000000000",' ',15,"LEFT")
                        + ship(rs.getString("AMOUNT_SIGN"),' ',1,"RIGHT")
                        + ship(rs.getString("MONETARY_AMOUNT").replaceAll("\\.", ""),'0',14,"RIGHT")
                        + ship("",' ',8,"LEFT")
                        + ship(rs.getString("CLASS_FLD"),' ',5,"LEFT")
                        + ship("",' ',10,"LEFT")
                        + ship("",' ',10,"LEFT")
                        + ship("",' ',10,"LEFT")
                        //+ ship(rs.getString("CHARTFIELD2"),' ',10,"LEFT")
                        //+ ship(rs.getString("CHARTFIELD3"),' ',10,"LEFT")
                        + ship("",' ',5,"LEFT")
                        + ship("",' ',5,"LEFT");
                iCount++;
            }
        }catch(Exception e){
            System.out.println(e);
        }

        writeFileNew(data_export);
        return status;
    }
    
    public static void main(String[] arg){
    
    }
    
}
