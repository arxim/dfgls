<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="java.sql.*"%>

<%@ include file="../../_global.jsp" %>

<%
//
// Verify permission
//

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_GUARANTEE_MAIN)) {
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
            labelMap.add("TITLE_MAIN", "Guarantee", "การันตี");
            labelMap.add("MM", "Month", "เดือน");
            labelMap.add("YYYY", "Year", "ปี");
            labelMap.add("GUARANTEE_DR_CODE", "Doctor", "แพทย์");
            labelMap.add("ADMISSION_TYPE_CODE", "IPD/OPD", "ประเภทผู้ป่วย");
            labelMap.add("GUARANTEE_TYPE_CODE", "Type", "ประเภทการันตี");
            labelMap.add("START_DATE", "Start Date", "เริ่มวันที่");
            labelMap.add("START_TIME", "Start Time", "เริ่มเวลา");
            labelMap.add("END_DATE", "End Date", "ถึงวันที่");
            labelMap.add("END_TIME", "End Time", "ถึงเวลา");
            labelMap.add("GUARANTEE_AMOUNT", "Guarantee Amount", "จำนวนเงินการันตี");
            labelMap.add("GUARANTEE_EXCLUDE_AMOUNT", "Extra Amount", "จำนวนเงินค่าเวร");
            labelMap.add("GUARANTEE_FIX", "Fix Amount","จำนวนเงิน fix");
            labelMap.add("ACTIVE", "Active", "สถานะ");
            //labelMap.add("GUARANTEE_AMOUNT", "Guarantee Amount", "Guarantee Amount");
            //labelMap.add("GUARANTEE_PAID_AMOUNT", "Guarantee Paid Amount", "Guarantee Paid Amount");

            labelMap.add("TITLE_DATA", "Guarantee Details", "Guarantee Details");

            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
			ProcessUtil util = new ProcessUtil();
            DBConnection c = new DBConnection();
            c.connectToLocal();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
            String mouth = "";
            String year = "";
            c.Close();

            if(request.getParameter("MM") == null){
            	mouth = b.getMm();
            	year = b.getYyyy();
            }else{
            	mouth = request.getParameter("MM");
            	year = request.getParameter("YYYY");
            }
            
            
            DataRecord doctorRec = null;
            String mm = request.getParameter("MM") != null ? request.getParameter("MM") : JDate.getMonth();
            String yyyy = request.getParameter("YYYY") != null ? request.getParameter("YYYY") : JDate.getYear();
            
            if (request.getParameter("GUARANTEE_DR_CODE") != null) {
            	doctorRec = DBMgr.getRecord("SELECT * FROM DOCTOR WHERE CODE = '" + request.getParameter("GUARANTEE_DR_CODE") + "' AND HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE")+"'");
                //doctorRec = DBMgr.getRecord("SELECT * FROM DOCTOR WHERE CODE = '" + request.getParameter("GUARANTEE_DR_CODE") + "'");
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
            
            function GUARANTEE_DR_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.GUARANTEE_DR_CODE.blur();
                    return false;
                }else {
                    return true;
                }
            }

            function AJAX_Refresh_GUARANTEE_DR() {
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.GUARANTEE_DR_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_GUARANTEE_DR);
            }
            
            function AJAX_Handle_Refresh_GUARANTEE_DR() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.GUARANTEE_DR_CODE.value = "";
                        document.mainForm.GUARANTEE_DR_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.GUARANTEE_DR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }           
        </script>
    </head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="">
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("guarantee_main.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
		</table>
            </center>
            <table class="form">
                <tr>
				<th colspan="4">
				  <div style="float: left;">
				  ${labelMap.TITLE_MAIN}</div>
				</th>
				</tr>
				<tr>
                    <td class="label">
                        <label for="aText">${labelMap.MM}</label>
                    </td>
                    <td class="input"><%=util.selectMM(session.getAttribute("LANG_CODE").toString(), "MM", mouth)%></td>
                    <td class="label">
                         <label>${labelMap.YYYY}</label>
					</td>
                    <td class="input"><%=util.selectYY("YYYY", year)%></td>
                </tr>                
				<tr>
                    <td class="label"><label for="GUARANTEE_DR_CODE">${labelMap.GUARANTEE_DR_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="GUARANTEE_DR_CODE" name="GUARANTEE_DR_CODE" class="short" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" onkeypress="return GUARANTEE_DR_CODE_KeyPress(event);" onblur="AJAX_Refresh_GUARANTEE_DR();" />
                        <input id="SEARCH_GUARANTEE_DR_CODE" name="SEARCH_GUARANTEE_DR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=GUARANTEE_DR_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_GUARANTEE_DR'); return false;" />
                        <input type="text" id="GUARANTEE_DR_NAME" name="GUARANTEE_DR_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME_" + labelMap.getFieldLangSuffix()) %>" />
                    </td>
                </tr>
                <tr>
                    <th colspan="6" class="buttonBar">                        
                        <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="window.location = 'guarantee_main.jsp?GUARANTEE_DR_CODE=' + document.mainForm.GUARANTEE_DR_CODE.value + '&MM=' +  document.mainForm.MM.value + '&YYYY=' +  document.mainForm.YYYY.value; return false;" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='guarantee_main.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
            <hr/>
            <table class="data">
                <tr>
                    <th colspan="11" class="alignLeft">${labelMap.TITLE_DATA}</th>
                </tr>
                <tr>
                    <td class="sub_head">${labelMap.ADMISSION_TYPE_CODE}</td>
                    <td class="sub_head">${labelMap.GUARANTEE_TYPE_CODE}</td>
                    <td class="sub_head">${labelMap.START_DATE}</td>
                    <td class="sub_head">${labelMap.START_TIME}</td>
                    <td class="sub_head">${labelMap.END_DATE}</td>
                    <td class="sub_head">${labelMap.END_TIME}</td>
                    <td class="sub_head">${labelMap.GUARANTEE_AMOUNT}</td>
                    <td class="sub_head">${labelMap.GUARANTEE_EXCLUDE_AMOUNT}</td>
                    <td class="sub_head">${labelMap.GUARANTEE_FIX}</td>
                    <td class="sub_head">${labelMap.ACTIVE}</td>
                    <td class="sub_head">${labelMap.EDIT}</td>
                </tr>
                <%

            DBConnection con = new DBConnection();
            con.connectToLocal();
            //con.connectToServer();
            ResultSet rs = con.executeQuery("SELECT GUARANTEE_DR_CODE, GUARANTEE_AMOUNT, GUARANTEE_EXCLUDE_AMOUNT, GUARANTEE_FIX_AMOUNT, ADMISSION_TYPE_CODE, GUARANTEE_TYPE_CODE, MM, YYYY, START_DATE, START_TIME, END_DATE, END_TIME, GUARANTEE_CODE ,ACTIVE FROM STP_GUARANTEE WHERE MM = '" + mm + "' AND YYYY = '" + yyyy + "' AND GUARANTEE_DR_CODE = '" + DBMgr.getRecordValue(doctorRec, "CODE") + "' AND HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' ORDER BY ACTIVE DESC");
            int i = 0;
            String linkEdit;
            String activeIcon;
            while (rs.next()) {
            	activeIcon = "<img src=\"../../images/" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? "" : "in") + "active_icon.png\" alt=\"" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? labelMap.get(LabelMap.ACTIVE_1) : labelMap.get(LabelMap.ACTIVE_0)) + "\" />";
                linkEdit = "<a href=\"guarantee_detail.jsp?GUARANTEE_CODE="+ rs.getString("GUARANTEE_CODE")+"&GUARANTEE_DR_CODE=" + rs.getString("GUARANTEE_DR_CODE") + "&ADMISSION_TYPE_CODE=" + rs.getString("ADMISSION_TYPE_CODE") + "&GUARANTEE_TYPE_CODE=" + rs.getString("GUARANTEE_TYPE_CODE") + "&MM=" + rs.getString("MM") + "&YYYY=" + rs.getString("YYYY") + "&START_DATE=" + rs.getString("START_DATE") + "&START_TIME=" + rs.getString("START_TIME") + "&END_DATE=" + rs.getString("END_DATE") + "&END_TIME=" + rs.getString("END_TIME") + "\" title=\"" + labelMap.get(LabelMap.EDIT) + "\"><img src=\"../../images/edit_button.png\" alt=\"" + labelMap.get(LabelMap.EDIT) + "\" /></a>";
                %>                
                <tr>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("ADMISSION_TYPE_CODE"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("GUARANTEE_TYPE_CODE"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= JDate.showDate(Util.formatHTMLString(rs.getString("START_DATE"), true))%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= JDate.showTime(Util.formatHTMLString(rs.getString("START_TIME"), true))%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= JDate.showDate(Util.formatHTMLString(rs.getString("END_DATE"), true))%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= JDate.showTime(Util.formatHTMLString(rs.getString("END_TIME"), true))%></td>                    
                    <td class="row<%=i % 2%> alignRight"><%= rs.getDouble("GUARANTEE_AMOUNT")%></td>
                    <td class="row<%=i % 2%> alignRight"><%= rs.getDouble("GUARANTEE_EXCLUDE_AMOUNT")%></td>
                    <td class="row<%=i % 2%> alignRight"><%= rs.getDouble("GUARANTEE_FIX_AMOUNT")%></td>
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
            //con.freeConnection();
                %>                
                <tr>
                    <th colspan="11" class="buttonBar">
                        <input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}" onclick="window.location = 'guarantee_detail.jsp?GUARANTEE_DR_CODE=' + document.mainForm.GUARANTEE_DR_CODE.value + '&MM=' + document.mainForm.MM.value + '&YYYY=' + document.mainForm.YYYY.value;" >
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
