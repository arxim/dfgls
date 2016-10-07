<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
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
            labelMap.add("HANDICRAFT", "Handicraft", "หัตถการ");
            //labelMap.add("ACTIVE", "Status", "สถานะ");
            //labelMap.add("ACTIVE_0", "Inactive", "ไม่ใช้งาน");
            //labelMap.add("ACTIVE_1", "Active", "ใช้งาน");
            labelMap.add("ORDER_ITEM_CATEGORY_CODE", "OrderItem Cat(DF Code)", "กลุ่มรายการรักษา");
            labelMap.add("ACCOUNT_CODE", "Account Code", "รหัสลงบัญชี");
            labelMap.add("IS_COMPUTE", "DF Calculate", "คำนวณค่าแพทย์");
            labelMap.add("IS_COMPUTE_0", "No", "ไม่");
            labelMap.add("IS_COMPUTE_1", "Yes", "คำนวณ");
            labelMap.add("IS_ALLOC_FULL_TAX", "Calculate Tax From", "คิดภาษีจาก");
            labelMap.add("IS_ALLOC_FULL_TAX_0", "Allocate", "ส่วนแบ่ง");
            labelMap.add("IS_ALLOC_FULL_TAX_1", "Amount", "ยอดเต็ม");
            labelMap.add("IS_GUARANTEE", "Guarantee Calculate", "คำนวณการันตี");
            labelMap.add("IS_GUARANTEE_0", "No", "ไม่");
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
            byte MODE = DBMgr.MODE_INSERT;

            if (request.getParameter("MODE") != null) {

                MODE = Byte.parseByte(request.getParameter("MODE"));

                orderItemRec = new DataRecord("ORDER_ITEM");

                orderItemRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                orderItemRec.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                orderItemRec.addField("DESCRIPTION_THAI", Types.VARCHAR, request.getParameter("DESCRIPTION_THAI"));
                orderItemRec.addField("DESCRIPTION_ENG", Types.VARCHAR, request.getParameter("DESCRIPTION_ENG"));
                //orderItemRec.addField("HANDICRAFT", Types.NUMERIC, request.getParameter("HANDICRAFT"));
                orderItemRec.addField("ORDER_ITEM_CATEGORY_CODE", Types.VARCHAR, request.getParameter("ORDER_ITEM_CATEGORY_CODE"));
                orderItemRec.addField("ACCOUNT_CODE", Types.VARCHAR, request.getParameter("ACCOUNT_CODE"));
                orderItemRec.addField("IS_COMPUTE", Types.VARCHAR, request.getParameter("IS_COMPUTE"));
                orderItemRec.addField("IS_ALLOC_FULL_TAX", Types.VARCHAR, request.getParameter("IS_ALLOC_FULL_TAX"));
                orderItemRec.addField("IS_GUARANTEE", Types.VARCHAR, request.getParameter("IS_GUARANTEE"));
                orderItemRec.addField("EXCLUDE_TREATMENT", Types.VARCHAR, request.getParameter("EXCLUDE_TREATMENT"));
                orderItemRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                orderItemRec.addField("TAX_TYPE_CODE", Types.VARCHAR, request.getParameter("TAX_TYPE_CODE"));
                orderItemRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                orderItemRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                orderItemRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

                if (MODE == DBMgr.MODE_INSERT) {

                    if (DBMgr.insertRecord(orderItemRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/order_item.jsp"));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } else if (MODE == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(orderItemRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/order_item.jsp"));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            } else if (request.getParameter("CODE") != null) {
                orderItemRec = DBMgr.getRecord("SELECT * FROM ORDER_ITEM WHERE CODE = '" + request.getParameter("CODE") + "'");
                if (orderItemRec == null) {
                    MODE = DBMgr.MODE_INSERT;
                } else {
                    MODE = DBMgr.MODE_UPDATE;
                    orderItemCategoryRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION_" + labelMap.getFieldLangSuffix() + " AS DESCRIPTION FROM ORDER_ITEM_CATEGORY WHERE CODE = '" + DBMgr.getRecordValue(orderItemRec, "ORDER_ITEM_CATEGORY_CODE") + "'");
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
                var target = "../../RetrieveData?TABLE=ORDER_ITEM_CATEGORY&COND=CODE='" + document.mainForm.ORDER_ITEM_CATEGORY_CODE.value + "'";
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
                document.mainForm.IS_ALLOC_FULL_TAX_1.checked = true;
                document.mainForm.IS_GUARANTEE_1.checked = true;
                document.mainForm.EXCLUDE_TREATMENT_1.checked = true;
                document.mainForm.ACTIVE_1.checked = true;
                return false;
            }
            
        </script>
    </head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="order_item.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=MODE%>" />
            <table class="form">
                <tr>
                  <th colspan="4">
				  <div style="float: left;">${labelMap.TITLE_MAIN}</div>
				  </th>
                </tr>
                <tr>
                    <td class="label"><label for="CODE">${labelMap.CODE} *</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="CODE" name="CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(orderItemRec, "CODE")%>"<%= MODE == DBMgr.MODE_UPDATE ? " readonly=\"readonly\"" : ""%> onkeypress="return CODE_KeyPress(event);" />
                        <input id="SEARCH_CODE" name="SEARCH_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&BEINSIDEHOSPITAL=1&TARGET=CODE&HANDLE=Refresh_ORDER_ITEM'); return false;" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION_ENG">${labelMap.DESCRIPTION_ENG} *</label></td>
                    <td colspan="3" class="input"><input type="text" id="DESCRIPTION_ENG" name="DESCRIPTION_ENG" maxlength="255" class="long" value="<%= DBMgr.getRecordValue(orderItemRec, "DESCRIPTION_ENG")%>" /></td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION_THAI">${labelMap.DESCRIPTION_THAI} *</label></td>
                    <td colspan="3" class="input"><input type="text" id="DESCRIPTION_THAI" name="DESCRIPTION_THAI" maxlength="255" class="long" value="<%= DBMgr.getRecordValue(orderItemRec, "DESCRIPTION_THAI")%>" /></td>
                </tr>
				<tr>
                    <td class="label"><label for="ORDER_ITEM_CATEGORY_CODE">${labelMap.ORDER_ITEM_CATEGORY_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="ORDER_ITEM_CATEGORY_CODE" name="ORDER_ITEM_CATEGORY_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(orderItemCategoryRec, "CODE")%>" onkeypress="return ORDER_ITEM_CATEGORY_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM_CATEGORY();" />
                        <input id="SEARCH_ORDER_ITEM_CATEGORY_CODE" name="SEARCH_ORDER_ITEM_CATEGORY_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM_CATEGORY&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&TARGET=ORDER_ITEM_CATEGORY_CODE&HANDLE=AJAX_Refresh_ORDER_ITEM_CATEGORY'); return false;" />
                        <input type="text" id="ORDER_ITEM_CATEGORY_DESCRIPTION" name="ORDER_ITEM_CATEGORY_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(orderItemCategoryRec, "DESCRIPTION")%>" />                    </td>
                </tr>
				
                <tr>
                    <td class="label"><label for="ACCOUNT_CODE">${labelMap.ACCOUNT_CODE}</label></td>
                    <td colspan="3" class="input">
					<%=DBMgr.generateDropDownList("ACCOUNT_CODE", "long", "SELECT CODE, CODE + ' : ' + DESCRIPTION AS DESCRIPTION FROM ACCOUNT ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(orderItemRec, "ACCOUNT_CODE"))%>					</td>
                </tr>
				
                <tr>
                    <td class="label"><label for="IS_COMPUTE_1">${labelMap.IS_COMPUTE}</label></td>
                    <td class="input">
                        <input type="radio" id="IS_COMPUTE_1" name="IS_COMPUTE" value="Y"<%= DBMgr.getRecordValue(orderItemRec, "IS_COMPUTE").equalsIgnoreCase("Y") ? " checked=\"checked\"" : ""%> />
                               <label for="IS_COMPUTE_1">${labelMap.IS_COMPUTE_1}</label>
                        <input type="radio" id="IS_COMPUTE_0" name="IS_COMPUTE" value="N"<%= DBMgr.getRecordValue(orderItemRec, "IS_COMPUTE").equalsIgnoreCase("N") ? " checked=\"checked\"" : ""%> />
                               <label for="IS_COMPUTE_0">${labelMap.IS_COMPUTE_0}</label>                    </td>
                    <td class="label"><label for="IS_ALLOC_FULL_TAX_1">${labelMap.IS_ALLOC_FULL_TAX}</label></td>
                    <td class="input">
                        <input type="radio" id="IS_ALLOC_FULL_TAX_1" name="IS_ALLOC_FULL_TAX" value="Y"<%= DBMgr.getRecordValue(orderItemRec, "IS_ALLOC_FULL_TAX").equalsIgnoreCase("Y") ? " checked=\"checked\"" : ""%> />
                               <label for="IS_ALLOC_FULL_TAX_1">${labelMap.IS_ALLOC_FULL_TAX_1}</label>
                        <input type="radio" id="IS_ALLOC_FULL_TAX_0" name="IS_ALLOC_FULL_TAX" value="N"<%= DBMgr.getRecordValue(orderItemRec, "IS_ALLOC_FULL_TAX").equalsIgnoreCase("N") ? " checked=\"checked\"" : ""%> />
                               <label for="IS_ALLOC_FULL_TAX_0">${labelMap.IS_ALLOC_FULL_TAX_0}</label>                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="IS_GUARANTEE_1">${labelMap.IS_GUARANTEE}</label></td>
                    <td class="input">
                        <input type="radio" id="IS_GUARANTEE_1" name="IS_GUARANTEE" value="Y"<%= DBMgr.getRecordValue(orderItemRec, "IS_GUARANTEE").equalsIgnoreCase("Y") ? " checked=\"checked\"" : ""%> />
                               <label for="IS_GUARANTEE_1">${labelMap.IS_GUARANTEE_1}</label>
                        <input type="radio" id="IS_GUARANTEE_0" name="IS_GUARANTEE" value="N"<%= DBMgr.getRecordValue(orderItemRec, "IS_GUARANTEE").equalsIgnoreCase("N") ? " checked=\"checked\"" : ""%> />
                               <label for="IS_GUARANTEE_0">${labelMap.IS_GUARANTEE_0}</label>                    </td>
                    <td class="label"><label for="EXCLUDE_TREATMENT_1">${labelMap.EXCLUDE_TREATMENT}</label></td>
                    <td class="input">
                        <input type="radio" id="EXCLUDE_TREATMENT_1" name="EXCLUDE_TREATMENT" value="Y"<%= DBMgr.getRecordValue(orderItemRec, "EXCLUDE_TREATMENT").equalsIgnoreCase("Y") ? " checked=\"checked\"" : ""%> />
                               <label for="EXCLUDE_TREATMENT_1">${labelMap.EXCLUDE_TREATMENT_1}</label>
                        <input type="radio" id="EXCLUDE_TREATMENT_0" name="EXCLUDE_TREATMENT" value="N"<%= DBMgr.getRecordValue(orderItemRec, "EXCLUDE_TREATMENT").equalsIgnoreCase("N") ? " checked=\"checked\"" : ""%> />
                               <label for="EXCLUDE_TREATMENT_0">${labelMap.EXCLUDE_TREATMENT_0}</label>                    </td>
                </tr>
				<tr>
					<td class="label"><label for="TAX_TYPE_CODE">${labelMap.TAX_TYPE_CODE}</label></td>
                    <td class="input">
                        <%=DBMgr.generateDropDownList("TAX_TYPE_CODE", "medium", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM TAX_TYPE ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(orderItemRec, "TAX_TYPE_CODE"))%>                    </td>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(orderItemRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(orderItemRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
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
