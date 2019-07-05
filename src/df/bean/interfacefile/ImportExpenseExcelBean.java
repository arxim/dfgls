package df.bean.interfacefile;
import java.io.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.*;

import df.bean.db.conn.DBConnection;
import java.sql.SQLException;
import df.bean.db.conn.DBConn;
import df.bean.process.ProcessUtil;
import df.bean.db.table.Batch;
import df.bean.db.table.TRN_Error;
import df.bean.obj.util.JDate;

public class ImportExpenseExcelBean extends InterfaceTextFileBean
{
	private String hospital_code;
	DBConn cdb;
	DBConnection cc = new DBConnection();
	String result = "";
	String user_name=this.getUserName();
	String batch_year="", batch_month="";
	//private String processName="Import Excel Expense";
	private String remarkMessage="", sqlMessage="";
	
	public void setHospital(String s){
    	this.hospital_code = s;
    }

    @Override
    public boolean insertData(String fn, DBConnection da) {
		System.out.println("Start Import Excel Expense");
		String[][] ExpArr = null;
    	int num=0;
    	boolean statusSave=true,status=true,statusShow=true;
		String processName="Import Excel Expense";
		TRN_Error.setUser_name(this.getUserName());
		TRN_Error.setHospital_code(this.hospital_code);
		
		System.out.println("java username="+this.getUserName());
		System.out.println("java hospital_code="+this.hospital_code);
    	try {
    		cc.connectToLocal();
    		//cdb = new DBConn(da.getConnection());
    		cdb = new DBConn();
            if (cdb.getStatement() == null) {
                cdb.setStatement();
            }
        } catch (SQLException ex) {
            this.result = ""+ex;
            this.setMessage(this.result);
            System.out.println(ex);
        }
    	try
    	{
	    	FileInputStream fileName=new FileInputStream(fn);
	    	// เปิด excel file
	    	POIFSFileSystem fs = new POIFSFileSystem(fileName);
	    	// เปิด workbook
	    	HSSFWorkbook workbook = new HSSFWorkbook(fs);
	    	// ดึง sheet ที่ต้องการออกมา
	    	HSSFSheet sheet = workbook.getSheetAt(0);
	    	HSSFRow tempRow; HSSFCell tempCell;
	    	int numRow=0, numCell=0;
	    	// get row
	    	numRow=sheet.getLastRowNum();
	    	if(statusShow){System.out.println("numRow="+numRow);}
	    	tempRow = sheet.getRow( 1 );
	    	numCell=tempRow.getLastCellNum();
        	if(statusShow){System.out.println("numCell="+numCell);}
        	ExpArr=new String[numRow][numCell];
        	int numNoSave=0;
    		int numSave=0;
    		String expenseAcc="";
    		
	    if(numCell ==10)	
        {
	    	for(int r=1;r<=numRow;r++)
	    	{
	    		//if(statusShow){System.out.println("r="+r);}
	    		
	    		// get row
	    		tempRow = sheet.getRow( r );
	    		//count number of Cell
	    		int rr=r-1;
	    		String YYYY="", MM="", employeeID="", doctorCode="", lineNo="", expenseCode="", note="", departmentCode="", locationCode="",
	        	expenseAccountCode="", supplierCode="", docNo="", docDate="", taxTypeCode="";
	        	double expenseSign=0, amount=0, taxAmount=0;
	        	ProcessUtil proUtil = new ProcessUtil();
				Batch b = new Batch(this.hospital_code, da);
				batch_year = b.getYyyy();
				batch_month = b.getMm();
				boolean statusDoctor=false, statusExpense=false, statusDepartment=false, statusDepToLoc=false, statusLocation=false;
				boolean statusAmount=false, statusTaxAmount=false;
				boolean statusPrice=false, statusTax=false, statusDep=false, statusLoc=false;
		    	doctorCode=String.valueOf(tempRow.getCell((short) 0));
		    	if(statusShow){System.out.println(" doctorCode="+doctorCode); }
		    	if(!doctorCode.equals(""))
		    	{
			    	String[][] DoctorArr = null;
			    	String sql_doctor="SELECT * FROM DOCTOR WHERE HOSPITAL_CODE='"+this.hospital_code+"' AND CODE='"+doctorCode+"'";
			    	if(statusShow){System.out.println(" doctorCode="+doctorCode+sql_doctor); }
			    	try
			        {   
			    		DoctorArr = cdb.query(sql_doctor);
			    		if(statusShow){System.out.println("DoctorArr="+DoctorArr.length);}
			    		if(DoctorArr.length == 0)
			    		{  
			    			if(statusShow){System.out.println("ไม่พบ Doctor Code="+doctorCode+" นี้ๆๆๆๆๆๆๆๆๆ");}
			    			setMessage("Row No.="+r+" Doctor Code="+doctorCode+" notfound");
			    			if(statusShow){System.out.println("setMessage="+this.getMessage());}
			        				
				            TRN_Error.writeErrorLog(cc.getConnection(), processName, this.getMessage(), "", sql_doctor,"");
				            //statusSave=false;
				            statusDoctor=false;
			    		}
			    		else
			    		{
			    			//statusSave=true;
			    			statusDoctor=true;
			    			System.out.println("statusDoctor="+statusDoctor);
			    		}
			         }
			    	 catch(Exception e)
			    	 {
			    	    System.out.println("Excepiton Query Data Doctor Exception : "+e+" query="+sql_doctor);
			    	    setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Sql Data Error");
			    	    TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), e.toString(), sql_doctor,"");
			    	    //statusSave=false;
			    	    statusDoctor=false;
			    	 }
		    	}
		    	else
		    	{
		    	   //Doctor Code is null
		    	   setMessage("Row No.="+r+" Doctor Code is null");
		    	   TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", "","");
		    	   //statusSave=false;
		    	   statusDoctor=false;
		    	}
		    	
		    	//expense Code
		    	   expenseCode=String.valueOf(tempRow.getCell((short) 1));
		    	   if(statusShow){System.out.println(" expenseCode="+expenseCode); }
		    	   if(!expenseCode.equals(""))
		    	   {
		    	   		String[][] ExpenseArr = null;
			    	    String sql_expense="SELECT * FROM EXPENSE WHERE HOSPITAL_CODE='"+this.hospital_code+"' AND CODE='"+expenseCode+"'";
				    	try
				    	{   
				    		ExpenseArr = cdb.query(sql_expense);
				    		if(ExpenseArr.length == 0)
				    		{
				    			if(statusShow){System.out.println("ไม่พบ Expense Code="+expenseCode+" นี้");}
				    			setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Expense Code="+expenseCode+" notfound");
				    			if(statusShow){ System.out.println(this.getMessage());}
					            TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", sql_expense,"");
					            //statusSave=false;
					            statusExpense=false;
				    		}
				    		else
				    		{
				    			//statusSave=true;
				    			statusExpense=true;
				    			String ExpDisArr[][]=null;
					    		ExpDisArr=GetExp(expenseCode);
					    		expenseSign=Integer.parseInt(ExpDisArr[0][0]);
					    		expenseAcc=ExpDisArr[0][1];
				    		}
				    	}
				    	catch(Exception e)
				    	{
				    	    if(statusShow){System.out.println("Excepiton Query Data Doctor Exception : "+e+" query="+sql_expense);}
				    	    setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Expense Code="+expenseCode+" Sql Data Error");
				    	    if(statusShow){ System.out.println(this.getMessage());}
				    	    TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), e.toString(), sql_expense,"");
				    	    statusSave=false;
				    	    statusExpense=false;
				    	}
			    	}
			    	else
			    	{
			    	    //Expense Code is null
				    	setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Expense Code is null");
				    	if(statusShow){ System.out.println(this.getMessage());}
				    	TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", "","");
				    	statusSave=false;
				    	statusExpense=false;
			    	 }
		    	   //amount
		    	   try
		    	    {
					   	amount=Double.parseDouble(String.valueOf(tempRow.getCell((short) 2)));
				    	if(statusShow){System.out.println(" amount="+amount);}
				    	    		
					   	if(amount<0)
					   	{
					   		setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Amount="+amount+" is less or equals 0");
					   		if(statusShow){ System.out.println(this.getMessage());}
					        TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", "","");
					        //statusSave=false;
					        statusAmount=false;
					   	}
					   	else if(amount==0)
					   	{
					   		statusAmount=false;
					   	}
					   	else
					   	{
					   		statusAmount=true;
					   	}
				    }
				    catch(Exception e)
				    {
				    	setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Amount="+amount+" is null");
				    	if(statusShow){ System.out.println(this.getMessage());}
				    	TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", "","");
				    	//statusSave=false;
				    	statusPrice=false;
				    }
				    //tax amount
			    	try
			    	{
				    	 taxAmount=Double.parseDouble(String.valueOf(tempRow.getCell((short) 3)));
				    	 if(statusShow){System.out.println("tesssssssssssssssssss");}
				    	 if(statusShow) {System.out.println(" taxAmount="+taxAmount);}
				    	    		
				    	 if(taxAmount<0)
					    {
					    	setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Tax Amount="+taxAmount+" is less ");
					    	if(statusShow){ System.out.println(this.getMessage());}
					    	TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", "","");
					    	//statusSave=false;
					    	statusTaxAmount=false;
					    }
				    	else if(taxAmount==0)
				    	{
				    	    statusTaxAmount=false;
				    	}
					    else
					    {
					    	 statusTaxAmount=true;
					    	 if(statusShow){System.out.println("statusTaxAmount="+statusTaxAmount);}
					    }
				    }
				    catch(Exception e)
				    {
				    	setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Tax Amount="+taxAmount+" is null");
				    	if(statusShow){ System.out.println(this.getMessage());}
				    	TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", "","");
				    	 //statusSave=false;
				    	statusPrice=false;
				    }
				    if(statusShow)
				    {
				    	 System.out.println("statusAmount="+statusAmount);
				    	 System.out.println("statusTaxAmount="+statusTaxAmount);
				    }
				    if(statusAmount==false && statusTaxAmount==false)//!(F && F)=T
			    	{
			    	  //statusSave=false;
				    	statusPrice=false;
			    	  if(statusShow){System.out.println("statusSave amount f taxamount f="+statusSave);}
			    	  setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Amount and Tax Amount incorrect");
				    	if(statusShow){ System.out.println(this.getMessage());}
				    	TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", "","");
			    	}
			    	else
			    	{
			    	    //statusSave=true;
			    		statusPrice=true;
			    	    if(statusShow)
			    	    {
			    	    	System.out.println("Amount+TaxAmount");
			    	    	System.out.println("statusSave="+statusSave);
			    	    }
			    	}
				    
