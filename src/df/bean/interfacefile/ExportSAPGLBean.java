/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.interfacefile;
import df.bean.db.conn.DBConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import df.bean.db.conn.DBConn;
import df.bean.obj.util.JDate;

public class ExportSAPGLBean extends InterfaceTextFileBean{
	
    private ResultSet rs;
    private String payDate;
    
    @Override
    public boolean insertData(String fn, DBConnection d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setPayDate(String pdate){
    	this.payDate = pdate.replaceAll("//", ".");
    }
    
    @Override
    public boolean exportData(String fn, String hp, String type, String year, String month, DBConn dInMethod, String path) {
      
    	boolean status = true;
        DBConnection conn = new DBConnection();
        conn.connectToLocal();
        DBConn cn = new DBConn();
        try { cn.setStatement(); } catch (SQLException e1) {}
        
        String[] data_export = null;
        String data = "SELECT CASE WHEN LEN(IEP.HOSPITAL_CODE)=5 THEN SUBSTRING(IEP.HOSPITAL_CODE,3,LEN(IEP.HOSPITAL_CODE)) ELSE IEP.HOSPITAL_CODE END AS HOSPITAL_CODE,"+ 
         		" ACCOUNTING_DT, ACCOUNTING_TIME, TYPE, CURRENCY, " +
                " ACCOUNT, DEPT_ID, PRODUCT, SUM(MONETARY_AMOUNT) AS MONETARY_AMOUNT, CLASS_FLD, " +
                " CASE WHEN (AMOUNT_SIGN = '*' OR AMOUNT_SIGN = '+') THEN '40' ELSE '50' END AS AMOUNT_SIGN, " +
                " CASE WHEN (AMOUNT_SIGN = '-' OR AMOUNT_SIGN = '+') THEN 'DF' ELSE 'AJ' END AS DB_CR, "+
                " AMOUNT_SIGN AS DB_CR_1, DP.GL_CODE AS DEPARTMENT_GL_CODE,  AC.GL_CODE AS ACCOUNT_GL_CODE " +
                " FROM INT_ERP_GL IEP " +
                " LEFT JOIN HOSPITAL HP ON IEP.HOSPITAL_CODE = HP.CODE "+
                " LEFT JOIN DEPARTMENT DP ON DP.CODE =  IEP.DEPT_ID AND DP.HOSPITAL_CODE =  IEP.HOSPITAL_CODE " + 
                " LEFT JOIN ACCOUNT AC ON AC.CODE = IEP.ACCOUNT " + 
                " WHERE IEP.YYYY = '"+year+"' AND IEP.MM='"+month+"' AND IEP.HOSPITAL_CODE = '"+hp+"' " +
                " AND MONETARY_AMOUNT > 0 AND TYPE='"+type+"'" +
                " GROUP BY IEP.HOSPITAL_CODE, ACCOUNTING_DT, ACCOUNTING_TIME, TYPE, CURRENCY, ACCOUNT, DEPT_ID, PRODUCT, CLASS_FLD, AMOUNT_SIGN  , DP.GL_CODE ,  AC.GL_CODE"+
                " ORDER BY DB_CR DESC, PRODUCT;";
        try {
            setFileName(path);
            rs = conn.executeQuery(data);
            int countRow = cn.countRow(data);
            data_export = new String[countRow];
            int iCount = 0;
            String docNo = "";
            String docText = "";
            String postDate = "";
            String item = "";
            if(type.equals("GL")){
            	item = "||||";
            	postDate = this.payDate.replaceAll("/", ".");
            	docText = "DF Payment"+JDate.getEndMonthDate(year, month)+"."+month+"."+year+"|";
            }else{
            	item = "";
            	docText = "Accrue DF Payment"+JDate.getEndMonthDate(year, month)+"."+month+"."+year+"|";
            	postDate = JDate.getEndMonthDate(year, month)+"."+month+"."+year;
            }
        	//System.out.println(data);
            while(rs.next()){
            	
        		if( rs.getString("DB_CR").equals("DF")){
        			if(rs.getString("PRODUCT").equals("IPD")){
        				docNo = "1";
        			}else{
        				docNo = "2";
        			}
        		}else{
        			docNo = "3";
        		}
        		//System.out.println("start"+iCount);
                data_export[iCount] = ""
                		+ docNo+"|"
                		//+ this.genDocNo(iCount+1, 900)+"|"
                		+ rs.getString("HOSPITAL_CODE")+"|"
                		//+ rs.getString("HOSPITAL_CODE").substring(2, 5)+"|"
                		+ JDate.getEndMonthDate(year, month)+"."+month+"."+year+"|"
                		+ postDate+"|"
                		+ rs.getString("CURRENCY")+"|                         |001|"
                		+ rs.getString("AMOUNT_SIGN")+"|"
                		+ this.getValueNotNullGL(rs.getString("ACCOUNT_GL_CODE"))+"||||"
                		+ this.getValueNotNullGL(rs.getString("DEPARTMENT_GL_CODE")) +"|"
                		+ "          |"
                		+ rs.getString("MONETARY_AMOUNT")+"|"
                		+ docText
                		+ rs.getString("PRODUCT")+"|"
                		+ rs.getString("CLASS_FLD")
                		+ item ;
                //System.out.println(data_export[iCount]);
                iCount++;
            }
        }catch(Exception e){
            System.out.println(data_export.length+" Gen File Error: "+e+data);
        }

        writeFileNew(data_export);
        return status;
    }

    public String genDocNo(int count, int max){
        try{
        	int sign = (count > 0 ? 1 : -1) * (max > 0 ? 1 : -1);
            return ""+sign * (Math.abs(count) + Math.abs(max) - 1) / Math.abs(max);
        }catch(NumberFormatException ex){
            ex.printStackTrace();
        }
        return null;
    }

    private String getValueNotNullGL(String val){
    	if(val != null)
    		return val;
    	System.out.println("err "+val);
    	return "";
    }

    public static void main(String[] arg){
    	ExportSAPGLBean e = new ExportSAPGLBean();
    	System.out.println(e.genDocNo(0, 900));
    }

	@Override
	public boolean exportData(String fn, String hp_code, String type, String year, String month, DBConn d, String path,
			String filing_type) {
		// TODO Auto-generated method stub
		return false;
	}
}