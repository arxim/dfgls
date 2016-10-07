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
<%@page import="df.bean.obj.util.Variables"%>
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
/*
			Type of Doctor
            1	System Admin 	
			2	Manager		
            3	Organize Doctor	
            4	DF User(IT)	
           *** 5	User(Doctor)	***
*/
//
// Initial LabelMap
//
//System.out.println("USER_GROUP_CODE="+session.getAttribute("USER_GROUP_CODE").toString());
        
			String select_type="";

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Setup Period Expense", "กำหนดระยะเวลาในการจ่ายค่าใช้จ่าย");
            labelMap.add("LABEL_MM_YYYY", "Month/Year", "เดือน/ปี");
            labelMap.add("LABEL_DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("LABEL_DOCTOR_NAME", "Doctor Name", "ชื่อแพทย์");
            labelMap.add("LABEL_EXPENSE_CODE", "Expense Code", "รหัสค่าใช้จ่าย");
            labelMap.add("LABEL_AMOUNT", "Amount", "จำนวนเงิน");
            labelMap.add("LABEL_TAX_AMOUNT", "Tax Amount", "จำนวนเงินภาษี");
            labelMap.add("LABEL_DATE", "Start-End Term", "ช่วงที่เริ่ม-สิ้นสุด");
            labelMap.add("LABEL_STATUS", "Status", "สถานะ");
            labelMap.add("LABEL_EDIT", "Edit", "แก้ไข");
            labelMap.add("LABEL_TYPE", "Type", "ประเภทการจ่ายเงิน");
           
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
			//ProcessUtil util = new ProcessUtil();
			ProcessUtil proUtil = new ProcessUtil();
			DataRecord doctorRec = null;
			
			String current_mm="", current_year="";
			if(request.getParameter("MM") != null)
			{
				current_mm=request.getParameter("MM");
			}
			else
			{
				current_mm=JDate.getMonth();
			}
			if(request.getParameter("YYYY") != null)
			{
				current_year =request.getParameter("YYYY"); 
			}
			else
			{
				current_year =JDate.getYear(); 
			}
			
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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
                //alert(target);
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
            }
            
            function AJAX_Handle_Refresh_DOCTOR() {
 		       if (AJAX_IsComplete()) {
                    //alert('AJAX_Handle_Refresh_DEPARTMENT');
                    var xmlDoc = AJAX.responseXML;
                    //alert("xmlDoc="+xmlDoc);
					//alert("result="+isXMLNodeExist(xmlDoc, "CODE"));
                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
						//alert(isXMLNodeExist(xmlDoc, "CODE"));
						//alert("55555555");
						//alert("name="+getXMLNodeValue(xmlDoc, "NAME_ENG"));
                        document.mainForm.DOCTOR_CODE_SEARCH.value = "";
                        document.mainForm.DOCTOR_NAME_SEARCH.value = "";
                        return;
                    }
                   // alert("kkkkkkkkkkk");
                    // Data found
					//var d_code=getXMLNodeValue(xmlDoc, "DOCTOR_PROFILE_CODE");
					//var code=getXMLNodeValue(xmlDoc, "CODE");
					//alert("name="+getXMLNodeValue(xmlDoc, "NAME_ENG"));
					document.mainForm.DOCTOR_NAME_SEARCH.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
					
                }
            }   
            function doctorOnSearch(id)
            {
            	//alert("ok");
            	if(id==5)
            	{
					var DoctorProfileCode = document.getElementById('DoctorProfileCode');
					var url='../search.jsp?TABLE=DOCTOR&TABLE1=DOCTOR_PROFILE&TARGET=DOCTOR_CODE_SEARCH&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&COND=[AND (DOCTOR.DOCTOR_PROFILE_CODE=DOCTOR_PROFILE.CODE) AND (DOCTOR.ACTIVE=\''+1+'\') AND (DOCTOR.DOCTOR_PROFILE_CODE=\''+ DoctorProfileCode.value +'\') ]&HANDLE=AJAX_Refresh_DOCTOR';
				}
				else
				{
					var url='../search.jsp?TABLE=DOCTOR&TABLE1=DOCTOR_PROFILE&TARGET=DOCTOR_CODE_SEARCH&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&COND=[AND (DOCTOR.DOCTOR_PROFILE_CODE=DOCTOR_PROFILE.CODE) AND (DOCTOR.ACTIVE=\''+1+'\')]&HANDLE=AJAX_Refresh_DOCTOR';
				}
				//alert(url);
				openSearchForm(url);
			}	
	    </script>
    </head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="">
        <input type="hidden" name="DoctorProfileCode" id="DoctorProfileCode" value="<%=session.getAttribute("USER_ID").toString() %>">
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("stp_tax_reduce_main.jsp", labelMap.getFieldLangSuffix(), new DBConnection())%></font></b>
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
                  <td colspan="3" class="input"><input type="text" id="DOCTOR_CODE_SEARCH" name="DOCTOR_CODE_SEARCH" class="short" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" readonly/>
                    <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="doctorOnSearch(<%=session.getAttribute("USER_GROUP_CODE").toString() %>); return false;" />
                    <input type="text" id="DOCTOR_NAME_SEARCH" name="DOCTOR_NAME_SEARCH" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME_" + labelMap.getFieldLangSuffix()) %>" /></td>
                </tr><tr>
                  <td class="label"><label for="LABEL_MM_YYYY">${labelMap.LABEL_MM_YYYY}</label></td>
                  <td colspan="3" class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "MM",current_mm)%>
