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
<%@page import="java.sql.*"%>

<%    if (session.getAttribute("LANG_CODE") == null)
        session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
    
    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
    labelMap.add("TITLE_MAIN", "Tax Reduce", "การกำหนดรายการลดหย่อนภาษี");
    labelMap.add("YYYY", "Year", "ปี");
    labelMap.add("REDUCE_DESCRIPTION", "Reduce Description", "รายละเอียดรายการลดหย่อนภาษี");
    labelMap.add("AMOUNT", "Amount", "จำนวนเงิน");
    labelMap.add("PERCENT", "Percent", "จำนวนเปอร์เซ็นต์");
    labelMap.add("LABEL_STATUS", "Status", "สถานะ");
    
    request.setAttribute("labelMap", labelMap.getHashMap());

    request.setCharacterEncoding("UTF-8");
    DataRecord doctorRec = null;
    ProcessUtil util = new ProcessUtil();
    String yyyy = request.getParameter("YYYY") != null ? request.getParameter("YYYY") : JDate.getYear();

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

    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="BankSrvl">
            <center>
                <table width="800" border="0">
                    <tr><td align="left">                  
                    <b><font color='#003399'><%=Utils.getInfoPage("tax_reduce_main.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
		</table>
            </center>
        <table class="form">
                <tr><th colspan="2">${labelMap.TITLE_MAIN}</th></tr>
                <tr>
                    <td class="label"><label>${labelMap.YYYY}</label></td>
                    <td class="input"><%=util.selectYY("YYYY", yyyy)%></td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="window.location = 'tax_reduce_main.jsp?YYYY=' +  document.mainForm.YYYY.value; return false;" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='tax_reduce_main.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />                    </th>
                </tr>
            </table>
            <hr />
            <table class="data">
                <tr>
                    <th colspan="7" class="alignLeft">Tax Reduce  Detail</th>
                </tr>
                <tr>
                    <td class="sub_head">${labelMap.YYYY}</td>
                    <td class="sub_head">${labelMap.REDUCE_DESCRIPTION}</td>
                    <td class="sub_head">${labelMap.AMOUNT}</td>
                    <td class="sub_head">${labelMap.PERCENT}</td>
                    <td class="sub_head">${labelMap.LABEL_STATUS}</td>
                    <td class="sub_head">${labelMap.EDIT}</td>
                </tr>

                <%
                if(!yyyy.equals("")){
                    DBConnection con = new DBConnection();
                    con.connectToServer();
                    String sqlCommand = "SELECT CODE, YYYY, MASTER_REDUCE_NAME, MASTER_REDUCE_MAX_AMOUNT, MASTER_REDUCE_MAX_PERCENT, STATUS "+
                            "FROM STP_MASTER_TAX_REDUCE " +
                            "WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' " +
                            "AND YYYY = '" + yyyy + "' " ;
                    ResultSet rs = con.executeQuery(sqlCommand);
                    int i = 0;
                    String linkEdit="", activeIcon="";
                    while (rs.next()) {
                    	activeIcon = "<img src=\"../../images/" + (rs.getString("STATUS") != null && rs.getString("STATUS").equalsIgnoreCase("1") ? "" : "in") + "active_icon.png\" alt=\"" + (rs.getString("STATUS") != null && rs.getString("STATUS").equalsIgnoreCase("1") ? labelMap.get(LabelMap.ACTIVE_1) : labelMap.get(LabelMap.ACTIVE_0)) + "\" />";
                    	linkEdit = "<a href=\"tax_reduce_detail.jsp?CODE=" + rs.getString("CODE") + "\" title=\"" + labelMap.get(LabelMap.EDIT) + "\"><img src=\"../../images/edit_button.png\" alt=\"" + labelMap.get(LabelMap.EDIT) + "\" /></a>";
                        %>
                        <tr>
                            <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("YYYY"), true)%></td>
                            <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("MASTER_REDUCE_NAME"), true)%></td>
                            <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("MASTER_REDUCE_MAX_AMOUNT"), true)%></td>
                            <td class="row<%=i % 2%> alignRight"><%= Util.formatHTMLString(rs.getString("MASTER_REDUCE_MAX_PERCENT"), true)%></td>
                            <td class="row<%=i % 2%> alignCenter">&nbsp;<%= activeIcon %></td>
                            <td class="row<%=i % 2%> alignCenter"><%=linkEdit%> </td>
                        </tr>
                        <%
                        i++;
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    con.freeConnection();
                    
                }
                %>
                <tr>
                    <th colspan="7" class="buttonBar">
                        <input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}" onclick="window.location = 'tax_reduce_detail.jsp?YYYY=' + document.mainForm.YYYY.value;" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
