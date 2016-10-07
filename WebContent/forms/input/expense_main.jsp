<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="java.sql.*"%>

<%    if (session.getAttribute("LANG_CODE") == null)
        session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
    
    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
    labelMap.add("TITLE_MAIN", "Adjust Revenue", "รายการปรับปรุง");
    labelMap.add("MM", "Month", "เดือน");
    labelMap.add("YYYY", "Year", "ปี");
    labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
    labelMap.add("EXPENSE_DESCRIPTION", "Adjust Revenue Details", "รายละเอียดรายการปรับปรุง");
    labelMap.add("EXPENSE_TYPE", "Adjust Type", "ประเภทรายการปรับปรุง");
    labelMap.add("AMOUNT", "Amount", "จำนวน");
    labelMap.add("NOTE", "TAX Amount", "จำนวนเงินภาษี");
    request.setAttribute("labelMap", labelMap.getHashMap());

    request.setCharacterEncoding("UTF-8");
    DataRecord doctorRec = null;
    ProcessUtil util = new ProcessUtil();
    DBConnection c = new DBConnection();
    c.connectToLocal();
    Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
    c.Close();

    String mm = request.getParameter("MM") != null ? request.getParameter("MM") : JDate.getMonth();
    String yyyy = request.getParameter("YYYY") != null ? request.getParameter("YYYY") : JDate.getYear();

    if (request.getParameter("EXPENSE_DR_CODE") != null) {
        doctorRec = DBMgr.getRecord("SELECT * FROM DOCTOR WHERE CODE = '" + request.getParameter("EXPENSE_DR_CODE") + "'");
    }
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
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
        <script type="text/javascript">

            function EXPENSE_DR_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.EXPENSE_DR_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_EXPENSE_DR() {
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.EXPENSE_DR_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_EXPENSE_DR);
            }

            function AJAX_Handle_Refresh_EXPENSE_DR() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.EXPENSE_DR_CODE.value = "";
                        document.mainForm.EXPENSE_DR_NAME.value = "";
                        return;
                    }

                    // Data found
                    document.mainForm.EXPENSE_DR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
        </script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="BankSrvl">
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("expense_main.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
        <table class="form">
                <tr><th colspan="4">${labelMap.TITLE_MAIN}</th></tr>
                <tr>
                    <td class="label">
                        <label>${labelMap.MM}</label>
                    </td>
                    <td class="input"><%=util.selectMM(session.getAttribute("LANG_CODE").toString(), "MM",b.getMm())%></td>
                    <td class="label">
                         <label>${labelMap.YYYY}</label>
					</td>
                    <td class="input"><%=util.selectYY("YYYY", b.getYyyy())%></td>
                </tr>
                <!--
                <tr>
                    <td class="label">
                        <label for="aText">${labelMap.MM}</label>
                    </td>
                    <td class="input">
                        <%=util.selectMM(session.getAttribute("LANG_CODE").toString(), "MM", mm)%>
                    </td>
                    <td class="label">
                        <label>${labelMap.YYYY}</label>
                    </td>
                    <td class="input">
                        <%=util.selectYY("YYYY", yyyy)%>
                    </td>
                </tr>
                 -->
                <tr>
                    <td class="label">
                        <label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input type="text" id="EXPENSE_DR_CODE" name="EXPENSE_DR_CODE" class="short" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" onkeypress="return EXPENSE_DR_CODE_KeyPress(event);" onblur="AJAX_Refresh_EXPENSE_DR();" />
                        <input id="SEARCH_EXPENSE_DR_CODE" name="SEARCH_EXPENSE_DR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=EXPENSE_DR_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_EXPENSE_DR'); return false;" />
                        <input type="text" id="EXPENSE_DR_NAME" name="EXPENSE_DR_NAME" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME_" + labelMap.getFieldLangSuffix()) %>" />
                    </td>
                </tr>
                <tr>
                    <th colspan="6" class="buttonBar">
                        <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="window.location = 'expense_main.jsp?EXPENSE_DR_CODE=' + document.mainForm.EXPENSE_DR_CODE.value + '&MM=' +  document.mainForm.MM.value + '&YYYY=' +  document.mainForm.YYYY.value; return false;" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='expense_main.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
            <hr />
            <table class="data">
                <tr>
                    <th colspan="7" class="alignLeft">${labelMap.EXPENSE_DESCRIPTION}</th>
                </tr>
                <tr>
                    <td class="sub_head">${labelMap.YYYY}</td>
                    <td class="sub_head">${labelMap.MM}</td>
                    <td class="sub_head">${labelMap.EXPENSE_DESCRIPTION}</td>
                    <td class="sub_head">${labelMap.EXPENSE_TYPE}</td>
                    <td class="sub_head">${labelMap.NOTE}</td>
                    <td class="sub_head">${labelMap.AMOUNT}</td>
                    <td class="sub_head">${labelMap.EDIT}</td>
                </tr>

                <%
                String disable = "disabled";
                if(DBMgr.getRecordValue(doctorRec, "CODE") != ""){
                    DBConnection con = new DBConnection();
                    con.connectToLocal();
                    String sqlCommand = "SELECT YYYY, MM, INVOICE_TYPE_DESCRIPTION, NOTE, LINE_NO, AMOUNT, TAX_AMOUNT, DOCTOR_CODE, HOSPITAL_CODE, EXPENSE_CODE," +
                            " (SELECT     DESCRIPTION " +
                            "FROM          EXPENSE " +
                            "WHERE      (CODE = a.EXPENSE_CODE AND HOSPITAL_CODE = a.HOSPITAL_CODE)) AS ExpenseDesc " +
                            "FROM         TRN_EXPENSE_DETAIL AS a " +
                            "WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' " +
                            "AND MM = '" + mm + "' " +
                            "AND YYYY = '" + yyyy + "' " +
                            "AND DOCTOR_CODE = '" + DBMgr.getRecordValue(doctorRec, "CODE") + "' ";
                    System.out.println(sqlCommand);
                    ResultSet rs = con.executeQuery(sqlCommand);
                    int i = 0;
                    String linkEdit;
                    while (rs.next()) {
                        linkEdit = "<a href=\"expense_detail.jsp?DOCTOR_CODE=" + rs.getString("DOCTOR_CODE") + "&YYYY=" + rs.getString("YYYY") + "&MM=" + rs.getString("MM") + "&HOSPITAL_CODE=" + rs.getString("HOSPITAL_CODE") + "&LINE_NO=" + rs.getString("LINE_NO") + "&EXPENSE_CODE=" + rs.getString("EXPENSE_CODE") + "\" title=\"" + labelMap.get(LabelMap.EDIT) + "\"><img src=\"../../images/edit_button.png\" alt=\"" + labelMap.get(LabelMap.EDIT) + "\" /></a>";
                        %>
                        <tr>
                            <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("YYYY"), true)%></td>
                            <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("MM"), true)%></td>
                            <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("ExpenseDesc"), true)%></td>
                            <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("INVOICE_TYPE_DESCRIPTION"), true)%></td>
                            <td class="row<%=i % 2%> alignRight"><%= Util.formatHTMLString(rs.getString("TAX_AMOUNT"), true)%></td>
                            <td class="row<%=i % 2%> alignRight"><%= Util.formatHTMLString(rs.getString("AMOUNT"), true)%></td>
                            <td class="row<%=i % 2%> alignCenter">
                                <%=linkEdit%>
                            </td>
                        </tr>
                        <%
                        i++;
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    con.Close();
                    disable = "";
                }
                %>
                <tr>
                    <th colspan="7" class="buttonBar">
                        <input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}" onclick="window.location = 'expense_detail.jsp?DOCTOR_CODE=' + document.mainForm.EXPENSE_DR_CODE.value + '&MM=' + document.mainForm.MM.value + '&YYYY=' + document.mainForm.YYYY.value;" <%=disable%>/>
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
