<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="java.sql.*"%>

<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_DISTRIBUTE_REVENUE)) {
                response.sendRedirect("../message.jsp");
                return;
            }

            //
            // Initial LabelMap
            //
            request.setCharacterEncoding("UTF-8");
            DataRecord doctorProfileRec = null, disRec= null;
            String query = "",query_dis="";
            
			byte MODE = DBMgr.MODE_INSERT;
            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            String dt = JDate.showDate(JDate.getDate());
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Distribute Revenue", "การกระจายรายได้แพทย์");
            labelMap.add("LABEL_DOCTOR_PROFILE_CODE", "Doctor Profile Code", "Doctor Profile Code");
            
            labelMap.add("LABEL_DISTRIBUTE_TYPE", "Distribute Type", "ประเภทการกระจายรายได้");
			labelMap.add("DISTRIBUTE_1", "%", "%");
            labelMap.add("DISTRIBUTE_2", "Amount", "จำนวนเงิน");
            
            labelMap.add("LABEL_REVENUE_TYPE", "Revenue Type", "ประเภทรายได้");
            labelMap.add("REVENUE_1", "DF", "DF");
            labelMap.add("REVENUE_2", "Absorb", "Absorb");
            
            labelMap.add("CANCEL_INVOICE","Cancel Invoice","ยกเลิก Invoice");
            labelMap.add("CANCEL_0","Yes","Yes");
            labelMap.add("CANCEL_1","No","No");
            
            labelMap.add("LABEL_DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("LABEL_DOCTOR_NAME", "Doctor Name", "ชื่อคณะบุคคล");
            labelMap.add("LABEL_PERCENT", "%", "%");
            labelMap.add("LABEL_AMOUNT", "Amount", "จำนวนเงิน");
            labelMap.add("TITLE_DATA", "Doctor Detail", "รายละเอียดของแพทย์");
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //
			 if (request.getParameter("MODE") != null) 
			 {

                MODE = Byte.parseByte(request.getParameter("MODE"));
                String insertData="", updateData="", deleteData="",arrDoctor[][]=null, doctorCode="";
                int num_doctor=0;
                DBConnection con = new DBConnection();
				con.connectToLocal();
				
				DBConn conDoctor = new DBConn();
				conDoctor.setStatement();
				if(MODE == DBMgr.MODE_INSERT || MODE == DBMgr.MODE_UPDATE)
				{
					String getDoctorProfileCode=request.getParameter("DOCTOR_PROFILE_CODE");
					String getDistributeType=request.getParameter("DISTRIBUTE");
					String getRevenueType=request.getParameter("REVENUE");
					String getActive=request.getParameter("ACTIVE");
					
					String arrDis[]				=request.getParameterValues("DIS[]");
					String arrDoctorCode[]		=request.getParameterValues("DOCTOR_CODE[]");
					String arrDisPercent[]		=request.getParameterValues("DIS_PERCENT[]");
					String arrDisAmount[]		=request.getParameterValues("DIS_AMOUNT[]");
				
					if (MODE == DBMgr.MODE_UPDATE) 
					{
						deleteData="DELETE FROM STP_DISTRIBUTE_REVENUE WHERE "
				        +" HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"' "
				        +" AND DOCTOR_PROFILE_CODE='"+getDoctorProfileCode+"'";
				        //System.out.println("deleteData="+deleteData);
				        ResultSet rsDelete = con.executeQuery(deleteData);
					}
					String sqlDoctor="SELECT CODE FROM DOCTOR WHERE DOCTOR_PROFILE_CODE='"+request.getParameter("DOCTOR_PROFILE_CODE")+"' AND ACTIVE='1' AND HOSPITAL_CODE  = '"  + session.getAttribute("HOSPITAL_CODE").toString()+ "' ORDER BY CODE ";
					//System.out.println("sqlDoctor= "+sqlDoctor);
					arrDoctor = conDoctor.query(sqlDoctor);
					//System.out.println("arrDoctor="+arrDoctor.length);
					
					for(int nd=0; nd < arrDoctor.length;nd++)
					{
						
						doctorCode=arrDoctor[nd][0];
						
						//String getDis			=arrDis[nd];
						String getDoctorCode	=arrDoctorCode[nd];
						String getDisPercent	=arrDisPercent[nd];
						String getDisAmount		=arrDisAmount[nd];
						if(getDisPercent.equals(""))
						{
							getDisPercent="0";
						}
						if(getDisAmount.equals(""))
						{
							getDisAmount="0";
						}
						
						//System.out.println("============="+nd+"====================");
						//System.out.println("num "+nd+"= "+doctorCode);
						//System.out.println("getDis="+getDis);
						//System.out.println("getDoctorCode="+getDoctorCode);
						//System.out.println("getDisPercent="+getDisPercent);
						//System.out.println("getDisAmount="+getDisAmount);
						
						if(doctorCode.equals(getDoctorCode) && (!getDisPercent.equals("0") || !getDisAmount.equals("0")))
						{
// 							insertData="INSERT INTO STP_DISTRIBUTE_REVENUE (HOSPITAL_CODE, DOCTOR_PROFILE_CODE, "
// 							+" DISTRIBUTE_TYPE, REVENUE_TYPE, DOCTOR_CODE, DISTRIBUTE_PERCENT, DISTRIBUTE_AMOUNT, "
// 							+" ACTIVE, UPDATE_DATE, UPDATE_TIME, USER_ID)"
// 				            +" VALUES('"+session.getAttribute("HOSPITAL_CODE").toString()+"', '"+getDoctorProfileCode+"',"
// 				            +"'"+getDistributeType+"', '"+getRevenueType+"', '"+getDoctorCode+"', "+getDisPercent+", "
// 				            +getDisAmount+",'"+getActive+"','"+JDate.getDate()+"', '"+JDate.getTime()+ "', "
// 				            +"'"+session.getAttribute("USER_ID").toString()+"')"; 
				            
							insertData="INSERT INTO STP_DISTRIBUTE_REVENUE (HOSPITAL_CODE, DOCTOR_PROFILE_CODE, "
							+" DISTRIBUTE_TYPE, REVENUE_TYPE, DOCTOR_CODE, DISTRIBUTE_PERCENT, DISTRIBUTE_AMOUNT, "
							+" ACTIVE, UPDATE_DATE, UPDATE_TIME, USER_ID)"
						    +" VALUES('"+session.getAttribute("HOSPITAL_CODE").toString()+"', '"+getDoctorProfileCode+"',"
						    +"'', '', '"+getDoctorCode+"', "+getDisPercent+", "
						    +getDisAmount+",'"+getActive+"','"+JDate.getDate()+"', '"+JDate.getTime()+ "', "
						    +"'"+session.getAttribute("USER_ID").toString()+"')"; 
							
							
							ResultSet rsInsertData = con.executeQuery(insertData);
							
						}
						
					}
					session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/distribute_revenue.jsp"));
					response.sendRedirect("../message.jsp");
	                return;
				}
			 }
       			
				             
            if (request.getParameter("DOCTOR_PROFILE_CODE") != null) 
            {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM DOCTOR_PROFILE WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + request.getParameter("DOCTOR_PROFILE_CODE") + "'";
                doctorProfileRec = DBMgr.getRecord(query);
                query_dis = "SELECT * FROM STP_DISTRIBUTE_REVENUE WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND DOCTOR_PROFILE_CODE = '" + request.getParameter("DOCTOR_PROFILE_CODE") + "'";
                disRec = DBMgr.getRecord(query_dis);
                //out.println("disRec="+disRec);
                
                if(disRec== null)
                {
                	MODE=DBMgr.MODE_INSERT;
                }
                else
                {
                	MODE=DBMgr.MODE_UPDATE;
                }
                //System.out.println("MODE="+MODE);
            }
            
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />

        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
        <script type="text/javascript">
            
            function DOCTOR_PROFILE_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_PROFILE_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_PROFILE() {
                var target = "../../RetrieveData?TABLE=DOCTOR_PROFILE&COND=CODE='" + document.mainForm.DOCTOR_PROFILE_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_PROFILE);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_PROFILE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DOCTOR_PROFILE_CODE.value = "";
                        document.mainForm.DOCTOR_PROFILE_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_PROFILE_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }    
              function updateAllCheckBox() 
            { 
                for (var i = 0; i < document.mainForm.elements.length; i++) { 
                    var x = document.mainForm.elements[i]; 
                    //alert("x="+x);
                    if (x.type == 'checkbox') { 
                        x.checked = document.mainForm.allCheckBox.checked; 
                    } 
                } 
            } 
            // check ว่ามีการ click ที่ Checkbox หรือไม่
            function chCheckBox()
            {
            	var n=0;
            	for (var i = 0; i < document.mainForm.elements.length; i++) { 
                    var x = document.mainForm.elements[i]; 
                    //alert("x="+x);
                    if (x.type == 'checkbox') 
                    { 
                    	if(x.checked==true)
                    	{
                    		n++;
                    	}
                    	else
                    	{
                    		//don't use
                    	} 
                    } 
                } 
                if(n>0)
                {
                	return true;
                }
                else
                {
                	return false;
                }
            }
            // checkbox ไหนไม่ได้ click ให้ %, Amount=""
            function checkValue()
            {
            	//alert("checkValue");
            	//alert(document.mainForm.elements.length);
            	var dis_check = document.mainForm.elements['DIS[]'];
            	var dis_percent = document.mainForm.elements['DIS_PERCENT[]'];
                var dis_amount = document.mainForm.elements['DIS_AMOUNT[]'];;
                //alert(dis_percent);
                //alert(dis_amount);    
                var num_check=0;
                var num_peramount=0;
            	for (var i = 0; i < dis_check.length; i++) 
            	{ 
                    	if(dis_check[i].checked==false)
                    	{
                    		dis_percent[i].value="";
                    		dis_amount[i].value="";
                    	}
                } 
                
            }
            //check ว่า checkbox ที่เลือกนั้นใส่ข้อมูลแล้วหรือไม่
            function chCheckBoxData()
            {
            	//alert("chCheckBoxData");
            	//alert(document.mainForm.elements.length);
            	var dis_check = document.mainForm.elements['DIS[]'];
            	var dis_percent = document.mainForm.elements['DIS_PERCENT[]'];
                var dis_amount = document.mainForm.elements['DIS_AMOUNT[]'];;
                //alert(dis_percent);
                //alert(dis_amount);    
                var num_check=0;
                var num_peramount=0;
            	for (var i = 0; i < dis_check.length; i++) 
            	{ 
                    //var x = dis_check[i]; 
                    //alert("dis_percent="+dis_percent[i].value);
                    //alert("x="+x);
                    //alert(x.type);
                     
                    	//alert("checkbox dddddddddddddddd");
                    	//alert(document.mainForm.DISTRIBUTE[0].checked);
                    	if(dis_check[i].checked == true)
                    	{
                    		num_check++;
                    		//alert("kkkkkkk");
                    		//alert(dis_percent[i].value);
                    		if(dis_percent[i].value == "" )  // && document.mainForm.DISTRIBUTE[0].checked==true)
                    		{
                    			alert("Please, fill Percent");
                    			dis_percent[i].focus();
                    			return false;
                    		}
                    		else if(dis_amount[i].value == "")  // && document.mainForm.DISTRIBUTE[1].checked==true)
                    		{
                    			alert("Please, fill Amount");
                    			dis_amount[i].focus();
                    			return false;
                    		}
                    		else
                    		{
                    			num_peramount++;
                    		}
                    		
                    	}
                    
                } 
            	
                if(num_check==num_peramount)
                {
                	return true;
                }
                
            }
            //check ว่า ค่าที่ใส่นั้นมีการ checkbox หรือไม่
            function chDataCheckBox()
            {
            	//alert("chCheckBoxData");
            	//alert(document.mainForm.elements.length);
            	var dis_check = document.mainForm.elements['DIS[]'];
            	var dis_percent = document.mainForm.elements['DIS_PERCENT[]'];
                var dis_amount = document.mainForm.elements['DIS_AMOUNT[]'];;
                //alert(dis_percent);
                //alert(dis_percent.length);    
                var num_check=0;
                var num_peramount=0;
            	for (var i = 0; i < dis_percent.length; i++) 
            	{ 
                    //var x = dis_check[i]; 
                  	//alert("dis_percent="+dis_percent[i].value);
                    //alert("dis_amount="+dis_amount[i].value);
                    //alert(x.type);
                     
                    	//alert("checkbox dddddddddddddddd");
                    	//alert(document.mainForm.DISTRIBUTE[0].checked);
                    	if((dis_percent[i].value !="" || dis_amount[i].value !="") && dis_check[i].checked==false)
                    	{
                    		alert("Please, click Checkbox");
                    		dis_check[i].focus();
                    		return false;
                    	}
                } 
                return true;
               
            }
			//==============sum  all percentage================
      	function cal_pct()
      	{
      		//alert("cal_pct");
      			var total=0;
      			var dis_check = document.mainForm.elements['DIS[]'];
            	var dis_percent = document.mainForm.elements['DIS_PERCENT[]'];
                var dis_amount = document.mainForm.elements['DIS_AMOUNT[]'];
                //alert(dis_percent);
                var num_percent = dis_percent.length;
                if(num_percent==undefined)
				{
					total=parseFloat(dis_percent.value);
					//alert(total);
				}  
				else
				{ 
	            	for (var i = 0; i < dis_check.length; i++) 
	            	{ 
	                    var x = document.mainForm.elements[i]; 
	                    //alert("x="+x);
	                    
	                    	if(dis_check[i].checked==true)
	                    	{
	                    		//alert(parseInt(dis_percent[i].value));
	                    		total=total+parseFloat(dis_percent[i].value);
	                    		//alert("total="+total);
	                    		
	                    	}
	                   
	                } 
	            }
                if(total !=100.00)
                {
                	return true;
                }
                else
                {
                	return false;
                }
      	}
			function SAVE_CLICK()
			{
				var num=chCheckBox();
				//alert("num="+num);
				if(num)
				{
					//chCheckBoxData();
					if(chCheckBoxData()&& chDataCheckBox())
					{
						var pct=cal_pct();
						//alert("pct="+pct);
						if(pct)
						{
							alert("Percent <> 100");
							return false;
						}
						else
						{
							document.mainForm.submit();
						}
					}
					
				}
				else
				{
					alert("Please, Click Checkbox");
					return false;
				}
			/*
				if(document.dataForm.RECEIPT_DATE.value == ""){
                                        alert("Receipt Date is current date");
                                        document.dataForm.submit();
					//document.dataForm.RECEIPT_DATE.focus();
				}else{
                                        document.dataForm.submit();
				}*/
			}  
			
        </script>
    </head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="distribute_revenue.jsp">
        <input type="hidden" name="MODE" id="MODE" value="<%=MODE %>"/>
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("distribute_revenue.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
          <table class="form">
                <tr>
				<th colspan="4">
				  <div style="float: left;">
				  ${labelMap.TITLE_MAIN}</div>				</th>
				</tr>
                <tr>
                    <td class="label"><label for="LABEL_DOCTOR_PROFILE_CODE">${labelMap.LABEL_DOCTOR_PROFILE_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_PROFILE_CODE" name="DOCTOR_PROFILE_CODE" class="short" value="<%= DBMgr.getRecordValue(doctorProfileRec, "CODE") %>" onkeypress="return DOCTOR_PROFILE_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_PROFILE();" />
                        <input id="SEARCH_DOCTOR_PROFILE_CODE" name="SEARCH_DOCTOR_PROFILE_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="doctorProfileOnSearch(); return false;" />
                        <input type="text" id="DOCTOR_PROFILE_NAME" name="DOCTOR_PROFILE_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorProfileRec, "NAME") %>" />                    </td>
                </tr>
<!--                 <tr> -->
<%--                   <td class="label"><label for="LABEL_DISTRIBUTE_TYPE">${labelMap.LABEL_DISTRIBUTE_TYPE}</label></td> --%>
<%--                   <td colspan="3" class="input"><input type="radio" id="DISTRIBUTE" name="DISTRIBUTE" value="1" <%= DBMgr.getRecordValue(disRec, "DISTRIBUTE_TYPE").equalsIgnoreCase("1") || DBMgr.getRecordValue(disRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> /> --%>
<%--                     <label for="radio">${labelMap.DISTRIBUTE_1}</label> --%>
<%--                     <input type="radio" id="DISTRIBUTE" name="DISTRIBUTE" value="2"<%= DBMgr.getRecordValue(disRec, "DISTRIBUTE_TYPE").equalsIgnoreCase("2") ? " checked=\"checked\"" : ""%> /> --%>
<%--                     <label for="radio2">${labelMap.DISTRIBUTE_2}</label></td> --%>
<!--                 </tr> -->
<!--                 <tr> -->
<%--                   <td class="label"><label for="LABEL_REVENUE_TYPE">${labelMap.LABEL_REVENUE_TYPE}</label></td> --%>
<%--                   <td colspan="3" class="input"><input type="radio" id="REVENUE" name="REVENUE" value="1" <%= DBMgr.getRecordValue(disRec, "REVENUE_TYPE").equalsIgnoreCase("1") || DBMgr.getRecordValue(disRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> /> --%>
<%--                     <label for="radio3">${labelMap.REVENUE_1}</label> --%>
<%--                     <input type="radio" id="REVENUE" name="REVENUE" value="2"<%= DBMgr.getRecordValue(disRec, "REVENUE_TYPE").equalsIgnoreCase("2") ? " checked=\"checked\"" : ""%> /> --%>
<%--                     <label for="radio4">${labelMap.REVENUE_2}</label></td> --%>
<!--                 </tr> -->
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input"><input type="radio" id="ACTIVE_1" name="ACTIVE" value="1" <%= DBMgr.getRecordValue(disRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(disRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
                      <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                      <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(disRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> />
                      <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label></td>
                </tr>
                <tr>
                    <th colspan="6" class="buttonBar">                        
                        <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="window.location='distribute_revenue.jsp?MODE='+<%=DBMgr.MODE_QUERY %>+'&DOCTOR_PROFILE_CODE=' + document.mainForm.DOCTOR_PROFILE_CODE.value; return false;"/>
                        <input type="button" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='distribute_revenue.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />                    </th>
                </tr>
            </table>
            <hr />
            <table class="data">
                <tr>
                    <!--<th colspan="8" class="alignLeft">${labelMap.TITLE_DATA}</th>-->
					<th colspan="5">
			  	  <div style="float: left;">${labelMap.TITLE_DATA}</div></th>
                </tr>
                <tr>
                    <td class="sub_head"><input type="checkbox" id="allCheckBox" name="allCheckBox" onclick="updateAllCheckBox()" /></td>
                    <td class="sub_head">${labelMap.LABEL_DOCTOR_CODE}</td>
                    <td class="sub_head">${labelMap.LABEL_DOCTOR_NAME}</td>
                    <td class="sub_head">${labelMap.LABEL_PERCENT}</td>
                    <td class="sub_head">${labelMap.LABEL_AMOUNT}</td>
                </tr>
                <%
                if (request.getParameter("MODE") != null) {

                    String cond = "";
                    if (request.getParameter("DOCTOR_PROFILE_CODE") != null && !request.getParameter("DOCTOR_PROFILE_CODE").equalsIgnoreCase("")) {
                        cond += " WHERE DOCTOR_PROFILE_CODE = '" + request.getParameter("DOCTOR_PROFILE_CODE") + "' AND ACTIVE='1' AND HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' ORDER BY CODE ";
                    }
                    
                    DBConnection con = new DBConnection();
                    con.connectToServer();

                    query = "SELECT CODE, NAME_"+labelMap.getFieldLangSuffix() +" AS DOCTOR_NAME FROM DOCTOR " + cond;
                    ResultSet rs = con.executeQuery(query);
                    int i = 0;
                    String dis_percent="",dis_amount="",show_check="";
                    
                    while (rs.next()) {
                    	query_dis = "SELECT * FROM STP_DISTRIBUTE_REVENUE ";
                    	query_dis+= " WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' ";
                    	query_dis+= " AND DOCTOR_CODE = '" + rs.getString("CODE") + "'";
                    	query_dis+= " AND DOCTOR_PROFILE_CODE= '"+request.getParameter("DOCTOR_PROFILE_CODE")+"'";
                        //ResultSet rs_dis = con.executeQuery(query_dis);
                        DataRecord IncomeDisRec = DBMgr.getRecord(String.format(query_dis));
        				dis_percent = DBMgr.getRecordValue(IncomeDisRec, "DISTRIBUTE_PERCENT");
        				dis_amount = DBMgr.getRecordValue(IncomeDisRec, "DISTRIBUTE_AMOUNT");
        				if((dis_percent !="" || dis_amount !="") && (dis_percent !="0" || dis_amount !="0"))
        				{
        					show_check="checked";
        				}
        				else
        				{
        					show_check="";
        				}
                %>                
                <tr>
                    <td class="row<%=i % 2%> alignCenter"><input type="checkbox" id="DIS[]" name="DIS[]" value="<%=rs.getString("CODE") %>" <%=show_check %> onclick="checkValue();"/></td>
                    <td class="row<%=i % 2%>"><input type="textbox" name="DOCTOR_CODE[]" id="DOCTOR_CODE[]" size="10" value="<%=rs.getString("CODE") %>" readonly></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("DOCTOR_NAME"), true)%></td>
                  <td align="center" class="row<%=i % 2%>"><input type="textbox" name="DIS_PERCENT[]" id="DIS_PERCENT[]" size="5" maxlength="6" value="<%=DBMgr.getRecordValue(IncomeDisRec, "DISTRIBUTE_PERCENT") %>" ></td>
					<td align="center" class="row<%=i % 2%>"><input type="textbox" name="DIS_AMOUNT[]" id="DIS_AMOUNT[]" size="10" value="<%=DBMgr.getRecordValue(IncomeDisRec, "DISTRIBUTE_AMOUNT") %>" ></td>
                    
                </tr>
                <%
                        i++;
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    con.freeConnection();
                }
                %>                
                <tr>
                    <th colspan="5" class="buttonBar">                        
                        <input type="button" id="BT_SAVE" name="BT_SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_CLICK();"/>                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
<script language="javascript">
	function doctorProfileOnSearch(){
		//var y = document.getElementById('YYYY');
		//var m = document.getElementById('MM');
		openSearchForm('../search.jsp?TABLE=DOCTOR_PROFILE&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_PROFILE_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_PROFILE');	
	}
</script>
