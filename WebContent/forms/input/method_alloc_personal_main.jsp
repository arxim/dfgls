<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="java.sql.*"%>

<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_METHOD_ALLOC_PERSONAL_MAIN)) {
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
            labelMap.add("TITLE_MAIN", "Special Allocate", "เงื่อนไขการแบ่งค่าแพทย์แบบพิเศษ");
            labelMap.add("DOCTOR_CODE", "Doctor", "แพทย์");
            
            labelMap.add("TITLE_DATA", "Special Allocate Details", "รายละเอียดเงื่อนไขการแบ่งค่าแพทย์แบบพิเศษ");
            labelMap.add("ORDER_ITEM_CODE", "Order Item", "รายการรักษา");
            labelMap.add("ORDER_ITEM_DESCRIPTION", "Description", "ชื่อรายการรักษา");
            labelMap.add("ADMISSION_TYPE_CODE", "Admission Type", "แผนกรับผู้ป่วย");
            labelMap.add("PRICE", "Price", "ราคา");
            labelMap.add("NORMAL_ALLOCATE_PCT", "%", "%");
            labelMap.add("NORMAL_ALLOCATE_AMT", "Amount", "จำนวน");
            
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            DataRecord doctorRec = null;
            String arr = "";
            if (request.getParameter("DOCTOR_CODE") != null) {
                doctorRec = DBMgr.getRecord("SELECT * FROM DOCTOR WHERE CODE = '" + request.getParameter("DOCTOR_CODE") + "'");
                if(doctorRec==null)
                {
                    arr +="<script language='javascript'>";
                    arr +="alert('"+ labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)+"')";  
                    arr +="</script>";
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
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
        <script type="text/javascript">
            
            function DOCTOR_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    if(document.mainForm.DOCTOR_CODE.value != ""){
                        AJAX_Refresh_DOCTOR();                    
                    }
                    //document.mainForm.DOCTOR_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR() {
                var target = "../../RetrieveData?A=1&TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
            }
            
            function AJAX_Handle_Refresh_DOCTOR() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        alert("<%=labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)%>");
                        //document.mainForm.DOCTOR_CODE.value = "";
                        document.mainForm.DOCTOR_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
            
        </script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="">
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                            <b><font color='#003399'><%=Utils.getInfoPage("method_alloc_personal_main.jsp", labelMap.getFieldLangSuffix(),  new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" onkeypress="return DOCTOR_CODE_KeyPress(event);" />
                        <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR'); return false;" />
                        <input type="text" id="DOCTOR_NAME" name="DOCTOR_NAME" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME_" + labelMap.getFieldLangSuffix()) %>" />
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="window.location = 'method_alloc_personal_main.jsp?DOCTOR_CODE=' + document.mainForm.DOCTOR_CODE.value; return false;" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='method_alloc_personal_main.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
            <hr />
            <table class="data" id="dataTable" name="dataTable">
                <tr>
                    <th colspan="8" class="alignLeft">${labelMap.TITLE_DATA}</th>
                </tr>
                <tr>
                    <td class="sub_head"><%=labelMap.get("ORDER_ITEM_CODE")%></td>
                    <td class="sub_head"><%=labelMap.get("ORDER_ITEM_DESCRIPTION")%></td>
                    <td class="sub_head"><%=labelMap.get("ADMISSION_TYPE_CODE")%></td>
                    <td class="sub_head"><%=labelMap.get("PRICE")%></td>
                    <td class="sub_head"><%=labelMap.get("NORMAL_ALLOCATE_PCT")%></td>
                    <td class="sub_head"><%=labelMap.get("NORMAL_ALLOCATE_AMT")%></td>
                    <td class="sub_head"><%=labelMap.get(LabelMap.ACTIVE)%></td>
                    <td class="sub_head"><%=labelMap.get(LabelMap.EDIT)%></td>
                </tr>
                <%
	                DBConnection con = new DBConnection();
	                con.connectToLocal();
		            String query = "SELECT M.ADMISSION_TYPE_CODE, M.ORDER_ITEM_CODE, O.DESCRIPTION_" + labelMap.getFieldLangSuffix() + " AS ORDER_ITEM_DESCRIPTION, M.DOCTOR_TREATMENT_CODE, M.DOCTOR_CODE, M.PRICE, M.TAX_TYPE_CODE, M.EXCLUDE_TREATMENT, M.NORMAL_ALLOCATE_PCT, M.NORMAL_ALLOCATE_AMT, M.ACTIVE FROM STP_METHOD_ALLOC_PERSONAL M, ORDER_ITEM O WHERE M.ORDER_ITEM_CODE = O.CODE AND O.HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND M.HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND M.DOCTOR_CODE = '" + DBMgr.getRecordValue(doctorRec, "CODE") + "' ORDER BY ACTIVE DESC";
		            ResultSet rs = con.executeQuery(query);
		            int i = 0;
		            String activeIcon, linkEdit;
		            while (rs.next()) {
	                activeIcon = "<img src=\"../../images/" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? "" : "in") + "active_icon.png\" alt=\"" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? labelMap.get(LabelMap.ACTIVE_1) : labelMap.get(LabelMap.ACTIVE_0)) + "\" />";
	                linkEdit = "<a href=\"method_alloc_personal_detail.jsp?ADMISSION_TYPE_CODE=" + rs.getString("ADMISSION_TYPE_CODE") + "&ORDER_ITEM_CODE=" + rs.getString("ORDER_ITEM_CODE") + "&DOCTOR_TREATMENT_CODE=" + rs.getString("DOCTOR_TREATMENT_CODE") + "&DOCTOR_CODE=" + rs.getString("DOCTOR_CODE") + "&PRICE=" + rs.getString("PRICE") + "&TAX_TYPE_CODE=" + rs.getString("TAX_TYPE_CODE") + "&EXCLUDE_TREATMENT=" + rs.getString("EXCLUDE_TREATMENT") + "&NORMAL_ALLOCATE_PCT=" + rs.getString("NORMAL_ALLOCATE_PCT") + "&NORMAL_ALLOCATE_AMT=" + rs.getString("NORMAL_ALLOCATE_AMT") + "\" title=\"" + labelMap.get(LabelMap.EDIT) + "\"><img src=\"../../images/edit_button.png\" alt=\"" + labelMap.get(LabelMap.EDIT) + "\" /></a>";
                %>                
                <tr>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("ORDER_ITEM_CODE"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("ORDER_ITEM_DESCRIPTION"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("ADMISSION_TYPE_CODE"), true)%></td>
                    <td class="row<%=i % 2%> alignRight"><%= Util.formatHTMLString(rs.getString("PRICE"), true)%></td>
                    <td class="row<%=i % 2%> alignRight"><%= Util.formatHTMLString(rs.getString("NORMAL_ALLOCATE_PCT"), true)%></td>
                    <td class="row<%=i % 2%> alignRight"><%= Util.formatHTMLString(rs.getString("NORMAL_ALLOCATE_AMT"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= activeIcon %></td>
                    <td class="row<%=i % 2%> alignCenter"><%= linkEdit%></td>
                </tr>
                <%
	                i++;
		            }
		            if (rs != null) {
		                rs.close();
		            }
		            con.Close();
                %>                
                <tr>
                    <th colspan="8" class="buttonBar">                        
                        <input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}" onclick="window.location = 'method_alloc_personal_detail.jsp?DOCTOR_CODE=' + document.mainForm.DOCTOR_CODE.value;" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
<%=arr%>