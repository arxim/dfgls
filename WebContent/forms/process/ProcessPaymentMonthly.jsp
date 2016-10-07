<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.DialogBox"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.db.table.TrnDaily"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@include file="../../_global.jsp" %>
<%@page import="df.bean.obj.util.Utils"%>
<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_PROCESS_DEMO)) {
                response.sendRedirect("../message.jsp");
                return;
            }
				ProcessUtil proUtil = new ProcessUtil();
				LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
				labelMap.add("TITLE_MAIN", "Payment Summary", "Payment Summary");
				labelMap.add("DATE", "Date", "Date");
				labelMap.add("END_DATE", "End Date", "วันที่สิ้นสุด");
				labelMap.add("MM", "Month", "เดือน");
				labelMap.add("YYYY", "Year", "ปี");
				labelMap.add("COL_3", "Status", "สถานะ");
				labelMap.add("COL_5", "Payment Summary", "Payment Summary");
				request.setAttribute("labelMap", labelMap.getHashMap());
				
				String startDateStr = JDate.showDate(JDate.getDate());
	            String hospitalCode = session.getAttribute("HOSPITAL_CODE").toString();

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/epoch_styles.css" />
        <link rel="stylesheet" type="text/css" href="../../css/default/easyui.css" />
        <link rel="stylesheet" type="text/css" href="../../css/icon.css" />
        <script type="text/javascript" src="../../javascript/epoch_classes.js"></script>
        <script type="text/javascript" src="../../javascript/jquery-1.6.2.min.js"></script>
        <script type="text/javascript" src="../../javascript/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="../../javascript/jquery-ui-1.8.16.custom.min.js"></script>
        <script type="text/javascript">
    	var doctorData = new Object();
		var objData = new Object();
		var c = 0 ;
    	var result = false;
    	var dataComplete = 0;
    	var dataFail = 0;
    	var status = 0;
	        function getDoctorData(){
	        	var hospitalCode = $("#hospitalCode").val();
				$.ajax({
					type: "GET",
					url: "../../ProcessCalPaymentSrvl",
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
	        
	        /*
	        * jQuery.ajaxQueue - A queue for ajax requests
	        * 
	        * (c) 2011 Corey Frang
	        * Dual licensed under the MIT and GPL licenses.
	        *
	        * Requires jQuery 1.5+
	        */ 
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
 			function confirm(){
 		    	var img = document.getElementById('imgStatus');
 				var date = $("#DATE").val();
 		    	var hospitalCode = $("#hospitalCode").val();
				var i=0;
				$.messager.confirm('Confirm Process', 'Do you want to do this process?', function(r){
					if(r){
		                document.mainForm.RUN.disabled = true;
		                document.mainForm.STOP.disabled = false;
		                document.mainForm.CLOSE.disabled = true;
		    			img.src = '../../images/processing_icon.gif';
						document.getElementById("PROGRESS").innerHTML = "0 / " + objData.listDoctorCode.length;
						for(i=0;i<objData.listDoctorCode.length;i++){
						$.ajaxQueue({
								type: "GET",
								url: "../../ProcessCalPaymentSrvl",
								data: {mode:"paymentSummmary",maxSize:objData.listDoctorCode.length, hospitalCode:hospitalCode,date:date,count:i,doctorCode:objData.listDoctorCode[i].doctorCode},
								contentType: "application/json; charset=utf-8",
								dataType: "json",
								cache : false,
								success: function(data){	
									document.getElementById("PROGRESS").innerHTML = data.count+" / " + objData.listDoctorCode.length;
									if(data.lastData == "Y"){
										getResult(data.count);
									}
								},
								error: function(xhr,state,exception) {
									alert("exception => " + exception);
								}
						    }); 
						}
					}
				});
 			}
 			function getResult(dataComplete){
 		    	var img = document.getElementById('imgStatus');
 				result = false;
 				if(dataComplete > 1){
 					result = true;
 				}
				if(result){
					img.src = "../../images/succeed_icon.gif";
					alert("      complete!!!!   ");
	         	 }else{
	         		img.src = "../../images/failed_icon.gif";
					alert("      Insert Fail!!!!   ");
		        }
                document.mainForm.RUN.disabled = false;
                document.mainForm.STOP.disabled = true;
                document.mainForm.CLOSE.disabled = false;
 			}
	        window.onload = function () {
	            var dp_cal;
	            dp_cal  = new Epoch('epoch_popup','popup',document.getElementById('DATE'),document.getElementById('showDate1'));
	        };
	        
	        function chooseStartDate(){
				var dp_cal;
				dp_cal  = new Epoch('epoch_popup','popup',document.getElementById('DATE'),document.getElementById('showDate1'));
	        }
            function STOP_Click() {
                document.mainForm.RUN.disabled = false;
                document.mainForm.STOP.disabled = true;
                document.mainForm.CLOSE.disabled = false;
            }
        </script>
    </head>
    <body onload="getDoctorData()">
        <form id="mainForm" name="mainForm" method="post" action="ProcessPayMentMonthly.jsp">
			<input type="hidden" id="hospitalCode" name="hospitalCode" value="<%=hospitalCode%>"/>
			<center>
                <table width="800" border="0">
                    <tr><td align="left">
                            <b><font color='#003399'><%=Utils.getInfoPage("ProcessPaymentMonthly.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
                </table>
            </center>
            	<table class="form" border="1">
            		<tr class="label" style="background: #666666">
            			 <th colspan="2">${labelMap.TITLE_MAIN}</th>
            		</tr>
            		<tr align="center">
            		<td class="label" align="right" width="40%">${labelMap.DATE}</td>
                    <td class="input" valign="middle" width="60%" align="left">
                        <input type="text" value="<%=startDateStr%>" id="DATE" name="DATE" class="short"/>
                         <a href="#" class="position"><img onclick="chooseStartDate();"  id="showDate1" class="" src="../../images/calendar_button.png"/></a>    
                    </td>
                   </tr>
                	<tr>
                    	<th colspan="2" class="buttonBar">
                        	<input type="button" id="RUN" name="RUN" class="button" value="${labelMap.RUN}" onclick="confirm()"/>
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
                    <td class="sub_head"><%=labelMap.get("COL_5")%></td>
<%--                    <td class="sub_head"><%=labelMap.get("COL_2")%></td>    --%>
                    <td class="sub_head"><%=labelMap.get("COL_3")%></td>
                </tr>
                
                <tr>
                    <td class="" align="center"><%="Process Payment"%></td>
<%--                    <td class="row<%=i % 2%> alignCenter"><%=""%></td>  --%>
                    <td class="" align="center"><img id="imgStatus" src="../../images/waiting_icon.gif"/></td>
                </tr>
			<script type="text/javascript">
                    document.getElementById("PROGRESS").innerHTML = "0 / 0";
			</script>
            </table>
    		
    	</form>
    </body>
</html>