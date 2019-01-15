<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.table.Batch"%>

<%  if (session.getAttribute("LANG_CODE") == null)
        session.setAttribute("LANG_CODE", LabelMap.LANG_EN);

	ProcessUtil proUtil = new ProcessUtil();
	DBConnection c = new DBConnection();
	c.connectToLocal();
	Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
	c.Close();
    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	labelMap.add("TITLE_MAIN", "Verify New Master Data", "รายงานตรวจสอบการนำเข้าข้อมูลพื้นฐาน");
	labelMap.add("REPORT_NAME", "Report Name", "ชื่อรายงาน");
	labelMap.add("REPORT_DOCTOR", "New Doctor Code", "นำเข้ารหัสแพทย์ใหม่");
	labelMap.add("REPORT_UPDATE_DOCTOR", "Update Doctor Info.", "แก้ไขข้อมูลแพทย์");
	labelMap.add("REPORT_ORDER_ITEM", "New Order Item", "นำเข้ารายการรักษาใหม่");
	labelMap.add("REPORT_ORDER_ITEM_INACTIVE", "Order Item Inactive", "รายการรักษาที่ Inactive แล้วในระบบ");
	labelMap.add("REPORT_PAYOR", "New Payor Office", "นำเข้าบริษัทคู่สัญญาใหม่");
	labelMap.add("SAVE_FILE", "Save as filename", "จัดเก็บไฟล์ชื่อ");
	labelMap.add("VIEW", "View", "แสดงผล");
	labelMap.add("MM", "Month", "เดือน");
	labelMap.add("YYYY", "Year", "ปี");
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
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
		<script type="text/javascript">
            function Report_View() {
			    if(document.mainForm.REPORT_FILE_NAME.value == "None"){
                    alert("Please Select Report");
                    document.mainForm.REPORT_FILE_NAME.focus();
                }else{
					document.mainForm.REPORT_DISPLAY.value = "view";
					document.mainForm.target = "_blank";
					document.mainForm.submit();
            	}
			}
			function Report_Save() {
				if(document.mainForm.REPORT_FILE_NAME.value == "None"){
                    alert("Please Select Report");
                    document.mainForm.REPORT_FILE_NAME.focus();
                }else{
					document.mainForm.REPORT_DISPLAY.value = "save";
					document.mainForm.target = "_blank";
					document.mainForm.submit();
				}
			}
		</script>
    </head>
	<body>
        
        <form id="mainForm" name="mainForm" method="get" action="../../ViewReportSrvl">
        <center>
		<table width="800" border="0">
		<tr><td align="left">
		<b><font color='#003399'><%=Utils.getInfoPage("verify_new_master_report.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
		</td></tr>
		</table>
        </center>
		<table class="form">
                <input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
				<input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="verify"/>
                <tr>
                  <th colspan="4">
				  <div style="float: left;">${labelMap.TITLE_MAIN}</div>
				  </th>
                </tr>
				<tr>
                    <td class="label"><label for="REPORT_NAME">${labelMap.REPORT_NAME}</label>
					</td>
                    <td colspan="3" class="input">
						<select class="medium" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME">
	                    	<option value="None">-- Select Report --</option>
	                        <option value="VerifyNewDoctor">${labelMap.REPORT_DOCTOR}</option>
	                        <option value="VerifyUpdateDoctor">${labelMap.REPORT_UPDATE_DOCTOR}</option>
	                        <option value="VerifyNewOrderItem">${labelMap.REPORT_ORDER_ITEM}</option>
	                        <option value="VerifyOrderItemInactive">${labelMap.REPORT_ORDER_ITEM_INACTIVE}</option>
	                        <option value="VerifyNewPayorOffice">${labelMap.REPORT_PAYOR}</option>
	                    </select>
					</td>
				</tr>
				<tbody id='term'>
				<tr>
                    <td class="label"><label>${labelMap.MM}</label></td>
                    <td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "MM", b.getMm())%></td>
                    <td class="label"><label>${labelMap.YYYY}</label></td>
                    <td class="input"><%=proUtil.selectYY("YYYY", b.getYyyy())%></td>
                </tr>
                </tbody>                
                <tr>
                    <td class="label"><label for="SAVE_FILE">${labelMap.SAVE_FILE}</label></td>
                    <td colspan="3" class="input"><input type="text" class="medium" id="SAVE_FILE" name="SAVE_FILE"/>
                        <select id="FILE_TYPE" name="FILE_TYPE">
                            <option value="all">all</option>
                            <option value="xls">xls</option>
                            <option value="pdf">pdf</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
					<input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="Report_Save();" />
                    <input type="button" id="VIEW" name="VIEW" class="button" value="${labelMap.VIEW}" onclick="Report_View();" />
                    <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                  	<input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" /> 
				  </th>
                </tr>
          </table>
    </form>
	</body>
</html>
