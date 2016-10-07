<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="java.sql.Types"%>
<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //
       
            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_DEPARTMENT)) {
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
            labelMap.add("TITLE_MAIN", "Hospital Setup", "ตั้งค่าโรงพยาบาล");
            labelMap.add("CODE", "Hospital Code", "รหัสโรงพยาบาล");
            labelMap.add("DESCRIPTION_THAI","Description Thai","");
            labelMap.add("DESCRIPTION_ENG","Description ENG","");
            labelMap.add("ADDRESS1","ADDRESS1","");
            labelMap.add("ADDRESS2","ADDRESS2","");
            labelMap.add("ADDRESS3","ADDRESS3","");
            labelMap.add("ZIP","Zip","รหัสไปรษณีย์");
            labelMap.add("TEL","Tel","โทรศัพท์");
            labelMap.add("EMAIL","Email","อีเมล์");
            labelMap.add("TAXNO","Tax No.","เลขประจำตัวผู้เสียภาษี");
            labelMap.add("COMPANY_NAME","Company Name","ชื่อบริษัท");
            labelMap.add("BANK_CODE", "Bank Code", "รหัสธนาคาร"); 
            labelMap.add("BANK_BRANCH_CODE", "Branch Code", "รหัสสาขา");
            labelMap.add("ACCOUNT_NO", "Bank Account No", "เลขที่บัญชี");
            labelMap.add("CREATE_DATE", "Create Date", "วันที่สร้างข้อมูล");
            labelMap.add("CREATE_TIME", "Create Time", "เวลาในการสร้างข้อมูล");
            labelMap.add("UPDATE_DATE","UPDATE_DATE","วันที่มีการแก้ไขข้อมูล");
            labelMap.add("UPDATE_TIME","UPDATE_TIME","เวลาในการแก้ไขข้อมูล");
            labelMap.add("ACTIVE","ACTIVE","สถานะใช้งาน");
            labelMap.add("USER_ID","USER_ID","");
            labelMap.add("OF_ADDRESSNO","Address No.","เลขที่");
            labelMap.add("OF_MOO","Moo No.","หมู่ที่");
            labelMap.add("OF_BUILDING_NAME","Building Name","อาคาร");
            labelMap.add("OF_BUILDING_NO","Building No.","เลขที่ห้อง");
            labelMap.add("OF_BUILDING_FLOOR","Building Floor","ชั้นที่");
            labelMap.add("OF_VILLAGE","Village","หมู่บ้าน");
            labelMap.add("OF_SOI","Soi","ซอย");
            labelMap.add("OF_ROAD","Road","ถนน");
            labelMap.add("OF_SUBDISTRICT","Sub District","ตำบล");
            labelMap.add("OF_DISTRICT","District","อำเภอ");
            labelMap.add("OF_PROVINCE","Province","จังหวัด");
            labelMap.add("COMPANY_NAME1","Company Name1","ชื่อบริษัท");
            labelMap.add("TAX_END_MONTH_OF_YEAR","Tax 40(2) Month End","เดือนสิ้นสุดการคำนวนภาษีรอบปี");
            labelMap.add("CITI_PARTY_NAME","(Citi Bank) Party Name","(Citi Bank) Party Name");
            labelMap.add("CITI_PARTY_ID","(Citi Bank) Party ID","(Citi Bank) Party ID");
            labelMap.add("CITI_PARTY_AC","(Citi Bank) Account","(Citi Bank) Account");
            labelMap.add("GUARANTEE_INCLUDE_EXTRA","Guarantee Include Extra","Guarantee Include Extra");
            labelMap.add("GUARANTEE_DAY","Guarantee Day","Guarantee Day");
            labelMap.add("GUARANTEE_ALL_ALLOC","Guarantee Sharing Refund","คืนส่วนแบ่ง รพ.กรณีไม่ถึงการันตี");
            labelMap.add("GL_ACCOUNT_CODE","GL Accunt Code","GL Account Code");
            labelMap.add("AC_ACCOUNT_CODE","AC Account Code","AC Account Code");
            labelMap.add("SHARING_ACCOUNT" , " Sharing Account Code " , " Sharing Account Code ");
            labelMap.add("EARNING_ACCOUNT" , " Earning Account Code " , " Earning Account Code ");
            labelMap.add("IS_ONWARD","Import Onward / Mobile Checkup","นำเข้า Onward และ Mobile Checkup");
            labelMap.add("IS_PARTIAL","Partial Payment","แบ่งชำระหนี้]");
            labelMap.add("IS_COMBINE_BILL","Combine Bill","รวมบิล");
            labelMap.add("IS_COMBINE_BILL_0","No","ไม่รวมบิล");
            labelMap.add("IS_COMBINE_BILL_1","Yes","รวมบิล");

            labelMap.add("DISCHARGE_BASIS","Discharge Basis","ทำจ่ายค่าแพทย์ในการันตี");
            labelMap.add("DISCHARGE_BASIS_0","No","รอรับชำระ");
            labelMap.add("DISCHARGE_BASIS_1","Yes","ทำจ่ายทั้งหมด");
            labelMap.add("GUARANTEE_ONWARD","Guarantee Onward","คำนวณการันตีรายการ Onward");
            labelMap.add("GUARANTEE_ONWARD_0","No","ไม่คำนวณ");
            labelMap.add("GUARANTEE_ONWARD_1","Yes","คำนวณ");

            labelMap.add("IS_PARTIAL_0","No","ไม่ทำงาน");
            labelMap.add("IS_PARTIAL_1","Yes","ทำงาน");
            
            
            labelMap.add("IS_ONWARD_0","No","ไม่คำนวน");
            labelMap.add("IS_ONWARD_1","Yes","คำนวน");
            
            labelMap.add("GUARANTEE_ALL_ALLOC_0","No","คืน");
            labelMap.add("GUARANTEE_ALL_ALLOC_1","Yes","ไม่คืน");
            
            labelMap.add("GUARANTEE_DAY_0","INV","วันที่ออกบิล");
            labelMap.add("GUARANTEE_DAY_1","VER","วันที่แพทย์คีย์");
            
            labelMap.add("GUARANTEE_INCLUDE_EXTRA_0","No","ไม่รวม");
            labelMap.add("GUARANTEE_INCLUDE_EXTRA_1","Yes","รวม");
            
            
            labelMap.add("MONTH1","January","มกราคม");
            labelMap.add("MONTH2","February","กุมภาพันธ์");
            labelMap.add("MONTH3","March","มีนาคม");
            labelMap.add("MONTH4","April","เมษายน");
            labelMap.add("MONTH5","May","พฤษภาคม");
            labelMap.add("MONTH6","June","มิถุนายน");
            labelMap.add("MONTH7","July","กรกฏาคม");
            labelMap.add("MONTH8","August","สิงหาคม");
            labelMap.add("MONTH9","September","กันยายน");
            labelMap.add("MONTH10","October","ตุลาคม");
            labelMap.add("MONTH11","November","พฤศจิกายน");
            labelMap.add("MONTH12","December","ธันวาคม");
            
            //labelMap.add("CONTRACT_NAME_THAI","CONTRACT_NAME_THAI","");
            ///labelMap.add("CONTRACT_NAME_ENG","CONTRACT_NAME_ENG","");
            //labelMap.add("CONTRACT_POSITION","CONTRACT_POSITION","");
           /// labelMap.add("CONTRACT_TEL","CONTRACT_TEL","");
            //labelMap.add("CONTRACT_EMAIL","CONTRACT_EMAIL","");
      
        
   


          
           // labelMap.add("ALERT_BANK","Plase select BANK","กรุณาเลือกธนาคาร");
            
            //-------- Add by Tong
          


          
       
            request.setAttribute("labelMap", labelMap.getHashMap());

            String[] langValue = {labelMap.get("langThai"),labelMap.get("langEng")};
            String[] langName = {"T","E"};

            /*
            Type of Doctor
            1	System Admin 	-> All

            2	Manager		->
                    - มีหน้าที่ตรวจสอบข้อมูลและทำการ Active รหัสแพทย์นั้นๆ
                    - ไม่สามารถแก้ไขข้อมูลทั้งหมดได้ยกเว้น Active
            3	Organize Doctor	->
                    - สามารถดูข้อมูลและเพิ่มเติมเปลี่ยนแปลงแก้ไขได้ทั้งหมด
                    - ไม่สามารถ Active รหัสแพทย์นั้นได้ (ในกรณีเพิ่มหรือแก้ไข ให้ Set เป็น InActive)
            4	DF User(IT)	->
                    - สามารถดูข้อมูลได้ทั้งหมดแต่ไม่สามารถทำการเปลี่ยนแปลงแก้ไข

            5	User(Doctor)	->
            */
            String CODE = "";
            String disabledManager = "";
            request.setCharacterEncoding("UTF-8");
           
            DataRecord HOSPITALRec = null, hospitalUnitRec = null;
            byte MODE = DBMgr.MODE_INSERT;
            
            if (request.getParameter("MODE") != null) {
                HOSPITALRec = new DataRecord("HOSPITAL");
                HOSPITALRec.addField("CODE", Types.VARCHAR, request.getParameter("CODE"), true);
                HOSPITALRec.addField("DESCRIPTION_THAI", Types.VARCHAR, request.getParameter("DESCRIPTION_THAI"));
                HOSPITALRec.addField("DESCRIPTION_ENG", Types.VARCHAR, request.getParameter("DESCRIPTION_ENG"));
                HOSPITALRec.addField("ADDRESS1", Types.VARCHAR, request.getParameter("OF_ADDRESSNO")+" "+request.getParameter("OF_SOI")+" "+request.getParameter("OF_MOO"));
                HOSPITALRec.addField("ADDRESS2", Types.VARCHAR, request.getParameter("OF_ROAD")+" "+request.getParameter("OF_SUBDISTRICT"));
                HOSPITALRec.addField("ADDRESS3", Types.VARCHAR, request.getParameter("OF_DISTRICT")+" "+request.getParameter("OF_PROVINCE"));
                HOSPITALRec.addField("ZIP", Types.VARCHAR, request.getParameter("ZIP"));
                HOSPITALRec.addField("TEL", Types.VARCHAR, request.getParameter("TEL"));
                HOSPITALRec.addField("EMAIL", Types.VARCHAR, request.getParameter("EMAIL"));
                HOSPITALRec.addField("TAXNO", Types.VARCHAR, request.getParameter("TAXNO"));
                HOSPITALRec.addField("COMPANY_NAME", Types.VARCHAR, request.getParameter("COMPANY_NAME"));
                HOSPITALRec.addField("BANK_CODE", Types.VARCHAR, request.getParameter("BANK_CODE"));
                HOSPITALRec.addField("BANK_BRANCH_CODE", Types.VARCHAR, request.getParameter("BANK_BRANCH_CODE"));
                HOSPITALRec.addField("ACCOUNT_NO", Types.VARCHAR, request.getParameter("ACCOUNT_NO"));
                HOSPITALRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                HOSPITALRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                HOSPITALRec.addField("ACTIVE", Types.VARCHAR, request.getParameter("ACTIVE"));
                HOSPITALRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                HOSPITALRec.addField("OF_ADDRESSNO", Types.VARCHAR, request.getParameter("OF_ADDRESSNO"));
                HOSPITALRec.addField("OF_MOO", Types.VARCHAR, request.getParameter("OF_MOO"));
                HOSPITALRec.addField("OF_BUILDER", Types.VARCHAR, request.getParameter("OF_BUILDING_NAME"));
                HOSPITALRec.addField("OF_BUILDER_NUMBER", Types.VARCHAR, request.getParameter("OF_BUILDING_NO"));
                HOSPITALRec.addField("OF_BUILDER_FLOOR", Types.VARCHAR, request.getParameter("OF_BUILDING_FLOOR"));
                HOSPITALRec.addField("OF_VILLAGE", Types.VARCHAR, request.getParameter("OF_VILLAGE"));
                HOSPITALRec.addField("OF_SOI", Types.VARCHAR, request.getParameter("OF_SOI"));
                HOSPITALRec.addField("OF_ROAD", Types.VARCHAR, request.getParameter("OF_ROAD"));
                HOSPITALRec.addField("OF_SUBDISTRICT", Types.VARCHAR, request.getParameter("OF_SUBDISTRICT"));
                HOSPITALRec.addField("OF_DISTRICT", Types.VARCHAR, request.getParameter("OF_DISTRICT"));
                HOSPITALRec.addField("OF_PROVINCE", Types.VARCHAR, request.getParameter("OF_PROVINCE"));
                HOSPITALRec.addField("COMPANY_NAME1", Types.VARCHAR, request.getParameter("COMPANY_NAME"));
                HOSPITALRec.addField("TAX_END_MONTH_OF_YEAR", Types.VARCHAR, request.getParameter("TAX_END_MONTH_OF_YEAR"));
                HOSPITALRec.addField("CITI_PARTY_NAME", Types.VARCHAR, request.getParameter("CITI_PARTY_NAME"));
                HOSPITALRec.addField("CITI_PARTY_ID", Types.VARCHAR, request.getParameter("CITI_PARTY_ID"));
                HOSPITALRec.addField("CITI_PARTY_AC", Types.VARCHAR, request.getParameter("CITI_PARTY_AC"));
                HOSPITALRec.addField("GUARANTEE_INCLUDE_EXTRA", Types.VARCHAR, request.getParameter("GUARANTEE_INCLUDE_EXTRA"));    
                HOSPITALRec.addField("GUARANTEE_DAY", Types.VARCHAR, request.getParameter("GUARANTEE_DAY"));    
                HOSPITALRec.addField("GL_ACCOUNT_CODE", Types.VARCHAR, request.getParameter("GL_ACCOUNT_CODE"));    
                HOSPITALRec.addField("AC_ACCOUNT_CODE", Types.VARCHAR, request.getParameter("AC_ACCOUNT_CODE")); 
                HOSPITALRec.addField("SHARING_ACCOUNT", Types.VARCHAR, request.getParameter("SHARING_ACCOUNT"));
                HOSPITALRec.addField("EARNING_ACCOUNT", Types.VARCHAR ,request.getParameter("EARNING_ACCOUNT"));
                HOSPITALRec.addField("GUARANTEE_ALL_ALLOC", Types.VARCHAR, request.getParameter("GUARANTEE_ALL_ALLOC"));    
                HOSPITALRec.addField("IS_DISCHARGE_BASIS", Types.VARCHAR, request.getParameter("DISCHARGE_BASIS"));    
                HOSPITALRec.addField("IS_GUARANTEE_ONWARD", Types.VARCHAR, request.getParameter("GUARANTEE_ONWARD"));    
                HOSPITALRec.addField("IS_ONWARD", Types.VARCHAR, request.getParameter("IS_ONWARD"));    
                HOSPITALRec.addField("IS_PARTIAL", Types.VARCHAR, request.getParameter("IS_PARTIAL"));    
                HOSPITALRec.addField("IS_JOIN_BILL", Types.VARCHAR, request.getParameter("IS_COMBINE_BILL"));    
                
                //out.println(request.getParameter("MODE"));
                if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_INSERT) {
                    //out.println("Insert");
                    if (DBMgr.insertRecord(HOSPITALRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/hospital.jsp"));
                    }else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }
                else if (Byte.parseByte(request.getParameter("MODE")) == DBMgr.MODE_UPDATE) {
                    //out.println("Update");
                    if (DBMgr.updateRecord(HOSPITALRec)) {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/hospital.jsp"));
                    }else {
                        session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                    }
                }
                //return;
                response.sendRedirect("../message.jsp");
                return;
            }
            else if (session.getAttribute("HOSPITAL_CODE")!=null || CODE != "") {
                if(session.getAttribute("HOSPITAL_CODE")==null){
                    HOSPITALRec = DBMgr.getRecord("SELECT * FROM HOSPITAL WHERE CODE = '" + CODE + "'");
                }else{
                    HOSPITALRec = DBMgr.getRecord("SELECT * FROM HOSPITAL WHERE CODE = '" + session.getAttribute("HOSPITAL_CODE") + "'");
                }
                if (HOSPITALRec == null) {
                    MODE = DBMgr.MODE_INSERT;
                }
                else {
                    MODE = DBMgr.MODE_UPDATE;
                }
            }
            ProcessUtil util = new ProcessUtil();
            DBConnection c = new DBConnection();
            c.connectToLocal();
            Batch b = new Batch(DBMgr.getRecordValue(HOSPITALRec, "CODE"), c);
            c.Close();
            
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/md5.js"></script>
        <script type="text/javascript">

            function CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    Refresh_HOSPITAL();
                    return false;
                }
                else {
                    return true;
                }
            }

            function Refresh_HOSPITAL() {
                var to = document.location.pathname.lastIndexOf('?');
                if (to < 0) {
                    window.location = document.location.pathname + '?CODE=' + document.mainForm.CODE.value;
                }
                else {
                    window.location = document.location.pathname.substr(0, to) + '?CODE=' + document.mainForm.CODE.value;
                }
            }

            function HOSPITAL_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_HOSPITAL() {
                var target = "../../RetrieveData?TABLE=HOSPITAL&COND=CODE='" + document.mainForm.CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_HOSPITAL);
            }

            function AJAX_Handle_Refresh_HOSPITAL() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.CODE.value = "";
                        return;
                    }

                    // Data found
                    document.mainForm.CODE.value = getXMLNodeValue(xmlDoc, "CODE");
                }
            }

            function AJAX_VerifyData() {
                var target = "../../RetrieveData?TABLE=HOSPITAL&COND=CODE='" + document.mainForm.CODE.value + "'";
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
                                document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT %>";
                                document.mainForm.submit();
                                //alert("<%=labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)%>");
                            }
                            break;
                    }
                }
            }

            function SAVE_Click() {
                if (!isObjectEmptyString(document.mainForm.CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                    !isObjectEmptyString(document.mainForm.DESCRIPTION_THAI, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
                    !isObjectEmptyString(document.mainForm.DESCRIPTION_ENG, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>')) {
                    AJAX_VerifyData();
                }
            }

            function RESET_Click() {
                window.location.href = "HOSPITAL_main.jsp";
                return false;
            }

            function BANK_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.BANK_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_BANK() {
                var target = "../../RetrieveData?TABLE=BANK&COND=CODE='" + document.mainForm.BANK_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_BANK);
            }

            function AJAX_Handle_Refresh_BANK() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        //document.mainForm.BANK_CODE.value = "";
                        document.mainForm.BANK_DESCRIPTION.value = "";
                        //document.mainForm.SEARCH_BANK_BRANCH_CODE.disabled = false;
                        return;
                    }

                    // Data found
                    document.mainForm.BANK_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                    //document.mainForm.SEARCH_BANK_BRANCH_CODE.disabled = true;

                    if(document.mainForm.BANK_BRANCH_CODE.value != ""){
                        AJAX_Refresh_BANK_BRANCH();
                    }
                }
            }

            function BANK_BRANCH_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.BANK_BRANCH_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_BANK_BRANCH() {
                var target = "../../RetrieveData?TABLE=BANK_BRANCH&COND=BANK_CODE='" + document.mainForm.BANK_CODE.value + "' AND CODE='" + document.mainForm.BANK_BRANCH_CODE.value + "'";
                AJAX_Request(target, AJAX_Handle_Refresh_BANK_BRANCH);
            }

            function AJAX_Handle_Refresh_BANK_BRANCH() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.BANK_BRANCH_CODE.value = "";
                        document.mainForm.BANK_BRANCH_DESCRIPTION.value = "";
                        return;
                    }

                    // Data found
                    document.mainForm.BANK_BRANCH_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                }
            }
        </script>
    </head>
    <body>
            <form id="mainForm" name="mainForm" method="post">
            <input type="hidden" id="MODE" name="MODE" value="<%=MODE%>" />
            <table class="form">
                <tr>
                    <th colspan="4">
				  	<div style="float: left;">${labelMap.TITLE_MAIN}</div>
				  	<div style="float: right;" id="Language" name="Language"></div>
				  	</th>
                </tr>
                <tr>
                    <td class="label"><label for="CODE">${labelMap.CODE} *</label></td>
                    <td class="input">
                        <input type="text" id="CODE" name="CODE" class="short" maxlength="20" readonly="readonly" value="<%= DBMgr.getRecordValue(HOSPITALRec, "CODE") %>" onkeypress="return CODE_KeyPress(event);"/>
                    </td>
                    <td class="label">
                        <label for="COMPANY_NAME">${labelMap.COMPANY_NAME}</label>
                    </td>
                    <td class="input" >
                        <input name="COMPANY_NAME" type="text" class="medium" id="COMPANY_NAME" value="<%= DBMgr.getRecordValue(HOSPITALRec, "COMPANY_NAME") %>" maxlength="50"/>
                    </td>
                    
                </tr>
                <tr>
                    <td class="label"><label for="NAME">${labelMap.DESCRIPTION_THAI}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DESCRIPTION_THAI" name="DESCRIPTION_THAI" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(HOSPITALRec, "DESCRIPTION_THAI") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="EMAIL">${labelMap.DESCRIPTION_ENG}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DESCRIPTION_ENG" name="DESCRIPTION_ENG" class="long" maxlength="255" value="<%= DBMgr.getRecordValue(HOSPITALRec, "DESCRIPTION_ENG") %>" />
                    </td>
                </tr>                
                <tr>
                    <td class="label">
                        <label for="TAX_END_MONTH_OF_YEAR">${labelMap.TAX_END_MONTH_OF_YEAR}</label>
                    </td>
                    
                    <td class="input" >
                        <%= util.selectMM(session.getAttribute("LANG_CODE").toString(), "TAX_END_MONTH_OF_YEAR", DBMgr.getRecordValue(HOSPITALRec, "TAX_END_MONTH_OF_YEAR").equals("") ? b.getMm() : DBMgr.getRecordValue(HOSPITALRec, "TAX_END_MONTH_OF_YEAR"))  %>
                    </td>

                    <td class="label">
                        <label for="TAXNO">${labelMap.TAXNO}</label>
                    </td>
                    <td class="input" >
                        <input name="TAXNO" type="text" class="medium" id="TAXNO" value="<%= DBMgr.getRecordValue(HOSPITALRec, "TAXNO") %>" maxlength="50"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="BANK_ACCOUNT_NO">${labelMap.ACCOUNT_NO}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input name="ACCOUNT_NO" type="text" class="medium" id="ACCOUNT_NO" value="<%= DBMgr.getRecordValue(HOSPITALRec, "ACCOUNT_NO") %>" maxlength="50"/>
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="BANK_CODE">${labelMap.BANK_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="BANK_CODE" name="BANK_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(HOSPITALRec, "BANK_CODE") %>" onkeypress="return BANK_CODE_KeyPress(event);" onblur="AJAX_Refresh_BANK();" />
                        <input id="SEARCH_BANK_CODE" name="SEARCH_BANK_CODE" type="image" <%=disabledManager%> class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=BANK&BEACTIVE=1&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&TARGET=BANK_CODE&HANDLE=AJAX_Refresh_BANK'); return false;" />
                        <input type="text" id="BANK_DESCRIPTION" name="BANK_DESCRIPTION" class="long" readonly="readonly" value="" />
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="BANK_BRANCH_CODE">${labelMap.BANK_BRANCH_CODE}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input type="text" id="BANK_BRANCH_CODE" name="BANK_BRANCH_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(HOSPITALRec, "BANK_BRANCH_CODE") %>" onkeypress="return BANK_BRANCH_CODE_KeyPress(event);" onblur="AJAX_Refresh_BANK_BRANCH();" />
                        <input id="SEARCH_BANK_BRANCH_CODE" name="SEARCH_BANK_BRANCH_CODE" type="image" <%=disabledManager%> class="image_button" src="../../images/search_button.png" alt="Search" onclick="return checkBankBranchCode()" />
                        <input type="text" id="BANK_BRANCH_DESCRIPTION" name="BANK_BRANCH_DESCRIPTION" class="long" readonly="readonly" value="" />
                    </td>
                </tr> 
                <tr>
                    <td class="label">
                        <label for="CITI_PARTY_NAME">${labelMap.CITI_PARTY_NAME}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input type="text" id="CITI_PARTY_NAME" name="CITI_PARTY_NAME" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(HOSPITALRec, "CITI_PARTY_NAME") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="CITI_PARTY_ID">${labelMap.CITI_PARTY_ID}</label>
                    </td>
                    <td class="input" >
                        <input type="text" id="CITI_PARTY_ID" name="CITI_PARTY_ID" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(HOSPITALRec, "CITI_PARTY_ID") %>" />
                    </td>
                    <td class="label">
                        <label for="CITI_PARTY_AC">${labelMap.CITI_PARTY_AC}</label>
                    </td>
                    <td class="input" >
                        <input type="text" id="CITI_PARTY_AC" name="CITI_PARTY_AC" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(HOSPITALRec, "CITI_PARTY_AC") %>" />
                    </td>
                </tr>
                
               	<!---------- Add by Tong. ------------>               
                <tr>
                    <td class="label">
                        <label for="OF_BUILDING_NAME">${labelMap.OF_BUILDING_NAME}</label>
                    </td>
                    <td class="input" >
                        <input name="OF_BUILDING_NAME" type="text" class="medium" id="OF_BUILDING_NAME" value="<%= DBMgr.getRecordValue(HOSPITALRec, "OF_BUILDING_NAME") %>" maxlength="50"/>
                    </td>
                    <td class="label">
                        <label for="OF_BUILDING_NO">${labelMap.OF_BUILDING_NO}</label>
                    </td>
                    <td class="input" >
                        <input name="OF_BUILDING_NO" type="text" class="medium" id="OF_BUILDING_NO" value="<%= DBMgr.getRecordValue(HOSPITALRec, "OF_BUILDING_NO") %>" maxlength="50"/>
                    </td>
                </tr>                
                <tr>
                    <td class="label">
                        <label for="OF_BUILDING_FLOOR">${labelMap.OF_BUILDING_FLOOR}</label>
                    </td>
                    <td class="input" >
                        <input name="OF_BUILDING_FLOOR" type="text" class="medium" id="OF_BUILDING_FLOOR" value="<%= DBMgr.getRecordValue(HOSPITALRec, "OF_BUILDING_FLOOR") %>" maxlength="50"/>
                    </td>
                    <td class="label">
                        <label for="OF_VILLAGE">${labelMap.OF_VILLAGE}</label>
                    </td>
                    <td class="input" >
                        <input name="OF_VILLAGE" type="text" class="medium" id="OF_VILLAGE" value="<%= DBMgr.getRecordValue(HOSPITALRec, "OF_VILLAGE") %>" maxlength="50"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="OF_ADDRESSNO">${labelMap.OF_ADDRESSNO}</label>
                    </td>
                    <td class="input" >
                        <input name="OF_ADDRESSNO" type="text" class="medium" id="OF_ADDRESSNO" value="<%= DBMgr.getRecordValue(HOSPITALRec, "OF_ADDRESSNO") %>" maxlength="50"/>
                    </td>
                    <td class="label">
                        <label for="OF_MOO">${labelMap.OF_MOO}</label>
                    </td>
                    <td class="input" >
                        <input name="OF_MOO" type="text" class="medium" id="OF_MOO" value="<%= DBMgr.getRecordValue(HOSPITALRec, "OF_MOO") %>" maxlength="50"/>
                    </td>
                </tr> 
               	<tr>
                    <td class="label">
                        <label for="OF_SOI">${labelMap.OF_SOI}</label>
                    </td>
                    <td class="input" >
                        <input name="OF_SOI" type="text" class="medium" id="OF_SOI" value="<%= DBMgr.getRecordValue(HOSPITALRec, "OF_SOI") %>" maxlength="50"/>
                    </td>
                    <td class="label">
                        <label for="OF_ROAD">${labelMap.OF_ROAD}</label>
                    </td>
                    <td class="input" >
                        <input name="OF_ROAD" type="text" class="medium" id="OF_ROAD" value="<%= DBMgr.getRecordValue(HOSPITALRec, "OF_ROAD") %>" maxlength="50"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="OF_SUBDISTRICT">${labelMap.OF_SUBDISTRICT}</label>
                    </td>
                    <td class="input" >
                        <input name="OF_SUBDISTRICT" type="text" class="medium" id="OF_SUBDISTRICT" value="<%= DBMgr.getRecordValue(HOSPITALRec, "OF_SUBDISTRICT") %>" maxlength="50"/>
                    </td>
                    <td class="label">
                        <label for="OF_DISTRICT">${labelMap.OF_DISTRICT}</label>
                    </td>
                    <td class="input" >
                        <input name="OF_DISTRICT" type="text" class="medium" id="OF_DISTRICT" value="<%= DBMgr.getRecordValue(HOSPITALRec, "OF_DISTRICT") %>" maxlength="50"/>
                    </td>
                </tr>  
                <tr>
                    <td class="label">
                        <label for="OF_PROVINCE">${labelMap.OF_PROVINCE}</label>
                    </td>
                    <td class="input" >
                        <input name="OF_PROVINCE" type="text" class="medium" id="OF_PROVINCE" value="<%= DBMgr.getRecordValue(HOSPITALRec, "OF_PROVINCE") %>" maxlength="50"/>
                    </td>
                    <td class="label">
                        <label for="ZIP">${labelMap.ZIP}</label>
                    </td>
                    <td class="input" >
                        <input name="ZIP" type="text" class="medium" id="ZIP" value="<%= DBMgr.getRecordValue(HOSPITALRec, "ZIP") %>" maxlength="50"/>
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="TEL">${labelMap.TEL}</label>
                    </td>
                    <td class="input" >
                        <input name="TEL" type="text" class="medium" id="TEL" value="<%= DBMgr.getRecordValue(HOSPITALRec, "TEL") %>" maxlength="50"/>
                    </td>
                    <td class="label">
                        <label for="EMAIL">${labelMap.EMAIL}</label>
                    </td>
                    <td class="input" >
                    
                        <input name="EMAIL" type="text" class="medium" id="EMAIL" value="<%= DBMgr.getRecordValue(HOSPITALRec, "EMAIL") %>" maxlength="50"/>
                   
                    </td>
                </tr>
                 <tr>
                    <td class="label"><label for="GUARANTEE_INCLUDE_EXTRA">${labelMap.GUARANTEE_INCLUDE_EXTRA}</label></td>
                    <td class="input">
                        <input type="radio" id="GUARANTEE_INCLUDE_EXTRA_1" name="GUARANTEE_INCLUDE_EXTRA" value="Y"<%= DBMgr.getRecordValue(HOSPITALRec, "GUARANTEE_INCLUDE_EXTRA").equalsIgnoreCase("Y") || DBMgr.getRecordValue(HOSPITALRec, "GUARANTEE_INCLUDE_EXTRA").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="GUARANTEE_INCLUDE_EXTRA_1">${labelMap.GUARANTEE_INCLUDE_EXTRA_1}</label>
                        <input type="radio" id="GUARANTEE_INCLUDE_EXTRA_0" name="GUARANTEE_INCLUDE_EXTRA" value="N"<%= DBMgr.getRecordValue(HOSPITALRec, "GUARANTEE_INCLUDE_EXTRA").equalsIgnoreCase("N") ? " checked=\"checked\"" : "" %> />
                        <label for="GUARANTEE_INCLUDE_EXTRA_0">${labelMap.GUARANTEE_INCLUDE_EXTRA_0}</label>
                    </td>
                    <td class="label"><label for="GUARANTEE_DAY">${labelMap.GUARANTEE_DAY}</label></td>
                    <td class="input">
                        <input type="radio" id="GUARANTEE_DAY_1" name="GUARANTEE_DAY" value="Y"<%= DBMgr.getRecordValue(HOSPITALRec, "GUARANTEE_INCLUDE_EXTRA").equalsIgnoreCase("Y") || DBMgr.getRecordValue(HOSPITALRec, "GUARANTEE_INCLUDE_EXTRA").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="GUARANTEE_DAY_1">${labelMap.GUARANTEE_DAY_1}</label>
                        <input type="radio" id="GUARANTEE_DAY_0" name="GUARANTEE_DAY" value="N"<%= DBMgr.getRecordValue(HOSPITALRec, "GUARANTEE_INCLUDE_EXTRA").equalsIgnoreCase("N") ? " checked=\"checked\"" : "" %> />
                        <label for="GUARANTEE_DAY_0">${labelMap.GUARANTEE_DAY_0}</label>
                    </td>
                </tr>
                
                
                
                <!-- edit -->
                 <tr>
                    <td class="label"><label for="GUARANTEE_ALL_ALLOC">${labelMap.GUARANTEE_ALL_ALLOC}</label></td>
                    <td class="input">
                        <input type="radio" id="GUARANTEE_ALL_ALLOC_1" name="GUARANTEE_ALL_ALLOC" value="Y"<%= DBMgr.getRecordValue(HOSPITALRec, "GUARANTEE_ALL_ALLOC").equalsIgnoreCase("Y") || DBMgr.getRecordValue(HOSPITALRec, "GUARANTEE_ALL_ALLOC").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="GUARANTEE_ALL_ALLOC_1">${labelMap.GUARANTEE_ALL_ALLOC_1}</label>
                        <input type="radio" id="GUARANTEE_ALL_ALLOC_0" name="GUARANTEE_ALL_ALLOC" value="N"<%= DBMgr.getRecordValue(HOSPITALRec, "GUARANTEE_ALL_ALLOC").equalsIgnoreCase("N") ? " checked=\"checked\"" : "" %> />
                        <label for="GUARANTEE_ALL_ALLOC_0">${labelMap.GUARANTEE_ALL_ALLOC_0}</label>
                    </td>
                    <td class="label"><label for="IS_ONWARD">${labelMap.IS_ONWARD}</label></td>
                    <td class="input">
                        <input type="radio" id="IS_ONWARD_1" name="IS_ONWARD" value="Y"<%= DBMgr.getRecordValue(HOSPITALRec, "IS_ONWARD").equalsIgnoreCase("Y") || DBMgr.getRecordValue(HOSPITALRec, "IS_ONWARD").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="IS_ONWARD_1">${labelMap.IS_ONWARD_1}</label>
                        <input type="radio" id="IS_ONWARD_0" name="IS_ONWARD" value="N"<%= DBMgr.getRecordValue(HOSPITALRec, "IS_ONWARD").equalsIgnoreCase("N") ? " checked=\"checked\"" : "" %> />
                        <label for="IS_ONWARD_0">${labelMap.IS_ONWARD_0}</label>
                    </td>
                </tr>

                <!-- edit -->
                 <tr>
                    <td class="label"><label for="GUARANTEE_ONWARD">${labelMap.GUARANTEE_ONWARD}</label></td>
                    <td class="input">
                        <input type="radio" id="GUARANTEE_ONWARD_1" name="GUARANTEE_ONWARD" value="Y"<%= DBMgr.getRecordValue(HOSPITALRec, "IS_GUARANTEE_ONWARD").equalsIgnoreCase("Y") || DBMgr.getRecordValue(HOSPITALRec, "GUARANTEE_ONWARD").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="GUARANTEE_ONWARD_1">${labelMap.GUARANTEE_ONWARD_1}</label>
                        <input type="radio" id="GUARANTEE_ONWARD_0" name="GUARANTEE_ONWARD" value="N"<%= DBMgr.getRecordValue(HOSPITALRec, "IS_GUARANTEE_ONWARD").equalsIgnoreCase("N") ? " checked=\"checked\"" : "" %> />
                        <label for="GUARANTEE_ONWARD_0">${labelMap.GUARANTEE_ONWARD_0}</label>
                    </td>
                    <td class="label"><label for="DISCHARGE_BASIS">${labelMap.DISCHARGE_BASIS}</label></td>
                    <td class="input">
                        <input type="radio" id="DISCHARGE_BASIS_1" name="DISCHARGE_BASIS" value="Y"<%= DBMgr.getRecordValue(HOSPITALRec, "IS_DISCHARGE_BASIS").equalsIgnoreCase("Y") || DBMgr.getRecordValue(HOSPITALRec, "IS_DISCHARGE_BASIS").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="DISCHARGE_BASIS_1">${labelMap.DISCHARGE_BASIS_1}</label>
                        <input type="radio" id="DISCHARGE_BASIS_0" name="DISCHARGE_BASIS" value="N"<%= DBMgr.getRecordValue(HOSPITALRec, "IS_DISCHARGE_BASIS").equalsIgnoreCase("N") ? " checked=\"checked\"" : "" %> />
                        <label for="DISCHARGE_BASIS_0">${labelMap.DISCHARGE_BASIS_0}</label>
                    </td>
                </tr>

                <!-- edit -->
                <tr>
                    <td class="label">
                        <label for="GL_ACCOUNT_CODE">${labelMap.GL_ACCOUNT_CODE}</label>
                    </td>
                    <td class="input" >
                        <input name="GL_ACCOUNT_CODE" type="text" class="medium" id="GL_ACCOUNT_CODE" value="<%= DBMgr.getRecordValue(HOSPITALRec, "GL_ACCOUNT_CODE") %>" maxlength="50"/>
                    </td>
                    <td class="label">
                        <label for="AC_ACCOUNT_CODE">${labelMap.AC_ACCOUNT_CODE}</label>
                    </td>
                    <td class="input" >
                        <input name="AC_ACCOUNT_CODE" type="text" class="medium" id="AC_ACCOUNT_CODE" value="<%= DBMgr.getRecordValue(HOSPITALRec, "AC_ACCOUNT_CODE") %>" maxlength="50"/>
                    </td>
                </tr>
                
                
                <!--    -->
                <tr>
                    <td class="label">
                        <label for="SHARING_ACCOUNT">${labelMap.SHARING_ACCOUNT}</label>
                    </td>
                    <td class="input" >
                        <input name="SHARING_ACCOUNT" type="text" class="medium" id="SHARING_ACCOUNT" value="<%= DBMgr.getRecordValue(HOSPITALRec, "SHARING_ACCOUNT") %>" maxlength="50"/>
                    </td>
                    <td class="label">
                        <label for="EARNING_ACCOUNT">${labelMap.EARNING_ACCOUNT}</label>
                    </td>
                    <td class="input" >
                        <input name="EARNING_ACCOUNT" type="text" class="medium" id="EARNING_ACCOUNT" value="<%= DBMgr.getRecordValue(HOSPITALRec, "EARNING_ACCOUNT") %>" maxlength="50"/>
                    </td>
                </tr>
                <!-- edit -->
              
                 <!-- edit -->
                 <tr>
                    <td class="label"><label for="IS_PARTIAL">${labelMap.IS_PARTIAL}</label></td>
                    <td class="input">
                        <input type="radio" id="IS_PARTIAL_1" name="IS_PARTIAL" value="Y"<%= DBMgr.getRecordValue(HOSPITALRec, "IS_PARTIAL").equalsIgnoreCase("Y") || DBMgr.getRecordValue(HOSPITALRec, "IS_PARTIAL").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="IS_PARTIAL_1">${labelMap.IS_PARTIAL_1}</label>
                        <input type="radio" id="IS_PARTIAL_0" name="IS_PARTIAL" value="N"<%= DBMgr.getRecordValue(HOSPITALRec, "IS_PARTIAL").equalsIgnoreCase("N") ? " checked=\"checked\"" : "" %> />
                        <label for="IS_PARTIAL_0">${labelMap.IS_PARTIAL_0}</label>
                    </td>
                    <td class="label"><label for="IS_COMBINE_BILL">${labelMap.IS_COMBINE_BILL}</label></td>
                    <td class="input">
                        <input type="radio" id="IS_COMBINE_BILL_1" name="IS_COMBINE_BILL" value="Y"<%= DBMgr.getRecordValue(HOSPITALRec, "IS_JOIN_BILL").equalsIgnoreCase("Y") || DBMgr.getRecordValue(HOSPITALRec, "IS_JOIN_BILL").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="IS_COMBINE_BILL_1">${labelMap.IS_COMBINE_BILL_1}</label>
                        <input type="radio" id="IS_COMBINE_BILL_0" name="IS_COMBINE_BILL" value="N"<%= DBMgr.getRecordValue(HOSPITALRec, "IS_JOIN_BILL").equalsIgnoreCase("N") ? " checked=\"checked\"" : "" %> />
                        <label for="IS_COMBINE_BILL_0">${labelMap.IS_COMBINE_BILL_0}</label>
                    </td>
                </tr>
                <!-- edit -->
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1"<%= DBMgr.getRecordValue(HOSPITALRec, "ACTIVE").equalsIgnoreCase("1") || DBMgr.getRecordValue(HOSPITALRec, "ACTIVE").equalsIgnoreCase("") ? " checked=\"checked\"" : "" %> />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0"<%= DBMgr.getRecordValue(HOSPITALRec, "ACTIVE").equalsIgnoreCase("0") ? " checked=\"checked\"" : "" %> />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="submit" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="return RESET_Click()" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
<script language="javascript">
	function checkBankBranchCode(){
        if(document.mainForm.BANK_DESCRIPTION.value==""){
            alert("${labelMap.ALERT_BANK}");
            document.mainForm.BANK_CODE.focus();
        }else{
            openSearchForm('../search.jsp?TABLE=BANK_BRANCH&BEACTIVE=1&DISPLAY_FIELD=DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>&COND=[ and BANK_CODE=\''+ document.mainForm.BANK_CODE.value +'\']&TARGET=BANK_BRANCH_CODE&HANDLE=AJAX_Refresh_BANK_BRANCH');
        }
        return false;
	}
</script>