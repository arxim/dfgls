/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.interfacefile;

import df.bean.db.conn.DBConnection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import df.bean.db.conn.DBConn;
import df.bean.obj.util.*;

/**
 * @author nopphadon
 */
public class ExportRDTaxBean extends InterfaceTextFileBean {
	final static Logger logger = Logger.getLogger(ExportRDTaxBean.class);
    private ResultSet rs;
    private Statement stm;
    private String payment_date = "";
    private String transaction_date = "";
    private String message = "";

    public String getMessage(){
    	return this.message;
    }
    public void setPaymentDate(String s){
    	this.payment_date = s;
    }
    public void setTransactionDate(String s){
    	this.transaction_date = s;
    }
    
    @Override
    public boolean insertData(String fn, DBConnection d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    @Override
    public boolean exportData(String fn, String hp, String type, String year, String month, DBConn d, String path, String filing_type) {
        boolean status = true;
        String tax_month = "";
        String[][] temp_data = null;
        //if(Variables.phase.equals("test")){}
        String cond = "";
        
        logger.info(type);
        //type "00" ภงด.1ก
        if(type.equals("00")){
        	tax_month = "00";
        	month = "13";
        }else{
        	tax_month = month;
        }
        logger.info(month);
        if(type.equals("00")) {
        	cond = "AND D.TAX_402_METHOD LIKE (CASE WHEN S.HOSPITAL_CODE = '00019' THEN '%' ELSE 'STP' END) ";
        }
        else if(type.equals("01")){
        	cond = "AND D.TAX_402_METHOD LIKE (CASE WHEN S.HOSPITAL_CODE = '00019' THEN '%' ELSE 'STP' END) AND S.IS_LEGAL_ENTITY <> 'Y' ";
        }
        else if(type.equals("03")) {
        	cond = "AND S.TAX_402_METHOD NOT IN ('STP', 'SUM') AND S.IS_LEGAL_ENTITY <> 'Y' ";
        }
        else if(type.equals("53")) {
        	cond = "AND S.TAX_402_METHOD NOT IN ('STP', 'SUM') AND S.IS_LEGAL_ENTITY = 'Y' ";
        }
        
        String dat = "SELECT '"+type+"', H.TAXNO, '0000000000', '0000', "+//0-3
        			 "CASE WHEN D.TAX_ID = '' THEN '0000000000000' ELSE D.TAX_ID END AS NATION_ID, " +//4 get from doctor
        			 "'0000000000' AS TAX_ID, " +//5 fix 0
        			 "'', D.NAME_THAI, '', SUBSTRING(D.ADDRESS1+' '+D.ADDRESS2,1,80), D.ADDRESS3, D.ZIP, "+//6-11
        			 "'"+tax_month+"', S.YYYY, '4', '"+this.payment_date+"', "+ //12-15
        			 "'0', S.SUM_NORMAL_TAX_AMT, S.NET_TAX_MONTH, '1', "+//16-19
        			 "CASE WHEN S.IS_LEGAL_ENTITY = 'Y' THEN 'ค่าเช่าเครื่องมือ' ELSE 'ค่าวิชาชีพ' END AS REVENUE_TYPE, S.TAX_402_METHOD "+
        			 "FROM DOCTOR D LEFT OUTER JOIN HOSPITAL H "+
        			 "ON D.HOSPITAL_CODE = H.CODE "+
        			 "LEFT OUTER JOIN DOCTOR_PROFILE DP "+
        			 "ON D.DOCTOR_PROFILE_CODE = DP.CODE AND D.HOSPITAL_CODE = DP.HOSPITAL_CODE "+
        			 "LEFT OUTER JOIN SUMMARY_TAX_402 S "+
        			 "ON D.CODE = S.DOCTOR_CODE AND D.HOSPITAL_CODE = S.HOSPITAL_CODE "+
        			 "WHERE S.HOSPITAL_CODE = '"+hp+"' AND "+
        			 "S.YYYY = '"+year+"' AND S.ACTIVE = '1' AND "+
        			 "S.MM = '"+month+"' AND SUM_NORMAL_TAX_AMT > 0 "+cond;  
        
        //if(type.equals("R00")){
        //	dat = dat.replaceAll("S.MM = '"+month+"'", "S.MM = '13'");
        //}
        logger.info(dat);
        try {
            setFileName(path);//set filename read
            temp_data = d.query(dat);//get data            
            if(temp_data.length>0){
            	if(type.equals("03") || type.equals("53")) {
            		writeFileNew(setFormatFile(temp_data, type));
            	}
            	else {
            		writeFileNew(setFormatFilePayroll(temp_data, type, filing_type));
            	}
            	//writeFileNew(setFormatFile(temp_data, type));
            }else{
            	logger.info("Data is null");
            	logger.info(dat);
            	this.message = "There is no data.";
            	status = false;
            }
        }catch(Exception e){
        	status = false;
            logger.error(e);
        }

        return status;
    }
    
    private String[] setFormatFile(String[][] t, String tax_type){
    	String tax_month = "";
    	String[] dt = new String[t.length];
    	try{
	    	for(int i = 0; i<(t.length); i++){
	    		//Initial Data
	    		t[i][2] = t[i][2].replaceAll(" ", "");
	    		t[i][2] = t[i][2].replaceAll("-", "");
	    		t[i][4] = t[i][4].replaceAll(" ", "");
	    		t[i][4] = t[i][4].replaceAll("-", "");
	    		t[i][5] = t[i][5].replaceAll(" ", "");
	    		t[i][5] = t[i][5].replaceAll("-", "");
	    		if(t[i][4].length()!= 13){ t[i][4] = "0000000000000"; }
	    		if(t[i][5].length()!= 10){ t[i][5] = "0000000000"; }
	    		if(tax_type.equals("00")){
	    			tax_month = "00";
	    		}else{
	    			tax_month = JDate.getNextMonth(t[i][12], t[i][13]);
	    		}
	    		//Initial Data End
	    		
	    		dt[i]= t[i][0]+"|"+ //Normal
	    		t[i][1]+"|"+//Employer ID
	    		t[i][2]+"|"+//Employer Tax ID
	    		t[i][3]+"|"+//Employer Branch Location
	    		t[i][4]+"|"+//National ID
	    		t[i][5]+"|"+//Tax ID
	    		t[i][6]+"|"+//Title Name
	    		t[i][7]+"|"+//Employee Name
	    		t[i][8]+"|"+//Employee Surname
	    		t[i][9]+"|"+//Address 1
	    		t[i][10]+"|"+//Address 2
	    		t[i][11]+"|"+//Post Code
	    		tax_month+"|"+//Tax Month
	    		JDate.getThaiYear(JDate.getYearOfNextMonth(t[i][12], t[i][13]))+"|"+//Tax Year
	    		t[i][14]+"|"+//Income Code
	    		t[i][15].substring(0, 4)+JDate.getThaiYear(t[i][13])+"|"+//Pay Date
	    		t[i][16]+"|"+//Tax Rate
	    		t[i][17]+"|"+//Revenue
	    		t[i][18]+"|"+//Tax
	    		t[i][19]+"|"+//Tax Condition
	    		t[i][20]+"|"+//RevenueType
	    		t[i][21]+"|"+//Tax402Method
	    		JDate.saveTaxDate(this.payment_date);//Pay Date;
	    	}
    	}catch (Exception e){
    		logger.error("RD Tax Write : "+e);
    	}
    	return dt;
    }
    private String[] setFormatFilePayroll(String[][] t, String tax_type, String filing_type){
    	String tax_month = "";
    	String[] dt = new String[t.length];
    	try{
	    	for(int i = 0; i<(t.length); i++){
	    		//Initial Data
	    		t[i][2] = t[i][2].replaceAll(" ", "");
	    		t[i][2] = t[i][2].replaceAll("-", "");
	    		t[i][4] = t[i][4].replaceAll(" ", "");
	    		t[i][4] = t[i][4].replaceAll("-", "");
	    		t[i][5] = t[i][5].replaceAll(" ", "");
	    		t[i][5] = t[i][5].replaceAll("-", "");
	    		if(t[i][4].length()!= 13){ t[i][4] = "0000000000000"; }
	    		if(t[i][5].length()!= 10){ t[i][5] = "0000000000"; }
	    		
	    		if(tax_type.equals("00")){
	    			tax_month = "00";
	    		}else{
	    			tax_month = JDate.getNextMonth(t[i][12], t[i][13]);
	    		}
	    		dt[i] = filing_type+"|";
	    		//Initial Data End
	    		//System.out.println(t[i][0]);
	    		/*if(t[i][0].equals("00")){
	    			dt[i]= "0|";
	    		}
	    		else{
	    			dt[i]= "2|";
	    		}*/
	    		dt[i] = dt[i]+//Normal
	    		"000000|"+//Employer ID
	    		t[i][4]+"|"+//National ID
	    		t[i][5]+"|"+//Tax ID
	    		t[i][6]+"|"+//Title Name
	    		t[i][7]+"|"+//Employee Name
	    		t[i][8]+"|"+//Employee Surname
	    		JDate.saveTaxDate(this.payment_date)+"|"+//Pay Date
	    		t[i][17]+"|"+//Revenue
	    		t[i][18]+"|"+//Tax
	    		"1";//Tax Condition
	    	}
    	}catch (Exception e){
    		logger.error("RD Tax Write : "+e);
    	}
    	return dt;
    }
    
	@Override
	public boolean exportData(String fn, String hp_code, String type, String year, String month, DBConn d, String path) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
