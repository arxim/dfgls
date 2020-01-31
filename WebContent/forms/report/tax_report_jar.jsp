<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>

<%@ include file="../../_global.jsp" %>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.table.Batch"%>

<%    
    if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            ProcessUtil proUtil = new ProcessUtil();
            DBConnection c = new DBConnection();
            c.connectToLocal();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
            c.Close();
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Tax Report", "รายงานภาษี");
            labelMap.add("REPORT_NAME", "Report Name", "ชื่อรายงาน");
            labelMap.add("DOCTOR_CODE_FROM", "From Doctor Code", "จากแพทย์รหัส");
            labelMap.add("DOCTOR_CODE_TO", "To Doctor Code", "ถึงแพทย์รหัส");
            labelMap.add("DOCTOR_PROFILE_CODE","Doctor Profile Code","รหัส Profile แพทย์");
			labelMap.add("MM", "Month", "เดือน");
			labelMap.add("YYYY", "Year", "ปี");
			
			labelMap.add("REPORT_402_FRONT", "ใบปะหน้า ภ.ง.ด.1 รายเดือน", "ใบปะหน้า ภ.ง.ด.1 รายเดือน");
			labelMap.add("REPORT_402", "รายงาน ภ.ง.ด.1 รายเดือน (บุคคล)", "รายงาน ภ.ง.ด.1 รายเดือน (บุคคล)");
			labelMap.add("REPORT_TAX3", "รายงาน ภ.ง.ด.3 รายเดือน (บุคคล)", "รายงาน ภ.ง.ด.3 รายเดือน (บุคคล)");
			labelMap.add("REPORT_53_FRONT", "ใบปะหน้า ภ.ง.ด.53 รายเดือน","ใบปะหน้า ภ.ง.ด.53 รายเดือน");
			labelMap.add("REPORT_TAX53", "รายงาน ภ.ง.ด.53 รายเดือน (นิติบุคคล)", "รายงาน ภ.ง.ด.53 รายเดือน (นิติบุคคล)");
			labelMap.add("WHT_402_MONTHLY", "หนังสือหัก ณ ที่จ่าย (รายเดือน)", "หนังสือหัก ณ ที่จ่าย (รายเดือน)");			
			labelMap.add("REPORT_402_YEARLY", "หนังสือรับรองรายได้ 40(2) รายปี", "หนังสือรับรองรายได้ 40(2) รายปี");			
			labelMap.add("REPORT_406", "หนังสือรับรองรายได้ 40(6)", "หนังสือรับรองรายได้ 40(6)");
			labelMap.add("REPORT_402_NEW", "รายงานภาษี 40(2) รายเดือน", "รายงานภาษี 40(2) รายเดือน");
			labelMap.add("REPORT_402_YEARLY_FRONT", "ใบปะหน้าภาษี 40(2) รายปี", "ใบปะหน้าภาษี 40(2) รายปี");	
			labelMap.add("REPORT_SUMMARY_402_YEARLY", "รายงานสรุปภาษี 40(2)รายปี", "รายงานสรุปภาษี 40(2)รายปี");
			labelMap.add("REPORT_TAX91", "รายงาน ภ.ง.ด.91", "รายงาน ภ.ง.ด.91");
			
            labelMap.add("SAVE_FILE", "Save as filename", "จัดเก็บไฟล์ชื่อ");
            labelMap.add("DOCUMENT_TYPE", "Document Type", "ประเภทเอกสาร");
            labelMap.add("VIEW", "View", "แสดงผล");
            labelMap.add("MESSAGE", "Please Select Report Module", "กรุณาเลือกรายงาน");
            labelMap.add("TERM", "TERM", "ช่วง");
            labelMap.add("PAY_DATE","Pay Date","Pay Date");
            labelMap.add("PRINT_DATE","Print Date","Print Date");       
            labelMap.add("FILLING_DATE","Filling Date", "วันที่ยื่นแบบ");
            //
            labelMap.add("TERM_FIRST","First Term","ครึ่งปีแรก");
            labelMap.add("TERM_SECOND","Second Term","ครึ่งปีหลัง");
            labelMap.add("TERM_END","Yearly","ทั้งปี");
            String report = "";
            request.setAttribute("labelMap", labelMap.getHashMap());
            //request.getParameter("DOCTOR_CODE_FROM");
            //out.println("doctor_code_from="+request.getParameter("DOCTOR_CODE_FROM"));
            
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript">
            function Report_View() {
            	if(document.mainForm.REPORT_FILE_NAME.value=='SummaryTax402Yearly' || document.mainForm.REPORT_FILE_NAME.value=='Tax402SummaryYearlyFrontPage'){
            		document.getElementById('YYYY').value = document.getElementById('YYYY1').value;
                }
            	if(document.mainForm.REPORT_FILE_NAME.value == "None"){
                    alert("Please Select Report");
                    document.mainForm.REPORT_FILE_NAME.focus();
                }else{
					if(document.mainForm.MM.value == "" || document.mainForm.YYYY.value == ""){
						alert("Please Select Month/Year");
						if(document.mainForm.MM.value == ""){
							document.mainForm.MM.focus();
						}else{
							document.mainForm.YYYY.focus();
						}
					}else{
						document.mainForm.REPORT_DISPLAY.value = "view";
                    	document.mainForm.target = "_blank";
                    	document.mainForm.submit();
					}
                }
            }
            function Report_Save() {
            	if(document.mainForm.REPORT_FILE_NAME.value=='SummaryTax402Yearly'){
            		document.getElementById('YYYY').value = document.getElementById('YYYY1').value;
                }   
                if(document.mainForm.REPORT_FILE_NAME.value == "None" || document.mainForm.SAVE_FILE.value == ""){                    
                    if(document.mainForm.REPORT_FILE_NAME.value == "None"){
						alert("Please Select Report");
                        document.mainForm.REPORT_FILE_NAME.focus();
                    }else{
						alert("Please Enter File Name");
                        document.mainForm.SAVE_FILE.focus();
                    }					
                }else{
					if(document.mainForm.MM.value == "" || document.mainForm.YYYY.value == ""){
						alert("Please Select Month/Year");
						if(document.mainForm.MM.value == ""){
							document.mainForm.MM.focus();
						}else{
							document.mainForm.YYYY.focus();
						}
					}else{
						document.mainForm.REPORT_DISPLAY.value = "save";
                    	document.mainForm.target = "_blank";
                    	document.mainForm.submit();
					}
                }
            }
            function changeDropDownList(){
				var e = document.getElementById('Tax406');
				var term = document.getElementById('term');
				var year = document.getElementById('Tax402Year');
				var d = document.getElementById('Tax402');
                if(document.mainForm.REPORT_FILE_NAME.value=='TaxLetter406'){
                    e.style.display = "";
                	d.style.display = 'none';
                	term.style.display = 'none';
                	year.style.display = 'none';
                	year_term.style.display = 'none';
                    document.getElementById('YEAR').disabled = "";
                    document.getElementById('PRINT_DATE').disabled = "";
                    document.getElementById('DOCTOR_CODE_FROM').disabled = "";
                }else if(document.mainForm.REPORT_FILE_NAME.value=='rd1_monthly' || document.mainForm.REPORT_FILE_NAME.value=='rd3_monthly' || document.mainForm.REPORT_FILE_NAME.value=='rd53_monthly'){
                    d.style.display = "";
                    term.style.display = "";
                	e.style.display = 'none';
                	year.style.display = 'none';
                	year_term.style.display = 'none';
                    document.getElementById('PAY_DATE').disabled = "";
                    document.getElementById('FILLING_DATE').disabled = "";
                    document.getElementById('MM').disabled = "";
                    document.getElementById('YYYY').disabled = "";
                }else if(document.mainForm.REPORT_FILE_NAME.value=='rd1_monthly_cover' || document.mainForm.REPORT_FILE_NAME.value=='rd53_monthly_cover' || mainForm.REPORT_FILE_NAME.value=='rd_wht_monthly'){
                	d.style.display = "";
                    term.style.display = "";
                	e.style.display = 'none';
                	year.style.display = 'none';
                	year_term.style.display = 'none';
                    document.getElementById('MM').disabled = "";
                    document.getElementById('YYYY').disabled = "";
                    document.getElementById('LABEL_PAYDATE').style.display = "none";
					document.getElementById('INPUT_PAYDATE').style.display = "none";
                }else if(document.mainForm.REPORT_FILE_NAME.value=='rd_summary_yearly'){
                    year.style.display = "";
                	e.style.display = 'none';
                	d.style.display = 'none';
                	term.style.display = "";
                	year_term.style.display = 'none';
                    document.getElementById('MM').disabled = "";
                    document.getElementById('YYYY402').disabled = "";
                    document.getElementById('PRINT_DATE').disabled = "";
                }else{
                	e.style.display = 'none';
                	d.style.display = 'none';
                	term.style.display = 'none';
                	year.style.display = 'none';
                	year_term.style.display = 'none';
                }
            }
                       
