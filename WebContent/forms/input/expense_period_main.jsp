<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.obj.util.*" %>
<%@page import="java.sql.*"%>

<%@ include file="../../_global.jsp" %>

<%
//
// Verify permission
//   

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_EXPENSE_PERIOD)) {
                response.sendRedirect("../message.jsp");
                return;
            }

//
// Initial LabelMap
//

			String select_type="";

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Setup Advance Adjust", "ตั้งค่ารายการปรับปรุงล่วงหน้า");
            labelMap.add("LABEL_DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("LABEL_DOCTOR_NAME", "Doctor Name", "ชื่อแพทย์");
            labelMap.add("LABEL_EXPENSE_CODE", "Expense Code", "รหัสค่าใช้จ่าย");
            labelMap.add("LABEL_AMOUNT", "Amount", "จำนวนเงิน");
            labelMap.add("LABEL_TAX_AMOUNT", "Tax Amount", "จำนวนเงินภาษี");
            labelMap.add("LABEL_DATE", "Start-End Term", "ช่วงที่เริ่ม-สิ้นสุด");
            labelMap.add("LABEL_STATUS", "Status", "สถานะ");
            labelMap.add("LABEL_EDIT", "Edit", "แก้ไข");
            labelMap.add("LABEL_TYPE", "Adjust Type", "ประเภทรายการปรับปรุงค่าแพทย์");
            labelMap.add("LABEL_TYPE_1", "Fix Money", "กำหนดยอดเงินแบ่งจ่าย");
            labelMap.add("LABEL_TYPE_2", "Fix Month", "กำหนดจำนวนเดือนแบ่งจ่าย");

            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
			ProcessUtil util = new ProcessUtil();
            DataRecord doctorRec = null;
            //String mm = request.getParameter("MM") != null ? request.getParameter("MM") : JDate.getMonth();
            //String yyyy = request.getParameter("YYYY") != null ? request.getParameter("YYYY") : JDate.getYear();
            //System.out.println("group="+request.getParameter("GROUP_CODE"));
            if (request.getParameter("DOCTOR_CODE") != null) {
            	doctorRec = DBMgr.getRecord("SELECT * FROM DOCTOR WHERE CODE = '" + request.getParameter("DOCTOR_CODE") + "'");
            }
            if (request.getParameter("SEL_TYPE") != null) {
				select_type=request.getParameter("SEL_TYPE");
				//out.println("select_type="+select_type);
			}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
        <script type="text/javascript">
            
            function DOCTOR_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CODE_SEARCH.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR() {
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE_SEARCH.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
            }
            
            function AJAX_Handle_Refresh_DOCTOR() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DOCTOR_CODE_SEARCH.value = "";
                        document.mainForm.DOCTOR_NAME_SEARCH.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_NAME_SEARCH.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }           
        </script>
    </head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="">
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("expense_period_main.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
		</table>
            </center>
            <table class="form">
                <tr>
				<th colspan="4">
				  <div style="float: left;">
				  ${labelMap.TITLE_MAIN}</div>				</th>
				</tr>
                <tr>
                  <td class="label"><label for="LABEL_DOCTOR_CODE">${labelMap.LABEL_DOCTOR_CODE}</label></td>
                  <td colspan="3" class="input"><input type="text" id="DOCTOR_CODE_SEARCH" name="DOCTOR_CODE_SEARCH" class="short" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR();" />
                    <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="doctorOnSearch(); return false;" />
                    <input type="text" id="DOCTOR_NAME_SEARCH" name="DOCTOR_NAME_SEARCH" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME_" + labelMap.getFieldLangSuffix()) %>" /></td>
                </tr>
                <tr>
                    <td class="label"><label for="LABEL_TYPE">${labelMap.LABEL_TYPE}</label></td>
                    <td colspan="3" class="input"><select name="SEL_TYPE">
                      <option value="">SELECT TYPE</option>
                      <option value="1" <%= (select_type.equals("1") ? "selected" : "") %>>${labelMap.LABEL_TYPE_1}</option>
                      <option value="2" <%= (select_type.equals("2") ? "selected" : "") %>>${labelMap.LABEL_TYPE_2}</option>
                    </select>
                    </td>
                </tr>
                <tr>
                    <th colspan="6" class="buttonBar">                        
                        <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="window.location = 'expense_period_main.jsp?DOCTOR_CODE=' + document.mainForm.DOCTOR_CODE_SEARCH.value+'&SEL_TYPE='+ document.mainForm.SEL_TYPE.value ; return false;" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='expense_period_main.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />                    </th>
                </tr>
            </table>
            <hr />
            <table class="data">
                <tr>
                    <th colspan="10" class="alignLeft">&nbsp;</th>
                </tr>
                <tr>
                    <td class="sub_head">${labelMap.LABEL_DOCTOR_NAME}</td>
                    <td class="sub_head">${labelMap.LABEL_EXPENSE_CODE}</td>
                    <td class="sub_head">${labelMap.LABEL_TYPE}</td>
                    <td class="sub_head">${labelMap.LABEL_AMOUNT}</td>
                    <td class="sub_head">${labelMap.LABEL_TAX_AMOUNT}</td>
                    <td class="sub_head">${labelMap.LABEL_DATE}</td>
                    <td class="sub_head">${labelMap.LABEL_STATUS}</td>
                    <td class="sub_head">${labelMap.LABEL_EDIT}</td>
                    
                </tr>
                <%

            DBConnection con = new DBConnection();
            con.connectToLocal();
            String sql="SELECT SD.EXPENSE_CODE AS EXPENSE_CODE, SD.AMOUNT AS AMOUNT, SD.DOCTOR_CODE AS DOCTOR_CODE "
            +", SD.ACTIVE AS ACTIVE, DC.NAME_"+ labelMap.getFieldLangSuffix() +" AS DOCTOR_NAME, ";
            sql+=" SD.START_TERM_MM, SD.START_TERM_YYYY, SD.END_TERM_MM, SD.END_TERM_YYYY, SD.TYPE_EXPENSE, SD.TAX_AMOUNT ";
            sql+=" FROM STP_PERIOD_EXPENSE SD, DOCTOR DC ";
            sql+=" WHERE (SD.DOCTOR_CODE=DC.CODE AND SD.HOSPITAL_CODE=DC.HOSPITAL_CODE) ";
            sql+=" AND SD.HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' ";
			if(!DBMgr.getRecordValue(doctorRec, "CODE").equals("")) 
			{
				sql+=" AND SD.DOCTOR_CODE = '" + DBMgr.getRecordValue(doctorRec, "CODE") + "' ";
			}
			if(select_type !="") 
			{
				sql+=" AND SD.TYPE_EXPENSE='"+select_type+"'";
			}
            sql+=" ORDER BY SD.DOCTOR_CODE, SD.EXPENSE_CODE ASC";
            //out.println("sql="+sql);
             ResultSet rs = con.executeQuery(sql);
            
            int i = 0;
            String linkEdit;
            String activeIcon;
            String doctor_name_show="", date_show="";
            
             while (rs.next()) {
            	String show_type="";
            	doctor_name_show="("+rs.getString("DOCTOR_CODE")+") "+rs.getString("DOCTOR_NAME");
            	date_show=rs.getString("START_TERM_MM")+"/"+rs.getString("START_TERM_YYYY")+"-"+rs.getString("END_TERM_MM")+"/"+rs.getString("END_TERM_YYYY");
            	activeIcon = "<img src=\"../../images/" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? "" : "in") + "active_icon.png\" alt=\"" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? labelMap.get(LabelMap.ACTIVE_1) : labelMap.get(LabelMap.ACTIVE_0)) + "\" />";
            	linkEdit = "<a href='expense_period_detail.jsp?DOCTOR_CODE="+ rs.getString("DOCTOR_CODE")+"&ACTIVE="+ rs.getString("ACTIVE")+"&START_TERM_YYYY="+ rs.getString("START_TERM_YYYY")+"&START_TERM_MM="+ rs.getString("START_TERM_MM")+"&EXPENSE_CODE="+ rs.getString("EXPENSE_CODE")+"&SEL_TYPE=" + rs.getString("TYPE_EXPENSE")+ "'><img src=\"../../images/edit_button.png\" alt=\"" + labelMap.get(LabelMap.EDIT) + "\" /></a>";
                if(rs.getString("TYPE_EXPENSE").equals("1")) { show_type="Fix Money / เงินเท่ากันทุกเดือน"; }
                else { show_type="Fix Month / เงินตามจำนวนเดือน";} %>                
                <tr>
                    <td class="row<%=i % 2%>">&nbsp;<%=doctor_name_show%></td>
                    <td class="row<%=i % 2%>">&nbsp;<%= Util.formatHTMLString(rs.getString("EXPENSE_CODE"), true)%></td>
                    <td class="row<%=i % 2%> ">&nbsp;<%=show_type%></td>
                    <td class="row<%=i % 2%> ">&nbsp;<%=JNumber.getShowMoney(Double.parseDouble(Util.formatHTMLString(rs.getString("AMOUNT"), true)))%></td>
                    <td class="row<%=i % 2%> ">&nbsp;<%=JNumber.getShowMoney(Double.parseDouble(Util.formatHTMLString(rs.getString("TAX_AMOUNT"), true)))%></td>
                    <td class="row<%=i % 2%> ">&nbsp;<%=date_show%></td>
                    <td class="row<%=i % 2%> alignCenter">&nbsp;<%= activeIcon %></td>
                    <td class="row<%=i % 2%> alignCenter">&nbsp;<%= linkEdit%></td>
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
                    <th colspan="10" class="buttonBar">
                        <input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}" onclick="window.location = 'expense_period_detail.jsp?DOCTOR_CODE=' + document.mainForm.DOCTOR_CODE_SEARCH.value+'&SEL_TYPE='+ document.mainForm.SEL_TYPE.value;" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
<script language="javascript">
	function doctorOnSearch(){
		//var y = document.getElementById('YYYY');
		//var m = document.getElementById('MM');
		openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE_SEARCH&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR');	
	}
</script>

