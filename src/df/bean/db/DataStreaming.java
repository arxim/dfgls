/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package df.bean.db;

import df.bean.db.conn.DBConnection;
import df.bean.interfacefile.InterfaceData;
import df.bean.obj.util.JDate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author TomMy
 */
public class DataStreaming {
    private DBConnection con;
    public DataStreaming(DBConnection in){
        this.con=in;
    }
    public DataStreaming() {
		// TODO Auto-generated constructor stub
	}
	public  ArrayList<HashMap<String, String>>  ReadFile(InputStream in){
        return null;
    }
 
    
    public String[] QueryCol(String table) {
        String [] ColData = null;
        try {
                ResultSet rs = con.executeQuery("SELECT * FROM "+table);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                ColData=new String[columnCount];
                // The column count starts from 1
                for (int i = 1; i < columnCount + 1; i++ ) {
                  String name = rsmd.getColumnName(i);
                  ColData[i-1]=name;
                  // Do stuff with name
                }
            
        } catch (SQLException ex) {
            Logger.getLogger(DataStreaming.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ColData;
    }
       public  ArrayList<HashMap<String,String>> ArrayColheader=new ArrayList<HashMap<String,String>>();
    public ArrayList<HashMap<String,String>> setColHeader(String colName,Integer Index) {
       HashMap<String,String> map=new HashMap<String,String>();
       map.put("Index",String.valueOf(Index));
       map.put("colName",colName );
      ArrayColheader.add(map);
      return ArrayColheader;
    }
    public ArrayList<HashMap<Integer,String>> getDataFromTable(String [] colMetaData,String database){
          ArrayList<HashMap<Integer,String>> SQLDATA=new ArrayList<HashMap<Integer,String>>();
        try {
            ResultSet executeQuery = con.executeQuery("select * from "+database);
            while(executeQuery.next()){
                HashMap<Integer,String> map=new HashMap<Integer,String>();
                for(int i=0;i<colMetaData.length;i++){
                    map.put(i,executeQuery.getString(i+1).trim());
                }
                SQLDATA.add(map);
            }
            
            
           
        } catch (SQLException ex) {
            Logger.getLogger(DataStreaming.class.getName()).log(Level.SEVERE, null, ex);
        }
         return SQLDATA;
    }
    
    
    public ArrayList<HashMap<Integer,String>> getDataFromSQL(String [] colMetaData,String SQLCOMMAND){
        ArrayList<HashMap<Integer,String>> SQLDATA=new ArrayList<HashMap<Integer,String>>();
      try {
          ResultSet executeQuery = con.executeQuery(SQLCOMMAND);
          while(executeQuery.next()){
              HashMap<Integer,String> map=new HashMap<Integer,String>();
              for(int i=0;i<colMetaData.length;i++){
                  map.put(i,executeQuery.getString(i+1)!=null?executeQuery.getString(i+1).replace("'", "''"):"");
                  
              }
              SQLDATA.add(map);
          }
          
          System.out.println(SQLDATA);
         
      } catch (SQLException ex) {
          Logger.getLogger(DataStreaming.class.getName()).log(Level.SEVERE, null, ex);
      }
       return SQLDATA;
  }
    public String [] getMetaDataCol(String table){
         String[] setDatabase = QueryCol(table);
        for(int i=0;i<setDatabase.length;i++){
            if(i+1==setDatabase.length){
                //System.out.print(setDatabase[i]);  
            }else{
               // System.out.print(setDatabase[i]+"|");  
            }
          
        }
        //System.out.println(); 
        return setDatabase;
    }
    private Scanner inputStream;
    private ArrayList<String> data_list;
     ArrayList<HashMap<Integer, String>> ListOfData=new ArrayList<HashMap<Integer, String>>();
    public ArrayList<HashMap<Integer, String>> setFileInputStream(String fileString) {
        try {
            boolean Startline=false;
            int line_amt = 0;
            int col_amt = 0;
            String line_data = "";
            inputStream = new Scanner(new FileInputStream(fileString),"Cp874");
            data_list = new ArrayList<String>();
            while (inputStream.hasNextLine()) {
               
                HashMap<Integer, String> map=new HashMap<Integer, String>();
            line_data = inputStream.nextLine();
                if(Startline){
                String[] split = line_data.split("[|]");
                for(int i=0;i<split.length;i++){
                      
                        
                            map.put(i, split[i]);
                       
                }
                ListOfData.add(map);
                data_list.add(line_data);  
                
                if (line_data.split("[|]").length > col_amt) {
                    col_amt = line_data.split("[|]").length;
                }
                }
                Startline=true;
                line_amt++;
            }

        
            inputStream.close();
            //inputStream = null;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataStreaming.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ListOfData;
    }
    
    public void setFileOutputStream(String OutputStreamTxT,ArrayList<HashMap<Integer, String>> list) {
           boolean status = true;
        PrintWriter outputStream = null;
        try{
            File f = new File(OutputStreamTxT);
            if(f.exists()){
                f.delete();
            }
            FileOutputStream fOut = new FileOutputStream(f);
            OutputStreamWriter osw = new OutputStreamWriter(fOut, "Cp874");
            outputStream = new PrintWriter(osw);
            System.out.println("Size of data : "+list.size());
            for ( int i = 0; i<list.size(); i++ ){
                
                for(int k=0;k<list.get(i).size();k++){
                    try {
                    //out.write(data[i].trim()+"\n");
                    
                    if(k+1==list.get(i).size()){
                        outputStream.print("" + list.get(i).get(k));
                    }else{
                        outputStream.print("" + list.get(i).get(k)+"|");
                    }
                    
                    
                } catch (Exception ex) {
                    System.out.println("Output file "+ex);
                }
                }
                outputStream.println();
            }
            
            outputStream.close();
        } catch (Exception ex){
            
        }
        
    }
    public void setFileOutputStream(String OutputStreamTxT,String data) {
        boolean status = true;
     PrintWriter outputStream = null;
     try{
         File f = new File(OutputStreamTxT);
         if(f.exists()){
             f.delete();
         }
         FileOutputStream fOut = new FileOutputStream(f);
         OutputStreamWriter osw = new OutputStreamWriter(fOut, "Cp874");
         outputStream = new PrintWriter(osw);
         
       
             
           
                 try {
                 //out.write(data[i].trim()+"\n");
                 
                
                     outputStream.print(data);
                
                 
                 
             } catch (Exception ex) {
                 System.out.println("Output file "+ex);
             }
             
             outputStream.println();
         
         
         outputStream.close();
     } catch (Exception ex){
         
     }
     
 }
   public String [] MetaDataCol(String [] ColArray){
         
        return ColArray;
    }
   public ArrayList<HashMap<Integer, String>>  Excel2003_2_List(String filePath){
       ArrayList<HashMap<Integer, String>> data=new ArrayList<HashMap<Integer, String>>();
       try{
    	   InputStream myxls2003 = new FileInputStream(filePath);
           HSSFWorkbook wb2003 = new HSSFWorkbook(myxls2003);
           HSSFSheet sheet2003 = wb2003.getSheetAt(0);       // first sheet
           System.out.println("File Path : "+filePath);
           for(int i=1;i<sheet2003.getPhysicalNumberOfRows();i++){
        	   HashMap<Integer, String> map=new HashMap<Integer, String>();
               HSSFRow row2003 = sheet2003.getRow(i);
               System.out.println("Excel Row : "+row2003.getPhysicalNumberOfCells());
               System.out.println("Excel Column 0: "+row2003.getCell((short)0));
               System.out.println("Excel Column 1: "+JDate.saveDate(""+row2003.getCell((short)1)).substring(0, 6));
               System.out.println("Excel Column 2: "+row2003.getCell((short)2));
               String t = row2003.getCell((short)3)==null ? "" :""+row2003.getCell((short)3);
               System.out.println("Excel Column 3: "+t);
               System.out.println("Excel Column 4: "+row2003.getCell((short)4));
               System.out.println("Excel Column 5: "+row2003.getCell((short)5));
               System.out.println("Excel Column 6: "+row2003.getCell((short)6));
               System.out.println("Excel Column 7: "+row2003.getCell((short)7));
               System.out.println("Excel Column 8: "+row2003.getCell((short)8));
               System.out.println("Excel Column 9: "+row2003.getCell((short)9));

               for(int k=0;k<row2003.getPhysicalNumberOfCells();k++){
            	   HSSFCell cell2003   = row2003.getCell((short)k);  
                   		if(k==1){
                                        	  if(cell2003==null){
                                        		  map.put(k, "000000");
                                        	  }else{
                                               String dateformated="";
                                                     String datePattern = "\\d{1,2}/\\d{1,2}/\\d{4}";
                                                     boolean    retval = cell2003.toString().matches(datePattern);
                                                     if(retval){
                                                         dateformated=cell2003.toString();
                                                        // System.out.print(cell2007+"|");    
                                                     }else{
                                                   try {  String str_date=cell2003.toString();
                                                           DateFormat formatter ; 
                                                           Date date ; 
                                                           formatter = new SimpleDateFormat("dd-MMM-yyyy");
                                                           date = (Date)formatter.parse(str_date); 
                                                           SimpleDateFormat sm=new SimpleDateFormat("dd/MM/yyyy");
                                                           dateformated=sm.format(date);
                                                    } catch (ParseException e){
                                                        
                                                    }
                                                     }
                                                     map.put(k, dateformated.toString());
                                        	  }
                                                      
                                                   //System.out.print(dateformated);    
                                        }else{
                                       //System.out.print(cell2007+"|"); 
                                            String databfconverter="";
                                            if(cell2003==null){
                                                databfconverter="''";
                                            }else{
                                                databfconverter=cell2003.toString();
                                            }
                                            map.put(k, databfconverter.toString());
                                        }
                                  
                                      }
                                      if(map.get(1).length()>0){
                                    	  String enddate="99/99/9999";
                                    	  if(map.get(3).length()<=0||map.get(3)==null){
                                    		  enddate="99/99/9999";
                                    	  }else{
                                    		  enddate=map.get(3); 
                                    	  }
                                    System.out.println(JDate.saveDate(map.get(1).substring(0, map.get(1).length()-2))+"|"+JDate.saveDate(enddate.substring(0,enddate.length()-2)));
                                      if(Integer.parseInt(JDate.saveDate(map.get(1).substring(0, map.get(1).length()-2)))<Integer.parseInt(JDate.saveDate(enddate.substring(0, enddate.length()-2)))){
                                  	    data.add(map);  
                                    }}
                   }
                   
       } catch (IOException ex) {
           Logger.getLogger( DataStreaming.class.getName()).log(Level.SEVERE, null, ex);
       }
       return data;
   }
   
   public ArrayList<HashMap<Integer, String>>  Excel2007_2_List(String filePath){
       ArrayList<HashMap<Integer, String>> data=new ArrayList<HashMap<Integer, String>>();
       try {
           
                     XSSFWorkbook wb2007     = new XSSFWorkbook(filePath);
                     XSSFSheet sheet2007 = wb2007.getSheetAt(0);       // first sheet
                     
                   
                   for(int i=1;i<sheet2007.getPhysicalNumberOfRows();i++){
                       HashMap<Integer, String> map=new HashMap<Integer, String>();
                                       XSSFRow row2007     = sheet2007.getRow(i);        // third row
                                      for(int k=0;k<row2007.getPhysicalNumberOfCells();k++){
                                       XSSFCell cell2007   = row2007.getCell(k);  // fourth cell
                                       System.out.println(k+">"+cell2007);
                                        if(k==1){
                                               String dateformated="";
                                                     String datePattern = "\\d{1,2}/\\d{1,2}/\\d{4}";
                                                     boolean    retval = cell2007.toString().matches(datePattern);
                                                     if(retval){
                                                         dateformated=cell2007.toString();
                                                        // System.out.print(cell2007+"|");    
                                                     }else{
                                                   try {  String str_date=cell2007.toString();
                                                   System.out.println(str_date);
                                                           DateFormat formatter ; 
                                                           Date date ; 
                                                           String tempdate[]=str_date.split("-");
                                                           formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
                                                          
                                                           SimpleDateFormat sm=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
                                                           dateformated=sm.format(formatter.parse(str_date));
                                                    } catch (ParseException e){
                                                        
                                                    }
                                                     }

                                                       map.put(k, dateformated.toString());
                                                   //System.out.print(dateformated);    
                                        }else if(i==3){
                                        //System.out.print(cell2007+"|"); 
                                            String databfconverter="";
                                            if(cell2007==null){
                                                databfconverter="''";
                                            }else{
                                               // String dateformated="";
                                                String datePattern = "\\d{1,2}/\\d{1,2}/\\d{4}";
                                                boolean    retval = cell2007.toString().matches(datePattern);
                                                if(retval){
                                                	databfconverter=cell2007.toString();
                                                   // System.out.print(cell2007+"|");    
                                                }else{
                                              try {  String str_date=cell2007.toString();
                                              		  System.out.println(str_date);
                                                      DateFormat formatter ; 
                                                      Date date ; 
                                                      String tempdate[]=str_date.split("-");
                                                      formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
                                                     
                                                      SimpleDateFormat sm=new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
                                                      databfconverter=sm.format(formatter.parse(String.valueOf(InterfaceData.intToString(Integer.parseInt(tempdate[0]), 2))+"-"+tempdate[1]+"-"+tempdate[2]));

                                              } catch (ParseException e){
                                                   
                                               }
                                                }
                                            }
                                            map.put(k, databfconverter.toString());
                                         
                                        }else{
                                       	  String databfconverter = "";
                                       	  if(cell2007==null){
                                                  databfconverter = "''";
                                             }else{
                                           	  databfconverter=cell2007.toString();
                                             } 
                                       	  map.put(k, databfconverter.toString());
                                        }
                                   
                                      }
                                      
                                      
                                      if(Integer.parseInt(JDate.saveDate(map.get(1).substring(0, map.get(1).length()-2)))<Integer.parseInt(JDate.saveDate(map.get(3).substring(0, map.get(3).length()-2)))){
                                    	    data.add(map);  
                                      }
                                      
                              // System.out.println();
                                   
                                      
                   }
                   
       } catch (IOException ex) {
           Logger.getLogger(DataStreaming.class.getName()).log(Level.SEVERE, null, ex);
       }
       return data;
   }
}
