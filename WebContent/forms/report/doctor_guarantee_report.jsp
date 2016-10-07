<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>

<%@ include file="../../_global.jsp" %>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.db.DBMgr"%>
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
            labelMap.add("TITLE_MAIN", "Management Report", "รายงานรายการบริหารศูนย์ฯ");
            labelMap.add("REPORT_NAME", "Report Name", "ชื่อรายงาน");
            labelMap.add("DOCTOR_CODE_FROM", "From Doctor Code", "จากแพทย์รหัส");
            labelMap.add("DOCTOR_CODE_TO", "To Doctor Code", "ถึงแพทย์รหัส");
			labelMap.add("MM", "Month", "เดือน");
			labelMap.add("YYYY", "Year", "ปี");
			labelMap.add("DOCTOR_TYPE_CODE", "Doctor Type", "ประเภทแพทย์");
			
			labelMap.add("REPORT_HEART_INVASIVE_MANAGEMENT", "Heart Invasive Management Report", "รายการบริหารศูนย์หัวใจ(Invasive)");
			labelMap.add("REPORT_HEART_NON_INVASIVE_MANAGEMENT", "Heart Non Invasive Management Report", "รายการบริหารศูนย์หัวใจ(Non Invasive)");
			labelMap.add("REPORT_HEART_SERGERY_MANAGEMENT", "Heart Sergery Management Report", "รายการบริหารศูนย์หัวใจ(Sergery)");
			labelMap.add("REPORT_XRAY_BOND_MANAGEMENT", "X-Ray Bond Density Management Report", "รายการบริหารศูนย์เอ็กซ์เรย์(Bond Density)");
			labelMap.add("REPORT_XRAY_MANAGEMENT", "X-Ray Management Report", "รายการบริหารศูนย์เอ็กซ์เรย์");
			labelMap.add("REPORT_TTM_MANAGEMENT", "TTM Management Report", "รายงานรายการนวดแผนไทย");
			labelMap.add("REPORT_DOCTOR_PROFILE_GUARANTEE", "Report Doctorprofile Guarantee", "รายงาน Report Doctorprofile Guarantee");
			labelMap.add("REPORT_DOCTOR_GUARANTEE", "Report Doctor Guarantee", "รายงาน Report Doctor Guarantee");
			
			
			labelMap.add("REPORT_TTM_MANAGEMENT", "Doctor Profile Grarantee", "รายงานแพทย์การันตี");
			
            labelMap.add("SAVE_FILE", "Save as filename", "จัดเก็บไฟล์ชื่อ");
            labelMap.add("DOCUMENT_TYPE", "Document Type", "ประเภทเอกสาร");
            labelMap.add("VIEW", "View", "แสดงผล");
            labelMap.add("CLEAR_DATA","Clear data","ล้างข้อมูล");
            labelMap.add("MESSAGE", "Please Select Report Module", "กรุณาเลือกรายงาน");
            String report = "";
            request.setAttribute("labelMap", labelMap.getHashMap());
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript">
            function Report_View() {
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
                var dc = document.getElementById('block_doctor_code');
                var df = document.getElementById('block_doctor_profile_code');
                if(document.mainForm.REPORT_FILE_NAME.value == "SummaryRevenueByDoctorProfileGuarantee"){
                    df.style.display = 'block';
                }else{
                	df.style.display = 'none';
                }

                if(document.mainForm.REPORT_FILE_NAME.value == "SummaryRevenueByDoctorCodeGuarantee"){
                    dc.style.display = 'block';
                }else{
                	dc.style.display = 'none';
                } 
                if(document.mainForm.REPORT_FILE_NAME.value == "None"){
                	df.style.display = 'none';
                	dc.style.display = 'none';
                }                            
            }
                       
            /********************************************************************************************************************
             * Retreive doctor information : Begin
             */            
                        function DOCTOR_CODE_KeyPress(e) {
                            var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                            if (key == 13) {
                                document.mainForm.DOCTOR_CODE.blur();
                                return false;
                            }
                            else {
                                return true;
                            }
                        }

                        function AJAX_Refresh_DOCTOR() {
                            var target = "../../RetrieveData?A=1&TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE").toString() %>'";
                            AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
                        }
                        
                        function AJAX_Handle_Refresh_DOCTOR() {
                            if (AJAX_IsComplete()) {
                                var xmlDoc = AJAX.responseXML;
                                if (!isXMLNodeExist(xmlDoc, "CODE")) {
                                    //document.mainForm.DOCTOR_CODE.value = "";
                                    document.mainForm.DOCTOR_NAME.value = "";
                                    return;
                                }
                                
                                // Data found                                
                                document.mainForm.DOCTOR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_<%= labelMap.getFieldLangSuffix() %>");
                            }
                        }
                                    
            /********************************************************************************************************************
             * Retreive doctor information : End
             */            

             /********************************************************************************************************************
              * Retreive doctor profile information : begin
              */ 
                         function DOCTOR_PROFILE_CODE_KeyPress(e) {
                             var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                             if (key == 13) {
                                 document.mainForm.DOCTORE_PROFILE_CODE.blur();
                                 return false;
                             }
                             else {
                                 return true;
                             }
                         }
                         
                         function AJAX_Refresh_DOCTOR_PROFILE_CODE() {
                             var target = "../../RetrieveData?TABLE=DOCTOR_PROFILE&COND=CODE='" + document.mainForm.DOCTOR_PROFILE_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE").toString() %>'";
                             AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_PROFILE_CODE);
                         }
                         
                         function AJAX_Handle_Refresh_DOCTOR_PROFILE_CODE() {
                             if (AJAX_IsComplete()) {
                                                     
                                 var xmlDoc = AJAX.responseXML;

                                 // Data not found
                                 if (!isXMLNodeExist(xmlDoc, "CODE")) {
                                     //document.mainForm.DOCTOR_DEPARTMENT_CODE.value = "";
                                     document.mainForm.DOCTOR_PROFILE_NAME.value = "";
                                     return;
                                 }
                                 
                                 // Data found
                                 document.mainForm.DOCTOR_PROFILE_NAME.value = getXMLNodeValue(xmlDoc, "NAME_THAI");
                             }
                         }
					/********************************************************************************************************************
					 * Retreive doctor profile information : End
					 */                            
        </script>
    </head>
    <body leftmargin="0" onload='changeDropDownList();'>
        <form id="mainForm" name="mainForm" method="get" action="../../ViewReportSrvl">
            <center>
		<table width="800" border="0">
		<tr><td align="left">
		<b><font color='#003399'><%=Utils.getInfoPage("doctorprofile_guarantee.jsp", labelMap.getFieldLangSuffix(), new DBConnection()) %></font></b>
		</td></tr>
		</table>
            </center>
            <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
            <input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="df_guarentee"/>            
            <table class="form">
                <tr>
                    <th colspan="4">
                        <div style="float: left;">${labelMap.REPORT_TTM_MANAGEMENT}</div>                    </th>
                </tr>
                <tr>
                    <td class="label"><label for="REPORT_NAME">${labelMap.REPORT_NAME}</label>                    </td>
                    <td colspan="3" class="input">
						<select class="mediumMax" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" onchange="changeDropDownList();">
	                        <option value="None">-- Select Report --</option>
							<option value="SummaryRevenueByDoctorProfileGuarantee">${labelMap.REPORT_DOCTOR_PROFILE_GUARANTEE}</option>
							<option value="SummaryRevenueByDoctorCodeGuarantee">${labelMap.REPORT_DOCTOR_GUARANTEE}</option>
							<option value="SummaryRevenueByDoctor">${labelMap.REPORT_REVENUE}</option>							
	                    </select>
					</td>
                </tr>             
				<tr>
                    <td class="label">
                        <label>${labelMap.MM}</label>					</td>
                    <td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "MM", b.getMm())%></td>
                    <td class="label">
                         <label>${labelMap.YYYY}</label>
					</td>
                    <td class="input"><%=proUtil.selectYY("YYYY", b.getYyyy())%></td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="DOCTOR_TYPE_CODE"><span class="style1">${labelMap.DOCTOR_TYPE_CODE}</span></label>                    </td>
                    <td class="input" colspan="3">
						<select id="DOCTOR_TYPE_CODE" name="DOCTOR_TYPE_CODE" class="mediumMax">
							<option value="%">--SELECT ALL--</option>
							<option value="GDM">การันตีรายวันคิดเป็นเดือน(DLY_MLY)</option>
							<option value="GM">การันตีรายเดือน(MLY)</option>
							<option value="GMT">การันตีรายเดือนและมีค่าเวร</option>
							<option value="GD">การันตีวัน/ช.ม. (DLY)</option>
							<option value="SS">ขั้นบันได(STP)</option>
							<option value="CS">ทั่วไป(CS)</option>
							<option value="IT">แพทย์เวร</option>
							<option value="G%">แพทย์การันตี</option>
						</select>
                    </td>
                </tr>
                <tbody id='block_doctor_profile_code'>
	                <tr>
	                    <td class="label"><label for="DOCTOR_PROFILE_CODE">${labelMap.DOCTOR_PROFILE_CODE}</label></td>
	                    <td colspan="3" class="input">
	                        <input type="text" id="DOCTOR_PROFILE_CODE" name="DOCTOR_PROFILE_CODE" class="short" value="" onkeypress="return DOCTOR_PROFILE_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_PROFILE_CODE();" />
	                        <input id="SEARCH_DOCTOR_PROFILE_CODE" name="SEARCH_DOCTOR_PROFILE_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_PROFILE&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_PROFILE_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_PROFILE_CODE'); return false;" />
	                        <input type="text" id="DOCTOR_PROFILE_NAME" name="DOCTOR_PROFILE_NAME" class="mediumMax" readonly="readonly" value="" />
	                    </td>
	                </tr>                 
                </tbody>
                <tbody id='block_doctor_code'>
	                <tr>
	                    <td class="label"><label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}</label></td>
	                    <td colspan="3" class="input">
	                        <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" value="" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR();" />
	                        <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR'); return false;" />
	                        <input type="text" id="DOCTOR_NAME" name="DOCTOR_NAME" class="mediumMax" readonly="readonly" value="" />
	                    </td>
	                </tr>               
                </tbody>
                <tr>
                    <td class="label"><label for="SAVE_FILE">${labelMap.SAVE_FILE}</label></td>
                    <td class="input" colspan="3"><input type="text" class="short" id="SAVE_FILE" name="SAVE_FILE"/>
                        <select id="FILE_TYPE" name="FILE_TYPE">
                            <option value="">Select</option>
                            <option value="xls">txt</option>
                            <option value="pdf">pdf</option>
                        </select>                    </td>
                </tr>
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
