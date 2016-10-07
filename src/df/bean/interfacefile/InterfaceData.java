/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package df.bean.interfacefile;


import df.bean.db.DataStreaming;
import df.bean.db.conn.DBConnection;
import df.bean.obj.util.JDate;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author TomMy
 */
public class InterfaceData {
    public DBConnection Connection;
    public DataStreaming dataStreaming; 
    public boolean isComplete = false;
    public String[] ColheaderData;
    public String[] metaDataCol;
    public ArrayList<HashMap<String,String>> ArrayColheader = new ArrayList<HashMap<String,String>>();
    public ArrayList<HashMap<Integer, String>> doProcessing;
    public ArrayList<HashMap<String, String>> SQLRESULT = new ArrayList<HashMap<String, String>>();
    
    public DBConnection setConnection() {
        DBConnection con1 =new DBConnection();
        con1.connectToLocal();
        return con1;
    }
    public InterfaceData() {
    	System.out.println("Connect Super");
    	this.Connection =setConnection();
        this.dataStreaming = new DataStreaming(this.Connection);
    }

    public ArrayList<HashMap<String,String>> setColHeader(String colName,Integer Index) {
    	HashMap<String,String>  map =   new HashMap<String,String>();
        map.put("Index",String.valueOf(Index));
        map.put("colName",colName );
        ArrayColheader.add(map);
        return ArrayColheader;
    }
    
    public void Disconnection(DBConnection con) {
    	con.Close();
    }
    
    public boolean isComplete(){
    	boolean StatusCompelete=false;
        if(UnComplete==0){
        	StatusCompelete=true;
        }else{
        	StatusCompelete=false;
        }
        return StatusCompelete;
    }

