/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.interfacefile;

import org.apache.log4j.Logger;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;
import df.bean.db.table.PayorOffice;

/**
 *
 * @author nopphadon
 */
public class ExportReportSummaryDailyBean extends InterfaceTextFileBean{
	final static Logger logger = Logger.getLogger(ExportReportSummaryDailyBean.class);

    @Override
    public boolean insertData(String fn, DBConnection d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean exportData(String fn, String hp_code, String type, String year, String month, DBConn d, String path) {
        //type = sql statement command
        //fn = file name
        //path = folder to write file
        String[] title_data = null;
        String[] sub_data = null;
        String[][] temp_data = null;
        boolean status = false;
        try {
            setFileName(fn);//set filename read
            logger.info("type: "+type);
            if(hp_code.equals("Accrue") || hp_code.equals("GL")) {
            	int size = d.query(type).length;
            	int start = 1, end = 50000;
            	boolean newFile = true;
            	for(int j = 0; j < Math.ceil(size/50000.0); j++) {
            		if(j == 0) {
            			type += "WHERE Q.ROWNUMBERS BETWEEN "+start+" AND "+end;
            		}else {
            			newFile = false;
            			start += 50000;
            			end += 50000;
            			type = type.replaceFirst("WHERE Q.ROWNUMBERS BETWEEN(.*)", "WHERE Q.ROWNUMBERS BETWEEN "+start+" AND "+end);
            		}
            		temp_data = d.query(type);
    	            d.countColumn(type);
    	            title_data = d.getTitleName();
    	            sub_data = new String[temp_data.length+1];            
    	            for(int i = 0; i<sub_data.length; i++){
    	                if(i == 0){
    	                	if(j == 0) {
	    	                    sub_data[i] = "";
	    	                    for(int x = 0; x<title_data.length; x++){
	    	                        sub_data[i] = sub_data[i]+title_data[x]+"|";
	    	                    }
    	                	}
    	                }else{
    	                    sub_data[i] = "";
    	                    for(int x = 0; x<temp_data[i-1].length; x++){
    	                        sub_data[i] = sub_data[i]+temp_data[i-1][x]+"|";
    	                    }
    	                }
    	            }
    	            status = writeFileNewACGL(sub_data, newFile);
            	}
            }else{
            	temp_data = d.query(type);
	            d.countColumn(type);
	            title_data = d.getTitleName();
	            sub_data = new String[temp_data.length+1];            
	            for(int i = 0; i<sub_data.length; i++){
	                if(i == 0){
	                    sub_data[i] = "";
	                    for(int x = 0; x<title_data.length; x++){
	                        sub_data[i] = sub_data[i]+title_data[x]+"|";
	                    }
	                }else{
	                    sub_data[i] = "";
	                    for(int x = 0; x<temp_data[i-1].length; x++){
	                        sub_data[i] = sub_data[i]+temp_data[i-1][x]+"|";
	                    }
	                }
	            }
	            status = writeFileNew(sub_data);
            }
        }catch(Exception e){
            logger.error("Export Data : "+e);
        }
        return status;
    }

	@Override
	public boolean exportData(String fn, String hp_code, String type, String year, String month, DBConn d, String path,
			String filing_type) {
		// TODO Auto-generated method stub
		return false;
	}

}
