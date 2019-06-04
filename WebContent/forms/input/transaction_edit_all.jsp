<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.*" %>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.table.Batch"%>

<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_RECEIPT)) {
                response.sendRedirect("../message.jsp");
                return;
            }
			
			//
            // Initial LabelMap
            //
            
			DBConnection con;
			con = new DBConnection();
			con.connectToLocal();
			
			DBConn conData = new DBConn();
            conData.setStatement();
            String showButtonSave="";
            
			Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), con);
			
			String batch_year = b.getYyyy();
			String batch_month = b.getMm();
			
			String sqlGurantee="SELECT DISTINCT YYYY, MM FROM SUMMARY_GUARANTEE "
			+" WHERE YYYY='"+batch_year+"' AND MM='"+batch_month+"' AND HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"'";
			
			//out.print(sqlGurantee);
			
			String [][]arrBatch = conData.query(sqlGurantee);
	
			if(arrBatch.length != 0) {
			 	showButtonSave = "disabled";
			}
			
            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            
            String dt = JDate.showDate(JDate.getDate());
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            
            labelMap.add("TITLE_MAIN", "Edit Transaction", "จัดการข้อมูลใบแจ้งหนี้/ใบเสร็จ");
            labelMap.add("INVOICE_NO", "Invoice No", "เลขที่ใบแจ้งหนี้");
            labelMap.add("INVOICE_DATE", "Invoice Date", "วันที่ใบแจ้งหนี้");
			labelMap.add("RECEIPT_DATE", "Receipt Date", "วันที่รับชำระ");
            labelMap.add("PAYOR_OFFICE_CODE", "Payor Office", "บริษัทคู่สัญญา");
            labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "รายการรักษา");
            labelMap.add("HN_NO", "HN No", "HN No");
            labelMap.add("RESIDENT_TYPE", "Resident Type", "Resident Type");
            labelMap.add("Status", "Act", "สถานะ");
            labelMap.add("LINE_NO", "Line No", "Line No");
            labelMap.add("AMOUNT_AFT_DISCOUNT", "Amount", "จำนวนเงิน");
            labelMap.add("NOTE", "Note", "หมายเหตุ");
            labelMap.add("ROLLBACK", "Rollback", "Rollback");
            labelMap.add("PATIENT_NAME", "Patient Name", "ชื่อผู้ป่วย");
            labelMap.add("DETAIL", "Detail", "รายละเอียด");
            labelMap.add("M_RECEIPT", "Receipt", "รับชำระ");
            labelMap.add("TITLE_DATA", "Data List", "รายการข้อมูล");
            labelMap.add("FROM_DATE" , "From Transaction Date" , "จากวันที่");
            labelMap.add("TO_DATE" , "To Transaction Date" , "ถึงวันที่");
            
            
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //
            request.setCharacterEncoding("UTF-8");
            DataRecord payorOfficeRec = null, doctorRec = null, hnNoRec = null, orderRec = null, residentRec = null;
            String query = "";
            
            int table_row=0;
            
            if (request.getParameter("PAYOR_OFFICE_CODE") != null && !request.getParameter("PAYOR_OFFICE_CODE").equalsIgnoreCase("")) {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM PAYOR_OFFICE WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + request.getParameter("PAYOR_OFFICE_CODE") + "'";
                payorOfficeRec = DBMgr.getRecord(query);
            }
            
            if (request.getParameter("DOCTOR_CODE") != null) {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM DOCTOR WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + request.getParameter("DOCTOR_CODE") + "'";
                doctorRec = DBMgr.getRecord(query);
            }
            
            if (request.getParameter("HN_NO") != null && !request.getParameter("HN_NO").equalsIgnoreCase("")) {
                query = "SELECT HN_NO, PATIENT_NAME FROM TRN_DAILY WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND HN_NO = '" + request.getParameter("HN_NO") + "'";
                hnNoRec = DBMgr.getRecord(query);
            }
            
            if (request.getParameter("RESIDENT_TYPE") != null && !request.getParameter("RESIDENT_TYPE").equalsIgnoreCase("")) {
            	query = "SELECT CODE, DESCRIPTION FROM RESIDENT_TYPE WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + request.getParameter("RESIDENT_TYPE") + "'";
                residentRec = DBMgr.getRecord(query);
            }
            
            if (request.getParameter("ORDER_ITEM_CODE") != null && !request.getParameter("ORDER_ITEM_CODE").equalsIgnoreCase("")) {
                query = "SELECT CODE, DESCRIPTION_"+labelMap.getFieldLangSuffix()+" AS NAME FROM ORDER_ITEM WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + request.getParameter("ORDER_ITEM_CODE") + "'";
                orderRec = DBMgr.getRecord(query);
            } else  {
            	System.out.println("order_item_code="+request.getParameter("ORDER_ITEM_CODE"));
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
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javaScript" src="../../javascript/jquery-1.6.min.js"></script>
        <script type="text/javascript">
            
        //-------All------สำหรับ popup หน้าที่กำหนด----------------------------------------------------------
			function SetNewSize(new_location,height,width){
        	
				var resolution_height = screen.availHeight; 
				var resolution_width  = screen.availWidth; 
				var win_height = height;
				var win_width =  width;
				var xpos  = ( resolution_width  - win_width  ) / 2;
				var ypos  = ( resolution_height - win_height ) / 2;
			
				//กำหนดขนาดของหน้าจอ login และให้อยู่ตรงกลาง
				windows_parameter = 'resizable=no,scrollbars=yes,width='+win_width+', height='+win_height+',left='+xpos.toFixed(0)+',top='+ypos.toFixed(0);
				windows = window.open(new_location,'', windows_parameter);
			}
            
            function INVOICE_NO_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.INVOICE_NO.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_INVOICE() {
                var target = "../../RetrieveData?TABLE=TRN_DAILY&COND=INVOICE_NO='" + document.mainForm.INVOICE_NO.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_INVOICE);
            }
            
            function AJAX_Handle_Refresh_INVOICE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    if (!isXMLNodeExist(xmlDoc, "INVOICE_NO")) {
                        document.mainForm.INVOICE_NO.value = "";
                        return;
                    }
                }
            }
            
            function PAYOR_OFFICE_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
                if (key == 13) {
                    document.mainForm.PAYOR_OFFICE_CODE.blur();
                    return false;
                } else {
                    return true;
                }
            }

            function AJAX_Refresh_PAYOR_OFFICE() {
                var target = "../../RetrieveData?TABLE=PAYOR_OFFICE&COND=CODE='" + document.mainForm.PAYOR_OFFICE_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_PAYOR_OFFICE);
            }
            
            function AJAX_Handle_Refresh_PAYOR_OFFICE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.PAYOR_OFFICE_CODE.value = "";
                        document.mainForm.PAYOR_OFFICE_NAME.value = "";
                        return;
                    }
                    // Data found
                    document.mainForm.PAYOR_OFFICE_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
            
            function DOCTOR_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CODE.blur();
                    return false;
                } else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR() {
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
            }
            
            function AJAX_Handle_Refresh_DOCTOR() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.DOCTOR_CODE.value = "";
                        document.mainForm.DOCTOR_NAME.value = "Unknow Doctor";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
            
            function AJAX_Refresh_DOCTOR_ARR() {
                //var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.dataForm.DOCTOR_CODE_ARR[id].value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
            }
            
            function HN_NO_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.HN_NO.blur();
                    return false;
                } else {
                    return true;
                }
            }

            function AJAX_Refresh_HN_NO() {
                var target = "../../RetrieveData?TABLE=TRN_DAILY&COND=HN_NO='" + document.mainForm.HN_NO.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_HN_NO);
            }
            
            function AJAX_Handle_Refresh_HN_NO() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "HN_NO")) {
                        document.mainForm.HN_NO.value = "";
                        document.mainForm.PATIENT_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.PATIENT_NAME.value = getXMLNodeValue(xmlDoc, "PATIENT_NAME");
                }
            }
            
            function ORDER_ITEM_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.ORDER_ITEM_CODE.blur();
                    return false;
                } else {
                    return true;
                }
            }

            function AJAX_Refresh_ORDER_ITEM() {
                var target = "../../RetrieveData?TABLE=ORDER_ITEM&COND=CODE='" + document.mainForm.ORDER_ITEM_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_ORDER_ITEM);
            }
            
            function AJAX_Handle_RESIDENT_TYPE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        // Data not found
                        document.mainForm.RESIDENT_TYPE.value = "";
                        document.mainForm.RESIDENT_TYPE.value = "";
                    } else {
                        // Data found
                        document.mainForm.RESIDENT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                    }
                }
            }
            
            function RESIDENT_TYPE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.RESIDENT_TYPE.blur();
                    return false;
                } else {
                    return true;
                }
            }

            function AJAX_Refresh_RESIDENT_TYPE() {
                var target = "../../RetrieveData?TABLE=RESIDENT_TYPE&COND=CODE='" + document.mainForm.RESIDENT_TYPE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_RESIDENT_TYPE);
            }
            
            function AJAX_Handle_RESIDENT_TYPE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        // Data not found
                        document.mainForm.RESIDENT_TYPE.value = "";
                        document.mainForm.RESIDENT_TYPE.value = "";
                    } else {
                        // Data found
                        document.mainForm.RESIDENT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                    }
                }
            }
            
            function updateAllCheckBox(){
            	var dis_check = document.dataForm.elements['DIS[]'];
            	
            	if(dataForm.allCheckBox.checked == true) {
            		if(dis_check.length==undefined) {
            			dis_check.checked=true;
            		} else 	{
		            	for (var i = 0; i < dis_check.length; i++) { 
		                   dis_check[i].checked=true;
		                }
		            }
	             } else  {
	             	if(dis_check.length == undefined ){
            			dis_check.checked=false;
            		} else {
		             	for (var i = 0; i < dis_check.length; i++) { 
		                   dis_check[i].checked=false;
		                }
		            }
	             }
            } 
            
			function save_data() {
// 				var actionIndex = 0;
// 				$('input[name="DIS[]"]').each(function(){ 
// 					actionIndex += $(this).is(':checked') == true ? 1 : 0 ;
// 				});
				
// 				if(actionIndex > 0){ 
//					document.dataForm.submit();
// 				} else  {
// 					alert("Please seledct Transaction.");
// 					return false;
// 				}

				for(var intActive=0; intActive < document.getElementsByName("ACTIVE_ARR[]").length; intActive++){
					document.getElementsByName("ACTIVE_ARR[]")[intActive].value = document.getElementsByName("chkACTIVE_ARR[]")[intActive].value;
				}
				
				document.dataForm.submit();
           }
			
            //---- ให้คีย์ได้แต่ตัวเลข
			function IsNumericKeyPress(event) {
            	
				event = event || window.event; //For IE
				  
				if(event.keyCode == 46) {
					event.returnValue = true;
				} else if (((event.keyCode < 48) || (event.keyCode > 57))) {
					event.returnValue = false;
				}
				
			}
            
            function actionNewDoctorCode(){ 
            	  $(".NEW_DOCTOR_CODE").val($("#NEW_DOCTOR_CODE").val());
            }
            
            function actionNewOrderItem(){ 
            	 $(".NEW_ORDER_ITEM_CODE").val($("#NEW_ORDER_ITEM_CODE").val());
            }
            
            function checkActive(event){
            	if(event.checked){
            		event.defaultValue = 1;
        		}else{
        			event.defaultValue = 0;
        		} 
            }
            
            // JQuery start 
            $(document).ready(function(){
            	
            	 $("#NEW_DOCTOR_CODE").attr("disabled",true);
            	 $("#NEW_ORDER_ITEM_CODE").attr("disabled",true);
            	   	
            	 /**
                  Check Box New Doctor Action Click
            	 */
            	   $("#ACTION_NEW_DOCTOR_CODE").click(function(){
            	 			 
            		    var id =  $("#NEW_DOCTOR_CODE");
            		    
            	 		if($(this).is(":checked")){ 
            	 			$(id).removeAttr("disabled");          
            	 		} else { 
            	 			$(id).val("");
            	 			$(id).attr("disabled",true);
            	 			
            	 			if($("#FROM_DATE").val() != "" && $("#TO_DATE").val() != "")
    	           	 		{ 
    	           	 			$("#mainForm").submit();
    	           	 		}
            	 			
            	 		};
            	 		
            	   });
            	 
            	  $("#NEW_DOCTOR_CODE").blur(function(){ 
            	  	actionNewDoctorCode();
            	  });
            	 
            	  $("#ACTION_NEW_ORDER_ITEM_CODE").click(function(){
      	 			 
	           		 var id =  $("#NEW_ORDER_ITEM_CODE");
	           		    
	           	 	 if($(this).is(":checked")){ 
	           	 	 	$(id).removeAttr("disabled");          
	           	 	 }else{ 
	           	 		 
	           	 		$(id).val("");
	           	 		$(id).attr("disabled",true);
	           	 		
	           	 		if($("#FROM_DATE").val() != "" && $("#TO_DATE").val() != "")
	           	 		{ 
	           	 			$("#mainForm").submit();
	           	 		}
	           	 		
	           	 	 };
	           	 	 
           	 	  });
            	   
            	    $("#NEW_ORDER_ITEM_CODE").blur(function(){
            		   actionNewOrderItem();
            	    });
            });        
            
    </script>
	<style type="text/css">
		<!--
		.style1 {color: #003399}
		.style2 {color: #033}
		-->
	</style>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="transaction_edit_all.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=DBMgr.MODE_INSERT%>" />
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("transaction_edit_all.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>
                
                 <tr>
                    <td class="label"><label for="FROM_DATE">${labelMap.FROM_DATE}</label></td>
                    <td class="input">
                    	<input name="FROM_DATE" type="text" class="short" id="FROM_DATE" maxlength="10" value="<%= request.getParameter("FROM_DATE") != null ? request.getParameter("FROM_DATE") : "" %>" />
                    	<input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('FROM_DATE'); return false;" />
              		</td>
                    <td class="label">
                        <label for="TO_DATE">${labelMap.TO_DATE}</label>
                    </td>
                    <td class="input">
                        <input name="TO_DATE" type="text" class="short" id="TO_DATE" maxlength="10" value="<%= request.getParameter("TO_DATE") != null ? request.getParameter("TO_DATE") : "" %>" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('TO_DATE'); return false;" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="INVOICE_NO">${labelMap.INVOICE_NO}</label></td>
                    <td class="input">
                        <input type="text" id="INVOICE_NO" name="INVOICE_NO" class="short" maxlength="20" value="<%= request.getParameter("INVOICE_NO") != null ? request.getParameter("INVOICE_NO") : "" %>" onkeypress="return INVOICE_NO_KeyPress(event);" onblur="AJAX_Refresh_INVOICE();" />
                        <input id="SEARCH_INVOICE_NO" name="SEARCH_INVOICE_NO" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=TRN_DAILY&RETURN_FIELD=INVOICE_NO&DISPLAY_FIELD=INVOICE_DATE&BEINSIDEHOSPITAL=1&TARGET=INVOICE_NO&HANDLE=AJAX_Refresh_INVOICE'); return false;" />
                    </td>
                    <td class="label">
                        <label for="INVOICE_DATE">${labelMap.INVOICE_DATE}</label>
                    </td>
                    <td class="input">
                        <input name="INVOICE_DATE" type="text" class="short" id="INVOICE_DATE" maxlength="10" value="<%= request.getParameter("INVOICE_DATE") != null ? request.getParameter("INVOICE_DATE") : "" %>" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('INVOICE_DATE'); return false;" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR();" />
                        <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR'); return false;" />
                        <input type="text" id="DOCTOR_NAME" name="DOCTOR_NAME" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="PAYOR_OFFICE_CODE">${labelMap.PAYOR_OFFICE_CODE}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input name="PAYOR_OFFICE_CODE" type="text" class="short" id="PAYOR_OFFICE_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(payorOfficeRec, "CODE") %>" onkeypress="return PAYOR_OFFICE_CODE_KeyPress(event);" onblur="AJAX_Refresh_PAYOR_OFFICE();" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=PAYOR_OFFICE&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=PAYOR_OFFICE_CODE&HANDLE=AJAX_Refresh_PAYOR_OFFICE'); return false;" />
                        <input name="PAYOR_OFFICE_NAME" type="text" class="long" id="PAYOR_OFFICE_NAME" readonly="readonly" value="<%= DBMgr.getRecordValue(payorOfficeRec, "NAME") %>" maxlength="255" />                    
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ORDER_ITEM_CODE">${labelMap.ORDER_ITEM_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="ORDER_ITEM_CODE" name="ORDER_ITEM_CODE" class="short" value="<%= DBMgr.getRecordValue(orderRec, "CODE") %>" onkeypress="return ORDER_ITEM_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM();" />
                        <input id="SEARCH_ORDER_ITEM_CODE" name="SEARCH_ORDER_ITEM_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM&DISPLAY_FIELD=DESCRIPTION_<%= labelMap.getFieldLangSuffix() %>&TARGET=ORDER_ITEM_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_ORDER_ITEM'); return false;" />
                        <input type="text" id="ORDER_ITEM_NAME" name="ORDER_ITEM_NAME" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(orderRec, "NAME") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="HN_NO">${labelMap.HN_NO}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="HN_NO" name="HN_NO" class="short" value="<%= DBMgr.getRecordValue(hnNoRec, "HN_NO") %>" onkeypress="return HN_NO_KeyPress(event);" onblur="AJAX_Refresh_HN_NO();" />
                        <input id="SEARCH_HN_NO" name="SEARCH_HN_NO" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=TRN_DAILY&RETURN_FIELD=HN_NO&DISPLAY_FIELD=PATIENT_NAME&BEINSIDEHOSPITAL=1&TARGET=HN_NO&HANDLE=AJAX_Refresh_HN_NO'); return false;" />
                        <input type="text" id="PATIENT_NAME" name="PATIENT_NAME" value="<%= DBMgr.getRecordValue(hnNoRec, "PATIENT_NAME") %>" class="long" readonly="readonly" />
                    </td>
                </tr>
              <%--   <tr>
                    <td class="label"><label for="RESIDENT_TYPE">${labelMap.RESIDENT_TYPE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="RESIDENT_TYPE" name="RESIDENT_TYPE" class="short" value="<%= DBMgr.getRecordValue(residentRec, "CODE") %>" onkeypress="return RESIDENT_TYPE_KeyPress(event);" onblur="AJAX_Refresh_RESIDENT_TYPE();" />
                        <input id="SEARCH_RESIDENT_TYPE" name="SEARCH_RESIDENT_TYPE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=RESIDENT_TYPE&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&TARGET=RESIDENT_TYPE&HANDLE=AJAX_Refresh_RESIDENT_TYPE'); return false;" />
                        <input type="text" id="RESIDENT_DESCRIPTION" name="RESIDENT_DESCRIPTION" value="<%= DBMgr.getRecordValue(residentRec, "DESCRIPTION") %>" class="long" readonly="readonly" />
                    </td>
                </tr> --%>
                <tr>
                    <th colspan="6" class="buttonBar">                        
                        <input type="submit" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}"  />
                        <input type="button" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='transaction_edit.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
                <tr>
                	 <td class="label"><label for="HN_NO">${labelMap.DOCTOR_CODE}</label></td>
                	 <td colspan="3" class="label" style="text-align: left;">
                	 	 <input type="checkbox" name="ACTION_NEW_DOCTOR_CODE" id="ACTION_NEW_DOCTOR_CODE" value="1"/>
                	 	 <input type="text" name="NEW_DOCTOR_CODE" id="NEW_DOCTOR_CODE" onkeyup="actionNewDoctorCode()"/>
                	 </td>
                </tr>
                <tr>
                	<td class="label"><label for="HN_NO">${labelMap.ORDER_ITEM_CODE}</label></td>
                	<td colspan="3" class="label" style="text-align: left;">
                		 <input type="checkbox" name="ACTION_NEW_ORDER_ITEM_CODE" id="ACTION_NEW_ORDER_ITEM_CODE" value="1"/>
                		 <input type="text" name="NEW_ORDER_ITEM_CODE" id="NEW_ORDER_ITEM_CODE" onkeyup="actionNewOrderItem()"/>
                	</td>	
                </tr>
              </table>
        </form>
        <hr/>
        <form id="dataForm" name="dataForm" method="post" action="transaction_edit_update_all.jsp">
        <input type="hidden" id="MODE" name="MODE" value="<%= DBMgr.MODE_QUERY %>" />
            <table class="data">
                <tr>
					<th colspan="13" class="buttonBar"><input type="button" id="UPDATE" name="UPDATE" class="button" value="${labelMap.SAVE}" onclick="save_data();" <%=showButtonSave %>/></th>
                </tr>
                <tr>
                    <td class="sub_head">${labelMap.INVOICE_NO}</td>
                    <td class="sub_head">${labelMap.INVOICE_DATE}</td>
                    <td class="sub_head">${labelMap.ORDER_ITEM_CODE}</td>
                    <td class="sub_head">${labelMap.Status}</td>
                    <td class="sub_head">${labelMap.LINE_NO}</td>
                    <td class="sub_head">${labelMap.DOCTOR_CODE}</td>
                    <td class="sub_head">${labelMap.AMOUNT_AFT_DISCOUNT}</td>
                    <td class="sub_head">${labelMap.NOTE}</td>
                    <td class="sub_head">${labelMap.DETAIL}</td>
                </tr>
                <%
                if (request.getParameter("MODE") != null) {

                    String cond = "";
                    
                    if (request.getParameter("INVOICE_NO") != null && !request.getParameter("INVOICE_NO").equalsIgnoreCase("")) {
                        cond += " AND INVOICE_NO = '" + request.getParameter("INVOICE_NO") + "'";
                    }
                    
                    if (request.getParameter("INVOICE_DATE") != null && !request.getParameter("INVOICE_DATE").equalsIgnoreCase("")) {
                        cond += " AND INVOICE_DATE = '" + JDate.saveDate(request.getParameter("INVOICE_DATE")) + "'";
                    }
                    
                    if (request.getParameter("PAYOR_OFFICE_CODE") != null && !request.getParameter("PAYOR_OFFICE_CODE").equalsIgnoreCase("")) {
                        cond += " AND PAYOR_OFFICE_CODE = '" + request.getParameter("PAYOR_OFFICE_CODE") + "'";
                    }
                    
                    if (request.getParameter("DOCTOR_CODE") != null && !request.getParameter("DOCTOR_CODE").equalsIgnoreCase("")) {
                        cond += " AND DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") + "'";
                    }
                    
                    if (request.getParameter("HN_NO") != null && !request.getParameter("HN_NO").equalsIgnoreCase("")) {
                        cond += " AND HN_NO = '" + request.getParameter("HN_NO") + "'";
                    }
                    
                    if (request.getParameter("ORDER_ITEM_CODE") != null && !request.getParameter("ORDER_ITEM_CODE").equalsIgnoreCase("")) {
                        cond += " AND ORDER_ITEM_CODE = '" + request.getParameter("ORDER_ITEM_CODE") + "'";
                    }
                    
                    if (request.getParameter("RESIDENT_TYPE") != null && !request.getParameter("RESIDENT_TYPE").equalsIgnoreCase("")) {
                        cond += " AND NATIONALITY_DESCRIPTION = '" + request.getParameter("RESIDENT_TYPE") + "'";
                    }
                    
                    if (request.getParameter("FROM_DATE") != null && !request.getParameter("FROM_DATE").equalsIgnoreCase("") && request.getParameter("TO_DATE") != null && !request.getParameter("TO_DATE").equalsIgnoreCase("")) {
                        cond += " AND TRANSACTION_DATE  BETWEEN '" + JDate.saveDate(request.getParameter("FROM_DATE")) + "' AND '" + JDate.saveDate(request.getParameter("TO_DATE")) + "'";
                    }
                    
                    query = "SELECT INVOICE_NO, INVOICE_DATE, RECEIPT_DATE , LINE_NO, PAYOR_OFFICE_CODE, DOCTOR_CODE, ORDER_ITEM_CODE, HN_NO,"
                    +" PATIENT_NAME, AMOUNT_BEF_DISCOUNT, AMOUNT_AFT_DISCOUNT, ACTIVE, NOTE, TRANSACTION_DATE"
                    +" FROM TRN_DAILY "
                    +" WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' "
                    +" AND (BATCH_NO IS NULL OR BATCH_NO = '')" 
                    +" AND (GUARANTEE_NOTE IS NULL OR GUARANTEE_NOTE= '')"
                    //+" AND (COMPUTE_DAILY_DATE = '' OR COMPUTE_DAILY_DATE IS NULL) "
                    +" AND IS_WRITE_OFF = 'N' "
                    //+" AND INVOICE_TYPE <> 'ORDER' "
                    + cond + " ORDER BY TRN_DAILY.INVOICE_NO , TRN_DAILY.LINE_NO ";
                    System.out.println(query); 
                    ResultSet rs = con.executeQuery(query);
                    
                    int i = 0;
                    DBConn cdb = new DBConn();
                	cdb.setStatement();
                	String [][]TrnArr = cdb.query(query);
                    int num_trn=0;
                    num_trn = TrnArr.length;
                    if(num_trn == 0) {
						showButtonSave="disabled";
					}
                	while (rs.next()) {
                    %>
                	<tr>
	                    <td class="row<%=i % 2%> alignCenter">
	                    	<input type="hidden" name="RECEIPT_DATE[]" id="RECEIPT_DATE[]" value="<%= Util.formatHTMLString(rs.getString("RECEIPT_DATE"), true)%>"/>
	                		<input type="hidden" name="AMT_BEF_DIS[]" id="AMT_BEF_DIS[]" value="<%= Util.formatHTMLString(rs.getString("AMOUNT_BEF_DISCOUNT"), true)%>"/>
							<input type="hidden" name="INVOICE_NUMBER[]" id="INVOICE_NUMBER[]" value="<%= Util.formatHTMLString(rs.getString("INVOICE_NO"), true)%>"/>
							<input type="hidden" name="TRANSACTION_DATE[]" id="TRANSACTION_DATE[]" value="<%= Util.formatHTMLString(rs.getString("TRANSACTION_DATE"), true)%>"/>
	                    	<input type="hidden" id="DIS[]" name="DIS[]" value="<%=rs.getString("LINE_NO")%>"/>
	                    	<%=Util.formatHTMLString(rs.getString("INVOICE_NO"), true)%>
	                    </td>
	                    <td class="row<%=i % 2%>"><%=JDate.showDate(rs.getString("INVOICE_DATE"))%></td>
	                    <td class="row<%=i % 2%>">
	                    	 <input type="text" id='ORDER_ITEM_CODE[]' class="NEW_ORDER_ITEM_CODE" name="ORDER_ITEM_CODE[]" value="<%=Util.formatHTMLString(rs.getString("ORDER_ITEM_CODE"), true)%>" style="width:70px;"/>
	                    </td>
	                    <td class="row<%=i % 2%> alignCenter">
	                    	<input type="checkbox"  name="chkACTIVE_ARR[]" id="chkACTIVE_ARR[]" value="<%=rs.getString("ACTIVE")%>" onclick="checkActive(this)" <%=("1".equals(rs.getString("ACTIVE"))?"checked":"")%> />
	                    	<input type="hidden" size="4" name="ACTIVE_ARR[]" id="ACTIVE_ARR[]"  value="<%=rs.getString("ACTIVE")%>" />
	                    </td>
	                    <td class="row<%=i % 2%>">
	                    	<input type="hidden" size="10" name="LINE_NO_ARR[]" id="LINE_NO_ARR[]" value="<%=rs.getString("LINE_NO")%>"/>
	                    	<%=rs.getString("LINE_NO") %>
	                    </td>
	                    <td class="row<%=i % 2%> alignCenter">
	                    	<input type="text" size="7" name="DOCTOR_CODE_ARR[]" id="DOCTOR_CODE_ARR[]" class="NEW_DOCTOR_CODE" value="<%= Util.formatHTMLString(rs.getString("DOCTOR_CODE"), true)%>" />
	                    </td>
	                    <td class="row<%=i % 2%>">
	                    	<input type="text" size="4" name="AMOUNT_AFT_DISCOUNT_ARR[]" id="AMOUNT_AFT_DISCOUNT_ARR[]" onkeypress="IsNumericKeyPress(event);" value="<%=rs.getString("AMOUNT_AFT_DISCOUNT")%>" />
	                    </td>
	                    <td class="row<%=i % 2%>"><textarea name="NOTE_ARR[]" id="NOTE_ARR[]" rows="2" cols="6" style="padding:0px; margin:0px;	"><%=rs.getString("NOTE")%></textarea></td>
	                    <td class="row<%=i % 2%>"><a href='#' onclick="SetNewSize('transaction_detail.jsp?LINE_NO=<%=Util.formatHTMLString(rs.getString("LINE_NO"), true) %>&INVOICE_NO=<%= Util.formatHTMLString(rs.getString("INVOICE_NO"), true)%>&RECEIPT_DATE=<%= Util.formatHTMLString(rs.getString("RECEIPT_DATE"), true)%>','600','900');">Detail</a></td>
             		</tr>
                	<%
                       i++;
                	   table_row++;
                    }
                	
                    if (rs != null) {
                        rs.close();
                    }
                    
                    con.Close();
                }
                %>                
                <tr>
                    <th colspan="13" class="buttonBar">                        
                        <input type="button" id="UPDATE" name="UPDATE" class="button" value="${labelMap.SAVE}" onclick="save_data();" <%=showButtonSave %>/>
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>