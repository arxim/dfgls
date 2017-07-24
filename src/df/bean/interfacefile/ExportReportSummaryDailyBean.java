/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package df.bean.interfacefile;

import df.bean.db.conn.DBConn;
import df.bean.db.conn.DBConnection;

/**
 *
 * @author nopphadon
 */
public class ExportReportSummaryDailyBean extends InterfaceTextFileBean{

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
        try {
            setFileName(fn);//set filename read
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
        }catch(Exception e){
            System.out.println("Export Data : "+e);
        }
        System.out.println(sub_data.length);
        //writeFile(sub_data);
        return writeFileNew(sub_data);
        //return status;
    }

	@Override
	public boolean exportData(String fn, String hp_code, String type, String year, String month, DBConn d, String path,
			String filing_type) {
		// TODO Auto-generated method stub
		return false;
	}

}
