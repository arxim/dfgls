<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.Types"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.conn.DBConnection"%>
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
//out.println("session="+session);
//out.println("page="+Guard.PAGE_INPUT_GROUP_DOCTOR_CATEGORY);
            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_GROUP_ORDER_ITEM_CATEGORY)) {
                response.sendRedirect("../message.jsp");
                return;
            }

//
// Initial LabelMap
//

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Group Prorate Doctor", "Group Prorate Doctor");
            labelMap.add("LABEL_GROUP_CODE", "Group Code", "รหัสกลุ่ม");
            labelMap.add("LABEL_DOCTOR_CATEGORY", "Doctor Category Code", "รหัสกลุ่มแพทย์");
            labelMap.add("LABEL_DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("LABEL_TYPE", "Type", "ประเภท");
           
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            
            byte MODE = DBMgr.MODE_INSERT;
            DataRecord prorateRec = null, doctorCategoryRec = null, groupRec=null, doctorRec=null;
            DataRecord stpDelete = null;
            String MM = "", YYYY = "";
            String select_type="";
            System.out.println("GROUP_CODE="+request.getParameter("GROUP_CODE"));
            System.out.println("DOCTOR_CATEGORY_CODE="+request.getParameter("DOCTOR_CATEGORY_CODE"));
            System.out.println("DOCTOR_CODE="+request.getParameter("DOCTOR_CODE"));
                
            //out.println("group_code="+request.getParameter("GROUP_CODE"));
            //out.println("doctor_category_code="+request.getParameter("DOCTOR_CATEGORY_CODE"));
            
            if (request.getParameter("MODE") != null) {
                // Insert or update
                MODE = Byte.parseByte(request.getParameter("MODE"));

                prorateRec = new DataRecord("STP_PRORATE_DOCTOR");
                
                //, , , , , , , , 
                prorateRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                prorateRec.addField("GROUP_CODE", Types.VARCHAR, request.getParameter("GROUP_CODE"), true);
                prorateRec.addField("DOCTOR_CATEGORY_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CATEGORY_CODE"), true);
                prorateRec.addField("DOCTOR_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CODE"),true);
                prorateRec.addField("TYPE", Types.VARCHAR, request.getParameter("SEL_TYPE"));
                                
                prorateRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                prorateRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                prorateRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                prorateRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

                if(1==1){
                	out.println(request.getParameter("GROUP_CODE"));
                	//return;
                }
                if (MODE == DBMgr.MODE_INSERT) {
                    if (DBMgr.insertRecord(prorateRec)) {
                        
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/group_prorate_main.jsp?GROUP_CODE=%1$s&SEL_TYPE=%2$S", prorateRec.getField("GROUP_CODE").getValue(),prorateRec.getField("SEL_TYPE").getValue())));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } 
                else if (MODE == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(prorateRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/group_prorate_main.jsp?GROUP_CODE=%1$s&SEL_TYPE=%2$S", prorateRec.getField("GROUP_CODE").getValue(),prorateRec.getField("SEL_TYPE").getValue())));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            }
            
            else if (request.getParameter("GROUP_CODE") != null && request.getParameter("DOCTOR_CATEGORY_CODE") != null && request.getParameter("DOCTOR_CODE") != null) {
                // Edit
                MODE = DBMgr.MODE_UPDATE;
                //out.println("mode="+MODE);
                //String query = String.format("SELECT CODE AS CODE,DOCTOR_CATEGORY_CODE FROM STP_GROUP WHERE HOSPITAL_CODE = '%1$s' AND GROUP_CODE = '%2$s' AND DOCTOR_CATEGORY_CODE = '%3$s' ", session.getAttribute("HOSPITAL_CODE"), request.getParameter("GROUP_CODE"), request.getParameter("DOCTOR_CATEGORY_CODE"));
                //out.println(query);
                groupRec = DBMgr.getRecord(String.format("SELECT CODE, GROUP_NAME_%1$s AS NAME FROM STP_GROUP WHERE CODE = '%2$s'", labelMap.getFieldLangSuffix(), request.getParameter("GROUP_CODE")));
                prorateRec = DBMgr.getRecord(String.format("SELECT * FROM STP_PRORATE_DOCTOR WHERE GROUP_CODE = '%1$s' AND DOCTOR_CATEGORY_CODE='%2$s' AND DOCTOR_CODE='%3$s' ", request.getParameter("GROUP_CODE"),request.getParameter("DOCTOR_CATEGORY_CODE"),request.getParameter("DOCTOR_CODE")));
                doctorCategoryRec = DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION FROM DOCTOR_CATEGORY WHERE CODE = '%1$s'", request.getParameter( "DOCTOR_CATEGORY_CODE")));
				doctorRec = DBMgr.getRecord(String.format("SELECT CODE, NAME_%1$s FROM DOCTOR WHERE CODE = '%2$s'", labelMap.getFieldLangSuffix(),request.getParameter( "DOCTOR_CODE")));

                }
            else if (request.getParameter("GROUP_CODE") != null ) {
                // New
                MODE = DBMgr.MODE_INSERT;
                //MM = request.getParameter("MM");
                //YYYY = request.getParameter("YYYY");
                groupRec = DBMgr.getRecord(String.format("SELECT CODE, GROUP_NAME_%1$s AS NAME FROM STP_GROUP WHERE CODE = '%2$s'", labelMap.getFieldLangSuffix(), request.getParameter("GROUP_CODE")));
            	select_type=request.getParameter("SEL_TYPE");
            }
            else {
                response.sendRedirect("../message.jsp");
            }
            //out.println(DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_TYPE_CODE"));
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
        <script type="text/javascript">
            
            function GROUP_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.GROUP_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_GROUP() {
                var target = "../../RetrieveData?TABLE=STP_GROUP&COND=CODE='" + document.mainForm.GROUP_CODE.value +"'";
                AJAX_Request(target, AJAX_Handle_Refresh_GROUP);
            }
            
            function AJAX_Handle_Refresh_GROUP() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.GROUP_CODE.value = "";
                        document.mainForm.GROUP_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.GROUP_NAME.value = getXMLNodeValue(xmlDoc, "GROUP_NAME_"+"<%=labelMap.getFieldLangSuffix()%>");
                }
            }

            //GUARANTEE_LOCATION
            //GUARANTEE_LOCATION_CODE_KeyPress
           
            function DOCTOR_CATEGORY_CODE_KeyPress(e) {

                 var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CATEGORY_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_CATEGORY() {
                var target = "../../RetrieveData?TABLE=DOCTOR_CATEGORY&COND=CODE='" + document.mainForm.DOCTOR_CATEGORY_CODE.value +"'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_CATEGORY);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_CATEGORY() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //alert("AJAX_Handle_Refresh_GUARANTEE_LOCATION");
                        document.mainForm.DOCTOR_CATEGORY_CODE.value = "";
                        document.mainForm.DOCTOR_CATEGORY_NAME.value = "";
                        document.mainForm.DOCTOR_CODE.disabled=true;
                        document.mainForm.DOCTOR_NAME.disabled=true;
                        document.mainForm.DOCTOR_CODE.value="";
                        document.mainForm.DOCTOR_NAME.value="";
                        document.getElementById('SEARCH_DOCTOR_CODE').disabled=true;;
                        return;
                    }
                    
                    // Data found
                    //alert("dd="+getXMLNodeValue(xmlDoc, "DESCRIPTION"));
                    document.mainForm.DOCTOR_CATEGORY_NAME.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                    document.mainForm.DOCTOR_CODE.disabled=false;
                    document.mainForm.DOCTOR_NAME.disabled=false;
                    document.mainForm.DOCTOR_CODE.value="";
                    document.mainForm.DOCTOR_NAME.value="";
                    document.getElementById('SEARCH_DOCTOR_CODE').disabled=false;
                }
            }     
			
             function DOCTOR_CODE_KeyPress(e) {

                 var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_CODE() {
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_CODE);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_CODE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //alert("AJAX_Handle_Refresh_GUARANTEE_LOCATION");
                        document.mainForm.DOCTOR_CODE.value = "";
                        document.mainForm.DOCTOR_NAME.value = "";
                        
                        return;
                    }
                    
                    // Data found
                    //alert("dd="+getXMLNodeValue(xmlDoc, "DESCRIPTION"));
                    document.mainForm.DOCTOR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_"+"<%=labelMap.getFieldLangSuffix()%>");
                    //alert(document.mainForm.ORDER_ITEM_CATEGORY_NAME.value);
                }
            }       
            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=STP_PRORATE_DOCTOR&COND=HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'"
                    + " AND GROUP_CODE='" + document.mainForm.GROUP_CODE.value + "'"
                    + " AND DOCTOR_CATEGORY_CODE='" + document.mainForm.DOCTOR_CATEGORY_CODE.value + "'"
                    + " AND TYPE='" + document.mainForm.SEL_TYPE.value + "'"
                    + " AND DOCTOR_CODE='" + document.mainForm.DOCTOR_CODE.value + "'";
                 //document.mainForm.showsql.value = target;
                //alert(type_code);
                AJAX_Request(target, AJAX_Handle_VerifyData);
            }
            
            function AJAX_Handle_VerifyData() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist1 = isXMLNodeExist(xmlDoc, "HOSPITAL_CODE");
                    var beExist2 = isXMLNodeExist(xmlDoc, "GROUP_CODE");
                    var beExist3 = isXMLNodeExist(xmlDoc, "DOCTOR_CATEGORY_CODE");
					var beExist4 = isXMLNodeExist(xmlDoc, "DOCTOR_CODE");
					var beExist5 = isXMLNodeExist(xmlDoc, "TYPE");
                    if(document.mainForm.MODE.value=="<%=DBMgr.MODE_INSERT%>")
                    {
                       if (beExist1 && beExist2 && beExist3 && beExist4 && beExist5) 
                       {
                          alert('Doctor Code :'+getXMLNodeValue(xmlDoc, "DOCTOR_CODE")+' is in the system');
                       }
                       else 
                       {
                          document.mainForm.submit();
                       }
                    }
                   else//MODE_UPDATE
                   {
                   		document.mainForm.submit();
                   }
                }
            }
            
            
            //  !isObjectEmptyString(document.mainForm.GUARANTEE_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
            function SAVE_Click() {
                if (!isObjectEmptyString(document.mainForm.GROUP_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.DOCTOR_CATEGORY_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.DOCTOR_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')){
                    	if(mainForm.SEL_TYPE.value=="")
                    	{
                    		alert("Plase, Select type");
                    		return false;
                    	}
                    	else
                    	{
							AJAX_VerifyData();
						}
                }
            }

            function ChDoctor(id)
            {
            	//alert("id="+id);
            	if(id=="<%=DBMgr.MODE_INSERT%>")
            	{
            		document.mainForm.DOCTOR_CODE.disabled=true;
            		document.mainForm.DOCTOR_NAME.disabled=true;
            		document.getElementById('SEARCH_DOCTOR_CODE').disabled=true;
            	}
            	else
            	{
            		document.mainForm.DOCTOR_CODE.disabled=false;
            		document.mainForm.DOCTOR_NAME.disabled=false;
            		document.getElementById('SEARCH_DOCTOR_CODE').disabled=false;
            	}
            }

           	
        </script>
        <style type="text/css">
<!--
.style1 {color: #003399}
-->
        </style>
</head>    
    <body onload="ChDoctor('<%= MODE %>');">
        <form id="mainForm" name="mainForm" method="post" action="group_prorate_detail.jsp">
           <input type="hidden" id="MODE" name="MODE" value="<%= MODE %>" />
            <table class="form">
                <tr><th colspan="2">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
				</th></tr>
                <tr>
                  <td width="249" class="label"><label for="LABEL_GROUP_CODE"><span class="style1">${labelMap.LABEL_GROUP_CODE} *</span></label>                    </td>
                    <td width="539" class="input">
                        <input type="text" id="GROUP_CODE" name="GROUP_CODE" class="short" readonly="readonly" value="<%= DBMgr.getRecordValue(groupRec, "CODE") %>" />
                  <input type="text" id="GROUP_NAME" name="GROUP_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(groupRec, "NAME") %>" />                    </td>
                </tr>
               
                <tr>
                  <td class="label"><label for="LABEL_DOCTOR_CATEGORY"><span class="style1">${labelMap.LABEL_DOCTOR_CATEGORY} *</span></label>                    </td>
                    <td class="input">
                        <input name="DOCTOR_CATEGORY_CODE" type="text" class="short" id="DOCTOR_CATEGORY_CODE" onblur="AJAX_Refresh_DOCTOR_CATEGORY();" onkeypress="return DOCTOR_CATEGORY_CODE_KeyPress(event);" value="<%= DBMgr.getRecordValue(prorateRec, "DOCTOR_CATEGORY_CODE") %>" maxlength="20" />
                        <input id="SEARCH_DOCTOR_CATEGORY_CODE" name="SEARCH_DOCTOR_CATEGORY_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="doctorOnSearch(1); return false;" />
                        <input type="text" id="DOCTOR_CATEGORY_NAME" name="DOCTOR_CATEGORY_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorCategoryRec, "DESCRIPTION") %>" />                    </td>
                </tr>
                
                <tr>
                  <td class="label"><label for="LABEL_DOCTOR_CODE"><span class="style1">${labelMap.LABEL_DOCTOR_CODE} *</span></label></td>
                    <td class="input">
                        <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_CODE();" />
                        <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="doctorOnSearch(2); return false;" />
                        <input type="text" id="DOCTOR_NAME" name="DOCTOR_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME_" + labelMap.getFieldLangSuffix()) %>" />
                    </td>
                </tr>
                <tr>
                  <td class="label"><label for="LABEL_TYPE"><span class="style1">${labelMap.LABEL_TYPE} *</span></label></td>
                  <td class="input"><select name="SEL_TYPE">
                    <option value='' <%= ("".equalsIgnoreCase(DBMgr.getRecordValue(prorateRec, "TYPE")) || select_type.equals("POOL") ? "selected" : "") %>>SELECT TYPE</option>
                    <option value='POOL' <%= ("POOL".equalsIgnoreCase(DBMgr.getRecordValue(prorateRec, "TYPE")) || select_type.equals("POOL") ? "selected" : "") %>>POOL</option>
                    <option value='CK'" <%= ("CK".equalsIgnoreCase(DBMgr.getRecordValue(prorateRec, "TYPE")) || select_type.equals("CK") ? "selected" : "") %>>C.K.</option>
                  </select></td>
                </tr>
                <tr>
               	  <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td> 
                    <td class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(prorateRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(prorateRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(prorateRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>                    </td>
                </tr>
                <tr>
                    <th colspan="2" class="buttonBar">                        
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click()" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.history.back()" />                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
<script language="javascript">
	
	function doctorOnSearch(id){
		if(id==1)
		{
			var group_code = document.getElementById('GROUP_CODE');
			//alert(group_code);
			var url='../search.jsp?TABLE=DOCTOR_CATEGORY&TABLE1=STP_GROUP_DOCTOR_CATEGORY&TARGET=DOCTOR_CATEGORY_CODE&DISPLAY_FIELD=DESCRIPTION&COND=[AND (STP_GROUP_DOCTOR_CATEGORY.DOCTOR_CATEGORY_CODE=DOCTOR_CATEGORY.CODE) AND (STP_GROUP_DOCTOR_CATEGORY.GROUP_CODE=\''+ group_code.value +'\') ]&HANDLE=AJAX_Refresh_DOCTOR_CATEGORY';
			//alert(url);
			//var m = document.getElementById('MM');
			//openSearchForm('../search.jsp?TABLE=DOCTOR_CATEGORY&TABLE1=STP_GROUP_DOCTOR_CATEGORY&TARGET=DOCTOR_CATEGORY_CODE&DISPLAY_FIELD=DESCRIPTION&COND=[AND (STP_GROUP_DOCTOR_CATEGORY.DOCTOR_CATEGORY_CODE=DOCTOR_CATEGORY.CODE) AND (STP_GROUP_DOCTOR_CATEGORY.GROUP_CODE=\''+ group_code.value +'\') ]&HANDLE=AJAX_Refresh_DOCTOR_CATEGORY');
			openSearchForm(url);
		}
		else if(id==2)
		{
			var doctor_category = document.getElementById('DOCTOR_CATEGORY_CODE');
			//var m = document.getElementById('MM');
			openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE&BEINSIDEHOSPITAL=1&COND=[AND (DOCTOR_CATEGORY_CODE=\''+ doctor_category.value +'\') ]&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_CODE');	
		}
	}
</script>