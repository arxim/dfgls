<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../../error.jsp"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@page import="java.sql.Types"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@ include file="../../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_METHOD_ALLOC_ITEM_DETAIL)) {
                response.sendRedirect("../../message.jsp");
                return;
            }

            //
            // Initial LabelMap
            //

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Doctor Category ", "Doctor Category");
            labelMap.add("ADMISSION_TYPE_CODE", "Admission Type", "แผนกรับผู้ป่วย");
            labelMap.add("DOCTOR_TREATMENT_CODE", "Doctor Treatment", "Doctor Treatment");
            labelMap.add("DOCTOR_CATEGORY_CODE", "Doctor Category", "Doctor Category");
            labelMap.add("ORDER_ITEM_CATEGORY_CODE" , "Order Item Category Code" ,  "Order Item Category Code" );
            labelMap.add("ORDER_ITEM_CODE", "Order Item", "Order Item");
            labelMap.add("PAYOR_OFFICE_CODE" , "PAYOR Office Code" , "PAYOR Office Code");
            labelMap.add("PAYOR_OFFICE_CATEGORY_CODE" , "PAYOR Office Category Code" , "PAYOR Office Category Code" );
            labelMap.add("AMOUNT_START", "Price", "ราคา");
            labelMap.add("TAX_TYPE_CODE", "Tax Type", "ประเภทภาษี");
            labelMap.add("TAX_SOURCE" , "Tax Source", "Tax Source");
            labelMap.add("TAX_RATE" , "Tax Rate" , "Tax Rate");
			labelMap.add("PRIVATE_DOCTOR", "Private Doctor", "Private Doctor");
            labelMap.add("PRIVATE_DOCTOR_0", "No", "ไม่");
            labelMap.add("PRIVATE_DOCTOR_1", "Yes", "ใช่");
            labelMap.add("PROCEDURE" , "Procedure" , "Procedure");
            labelMap.add("NORMAL_ALLOCATE_PCT", "Allocate Percentage", "ส่วนแบ่งอัตราส่วน");
            labelMap.add("NORMAL_ALLOCATE_AMT", "Allocate Amount", "ราคาส่วนแบ่งแพทย์");
            labelMap.add("GUARANTEE_SOURCE", "Revenue for Guarantee", "จำนวนเงินคิดการันตี");
            labelMap.add("REVENUE_GUARANTEE" , "Revenue for Guarantee" ,  "Revenue for Guarantee");
            labelMap.add("REMARK", "Remark", "หมายเหตุ");
            labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //
            request.setCharacterEncoding("UTF-8");
            
            byte MODE = DBMgr.MODE_INSERT;
            
            DataRecord methodRec = null;
            DataRecord doctorCategoryRec = null;
            DataRecord doctorRec = null;
            DataRecord orderItemCateRec = null;
            DataRecord orderItemRec = null;
            DataRecord payorCateRec = null;
            DataRecord payorRec = null;
            
            if (request.getParameter("MODE") != null) {
            	
            	// Insert or update
                MODE = Byte.parseByte(request.getParameter("MODE"));
                
                methodRec = new DataRecord("STP_METHOD_ALLOCATE");
                methodRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                methodRec.addField("METHOD_SEQUENCE", Types.NUMERIC, request.getParameter("METHOD_SEQUENCE"), true);
                methodRec.addField("DOCTOR_CATEGORY_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CATEGORY_CODE"), true);
                methodRec.addField("DOCTOR_CODE", Types.VARCHAR, "", true);
                methodRec.addField("ORDER_ITEM_CATEGORY_CODE", Types.VARCHAR, request.getParameter("ORDER_ITEM_CATEGORY_CODE"), true);
                methodRec.addField("ORDER_ITEM_CODE", Types.VARCHAR, request.getParameter("ORDER_ITEM_CODE")  ,  true);
                methodRec.addField("PAYOR_CATEGORY_CODE", Types.VARCHAR, request.getParameter("PAYOR_OFFICE_CATEGORY_CODE"), true);
                methodRec.addField("PAYOR_CODE", Types.VARCHAR, request.getParameter("PAYOR_CODE"), true);
                methodRec.addField("ADMISSION_TYPE_CODE",Types.VARCHAR, request.getParameter("ADMISSION_TYPE_CODE") , true);
                methodRec.addField("DOCTOR_TREATMENT_CODE", Types.VARCHAR, request.getParameter("DOCTOR_TREATMENT_CODE")  , true);
                methodRec.addField("IS_PROCEDURE", Types.VARCHAR, request.getParameter("IS_PROCEDURE"));
                methodRec.addField("PRIVATE_DOCTOR", Types.VARCHAR, request.getParameter("PRIVATE_DOCTOR"), true);
                methodRec.addField("AMOUNT_START", Types.NUMERIC, request.getParameter("AMOUNT_START"), true);
                methodRec.addField("AMOUNT_END", Types.NUMERIC ,  "0" , true);
                methodRec.addField("NORMAL_ALLOCATE_PCT", Types.NUMERIC , request.getParameter("NORMAL_ALLOCATE_PCT"), true);
                methodRec.addField("NORMAL_ALLOCATE_AMT", Types.NUMERIC , request.getParameter("NORMAL_ALLOCATE_AMT"), true);
                methodRec.addField("GUARANTEE_SOURCE", Types.VARCHAR, request.getParameter("GUARANTEE_SOURCE"));
                methodRec.addField("TAX_TYPE_CODE", Types.VARCHAR, request.getParameter("TAX_TYPE_CODE"));
                methodRec.addField("TAX_RATE", Types.NUMERIC , request.getParameter("TAX_RATE"));
                methodRec.addField("TAX_SOURCE", Types.VARCHAR, request.getParameter("TAX_SOURCE"));
                methodRec.addField("UPDATE_USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                methodRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                methodRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                methodRec.addField("ACTIVE", Types.VARCHAR,  request.getParameter("ACTIVE"));
                methodRec.addField("NOTE", Types.VARCHAR,  request.getParameter("NOTE"));
               
                if (MODE == DBMgr.MODE_INSERT) {

                    if (DBMgr.insertRecord(methodRec)) {
                    	System.out.println(" U have insert Data.");
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/methodAllocateBasic/stp_method_doctor_category.jsp?DOCTOR_CATEGORY_CODE=" + methodRec.getField("DOCTOR_CATEGORY_CODE").getValue()));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } 
                else if (MODE == DBMgr.MODE_UPDATE) {
                	if (DBMgr.updateRecord(methodRec)) {
                  		System.out.print(" Update complete.");
                		session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/methodAllocateBasic/stp_method_doctor_category.jsp?DOCTOR_CATEGORY_CODE=" +  request.getParameter("DOCTOR_CATEGORY_CODE")));
                    }  else {
                    	System.out.print(" Update fail.");
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }
                response.sendRedirect("../../message.jsp");
                return;
            }
            else if (request.getParameter("METHOD_SEQUENCE") != null && 
            		request.getParameter("DOCTOR_CATEGORY_CODE") != null  && 
            		request.getParameter("DOCTOR_CODE")  != null &&
            		request.getParameter("ORDER_ITEM_CATEGORY_CODE")  != null &&
            		request.getParameter("ORDER_ITEM_CODE")  != null &&
            		request.getParameter("PAYOR_OFFICE_CATEGORY_CODE")  != null &&
            		request.getParameter("PAYOR_CODE")  != null &&
            		request.getParameter("ADMISSION_TYPE_CODE")  != null &&
            		request.getParameter("DOCTOR_TREATMENT_CODE")  != null &&
            		//request.getParameter("IS_PROCEDURE")  != null &&
            		request.getParameter("PRIVATE_DOCTOR")  != null &&
            		request.getParameter("AMOUNT_START")  != null &&
            		request.getParameter("AMOUNT_END")  != null &&
            		request.getParameter("NORMAL_ALLOCATE_PCT")  != null &&
            		request.getParameter("NORMAL_ALLOCATE_AMT")  != null 
            		) {
              
            	// Edit
                MODE = DBMgr.MODE_UPDATE;
            	String query  = " SELECT * FROM  STP_METHOD_ALLOCATE "
            		    +  " WHERE "
            		    +  " HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE") + "'"
            		    +  " AND METHOD_SEQUENCE  = " +  request.getParameter("METHOD_SEQUENCE")
            		    +  " AND DOCTOR_CATEGORY_CODE = "+ request.getParameter("DOCTOR_CATEGORY_CODE") 
            		    +  " AND DOCTOR_CODE = " + request.getParameter("DOCTOR_CODE") 
            		    +  " AND ORDER_ITEM_CATEGORY_CODE = " + request.getParameter("ORDER_ITEM_CATEGORY_CODE") 
            		    +  " AND ORDER_ITEM_CODE = "+ request.getParameter("ORDER_ITEM_CODE")
            		    +  " AND PAYOR_CATEGORY_CODE = " +  request.getParameter("PAYOR_OFFICE_CATEGORY_CODE")
            		    +  " AND PAYOR_CODE = " + request.getParameter("PAYOR_CODE")
            		    +  " AND ADMISSION_TYPE_CODE = "+request.getParameter("ADMISSION_TYPE_CODE")
            		    +  " AND DOCTOR_TREATMENT_CODE = " +request.getParameter("DOCTOR_TREATMENT_CODE")
            		    +  " AND IS_PROCEDURE = " +request.getParameter("IS_PROCEDURE")
            		    +  " AND PRIVATE_DOCTOR = "+ request.getParameter("PRIVATE_DOCTOR")
            		    +  " AND AMOUNT_START =  " +request.getParameter("AMOUNT_START")
            		    +  " AND AMOUNT_END = "+request.getParameter("AMOUNT_END")
            		    +  " AND NORMAL_ALLOCATE_PCT = " + request.getParameter("NORMAL_ALLOCATE_PCT") 
            		    +  " AND NORMAL_ALLOCATE_AMT  = "  +  request.getParameter("NORMAL_ALLOCATE_AMT");
                
                methodRec = DBMgr.getRecord(query);
                doctorCategoryRec = DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION FROM DOCTOR_CATEGORY WHERE CODE = %1$s", request.getParameter("DOCTOR_CATEGORY_CODE")));
                String doctorCode = String.format("SELECT CODE, DESCRIPTION FROM DOCTOR_CATEGORY WHERE CODE = %1$s ", request.getParameter("DOCTOR_CATEGORY_CODE"));
               // doctorRec = DBMgr.getRecord(String.format("SELECT CODE, NAME_THAI FROM DOCTOR WHERE CODE = %1$s", request.getParameter("DOCTOR_CODE")));
                orderItemCateRec = DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION_THAI FROM ORDER_ITEM_CATEGORY WHERE CODE =  %1$s ", request.getParameter("ORDER_ITEM_CATEGORY_CODE")));
                orderItemRec  =  DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION_THAI FROM ORDER_ITEM WHERE CODE =  %1$s ", request.getParameter("ORDER_ITEM_CODE")));
                payorCateRec = DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION FROM PAYOR_OFFICE_CATEGORY WHERE CODE =  %1$s ", request.getParameter("PAYOR_OFFICE_CATEGORY_CODE")));
                //payorRec = DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION_THAI FROM PAYOR_OFFICE WHERE CODE =  %1$s", request.getParameter("PAYOR_OFFICE_CODE")));
         	
            }
            else if (request.getParameter("DOCTOR_CATEGORY_CODE") != null) {
            	MODE = DBMgr.MODE_INSERT;
                doctorCategoryRec = DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION FROM DOCTOR_CATEGORY WHERE CODE =  %1$s ", request.getParameter("DOCTOR_CATEGORY_CODE")));
            } else {
                response.sendRedirect("../../message.jsp");
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" type="text/css" href="../../../css/share.css" media="all" />
        <script type="text/javascript" src="../../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../../javascript/util.js"></script>
        <script type="text/javascript" src="../../../javascript/jquery-1.6.min.js"></script>
    
        <script type="text/javascript">
            
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

            /**
            *  Search Doctor Category
            **/
            function AJAX_Refresh_DOCTOR_CATEGORY() {
                var target = "../../../RetrieveData?TABLE=DOCTOR_CATEGORY&COND=CODE='" + document.mainForm.DOCTOR_CATEGORY_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_CATEGORY);
            }
            
            /**
            *   Seach Doctor
            **/
            function AJAX_Refresh_DOCTOR() {
                var target = "../../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
            }
            
            /**
            * Search Order Item Category
            **/
            function AJAX_Refresh_ORDER_ITEM_CATEGORY() {
                var target = "../../../RetrieveData?TABLE=ORDER_ITEM_CATEGORY&COND=CODE='" + document.mainForm.ORDER_ITEM_CATEGORY_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_ORDER_ITEM_CATEGORY);
            }
            
            /**
             *  Seach Order Item
             **/
             function AJAX_Refresh_ORDER_ITEM() {
                 var target = "../../../RetrieveData?TABLE=ORDER_ITEM&COND=CODE='" + document.mainForm.ORDER_ITEM_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                 AJAX_Request(target, AJAX_Handle_Refresh_ORDER_ITEM);
             }
            
             /**
              *  Seach PAYOR Office Category
              **/
              function AJAX_Refresh_PAYOR_OFFICE_CATEGORY() {
                  var target = "../../../RetrieveData?TABLE=PAYOR_OFFICE_CATEGORY&COND=CODE='" + document.mainForm.PAYOR_OFFICE_CATEGORY_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                  AJAX_Request(target, AJAX_Handle_Refresh_PAYOR_OFFICE_CATEGORY);
              }
           
             
             /**
              *  Seach PAYOR Office
              **/
              function AJAX_Refresh_PAYOR_OFFICE() {
                  var target = "../../../RetrieveData?TABLE=PAYOR_OFFICE&COND=CODE='" + document.mainForm.PAYOR_OFFICE_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                  AJAX_Request(target, AJAX_Handle_Refresh_PAYOR_OFFICE);
              }
             
            
            /**
            *  Handle Doctor Category
            **/
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

            /**
             *  Handle Doctor 
             **/
             function AJAX_Handle_Refresh_DOCTOR() {
                 if (AJAX_IsComplete()) {
                     var xmlDoc = AJAX.responseXML;
                     // Data not found
                     if (!isXMLNodeExist(xmlDoc, "CODE")) {
                         document.mainForm.DOCTOR_CODE.value = "";
                         document.mainForm.DOCTOR_NAME_THAI.value = "";
                         return;
                     }
                     // Data found
                     document.mainForm.DOCTOR_NAME_THAI.value = getXMLNodeValue(xmlDoc, "NAME_THAI");
                 }
             }
            
           
             /**
              *  Handle Order Item  Category
              **/
              function AJAX_Handle_Refresh_ORDER_ITEM_CATEGORY() {
                  if (AJAX_IsComplete()) {
                      var xmlDoc = AJAX.responseXML;
                      // Data not found
                      if (!isXMLNodeExist(xmlDoc, "CODE")) {
                          document.mainForm.ORDER_ITEM_CATEGORY_CODE.value = "";
                          document.mainForm.ORDER_ITEM_CATEGORY_DESCRIPTION.value = "";
                          return;
                      }
                      // Data found
                      document.mainForm.ORDER_ITEM_CATEGORY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_THAI");
                  }
              }
            
            
              /**
               *  Handle Order Item 
               **/
               function AJAX_Handle_Refresh_ORDER_ITEM() {
                   if (AJAX_IsComplete()) {
                       var xmlDoc = AJAX.responseXML;
                       // Data not found
                       if (!isXMLNodeExist(xmlDoc, "CODE")) {
                           document.mainForm.ORDER_ITEM_CODE.value = "";
                           document.mainForm.ORDER_ITEM_DESCRIPTION.value = "";
                           return;
                       }
                       // Data found
                       document.mainForm.ORDER_ITEM_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_THAI");
                   }
               }
             
            
               /**
                *  Handle PAYOR Office Category
                **/
                function AJAX_Handle_Refresh_PAYOR_OFFICE_CATEGORY() {
                    if (AJAX_IsComplete()) {
                        var xmlDoc = AJAX.responseXML;
                        // Data not found
                        if (!isXMLNodeExist(xmlDoc, "CODE")) {
                            document.mainForm.PAYOR_OFFICE_CATEGORY_CODE.value = "";
                            document.mainForm.PAYOR_OFFICE_CATEGORY_DESCRIPTION.value = "";
                            return;
                        }
                        // Data found
                        document.mainForm.PAYOR_OFFICE_CATEGORY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                    }
              }

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
             
             
             function ORDER_ITEM_CODE_KeyPress(e) {
                 var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
                 if (key == 13) {
                     document.mainForm.ORDER_ITEM_CODE.blur();
                     return false;
                 }
                 else {
                     return true;
                 }
             }
             
             function PAYOR_OFFICE_CATEGORY_CODE_KeyPress(e) {
                 var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
                 if (key == 13) {
                     document.mainForm.PAYOR_OFFICE_CATEGORY_CODE.blur();
                     return false;
                 }
                 else {
                     return true;
                 }
             }
             
             function PAYOR_OFFICE_CODE_KeyPress(e) {
                 var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
                 if (key == 13) {
                     document.mainForm.PAYOR_OFFICE_CATEGORY_CODE.blur();
                     return false;
                 }
                 else {
                     return true;
                 }
             }
             
            function AJAX_VerifyData() {
            	var target = "../../../RetrieveData?TABLE=STP_METHOD_ALLOCATE&COND=HOSPITAL_CODE='${sessionScope.HOSPITAL_CODE}' AND METHOD_SEQUENCE = " + getAttVal("METHOD_SEQUENCE") + " AND DOCTOR_CATEGORY_CODE = " + getAttValStr("DOCTOR_CATEGORY_CODE") + " AND DOCTOR_CODE = "+ getAttValStr("DOCTOR_CODE") +" AND ORDER_ITEM_CATEGORY_CODE =  " + getAttValStr("ORDER_ITEM_CATEGORY_CODE")+ " AND ORDER_ITEM_CODE = " + getAttValStr("ORDER_ITEM_CODE") + " AND PAYOR_CATEGORY_CODE =  " + getAttValStr("PAYOR_OFFICE_CATEGORY_CODE") + " AND PAYOR_CODE = " + getAttValStr("PAYOR_CODE") + " AND ADMISSION_TYPE_CODE = " + getAttValStr("ADMISSION_TYPE_CODE") + " AND DOCTOR_TREATMENT_CODE =  " + getAttValStr("DOCTOR_TREATMENT_CODE") + " AND PRIVATE_DOCTOR =  " + getAttValSelect("PRIVATE_DOCTOR") + "   AND  AMOUNT_START = " + getAttVal("AMOUNT_START") + "  AND  AMOUNT_END = 0 AND NORMAL_ALLOCATE_PCT = " + getAttVal("NORMAL_ALLOCATE_PCT") + " AND NORMAL_ALLOCATE_AMT = " + getAttVal("NORMAL_ALLOCATE_AMT");
                AJAX_Request(target, AJAX_Handle_Verify_Data);
            }
            
            function getAttVal($id) { 
            	if($("#" + $id).val() == "" ||  $("#" + $id).val() == undefined ){
            		 return 0;
            	} else  {
            		return $("#" + $id).val();
            	}
            }
            
            function getAttValSelect($id) {
            	return "'" + $("#" + $id + " option:selected").val() +  "'";
            }
            
            function getAttValStr($id){
            	if($("#" + $id).val() == "" ||  $("#" + $id).val() == undefined ){
           		    return "''";
	           	} else  {
	           		return "'" + $("#" + $id).val() + "'";
	           	}	
            }
            
            function getAttValRadio($id){ 
            	return "'" + $("#" + $id + ":checked").val() + "'";
	       	}
            
            function getAttValSelect($id) {
            	return "'" + $("#" + $id + " option:selected").val() +  "'";
            }
            
            function AJAX_Handle_Verify_Data () {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    var beExist = isXMLNodeExist(xmlDoc, "DOCTOR_CATEGORY_CODE");
                    switch (document.mainForm.MODE.value) {
                        case "<%=DBMgr.MODE_INSERT%>" :
                            if (beExist) {
                                if (confirm("<%=labelMap.get(LabelMap.CONFIRM_REPLACE_DATA)%>")) {
                                    document.mainForm.MODE.value= "<%=DBMgr.MODE_UPDATE%>";
                                    document.mainForm.submit();
                                }
                            }
                            else {
                                document.mainForm.submit();
                            }
                            break;
                        case "<%=DBMgr.MODE_UPDATE%>" :
                            if (beExist) {
                                document.mainForm.submit();
                            }
                            else {
                                if (confirm("<%=labelMap.get(LabelMap.CONFIRM_ADD_NEW_DATA)%>")) {
                                    document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT%>";
                                    document.mainForm.submit();
                                }
                            }
                            break;
                    }
                }
            }

            function SAVE_Click() {
            	//AJAX_VerifyData();
                if (!isObjectEmptyString(document.mainForm.DOCTOR_CATEGORY_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                        !isObjectSelectEmptyString(document.mainForm.ADMISSION_TYPE_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                        !isObjectSelectEmptyString(document.mainForm.DOCTOR_TREATMENT_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                        !isObjectSelectEmptyString(document.mainForm.TAX_TYPE_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                        !isObjectSelectEmptyString(document.mainForm.TAX_SOURCE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                        !isObjectEmptyString(document.mainForm.AMOUNT_START, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                        isObjectValidNumber(document.mainForm.AMOUNT_START, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
                        !isObjectEmptyString(document.mainForm.NORMAL_ALLOCATE_AMT, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                        isObjectValidNumber(document.mainForm.NORMAL_ALLOCATE_AMT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>'))  { 
                    AJAX_VerifyData();
                } 
            }
            
            function btnClose() {
            	window.location.href = "stp_method_doctor_category.jsp?DOCTOR_CATEGORY_CODE="+ document.mainForm.DOCTOR_CATEGORY_CODE.value;
            }
            
        </script>
        <style type="text/css">
<!--
.style1 {color: #003399}
-->
        </style>
</head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="stp_method_doctor_category_form.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= MODE %>" />
            <input name="METHOD_SEQUENCE"  id= "METHOD_SEQUENCE" type="hidden" value="1"/>
            <input name="ORDER_ITEM_CATEGORY_CODE" type="hidden" class="medium" id="ORDER_ITEM_CATEGORY_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(orderItemCateRec, "CODE")%>" onkeypress="return ORDER_ITEM_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM_CATEGORY();" />
           	<input name="ORDER_ITEM_CODE" type="hidden" class="medium" id="ORDER_ITEM_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(orderItemRec, "CODE")%>" onkeypress="return ORDER_ITEM_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM();" />
            <input name="PAYOR_OFFICE_CATEGORY_CODE" type="hidden" class="medium" id="PAYOR_OFFICE_CATEGORY_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(payorCateRec, "CODE")%>" onkeypress="return PAYOR_OFFICE_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_PAYOR_OFFICE_CATEGORY();" />
                      
            <table class="form">
                <tr>
                    <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>
                
                <tr>
                  <td class="label">
                    <label for="DOCTOR_CATEGORY_CODE"><span class="style1">${labelMap.DOCTOR_CATEGORY_CODE}*</span></label></td>
                    <td class="input" colspan="3">
                    	<input name="DOCTOR_CATEGORY_CODE" readonly="readonly" type="text" class="medium" id="DOCTOR_CATEGORY_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(doctorCategoryRec, "CODE")%>" onkeypress="return DOCTOR_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_CATEGORY();" />
                       	<input name="DOCTOR_CATEGORY_DESCRIPTION" type="text" class="mediumMax" id="DOCTOR_CATEGORY_DESCRIPTION" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorCategoryRec, "DESCRIPTION")%>" maxlength="255" />                    
                    </td>
                </tr>
               
				<tr>
                    <td class="label"><label for="ADMISSION_TYPE_CODE"><span class="style1">${labelMap.ADMISSION_TYPE_CODE}*</span></label></td>
                    <td class="input">
                        <%= DBMgr.generateDropDownList("ADMISSION_TYPE_CODE", "medium", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM ADMISSION_TYPE ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(methodRec, "ADMISSION_TYPE_CODE")) %>
                    </td>
					<td class="label"><label for="DOCTOR_TREATMENT_CODE"><span class="style1">${labelMap.DOCTOR_TREATMENT_CODE}*</span></label></td>
                    <td class="input">
                        <%= DBMgr.generateDropDownList("DOCTOR_TREATMENT_CODE", "medium", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM DOCTOR_TREATMENT ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(methodRec, "DOCTOR_TREATMENT_CODE")) %>
                    </td>
                </tr>
				<tr>
                    <td class="label"><label for="aText"><span class="style1">${labelMap.TAX_TYPE_CODE}*</span></label></td>
                    <td class="input">
                        <%=DBMgr.generateDropDownList("TAX_TYPE_CODE", "medium", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM TAX_TYPE ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(methodRec, "TAX_TYPE_CODE"))%>
                    </td>
                    <td class="label"><label for="GUARANTEE_SOURCE">${labelMap.GUARANTEE_SOURCE}</label>                    </td>
                    <td colspan="" class="input">
				       <%=DBMgr.generateDropDownList("GUARANTEE_SOURCE", "medium", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM TAX_SOURCE ORDER BY CODE", "DESCRIPTION", "CODE", DBMgr.getRecordValue(methodRec, "GUARANTEE_SOURCE"))%>
                 	</td>
                </tr>
         		<tr>
                    <td class="label"><label for="TAX_SOURCE"><span class="style1">${labelMap.TAX_SOURCE}*</span></label></td>
                    <td class="input">
                        <%=DBMgr.generateDropDownList("TAX_SOURCE", "medium", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM TAX_SOURCE ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(methodRec, "TAX_SOURCE"))%>
                    </td>
                    <td class="label"><label for="TAX_RATE">${labelMap.TAX_RATE}</label></td>
                    <td colspan="" class="input">
						     <%=DBMgr.generateDropDownList("TAX_RATE", "medium", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM TAX_RATE ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(methodRec, "TAX_RATE"))%>
                   </td>
                </tr>
         		
         		<tr>
					<td class="label"><label for="PRIVATE_DOCTOR"><span class="style1">${labelMap.PRIVATE_DOCTOR}*</span></label></td>
	                <td class="input">
	              		<select id="PRIVATE_DOCTOR" name="PRIVATE_DOCTOR" class="medium">
	              			<option value=""  <%=DBMgr.getRecordValue(methodRec, "PRIVATE_DOCTOR").equalsIgnoreCase("") ? " selected=\"selected\"" : "" %>>-- Undefine --</option>
	              			<option value="Y" <%=DBMgr.getRecordValue(methodRec, "PRIVATE_DOCTOR").equalsIgnoreCase("Y") ? " selected=\"selected\"" : "" %>>Yes</option>
	              			<option value="N" <%=DBMgr.getRecordValue(methodRec, "PRIVATE_DOCTOR").equalsIgnoreCase("N") ? " selected=\"selected\"" : "" %>>No</option>
	              		</select>	               
	                </td>
	                <td class="label">
	                	<label for="PROCEDURE"><span class=""> ${labelMap.PROCEDURE}</span></label>
	                </td>
	                <td class="input">
	                     <select id="IS_PROCEDURE" name="IS_PROCEDURE" class="medium">
	              			<option value=""  <%=DBMgr.getRecordValue(methodRec, "IS_PROCEDURE").equalsIgnoreCase("") ? " selected=\"selected\"" : "" %>>-- Undefine --</option>
	              			<option value="Y" <%=DBMgr.getRecordValue(methodRec, "IS_PROCEDURE").equalsIgnoreCase("Y") ? " selected=\"selected\"" : "" %>>Yes</option>
	              			<option value="N" <%=DBMgr.getRecordValue(methodRec, "IS_PROCEDURE").equalsIgnoreCase("N") ? " selected=\"selected\"" : "" %>>No</option>
	              		</select>
	                </td>
                </tr>
                
                <tr>
					<td class="label"><label for="AMOUNT_START">${labelMap.AMOUNT_START}*</label></td>
                    <td class="input"><input type="text" id="AMOUNT_START" name="AMOUNT_START" class="medium short alignRight" value="<%= DBMgr.getRecordValue(methodRec, "AMOUNT_START") %>" maxlength="15" /></td>
                    <td class="label"><label for="NORMAL_ALLOCATE_AMT">${labelMap.NORMAL_ALLOCATE_AMT}*</label></td>
                    <td class="input"><input type="text" id="NORMAL_ALLOCATE_AMT" name="NORMAL_ALLOCATE_AMT" class="medium short alignRight" value="<%= DBMgr.getRecordValue(methodRec, "NORMAL_ALLOCATE_AMT") %>" maxlength="15" /></td>
                </tr>
                
                <tr>
					<td class="label"><label for="NORMAL_ALLOCATE_PCT">${labelMap.NORMAL_ALLOCATE_PCT}*</label></td>
                    <td colspan="3" class="input"><input type="text" id="NORMAL_ALLOCATE_PCT" name="NORMAL_ALLOCATE_PCT" class="medium short alignRight" value="<%= DBMgr.getRecordValue(methodRec, "NORMAL_ALLOCATE_PCT") %>" maxlength="15" /></td>
                </tr>
                <tr>
                    <td class="label"><label for="NOTE">${labelMap.REMARK}</label></td>
                    <td colspan="3" class="input"><textarea id="NOTE" name="NOTE" class="long" style="width: 578px; height: 44px;" rows="3"><%= DBMgr.getRecordValue(methodRec, "NOTE") %></textarea></td>
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
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="btnClose()" />
                    </th>
                </tr>
            </table>
       </form>
    </body>
</html>