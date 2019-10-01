<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.Types"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>

<%@page import="df.bean.obj.util.JNumber"%>
<%@ include file="../../_global.jsp" %>

<%
//
// Verify permission
//

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_GUARANTEE_DETAIL)) {
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
            labelMap.add("TITLE_MAIN", "Guarantee Details", "Guarantee Details");
            labelMap.add("G_DR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("GUARANTEE_CODE", "Guarantee Code", "รหัสการันตี");
            labelMap.add("G_TYPE_CODE", "Guarantee Type Code", "ประเภทการันตี");
            labelMap.add("ADMISSION", "Admission Type Code", "แผนกรับผู้ป่วย");
            labelMap.add("G_LOCATION", "Guarantee Department", "การันตีเฉพาะสถานที่");
            labelMap.add("MM", "Month", "เดือน");
            labelMap.add("YYYY", "Year", "ปี");
            labelMap.add("START_DATE", "Start Date (dd/mm/yyyy)", "เริ่มวันที่");
            labelMap.add("START_TIME", "Start Time", "เริ่มเวลา");
            labelMap.add("EARLY_TIME", "Early Time", "เริ่มก่อนเวลา");
            labelMap.add("END_DATE", "End Date (dd/mm/yyyy)", "ถึงวันที่");
            labelMap.add("END_TIME", "End Time", "ถึงเวลา");
            labelMap.add("LATE_TIME", "Late Time", "สิ้นสุดเวลา");
            labelMap.add("AMOUNT_PER_TIME", "Amount per Hour", "รายได้ต่อชั่วโมง");
            labelMap.add("G_AMOUNT", "Guarantee Amount", "จำนวนเงินการันตีค่าแพทย์");
            labelMap.add("FIX_AMOUNT", "Guarantee Fix Amount", "จำนวนเงินค่าแพทย์คงที่");
            labelMap.add("INCLUDE_AMOUNT", "Include Guarantee", "จำนวนเงินรวมการันตี");
            labelMap.add("EXCLUDE_AMOUNT", "Extra Amount", "จำนวนเงินเพิ่มพิเศษ");
            labelMap.add("SPECIAL_AMOUNT", "Guarantee Special", "Guarantee Special");
            labelMap.add("OVER_ALLOCATE_PCT", "Over Guarantee Alloc.", "ส่วนแบ่งเกินการันตี");
            labelMap.add("GUARANTEE_ALLOCATE_PCT", "In Guarantee Alloc.", "ส่วนแบ่งในการันตี");
            labelMap.add("GUARANTEE_CHK_VERI", "Doctor Guarantee", "แพทย์การันตี");
            labelMap.add("GUARANTEE_CHK_DATE_TRUE","Yes","ใช่");
            labelMap.add("GUARANTEE_CHK_DATE_FALSE","No","ไม่ใช่");
            labelMap.add("ALERT_INVOICE", "INVOICE DATE ERROR", "วันที่ของ invoice date ไม่ถูกต้อง");
            labelMap.add("ABSORB_AMOUNT", "Absorb Amount", "จำนวนเงินเติมการันตี");
            labelMap.add("ABSORB_REMAIN_AMOUNT", "Absorb Remain Amount", "จำนวนเติมการันตีคงเหลือ");
            
            // Change Request
            labelMap.add("GUARANTEE_AMOUNT", "Amount of Guarantee", "จำนวนเงินคิดการันตี");
            labelMap.add("GUARANTEE_BY_DF", "Allocate Amount", "จำนวนเงินส่วนแบ่ง");
            labelMap.add("GUARANTEE_BY_AMOUNT", "Transaction Amount", "จำนวนเงินเต็ม");
            labelMap.add("GUARANTEE_SOURCE", "Revenue for Guarantee", "คิดการันตีจากยอด");
            labelMap.add("GUARANTEE_DAY", "Guarantee by Day.", "วันที่นำมาเทียบการันตี");
            labelMap.add("GUARANTEE_SOURCE_DF", "DF After Allocate", "จำนวนเงินหลังแบ่ง");
            labelMap.add("GUARANTEE_SOURCE_DF_VALUE", "AF", "AF");
            labelMap.add("GUARANTEE_SOURCE_TAX", "DF Before Allocate", "จำนวนเงินก่อนแบ่ง");
            labelMap.add("GUARANTEE_SOURCE_TAX_VALUE", "BF", "BF");
            labelMap.add("GL_DEPARTMENT_CODE", "GL Department Cost", "แผนก");
            labelMap.add("NOTE", "Note", "อ้างอิง");
            labelMap.add("IS_INCLUDE_LOCATION_0", "Except", " ไม่");
            labelMap.add("IS_INCLUDE_LOCATION_1", "Specific", " ใช่");
            labelMap.add("BAHT", "Baht", "บาท");
            labelMap.add("HOUR", "Hour", "ช.ม.");
            labelMap.add("BAHT_PER_HOUR", "Baht/Hour", "บาท/ช.ม.");
            
            labelMap.add("MSG_ALERT_CHECK_DATE", "Plese check date guarantee.", " กรุณาตรวจสอบวันที่การันตี");
            labelMap.add("GUARANTEE_TAX_TYPE","Guarantee Tax Type","ประเภทภาษีการันตี");
            
            request.setAttribute("labelMap", labelMap.getHashMap());
            request.setCharacterEncoding("UTF-8");
            
            byte MODE = DBMgr.MODE_INSERT;
            DataRecord stpGuaranteeRec = null, doctorRec = null, location = null, guaranteeTypeRec = null  , department = null;
            DataRecord stpDelete = null;
            String MM = "", YYYY = "";
 
            if (request.getParameter("MODE") != null) {

                // Insert or update
                MODE = Byte.parseByte(request.getParameter("MODE"));
                stpGuaranteeRec = new DataRecord("STP_GUARANTEE");
                stpDelete = new DataRecord("STP_GUARANTEE");
                stpGuaranteeRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                stpGuaranteeRec.addField("GUARANTEE_DR_CODE", Types.VARCHAR, request.getParameter("GUARANTEE_DR_CODE"), true);
                stpGuaranteeRec.addField("GUARANTEE_CODE", Types.VARCHAR, request.getParameter("GUARANTEE_CODE"), true);
                stpGuaranteeRec.addField("GUARANTEE_TYPE_CODE", Types.VARCHAR, request.getParameter("GUARANTEE_TYPE_CODE"), true);

                // Is BOI Edit
                if("MLD".equals(request.getParameter("GUARANTEE_TYPE_CODE")) || "DLY".equals(request.getParameter("GUARANTEE_TYPE_CODE"))){ 
                	stpGuaranteeRec.addField("IS_GUARANTEE_DAILY", Types.VARCHAR, "Y");
                }else {
                	stpGuaranteeRec.addField("IS_GUARANTEE_DAILY", Types.VARCHAR, "N");
                }
                
                stpGuaranteeRec.addField("ADMISSION_TYPE_CODE", Types.VARCHAR, request.getParameter("ADMISSION_TYPE_CODE"), true);
                stpGuaranteeRec.addField("MM", Types.VARCHAR, request.getParameter("MM"), true);
                stpGuaranteeRec.addField("YYYY", Types.VARCHAR, request.getParameter("YYYY"), true);
                stpGuaranteeRec.addField("START_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("START_DATE")), true);
                stpGuaranteeRec.addField("START_TIME", Types.VARCHAR, JDate.saveTimeNOColon(request.getParameter("START_TIME")), true);
                stpGuaranteeRec.addField("END_DATE", Types.VARCHAR, JDate.saveDate(request.getParameter("END_DATE")), true);
                stpGuaranteeRec.addField("END_TIME", Types.VARCHAR,  JDate.saveTimeNOColon(request.getParameter("END_TIME")), true);                
                stpGuaranteeRec.addField("GUARANTEE_LOCATION", Types.VARCHAR, request.getParameter("GUARANTEE_LOCATION_CODE"));
                stpGuaranteeRec.addField("GL_DEPARTMENT_CODE", Types.VARCHAR, request.getParameter("GL_DEPARTMENT_CODE"));
                
				if(JDate.saveTime(request.getParameter("EARLY_TIME")) == ""){
					stpGuaranteeRec.addField("EARLY_TIME", Types.VARCHAR,  JDate.saveTime(request.getParameter("START_TIME")));
				}else{
					stpGuaranteeRec.addField("EARLY_TIME", Types.VARCHAR,  JDate.saveTime(request.getParameter("EARLY_TIME")));
				}
				
				if(JDate.saveTime(request.getParameter("LATE_TIME")) == ""){
					stpGuaranteeRec.addField("LATE_TIME", Types.VARCHAR,  JDate.saveTime(request.getParameter("END_TIME")));
				}else{
					stpGuaranteeRec.addField("LATE_TIME", Types.VARCHAR,  JDate.saveTime(request.getParameter("LATE_TIME")));
				}
				
				stpGuaranteeRec.addField("AMOUNT_PER_TIME"  ,  Types.NUMERIC ,  request.getParameter("AMOUNT_PER_TIME"));   	//  new field record
				stpGuaranteeRec.addField("AMOUNT_OF_TIME"  ,  Types.NUMERIC ,  request.getParameter("AMOUNT_OF_TIME")); //  new field record
				stpGuaranteeRec.addField("AMOUNT_DIFF_TIME"  ,  Types.NUMERIC ,  request.getParameter("AMOUNT_DIFF_TIME")); //  new field record
                stpGuaranteeRec.addField("GUARANTEE_AMOUNT", Types.NUMERIC, request.getParameter("GUARANTEE_AMOUNT"));
                stpGuaranteeRec.addField("GUARANTEE_FIX_AMOUNT", Types.NUMERIC, request.getParameter("GUARANTEE_FIX_AMOUNT"));
                stpGuaranteeRec.addField("GUARANTEE_INCLUDE_AMOUNT", Types.NUMERIC, request.getParameter("GUARANTEE_INCLUDE_AMOUNT"));
                stpGuaranteeRec.addField("GUARANTEE_EXCLUDE_AMOUNT", Types.NUMERIC, request.getParameter("GUARANTEE_EXCLUDE_AMOUNT"));
                stpGuaranteeRec.addField("OVER_ALLOCATE_PCT", Types.NUMERIC, request.getParameter("OVER_ALLOCATE_PCT"));
                stpGuaranteeRec.addField("GUARANTEE_ALLOCATE_PCT", Types.NUMERIC, request.getParameter("GUARANTEE_ALLOCATE_PCT"));
                stpGuaranteeRec.addField("GUARANTEE_SOURCE", Types.VARCHAR, request.getParameter("GUARANTEE_SOURCE"));
                stpGuaranteeRec.addField("GUARANTEE_DAY", Types.VARCHAR, request.getParameter("GUARANTEE_DAY"));
                stpGuaranteeRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                stpGuaranteeRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                stpGuaranteeRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                stpGuaranteeRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                stpGuaranteeRec.addField("NOTE", Types.VARCHAR, request.getParameter("NOTE"));
                stpGuaranteeRec.addField("IS_INCLUDE_LOCATION", Types.VARCHAR, request.getParameter("GUARANTEE_LOCATION_CODE").equals("")?"" :request.getParameter("IS_INCLUDE_LOCATION"));
                stpGuaranteeRec.addField("INCLUDE_PER_TIME" , Types.NUMERIC , request.getParameter("INCLUDE_AMOUNT_PER_HOUR"));
                stpGuaranteeRec.addField("INCLUDE_OF_TIME" , Types.NUMERIC , request.getParameter("INCLUDE_HOUR"));
                stpGuaranteeRec.addField("TAX_TYPE_CODE" , Types.VARCHAR , request.getParameter("TAX_TYPE_CODE"));
                
                
                if (MODE == DBMgr.MODE_INSERT) {
                    //REPLACE DATA
                	if("REPLACE".equalsIgnoreCase(request.getParameter("EDIT_MODE_INSERTUPDATE"))){
                    	System.out.println("Replace Data : "+request.getParameter("EDIT_GUARANTEE_TYPE_CODE"));
                        stpDelete.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                        stpDelete.addField("GUARANTEE_DR_CODE", Types.VARCHAR, request.getParameter("EDIT_GUARANTEE_DR_CODE"), true);
                        stpDelete.addField("GUARANTEE_CODE", Types.VARCHAR, request.getParameter("EDIT_GUARANTEE_CODE"), true);
                        stpDelete.addField("GUARANTEE_TYPE_CODE", Types.VARCHAR, request.getParameter("EDIT_GUARANTEE_TYPE_CODE"), true);
                        stpDelete.addField("ADMISSION_TYPE_CODE", Types.VARCHAR, request.getParameter("EDIT_ADMISSION_TYPE_CODE"), true);
                        stpDelete.addField("MM", Types.VARCHAR, request.getParameter("EDIT_MM"), true);
                        stpDelete.addField("YYYY", Types.VARCHAR, request.getParameter("EDIT_YYYY"), true);
                        stpDelete.addField("START_DATE", Types.VARCHAR, request.getParameter("EDIT_START_DATE"), true);
                        stpDelete.addField("START_TIME", Types.VARCHAR, JDate.saveTimeNOColon(request.getParameter("EDIT_START_TIME")), true);
                        stpDelete.addField("END_DATE", Types.VARCHAR, request.getParameter("EDIT_END_DATE"), true);
                        stpDelete.addField("END_TIME", Types.VARCHAR,  JDate.saveTimeNOColon(request.getParameter("EDIT_END_TIME")), true);
                        DBMgr.deleteRecord(stpDelete);
                    }
                    if (DBMgr.insertRecord(stpGuaranteeRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/guarantee_main.jsp?GUARANTEE_DR_CODE=%1$s&MM=%2$s&YYYY=%3$s", stpGuaranteeRec.getField("GUARANTEE_DR_CODE").getValue(), stpGuaranteeRec.getField("MM").getValue(), stpGuaranteeRec.getField("YYYY").getValue())));
                    }else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }else if (MODE == DBMgr.MODE_UPDATE) {
                    if (DBMgr.updateRecord(stpGuaranteeRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", String.format("input/guarantee_main.jsp?GUARANTEE_DR_CODE=%1$s&MM=%2$s&YYYY=%3$s", stpGuaranteeRec.getField("GUARANTEE_DR_CODE").getValue(), stpGuaranteeRec.getField("MM").getValue(), stpGuaranteeRec.getField("YYYY").getValue())));
                    }else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }

                response.sendRedirect("../message.jsp");
                return;
            }else if (request.getParameter("GUARANTEE_DR_CODE") != null && request.getParameter("ADMISSION_TYPE_CODE") != null && request.getParameter("GUARANTEE_TYPE_CODE") != null && request.getParameter("MM") != null && request.getParameter("YYYY") != null && request.getParameter("START_DATE") != null && request.getParameter("START_TIME") != null && request.getParameter("END_DATE") != null && request.getParameter("END_TIME") != null) {
                // Edit
                MODE = DBMgr.MODE_UPDATE;
                String query = String.format(
                		"SELECT * "+
                		"FROM STP_GUARANTEE "+
                		"WHERE HOSPITAL_CODE = '%1$s' AND GUARANTEE_DR_CODE = '%2$s' AND ADMISSION_TYPE_CODE = '%3$s' "+
                		"AND GUARANTEE_TYPE_CODE = '%4$s' AND MM = '%5$s' AND YYYY = '%6$s' AND START_DATE = '%7$s' AND START_TIME = '%8$s' "+
                		"AND END_DATE = '%9$s' AND END_TIME = '%10$s' AND GUARANTEE_CODE='%11$s' ", 
                		session.getAttribute("HOSPITAL_CODE"), request.getParameter("GUARANTEE_DR_CODE"), request.getParameter("ADMISSION_TYPE_CODE"), 
                		request.getParameter("GUARANTEE_TYPE_CODE"), request.getParameter("MM"), request.getParameter("YYYY"), 
                		request.getParameter("START_DATE"), request.getParameter("START_TIME"), request.getParameter("END_DATE"), 
                		request.getParameter("END_TIME"), request.getParameter("GUARANTEE_CODE"));
                System.out.println(query);
                stpGuaranteeRec = DBMgr.getRecord(query);
                location = DBMgr.getRecord("SELECT CODE, DESCRIPTION FROM DEPARTMENT WHERE CODE = '" + DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_LOCATION") + "' AND HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE")+"'");
                doctorRec = DBMgr.getRecord(String.format("SELECT GUARANTEE_START_DATE, GUARANTEE_START_DATE, CODE, NAME_%1$s AS NAME, IN_GUARANTEE_PCT, DEPARTMENT_CODE, GUARANTEE_SOURCE, GUARANTEE_DAY FROM DOCTOR WHERE CODE = '%2$s'", labelMap.getFieldLangSuffix(), DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_DR_CODE")));
                department = DBMgr.getRecord("SELECT CODE, DESCRIPTION FROM DEPARTMENT WHERE CODE = '" + DBMgr.getRecordValue(stpGuaranteeRec, "GL_DEPARTMENT_CODE") + "' AND HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE")+"'");
                guaranteeTypeRec = DBMgr.getRecord(String.format("SELECT CODE, DESCRIPTION FROM GUARANTEE_TYPE WHERE CODE = '%1$s'", DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_TYPE_CODE")));
            }else if (request.getParameter("GUARANTEE_DR_CODE") != null && request.getParameter("MM") != null && request.getParameter("YYYY") != null) {
                // New
                MODE = DBMgr.MODE_INSERT;
                MM = request.getParameter("MM");
                YYYY = request.getParameter("YYYY");
                String sql =  String.format("SELECT GUARANTEE_START_DATE,GUARANTEE_START_DATE,OVER_GUARANTEE_PCT,CODE, NAME_%1$s AS NAME, IN_GUARANTEE_PCT, DEPARTMENT_CODE, GUARANTEE_SOURCE, GUARANTEE_DAY FROM DOCTOR WHERE HOSPITAL_CODE = '" +session.getAttribute("HOSPITAL_CODE") + "' AND  CODE = '%2$s'", labelMap.getFieldLangSuffix(), request.getParameter("GUARANTEE_DR_CODE"));
                doctorRec = DBMgr.getRecord(sql);
                department = DBMgr.getRecord("SELECT CODE, DESCRIPTION FROM DEPARTMENT WHERE CODE = '" + DBMgr.getRecordValue(doctorRec, "DEPARTMENT_CODE") + "' AND HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE")+"'");
            }else {
                response.sendRedirect("../message.jsp");
            }
            
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/jquery-1.6.min.js"></script>
        <script type="text/javascript">
        
            var num_default_value = "0.00" ;
            var TYPE_CODE_CHECK = "MLY";
            var TYPE_CODE_CHECK_DLY = "MLD";
            var TYPE_CODE_CHECK_STP = "STP";
            
            function GUARANTEE_TYPE_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.GUARANTEE_TYPE_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_GUARANTEE_TYPE() {
                var target = "../../RetrieveData?TABLE=GUARANTEE_TYPE&COND=CODE='" + document.mainForm.GUARANTEE_TYPE_CODE.value +"'";
                AJAX_Request(target, AJAX_Handle_Refresh_GUARANTEE_TYPE);
            }
            
            function AJAX_Handle_Refresh_GUARANTEE_TYPE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.GUARANTEE_TYPE_CODE.value = "";
                        document.mainForm.GUARANTEE_TYPE_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.GUARANTEE_TYPE_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }

            //GUARANTEE_LOCATION
            //GUARANTEE_LOCATION_CODE_KeyPress
            function GUARANTEE_LOCATION_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.GUARANTEE_LOCATION_CODE.blur();
                    return false;
                }else {
                    return true;
                }
            }

            function AJAX_Refresh_GUARANTEE_LOCATION() {
                var target = "../../RetrieveData?TABLE=DEPARTMENT&COND=CODE='" + document.mainForm.GUARANTEE_LOCATION_CODE.value +"'"
                + " AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";

                AJAX_Request(target, AJAX_Handle_Refresh_GUARANTEE_LOCATION);
            }
            
            function AJAX_Handle_Refresh_GUARANTEE_LOCATION() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //alert("AJAX_Handle_Refresh_GUARANTEE_LOCATION");
                        document.mainForm.GUARANTEE_LOCATION_CODE.value = "";
                        document.mainForm.GUARANTEE_LOCATION_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.GUARANTEE_LOCATION_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }            

            //  ----------------------------------------------------------- GUARANTEE DEPARTMENT --------------------------------------------------
               function GL_DEPARTMENT_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.GL_DEPARTMENT_CODE.blur();
                    return false;
                } else {
                    return true;
                }
            }

            function AJAX_Refresh_GL_DEPARTMENT() {
                var target = "../../RetrieveData?TABLE=DEPARTMENT&COND=CODE='" + document.mainForm.GL_DEPARTMENT_CODE.value +"'"
                + " AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                
                AJAX_Request(target, AJAX_Handle_Refresh_GL_DEPARTMENT);
            }
            
            function AJAX_Handle_Refresh_GL_DEPARTMENT() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //alert("AJAX_Handle_Refresh_GUARANTEE_LOCATION");
                        document.mainForm.GL_DEPARTMENT_CODE.value = "";
                        document.mainForm.GL_DEPARTMENT_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.GL_DEPARTMENT_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }            

            // ---------------------------------------------------- END GUARANTEE ---------------------------------------------------------------------------
            
            function AJAX_VerifyData() {
                //alert("2");
                var guacode = document.mainForm.GUARANTEE_CODE;
                var type_code = document.mainForm.GUARANTEE_TYPE_CODE.value;
                var con = "";
                if(type_code==TYPE_CODE_CHECK || type_code==TYPE_CODE_CHECK_DLY){
                    if(guacode.value != ""){
                        con = " AND GUARANTEE_CODE='" + document.mainForm.GUARANTEE_CODE.value + "'"
                        + " AND START_DATE='" + toSaveDate(document.mainForm.START_DATE.value) + "'"
                        + " AND START_TIME='" + toSaveTimeNOColon(document.mainForm.START_TIME.value) + "'"
                        + " AND END_DATE='" + toSaveDate(document.mainForm.END_DATE.value) + "'"
                        + " AND END_TIME='" + toSaveTimeNOColon(document.mainForm.END_TIME.value) + "'";
                    }else{
                        con = " AND GUARANTEE_CODE='" + document.mainForm.GUARANTEE_CODE.value + "'"
                        + " AND START_DATE='" + toSaveDate(document.mainForm.START_DATE.value) + "'"
                        + " AND START_TIME='" + toSaveTimeNOColon(document.mainForm.START_TIME.value) + "'"
                        + " AND END_DATE='" + toSaveDate(document.mainForm.END_DATE.value) + "'"
                        + " AND END_TIME='" + toSaveTimeNOColon(document.mainForm.END_TIME.value) + "'";
                    }
                }else{
                    if(guacode.value != ""){
                        con = " AND GUARANTEE_CODE='" + document.mainForm.GUARANTEE_CODE.value + "'"
                            + " AND START_DATE='" + toSaveDate(document.mainForm.START_DATE.value) + "'"
                            + " AND START_TIME='" + toSaveTimeNOColon(document.mainForm.START_TIME.value) + "'"
                            + " AND END_DATE='" + toSaveDate(document.mainForm.END_DATE.value) + "'"
                            + " AND END_TIME='" + toSaveTimeNOColon(document.mainForm.END_TIME.value) + "'";
                    }else{
                        con = " AND GUARANTEE_CODE='" + toSaveDate(document.mainForm.START_DATE.value) + toSaveTimeNOColon(document.mainForm.START_TIME.value) + "'"
                            + " AND START_DATE='" + toSaveDate(document.mainForm.START_DATE.value) + "'"
                            + " AND START_TIME='" + toSaveTimeNOColon(document.mainForm.START_TIME.value) + "'"
                            + " AND END_DATE='" + toSaveDate(document.mainForm.END_DATE.value) + "'"
                            + " AND END_TIME='" + toSaveTimeNOColon(document.mainForm.END_TIME.value) + "'";
                    }
                }
                
                var target = "../../RetrieveData?TABLE=STP_GUARANTEE&COND=HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'"
                    + " AND GUARANTEE_DR_CODE='" + document.mainForm.GUARANTEE_DR_CODE.value + "'"
                    + con
                    + " AND GUARANTEE_TYPE_CODE='" + document.mainForm.GUARANTEE_TYPE_CODE.value + "'"
                    + " AND ADMISSION_TYPE_CODE='" + document.mainForm.ADMISSION_TYPE_CODE.value + "'"
                    + " AND YYYY='" + document.mainForm.YYYY.value + "'"
                    + " AND MM='" + document.mainForm.MM.value + "'";

                AJAX_Request(target, AJAX_Handle_VerifyData);
            }
            
            function AJAX_Handle_VerifyData() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    var beExist = isXMLNodeExist(xmlDoc, "HOSPITAL_CODE");
                    switch (document.mainForm.MODE.value) {
                    case "<%=DBMgr.MODE_INSERT%>" :
                            var type_code = document.mainForm.GUARANTEE_TYPE_CODE.value;
                            if(type_code==TYPE_CODE_CHECK || type_code==TYPE_CODE_CHECK_STP || type_code==TYPE_CODE_CHECK_DLY){
                                document.mainForm.GUARANTEE_CODE.value = document.mainForm.YYYY.value + document.mainForm.MM.value;
                            }else{
                                document.mainForm.GUARANTEE_CODE.value = toSaveDate(document.mainForm.START_DATE.value) + toSaveTimeNOColon(document.mainForm.START_TIME.value);
                            }

                            if (beExist) {
                               if (confirm("<%=labelMap.get("CONFIRM_REPLACE_DATA")%>")) {
                                    document.mainForm.MODE.value= "<%=DBMgr.MODE_UPDATE%>";
                                    document.mainForm.submit();
                                }else if (confirm("<%=labelMap.get(LabelMap.CONFIRM_ADD_NEW_DATA)%>")) {
                                    document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT%>";
                                    document.mainForm.submit();
                            	}
                            }else {
                                document.mainForm.submit();
                            }
                        break;
                    case "<%=DBMgr.MODE_UPDATE%>" :
                        if (beExist) {
                             if(chekcUpdateInsertPK()){
                                //Update Record - PK No Edit
                                document.mainForm.submit();
                            }else{
                                //Error - PK duplicate in Database
                                alert('Duplicate Data');
                            }
                        }else {
                        	var type_code = document.mainForm.GUARANTEE_TYPE_CODE.value;
                        	if(type_code=="MLY" || type_code=="STP" || type_code=="MLD"){
                               	document.mainForm.GUARANTEE_CODE.value = document.mainForm.YYYY.value + document.mainForm.MM.value;
                            }else{
                               	document.mainForm.GUARANTEE_CODE.value = toSaveDate(document.mainForm.START_DATE.value) + toSaveTimeNOColon(document.mainForm.START_TIME.value);
                            }
                            if (confirm("<%=labelMap.get("CONFIRM_REPLACE_DATA")%>")) {
                                //Delete Record, Update Record - PK Edit
                                document.mainForm.EDIT_MODE_INSERTUPDATE.value = "REPLACE";
                                document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT%>";
                                document.mainForm.submit();
                            }else if (confirm("<%=labelMap.get(LabelMap.CONFIRM_ADD_NEW_DATA)%>")) {
                                document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT%>";
                                document.mainForm.submit();
                            }
                        }
                        break;
                    }
                }
            }
            
            function chekcUpdateInsertPK(){
                var type_code = document.mainForm.GUARANTEE_TYPE_CODE.value;
                if(type_code==TYPE_CODE_CHECK){
                    if((document.mainForm.EDIT_GUARANTEE_DR_CODE.value == document.mainForm.GUARANTEE_DR_CODE.value)
                            && (document.mainForm.EDIT_GUARANTEE_DR_CODE.value == document.mainForm.GUARANTEE_DR_CODE.value)
                            && (document.mainForm.EDIT_GUARANTEE_CODE.value == document.mainForm.GUARANTEE_CODE.value)
                            && (document.mainForm.EDIT_GUARANTEE_TYPE_CODE.value == document.mainForm.GUARANTEE_TYPE_CODE.value)
                            && (document.mainForm.EDIT_ADMISSION_TYPE_CODE.value == document.mainForm.ADMISSION_TYPE_CODE.value)
                            && (document.mainForm.EDIT_MM.value == document.mainForm.MM.value)
                            && (document.mainForm.EDIT_YYYY.value == document.mainForm.YYYY.value)
                            && (document.mainForm.EDIT_START_DATE.value == toSaveDate(document.mainForm.START_DATE.value))
                            && (document.mainForm.EDIT_START_TIME.value == toSaveTimeNOColon(document.mainForm.START_TIME.value))
                            && (document.mainForm.EDIT_END_DATE.value == toSaveDate(document.mainForm.END_DATE.value))
                            && (document.mainForm.EDIT_END_TIME.value == toSaveTimeNOColon(document.mainForm.END_TIME.value))                            
                        ){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    if((document.mainForm.EDIT_GUARANTEE_DR_CODE.value == document.mainForm.GUARANTEE_DR_CODE.value)
                            && (document.mainForm.EDIT_GUARANTEE_DR_CODE.value == document.mainForm.GUARANTEE_DR_CODE.value)
                            && (document.mainForm.EDIT_GUARANTEE_CODE.value == document.mainForm.GUARANTEE_CODE.value)
                            && (document.mainForm.EDIT_GUARANTEE_TYPE_CODE.value == document.mainForm.GUARANTEE_TYPE_CODE.value)
                            && (document.mainForm.EDIT_ADMISSION_TYPE_CODE.value == document.mainForm.ADMISSION_TYPE_CODE.value)
                            && (document.mainForm.EDIT_MM.value == document.mainForm.MM.value)
                            && (document.mainForm.EDIT_YYYY.value == document.mainForm.YYYY.value)
                            && (document.mainForm.EDIT_START_DATE.value == toSaveDate(document.mainForm.START_DATE.value))
                            && (document.mainForm.EDIT_START_TIME.value == toSaveTimeNOColon(document.mainForm.START_TIME.value))
                            && (document.mainForm.EDIT_END_DATE.value == toSaveDate(document.mainForm.END_DATE.value))
                            && (document.mainForm.EDIT_END_TIME.value == toSaveTimeNOColon(document.mainForm.END_TIME.value))
                        ){
                        return true;
                    }else{
                        return false;
                    }
                }
            }
            
            /// ตรวจสอบวันที่ การันตี ว่าผ่านหรือไม่ ?? 
            function AJAX_Handle_CHECK_DATE(){
            	  var status  = false;
            	  if (AJAX_IsComplete()) {
                      
            		  var xmlDoc = AJAX.responseXML;
                   
                      if (getXMLNodeValue(xmlDoc, "STATUS") == 'pass' ) {
                    	 
                    	  if (!isObjectEmptyString(document.mainForm.GUARANTEE_DR_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                                  !isObjectEmptyString(document.mainForm.MM, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                                  !isObjectEmptyString(document.mainForm.YYYY, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                                  !isObjectEmptyString(document.mainForm.GUARANTEE_TYPE_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                                  !isObjectEmptyString(document.mainForm.ADMISSION_TYPE_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                                  !isObjectEmptyString(document.mainForm.START_DATE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                                  //isObjectValidDate(document.mainForm.START_DATE, '<%=labelMap.get(LabelMap.ALERT_INVALID_DATE)%>') && 
                                  !isObjectEmptyString(document.mainForm.END_DATE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                                  //isObjectValidDate(document.mainForm.END_DATE, '<%=labelMap.get(LabelMap.ALERT_INVALID_DATE)%>') && 
                                  !isObjectEmptyString(document.mainForm.START_TIME, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                                  //Check GUARANTEE_ALLOCATE_PCT,OVER_ALLOCATE_PCT : Null Om 20091215
                                  !isObjectEmptyString(document.mainForm.GUARANTEE_ALLOCATE_PCT, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                                  !isObjectEmptyString(document.mainForm.OVER_ALLOCATE_PCT, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                                  //isObjectValidTime(document.mainForm.START_TIME, '<%=labelMap.get(LabelMap.ALERT_INVALID_TIME)%>') && 
                                  !isObjectEmptyString(document.mainForm.END_TIME, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                                  //isObjectValidTime(document.mainForm.END_TIME, '<%=labelMap.get(LabelMap.ALERT_INVALID_TIME)%>') && 
                                  isObjectValidTime(document.mainForm.EARLY_TIME, '<%=labelMap.get(LabelMap.ALERT_INVALID_TIME)%>') && 
                                  isObjectValidTime(document.mainForm.LATE_TIME, '<%=labelMap.get(LabelMap.ALERT_INVALID_TIME)%>') && 
                                  isObjectValidNumber(document.mainForm.GUARANTEE_AMOUNT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
                                  isObjectValidNumber(document.mainForm.GUARANTEE_FIX_AMOUNT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
                                  isObjectValidNumber(document.mainForm.GUARANTEE_INCLUDE_AMOUNT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
                                  isObjectValidNumber(document.mainForm.GUARANTEE_EXCLUDE_AMOUNT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
                                  isObjectValidNumber(document.mainForm.GUARANTEE_ALLOCATE_PCT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') && 
                                  isObjectValidNumber(document.mainForm.OVER_ALLOCATE_PCT, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>')
                                  ) {
                                  //Check GUARANTEE_ALLOCATE_PCT,OVER_ALLOCATE_PCT : 0 Om 20091215
                                  if(document.mainForm.GUARANTEE_ALLOCATE_PCT.value == '0')
                                  {
	                                  alert("Please required fields In Guarantee Alloc Value > 0.");
	              				    	document.mainForm.GUARANTEE_ALLOCATE_PCT.value="";
	                                  document.mainForm.GUARANTEE_ALLOCATE_PCT.focus();
                                  }
                                 else if(document.mainForm.OVER_ALLOCATE_PCT.value == '0')
                                  {
	                                  alert("Please required fields Over Guarantee Alloc Value > 0.");
	                                  document.mainForm.OVER_ALLOCATE_PCT.value="";
	                                  document.mainForm.OVER_ALLOCATE_PCT.focus();
                                  }
                                else
                                {
                                  AJAX_VerifyData();   
                                }
                              }
                    	  
                      		status = true;
                      		//console.log("data status action : " + status);
                      		
                      }  else {
                    	  alert('${labelMap.MSG_ALERT_CHECK_DATE}');
                    	  status = false;   
                      }  
                  }				
            	return status;
            }
            
       		function SAVE_Click() {
            	
            	var  start_date =  $("#START_DATE").val();
            	var  start_time =  $("#START_TIME").val();
            	var  end_date  = $("#END_DATE").val();
            	var  end_time  = $("#END_TIME").val();
           		var  target = "../../CheckDateGuarantee?start_date=" + start_date + "&end_date=" + end_date + "&start_time=" + start_time + "&end_time=" + end_time ;
		   	  	AJAX_Request(target, AJAX_Handle_CHECK_DATE);
           	  	
            }
            
            // Check data on table SUMMARY_MONTHLY : 2009-07-01 By Nop
            function AJAX_Verify_Check_Summary_Monthly() {
                var date_input = "00/" + document.mainForm.MM.value + "/" + document.mainForm.YYYY.value;
                var target = "../../CheckSummaryMonthlySrvl?DATE_INPUT=" + date_input+"&FORM=guarantee";
                AJAX_Request(target, AJAX_Handle_Verify_Check_Summary_Monthly);
            }  

            function AJAX_Handle_Verify_Check_Summary_Monthly(){
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    if (getXMLNodeValue(xmlDoc, "STATUS")=='YES') {
                        document.mainForm.SAVE.disabled = false;
                    }else{
                    	document.mainForm.SAVE.disabled = true;
                    }
                }				
           	}
           	
        	function LOAD_AJAX_GUARANTEE_AMOUNT(ga_type,val,action){
				var start_date = document.mainForm.START_DATE.value;
				var end_date = document.mainForm.END_DATE.value;
				var start_time = document.mainForm.START_TIME.value;
				var end_time = document.mainForm.END_TIME.value;
				var dr_code = document.mainForm.GUARANTEE_DR_CODE.value;
				var amount_per_time = document.mainForm.AMOUNT_PER_TIME.value;
				
				
				var target = "../../GetHourTimeSrvl?start_date=" + start_date + "&end_date=" + end_date + "&start_time=" + start_time + "&end_time=" + end_time + "&ga_type="+ga_type+"&dr_code="+dr_code+ "&amount_per_time="+amount_per_time;

			}
            
           	function AJAX_GUARANTEE_AMOUNT(ga_type,val,action){
				var start_date = document.mainForm.START_DATE.value;
				var end_date = document.mainForm.END_DATE.value;
				var start_time = document.mainForm.START_TIME.value;
				var end_time = document.mainForm.END_TIME.value;
				var dr_code = document.mainForm.GUARANTEE_DR_CODE.value;
				var amount_per_time = document.mainForm.AMOUNT_PER_TIME.value;
				
				
				var target = "../../GetHourTimeSrvl?start_date=" + start_date + "&end_date=" + end_date + "&start_time=" + start_time + "&end_time=" + end_time + "&ga_type="+ga_type+"&dr_code="+dr_code+ "&amount_per_time="+amount_per_time;
					
				AJAX_Request(target, AJAX_Handle_GUARANTEE_AMOUNT); // ให้ทำ  function AJAX_Handle_GUARANTEE_AMOUNT 
				
			}
            
           function AJAX_Handle_GUARANTEE_AMOUNT(){
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                    //alert(document.mainForm.GUARANTEE_TYPE.value);
                     if(document.mainForm.GUARANTEE_TYPE.value=="GA"){
                    	 if (document.mainForm.AMOUNT_PER_TIME.value == "0") {
                    		 document.mainForm.AMOUNT_PER_TIME.value = "0";
							} else {
								document.mainForm.AMOUNT_PER_TIME.value =getXMLNodeValue(xmlDoc, "STATUS2");
	                      		document.mainForm.GUARANTEE_AMOUNT.value = getXMLNodeValue(xmlDoc, "STATUS");
							}
                    
                      		document.mainForm.AMOUNT_OF_TIME.value = getXMLNodeValue(xmlDoc,"STATUS1");
                      		document.mainForm.GUARANTEE_FIX_AMOUNT.readOnly=false;
      	           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.readOnly=true;
      	           			document.mainForm.GUARANTEE_AMOUNT.readOnly=false;
      	           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.value=num_default_value;
      	           			document.mainForm.GUARANTEE_FIX_AMOUNT.value=num_default_value;
                    	}else if(document.mainForm.GUARANTEE_TYPE.value=="GEA"){
                    		if (document.mainForm.AMOUNT_PER_TIME.value == "0") {
                       		 	document.mainForm.AMOUNT_PER_TIME.value = "0";
   							} else {
   								document.mainForm.AMOUNT_PER_TIME.value =getXMLNodeValue(xmlDoc, "STATUS2");
   								document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.value = getXMLNodeValue(xmlDoc, "STATUS");
   							}
                    		
                    		document.mainForm.AMOUNT_OF_TIME.value = getXMLNodeValue(xmlDoc,"STATUS1");
                    		document.mainForm.GUARANTEE_FIX_AMOUNT.readOnly=true;
    	           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.readOnly=false;
    	           			document.mainForm.GUARANTEE_AMOUNT.readOnly=true;
    	           			document.mainForm.GUARANTEE_AMOUNT.value=num_default_value;
    	           			document.mainForm.GUARANTEE_FIX_AMOUNT.value=num_default_value;
    	           			document.mainForm.GUARANTEE_INCLUDE_AMOUNT.value=num_default_value;
    	           			document.mainForm.INCLUDE_AMOUNT_PER_HOUR.value=num_default_value;
    	           			document.mainForm.INCLUDE_HOUR.value=num_default_value;
    	           			
                    	}
                    } 		
           	}
           	
           //+++++++++++++++++++++++++++++++++++++++ 20090819
           	//function chkStepSharing(){
            //}
           	 function amountType(){
           		AJAX_GUARANTEE_AMOUNT(document.mainForm.GUARANTEE_TYPE.value,"0","")
           	} 
           		
            function startDate(val){
            	if(val != ''){
	            	CheckDate(val,'START_DATE','END_DATE');
	            	if(toSaveDate(document.mainForm.START_DATE.value) > toSaveDate(document.mainForm.END_DATE.value)){
	            		//alert("Plese choose start date before end date.");
	            		document.mainForm.END_DATE.value = document.mainForm.START_DATE.value;
	            	} else {
	            		AJAX_GUARANTEE_AMOUNT(document.mainForm.GUARANTEE_TYPE.value,"0","");
	            	}
	            } 
            }
            
            function endDate(val) { 
            	if(val != '') { 
            		CheckEndDate(val,'START_DATE','END_DATE');
            		if(toSaveDate(document.mainForm.START_DATE.value) > toSaveDate(document.mainForm.END_DATE.value)){
                		//alert("Plese choose start date before end date.");
	            		document.mainForm.START_DATE.value = document.mainForm.END_DATE.value;
                	} else {
	            		AJAX_GUARANTEE_AMOUNT(document.mainForm.GUARANTEE_TYPE.value,"0","");
                	}
	            	return true;
            	}
            	return false;
            }
            
            function startTime(val){ 
            	//alert(document.getElementById(val.id).value);
                if(val != '') {
	                 if(!checkKeyTime(val)){
	                	 document.getElementById(val.id).value="";
	                 }
	                 
	                 if(!document.mainForm.EARLY_TIME.value == "" || !document.mainForm.EARLY_TIME.value =="00:00:00"){
	                	 document.mainForm.EARLY_TIME.value = val.value;
	                 }
	                 
	               	 if (document.mainForm.START_TIME.value != "" &&  document.mainForm.END_TIME.value != "") {
	               		 if(toSaveDate(document.mainForm.START_DATE.value) <= toSaveDate(document.mainForm.END_DATE.value)){
	                		if(document.mainForm.START_TIME.value > document.mainForm.END_TIME.value){
	                			alert("Plese choose start time before end time.");
	                		} else {
			                	AJAX_GUARANTEE_AMOUNT(document.mainForm.GUARANTEE_TYPE.value,"0","");
			                }
		                }
					 } 
                }
		    }
            
            function endTime(val){ 
				if(val) {
					if(!checkKeyTime(val)){
	                	 document.getElementById(val.id).value="";
	                 }
					if(!document.mainForm.LATE_TIME.value == "" || !document.mainForm.LATE_TIME.value =="00:00:00"){
	                	 document.mainForm.LATE_TIME.value = val.value;
	                 }
					
					if (document.mainForm.START_TIME.value != "" &&  document.mainForm.END_TIME.value != "") {
	               		 if(toSaveDate(document.mainForm.START_DATE.value) <= toSaveDate(document.mainForm.END_DATE.value)){
	                		if(document.mainForm.START_TIME.value > document.mainForm.END_TIME.value){
	                			alert("Plese choose start time before end time.");
	                		} else {
			                	AJAX_GUARANTEE_AMOUNT(document.mainForm.GUARANTEE_TYPE.value,"0","");
			                }
		                } 
					 }
					

				}
            }
            
            function guaranteeType(){
           		document.mainForm.AMOUNT_PER_TIME.value="";
           		if(document.mainForm.GUARANTEE_TYPE.value=="GA"){
           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.readOnly=true;
           			document.mainForm.GUARANTEE_AMOUNT.readOnly=false;
           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.value=num_default_value;
           			document.mainForm.GUARANTEE_AMOUNT.value=num_default_value;		

           		}else if(document.mainForm.GUARANTEE_TYPE.value=="GEA"){
           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.readOnly=false;
           			document.mainForm.GUARANTEE_AMOUNT.readOnly=true;
           			document.mainForm.GUARANTEE_AMOUNT.value=num_default_value;
           		}else{
           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.readOnly=false;
           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.value=num_default_value;
           			document.mainForm.GUARANTEE_AMOUNT.readOnly=false;
           			document.mainForm.GUARANTEE_AMOUNT.value=num_default_value;		
           		}
           		AJAX_GUARANTEE_AMOUNT(document.mainForm.GUARANTEE_TYPE.value,"0","");
	        }
            
            function guaranteeAmount(){
            	document.mainForm.AMOUNT_PER_TIME.value="0";
            } 
            
            function guaranteeExcludeAmount(){
            	document.mainForm.AMOUNT_PER_TIME.value="0";
            } 
            
            
            /* function amount_of_time(val) { 
            	if(val != '') { 
		           		if(document.mainForm.GUARANTEE_TYPE.value == "GA"){ // value  GUARANTEE_AMOUNT
		          			document.mainForm.GUARANTEE_AMOUNT.readOnly = true; 
		           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.readOnly=true;
		           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.value=num_default_value;
		           			document.mainForm.GUARANTEE_FIX_AMOUNT.readOnly=true;
		           			document.mainForm.GUARANTEE_FIX_AMOUNT.value=num_default_value;
		           			AJAX_GUARANTEE_AMOUNT("",val,"onblur");
		           		}else if(document.mainForm.GUARANTEE_TYPE.value == "GEA"){
		            		document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.readOnly=true;
		           			document.mainForm.GUARANTEE_AMOUNT.readOnly=true;
		           			document.mainForm.GUARANTEE_AMOUNT.value=num_default_value;           			
		           			document.mainForm.GUARANTEE_FIX_AMOUNT.readOnly=true;
		           			document.mainForm.GUARANTEE_FIX_AMOUNT.value=num_default_value;
		           			AJAX_GUARANTEE_AMOUNT("",val,"onblur");
		           		}else{
		           			document.mainForm.GUARANTEE_FIX_AMOUNT.readOnly=false;
		           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.readOnly=false;
		           			document.mainForm.GUARANTEE_AMOUNT.readOnly=false;
		           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.value=num_default_value;
		           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.value=num_default_value;
		           			document.mainForm.GUARANTEE_AMOUNT.value=num_default_value;
		           			document.mainForm.GUARANTEE_EXCLUDE_AMOUNT.value=num_default_value;
		           			document.mainForm.GUARANTEE_FIX_AMOUNT.value=num_default_value;
		           		}
	                } 
            } */
            
            //  set default value
           function setDefaultValue(num){
        	   	if($(num).val() == "" ){
        	   			 $(num).val(0);
        	   	} 
        	   	
           }
           
           function calIncludeGuarantee(){ 
        	   var amount_per_hour = 0;
        	   var hour = 0;
        	   
        	   if($("#INCLUDE_AMOUNT_PER_HOUR").val() != ''){ amount_per_hour = $("#INCLUDE_AMOUNT_PER_HOUR").val(); }
        	   
        	   if($("#INCLUDE_HOUR").val() != '') { hour = $("#INCLUDE_HOUR").val(); }
        	
        	   $("#GUARANTEE_INCLUDE_AMOUNT").val((amount_per_hour * hour).toFixed(2));	
        	   
           }
           
           
           $( document ).ready(function() {
        	    
        	   
        	   	jQuery('#INCLUDE_AMOUNT_PER_HOUR').keyup(function () { 
        		    this.value = this.value.replace(/[^0-9\.]/g,'');
        		});
        	   
        	 	jQuery('#INCLUDE_HOUR').keyup(function () { 
        		    this.value = this.value.replace(/[^0-9\.]/g,'');
        		});
        	 	
        		jQuery("#GUARANTEE_INCLUDE_AMOUNT").keyup(function () { 
        		    this.value = this.value.replace(/[^0-9\.]/g,'');
        		});
        		
        		jQuery("#AMOUNT_OF_TIME").keyup(function () { 
        		    this.value = this.value.replace(/[^0-9\.]/g,'');
        		});
      
        		jQuery("#OVER_ALLOCATE_PCT").keyup(function () { 
        		    this.value = this.value.replace(/[^0-9\.]/g,'');
        		});
        		
        		jQuery("#GUARANTEE_ALLOCATE_PCT").keyup(function () { 
        		    this.value = this.value.replace(/[^0-9\.]/g,'');
        		});
        		
        	   	jQuery("#INCLUDE_AMOUNT_PER_HOUR").keyup( function(){
        	   		calIncludeGuarantee();
        	   	});
        	   	
        	   	jQuery("#INCLUDE_HOUR").keyup( function(){
        	   		calIncludeGuarantee();
        	   	});
        	   	
        	});
           
        </script>
        <style type="text/css">
<!--
.style1 {color: #003399}
-->
        </style>
</head>    
    <body onload="AJAX_Verify_Check_Summary_Monthly();" >
        <form id="mainForm" name="mainForm" method="post" action="guarantee_detail.jsp">     
            <input type="hidden" size="50" id="EDIT_MODE_INSERTUPDATE" name="EDIT_MODE_INSERTUPDATE" value="" /><br/>
            <input type="hidden" size="50" id="EDIT_HOSPITAL_CODE" name="EDIT_HOSPITAL_CODE" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "HOSPITAL_CODE") %>" />
		   	<input type="hidden" size="50" id="EDIT_GUARANTEE_DR_CODE" name="EDIT_GUARANTEE_DR_CODE" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_DR_CODE") %>" />
            <input type="hidden" size="50" id="EDIT_GUARANTEE_CODE" name="EDIT_GUARANTEE_CODE" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_CODE") %>" />
            <input type="hidden" size="50" id="EDIT_GUARANTEE_TYPE_CODE" name="EDIT_GUARANTEE_TYPE_CODE" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_TYPE_CODE") %>" />
            <input type="hidden" size="50" id="EDIT_ADMISSION_TYPE_CODE" name="EDIT_ADMISSION_TYPE_CODE" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "ADMISSION_TYPE_CODE") %>" />
            <input type="hidden" size="50" id="EDIT_MM" name="EDIT_MM" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "MM") %>" />
            <input type="hidden" size="50" id="EDIT_YYYY" name="EDIT_YYYY" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "YYYY") %>" />
            <input type="hidden" size="50" id="EDIT_START_DATE" name="EDIT_START_DATE" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "START_DATE") %>" />
            <input type="hidden" size="50" id="EDIT_START_TIME" name="EDIT_START_TIME" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "START_TIME") %>" />
            <input type="hidden" size="50" id="EDIT_END_DATE" name="EDIT_END_DATE" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "END_DATE") %>" />
            <input type="hidden" size="50" id="EDIT_END_TIME" name="EDIT_END_TIME" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "END_TIME") %>" />
            <input type="hidden" id="MODE" name="MODE" value="<%= MODE %>" />
            <table class="form">
                <tr><th colspan="4">${labelMap.TITLE_MAIN}</th></tr>
                <tr>
                    <td class="label">
                        <label for="GUARANTEE_DR_CODE"><span class="style1">${labelMap.G_DR_CODE} *</span></label>                    </td>
                    <td class="input" colspan="3">
                        <input type="text" id="GUARANTEE_DR_CODE" name="GUARANTEE_DR_CODE" class="short" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" />
                        <input type="text" id="GUARANTEE_DR_NAME" name="GUARANTEE_DR_NAME" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME") %>" />                    </td>
                </tr>
                <tr>
                  <td class="label">
                    <label for="MM"><span class="style1">${labelMap.MM} *</span></label>                    </td>
                    <td class="input">
                        <input type="text" id="MM" name="MM" class="short" value="<%= stpGuaranteeRec == null ? MM : DBMgr.getRecordValue(stpGuaranteeRec, "MM") %>" readonly="readonly" />                    </td>
                  <td class="label">
                    <label for="YYYY"><span class="style1">${labelMap.YYYY} *</span></label>                    </td>
                    <td class="input">
                        <input type="text" id="YYYY" name="YYYY" class="short" value="<%= stpGuaranteeRec == null ? YYYY : DBMgr.getRecordValue(stpGuaranteeRec, "YYYY") %>" readonly="readonly" />                    </td>
                </tr>
                <input type="hidden" name="GUARANTEE_CODE" value="<%=DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_CODE")%>" />
                <tr>
                    <td class="labelRequest"><label for="GUARANTEE_TYPE_CODE">${labelMap.G_TYPE_CODE} *</label></td>
					<td colspan="" class="input">
                        <%= DBMgr.generateDropDownList("GUARANTEE_TYPE_CODE", "medium", "inActive", "SELECT CODE, DESCRIPTION, ACTIVE FROM GUARANTEE_TYPE WHERE ACTIVE = '1' ORDER BY DESCRIPTION", "DESCRIPTION", "CODE", DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_TYPE_CODE")=="" ?"DLY" :DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_TYPE_CODE")) %>
					</td>
					<td class="labelRequest"><label for="ADMISSION_TYPE_CODE">${labelMap.ADMISSION} *</label></td>
                    <td colspan="" class="input">
						<select id="ADMISSION_TYPE_CODE" name="ADMISSION_TYPE_CODE" class="short">
							<option value="U"<%= DBMgr.getRecordValue(stpGuaranteeRec, "ADMISSION_TYPE_CODE").equalsIgnoreCase("U") ? " selected=\"selected\"" : "" %>>ALL</option>
							<option value="I"<%= DBMgr.getRecordValue(stpGuaranteeRec, "ADMISSION_TYPE_CODE").equalsIgnoreCase("I") ? " selected=\"selected\"" : "" %>>IPD</option>
							<option value="O"<%= DBMgr.getRecordValue(stpGuaranteeRec, "ADMISSION_TYPE_CODE").equalsIgnoreCase("O") ? " selected=\"selected\"" : "" %>>OPD</option>
                        </select>
                    </td>
				</tr>
                <tr>
                    <td class="label">
                        <label for="GUARANTEE_LOCATION">${labelMap.G_LOCATION}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input name="GUARANTEE_LOCATION_CODE" type="text" class="short" id="GUARANTEE_LOCATION_CODE" onblur="AJAX_Refresh_GUARANTEE_LOCATION();" onkeypress="return GUARANTEE_LOCATION_CODE_KeyPress(event);" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_LOCATION") %>" maxlength="20" />
                        <input id="SEARCH_GUARANTEE_LOCATION_CODE" name="SEARCH_GUARANTEE_LOCATION_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&TARGET=GUARANTEE_LOCATION_CODE&DISPLAY_FIELD=DESCRIPTION&BEACTIVE=1&HANDLE=AJAX_Refresh_GUARANTEE_LOCATION'); return false;" />
                        <input type="text" id="GUARANTEE_LOCATION_DESCRIPTION" name="GUARANTEE_LOCATION_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(location, "DESCRIPTION") %>" />
                   	 	
                   	 	<input type="radio" id="IS_INCLUDE_LOCATION_1" name="IS_INCLUDE_LOCATION"  value="Y" <%=DBMgr.getRecordValue(stpGuaranteeRec, "IS_INCLUDE_LOCATION").equalsIgnoreCase("Y") ? " checked=\"checked\"" : "" %> />
                        <label for="IS_INCLUDE_LOCATION_1">${labelMap.IS_INCLUDE_LOCATION_1}</label>
                        <input type="radio" id="IS_INCLUDE_LOCATION_0" name="IS_INCLUDE_LOCATION"  value="N" <%=DBMgr.getRecordValue(stpGuaranteeRec, "IS_INCLUDE_LOCATION").equalsIgnoreCase("N") || DBMgr.getRecordValue(stpGuaranteeRec, "IS_INCLUDE_LOCATION").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="IS_INCLUDE_LOCATION">${labelMap.IS_INCLUDE_LOCATION_0}</label>
                   </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="GL_DEPARTMENT_CODE">${labelMap.GL_DEPARTMENT_CODE}</label>                    </td>
                    <td class="input" colspan="3">
                        <input name="GL_DEPARTMENT_CODE" type="text" class="short" id="GL_DEPARTMENT_CODE" onblur="AJAX_Refresh_GL_DEPARTMENT();" onkeypress="return GL_DEPARTMENT_CODE_KeyPress(event);" 
                        	   value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "GL_DEPARTMENT_CODE").equalsIgnoreCase("") ?  
                        			   	  DBMgr.getRecordValue(doctorRec, "DEPARTMENT_CODE") : DBMgr.getRecordValue(stpGuaranteeRec, "GL_DEPARTMENT_CODE") %>" maxlength="20" />
                        <input id="SEARCH_GL_DEPARTMENT_CODE" name="SEARCH_GL_DEPARTMENT_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DEPARTMENT&TARGET=GL_DEPARTMENT_CODE&DISPLAY_FIELD=DESCRIPTION&BEACTIVE=1&HANDLE=AJAX_Refresh_GL_DEPARTMENT'); return false;" />
                        <input type="text" id="GL_DEPARTMENT_DESCRIPTION" name="GL_DEPARTMENT_DESCRIPTION" class="mediumMax" readonly="readonly" value="<%= DBMgr.getRecordValue(department, "DESCRIPTION") %>" />
                   </td>
                </tr>
                <tr>
                  <td class="label">
                    <label for="START_DATE"><span class="style1">${labelMap.START_DATE}* </span></label>                    </td>
                    <td class="input">
                        <input name="START_DATE" type="text" class="short" id="START_DATE" maxlength="10" value="<%= JDate.showDate(DBMgr.getRecordValue(stpGuaranteeRec, "START_DATE"))%>" onblur="return startDate(this.value)" />
                        <input name="HID_START_DATE" type="hidden" class="short" id="HID_START_DATE" maxlength="10" value="<%= JDate.showDate(DBMgr.getRecordValue(stpGuaranteeRec, "START_DATE"))%>" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('START_DATE'); return false;" />
                        </td>
                    <td class="labelRequest">
                        <label for="END_DATE">${labelMap.END_DATE} *</label>                    </td>
                    <td class="input">
                        <input name="END_DATE" type="text" class="short" id="END_DATE" maxlength="10" value="<%= JDate.showDate(DBMgr.getRecordValue(stpGuaranteeRec, "END_DATE"))%>" onblur="return endDate(this.value)" />
                        <input name="HID_END_DATE" type="hidden" class="short" id="HID_END_DATE" maxlength="10" value="<%= JDate.showDate(DBMgr.getRecordValue(stpGuaranteeRec, "END_DATE"))%>" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('END_DATE'); return false;" />
                        </td>
                </tr>
                <tr>
                  <td class="label">
                    <label for="START_TIME"><span class="style1">${labelMap.START_TIME} *</span></label>                    </td>
                    <td class="input">
                        <input name="START_TIME" type="text" class="short" id="START_TIME" maxlength="4" value="<%= JDate.showTime(DBMgr.getRecordValue(stpGuaranteeRec, "START_TIME")) %>" onchange="return startTime(this);" />
                        HHMM
                    </td>
                    <td class="labelRequest">
                        <label for="bText">${labelMap.END_TIME} *</label>                    </td>
                    <td class="input">
                        <input name="END_TIME" type="text" class="short" id="END_TIME" maxlength="4" value="<%= JDate.showTime(DBMgr.getRecordValue(stpGuaranteeRec, "END_TIME")) %>"  onblur="return endTime(this);" />
                        HHMM
                    </td>
                </tr>
                <tr>
                    <td class="label">
                    <label for="START_TIME">${labelMap.EARLY_TIME}</label>                    </td>
                    <td class="input"><input name="EARLY_TIME" type="text" class="short" id="EARLY_TIME" maxlength="4" value="<%= JDate.showTime(DBMgr.getRecordValue(stpGuaranteeRec, "EARLY_TIME")) %>"  onchange="return startTime(this);" /> HHMM</td>
                    <td class="label">
                        <label for="bText">${labelMap.LATE_TIME}</label>                    </td>
                    <td class="input">
                        <input name="LATE_TIME" type="text" class="short" id="LATE_TIME" maxlength="4" value="<%= JDate.showTime(DBMgr.getRecordValue(stpGuaranteeRec, "LATE_TIME")) %>"  onchange="return endTime(this);" /> HHMM</td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="AMOUNT_PER_TIME">${labelMap.AMOUNT_PER_TIME}</label>                    </td>
                    <td class="input" colspan="1">
                        <input  name="AMOUNT_PER_TIME" type="text" class="short alignRight" id="AMOUNT_PER_TIME" maxlength="13" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "AMOUNT_PER_TIME") %>"   onblur="return amountType(this.value)"/> Baht</td>
                	<td class="input" colspan="1">
                		<input name="AMOUNT_OF_TIME" type="text" class="short alignRight" id="AMOUNT_OF_TIME" maxlength="13" value = "<%=DBMgr.getRecordValue(stpGuaranteeRec, "AMOUNT_OF_TIME") %>" readonly="readonly" /> Hour
                	</td>
                	<td class="input" colspan="1">
                		<select id="GUARANTEE_TYPE" name="GUARANTEE_TYPE" class="medium" onchange="guaranteeType()">
                			<option value="NONE">--- Amount Type ---</option>
                			<option value="GA" <%
                				 if(!"".equals(DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_AMOUNT"))){ 
                					 if(!"0.00".equals(DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_AMOUNT"))){
                						 out.print(" selected='selected'");
                					 }else{ 
                						 out.print("");
                					 }
                				 } else { 
                					 out.print("");
                				 }
                					%>>Guarantee Amount</option>

                            <option value="GEA" <%
                       		if(!"".equals(DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_EXCLUDE_AMOUNT"))){ 
                            	  if(!"0.00".equals(DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_EXCLUDE_AMOUNT"))){
                            		  out.print(" selected='selected'");  
                            	  } else {
                            		  out.print("");
                            	  }
                            } else { 
                            		 out.print("");
                            }
									%>>Extra Amount</option>
                        </select>
                    </td>
                </tr>
                <tr>
                <td class="label">
                        <label for="GUARANTEE_AMOUNT">${labelMap.G_AMOUNT}</label>                    </td>
                    <td class="input">
                        <input name="GUARANTEE_AMOUNT" type="text" class="short alignRight" id="GUARANTEE_AMOUNT" maxlength="13" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_AMOUNT") %>" onchange="return guaranteeAmount(this);"/> Baht</td>
                   
                	 <td class="label">
                        <label for="TAX_TYPE_CODE">${labelMap.GUARANTEE_TAX_TYPE}</label></td>
                     <td class="input">
                        <select id="TAX_TYPE_CODE" name="TAX_TYPE_CODE" class="medium">
                            <option value=""<%= DBMgr.getRecordValue(stpGuaranteeRec, "TAX_TYPE_CODE").equalsIgnoreCase("") ? " selected=\"selected\"" : "" %>>--- ไม่กำหนด ---</option>
                            <option value="402" <%= DBMgr.getRecordValue(stpGuaranteeRec, "TAX_TYPE_CODE").equalsIgnoreCase("402") ? " selected=\"selected\"" : "" %>>ภาษี 40(2) </option>
                            <option value="406"<%= DBMgr.getRecordValue(stpGuaranteeRec, "TAX_TYPE_CODE").equalsIgnoreCase("406") ? " selected=\"selected\"" : "" %>>ภาษี 40(6) </option>
                        </select>  
                     </td>
                </tr>
                <tr>
                <td class="label">
                        <label for="GUARANTEE_EXCLUDE_AMOUNT">${labelMap.EXCLUDE_AMOUNT}</label></td>
                    <td class="input">
                        <input name="GUARANTEE_EXCLUDE_AMOUNT" type="text" class="short alignRight" id="GUARANTEE_EXCLUDE_AMOUNT" maxlength="13" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_EXCLUDE_AMOUNT") %>" onchange="return guaranteeExcludeAmount(this);" /> Baht</td>
                  
                     <td class="label"><label for="ABSORB_AMOUNT">${labelMap.ABSORB_AMOUNT}</label></td>
                    <td class="input"><input name="ABSORB_AMOUNT" type="text" class="short alignRight" readonly="readonly" id="ABSORB_AMOUNT" maxlength="13" value="<%= JNumber.getShowMoney(Double.parseDouble(DBMgr.getRecordValue(stpGuaranteeRec, "HP402_ABSORB_AMOUNT").equalsIgnoreCase("") ? "0" : DBMgr.getRecordValue(stpGuaranteeRec, "HP402_ABSORB_AMOUNT"))) %>" /> Baht</td>
                </tr>
                <tr>
                	<td class="label">
                        <label for="GUARANTEE_FIX_AMOUNT">${labelMap.FIX_AMOUNT}</label>                    </td>
                    <td class="input">
                        <input name="GUARANTEE_FIX_AMOUNT" type="text" class="short alignRight" id="GUARANTEE_FIX_AMOUNT" maxlength="13" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_FIX_AMOUNT") %>" /> Baht</td>
                   
                      <td class="label"><label for="ABSORB_REMAIN_AMOUNT">${labelMap.ABSORB_REMAIN_AMOUNT}</label>                    </td>
                    <td class="input"><input name="ABSORB_REMAIN_AMOUNT" type="text" class="short alignRight" readonly="readonly" id="ABSORB_REMAIN_AMOUNT" maxlength="13" value="<%= JNumber.getShowMoney(Double.parseDouble(DBMgr.getRecordValue(stpGuaranteeRec, "DF_ABSORB_AMOUNT").equalsIgnoreCase("") ? "0" : DBMgr.getRecordValue(stpGuaranteeRec, "DF_ABSORB_AMOUNT"))) %>" /> Baht</td>
				</tr>
				
				<tr>
                    <td class="label">
                        <label for="GUARANTEE_INCLUDE_AMOUNT">${labelMap.INCLUDE_AMOUNT}</label>
                    </td>
                    <td class="input" colspan="1">
                        <input name="GUARANTEE_INCLUDE_AMOUNT" type="text" class="short alignRight" id="GUARANTEE_INCLUDE_AMOUNT" maxlength="13" value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_INCLUDE_AMOUNT") %>" /> ${labelMap.BAHT}
                    </td>
                    <td class="input" colspan="1">
                		<input name="INCLUDE_AMOUNT_PER_HOUR" type="text" class="short alignRight" id="INCLUDE_AMOUNT_PER_HOUR" maxlength="13" value = "<%=DBMgr.getRecordValue(stpGuaranteeRec, "INCLUDE_PER_TIME") %>" /> ${labelMap.BAHT_PER_HOUR}
                	</td>
                    <td class="input" colspan="1">
                		<input name="INCLUDE_HOUR" type="text" class="short alignRight" id="INCLUDE_HOUR" maxlength="13" value = "<%=DBMgr.getRecordValue(stpGuaranteeRec, "INCLUDE_OF_TIME") %>" /> ${labelMap.HOUR}
                	</td>
                </tr>				
				<tr>      
				<td class="labelRequest"> 
                        <label for="GUARANTEE_ALLOCATE_PCT">${labelMap.GUARANTEE_ALLOCATE_PCT} *</label>                    </td>
                    <td class="input">
                        <input name="GUARANTEE_ALLOCATE_PCT" type="text" class="short alignRight" id="GUARANTEE_ALLOCATE_PCT" maxlength="13" 
                        value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_ALLOCATE_PCT").equalsIgnoreCase("") ||
                        		   DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_ALLOCATE_PCT").equalsIgnoreCase("0") ? 
                        		   DBMgr.getRecordValue(doctorRec, "IN_GUARANTEE_PCT") : 
                        		   DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_ALLOCATE_PCT") %>" /> %
                    </td> 
                                      
                     <td class="label"><label for="GUARANTEE_SOURCE">${labelMap.GUARANTEE_SOURCE}</label></td>
                    <td class="input">
                        <select id="GUARANTEE_SOURCE" name="GUARANTEE_SOURCE" class="medium">
                            <option value=""<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_SOURCE").equalsIgnoreCase("") && DBMgr.getRecordValue(doctorRec, "GUARANTEE_SOURCE").equalsIgnoreCase("") ? " selected=\"selected\"" : "" %>>--- No Specify ---</option>
                            <option value="${labelMap.GUARANTEE_SOURCE_TAX_VALUE}"<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_SOURCE").equals("BF") ? " selected=\"selected\"" : DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_SOURCE").equals("") && DBMgr.getRecordValue(doctorRec, "GUARANTEE_SOURCE").equals("BF") ? " selected=\"selected\"" : "" %>>${labelMap.GUARANTEE_SOURCE_TAX}</option>
                            <option value="${labelMap.GUARANTEE_SOURCE_DF_VALUE}"<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_SOURCE").equals("AF") ? " selected=\"selected\"" : DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_SOURCE").equals("") && DBMgr.getRecordValue(doctorRec, "GUARANTEE_SOURCE").equals("AF") ? " selected=\"selected\"" : "" %>>${labelMap.GUARANTEE_SOURCE_DF}</option>
                        </select>                   
                    </td>
                </tr>
                <tr>
                    <td class="labelRequest"><label for="OVER_ALLOCATE_PCT">${labelMap.OVER_ALLOCATE_PCT} *</label></td>
                    <td class="input">
                        <input name="OVER_ALLOCATE_PCT" type="text" class="short alignRight" id="OVER_ALLOCATE_PCT" maxlength="13" 
                        value="<%= DBMgr.getRecordValue(stpGuaranteeRec, "OVER_ALLOCATE_PCT").equalsIgnoreCase("") || 
                        		   DBMgr.getRecordValue(stpGuaranteeRec, "OVER_ALLOCATE_PCT").equalsIgnoreCase("0") ?
                        		   DBMgr.getRecordValue(doctorRec, "OVER_GUARANTEE_PCT") : 
                        		   DBMgr.getRecordValue(stpGuaranteeRec, "OVER_ALLOCATE_PCT") %>" /> %
                        		
                    </td>
                    
                    <td class="label"><label for="GUARANTEE_DAY">${labelMap.GUARANTEE_DAY}</label></td>
                    <td class="input">
                        <select id="GUARANTEE_DAY" name="GUARANTEE_DAY" class="medium">
                            <option value=""<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_DAY").equalsIgnoreCase("") ? " selected=\"selected\"" : "" %>>--- No Specify ---</option>
							<option value="INV"<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_DAY").equalsIgnoreCase("INV") ? " selected=\"selected\"" : DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_DAY").equals("") && DBMgr.getRecordValue(doctorRec, "GUARANTEE_DAY").equals("INV") ? " selected=\"selected\"" : "" %>>Invoice Date</option>
							<option value="VER"<%= DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_DAY").equalsIgnoreCase("VER") ? " selected=\"selected\"" : DBMgr.getRecordValue(stpGuaranteeRec, "GUARANTEE_DAY").equals("") && DBMgr.getRecordValue(doctorRec, "GUARANTEE_DAY").equals("VER") ? " selected=\"selected\"" : "" %>>Execute Date</option>
                        </select>                   
                    </td>
                </tr>
			    <tr>
			         
			        <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td> 
                    <td class="input" colspan="3">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1" <%=DBMgr.getRecordValue(stpGuaranteeRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(stpGuaranteeRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : ""%> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0" <%=DBMgr.getRecordValue(stpGuaranteeRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : ""  %> />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>
					</td>
                </tr>
                <tr>
               		<td class="label"><label for="Note">${labelMap.Note}</label></td>
                    <td class="input"colspan="3">
                    <textarea  name="NOTE" id="NOTE"  rows="2" cols="60"><%= DBMgr.getRecordValue(stpGuaranteeRec, "NOTE")%></textarea></td> 
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">                        
                    	<input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="return SAVE_Click()" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location.href='guarantee_main.jsp?GUARANTEE_DR_CODE=<%=request.getParameter("GUARANTEE_DR_CODE")%>&MM=<%=request.getParameter("MM")%>&YYYY=<%=request.getParameter("YYYY")%>&HOSPITAL_CODE=<%=session.getAttribute("HOSPITAL_CODE")%>'" />                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>