    public void setDefaultDataCell(int SetOnCell,String MutiCell){
                        int DataCount=0;
                        ArrayList<HashMap<Integer, String>> Text2Edit=new ArrayList<HashMap<Integer, String>>();
                        if(MutiCell.startsWith("SELECT")||MutiCell.startsWith("select")){
                        }else if(MutiCell.startsWith("[")){
                        String str = MutiCell;
                        if(str.length()>=2){
                                String substring = str.substring(1,str.length()-1);   
                                String[] split = substring.split(" ");
                                DataCount=split.length;
                                 for(int p=0;p<doProcessing.size();p++){
                                     HashMap<Integer,String> hashMap=new HashMap<Integer, String>(); 
                                    for(int k=0;k<split.length;k++){
                                           if(split[k].startsWith("str(")&&split.length>=2){
                                               String StringExp=split[k].substring(4,split[k].length()-1);
                                               hashMap.put(k-1, doProcessing.get(p).get(Integer.parseInt(split[k-1])));
                                               hashMap.put(k,StringExp);
                                               hashMap.put(k+1, doProcessing.get(p).get(Integer.parseInt(split[k+1])));
                                           }else if(split[k].startsWith("opr(")&&split.length>=2){
                                                String StringOpr=split[k].substring(4,split[k].length()-1);
                                                String var1 = doProcessing.get(p).get(Integer.parseInt(split[k-1]));
                                                String var2 = doProcessing.get(p).get(Integer.parseInt(split[k+1]));
                                                String opr=var1+StringOpr+var2;
                                                    ScriptEngineManager manager = new ScriptEngineManager();
                                                    ScriptEngine engine = manager.getEngineByName("js");        
                                                    try {
                                                        Object result = engine.eval(opr);
                                                        hashMap.put(k-1, "");
                                                        hashMap.put(k, result.toString());
                                                        hashMap.put(k+1, "");
                                                    } catch (ScriptException ex) {
                                                        Logger.getLogger(InterfaceData.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                           }else if(split.length==1&&split[k].startsWith("str(")){
                                                String StringExp=split[k].substring(4,split[k].length()-1);
                                                hashMap.put(k-1, "");
                                                hashMap.put(k, StringExp.toString());
                                                hashMap.put(k+1, "");
                                           }else if(split.length==1){
                                        	   System.out.println(doProcessing.get(p).get(Integer.parseInt(split[k])));
                                        	   String var1 = doProcessing.get(p).get(Integer.parseInt(split[k]));
                                               hashMap.put(k-1, "");
                                               hashMap.put(k, var1.toString());
                                               hashMap.put(k+1, "");
                                        	
                                           }
                                  }
                                    Text2Edit.add(hashMap);
                                }                  
                        }
                        }
                        for(int i=0;i<doProcessing.size();i++){
                            String Combind="";
                            for(int k=0;k<DataCount;k++){
                                Combind+=Text2Edit.get(i).get(k);
                            }
                            doProcessing.get(i).put(SetOnCell, Combind);
                        }
    } 
        
        public void setDefaultDataCell(String SetOnCell,String MutiCell){
                        int DataCount=0;
                        ArrayList<HashMap<Integer, String>> Text2Edit=new ArrayList<HashMap<Integer, String>>();
                        if(MutiCell.startsWith("SELECT")||MutiCell.startsWith("select")){
                        }else if(MutiCell.startsWith("[")){
                        String str = MutiCell;
                        if(str.length()>=1){
                                String substring = str.substring(1,str.length()-1);   
                                String[] split = substring.split(" ");
                                DataCount=split.length;
                                 for(int p=0;p<doProcessing.size();p++){
                                     HashMap<Integer,String> hashMap=new HashMap<Integer, String>(); 
                                    for(int k=0;k<split.length;k++){
                                           if(split[k].startsWith("str(")&&split.length>=2){
                                               String StringExp=split[k].substring(4,split[k].length()-1);
                                               hashMap.put(k-1, doProcessing.get(p).get(Integer.parseInt(split[k-1])));
                                               hashMap.put(k,StringExp);
                                               hashMap.put(k+1, doProcessing.get(p).get(Integer.parseInt(split[k+1])));
                                           }else if(split[k].startsWith("opr(")&&split.length>=2){
                                                String StringOpr=split[k].substring(4,split[k].length()-1);
                                                String var1 = doProcessing.get(p).get(Integer.parseInt(split[k-1]));
                                                String var2 = doProcessing.get(p).get(Integer.parseInt(split[k+1]));
                                                String opr=var1+StringOpr+var2;
                                                    ScriptEngineManager manager = new ScriptEngineManager();
                                                    ScriptEngine engine = manager.getEngineByName("js");        
                                                    try {
                                                        Object result = engine.eval(opr);
                                                        hashMap.put(k-1, "");
                                                        hashMap.put(k, result.toString());
                                                        hashMap.put(k+1, "");
                                                    } catch (ScriptException ex) {
                                                        Logger.getLogger(InterfaceData.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                           }else if(split.length==1&&split[k].startsWith("str(")){
                                                String StringExp=split[k].substring(4,split[k].length()-1);
                                                hashMap.put(k-1, "");
                                                hashMap.put(k, StringExp.toString());
                                                hashMap.put(k+1, "");
                                           }else if(split.length==1){
                                        	   System.out.println(doProcessing.get(p).get(Integer.parseInt(split[k])));
                                        	   String var1 = doProcessing.get(p).get(Integer.parseInt(split[k]));
                                               hashMap.put(k-1, "");
                                               hashMap.put(k, var1.toString());
                                               hashMap.put(k+1, "");
                                        	
                                           }
                                  }
                                    Text2Edit.add(hashMap);
                                }                  
                        }
                        }
                        for(int i=0;i<doProcessing.size();i++){
                            String Combind="";
                            for(int k=0;k<DataCount;k++){
                                Combind+=Text2Edit.get(i).get(k);
                            }
                                 for(int p=0;p<this.metaDataCol.length;p++){
                                    if(this.metaDataCol[p].equals(SetOnCell)){
                                       this.doProcessing.get(i).put(p, Combind.toString()); 
                                    }
                                }
                          
                        }
  
    }
    public boolean isOperations(String Op){
                        boolean opCheck=false;
                        if(Op.equals("+")||Op.equals("-")||Op.equals("*")||Op.equals("~")){
                            opCheck=true;
                        }else{
                            opCheck=false;
                        }
        return opCheck;
    }
    public void SQLCommit(String Database,ArrayList<HashMap<Integer, String>> list){
      

                        for(int i=0;i<list.size();i++){
                            String SQLINSERT="";
                              for(int k=0;k<list.get(i).size();k++){
                                  if(list.get(i).get(k)==null){
                                      list.get(i).put(k,"''");
                                  }
                                  
                                  
                                  if((k+1)==list.get(i).size()){
                                      if(list.get(i).get(k).equals("''")){
                                          SQLINSERT+=""+list.get(i).get(k)+"";
                                      }else{
                                          SQLINSERT+="'"+list.get(i).get(k)+"'";
                                      }
                                  }else{
                                      if(list.get(i).get(k).equals("''")){
                                          SQLINSERT+=""+list.get(i).get(k)+",";
                                      }else{
                                          SQLINSERT+="'"+list.get(i).get(k)+"',";
                                      }
                                  }
                              }
                                  String SQLCOMMAND="INSERT INTO "+Database+" VALUES ("+SQLINSERT+")";
                                 // System.out.println(SQLCOMMAND);
                                  executeUpdate(SQLCOMMAND);
                                  
                        }
                         for(int k=0;k<this.SQLRESULT.size();k++){
                            // System.out.println(SQLRESULT.get(k));
                         }
                        Disconnection(this.Connection);
           
    }
    int Complete=0;
    int UnComplete=0;
    
    public int executeUpdate(String sqlCommand) {
    	//System.out.println(sqlCommand);
                        int ret = 0;
                        HashMap<String,String> hashMap=new HashMap<String, String>(); 
                        if (this.Connection != null) {
                         

                                ret = this.Connection.executeUpdate(sqlCommand);
                                System.out.println(ret);
                                if(ret==1){
                                	Complete=Complete+1;
                                }else{
                                	UnComplete=UnComplete+1;
                                	hashMap.put("Command", sqlCommand);
                                	hashMap.put("Message", "Violation of PRIMARY KEY constraint");
                                	SQLRESULT.add(hashMap);
                                }
                        }
            return ret;
    }
    public void printArraylist(ArrayList<HashMap<Integer, String>> list){
                       for(int i=0;i<list.size();i++){
                    	    System.out.println(list.get(i));
                       }
                           
                        
    }
    public void setFileOutputStream(String filePath,ArrayList<HashMap<Integer, String>> list){
    	System.out.println(filePath);
        this.dataStreaming.setFileOutputStream(filePath, list);
    }
    
    public void setFileOutputStream(String filePath,String data){
    	System.out.println(filePath);
        dataStreaming.setFileOutputStream(filePath, data);
    }
    
    
    public  void CalExp(int celindex,String exp){
                        try {
                            ScriptEngineManager manager = new ScriptEngineManager();
                            ScriptEngine engine = manager.getEngineByName("js");
                            String expString=exp;
                            for(int i=0;i<this.doProcessing.size();i++){
                                for(int k=0;k<this.doProcessing.get(i).size();k++){
                                      if(this.doProcessing.get(i).get(k)!=(null)){
                                           expString= expString.replace("["+k+"]",this.doProcessing.get(i).get(k)); 
                                      }
                                 
                                   
                                }
                               System.out.println(expString);
                                Object result = engine.eval(expString);
                                this.doProcessing.get(i).put(celindex, result.toString());
                            }
                        } catch (ScriptException ex) {
                            Logger.getLogger(InterfaceData.class.getName()).log(Level.SEVERE, null, ex);
                        }
    }
        public  void CalExp(String celindex,String exp){
                        try {
                            ScriptEngineManager manager = new ScriptEngineManager();
                            ScriptEngine engine = manager.getEngineByName("js");
                            String expString=exp;
                            for(int i=0;i<this.doProcessing.size();i++){
                                for(int k=0;k<this.doProcessing.get(i).size();k++){
                                      if(this.doProcessing.get(i).get(k)!=(null)){
                                           expString= expString.replace("["+k+"]",this.doProcessing.get(i).get(k)); 
                                      }
                                 
                                   
                                }
                            //   System.out.println(expString);
                                Object result = engine.eval(expString);
                               
                                for(int p=0;p<this.metaDataCol.length;p++){
                                    if(this.metaDataCol[p].equals(celindex)){
                                       this.doProcessing.get(i).put(p, result.toString()); 
                                    }
                                }
                                
                            }
                        } catch (ScriptException ex) {
                            Logger.getLogger(InterfaceData.class.getName()).log(Level.SEVERE, null, ex);
                        }
    }  
        
        public  void CalExpByNumber(Integer celindex,String exp){
//            try {
//                ScriptEngineManager manager = new ScriptEngineManager();
//                ScriptEngine engine = manager.getEngineByName("js");
        	String expString=exp;
                for(int i=0;i<this.doProcessing.size();i++){
                	if(expString.length()>=3){
                	
                	 expString= exp.replace(expString,this.doProcessing.get(i).get(expString.substring(1, expString.length()-1))); 
                			this.doProcessing.get(i).put(celindex, expString.toString()); 
                	}
                	

                       
                   // }
                //   System.out.println(expString);
                   
                   
                   // for(int p=0;p<this.metaDataCol.length;p++){
                      //  if(this.metaDataCol[p].equals(celindex)){
                           
                      //  }
                    //}
                    
                }
//            } catch (ScriptException ex) {
//                Logger.getLogger(InterfaceData.class.getName()).log(Level.SEVERE, null, ex);
//            }
}
     public  void InfaceProcessingDataText2SQL(String file,String Database){
			            metaDataCol = this.dataStreaming.getMetaDataCol(Database);
			            ArrayList<HashMap<Integer, String>> FileInputStream = this.dataStreaming.setFileInputStream(file);
			            dataStreaming.ArrayColheader=this.ArrayColheader;
			            this.doProcessing = doProcessing(metaDataCol,FileInputStream, this.dataStreaming);
}   
     public  void InfaceProcessingDataExcel2SQL(ArrayList<HashMap<Integer, String>> list,String Database){
         metaDataCol = this.dataStreaming.getMetaDataCol(Database);
         ArrayList<HashMap<Integer, String>> FileInputStream = list;
         dataStreaming.ArrayColheader=this.ArrayColheader;
         this.doProcessing = doProcessing(metaDataCol,FileInputStream, this.dataStreaming);
}   
     
    public void InfaceProcessingDataSQL2SQL(String source) {
                        metaDataCol = this.dataStreaming.getMetaDataCol(source);
                        ArrayList<HashMap<Integer, String>> dataFromTable = dataStreaming.getDataFromTable(metaDataCol, source);
                        dataStreaming.ArrayColheader=this.ArrayColheader;
                        this.doProcessing = doProcessing(metaDataCol,dataFromTable, this.dataStreaming);
    }

    void InfaceProcessingDataSQL2Text(String source) {
                        metaDataCol = this.dataStreaming.getMetaDataCol(source);
                        ArrayList<HashMap<Integer, String>> dataFromTable = dataStreaming.getDataFromTable(metaDataCol, source);
                        dataStreaming.ArrayColheader=this.ArrayColheader;
                        this.doProcessing = doProcessing(metaDataCol,dataFromTable, this.dataStreaming);
    }
    
    
    public int CustomizeHeader=1;
    public int CustomizeHeaderExpression=2;
    void InfaceProcessingDataSQL2Text(String [] header,int mode,String source,String SQLCOMMAND) {
    	if(mode==CustomizeHeader){
            metaDataCol = header;
            ArrayList<HashMap<Integer, String>> dataFromTable = dataStreaming.getDataFromTable(metaDataCol, source);
            dataStreaming.ArrayColheader=this.ArrayColheader;
            this.doProcessing = doProcessing(metaDataCol,dataFromTable, this.dataStreaming);
    	}

    }
      void InfaceProcessingDataSQL2Text(String [] header,int mode,String Command) {
    	if(mode==CustomizeHeaderExpression){
    		 metaDataCol = header;
    		 dataStreaming.ArrayColheader=this.ArrayColheader;
                 ArrayList<HashMap<Integer, String>> dataFromTable = dataStreaming.getDataFromSQL(metaDataCol, Command
);
                 this.doProcessing =doProcessingWithHeader(metaDataCol,dataFromTable, this.dataStreaming);
                
                // printArraylist(doProcessing);
    	}

    }
    
    
    public ArrayList<HashMap<Integer, String>> AddHeader(ArrayList<HashMap<Integer, String>> list,String source){
                        String[] metaDataCol = this.dataStreaming.getMetaDataCol(source);
                        HashMap<Integer,String> hashMap=new HashMap<Integer, String>(); 
                        for(int i=0;i<metaDataCol.length;i++){
                            hashMap.put(i, metaDataCol[i].toString());
                        }
                        list.add(0,hashMap);
        return list;
    }
        public ArrayList<HashMap<Integer, String>> AddHeader(ArrayList<HashMap<Integer, String>> list,String [] Header){
                        metaDataCol = Header;
                        HashMap<Integer,String> hashMap=new HashMap<Integer, String>(); 
                        for(int i=0;i<metaDataCol.length;i++){
                            hashMap.put(i, metaDataCol[i].toString());
                        }
                        list.add(0,hashMap);
        return list;
    }
    public void MetaDataCol(String [] ColHeader){
                        this.ColheaderData=ColHeader;
     
    }
    
    void InfaceProcessingDataText2Text(String source) {
                        metaDataCol = this.ColheaderData;
                        ArrayList<HashMap<Integer, String>> dataFromTable = dataStreaming.setFileInputStream(source);
                        dataStreaming.ArrayColheader=this.ArrayColheader;
                        this.doProcessing =doProcessing(metaDataCol,dataFromTable, this.dataStreaming);
                        
    }
    public void InfaceProcessingDataString2SQL(String source,String command) {
    	
        metaDataCol = this.dataStreaming.getMetaDataCol(source);
        ArrayList<HashMap<Integer, String>> dataFromTable = dataStreaming.getDataFromSQL(metaDataCol, command);
        dataStreaming.ArrayColheader=this.ArrayColheader;
        this.doProcessing = dataFromTable;
    	
    	
    	
//    	
//        metaDataCol = this.databaseStreaming.getMetaDataCol(source);
//        System.out.println( command.getCOMMAND());
//        ArrayList<HashMap<Integer, String>> dataFromTable = databaseStreaming.getDataFromSQL(metaDataCol, command.getCOMMAND());
//        		//databaseStreaming.getDataFromTable(metaDataCol, source);
//        databaseStreaming.ArrayColheader=this.ArrayColheader;
//        this.doProcessing = this.processing.doProcessing(metaDataCol,dataFromTable, this.databaseStreaming);
        
        
//        
//        ArrayList<HashMap<Integer, String>> dataFromTable = fileSteaming.setFileInputStream(source);
//        databaseStreaming.ArrayColheader=this.ArrayColheader;
//        this.doProcessing = this.processing.doProcessing(metaDataCol,dataFromTable, this.databaseStreaming);
        
    }

       
		public int getSizeErrorArraylist() {
			return UnComplete;
		}
		public int getSizeCompleteArraylist() {
			return Complete;
		}

    void addColHeader(String tesT3, int slot) {
            for(int i=0;i<doProcessing.size();i++){
                doProcessing.get(i).put(slot,"''");
            }
    }
    public ArrayList<HashMap<Integer,String>> getDataFromTable(String database){
    	String []col=dataStreaming.QueryCol(database);
    	
    	
		return dataStreaming.getDataFromTable(col, database);
    	
    }
    public static String intToString(int num, int digits) {
        assert digits > 0 : "Invalid number of digits";

        // create variable length array of zeros
        char[] zeros = new char[digits];
        Arrays.fill(zeros, '0');
        // format number as String
        DecimalFormat df = new DecimalFormat(String.valueOf(zeros));
       
        return df.format(num);
    }

   public void setDefaultDate(int key){
	     for(int i=0;i<this.doProcessing.size();i++){
	    	 
	    	 
	    	 this.doProcessing.get(i).put(key, JDate.saveDate(this.doProcessing.get(i).get(key)));
	    	 
	    	 
	    	 
	     	}
   }
   public static String getFileExtension(String filePath)
{
   File f = new File(filePath);
   String name = f.getName();
   int k = name.lastIndexOf(".");
   String ext = null;
   if(k != -1)
       ext = name.substring(k + 1, name.length());
   return ext;
}
   public void removeColData(int col){
	  for(int i=0;i<this.doProcessing.size();i++){
		  this.doProcessing.get(i).remove(col);
	  }
   }
   
   
   public ArrayList<HashMap<Integer, String>> doProcessing(String[] metaDataCol, ArrayList<HashMap<Integer, String>> FileInputStream, DataStreaming dataStreaming) {
       ArrayList<HashMap<Integer, String>> ListOfData=new ArrayList<HashMap<Integer, String>>();
             for(int k=0;k<FileInputStream.size();k++){ 
                   HashMap<Integer,String> hashMap=new HashMap<Integer, String>();
                   for(int v=0;v<metaDataCol.length;v++){
                   hashMap.put(v, "''");
                   }  
                   ListOfData.add(hashMap);
             }
       
          for(int k=0;k<FileInputStream.size();k++){ 
              HashMap<Integer,String> hashMap=new HashMap<Integer, String>();
              
              for(int i=0;i<dataStreaming.ArrayColheader.size();i++){ 
                  for(int v=0;v<metaDataCol.length;v++){
                      if(dataStreaming.ArrayColheader.get(i).get("colName").equals(metaDataCol[v])){
                           String get = dataStreaming.ArrayColheader.get(i).get("Index");
                          // hashMap.put(i, FileInputStream.get(k).get(Integer.parseInt(get)));
                           ListOfData.get(k).put(v, FileInputStream.get(k).get(Integer.parseInt(get)));
                      }else{
                          
                      }
                  }
             }
         }
           return ListOfData;
   }

  public ArrayList<HashMap<Integer, String>> doProcessingWithHeader(String[] metaDataCol, ArrayList<HashMap<Integer, String>> FileInputStream, DataStreaming databaseStreaming) {
       ArrayList<HashMap<Integer, String>> ListOfData=new ArrayList<HashMap<Integer, String>>();
             for(int k=0;k<FileInputStream.size();k++){ 
                   HashMap<Integer,String> hashMap=new HashMap<Integer, String>();
                   for(int v=0;v<metaDataCol.length;v++){
                   hashMap.put(v, "''");
                   }  
                   ListOfData.add(hashMap);
             }
            
          for(int k=0;k<FileInputStream.size();k++){ 
             // HashMap<Integer,String> hashMap=new HashMap<Integer, String>();
              
              for(int v=0;v<metaDataCol.length;v++){
                  ListOfData.get(k).put(v, FileInputStream.get(k).get(v));
              }
         }
           return ListOfData;
   }
}
