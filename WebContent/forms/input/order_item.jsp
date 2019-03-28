<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>

<%@page import="java.sql.Types"%>

<%@ include file="../../_global.jsp" %>

<%
//
// Verify permission
//

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_ORDER_ITEM)) {
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
            labelMap.add("TITLE_MAIN", "Order Item", "Order Item");
            labelMap.add("CODE", "Code", "รหัส");
            labelMap.add("DESCRIPTION_THAI", "Description (Thai)", "ชื่อ (ไทย)");
            labelMap.add("DESCRIPTION_ENG", "Description (Eng)", "ชื่อ (อังกฤษ)");
            labelMap.add("IS_STEP_COMPUTE", "Allocate Step", "คำนวณส่วนแบ่งขั้นบันได");
            labelMap.add("IS_STEP_COMPUTE_0", "No", "ไม่ใช่");
            labelMap.add("IS_STEP_COMPUTE_1", "Yes", "ใช่");
            labelMap.add("PAYMENT_TIME", "Time to Payment", "ทำจ่ายสิ้นเดือน");
            labelMap.add("PAYMENT_TIME_0", "2 Time", "2 ครั้ง");
            labelMap.add("PAYMENT_TIME_1", "1 Time ", "1 ครั้ง ");
            labelMap.add("ORDER_ITEM_CATEGORY_CODE", "Order Item Category", "กลุ่มรายการรักษา");
            labelMap.add("ACCOUNT_CODE", "Account Code", "รหัสลงบัญชี");
            labelMap.add("IS_COMPUTE", "Allocate Revenue", "คำนวณจ่ายค่าแพทย์");
            labelMap.add("IS_COMPUTE_0", "No", "ไม่คำนวณ");
            labelMap.add("IS_COMPUTE_1", "Yes", "คำนวณ");
            labelMap.add("IS_ALLOC_FULL_TAX", "Revenue For Tax", "รายได้นำไปยื่นภาษี");
            labelMap.add("IS_ALLOC_FULL_TAX_0", "After Allocate", "ยอดหลังแบ่ง");
            labelMap.add("IS_ALLOC_FULL_TAX_1", "Before Allocate", "ยอดก่อนแบ่ง");
            labelMap.add("IS_GUARANTEE", "Guarantee Process", "คำนวณการันตี");
            labelMap.add("IS_GUARANTEE_0", "No", "ไม่คำนวณ");
            labelMap.add("IS_GUARANTEE_1", "Yes", "คำนวณ");
            labelMap.add("EXCLUDE_TREATMENT", "Step Calculate", "คำนวณแบ่งขั้นบันได");
            labelMap.add("EXCLUDE_TREATMENT_0", "No", "ไม่");
            labelMap.add("EXCLUDE_TREATMENT_1", "Yes", "คำนวณ");
            labelMap.add("TAX_TYPE_CODE", "Tax Type", "ประเภทภาษี");
            request.setAttribute("labelMap", labelMap.getHashMap());

//
// Process request
//

            request.setCharacterEncoding("UTF-8");
            DataRecord orderItemRec = null, orderItemCategoryRec = null;
            DataRecord orderItemRecLog = null;
            byte MODE = DBMgr.MODE_INSERT;
			String getcode = "";
			String codescript = "";
			String remark = "";
            if (request.getParameter("MODE") != null) {

                MODE = Byte.parseByte(request.getParameter("MODE"));

                orderItemRec = new DataRecord("ORDER_ITEM");
                orderItemRecLog = new DataRecord("LOG_ORDER_ITEM");

                orderItemRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                orderItemRec.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                orderItemRec.addField("DESCRIPTION_THAI", Types.VARCHAR, request.getParameter("DESCRIPTION_THAI"));
                orderItemRec.addField("DESCRIPTION_ENG", Types.VARCHAR, request.getParameter("DESCRIPTION_ENG"));
                orderItemRec.addField("PAYMENT_TIME", Types.NUMERIC, request.getParameter("PAYMENT_TIME"));
                orderItemRec.addField("ORDER_ITEM_CATEGORY_CODE", Types.VARCHAR, request.getParameter("ORDER_ITEM_CATEGORY_CODE"));
                orderItemRec.addField("ACCOUNT_CODE", Types.VARCHAR, request.getParameter("ACCOUNT_CODE"));
                orderItemRec.addField("IS_COMPUTE", Types.VARCHAR, request.getParameter("IS_COMPUTE"));
                orderItemRec.addField("IS_STEP_COMPUTE", Types.VARCHAR, request.getParameter("IS_STEP_COMPUTE"));
                orderItemRec.addField("IS_ALLOC_FULL_TAX", Types.VARCHAR, request.getParameter("IS_ALLOC_FULL_TAX"));
                orderItemRec.addField("IS_GUARANTEE", Types.VARCHAR, request.getParameter("IS_GUARANTEE"));
                orderItemRec.addField("EXCLUDE_TREATMENT", Types.VARCHAR, request.getParameter("EXCLUDE_TREATMENT"));
                orderItemRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                orderItemRec.addField("TAX_TYPE_CODE", Types.VARCHAR, request.getParameter("TAX_TYPE_CODE"));
                orderItemRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                orderItemRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                orderItemRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                orderItemRec.addField("IS_PROCEDURE", Types.VARCHAR, request.getParameter("IS_PROCEDURE"));
                
                //for log
                orderItemRecLog.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                orderItemRecLog.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                orderItemRecLog.addField("DESCRIPTION_THAI", Types.VARCHAR, request.getParameter("DESCRIPTION_THAI"));
                orderItemRecLog.addField("DESCRIPTION_ENG", Types.VARCHAR, request.getParameter("DESCRIPTION_ENG"));
                orderItemRecLog.addField("PAYMENT_TIME", Types.NUMERIC, request.getParameter("PAYMENT_TIME"));
                orderItemRecLog.addField("ORDER_ITEM_CATEGORY_CODE", Types.VARCHAR, request.getParameter("ORDER_ITEM_CATEGORY_CODE"));
                orderItemRecLog.addField("ACCOUNT_CODE", Types.VARCHAR, request.getParameter("ACCOUNT_CODE"));
                orderItemRecLog.addField("IS_COMPUTE", Types.VARCHAR, request.getParameter("IS_COMPUTE"));
                orderItemRecLog.addField("IS_STEP_COMPUTE", Types.VARCHAR, request.getParameter("IS_STEP_COMPUTE"));
                orderItemRecLog.addField("IS_ALLOC_FULL_TAX", Types.VARCHAR, request.getParameter("IS_ALLOC_FULL_TAX"));
                orderItemRecLog.addField("IS_GUARANTEE", Types.VARCHAR, request.getParameter("IS_GUARANTEE"));
                orderItemRecLog.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                orderItemRecLog.addField("TAX_TYPE_CODE", Types.VARCHAR, request.getParameter("TAX_TYPE_CODE"));
                orderItemRecLog.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate(), true);
                orderItemRecLog.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime(), true);
                orderItemRecLog.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

                if (MODE == DBMgr.MODE_INSERT) {
                	orderItemRecLog.addField("REMARK", Types.VARCHAR, remark);
                    if (DBMgr.insertRecord(orderItemRec) && DBMgr.insertRecord(orderItemRecLog)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/order_item.jsp"));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } else if (MODE == DBMgr.MODE_UPDATE) {
                	DataRecord orderItem = DBMgr.getRecord("SELECT HOSPITAL_CODE, CODE, DESCRIPTION_THAI, DESCRIPTION_ENG, "+
                			"PAYMENT_TIME, ORDER_ITEM_CATEGORY_CODE, ACCOUNT_CODE, IS_COMPUTE, IS_STEP_COMPUTE, IS_ALLOC_FULL_TAX, IS_GUARANTEE, "+
                			"EXCLUDE_TREATMENT, ACTIVE, TAX_TYPE_CODE, UPDATE_DATE, UPDATE_TIME, USER_ID, IS_PROCEDURE "+
               				"FROM ORDER_ITEM WHERE CODE = '" + request.getParameter("CODE") + "' AND HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE") + "' " );
             		
               		remark = "แก้ไข ";
               		for(int i = 0; i < orderItem.getSize(); i++){
               			if(!orderItem.getValueOfIndex(i).getValue().equalsIgnoreCase(orderItemRec.getValueOfIndex(i).getValue())
               					&& !orderItemRec.getValueOfIndex(i).getName().equals("USER_ID")
               					&& !orderItemRec.getValueOfIndex(i).getName().equals("UPDATE_DATE")
               					&& !orderItemRec.getValueOfIndex(i).getName().equals("UPDATE_TIME")){
               				System.out.println(orderItem.getValueOfIndex(i).getValue()+", "+orderItemRec.getValueOfIndex(i).getValue());
               				System.out.println("แก้ไข"+orderItemRec.getValueOfIndex(i).getName());
               				remark += orderItemRec.getValueOfIndex(i).getName()+", ";
               			}
               		}
               		orderItemRecLog.addField("REMARK", Types.VARCHAR, remark.substring(0, remark.length()-2));
                    if (DBMgr.updateRecord(orderItemRec) && DBMgr.insertRecord(orderItemRecLog)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/order_item.jsp"));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            } else if (request.getParameter("CODE") != null) {
                orderItemRec = DBMgr.getRecord("SELECT * FROM ORDER_ITEM WHERE CODE = '" + request.getParameter("CODE") + "' AND HOSPITAL_CODE = '" +session.getAttribute("HOSPITAL_CODE").toString()+"'");
                if (orderItemRec == null) {
                    MODE = DBMgr.MODE_INSERT;
					getcode = request.getParameter("CODE");
					codescript = "<script language=\"javascript\">";
					codescript+= "	alert('Data Not Found');";
					codescript+= "</script>";
                } else {
                    MODE = DBMgr.MODE_UPDATE;
                    orderItemCategoryRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION_" + labelMap.getFieldLangSuffix() + " AS DESCRIPTION FROM ORDER_ITEM_CATEGORY WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND CODE = '" + DBMgr.getRecordValue(orderItemRec, "ORDER_ITEM_CATEGORY_CODE") + "'");
					getcode = DBMgr.getRecordValue(orderItemRec, "CODE");
                }
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript">

            function CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    Refresh_ORDER_ITEM();
                    return false;
                }
                else {
                    return true;
                }
            }

            function Refresh_ORDER_ITEM() {
                var to = document.location.pathname.lastIndexOf('?');
                if (to < 0) {
                    window.location = document.location.pathname + '?CODE=' + document.mainForm.CODE.value;
                }
                else {
                    window.location = document.location.pathname.substr(0, to) + '?CODE=' + document.mainForm.CODE.value;
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
                var target = "../../RetrieveData?TABLE=ORDER_ITEM_CATEGORY&COND=CODE='" + document.mainForm.ORDER_ITEM_CATEGORY_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_ORDER_ITEM_CATEGORY);
            }
            
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
                    document.mainForm.ORDER_ITEM_CATEGORY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                }
            }

            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=ORDER_ITEM&COND=CODE='" + document.mainForm.CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_VerifyData);
            }
            
            function AJAX_Handle_VerifyData() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist = isXMLNodeExist(xmlDoc, "CODE");
                    
                    switch (document.mainForm.MODE.value) {
                    case "<%=DBMgr.MODE_INSERT%>" :
                            if (beExist) {
                                if (confirm("<%=labelMap.get("CONFIRM_REPLACE_DATA")%>")) {
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
                            alert("<%=labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)%>");
                        }
                        break;
                    }
                }
            }

            function SAVE_Click() {
                if (!isObjectEmptyString(document.mainForm.CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.DESCRIPTION_ENG, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
					!isObjectEmptyString(document.mainForm.ORDER_ITEM_CATEGORY_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.DESCRIPTION_THAI, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                    AJAX_VerifyData();
                }
            }
            
            function RESET_Click() {
                document.mainForm.reset();
                document.mainForm.MODE.value = "<%=DBMgr.MODE_INSERT%>";
                document.mainForm.CODE.value = '';
                document.mainForm.CODE.readOnly = false;
                document.mainForm.DESCRIPTION_THAI.value = '';
                document.mainForm.DESCRIPTION_ENG.value = '';
                //document.mainForm.HANDICRAFT.value = '';
                document.mainForm.ORDER_ITEM_CATEGORY_CODE.value = '';
                document.mainForm.ORDER_ITEM_CATEGORY_DESCRIPTION.value = '';
                document.mainForm.IS_COMPUTE_1.checked = true;
                //document.mainForm.IS_ALLOC_FULL_TAX_1.checked = true;
                document.mainForm.IS_GUARANTEE_1.checked = true;
                //document.mainForm.EXCLUDE_TREATMENT_1.checked = true;
                document.mainForm.ACTIVE_1.checked = true;
                return false;
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
        <form id="mainForm" name="mainForm" method="post" action="order_item.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=MODE%>" />
            <input type="hidden" id="EXCLUDE_TREATMENT" name="EXCLUDE_TREATMENT" value="N" />
            <input type="hidden" id="IS_ALLOC_FULL_TAX" name="IS_ALLOC_FULL_TAX" value="Y" />
            <input type="hidden" id="IS_PROCEDURE" name="IS_PROCEDURE" value="" />
            
            <input type="hidden" id="TAX_TYPE_CODE" name="TAX_TYPE_CODE" value="406" />            
			<center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("order_item.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                  <th colspan="4">
				  <div style="float: left;">${labelMap.TITLE_MAIN}</div>
				  </th>
                </tr>
                <tr>
                    <td class="label"><label for="CODE"><span class="style1">${labelMap.CODE}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="CODE" name="CODE" class="short" maxlength="20" value="<%= getcode %>"<%= MODE == DBMgr.MODE_UPDATE ? " readonly=\"readonly\"" : ""%> onkeypress="return CODE_KeyPress(event);" />
                        <input id="SEARCH_CODE" name="SEARCH_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&BEINSIDEHOSPITAL=1&TARGET=CODE&HANDLE=Refresh_ORDER_ITEM'); return false;" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION_ENG"><span class="style1">${labelMap.DESCRIPTION_ENG}*</span></label></td>
                    <td colspan="3" class="input"><input type="text" id="DESCRIPTION_ENG" name="DESCRIPTION_ENG" maxlength="255" class="long" value="<%= DBMgr.getRecordValue(orderItemRec, "DESCRIPTION_ENG")%>" /></td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION_THAI"><span class="style1">${labelMap.DESCRIPTION_THAI}*</span></label></td>
                    <td colspan="3" class="input"><input type="text" id="DESCRIPTION_THAI" name="DESCRIPTION_THAI" maxlength="255" class="long" value="<%= DBMgr.getRecordValue(orderItemRec, "DESCRIPTION_THAI")%>" /></td>
                </tr>
				<tr>
                    <td class="label"><label for="ORDER_ITEM_CATEGORY_CODE"><span class="style1">${labelMap.ORDER_ITEM_CATEGORY_CODE}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="ORDER_ITEM_CATEGORY_CODE" name="ORDER_ITEM_CATEGORY_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(orderItemCategoryRec, "CODE")%>" onkeypress="return ORDER_ITEM_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM_CATEGORY();" />
                        <input id="SEARCH_ORDER_ITEM_CATEGORY_CODE" name="SEARCH_ORDER_ITEM_CATEGORY_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM_CATEGORY&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&TARGET=ORDER_ITEM_CATEGORY_CODE&HANDLE=AJAX_Refresh_ORDER_ITEM_CATEGORY'); return false;" />
                        <input type="text" id="ORDER_ITEM_CATEGORY_DESCRIPTION" name="ORDER_ITEM_CATEGORY_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(orderItemCategoryRec, "DESCRIPTION")%>" />                    </td>
                </tr>
				
                <tr>
                    <td class="label"><label for="ACCOUNT_CODE"><span class="style1">${labelMap.ACCOUNT_CODE}*</span></label></td>
                    <td colspan="3" class="input">
					<%=DBMgr.generateDropDownList("ACCOUNT_CODE", "long", "SELECT CODE, CODE + ' : ' + DESCRIPTION AS DESCRIPTION FROM ACCOUNT ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", ( DBMgr.getRecordValue(orderItemRec, "ACCOUNT_CODE")=="" ? "602101" : DBMgr.getRecordValue(orderItemRec, "ACCOUNT_CODE") ))%>
					</td>
                </tr>
                <tr>
                    <td class="label"><label for="PAYMENT_TIME"><span class="style1">${labelMap.PAYMENT_TIME}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="PAYMENT_TIME_1" name="PAYMENT_TIME" value="1"<%= DBMgr.getRecordValue(orderItemRec, "PAYMENT_TIME").equalsIgnoreCase("1") || DBMgr.getRecordValue(orderItemRec, "PAYMENT_TIME")=="" ? " checked=\"checked\"" : ""%> />
                               <label for="PAYMENT_TIME_1">${labelMap.PAYMENT_TIME_1}</label>
                        <input type="radio" id="PAYMENT_TIME_0" name="PAYMENT_TIME" value="2"<%= DBMgr.getRecordValue(orderItemRec, "PAYMENT_TIME").equalsIgnoreCase("2") ? " checked=\"checked\"" : ""%> />
                               <label for="PAYMENT_TIME_0">${labelMap.PAYMENT_TIME_0}</label>
					</td>
                </tr>
                <tr>
                    <td class="label"><label for="IS_STEP_COMPUTE"><span class="style1">${labelMap.IS_STEP_COMPUTE}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="IS_STEP_COMPUTE_1" name="IS_STEP_COMPUTE" value="1"<%= DBMgr.getRecordValue(orderItemRec, "IS_STEP_COMPUTE").equalsIgnoreCase("1") || DBMgr.getRecordValue(orderItemRec, "IS_STEP_COMPUTE")=="" ? " checked=\"checked\"" : ""%> />
                               <label for="IS_STEP_COMPUTE_1">${labelMap.IS_STEP_COMPUTE_1}</label>
                        <input type="radio" id="IS_STEP_COMPUTE_0" name="IS_STEP_COMPUTE" value="0"<%= DBMgr.getRecordValue(orderItemRec, "IS_STEP_COMPUTE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> />
                               <label for="IS_STEP_COMPUTE_0">${labelMap.IS_STEP_COMPUTE_0}</label>
					</td>
                </tr>
                <tr>
                    <td class="label"><label for="IS_COMPUTE_1"><span class="style1">${labelMap.IS_COMPUTE}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="IS_COMPUTE_1" name="IS_COMPUTE" value="Y"<%= DBMgr.getRecordValue(orderItemRec, "IS_COMPUTE").equalsIgnoreCase("Y") || DBMgr.getRecordValue(orderItemRec, "IS_COMPUTE")=="" ? " checked=\"checked\"" : ""%> />
                               <label for="IS_COMPUTE_1">${labelMap.IS_COMPUTE_1}</label>
                        <input type="radio" id="IS_COMPUTE_0" name="IS_COMPUTE" value="N"<%= DBMgr.getRecordValue(orderItemRec, "IS_COMPUTE").equalsIgnoreCase("N") ? " checked=\"checked\"" : ""%> />
                               <label for="IS_COMPUTE_0">${labelMap.IS_COMPUTE_0}</label>
                    </td>
               </tr>
               <tr>
                    <td class="label"><label for="IS_GUARANTEE_1"><span class="style1">${labelMap.IS_GUARANTEE}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="IS_GUARANTEE_1" name="IS_GUARANTEE" value="Y"<%= DBMgr.getRecordValue(orderItemRec, "IS_GUARANTEE").equalsIgnoreCase("Y") || DBMgr.getRecordValue(orderItemRec, "IS_GUARANTEE")=="" ? " checked=\"checked\"" : ""%> />
                               <label for="IS_GUARANTEE_1">${labelMap.IS_GUARANTEE_1} </label>
                        <input type="radio" id="IS_GUARANTEE_0" name="IS_GUARANTEE" value="N"<%= DBMgr.getRecordValue(orderItemRec, "IS_GUARANTEE").equalsIgnoreCase("N") ? " checked=\"checked\"" : ""%> />
                               <label for="IS_GUARANTEE_0"> ${labelMap.IS_GUARANTEE_0}</label>
					</td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1"><span class="style1">${labelMap.ACTIVE}*</span></label></td>
                    <td colspan="3" class="input">
                        <%-- <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(orderItemRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(orderItemRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> /> --%>
                  	    <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(orderItemRec, "ACTIVE").equalsIgnoreCase("1") ? " checked=\"checked\"" : ""%>/>           
                               <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(orderItemRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> />
                               <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="return RESET_Click()" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
<%=codescript%>