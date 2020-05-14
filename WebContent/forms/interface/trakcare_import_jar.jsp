<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap,java.util.*" errorPage="../error.jsp"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.obj.util.ReadProperties"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils "%>
<%@page import="java.sql.Types"%>

<%    
        if (!Guard.checkPermission(session, Guard.PAGE_INPUT_METHOD_ALLOC_ITEM_MAIN)) {
            response.sendRedirect("../message.jsp");
            return;
        }

        if (session.getAttribute("LANG_CODE") == null)
            session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
    
	    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
		labelMap.add("TITLE_MAIN", "Interface Inbound", "นำเข้าข้อมูลค่าแพทย์");
		labelMap.add("INTERFACE_PROCESS", "Interface Process", "นำเข้ารายการ");
		labelMap.add("FILE_INTERFACE", "File Interface", "ไฟล์นำเข้า");
		labelMap.add("FROM_DATE", "From Date", "ตั้งแต่วันที่");
	    labelMap.add("TO_DATE", "To Date", "ถึงวันที่");
	    labelMap.add("SELECT_DATE","File Interface Date","วันที่นำเข้าไฟล์");
		labelMap.add("INTERFACE_TRANSACTION", "Interface DF Transaction", "นำเข้ารายการค่าแพทย์");
		labelMap.add("INTERFACE_TRANSACTION_RESULT", "Interface DF Result", "นำเข้ารายการแพทย์อ่านผล");
		labelMap.add("INTERFACE_AR_TRANSACTION", "Interface AR Transaction", "นำเข้ารายการรับชำระหนี้");
		labelMap.add("INTERFACE_ONWARD", "Interface Onward (Monthly)", "นำเข้ารายการค่าแพทย์ Onward (รายเดือน)");
		labelMap.add("INTERFACE_GUARANTEE", "Interface Guarantee", "นำเข้ารายการรับชำระหนี้");
		labelMap.add("INTERFACE_CO", "Interface C/O", "นำเข้ารายการค่าใช้จ่าย");
		labelMap.add("INTERFACE_PATHO", "Interface Pathology", "นำเข้ารายการแล๊ปชิ้นเนื้อ");
		labelMap.add("INTERFACE_EXPENSE", "File Excel Expense", "นำเข้ารายได้/ค่าใช้จ่าย");
		labelMap.add("INTERFACE_TIME_TABLE", "File Excel Time Table", "นำเข้าตารางเวลาแพทย์");
		labelMap.add("INTERFACE_OPD_CHECKUP", "Interface OPD Check Up (Monthly)", "นำเข้ารายการตรวจสุขภาพ OPD");
		labelMap.add("INTERFACE_DISCHARGE_SUMMARY", "File Excel Discharge Summary", "Discharge Summary");
		labelMap.add("SAVE", "Import", "นำเข้า");
		
    	request.setAttribute("labelMap", labelMap.getHashMap());
    	ReadProperties rp = new ReadProperties();
	    DBConnection conn = new DBConnection();
	    conn.connectToLocal();
		String report = ""; 
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
		<link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
		<script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/jquery-1.6.2.min.js"></script>
        <script type="text/javascript" src="../../javascript/jquery-ui-1.8.16.custom.min.js"></script>
		<script type="text/javascript">
	        function Interface_Save() {
	        var selectTypeDF = document.mainForm.INTERFACE_PROCESS;
	        var selectFileType = document.mainForm.selectFileType;
	        var file = document.mainForm.FILE_INTERFACE;
	        var date = document.mainForm.INTERFACE_DATE;
	        var get_type = '';
                
                for(i=0; i < selectFileType.length ; i++ ){
                    if(selectFileType[i].checked){
                        get_type = selectFileType[i].value;
                    }
                }
                
                if(selectTypeDF.value == ''){
		    	alert("Please Select Process or Choose Import File");
                    selectTypeDF.focus();
                    return false;
                }else if(get_type=='date'){
                    if(date.value == ''){
                        alert('Invalid date');
                        date.focus();
                        return false;
                    }
                }else{
                    if(file.value == ''){
                        alert('Invalid File');
                        file.focus();
                        return false;
                    }
                }
                
                document.mainForm.SOURCE_FILE.value = document.mainForm.FILE_INTERFACE.value;
                <%-- document.mainForm.action = "http://<%= rp.getPropertiesData("config.properties", "interface.","ip").get("ip") %>:8883/interfaceFileDF"; --%>
                document.mainForm.action = "http://<%= rp.getPropertiesData("config.properties", "interface.","ip").get("ip") %>:8883/interfaceFileDF";
                
                
                
                document.getElementById("messageModal").style.display = "block";
                document.getElementById('msgBody').textContent = "Please Wait..";
                
                // When the user clicks on <span> (x), close the modal
                document.getElementsByClassName("close")[0].onclick = function() {
                document.getElementById("messageModal").style.display = "none";
                }
               <%--  var target = "http://<% rp.getPropertiesData("config.properties", "interface.","ip").get("ip"); %>:8883/interfaceFileDF?INTERFACE_PROCESS="+document.mainForm.INTERFACE_PROCESS.value+"&INTERFACE_DATE=" + document.mainForm.INTERFACE_DATE.value + "&businessCode="+document.mainForm.businessCode.value; --%>
               <%-- var target = "http://<%= rp.getPropertiesData("config.properties", "interface.","ip").get("ip") %>:8883/interfaceFileDF?INTERFACE_PROCESS="+document.mainForm.INTERFACE_PROCESS.value+"&INTERFACE_DATE=" + document.mainForm.INTERFACE_DATE.value + "&businessCode="+document.mainForm.businessCode.value; --%>
              
               if(get_type=="file"){
            	   
            	  /*  var form = document.mainForm.FILE_INTERFACE.files[0]; // You need to use standard javascript object here
           		   var formData = new FormData(form); */
           		 
            	   var target = "http://<%= rp.getPropertiesData("config.properties", "interface.","ip").get("ip") %>:8883/uploadFile";
            	   /* ?INTERFACE_PROCESS="+document.mainForm.INTERFACE_PROCESS.value+"&INTERFACE_DATE=" + document.mainForm.INTERFACE_DATE.value + "&businessCode="+document.mainForm.businessCode.value; */  
           		   document.mainForm.action = target;
           		   document.mainForm.submit();
            	  //document.mainForm.FILE_INTERFACE.files[0];
            	   
               }else{
            	   var target = "http://<%= rp.getPropertiesData("config.properties", "interface.","ip").get("ip") %>:8883/interfaceFileDF?INTERFACE_PROCESS="+document.mainForm.INTERFACE_PROCESS.value+"&INTERFACE_DATE=" + document.mainForm.INTERFACE_DATE.value + "&businessCode="+document.mainForm.businessCode.value;  
            	   AJAX_Request(target, AJAX_Result_Message);
               }
               
               
                return true;
			}
            
            function AJAX_Result_Message(){
            	
            	if (AJAX_IsComplete()) {
            		var xmlDoc = AJAX.responseText;
            		
            		document.getElementById('messageModal').style.display = 'none';
            		document.getElementById('msgBody').textContent = xmlDoc;
            		document.getElementById('messageModal').style.display = 'block';
            		document.getElementById('btnClose').style.display = 'block';
            		
                    
                    //Data found
					//alert(xmlDoc);
                }
            }
                
            function changeDropDownList(){
				var e = document.getElementById('excel');
				var f = document.getElementById('download_excel');
				var g = document.getElementById('download_time_table');
				if(document.mainForm.INTERFACE_PROCESS.value=='ImportExpenseExcel'){
                    e.style.display = 'none';
                    f.style.display = 'table-row';
                    g.style.display = 'none';
                }else if(document.mainForm.INTERFACE_PROCESS.value=='ImportTimeTable'){
                    e.style.display = 'none';
                    f.style.display = 'none';
                    g.style.display = 'table-row';
            	}else{
                	e.style.display = 'table-row';
                	f.style.display = 'none';
                    g.style.display = 'none';
                }
			}
            function AJAX_Handle_Refresh_Result_Message() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    //Data found
					alert(xmlDoc);
					alert(getXMLNodeValue(xmlDoc, "SUCCESS"));
                }
            }
			$(document).ready(function() {
				 $(function() {
				     $("select#INTERFACE_PROCESS").change(function() {
				    	 if($("#INTERFACE_PROCESS").val()=="ImportDischargeSummary"){
				    		 document.getElementById('FILE_INTERFACE').disabled = false;
				    		 document.getElementById('INTERFACE_DATE').disabled = true;
				    		 $('#selectFileType').attr('checked', 'checked');
				    		 $('#selectFileType1').attr('disabled', 'disabled');				    		 
				    	 }else{
				    	 	 $('#selectFileType1').removeAttr('disabled');
				    	 }
				     });
		            });
				});
		</script>
		<style>
			.no-close .ui-dialog-titlebar-close {display: none }
			
			/* The Modal (background) */
			.modal {
  				display: none; /* Hidden by default */
  				position: fixed; /* Stay in place */
  				z-index: 1; /* Sit on top */
  				padding-top: 100px; /* Location of the box */
  				left: 0;
  				top: 0;
  				width: 100%; /* Full width */
  				height: 100%; /* Full height */
  				overflow: auto; /* Enable scroll if needed */
  				background-color: rgb(0,0,0); /* Fallback color */
  				background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
			}

			/* Modal Content */
			.modal-content {
				position: relative;
  				background-color: #fefefe;
  				margin: auto;
  				padding: 0px;
  				border: 1px solid #888;
  				font-size: 14px;
  				width: 30%;
			}

			/* The Close Button */
			.close {
  				color: #aaaaaa;
  				float: right;
  				font-size: 28px;
  				font-weight: bold;
			}
			
			#btnClose {
				display: none; /* Hidden by default */
  				float: right;
			}

			.close:hover, .close:focus {
  				color: #000;
  				text-decoration: none;
  				cursor: pointer;
			}
			
			.modal-header {
  				padding: 2px 10px;
  				color: white;
			}

			.modal-body {padding: 2px 20px;}

			.modal-footer {
  				padding: 2px 10px;
  				color: white;
			}
		</style>
    </head>
	<body onload="changeDropDownList();">
   		 <div style="visibility: hidden;" id="dialog-modal" title="Message">
   		 	<p>Please Wait...</p>
         </div>
        <form id="mainForm" name="mainForm" method="post" target="myiframe" enctype="multipart/form-data">
        <input type="hidden" id="SOURCE_FILE" name="SOURCE_FILE"/>
        <input type="hidden" id="businessCode" name="businessCode" value=<%=session.getAttribute("HOSPITAL_CODE")%> />
            <center>
                <table width="800" border="0">
				<tr><td align="left">
				<b><font color='#003399'><%=Utils.getInfoPage("trakcare_import_jar.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
				</td></tr>
				</table>
            </center>
		<table class="form">
                <tr>
                  <th colspan="4"><div style="float: left;">${labelMap.TITLE_MAIN}</div></th>
			    </tr>
		        <tr>
		          <td class="label"><label for="INTERFACE_PROCESS">${labelMap.INTERFACE_PROCESS}</label></td>
		          <td colspan="3" class="input"><select class="mediumMax" id="INTERFACE_PROCESS" name="INTERFACE_PROCESS" onchange="changeDropDownList();">
                    <option value="">-- Select --</option>
                    <option value="ImportTransaction">${labelMap.INTERFACE_TRANSACTION}</option>
                    <option value="ImportVerifyTransaction">${labelMap.INTERFACE_TRANSACTION_RESULT}</option>
                    <option value="ImportARTransaction">${labelMap.INTERFACE_AR_TRANSACTION}</option>
                    <!-- 
                    <option value="ImportOnWard">${labelMap.INTERFACE_ONWARD}</option>
                    <option value="ImportOPDCheckup">${labelMap.INTERFACE_OPD_CHECKUP}</option>
                    <option value="ImportExpense">${labelMap.INTERFACE_CO}</option>
                    <option value="ImportExpenseExcel">${labelMap.INTERFACE_EXPENSE}</option>
                    <option value="ImportTimeTable">${labelMap.INTERFACE_TIME_TABLE}</option>
                    <option value="ImportDischargeSummary">${labelMap.INTERFACE_DISCHARGE_SUMMARY}</option>
                     -->
                  </select></td>
        		</tr>
        	<tr id='download_excel'>
       			<td align="right" class="label"><strong> ${labelMap.INTERFACE_EXPENSE} </strong></td>
          		<td colspan="3" valign="middle" class="input"><a href="../../templete_expense.xls"><img src="../../images/xls_icon.gif" width="25" height="25" border="0" /></a> Click icon to download </td>
			</tr>
        	<tr id='download_time_table'>
       			<td align="right" class="label"><strong> ${labelMap.INTERFACE_TIME_TABLE} </strong></td>
          		<td colspan="3" valign="middle" class="input"><a href="../../time_table.xls"><img src="../../images/xls_icon.gif" width="25" height="25" border="0" /></a> Click icon to download </td>
			</tr>
        <tr>
            <td class="label" rowspan="1"><label for="FILE_INTERFACE">${labelMap.FILE_INTERFACE}</label></td>
            <td colspan="3" class="input">
                <input type="radio" id="selectFileType" name="selectFileType" value="file" onclick="functionType(this);" checked>
                <input type="file" class="long" id="FILE_INTERFACE" name="FILE_INTERFACE">
            </td>
        </tr>
        <tr id='excel'>
        	<td class="label" rowspan="1"><label for="FILE_INTERFACE">${labelMap.SELECT_DATE}</label></td>
            <td colspan="3" class="input">
                <input type="radio" id="selectFileType1" name="selectFileType" value="date" onclick="functionType(this);">
                <input name="INTERFACE_DATE" type="text" class="short" id="INTERFACE_DATE" maxlength="10" value="<%=JDate.showDate(JDate.getDate()) %>" />
                <input type="image"  id="image_button" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('INTERFACE_DATE'); return false;" />
            </td>
        </tr>
        <tr>
            <th colspan="4" class="buttonBar">
            <input type="button" id="LOAD" name="LOAD" class="button" value="Load" onclick="Interface_Save();"  />
            <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="Interface_Save();" />
            <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
            <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />				  	</th>
        </tr>
    </table>
    </form>
    <div id="messageModal" class="modal">
  		<!-- Modal content -->
  		<div class="modal-content">
    	<div class="modal-header">
      		<span class="close">&times;</span>
      		<h3>Message</h3>
    	</div>
    	<div class="modal-body">
      		<span id='msgBody'></span>
    	</div>
    	<div class="modal-footer">
      		<h3>.
      			<input type="button" id="btnClose" name="btnClose" class="button" value="${labelMap.CLOSE}" onclick="document.getElementById('messageModal').style.display = 'none';"/>
      		</h3>
    	</div>	
  		</div>
	</div>
 	<iframe name="myiframe" width="0" height="0"></iframe>
	</body>
</html>
<script language="javascript">
    function functionType(ck){
        var e = document.getElementById('FILE_INTERFACE');
        var d = document.getElementById('INTERFACE_DATE');
        var s = ck;
        if(s.value == 'date'){
            e.disabled = true;
            d.disabled = false;
        }else{
            e.disabled = false;
            d.disabled = true;
        }
    }
    functionType(document.mainForm.selectFileType);
</script>