<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap,java.util.*" errorPage="../error.jsp"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
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
	labelMap.add("TITLE_MAIN", "Interface Transaction", "นำเข้ารายการประจำวัน");
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
	labelMap.add("INTERFACE_EXPENSE", "File Excel Expense", "นำเข้ารายการค่าใช้จ่ายจากไฟล์ Excel");
	labelMap.add("INTERFACE_TIME_TABLE", "File Excel Time Table", "นำเข้ารายการตารางเวลาแพทย์");
	labelMap.add("INTERFACE_ON_WARD", "Interface On Ward", "Interface On Ward");
	labelMap.add("SAVE", "Import", "นำเข้า");
    request.setAttribute("labelMap", labelMap.getHashMap());
    
        DBConnection conn = new DBConnection();
        conn.connectToLocal();
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
                document.mainForm.submit();
                return true;
                /*
				if(document.mainForm.FILE_INTERFACE.value == "" || document.mainForm.INTERFACE_PROCESS.value == ""){
					alert("Please Select Process or Choose Import File");
                    return false;
					if(document.mainForm.INTERFACE_PROCESS.value == ""){
						document.mainForm.INTERFACE_PROCESS.focus();
					}else{
						document.mainForm.FILE_INTERFACE.focus();
					}					
				}else{
            		document.mainForm.SOURCE_FILE.value = document.mainForm.FILE_INTERFACE.value;
                    return false;
            		document.mainForm.submit();
				}
                */
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
				/*
				if(document.mainForm.INTERFACE_PROCESS.value=='ImportExpenseExcel'){
                    e.style.display = 'none';
                    f.style.display = 'block';
                    g.style.display = 'none';
                }else if(document.mainForm.INTERFACE_PROCESS.value=='ImportTimeTable'){
                    e.style.display = 'none';
                    f.style.display = 'none';
                    g.style.display = 'block';
            	}else{
                	e.style.display = 'block';
                	f.style.display = 'none';
                    g.style.display = 'none';
                }
                */
			}
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
	<body onload="changeDropDownList();">
        <form id="mainForm" name="mainForm" method="post" action="../../ImportFileSrvl" target="myiframe" enctype="multipart/form-data">
        <input type="hidden" id="SOURCE_FILE" name="SOURCE_FILE"/>
        <center>
			<table width="800" border="0">
				<tr><td align="left">
				<b><font color='#003399'><%=Utils.getInfoPage("trakcare_import.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
				</td></tr>
			</table>
        </center>
		<table class="form">
                <tr>
                  <th colspan="4">
			    <div style="float: left;">${labelMap.TITLE_MAIN}</div>				</tr>
		        <tr>
		          <td class="label"><label for="INTERFACE_PROCESS">${labelMap.INTERFACE_PROCESS}</label></td>
		          <td colspan="3" class="input"><select class="mediumMax" id="INTERFACE_PROCESS" name="INTERFACE_PROCESS" onchange="changeDropDownList();">
                    <option value="">-- Select --</option>
                    <option value="ImportTransaction">${labelMap.INTERFACE_TRANSACTION}</option>
                    <option value="ImportVerifyTransaction">${labelMap.INTERFACE_TRANSACTION_RESULT}</option>
                    <option value="ImportOnWard">${labelMap.INTERFACE_ON_WARD}</option>
                    <option value="ImportARTransaction">${labelMap.INTERFACE_AR_TRANSACTION}</option>
                    <option value="ImportExpense">${labelMap.INTERFACE_CO}</option>
                    <option value="ImportExpenseExcel">${labelMap.INTERFACE_EXPENSE}</option>
                    <option value="ImportTimeTable">${labelMap.INTERFACE_TIME_TABLE}</option>
                    <option value="ImportTimeTable">${labelMap.INTERFACE_TIME_TABLE}</option>
                  </select></td>
        </tr>
		<tbody id='download_excel'>
        	<tr>
       			<td align="right" class="label"><strong> Expense Template </strong></td>
          		<td colspan="3" valign="middle" class="input"><a href="../../templete_expense.xls"><img src="../../images/xls_icon.gif" width="25" height="25" border="0" /></a> Click icon to download </td>
			</tr>
		</tbody>
		<tbody id='download_time_table'>
        	<tr>
       			<td align="right" class="label"><strong> Expense Template </strong></td>
          		<td colspan="3" valign="middle" class="input"><a href="../../time_table.xls"><img src="../../images/xls_icon.gif" width="25" height="25" border="0" /></a> Click icon to download </td>
			</tr>
		</tbody>
        <tr>
            <td class="label" rowspan="2"><label for="FILE_INTERFACE">${labelMap.FILE_INTERFACE}</label></td>
            <td colspan="3" class="input">
                <input type="radio" id="selectFileType" name="selectFileType" value="file" onclick="functionType(this);" checked>
                <input type="file" class="long" id="FILE_INTERFACE" name="FILE_INTERFACE">
            </td>
        </tr>
        <tbody id='excel'>
        <tr>
        	<td class="label" rowspan="2"></td>
            <td colspan="3" class="input">
                <input type="radio" id="selectFileType" name="selectFileType" value="date" onclick="functionType(this);">
                <input name="INTERFACE_DATE" type="text" class="short" id="INTERFACE_DATE" maxlength="10" value="<%=JDate.showDate(JDate.getDate()) %>" />
                <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('INTERFACE_DATE'); return false;" />
            </td>
        </tr>
        </tbody>
        <tr>
            <th colspan="4" class="buttonBar">
            <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="Interface_Save();" />
            <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
            <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />				  	</th>
        </tr>
    </table>
    </form>
    <iframe id="myiframe" name="myiframe" src="../../ImportFileSrvl" width="0" height="0"></iframe>
	</body>
</html>
<script language="javascript">
    function functionType(ck){
        var e = document.getElementById('FILE_INTERFACE');
        var d = document.getElementById('INTERFACE_DATE');
        var s = ck;
        //alert(s.value);
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
