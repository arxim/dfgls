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
            labelMap.add("TITLE_MAIN", "Set Order Item Category", "Set Order ITem Category");
            labelMap.add("LABEL_GROUP_CODE", "Group Code", "รหัสกลุ่ม");
            labelMap.add("LABEL_DOCTOR_CATEGORY", "Doctor Category Code", "รหัสกลุ่มแพทย์");
            labelMap.add("LABEL_ORDER_ITEM_CATEGORY", "Order Item Category Code", "รหัสรายการการรักษา");
           
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            
            byte MODE = DBMgr.MODE_INSERT;
            DataRecord orderItemRec = null, doctorCategoryRec = null, groupRec=null, groupOrderRec=null;
            DataRecord stpDelete = null;
            String MM = "", YYYY = "";
            //out.println("group_code="+request.getParameter("GROUP_CODE"));
            //out.println("doctor_category_code="+request.getParameter("DOCTOR_CATEGORY_CODE"));
            
            if (request.getParameter("MODE") != null) {
                // Insert or update
                MODE = Byte.parseByte(request.getParameter("MODE"));

                groupOrderRec = new DataRecord("STP_GROUP_ITEM_CATEGORY");
                stpDelete = new DataRecord("STP_GROUP_ITEM_CATEGORY");

                //, , , , , , , , 
                groupOrderRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                groupOrderRec.addField("GROUP_CODE", Types.VARCHAR, request.getParameter("GROUP_CODE"), true);
                groupOrderRec.addField("DOCTOR_CATEGORY_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CATEGORY_CODE"), true);
                groupOrderRec.addField("ORDER_ITEM_CATEGORY_CODE", Types.VARCHAR, request.getParameter("ORDER_ITEM_CATEGORY_CODE"),true);
                                
                groupOrderRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                groupOrderRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                groupOrderRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                groupOrderRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

                if(1==1){
                	out.println(request.getParameter("GROUP_CODE"));
                	//return;
                }
                if (MODE == DBMgr.MODE_INSERT) {
                    if (DBMgr.insertRecord(groupOrderRec)) {
                        
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/group_order_item_category_main.jsp?GROUP_CODE=%1$s", groupOrderRec.getField("GROUP_CODE").getValue())));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } 
                else if (MODE == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(groupOrderRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/group_order_item_category_main.jsp?GROUP_CODE=%1$s", groupOrderRec.getField("GROUP_CODE").getValue())));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            }
            
            else if (request.getParameter("GROUP_CODE") != null && request.getParameter("DOCTOR_CATEGORY_CODE") != null && request.getParameter("ORDER_ITEM_CATEGORY_CODE") != null ) {
                // Edit
                MODE = DBMgr.MODE_UPDATE;
                //out.println("mode="+MODE);
                //String query = String.format("SELECT CODE AS CODE,DOCTOR_CATEGORY_CODE FROM STP_GROUP WHERE HOSPITAL_CODE = '%1$s' AND GROUP_CODE = '%2$s' AND DOCTOR_CATEGORY_CODE = '%3$s' ", session.getAttribute("HOSPITAL_CODE"), request.getParameter("GROUP_CODE"), request.getParameter("DOCTOR_CATEGORY_CODE"));
                //out.println(query);
                groupRec = DBMgr.getRecord(String.format("SELECT CODE, GROUP_NAME_%1$s AS NAME FROM STP_GROUP WHERE CODE = '%2$s'", labelMap.getFieldLangSuffix(), request.getParameter("GROUP_CODE")));
                groupOrderRec = DBMgr.getRecord(String.format("SELECT * FROM STP_GROUP_ITEM_CATEGORY WHERE GROUP_CODE = '%1$s' AND DOCTOR_CATEGORY_CODE='%2$s' AND ORDER_ITEM_CATEGORY_CODE='%3$s' ", request.getParameter("GROUP_CODE"),request.getParameter("DOCTOR_CATEGORY_CODE"),request.getParameter("ORDER_ITEM_CATEGORY_CODE")));
                doctorCategoryRec = DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION FROM DOCTOR_CATEGORY WHERE CODE = '%1$s'", request.getParameter( "DOCTOR_CATEGORY_CODE")));
				orderItemRec = DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION_%1$s FROM ORDER_ITEM_CATEGORY WHERE CODE = '%2$s'", labelMap.getFieldLangSuffix(),request.getParameter( "ORDER_ITEM_CATEGORY_CODE")));

                }
            else if (request.getParameter("GROUP_CODE") != null ) {
                // New
                MODE = DBMgr.MODE_INSERT;
                //MM = request.getParameter("MM");
                //YYYY = request.getParameter("YYYY");
                groupRec = DBMgr.getRecord(String.format("SELECT CODE, GROUP_NAME_%1$s AS NAME FROM STP_GROUP WHERE CODE = '%2$s'", labelMap.getFieldLangSuffix(), request.getParameter("GROUP_CODE")));
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
                    document.mainForm.GROUP_NAME.value = getXMLNodeValue(xmlDoc, "GROUP_NAME_"+labelMap.getFieldLangSuffix());
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
                        return;
                    }
                    
                    // Data found
                    //alert("dd="+getXMLNodeValue(xmlDoc, "DESCRIPTION"));
                    document.mainForm.DOCTOR_CATEGORY_NAME.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }     
			
             function ORDER_ITEM_CATEGORY_CODE_KeyPress(e) {

                 var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.ORDER_ITEM_CATEGORY_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_ORDER_ITEM_CATEGORY() {
                var target = "../../RetrieveData?TABLE=ORDER_ITEM_CATEGORY&COND=CODE='" + document.mainForm.ORDER_ITEM_CATEGORY_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_ORDER_ITEM_CATEGORY);
            }
            
            function AJAX_Handle_Refresh_ORDER_ITEM_CATEGORY() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //alert("AJAX_Handle_Refresh_GUARANTEE_LOCATION");
                        document.mainForm.ORDER_ITEM_CATEGORY_CODE.value = "";
                        document.mainForm.ORDER_ITEM_CATEGORY_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    //var nameDes="DESCRIPTION_ENG";//+labelMap.getFieldLangSuffix();
                    //alert("name="+nameDes);
                    //alert("<%=labelMap.getFieldLangSuffix()%>");
                    //alert("dd="+getXMLNodeValue(xmlDoc, "DESCRIPTION"));
                    document.mainForm.ORDER_ITEM_CATEGORY_NAME.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_"+"<%=labelMap.getFieldLangSuffix()%>");
                    //document.mainForm.ORDER_ITEM_CATEGORY_NAME.value = getXMLNodeValue(xmlDoc, "HOSPITAL_CODE");
                    //alert(document.mainForm.ORDER_ITEM_CATEGORY_NAME.value);
                }
            }       
            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=STP_GROUP_ITEM_CATEGORY&COND=HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'"
                    + " AND GROUP_CODE='" + document.mainForm.GROUP_CODE.value + "'"
                    + " AND DOCTOR_CATEGORY_CODE='" + document.mainForm.DOCTOR_CATEGORY_CODE.value + "'"
                    + " AND ORDER_ITEM_CATEGORY_CODE='" + document.mainForm.ORDER_ITEM_CATEGORY_CODE.value + "'";
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
					var beExist4 = isXMLNodeExist(xmlDoc, "ORDER_ITEM_CATEGORY_CODE");
                    if(document.mainForm.MODE.value=="<%=DBMgr.MODE_INSERT%>")
                    {
                       if (beExist1 && beExist2 && beExist3 && beExist4) 
                       {
                          alert('Order Item Category :'+getXMLNodeValue(xmlDoc, "ORDER_ITEM_CATEGORY_CODE")+' is in the system');
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
                    !isObjectEmptyString(document.mainForm.ORDER_ITEM_CATEGORY_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') ){
					AJAX_VerifyData();
                }
            }

            

           	
        </script>
        <style type="text/css">
<!--
.style1 {color: #003399}
-->
        </style>
</head>    
    <body >
        <form id="mainForm" name="mainForm" method="post" action="group_order_item_category_detail.jsp">
           <input type="hidden" id="MODE" name="MODE" value="<%= MODE %>" />
            <table class="form">
                <tr><th colspan="2">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
				</th></tr>
                <tr>
                  <td width="274"  class="label"><label for="LABEL_GROUP_CODE"><span class="style1">${labelMap.LABEL_GROUP_CODE} *</span></label>                    </td>
                    <td width="514" class="input">
                        <input type="text" id="GROUP_CODE" name="GROUP_CODE" class="short" readonly="readonly" value="<%= DBMgr.getRecordValue(groupRec, "CODE") %>" />
                  <input type="text" id="GROUP_NAME" name="GROUP_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(groupRec, "NAME") %>" />                    </td>
                </tr>
               
                <tr>
                    <td class="label"><label for="LABEL_DOCTOR_CATEGORY"><span class="style1">${labelMap.LABEL_DOCTOR_CATEGORY} *</span></label>                    </td>
                    <td class="input">
                        <input name="DOCTOR_CATEGORY_CODE" type="text" class="short" id="DOCTOR_CATEGORY_CODE" onblur="AJAX_Refresh_DOCTOR_CATEGORY();" onkeypress="return DOCTOR_CATEGORY_CODE_KeyPress(event);" value="<%= DBMgr.getRecordValue(groupOrderRec, "DOCTOR_CATEGORY_CODE") %>" maxlength="20" />
                        <input id="SEARCH_DOCTOR_CATEGORY_CODE" name="SEARCH_DOCTOR_CATEGORY_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="doctorCategoryOnSearch(1); return false;" />
                        <input type="text" id="DOCTOR_CATEGORY_NAME" name="DOCTOR_CATEGORY_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorCategoryRec, "DESCRIPTION") %>" />                    </td>
                </tr>
                <tr>
                  <td class="label"> <label for="LABEL_ORDER_ITEM_CATEGORY"><span class="style1">${labelMap.LABEL_ORDER_ITEM_CATEGORY} *</span></label></td>
                    <td class="input"><input name="ORDER_ITEM_CATEGORY_CODE" type="text" class="short" id="ORDER_ITEM_CATEGORY_CODE" onblur="AJAX_Refresh_ORDER_ITEM_CATEGORY();" onkeypress="return ORDER_ITEM_CATEGORY_CODE_KeyPress(event);" value="<%= DBMgr.getRecordValue(groupOrderRec, "ORDER_ITEM_CATEGORY_CODE") %>" maxlength="20" />
                      <input id="SEARCH_ORDER_ITEM_CATEGORY_CODE" name="SEARCH_ORDER_ITEM_CATEGORY_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="doctorCategoryOnSearch(2); return false;" />
                      <input type="text" id="ORDER_ITEM_CATEGORY_NAME" name="ORDER_ITEM_CATEGORY_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(orderItemRec, "DESCRIPTION_"+labelMap.getFieldLangSuffix()) %>" /></td>
                </tr>
                <tr>
                	<td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td> 
                    <td class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(groupOrderRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(groupOrderRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(groupOrderRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> />
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
	
	function doctorCategoryOnSearch(id){
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
			var doctor_category_codee = document.getElementById('DOCTOR_CATEGORY_CODE');
			//openSearchForm('../search.jsp?TABLE=ORDER_ITEM_CATEGORY&TARGET=ORDER_ITEM_CATEGORY_CODE&DISPLAY_FIELD=DESCRIPTION_ENG&BEACTIVE=1&HANDLE=AJAX_Refresh_ORDER_ITEM_CATEGORY')
			var url='../search.jsp?TABLE=ORDER_ITEM_CATEGORY&TABLE1=STP_METHOD_ALLOC_ITEM_CATEGORY&TARGET=ORDER_ITEM_CATEGORY_CODE&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&COND=[AND (ORDER_ITEM_CATEGORY.CODE=STP_METHOD_ALLOC_ITEM_CATEGORY.ORDER_ITEM_CODE) AND (STP_METHOD_ALLOC_ITEM_CATEGORY.DOCTOR_CATEGORY_CODE=\''+ doctor_category_codee.value +'\') ]&HANDLE=AJAX_Refresh_ORDER_ITEM_CATEGORY';
			openSearchForm(url);
		}	
	}
</script>