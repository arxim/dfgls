package df.bean.interfacefile;
import java.io.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import com.lowagie.text.Row;

import df.bean.db.conn.DBConnection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import df.bean.db.conn.DBConn;
import df.bean.process.ProcessUtil;
import df.bean.db.table.Batch;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;

public class ImportTimeTableBean extends InterfaceTextFileBean {
	String hospital_code;
	String result = "";
	String user_name=this.getUserName();
	String batch_year="", batch_month="";
	String sqlMessage="";
	int count=0;
	
	public void setHospital(String s){
		this.hospital_code = s;	
    }
	
	@Override
	public boolean insertData(String fn, DBConnection d) {
		DBConn cdb= null;
		DBConnection cc = new DBConnection();		
    	boolean statusSave=true, status=false, statusShow=true;
		String processName="ImportTimeTable";
		TRN_Error.setUser_name(this.getUserName());
		TRN_Error.setHospital_code(this.hospital_code);
		
    	try {
    		cc.connectToLocal();
    		cdb = new DBConn();
            if (cdb.getStatement() == null) {
                cdb.setStatement();
            }
        } catch (SQLException ex) {
            this.result = ""+ex;
            this.setMessage(this.result);
            System.out.println(ex);
        }
		//----------END DBConn
    	//Start DATA 
    	try{
    		System.out.println("Start Import Excel TimeTable");
    		FileInputStream fileName=new FileInputStream(fn);
	    	POIFSFileSystem fs = new POIFSFileSystem(fileName);
	    	HSSFWorkbook workbook = new HSSFWorkbook(fs);
	    	HSSFSheet sheet = workbook.getSheetAt(0);
	    	HSSFRow tempRow;
    		String sql_insert="";
	    	for (int r = 2; r <= sheet.getPhysicalNumberOfRows(); r++) {
	    		System.out.println(r);
	    		tempRow = sheet.getRow(r);
	    		if((tempRow != null)){
	    			if(tempRow.getCell((short) 0)!=null && tempRow.getCell((short) 0).toString()!=""){
	    				//System.out.println(tempRow.getCell((short) 0));
	    				sql_insert ="INSERT INTO STP_GUARANTEE VALUES";
		    			sql_insert +="(";
	    				//HOSPITAL_CODE
						sql_insert += "'"+this.hospital_code+"',";
						//GUARANTEE_DR_CODE
						if((tempRow.getCell((short) 0).getCellType()== HSSFCell.CELL_TYPE_STRING)){
							sql_insert += "LTRIM(RTRIM("+"'"+tempRow.getCell((short) 0)+"')),";
						}else{
							sql_insert += "#"+tempRow.getCell((short) 0)+",";
						}
						//GUARANTEE_CODE
						if(tempRow.getCell((short) 1).toString().equals("DLY")){
							sql_insert += "'"+JDate.saveDate(tempRow.getCell((short) 3).toString())+tempRow.getCell((short) 5).toString()+"',";
						}else if(tempRow.getCell((short) 1).toString().equals("MLD")){
							sql_insert += "'"+JDate.saveDate(tempRow.getCell((short) 3).toString()).substring(0, 6)+"',";
						}else{
							sql_insert +="'"+ JDate.saveDate(tempRow.getCell((short) 3).toString()).substring(0, 6)+"',";
						}
						//GUARANTEE_TYPE_CODE
						sql_insert += "'"+tempRow.getCell((short) 1)+"',";
						//GUARANTEE_LOCATION
						sql_insert +="'',";
						//IS_INCLUDE_LOCATION
						sql_insert +="'',";
						//ADMISSION_TYPE_CODE
						sql_insert += "'"+tempRow.getCell((short) 2)+"',";
						//MM
						sql_insert += "'"+JDate.saveDate(tempRow.getCell((short) 3).toString()).substring(4, 6)+"',";
						//YYYY
						sql_insert +="'"+ JDate.saveDate(tempRow.getCell((short) 3).toString()).substring(0, 4)+"',";
						//START_DATE
						if((tempRow.getCell((short) 3).getCellType()== HSSFCell.CELL_TYPE_STRING)){
							sql_insert +="LTRIM(RTRIM("+"'"+ JDate.saveDate(tempRow.getCell((short) 3).toString())+"')),";
						}else{
							sql_insert +="#"+ JDate.saveDate(tempRow.getCell((short) 3).toString())+",";
						}
						//START_TIME
						if((tempRow.getCell((short) 5).getCellType()== HSSFCell.CELL_TYPE_STRING)){
							sql_insert +="LTRIM(RTRIM("+"'"+ tempRow.getCell((short) 5).toString()+"')),";
						}else{
							sql_insert +="#"+ tempRow.getCell((short) 5).toString()+",";
						}
						//EARLY_TIME
						if((tempRow.getCell((short) 7).getCellType()== HSSFCell.CELL_TYPE_STRING)){
							if(tempRow.getCell((short) 7).toString()!= null  && tempRow.getCell((short) 7).toString()!= ""){
								sql_insert +="LTRIM(RTRIM("+"'"+ tempRow.getCell((short) 7).toString()+"')),";
							}else{
								sql_insert +="LTRIM(RTRIM("+"'"+ tempRow.getCell((short) 5).toString()+"')),";
							}
						}else{
							sql_insert +="#"+ tempRow.getCell((short) 7).toString()+",";
						}
						//END_DATE
						if((tempRow.getCell((short) 4).getCellType()== HSSFCell.CELL_TYPE_STRING)){
							sql_insert +="LTRIM(RTRIM("+"'"+ JDate.saveDate(tempRow.getCell((short) 4).toString())+"')),";
						}else{
							sql_insert +="#"+ JDate.saveDate(tempRow.getCell((short) 4).toString())+",";
						}
						//END_TIME
						if((tempRow.getCell((short) 6).getCellType()== HSSFCell.CELL_TYPE_STRING)){
							sql_insert +="LTRIM(RTRIM("+"'"+ tempRow.getCell((short) 6).toString()+"')),";
						}else{
							sql_insert +="#"+ tempRow.getCell((short) 6).toString()+",";
						}
						//LATE_TIME
						if((tempRow.getCell((short) 8).getCellType()== HSSFCell.CELL_TYPE_STRING)){
							if(tempRow.getCell((short) 8).toString()!= null  && tempRow.getCell((short) 8).toString()!= ""){
								sql_insert +="LTRIM(RTRIM("+"'"+ tempRow.getCell((short) 8).toString()+"')),";
							}else{
								sql_insert +="LTRIM(RTRIM("+"'"+ tempRow.getCell((short) 6).toString()+"')),";
							}
						}else{
							sql_insert +="#"+ tempRow.getCell((short) 8).toString()+",";
						}
						//GUARANTEE_AMOUNT,GUARANTEE_EXCLUDE_AMOUNT
						if(tempRow.getCell((short) 10).toString().equals("GA")){
						sql_insert +="'"+tempRow.getCell((short) 9)+"',";
						}else{
						sql_insert +="0.00,";
						}
						//GUARANTEE_FIX_AMOUNT
						sql_insert +="0.00,";
						//GUARANTEE_INCLUDE_AMOUNT
						sql_insert +="0.00,";
						//GUARANTEE_EXCLUDE_AMOUNT
						if(tempRow.getCell((short) 10).toString().equals("OT")){
							sql_insert += tempRow.getCell((short) 9)+" ,";
							}else{
							sql_insert +="0.00,";
							}
						//OVER_ALLOCATE_PCT
						if(tempRow.getCell((short) 12).toString()!= null  && tempRow.getCell((short) 12).toString()!= ""){
							sql_insert += tempRow.getCell((short) 12)+" ,";
							}else{
							sql_insert +="100,";
							}
						//GUARANTEE_ALLOCATE_PCT
						if(tempRow.getCell((short) 11).toString()!= null && tempRow.getCell((short) 11).toString()!= ""){
							sql_insert += tempRow.getCell((short) 11)+" ,";
							}else{
							sql_insert +="100,";
							}
						//ACTIVE
						sql_insert +="'1',";
						//UPDATE_DATE
						sql_insert +="'',";
						//UPDATE_TIME
						sql_insert +="'',";
						//USER_ID
						sql_insert +="'Import:',";
						//OVER_GUARANTEE_AMOUNT
						sql_insert +="0.00,";
						//GUARANTEE_DAY
						sql_insert +="'VER',";
						//GUARANTEE_PERIOD
						sql_insert +="'',";
						//GUARANTEE_SOURCE
						//sql_insert += "'"+tempRow.getCell((short) 13)+"',";
						if(tempRow.getCell((short) 13) == null ){
							sql_insert += "'AF',";
						}else{
							sql_insert += "'"+tempRow.getCell((short) 13)+"',";
						}
						//AMOUNT_OF_TIME
						sql_insert += "0.00,";
						//AMOUNT_PER_TIME
						sql_insert +="0.00,";
						//INCLUDE_OF_TIME
						sql_insert +="0.00,";
						//INCLUDE_PER_TIME
						sql_insert +="0.00,";
						//DEDUCT_ABSORB_AMOUNT
						sql_insert +="0.00,";
						//ABSORB_AMOUNT
						sql_insert +="0.00,";
						//ABSORB_REMAIN_AMOUNT
						sql_insert +="0.00,";
						//NOTE
						sql_insert +="'',";
						//IS_GUARANTEE_DAILY
						if(tempRow.getCell((short) 1).toString().equals("MLY")|| tempRow.getCell((short) 1).toString().equals("MLA")){
							sql_insert +="'N',";
						}else{
							sql_insert +="'Y',";
						}
						//IS_PROCESS	
						sql_insert +="'',";
						//GL_DEPARTMENT_CODE
						sql_insert +="'',";
						//OLD_ABSORB_AMOUNT
						sql_insert +="0.00 ,";
						//OLD_GUARANTEE_AMOUNT
						sql_insert +="0.00,";
						//OLD_GUARANTEE_FIX_AMOUNT	
						sql_insert +="0.00 ,";
						//OLD_GUARANTEE_INCLUDE_AMOUNT	
						sql_insert +="0.00 ,";
						//OLD_GUARANTEE_EXCLUDE_AMOUNT	
						sql_insert +="0.00 ,";
						//OLD_ACTIVE
						sql_insert +="'',";	
						//OLD_START_DATE	
						sql_insert +="'',";
						//OLD_END_DATE	
						sql_insert +="'',";
						//OLD_GUARANTEE_SOURCE	
						sql_insert +="'',";
						//DF406_CASH_AMOUNT
						sql_insert +="0.00 ,";
						//DF406_CREDIT_AMOUNT
						sql_insert +="0.00 ,";
						//DF406_HOLD_AMOUNT	
						sql_insert +="0.00 ,";
						//DF402_CASH_AMOUNT	
						sql_insert +="0.00 ,";
						//DF402_CREDIT_AMOUNT	
						sql_insert +="0.00 ,";
						//DF402_HOLD_AMOUNT	
						sql_insert +="0.00 ,";
						//DF400_CASH_AMOUNT	
						sql_insert +="0.00 ,";
						//DF400_CREDIT_AMOUNT	
						sql_insert +="0.00 ,";
						//DF400_HOLD_AMOUNT
						sql_insert +="0.00 ,";
						//DF_CASH_AMOUNT	
						sql_insert +="0.00 ,";
						//DF_HOLD_AMOUNT	
						sql_insert +="0.00 ,";
						//HP402_ABSORB_AMOUNT	
						sql_insert +="0.00 ,";
						//DF406_ABSORB_AMOUNT	
						sql_insert +="0.00 ,";
						//DF402_ABSORB_AMOUNT	
						sql_insert +="0.00 ,";
						//DF400_ABSORB_AMOUNT	
						sql_insert +="0.00 ,";
						//DF_ABSORB_AMOUNT	
						sql_insert +="0.00 ,";
						//SUM_DR_OVER_AMOUNT	
						sql_insert +="0.00 ,";
						//SUM_HP_OVER_AMOUNT	
						sql_insert +="0.00 ,";
						//GUARANTEE_PAID_AMOUNT	
						sql_insert +="0.00 ,";
						//SUM_TAX_406	
						sql_insert +="0.00 ,";
						//SUM_TAX_402	
						sql_insert +="0.00 ,";
						//SUM_TAX_400	
						sql_insert +="0.00 ";
						sql_insert += ")";
		    			count++;
	    			}else{break;}
		    		try {
		    			System.out.print(sql_insert);
						cdb.insert(sql_insert);
					} catch (Exception e){
						status= false;
						this.setMessage("Import Time Table :"+e.toString());
			    		TRN_Error.writeErrorLog(new DBConnection(),processName,"row:"+count+"  source:"+fn, e.toString(), sqlMessage,"");
					}
	    		}
	    	}//End Loop
	    	if(status){
	    		cdb.commitDB();
	    		this.setMessage(" Complete" +count+" Row");
	    	}else{
	    		cdb.rollDB();
	    		this.setMessage(" Plaese check your data!! ");
	    	}

    		cdb.closeDB("");	    	
	    	
    	}catch(Exception e)
    	{
    		System.out.println("Error : "+e);
    		this.setMessage(" :  Invalid format of template");
    		//this.setMessage("Can't open file excel");
    		System.out.println(e);
    		TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), e.toString(), sqlMessage,"");
    		status= true;

    	}
    	cdb.closeDB("");
		return status= true;
	}
	@Override
    public boolean exportData(String fn, String hp, String type, String year, String month, DBConn d, String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }	
}