/********************************************************************************************************************
 * Retreive doctor information : Begin
 */            
            function DOCTOR_CODE_FROM_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
                if (key == 13) {
                    document.mainForm.DOCTOR_CODE_FROM.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            function AJAX_Refresh_DOCTOR_FROM() {
            //alert("code1="+code);
            //alert("code2="+document.mainForm.DOCTOR_CODE_FROM.value);
                var target = "../../RetrieveData?A=1&TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE_FROM.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                //alert("target="+target);//A=1
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_FROM);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_FROM() 
            {
            	//alert(AJAX_IsComplete());
            	
                if (AJAX_IsComplete()) 
                {
                	var xmlDoc = AJAX.responseXML;
					//alert("CODE="+getXMLNodeValue(xmlDoc, "DOCTOR"));
                    if (!isXMLNodeExist(xmlDoc, "DOCTOR")) 
                    {
                        ddocument.mainForm.DOCTOR_CODE_FROM.value = "";
                        document.mainForm.DOCTOR_NAME_FROM.value = "";
                        return;
                   }
                   		// Data found
                    	document.mainForm.DOCTOR_NAME_FROM.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                	//e.innerHTML = 'success2';
                	//alert("code_after="+document.mainForm.DOCTOR_CODE_FROM.value);
                }
            }
                        
/********************************************************************************************************************
 * Retreive doctor information : End
 */
 
 /********************************************************************************************************************
 * Retreive doctor information : Begin
 */            
            function DOCTOR_CODE_TO_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
                if (key == 13) {
                    document.mainForm.DOCTOR_CODE_TO.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            function AJAX_Refresh_DOCTOR_TO() {
                var target = "../../RetrieveData?A=1&TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE_TO.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_TO);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_TO() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DOCTOR_CODE_TO.value = "";
                        document.mainForm.DOCTOR_NAME_TO.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_NAME_TO.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
                        
/********************************************************************************************************************
 * Retreive doctor information : End
 */
        </script>
    </head>
    <body leftmargin="0" onload='changeDropDownList()'>
        <form id="mainForm" name="mainForm" method="get" action="../../ViewReportSrvl">
            <center>
		<table width="800" border="0">
		<tr><td align="left">
		<b><font color='#003399'><%=Utils.getInfoPage("tax_report_jar.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
		</td></tr>
		</table>
            </center>
            <table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
                <input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="tax_report"/>
                <tr>
                    <th colspan="4"><div style="float: left;">${labelMap.TITLE_MAIN}</div></th>
                </tr>
                <tr>
                    <td class="label"><label for="REPORT_NAME">${labelMap.REPORT_NAME}</label></td>
                    <td colspan="3" class="input">
					<select class="mediumMax" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" onchange="changeDropDownList();">
                        <option value="None">-- Select Tax Report --</option>
                        <option value="rd1_monthly_cover">ใบปะหน้า ภ.ง.ด.1 รายเดือน</option>
						<option value="rd1_monthly">รายงาน ภ.ง.ด.1 รายเดือน (บุคคล)</option>
                        <!-- <option value="rd3_monthly">รายงาน ภ.ง.ด.3 รายเดือน (บุคคล)</option> -->
                        <option value="rd53_monthly_cover">ใบปะหน้า ภ.ง.ด.53 รายเดือน</option>
                        <option value="rd53_monthly">รายงาน ภ.ง.ด.53 รายเดือน (นิติบุคคล)</option>
                        <option value="rd_wht_monthly">หนังสือหัก ณ ที่จ่าย (รายเดือน)</option>
                        <option value="rd1a_yearly_cover">ใบปะหน้า ภ.ง.ด.1ก รายปี</option>
                        <option value="rd1a_yearly">รายงาน ภ.ง.ด.1ก รายปี</option>
                        <option value="rd_summary_yearly">หนังสือรับรองรายได้ 40(2) รายปี</option>
						<option value="rd406_period">หนังสือรับรองรายได้ 40(6)</option>
                    </select>
                </tr>
                <tbody id='year_term'>
				<tr>
                    <td class="label"><label>${labelMap.YYYY}</label></td>
                    <td colspan="3" class="input"><%=proUtil.selectYY("YYYY1", b.getYyyy())%> </td>
                </tr>
                </tbody>                
                <tbody id='term'>
				<tr>
                    <td class="label"><label>${labelMap.MM}</label><NULL/td>
                    <td class="input"><%=proUtil.selectMM402(session.getAttribute("LANG_CODE").toString(), "MM", b.getMm())%></td>
                    <td class="label"><label>${labelMap.YYYY}</label></td>
                    <td class="input"><%=proUtil.selectYY("YYYY", b.getYyyy())%></td>
                </tr>
                </tbody>                
                <tbody id='Tax406'>
                	<tr>
                		<td class="label"><label for="TERM">${labelMap.TERM}</label></td>
                		<td class="input" >
                			<select name='TERM' class='medium'>
                				<option value='01'>${labelMap.TERM_FIRST}</option>
                				<option value='06'>${labelMap.TERM_SECOND}</option>
                				<option value='12'>${labelMap.TERM_END}</option>
                			</select>
                		</td>
                		<td class="label"><label>${labelMap.YYYY}</label></td>
                    	<td class="input"><%=proUtil.selectYY("YEAR", b.getYyyy())%></td>
                	</tr>
                	<tr>
                		<td class="label"><label for="PRINT_DATE">${labelMap.PRINT_DATE}</label></td>
                		<td colspan="3" class="input">
	                		<input name="PRINT_DATE" type="text" class="short" id="PRINT_DATE" maxlength="10" value="" />
	                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('PRINT_DATE'); return false;" />
                        </td>
                	</tr>
	               	<tr>
	                    <td class="label"><label for="DOCTOR_CODE_FROM">${labelMap.DOCTOR_CODE_FROM}</label></td>
	                    <td colspan="3" class="input">
	                        <input type="text" id="DOCTOR_CODE_FROM" name="DOCTOR_CODE_FROM" class="short" onkeypress=" return DOCTOR_CODE_FROM_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_FROM();" />
	                        <input id="SEARCH_DOCTOR_CODE_FROM" name="SEARCH_DOCTOR_CODE_FROM" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_ <%= labelMap.getFieldLangSuffix() %>&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=DOCTOR_CODE_FROM&HANDLE=AJAX_Refresh_DOCTOR_FROM'); return false;" />
	                        <input type="text" id="DOCTOR_NAME_FROM" name="DOCTOR_NAME_FROM" class="mediumMax" readonly="readonly" />                    </td>
	                </tr>
                </tbody>
                <tbody id='Tax402'>
                	<tr>
                		<td class="label" id="LABEL_PAYDATE"><label for="PAY_DATE">${labelMap.PAY_DATE}</label></td>
                		<td colspan="3" class="input" id="INPUT_PAYDATE">
	                		<input name="PAY_DATE" type="text" class="short" id="PAY_DATE" maxlength="10" value="" />
	                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('PAY_DATE'); return false;" />
                        </td>
                	</tr>
                	<tr>
                		<td class="label"><label for="FILLING_DATE">${labelMap.FILLING_DATE}</label></td>
                		<td colspan="3" class="input">
	                		<input name="FILLING_DATE" type="text" class="short" id="FILLING_DATE" maxlength="10" value="" />
	                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('FILLING_DATE'); return false;" />
                        </td>
                	</tr>
                </tbody>
                <tbody id='Tax402Year'>
                	<!-- 
                	<tr>
                		<td class="label"><label>${labelMap.YYYY}</label></td>
                    	<td class="input" colspan="3" ><%=proUtil.selectYY("YYYY402", b.getYyyy())%></td>
                	</tr>
                	 -->
	               	<tr>
	                    <td class="label"><label for="DOCTOR_CODE_TO">${labelMap.DOCTOR_CODE_TO}</label></td>
	                    <td class="input">
	                        <input type="text" id="DOCTOR_CODE_TO" name="DOCTOR_CODE_TO" class="short" onkeypress="return DOCTOR_CODE_TO_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_TO();" />
	                        <input id="SEARCH_DOCTOR_CODE_TO" name="SEARCH_DOCTOR_CODE_TO" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=DOCTOR_NAME_TO&TARGET=DOCTOR_CODE_TO&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_TO'); return false;" />
	                        <input type="text" id="DOCTOR_NAME_TO" name="DOCTOR_NAME_TO" class="mediumMax" readonly="readonly" />
	                    </td>
	                    <td class="label"><label for="PRINT_DATE">${labelMap.PRINT_DATE}</label></td>
                		<td class="input">
	                		<input name="PRINTING_DATE" type="text" class="short" id="PRINTING_DATE" maxlength="10" value="" />
	                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('PRINTING_DATE'); return false;" />
                        </td>
	                    
	                </tr>
                </tbody>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="Report_Save();" disabled/>
                        <input type="button" id="VIEW" name="VIEW" class="button" value="${labelMap.VIEW}" onclick="Report_View();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
					</th>
                </tr>
            </table>
        </form>
    </body>
</html>