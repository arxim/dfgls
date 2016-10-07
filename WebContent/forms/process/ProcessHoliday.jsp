<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>
<%@page import="java.sql.*"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.obj.doctor.DoctorList"%>
<%@page import="df.bean.obj.Item.DrMethodAllocation"%>
<%@page import="df.bean.db.table.TrnDaily"%>
<%@page import="df.bean.obj.doctor.CareProvider"%>
<%@page import="df.bean.interfacefile.InterfaceData"%>
<%@ include file="../../_global.jsp" %>
<%@page import="df.bean.obj.util.Utils "%>
<%@page import="df.bean.process.ProcessUtil"%>
<%
      // Verify permission
      if (!Guard.checkPermission(session, Guard.PAGE_PROCESS_DEMO)) {
          response.sendRedirect("../message.jsp");
          return;
      }

      // Initial LabelMap
      if (session.getAttribute("LANG_CODE") == null) { session.setAttribute("LANG_CODE", LabelMap.LANG_EN);}
      
      LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
      labelMap.add("TITLE_MAIN", "Calculate Holiday Process", "คำนวณส่วนแบ่งเบื้องต้น");
      labelMap.add("COL_0", "Process", "Process");
      labelMap.add("COL_1", "Status", "Status");
      labelMap.add("YYYY", "Year", "ปี");
      labelMap.add("MM", "Month", "เดือน");
      labelMap.add("PROGRESS", "Progress", "Progress");
      request.setAttribute("labelMap", labelMap.getHashMap());
      String startDateStr = JDate.showDate(JDate.getDate());
      String endDateStr = JDate.showDate(JDate.getDate());
      String hospitalCode = session.getAttribute("HOSPITAL_CODE").toString();
      ProcessUtil proUtil = new ProcessUtil();
      DBConnection db = new DBConnection();
      db.connectToLocal();
      Batch batch = new Batch(session.getAttribute("HOSPITAL_CODE").toString(),db);
      db.Close();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
    
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link type="text/css" href="../../css/themes/custom-theme/jquery.ui.all.css" rel="stylesheet" media="screen"/>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/jquery-1.6.2.min.js"></script>
        <script type="text/javascript" src="../../javascript/jquery-ui-1.8.16.custom.min.js"></script>
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script src="../../javascript/DOMPaser.js" type="text/javascript"></script>
      
	</head>
    <body>
	<form id="mainForm" name="mainForm" method="post" action="ProcessHoliday.jsp">
		        <center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("ProcessHoliday.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>

	<table class="form">	
 		<tr><th colspan="4">${labelMap.TITLE_MAIN}</th></tr>
        <tr>
        	<td class="label"><label>${labelMap.MM}</label></td>
            <td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "MM", batch.getMm())%></td>
            <td class="label"><label>${labelMap.YYYY}</label></td>
            <td class="input"><%=proUtil.selectYY("YYYY", batch.getYyyy())%></td>
        </tr>                
        <tr>
        	<th colspan="4" class="buttonBar">
<%--         	    <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="window.location = 'ProcessHoliday.jsp?SELECT_DATA=true&state=notrun&YYYY=' + document.mainForm.YYYY.value + '&MM=' +  document.mainForm.MM.value; return false;" /> --%>
	            <input type="button" id="RUN" name="RUN" class="button" value="${labelMap.RUN}" onclick="RUN_Click()" />
	            <input type="button" id="STOP" name="STOP" class="button" value="Stop" onclick="STOP_Click()" disabled="disabled" />
	            <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
			</th>
        </tr>
	</table>
    <hr></hr>
    <table class="data" id="dataTable">
		<tr>
             <th colspan="6" class="alignLeft">
                <div style="float: left;">${labelMap.TITLE_MAIN}</div>
                <div style="float: right;" id="PROGRESS" name="PROGRESS"></div>
            </th>
        </tr>
          <tr>
                    <td class="sub_head"><%=labelMap.get("COL_0")%></td>
                    <td class="sub_head"><%=labelMap.get("PROGRESS")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_1")%></td>
          </tr>
            <tr>
                    <td class="row0 alignCenter">Holiday Calculate</td>
                    <td class="row0 alignCenter"><div id="progress_monthly" name="progress_monthly"></div></td>
                    <td class="row0 alignCenter"><div id="img_holiday_calculate" name="img_holiday_calculate"><img src="../../images/waiting_icon.gif" alt="" /></div></td>
                </tr>
