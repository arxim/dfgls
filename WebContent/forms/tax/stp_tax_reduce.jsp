<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap,java.util.*" errorPage="../error.jsp"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.bean.obj.util.JDate" %>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.obj.util.Utils "%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="java.sql.Types"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@ include file="../../_global.jsp" %><%    
 

		if (!Guard.checkPermission(session, Guard.PAGE_INPUT_METHOD_ALLOC_ITEM_MAIN)) {
            response.sendRedirect("../message.jsp");
            return;
        }

        if (session.getAttribute("LANG_CODE") == null)
            session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
    
    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	labelMap.add("TITLE_MAIN", "TAX", "คำนวณภาษี");

    request.setAttribute("labelMap", labelMap.getHashMap());
    
    String current_mm="", current_year="";
    current_mm=JDate.getMonth();
	current_year =JDate.getYear(); 
	
	DBConnection con = new DBConnection();
    con.connectToLocal();
    
    DBConn conData = new DBConn();
    conData.setStatement();
    
    DBConn conSaveData = new DBConn();
    conSaveData.setStatement();
    
    request.setCharacterEncoding("UTF-8");
    DataRecord tax91Rec = null, doctorRec = null, doctorProfileRec=null, taxReduceRec=null;
    byte MODE = DBMgr.MODE_INSERT;
	String showC01="", showC14="", showA06="",showBtSave="", people_no="", people_tax_id="";
    String arr07[]=null, arr08[]=null, arr09[]=null;
    String doctorCode="", getMM="", getYYYY="";
    try{
	    if(request.getParameter("DOCTOR_CODE").toString()!=null)
		{
			doctorCode=request.getParameter("DOCTOR_CODE").toString();
		}
	}
    catch(Exception e)
    {
    	System.out.println("No Doctor");
    }
    try{
	    if(request.getParameter("MM").toString()!=null)
		{
			getMM=request.getParameter("MM").toString();
		}
		else
		{
			getMM=current_mm;
		}
    }
    catch(Exception e)
    {
    	System.out.println("No Month");
    	getMM=current_mm;
    }
    try{
		if(request.getParameter("YYYY").toString() !=null)
		{
			getYYYY=request.getParameter("YYYY").toString();
		}
		else
		{
			getYYYY=current_year;
		}
    }
    catch(Exception e)
    {
    	System.out.println("No Year");
    	getYYYY=current_year;
    }
    if (request.getParameter("MODE") != null) {

        MODE = Byte.parseByte(request.getParameter("MODE"));
 
		//=======c04=========
		String c04021="", c04041="", c04061="", c04081="", c04_031="0", c04_041="0", c04_081="0", c04_091="0";
		try{
			if(request.getParameter("c04_03") !=null)
			{
				c04021=request.getParameter("c04_03");
			}
		} catch(Exception e) {}
		try{
			if(request.getParameter("c04_04") !=null)
			{
				c04041=request.getParameter("c04_04");
			}
		} catch(Exception e) {}
		try{
			if(request.getParameter("c04_08") !=null)
			{
				c04061=request.getParameter("c04_08");
			}
		} catch(Exception e) {}
		try{
			if(request.getParameter("c04_09") !=null)
			{
				c04081=request.getParameter("c04_09");
			}
		} catch(Exception e) {}

		if(!c04021.equals("")) {c04_031="30000"; }
		if(!c04041.equals("")) {c04_041="30000"; }
		if(!c04061.equals("")) {c04_081="30000"; }
		if(!c04081.equals("")) {c04_091="30000"; }
		//===================
		//out.println(request.getParameter("a01"));
        tax91Rec = new DataRecord("STP_TAX_REDUCE");
		
        tax91Rec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
        tax91Rec.addField("DOCTOR_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CODE"),true);
		if(MODE == DBMgr.MODE_INSERT) 
		{
			tax91Rec.addField("YYYY", Types.VARCHAR, current_year,true);
			tax91Rec.addField("MM", Types.VARCHAR, current_mm,true);
		}
		else
		{
			tax91Rec.addField("YYYY", Types.VARCHAR, request.getParameter("OLD_YYYY"),true);
			tax91Rec.addField("MM", Types.VARCHAR, request.getParameter("OLD_MM"),true);
		}
			//tax91Rec.addField("PEOPLE_TAX_ID", Types.VARCHAR, request.getParameter("PEOPLE_TAX_ID"));
        tax91Rec.addField("DOCTOR_STATUS", Types.VARCHAR, request.getParameter("DOCTOR_STATUS"),true);
        tax91Rec.addField("SPOUSE_BIRTHDAY", Types.VARCHAR, JDate.saveDate(request.getParameter("SPOUSE_BIRTHDAY")));
       	tax91Rec.addField("SPOUSE_NO", Types.VARCHAR, request.getParameter("SPOUSE_NO"));
        tax91Rec.addField("SPOUSE_TAX_NO", Types.VARCHAR, request.getParameter("SPOUSE_TAX_ID"));
        tax91Rec.addField("SPOUSE_NAME", Types.VARCHAR, request.getParameter("SPOUSE_NAME"));
	    tax91Rec.addField("SPOUSE_TYPE", Types.VARCHAR, request.getParameter("SPOUSE_TYPE"));
        tax91Rec.addField("SPOUSE_TYPE1", Types.VARCHAR, request.getParameter("SPOUSE_TYPE1"));
        tax91Rec.addField("A08", Types.NUMERIC, request.getParameter("a08_02"));
        tax91Rec.addField("A081", Types.NUMERIC, request.getParameter("a08_021"));
        tax91Rec.addField("A10", Types.NUMERIC, request.getParameter("a10"));
        tax91Rec.addField("A101", Types.NUMERIC, request.getParameter("a101"));
        tax91Rec.addField("B01", Types.NUMERIC, request.getParameter("b01"));
        tax91Rec.addField("B011", Types.NUMERIC, request.getParameter("b011"));
        tax91Rec.addField("B02", Types.NUMERIC, request.getParameter("b02"));
        tax91Rec.addField("B021", Types.NUMERIC, request.getParameter("b021"));
        tax91Rec.addField("B03", Types.NUMERIC, request.getParameter("b03"));
        tax91Rec.addField("B031", Types.NUMERIC, request.getParameter("b031"));
        tax91Rec.addField("B04", Types.NUMERIC, request.getParameter("b04"));
        tax91Rec.addField("B05", Types.NUMERIC, request.getParameter("b05"));
        tax91Rec.addField("B06", Types.NUMERIC, request.getParameter("b06"));
        tax91Rec.addField("B061", Types.NUMERIC, request.getParameter("b061"));
        tax91Rec.addField("B07", Types.NUMERIC, request.getParameter("b07"));
        tax91Rec.addField("B071", Types.NUMERIC, request.getParameter("b071"));
        tax91Rec.addField("C01", Types.NUMERIC, request.getParameter("c01"));
        tax91Rec.addField("C02", Types.NUMERIC, request.getParameter("c02"));
        tax91Rec.addField("C03_01", Types.NUMERIC, request.getParameter("c03_01"));
        tax91Rec.addField("C03_02", Types.NUMERIC, request.getParameter("c03_02"));
        tax91Rec.addField("C03_03", Types.NUMERIC, request.getParameter("c03_03"));
        tax91Rec.addField("C03_04", Types.NUMERIC, request.getParameter("c03_04"));
        tax91Rec.addField("C04_01", Types.VARCHAR, request.getParameter("c04_03"));//บัตรประชาชนบิดาผู้มีเงินได้
        tax91Rec.addField("C04_02", Types.NUMERIC, c04_031);//จำนวนเงินของบิดาผู้มีเงินได้
        tax91Rec.addField("C04_03", Types.VARCHAR, request.getParameter("c04_04"));//บัตรประชาชนมารดาผู้มีเงินได้
        tax91Rec.addField("C04_04", Types.NUMERIC, c04_041);//จำนวนเงินของมารดาผู้มีเงินได้
        tax91Rec.addField("C04_05", Types.VARCHAR, request.getParameter("c04_08"));
        tax91Rec.addField("C04_06", Types.NUMERIC, c04_081);
        tax91Rec.addField("C04_07", Types.VARCHAR, request.getParameter("c04_09"));
        tax91Rec.addField("C04_08", Types.NUMERIC, c04_091);
		try{
        tax91Rec.addField("C05", Types.NUMERIC, request.getParameter("c05"));
		} catch(Exception e) { }try{
        tax91Rec.addField("C051", Types.NUMERIC, request.getParameter("c051"));
		} catch(Exception e) { }
        tax91Rec.addField("C06_01", Types.VARCHAR, request.getParameter("c06_03"));
        tax91Rec.addField("C06_02", Types.VARCHAR, request.getParameter("c06_04"));
        tax91Rec.addField("C06_03", Types.VARCHAR, request.getParameter("c06_08"));
        tax91Rec.addField("C06_04", Types.VARCHAR, request.getParameter("c06_09"));
        tax91Rec.addField("C06_05", Types.NUMERIC, request.getParameter("c06_10"));
		try{
        tax91Rec.addField("C06_051", Types.NUMERIC, request.getParameter("c06_101"));
		} catch(Exception e) {}
        tax91Rec.addField("STATUS", Types.VARCHAR, request.getParameter("STATUS"));
        tax91Rec.addField("CREATE_DATE", Types.VARCHAR, JDate.getDate());
        tax91Rec.addField("CREATE_TIME", Types.VARCHAR, JDate.getTime());
        tax91Rec.addField("CREATE_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
        tax91Rec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
        tax91Rec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
        tax91Rec.addField("UPDATE_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
		
        //System.out.println("USER_GROUP_CODE="+session.getAttribute("USER_GROUP_CODE").toString());
        //String arr07[]=null, arr08[]=null, arr09[]=null;
        try{
	     if(request.getParameterValues("c07[]") !=null )
	     {
	       	arr07	=request.getParameterValues("c07[]");
	     }
       }
       catch(Exception e)
       {
       		System.out.println("ไม่มีข้อมูลรายการของผู้มีเงินได้");
       }
       try{
	   		if(request.getParameterValues("c08[]") !=null )
	        {
	           	arr08	=request.getParameterValues("c08[]");
	         }
       }
       catch(Exception e)
       {
       		System.out.println("ไม่มีข้อมูลรายการของคู่สมรส");
       }
       try
       {
	   		if(request.getParameterValues("c09[]") !=null )
	        {
	           	arr09	=request.getParameterValues("c09[]");
	        }
       }
       catch(Exception e)
       {
       		System.out.println("ไม่มีข้อมูลรายการ");
       }
       if (MODE == DBMgr.MODE_INSERT) 
       {
        	if (DBMgr.insertRecord(tax91Rec)) 
            {
        		//System.out.println("ddddddddddddddddddd");
        		
            	String sqlDataReduce="SELECT CODE FROM STP_MASTER_TAX_REDUCE WHERE STATUS=1 "
            	    +" AND HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"' ";
    				System.out.println("sqlData= "+sqlDataReduce);
    				String [][]arrDataReduce = conData.query(sqlDataReduce);
    				System.out.println("arrDataReduce="+arrDataReduce.length);
    				
    				if(arrDataReduce.length !=0)
    				{
    					for(int h=0;h<arrDataReduce.length;h++)
    					{
    						int num_reduce=0;
    						String insertData="";
    						double getAmount1=0,getAmount2=0;
    						num_reduce=Integer.parseInt(arrDataReduce[h][0]);
    						//System.out.println("num_reduce="+num_reduce);
    						for(int d=0;d<arr09.length;d++)
        					{
    							//System.out.println("arr09="+arr09[d]);
    							try
    							{
	    							if(num_reduce==Integer.parseInt(arr09[d]))
	    							{
			    						getAmount1=Double.parseDouble(arr07[d]);
			    						try
			    						{
			    							getAmount2=Double.parseDouble(arr08[d]);
			    						}
	    								catch(Exception e)
	    								{
	    									getAmount2=0;
	    								}
			    						//System.out.println("arr07="+arr07[d]);
			    						//System.out.println("arr08="+arr08[d]);
			    						insertData="INSERT INTO STP_TAX_REDUCE_DETAIL "
										+" (HOSPITAL_CODE, DOCTOR_CODE, YYYY, MM, MASTER_REDUCE_CODE, "
										+" AMOUNT_1, AMOUNT_2, CREATE_ID, CREATE_DATE, CREATE_TIME, "
										+" UPDATE_ID, UPDATE_DATE, UPDATE_TIME) "
										+" VALUES('"+session.getAttribute("HOSPITAL_CODE").toString()+"', "
										+" '"+request.getParameter("DOCTOR_CODE")+"', "
										+" '"+current_year+"', '"+current_mm+"', '"+num_reduce+"', "+getAmount1+", "
										+getAmount2+", '"+session.getAttribute("USER_ID").toString()+"','"+JDate.getDate()+"', "
										+" '"+JDate.getTime()+"', '"+session.getAttribute("USER_ID").toString()+"', "
										+" '"+JDate.getDate()+"', '"+JDate.getTime()+"')";
										System.out.println("sqlInsert="+insertData);
										//ResultSet rsInsertData = con.executeQuery(insertData);
										try
							    	    {
											conData.insert(insertData);
											conData.commitDB();
							    	    }
										catch(Exception e)
							    	    {
							    	       System.out.println("insert_tax_reduce TABLE: STP_TAX_REDUCE_DETAIL Excepiton : "+e+"query="+insertData);
							    	       conData.rollDB();
							    	    }
	    							}
    							}
    							catch(Exception e)
					    	    {
					    	       System.out.println("Excepiton : "+e);
					    	    }
        					}
    					}
    				}
    					
                session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "tax/stp_tax_reduce_main.jsp"));
            } 
            else 
            {
                session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
            }
        } 
        else if (MODE == DBMgr.MODE_UPDATE) 
        {
            if (DBMgr.updateRecord(tax91Rec)) 
            {
            	String sqlDelete="DELETE FROM STP_TAX_REDUCE_DETAIL WHERE DOCTOR_CODE='"+request.getParameter("DOCTOR_CODE")+"' AND HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"'"
        		+" AND MM='"+request.getParameter("OLD_MM")+"' AND YYYY='"+request.getParameter("OLD_YYYY")+"'";
        		System.out.println("sqlDelete="+sqlDelete);
        		try
	    	    {
					conData.insert(sqlDelete);
					conData.commitDB();
	    	    }
	    	    catch(Exception e)
	    	    {
	    	       System.out.println("DELETE STP_TAX_REDUCE_DETAIL Excepiton : "+e+"query="+sqlDelete);
	    	       conData.rollDB();
	    	    }
        		String sqlDataReduce="SELECT CODE FROM STP_MASTER_TAX_REDUCE WHERE STATUS=1 "
            	    +" AND HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"' ";
    				//System.out.println("sqlData= "+sqlDataReduce);
    				String [][]arrDataReduce = conData.query(sqlDataReduce);
    				//System.out.println("arrDataReduce="+arrDataReduce.length);
    				
    				if(arrDataReduce.length !=0)
    				{
    					for(int h=0;h<arrDataReduce.length;h++)
    					{
    						int num_reduce=0;
    						String insertData="";
    						double getAmount1=0,getAmount2=0;
    						num_reduce=Integer.parseInt(arrDataReduce[h][0]);
    						//System.out.println("num_reduce="+num_reduce);
    						for(int d=0;d<arr09.length;d++)
        					{
    							if(num_reduce==Integer.parseInt(arr09[d]))
    							{
		    						getAmount1=Double.parseDouble(arr07[d]);
		    						try{
		    						getAmount2=Double.parseDouble(arr08[d]);
		    						}
		    						catch(Exception e)
		    						{
		    							getAmount2=0;
		    						}
		    						insertData="INSERT INTO STP_TAX_REDUCE_DETAIL "
									+" (HOSPITAL_CODE, DOCTOR_CODE, YYYY, MM, MASTER_REDUCE_CODE, "
									+" AMOUNT_1, AMOUNT_2, CREATE_ID, CREATE_DATE, CREATE_TIME, "
									+" UPDATE_ID, UPDATE_DATE, UPDATE_TIME) "
									+" VALUES('"+session.getAttribute("HOSPITAL_CODE").toString()+"', "
									+" '"+request.getParameter("DOCTOR_CODE")+"', "
									+" '"+request.getParameter("OLD_YYYY")+"', '"+request.getParameter("OLD_MM")+"', '"+num_reduce+"', "+getAmount1+", "
									+getAmount2+", '"+session.getAttribute("USER_ID").toString()+"','"+JDate.getDate()+"', "
									+" '"+JDate.getTime()+"', '"+session.getAttribute("USER_ID").toString()+"', "
									+" '"+JDate.getDate()+"', '"+JDate.getTime()+"')";
									//System.out.println("sqlInsert="+insertData);
									//ResultSet rsInsertData = con.executeQuery(insertData);
									try
						    	    {
										conData.insert(insertData);
										conData.commitDB();
						    	    }
									catch(Exception e)
						    	    {
						    	       System.out.println("insert_tax_reduce TABLE: STP_TAX_REDUCE_DETAIL Excepiton : "+e+"query="+insertData);
						    	       conData.rollDB();
						    	    }
    							}
        					}
    					}
    				}
                session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "tax/stp_tax_reduce_main.jsp"));
            } 
            else 
            {
                session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
            }
        }

        response.sendRedirect("../message.jsp");
        return;
    } 
    else if (!doctorCode.equals("")) 
    {
    	
    	//doctorCode=request.getParameter("DOCTOR_CODE").toString();
    	
    	String sqlTax91="";
    	sqlTax91="SELECT * FROM STP_TAX_REDUCE WHERE DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") + 
		"' AND HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"'"
		+" AND MM='"+getMM+"' AND YYYY='"+getYYYY+"'";
    	tax91Rec = DBMgr.getRecord(sqlTax91);
    	System.out.println("sqlTax91="+sqlTax91);
    	doctorRec = DBMgr.getRecord("SELECT * FROM DOCTOR WHERE CODE = '" + request.getParameter("DOCTOR_CODE") + "'");
    	if (tax91Rec == null) 
        {
    		sqlTax91="SELECT * FROM STP_TAX_REDUCE WHERE DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") + 
    		"' AND HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' ORDER BY  YYYY + MM DESC";
    		//+" AND MM='"+current_mm+"' AND YYYY='"+current_year+"'";
    		tax91Rec = DBMgr.getRecord(sqlTax91);
        	
    		MODE = DBMgr.MODE_INSERT;
	        String dCode=DBMgr.getRecordValue(doctorRec, "CODE");
	        String dpCode=DBMgr.getRecordValue(doctorRec, "DOCTOR_PROFILE_CODE");
	            
	            if(dCode.equals(dpCode))
	            {
	            	showC01="30000.00";
	                showC14="30000.00";
	                showA06="30000.00";
	            }
	            else
	            {
	            	showC01="60000.00";
	                showC14="60000.00";
	                showA06="60000.00";
	            }
        } 
        else 
        {
        	MODE = DBMgr.MODE_UPDATE;
            showBtSave="1";
            showC01=DBMgr.getRecordValue(tax91Rec, "C01");
            showC14=DBMgr.getRecordValue(tax91Rec, "C14_01");
		}
        System.out.println("MODE="+MODE);
        
    } 
    
