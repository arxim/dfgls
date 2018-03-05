<%-- 
    Document   : ProcessDrCreateRandomPassword
    Created on : 30 ต.ค. 2551, 13:04:52
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_PROCESS_DEMO)) {
                response.sendRedirect("../message.jsp");
                return;
            }

            //
            // Initial LabelMap
            //

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }   
            
            String insert = request.getParameter("insert");
            if("insert".equalsIgnoreCase(insert)){
            	String[] code = request.getParameterValues("arr_loginname");
            	String[] password = request.getParameterValues("arr_password");
            	String[] hospital = request.getParameterValues("arr_hospital");
            	String[] name = request.getParameterValues("arr_name");
            	
            	DBConnection con = new DBConnection();
            	con.connectToLocal();
            	con.beginTrans();
            	String sql = "";
            	try{
                	for(int i=0 ; i < code.length ; i++){
                		sql = "INSERT INTO USERS(HOSPITAL_CODE,LOGIN_NAME,PASSWORD, NAME, USER_GROUP_CODE,ACTIVE,LANG_CODE) "
                		+" VALUES('"+hospital[i]+"','" + code[i] + "','" + password[i] + "','" 
                		+new String(name[i].getBytes("ISO8859-1"),"utf-8") + "','5','1','E')";
                		con.executeUpdate(sql);
                		//out.print(sql + "<br>");
                	}
                	con.commitTrans();
                	out.print("Success !");
            	}catch (Exception e){
            		con.rollBackTrans();	
            	}finally{
                	con.Close();
                	if(0==0){ return; }            		
            	}
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript" src="../../javascript/md5.js"></script>
        <script>
	        function generatePassword() {
	            var length = 8,
	                charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
	                retVal = "";
	            for (var i = 0, n = charset.length; i < length; ++i) {
	                retVal += charset.charAt(Math.floor(Math.random() * n));
	            }
	            return retVal;
	        }
	        
	        function Report_Save() {
		       	fnExcelReport();
	        }
	        

	        function fnExcelReport()
	        {
	            var tab_text="<table border='2px'>";
	            var textRange;
	            var tab = document.getElementById('dataTable'); // id of table
	            
	            tab_text += '<tr>';
	            tab_text += '<td>Doctor Code</td>';
	            tab_text += '<td>Doctor Name</td>';
	            tab_text += '<td>Password</td>';
	            tab_text += '</tr>';
	            
	            for (var r = 0; r < tab.rows.length; r++) {
	            	tab_text += '<tr>';
	            	for (var c = 0; c < tab.rows[r].cells.length - 1; c++) {
		            	var cell = tab.rows[r].cells[c];
		            	var text = '';
		            	if (cell.getElementsByTagName("INPUT").length) {
		            		text = cell.getElementsByTagName("INPUT")['password'].value;
		            	} else {
		            		text = cell.textContent;
		            	}
		            	tab_text += '<td>' + text + '</td>';
		            }
	            	tab_text += '</tr>';
	            }
	            tab_text += "</table>";
	            
	            tab_text= tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
	            tab_text= tab_text.replace(/<img[^>]*>/gi,""); // remove if u want images in your table
	            //tab_text= tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params
				
	            var ua = window.navigator.userAgent;
	            var msie = ua.indexOf("MSIE"); 
	            var date = new Date();
                var dd = date.getDate();
                var mm = date.getMonth()+1; //January is 0!
                var yyyy = date.getFullYear();
                var hh = date.getHours();
               	var minutes = date.getMinutes();
                var ss = date.getSeconds();
                if(dd<10){
                    dd='0'+dd;
                } 
                if(mm<10){
                    mm='0'+mm;
                } 
                var today = yyyy+mm+dd+hh+minutes+ss;
                var filename = 'NewUser_' + today + '.xls';
                
	            if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) {
	            	// If Internet Explorer
	                txtArea1.document.open("application/vnd.ms-excel","replace");
	                txtArea1.document.write(tab_text);
	                txtArea1.document.close();
	                txtArea1.focus();
	                sa=txtArea1.document.execCommand("SaveAs", true, filename);
	            } else {
	            	//other browser
	            	sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));  
	            	/*var sa = document.createElement('a');
	                var data_type = 'data:application/vnd.ms-excel';
	                var table_div = tab_text;    //Your tab_text   
	                var table_html = table_div.replace(/ /g, '%20');
	                sa.href = data_type + ', ' + table_html;
	                //setting the file name
	                sa.download = filename;
	                //triggering the function
	                sa.click();*/
	            }             

	            return (sa);
	        }
        </script>
    </head>
    <body>
    	<% 
    	DBConnection con = new DBConnection();
    	con.connectToLocal();
    	ResultSet rs = con.executeQuery("SELECT DP.CODE, DP.NAME_THAI, DP.HOSPITAL_CODE FROM DOCTOR_PROFILE DP LEFT JOIN USERS U ON DP.HOSPITAL_CODE = U.HOSPITAL_CODE AND DP.CODE = U.LOGIN_NAME WHERE DP.ACTIVE='1' AND DP.HOSPITAL_CODE  = '"+session.getAttribute("HOSPITAL_CODE")+"' AND U.LOGIN_NAME IS NULL");
    	%>
        <form id="mainForm" name="mainForm" method="post" onsubmit="Report_Save();">
            <table class="data" id="dataTable" name="dataTable">
            	<%
            	int i = 1; 
            	while(rs.next())
            	{
            		%>
		                <tr>
		                    <td class="row<%=i % 2%> alignCenter"><%=rs.getString("CODE") %></td>
		                    <td class="row<%=i % 2%> alignLeft"><%=rs.getString("NAME_THAI") %></td>
		                    <td class="row<%=i % 2%> alignCenter">"
		                    	<span id="id_<%=rs.getString("CODE")%>"></span>
				                <input type="hidden" name="arr_loginname" value="<%=rs.getString("CODE") %>"/>		                
				                <input type="hidden" name="arr_name" value="<%=rs.getString("NAME_THAI") %>"/>	
				                <input type="hidden" name="arr_hospital" value="<%=rs.getString("HOSPITAL_CODE") %>"/>
			                   	<script language="javascript">
				                   	var d = document.getElementById("id_<%=rs.getString("CODE")%>");
			                   		var defaultPassword = generatePassword();
			                   		var inputs = '';
			                   		inputs += "<input type='text' name='password' value='"+defaultPassword+"' />";
			                   		var encrypt = hex_md5(defaultPassword);
			                   		inputs += "<input type='hidden' name='arr_password' value='" + encrypt + "'/>";
			                   		d.innerHTML = inputs;
		                   		</script>	 		                    	
		                    </td>
		                    <td class="row<%=i % 2%> alignCenter">E</td>
		                </tr>           
                	<% 
                	i++;
                }
                %>
            </table>
            <input type="hidden" name="insert" value="insert"/>
            <input type="submit" name="submit" value=" Save " />
        </form>
        <iframe id="txtArea1" style="display:none"></iframe>
    </body>
</html>
