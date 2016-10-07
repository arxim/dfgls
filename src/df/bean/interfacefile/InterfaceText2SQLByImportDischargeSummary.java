package df.bean.interfacefile;

import df.bean.db.DataStreaming;
import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class InterfaceText2SQLByImportDischargeSummary extends InterfaceData{
    ArrayList<HashMap<Integer, String>> setDefaultDataCell;
    public InterfaceText2SQLByImportDischargeSummary(){
    	System.out.println("New Construct");
    }
    public InterfaceText2SQLByImportDischargeSummary(String hospitalcode,String source,String destination) {
    	System.out.println("In Class");
    	InterfaceData data=new InterfaceData();
    	data.setColHeader("HOSPITAL_CODE",0);
    	data.setColHeader("INVOICE_DATE",1);
    	data.setColHeader("INVOICE_NO",2);
    	data.setColHeader("COMPLETE_DATE",3);
    	data.setColHeader("HN_NO",4);
    	data.setColHeader("EPISODE_NO",5);
    	data.setColHeader("DOCTOR_CODE",6);
    	data.setColHeader("ORDER_ITEM_CODE",7);
    	data.setColHeader("LINE_NO",8);
    	data.setColHeader("PAYMENT_STATUS",9);     
    	data.setColHeader("YYYY",10);
    	data.setColHeader("MM",11);

     if(getFileExtension(source).equals("xlsx")){
    	 data.InfaceProcessingDataExcel2SQL(new DataStreaming().Excel2007_2_List(source), destination);
     }else if(getFileExtension(source).equals("xls")){
    	 data.InfaceProcessingDataExcel2SQL(new DataStreaming().Excel2003_2_List(source), destination);
     }
     
     DBConnection dbc=new DBConnection();
     dbc.connectToLocal();
     System.out.println("SELECT * FROM BATCH where CLOSE_DATE='' AND HOSPITAL_CODE='"+hospitalcode+"'");
     ResultSet re=dbc.executeQuery("SELECT * FROM BATCH where CLOSE_DATE='' AND HOSPITAL_CODE='"+hospitalcode+"'");
     try {
		while(re.next()){
			data.setDefaultDataCell(10, "[str("+re.getString("YYYY")+")]");
			data.setDefaultDataCell(11,  "[str("+re.getString("MM")+")]");
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
     for(int i=0;i<data.doProcessing.size();i++){
    	 data.doProcessing.get(i).put(1, JDate.saveDate(data.doProcessing.get(i).get(1)));
    	 data.doProcessing.get(i).put(3, JDate.saveDate(data.doProcessing.get(i).get(3)));
     }
     SQLCommit(destination,data.doProcessing); 
     dbc.executeUpdate("DELETE FROM INT_HIS_DISCHARGE WHERE SUBSTRING(COMPLETE_DATE,0,7)=SUBSTRING(INVOICE_DATE,0,7)");
     dbc.Close();
    }
   
}

