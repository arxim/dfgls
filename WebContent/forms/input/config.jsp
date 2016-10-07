<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="java.sql.*"%>
<%
    if (session.getAttribute("LANG_CODE") == null)
        session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
    
    ProcessUtil proUtil = new ProcessUtil();
    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	labelMap.add("TITLE_MAIN", "CONFIG", "CONFIG");
	labelMap.add("PROCESS_NAME", "Export Module", "จัดทำไฟล์ข้อมูลระบบ");
    labelMap.add("IP", "IP", "ไอพี");
    
	labelMap.add("IP_INTERFACE_TRANSACTION", "File Interface DF Transaction", "ไฟล์นำเข้ารายการค่าแพทย์");
	labelMap.add("IP_INTERFACE_TRANSACTION_RESULT", "File Interface DF Result", "ไฟล์นำเข้ารายการแพทย์อ่านผล");
	labelMap.add("IP_INTERFACE_AR_TRANSACTION", "File Interface AR Transaction", "ไฟล์นำเข้ารายการรับชำระหนี้");
	labelMap.add("IP_INTERFACE_GUARANTEE", "File Interface Guarantee", "ไฟล์นำเข้ารายการรับชำระหนี้");
	labelMap.add("IP_INTERFACE_CO", "File Interface C/O", "ไฟล์นำเข้ารายการค่าใช้จ่าย");
	labelMap.add("IP_INTERFACE_PATHO", "File Interface Pathology", "ไฟล์นำเข้ารายการแลปชิ้นเนื้อ");
	labelMap.add("IP_INTERFACE_CITI_BANK", "File Interface Citibank", "ส่งออกไฟล์ Citibank");


	labelMap.add("SAVE_FILE", "Save as filename", "จัดเก็บไฟล์ชื่อ");
	labelMap.add("PAY_TYPE", "Revenue Type", "ประเภทรายได้");
	labelMap.add("VIEW", "View", "แสดงผล");
	labelMap.add("PAID_DF", "Doctorfee", "ค่าส่วนแบ่งแพทย์");
	labelMap.add("PAID_SALARY", "Salary", "ค่าเงินเดือน");
	String report = "";
    request.setAttribute("labelMap", labelMap.getHashMap());


    DBConnection conUpdate;
    String name[] = request.getParameterValues("NAME");
    String value[] = request.getParameterValues("VALUE");
    if(name!=null){
        String SQLCommand = "DELETE CONFIG WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"'";
        for(int i=0; i < name.length ; i++){
           SQLCommand += "insert CONFIG(NAME, VALUE, HOSPITAL_CODE) values('"+ name[i] +"','"+ value[i] +"', '"+ session.getAttribute("HOSPITAL_CODE").toString() +"'); ";
        }
        conUpdate = new DBConnection();
        conUpdate.connectToLocal();
        conUpdate.executeUpdate(SQLCommand);
        conUpdate.Close();
        //out.println(SQLCommand);
    }else{
        //out.println(" name is  null");
    }
   
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
			function Report_Save() {
			//document.mainForm.REPORT_DISPLAY.value = "save";
			alert("SAVE");
            document.mainForm.target = "_blank";
			document.mainForm.submit();
			}
			function changeDropDownList(){
				if(document.mainForm.PROCESS_NAME.value == "%"){
					document.mainForm.PAY_TYPE.disabled = false;
				}
				if(document.mainForm.PROCESS_NAME.value == "ExportBank"){
					document.mainForm.PAY_TYPE.disabled = false;
				}
				if(document.mainForm.PROCESS_NAME.value == "ExportAP"){
					document.mainForm.PAY_TYPE.disabled = true;
				}
				if(document.mainForm.PROCESS_NAME.value == "ExportPayroll"){
					document.mainForm.PAY_TYPE.disabled = true;
				}
			}
		</script>
    </head>
	<body>
        <form name="mainForm" method="post">
       	<input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
		<input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="checklist"/>        
		<table class="form">
                <tr>
                	<th colspan="2"><div style="float: left;">${labelMap.TITLE_MAIN}</div></th>
                </tr>
                <%
                    String disable = "disabled";
                    DBConnection con = new DBConnection();
                    con.connectToLocal();
                    //con.connectToServer();
                    
                    String sqlCommand = "select * from CONFIG WHERE HOSPITAL_CODE = '"+ session.getAttribute("HOSPITAL_CODE").toString() +"'";
                    ResultSet rs = con.executeQuery(sqlCommand);
                    int i = 0;
                    while (rs.next()) {
                        %>
                        <tr>
                            <td class="label" style='text-align:left;'>
                                <label for="aText"><%= Util.formatHTMLString(rs.getString("NAME"), true)%><input type="hidden" name="NAME" value="<%= Util.formatHTMLString(rs.getString("NAME"), true)%>"/></label>
                            </td>
                            <td class="input">
                                <input type="text" class="long" name="VALUE" value="<%= Util.formatHTMLString(rs.getString("VALUE"), true)%>"/>
                            </td>
                        </tr>
                        <%
                        i++;
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    con.Close();
                    disable = "";
                %>
                <tr>
                    <th colspan="2" class="buttonBar">
                        <input type="submit" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}"/>
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
          </table>
    </form>
	</body>
</html>