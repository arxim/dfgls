<%-- 
    Document   : ProcessImportBill
    Created on : 
    Author     : T.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_PROCESS_PREPARE_GUARANTEE)) {
                response.sendRedirect("../message.jsp");
                return;
            }

            //
            // Initial LabelMap
            //

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }           
            ProcessUtil proUtil = new ProcessUtil();
            DBConnection c = new DBConnection();
            c.connectToLocal();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
            c.Close();
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Tax 40(2) Process", "คำนวณรายการภาษี");
            labelMap.add("START_DATE", "Start Date", "วันที่เริ่มต้น");
            labelMap.add("END_DATE", "End Date", "วันที่สิ้นสุด");
            labelMap.add("MM", "Month", "เดือน");
            labelMap.add("YYYY", "Year", "ปี");
            labelMap.add("COL_0", "DR Code", "ลำดับ");
            labelMap.add("COL_1", "Process", "ขบวนการ");
            labelMap.add("COL_2", "Month", "เดือน");
            labelMap.add("COL_3", "Status", "สถานะ");
            labelMap.add("COL_4", "Tax Calculation", "การคำนวณภาษี");
            request.setAttribute("labelMap", labelMap.getHashMap());
            String startDateStr = JDate.showDate(JDate.getDate());
            String endDateStr = JDate.showDate(JDate.getDate());
			String currentMonth = JDate.getDate().substring(0, 4);

			//forcompute
			Boolean CheckRun = false;
			String mm = "";
			String yy = "";
			if(request.getParameter("MM") != null && request.getParameter("YYYY") != null ){
			    mm = request.getParameter("MM");
			    yy = request.getParameter("YYYY");
			    CheckRun = false;
			}else{
			    mm = JDate.getMonth();
			    yy = JDate.getYear();
			}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript"><!--
        <%-- [ AJAX --%>
            function AJAX_Send_Request() {
	            table.rows[currentRowID].cells[2].innerHTML = '<img src="../../images/processing_icon.gif" alt="" />';
	            var target = "../../ProcessTax402Srvl?"
	                    + "&HOSPITAL_CODE="+"<%=session.getAttribute("HOSPITAL_CODE").toString()%>"
	                    + "&MM=" + document.mainForm.MM.value
	                    + "&YYYY=" + document.mainForm.YYYY.value;
	            AJAX_Request(target, AJAX_Handle_Response);
            }
            
            function AJAX_Handle_Response() {              
                if (AJAX_IsComplete()) {
                	//alert("test 11");
                    var xmlDoc = AJAX.responseXML;                    
                    if (xmlDoc.getElementsByTagName("SUCCESS")[0] == null) {
                        alert("Exception in update process");
                        return;
                    }

                    //alert("current row loop : " + currentRowID);
                    if (xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue == "0") {
                        table.rows[currentRowID].cells[2].innerHTML = '<img src="../../images/failed_icon.gif" alt="" />';
                    }
                    else {
                        table.rows[currentRowID].cells[2].innerHTML = '<img src="../../images/succeed_icon.gif" alt="" />';
                    }                    
                    document.getElementById("PROGRESS").innerHTML = " 0 / " + numRow;
                            
                    currentRowID++;
                    //AJAX_Send_Request();
                    AJAX_Data_Request();
                }
            }

            
            function AJAX_Data_Request(){
                var target = "../../ProcessTax402GetDataSrvl?"
                    + "&MM=" + document.mainForm.MM.value
                    + "&YYYY=" + document.mainForm.YYYY.value;
	            //alert(currentRowID+" : "+toRowID+" : "+target);
	            AJAX_Request(target, AJAX_Data_Handle_Response);		
            }
            
            function AJAX_Data_Handle_Response(){
                if (AJAX_IsComplete()) {
                	var tbl= document.getElementById("dataTable");  
                	if(!delete_first_row){
                		tbl.deleteRow(2);     
                		delete_first_row = true;
                	}          	
                    var xmlDoc = AJAX.responseXML;
                    //--------------------------------------------
        			var x =  xmlDoc.getElementsByTagName('DOCTOR_CODE');
               	    var y =  xmlDoc.getElementsByTagName('TAX_402_METHOD');
        			var newEl = document.createElement('TABLE');
        			newEl.setAttribute('cellPadding',5);
        			var tmp = document.createElement('TBODY');
        			newEl.appendChild(tmp);
    			   	var row = null;		
    			   	var container = null;
    			   	var theData = null;
    			   	var numRow = 0;
        			
        			 for (var i=0 ; i <x.length; i++) {
                         
                         
                        row = document.createElement('TR');
     					if (x[i].nodeType != 1) continue;
      					container = document.createElement('TD');
      					container.setAttribute('align','center');
      					theData = document.createTextNode(x[i].childNodes[0].nodeValue);
      					container.appendChild(theData);
      					row.appendChild(container);
      					
      					container = document.createElement('TD');
      					container.setAttribute('align','center');
      					theData = document.createTextNode(y[i].childNodes[0].nodeValue);
      					container.appendChild(theData);
      					row.appendChild(container);
      					
      					
      					container = document.createElement('TD');
     					container.setAttribute('align','center');
     					container.innerHTML = '<img src="../../images/waiting_icon.gif" alt="" />';
     					row.appendChild(container); 
      					
      					tmp.appendChild(row);
      					numRow++;
                     }
        			
        			tbl.appendChild(tmp);
        			currentRowID=2;
        			toRowID=numRow;
        			//alert(toRowID);
        			document.getElementById("PROGRESS").innerHTML = "0 / " + toRowID; 
        			//STOP_Click(); */
        			AJAX_Send_Update_Request();               
                }
            }       
            function AJAX_Send_Update_Request() { 
            	var tbl = document.getElementById("dataTable");             	
            	if (currentRowID <= (toRowID+1) ) {
            		tbl.rows[currentRowID].cells[2].innerHTML = '<img src="../../images/processing_icon.gif" alt="" />';
            		var target = "../../ProcessTax402GetUpdateSrvl?"
	            		+ "&DOCTOR_CODE="+ tbl.rows[currentRowID].cells[0].innerHTML
	            		+ "&TAX_METHOD="+ tbl.rows[currentRowID].cells[1].innerHTML
	                    + "&MM=" + document.mainForm.MM.value
	                    + "&MM=" + document.mainForm.MM.value
	                    + "&YYYY=" + document.mainForm.YYYY.value;  
            		AJAX_Request(target, AJAX_Handle_Update_Response);
            	}else {
                	alert("Process Complete");
            		document.mainForm.RUN.disabled = false;
            		document.mainForm.STOP.disabled = true;
            		document.mainForm.CLOSE.disabled = false;
            	}
            }

            function AJAX_Handle_Update_Response(){
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    if (xmlDoc.getElementsByTagName("SUCCESS")[0] == null) {
                        alert("Exception in update process");
                        return;
                    }
                    if (xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue == "Complete") {
                        table.rows[currentRowID].cells[2].innerHTML = '<img src="../../images/succeed_icon.gif" alt="" />';
                    }else {
                        table.rows[currentRowID].cells[2].innerHTML = '<img src="../../images/failed_icon.gif" alt="" />';
                    }                    
                    document.getElementById("PROGRESS").innerHTML = (currentRowID-1)+" / " + toRowID;                            
                    currentRowID++;
                    AJAX_Send_Update_Request();
                }            	
            }
        <%-- AJAX ] --%>
            
            function SELECT_Click() {
                document.mainForm.submit();
            }
            
            var fromRowID = 0;
            var toRowID = 0;
            var numRow = 0;
            var currentRowID = 0;
            var table = document.getElementById("dataTable"); 
            var delete_first_row = false;
			//RUN_Click
            function RUN_Click() {
                //table high = 4 : first table(top index) = 0
                table = document.getElementById("dataTable"); 
                fromRowID = currentRowID = 2;
                toRowID = document.getElementById("dataTable").rows.length - 1;
				//alert(table+":"+document.getElementById("dataTable").rows.length);
                //document.mainForm.SELECT.disabled = true;
                document.mainForm.RUN.disabled = true;
                document.mainForm.STOP.disabled = false;
                document.mainForm.CLOSE.disabled = true;
                document.getElementById("PROGRESS").innerHTML = "0 / " + numRow;
                AJAX_Send_Request();
            }
            
            function STOP_Click() {
                toRowID = 0;
                document.mainForm.RUN.disabled = false;
                document.mainForm.STOP.disabled = true;
                document.mainForm.CLOSE.disabled = false;
            }
            function load(){
            	location.href = "http://localhost:8082/tax/initProcess/<%=session.getAttribute("HOSPITAL_CODE").toString() %>";
            }
    </script>
    </head>
    <body onload="load();"></body>
</html>
