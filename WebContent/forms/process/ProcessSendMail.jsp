<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@ include file="../../_global.jsp"%>

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
            ProcessUtil proUtil = new ProcessUtil();
            DBConnection c = new DBConnection();
            c.connectToLocal();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
            c.Close();
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Send e-mail", "แสดงรายงานภาษีสำหรับแพทย์");
            labelMap.add("TAX_TYPE", "Tax Type", "ประเภทภาษี");
            labelMap.add("PRINT_DATE", "Issued On", "ลงวันที่");
            /* labelMap.add("END_DATE", "Payment Term To", "สิ้นสุดงวดเดือน"); */
            labelMap.add("FIRST_TAX_TERM", "First Term", "รายได้ครึ่งปีแรก");
            labelMap.add("SECOND_TAX_TERM", "Second Term", "รายได้ครึ่งปีหลัง");
            labelMap.add("ALL_TAX_TERM", "Yearly", "รายได้ทั้งปี");
            labelMap.add("MM", "Tax Term Month", "รายได้ทั้งปี");
            labelMap.add("YYYY", "Year", "รายได้ทั้งปี");
            labelMap.add("TAX_402", "Tax 40(2)", "ภาษี 40(2)");
            labelMap.add("TAX_406", "Tax 40(6)", "ภาษี 40(6)");
            /* labelMap.add("COL_0", "No.", "ลำดับ");
            labelMap.add("COL_1", "Doctor Code", "รหัสแพทย์");
            labelMap.add("COL_2", "Doctor Name", "ชื่อแพทย์");
            labelMap.add("COL_3", "Status", "สถานะ"); */

            request.setAttribute("labelMap", labelMap.getHashMap());
            
            /* String startDateStr = request.getParameter("START_DATE");
            String endDateStr = request.getParameter("END_DATE"); */
            
            //
            // Process request
            //
            String mm_str="";
            String yy_str="";
            
            if(request.getParameter("YYYY") !=null){
            	yy_str=request.getParameter("YYYY");
            }else{
            	yy_str=b.getYyyy();
            }
            /* if(request.getParameter("MM") !=null)
            {
            	mm_str=request.getParameter("MM");
            }
            
            String cond = "1 <> 1"; */
            /* if (request.getParameter("START_DATE") != null && request.getParameter("END_DATE") != null) {
                startDateStr = "01/" + request.getParameter("START_DATE");
                endDateStr = "31/" + request.getParameter("END_DATE");
                
                cond = " D.ACTIVE = '1' " +
                       " AND D.HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "'" +
                       " AND T.PAYMENT_DATE >= '" + JDate.saveDate(startDateStr) + "'" +
                       " AND T.PAYMENT_DATE <= '" + JDate.saveDate(endDateStr) + "'";
             } */
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${labelMap.TITLE_MAIN}</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" type="text/css" href="../../css/share.css"
		media="all" />
	<script type="text/javascript" src="../../javascript/util.js"></script>
	<script type="text/javascript" src="../../javascript/ajax.js"></script>
	<link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
	<script type="text/javascript" src="../../javascript/calendar.js"></script>
	<script type="text/javascript">
        <%-- [ AJAX --%>
        function AJAX_Send_Request() {
			if(document.mainForm.TAX_TYPE.value.includes('Tax402')){
				document.getElementById('YYYY').value = document.getElementById('YYYY402').value;
			}
			if(document.mainForm.TAX_TYPE.value.includes('TaxLetter406')){
				document.getElementById('YYYY').value = document.getElementById('YYYY406').value;
			}
//			alert(document.getElementById('YYYY').value);
            var target = "../../ProcessSendMailSrvl?"
                + "&HOSPITAL_CODE=<%=session.getAttribute("HOSPITAL_CODE").toString()%>"
				+ "&MM=" + document.mainForm.MM.value
                + "&YYYY="+ document.getElementById('YYYY').value
                + "&TAX_TYPE="+document.mainForm.TAX_TYPE.value ;
				
				//document.mainForm.submit();
				 AJAX_Request(target, AJAX_Handle_Response);
        }

        function AJAX_Handle_Response() {
        	//alert("AJAX_Handle_Response");
			
            if (AJAX_IsComplete()) {
                var xmlDoc = AJAX.responseXML;
                if (xmlDoc.getElementsByTagName("SUCCESS")[0] == null) {
                    alert("Exception in update process");
                    return;
                }
				
                if (xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue == "SUCCESS") {
                    alert('Send e-mail COMPLETE');
                }else {
                    alert('Send e-mail FAIL PLEASE CHECK YOUR DATA!!');
                }
                //document.getElementById("PROGRESS").innerHTML = (currentRowID - 1) + " / " + numRow;
                //currentRowID++;
                //AJAX_Send_Request();
                document.mainForm.RUN.disabled = false;
                document.getElementById("divWait").style.display = "none";
                return;
            }
        }
        <%-- AJAX ] --%>
        function RUN_Click() {
            document.mainForm.RUN.disabled = true;
            document.getElementById("divWait").style.display = "block";
            AJAX_Send_Request();
        }
            
        function changeDropDownList(){
//        	alert(document.mainForm.TAX_TYPE.value);
        	if(document.mainForm.TAX_TYPE.value.includes('TaxLetter406')){
        		//alert(document.mainForm.PRINT_DATE.value)
        		document.getElementById('tax402').style.display = 'none';
        		document.getElementById('tax406').style.display = ""; 
//        		document.getElementById('printDate').style.display = "";
        	}
        	else if(document.mainForm.TAX_TYPE.value=='Tax402SummaryYearlyForDoctor'){
        		document.getElementById('tax402').style.display = "";
//				document.getElementById('printDate').style.display = "";
        		document.getElementById('tax406').style.display = 'none';
        	}
        	else{
        		document.getElementById('tax402').style.display = 'none';
        		document.getElementById('tax406').style.display = 'none';
        		document.getElementById("divWait").style.display = "none";
        	}
        }
           
        </script>
