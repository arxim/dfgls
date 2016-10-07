<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>

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
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            DataRecord user_group = null;
            byte MODE = DBMgr.MODE_INSERT;
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "User Group", "กลุ่มผู้ใช้");
            labelMap.add("USER_GROUP","User Group","กลุ่มผู้ใช้งาน");
            request.setAttribute("labelMap", labelMap.getHashMap());

            if (request.getParameter("MODE") != null) {
                MODE = Byte.parseByte(request.getParameter("MODE"));
                String[] arr_menu = request.getParameterValues("name_menu");
                String user_group_code = request.getParameter("USER_GROUP");

                DBConnection con = new DBConnection();
                con.connectToLocal();

                String sql_delete = "DELETE STP_MENU_MATCH WHERE USER_GROUP_CODE='" + user_group_code + "' AND HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString()+ "'";
                
                con.executeUpdate(sql_delete);
                String sql_insert = "";
                
                for(int i=0; i < arr_menu.length; i++ ){
                    sql_insert += " INSERT INTO  STP_MENU_MATCH(USER_GROUP_CODE , MENU_CODE , HOSPITAL_CODE) VALUES ('"+user_group_code+"','"+arr_menu[i]+"' , '" + session.getAttribute("HOSPITAL_CODE").toString()+ "'); ";
                }
                con.executeUpdate(sql_insert);
                con.Close();
                response.sendRedirect("user_group.jsp");
                return;
            }
            if(request.getParameter("USER_GROUP") != null){
                user_group = DBMgr.getRecord("SELECT * FROM USER_GROUP WHERE USER_GROUP = '" + request.getParameter("USER_GROUP") + "' AND HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString()+ "'");
            }

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
    </head>
    <body>
    	<form id="mainForm" name="mainForm" method="post" action="user_group_detail.jsp">
        <table class="form">
                <input type="hidden"  name="USER_GROUP" value="<%=request.getParameter("USER_GROUP")%>"/>
                <input type="hidden"  name="HOSPITAL_CODE"  value="'<%=session.getAttribute("HOSPITAL_CODE").toString() %>"/>
                <input type="hidden" id="MODE" name="MODE" value="<%= MODE %>" />
                <tr>
                    <th colspan="6">
				  	<div style="float: left;">${labelMap.TITLE_MAIN}</div>
				  	</th>
                </tr>
                <tr>
                    <td class="label"><label for="MODULE_02_1_CHECKBOX">${labelMap.USER_GROUP}</label></td>
                    <td class="input"><label for="MODULE_02_1_CHECKBOX"><%= DBMgr.getRecordValue(user_group, "ACTION_TYPE")%></label></td>
                </tr>
                <%
                
                String menusql = "SELECT a.*, (SELECT '1' FROM STP_MENU_MATCH AS b WHERE b.MENU_CODE=a.CODE AND b.USER_GROUP_CODE='"+ request.getParameter("USER_GROUP") +"' AND b.HOSPITAL_CODE  = '"+session.getAttribute("HOSPITAL_CODE").toString()+"') as checkmenu FROM STP_MENU as a WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' order by convert(int,CODE)";
               
                DBConnection con = new DBConnection();
                con.connectToServer();
                ResultSet rs = con.executeQuery(menusql);
                int i = 0;
                String dd = "";
                String onclickHeader = "";
                String nameHeader = "";
                String nameMenu = "";
                String value = "";
                while (rs.next()) {
                    i++;
                    value = "value=\"" + rs.getString("CODE") + "\"";
                     if("0".equalsIgnoreCase(rs.getString("PARENT_CODE"))) {
                        dd = "" ;
                        onclickHeader = "onclick=\"headchk('"+ rs.getString("CODE") +"',this);\"";
                        nameHeader = "name=\"name_menu\" id=\"head" + rs.getString("CODE") + "\"";
                        nameMenu = "";
                     }else{
                        dd = "<dd>"  ;
                        onclickHeader = "";
                        nameHeader = "";
                        nameMenu = "name=\"name_menu\" id=\"menu" + rs.getString("PARENT_CODE") + "\"";
                     }
                    %>
                    <tr>
                        <td class="label"><label>&nbsp;</label></td>
                        <td class="input">
                            <label for="MODULE_02_1_CHECKBOX">
                                <%=dd%>
                                <input type="checkbox" <%=nameHeader%> <%=nameMenu%> <%=value%> <%=onclickHeader%> <%= "1".equalsIgnoreCase(rs.getString("checkmenu")) ? "checked" : "" %>/>
                                <%=rs.getString("MENU_"+labelMap.getFieldLangSuffix()) %>
                            </label>
                        </td>
                    </tr>
                    <%
                }
                %>
                <tr>
                    <th colspan="6" class="buttonBar">
                        <input type="submit" id="NEW" name="NEW" class="button" value="${labelMap.SAVE}"/>
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="history.back();" />
                    </th>
                </tr>        	   
        </table>
        </form> 
        
        <script language="javascript">
			    function headchk(id,obj){
			        var nmenu = 'menu' + id;
			        var e = document.mainForm[nmenu];
			        if(obj.checked == true){
			            if(e.length != undefined){
			                for(var i = 0; i < e.length ; i++){
			                    e[i].checked = true;
			                }
			            }else{
			                document.getElementById(nmenu).checked = true ;
			            }
			        }else{
			            if(e.length != undefined){
			                for(var i = 0; i < e.length ; i++){
			                    e[i].checked = false;
			                }
			            }else{
			                document.getElementById(nmenu).checked = false ;
			            }
			        }
			    }
		</script>


    </body>
</html>
