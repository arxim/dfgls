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
            labelMap.add("TITLE_MAIN", "Management Revenue Report", "รายงานรายการบริหารรายได้แพทย์");
            labelMap.add("REPORT_NAME", "Report Name", "ชื่อรายงาน");
            labelMap.add("LABEL_GROUP", "Group Code", "กลุ่ม");
 			labelMap.add("MM", "Month", "เดือน");
			labelMap.add("YYYY", "Year", "ปี");			
		
            labelMap.add("SAVE_FILE", "Save as filename", "จัดเก็บไฟล์ชื่อ");
            labelMap.add("DOCUMENT_TYPE", "Document Type", "ประเภทเอกสาร");
            labelMap.add("VIEW", "View", "แสดงผล");
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
            function Report_View() 
            {
                
					if(document.mainForm.MM.value == "" || document.mainForm.YYYY.value == "")
					{
						alert("Please Select Month/Year");
						if(document.mainForm.MM.value == "")
						{
							document.mainForm.MM.focus();
						}
						else
						{
							document.mainForm.YYYY.focus();
						}
					}
					else
					{
						document.mainForm.REPORT_DISPLAY.value = "view";
                    	document.mainForm.target = "_blank";
                    	document.mainForm.submit();
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
            
            function GROUP_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.GROUP_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_GROUP() {
                var target = "../../RetrieveData?A=1&TABLE=STP_GROUP&COND=CODE='" + document.mainForm.GROUP_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_GROUP);
            }
            
            function AJAX_Handle_Refresh_GROUP() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.GROUP_CODE.value = "";
                        document.mainForm.GROUP_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.GROUP_NAME.value = getXMLNodeValue(xmlDoc, "GROUP_NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
                        

        </script>
    </head>
    <body leftmargin="0" >
        <form id="mainForm" name="mainForm" method="get" action="../../ViewReportSrvl">
            <center>
		<table width="800" border="0">
		<tr><td align="left">
		<b><font color='#003399'><%=Utils.getInfoPage("management_revenue.jsp", labelMap.getFieldLangSuffix(), new DBConnection()) %></font></b>
		</td></tr>
		</table>
            </center>
            <table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY" />
                <input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="management_revenue"/>
                <input type="hidden" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" value="GroupRevenue"/>
                <tr>
                    <th colspan="4">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>                    </th>
                </tr>
                
				<tr>
                    <td class="label">
                        <label>${labelMap.MM}</label>					</td>
                    <td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "MM", b.getMm())%></td>
                    <td class="label">
                         <label>${labelMap.YYYY}</label>					</td>
                    <td class="input"><%=proUtil.selectYY("YYYY", b.getYyyy())%></td>
                </tr>
                    <tr>
	                    <td class="label"><label for="LABEL_GROUP">${labelMap.LABEL_GROUP}</label></td>
	                    <td colspan="3" class="input">
	                        <input type="text" id="GROUP_CODE" name="GROUP_CODE" class="short" value="" onkeypress="return GROUP_CODE_KeyPress(event);" onblur="AJAX_Refresh_GROUP();" />
	                        <input id="SEARCH_GROUP_CODE_FROM" name="SEARCH_GROUP_CODE_FROM" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=STP_GROUP&DISPLAY_FIELD=GROUP_NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=GROUP_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_GROUP'); return false;" />
	                        <input type="text" id="GROUP_NAME" name="GROUP_NAME" class="mediumMax" readonly="readonly" value="" />                    </td>
	                </tr>
               
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="VIEW" name="VIEW" class="button" value="${labelMap.VIEW}" onclick="Report_View();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />					</th>
                </tr>
            </table>
        </form>
    </body>
</html>
