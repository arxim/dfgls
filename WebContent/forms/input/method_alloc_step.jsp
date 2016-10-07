<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>

<%@page import="java.sql.Types"%>

<%@ include file="../../_global.jsp" %>

<%
			//
			// Verify permission
			//

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_ORDER_ITEM)) {
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
            labelMap.add("TITLE_MAIN", "Setup Step Allocate", "Setup Step Allocate");
            labelMap.add("STEP_ID", "STEP CODE", "STEP CODE");
            labelMap.add("STEP_SEQ" , "STEP SEQ." , "STEP SEQ." );
            labelMap.add("ADMISSION_TYPE_CODE", "Admission Type" , "Admission Type");
            labelMap.add("TAX_TYPE_CODE" , " Tax Type" , "Tax Type");
            labelMap.add("TAX_RATE" , "Tax Rate" , "Tax Rate.");
            labelMap.add("AMOUNT_START" , "Amount Start" , "Amount Start");
            labelMap.add("AMOUNT_END"  , "Amount End" , "Amount End");
            labelMap.add("ALLOCATE_PCT"  , "Allocate Pct." ,  "Allocate Pct.");
            request.setAttribute("labelMap", labelMap.getHashMap());

			//
			// Process request
			//

            request.setCharacterEncoding("UTF-8");
            DataRecord stepAllocateRecord = null;
            DataRecord doctorCategory = null;
            DataRecord maxValueOnStep = null;
            
            
            byte MODE = DBMgr.MODE_INSERT;
			String getcode = "";
			String getDescription = "";
			String codescript = "";
			String stepSeq = "";
			
            if (request.getParameter("MODE") != null) {

                MODE = Byte.parseByte(request.getParameter("MODE"));

                stepAllocateRecord = new DataRecord("STP_METHOD_ALLOC_STEP");

                stepAllocateRecord.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                stepAllocateRecord.addField("STEP_ID", Types.VARCHAR, request.getParameter("CODE"), true);
                stepAllocateRecord.addField("STEP_SEQ", Types.VARCHAR, request.getParameter("STEP_SEQ"), true);
                stepAllocateRecord.addField("ADMISSION_TYPE_CODE", Types.VARCHAR, request.getParameter("ADMISSION_TYPE_CODE"));
                stepAllocateRecord.addField("TAX_TYPE_CODE", Types.VARCHAR, request.getParameter("TAX_TYPE_CODE"));
                stepAllocateRecord.addField("TAX_RATE", Types.NUMERIC, request.getParameter("TAX_RATE"));
                stepAllocateRecord.addField("AMOUNT_START", Types.NUMERIC, request.getParameter("AMOUNT_START"));
                stepAllocateRecord.addField("AMOUNT_END", Types.NUMERIC, request.getParameter("AMOUNT_END"));
                stepAllocateRecord.addField("ALLOCATE_PCT", Types.NUMERIC, request.getParameter("ALLOCATE_PCT"));
                
       
                if (MODE == DBMgr.MODE_INSERT) {

                    if (DBMgr.insertRecord(stepAllocateRecord)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/method_alloc_step_main.jsp?DOCTOR_CATEGORY_CODE="+request.getParameter("CODE")));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                } else if (MODE == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(stepAllocateRecord)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/method_alloc_step_main.jsp?DOCTOR_CATEGORY_CODE="+request.getParameter("CODE")));
                    } else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            } else if (request.getParameter("STEP_ID") != null) {
            	
            	stepAllocateRecord = DBMgr.getRecord("SELECT * FROM STP_METHOD_ALLOC_STEP  WHERE STEP_ID = '" + request.getParameter("STEP_ID") + "'AND STEP_SEQ = '" + request.getParameter("STEP_SEQ") + "'  AND HOSPITAL_CODE = '" +session.getAttribute("HOSPITAL_CODE").toString()+"'");
            	
            	doctorCategory = DBMgr.getRecord("SELECT CODE , DESCRIPTION FROM DOCTOR_CATEGORY WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND CODE = '" + request.getParameter("STEP_ID")  + "'");
				getcode = DBMgr.getRecordValue(doctorCategory, "CODE");
				getDescription = DBMgr.getRecordValue(doctorCategory, "DESCRIPTION");
				
				if(DBMgr.getRecordValue(maxValueOnStep, "STEP_SEQ").equals("")){ 
					stepSeq = "1";
				} else { 
					stepSeq = ( Integer.parseInt(DBMgr.getRecordValue(maxValueOnStep, "STEP_SEQ")) + 1 ) + "" ;
				}
        	   
            	if (stepAllocateRecord == null) {
					String sqlGetMax = "SELECT  STEP_SEQ  FROM STP_METHOD_ALLOC_STEP  WHERE STEP_ID = '" + request.getParameter("STEP_ID") + "' AND STEP_SEQ = ( SELECT MAX(CAST(STEP_SEQ AS INT)) FROM STP_METHOD_ALLOC_STEP WHERE STEP_ID = '" + request.getParameter("STEP_ID") + "' AND HOSPITAL_CODE = '" +session.getAttribute("HOSPITAL_CODE").toString()+"' ) AND HOSPITAL_CODE = '" +session.getAttribute("HOSPITAL_CODE").toString()+"'";
					maxValueOnStep = DBMgr.getRecord(sqlGetMax);
                    MODE = DBMgr.MODE_INSERT;
            	} else {
            		stepSeq = DBMgr.getRecordValue(stepAllocateRecord, "STEP_SEQ");
                    MODE = DBMgr.MODE_UPDATE;
                }
            }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript">

            function CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    Refresh_ORDER_ITEM();
                    return false;
                }
                else {
                    return true;
                }
            }

            function Refresh_ORDER_ITEM() {
                var to = document.location.pathname.lastIndexOf('?');
                if (to < 0) {
                    window.location = document.location.pathname + '?CODE=' + document.mainForm.CODE.value;
                }
                else {
                    window.location = document.location.pathname.substr(0, to) + '?CODE=' + document.mainForm.CODE.value;
                }
            }


            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=STP_METHOD_ALLOC_STEP&COND=STEP_ID='" + document.mainForm.CODE.value + "' AND STEP_SEQ='"+document.mainForm.STEP_SEQ.value+"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_VerifyData);
            }
            
            function AJAX_Handle_VerifyData() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist = isXMLNodeExist(xmlDoc, "STEP_ID");
                    
                    switch (document.mainForm.MODE.value) {
                    case "<%=DBMgr.MODE_INSERT%>" :
                            if (beExist) {
                                if (confirm("<%=labelMap.get("CONFIRM_REPLACE_DATA")%>")) {
                                document.mainForm.MODE.value= "<%=DBMgr.MODE_UPDATE%>";
                                document.mainForm.submit();
                            }
                        }
                        else {
                            document.mainForm.submit();
                        }
                        break;
                    case "<%=DBMgr.MODE_UPDATE%>" :
                        if (beExist) {
                            document.mainForm.submit();
                        }
                        else {
                            alert("<%=labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)%>");
                        }
                        break;
                    }
                }
            }

            function SAVE_Click() {
                if (!isObjectEmptyString(document.mainForm.CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    !isObjectEmptyString(document.mainForm.STEP_SEQ, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                    AJAX_VerifyData();
                }
            }
            
            function RESET_Click() {
                document.mainForm.reset();
                document.mainForm.MODE.value = "<%=DBMgr.MODE_INSERT%>";
                return false;
            }
            
        </script>
        <style type="text/css">
<!--
.style1 {color: #003399}
.style2 {color: #033}
-->
        </style>
</head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="method_alloc_step.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=MODE%>" />
                        
			<center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'> Setup Allocate > Step Allocate </font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                  <th colspan="4">
				  <div style="float: left;">${labelMap.TITLE_MAIN}</div>
				  </th>
                </tr>
                <tr>
                    <td class="label"><label for="CODE"><span class="style1">${labelMap.STEP_ID}</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="CODE" name="CODE" class="short" maxlength="20" value="<%= getcode %>" readonly="readonly"/>
            			<input type="text" id="DESCRIPTON" name="DESCRIPTON" class="mediumMax" maxlength="255" value="<%= getDescription %>" readonly="readonly" />
            		</td>
                </tr>
                <tr>
                    <td class="label"><label for="STEP_SEQ"><span class="style1">${labelMap.STEP_SEQ} * </span></label></td>
                    <td colspan="3" class="input"><input type="text" id="STEP_SEQ" name="STEP_SEQ" maxlength="10" class="short" value="<%=stepSeq%>" /></td>
                </tr>
                <tr>
                    <td class="label"><label for="ADMISSION_TYPE_CODE"><span class="style1">${labelMap.ADMISSION_TYPE_CODE}*</span></label></td>
                    <td colspan="3" class="input">
                    	<%= DBMgr.generateDropDownList("ADMISSION_TYPE_CODE", "medium", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM ADMISSION_TYPE ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(stepAllocateRecord, "ADMISSION_TYPE_CODE")) %>
                    </td>
                </tr>
				<tr>
                    <td class="label"><label for="ORDER_ITEM_CATEGORY_CODE"><span class="style1">${labelMap.TAX_TYPE_CODE}*</span></label></td>
                    <td colspan="3" class="input">
             			    <%=DBMgr.generateDropDownList("TAX_TYPE_CODE", "medium", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM TAX_TYPE ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(stepAllocateRecord, "TAX_TYPE_CODE"))%>
                 	</td>
                </tr>
				
                <tr>
                    <td class="label"><label for="ACCOUNT_CODE"><span class="style1">${labelMap.TAX_RATE}*</span></label></td>
                    <td colspan="3" class="input">
						<select name="TAX_RATE" class="medium" id="TAX_RATE">
                    		<%if(MODE == DBMgr.MODE_INSERT){ %>
	                    		<option value="1" selected="selected">1</option>
	                    		<option value="1.1111">1.1111</option>
	                    		<option value="1.2500">1.2500</option>
                    		<%}else{%>
	                    		<option value="1" <%if(DBMgr.getRecordValue(stepAllocateRecord, "TAX_RATE").equals("1.0000")){out.println("selected");}%>>1</option>
	                    		<option value="1.1111" <%if(DBMgr.getRecordValue(stepAllocateRecord, "TAX_RATE").equals("1.1111")){out.println("selected");}%>>1.1111</option>
	                    		<option value="1.2500" <%if(DBMgr.getRecordValue(stepAllocateRecord, "TAX_RATE").equals("1.2500")){out.println("selected");}%>>1.2500</option>
	                    	<%} %>
                    	</select>
					</td>
                </tr>
                
                <tr>
                    <td class="label"><label for="AMOUNT_START"><span class="style1">${labelMap.AMOUNT_START}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" class="medium" id="AMOUNT_START" name="AMOUNT_START" value="<%= DBMgr.getRecordValue(stepAllocateRecord, "AMOUNT_START")%>" />
                               
					</td>
                </tr>
				
				 <tr>
                    <td class="label"><label for="AMOUNT_END"><span class="style1">${labelMap.AMOUNT_END}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" class="medium" id="AMOUNT_END" name="AMOUNT_END" value="<%= DBMgr.getRecordValue(stepAllocateRecord, "AMOUNT_END")%>" />
                               
					</td>
                </tr>
                
                 <tr>
                    <td class="label"><label for="ALLOCATE_PCT"><span class="style1">${labelMap.ALLOCATE_PCT}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" class="medium" id="ALLOCATE_PCT" name="ALLOCATE_PCT" value="<%= DBMgr.getRecordValue(stepAllocateRecord, "ALLOCATE_PCT")%>" />
                               
					</td>
                </tr>
                
               <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="return RESET_Click()" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
<%=codescript%>