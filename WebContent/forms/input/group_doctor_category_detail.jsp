<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.Types"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="java.sql.*"%>

<%@ include file="../../_global.jsp" %>
<%
//
// Verify permission
//
//out.println("session="+session);
//out.println("page="+Guard.PAGE_INPUT_GROUP_DOCTOR_CATEGORY);

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_GROUP_DOCTOR_CATEGORY)) {
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
            labelMap.add("TITLE_MAIN", "Set Doctor Category", "Set Doctor Category");
            labelMap.add("LABEL_GROUP_CODE", "Group Code", "รหัสกลุ่ม");
            labelMap.add("LABEL_DOCTOR_CATEGORY", "Doctor Category Code", "รหัสกลุ่มแพทย์");
            labelMap.add("LABEL_DF", "DF", "DF");
            labelMap.add("LABEL_POOL", "POOL", "POOL");
            labelMap.add("LABEL_CK", "C.K.", "C.K.");
           
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            
            byte MODE = DBMgr.MODE_INSERT;
            DataRecord groupdoctorRec = null, doctorCategoryRec = null, groupRec=null;
            DataRecord stpDelete = null;
            String MM = "", YYYY = "";
            //out.println("group_code="+request.getParameter("GROUP_CODE"));
            //out.println("doctor_category_code="+request.getParameter("DOCTOR_CATEGORY_CODE"));
            
            if (request.getParameter("MODE") != null) {
                // Insert or update
                MODE = Byte.parseByte(request.getParameter("MODE"));

                groupdoctorRec = new DataRecord("STP_GROUP_DOCTOR_CATEGORY");
                stpDelete = new DataRecord("STP_GROUP_DOCTOR_CATEGORY");

                //, , , , , , , , 
                groupdoctorRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                groupdoctorRec.addField("GROUP_CODE", Types.VARCHAR, request.getParameter("GROUP_CODE"), true);
                groupdoctorRec.addField("DOCTOR_CATEGORY_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CATEGORY_CODE"),true);
                groupdoctorRec.addField("DF", Types.NUMERIC, request.getParameter("GROUP_DF"));
                groupdoctorRec.addField("POOL", Types.NUMERIC, request.getParameter("GROUP_POOL"));
                groupdoctorRec.addField("CK", Types.NUMERIC, request.getParameter("GROUP_CK"));
                                
                groupdoctorRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                groupdoctorRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                groupdoctorRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                groupdoctorRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

                if(1==1){
                	out.println(request.getParameter("GROUP_CODE"));
                	//return;
                }
                if (MODE == DBMgr.MODE_INSERT) {
                    if (DBMgr.insertRecord(groupdoctorRec)) {
                        
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/group_doctor_category_main.jsp?GROUP_CODE=%1$s", groupdoctorRec.getField("GROUP_CODE").getValue())));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } 
                else if (MODE == DBMgr.MODE_UPDATE) {
                	
                    if (DBMgr.updateRecord(groupdoctorRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/group_doctor_category_main.jsp?GROUP_CODE=%1$s", groupdoctorRec.getField("GROUP_CODE").getValue())));
                    } 
                    else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            }
            
            else if (request.getParameter("GROUP_CODE") != null && request.getParameter("DOCTOR_CATEGORY_CODE") != null ) {
                // Edit
                MODE = DBMgr.MODE_UPDATE;
                //out.println("mode="+MODE);
                //String query = String.format("SELECT CODE AS CODE,DOCTOR_CATEGORY_CODE FROM STP_GROUP WHERE HOSPITAL_CODE = '%1$s' AND GROUP_CODE = '%2$s' AND DOCTOR_CATEGORY_CODE = '%3$s' ", session.getAttribute("HOSPITAL_CODE"), request.getParameter("GROUP_CODE"), request.getParameter("DOCTOR_CATEGORY_CODE"));
                //out.println(query);
                groupRec = DBMgr.getRecord(String.format("SELECT CODE, GROUP_NAME_%1$s AS NAME FROM STP_GROUP WHERE CODE = '%2$s'", labelMap.getFieldLangSuffix(), request.getParameter("GROUP_CODE")));
                groupdoctorRec = DBMgr.getRecord(String.format("SELECT * FROM STP_GROUP_DOCTOR_CATEGORY WHERE GROUP_CODE = '%1$s' AND DOCTOR_CATEGORY_CODE='%2$S'", request.getParameter("GROUP_CODE"),request.getParameter("DOCTOR_CATEGORY_CODE")));
                doctorCategoryRec = DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION FROM DOCTOR_CATEGORY WHERE CODE = '%1$s'", request.getParameter( "DOCTOR_CATEGORY_CODE")));

                }
            else if (request.getParameter("GROUP_CODE") != null ) {
                // New
                MODE = DBMgr.MODE_INSERT;
                //MM = request.getParameter("MM");
                //YYYY = request.getParameter("YYYY");
                groupRec = DBMgr.getRecord(String.format("SELECT CODE, GROUP_NAME_%1$s AS NAME FROM STP_GROUP WHERE CODE = '%2$s'", labelMap.getFieldLangSuffix(), request.getParameter("GROUP_CODE")));
            }
            else {
                response.sendRedirect("../message.jsp");
            }
            //out.println(DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_TYPE_CODE"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript">
            
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
                var target = "../../RetrieveData?TABLE=STP_GROUP&COND=CODE='" + document.mainForm.GROUP_CODE.value +"'";
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
                    document.mainForm.GROUP_NAME.value = getXMLNodeValue(xmlDoc, "GROUP_NAME_"+labelMap.getFieldLangSuffix());
                }
            }

            //GUARANTEE_LOCATION
            //GUARANTEE_LOCATION_CODE_KeyPress
           
            function DOCTOR_CATEGORY_CODE_KeyPress(e) {

                 var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CATEGORY_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_CATEGORY() {
                var target = "../../RetrieveData?TABLE=DOCTOR_CATEGORY&COND=CODE='" + document.mainForm.DOCTOR_CATEGORY_CODE.value +"'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_CATEGORY);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_CATEGORY() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //alert("AJAX_Handle_Refresh_GUARANTEE_LOCATION");
                        document.mainForm.DOCTOR_CATEGORY_CODE.value = "";
                        document.mainForm.DOCTOR_CATEGORY_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    //alert("dd="+getXMLNodeValue(xmlDoc, "DESCRIPTION"));
                    document.mainForm.DOCTOR_CATEGORY_NAME.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }            
            
            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=STP_GROUP_DOCTOR_CATEGORY&COND=HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'"
                    //+ " AND GROUP_CODE='" + document.mainForm.GROUP_CODE.value + "'"
                    + " AND DOCTOR_CATEGORY_CODE='" + document.mainForm.DOCTOR_CATEGORY_CODE.value + "'";
                 //document.mainForm.showsql.value = target;
                //alert(type_code);
                AJAX_Request(target, AJAX_Handle_VerifyData);
            }
            
            function AJAX_Handle_VerifyData() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist1 = isXMLNodeExist(xmlDoc, "HOSPITAL_CODE");
                    //var beExist2 = isXMLNodeExist(xmlDoc, "GROUP_CODE");
                    var beExist3 = isXMLNodeExist(xmlDoc, "DOCTOR_CATEGORY_CODE");
                    if(document.mainForm.MODE.value=="<%=DBMgr.MODE_INSERT%>")
                    {
                       if (beExist1 && beExist3) 
                       {
                          alert("Doctor Category : "+document.mainForm.DOCTOR_CATEGORY_NAME.value +" is in system ");
                       }
                       else 
                       {
                       		var df=parseInt(document.mainForm.GROUP_DF.value);
                       		var pool=parseInt(document.mainForm.GROUP_POOL.value);
                       		var ck=parseInt(document.mainForm.GROUP_CK.value);
                       		var SumTotal=df+pool+ck;
                       		if(SumTotal< 100 || SumTotal>100)
                       		{
                       			alert("DF, POOL AND CK SUM PERCENT <>100");
                       		}
                       		else
                       		{
                          		document.mainForm.submit();
                          	}
                       }
                    }
                   else//MODE_UPDATE
                   {
                   		document.mainForm.submit();
                   }
                }
            }
            
            
            //  !isObjectEmptyString(document.mainForm.GUARANTEE_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
            function SAVE_Click() {
                if (!isObjectEmptyString(document.mainForm.GROUP_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.DOCTOR_CATEGORY_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.GROUP_DF, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.GROUP_POOL, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.GROUP_CK, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    isObjectValidNumber(document.mainForm.GROUP_DF, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
                    isObjectValidNumber(document.mainForm.GROUP_POOL, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
                    isObjectValidNumber(document.mainForm.GROUP_CK, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>')) {
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
    <body >
        <form id="mainForm" name="mainForm" method="post" action="group_doctor_category_detail.jsp">
           <input type="hidden" id="MODE" name="MODE" value="<%= MODE %>" />
            <table class="form">
                <tr><th colspan="2">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
				</th></tr>
                <tr>
                    <td class="label">
                        <label for="LABEL_GROUP_CODE"><span class="style1">${labelMap.LABEL_GROUP_CODE} *</span></label>                    </td>
                    <td class="input">
                        <input type="text" id="GROUP_CODE" name="GROUP_CODE" class="short" readonly="readonly" value="<%= DBMgr.getRecordValue(groupRec, "CODE") %>" />
                        <input type="text" id="GROUP_NAME" name="GROUP_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(groupRec, "NAME") %>" />                    </td>
                </tr>
               
                <tr>
                    <td class="label">
                        <label for="LABEL_DOCTOR_CATEGORY"><span class="style1">${labelMap.LABEL_DOCTOR_CATEGORY} *</span></label>                    </td>
                    <td class="input">
                        <input name="DOCTOR_CATEGORY_CODE" type="text" class="short" id="DOCTOR_CATEGORY_CODE" onblur="AJAX_Refresh_DOCTOR_CATEGORY();" onkeypress="return DOCTOR_CATEGORY_CODE_KeyPress(event);" value="<%= DBMgr.getRecordValue(groupdoctorRec, "DOCTOR_CATEGORY_CODE") %>" maxlength="20" />
                        <input id="SEARCH_DOCTOR_CATEGORY_CODE" name="SEARCH_DOCTOR_CATEGORY_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_CATEGORY&TARGET=DOCTOR_CATEGORY_CODE&DISPLAY_FIELD=DESCRIPTION&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_CATEGORY'); return false;" />
                        <input type="text" id="DOCTOR_CATEGORY_NAME" name="DOCTOR_CATEGORY_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorCategoryRec, "DESCRIPTION") %>" />                    </td>
                </tr>
                <tr>
                  <td class="label">
                    <label for="LABEL_DF"><span class="style1">${labelMap.LABEL_DF} *</span></label>                    </td>
                    <td class="input"><input name="GROUP_DF" type="text" class="short alignRight" id="GROUP_DF" value="<%= DBMgr.getRecordValue(groupdoctorRec, "DF") %>" size="5" maxlength="3" /> 
                      % </td>
                </tr>
                <tr>
                  <td class="label">
                    <label for="LABEL_POOL"><span class="style1">${labelMap.LABEL_POOL} *</span></label>                    </td>
                    <td class="input"><input name="GROUP_POOL" type="text" class="short alignRight" id="GROUP_POOL" value="<%= DBMgr.getRecordValue(groupdoctorRec, "POOL") %>" size="5" maxlength="3" />
                    %</td>
                </tr>
                <tr>
                    <td class="label">
                    <label for="LABEL_CK"><span class="style1">${labelMap.LABEL_CK} *</span></label>                    </td>
                    <td class="input"><input name="GROUP_CK" type="text" class="short alignRight" id="GROUP_CK" value="<%= DBMgr.getRecordValue(groupdoctorRec, "CK") %>" size="5" maxlength="3" />
                    %</td>
                </tr>
                <tr>
                	<td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td> 
                    <td class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(groupdoctorRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(groupdoctorRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(groupdoctorRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>                    </td>
                </tr>
                <tr>
                    <th colspan="2" class="buttonBar">                        
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click()" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.history.back()" />                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
