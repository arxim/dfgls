<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="java.sql.Types"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.db.conn.DBConn"%>

<%    if (session.getAttribute("LANG_CODE") == null)
        session.setAttribute("LANG_CODE", LabelMap.LANG_EN);

    DBConn conData = new DBConn();
    conData.setStatement();

    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
    labelMap.add("TITLE_MAIN", "Tax Reduce Detail", "รายละเอียดรายการค่าลดหย่อนภาษี");
    labelMap.add("YYYY", "Year", "ปี");
    labelMap.add("REDUCE_DESCRIPTION", "Reduce Description", "ชื่อรายการค่าลดหย่อน");
    labelMap.add("AMOUNT", "Amount", "จำนวนเงิน");
    labelMap.add("PERCENT", "Percent", "จำนวนเปอร์เซ็นต์");
	labelMap.add("ACTIVE", "Status", "สถานะ");
    labelMap.add("ACTIVE_0", "Inactive", "ไม่ใช้งาน");
    labelMap.add("ACTIVE_1", "Active", "ใช้งาน");
    labelMap.add("MSG_NOT_SAVE", "This data is in system", "รายการลดหย่อนนี้มีอยู่ในระบบแล้ว");
    labelMap.add("MSG_PERCENT", "Please,fill percent less than 100", "จำนวนเปอร์เซ็นต์ ต้องไม่เกิน 100%");

    request.setAttribute("labelMap", labelMap.getHashMap());

    request.setCharacterEncoding("UTF-8");
    byte MODE = DBMgr.MODE_INSERT;
    DataRecord stpReduceRec = null;
    String YYYY = "";
    double amount=0, percent=0;
    ProcessUtil util = new ProcessUtil();

    if (request.getParameter("MODE") != null) {
        // Insert or update

        MODE = Byte.parseByte(request.getParameter("MODE"));

        stpReduceRec = new DataRecord("STP_MASTER_TAX_REDUCE");

        //, , , , , , , ,
        String sql_tax="";
        if(request.getParameter("YYYY") !=null)
        {
        	YYYY=request.getParameter("YYYY");
        }
        if(request.getParameter("MASTER_REDUCE_MAX_AMOUNT") !=null)
        {
        	amount=Double.parseDouble(request.getParameter("MASTER_REDUCE_MAX_AMOUNT"));
        }
        if(request.getParameter("MASTER_REDUCE_MAX_AMOUNT") !=null)
        {
        	percent=Double.parseDouble(request.getParameter("MASTER_REDUCE_MAX_PERCENT"));
        }
        if(MODE == DBMgr.MODE_INSERT)
        {
        	sql_tax="INSERT INTO STP_MASTER_TAX_REDUCE (YYYY, HOSPITAL_CODE, MASTER_REDUCE_NAME, "
        			+"MASTER_REDUCE_MAX_AMOUNT, MASTER_REDUCE_MAX_PERCENT, STATUS, "
        			+"UPDATE_ID, UPDATE_DATE, UPDATE_TIME)"
        			+"VALUES('"+YYYY+"', '"+session.getAttribute("HOSPITAL_CODE").toString()+"', '"+request.getParameter("REDUCE_DESCRIPTION")+"'"
        			+", "+amount+", "+percent+", '"+request.getParameter("ACTIVE")+"' "
        			+", '"+session.getAttribute("USER_ID").toString()+"', '"+JDate.getDate()+"', '"+JDate.getTime()+"')";
        	System.out.println("insert="+sql_tax);
        	try
        	{
        		conData.insert(sql_tax);
        		conData.commitDB();
        		session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/tax_reduce_main.jsp?YYYY=%1$s", YYYY)));
            }
        	catch(Exception e)
        	{
        		System.out.println("Insert Tax Reduce Master Error : "+e);
        		conData.rollDB();
        		session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
        	} 
        }
        else if (MODE == DBMgr.MODE_UPDATE) 
        {
        	sql_tax="UPDATE STP_MASTER_TAX_REDUCE SET "
        		+"YYYY='"+YYYY+"',"
        		+"HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"',"
        		+"MASTER_REDUCE_NAME='"+request.getParameter("REDUCE_DESCRIPTION")+"', "
    			+"MASTER_REDUCE_MAX_AMOUNT="+amount+", "
    			+"MASTER_REDUCE_MAX_PERCENT="+percent+", "
    			+"STATUS='"+request.getParameter("ACTIVE")+"', "
    			+"UPDATE_ID='"+session.getAttribute("USER_ID").toString()+"', "
    			+"UPDATE_DATE='"+JDate.getDate()+"', "
    			+"UPDATE_TIME='"+JDate.getTime()+"'"
    			+" WHERE CODE="+request.getParameter("CODE");
        	System.out.println("update="+sql_tax);
    			
        	try
        	{
        		conData.insert(sql_tax);
        		conData.commitDB();
        		session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/tax_reduce_main.jsp?YYYY=%1$s", YYYY)));
            }
        	catch(Exception e)
        	{
        		conData.rollDB();
        		session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
        	}
        	
        }

        response.sendRedirect("../message.jsp");
        return;
    }
    else if (request.getParameter("CODE") != null ) {
        // Edit
        MODE = DBMgr.MODE_UPDATE;
        String query = String.format("SELECT * FROM STP_MASTER_TAX_REDUCE WHERE HOSPITAL_CODE = '%1$s' AND CODE = %2$s " 
                , session.getAttribute("HOSPITAL_CODE")
                , request.getParameter("CODE"));
		System.out.println("\n query="+query);
        stpReduceRec = DBMgr.getRecord(query);

        YYYY = DBMgr.getRecordValue(stpReduceRec, "YYYY");
	}
    else {
        // New
        MODE = DBMgr.MODE_INSERT;
        YYYY = JDate.getYear();;
    }
    /*else { 
        response.sendRedirect("../message.jsp");
    }*/
    System.out.println("\n MODE="+MODE);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript">
            
            //---------------------
            function AJAX_VerifyData() {
            var target;
            //alert("5555555555555");
                target = "../../RetrieveData?TABLE=STP_MASTER_TAX_REDUCE&COND=HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'"
               		+ " AND MASTER_REDUCE_NAME='" + document.mainForm.REDUCE_DESCRIPTION.value+ "'" 
                    + " AND YYYY='" + document.mainForm.YYYY.value + "'";
                
               //alert("target="+target);
                AJAX_Request(target, AJAX_Handle_VerifyData);
            }

            function AJAX_Handle_VerifyData() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist = isXMLNodeExist(xmlDoc, "MASTER_REDUCE_NAME");
                    //alert("beExist="+beExist);
                    //alert("value="+getXMLNodeValue(xmlDoc, "MASTER_REDUCE_NAME"));
                    if(document.mainForm.MODE.value == "<%=DBMgr.MODE_INSERT%>")
                    {
                    		if (beExist) //พบข้อมูล
                    		{
                                //alert("พบข้อมูล insert false");
                                alert("<%=labelMap.get("MSG_NOT_SAVE")%>");
                            }
                            else //ไม่พบข้อมูล
                            {
                                //alert("ไม่พบข้อมูล insert true");
                                document.mainForm.submit();
                            }
                     }
                     else if(document.mainForm.MODE.value == "<%=DBMgr.MODE_UPDATE%>")
                     { 
                        if (beExist) //พบข้อมูล
                        {
                        	//alert("code_db="+getXMLNodeValue(xmlDoc, "CODE"));
                        	//alert("code="+document.mainForm.CODE.value);
                        	if(getXMLNodeValue(xmlDoc, "CODE")==document.mainForm.CODE.value)//มี code เหมือนกับข้อมูลที่กำลังทำการปรับปรุง
                            {
                            	//alert("พบข้อมูล update true");
                            	document.mainForm.submit();
                            }
                            else // code คนละตัวกับที่ปรับปรุง
                            {
                            	alert("<%=labelMap.get("MSG_NOT_SAVE")%>");
                            }
                        }
                        else //ไม่พบข้อมูล
                        {
                            //alert("ไม่พบข้อมูล");
                            document.mainForm.submit();
                        }
                      }
                    }
                }
            


            function validateData()
            {
            	//alert("ddddddddddddd");
                var e = document.mainForm;
                if(!isObjectEmptyString(e.REDUCE_DESCRIPTION, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                !isObjectEmptyString(e.YYYY, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>'))
                {
                	//alert("wwwwwwwwwwwwwwwwww");
                	
                   if(e.MASTER_REDUCE_MAX_PERCENT.value > 100)
                   {
                   		alert("<%=labelMap.get("MSG_PERCENT")%>");
                   }
                   else
                   {
                       	AJAX_VerifyData();
                   }
                   
                }
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
    
            
        </script>
 <style type="text/css">
<!--
.style1 {color: #003399}
-->
        </style>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="tax_reduce_detail.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= MODE %>" />
            <% if(MODE == DBMgr.MODE_UPDATE) { %>
            <input type="hidden" id="CODE" name="CODE" value="<%=request.getParameter("CODE")%>" />
            <% } %>
            <table class="form">
                <tr><th colspan="4">${labelMap.TITLE_MAIN}</th></tr>
                <tr>
                    <td width="214" class="label">
                  <label><span class="style1">${labelMap.YYYY} * </span></label>                     </td>
                  <td width="574" colspan="3" class="input"><%=util.selectYY("YYYY", YYYY)%> </td>
                </tr>
				<tr>
                    <td class="label">
                        <label for="REDUCE_DESCRIPTION"><span class="style1">${labelMap.REDUCE_DESCRIPTION} *</span></label>                    </td>
                    <td class="input" colspan="3"><input name="REDUCE_DESCRIPTION" type="text" id="REDUCE_DESCRIPTION" value="<%= DBMgr.getRecordValue(stpReduceRec, "MASTER_REDUCE_NAME") %>" size="50" maxlength="255" /></td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="MASTER_REDUCE_MAX_AMOUNT">${labelMap.AMOUNT}</label>                    </td>
                    <td class="input" colspan="3"><input name="MASTER_REDUCE_MAX_AMOUNT" type="text" class="short" id="MASTER_REDUCE_MAX_AMOUNT" value="<%= DBMgr.getRecordValue(stpReduceRec, "MASTER_REDUCE_MAX_AMOUNT") %>" size="10" maxlength="7" onkeypress="IsNumericKeyPress();"/></td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="MASTER_REDUCE_MAX_PERCENT">${labelMap.PERCENT}</label>                    </td>
                    <td colspan="3" class="input" ><input name="MASTER_REDUCE_MAX_PERCENT" type="text" class="short" id="MASTER_REDUCE_MAX_PERCENT" value="<%= DBMgr.getRecordValue(stpReduceRec, "MASTER_REDUCE_MAX_PERCENT") %>" size="5" maxlength="3" onkeypress="IsNumericKeyPress();"/>
                      %</td>
				</tr>
                <tr>
                  <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                  <td colspan="3" class="input"><input type="radio" id="ACTIVE_1" name="ACTIVE" value="1" <%= DBMgr.getRecordValue(stpReduceRec, "STATUS").equalsIgnoreCase("1") || DBMgr.getRecordValue(stpReduceRec, "STATUS").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
                      <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                      <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0" <%= DBMgr.getRecordValue(stpReduceRec, "STATUS").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%>/>
                      <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>                  </td>
                </tr>
                
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="validateData();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location.href='tax_reduce_main.jsp?YYYY=<%=YYYY%>'" />                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
