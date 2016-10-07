package df.bean.interfacefile;




	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.ArrayList;
	import java.util.HashMap;


public class InterfaceSQL2SQLBackupHoliday extends  InterfaceData{
		 InterfaceData data;
		String hospitalcode="";
		public void SetBackupHoliday(String hospitalcode,String source,String destination,String tag,String con){
			this.hospitalcode=hospitalcode;
			 data=new InterfaceData();
			 String code=" SELECT *,'TAG' AS TAG FROM  "+source + " WHERE  HOSPITAL_CODE='"+this.hospitalcode+"' "+con+" ";
			 data.InfaceProcessingDataString2SQL(source,(code));
			 System.out.println(code);
			 //data.setDefaultDataCell("NOR", MutiCell)
			 data.CalExp(108, "\""+tag+"\"");	 
			 printArraylist(data.doProcessing);
			 SQLCommit(destination, data.doProcessing);
			 System.out.println("Backup Complete");
			 
		}
		
		public void GetBackupHoliday(String hospitalcode,String source,String destination,String tag){
			this.hospitalcode=hospitalcode;
			 data=new InterfaceData();
			 String code=" SELECT * FROM  "+source + " WHERE  HOSPITAL_CODE='"+this.hospitalcode+"' AND TAG='"+tag+"'";
			 data.InfaceProcessingDataString2SQL(source,(code));
			 data.removeColData(108);
			 printArraylist(data.doProcessing);
			 SQLCommit(destination, data.doProcessing);
			 System.out.println("Backup Complete");
			 
		}
		
//		public static void main(String [] args){
//			new InterfaceSQL2SQLBackupDischange().SetBackupDischange("00009","TRN_DAILY","HIS_TRN_DAILY","DISCHARGE","");
//			//new InterfaceSQL2SQLBackupDischange().GetBackupDischange("00009","HIS_TRN_DAILY","TRN_DAILY","DISCHARGE");
//		}

		@Override
		public void SQLCommit(String Database,ArrayList<HashMap<Integer, String>> list) {
							for(int i=0;i<list.size();i++){
		                        if (this.Connection != null) {
		                        	
		                            try {
		                              
		                                String sqlcom="";
		                                if(list.get(0).size()<=108){
		                                	sqlcom="SELECT COUNT(*) FROM "+Database +" WHERE HOSPITAL_CODE='"+this.hospitalcode+"' " +
													"AND INVOICE_NO='"+list.get(i).get(1)+"'  AND INVOICE_DATE='"+list.get(i).get(2)+"'   " +
													"AND TRANSACTION_DATE='"+list.get(i).get(5)+"' AND LINE_NO='"+list.get(i).get(18)+"' " +
													" AND INVOICE_TYPE='"+list.get(i).get(80)+"'";
		                                }else{
		                                	sqlcom="SELECT COUNT(*) FROM "+Database +" WHERE HOSPITAL_CODE='"+this.hospitalcode+"' " +
													"AND INVOICE_NO='"+list.get(i).get(1)+"'  AND INVOICE_DATE='"+list.get(i).get(2)+"'   " +
													"AND TRANSACTION_DATE='"+list.get(i).get(5)+"' AND LINE_NO='"+list.get(i).get(18)+"' " +
													" AND INVOICE_TYPE='"+list.get(i).get(80)+"' AND  TAG='"+ list.get(i).get(list.get(i).size())+"'";
		                                }
		                                
		                                
		                                ResultSet ret = this.Connection.executeQuery(	sqlcom  );
		                                ret.next();
		                                if(ret.getInt(1)>0){
		                                 
		                                        String SQLINSERT="";
		                                          for(int k=0;k<list.get(i).size();k++){
		                                              if(list.get(i).get(k)==null){
		                                                  list.get(i).put(k,"''");
		                                              }
		                                              
		                                              SQLINSERT+=" "+data.metaDataCol[k]+"=";
		                                              if((k+1)==list.get(i).size()){
		                                                  if(list.get(i).get(k).equals("''")){
		                                                      SQLINSERT+=""+list.get(i).get(k)+"";
		                                                  }else{
		                                                      SQLINSERT+=" '"+list.get(i).get(k)+"'";
		                                                  }
		                                              }else{
		                                                  if(list.get(i).get(k).equals("''")){
		                                                      SQLINSERT+=" "+list.get(i).get(k)+",";
		                                                  }else{
		                                                      SQLINSERT+=" '"+list.get(i).get(k)+"',";
		                                                  }
		                                              }
		                                          }
		                                          
		      	                                String sqlcom1="";
		    	                                if(list.get(0).size()<=108){
//		    	                                	 sqlcom1="UPDATE "+Database+" SET "+SQLINSERT+ "  WHERE HOSPITAL_CODE='"+this.hospitalcode+"' " +
//	                                    		  			"AND INVOICE_NO='"+list.get(i).get(1)+"'  AND INVOICE_DATE='"+list.get(i).get(2)+"'   " +
//	                                    		  			"AND TRANSACTION_DATE='"+list.get(i).get(5)+"' AND LINE_NO='"+list.get(i).get(18)+"' " +
//	                                    		  			" AND INVOICE_TYPE='"+list.get(i).get(80)+"'";
		    	                                	
		    	                                }else{
//		    	                                	 sqlcom1="UPDATE "+Database+" SET "+SQLINSERT+ "  WHERE HOSPITAL_CODE='"+this.hospitalcode+"' " +
//	                                    		  			"AND INVOICE_NO='"+list.get(i).get(1)+"'  AND INVOICE_DATE='"+list.get(i).get(2)+"'   " +
//	                                    		  			"AND TRANSACTION_DATE='"+list.get(i).get(5)+"' AND LINE_NO='"+list.get(i).get(18)+"' " +
//	                                    		  			" AND INVOICE_TYPE='"+list.get(i).get(80)+"' AND  TAG='"+list.get(i).get(list.get(i).size())+"'";
		    	                                	
		    	                                }
		                                          
		                                                  System.out.println(sqlcom1);
		                                                  executeUpdate(sqlcom1);
		                                              
		                                    
		                                }else{
		                                	HashMap<Integer, String> map=new HashMap<Integer, String>();
		                                	map=list.get(i);
		                                	ArrayList<HashMap<Integer, String>> list1=new ArrayList<HashMap<Integer, String>>();
		                                	list1.add(map);
		                                	super.SQLCommit(Database,list1);
		                                }
		                            } catch (SQLException e) {
		                               
		                            }
		                        }
							}
			
		}
		
	}
	 

