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
<%@page import="df.bean.obj.util.Utils "%>
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
            labelMap.add("TITLE_MAIN", "Process All Monthly", "Process All Monthly");
            labelMap.add("COL_0", "Process", "Process");
            labelMap.add("COL_1", "Status", "Status");
            labelMap.add("DATE", "Date", "Date");;
            labelMap.add("PROGRESS", "Progress", "Progress");
            
            request.setAttribute("labelMap", labelMap.getHashMap());
            String hospitalCode = session.getAttribute("HOSPITAL_CODE").toString();
            String user = session.getAttribute("USER_ID").toString();
            String startDateStr = JDate.showDate(JDate.getDate());

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />        
        <link rel="stylesheet" type="text/css" href="../../css/default/easyui.css" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/calendar.js"></script>        
        <script type="text/javascript" src="../../javascript/jquery-1.6.2.min.js"></script>
        <script type="text/javascript" src="../../javascript/jquery.easyui.min.js"></script>
        <script type="text/javascript">
    	var doctorData = new Object();
		var objData = new Object();
		var num = 0;
    	var num2 = 0;
    	var result = false;
    	var xhr;
		
        function serResult(resultBoolean){
        	result = resultBoolean;
        }
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
	        function getDoctorData(){
	        	var hospitalCode = $("#hospitalCode").val();
				$.ajax({
					type: "GET",
					url: "../../ProcessAllMonthlySrvl",
					data: {mode:"getDoctor", hospitalCode:hospitalCode},
					contentType: "application/json; charset=utf-8",
					dataType: "json",
					async : false,
					cache : false,
					success: function(data){
							doctorData = data;
					},
					error: function(xhr,state,exception) {
						doctorData="";
					}
				});
				objData = doctorData;
	        }
        $(document).ready(function() {
        	document.getElementById("progress_discharge").innerHTML = "0/1";     
        	document.getElementById("progress_monthly").innerHTML = "0/1";
        // 	document.getElementById("progress_bank").innerHTML = "0/1";     
        	
			function dischargeCalulate(){
				document.getElementById("img_discharge_monthly").src="../../images/processing_icon.gif";
					xhr = $.ajaxQueue({
							type: "GET",
							url: "../../ProcessAllMonthlySrvl",
							data: {mode:"processDischarge",date:$("#date").val(),term:$("#term").val(),hospitalCode : $("#hospitalCode").val()},
							contentType: "application/json; charset=utf-8",
							dataType: "json",
							cache : false,
							success: function(data){
 								document.getElementById("progress_discharge").innerHTML = data.count+" / 1";

									if(data.count>0){
										document.getElementById("img_discharge_monthly").src="../../images/succeed_icon.gif";
										if(data.count == 1){
											monthlyCalulate();
										}else{
											document.getElementById("img_discharge_monthly").src="../../images/failed_icon.gif";
											document.getElementById("img_monthly_calculate").src="../../images/failed_icon.gif";
											//document.getElementById("img_bank_payment").src="../../images/failed_icon.gif";
											document.getElementById("RUN").disabled = "";
											document.getElementById("STOP").disabled = "";
								        	alert("    Fail!!!  ");
										}
									}else{
										
										document.getElementById("img_discharge_monthly").src="../../images/failed_icon.gif";
										document.getElementById("img_monthly_calculate").src="../../images/failed_icon.gif";
										//document.getElementById("img_bank_payment").src="../../images/failed_icon.gif";
										document.getElementById("RUN").disabled = "";
										document.getElementById("STOP").disabled = "";
							        	alert("    Fail!!!  ");
									}

							
							},
							error: function(xhr,state,exception) {
								alert("exception DischargeCalulate => " + exception);
							}
					    }); 
	        	
			}
			function monthlyCalulate(){
				document.getElementById("img_monthly_calculate").src="../../images/processing_icon.gif";
					xhr = $.ajaxQueue({
							type: "GET",
							url: "../../ProcessAllMonthlySrvl",
							data: {mode:"processMonthly",date:$("#date").val(),term:$("#term").val(),hospitalCode : $("#hospitalCode").val(), user:'<%=user%>'},
							contentType: "application/json; charset=utf-8",
							dataType: "json",
							cache : false,
							success: function(data){

								document.getElementById("progress_monthly").innerHTML = data.count+" / 1";
							
									if(data.count>0){
										document.getElementById("img_monthly_calculate").src="../../images/succeed_icon.gif";
										if(data.count == 1){
								        	alert("    Monthly Process Complete    ");
										}else{
											document.getElementById("img_monthly_calculate").src="../../images/failed_icon.gif";
											//document.getElementById("img_bank_payment").src="../../images/failed_icon.gif";
											document.getElementById("RUN").disabled = "";
											document.getElementById("STOP").disabled = "";
								        	alert("    Fail!!!  ");
										}
									}else{
										document.getElementById("img_monthly_calculate").src="../../images/failed_icon.gif";
										//document.getElementById("img_bank_payment").src="../../images/failed_icon.gif";
										document.getElementById("RUN").disabled = "";
										document.getElementById("STOP").disabled = "";
							        	alert("    Fail!!!  ");
									}
							},
							error: function(xhr,state,exception) {
								alert("exception monthlyCalulate => " + exception);
							}
					    }); 
	        	
			}
			function bankPayment(){
				xhr = $.ajaxQueue({
					type: "GET",
					url: "../../ProcessAllMonthlySrvl",
					data: {mode:"bankPayment",date:$("#date").val(),term:$("#term").val(),hospitalCode : $("#hospitalCode").val(),
					recNo : "2",user : $("#user").val(),password:""},
					contentType: "application/json; charset=utf-8",
					dataType: "json",
					async : false,
					cache : false,
					success: function(data){
			        	//document.getElementById("img_bank_payment").src="../../images/processing_icon.gif";	
						if(data.count>0){
							//document.getElementById("progress_bank").innerHTML = data.count+" / 1";
				        	//document.getElementById("img_bank_payment").src="../../images/succeed_icon.gif";
							document.getElementById("RUN").disabled = "";
							document.getElementById("STOP").disabled = "";
				        	alert("    Monthly Process Complete    ");
						}else{
							//document.getElementById("progress_bank").innerHTML = data.count+" / 1";
				        	//document.getElementById("img_bank_payment").src="../../images/failed_icon.gif";
							document.getElementById("RUN").disabled = "";
							document.getElementById("STOP").disabled = "";
				        	alert("    Fail!!!  ");
						}
					},
					error: function(xhr,state,exception) {
						alert("exception bankPayment => " + exception);
					}
			    });
			}
	        $("#RUN").click(function () {
	        	document.getElementById("progress_discharge").innerHTML = "0/1";
	        	document.getElementById("progress_monthly").innerHTML = "0/1";
	        	//document.getElementById("progress_bank").innerHTML = "0/1";
				document.getElementById("img_discharge_monthly").src="../../images/waiting_icon.gif";
				document.getElementById("img_monthly_calculate").src="../../images/waiting_icon.gif";
				//document.getElementById("img_bank_payment").src="../../images/waiting_icon.gif";
				document.getElementById("RUN").disabled = "disabled";
				document.getElementById("STOP").disabled = "disabled";
				dischargeCalulate();
	          });
        });
        </script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="ProcessAllMonthly.jsp">
        	<input type="hidden" name="hospitalCode" id="hospitalCode" value="<%=hospitalCode%>"/>
        	<input type="hidden" name="user" id="user" value="<%=user%>"/>
        	<center>
                <table width="800" border="0">
                    <tr><td align="left">
                        <b><font color='#003399'><%=Utils.getInfoPage("ProcessAllMonthly.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>  
                <tr align="center">
                	<td class="label" align="right" width="25%">Payment Term</td>
                    <td class="input" valign="middle" width="25%" align="left">
                   	 	<select class="short" name="term" id="term">
                   	 	 	<option value="2">Month End</option>
                   	 		<option value="1">Half Month</option>
                   	 		
                   	 	</select>
                    </td>
                    
            		<td class="label" align="right" width="25%">Payment Date</td>
                    <td class="input" valign="middle" width="25%" align="left">
                    	<input type="text" value="<%=startDateStr%>" id="date" name="date" class="short" value="<%=request.getParameter("START_DATE") == null ? "" : request.getParameter("START_DATE")%>" />
                        <input name="image2" type="image" class="image_button" onclick="displayDatePicker('date'); return false;" src="../../images/calendar_button.png" alt="" />
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="RUN" name="RUN" class="button" value="${labelMap.RUN}"/>
                        <input type="button" id="STOP" name="STOP" class="button" value="Stop"/>
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />                    </th>
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
                    <td class="sub_head"><%=labelMap.get("PROGRESS")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_1")%></td>
                </tr>
                <tr>
                    <td class="row0 alignCenter">Discharge Calculate</td>
                    <td class="row0 alignCenter"><div id="progress_discharge" name="progress_discharge"></div></td>
                    <td class="row0 alignCenter"><img src="../../images/waiting_icon.gif" alt="" name="img_discharge_monthly" id="img_discharge_monthly"/></td>
                </tr>               
                <tr>
                    <td class="row0 alignCenter">Monthly Calculate</td>
                    <td class="row0 alignCenter"><div id="progress_monthly" name="progress_monthly"></div></td>
                    <td class="row0 alignCenter"><img src="../../images/waiting_icon.gif" alt="" name="img_monthly_calculate" id="img_monthly_calculate"/></td>
                </tr>
            </table>
        </form>
    </body>
</html>