			    	  if(statusShow){System.out.println("statusSave amount t taxamount t ="+statusSave);}
			    	  //docNo
			    	  if(!String.valueOf(tempRow.getCell((short) 4)).equals(""))
				    	{
				    	    docNo=String.valueOf(tempRow.getCell((short) 4));
				    	}
				    	else
				    	{
				    	    if(statusShow){System.out.println("docNo is null");}
				    	 }
			    	  //docDate
			    	    if(!String.valueOf(tempRow.getCell((short) 5)).equals(""))
			    		{
			    			//docDate=String.valueOf(tempRow.getCell((short) 5));
			    			docDate=JDate.saveDate(String.valueOf(tempRow.getCell((short) 5)));
			    			if(statusShow){System.out.println(" docDate="+docDate); }
			    		}	
			    	    else
				    	{
				    	    if(statusShow){System.out.println("docDate is null");}
				    	}
			    	    //note
			    	    if(!String.valueOf(tempRow.getCell((short) 6)).equals(""))
			    	    {
			    	    	note=String.valueOf(tempRow.getCell((short) 6));
			    	    	if(statusShow){System.out.println(" note="+note);}
			    	    }
			    	    else
				    	{
			    	    	if(statusShow){System.out.println("note is null");}
				    	}
			    	    //tax_type_code
			    	    