%>
<%	
String report = ""; 
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
		<link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
		<script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
<script language="javascript">
    //parent.toggleFrame();
    //++++++++++++++ Script ++++++++++++
    //document.getElementById('mytab')
	
	//=========ค้นหา doctor code===============
			function CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    AJAX_Refresh_DOCTOR();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_DOCTOR() {
                //alert('AJAX_Refresh_DEPARTMENT');
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value + "' AND HOSPITAL_CODE='<%= session.getAttribute("HOSPITAL_CODE").toString() %>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
            }
            
            function AJAX_Handle_Refresh_DOCTOR() {
            
                if (AJAX_IsComplete()) {
                    //alert('AJAX_Handle_Refresh_DEPARTMENT');
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
						//alert(isXMLNodeExist(xmlDoc, "CODE"));
                        document.mainForm.DOCTOR_CODE.value = "";
                        document.mainForm.DOCTOR_NAME.value = "";
                        return;
                    }
                   // alert("kkkkkkkkkkk");
                    // Data found
					//var d_code=getXMLNodeValue(xmlDoc, "DOCTOR_PROFILE_CODE");
					//var code=getXMLNodeValue(xmlDoc, "CODE");
					document.mainForm.DOCTOR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
					
                }
               // alert("ok loaddddd");
                window.location = 'stp_tax_reduce.jsp?DOCTOR_CODE=' + document.mainForm.DOCTOR_CODE.value;
                //mainForm.submit();
                //alert("okdddddd");
               //JsOnLoad(DBMgr.MODE_INSERT);
            }	
            
            function doctorOnSearch(id)
            {
            	//alert("ok");
            	if(id==5)
            	{
					var DoctorProfileCode = document.getElementById('DoctorProfileCode');
					var url='../search.jsp?TABLE=DOCTOR&TABLE1=DOCTOR_PROFILE&TARGET=DOCTOR_CODE&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&COND=[AND (DOCTOR.DOCTOR_PROFILE_CODE=DOCTOR_PROFILE.CODE) AND (DOCTOR.ACTIVE=\''+1+'\') AND (DOCTOR.DOCTOR_PROFILE_CODE=\''+ DoctorProfileCode.value +'\') ]&HANDLE=AJAX_Refresh_DOCTOR';
				}
				else
				{
					var url='../search.jsp?TABLE=DOCTOR&TABLE1=DOCTOR_PROFILE&TARGET=DOCTOR_CODE&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&COND=[AND (DOCTOR.DOCTOR_PROFILE_CODE=DOCTOR_PROFILE.CODE) AND (DOCTOR.ACTIVE=\''+1+'\') ]&HANDLE=AJAX_Refresh_DOCTOR';
				}
				openSearchForm(url);
			}	
            function AJAX_Refresh_TAX() {
                //alert('AJAX_Refresh_DEPARTMENT');
                var target = "../../RetrieveData?TABLE=TAX_91&COND=DOCTOR_CODE='" + document.mainForm.DOCTOR_CODE.value + "' AND YYYY='2009' AND HOSPITAL_CODE='<%= session.getAttribute("HOSPITAL_CODE").toString() %>'";
                AJAX_Request(target, AJAX_Handle_Refresh_TAX);
            }
            
            function AJAX_Handle_Refresh_TAX() {
                if (AJAX_IsComplete()) {
                    //alert('AJAX_Handle_Refresh_DEPARTMENT');
                    var xmlDoc = AJAX.responseXML;
					alert(getXMLNodeValue(xmlDoc, "DOCTOR_CODE"));
                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "DOCTOR_CODE")) {
						mainForm.MODE.value = <%=DBMgr.MODE_INSERT%>;
						alert(mainForm.MODE.value);
                        return;
                    }
                    
                    mainForm.MODE.value = <%=DBMgr.MODE_UPDATE%>;
                    alert(mainForm.MODE.value);
                    //mainForm.submit();
                }
            }	
            function getSetRadio(obj)
			{ 
				for (var i=0; i < obj.length; i++)
				{
					if (arguments.length > 1)
					{ // we are setting
						if (arguments[1] == null) 
						{	obj[i].checked = false;	} 
						else if (obj[i].value == arguments[1]) 
						{
							obj[i].checked = true;
							return true;
						}
					}
					else // we are getting
					{
						if (obj[i].checked) 
						return obj[i].value;
					}
				}
				return false;
			}
			/*เวลาใช้
						var e = document.frm;
						if(getSetRadio(e.heart_sex)==false)
						{
							alert('กรุณาเลือกเพศ');
							e.heart_sex[0].focus();
							return false;
							}
						if(true)
						{
							frm.submit();
						}
			*/
        function SAVE_Click()
        {
        	//alert("save ok");
        	//alert("mode="+mainForm.MODE.value);
        	//if(mainForm.MODE.value==<%//=DBMgr.MODE_INSERT%>)
        	//{
        		//document.mainForm.submit();
        	//}
        		if(mainForm.DOCTOR_CODE.value=="")
        		{
        			alert('กรุณาเลือกแพทย์');
					mainForm.DOCTOR_CODE.focus();
					return false;
        		}
        		if(mainForm.c01.value=="")
        		{
        			alert('กรุณาคลิก "Select" เพื่อแสดงแพทย์');
					mainForm.DOCTOR_CODE.focus();
					return false;
        		}
        		if(getSetRadio(mainForm.DOCTOR_STATUS)==false)
				{
					alert('กรุณาเลือกสถานภาพ');
					mainForm.DOCTOR_STATUS[0].focus();
					return false;
				}
             	
             	if(mainForm.SPOUSE_NO.value !="")
             	{
             		if(parseInt(mainForm.SPOUSE_NO.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนคู่สมรสไม่ครบ 13 หลัก");
             			mainForm.SPOUSE_NO.focus();
             			return false;
             		}
             	}
             	if(mainForm.SPOUSE_TAX_ID.value !="")
             	{
             		if(parseInt(mainForm.SPOUSE_TAX_ID.value.length) < 10)
             		{
             			alert("เลขประจำตัวผู้เสียภาษีอากรของคู่สมรสไม่ครบ 10 หลัก");
             			mainForm.SPOUSE_TAX_ID.focus();
             			return false;
             		}
             	}
             	if(mainForm.c04_01.checked==true)
             	{
             		if(parseInt(mainForm.c04_03.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนบิดาของผู้มีเงินได้ไม่ครบ 13 หลัก");
             			mainForm.c04_03.focus();
             			return false;
             		}
             	}
             	if(mainForm.c04_02.checked==true)
             	{
             		if(parseInt(mainForm.c04_04.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนมารดาของผู้มีเงินได้ไม่ครบ 13 หลัก");
             			mainForm.c04_04.focus();
             			return false;
             		}
             	}
             	if(mainForm.c04_06.checked==true)
             	{
             		if(parseInt(mainForm.c04_08.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนบิดาของคู่สมรสไม่ครบ 13 หลัก");
             			mainForm.c04_08.focus();
             			return false;
             		}
             	}
             	if(mainForm.c04_07.checked==true)
             	{
             		if(parseInt(mainForm.c04_09.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนมารดาของคู่สมรสไม่ครบ 13 หลัก");
             			mainForm.c04_09.focus();
             			return false;
             		}
             	}
             	if(mainForm.c06_01.checked==true)
             	{
             		if(parseInt(mainForm.c06_03.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนบิดาของผู้มีเงินได้ในการจ่ายเบี้ยประกันสุขภาพไม่ครบ 13 หลัก");
             			mainForm.c06_03.focus();
             			return false;
             		}
             	}
             	if(mainForm.c06_02.checked==true)
             	{
             		if(parseInt(mainForm.c06_04.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนมารดาของผู้มีเงินได้ในการจ่ายเบี้ยประกันสุขภาพไม่ครบ 13 หลัก");
             			mainForm.c06_04.focus();
             			return false;
             		}
             	}
             	if(mainForm.c06_06.checked==true)
             	{
             		if(parseInt(mainForm.c06_08.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนบิดาของคู่สมรสในการจ่ายเบี้ยประกันสุขภาพไม่ครบ 13 หลัก");
             			mainForm.c06_08.focus();
             			return false;
             		}
             	}
             	if(mainForm.c06_07.checked==true)
             	{
             		if(parseInt(mainForm.c06_09.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนมารดาของคู่สมรสในการจ่ายเบี้ยประกันสุขภาพไม่ครบ 13 หลัก");
             			mainForm.c06_09.focus();
             			return false;
             		}
             	}
           if(true)
           {            
        		document.mainForm.submit();
        	}
        	
        }	
    var CONTS_04 = 30000.00;
    
    function SelectElement(ElementName){
        return document.getElementById(ElementName);
    }    
    function SelectMaritalStatus(id)
    {   
    	//alert(id);
    	if(id =="2")//สมรส
    	{
   			mainForm.SPOUSE_NO.disabled=false;
    		mainForm.SPOUSE_BIRTHDAY.disabled=false;
    		mainForm.SPOUSE_TAX_ID.disabled=false;
    		mainForm.SPOUSE_NAME.disabled=false;
    		
    		mainForm.SPOUSE_TYPE[0].disabled=false;
    		mainForm.SPOUSE_TYPE[1].disabled=false;
    		mainForm.SPOUSE_TYPE[2].disabled=false;
    		mainForm.SPOUSE_TYPE[3].disabled=false;
    		if(mainForm.SPOUSE_TYPE[0].checked==true)
    		{
    			mainForm.SPOUSE_TYPE1[0].disabled=false;
	    		mainForm.SPOUSE_TYPE1[1].disabled=false;
	    		mainForm.SPOUSE_TYPE1[2].disabled=false;
    		}
    		else
    		{
	    		mainForm.SPOUSE_TYPE1[0].disabled=true;
	    		mainForm.SPOUSE_TYPE1[1].disabled=true;
	    		mainForm.SPOUSE_TYPE1[2].disabled=true;
	    	}
	    	//จำนวนบุตร
    		//mainForm.c03_01.disabled=false;
    		//mainForm.c03_03.disabled=false;
    		
			mainForm.c04_08.disabled=false;
			mainForm.c04_09.disabled=false;
			mainForm.c06_08.disabled=false;
			mainForm.c06_09.disabled=false;
			
    		//เงินได้และเบี้ยประกันสุขภาพ
			mainForm.c04_06.disabled=false;
			mainForm.c04_07.disabled=false;
			mainForm.c06_06.disabled=false;
			mainForm.c06_07.disabled=false;
			
			if(mainForm.SPOUSE_TYPE[1].checked==true)
			{
				mainForm.a08_011.disabled=false;
		    	mainForm.a101.disabled=false;
		    	
		    	mainForm.b011.disabled=false;
		    	mainForm.b021.disabled=false;
		    	mainForm.b031.disabled=false;
		    	mainForm.b061.disabled=false;
		    	//คู่สมรสอายุตั้งแต่ 65 ปีขึ้นไปและมีเงินได้รวมคำนวณ 190,000 บาท
				mainForm.b05.disabled=false;
		    	//เงินได้และเบี้ยประกันสุขภาพ
				mainForm.c04_06.disabled=false;
				mainForm.c04_07.disabled=false;
				mainForm.c06_06.disabled=false;
				mainForm.c06_07.disabled=false;
				
				mainForm.c051.disabled=false;
				mainForm.c06_101.disabled=false;
				//alert("ok");
				var c08 = document.mainForm.elements['c08[]'];
				//alert("c08="+c08.length);
				if(c08.length ==undefined)
				{
					c08.disabled=false;
				}
				else
				{
					for (var i = 0; i < c08.length; i++) 
				    { 
				        c08[i].disabled=false;
				    }
				}
			}
			else
			{
				mainForm.a08_011.disabled=true;
		    	mainForm.a101.disabled=true;
		    	
		    	mainForm.b011.disabled=true;
		    	mainForm.b021.disabled=true;
		    	mainForm.b031.disabled=true;
		    	
		    	mainForm.b061.disabled=true;
		    	//คู่สมรสอายุตั้งแต่ 65 ปีขึ้นไปและมีเงินได้รวมคำนวณ 190,000 บาท
				mainForm.b05.disabled=true;
		    	//เงินได้และเบี้ยประกันสุขภาพ
				mainForm.c04_06.disabled=true;
				mainForm.c04_07.disabled=true;
				mainForm.c06_06.disabled=true;
				mainForm.c06_07.disabled=true;
				
				mainForm.c051.disabled=true;
				mainForm.c06_101.disabled=true;
				//alert("ok");
				var c08 = document.mainForm.elements['c08[]'];
				//alert("c08="+c08.length);
				if(c08.length ==undefined)
				{
					c08.disabled=true;
				}
				else
				{
					for (var i = 0; i < c08.length; i++) 
				    { 
				        c08[i].disabled=true;
				    }
				}
			}
        }
        else 
        {
			mainForm.SPOUSE_NO.disabled=true;
			mainForm.SPOUSE_NO.value="";
    		mainForm.SPOUSE_BIRTHDAY.disabled=true;
    		mainForm.SPOUSE_BIRTHDAY.value="";
    		mainForm.SPOUSE_TAX_ID.disabled=true;
    		mainForm.SPOUSE_TAX_ID.value="";
    		mainForm.SPOUSE_NAME.disabled=true;
    		mainForm.SPOUSE_NAME.value="";
    		
    		mainForm.SPOUSE_TYPE[0].disabled=true;
    		mainForm.SPOUSE_TYPE[1].disabled=true;
    		mainForm.SPOUSE_TYPE[2].disabled=true;
    		mainForm.SPOUSE_TYPE[3].disabled=true;
    		
    		mainForm.SPOUSE_TYPE1[0].disabled=true;
    		mainForm.SPOUSE_TYPE1[1].disabled=true;
    		mainForm.SPOUSE_TYPE1[2].disabled=true;
    		
    		mainForm.SPOUSE_TYPE[0].checked=false;
    		mainForm.SPOUSE_TYPE[1].checked=false;
    		mainForm.SPOUSE_TYPE[2].checked=false;
    		mainForm.SPOUSE_TYPE[3].checked=false;
    		
    		mainForm.SPOUSE_TYPE1[0].checked=false;
    		mainForm.SPOUSE_TYPE1[1].checked=false;
    		mainForm.SPOUSE_TYPE1[2].checked=false;
    		
    		
			//เงินได้และเบี้ยประกันสุขภาพ
			mainForm.c04_06.disabled=true;
			mainForm.c04_07.disabled=true;
			mainForm.c06_06.disabled=true;
			mainForm.c06_07.disabled=true;
			
			mainForm.c04_06.checked=false;
			mainForm.c04_07.checked=false;
			mainForm.c06_06.checked=false;
			mainForm.c06_07.checked=false;
			
			mainForm.c04_08.disabled=true;
			mainForm.c04_09.disabled=true;
			mainForm.c06_08.disabled=true;
			mainForm.c06_09.disabled=true;
			
			mainForm.c04_08.value="";
			mainForm.c04_09.value="";
			mainForm.c04_10.value="";
			mainForm.c06_08.value="";
			mainForm.c06_09.value="";
			mainForm.c06_10.value="";
			
			//รายได้คู่สมรส
			mainForm.c02.value=0;
			//จำนวนบุตร
    		mainForm.c03_01.disabled=true;
    		mainForm.c03_01.value="";
    		mainForm.c03_03.disabled=true;
    		mainForm.c03_03.value="";
    		mainForm.c03_02.value="";
    		mainForm.c03_04.value="";
    	
			
			
        }
        //JsC12();
    }
	function ChType(id)
	{
		//alert("chtype="+id);
		//จำนวนบุตร
    		mainForm.c03_01.disabled=false;
    		mainForm.c03_03.disabled=false;
		if(id==1)//มีเงินได้
		{
			mainForm.SPOUSE_TYPE1[0].disabled=false;
    		mainForm.SPOUSE_TYPE1[1].disabled=false;
    		mainForm.SPOUSE_TYPE1[2].disabled=false;
			mainForm.c02.value=0.00;
			
			mainForm.a08_011.disabled=true;
	    	mainForm.a101.disabled=true;
	    	mainForm.a08_011.value="";
	    	mainForm.a101.value="";
	    	mainForm.a08_021.value="";
	    	
	    	mainForm.b011.disabled=true;
	    	mainForm.b021.disabled=true;
	    	mainForm.b031.disabled=true;
	    	mainForm.b061.disabled=true;
	    	
	    	mainForm.b011.value="";
	    	mainForm.b021.value="";
	    	mainForm.b031.value="";
	    	mainForm.b061.value="";
	    	//คู่สมรสอายุตั้งแต่ 65 ปีขึ้นไปและมีเงินได้รวมคำนวณ 190,000 บาท
			mainForm.b05.disabled=true;
			mainForm.b05.value="";
	    	//เงินได้และเบี้ยประกันสุขภาพ
			mainForm.c04_06.disabled=true;
			mainForm.c04_07.disabled=true;
			mainForm.c06_06.disabled=true;
			mainForm.c06_07.disabled=true;
			
			mainForm.c04_06.value="";
			mainForm.c04_07.value="";
			mainForm.c06_06.value="";
			mainForm.c06_07.value="";
			
			mainForm.c051.disabled=true;
			mainForm.c06_101.disabled=true;
			
			mainForm.c051.value="";
			mainForm.c06_101.value="";
			
			//alert("ok");
			var c08 = document.mainForm.elements['c08[]'];
			//alert("c08="+c08.length);
			if(c08.length ==undefined)
			{
				c08.disabled=true;
				c08.value="";
			}
			else
			{
				for (var i = 0; i < c08.length; i++) 
			    { 
			        c08[i].disabled=true;
			        c08[i].value="";
			    }
			}
			//JsC12();
		}
		else if(id==2)//รวมคำนวณ
		{
			mainForm.SPOUSE_TYPE1[0].disabled=true;
    		mainForm.SPOUSE_TYPE1[1].disabled=true;
    		mainForm.SPOUSE_TYPE1[2].disabled=true;
			mainForm.SPOUSE_TYPE1[0].checked=false;
    		mainForm.SPOUSE_TYPE1[1].checked=false;
    		mainForm.SPOUSE_TYPE1[2].checked=false;
			mainForm.c02.value=30000.00;
			//คู่สมรสอายุตั้งแต่ 65 ปีขึ้นไปและมีเงินได้รวมคำนวณ 190,000 บาท
			mainForm.b05.disabled=false;
			
			mainForm.a08_011.disabled=false;
	    	mainForm.a101.disabled=false;
	    	
	    	mainForm.b011.disabled=false;
	    	mainForm.b021.disabled=false;
	    	mainForm.b031.disabled=false;
	    	mainForm.b061.disabled=false;
	    	//เงินได้และเบี้ยประกันสุขภาพ
			mainForm.c04_06.disabled=false;
			mainForm.c04_07.disabled=false;
			mainForm.c06_06.disabled=false;
			mainForm.c06_07.disabled=false;
			
			mainForm.c051.disabled=false;
			mainForm.c06_101.disabled=false;
			//alert("ok");
			var c08 = document.mainForm.elements['c08[]'];
			//alert("c08="+c08.length);
			if(c08.length ==undefined)
			{
				c08.disabled=false;
			}
			else
			{
				for (var i = 0; i < c08.length; i++) 
			    { 
			        c08[i].disabled=false;
			    }
			}
			//JsC12();
		}
		else if(id==3)//แยกยื่น
		{
			mainForm.SPOUSE_TYPE1[0].disabled=true;
    		mainForm.SPOUSE_TYPE1[1].disabled=true;
    		mainForm.SPOUSE_TYPE1[2].disabled=true;
    		mainForm.SPOUSE_TYPE1[0].checked=false;
    		mainForm.SPOUSE_TYPE1[1].checked=false;
    		mainForm.SPOUSE_TYPE1[2].checked=false;
			
			mainForm.c02.value=0.00;
			//JsC12();
			
			mainForm.a08_011.disabled=true;
	    	mainForm.a101.disabled=true;
	    	mainForm.a08_011.value="";
	    	mainForm.a101.value="";
	    	mainForm.a08_021.value="";
	    	
	    	mainForm.b011.disabled=true;
	    	mainForm.b021.disabled=true;
	    	mainForm.b031.disabled=true;
	    	mainForm.b061.disabled=true;
	    	
	    	mainForm.b011.value="";
	    	mainForm.b021.value="";
	    	mainForm.b031.value="";
	    	mainForm.b061.value="";
	    	//คู่สมรสอายุตั้งแต่ 65 ปีขึ้นไปและมีเงินได้รวมคำนวณ 190,000 บาท
			mainForm.b05.disabled=true;
			mainForm.b05.value="";
	    	//เงินได้และเบี้ยประกันสุขภาพ
			mainForm.c04_06.disabled=true;
			mainForm.c04_07.disabled=true;
			mainForm.c06_06.disabled=true;
			mainForm.c06_07.disabled=true;
			
			mainForm.c04_06.value="";
			mainForm.c04_07.value="";
			mainForm.c06_06.value="";
			mainForm.c06_07.value="";
			
			mainForm.c051.disabled=true;
			mainForm.c06_101.disabled=true;
			
			mainForm.c051.value="";
			mainForm.c06_101.value="";
			
			//alert("ok");
			var c08 = document.mainForm.elements['c08[]'];
			//alert("c08="+c08.length);
			if(c08.length ==undefined)
			{
				c08.disabled=true;
				c08.value="";
			}
			else
			{
				for (var i = 0; i < c08.length; i++) 
			    { 
			        c08[i].disabled=true;
			        c08[i].value="";
			    }
			}
		}
		else if(id==4)//ไม่มีเงินได้
		{
			mainForm.SPOUSE_TYPE1[0].disabled=true;
    		mainForm.SPOUSE_TYPE1[1].disabled=true;
    		mainForm.SPOUSE_TYPE1[2].disabled=true;
    		mainForm.SPOUSE_TYPE1[0].checked=false;
    		mainForm.SPOUSE_TYPE1[1].checked=false;
    		mainForm.SPOUSE_TYPE1[2].checked=false;
			
			mainForm.c02.value=30000.00;
			
			mainForm.a08_011.disabled=true;
	    	mainForm.a101.disabled=true;
	    	mainForm.a08_011.value="";
	    	mainForm.a101.value="";
	    	mainForm.a08_021.value="";
	    	
	    	mainForm.b011.disabled=true;
	    	mainForm.b021.disabled=true;
	    	mainForm.b031.disabled=true;
	    	mainForm.b061.disabled=true;
	    	
	    	mainForm.b011.value="";
	    	mainForm.b021.value="";
	    	mainForm.b031.value="";
	    	mainForm.b061.value="";
	    	//คู่สมรสอายุตั้งแต่ 65 ปีขึ้นไปและมีเงินได้รวมคำนวณ 190,000 บาท
			mainForm.b05.disabled=true;
			mainForm.b05.value="";
	    	//เงินได้และเบี้ยประกันสุขภาพ
			mainForm.c04_06.disabled=true;
			mainForm.c04_07.disabled=true;
			mainForm.c06_06.disabled=true;
			mainForm.c06_07.disabled=true;
			
			mainForm.c04_06.value="";
			mainForm.c04_07.value="";
			mainForm.c06_06.value="";
			mainForm.c06_07.value="";
			
			mainForm.c051.disabled=true;
			mainForm.c06_101.disabled=true;
			
			mainForm.c051.value="";
			mainForm.c06_101.value="";
			
			//alert("ok");
			var c08 = document.mainForm.elements['c08[]'];
			//alert("c08="+c08.length);
			if(c08.length ==undefined)
			{
				c08.disabled=true;
				c08.value="";
			}
			else
			{
				for (var i = 0; i < c08.length; i++) 
			    { 
			        c08[i].disabled=true;
			        c08[i].value="";
			    }
			}
			
		}	
		//JsC12();
		JsC03_01();	
		JsC03_03();
		
	}
    

    function Js08(){
        var ma_def = 10;//
        //var a07 = SelectElement('a07'); 
		var a08_01 = SelectElement('a08_01');
		var a08_02 = SelectElement('a08_02');
		//if(a07.value=="") { a07.value=0; }
		if(a08_01.value=="") { a08_01.value=0; }
		if(a08_02.value=="") { a08_02.value=0; }
		//var ma_chk = (parseFloat(a07.value) * ma_def) / 100;
		
		a08_02.value = (parseFloat(a08_01.value) * 2).toFixed(2);
		//if(parseFloat(a08_02.value) > ma_chk) a08_02.value = ma_chk.toFixed(2);	
		//if(parseFloat(a08_02.value)< 0){a08_02.value=0;}
		//----
		//Js10();	
    }
  function Js081(){
        var ma_def = 10;//
        //var a07 = SelectElement('a07'); 
		var a08_011 = SelectElement('a08_011');
		var a08_021 = SelectElement('a08_021');
		//if(a07.value=="") { a07.value=0; }
		if(a08_011.value=="") { a08_011.value=0; }
		if(a08_021.value=="") { a08_021.value=0; }
		//var ma_chk = (parseFloat(a07.value) * ma_def) / 100;
		
		a08_021.value = (parseFloat(a08_011.value) * 2).toFixed(2);
		//if(parseFloat(a08_02.value) > ma_chk) a08_02.value = ma_chk.toFixed(2);	
		//if(parseFloat(a08_02.value)< 0){a08_02.value=0;}
		//----
		//Js10();	
    }
    function JsB06(){//ok
		var a = SelectElement('b01').value;
		var b = SelectElement('b02').value;
		var c = SelectElement('b03').value;
		var d = SelectElement('b04').value;
		var e = SelectElement('b05').value;
		var f = SelectElement('b06').value;

				
		if(a=='') a = 0;
		if(b=='') b = 0;
		if(c=='') c = 0;
		if(d=='') d = 0;
		if(e=='') e = 0;
		if(f=='') f = 0;
		
		var g = SelectElement('b07');
		g.value = ( parseFloat(a) + parseFloat(b) + parseFloat(c) + parseFloat(d) + parseFloat(e) + parseFloat(f) ).toFixed(2);
		if(parseFloat(g.value)<0) { g.value=0;}
		//SelectElement('a02').value = g.value;
		//Js01();
    }
    
	function JsB061(){//ok
		var a = SelectElement('b011').value;
		var b = SelectElement('b021').value;
		var c = SelectElement('b031').value;
		var d = SelectElement('b051').value;
		var f = SelectElement('b061').value;

				
		if(a=='') a = 0;
		if(b=='') b = 0;
		if(c=='') c = 0;
		if(f=='') f = 0;
		if(d=='') d = 0;
		
		var g = SelectElement('b071');
		g.value = ( parseFloat(a) + parseFloat(b) + parseFloat(c) + parseFloat(d) + parseFloat(f) ).toFixed(2);
		if(parseFloat(g.value)<0) { g.value=0;}
		//SelectElement('a02').value = g.value;
		//Js01();
    }
    function JsC03(el){//ok
    	var a = SelectElement('c03_01');
    	var b = SelectElement('c03_03');
    	var c = SelectElement('c03_02');
    	var d = SelectElement('c03_04');
        var x = 0;
        var y = 0;
        if(a.value == '' ){
            x = 0;
        }else{
            x = parseFloat(a.value);
        }
        if(b.value == '' ){
            y = 0;
        }else{
            y = parseFloat(b.value);
        }        
        if((x+y) > 3){
            alert('ลดหย่อนบุตรทั้งหมดได้ไม่เกิน 3 คน');
            //SelectElement(el).value = '';
            SelectElement(el).focus();   
            SelectElement('c03_01').value="";   
            SelectElement('c03_03').value="";
            SelectElement('c03_02').value="";   
            SelectElement('c03_04').value="";                           
            return false;
        }
		else
		{
			return true;
		}
    }
    function JsC03_01(){//ok
        var e = SelectElement('c03_01');
        var a = SelectElement('c03_02');
        var z = 0;
        if(e.value == '' ){
            z = 0;
        }else{
            z = parseFloat(e.value);
        }
        if(JsC03('c03_01'))
		{
			if(mainForm.SPOUSE_TYPE[0].checked==true || mainForm.SPOUSE_TYPE1[0].checked==true || mainForm.SPOUSE_TYPE1[1].checked==true || mainForm.SPOUSE_TYPE1[2].checked==true || mainForm.SPOUSE_TYPE[2].checked==true)
			{
				a.value = (7500*z).toFixed(2);   
			}
			else
			{
				a.value = (15000*z).toFixed(2);
			}
			//JsC12(); 
		}
    }

    function JsC03_03(){//ok
        var e = SelectElement('c03_03');
        var a = SelectElement('c03_04');
        var z = 0;
        if(e.value == '' ){
            z = 0;
        }else{
            z = parseFloat(e.value);
        }
        if(JsC03('c03_03'))
		{
			if(mainForm.SPOUSE_TYPE[0].checked==true || mainForm.SPOUSE_TYPE1[0].checked==true || mainForm.SPOUSE_TYPE1[1].checked==true || mainForm.SPOUSE_TYPE1[2].checked==true || mainForm.SPOUSE_TYPE[2].checked==true)
			{
				a.value = (8500*z).toFixed(2);   
			}
			else
			{
				a.value = (17000*z).toFixed(2);
			}
			//a.value = (17000*z).toFixed(2);
			//JsC12();
		}
    }
    
    function JsINT(obj){        
        if(obj.value != null){
	        if(obj.value == ''){
	            return parseFloat('0');
	        }else{
	            return parseFloat(obj.value);
	        }
        }else{
            return parseFloat('0');
        }
    }
    
  	//__________________    
    function JsC04_01(){//ok
    	if(SelectElement('c04_01').checked == true){
        	SelectElement('c04_03').readOnly = false;
        }else{
        	SelectElement('c04_03').readOnly = true; 
        }  
    	JsC04_05();       	
    }

    function JsC04_02(){//ok
    	if(SelectElement('c04_02').checked == true){
        	SelectElement('c04_04').readOnly = false;
        }else{
        	SelectElement('c04_04').readOnly = true; 
        } 
    	JsC04_05();      
    }
    
    function JsC04_05(){//ok
        var e = SelectElement('c04_05');
    	if(SelectElement('c04_01').checked == true && SelectElement('c04_02').checked == true){
        	e.value = (CONTS_04*2).toFixed(2);
    	}else if(SelectElement('c04_02').checked == true){
    		e.value = (CONTS_04).toFixed(2);
    	}else if(SelectElement('c04_01').checked == true){
    		e.value = (CONTS_04).toFixed(2);
    	}else{
    		e.value = 0.00;
    	}
    	//JsC12();
    }
    
    function JsC04_06(){//ok
    	if(SelectElement('c04_06').checked == true){
        	SelectElement('c04_08').readOnly = false;
        }else{
        	SelectElement('c04_08').readOnly = true; 
        }  
    	JsC04_10();       	
    }

    function JsC04_07(){//ok
    	if(SelectElement('c04_07').checked == true){
        	SelectElement('c04_09').readOnly = false;
        }else{
        	SelectElement('c04_09').readOnly = true; 
        } 
    	JsC04_10();      
    }
    
    function JsC04_10(){//ok
        var e = SelectElement('c04_10');
    	if(SelectElement('c04_06').checked == true && SelectElement('c04_07').checked == true){
        	e.value = (CONTS_04*2).toFixed(2);
    	}else if(SelectElement('c04_06').checked == true){
    		e.value = (CONTS_04).toFixed(2);
    	}else if(SelectElement('c04_07').checked == true){
    		e.value = (CONTS_04).toFixed(2);
    	}else{
    		e.value = 0.00;
    	}
    	//JsC12();
    }

    //__________________
    function JsC05_01(){//ok
    	if(SelectElement('c06_01').checked == true){
        	SelectElement('c06_03').readOnly = false;
        }else{
        	SelectElement('c06_03').readOnly = true; 
        }
    	JsC05_10();          
    }

    function JsC05_02(){//ok
    	if(SelectElement('c06_02').checked == true){
        	SelectElement('c06_04').readOnly = false;
        }else{
        	SelectElement('c06_04').readOnly = true; 
        }   
    	JsC05_10();      
    }

 /*   function JsC05_05(){
        if(SelectElement('c06_01').checked == true || SelectElement('c06_02').checked == true){
        	SelectElement('c06_05').readOnly = false;
        }else{
        	SelectElement('c06_05').value = '';
        	SelectElement('c06_05').readOnly = true;
        }
        JsC12();
    }*/
    
    function JsC05_06(){//ok
    	if(SelectElement('c06_06').checked == true){
        	SelectElement('c06_08').readOnly = false;
        }else{
        	SelectElement('c06_08').readOnly = true; 
        }  
    	JsC05_10();       
    }

    function JsC05_07(){//ok
    	if(SelectElement('c06_07').checked == true){
        	SelectElement('c06_09').readOnly = false;
        }else{
        	SelectElement('c06_09').readOnly = true;  
        }  
    	JsC05_10();       
    }

    function JsC05_10(){
        if(SelectElement('c06_01').checked == true || SelectElement('c06_02').checked == true || SelectElement('c06_06').checked == true || SelectElement('c06_07').checked == true){
        	SelectElement('c06_10').readOnly = false;
        }else{
        	SelectElement('c06_10').value = '';
        	SelectElement('c06_10').readOnly = true;
        }        
        //JsC12();
    }

   
    function computeTax(){//ข้อที่ 12
    	
        //alert(SelectElement('a12').value);
        //alert("goto compute tax");
        AJAX_Send_Request();        
    }
    
	function c07()
	{
		var c07 = document.mainForm.elements['c07[]'];
		var c09 = document.mainForm.elements['c09[]'];
		var c10 = document.mainForm.elements['c10[]'];
		//alert("c08="+c08.length);
		if(c09.length ==undefined)
		{
			if(parseFloat(c10.value) !=0)
			{
				if(parseFloat(c07.value) > parseFloat(c10.value))
				{
					alert("กรอกจำนวนเงินได้ไม่เกิน "+c10.value);
					c07.focus();
				}
			}
		}
		else
		{
			for (var i = 0; i < c09.length; i++) 
		    { 
			    if(parseFloat(c10[i].value) !=0)
				{
			        if(parseFloat(c07[i].value) > parseFloat(c10[i].value))
					{
						alert("กรอกจำนวนเงินได้ไม่เกิน "+c10[i].value);
						c07[i].focus();
					}
				}
		    }
		}
	}
	function c08()
	{
		var c08 = document.mainForm.elements['c08[]'];
		var c09 = document.mainForm.elements['c09[]'];
		var c10 = document.mainForm.elements['c10[]'];
		//alert("c08="+c08.length);
		if(c09.length ==undefined)
		{
			if(parseFloat(c10.value) !=0)
			{
				if(parseFloat(c08.value) > parseFloat(c10.value))
				{
					alert("กรอกจำนวนเงินได้คู่สมรสได้ไม่เกิน "+c10.value);
					c08.focus();
				}
			}
		}
		else
		{
			for (var i = 0; i < c09.length; i++) 
		    { 
				if( parseFloat(c10[i].value) !=0)
				{
					if(parseFloat(c08[i].value) > parseFloat(c10[i].value))
					{
						alert("กรอกจำนวนเงินได้คู่สมรสได้ไม่เกิน "+c10[i].value);
						c08[i].focus();
					}
				}
		    }
		}
	}
	function styleMNo(id){
	if(id !=2)
		{
			mainForm.SPOUSE_NO.disabled=true;
			mainForm.SPOUSE_TAX_ID.disabled=true;
			mainForm.SPOUSE_BIRTHDAY.disabled=true;
			mainForm.SPOUSE_NAME.disabled=true;
			
			mainForm.SPOUSE_TYPE[0].disabled=true;
	    	mainForm.SPOUSE_TYPE[1].disabled=true;
	    	mainForm.SPOUSE_TYPE[2].disabled=true;
	    	mainForm.SPOUSE_TYPE[3].disabled=true;
	
			mainForm.SPOUSE_TYPE1[0].disabled=true;
	    	mainForm.SPOUSE_TYPE1[1].disabled=true;
	    	mainForm.SPOUSE_TYPE1[2].disabled=true;
	    	
	    	mainForm.a08_011.disabled=true;
	    	mainForm.a101.disabled=true;
	    	
	    	mainForm.b011.disabled=true;
	    	mainForm.b021.disabled=true;
	    	mainForm.b031.disabled=true;
	    	mainForm.b061.disabled=true;
	    	//คู่สมรสอายุตั้งแต่ 65 ปีขึ้นไปและมีเงินได้รวมคำนวณ 190,000 บาท
			mainForm.b05.disabled=true;
	    	//จำนวนบุตร
	    	mainForm.c03_01.disabled=true;
	    	mainForm.c03_03.disabled=true;
	    	//เงินได้และเบี้ยประกันสุขภาพ
			mainForm.c04_06.disabled=true;
			mainForm.c04_07.disabled=true;
			mainForm.c06_06.disabled=true;
			mainForm.c06_07.disabled=true;
			
			mainForm.c051.disabled=true;
			mainForm.c06_101.disabled=true;
			//alert("ok");
			var c08 = document.mainForm.elements['c08[]'];
			//alert("c08="+c08.length);
			if(c08.length ==undefined)
			{
				c08.disabled=true;
			}
			else
			{
				for (var i = 0; i < c08.length; i++) 
			    { 
			        c08[i].disabled=true;
			    }
			}
			
		}
		else
		{
			SelectMaritalStatus(id);
		}
		//button
    	//mainForm.bt_save.disabled=true;
    	//mainForm.bt_print.disabled=true;	
		//JsC12();
	}


	
    function JsOnLoad(id){
    //alert("id="+id);
    //alert("dddddddddddddddddddddddddd");
       // SelectElement('c01').value = CONTS_C01; 
        //SelectElement('a01').value = 0;       
        styleMNo(id);
        //JsC12();
        //Add_tax();
		    

    }
	//---- ให้คีย์ได้แต่ตัวเลข
	function IsNumericKeyPress()
	{
		if(event.keyCode ==46)
		{
			event.returnValue = true;
		}
		else if (((event.keyCode < 48) || (event.keyCode > 57)))
			{
				event.returnValue = false;
			}
	}
    //Ajax functions
    function AJAX_Send_Request() {
     	var target = "../../ProcessTexComputeSvrl?mDr=" +  SelectElement('a11').value ;
     	//alert("target="+target);
     	AJAX_Request(target, AJAX_Handle_Response);
    }
    
    function AJAX_Handle_Response() {              
        if (AJAX_IsComplete()) {
        	//alert("test 11");
            var xmlDoc = AJAX.responseXML;                    
            if (xmlDoc.getElementsByTagName("SUCCESS")[0] == null) {
                alert("Exception in update process");
                return;
            }

            //alert("current row loop : " + currentRowID);
            if (xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue == "0") {
            	//SelectElement('a12').value = xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue;
            	alert('Compute error!');
            }
            else {
           // alert("OKKKKKKKK");
            	SelectElement('a12').value = xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue;
            	//SelectElement('a14').value = SelectElement('a12').value - SelectElement('a13').value;
            	//alert("12="+SelectElement('a12').value);
            	//Js15();
            }                    
        }
        else{
        //alert("no complete");
        }
    }
</script>        
    <style type="text/css">
<!--
.style3 {font-size: xx-small}
-->
    </style>
    </head><!--onload="JsOnLoad();"-->
 <%
 	String java_load="";
 	 if(MODE==DBMgr.MODE_UPDATE)
	 {
	 	java_load=DBMgr.getRecordValue(tax91Rec,"DOCTOR_STATUS");
	 }
 	 else
 	 {
 		java_load=DBMgr.getRecordValue(tax91Rec,"DOCTOR_STATUS");
 	 }
	 System.out.println("java_load="+java_load);
    %>
	<body onload="JsOnLoad('<%=java_load%>');">
        <form id="mainForm" name="mainForm" method="post" >
		<input type="hidden" name="MODE" id="MODE" value="<%=MODE %>"/>
		<input type="hidden" name="OLD_MM" id="OLD_MM" value="<%= DBMgr.getRecordValue(tax91Rec, "MM") %>"/>
		<input type="hidden" name="OLD_YYYY" id="OLD_YYYY" value="<%= DBMgr.getRecordValue(tax91Rec, "YYYY") %>"/>
		<input type="hidden" name="DoctorProfileCode" id="DoctorProfileCode" value="<%=session.getAttribute("USER_ID").toString() %>"/>
        
        <table width="800" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="" class="main_data_free_width">
            <tr>
              <td width="50%"><table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="">
                <tr>
                  <td width="60%" height="25" align="center" ><table width="100%" border="1" cellspacing="1" cellpadding="1">
                    <tr>
                      <td height="32" colspan="5" align="left"><strong>Doctor Code</strong> <span class="input">
                        <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" onkeypress="return CODE_KeyPress(event);" readonly/>
                        <input id="SEARCH_CODE" name="SEARCH_CODE" type="image" class="image_button" src="../../images/search_button_profile.png" alt="Search" onclick="doctorOnSearch(<%=session.getAttribute("USER_GROUP_CODE").toString() %>); return false;" />
                        </span>
                          <input name="DOCTOR_NAME" type="text" id="DOCTOR_NAME" value="<%=DBMgr.getRecordValue(doctorRec, "NAME_THAI")%>" size="50"  readonly="readonly"/>
                       </td>
                    </tr>
                    
                    <tr>
                      <td align="left"  class="data"><strong>เดือน/ปี</strong><%=MODE==DBMgr.MODE_INSERT ? current_mm+"/"+current_year : DBMgr.getRecordValue(tax91Rec, "MM")+"/"+DBMgr.getRecordValue(tax91Rec, "YYYY")  %></td>
                      <td colspan="4" align="left"  class="data"><strong>สถานะการใช้งาน</strong>
                        <input type="radio" name="STATUS" id="STATUS" value="1" <%= DBMgr.getRecordValue(tax91Rec, "STATUS").equalsIgnoreCase("1") ? " checked=\"checked\"" : MODE==DBMgr.MODE_INSERT ? " checked=\"checked\"" : ""%>/>
                        <span class="form"> ใช้งาน &nbsp;&nbsp;&nbsp;
                        <input type="radio" name="STATUS" id="STATUS"  value="0" <%= DBMgr.getRecordValue(tax91Rec, "STATUS").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%>/>
ไม่ใช้งาน &nbsp;&nbsp;&nbsp;</span></td>
                    </tr>
                    <% String doctor_status=DBMgr.getRecordValue(tax91Rec, "DOCTOR_STATUS");
									  	//out.println("doctor_status="+doctor_status);
									  	String status1="", status2="", status3="", status4="";
									  	
									  if(doctor_status.equals("1"))  		{  		status1="checked";		  }
									  else if(doctor_status.equals("2"))	{  		status2="checked";		  }
									  else if(doctor_status.equals("3"))	{  		status3="checked";		  }
									  else if(doctor_status.equals("4"))	{  		status4="checked";		  }
									  %>
                    <tr>
                      <td colspan="5" align="left"  class="data">สถานภาพ
                        <input type="radio" name="DOCTOR_STATUS" id="DOCTOR_STATUS" value="1" onclick="SelectMaritalStatus(1)" <%=status1%>/>
                          <span class="form"> โสด &nbsp;&nbsp;&nbsp;
                          <input type="radio" name="DOCTOR_STATUS" id="DOCTOR_STATUS"  value="2" onclick="SelectMaritalStatus(2)" <%=status2%>/>
                            สมรส &nbsp;&nbsp;&nbsp;
                            <input type="radio" name="DOCTOR_STATUS" id="DOCTOR_STATUS"  value="3" onclick="SelectMaritalStatus(3)" <%=status3%>/>
                            หม้าย &nbsp;&nbsp;&nbsp;
                            <input type="radio" name="DOCTOR_STATUS"  id="DOCTOR_STATUS" value="4" onclick="SelectMaritalStatus(4)" <%=status4%>/>
                            ตายระหว่างปีภาษี </span></td>
                    </tr>
                    <tr>
                      <td align="left"><%
                                          String st011="",st012="",st013="",st014="";
                                    	  String st021="",st022="",st023="";
                                    	  String st01=DBMgr.getRecordValue(tax91Rec, "SPOUSE_TYPE");
                                          String st02=DBMgr.getRecordValue(tax91Rec, "SPOUSE_TYPE1");
                                          if(st01.equals("1")) 
                                          { 
                                        	  st011="checked";
                                        	  if(st02.equals("1")) {st021="checked"; }
                                        	  else if(st02.equals("2")) {st022="checked";}
                                        	  else if(st02.equals("3")) {st023="checked";}
                                          }
                                          else if(st01.equals("2")) {st012="checked"; }
                                          else if(st01.equals("3")) {st013="checked"; }
                                          else if(st01.equals("4")) {st014="checked"; }
                                     %>
                          <br />
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td colspan="3">ประเภทเงินได้ </td>
                            </tr>
                            <tr>
                              <td colspan="3"><input name="SPOUSE_TYPE" type="radio" value="1" id="radio3" onclick="ChType(1)" <%=st011 %> />
                                (1) มีเงินได้ แต่ </td>
                            </tr>
                            <tr>
                              <td width="6%">&nbsp;</td>
                              <td colspan="2"><input name="SPOUSE_TYPE1" type="radio" value="1"  id="SPOUSE_TYPE1" <%=st021 %>/>
                                สมรสระหว่างปีภาษี</td>
                            </tr>
                            <tr>
                              <td>&nbsp;</td>
                              <td colspan="2"><input name="SPOUSE_TYPE1" type="radio" value="2"  id="radio" <%=st022 %>/>
                                หย่าระหว่างปีภาษี</td>
                            </tr>
                            <tr>
                              <td>&nbsp;</td>
                              <td colspan="2"><input name="SPOUSE_TYPE1" type="radio" value="3"  id="radio2" <%=st023 %>/>
                                ตายระหว่างปีภาษี</td>
                            </tr>
                            <tr>
                              <td colspan="3"><input name="SPOUSE_TYPE" type="radio" value="2"  id="radio4" onclick="ChType(2)" <%=st012 %> />
                                (2) มีเงินได้รวมคำนวณภาษี</td>
                            </tr>
                            <tr>
                              <td colspan="3"><input name="SPOUSE_TYPE" type="radio" value="3"  id="radio5" onclick="ChType(3)" <%=st013 %> />
                                (3) มีเงินได้แยกยื่นแบบฯ</td>
                            </tr>
                            <tr>
                              <td colspan="3"><input name="SPOUSE_TYPE" type="radio" value="4"  id="SPOUSE_TYPE" onclick="ChType(4)" <%=st014 %> />
                                (4) ไม่มีเงินได้ </td>
                            </tr>
                        </table></td>
                      <td colspan="4" align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td colspan="3" align="center" bordercolor="#99CCFF" bgcolor="#99CCFF" ><strong>คู่สมรส</strong></td>
                          </tr>
                          <tr>
                            <td bordercolor="#FFFFFF">เลขประจำตัวประชาชน</td>
                            <td bordercolor="#FFFFFF"><input name="SPOUSE_NO" type="text" id="SPOUSE_NO" size="15" maxlength="13" value="<%=DBMgr.getRecordValue(tax91Rec, "SPOUSE_NO")%>"  onkeypress="IsNumericKeyPress();"/></td>
                          </tr>
                          <tr>
                            <td bordercolor="#FFFFFF">เลขประจำตัวผู้เสียภาษีอากร</td>
                            <td bordercolor="#FFFFFF"><input name="SPOUSE_TAX_ID" type="text" id="SPOUSE_TAX_ID" size="15" maxlength="10" value="<%=DBMgr.getRecordValue(tax91Rec, "SPOUSE_TAX_NO")%>"  onkeypress="IsNumericKeyPress();" />
                                <br />
                                <span class="style3">(กรอกเฉพาะกรณีเป็นผู้ไม่มีเลขประจำตัวประชาชน)</span> </td>
                          </tr>
                          <tr>
                            <td bordercolor="#FFFFFF">วันเดือนปีเกิด</td>
                            <td bordercolor="#FFFFFF"><input name="SPOUSE_BIRTHDAY" type="text" class="short" id="SPOUSE_BIRTHDAY" maxlength="10" value="<%=JDate.showDate(DBMgr.getRecordValue(tax91Rec, "SPOUSE_BIRTHDAY"))%>" />
                                <input name="image2" type="image" class="image_button" onclick="displayDatePicker('SPOUSE_BIRTHDAY'); return false;" src="../../images/calendar_button.png" alt=""/></td>
                          </tr>
                          <tr>
                            <td bordercolor="#FFFFFF">ชื่อ - นามสกุล </td>
                            <td bordercolor="#FFFFFF"><input name="SPOUSE_NAME" type="text" id="SPOUSE_NAME" value="<%=DBMgr.getRecordValue(tax91Rec, "SPOUSE_NAME")%>"/></td>
                          </tr>
                      </table></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="center"  bgcolor="#99CCFF"><strong>รายการ</strong></td>
                      <td colspan="2" align="center" bgcolor="#99CCFF"><strong>ผู้มีเงินได้</strong></td>
                      <td colspan="2" align="center" bgcolor="#99CCFF"><strong>คู่สมรส</strong></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td width="42%" align="left">1. <strong>หัก</strong> เงินบริจาคสนับสนุนทางการศึกษา</td>
                      <td width="26%" colspan="2" align="left"><input type="text" name="a08_01"  id="a08_01" value="" class="short alignRight" onblur="Js08()"  onkeypress="IsNumericKeyPress();" maxlength="8"/></td>
                      <td width="32%" colspan="2" align="left"><input type="text" name="a08_011"  id="a08_011" value="" class="short alignRight" onblur="Js081()"  onkeypress="IsNumericKeyPress();" maxlength="8"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left">(2 เท่าของจำนวนเงินที่ได้จ่ายไปจริง ) </td>
                      <td colspan="2" align="left"><input type="text" name="a08_02"  id="a08_02" value="<%=DBMgr.getRecordValue(tax91Rec, "A08")%>" class="short alignRight" readonly="readonly"/></td>
                      <td colspan="2" align="left"><input type="text" name="a08_021"  id="a08_021" value="<%=DBMgr.getRecordValue(tax91Rec, "A081")%>" class="short alignRight" readonly="readonly"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left">2. <strong>หัก</strong> เงินบริจาค</td>
                      <td colspan="2" align="left"><input name="a10" type="text" class="short alignRight" id="a10"  value="<%=DBMgr.getRecordValue(tax91Rec, "A10")%>"  onkeypress="IsNumericKeyPress();" maxlength="8"/></td>
                      <td colspan="2" align="left"><input name="a101" type="text" class="short alignRight" id="a101"  value="<%=DBMgr.getRecordValue(tax91Rec, "A101")%>"  onkeypress="IsNumericKeyPress();" maxlength="8"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <th height="30" colspan="5" align="left" bgcolor="#99CCFF">3. รายการเงินได้ที่ได้รับยกเว้น</th>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left">3.1.เงินสะสม<strong>กองทุนสำรองเลี้ยงชีพ</strong> (ส่วนที่เกิน 10,000 บาท ) </td>
                      <td colspan="2" align="left"><input name="b01" type="text" class="short alignRight" id="b01" onblur="JsB06()" value="<%=DBMgr.getRecordValue(tax91Rec,"B01")%>"   onkeypress="IsNumericKeyPress();" readonly="readonly" maxlength="8"/></td>
                      <td colspan="2" align="left"><input name="b011" type="text" class="short alignRight" id="b011" onblur="JsB061()" value="<%=DBMgr.getRecordValue(tax91Rec,"B011")%>"   onkeypress="IsNumericKeyPress();" readonly="readonly" maxlength="8"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left">3.2.เงินสะสม <strong>กบข.</strong></td>
                      <td colspan="2" align="left"><input name="b02" type="text" class="short alignRight" id="b02" onblur="JsB06()" value="<%=DBMgr.getRecordValue(tax91Rec,"B02")%>"   onkeypress="IsNumericKeyPress();" readonly="readonly" maxlength="8"/></td>
                      <td colspan="2" align="left"><input name="b021" type="text" class="short alignRight" id="b021" onblur="JsB061()" value="<%=DBMgr.getRecordValue(tax91Rec,"B021")%>"   onkeypress="IsNumericKeyPress();" readonly="readonly" maxlength="8"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left">3.3.เงินสะสม<strong>กองทุนสงเคราะห์ครูโรงเรียนเอกชน</strong></td>
                      <td colspan="2" align="left"><input name="b03" type="text" class="short alignRight" id="b03" onblur="JsB06()" value="<%=DBMgr.getRecordValue(tax91Rec,"B03")%>"   onkeypress="IsNumericKeyPress();" readonly="readonly" maxlength="8"/></td>
                      <td colspan="2" align="left"><input name="b031" type="text" class="short alignRight" id="b031" onblur="JsB061()" value="<%=DBMgr.getRecordValue(tax91Rec,"B031")%>"   onkeypress="IsNumericKeyPress();" readonly="readonly" maxlength="8"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left">3.4. <strong>ผู้มีเงินได้อายุตั้งแต่ 65 ปีขึ้นไป 190,000  บาท </strong></td>
                      <td colspan="2" align="left"><input name="b04" type="text" class="short alignRight" id="b04" onblur="JsB06()" value="<%=DBMgr.getRecordValue(tax91Rec,"B04")%>"   onkeypress="IsNumericKeyPress();" maxlength="8"/></td>
                      <td colspan="2" align="left">&nbsp;</td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left">3.5. <strong>คู่สมรสอายุตั้งแต่ 65 ปีขึ้นไปและมีเงินได้รวมคำนวณ 190,000 บาท </strong></td>
                      <td colspan="2" align="left">&nbsp;</td>
                      <td colspan="2" align="left"><input name="b05" type="text" class="short alignRight" id="b05" onblur="JsB061()" value="<%=DBMgr.getRecordValue(tax91Rec,"B05")%>"  onkeypress="IsNumericKeyPress();"  maxlength="8"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left">3.6.เงินค่าชดเชยที่ได้รับตามกฏหมายแรงงาน<br />
                          <span class="style3">(กรณีนำมารวมคำนวณภาษี)</span></td>
                      <td colspan="2" align="left"><input name="b06" type="text" class="short alignRight" id="b06" onblur="JsB06()" value="<%=DBMgr.getRecordValue(tax91Rec,"B06")%>"  onkeypress="IsNumericKeyPress();"  readonly="readonly" maxlength="8"/></td>
                      <td colspan="2" align="left"><input name="b061" type="text" class="short alignRight" id="b061" onblur="JsB061()" value="<%=DBMgr.getRecordValue(tax91Rec,"B061")%>"  onkeypress="IsNumericKeyPress();"  readonly="readonly" maxlength="8"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left">3.7.รวม</td>
                      <td colspan="2" align="left"><input name="b07" type="text" class="short alignRight" id="b07" value="<%=DBMgr.getRecordValue(tax91Rec,"B07")%>" readonly="readonly"/></td>
                      <td colspan="2" align="left"><input name="b071" type="text" class="short alignRight" id="b071" value="<%=DBMgr.getRecordValue(tax91Rec,"B071")%>" readonly="readonly"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <th height="30" colspan="5" align="left" bgcolor="#99CCFF">4.รายการลดหย่อนและยกเว้นหลังจากหักค่าใช้จ่าย</th>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left">4.1.ผู้มีเงินได้</td>
                      <td colspan="2" align="left"><input name="c01" type="text" class="short alignRight" id="c01" value="<%=showC01 %>" readonly="readonly"/></td>
                      <td colspan="2" align="left">&nbsp;</td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left">4.2.คู่สมรส(30,000 บาท กรณีมีเงินได้รวมคำนวณภาษีหรือไม่มีเงินได้)</td>
                      <td colspan="2" align="left">&nbsp;</td>
                      <td colspan="2" align="left"><input name="c02" type="text" class="short alignRight" id="c02" value="<%=DBMgr.getRecordValue(tax91Rec,"C02")%>" readonly="readonly"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left">4.3.บุตรคนละ 15,000 บาท
                        <input type="text" name="c03_01"  id="c03_01" value="<%=DBMgr.getRecordValue(tax91Rec,"C03_01")%>" class="alignRight" size="3" onblur="JsC03_01()" onkeypress="IsNumericKeyPress();"  onkeyup="JsC03_01();"/>
                        คน</td>
                      <td colspan="2" align="left"><input name="c03_02" type="text" class="short alignRight" id="c03_02" value="<%=DBMgr.getRecordValue(tax91Rec,"C03_02")%>" readonly="readonly"/></td>
                      <td colspan="2" align="left">&nbsp;</td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left">&nbsp;&nbsp;บุตรคนละ 17,000 บาท
                        <input name="c03_03" type="text" class="alignRight" id="c03_03" onblur="JsC03_03()" value="<%=DBMgr.getRecordValue(tax91Rec,"C03_03")%>" size="3" onkeyup="JsC03_03();"  onkeypress="IsNumericKeyPress();"/>
                        คน</td>
                      <td colspan="2" align="left"><input name="c03_04" type="text" class="short alignRight" id="c03_04" value="<%=DBMgr.getRecordValue(tax91Rec,"C03_04")%>" readonly="readonly"/></td>
                      <td colspan="2" align="left">&nbsp;</td>
                    </tr>
                    <%
				  double c04031=0,c04041=0,c04081=0,c04091=0;
				  String c0403		=DBMgr.getRecordValue(tax91Rec,"C04_01");
				  if(DBMgr.getRecordValue(tax91Rec,"C04_02") !="")
				  {
				  	c04031		=Double.parseDouble(DBMgr.getRecordValue(tax91Rec,"C04_02"));
				  }
				  if(DBMgr.getRecordValue(tax91Rec,"C04_04") !="")
				  {
					  c04041	=Double.parseDouble(DBMgr.getRecordValue(tax91Rec,"C04_04"));
				  }
				  if(DBMgr.getRecordValue(tax91Rec,"C04_06") !="")
				  {
					  c04081	=Double.parseDouble(DBMgr.getRecordValue(tax91Rec,"C04_06"));
				  }
				  if(DBMgr.getRecordValue(tax91Rec,"C04_08") !="")
				  {
					  c04091	=Double.parseDouble(DBMgr.getRecordValue(tax91Rec,"C04_08"));
				  }
				  String c0404		=DBMgr.getRecordValue(tax91Rec,"C04_03");
				  String c0408		=DBMgr.getRecordValue(tax91Rec,"C04_05");
				  String c0409		=DBMgr.getRecordValue(tax91Rec,"C04_07");
				  
				  
				  String c0401="", c0402="", c0406="", c0407="";
				  double c0405=0, c0410=0;
				  if(!c0403.equals("")) { c0401="checked";}
				  if(!c0404.equals("")) { c0402="checked";}
				  if(!c0408.equals("")) { c0406="checked";}
				  if(!c0409.equals("")) { c0407="checked";}
					c0405=c04031+c04041;
					c0410= c04081+c04091;
				  %>
                    <tr bgcolor="#FFFFFF">
                      <td rowspan="3" align="left"> 4.4. ค่าดูแลบิดาและมารดา                          (ให้กรอกเลขประจำตัวประชาชน)&nbsp;&nbsp;&nbsp;</td>
                      <td height="25" align="left"><input type="checkbox" name="c04_01"   id="c04_01"  value="" onclick="JsC04_01();" <%=c0401%>/>
                        บิดา</td>
                      <td align="left"><input name="c04_03" type="text"   id="c04_03" class="short alignRight" value="<%=c0403%>" maxlength="13" readonly="readonly"   onkeypress="IsNumericKeyPress();"/></td>
                      <td align="left"><input type="checkbox" name="c04_06" id="c04_06" value=""  onclick="JsC04_06();" <%=c0406%>/>
                        บิดา                        </td>
                      <td align="left"><input name="c04_08" type="text"   id="c04_08" class="short alignRight" value="<%=c0408%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td align="left"><input type="checkbox" name="c04_02" id="c04_02" value="" onclick="JsC04_02();" <%=c0402%>/>
                        มารดา                        </td>
                      <td align="left"><input name="c04_04" type="text"   id="c04_04" class="short alignRight" value="<%=c0404%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/></td>
                      <td align="left"><input type="checkbox" name="c04_07" id="c04_07" value=""  onclick="JsC04_07();" <%=c0407%>/>
                        มารดา                        </td>
                      <td align="left"><input name="c04_09" type="text"   id="c04_09" class="short alignRight" value="<%=c0409%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td colspan="2" align="left"><input type="text" name="c04_05"   id="c04_05" value="<%=c0405%>" class="short alignRight" readonly="readonly"/></td>
                      <td colspan="2" align="left"><input type="text" name="c04_10"   id="c04_10"  value="<%=c0410%>" class="short alignRight" readonly="readonly"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td height="25" align="left">4.5. อุปการะเลี้ยงดูคนพิการหรือคนทุพพลภาพ </td>
                      <td colspan="2" align="left"><input name="c05" type="text" class="short alignRight" id="c05" " value="<%=DBMgr.getRecordValue(tax91Rec,"C05")%>"  onkeypress="IsNumericKeyPress();" maxlength="8"/></td>
                      <td colspan="2" align="left"><input name="c051" type="text" class="short alignRight" id="c051" " value="<%=DBMgr.getRecordValue(tax91Rec,"C051")%>"  onkeypress="IsNumericKeyPress();" maxlength="8"/></td>
                    </tr>
                    <%
				  String c0603=DBMgr.getRecordValue(tax91Rec,"C06_01");
				  String c0604=DBMgr.getRecordValue(tax91Rec,"C06_02");
				  String c0608=DBMgr.getRecordValue(tax91Rec,"C06_03");
				  String c0609=DBMgr.getRecordValue(tax91Rec,"C06_04");
				  String c0601="", c0602="", c0605="", c0606="";
				  if(!c0603.equals("")) { c0601="checked";}
				  if(!c0604.equals("")) { c0602="checked";}
				  if(!c0608.equals("")) { c0605="checked";}
				  if(!c0609.equals("")) { c0606="checked";}
					 
				  %>
                    <tr bgcolor="#FFFFFF">
                      <td rowspan="3" align="left"> 4.6.
                        
                        เบี้ยประกันสุขภาพบิดามารดา</td>
                      <td height="25" align="left"><input name="c06_01" type="checkbox" id="c06_01" onclick="JsC05_01();" value="" <%=c0601%>/>
                        บิดา                        </td>
                      <td height="25" align="left"><input name="c06_03" type="text" class="short alignRight" id="c06_03" value="<%=c0603%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/></td>
                      <td align="left"><input name="c06_06" type="checkbox" id="c06_06" onclick="JsC05_06();" value="" <%=c0605%>/>
                        บิดา                        </td>
                      <td align="left"><input name="c06_08" type="text" class="short alignRight" id="c06_08" value="<%=c0608%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td height="25" align="left"><input name="c06_02" type="checkbox" id="c06_02" onclick="JsC05_02();" value="" <%=c0602%>/>
                        มารดา                        </td>
                      <td height="25" align="left"><input name="c06_04" type="text" class="short alignRight" id="c06_04" value="<%=c0604%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/></td>
                      <td align="left"><input name="c06_07" type="checkbox" id="c06_07" onclick="JsC05_07();" value="" <%=c0606%>/>
                        มารดา                        </td>
                      <td align="left"><input name="c06_09" type="text" class="short alignRight" id="c06_09" value="<%=c0609%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/></td>
                    </tr>
                    <tr bgcolor="#FFFFFF">
                      <td colspan="2" align="left"><input type="text" name="c06_10"   id="c06_10"  value="<%=DBMgr.getRecordValue(tax91Rec,"C06_05")%>" class="short alignRight"  onkeypress="IsNumericKeyPress();" maxlength="8"/></td>
                      <td colspan="2" align="left"><input type="text" name="c06_101"   id="c06_101"  value="<%=DBMgr.getRecordValue(tax91Rec,"C06_051")%>" class="short alignRight"   onkeypress="IsNumericKeyPress();" maxlength="8"/></td>
                    </tr>
                        <%

                       
            String sql="SELECT CODE, MASTER_REDUCE_NAME, MASTER_REDUCE_MAX_AMOUNT ";
            sql+=" FROM STP_MASTER_TAX_REDUCE ";
            sql+=" WHERE YYYY='"+getYYYY+"' AND HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND STATUS='1' ";
            sql+=" ORDER BY CODE";
            System.out.println("sql="+sql);
            //String query="SELECT * FROM STP_GROUP_DOCTOR_CATEGORY ";
            ResultSet rs = con.executeQuery(sql);
            
            int num = 7;
            int MasterReduceCode=0;
            while (rs.next()) 
            {
            	String sqlAmount="";
            	String [][]arrData=null;
            	int Total=0;
            	double amountReduce=0;
            	MasterReduceCode=Integer.parseInt(rs.getString("CODE"));
            	amountReduce=Double.parseDouble(rs.getString("MASTER_REDUCE_MAX_AMOUNT"));
            	//System.out.println("MasterReduceCode="+MasterReduceCode);      
            	//System.out.println("YYYY="++" MM="+);
            	sqlAmount="SELECT AMOUNT_1, AMOUNT_2 FROM STP_TAX_REDUCE_DETAIL ";
            	if(MODE==DBMgr.MODE_INSERT)
            	{
            		sqlAmount+=" WHERE YYYY='"+DBMgr.getRecordValue(tax91Rec,"YYYY")+"' AND MM='"+DBMgr.getRecordValue(tax91Rec,"MM")+"' ";
            	}
            	else
            	{
        			sqlAmount+=" WHERE YYYY='"+getYYYY+"' AND MM='"+getMM+"' ";
            	}
            	if(request.getParameter("DOCTOR_CODE") !=null)
            	{
            		//String doctorCode=request.getParameter("DOCTOR_CODE").toString();
            		sqlAmount+=" AND DOCTOR_CODE='"+doctorCode+"'";		
            	}
            	else
            	{
            		sqlAmount+=" AND DOCTOR_CODE='0'";
            	}
            	sqlAmount+=" AND MASTER_REDUCE_CODE="+MasterReduceCode;
            	System.out.println("sqlData= "+sqlAmount);
                arrData = conData.query(sqlAmount);
                Total=arrData.length;
                System.out.println("Total="+arrData.length);
                double amount_1=0, amount_2=0;
                
                if(Total != 0)
                {
                	amount_1=Double.parseDouble(arrData[0][0]);
                    amount_2=Double.parseDouble(arrData[0][1]);
                }
                 %>
                <input type="hidden" name="c09[]" value="<%=MasterReduceCode%>">
                <input type="hidden" name="c10[]" value="<%=amountReduce%>">
                <tr bgcolor="#FFFFFF">
                      <td align="left">4.<%=num %>. <%=Util.formatHTMLString(rs.getString("MASTER_REDUCE_NAME"), true)%> </td>
                      <td colspan="2" align="left"><input name="c07[]" type="text" class="short alignRight" id="c07[]" value="<%=amount_1%>" onkeypress="IsNumericKeyPress();" maxlength="8" onblur="c07();"/></td>
                      <td colspan="2" align="left"><input name="c08[]" type="text" class="short alignRight" id="c08[]" value="<%=amount_2%>"  onkeypress="IsNumericKeyPress();" maxlength="8" onblur="c08();"/></td>
                  </tr>              
                	 
                <%
                num++;
                //System.out.println("num="+num);
            }
               %>   
                    
                    
                  </table></td>
                </tr>
              </table></td>
            </tr>
            <tr>
                <td align="center" height="28">
                
                 	<input type="button" name="bt_save" id="bt_save" value="บันทึก" onClick="SAVE_Click()" />
                 	<input type="reset" name="bt_reset" id="bt_reset" value="ล้าง"/>
                    <input type="button" name="bt_close" id="bt_close" value="ปิดหน้าจอ" onClick="window.location='../tax/stp_tax_reduce_main.jsp'"/>                </td>
            </tr>
        </table>

        </form>
	</body>
</html>