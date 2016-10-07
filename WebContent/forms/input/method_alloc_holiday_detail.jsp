<%@page import="com.sun.mail.iap.Response"%>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.Types"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_METHOD_ALLOC_ITEM_DETAIL)) {
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
            labelMap.add("TITLE_MAIN", "Long Weekend Allocate Details", "รายละเอียดการกำหนดส่วนแบ่ง");
            labelMap.add("DOCTOR_CATEGORY_CODE", "Doctor Category", "กลุ่มเงื่อนไขแพทย์");
            labelMap.add("ORDER_ITEM_CATEGORY_CODE", "Order Item Category", "กลุ่มรายการรักษา");
            labelMap.add("DOCTOR_CODE", "Doctor", "รหัสแพทย์");            
            labelMap.add("DATE_HOLIDAY", "Date", "วันหยุดนักขัตฤกษ์");
            labelMap.add("ORDER_ITEM_CODE", "Order Item", "รายการรักษา");
            labelMap.add("ADMISSION_TYPE_CODE", "Admission Type", "แผนกรับผู้ป่วย");
            labelMap.add("PATIENT_DEPARTMENT_CODE", "Patient Department", "แผนกรักษาผู้ป่วย");
            labelMap.add("INCLUDE", "Setup Type", "ประเภทการกำหนดเงื่อนไข");
            labelMap.add("DOCTOR_TREATMENT_CODE", "Doctor Treatment", "ปรเภทการรักษา");
            labelMap.add("NOR_ALLOCATE_PCT", "Allocate Percentage", "ส่วนแบ่งอัตราส่วน");
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            
            byte MODE = DBMgr.MODE_INSERT;
            DataRecord methodRec = null; 
            
            if (request.getParameter("MODE") != null) {
                // Insert or update
                MODE = Byte.parseByte(request.getParameter("MODE"));
                methodRec = new DataRecord("STP_HOLIDAY");
                methodRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                methodRec.addField("DOCTOR_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CODE")==null?"":request.getParameter("DOCTOR_CODE"), true);
                methodRec.addField("DOCTOR_CATEGORY_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CATEGORY_CODE")==null?"":request.getParameter("DOCTOR_CATEGORY_CODE"), true);
                methodRec.addField("ORDER_ITEM_CATEGORY_CODE", Types.VARCHAR, request.getParameter("ORDER_ITEM_CATEGORY_CODE")==null?"":request.getParameter("ORDER_ITEM_CATEGORY_CODE"), true);
                methodRec.addField("ORDER_ITEM_CODE", Types.VARCHAR, request.getParameter("ORDER_ITEM_CODE")==null?"":request.getParameter("ORDER_ITEM_CODE"), true);
                methodRec.addField("PATIENT_DEPARTMENT_CODE", Types.VARCHAR, request.getParameter("PATIENT_DEPARTMENT_CODE")==null?"":request.getParameter("PATIENT_DEPARTMENT_CODE"), true);
                methodRec.addField("DD", Types.VARCHAR, request.getParameter("DATE_HOLIDAY").split("/")[0], true);
                methodRec.addField("MM", Types.VARCHAR, request.getParameter("DATE_HOLIDAY").split("/")[1], true);
                methodRec.addField("YYYY", Types.VARCHAR, request.getParameter("DATE_HOLIDAY").split("/")[2], true);
                methodRec.addField("ADMISSION_TYPE_CODE", Types.VARCHAR, request.getParameter("ADMISSION_TYPE_CODE"));
                methodRec.addField("NOR_ALLOCATE_PCT", Types.NUMERIC, request.getParameter("NOR_ALLOCATE_PCT"));
                methodRec.addField("ACTIVE",Types.VARCHAR, request.getParameter("ACTIVE"));
                methodRec.addField("INCLUDE",Types.VARCHAR, request.getParameter("INCLUDE"));
              
                if (MODE == DBMgr.MODE_INSERT) {
                    methodRec.addField("CREATE_USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                    methodRec.addField("CREATE_DATE", Types.VARCHAR, JDate.getDate());
                    methodRec.addField("CREATE_TIME", Types.VARCHAR, JDate.getTime());
                    if (DBMgr.insertRecord(methodRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/method_alloc_holiday_main.jsp"));
                    }else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }else if (MODE == DBMgr.MODE_UPDATE) {
                    methodRec.addField("UPDATE_USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                    methodRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                    methodRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                    if (DBMgr.updateRecord(methodRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/method_alloc_holiday_main.jsp"));
                    }else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    } System.out.println("2");
                }
	
                response.sendRedirect("../message.jsp");
                return;
            }else if (request.getParameter("YYYY")!=null) {
                // New
            	MODE = DBMgr.MODE_UPDATE;
             	String query = String.format("SELECT DC.DESCRIPTION AS DOCTOR_CATEGORY_DESC, D.DESCRIPTION AS PATIENT_DEPARTMENT_DESC, "+
             			"DR.NAME_THAI AS DR_NAME_THAI, OC.DESCRIPTION_"+labelMap.getFieldLangSuffix()+" AS ORDER_ITEM_CAT_DESC, "+
             			"OI.DESCRIPTION_"+labelMap.getFieldLangSuffix()+" AS ORDER_ITEM_DESC, S.* FROM STP_HOLIDAY S "+
             			"LEFT OUTER JOIN DEPARTMENT D ON S.PATIENT_DEPARTMENT_CODE = D.CODE AND S.HOSPITAL_CODE = D.HOSPITAL_CODE "+
             			"LEFT OUTER JOIN DOCTOR_CATEGORY DC ON S.DOCTOR_CATEGORY_CODE = DC.CODE AND S.HOSPITAL_CODE = DC.HOSPITAL_CODE "+
             			"LEFT OUTER JOIN DOCTOR DR ON S.DOCTOR_CODE = DR.CODE AND S.HOSPITAL_CODE = DR.HOSPITAL_CODE "+
             			"LEFT OUTER JOIN ORDER_ITEM_CATEGORY OC ON S.ORDER_ITEM_CATEGORY_CODE = OC.CODE AND S.HOSPITAL_CODE = OC.HOSPITAL_CODE "+
             			"LEFT OUTER JOIN ORDER_ITEM OI ON S.ORDER_ITEM_CODE = OI.CODE AND S.HOSPITAL_CODE = OI.HOSPITAL_CODE "+
             			"WHERE S.HOSPITAL_CODE = '%1$s' AND S.DOCTOR_CODE = '%2$s' "+
             			"AND S.DOCTOR_CATEGORY_CODE = '%3$s' AND S.ORDER_ITEM_CATEGORY_CODE = '%4$s' "+
             			"AND S.ORDER_ITEM_CODE = '%5$s' AND S.DD = %6$s AND S.MM = '%7$s' AND S.YYYY = '%8$s' "+
             			"AND S.NOR_ALLOCATE_PCT='%9$s' AND S.ADMISSION_TYPE_CODE='%10$s' "+
             			"AND S.PATIENT_DEPARTMENT_CODE = '%11$s'",
            		 	session.getAttribute("HOSPITAL_CODE"), request.getParameter("DOCTOR_CODE"), request.getParameter("DOCTOR_CATEGORY_CODE"), 
            		 	request.getParameter("ORDER_ITEM_CATEGORY_CODE"), request.getParameter("ORDER_ITEM_CODE"), 
            		 	request.getParameter("DD"), request.getParameter("MM"), request.getParameter("YYYY"),
            		 	request.getParameter("NOR_ALLOCATE_PCT"),request.getParameter("ADMISSION_TYPE_CODE"),
            		 	request.getParameter("PATIENT_DEPARTMENT_CODE"));
             	
             	System.out.println("TEST > "+query);
             	methodRec = DBMgr.getRecord(query);
            }else{
            	MODE = DBMgr.MODE_INSERT;
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript">
            
            function DOCTOR_CATEGORY_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CATEGORY_CODE.blur();
                    return false;
                }else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_CATEGORY() {
                var target = "../../RetrieveData?TABLE=DOCTOR_CATEGORY&COND=CODE='" + document.mainForm.DOCTOR_CATEGORY_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_CATEGORY);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_CATEGORY() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DOCTOR_CATEGORY_CODE.value = "";
                        document.mainForm.DOCTOR_CATEGORY_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_CATEGORY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
            
            function ORDER_ITEM_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.ORDER_ITEM_CODE.blur();
                    return false;
                }else {
                    return true;
                }
            }

            function AJAX_Refresh_ORDER_ITEM() {
                var target = "../../RetrieveData?TABLE=ORDER_ITEM&COND=CODE='" + document.mainForm.ORDER_ITEM_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_ORDER_ITEM);
            }
            
            function AJAX_Handle_ORDER_ITEM() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        // Data not found
                        document.mainForm.ORDER_ITEM_CODE.value = "";
                        document.mainForm.ORDER_ITEM_DESCRIPTION.value = "";
                    }
                    else {
                        // Data found
                        document.mainForm.ORDER_ITEM_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                    }
                }
            }
