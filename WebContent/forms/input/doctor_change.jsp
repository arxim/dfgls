<%@page import="df.bean.process.ProcessDistributeRevenueCode"%>
<%@page import="java.util.HashMap"%>
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
	String showButtonSave = "";

	Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), con);
	String batch_year = b.getYyyy();
	String batch_month = b.getMm();

	String sqlGurantee = "SELECT DISTINCT YYYY, MM FROM SUMMARY_GUARANTEE  WHERE YYYY='"
			+ batch_year
			+ "' AND MM='"
			+ batch_month
			+ "' AND HOSPITAL_CODE = '"
			+ session.getAttribute("HOSPITAL_CODE").toString() + "'";

	String[][] arrBatch = conData.query(sqlGurantee);
	if (arrBatch.length != 0) {
		showButtonSave = "disabled";
	}

	if (session.getAttribute("LANG_CODE") == null) {
		session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
	}

	String dt = JDate.showDate(JDate.getDate());
	LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	labelMap.add("TITLE_MAIN", "Distribute Revenue Doctor Profile","ส่วยจัดการ Change data code .");
	labelMap.add("TRANSACTION_DATE_START", "Trnasaction date start",	"วันที่เริ่มต้น");
	labelMap.add("TRANSACTION_DATE_END", "Transactioin date end","วันที่สิ้นสุด");
	labelMap.add("DOCTOR_PROFILE_CODE", "Doctor Profile code",	"รหัสแพทย์");
	labelMap.add("VERIFY_DATE", "Verify date", "วันที่แพทย์ Excute");
	labelMap.add("INVOICE_NO" , "Inv No." , "เลขที่ใบเสร็จ");
	labelMap.add("LINE_NO", "Line No", "Line No");
	labelMap.add("DOCTOR_CODE_BEFOR", "Dr Code [Before]",	"รหัสแพทย์ [ ก่อน ]");
	labelMap.add("OLD_DOCTOR_CODE", "Dr Code [After]","รหัสแพทย์ [ หลัง ]");
	labelMap.add("AMOUNT_AFT_DISCOUNT", "Amount After Discount","ส่วนแบ่งแพทย์");
	labelMap.add("DR_AMT", "Dr. Amt", "ยอดหลังแบ่งค่าแพทย์");
	labelMap.add("ORDER_ITEM_CODE", " Order Item", "รหัสรายการรักษา");
	labelMap.add("ORDER_ITEM_CATEGORY_CODE" , "Order Item Category" , "กลุ่มรายการรักษา");
	labelMap.add("PAYOR_OFFICE_CODE", "Payor Office ", "บริษัทคู่สัญญา");
	labelMap.add("HN_NO", "HN No.", "รหัสผู้ป่วย");
	labelMap.add("DETAIL", "Detail", "Detail");
	labelMap.add("BTN_PROCESS", "Process", "เริ่มทำงาน");
	labelMap.add("PROCESS" ,  "Process" , " ทำงาน ");

	request.setAttribute("labelMap", labelMap.getHashMap());

	//
	// Process request
	//

	request.setCharacterEncoding("UTF-8");
	DataRecord payorOfficeRec = null, doctorRec = null, hnNoRec = null, orderRec = null;
	String query = "";

	int table_row = 0;
	
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
        <script type="text/javascript" src="../../javascript/jquery-1.6.min.js"></script>
      
        <script type="text/javaScript">
        
        var hospital_code   =  "<%=session.getAttribute("HOSPITAL_CODE")%>";
        var lang =  "<%=labelMap.getFieldLangSuffix()%>";
        
        // -----------------  Doctor Profile Search -----------------------
        function DOCTOR_PROFILE_CODE_KeyPress(e) {
			var key = window.event ? window.event.keyCode : e.which; // ? IE : Firefox
				if (key == 13) {
						document.mainForm.DOCTOR_PROFILE_CODE.blur();
						return false;
				} else {
						return true;
				}
		}

		function AJAX_Refresh_DOCTOR_PROFILE_CODE() {
		 	var doctor_profile_code  =  document.mainForm.DOCTOR_PROFILE_CODE.value;
			var target = "../../RetrieveData?TABLE=DOCTOR_PROFILE&COND=CODE='" +  doctor_profile_code	+ "' AND HOSPITAL_CODE='" + hospital_code + "'";
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
                document.mainForm.DOCTOR_PROFILE_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + lang);
            }
        }
        
        
        // -----------------  Payor Office Profile Search -----------------------
        function PAYOR_OFFICE_CODE_KeyPress(e) {
			var key = window.event ? window.event.keyCode : e.which; // ? IE : Firefox
				if (key == 13) {
						document.mainForm.PAYOR_OFFICE_CODE.blur();
						return false;
				} else {
						return true;
				}
		}

		function AJAX_Refresh_PAYOR_OFFICE() {
		 	var payor_office_code  =  document.mainForm.PAYOR_OFFICE_CODE.value;
			var target = "../../RetrieveData?TABLE=PAYOR_OFFICE&COND=CODE='" +  payor_office_code	+ "' AND HOSPITAL_CODE='" + hospital_code + "'";
			AJAX_Request(target, AJAX_Handle_Refresh_PAYOR_OFFICE_CODE);
        }
        
        function AJAX_Handle_Refresh_PAYOR_OFFICE_CODE() {
            if (AJAX_IsComplete()) {
                var xmlDoc = AJAX.responseXML;

                if (!isXMLNodeExist(xmlDoc, "CODE")) {
                    document.mainForm.PAYOR_OFFICE_CODE.value = "";
                    document.mainForm.PAYOR_OFFICE_NAME.value = "";
                    return;
                }
                
                // Data found
                document.mainForm.PAYOR_OFFICE_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + lang);
            }
        }
        
        	
        // -----------------  Payor Office Profile Search -----------------------
        function PAYOR_OFFICE_CODE_KeyPress(e) {
			var key = window.event ? window.event.keyCode : e.which; // ? IE : Firefox
				if (key == 13) {
						document.mainForm.PAYOR_OFFICE_CODE.blur();
						return false;
				} else {
						return true;
				}
		}

		function AJAX_Refresh_PAYOR_OFFICE() {
		 	var payor_office_code  =  document.mainForm.PAYOR_OFFICE_CODE.value;
			var target = "../../RetrieveData?TABLE=PAYOR_OFFICE&COND=CODE='" +  payor_office_code	+ "' AND HOSPITAL_CODE='" + hospital_code + "'";
			AJAX_Request(target, AJAX_Handle_Refresh_PAYOR_OFFICE_CODE);
        }
        
        function AJAX_Handle_Refresh_PAYOR_OFFICE_CODE() {
            if (AJAX_IsComplete()) {
                var xmlDoc = AJAX.responseXML;

                if (!isXMLNodeExist(xmlDoc, "CODE")) {
                    document.mainForm.PAYOR_OFFICE_CODE.value = "";
                    document.mainForm.PAYOR_OFFICE_NAME.value = "";
                    return;
                }
                
                // Data found
                document.mainForm.PAYOR_OFFICE_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + lang);
            }
        }
        
     // -----------------  Order Item Search -----------------------
        function ORDER_ITEM_CODE_KeyPress(e) {
			var key = window.event ? window.event.keyCode : e.which; // ? IE : Firefox
				if (key == 13) {
						document.mainForm.ORDER_ITEM_CODE.blur();
						return false;
				} else {
						return true;
				}
		}

		function AJAX_Refresh_ORDER_ITEM_CODE() {
		 	var order_item_code  =  document.mainForm.ORDER_ITEM_CODE.value;
			var target = "../../RetrieveData?TABLE=ORDER_ITEM&COND=CODE='" +  order_item_code	+ "' AND HOSPITAL_CODE='" + hospital_code + "'";
			AJAX_Request(target, AJAX_Handle_Refresh_ORDER_ITEM_CODE);
        }
        
        function AJAX_Handle_Refresh_ORDER_ITEM_CODE() {
            if (AJAX_IsComplete()) {
                var xmlDoc = AJAX.responseXML;

                if (!isXMLNodeExist(xmlDoc, "CODE")) {
                    document.mainForm.ORDER_ITEM_CODE.value = "";
                    document.mainForm.ORDER_ITEM_NAME.value = "";
                    return;
                }
                
                // Data found
                document.mainForm.ORDER_ITEM_NAME.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_" + lang);
            }
        }

        
        // -----------------  Order Item  Categofy Search -----------------------
        function ORDER_ITEM_CATEGORY_CODE_KeyPress(e) {
			var key = window.event ? window.event.keyCode : e.which; // ? IE : Firefox
				if (key == 13) {
						document.mainForm.ORDER_ITEM_CATEGORY_CODE.blur();
						return false;
				} else {
						return true;
				}
		}

		function AJAX_Refresh_ORDER_ITEM_CATEGORY_CODE() {
		 	var order_item_category_code  =  document.mainForm.ORDER_ITEM_CATEGORY_CODE.value;
			var target = "../../RetrieveData?TABLE=ORDER_ITEM_CATEGORY&COND=CODE='" +  order_item_category_code	+ "' AND HOSPITAL_CODE='" + hospital_code + "'";
			AJAX_Request(target, AJAX_Handle_Refresh_ORDER_ITEM_CATEGORY_CODE);
        }
        
        function AJAX_Handle_Refresh_ORDER_ITEM_CATEGORY_CODE() {
            if (AJAX_IsComplete()) {
                var xmlDoc = AJAX.responseXML;

                if (!isXMLNodeExist(xmlDoc, "CODE")) {
                    document.mainForm.ORDER_ITEM_CATEGORY_CODE.value = "";
                    document.mainForm.ORDER_ITEM_CATEGORY_NAME.value = "";
                    return;
                }
                
                // Data found
                document.mainForm.ORDER_ITEM_CATEGORY_NAME.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_" + lang);
            }
        }
        
     // -----------------  HN + PATIENT Search -----------------------
        function PATIENT_NAME_KeyPress(e) {
			var key = window.event ? window.event.keyCode : e.which; // ? IE : Firefox
				if (key == 13) {
						document.mainForm.HN_NO.blur();
						return false;
				} else {
						return true;
				}
		}

		function AJAX_Refresh_PATIENT_NAME() {
		 	var hn_no  =  document.mainForm.HN_NO.value;
			var target = "../../RetrieveData?TABLE=TRN_DAILY&COND=HN_NO='" +  hn_no	+ "' AND HOSPITAL_CODE='" + hospital_code + "'";
			AJAX_Request(target, AJAX_Handle_Refresh_PATIENT_NAME);
        }
        
        function AJAX_Handle_Refresh_PATIENT_NAME() {
            if (AJAX_IsComplete()) {
                var xmlDoc = AJAX.responseXML;

                if (!isXMLNodeExist(xmlDoc, "HN_NO")) {
                    document.mainForm.HN_NO.value = "";
                    document.mainForm.PATIENT_NAME.value = "";
                    return;
                }
                
                // Data found
                document.mainForm.PATIENT_NAME.value = getXMLNodeValue(xmlDoc,"PATIENT_NAME");
            }
        }
        
        
        function getDataList(){
        	
        	var msg =  '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>';
        	
        	  if (!isObjectEmptyString(document.mainForm.TRANSACTION_DATE_START, msg) && 
                      !isObjectEmptyString(document.mainForm.TRANSACTION_DATE_END, msg) && 
                      !isObjectEmptyString(document.mainForm.DOCTOR_PROFILE_CODE, msg)) {
        		  	 
        		  	 document.mainForm.ACTION.value =  "SELECT";
        		  	 
        		  	 document.mainForm.submit();
        		  
        	  }
        	
        }
        
		 function getProcess(){
		        	
		        	var msg =  '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>';
		        	
		        	  if (!isObjectEmptyString(document.mainForm.TRANSACTION_DATE_START, msg) && 
		                      !isObjectEmptyString(document.mainForm.TRANSACTION_DATE_END, msg) && 
		                      !isObjectEmptyString(document.mainForm.DOCTOR_PROFILE_CODE, msg)) {
		        		  	 
		        		  	document.mainForm.ACTION.value =  "PROCESS";
		        		  	 
		        		    document.mainForm.submit();
		        		  
		        	  }
        	
        }
 
        
        function SetNewSize(new_location,height,width){
        	
        	var resolution_height = screen.availHeight; 
			var resolution_width  = screen.availWidth; 
			var win_height = height;
			var win_width =  width;
			var xpos  = ( resolution_width  - win_width  ) / 2;
			var ypos  = ( resolution_height - win_height ) / 2;
		
			windows_parameter = 'resizable=no,scrollbars=yes,width='+win_width+', height='+win_height+',left='+xpos.toFixed(0)+',top='+ypos.toFixed(0);
			windows = window.open(new_location,'', windows_parameter);
		}
        
        function save_data(){
        	document.dataForm.submit();
        }
          
    </script>
	
	<style type="text/css">
		<!--
		.style1 {color: #003399}
		.style2 {color: #033}
		-->
	</style>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="doctor_change.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=DBMgr.MODE_INSERT%>" />
            <input type="hidden" id="ACTION" name="ACTION" value=""/>
            <center>
                <table width="800" border="0">
                    <tr>
                    	<td align="left">
                   	 		<b>
                   	 			<font color='#003399'>
                   	 				<%=Utils.getInfoPage("doctor_change.jsp",	labelMap.getFieldLangSuffix(), new DBConnection(""+ session.getAttribute("HOSPITAL_CODE")))%>
                   	 			</font>
                   	 		</b>
                    	</td>
                    </tr>
				</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>
                <tr>
                    <td class="label">
                    	<label for="TRANSACTION_DATE_START">
                    		${labelMap.TRANSACTION_DATE_START}</label>
                    	</td>
                    <td class="input">
           					<input type="text"  name="TRANSACTION_DATE_START"  id="TRANSACTION_DATE_START"  class="short"  value="${param.TRANSACTION_DATE_START}"/>
           					<input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('TRANSACTION_DATE_START'); return false;" />
                    </td>
                    <td class="label">
                        <label for="TRANSACTION_DATE_END">
                        	${labelMap.TRANSACTION_DATE_END}
                        </label>
                    </td>
                    <td class="input">
                        <input   type="text"   name="TRANSACTION_DATE_END" id="TRANSACTION_DATE_END"  class="short"  maxlength="10" value="${param.TRANSACTION_DATE_END}" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('TRANSACTION_DATE_END'); return false;" />
                    </td>
                </tr>
                <tr>
                    <td class="label">
                    		<label for="DOCTOR_PROFILE_CODE">
                    			${labelMap.DOCTOR_PROFILE_CODE}
                    		</label>
                    	</td>
                    <td colspan="3" class="input">
                        <input type="text"  name="DOCTOR_PROFILE_CODE"  id="DOCTOR_PROFILE_CODE"  class="short"  value="${param.DOCTOR_PROFILE_CODE}"  onblur="AJAX_Refresh_DOCTOR_PROFILE_CODE();" onkeypress="return DOCTOR_PROFILE_CODE_KeyPress(event);"/>
                        <input id="SEARCH_DOCTOR_PROFILE_CODE" name="SEARCH_DOCTOR_PROFILE_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_PROFILE&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&TARGET=DOCTOR_PROFILE_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_PROFILE_CODE'); return false;" />
                        <input type="text" id="DOCTOR_PROFILE_NAME" name="DOCTOR_PROFILE_NAME"  class="long"  readonly="readonly"  value="${param.DOCTOR_PROFILE_NAME}" style="width: 60%;"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="PAYOR_OFFICE_CODE">${labelMap.PAYOR_OFFICE_CODE}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input name="PAYOR_OFFICE_CODE" type="text" class="short" id="PAYOR_OFFICE_CODE" maxlength="20" value="${param.PAYOR_OFFICE_CODE}"  onkeypress="return PAYOR_OFFICE_CODE_KeyPress(event);" onblur="AJAX_Refresh_PAYOR_OFFICE();" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=PAYOR_OFFICE&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=PAYOR_OFFICE_CODE&HANDLE=AJAX_Refresh_PAYOR_OFFICE'); return false;" />
                        <input name="PAYOR_OFFICE_NAME" type="text" class="long" id="PAYOR_OFFICE_NAME" readonly="readonly" value="${param.PAYOR_OFFICE_NAME}" maxlength="255" style="width: 60%;"/>                    
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ORDER_ITEM_CODE">${labelMap.ORDER_ITEM_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="ORDER_ITEM_CODE" name="ORDER_ITEM_CODE" class="short" value="${param.ORDER_ITEM_CODE}" onkeypress="return ORDER_ITEM_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM_CODE();" />
                        <input id="SEARCH_ORDER_ITEM_CODE" name="SEARCH_ORDER_ITEM_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&TARGET=ORDER_ITEM_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_ORDER_ITEM_CODE'); return false;" />
                        <input type="text" id="ORDER_ITEM_NAME" name="ORDER_ITEM_NAME" class="long" readonly="readonly" value="${param.ORDER_ITEM_NAME}" style="width: 60%;"/>
                    </td>
                </tr>
                 <tr>
                    <td class="label"><label for="ORDER_ITEM_CATEGORY_CODE">${labelMap.ORDER_ITEM_CATEGORY_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="ORDER_ITEM_CATEGORY_CODE" name="ORDER_ITEM_CATEGORY_CODE" class="short" value="${param.ORDER_ITEM_CATEGORY_CODE}" onkeypress="return ORDER_ITEM_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM_CATEGORY_CODE();" />
                        <input id="SEARCH_ORDER_ITEM_CATEGORY_CODE" name="SEARCH_ORDER_ITEM_CATEGORY_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM_CATEGORY&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&TARGET=ORDER_ITEM_CATEGORY_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_ORDER_ITEM_CATEGORY_CODE'); return false;" />
                        <input type="text" id="ORDER_ITEM_CATEGORY_NAME" name="ORDER_ITEM_CATEGORY_NAME" class="long" readonly="readonly" value="${param.ORDER_ITEM_CATEGORY_NAME}" style="width: 60%;"/>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="HN_NO">${labelMap.HN_NO}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="HN_NO" name="HN_NO" class="short" value="${param.HN_NO}" onkeypress="return HN_NO_KeyPress(event);" onblur="AJAX_Refresh_PATIENT_NAME();" />
                        <input id="SEARCH_HN_NO" name="SEARCH_HN_NO" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=TRN_DAILY&RETURN_FIELD=HN_NO&DISPLAY_FIELD=PATIENT_NAME&BEINSIDEHOSPITAL=1&TARGET=HN_NO&HANDLE=AJAX_Refresh_PATIENT_NAME'); return false;" />
                        <input type="text" id="PATIENT_NAME" name="PATIENT_NAME" value="${param.PATIENT_NAME}" class="long" readonly="readonly" style="width: 60%;"/>
                    </td>
                </tr>
                <tr>
                    <th colspan="6" class="buttonBar">                        
                        <input type="button"  id="SELECT" name="SELECT" class="button"  value="${labelMap.SELECT}"  onclick="return getDataList()"/>
                        <input type="button"  id="SELECT" name="SELECT" class="button"  value="${labelMap.PROCESS}"  onclick="return getProcess()"/>
                        <input type="button" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='doctor_change.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
        </form>
        
        
        <hr/>
        
        <form id="dataForm" name="dataForm" method="post" action="doctor_change.jsp" onsubmit="save_data()">
        
        	<input type="hidden"  id="TRANSACTION_DATE_START"  name="TRANSACTION_DATE_START"  value="${param.TRANSACTION_DATE_START}"/>
        	<input type="hidden"  id="TRANSACTION_DATE_END"  name="TRANSACTION_DATE_END"  value="${param.TRANSACTION_DATE_END}" />
        	<input type="hidden"  id="DOCTOR_PROFILE_CODE"  name="DOCTOR_PROFILE_CODE"  value="${param.DOCTOR_PROFILE_CODE}"/>
        	<input type="hidden"  id="DOCTOR_PROFILE_NAME"  name="DOCTOR_PROFILE_NAME"  value="${param.DOCTOR_PROFILE_NAME}"/>
        	
            <input type="hidden" id="MODE" name="MODE" value="<%=DBMgr.MODE_QUERY%>" />
        
            <table class="data">
                <tr>             
                	<!-- <td class="sub_head">Index</td> -->
                    <td class="sub_head">${labelMap.VERIFY_DATE}</td>
                    <td class="sub_head">${labelMap.INVOICE_NO}</td>
                 	<td class="sub_head">${labelMap.LINE_NO}</td>
                 	<td class="sub_head">${labelMap.OLD_DOCTOR_CODE}</td>
                 	<td class="sub_head">${labelMap.DOCTOR_CODE}</td>
                 	<td class="sub_head">${labelMap.AMOUNT_AFT_DISCOUNT}</td>
                 	<td class="sub_head">${labelMap.DR_AMT }</td>
                    <td class="sub_head">${labelMap.DETAIL}</td>
                </tr>
                <%
                
                	
                	if (request.getParameter("MODE") != null) {

                		/**
                		 * 	1 TRN_START_DATE , TRN_END_DATE 
                		 *		2 DOCTOR_PROFILE_CODE
                		 * 	3 PAYOR_OFFICE_CODE
                		 * 	4 ORDER_ITEM_CODE
                		 * 	5 HN_NO 
                		 * 	6 HOSPITAL_CODE
                		*/
                	
                		 DBConn d = new DBConn();
                    	
                    	 HashMap<String, String> condition = new HashMap<String, String>();
                    	 
                    	 
                    	 condition.put("HOSPITAL_CODE", session.getAttribute("HOSPITAL_CODE").toString());
                    	 condition.put("TRN_START_DATE" , JDate.saveDate(request.getParameter("TRANSACTION_DATE_START") != null ? request.getParameter("TRANSACTION_DATE_START") : "00/00/0000"));
                    	 condition.put("TRN_END_DATE" ,  JDate.saveDate(request.getParameter("TRANSACTION_DATE_END") != null ? request.getParameter("TRANSACTION_DATE_END") : "00/00/0000"));
                    	 condition.put("DOCTOR_PROFILE_CODE" ,  request.getParameter("DOCTOR_PROFILE_CODE"));
                    	 condition.put("PAYOR_OFFICE_CODE", request.getParameter("PAYOR_OFFICE_CODE"));
                    	 condition.put("ORDER_ITEM_CODE" , request.getParameter("ORDER_ITEM_CODE"));
                    	 condition.put("ORDER_ITEM_CATEGORY_CODE" ,  request.getParameter("ORDER_ITEM_CATEGORY_CODE"));
                    	 condition.put("HN_NO" , request.getParameter("HN_NO"));
                    	 condition.put("ACTION" , request.getParameter("ACTION") !=  null ? request.getParameter("ACTION")  :  "");
                    	 condition.put("YYYY", batch_year);
                    	 condition.put("MM", batch_month);
                    	ProcessDistributeRevenueCode  obj  = new  ProcessDistributeRevenueCode(d, condition);
                        obj.doProcess();
                        // int index_i = 0;
                    	
                    	for(int i = 0 ; i <= (obj.getData().length -1); i++){
                    		
                    		//System.out.println(i+"|"+ JDate.showDate(obj.getData()[i][0]) +  "|" + obj.getData()[i][1]  + "|" + obj.getData()[i][2] +  "|" + obj.getData()[i][3] + "|" + obj.getData()[i][4] + "|" + obj.getData()[i][5]);
                    		
                    		out.print("<tr>");
                    		
                    		// out.print("<td align='center'>" + i + "</td>");
                    	 	out.print("<td>");
                    	 	out.print("<input type='hidden' id='KEY_DIS[]' name='KEY_DIS[]' value='" +obj.getData()[i][2]+ "' />");
	                    	out.print("<input type='hidden' id='INVOICE_NO[]' name='INVOICE_NO[]' value='" + obj.getData()[i][1]+ "' />");
                    		out.print(JDate.showDate(obj.getData()[i][0]));
	                    	out.print("</td>");
	                    	
	                    	out.print("<td>");
                    	 	out.print(obj.getData()[i][1]);
	                    	out.print("</td>");
	                    	
	                    	out.print("<td>");
                    	 	out.print(obj.getData()[i][2]);
	                    	out.print("</td>");
	                    	
	                    	out.print("<td>");
                    	 	out.print(obj.getData()[i][3]);
	                    	out.print("</td>");
	                    	
	                    	out.print("<td>");
                    	 	out.print("<input type='text' name='NEW_DOCTOR[]' value='" + obj.getData()[i][4] + "' style='width:100px;'/>");
	                    	out.print("</td>");
                    	
	                    	out.print("<td style='text-align:right;' style='width:100px;'>");
                    	 	out.print(obj.getData()[i][5]);
	                    	out.print("</td>");
	                    	
	                    	out.print("<td style='text-align:right;'>");
                    	 	out.print(obj.getData()[i][6]);
	                    	out.print("</td>");
	                    	
	                    	out.print("<td>");
	                    	out.print("<a href='#' onclick=\"SetNewSize('transaction_detail.jsp?LINE_NO=" + obj.getData()[i][2] + "&INVOICE_NO=" + obj.getData()[i][1] + "','600','900');\">");
	                    	out.print("Detail");
	                    	out.print("</a>");
	                    	out.print("</td>");
	                    		
                    		out.print("</tr>");
                    	}
                	}
                		%>
                <tr>
                    <th colspan="13" class="buttonBar">                        
                        <input type="button" id="UPDATE" name="UPDATE" class="button" value="${labelMap.SAVE}" onclick="save_data()"/>
                    </th>
                </tr>
            </table>
        </form>
        
        <%
		if (request.getParameterValues("NEW_DOCTOR[]") != null) {

			try {

				DBConn conn = new DBConn();
				conn.setStatement();

				String hospital_code = session.getAttribute("HOSPITAL_CODE").toString();

				String[] tempDataLineNo = request.getParameterValues("KEY_DIS[]");
				String[] tempInvoiceNo = request.getParameterValues("INVOICE_NO[]");
				String[] newDoctor = request.getParameterValues("NEW_DOCTOR[]");
				
				// Redirect Parameter
				String doctor_profile_code =  request.getParameter("DOCTOR_PROFILE_CODE");
			    String transaction_date_start =  request.getParameter("TRANSACTION_DATE_START");
			    String transaction_date_end  = request.getParameter("TRANSACTION_DATE_END");
				
				int dataSize = newDoctor.length;

				int index = 0;

				for (int i = 0; i < dataSize; i++) {

					String sqlCommand = "";

					sqlCommand += " UPDATE  TRN_DAILY  SET "
							+ " DOCTOR_CODE  =  '" + newDoctor[i] + "' "
							+ " WHERE LINE_NO = '" + tempDataLineNo[i]
							+ "' AND INVOICE_NO = '" + tempInvoiceNo[i]
							+ "' " + " AND HOSPITAL_CODE = '"
							+ hospital_code + "' "
							+ " AND ( BATCH_NO = '' OR BATCH_NO IS NULL ) "
							+ " AND MM  = '" + batch_month + "' AND YYYY = '" + batch_year + "' "
							+ " AND ACTIVE  = 1 AND IS_ONWARD <> 'Y' ";

					conn.insert(sqlCommand);
					conn.commitDB();
				}
		
			} catch (Exception e) {
				System.out.println(e.getMessage().toString());
			}
		}
	%>
	
      </body>
</html>