<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>

<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_USER_GROUP)) {
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
            labelMap.add("TITLE_MAIN", "User Group", "กลุ่มผู้ใช้");
            labelMap.add("USER_GROUP", "Type", "ประเภท");

            labelMap.add("CODE", "CODE", "รหัส");
            labelMap.add("DESCRIPTION", "DESCRIPTION", "รายละเอียด");

            request.setAttribute("labelMap", labelMap.getHashMap());

            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            DataRecord userGroupRec = null;

            if (request.getParameter("USER_GROUP") != null) {
            	
            	String sqlCommand =  " SELECT * FROM USER_GROUP WHERE USER_GROUP = " + request.getParameter("USER_GROUP") + " AND HOSPITAL_CODE =  '"+ session.getAttribute("HOSPITAL_CODE")+"'";
            	
                userGroupRec = DBMgr.getRecord(sqlCommand);
                
                if (userGroupRec == null) {
                    response.sendRedirect("../message.jsp");
                }
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"></meta>
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
        <script type="text/javascript">
            
        </script>
    </head>
    <body>
    	<form id="permissionForm" name="permissionForm" method="post" action="user_group_detail.jsp" >
        <table class="form">            
                <tr>
                    <th colspan="4">
				  	<div style="float: left;">${labelMap.TITLE_MAIN}</div>
				  	</th>
                </tr>
                <tr>
                    <th>${labelMap.CODE}</th>
                    <th>${labelMap.DESCRIPTION}</th>
                    <th>${labelMap.EDIT}</th>
                </tr>
                <%
                String menusql = "SELECT  * FROM USER_GROUP WHERE HOSPITAL_CODE  = '" + session.getAttribute("HOSPITAL_CODE") + "'";
                DBConnection con = new DBConnection();
                con.connectToServer();
                ResultSet rs = con.executeQuery(menusql);
                String linkEdit = "";
                while (rs.next()) {
                    linkEdit = "<a href=\"user_group_detail.jsp?USER_GROUP="+rs.getString("USER_GROUP")+"\" title=\"" + labelMap.get(LabelMap.EDIT) + "\"><img src=\"../../images/edit_button.png\" alt=\"Edit\" /></a>";
                    %>
                    <tr>
                        <td class="label"><label><%=rs.getString("USER_GROUP") %></label></td>
                        <td class="input">
                            <label for="MODULE_02_1_CHECKBOX"><%=rs.getString("ACTION_TYPE") %></label>
                        </td>
                        <td class="input alignCenter">
                            <%=linkEdit%>
                        </td>
                    </tr>
                <%
                }
                %>
	                <tr>
	                    <th colspan="6" class="buttonBar">                        
	                        <!--  <input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}" onclick="window.location.href='user_group_detail.jsp'"></input> -->
							<input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'"></input>
	                    </th>
	                </tr>
        </table>
        </form>
    </body>
</html>