//////////////////////////////////////

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

            function AJAX_Refresh_ORDER_CATEGORY_ITEM() {
                var target = "../../RetrieveData?TABLE=ORDER_ITEM_CATEGORY&COND=CODE='" + document.mainForm.ORDER_ITEM_CATEGORY_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_ORDER_CATEGORY_ITEM);
            }
            
            function AJAX_Handle_ORDER_CATEGORY_ITEM() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        // Data not found
                        document.mainForm.ORDER_ITEM_CATEGORY_CODE.value = "";
                        document.mainForm.ORDER_ITEM_CATEGORY_DESCRIPTION.value = "";
                    }
                    else {
                        // Data found
                        document.mainForm.ORDER_ITEM_CATEGORY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                    }
                }
            }

///////////////////////////////////////

//////////////////////////////////////

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
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_DOCTOR_CODE);
            }
            
            function AJAX_Handle_DOCTOR_CODE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        // Data not found
                        document.mainForm.DOCTOR_CODE.value = "";
                        document.mainForm.DOCTOR_CODE_DESCRIPTION.value = "";
                    }
                    else {
                        // Data found
                        document.mainForm.DOCTOR_CODE_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "NAME_<%=labelMap.getFieldLangSuffix()%>");
                    }
                }
            }

///////////////////////////////////////

			function PATIENT_DEPARTMENT_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.PATIENT_DEPARTMENT_CODE.blur();
                    return false;
                }else {
                    return true;
                }
            }

            function AJAX_Refresh_PATIENT_DEPARTMENT() {
                var target = "../../RetrieveData?TABLE=DEPARTMENT&COND=CODE='" + document.mainForm.PATIENT_DEPARTMENT_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_PATIENT_DEPARTMENT);
            }
            
            function AJAX_Handle_PATIENT_DEPARTMENT() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        // Data not found
                        document.mainForm.PATIENT_DEPARTMENT_CODE.value = "";
                        document.mainForm.PATIENT_DEPARTMENT_DESCRIPTION.value = "";
                    }
                    else {
                        // Data found
                        document.mainForm.PATIENT_DEPARTMENT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                    }
                }
            }
            