				    	    			if(!String.valueOf(tempRow.getCell((short) 7)).equals(""))
				    	    			{
						    	    		taxTypeCode=String.valueOf(tempRow.getCell((short) 7));
						    	    		if(statusShow){System.out.println(" taxTypeCode="+taxTypeCode); }
						    	    		if(taxTypeCode.equals("400") || taxTypeCode.equals("401") || taxTypeCode.equals("402") || taxTypeCode.equals("406"))
						    	    		{
						    	    			//statusSave=true;
						    	    			statusTax=true;
						    	    		}
						    	    		else
						    	    		{
						    	    			setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Tax Type Code ="+taxTypeCode+" is incorrect");
						    	                TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", "","");
						    	                //statusSave=false;
						    	                statusTax=true;
						    	    		}
				    	    			}
				    	    			else
				    	    			{
				    	    				setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Tax Type Code ="+taxTypeCode+" is null");
					    	                TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", "","");
					    	                //statusSave=false;
					    	                statusTax=false;
				    	    			}
				    	    			
				    	    			
				    	    			//department code
				    	    			departmentCode=String.valueOf(tempRow.getCell((short) 8));
				    	    			if(statusShow){System.out.println(" departmentCode="+departmentCode); }
				    	    			if(!departmentCode.equals(""))
				    	    			{
					    	    			String[][] DepartmentArr = null;
						    	    		String sql_department="SELECT * FROM DEPARTMENT WHERE HOSPITAL_CODE='"+this.hospital_code+"' AND CODE='"+departmentCode+"'";
						    	    		if(statusShow){System.out.println("sql_department="+sql_department);}
						    	    		try
						    	            {   
						    	    			DepartmentArr = cdb.query(sql_department);
						    	    			if(DepartmentArr.length == 0)
						    	    			{
						    	    				if(statusShow){System.out.println("ไม่พบ Department Code="+departmentCode+" นี้ จึงไปดึงข้อมูลจาก Doctor Code");}
								    	               
						    	    				statusDepartment=true;
						    	    			}
						    	    			else
						    	    			{
						    	    				//statusSave=true;
						    	    				statusDep=true;
						    	    				statusDepToLoc=true;
						    	    			}
						    	            }
						    	    		
						    	            catch(Exception e)
						    	            {
						    	                //System.out.println("Excepiton Query Data Department Code in Table Department Exception : "+e+" query="+sql_department);
						    	                setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Department Code ="+departmentCode+" sql data Error");
						    	                TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), e.toString(), sql_department,"");
						    	                statusSave=false;
						    	                statusDep=false;
						    	            }
				    	    			}
				    	    			else
				    	    			{
				    	    				statusDepartment=true;
				    	    			}
				    	    			if(statusDepartment)
				    	    			{
				    	    				if(statusDoctor)
				    	    				{
					    	    				String[][] DeDoctorArr=null;
					    	    				String sql_de_doctor="SELECT DEPARTMENT_CODE FROM DOCTOR WHERE HOSPITAL_CODE='"+this.hospital_code+"' AND CODE='"+doctorCode+"' ";
					    	    				if(statusShow){System.out.println("sql_de_doctor="+sql_de_doctor);}
					    	    				try
					    	    				{
					    	    					DeDoctorArr=cdb.query(sql_de_doctor);
					    	    					if(DeDoctorArr.length == 0)
					    	    					{
					    	    						if(statusShow){System.out.println("ไม่พบ Department Code ภายใน Doctor Code");}
					    	    						setMessage("Row No.="+r+" Doctor Code="+doctorCode+" query Department Code from table DOCTOR is notfound");
								    	                TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", sql_de_doctor,"");
								    	                //statusSave=false;
								    	               // statusDep=false;
								    	               // statusDep=false;
								    	                statusDep=true;
					    	    					}
					    	    					else
					    	    					{
					    	    						if(statusShow){System.out.println("DeDoctorArr[0][0]="+DeDoctorArr[0][0]);}
					    	    						if(DeDoctorArr[0][0] !=null)
					    	    						{
					    	    							departmentCode=DeDoctorArr[0][0];
					    	    							if(statusShow){System.out.println("DeDoctorArr[0][0]="+DeDoctorArr[0][0]);}
					    	    							statusDepToLoc=true;
					    	    							statusDep=true;//edit this here !!/2017
					    	    						}
					    	    						else
					    	    						{
					    	    							setMessage("Row No.="+r+" Doctor Code="+doctorCode+" query Department Code from table DOCTOR  is null");
									    	                TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", sql_de_doctor,"");
									    	                statusDepToLoc=false;
					    	    							//statusSave=false;
									    	               // statusDep=false;
									    	                statusDep=true;
					    	    						}
					    	    					}
					    	    				}
					    	    				catch(Exception e)
					    	    				{
					    	    					//System.out.println("Excepiton Query Data Department Code in Table Doctor Exception : "+e+" query="+sql_de_doctor);
					    	    					setMessage("Row No.="+r+" Doctor Code="+doctorCode+" query Department Code from table DOCTOR  is query data Error");
							    	                TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), e.toString(), sql_de_doctor,"");
							    	                //statusSave=false;
							    	                statusDep=false;
					    	    				}
				    	    				}
				    	    				else
				    	    				{
				    	    					setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Can't query Department Code from table DOCTOR " );
						    	                TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", "","");
						    	                //statusSave=false;
						    	                statusDep=false;
				    	    				}
				    	    			}
				    	    			//location code
				    	    			locationCode=String.valueOf(tempRow.getCell((short) 9));
				    	    			if(statusShow){System.out.println(" locationCode="+locationCode); }
				    	    			if(!locationCode.equals(""))
				    	    			{
					    	    			String[][] LocationArr = null;
						    	    		String sql_location="SELECT * FROM LOCATION WHERE HOSPITAL_CODE='"+this.hospital_code+"' AND CODE='"+locationCode+"'";
						    	    		if(statusShow){System.out.println("sql_location="+sql_location);}
						    	    		try
						    	            {   
						    	    			LocationArr = cdb.query(sql_location);
						    	    			if(LocationArr.length == 0)
						    	    			{
						    	    				if(statusShow){System.out.println("ไม่พบ Location Code="+locationCode+" นี้ จึงไปดึงข้อมูลจาก Department");}
						    	    				statusLocation=true;
						    	    			}
						    	    			else
						    	    			{
						    	    				//statusSave=true;
						    	    				statusLocation=false;
						    	    				statusLoc=true;
						    	    			}
						    	            }
						    	            catch(Exception e)
						    	            {
						    	                if(statusShow){System.out.println("Excepiton Query Data Location Code in Table Location Exception : "+e+" query="+sql_location);}
						    	                //statusSave=false;
						    	                statusLoc=true;
						    	            }
				    	    			}
				    	    			else
				    	    			{
				    	    				statusLocation=true;
				    	    			}
				    	    			
				    	    			if(statusLocation)
				    	    			{
				    	    				if(statusDepToLoc)//department is not null and value in database
				    	    				{
				    	    					String[][] LoDepartmentArr=null;
					    	    				String sql_lo_department="SELECT DEFAULT_LOCATION_CODE FROM DEPARTMENT WHERE HOSPITAL_CODE='"+this.hospital_code+"' AND CODE='"+departmentCode+"' ";
					    	    				if(statusShow){System.out.println("sql_lo_department="+sql_lo_department);}
					    	    				try
					    	    				{
					    	    					LoDepartmentArr=cdb.query(sql_lo_department);
					    	    					if(LoDepartmentArr.length == 0)
					    	    					{
					    	    						if(statusShow){System.out.println("ไม่พบ Location Code ภายใน Department Code");}
					    	    						setMessage("Row No.="+r+" Doctor Code="+doctorCode+" query Location Code from table DEPARTMENT by departmentCode="+departmentCode+" is notfound");
								    	                TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", sql_lo_department,"");
								    	                //statusSave=false;
								    	                statusLoc=false;
					    	    						
					    	    					}
					    	    					else
					    	    					{
					    	    						locationCode=LoDepartmentArr[0][0];
					    	    						if(statusShow){System.out.println("LoDepartmentArr[0][0]="+LoDepartmentArr[0][0]);}
					    	    						//statusSave=true;
					    	    						statusLoc=true;
					    	    					}
					    	    				}
					    	    				catch(Exception e)
					    	    				{
					    	    					if(statusShow){System.out.println("Excepiton Query Data Location Code in Table Department Exception : "+e+" query="+sql_lo_department);}
					    	    					setMessage("Row No.="+r+" Doctor Code="+doctorCode+" query Location Code from table DEPARTMENT by departmentCode="+departmentCode+" is query data Error");
							    	                TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), e.toString(), sql_lo_department,"");
							                        //statusSave=false;
							    	                statusLoc=false;
							    	                
					    	    				}
				    	    				}
				    	    				else
				    	    				{
				    	    					setMessage("Row No.="+r+" Doctor Code="+doctorCode+" Can't query Location Code from table DEPARTMENT by Deparment Code="+departmentCode);
						    	                TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", "","");
						    	                //statusSave=false;
						    	                statusLoc=false;
				    	    				}
				    	    			} 	    		
				    	    		//}//amount & taxamount is true
				    	    		
		    	    			//}//if statusExpense=true
		    	    		if(statusShow){System.out.println("statusSave="+statusSave);}
		    	    		if(statusDoctor==true && statusExpense==true && statusPrice==true && statusTax==true && statusDep==true && statusLoc==true)
		    	    		{
		    	    			statusSave=true;
		    	    		}
		    	    		else
		    	    		{
		    	    			statusSave=false;
		    	    		}
		    	    		if(statusSave)
	    	    	    	{
	    	    				String ExpSeArr[][]=null;
	    	    				String sql="SELECT * FROM TRN_EXPENSE_DETAIL WHERE HOSPITAL_CODE='"+this.hospital_code+"'"
	    	    				+" AND DOCTOR_CODE='"+doctorCode+"' AND LINE_NO='"+r+"' "//comment line below (not check duplicate EXPENSE_CODE)
	    	    				//+" AND DOCTOR_CODE='"+doctorCode+"' AND EXPENSE_CODE='"+expenseCode+"' "
		    					+" AND AMOUNT="+amount+" AND TAX_AMOUNT="+taxAmount
		    					+" AND YYYY='"+batch_year+"' AND MM='"+batch_month+"' ";
		    					try
								{
		    				 		ExpSeArr=cdb.query(sql);
		    				 		if(ExpSeArr.length >0){
		    				 			setMessage("Doctor Code="+doctorCode+" มีข้อมูลในตาราง TRN_EXPENSE_DETAIL แล้ว");
		    				 			if(statusShow){ System.out.println(this.getMessage());}
		    							TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", sql,"");
		    							numNoSave++;
		    							
		    				 		}else//ไม่มีข้อมูลในระบบ
		    				 		{
		    				 			String sql_insert="INSERT INTO TRN_EXPENSE_DETAIL(HOSPITAL_CODE, YYYY, MM, DOCTOR_CODE, EXPENSE_CODE,"
		    	    						+" EXPENSE_SIGN, EXPENSE_ACCOUNT_CODE, AMOUNT, TAX_AMOUNT, UPDATE_DATE, UPDATE_TIME," 
		    	    						+" USER_ID, DOC_NO, DOC_DATE, NOTE, TAX_TYPE_CODE, DEPARTMENT_CODE, LOCATION_CODE, "
		    	    						+ "BATCH_NO, PAYMENT_DATE, PAYMENT_TERM, LINE_NO) "
		    	    						+" VALUES('"+this.hospital_code+"','"+batch_year+"', '"+batch_month+"', '"+doctorCode+"','"+expenseCode+"', "+expenseSign+", "
		    	    						+" '"+expenseAcc+"', "+amount+","+taxAmount+","
		    	    						+" '"+JDate.getDate()+"','"+JDate.getTime()+"', '"+this.getUserName()+"_EXCEL"+"', '"+docNo+"',"
		    	    						+" '"+docDate+"', '"+note+"', '"+taxTypeCode+"', '"+departmentCode+"', '"+locationCode+"', "
		    	    						+" '', '', '', '"+JDate.getDate()+JDate.getTime()+"_"+r+"')";
		    	    	    			if(statusShow){System.out.println("sql_insert="+sql_insert);}
		    	    					 	try
		    	    						{
		    	    					 		cdb.insert(sql_insert);
		    	    					 		numSave++;
		    	    						}
		    	    					 	catch(Exception e)
		    	    					 	{
		    	    					 		if(statusShow){System.out.println("Insert Expense for Excel Excepiton : "+e+"query="+sql_insert);}
		    	    					 		//cdb.rollDB();
		    	    					 		numNoSave++;
		    	    					 		
		    	    					 	}
		    				 		}
		    				 	}
		    				 	catch(Exception e)
		    				 	{
		    				 		setMessage("Query Expense Condition from excel Excepiton : "+e+"query="+sql);
	    				 			if(statusShow){ System.out.println(this.getMessage());}
	    							TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", sql,"");
	    							numNoSave++;
		    				 		//return false;
		    				 	}                                                                                               
	    	    	    			
	    	    			}
	    	    			else
	    	    			{
	    	    				numNoSave++;
	    	    			}
	    	    			
		    	}//for row
	    	System.out.println("numNoSave="+numNoSave+" numRow="+numRow);
	    	if(numSave == numRow)
	    	{
	    		cdb.commitDB();
	    		
	    	}
	    	else
	    	{
	    		cdb.rollDB();
	    	}
		    	if(statusShow){
		    	System.out.println("สรุปการ Import Expense จาก Flat File Excel วันที่ "+JDate.getDate()+" เวลา "+JDate.getTime());
	    		System.out.println("Save Complete="+numSave);
	    		System.out.println("Save Incomplete="+numNoSave);
		    	}
		    	setMessage(" : Data Total="+numRow+" Complete="+numSave+" Incomplete="+numNoSave);
		    	status= true;
		    }
			else
			{
				setMessage("Number Column is not quals 10 Columns");
				TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), "", sqlMessage,"");
				status= false;	
			}
		}
    	catch(Exception e)
    	{
    		System.out.println("eeee="+e);
    		//System.out.println("connect="+da.getConnection());
    		setMessage("Can't open file excel");
    		//System.out.println("connect="+da.getConnection());
    		TRN_Error.writeErrorLog(cc.getConnection(), processName,  this.getMessage(), e.toString(), sqlMessage,"");
    		status= false;
    	}
    	
    	return status;
    }
	@Override
    public boolean exportData(String fn, String hp, String type, String year, String month, DBConn d, String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	
	private String[][] GetExp(String ExpCode)
	{
        //Query data from table EXPENSE
		DBConn c = new DBConn();
		try {
			c.setStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
		}
    	String sql="";
        String[][] ExpArr = null;
        sql="SELECT SIGN, ACCOUNT_CODE FROM EXPENSE "
        +" WHERE HOSPITAL_CODE='"+hospital_code+"' AND ACTIVE=1 "
        +" AND CODE='"+ExpCode+"'";
                
      	//System.out.println("sql="+sql);
    	try{   
    		ExpArr = c.query(sql);	
        }catch(Exception e){
            System.out.println("Excepiton Query Data Table EXPENSE : "+e+" query="+sql);
        }finally{
        	c.closeDB("");
        }
        return ExpArr;
    }

	@Override
	public boolean exportData(String fn, String hp_code, String type, String year, String month, DBConn d, String path,
			String filing_type) {
		// TODO Auto-generated method stub
		return false;
	} 
}