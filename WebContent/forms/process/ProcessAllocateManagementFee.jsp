<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>
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
                            
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Allocate Management Fee", "คำนวนส่วนแบ่งพิเศษ");
            labelMap.add("COL_0", "No.", "ลำดับ");
            labelMap.add("COL_1", "Process", "การคำนวณ");
            labelMap.add("COL_2", "Status", "สถานะ");

            request.setAttribute("labelMap", labelMap.getHashMap());
            
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
    </head>
    <body>
    
            <table class="data">
                <tr>
                    <th colspan="2" style="text-align: left;">${labelMap.TITLE_MAIN}</th>
                </tr>
                  <tr>
                	<td class="lable" style="text-align:right;">
                			Allocate Percent 
                	</td>
                	<td class="input">
                		<input type="text" name="txtPercent" id="txtPercent" class="short" value="1"/>
                			%
                	</td>
                </tr>
                <tr>
                	<td style="text-align:right;">
                		Payment 
                	</td>
                	<td>
                		<select id="term" name="term" class="short">
                   	 		 <option value="1">Half Month</option>
                   	 		 <option value="2">Month End</option>
                   	 	</select>
                	</td>
                </tr>
                <tr>
                    <th colspan="2" class="buttonBar">  
                    		<input type="button" id="RUN" name="RUN" class="button" value="${labelMap.RUN}" />
	                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />                    
                    </th>
                </tr>
            </table>
            
            <hr />
            
            <table class="data" id="dataTable" name="dataTable">
                <tr>
                    <th colspan="3" class="alignLeft">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
                        <div style="float: right;" id="PROGRESS" name="PROGRESS"></div>
                    </th>
                </tr>
                <tr>
                    <td class="sub_head"><%=labelMap.get("COL_0")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_1")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_2")%></td>
                </tr>                
                <tr>
                	<td style="text-align: center;">
                		1
                	</td>
                	<td>
                		 Process Allocate Management Fee
                	</td>
                	<td style="text-align: center;">
                		<span id="icon-wait"></span>
                		<span id="icon-success"></span>
                	</td>
                </tr>           
          	</table>
          	
          	 <script type="text/javascript" src="../../javascript/util.js"></script>
	         <script type="text/javascript" src="../../javascript/ajax.js"></script>
	         <script type="text/javascript" src="../../javascript/calendar.js"></script>
	         <script type="text/javascript" src="../../javascript/jquery-1.6.2.min.js"></script>
             <script type="text/javascript" src="../../javascript/calendar.js"></script>
	         <script type="text/javascript" src="../../javascript/jquery-1.6.2.min.js"></script>
	         <script type="text/javascript">

		        	 /**
		             * Start Jquery
		        	 */
		        	 
		        	 // Get Hospital Code Current
		        	 var  hospitalCode = "<%=session.getAttribute("HOSPITAL_CODE")%>";
		
		        	 $(function() { 
		        
		        	 // set default data
		        	 $("#icon-wait").html("<img  src='../../images/waiting_icon.gif' />");
		        	
		        	 $(document).ajaxSend(function( event, request, settings ) {
		             	 $("#icon-wait").html("<img  src='../../images/processing_icon.gif' />");
		             }).ajaxError(function( event,request, settings ){ 
		            	 $("#icon-wait").html("<img  src='../../images/failed_icon.gif' />");
		             });
		        	 
		        	 $("#RUN").click(function() { 
		        		 
				             $.ajax({
		                         url: '../../ProcessAllocateManagementFeeSrvl',
		                         type: 'POST',
		                         data: {
		                         	 txtPercenter : $("#txtPercent").val() , 
		                         	 hospitalCode : hospitalCode ,
		                         	 payment : $("select[name=term]").val()
		                         }, 
		                         success: function(data) {
		                        	if(data.status == "SUCCESS") {
		                        		$("#icon-wait").html("<img  src='../../images/pass_icon.gif' />");
		              		        } else {
		                        		$("#icon-wait").html("<img  src='../../images/failed_icon.gif' />");
		                        	}
		                       	 } , 
		                         error: function (){ 
		                        	 $(this).ajaxSuccess(function(){
		          		            	 $("#icon-wait").html("<img  src='../../images/failed_icon.gif' />");
		          		             });
		                         }
		                     });
		        		 });
		        	 });
        	 
        	 </script>
        
    </body>
</html>
