<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap,java.util.*" errorPage="../error.jsp"%>
<%@page import="df.jsp.Guard"%>

<%    
        if (!Guard.checkPermission(session, Guard.PAGE_INPUT_METHOD_ALLOC_ITEM_MAIN)) {
            response.sendRedirect("../message.jsp");
            return;
        }

        if (session.getAttribute("LANG_CODE") == null)
            session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
    
    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	labelMap.add("TITLE_MAIN", "Import Transaction", "นำเข้ารายการประจำวัน");
	labelMap.add("INTERFACE_PROCESS", "Interface Process", "นำเข้ารายการ");
	labelMap.add("FILE_INTERFACE", "File Interface", "ไฟล์นำเข้า");
	labelMap.add("FROM_DATE", "From Date", "ตั้งแต่วันที่");
    labelMap.add("TO_DATE", "To Date", "ถึงวันที่");
	labelMap.add("INTERFACE_TRANSACTION", "Interface DF Transaction", "นำเข้ารายการค่าแพทย์");
	labelMap.add("INTERFACE_TRANSACTION_RESULT", "Interface DF Result", "นำเข้ารายการแพทย์อ่านผล");
	labelMap.add("INTERFACE_AR_TRANSACTION", "Interface AR Transaction", "นำเข้ารายการรับชำระหนี้");
	labelMap.add("INTERFACE_GUARANTEE", "Interface Guarantee", "นำเข้ารายการรับชำระหนี้");
	labelMap.add("INTERFACE_CO", "Interface C/O", "นำเข้ารายการค่าใช้จ่าย");
	labelMap.add("INTERFACE_PATHO", "Interface Pathology", "นำเข้ารายการแลปชิ้นเนื้อ");
	labelMap.add("SAVE", "Import", "นำเข้า");
    request.setAttribute("labelMap", labelMap.getHashMap());
%>
<%	String report = ""; %>

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
		<script type="text/javascript">
			function Interface_Save() {
				if(document.mainForm.FILE_INTERFACE.value == "" || document.mainForm.INTERFACE_PROCESS.value == ""){
					alert("Please Select Process or Choose Import File");
					if(document.mainForm.INTERFACE_PROCESS.value == ""){
						document.mainForm.INTERFACE_PROCESS.focus();
					}else{
						document.mainForm.FILE_INTERFACE.focus();
					}					
				}else{
            		document.mainForm.SOURCE_FILE.value = document.mainForm.FILE_INTERFACE.value;
            		document.mainForm.submit();
				}
			}
            function changeDropDownList(){}
			
			function AJAX_Import_Transaction() {
                var target = "../../ImportFileSrvl?INTERFACE_PROCESS='"+document.mainForm.INTERFACE_PROCESS.value+"'&SOURCE_FILE='" + document.mainForm.FILE_INTERFACE.value + "'";
				AJAX_Request(target, AJAX_Handle_Refresh_Result_Message());
            }
            
            function AJAX_Handle_Refresh_Result_Message() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    //Data found
					alert(xmlDoc);
					alert(getXMLNodeValue(xmlDoc, "SUCCESS"));
                }
            }
		</script>
    </head>
	<body>
        <form id="mainForm" name="mainForm" method="post" action="../../ImportFileSrvl" target="myiframe" enctype="multipart/form-data">
        <input type="hidden" id="SOURCE_FILE" name="SOURCE_FILE"/>
		<table class="form">
                <tr>
                  <th colspan="4">
				  <div style="float: left;">${labelMap.TITLE_MAIN}</div>
				</tr>
		<tr>
          <td class="label"><label for="INTERFACE_PROCESS">${labelMap.INTERFACE_PROCESS}</label></td>
                    <td colspan="3" class="input"><select class="mediumMax" id="INTERFACE_PROCESS" name="INTERFACE_PROCESS" onchange="changeDropDownList();">
                      <option value="">-- Select --</option>
                      <option value="ImportTransaction">${labelMap.INTERFACE_TRANSACTION}</option>
                      <option value="ImportVerifyTransaction">${labelMap.INTERFACE_TRANSACTION_RESULT}</option>
                      <option value="ImportARTransaction">${labelMap.INTERFACE_AR_TRANSACTION}</option>
                      <option value="ImportGuarantee">${labelMap.INTERFACE_GUARANTEE}</option>
                      <option value="ImportExpense">${labelMap.INTERFACE_CO}</option>
                      <option value="ImportPatho">${labelMap.INTERFACE_PATHO}</option>
                    </select></td>
		</tr>
                <tr>
                    <td class="label"><label for="FILE_INTERFACE">${labelMap.FILE_INTERFACE}</label></td>
                	<td colspan="3" class="input">
						<input type="file" class="long" id="FILE_INTERFACE" name="FILE_INTERFACE">
					</td>
				</tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                    <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="Interface_Save();" />
                    <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                  	<input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />				  	</th>
                </tr>
          </table>
    </form>
    <iframe name="myiframe" src="../../ImportFileSrvl" width="0" height="0"></iframe>
	</body>
</html>