/ <%=proUtil.selectYY("YYYY", current_year)%></td>
                </tr>
                
                <tr>
                    <th colspan="6" class="buttonBar">                        
                        <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="window.location = 'stp_tax_reduce_main.jsp?DOCTOR_CODE=' + document.mainForm.DOCTOR_CODE_SEARCH.value+'&MM='+ document.mainForm.MM.value+'&YYYY='+ document.mainForm.YYYY.value ; return false;" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='stp_tax_reduce_main.jsp'" />
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
                    <td class="sub_head">${labelMap.LABEL_MM_YYYY}</td>
                    <td class="sub_head">${labelMap.LABEL_STATUS}</td>
                    <td class="sub_head">${labelMap.LABEL_EDIT}</td>
                    
                </tr>
                <%

            DBConnection con = new DBConnection();
            con.connectToLocal();
            String sql="SELECT T.DOCTOR_CODE, D.NAME_"+ labelMap.getFieldLangSuffix() +" AS DOCTOR_NAME, "
            +" T.YYYY, T.MM, T.STATUS "
            +" FROM STP_TAX_REDUCE T, DOCTOR D "
            +" WHERE T.DOCTOR_CODE=D.CODE ";
            sql+=" AND T.HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' ";
			if(!DBMgr.getRecordValue(doctorRec, "CODE").equals("")) 
			{
				sql+=" AND T.DOCTOR_CODE = '" + DBMgr.getRecordValue(doctorRec, "CODE") + "' ";
			}
			else if(session.getAttribute("USER_GROUP_CODE").toString().equals("5"))
			{
				sql+=" AND D.DOCTOR_PROFILE_CODE='"+session.getAttribute("USER_ID").toString()+"'";
			}
			sql+=" AND T.MM='"+current_mm+"' AND T.YYYY='"+current_year+"'";
			sql+=" ORDER BY T.DOCTOR_CODE, T.YYYY, T.MM DESC";
            //out.println("sql="+sql);
             ResultSet rs = con.executeQuery(sql);
            
            int i = 0;
            String linkEdit="";
            String activeIcon="";
            String doctor_name_show="", date_show="";
            
             while (rs.next()) {
            	String show_type="";
            	doctor_name_show="("+rs.getString("DOCTOR_CODE")+") "+rs.getString("DOCTOR_NAME");
            	date_show=rs.getString("MM")+"/"+rs.getString("YYYY");
            	activeIcon = "<img src=\"../../images/" + (rs.getString("STATUS") != null && rs.getString("STATUS").equalsIgnoreCase("1") ? "" : "in") + "active_icon.png\" alt=\"" + (rs.getString("STATUS") != null && rs.getString("STATUS").equalsIgnoreCase("1") ? labelMap.get(LabelMap.ACTIVE_1) : labelMap.get(LabelMap.ACTIVE_0)) + "\" />";
            	linkEdit = "<a href='stp_tax_reduce.jsp?DOCTOR_CODE="+ rs.getString("DOCTOR_CODE")+"&MM="+ rs.getString("MM")+"&YYYY=" + rs.getString("YYYY")+ "'><img src=\"../../images/edit_button.png\" alt=\"" + labelMap.get(LabelMap.EDIT) + "\" /></a>";
                //if(rs.getString("TYPE_EXPENSE").equals("1")) { show_type="จ่ายเงินเท่ากันทุกๆ เดือน"; }
                //else { show_type="จำนวนเงินแบ่งจ่ายเป็นรายเดือน";} %>                
                <tr>
                    <td class="row<%=i % 2%>">&nbsp;<%=doctor_name_show%></td>
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
                        <input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}" onclick="window.location = 'stp_tax_reduce.jsp';" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>


