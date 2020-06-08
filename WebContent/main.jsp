<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="forms/error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@ include file="_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_ROOT)) {
                response.sendRedirect("loginform.jsp");
                return;
            }
            
            String lang = session.getAttribute("LANG_CODE").toString();
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            
            DBConn conn = new DBConn();
            conn.setStatement();
            //String qHos = "SELECT DESCRIPTION_"+ labelMap.getFieldLangSuffix() +",  STP_MENU.LINK_PAGE FROM HOSPITAL LEFT OUTER JOIN STP_MENU ON HOSPITAL.CODE = STP_MENU.HOSPITAL_CODE "+
            //"WHERE HOSPITAL.CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND STP_MENU.MENU_ENG = 'Doctorfee Report'";

            String qHos = "SELECT TOP 1 DESCRIPTION_"+ labelMap.getFieldLangSuffix() +",  STP_MENU.LINK_PAGE FROM HOSPITAL "
            		+ "LEFT OUTER JOIN STP_MENU ON HOSPITAL.CODE = STP_MENU.HOSPITAL_CODE "
            		+ "LEFT OUTER JOIN STP_MENU_MATCH SM ON STP_MENU.HOSPITAL_CODE = SM.HOSPITAL_CODE AND STP_MENU.CODE = SM.MENU_CODE "
                    + "WHERE HOSPITAL.CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND SM.USER_GROUP_CODE = '5' AND LINK_PAGE <> '' ";
            
            String[][] arrHos = conn.query(qHos);
            //System.out.println(arrHos[0][1]);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <title>
		<%=arrHos[0][0]+" :: Doctor Fee Version 4.5.5 ::"%>
	</title>
    <link rel="stylesheet" type="text/css" href="template/css/reset.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="template/css/text.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="template/css/grid.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="template/css/layout.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="template/css/nav.css" media="screen" />
    <!--[if IE 6]><link rel="stylesheet" type="text/css" href="css/ie6.css" media="screen" /><![endif]-->
    <!--[if IE 7]><link rel="stylesheet" type="text/css" href="css/ie.css" media="screen" /><![endif]-->
    <!-- BEGIN: load jquery -->
    <script type="text/javascript" src="template/js/jquery-1.6.4.min.js" ></script>
    <script type="text/javascript" src="template/js/jquery-ui/jquery.ui.core.min.js"></script>
    <script type="text/javascript" src="template/js/jquery-ui/jquery.ui.widget.min.js"></script>
    <script type="text/javascript" src="template/js/jquery-ui/jquery.ui.accordion.min.js" ></script>
    <script type="text/javascript" src="template/js/setup.js" ></script>
    
    <script type="text/javascript">

	        $(document).ready(function () {
	          //  setupPrettyPhoto();
	            setupLeftMenu();
				setSidebarHeight();
	        });
    
            var origCols = true;
            var xw = screen.availWidth;
            var xwMenuFrame = 250;
            var xwSearchFrame = 0;
            var xwMainFrame = '*';
            var searchOpen = "";
            
            var milisec=0;
            var seconds=0;

            function toggleFrame() {
                if (origCols) {
                    hideFrame();
                    origCols = false;
                    topFrame.changeLableMenu(false);
                } else {
                    showFrame();
                    origCols = true;
                    topFrame.changeLableMenu(true);
                }
            }
            
            function hideFrame() {
                var frameset = document.getElementById("controlFrame");
                xwMenuFrame = 0;
                if(xwSearchFrame>0) xwSearchFrame = xwSearchFrame + 250;
                frameset.cols = ""+xwMenuFrame+","+xwSearchFrame+", *";
                if(xwSearchFrame>0) xwSearchFrame = xwSearchFrame - 250;
            }

            function showFrame() {
                var frameset = document.getElementById("controlFrame");
                xwMenuFrame = 250;
                frameset.cols = ""+xwMenuFrame+","+xwSearchFrame+", *";
            }
            
            function OpenSearch(dt){
                var frameset = document.getElementById("controlFrame");
                var urlFrame = document.getElementById("FrameSearch");
                dt = dt.replace("../","./forms/") ;
                xwSearchFrame = xw-xwMenuFrame;
                if(dt==searchOpen){
                    frameset.cols = ""+xwMenuFrame+","+xwSearchFrame+",*";
                }else{
                    searchOpen = dt;
                    urlFrame.src = dt;
                    frameset.cols = ""+xwMenuFrame+","+xwSearchFrame+",*";
                }
            }
            
            function CloseSearch(){
                var frameset = document.getElementById("controlFrame");
                xwSearchFrame = 0;
                frameset.cols = ""+xwMenuFrame+","+xwSearchFrame+", *";
            }

            //for load first page
        	function onLoadPage(id){
        		var f = document.getElementById("mainFrame");
        		if(id=='5'){
            		//for gls
        			//f.src = "./forms/report/df_report.jsp";
            		//for non gls
            		//f.src = "./forms/report/df_report_doctor.jsp";
            		f.src = "./<%=arrHos[0][1]%>";
        		}else if(id== '3'){
        			f.src = "./AppMain.jsp"
        		}
        	}
            
        </script>        
        
        <style type="text/css">
        
			body{ 
				background: none repeat scroll 0 0 #2E5E79;
				color: #333;
				font-size: 11px;
				padding: 0 0 0px;
			}
			
		</style>
   
</head>

<frameset rows="60,* , 30" cols="*" frameborder="no" border="0" framespacing="0">

        <frame src="header.jsp" name="topFrame" scrolling="no" noresize="noresize" id="topFrame" title="topFrame" />

        <frameset rows="*" cols="250,*,*" id="controlFrame" name="controlFrame" framespacing="0" frameborder="no" border="0">
            
            <frame src="menu.jsp" id="leftFrame" name="leftFrame" noresize="noresize" title="leftFrame"  border="0" />
            
            <frame src="FrameSearch.jsp" id="FrameSearch" name="FrameSearch" noresize="noresize" title="FrameSearch" />
            
            <frame src="./forms/process/ProcessFlow.jsp" id="mainFrame" name="mainFrame" title="mainFrame"/>
            
            <noframes>
                <body></body>
            </noframes>
            
        </frameset>
        
		<frame src="footer.jsp" name="topFrame" scrolling="no" noresize="noresize" id="topFrame" title="topFrame" />

        <noframes>
            <body></body>
        </noframes>
    </frameset>
    
</html>