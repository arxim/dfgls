<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.obj.util.Variables"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link rel="stylesheet" type="text/css" href="css/share.css" media="all" />
<link rel="stylesheet" type="text/css" href="css/header.css" media="all" />
<link rel="stylesheet" type="text/css" href="template/css/reset.css"	media="screen" />
<link rel="stylesheet" type="text/css" href="template/css/text.css"	media="screen" />
<link rel="stylesheet" type="text/css" href="template/css/grid.css"	media="screen" />
<link rel="stylesheet" type="text/css" href="template/css/layout.css" media="screen" />
<link rel="stylesheet" type="text/css" href="template/css/nav.css"	media="screen" />
<link rel="stylesheet" type="text/css" href="template/css/prettyPhoto.css" />

<script type="text/javascript"  src="template/js/jquery-1.6.4.min.js" ></script>
<script type="text/javascript"	src="template/js/jquery-ui/jquery.ui.core.min.js"></script>

<!--[if IE 6]><link rel="stylesheet" type="text/css" href="css/ie6.css" media="screen" /><![endif]-->
<!--[if IE 7]><link rel="stylesheet" type="text/css" href="css/ie.css" media="screen" /><![endif]-->
<!-- BEGIN: load jquery -->

<script language="Javascript" type="text/javaScript">

			// Function menu data
			/*
	        function changeLableMenu(value){
	            var e = document.getElementById("control_menu");
	            if(value){
					e.innerHTML = "<a href='javascript:parent.toggleFrame();' title='Hide menu.' style='color:white;'></a>";
	            }else{
	                e.innerHTML = "<a href='javascript:parent.toggleFrame();' title='Show menu.' style='color:white;'></a>";
	            }
	        }
	        */
	        /*
	       	*  Change Languag
	        */
	        function ChangeLang(ln){
	            var e = document.getElementById("ifrm");
	            if(ln=="TH"){
	                e.src = "switch_lang.jsp?lang=<%=LabelMap.LANG_TH%>";
	            }else{
	                e.src = "switch_lang.jsp?lang=<%=LabelMap.LANG_EN%>";
				}
	            
				parent.mainFrame.location.reload();
				parent.leftFrame.location.reload();
			}
</script>

<style type="text/css">
body{ 
	color: #333;
	font-size: 11px;
	padding: 0 0 0px;
}
</style>

</head>
<body style="background-color: #204562;">
 <div class="container_12" style="background-color: #204562;">
	<div class="grid_12 header-repeat">
		<div id="branding" style="background-color: #204562;">
			<div class="floatleft">

				<!-- <img src="img/logo.png" alt="Logo" /> -->
					<table border="1">
						<tr valign="middle">
							<td id="control_menu" >
								<a href="javascript:parent.toggleFrame();"  title="Hide/Show Menu"  style="color:white;"><img src='images/menu_show.png' width='20' height='20' /></a>
							</td>
							<td style="padding-left: 10px;" align="center">
							<h1 style="color:white;">iDoctor V4.5.5</h1> 
							</td>
						</tr>
					</table>	
                  
			</div>
			<div class="floatleft" style="padding-left:40px;">
		
			 	<!--   Icon  -->
			            <%
			            //String menusql = "select * from STP_MENU order by convert(int,CODE)";
			            String menusql = "select ICON_LINK, ICON_PICTURE, ICON_DESCRIPTION from STP_MENU as a where HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE")+"' AND CODE in (select MENU_CODE from STP_MENU_MATCH as b where b.MENU_CODE=a.CODE and b.USER_GROUP_CODE='"+ session.getAttribute("USER_GROUP_CODE").toString() +"') " +
			                    //"and a.HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' and a.ICON_LINK IS NOT NULL and a.ICON_LINK<>'' order by convert(int,CODE);";
			                    "and a.ICON_LINK IS NOT NULL and a.ICON_LINK<>'' order by convert(int,CODE);";
			            
			            DBConnection con = new DBConnection();
			            con.connectToLocal();
			            
			            ResultSet rs = con.executeQuery(menusql);
			            int i = 0;
			            while (rs.next()) {
			            	%>
			           		<a href="<%=rs.getString("ICON_LINK")%>" target="mainFrame" style="color: none; ahover:none;">
			           			<img width='34px' height='34px' src="<%=rs.getString("ICON_PICTURE")%>" class="interfaceButton" title="<%=rs.getString("ICON_DESCRIPTION")%>"/>
			           	  	</a>
			                
			                <%
			                i++;
			            }
			            con.Close();
 			            %>                     
            		<a href=<%=session.getAttribute("USER_GROUP_CODE").toString().equals("5")? "doctor_manual.pdf" : "user_manual.pdf" %> target="_blank">
            			<img width='34px' height='34px' src="images/help_button_new.png" class="helpButton" title="Manual" onmouseover="src='images/help_button_new.png'" onmouseout="src='images/help_button_new.png'" />
            		</a>
            
        	</div>
       		
       		<iframe id="ifrm" name="ifrm" src="" width="0" height="10"></iframe>		
       		 	
			<div class="floatright">
				<div class="floatleft">
					<!-- <img src="template/img/img-profile.png" alt="Profile Pic" /> -->
				</div>
				<div class="floatleft marginleft10">
					<ul class="inline-ul floatleft" style="padding-right: 20px;">
						<li>
						
							<%
			        			DBConnection conn=new DBConnection();
			        			conn.connectToLocal();
								String group;
			        			ResultSet rsUser=conn.executeQuery("SELECT * FROM [USERS] AS USERS ,USER_GROUP AS USER_GROUP"
			        					  +"  WHERE USERS.USER_GROUP_CODE=USER_GROUP.USER_GROUP"
			        					  +"  AND LOGIN_NAME='"+session.getAttribute("USER_ID")+"'"
			        					  +"  AND USERS.HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE")+"'");
			        			//rsUser.next();
								if(rsUser.next()){
									out.print("Name : "+rsUser.getString("NAME"));
									group = rsUser.getString("ACTION_TYPE").toString();
								}
								else{
									out.print("Name : "+session.getAttribute("USER_ID"));
									group = "Doctor User";
								}
			        			//out.print("Name : "+rsUser.getString("NAME"));
			        		%>
						</li>
						<li>|</li>
						<li><a href="forms/input/users_main.jsp" target="mainFrame">Profile</a></li>
						<li>|</li>
						<li><a href="logout.jsp" onclick="return confirm('คุณ <%=session.getAttribute("USER_ID")%> ต้องการออกจากระบบหรือไม่ ?')"  target="_top">Logout</a></li>
					</ul>
					<br />
					<ul class="inline-ul floatleft">
						<li>
							<%
			           			out.print("  Group : "+group );
			        			conn.Close();
        					%>
						</li>
						<li>|</li>
						<li><a href="javascript:ChangeLang('EN');">EN</a></li>
						<li>|</li>
						<li><a href="javascript:ChangeLang('TH');">TH</a></li>
					</ul>
				 </div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
</div>
</body>
</html>