///////////////////////////////////////
            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=STP_HOLIDAY&COND=HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>' AND DOCTOR_CODE='"
                		+ document.mainForm.DOCTOR_CODE.value 
                		+ "' AND DOCTOR_CATEGORY_CODE='" + document.mainForm.DOCTOR_CATEGORY_CODE.value 
                		+ "' AND ADMISSION_TYPE_CODE='" + document.mainForm.ADMISSION_TYPE_CODE.value 
                		+ "' AND ORDER_ITEM_CATEGORY_CODE='" + document.mainForm.ORDER_ITEM_CATEGORY_CODE.value 
                		+ "' AND ORDER_ITEM_CODE='" + document.mainForm.ORDER_ITEM_CODE.value 
                		+ "' AND PATIENT_DEPARTMENT_CODE='" + document.mainForm.PATIENT_DEPARTMENT_CODE.value 
                		+ "' AND DD='" + (document.mainForm.DATE_HOLIDAY.value).split("/")[0] 
                		+ "' AND MM='" + (document.mainForm.DATE_HOLIDAY.value).split("/")[1] 
                		+ "' AND YYYY='" + (document.mainForm.DATE_HOLIDAY.value).split("/")[2]  + "'";
                AJAX_Request(target, AJAX_Handle_Verify_Data);
            }
            
            function AJAX_Handle_Verify_Data() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
				
                    var beExist_DOCTOR_CODE = isXMLNodeExist(xmlDoc, "DOCTOR_CODE");
                    var beExist_DOCTOR_CATEGORY_CODE = isXMLNodeExist(xmlDoc, "DOCTOR_CATEGORY_CODE");
                    var beExist_ORDER_ITEM_CATEGORY_CODE = isXMLNodeExist(xmlDoc, "ORDER_ITEM_CATEGORY_CODE");
                    var beExist_ORDER_ITEM_CODE = isXMLNodeExist(xmlDoc, "ORDER_ITEM_CODE");
                    var beExist_PATIENT_DEPARTMENT_CODE = isXMLNodeExist(xmlDoc, "PATIENT_DEPARTMENT_CODE");
                    var beExist_DD = isXMLNodeExist(xmlDoc, "DD");
                    var beExist_MM = isXMLNodeExist(xmlDoc, "MM");
                    var beExist_YYYY = isXMLNodeExist(xmlDoc, "YYYY");
                    var beExist_ADMISSION_TYPE_CODE = isXMLNodeExist(xmlDoc, "ADMISSION_TYPE_CODE");
                    
                    switch (document.mainForm.MODE.value) {
                        case "<%=DBMgr.MODE_INSERT%>" :
                            if (beExist_DOCTOR_CODE&&beExist_DOCTOR_CATEGORY_CODE&&beExist_ORDER_ITEM_CATEGORY_CODE&&
                            	beExist_ORDER_ITEM_CODE&&beExist_DD&&beExist_MM&&beExist_YYYY&&
                            	beExist_PATIENT_DEPARTMENT_CODE&&
                            	beExist_MM&&beExist_YYYY&&beExist_ADMISSION_TYPE_CODE) {
                                //if (confirm("<%=labelMap.get(LabelMap.CONFIRM_REPLACE_DATA)%>")) {
                                //    document.mainForm.MODE.value= "<%=DBMgr.MODE_UPDATE%>";
                                //    document.mainpForm.submit();
                                //}
                                confirm("<%=labelMap.get(LabelMap.ALERT_DUPLICATE_DATA)%>");
                            }else {
                                document.mainForm.submit();
                            }
                            break;
                        case "<%=DBMgr.MODE_UPDATE%>" :
                            if (beExist_DOCTOR_CODE&&beExist_DOCTOR_CATEGORY_CODE&&beExist_ORDER_ITEM_CATEGORY_CODE&&
                            	beExist_ORDER_ITEM_CODE&&beExist_DD&&beExist_MM&&beExist_YYYY&&
                            	beExist_PATIENT_DEPARTMENT_CODE&&
                            	beExist_ADMISSION_TYPE_CODE) {
                                document.mainForm.submit();
                            }else {
                            	confirm("<%=labelMap.get(LabelMap.ALERT_DUPLICATE_DATA)%>");
                            	//if (confirm("<%=labelMap.get(LabelMap.ALERT_DUPLICATE_DATA)%>")) {
                                //    document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT%>";
                                //    document.mainForm.submit();
                            	//}
                            }
                            break;
                    }
                }
            }
            function SAVE_Click() {
            	if( document.mainForm.INCLUDE.value=="Y"){
            		if(	!isObjectEmptyString(document.mainForm.NOR_ALLOCATE_PCT, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')&&
            			!isObjectEmptyString(document.mainForm.DATE_HOLIDAY, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')&&
            			!isObjectEmptyString(document.mainForm.INCLUDE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')){
            			  AJAX_VerifyData();
            		}
            	}else if(document.mainForm.INCLUDE.value=="N"){
            		if(	!isObjectEmptyString(document.mainForm.DATE_HOLIDAY, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')&&
               			!isObjectEmptyString(document.mainForm.INCLUDE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')){
               			AJAX_VerifyData();
                	}
            	}
            }
            <%   
            String docyorCatCode="";
            if(request.getParameter("DOCTOR_CATEGORY_CODE")!=null){
            	docyorCatCode=request.getParameter("DOCTOR_CATEGORY_CODE");
            }else{
            	docyorCatCode="";
            }
            %>
            
        </script>
					<script type="text/javascript">
					var temp_val; // ตัวแปรสำหรับเก็บค่าเก่า สำหรับกรณียกเลิก
					var selIdx; // ตัวแปรสำหรับเก็บค่ารายการเลือกเก่า สำหรับกรณียกเลิก
					function chk_select1(obj){ // เก็บค่าข้อมูลเมื่อคลิก
						temp_val=obj.value;	 // เก็บค่าข้อมูลเมื่อคลิก
						selIdx = obj.selectedIndex; //  เก็บค่าข้อมูลเมื่อคลิก	 
					}
					function chk_select2(obj){ // ตรวจสอบเมื่อเลือกเปลี่ยนแปลงรายการ
					if(obj.value=="Y"){
						  document.mainForm.DOCTOR_CODE.disabled = true;
						  document.mainForm.DOCTOR_CATEGORY_CODE.disabled = true;
						  document.mainForm.ORDER_ITEM_CATEGORY_CODE.disabled = true;
						  document.mainForm.ORDER_ITEM_CODE.disabled = true;
						  document.mainForm.PATIENT_DEPARTMENT_CODE.disabled = false;
						  document.mainForm.NOR_ALLOCATE_PCT.disabled = false;
						  document.mainForm.DOCTOR_CODE.value = "";
						  document.mainForm.DOCTOR_CATEGORY_CODE.value = "";
						  document.mainForm.ORDER_ITEM_CATEGORY_CODE.value = "";
						  document.mainForm.ORDER_ITEM_CODE.value = "";
						  document.mainForm.ORDER_ITEM_CODE.value = "";
						  document.getElementById('SEARCH_PATIENT_DEPARTMENT_CODE').disabled=false;
						  document.getElementById('SEARCH_DOCTOR_CATEGORY_CODE').disabled=true;
						  document.getElementById('SEARCH_DOCTOR_CODE').disabled=true;
						  document.getElementById('SEARCH_ORDER_ITEM_CATEGORY_CODE').disabled=true;
						  document.getElementById('SEARCH_ORDER_ITEM_CODE').disabled=true;
					}else if(obj.value=="N"){
						  document.mainForm.NOR_ALLOCATE_PCT.disabled = true;
						  document.mainForm.PATIENT_DEPARTMENT_CODE.disabled = true;
						  //document.mainForm.NOR_ALLOCATE_PCT.value = "";
						  document.mainForm.DOCTOR_CODE.disabled = false;
						  document.mainForm.DOCTOR_CATEGORY_CODE.disabled = false;
						  document.mainForm.ORDER_ITEM_CATEGORY_CODE.disabled = false;
						  document.mainForm.ORDER_ITEM_CODE.disabled = false;
						  document.getElementById('SEARCH_PATIENT_DEPARTMENT_CODE').disabled=true;
						  document.getElementById('SEARCH_DOCTOR_CATEGORY_CODE').disabled=false;
						  document.getElementById('SEARCH_DOCTOR_CODE').disabled=false;
						  document.getElementById('SEARCH_ORDER_ITEM_CATEGORY_CODE').disabled=false;
						  document.getElementById('SEARCH_ORDER_ITEM_CODE').disabled=false;
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
        <form id="mainForm" name="mainForm" method="post" action="method_alloc_holiday_detail.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= MODE %>" />
            <table class="form">
                <tr>
                    <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>
                <tr >
                    <td class="label"><label for="INCLUDE"><span class="style1">${labelMap.INCLUDE}*</span></label></td>
                    <td colspan="3" class="input">
      						<select id="INCLUDE" name="INCLUDE" class="medium" onchange="chk_select2(this)">
      						<option value="">--- Select ---</option>
      						<option value="Y" class="" <%=DBMgr.getRecordValue(methodRec, "INCLUDE").equals("Y")?"selected=\"selected\"":"" %>>Allocate Condition</option>
      						<option value="N" class="" <%=DBMgr.getRecordValue(methodRec, "INCLUDE").equals("N")?"selected=\"selected\"":"" %>>Exception Condition</option>      					
      						</select>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ADMISSION_TYPE_CODE"><span class="style1">${labelMap.ADMISSION_TYPE_CODE}*</span></label></td>
                    <td colspan="3"class="input">
      						<select id="ADMISSION_TYPE_CODE" name="ADMISSION_TYPE_CODE" class="medium">
      						<option value="">--- Select ---</option>
      						<option value="I" class="" <%=DBMgr.getRecordValue(methodRec, "ADMISSION_TYPE_CODE").equals("I")?"selected=\"selected\"":"" %>>IPD</option>
      						<option value="O" class="" <%=DBMgr.getRecordValue(methodRec, "ADMISSION_TYPE_CODE").equals("O")?"selected=\"selected\"":"" %>>OPD</option>
      						<option value="ALL" class="" <%=DBMgr.getRecordValue(methodRec, "ADMISSION_TYPE_CODE").equals("ALL")?"selected=\"selected\"":"" %>>ALL</option>
      						</select>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DATE_HOLIDAY"><span class="style1">${labelMap.DATE_HOLIDAY}*</span></label></td>
                    <td class="input">
                        <input name="DATE_HOLIDAY" type="text" class="short" id="DATE_HOLIDAY" maxlength="10" value="<%= JDate.showDate(DBMgr.getRecordValue(methodRec, "YYYY")+""+DBMgr.getRecordValue(methodRec, "MM")+DBMgr.getRecordValue(methodRec, "DD"))%>" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('DATE_HOLIDAY'); return false;" />
                    </td>
                    <td class="label"><label for="NOR_ALLOCATE_PCT"><span class="style1">${labelMap.NOR_ALLOCATE_PCT}*</span></label></td>
                    <td  class="input"><input type="text" id="NOR_ALLOCATE_PCT" name="NOR_ALLOCATE_PCT" class="short alignRight" value="<%=DBMgr.getRecordValue(methodRec, "NOR_ALLOCATE_PCT")%>" maxlength="13" />%</td>
				</tr>
                <tr>
                    <td class="label"><label for="PATIENT_DEPARTMENT_CODE"><span class="style1">${labelMap.PATIENT_DEPARTMENT_CODE}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="PATIENT_DEPARTMENT_CODE" name="PATIENT_DEPARTMENT_CODE" class="short" value="<%=DBMgr.getRecordValue(methodRec, "PATIENT_DEPARTMENT_CODE") %>" onkeypress="return PATIENT_DEPARTMENT_CODE_KeyPress(event);" onblur="AJAX_Refresh_PATIENT_DEPARTMENT();" />
                        <input id="SEARCH_PATIENT_DEPARTMENT_CODE" name="SEARCH_PATIENT_DEPARTMENT_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&TARGET=PATIENT_DEPARTMENT_CODE&BEINSIDEHOSPITAL=1&DISPLAY_FIELD=DESCRIPTION&BEACTIVE=1&HANDLE=AJAX_Refresh_PATIENT_DEPARTMENT'); return false;" />
                        <input type="text" id="PATIENT_DEPARTMENT_DESCRIPTION" name="PATIENT_DEPARTMENT_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(methodRec, "PATIENT_DEPARTMENT_DESC") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_CATEGORY_CODE">${labelMap.DOCTOR_CATEGORY_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_CATEGORY_CODE" name="DOCTOR_CATEGORY_CODE" class="short" value="<%= docyorCatCode %>" onkeypress="return DOCTOR_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_CATEGORY();" />
                        <input id="SEARCH_DOCTOR_CATEGORY_CODE" name="SEARCH_DOCTOR_CATEGORY_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_CATEGORY&TARGET=DOCTOR_CATEGORY_CODE&BEINSIDEHOSPITAL=1&DISPLAY_FIELD=DESCRIPTION&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_CATEGORY'); return false;" />
                        <input type="text" id="DOCTOR_CATEGORY_DESCRIPTION" name="DOCTOR_CATEGORY_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(methodRec, "DOCTOR_CATEGORY_DESC") %>" />
                      <script>
                     </script>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" value="<%= DBMgr.getRecordValue(methodRec, "DOCTOR_CODE")%>" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_CODE();" />
                        <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&TARGET=DOCTOR_CODE&BEINSIDEHOSPITAL=1&DISPLAY_FIELD=NAME_THAI&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_CODE'); return false;" />
                        <input type="text" id="DOCTOR_CODE_DESCRIPTION" name="DOCTOR_CODE_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(methodRec, "DR_NAME_THAI") %>" />
                    </td>
                </tr>
                 <tr>
                    <td class="label"><label for="ORDER_ITEM_CATEGORY_CODE">${labelMap.ORDER_ITEM_CATEGORY_CODE}</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="ORDER_ITEM_CATEGORY_CODE" name="ORDER_ITEM_CATEGORY_CODE" class="short" value="<%= DBMgr.getRecordValue(methodRec, "ORDER_ITEM_CATEGORY_CODE") %>" onkeypress="return ORDER_ITEM_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_CATEGORY_ITEM();" />
                        <input id="SEARCH_ORDER_ITEM_CATEGORY_CODE" name="SEARCH_ORDER_ITEM_CATEGORY_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM_CATEGORY&TARGET=ORDER_ITEM_CATEGORY_CODE&BEINSIDEHOSPITAL=1&DISPLAY_FIELD=DESCRIPTION_THAI&BEACTIVE=1&HANDLE=AJAX_Handle_ORDER_CATEGORY_ITEM'); return false;" />
                        <input type="text" id="ORDER_ITEM_CATEGORY_DESCRIPTION" name="ORDER_ITEM_CATEGORY_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(methodRec, "ORDER_ITEM_CAT_DESC") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ORDER_ITEM_CODE">${labelMap.ORDER_ITEM_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="ORDER_ITEM_CODE" name="ORDER_ITEM_CODE" class="short" value="<%=DBMgr.getRecordValue(methodRec, "ORDER_ITEM_CODE") %>" onkeypress="return ORDER_ITEM_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM();" />
                        <input id="SEARCH_ORDER_ITEM_CODE" name="SEARCH_ORDER_ITEM_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM&TARGET=ORDER_ITEM_CODE&BEINSIDEHOSPITAL=1&DISPLAY_FIELD=DESCRIPTION_THAI&BEACTIVE=1&HANDLE=AJAX_Refresh_ORDER_ITEM'); return false;" />
                        <input type="text" id="ORDER_ITEM_DESCRIPTION" name="ORDER_ITEM_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(methodRec, "ORDER_ITEM_DESC") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(methodRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(methodRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(methodRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>
                    </td>
                </tr>

                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click()" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location.href='method_alloc_holiday_main.jsp'" />
                    </th>
                </tr>
            </table>
        </form> 
				<script type="text/javascript">		
					if(document.mainForm.INCLUDE.value=="Y"){
						  document.mainForm.DOCTOR_CODE.disabled = true;
						  document.mainForm.DOCTOR_CATEGORY_CODE.disabled = true;
						  document.mainForm.ORDER_ITEM_CATEGORY_CODE.disabled = true;
						  document.mainForm.ORDER_ITEM_CODE.disabled = true;
						  document.mainForm.PATIENT_DEPARTMENT_CODE.disabled = false;
						  document.mainForm.NOR_ALLOCATE_PCT.disabled = false;
						  document.mainForm.DOCTOR_CODE.value = "";
						  document.mainForm.DOCTOR_CATEGORY_CODE.value = "";
						  document.mainForm.ORDER_ITEM_CATEGORY_CODE.value = "";
						  document.mainForm.ORDER_ITEM_CODE.value = "";
						  document.getElementById('SEARCH_PATIENT_DEPARTMENT_CODE').disabled=false;
						  document.getElementById('SEARCH_DOCTOR_CATEGORY_CODE').disabled=true;
						  document.getElementById('SEARCH_DOCTOR_CODE').disabled=true;
						  document.getElementById('SEARCH_ORDER_ITEM_CATEGORY_CODE').disabled=true;
						  document.getElementById('SEARCH_ORDER_ITEM_CODE').disabled=true;						  
					}else if(document.mainForm.INCLUDE.value=="N"){
						  document.mainForm.NOR_ALLOCATE_PCT.disabled = true;
						  document.mainForm.DOCTOR_CODE.disabled = false;
						  document.mainForm.DOCTOR_CATEGORY_CODE.disabled = false;
						  document.mainForm.ORDER_ITEM_CATEGORY_CODE.disabled = false;
						  document.mainForm.ORDER_ITEM_CODE.disabled = false;
						  document.mainForm.PATIENT_DEPARTMENT_CODE.disabled = true;
						  document.getElementById('SEARCH_PATIENT_DEPARTMENT_CODE').disabled=true;
						  document.getElementById('SEARCH_DOCTOR_CATEGORY_CODE').disabled=false;
						  document.getElementById('SEARCH_DOCTOR_CODE').disabled=false;
						  document.getElementById('SEARCH_ORDER_ITEM_CATEGORY_CODE').disabled=false;
						  document.getElementById('SEARCH_ORDER_ITEM_CODE').disabled=false;
					}
				</script>
    </body>
</html>
