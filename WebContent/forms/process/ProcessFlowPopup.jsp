<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="error.jsp"%>

<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.jsp.Util"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.process.ProcessUtil"%>

<%
            final int NUM_CONDITION = 3;
            
            //
            // Initial LabelMap
            //
            
            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            ProcessUtil proUtil = new ProcessUtil();
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Search", "ค้นหา");
            labelMap.add("KEYWORD", "Keyword", "คำค้นหา");
            labelMap.add("DISPLAY_FIELD", "View Column", "แสดงคอลัมน์");
            labelMap.add("ORDER_BY_FIELD", "Order By Column", "เรียงลำดับตามคอลัมน์");
            labelMap.add("ASC", "Ascending", "จากน้อยไปมาก");
            labelMap.add("DESC", "Descending", "จากมากไปน้อย");
            request.setAttribute("labelMap", labelMap.getHashMap());   
            request.setCharacterEncoding("UTF-8");

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/search.css" media="all" />
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="ProcessPaymentMonthly.jsp">
            <table class="data" id="dataTable" name="dataTable">
                <tr>
                    <th colspan="5" class="alignLeft">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
                        <div style="float: right;" id="PROGRESS" name="PROGRESS"></div>
                    </th>
                </tr>
                <tr>
                    <td class="sub_head"><%=labelMap.get("COL_0")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_1")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_4")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_3")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_2")%></td>

                </tr>
                
                    <tr>
                        <td class="row0 alignCenter"><div id="ImgPro01"><img src="../../images/success.png" border="1"></div></td>
                        <td class="row1 alignLeft"><%=labelMap.get("PROCESS_01")%></td>
                        <td class="row0 alignCenter">
                            <table cellpadding="0" cellspacing="0" border="0">
                                <tr>
                                    <td>
                                        <input type="text" value="__" id="START_DATE01" name="START_DATE01" class="short" value="<%=request.getParameter("START_DATE01") == null ? "" : request.getParameter("START_DATE01")%>" />
                                        <input name="image1" type="image" class="image_button" onclick="displayDatePicker('START_DATE01'); return false;" src="../../images/calendar_button.png" alt="" />   
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td class="row1 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processOnce('01')"></td> 
                        <td class="row0 alignLeft"><div id="countPro01">0</div></td> 
                    </tr>
            </table>
        </form>                
    </body>
</html>