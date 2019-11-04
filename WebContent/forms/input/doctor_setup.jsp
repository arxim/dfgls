<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.obj.util.Utils "%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.Types"%>

<%@ include file="../../_global.jsp" %>

<%
			// Short Page Profile Doctor
			// Verify permission
			//

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_DOCTOR_PROFILE)) {
                response.sendRedirect("../message.jsp");
                return;
            }

			//
			// Initial LabelMap
			//

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
			
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Doctor Profile", "ประวัติแพทย์");
            labelMap.add("CODE", "Profile Code", "รหัสประวัติแพทย์");
            labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("EMPLOYEE_ID", "Employee ID", "รหัสพนักงาน");
            labelMap.add("NAME_THAI", "Name (Thai)", "ชื่อ (ไทย)");
            labelMap.add("NAME_ENG", "Name (Eng)", "ชื่อ (อังกฤษ)");
            labelMap.add("DOCTOR_NAME","Doctor Name","ชื่อแพทย์");
            labelMap.add("BIRTH_DATE", "Birth Date", "เกิดวันที่");
            labelMap.add("FROM_DATE", "Start Working Date", "เริ่มงานวันที่");
            labelMap.add("TO_DATE", "To Date", "จนถึงวันที่");
            labelMap.add("NATION_ID", "Nation ID", "รหัสประจำตัวประชาชน");
            labelMap.add("TELEPHONE", "Phone No.", "หมายเลขโทรศัพท์");
            labelMap.add("MEMO", "Memo", "หมายเหตุ");
            labelMap.add("SUBTITLE_ADDRESS", "Address", "ที่อยู่");
            labelMap.add("ZIP", "Zip", "รหัสไปรษณีย์");
            labelMap.add("PLACE_OF_WORK", "Place of Work", "สถานที่ทำงาน");
            labelMap.add("SUBTITLE_CURRENT_POSITION", "Position", "รายละเอียดตำแหน่งงาน");
            labelMap.add("DEPARTMENT_CODE", "Department Code", "รหัสแผนก");
            labelMap.add("POSITION", "Position", "ตำแหน่ง");
            labelMap.add("BRANCH", "Branch", "แพทย์สาขา");
            labelMap.add("SUB_BRANCH", "Sub Branch", "สาขาเฉพาะทาง");
            labelMap.add("LICENSE_ID", "License ID", "เลขที่ใบอนุญาต");
            labelMap.add("LICENSE_EXPIRE_DATE", "License Expire Date", "วันที่หมดอายุ");
            labelMap.add("SUBTITLE_CURRENT_JOB", "Current Job", "งานปัจจุบัน");
            labelMap.add("UNDER_HOSPITAL", "Hospital", "สังกัดโรงพยาบาล");
            labelMap.add("UNDER_HOSPITAL_BRANCH", "Branch", "สาขา");
            labelMap.add("SPECIALTY", "Specialty", "แพทย์สาขา");
            labelMap.add("SUB_SPECIALTY", "Sub specialty", "สาขาเฉพาะทาง");
            labelMap.add("SUBTITLE_INSURANCE", "Insurance", "การประกัน");
            labelMap.add("INSURRANCE_NAME", "Company", "บริษัท");
            labelMap.add("INSURRANCE_NO", "Insurrance No", "กรมธรรม์เลขที่");
            labelMap.add("INSURRANCE_YEAR", "Insurrance year", "ระยะเวลาคุ้มครอง");
            labelMap.add("INSURRANCE_MONEY", "Insurrance amount", "วงเงินคุ้มครอง");
            labelMap.add("SUBTITLE_WORKING_HISTORY", "Working History", "ประวัติการทำงาน");
            labelMap.add("HP_REF_NAME_01", "Hospital", "โรงพยาบาล");
            labelMap.add("HP_REF_BRANCH_01", "Branch", "สาขา");
            labelMap.add("HP_REF_YEAR_01", "Duration", "ระยะเวลา");
            labelMap.add("HP_REF_NAME_02", "Hospital", "โรงพยาบาล");
            labelMap.add("HP_REF_BRANCH_02", "Branch", "สาขา");
            labelMap.add("HP_REF_YEAR_02", "Duration", "ระยะเวลา");
            labelMap.add("SUBTITLE_DOCTOR_DETAIL", "Doctor Details", "รายละเอียดแพทย์");
            labelMap.add("DOCTOR_CATEGORY", "Category", "ประเภทเงื่อนไข");
            labelMap.add("BANK_ACCOUNT_NO", "Bank Account No.", "เลขที่บัญชีธนาคาร");
            labelMap.add("BANK", "Bank", "ธนาคาร");

            request.setAttribute("labelMap", labelMap.getHashMap());
			
			//
			// Process request
			//

            request.setCharacterEncoding("UTF-8");
            DataRecord doctorProfileRec = null, departmentRec = null ,  doctorProfileOld  = null  , doctorProfileLog = null ;
            byte MODE = DBMgr.MODE_INSERT;

            if (request.getParameter("MODE") != null) {
                MODE = Byte.parseByte(request.getParameter("MODE"));
                doctorProfileRec = new DataRecord("DOCTOR_PROFILE");

                doctorProfileRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                doctorProfileRec.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                doctorProfileRec.addField("NAME_THAI", Types.VARCHAR, request.getParameter("NAME_THAI"));
                doctorProfileRec.addField("NAME_ENG", Types.VARCHAR, request.getParameter("NAME_ENG"));
                doctorProfileRec.addField("EMPLOYEE_ID", Types.VARCHAR, request.getParameter("EMPLOYEE_ID"));
                //doctorProfileRec.addField("BIRTH_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("BIRTH_DATE")));
                doctorProfileRec.addField("TELEPHONE", Types.VARCHAR, request.getParameter("TELEPHONE"));
                doctorProfileRec.addField("NATION_ID", Types.VARCHAR, request.getParameter("NATION_ID"));
                doctorProfileRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                doctorProfileRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                doctorProfileRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                doctorProfileRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

                if (MODE == DBMgr.MODE_INSERT) {

                    if (DBMgr.insertRecord(doctorProfileRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/doctor_setup.jsp?CODE=" + doctorProfileRec.getField("CODE").getValue()));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                    
                } else if (MODE == DBMgr.MODE_UPDATE) {
                	
                	/**
                	*    Set condition update data log 
                	**/
                	
                	doctorProfileOld = DBMgr.getRecord("SELECT * FROM DOCTOR_PROFILE WHERE CODE = '" +  request.getParameter("CODE") + "' AND HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' ");
                 	doctorProfileLog = new DataRecord("DOCTOR_PROFILE_LOG");
                 	
                 	doctorProfileLog.addField("HOSPITAL_CODE", Types.VARCHAR, DBMgr.getRecordValue(doctorProfileOld, "HOSPITAL_CODE"), true);
                 	doctorProfileLog.addField("CODE", Types.VARCHAR , DBMgr.getRecordValue(doctorProfileOld, "CODE"), true);
                 	doctorProfileLog.addField("NAME_THAI", Types.VARCHAR, DBMgr.getRecordValue(doctorProfileOld, "NAME_THAI"));
                 	doctorProfileLog.addField("NAME_ENG", Types.VARCHAR, DBMgr.getRecordValue(doctorProfileOld, "NAME_ENG"));
                 	doctorProfileLog.addField("EMPLOYEE_ID", Types.VARCHAR , DBMgr.getRecordValue(doctorProfileOld, "EMPLOYEE_ID"));
                 	doctorProfileLog.addField("TELEPHONE", Types.VARCHAR, DBMgr.getRecordValue(doctorProfileOld, "TELEPHONE"));
                 	doctorProfileLog.addField("NATION_ID", Types.VARCHAR, DBMgr.getRecordValue(doctorProfileOld,"NATION_ID"));
                 	doctorProfileLog.addField("ACTIVE", Types.VARCHAR, DBMgr.getRecordValue(doctorProfileOld, "ACTIVE"));
                 	doctorProfileLog.addField("UPDATE_DATE", Types.VARCHAR, DBMgr.getRecordValue(doctorProfileOld, "UPDATE_DATE"));
                 	doctorProfileLog.addField("UPDATE_TIME", Types.VARCHAR, DBMgr.getRecordValue(doctorProfileOld, "UPDATE_DATE"));
                 	doctorProfileLog.addField("USER_ID", Types.VARCHAR, DBMgr.getRecordValue(doctorProfileOld, "USER_ID"));
                	
                    if (DBMgr.updateRecord(doctorProfileRec)) {
                    	/**
                    	**  Set data  back up data
                    	**/
                    	
                    	if(!DBMgr.isExist(doctorProfileLog)){ 
                    		DBMgr.insertRecord(doctorProfileLog);
                    	} else { 
                    		DBMgr.updateRecord(doctorProfileLog);
                    	}
                    	
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/doctor_setup.jsp?CODE=" + doctorProfileRec.getField("CODE").getValue()));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            } 
            else if (request.getParameter("CODE") != null) {
                doctorProfileRec = DBMgr.getRecord("SELECT * FROM DOCTOR_PROFILE WHERE CODE = '" + request.getParameter("CODE") + 
                		"' AND HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"'");
                
                if (doctorProfileRec == null) {
                    MODE = DBMgr.MODE_INSERT;
                } else {
                    MODE = DBMgr.MODE_UPDATE;
                    departmentRec = DBMgr.getRecord("SELECT CODE, DESCRIPTION FROM DEPARTMENT WHERE CODE = '" + DBMgr.getRecordValue(doctorProfileRec, "DEPARTMENT_CODE") + "'");
                }
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
        <script type="text/javascript">
            
            function CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    Refresh_DOCTOR_PROFILE();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function Refresh_DOCTOR_PROFILE() {
                var to = document.location.pathname.lastIndexOf('?');
                if (to < 0) {
                    window.location = document.location.pathname + '?CODE=' + document.mainForm.CODE.value;
                }
                else {
                    window.location = document.location.pathname.substr(0, to) + '?CODE=' + document.mainForm.CODE.value;
                }
            }

            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=DOCTOR_PROFILE&COND=CODE='" + document.mainForm.CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE").toString()%>'";
                AJAX_Request(target, AJAX_Handle_VerifyData);
            }
            
            function AJAX_Handle_VerifyData() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist = isXMLNodeExist(xmlDoc, "CODE");
                    
                    switch (document.mainForm.MODE.value) {
                        case "<%=DBMgr.MODE_INSERT%>" :
                            if (beExist) {
                                if (confirm("<%=labelMap.get("CONFIRM_REPLACE_DATA")%>")) {
                                document.mainForm.MODE.value= "<%=DBMgr.MODE_UPDATE%>";
                                document.mainForm.submit();
                            }
                        }else {
                            document.mainForm.submit();
                        }
                        break;
                    case "<%=DBMgr.MODE_UPDATE%>" :
                        if (beExist) {
                            document.mainForm.submit();
                        }else {
                            alert("<%=labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)%>");
                        }
                        break;
                    }
                }
            }

            function SAVE_Click() {
                if (!isObjectEmptyString(document.mainForm.CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.NAME_ENG, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.NAME_THAI, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                AJAX_VerifyData();
            }
        }
        </script>
        <style type="text/css">
			<!--
			.style1 {color: #003399}
			-->
        </style>
</head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="doctor_setup.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= MODE%>" />
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                        <b><font color='#003399'><%=Utils.getInfoPage("doctor_setup.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                <th colspan="4" class="buttonBar">     
                		<div style="float: left;">${labelMap.TITLE_MAIN}</div> 
               	</th>
				</tr>
                <tr>
                  	<td class="label">
                    <label for="CODE"><span class="style1">${labelMap.DOCTOR_PROFILE_CODE}*</span></label>
                    </td>
                    <td class="input">
                        <input type="text" id="CODE" name="CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(doctorProfileRec, "CODE")%>" onkeypress="return CODE_KeyPress(event);"<%= MODE == DBMgr.MODE_UPDATE ? " readonly=\"readonly\"" : ""%> />
                        <input id="SEARCH_CODE" name="SEARCH_CODE" type="image" class="image_button" src="../../images/search_button_profile.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_PROFILE&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&BEINSIDEHOSPITAL=1&TARGET=CODE&HANDLE=Refresh_DOCTOR_PROFILE'); return false;" />
                        <input id="SEARCH_CODE_SUB" name="SEARCH_CODE_SUB" type="image" class="image_button" src="../../images/search_button_doctor.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&RETURN_FIELD=DOCTOR_PROFILE_CODE&DISPLAY_SUB_CODE=CODE&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&BEINSIDEHOSPITAL=1&TARGET=CODE&HANDLE=Refresh_DOCTOR_PROFILE'); return false;" />
                    </td>
                    <td class="label">
                    <label for="ACTIVE_1"><span class="style1">${labelMap.ACTIVE}*</span></label>                    </td>
                    <td class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(doctorProfileRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(doctorProfileRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
                               <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(doctorProfileRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> />
                               <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="EMPLOYEE_ID">${labelMap.EMPLOYEE_ID}</label>
                    </td>
                    <td class="input">
                        <input type="text" id="EMPLOYEE_ID" name="EMPLOYEE_ID" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(doctorProfileRec, "EMPLOYEE_ID")%>" />
                    </td>
                    <td class="label">
                        <label for="NATION_ID">${labelMap.NATION_ID}</label>
                    </td>
                    <td class="input">
                        <input type="text" id="NATION_ID" name="NATION_ID" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(doctorProfileRec, "NATION_ID")%>" />
                    </td>
                    
                </tr>
                <tr>
                  <td class="label">
                    <label for="NAME_ENG"><span class="style1">${labelMap.NAME_ENG}*</span></label>                    </td>
                    <td class="input" colspan="3">
                        <input type="text" id="NAME_ENG" name="NAME_ENG" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(doctorProfileRec, "NAME_ENG")%>" />
                    </td>
                </tr>
                <tr>
                  <td class="label">
                    <label for="NAME_THAI"><span class="style1">${labelMap.NAME_THAI}*</span></label>                    </td>
                    <td class="input" colspan="3">
                        <input type="text" id="NAME_THAI" name="NAME_THAI" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(doctorProfileRec, "NAME_THAI")%>" />
                    </td>
                </tr>
                
                <tr>
                  <td class="label">
                    <label for="TELEPHONE"><span class="style1">${labelMap.TELEPHONE}*</span></label>
                  </td>
                  <td class="input" colspan="3">
                        <input type="text" id="TELEPHONE" name="TELEPHONE" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(doctorProfileRec, "TELEPHONE")%>" />
                  </td>
                </tr>
                
                
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click()" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='doctor_setup.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
            <hr />
            <table class="data">
                <tr>
                    <th colspan="7" class="alignLeft">${labelMap.SUBTITLE_DOCTOR_DETAIL}</th>
                </tr>
                <tr>
                    <td class="sub_head">${labelMap.DOCTOR_CODE}</td>
                    <td class="sub_head">${labelMap.DOCTOR_NAME}</td>
                    <td class="sub_head">${labelMap.DOCTOR_CATEGORY}</td>
                    <td class="sub_head">${labelMap.BANK_ACCOUNT_NO}</td>
                    <td class="sub_head">${labelMap.BANK}</td>
                    <td class="sub_head">${labelMap.ACTIVE}</td>
                    <td class="sub_head"><%=labelMap.get(LabelMap.EDIT)%></td>
                </tr>
                <%

            DBConnection con = new DBConnection();
            con.connectToLocal();
            ResultSet rs = con.executeQuery("SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME, DOCTOR_CATEGORY_CODE, BANK_ACCOUNT_NO, ACTIVE , (SELECT DESCRIPTION_" + labelMap.getFieldLangSuffix() + " FROM BANK WHERE CODE=D.BANK_CODE AND COUNTRY_CODE = D.BANK_COUNTRY_CODE) AS 'BANK_NAME' FROM DOCTOR AS D WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND DOCTOR_PROFILE_CODE = '" + DBMgr.getRecordValue(doctorProfileRec, "CODE") + "' ORDER BY ACTIVE DESC");
            int i = 0;
            String activeIcon, linkEdit;
            while (rs.next()) {
                activeIcon = "<img src=\"../../images/" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? "" : "in") + "active_icon.png\" alt=\"" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? labelMap.get(LabelMap.ACTIVE_1) : labelMap.get(LabelMap.ACTIVE_0)) + "\" />";
                linkEdit = "<a href=\"doctor_detail.jsp?DOCTOR_PROFILE_CODE=" + DBMgr.getRecordValue(doctorProfileRec, "CODE") + "&CODE=" + rs.getString("CODE") + "\" title=\"" + labelMap.get(LabelMap.EDIT) + "\"><img src=\"../../images/edit_button.png\" alt=\"Edit\" /></a>";
                %>                
                <tr>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("CODE"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("NAME"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("DOCTOR_CATEGORY_CODE"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("BANK_ACCOUNT_NO"), true)%></td>
                    <td class="row<%=i % 2%>"><%= Util.formatHTMLString(rs.getString("BANK_NAME"), true)%></td>
                    <td class="row<%=i % 2%>" align="center"><%= activeIcon %></td>                    
                    <td class="row<%=i % 2%>" align="center"><%= linkEdit%></td>
                </tr>
                <%
                i++;
            }
            if (rs != null) {
                rs.close();
            }
            con.Close();
                %>                
                <tr>
                    <th colspan="7" class="buttonBar">
                        <input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}"<%= MODE == DBMgr.MODE_UPDATE ? "" : " disabled=\"disabled\""%> onclick="window.location = 'doctor_detail.jsp?DOCTOR_PROFILE_CODE=' + document.mainForm.CODE.value;" />
                    </th>
                </tr>
            </table>
        </form>
      </body>
</html>