<!--         <tr style="display: none;"> -->
<%--             <td class="sub_head"><%=labelMap.get("COL_0")%></td> --%>
<%--             <td class="sub_head"><%=labelMap.get("COL_1")%></td> --%>
<%--             <td class="sub_head"><%=labelMap.get("COL_2")%></td> --%>
<%--             <td class="sub_head"><%=labelMap.get("COL_3")%></td> --%>
<%--             <td class="sub_head"><%=labelMap.get("COL_4")%></td> --%>
<%--             <td class="sub_head"><%=labelMap.get("COL_5")%></td> --%>
<!--         </tr> -->

	</table>

	</form>
		  <script type="text/javascript">
    
	        $(document).ready(function(){ 
				$( "#START_DATE" ).datepicker({
					changeMonth: true,
					changeYear: true,
					buttonImageOnly: true,
					dateFormat: 'dd/mm/yy'
				}); 
				$( "#END_DATE" ).datepicker({
					changeMonth: true,
					changeYear: true,
					buttonImageOnly: true,
					dateFormat: 'dd/mm/yy'
				});				
			});
	        function GetXmlHttpObject()
	        {
	            if (window.XMLHttpRequest)
	            {
	               return new XMLHttpRequest();
	            }
	            if (window.ActiveXObject)
	            {
	              return new ActiveXObject("Microsoft.XMLHTTP");
	            }
	         return null;
	        }
			var xmlhttp;
			function AJAX_Send_Request(){
			  	 document.getElementById("img_holiday_calculate").innerHTML = '<img src="../../images/processing_icon.gif" alt="" />';
				   
					var target = "../../HolidaySrvl?"
                        + "HOSPITAL_CODE=<%=session.getAttribute("HOSPITAL_CODE").toString()%>"
                        + "&YYYY=" + document.mainForm.YYYY.value
                        + "&MM=" + document.mainForm.MM.value;
	                xmlhttp=GetXmlHttpObject();
					//alert(target);
	                if (xmlhttp==null){
						alert ("Your browser does not support Ajax HTTP");
	                 	return;
	                }
               	    xmlhttp.onreadystatechange=getOutput;
               	    xmlhttp.open("POST",target,true);
               	    xmlhttp.send(null);	            
			}
			function LTrim(str){
				if (str==null){return null;}
					for(var i=0;str.charAt(i)==" ";i++);
					return str.substring(i,str.length);
				}
			var numcount=1;
			function getOutput(){
				if (xmlhttp.readyState==4){
   					var xmlDoc = xmlhttp.responseText;
   					
                    if (xmlDoc== null) {
                        alert("Exception in update process");
                        return;
                    }
    				    if(xmlDoc== "1"){
    				    	document.getElementById("progress_monthly").innerHTML = "1/1";
    				    	 document.getElementById("img_holiday_calculate").innerHTML = '<img src="../../images/pass_icon.gif" alt="" />';
    				    }else{
    				    	 document.getElementById("img_holiday_calculate").innerHTML = '<img src="../../images/failed_icon.gif" alt="" />';
    				    	
    				    }
        				   
        				
                    }else{}
    				
			}
          
            function RUN_Click() {
                if(confirm('Confirm Process?')){
                	AJAX_Send_Request();
                }
            }
        
	        document.getElementById("progress_monthly").innerHTML = "0/1";
            var stopclick=false;
            function STOP_Click(){
            	document.mainForm.RUN.disabled = true;
            	document.mainForm.STOP.disabled = true;
            	document.mainForm.CLOSE.disabled = false;
            	stopclick=true;
            }
		</script>
	</body>
</html>