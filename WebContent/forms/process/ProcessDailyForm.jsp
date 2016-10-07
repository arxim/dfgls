<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>
<%@page import="java.sql.*" import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>
<%@include file="../../_global.jsp" %>
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
            labelMap.add("TITLE_MAIN", "Calculate Transaction", "คำนวณส่วนแบ่งขั้นต้น");
            labelMap.add("START_DATE", "Start Date", "วันที่เริ่มต้น");
            labelMap.add("END_DATE", "End Date", "วันที่สิ้นสุด");
            labelMap.add("COL_0", "No.", "ลำดับ");
            labelMap.add("COL_1", "Process", "ขบวนการ");
            labelMap.add("COL_2", "Month", "เดือน");
            labelMap.add("COL_3", "Status", "สถานะ");

            request.setAttribute("labelMap", labelMap.getHashMap());
            
            String startDateStr = JDate.showDate(JDate.getDate());
            String endDateStr = JDate.showDate(JDate.getDate());
            
            String hospitalCode = session.getAttribute("HOSPITAL_CODE").toString();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link type="text/css" href="../../css/themes/custom-theme/jquery.ui.all.css" rel="stylesheet" media="screen"/>
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/jquery-1.6.2.min.js"></script>
        <script type="text/javascript" src="../../javascript/jquery-ui-1.8.16.custom.min.js"></script>
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        
        <script type="text/javascript">
    	var invoiceData = new Object();
		var objData = new Object();
		var num = 0;
        (function($) {
	        var ajaxQueue = $({});
	        $.ajaxQueue = function( ajaxOpts ) {
	            var jqXHR,
	                dfd = $.Deferred(),
	                promise = dfd.promise();
	            ajaxQueue.queue( doRequest );
	            promise.abort = function( statusText ) {
	                if ( jqXHR ) {
	                    return jqXHR.abort( statusText );
	                }
	                var queue = ajaxQueue.queue(),
	                    index = $.inArray( doRequest, queue );

	                if ( index > -1 ) {
	                    queue.splice( index, 1 );
	                }
	                dfd.rejectWith( ajaxOpts.context || ajaxOpts,
	                    [ promise, statusText, "" ] );

	                return promise;
	            };
	            function doRequest( next ) {
	                jqXHR = $.ajax( ajaxOpts )
	                    .then( next, next )
	                    .done( dfd.resolve )
	                    .fail( dfd.reject );
	            }

	            return promise;
	        };
	        })(jQuery);
        function getInvoiceData(){
			$.ajax({
				type: "GET",
				url: "../../ProcessDailySrvl",
				data: {mode:"getInvoice", hospitalCode:$("#hospitalCode").val(),startDate:$("#START_DATE").val(),endDate:$("#END_DATE").val()},
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				async : false,
				cache : false,
				success: function(data){
					invoiceData = data;
				},
				error: function(xhr,state,exception) {
					alert("Data Not Found");
					invoiceData = "";
				}
			});
			objData = invoiceData;
        }
    	$(document).ready(function(){ 
			$("#RUN").click(function(){
				document.getElementById("RUN").disabled = "Y";
				getInvoiceData();
				//alert(objData.listInvoice.length);
				if(objData != null && objData != ""){
					var i=0;
		        	for(i=0;i<objData.listInvoice.length;i++){
						xhr = $.ajaxQueue({
								type: "GET",
								url: "../../ProcessDailySrvl",
								data: {mode:"calulate",startDate:$("#START_DATE").val(),endDate:$("#END_DATE").val(),inviceNo : objData.listInvoice[i].inviceNo,count:i,hospitalCode : $("#hospitalCode").val(),
									   maxSize : objData.listInvoice.length },
								contentType: "application/json; charset=utf-8",
								dataType: "json",
								cache : false,
								success: function(data){
									if(data.count==0){
							        	document.getElementById("imgStatus").src="../../images/processing_icon.gif";
										document.getElementById("PROGRESS").innerHTML = " 0/0";
									}
									document.getElementById("PROGRESS").innerHTML = data.count+" / " + objData.listInvoice.length;
									if(data.lastData == "Y"){
										document.getElementById("imgStatus").src="../../images/succeed_icon.gif";
										document.getElementById("RUN").disabled = "";
							        	alert("    Basic Allocation Complete    ");
									}
								},
								error: function(xhr,state,exception) {
									alert("exception => " + exception);
								}
						    }); 
						num++;
		        	}
				}else{
					alert("Data Not Found");
				}
			});
		});
        </script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="ProcessDailyForm.jsp">
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                    	<b><font color='#003399'><%=Utils.getInfoPage("ProcessDailyForm.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
                </table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>
			<tr>
                    <td class="label">
                        <label for="START_DATE">${labelMap.START_DATE}</label>
                    </td>
                    <td class="input">
                        <input type="text" value="<%=startDateStr%>" id="START_DATE" name="START_DATE" class="short" value="<%=request.getParameter("START_DATE") == null ? "" : request.getParameter("START_DATE")%>" />
                        <input name="image1" type="image" class="image_button" onclick="displayDatePicker('START_DATE'); return false;" src="../../images/calendar_button.png" alt="" />
                    </td>
                    <td class="label">
                        <label for="END_DATE">${labelMap.END_DATE}</label>
                    </td>
                    <td class="input">
                        <input type="text" value="<%=endDateStr%>" id="END_DATE" name="END_DATE" class="short" value="<%=request.getParameter("END_DATE") == null ? "" : request.getParameter("END_DATE")%>" />
                        <input name="image2" type="image" class="image_button" onclick="displayDatePicker('END_DATE'); return false;" src="../../images/calendar_button.png" alt="" />
                    </td>
                </tr>
                
                <tr>
                    <th colspan="4" class="buttonBar">                        
<%--                     <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="SELECT_Click()" />    --%>
                        <input type="button" id="RUN" name="RUN" class="button" value="${labelMap.RUN}" />
                        <input type="button" id="STOP" name="STOP" class="button" value="Stop" onclick="STOP_Click()" disabled="disabled" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
						</th>
                </tr>
            </table>
            <hr />
            <table class="data" id="dataTable" name="dataTable">
                <tr>
                    <th colspan="2" class="alignLeft">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
                        <div style="float: right;" id="PROGRESS" name="PROGRESS"></div>
                    </th>
                </tr>
                <tr>
                    <td class="sub_head"><%=labelMap.get("COL_1")%></td>
<%--                    <td class="sub_head"><%=labelMap.get("COL_2")%></td>    --%>
                    <td class="sub_head"><%=labelMap.get("COL_3")%></td>
                </tr>
                
<%
            int i = 1;
%>
                <tr>
                    <td class="row<%=i % 2%> alignCenter"><%="Calculation Process"%></td>
<%--                    <td class="row<%=i % 2%> alignCenter"><%=""%></td>  --%>
                    <td class="row<%=i % 2%> alignCenter"><img id="imgStatus" src="../../images/waiting_icon.gif"/></td>
                </tr>

				<%
				 i ++;
				%>
			<script type="text/javascript">
            	numRow = <%=i - 1%>;
            	document.getElementById("PROGRESS").innerHTML = "0 / " + 1;
			</script>
            </table>
			<%
            if (i > 1) {
                out.print("<script type=\"text/javascript\">document.mainForm.RUN.disabled = false;</script>");
            }

			%>
			<input type="hidden" id="hospitalCode" name="hospitalCode" value="<%=hospitalCode%>"/>
        </form>
    </body>
</html>