</head>
<body onload='changeDropDownList()'>
	<form id="mainForm" name="mainForm" method="post" action="ProcessSendMailSrvl">
		<table class="form">
			<tr>
				<th colspan="4">${labelMap.TITLE_MAIN}</th>
			</tr>
			<tr>
				<td class="label"><label>${labelMap.TAX_TYPE}</label></td>
				<td class="input">
					<select class="medium" id="TAX_TYPE" name="TAX_TYPE" onchange="changeDropDownList();">
						<option value="None">
							--Select Tax Type --
						</option>
						<option value="Tax402SummaryYearlyForDoctor">
							${labelMap.TAX_402}
						</option>
						<option value="<%=session.getAttribute("HOSPITAL_CODE").equals("050") ? "TaxLetter406ForDoctor050":"TaxLetter406ForDoctor"%>">
							${labelMap.TAX_406}
						</option>
						
					</select>
				</td>
			</tr>
			<tbody id='tax402'>
			<tr>
				<td class="label"><label>${labelMap.YYYY}</label></td>
				<td class="input" id="YYYY"><%=proUtil.selectYY("YYYY402", yy_str)%></td>
			</tr>
			</tbody>
			
			<tbody id='tax406'>
			<tr>
				<td class="label"><label>${labelMap.MM}</label></td>
				<td class="input">
					<select class="medium" id="MM" name="MM">
							<option value="None"
								<% if(mm_str.equals("None")) { out.println("selected");}%>>--
								Select Tax Term --</option>
							<option value="01">${labelMap.FIRST_TAX_TERM}</option>
							<option value="06">${labelMap.SECOND_TAX_TERM}</option>
							<option value="12">${labelMap.ALL_TAX_TERM}</option>
					</select>
				</td>

				<td class="label"><label>${labelMap.YYYY}</label></td>
				<td class="input"><%=proUtil.selectYY("YYYY406", yy_str)%></td>
			</tr>
			</tbody>
			
			<!--  tbody id='printDate'>
				<tr>
					<td class="label"><label for="PRINT_DATE">${labelMap.PRINT_DATE}</label>
					</td>
					<td class="input">
					<input type="text" id="PRINT_DATE" name="PRINT_DATE" class="short" /> 
					<input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('PRINT_DATE'); return false;" />
					</td>
					
				</tr>
			</tbody-->

			<tr>
				<th colspan="4" class="buttonBar">
					<input type="button" id="RUN" name="RUN" class="button" value="${labelMap.RUN}" onclick="RUN_Click()" /> <%-- disabled="disabled" --%> 
					<input type="button" id="STOP" name="STOP" class="button" value="Stop" onclick="STOP_Click()" disabled="disabled" /> 
					<input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" /></th>
			</tr>
		</table>
		<div id="divWait">
            <table class="form">
                <tr>
                    <td bgcolor="#FFFFFF" align="center"><img src="../../images/wait30trans.gif"></td>
                </tr>
            </table>
        </div>
		<hr />
	</form>
</body>
		