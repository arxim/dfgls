<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.util.*"%>
<%@page import="df.jsp.LabelMap"%>
<%@include file="_global.jsp"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.obj.util.Variables"%>
<%@page import="org.apache.log4j.Logger"%>
<%
	//
	// Verify permission
	//

	if (!Guard.checkPermission(session, Guard.PAGE_ROOT)) {
		response.sendRedirect("loginform.jsp");
		return;
	}

	String label = "";
	if (session.getAttribute("LANG_CODE") == null) {
		label = "ENG";
	} else {
		label = ("T".equalsIgnoreCase(session.getAttribute("LANG_CODE").toString())) ? "THAI" : "ENG";
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link rel="stylesheet" type="text/css" href="css/share.css" media="all" />
<link rel="stylesheet" type="text/css" href="css/header.css" media="all" />
<link rel="stylesheet" type="text/css" href="css/menu.css" media="all" />

<script type="text/javascript" src="javascript/menu/ddaccordion.js"></script>
<script type="text/javascript"	src="javascript/menu/jquery-1.2.6.pack.js"></script>

<script type="text/javascript">
            ddaccordion.init({
	                headerclass: "expandable", //Shared CSS class name of headers group that are expandable
	                contentclass: "categoryitems", //Shared CSS class name of contents group
	                revealtype: "click", //Reveal content when user clicks or onmouseover the header? Valid value: "click" or "mouseover"
	                mouseoverdelay: 200, //if revealtype="mouseover", set delay in milliseconds before header expands onMouseover
	                collapseprev: true, //Collapse previous content (so only one open at any time)? true/false 
	                defaultexpanded: [], //index of content(s) open by default [index1, index2, etc]. [] denotes no content
	                onemustopen: false, //Specify whether at least one header should be open always (so never all headers closed)
	                animatedefault: false, //Should contents open by default be animated into view?
	                persiststate: true, //persist state of opened contents within browser session?
	                toggleclass: ["", "openheader"], //Two CSS classes to be applied to the header when it's collapsed and expanded, respectively ["class1", "class2"]
	                togglehtml: ["prefix", "", ""], //Additional HTML added to the header when it's collapsed and expanded, respectively  ["position", "html1", "html2"] (see docs)
	                animatespeed: "fast", //speed of animation: integer in milliseconds (ie: 200), or keywords "fast", "normal", or "slow"
	                oninit:function(headers, expandedindices){ //custom code to run when headers have initalized
                        //do nothing
                	},
               		
                	onopenclose:function(header, index, state, isuseractivated){ //custom code to run whenever a header is opened or closed
                        //do nothing
                	}
                	
            }); // End function
            
            /*
            * Load Page Defualt
            */
            function onLoadInitPage(){
            	var page  =  '<%=session.getAttribute("USER_GROUP_CODE").toString()%>';
            	parent.onLoadPage(page);
				//parent.mainFrame.location.reload();
		    }
            
        </script>
       
       <style type="text/css">
          body   { 
            background: none repeat scroll 0 0 #2E5E79;
			color: #333;
			padding: 0 0 0px;
			font-size:12px;
          }
          .grid_2 {  
          	 padding-left: 10px;
          	 margin-left: 13px;
          	 margin-top: 10px;
          }
       </style>
       
</head>
<body onload="onLoadInitPage();">
	<div class="grid_2" >
		<div class="box sidemenu">
			<div class="block" id="section-menu">
				<div class="arrowlistmenu">
					<%
					Logger logger = Logger.getLogger("menu.jsp");
						String menusql = "SELECT a.* FROM STP_MENU as a WHERE CODE IN (SELECT MENU_CODE FROM STP_MENU_MATCH AS b WHERE b.MENU_CODE = a.CODE AND b.USER_GROUP_CODE='"
								+ session.getAttribute("USER_GROUP_CODE").toString()
								+ "' AND b.HOSPITAL_CODE = '"
								+ session.getAttribute("HOSPITAL_CODE").toString()
								+ "') "
								+ "AND a.HOSPITAL_CODE = '"
								+ session.getAttribute("HOSPITAL_CODE").toString()
								+ "' "
								+ " ORDER BY convert(int,CODE);";
						DBConnection con = new DBConnection();
						con.connectToLocal();
						logger.info(menusql);
						ResultSet rs = con.executeQuery(menusql);
						int i = 0;
						while (rs.next()) {
							if ("0".equalsIgnoreCase(rs.getString("PARENT_CODE"))) {
								if (i > 0)
									out.println("</ul>");
									out.println("<h3 class=\"menuheader expandable\">"+ rs.getString("MENU_" + label) + "</h3>");
									out.println("<ul class=\"categoryitems\">");
							} else {
								out.println("<li><a href=\""+ rs.getString("LINK_PAGE")	+ "\" target=\"mainFrame\">"+ rs.getString("MENU_" + label) + "</a></li>");
							}
							i++;
						}
						con.Close();
					%>
				</div>
			</div>
		</div>
	</div>
</body>
</html>