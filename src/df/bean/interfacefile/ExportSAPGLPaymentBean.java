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

public class ExportSAPGLPaymentBean extends InterfaceTextFileBean{
	
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
        String data = ""+
        		"SELECT *, DENSE_RANK ()  OVER(ORDER BY DB_CR DESC, PRODUCT, STATISTICS_CODE) AS GROUP_ID "+
        		"FROM(SELECT CASE WHEN LEN(IEP.HOSPITAL_CODE)=5 THEN SUBSTRING(IEP.HOSPITAL_CODE,3,LEN(IEP.HOSPITAL_CODE)) ELSE IEP.HOSPITAL_CODE END AS HOSPITAL_CODE, "+
        		"ACCOUNTING_DT, ACCOUNTING_TIME, TYPE, CURRENCY, "+
        		"ACCOUNT, DEPT_ID, PRODUCT, SUM(MONETARY_AMOUNT) AS MONETARY_AMOUNT, CLASS_FLD, "+
        		"CASE WHEN (AMOUNT_SIGN = '*' OR AMOUNT_SIGN = '+') THEN '40' ELSE '50' END AS AMOUNT_SIGN, "+
        		"CASE WHEN (AMOUNT_SIGN = '-' OR AMOUNT_SIGN = '+') THEN 'DF' ELSE 'AJ' END AS DB_CR, "+
        		"AMOUNT_SIGN AS DB_CR_1, DP.GL_CODE AS DEPARTMENT_GL_CODE, AC.GL_CODE AS ACCOUNT_GL_CODE, "+
        		"IEP.PROJECT_ID, '' AS STATISTICS_CODE "+
        		"FROM INT_ERP_GL IEP "+
        		"LEFT JOIN HOSPITAL HP ON IEP.HOSPITAL_CODE = HP.CODE "+
        		"LEFT JOIN DEPARTMENT DP ON DP.CODE = IEP.DEPT_ID AND DP.HOSPITAL_CODE = IEP.HOSPITAL_CODE "+
        		"LEFT JOIN ACCOUNT AC ON AC.CODE = IEP.ACCOUNT "+
        	   	"WHERE IEP.YYYY = '"+year+"' AND IEP.MM='"+month+"' AND IEP.HOSPITAL_CODE = '"+hp+"' "+
        	   	"AND MONETARY_AMOUNT > 0 AND TYPE='"+type+"' "+
        		"AND CLASS_FLD != '00' "+
        		"GROUP BY IEP.HOSPITAL_CODE, ACCOUNTING_DT, ACCOUNTING_TIME, TYPE, CURRENCY, ACCOUNT, DEPT_ID, PRODUCT, CLASS_FLD, AMOUNT_SIGN, DP.GL_CODE, AC.GL_CODE, IEP.PROJECT_ID, IEP.STATISTICS_CODE "+
        		"UNION ALL "+
        		"SELECT CASE WHEN LEN(TEMP_GL.HOSPITAL_CODE)=5 THEN SUBSTRING(TEMP_GL.HOSPITAL_CODE, 3, LEN(TEMP_GL.HOSPITAL_CODE)) ELSE TEMP_GL.HOSPITAL_CODE END AS HOSPITAL_CODE, "+
        		"'', '', PROCESS, CURRENCY, "+
        		"ACCOUNT_CODE, PATIENT_DEPARTMENT_CODE, CASE WHEN ADMISSION_TYPE_CODE = 'O' THEN 'OPD' ELSE 'IPD' END, AMOUNT_AFT_DISCOUNT, NATIONALITY_CODE AS CLASS_FLD, "+
        		"CASE WHEN (AMOUNT_SIGN = '*' OR AMOUNT_SIGN = '+') THEN '40' ELSE '50' END AS AMOUNT_SIGN, "+
        		"CASE WHEN (AMOUNT_SIGN = '-' OR AMOUNT_SIGN = '+') THEN 'DF' ELSE 'AJ' END AS DB_CR, "+
        		"AMOUNT_SIGN AS DB_CR_1, DP.GL_CODE AS DEPARTMENT_GL_CODE, AC.GL_CODE AS ACCOUNT_GL_CODE, "+
        		"TEMP_GL.DOCTOR_CODE AS PROJECT_ID, LINE_NO AS STATISTICS_CODE "+
        		"FROM TEMP_GL "+
        		"LEFT JOIN HOSPITAL HP ON HOSPITAL_CODE = HP.CODE "+
        		"LEFT JOIN DEPARTMENT DP ON DP.CODE = PATIENT_DEPARTMENT_CODE AND DP.HOSPITAL_CODE = TEMP_GL.HOSPITAL_CODE "+
        		"LEFT JOIN ACCOUNT AC ON AC.CODE = ACCOUNT_CODE "+
        	   	"WHERE TEMP_GL.HOSPITAL_CODE = '"+hp+"' AND YYYY+MM = '"+year+month+"' "+
        	   	"AND PROCESS = '"+type+"' AND LINE_NO LIKE 'COSAP%' "+
        		")Q "+
        		"ORDER BY DB_CR DESC, PRODUCT, STATISTICS_CODE";
        System.out.println(data);
        try {
            setFileName(path);
            rs = conn.executeQuery(data);
            int countRow = cn.countRow(data);
            data_export = new String[countRow];
            int iCount = 0;
            String docNo = "";
            String docText = "";
            String postDate = "";
            if(type.equals("GL")){
            	postDate = this.payDate.replaceAll("/", ".");
            	docText = "DF Payment"+JDate.getEndMonthDate(year, month)+"."+month+"."+year+"|";
            }else{
            	docText = "Accrue DF Payment"+JDate.getEndMonthDate(year, month)+"."+month+"."+year+"|";
            	postDate = JDate.getEndMonthDate(year, month)+"."+month+"."+year;
            }
        	System.out.println(docText);
            int docCount = 3;
        	String lineNo = "";
            while(rs.next()){
            	String docDate = JDate.getEndMonthDate(year, month)+"."+month+"."+year;
            	String invNo = "";
            	String info = "";
            	String projId = "";
            	String nation = rs.getString("CLASS_FLD").toString();
            	docNo = rs.getString("GROUP_ID").toString();
        		if( rs.getString("DB_CR").equals("DF") ){
        			info = docText;
        			/*
        			if(rs.getString("PRODUCT").equals("IPD")){
        				docNo = "1";
        			}else{
        				docNo = "2";
        			}
        			*/
        		}else if( !rs.getString("CLASS_FLD").equals("00") && !rs.getString("DB_CR").equals("DF") ){
        			info = docText;
        			//docNo = "3";
        		}else{
        			if(!rs.getString("STATISTICS_CODE").equals("")){
        				if(!rs.getString("STATISTICS_CODE").equals(lineNo)){
        					docCount++;
        				}
        				lineNo = rs.getString("STATISTICS_CODE");
        				String arr[] = null;
        				try{
            				arr = rs.getString("STATISTICS_CODE").toString().split("[|]");
            				invNo = arr[1];
            				docDate = JDate.showDate(arr[2]).replaceAll("/", ".");        					
        				}catch(Exception e){
        					docDate = "";
        				}
        				projId = rs.getString("PROJECT_ID").toString();
        				nation = "TH";
        				try{
        					if(arr[3].equals("")){
        						info = docText;
        					}else{
        						if(arr[3].length()>50){
        							info = arr[3].substring(0, 50)+"|";
        						}else{
        							info = arr[3]+"|";        							
        						}
        					}
        				}catch(Exception e){
        					System.out.println(e);
        					info = "|";
        				}
        			}
        			//docNo = docCount+"";
        		}
                data_export[iCount] = ""
                		+ docNo+"|"
                		+ rs.getString("HOSPITAL_CODE")+"|"
                		+ docDate+"|"//invoice date (new SAP GL)
                		+ postDate+"|"
                		+ rs.getString("CURRENCY")+"|                         |001|"
                		+ rs.getString("AMOUNT_SIGN")+"|"
                		+ this.getValueNotNullGL(rs.getString("ACCOUNT_GL_CODE"))+"||||"
                		+ this.getValueNotNullGL(rs.getString("DEPARTMENT_GL_CODE")) +"|"
                		+ "          |"
                		+ rs.getString("MONETARY_AMOUNT")+"|"
                		+ info//patient name (new SAP GL)
                		+ rs.getString("PRODUCT")+"|"
                		+ nation+"|"
                		+ projId+"|"//doctor code (new SAP GL)
                		+ invNo+"|"//invoice no (new SAP GL)
                		+ invNo+"|";//invoice no (new SAP GL)
                iCount++;
            }
        }catch(Exception e){
            System.out.println(data_export.length+" Gen File Error: "+e+data);
        }

        writeFileNew(data_export);
        return status;
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