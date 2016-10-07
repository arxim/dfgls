<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap,java.util.*" errorPage="../error.jsp"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.bean.obj.util.JDate" %>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.obj.util.Utils "%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="java.sql.Types"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@ include file="../../_global.jsp" %><%    
 

		if (!Guard.checkPermission(session, Guard.PAGE_INPUT_METHOD_ALLOC_ITEM_MAIN)) {
            response.sendRedirect("../message.jsp");
            return;
        }

        if (session.getAttribute("LANG_CODE") == null)
            session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
    
    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	labelMap.add("TITLE_MAIN", "TAX", "คำนวณภาษี");

    request.setAttribute("labelMap", labelMap.getHashMap());
    
    request.setCharacterEncoding("UTF-8");
    DataRecord tax91Rec = null, doctorRec = null, doctorProfileRec=null, sumTax402Rec=null;
    byte MODE = DBMgr.MODE_INSERT;
	String showC01="", showC14="", showA06="",showBtSave="", people_no="", people_tax_id="";
    if (request.getParameter("MODE") != null) {

        MODE = Byte.parseByte(request.getParameter("MODE"));
        //=======a14=========
		String a14_01=request.getParameter("a14_01_h");
		
		//=======a18_01=========
		String a18_01=request.getParameter("a18_01_h");
		
		//=======a20_01=========
		String a20_01=request.getParameter("a20_01_h");
		
		//=======c04=========
		String c04021=request.getParameter("c04_03");
		String c04041=request.getParameter("c04_04");
		String c04061=request.getParameter("c04_08");
		String c04081=request.getParameter("c04_09");
		String c04_031="", c04_041="", c04_081="", c04_091="";
		if(c04021 !="") {c04_031="30000"; }
		if(c04041 !="") {c04_041="30000"; }
		if(c04061 !="") {c04_081="30000"; }
		if(c04081 !="") {c04_091="30000"; }
		//===================
		//out.println(request.getParameter("a01"));
        tax91Rec = new DataRecord("TAX_91");
		
        tax91Rec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
        tax91Rec.addField("YYYY", Types.VARCHAR, "2009", true);
        tax91Rec.addField("DOCTOR_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CODE"),true);
        //tax91Rec.addField("PEOPLE_NO", Types.VARCHAR, request.getParameter("PEOPLE_NO"));
        //tax91Rec.addField("PEOPLE_BIRTHDAY", Types.VARCHAR, JDate.saveDate(request.getParameter("PEOPLE_BIRTHDAY")));
        //tax91Rec.addField("PEOPLE_TAX_ID", Types.VARCHAR, request.getParameter("PEOPLE_TAX_ID"));
        tax91Rec.addField("DOCTOR_STATUS", Types.VARCHAR, request.getParameter("DOCTOR_STATUS"));
        tax91Rec.addField("DOCTOR_TEL_HOME", Types.VARCHAR, request.getParameter("DOCTOR_TEL_HOME"));
        tax91Rec.addField("DOCTOR_TEL_OFFICE", Types.VARCHAR, request.getParameter("DOCTOR_TEL_OFFICE"));
        tax91Rec.addField("SPOUSE_BIRTHDAY", Types.VARCHAR, JDate.saveDate(request.getParameter("SPOUSE_BIRTHDAY")));
       	tax91Rec.addField("SPOUSE_NO", Types.VARCHAR, request.getParameter("SPOUSE_NO"));
        tax91Rec.addField("SPOUSE_TAX_NO", Types.VARCHAR, request.getParameter("SPOUSE_TAX_ID"));
        tax91Rec.addField("SPOUSE_NAME", Types.VARCHAR, request.getParameter("SPOUSE_NAME"));
	    tax91Rec.addField("SPOUSE_TYPE", Types.VARCHAR, request.getParameter("SPOUSE_TYPE"));
        tax91Rec.addField("SPOUSE_TYPE1", Types.VARCHAR, request.getParameter("SPOUSE_TYPE1"));
        tax91Rec.addField("PHALANX_01", Types.VARCHAR, request.getParameter("PHALANX_01"));
        tax91Rec.addField("PHALANX_01_01", Types.VARCHAR, request.getParameter("PHALANX_01_01"));
        tax91Rec.addField("PHALANX_02", Types.VARCHAR, request.getParameter("PHALANX_02"));
        tax91Rec.addField("PHALANX_02_01", Types.VARCHAR, request.getParameter("PHALANX_02_01"));
        
        tax91Rec.addField("A01", Types.NUMERIC, request.getParameter("a01"));
        tax91Rec.addField("A02", Types.NUMERIC, request.getParameter("a02"));
        tax91Rec.addField("A03", Types.NUMERIC, request.getParameter("a03"));
        tax91Rec.addField("A04", Types.NUMERIC, request.getParameter("a04"));
        tax91Rec.addField("A05", Types.NUMERIC, request.getParameter("a05"));
        tax91Rec.addField("A06", Types.NUMERIC, request.getParameter("a06"));
        tax91Rec.addField("A07", Types.NUMERIC, request.getParameter("a07"));
        tax91Rec.addField("A08", Types.NUMERIC, request.getParameter("a08_02"));
        tax91Rec.addField("A09", Types.NUMERIC, request.getParameter("a09"));
        tax91Rec.addField("A10", Types.NUMERIC, request.getParameter("a10"));
        tax91Rec.addField("A11", Types.NUMERIC, request.getParameter("a11"));
        tax91Rec.addField("A12", Types.NUMERIC, request.getParameter("a12"));
        tax91Rec.addField("A13", Types.NUMERIC, request.getParameter("a13"));
        tax91Rec.addField("A14_01", Types.VARCHAR, a14_01);//
        tax91Rec.addField("A14_02", Types.NUMERIC, request.getParameter("a14_03"));//หลักฐาน
        tax91Rec.addField("A14_03", Types.NUMERIC, request.getParameter("a14"));//จำนวนเงิน
        tax91Rec.addField("A14_04", Types.VARCHAR, request.getParameter("a14_04"));//กรณี
        tax91Rec.addField("A15", Types.NUMERIC, request.getParameter("a15"));
        tax91Rec.addField("A16", Types.NUMERIC, request.getParameter("a16"));
        tax91Rec.addField("A17", Types.NUMERIC, request.getParameter("a17"));
        tax91Rec.addField("A18_01", Types.VARCHAR, a18_01);
        tax91Rec.addField("A18_02", Types.NUMERIC, request.getParameter("a18"));
        tax91Rec.addField("A19", Types.NUMERIC, request.getParameter("a19"));
        tax91Rec.addField("A20_01", Types.VARCHAR, a20_01);
        tax91Rec.addField("A20_02", Types.NUMERIC, request.getParameter("a20"));
        
        tax91Rec.addField("B01", Types.NUMERIC, request.getParameter("b01"));
        tax91Rec.addField("B02", Types.NUMERIC, request.getParameter("b02"));
        tax91Rec.addField("B03", Types.NUMERIC, request.getParameter("b03"));
        tax91Rec.addField("B04", Types.NUMERIC, request.getParameter("b04"));
        tax91Rec.addField("B05", Types.NUMERIC, request.getParameter("b05"));
        tax91Rec.addField("B06", Types.NUMERIC, request.getParameter("b06"));
        tax91Rec.addField("B07", Types.NUMERIC, request.getParameter("b07"));
        
        tax91Rec.addField("C01", Types.NUMERIC, request.getParameter("c01"));
        tax91Rec.addField("C02", Types.NUMERIC, request.getParameter("c02"));
        tax91Rec.addField("C03_01", Types.NUMERIC, request.getParameter("c03_01"));
        tax91Rec.addField("C03_02", Types.NUMERIC, request.getParameter("c03_02"));
        tax91Rec.addField("C03_03", Types.NUMERIC, request.getParameter("c03_03"));
        tax91Rec.addField("C03_04", Types.NUMERIC, request.getParameter("c03_04"));
        tax91Rec.addField("C04_01", Types.VARCHAR, request.getParameter("c04_03"));//บัตรประชาชนบิดาผู้มีเงินได้
        tax91Rec.addField("C04_02", Types.NUMERIC, c04_031);//จำนวนเงินของบิดาผู้มีเงินได้
        tax91Rec.addField("C04_03", Types.VARCHAR, request.getParameter("c04_04"));//บัตรประชาชนมารดาผู้มีเงินได้
        tax91Rec.addField("C04_04", Types.NUMERIC, c04_041);//จำนวนเงินของมารดาผู้มีเงินได้
        tax91Rec.addField("C04_05", Types.VARCHAR, request.getParameter("c04_08"));
        tax91Rec.addField("C04_06", Types.NUMERIC, c04_081);
        tax91Rec.addField("C04_07", Types.VARCHAR, request.getParameter("c04_09"));
        tax91Rec.addField("C04_08", Types.NUMERIC, c04_091);
        tax91Rec.addField("C05", Types.NUMERIC, request.getParameter("c05"));
        tax91Rec.addField("C06_01", Types.VARCHAR, request.getParameter("c06_03"));
        tax91Rec.addField("C06_02", Types.VARCHAR, request.getParameter("c06_04"));
        tax91Rec.addField("C06_03", Types.VARCHAR, request.getParameter("c06_08"));
        tax91Rec.addField("C06_04", Types.VARCHAR, request.getParameter("c06_09"));
        tax91Rec.addField("C06_05", Types.NUMERIC, request.getParameter("c06_10"));
        tax91Rec.addField("C07", Types.NUMERIC, request.getParameter("c07"));
        tax91Rec.addField("C08", Types.NUMERIC, request.getParameter("c08"));
        tax91Rec.addField("C09", Types.NUMERIC, request.getParameter("c09"));
        tax91Rec.addField("C10", Types.NUMERIC, request.getParameter("c10"));
        tax91Rec.addField("C11", Types.NUMERIC, request.getParameter("c11"));
        tax91Rec.addField("C12", Types.NUMERIC, request.getParameter("c12"));
        tax91Rec.addField("C13", Types.NUMERIC, request.getParameter("c13"));
        tax91Rec.addField("C14_01", Types.NUMERIC, request.getParameter("c14"));
        tax91Rec.addField("C14_02", Types.NUMERIC, request.getParameter("c14_02"));

        tax91Rec.addField("CREATE_DATE", Types.VARCHAR, JDate.getDate());
        tax91Rec.addField("CREATE_TIME", Types.VARCHAR, JDate.getTime());
        tax91Rec.addField("CREATE_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
        tax91Rec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
        tax91Rec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
        tax91Rec.addField("UPDATE_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());

        if (MODE == DBMgr.MODE_INSERT) 
        {
           if (DBMgr.insertRecord(tax91Rec)) 
            {
                session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "tax/taxform.jsp?DOCTOR_CODE=" + tax91Rec.getField("DOCTOR_CODE").getValue()));
            } 
            else 
            {
                session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
            }
        } 
        else if (MODE == DBMgr.MODE_UPDATE) 
        {
            if (DBMgr.updateRecord(tax91Rec)) 
            {
                session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "tax/taxform.jsp?DOCTOR_CODE=" + tax91Rec.getField("DOCTOR_CODE").getValue()));
            } 
            else 
            {
                session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
            }
        }

        response.sendRedirect("../message.jsp");
        return;
    } 
    else if (request.getParameter("DOCTOR_CODE") != null) 
    {
    	String doctorCode=request.getParameter("DOCTOR_CODE").toString();
    	String msg="ไม่พบข้อมูลรวมรายได้ทั้งปีของ Doctor Code: "+doctorCode+"  ในระบบ";
    	//System.out.println("MSG="+msg);
    	//System.out.println("start doctor code="+request.getParameter("DOCTOR_CODE"));
    	String sqlTax91="SELECT * FROM TAX_91 WHERE DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") + 
		"' AND HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND YYYY='2009' ";
    	tax91Rec = DBMgr.getRecord(sqlTax91);
    	//out.println("sqlTax91="+sqlTax91);
    	doctorRec = DBMgr.getRecord("SELECT * FROM DOCTOR WHERE CODE = '" + request.getParameter("DOCTOR_CODE") + "'");
    	sumTax402Rec = DBMgr.getRecord("SELECT SUM_NORMAL_TAX_AMT , NET_TAX_MONTH FROM SUMMARY_TAX_402 WHERE DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") + "'AND YYYY='2009' AND MM='13'");
        if (tax91Rec == null) 
        {
        	if(sumTax402Rec == null)
        	{
        		%>
        		<script>
        		alert("<%=msg%>");
        		window.location="taxform.jsp";
        		</script>
        		<%
        		
        	}
        	else
        	{
	            MODE = DBMgr.MODE_INSERT;
	            //System.out.println("code="+request.getParameter("DOCTOR_CODE"));
	            String dCode=DBMgr.getRecordValue(doctorRec, "CODE");
	            String dpCode=DBMgr.getRecordValue(doctorRec, "DOCTOR_PROFILE_CODE");
	            //System.out.println("code="+dCode);
	            //System.out.println("doctor_profile_code="+dpCode);
	            String query_dp="SELECT BIRTH_DATE FROM DOCTOR_PROFILE WHERE CODE = '" + dpCode + "'";
	            System.out.println("query_dp="+query_dp);
	            doctorProfileRec = DBMgr.getRecord(query_dp);
	            
	            if(dCode.equals(dpCode))
	            {
	            	showC01="30000.00";
	                showC14="30000.00";
	                showA06="30000.00";
	            }
	            else
	            {
	            	//out.println("okkkkkkkkkkkkkkkkkkkkkk");
	            	showC01="60000.00";
	                showC14="60000.00";
	                showA06="60000.00";
	            }
        	}
            
        } 
        else 
        {
            MODE = DBMgr.MODE_UPDATE;
            showBtSave="1";
            showC01=DBMgr.getRecordValue(tax91Rec, "C01");
            showC14=DBMgr.getRecordValue(tax91Rec, "C14_01");
            showA06=DBMgr.getRecordValue(tax91Rec, "A06");
             //out.println("showBtSave="+showBtSave);
        }
        //System.out.println("MODE="+MODE);
        String tax_id=DBMgr.getRecordValue(doctorRec, "TAX_ID");
		int num_tax=tax_id.length();
		if(num_tax==10)
		{
			people_tax_id=tax_id;
		}
		else if(num_tax==13)
		{
			people_no=tax_id;
		}
    } 
%>
<%	
String report = ""; 
%>

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
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
<script language="javascript">
    //parent.toggleFrame();
    //++++++++++++++ Script ++++++++++++
    //document.getElementById('mytab')
	
	//=========ค้นหา doctor code===============
			function CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    AJAX_Refresh_DOCTOR();
                    return false;
                }
                else {
                    return true;
                }
            }
            
            function AJAX_Refresh_DOCTOR() {
                //alert('AJAX_Refresh_DEPARTMENT');
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value + "' AND HOSPITAL_CODE='<%= session.getAttribute("HOSPITAL_CODE").toString() %>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
            }
            
            function AJAX_Handle_Refresh_DOCTOR() {
            
                if (AJAX_IsComplete()) {
                    //alert('AJAX_Handle_Refresh_DEPARTMENT');
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
						//alert(isXMLNodeExist(xmlDoc, "CODE"));
                        document.mainForm.DOCTOR_CODE.value = "";
                        //document.mainForm.DEPARTMENT_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
					var d_code=getXMLNodeValue(xmlDoc, "DOCTOR_PROFILE_CODE");
					var code=getXMLNodeValue(xmlDoc, "CODE");
					/*if(d_code==code)
					{
						 SelectElement('c01').value = 30000;
						 SelectElement('c14').value = 30000;
						 SelectElement('a06').value = 30000;
					}
					else
					{
						SelectElement('c01').value = 60000;
						SelectElement('c14').value = 60000;
						SelectElement('a06').value = 60000;
					}
					var tax_id=getXMLNodeValue(xmlDoc, "TAX_ID");
					alert("tax="+tax_id.length);
					if(tax_id.length==10)
					{
						document.mainForm.PEOPLE_TAX_ID.value = getXMLNodeValue(xmlDoc, "TAX_ID");
					}
					else if(tax_id.length==13)
					{
						document.mainForm.PEOPLE_NO.value = getXMLNodeValue(xmlDoc, "TAX_ID");
					}
					
                  	document.mainForm.DOCTOR_CODE.value = getXMLNodeValue(xmlDoc, "CODE");
                    document.mainForm.DOCTOR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_THAI");
                    document.mainForm.ADDRESS1.value = getXMLNodeValue(xmlDoc, "ADDRESS1");
                    document.mainForm.ADDRESS2.value = getXMLNodeValue(xmlDoc, "ADDRESS2");
                    document.mainForm.ADDRESS3.value = getXMLNodeValue(xmlDoc, "ADDRESS3");
                    document.mainForm.ZIP.value = getXMLNodeValue(xmlDoc, "ZIP");*/
                    
                    //AJAX_Refresh_TAX();
                }
               
            }	
            
            function AJAX_Refresh_TAX() {
                //alert('AJAX_Refresh_DEPARTMENT');
                var target = "../../RetrieveData?TABLE=TAX_91&COND=DOCTOR_CODE='" + document.mainForm.DOCTOR_CODE.value + "' AND YYYY='2009' AND HOSPITAL_CODE='<%= session.getAttribute("HOSPITAL_CODE").toString() %>'";
                AJAX_Request(target, AJAX_Handle_Refresh_TAX);
            }
            
            function AJAX_Handle_Refresh_TAX() {
                if (AJAX_IsComplete()) {
                    //alert('AJAX_Handle_Refresh_DEPARTMENT');
                    var xmlDoc = AJAX.responseXML;
					alert(getXMLNodeValue(xmlDoc, "DOCTOR_CODE"));
                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "DOCTOR_CODE")) {
						mainForm.MODE.value = <%=DBMgr.MODE_INSERT%>;
						alert(mainForm.MODE.value);
                        return;
                    }
                    
                    mainForm.MODE.value = <%=DBMgr.MODE_UPDATE%>;
                    alert(mainForm.MODE.value);
                    //mainForm.submit();
                }
            }	
            function getSetRadio(obj)
			{ 
				for (var i=0; i < obj.length; i++)
				{
					if (arguments.length > 1)
					{ // we are setting
						if (arguments[1] == null) 
						{	obj[i].checked = false;	} 
						else if (obj[i].value == arguments[1]) 
						{
							obj[i].checked = true;
							return true;
						}
					}
					else // we are getting
					{
						if (obj[i].checked) 
						return obj[i].value;
					}
				}
				return false;
			}
			/*เวลาใช้
						var e = document.frm;
						if(getSetRadio(e.heart_sex)==false)
						{
							alert('กรุณาเลือกเพศ');
							e.heart_sex[0].focus();
							return false;
							}
						if(true)
						{
							frm.submit();
						}
			*/
        function SAVE_Click()
        {
        	//alert("save ok");
        	//alert("mode="+mainForm.MODE.value);
        	//if(mainForm.MODE.value==<%//=DBMgr.MODE_INSERT%>)
        	//{
        		//document.mainForm.submit();
        	//}
        	if (!isObjectEmptyString(document.mainForm.a01, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                  !isObjectEmptyString(document.mainForm.a13, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') && 
                    isObjectValidNumber(document.mainForm.a01, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') &&
                    isObjectValidNumber(document.mainForm.a13, '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>') ) 
             {
             	if(getSetRadio(mainForm.DOCTOR_STATUS)==false)
				{
					alert('กรุณาเลือกสถานภาพ');
					mainForm.DOCTOR_STATUS[0].focus();
					return false;
				}
             	if(mainForm.PEOPLE_NO.value !="")
             	{
             		//alert("ok");
             		//alert(mainForm.PEOPLE_NO.value.length);
             		if(parseInt(mainForm.PEOPLE_NO.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนผู้มีเงินได้ไม่ครบ 13 หลัก");
             			mainForm.PEOPLE_NO.focus();
             			return false;
             		}
             	}
             	if(mainForm.PEOPLE_TAX_ID.value !="")
             	{
             		if(parseInt(mainForm.PEOPLE_TAX_ID.value.length) < 10)
             		{
             			alert("เลขประจำตัวผู้เสียภาษีอากรของผู้มีเงินได้ไม่ครบ 10 หลัก");
             			mainForm.PEOPLE_TAX_ID.focus();
             			return false;
             		}
             	}
             	if(mainForm.SPOUSE_NO.value !="")
             	{
             		if(parseInt(mainForm.SPOUSE_NO.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนคู่สมรสไม่ครบ 13 หลัก");
             			mainForm.SPOUSE_NO.focus();
             			return false;
             		}
             	}
             	if(mainForm.SPOUSE_TAX_ID.value !="")
             	{
             		if(parseInt(mainForm.SPOUSE_TAX_ID.value.length) < 10)
             		{
             			alert("เลขประจำตัวผู้เสียภาษีอากรของคู่สมรสไม่ครบ 10 หลัก");
             			mainForm.SPOUSE_TAX_ID.focus();
             			return false;
             		}
             	}
             	if(mainForm.c04_01.checked==true)
             	{
             		if(parseInt(mainForm.c04_03.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนบิดาของผู้มีเงินได้ไม่ครบ 13 หลัก");
             			mainForm.c04_03.focus();
             			return false;
             		}
             	}
             	if(mainForm.c04_02.checked==true)
             	{
             		if(parseInt(mainForm.c04_04.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนมารดาของผู้มีเงินได้ไม่ครบ 13 หลัก");
             			mainForm.c04_04.focus();
             			return false;
             		}
             	}
             	if(mainForm.c04_06.checked==true)
             	{
             		if(parseInt(mainForm.c04_08.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนบิดาของคู่สมรสไม่ครบ 13 หลัก");
             			mainForm.c04_08.focus();
             			return false;
             		}
             	}
             	if(mainForm.c04_07.checked==true)
             	{
             		if(parseInt(mainForm.c04_09.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนมารดาของคู่สมรสไม่ครบ 13 หลัก");
             			mainForm.c04_09.focus();
             			return false;
             		}
             	}
             	if(mainForm.c06_01.checked==true)
             	{
             		if(parseInt(mainForm.c06_03.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนบิดาของผู้มีเงินได้ในการจ่ายเบี้ยประกันสุขภาพไม่ครบ 13 หลัก");
             			mainForm.c06_03.focus();
             			return false;
             		}
             	}
             	if(mainForm.c06_02.checked==true)
             	{
             		if(parseInt(mainForm.c06_04.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนมารดาของผู้มีเงินได้ในการจ่ายเบี้ยประกันสุขภาพไม่ครบ 13 หลัก");
             			mainForm.c06_04.focus();
             			return false;
             		}
             	}
             	if(mainForm.c06_06.checked==true)
             	{
             		if(parseInt(mainForm.c06_08.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนบิดาของคู่สมรสในการจ่ายเบี้ยประกันสุขภาพไม่ครบ 13 หลัก");
             			mainForm.c06_08.focus();
             			return false;
             		}
             	}
             	if(mainForm.c06_07.checked==true)
             	{
             		if(parseInt(mainForm.c06_09.value.length) < 13)
             		{
             			alert("เลขประจำตัวประชาชนมารดาของคู่สมรสในการจ่ายเบี้ยประกันสุขภาพไม่ครบ 13 หลัก");
             			mainForm.c06_09.focus();
             			return false;
             		}
             	}
            //alert("ssssssssssssssssssssss");
            document.mainForm.REPORT_DISPLAY.value = "";
        		document.mainForm.submit();
        	}
        }	
        function Report_View()
         {
         	  document.mainForm.REPORT_DISPLAY.value = "view";
              document.mainForm.action = "../../ViewReportSrvl";
              document.mainForm.target = "_blank";
              document.mainForm.submit();
				
            }
		function Ch14(id)
		{
			if(id=="2")
			{
				mainForm.a17.disabled=false;
				mainForm.a17.focus();
			}
			else
			{
				mainForm.a17.disabled=true;
			}
		}
    var CONTS_04 = 30000.00;
    
    function SelectElement(ElementName){
        return document.getElementById(ElementName);
    }    
    function SelectMaritalStatus(id)
    {   
    	//alert(id);
    	if(id =="2")//สมรส
    	{
   			mainForm.SPOUSE_NO.disabled=false;
    		mainForm.SPOUSE_BIRTHDAY.disabled=false;
    		mainForm.SPOUSE_TAX_ID.disabled=false;
    		mainForm.SPOUSE_NAME.disabled=false;
    		
    		mainForm.SPOUSE_TYPE[0].disabled=false;
    		mainForm.SPOUSE_TYPE[1].disabled=false;
    		mainForm.SPOUSE_TYPE[2].disabled=false;
    		mainForm.SPOUSE_TYPE[3].disabled=false;
    		if(mainForm.SPOUSE_TYPE[0].checked==true)
    		{
    			mainForm.SPOUSE_TYPE1[0].disabled=false;
	    		mainForm.SPOUSE_TYPE1[1].disabled=false;
	    		mainForm.SPOUSE_TYPE1[2].disabled=false;
    		}
    		else
    		{
	    		mainForm.SPOUSE_TYPE1[0].disabled=true;
	    		mainForm.SPOUSE_TYPE1[1].disabled=true;
	    		mainForm.SPOUSE_TYPE1[2].disabled=true;
	    	}
	    	//จำนวนบุตร
    		//mainForm.c03_01.disabled=false;
    		//mainForm.c03_03.disabled=false;
    		
			mainForm.c04_08.disabled=false;
			mainForm.c04_09.disabled=false;
			mainForm.c06_08.disabled=false;
			mainForm.c06_09.disabled=false;
			
    		//เงินได้และเบี้ยประกันสุขภาพ
			mainForm.c04_06.disabled=false;
			mainForm.c04_07.disabled=false;
			mainForm.c06_06.disabled=false;
			mainForm.c06_07.disabled=false;
			
        }
        else 
        {
			mainForm.SPOUSE_NO.disabled=true;
			mainForm.SPOUSE_NO.value="";
    		mainForm.SPOUSE_BIRTHDAY.disabled=true;
    		mainForm.SPOUSE_BIRTHDAY.value="";
    		mainForm.SPOUSE_TAX_ID.disabled=true;
    		mainForm.SPOUSE_TAX_ID.value="";
    		mainForm.SPOUSE_NAME.disabled=true;
    		mainForm.SPOUSE_NAME.value="";
    		
    		mainForm.SPOUSE_TYPE[0].disabled=true;
    		mainForm.SPOUSE_TYPE[1].disabled=true;
    		mainForm.SPOUSE_TYPE[2].disabled=true;
    		mainForm.SPOUSE_TYPE[3].disabled=true;
    		
    		mainForm.SPOUSE_TYPE1[0].disabled=true;
    		mainForm.SPOUSE_TYPE1[1].disabled=true;
    		mainForm.SPOUSE_TYPE1[2].disabled=true;
    		
    		mainForm.SPOUSE_TYPE[0].checked=false;
    		mainForm.SPOUSE_TYPE[1].checked=false;
    		mainForm.SPOUSE_TYPE[2].checked=false;
    		mainForm.SPOUSE_TYPE[3].checked=false;
    		
    		mainForm.SPOUSE_TYPE1[0].checked=false;
    		mainForm.SPOUSE_TYPE1[1].checked=false;
    		mainForm.SPOUSE_TYPE1[2].checked=false;
    		
    		
			//เงินได้และเบี้ยประกันสุขภาพ
			mainForm.c04_06.disabled=true;
			mainForm.c04_07.disabled=true;
			mainForm.c06_06.disabled=true;
			mainForm.c06_07.disabled=true;
			
			mainForm.c04_06.checked=false;
			mainForm.c04_07.checked=false;
			mainForm.c06_06.checked=false;
			mainForm.c06_07.checked=false;
			
			mainForm.c04_08.disabled=true;
			mainForm.c04_09.disabled=true;
			mainForm.c06_08.disabled=true;
			mainForm.c06_09.disabled=true;
			
			mainForm.c04_08.value="";
			mainForm.c04_09.value="";
			mainForm.c04_10.value="";
			mainForm.c06_08.value="";
			mainForm.c06_09.value="";
			mainForm.c06_10.value="";
			
			//รายได้คู่สมรส
			mainForm.c02.value=0;
			//จำนวนบุตร
    		mainForm.c03_01.disabled=true;
    		mainForm.c03_01.value="";
    		mainForm.c03_03.disabled=true;
    		mainForm.c03_03.value="";
    		mainForm.c03_02.value="";
    		mainForm.c03_04.value="";
    	
			
			
        }
        JsC12();
    }
	function ChType(id)
	{
		//alert("chtype="+id);
		//จำนวนบุตร
    		mainForm.c03_01.disabled=false;
    		mainForm.c03_03.disabled=false;
		if(id==1)
		{
			mainForm.SPOUSE_TYPE1[0].disabled=false;
    		mainForm.SPOUSE_TYPE1[1].disabled=false;
    		mainForm.SPOUSE_TYPE1[2].disabled=false;
			mainForm.c02.value=0.00;
			mainForm.b05.disabled=true;
			//JsC12();
		}
		else if(id==2)
		{
			mainForm.SPOUSE_TYPE1[0].disabled=true;
    		mainForm.SPOUSE_TYPE1[1].disabled=true;
    		mainForm.SPOUSE_TYPE1[2].disabled=true;
			mainForm.SPOUSE_TYPE1[0].checked=false;
    		mainForm.SPOUSE_TYPE1[1].checked=false;
    		mainForm.SPOUSE_TYPE1[2].checked=false;
			mainForm.c02.value=30000.00;
			mainForm.b05.disabled=false;
			//JsC12();
		}
		else if(id==3)
		{
			mainForm.SPOUSE_TYPE1[0].disabled=true;
    		mainForm.SPOUSE_TYPE1[1].disabled=true;
    		mainForm.SPOUSE_TYPE1[2].disabled=true;
    		mainForm.SPOUSE_TYPE1[0].checked=false;
    		mainForm.SPOUSE_TYPE1[1].checked=false;
    		mainForm.SPOUSE_TYPE1[2].checked=false;
			
			mainForm.c02.value=0.00;
			mainForm.b05.disabled=true;
			//JsC12();
		}
		else if(id==4)
		{
			mainForm.SPOUSE_TYPE1[0].disabled=true;
    		mainForm.SPOUSE_TYPE1[1].disabled=true;
    		mainForm.SPOUSE_TYPE1[2].disabled=true;
    		mainForm.SPOUSE_TYPE1[0].checked=false;
    		mainForm.SPOUSE_TYPE1[1].checked=false;
    		mainForm.SPOUSE_TYPE1[2].checked=false;
			
			mainForm.c02.value=30000.00;
			mainForm.b05.disabled=true;
			
		}	
		JsC12();
		JsC03_01();	
		JsC03_03();
	}
    function Js01(){//ok
        //alert(Obj.value);
        var tax_gov_def = 60000.00;
        var Income = SelectElement('a01').value ;
        if(Income == "") { Income=0;}
        var a02 = SelectElement('a02');
        var a03 = SelectElement('a03');
        var a04 = SelectElement('a04');//4.หักค่าใช้จ่าย(ร้อยละ 40 ของ 3. แต่ไม่เกินที่กฎหมายกำหนด)
        var a05 = SelectElement('a05');
        var a06 = SelectElement('a06');
        var a07 = SelectElement('a07');
		
		if(a02.value=="") { a02.value=0; }
		if(a03.value=="") { a03.value=0; }
		if(a04.value=="") { a04.value=0; }
		if(a05.value=="") { a05.value=0; }
		if(a06.value=="") { a06.value=0; }
		if(a07.value=="") { a07.value=0; }
		/*if(JsINT(SelectElement('a01')) <= 0){
        	alert('เงินเดือน ค่าจ้าง บำนาญ ฯลฯ ต้องมีค่ามากกว่า 0'); 
        	//SelectElement('a01').focus();
			return;
        }*/
        if(mainForm.DOCTOR_CODE.value !="")
        {
        	mainForm.bt_save.disabled=false;
        }
        //a02.value = 0;
        a03.value = (parseFloat(Income) - parseFloat(a02.value)).toFixed(2);
        a04.value = ((parseFloat(a03.value)*40)/100).toFixed(2) ;
        if(parseFloat(a04.value) > tax_gov_def) a04.value = tax_gov_def.toFixed(2);
        a05.value = (parseFloat(a03.value) - parseFloat(a04.value)).toFixed(2);
        a07.value = (parseFloat(a05.value) - parseFloat(a06.value)).toFixed(2); 
        
        if(parseFloat(a03.value)<0) {a03.value=0;}
        if(parseFloat(a04.value)<0) {a04.value=0;}
        if(parseFloat(a05.value)<0) {a05.value=0;}
        if(parseFloat(a07.value)<0) {a07.value=0;}

        //----
        
        Js10();
        Js08();
        //Js09();
        Js12();
        Js13();
        //JsB06();
    }

    function Js08(){
        var ma_def = 10;//
        var a07 = SelectElement('a07'); 
		var a08_01 = SelectElement('a08_01');
		var a08_02 = SelectElement('a08_02');
		if(a07.value=="") { a07.value=0; }
		if(a08_01.value=="") { a08_01.value=0; }
		if(a08_02.value=="") { a08_02.value=0; }
		var ma_chk = (parseFloat(a07.value) * ma_def) / 100;
		
		a08_02.value = (parseFloat(a08_01.value) * 2).toFixed(2);
		if(parseFloat(a08_02.value) > ma_chk) a08_02.value = ma_chk.toFixed(2);	
		if(parseFloat(a08_02.value)< 0){a08_02.value=0;}
		//----
		Js10();	
    }
/*
    function Js09(){
        var ma_def = 20;//
        var a07 = SelectElement('a07'); 
		var a09_01 = SelectElement('a09_01');
		var a09_02 = SelectElement('a09_02');
		var ma_chk = (a07.value * ma_def) / 100;
		
		a09_02.value = a09_01.value * 1.5;
		if(a09_02.value > ma_chk) a09_02.value = ma_chk;

		//---
		Js10();	
    }*/

    function Js10(){//ok ข้อที่ 9
	//alert("okkkkk");
        var a07 = SelectElement('a07');
        var a08_02 = SelectElement('a08_02');
		var a09 = SelectElement('a09');
		//alert(a07.value);
		//alert(a08_02.value);
		if(a07.value=="") { a07.value=0; }
		if(a08_02.value=="") { a08_02.value=0; }
		if(a09.value=="") { a09.value=0; }
        a09.value = (parseFloat(a07.value) - parseFloat(a08_02.value)).toFixed(2);
        if(parseFloat(a09.value)<0) {a09.value=0;}
		//alert("a09="+a09.value);
        Js12();
        Js13();
    }

    function Js11(){//ok ข้อที่ 10
        var ma_def = 10;//
        var a09 = SelectElement('a09'); 
        var a10 = SelectElement('a10'); 
        if(a09.value=="") 
        { a09.value=0; }
        if(parseFloat(a09.value)<0)
        {
        	alert("จำนวนเงินจากข้อ 9 มีค่าน้อยกว่า 0");
        	a10.value=0;
        }
        else
        {
	        var ma_chk = (parseFloat(a09.value) * ma_def) / 100;
	        if(parseFloat(a10.value) > ma_chk){
	            alert("หักเงินบริจาค(ไม่เกินร้อยละ 10 ของ 9.)");
	            a10.focus();
	        }
        }
        Js12();
    }

    function Js12(){//ok ข้อที่ 11
        var a09 = SelectElement('a09');
        var a10 = SelectElement('a10');
        var a11 = SelectElement('a11');
        if(a09.value=="") { a09.value=0; }
        if(a10.value=="") { a10.value=0; }
        if(a11.value=="") { a11.value=0; }
        a11.value = (parseFloat(a09.value) - parseFloat(a10.value)).toFixed(2);
        if(parseFloat(a11.value)<=0) {a11.value=0;}
        Js13();
    }
    /*function Js13(){//ok ข้อที่ 12
       	var a11 = SelectElement('a11').value;
       	var a12 = SelectElement('a12');
       	//alert("Js13");
       	//alert("a11="+a11.value);
       	if(a11.value=="") { a11.value=0; }
       	if(a12.value=="") { a12.value=0; }
       	var a11_tax=0;
       	var a11_result=0;
       	var a11_percent=0;
       	var a11_total=0;
       	if(a11<=150000)
       	{
       		a11_total=0;
       	}
       	else 
       	{
       		//alert("OK");
	       	if(a11>=150001 && a11<=500000)
	       	{
	       		a11_tax=150000;
	       		a11_percent=10;
	       	}
	       	else if(a11>=500001 && a11<=1000000)
	       	{
	       		a11_tax=500000;
	       		a11_percent=20;
	       	}
	       	else if(a11>=1000001 && a11<=4000000)
	       	{
	       		a11_tax=1000000;
	       		a11_percent=30;
	       	}
	       	else if(a11>=4000001)
	       	{
	       		a11_tax=4000000;
	       		a11_percent=37;
	       	}
	       	//alert("a11_tax="+a11_tax);
	       	a11_result=a11-a11_tax;
	       	//alert("a11_result="+a11_result);
	       	a11_total=(a11_result*a11_percent)/100;
	       	
	    }
	    //alert("a11_total="+a11_total);
        a12.value = a11_total.toFixed(2);
        Js15();
       
    }*/
    function Js13()
    {
    	var a11 = SelectElement('a11').value;
       	var a12 = SelectElement('a12');
       	//alert("Js13");
       	//alert("a11="+a11.value);
       	if(a11.value=="") { a11.value=0; }
       	if(a12.value=="") { a12.value=0; }
       	var a11_tax=0;
       	var a11_result=0;
       	var a11_percent=0;
       	var a11_total=0;
       	var test;
        
        if(parseFloat(a11) < 150001){
            test = 0;
        }else if(parseFloat(a11) < 500001){
            test = stepOne(a11);
        }else if(parseFloat(a11) < 1000001){
            test = stepTwo(a11);
        }else if(parseFloat(a11) < 4000001){
            test = stepThree(a11);
        }else{
            test = stepFour(a11);
        }
        a12.value = test.toFixed(2);
        if(parseFloat(a12.value)<=0) { a12.value=0;}
        Js15();
    }
    
    function stepOne(ct){
        var money = 0;
        money = parseFloat(ct) - 150000;
        //System.out.println("1Money : "+money+" | "+ct+" - 150000");
        money  = money * 0.1;
        //System.out.println("1Tax : "+money);
        return money;
    }
    function stepTwo(ct){
        var money = 0;
        money = parseFloat(ct) - 500000;
        //System.out.println("2Money : "+money+" | "+ct+" - 500000");
        money = (money * 0.2) + stepOne(500000);
        //System.out.println("2Tax : "+money);
        return money;
    }
    function stepThree(ct){
        var money = 0;
        money = parseFloat(ct) - 1000000;
        //System.out.println("3Money : "+money);
        money = (money * 0.3) + stepTwo(1000000);
        //System.out.println("3Tax : "+money);
        return money;
    }
    function stepFour(ct){
        var money = 0;
        money = parseFloat(ct) - 4000000;
        money = (money * 0.37) + stepThree(4000000);
        return money;
    }
    
    function Js14(){
        Js15();
    }
    function Js15(){
    	var a12_1 = JsINT(SelectElement('a12'));//.value;
    	var a13_1 = JsINT(SelectElement('a13'));//.value;  
    	var a12 = SelectElement('a12').value;
    	var a13 = SelectElement('a13').value;  
    	if(a12.value=="") { a12.value=0; }
    	if(a13.value=="") { a13.value=0; }
    	var result;
    	//alert("a12="+a12);  
    	//alert("a13="+a13);	
    	if(a12_1==0 && a13_1==0)
    	{
    		result=0;
    		SelectElement('a14').value = result.toFixed(2);
    		SelectElement('a14_01').checked = false;//ชำระเพิ่มเติม
    		SelectElement('a14_02').checked = false;//ชำระเกินไว้
    	}
    	else if(a12_1 > a13_1){
        	//alert('a12 > a13');
    		result = parseFloat(a12) - parseFloat(a13);
    		if(result<=0) { result=0;}
    		SelectElement('a14').value = result.toFixed(2);
    		SelectElement('a14_01').checked = true;//ชำระเพิ่มเติม
    		SelectElement('a14_02').checked = false;//ชำระเกินไว้
    		SelectElement('a14_01_h').value =1;//ชำระเพิ่มเติม
    	}else if(a12_1 < a13_1){
    		//alert('a12 < a13');
    		result = parseFloat(a13) - parseFloat(a12);
    		if(result<=0) { result=0;}
    		SelectElement('a14').value = result.toFixed(2);
    		SelectElement('a14_01').checked = false;//ชำระเพิ่มเติม
    		SelectElement('a14_02').checked = true;//ชำระเกินไว้
    		SelectElement('a14_01_h').value =2;//ชำระเกินไว้
    	}
    	Js19();
    	//Js21();
    }

    function Js18(){
    	Js19();
    	//Js21();     
    }
    
    function Js19(){//ข้อที่ 18
    //alert("LLLLLLLLLLLLLLLLLLLL");
    	var a12 = SelectElement('a12').value;
    	var a13 = SelectElement('a13').value;    	
    	var a17 = SelectElement('a17').value; 
    	var a12_1 = JsINT(SelectElement('a12'));//.value;
    	var a13_1 = JsINT(SelectElement('a13'));//.value;  
    	var a17_1 = JsINT(SelectElement('a17'));//.value;
    	var result;
    	if(a12=="") {a12=0; }
    	if(a13=="") {a13=0;	}
    	if(a17=="") {a17=0;	}
    	//alert("a12="+a12);
    	//alert("a13="+a13);
    	//alert("a17="+a17); 
    	if(a12_1==0 && a13_1==0)
    	{
    		//alert("0");
    		result=0;
    		SelectElement('a18').value = result.toFixed(2);
	    	SelectElement('a18_01').checked = false;
	    	SelectElement('a18_02').checked = false;
	    		
	    	SelectElement('a20').value = result.toFixed(2);
	    	SelectElement('a20_01').checked = false;
	    	SelectElement('a20_02').checked = false;
    	} 
    	else if(a12_1 > a13_1)
    	{
    		//alert(">");
    		result = parseFloat(a12) - parseFloat(a13) - parseFloat(a17);
    		if(result<0)
    		{
    			result=-1*result;
    			SelectElement('a18').value = result.toFixed(2);
	    		SelectElement('a18_01').checked = false;
	    		SelectElement('a18_02').checked = true;
	    		
	    		SelectElement('a20').value = result.toFixed(2);
	    		SelectElement('a20_01').checked = false;
	    		SelectElement('a20_02').checked = true;
	    		
	    		SelectElement('a18_01_h').value =2;//ชำระเกินไว้
	    		SelectElement('a20_01_h').value =2;//ชำระเกินไว้
    		}
    		else
    		{
	    		SelectElement('a18').value = result.toFixed(2);
	    		SelectElement('a18_01').checked = true;
	    		SelectElement('a18_02').checked = false;
	    		
	    		SelectElement('a20').value = result.toFixed(2);
	    		SelectElement('a20_01').checked = true;
	    		SelectElement('a20_02').checked = false;
	    		
	    		SelectElement('a18_01_h').value =1;//ชำระเพิ่มเติม
	    		SelectElement('a20_01_h').value =1;//ชำระเพิ่มเติม
	    	}
    	}
    	else
    	{
    		//alert("<");
    		result = parseFloat(a13) - parseFloat(a12) + parseFloat(a17);
    		alert("result="+result);
    		if(result<0)
    		{
    			result=-1*result;
	    		SelectElement('a18').value = result.toFixed(2);
	    		SelectElement('a18_01').checked = true;
	    		SelectElement('a18_02').checked = false;
	    		
	    		SelectElement('a20').value = result.toFixed(2);
	    		SelectElement('a20_01').checked = true;
	    		SelectElement('a20_02').checked = false;
	    		
	    		SelectElement('a18_01_h').value =1;//ชำระเพิ่มเติม
	    		SelectElement('a20_01_h').value =1;//ชำระเพิ่มเติม
	    	}
	    	else
	    	{
	    		SelectElement('a18').value = result.toFixed(2);
	    		SelectElement('a18_01').checked = false;
	    		SelectElement('a18_02').checked = true;
	    		
	    		SelectElement('a20').value = result.toFixed(2);
	    		SelectElement('a20_01').checked = false;
	    		SelectElement('a20_02').checked = true;
	    		
	    		SelectElement('a18_01_h').value =2;//ชำระเกินไว้
		    	SelectElement('a20_01_h').value =2;//ชำระเกินไว้
		    }
    	}
    }
/*
    function Js21(){//ข้อที่ 20
    	var a12 = SelectElement('a12').value;
    	var a13 = SelectElement('a13').value; 
    	var a17 = SelectElement('a17').value;
    	if(a13=="") a13=0; 
    	if(a14=="") a14=0;
    	if(a17=="") a17=0;
    	    	
    	if(a12 > a13){
    		SelectElement('a20').value = parseInt(a12) - parseInt(a13) - parseInt(a17);
    		SelectElement('a20_01').checked = true;
    		SelectElement('a20_02').checked = false;
    	}else{
    		SelectElement('a20').value = parseInt(a13) - parseInt(a12) + parseInt(a17);
    		SelectElement('a20_01').checked = false;
    		SelectElement('a20_02').checked = true;
    	}
    }    
   */ 
    function JsB06(){//ok
		var a = SelectElement('b01').value;
		var b = SelectElement('b02').value;
		var c = SelectElement('b03').value;
		var d = SelectElement('b04').value;
		var e = SelectElement('b05').value;
		var f = SelectElement('b06').value;

				
		if(a=='') a = 0;
		if(b=='') b = 0;
		if(c=='') c = 0;
		if(d=='') d = 0;
		if(e=='') e = 0;
		if(f=='') f = 0;
		
		var g = SelectElement('b07');
		g.value = ( parseFloat(a) + parseFloat(b) + parseFloat(c) + parseFloat(d) + parseFloat(e) + parseFloat(f) ).toFixed(2);
		if(parseFloat(g.value)<0) { g.value=0;}
		SelectElement('a02').value = g.value;
		Js01();
    }

    function JsC03(el){//ok
    	var a = SelectElement('c03_01');
    	var b = SelectElement('c03_03');
    	var c = SelectElement('c03_02');
    	var d = SelectElement('c03_04');
        var x = 0;
        var y = 0;
        if(a.value == '' ){
            x = 0;
        }else{
            x = parseFloat(a.value);
        }
        if(b.value == '' ){
            y = 0;
        }else{
            y = parseFloat(b.value);
        }        
        if((x+y) > 3){
            alert('ลดหย่อนบุตรทั้งหมดได้ไม่เกิน 3 คน');
            //SelectElement(el).value = '';
            SelectElement(el).focus();   
            SelectElement('c03_01').value="";   
            SelectElement('c03_03').value="";
            SelectElement('c03_02').value="";   
            SelectElement('c03_04').value="";                           
            return false;
        }
		else
		{
			return true;
		}
    }
    function JsC03_01(){//ok
        var e = SelectElement('c03_01');
        var a = SelectElement('c03_02');
        var z = 0;
        if(e.value == '' ){
            z = 0;
        }else{
            z = parseFloat(e.value);
        }
        if(JsC03('c03_01'))
		{
			if(mainForm.SPOUSE_TYPE[0].checked==true || mainForm.SPOUSE_TYPE1[0].checked==true || mainForm.SPOUSE_TYPE1[1].checked==true || mainForm.SPOUSE_TYPE1[2].checked==true || mainForm.SPOUSE_TYPE[2].checked==true)
			{
				a.value = (7500*z).toFixed(2);   
			}
			else
			{
				a.value = (15000*z).toFixed(2);
			}
			JsC12(); 
		}
    }

    function JsC03_03(){//ok
        var e = SelectElement('c03_03');
        var a = SelectElement('c03_04');
        var z = 0;
        if(e.value == '' ){
            z = 0;
        }else{
            z = parseFloat(e.value);
        }
        if(JsC03('c03_03'))
		{
			if(mainForm.SPOUSE_TYPE[0].checked==true || mainForm.SPOUSE_TYPE1[0].checked==true || mainForm.SPOUSE_TYPE1[1].checked==true || mainForm.SPOUSE_TYPE1[2].checked==true || mainForm.SPOUSE_TYPE[2].checked==true)
			{
				a.value = (8500*z).toFixed(2);   
			}
			else
			{
				a.value = (17000*z).toFixed(2);
			}
			//a.value = (17000*z).toFixed(2);
			JsC12();
		}
    }
    
    function JsINT(obj){        
        if(obj.value != null){
	        if(obj.value == ''){
	            return parseFloat('0');
	        }else{
	            return parseFloat(obj.value);
	        }
        }else{
            return parseFloat('0');
        }
    }
    
  	//__________________    
    function JsC04_01(){//ok
    	if(SelectElement('c04_01').checked == true){
        	SelectElement('c04_03').readOnly = false;
        }else{
        	SelectElement('c04_03').readOnly = true; 
        }  
    	JsC04_05();       	
    }

    function JsC04_02(){//ok
    	if(SelectElement('c04_02').checked == true){
        	SelectElement('c04_04').readOnly = false;
        }else{
        	SelectElement('c04_04').readOnly = true; 
        } 
    	JsC04_05();      
    }
    
    function JsC04_05(){//ok
        var e = SelectElement('c04_05');
    	if(SelectElement('c04_01').checked == true && SelectElement('c04_02').checked == true){
        	e.value = (CONTS_04*2).toFixed(2);
    	}else if(SelectElement('c04_02').checked == true){
    		e.value = (CONTS_04).toFixed(2);
    	}else if(SelectElement('c04_01').checked == true){
    		e.value = (CONTS_04).toFixed(2);
    	}else{
    		e.value = 0.00;
    	}
    	JsC12();
    }
    
    function JsC04_06(){//ok
    	if(SelectElement('c04_06').checked == true){
        	SelectElement('c04_08').readOnly = false;
        }else{
        	SelectElement('c04_08').readOnly = true; 
        }  
    	JsC04_10();       	
    }

    function JsC04_07(){//ok
    	if(SelectElement('c04_07').checked == true){
        	SelectElement('c04_09').readOnly = false;
        }else{
        	SelectElement('c04_09').readOnly = true; 
        } 
    	JsC04_10();      
    }
    
    function JsC04_10(){//ok
        var e = SelectElement('c04_10');
    	if(SelectElement('c04_06').checked == true && SelectElement('c04_07').checked == true){
        	e.value = (CONTS_04*2).toFixed(2);
    	}else if(SelectElement('c04_06').checked == true){
    		e.value = (CONTS_04).toFixed(2);
    	}else if(SelectElement('c04_07').checked == true){
    		e.value = (CONTS_04).toFixed(2);
    	}else{
    		e.value = 0.00;
    	}
    	JsC12();
    }

    //__________________
    function JsC05_01(){//ok
    	if(SelectElement('c06_01').checked == true){
        	SelectElement('c06_03').readOnly = false;
        }else{
        	SelectElement('c06_03').readOnly = true; 
        }
    	JsC05_10();          
    }

    function JsC05_02(){//ok
    	if(SelectElement('c06_02').checked == true){
        	SelectElement('c06_04').readOnly = false;
        }else{
        	SelectElement('c06_04').readOnly = true; 
        }   
    	JsC05_10();      
    }

 /*   function JsC05_05(){
        if(SelectElement('c06_01').checked == true || SelectElement('c06_02').checked == true){
        	SelectElement('c06_05').readOnly = false;
        }else{
        	SelectElement('c06_05').value = '';
        	SelectElement('c06_05').readOnly = true;
        }
        JsC12();
    }*/
    
    function JsC05_06(){//ok
    	if(SelectElement('c06_06').checked == true){
        	SelectElement('c06_08').readOnly = false;
        }else{
        	SelectElement('c06_08').readOnly = true; 
        }  
    	JsC05_10();       
    }

    function JsC05_07(){//ok
    	if(SelectElement('c06_07').checked == true){
        	SelectElement('c06_09').readOnly = false;
        }else{
        	SelectElement('c06_09').readOnly = true;  
        }  
    	JsC05_10();       
    }

    function JsC05_10(){
        if(SelectElement('c06_01').checked == true || SelectElement('c06_02').checked == true || SelectElement('c06_06').checked == true || SelectElement('c06_07').checked == true){
        	SelectElement('c06_10').readOnly = false;
        }else{
        	SelectElement('c06_10').value = '';
        	SelectElement('c06_10').readOnly = true;
        }        
        JsC12();
    }

    function JsC12(){//ok ข้อที่ ก6
    //alert("JsC12");
        var e = SelectElement('c14');
        var c01 = JsINT(SelectElement('c01'));
        var c02 = JsINT(SelectElement('c02'));
        var c03_02 = JsINT(SelectElement('c03_02'));
        var c03_04 = JsINT(SelectElement('c03_04'));
        var c04_05 = JsINT(SelectElement('c04_05'));
        var c04_10 = JsINT(SelectElement('c04_10'));
        var c05 = JsINT(SelectElement('c05'));
        var c06_10 = JsINT(SelectElement('c06_10'));
       	var c07 = JsINT(SelectElement('c07'));
        var c08 = JsINT(SelectElement('c08'));
        var c09 = JsINT(SelectElement('c09'));
        var c10 = JsINT(SelectElement('c10'));
        var c11 = JsINT(SelectElement('c11'));
        var c12 = JsINT(SelectElement('c12'));
        var c13 = JsINT(SelectElement('c13'));
        e.value = (c01 + c02 + c03_02 + c03_04 + c04_05 + c04_10 + c05 + c06_10 + c07 + c08 + c09 + c10 + c11 + c12 + c13).toFixed(2);
		//alert("e="+e.value);
        SelectElement('a06').value = e.value;
        Js01();
    }
    
    function computeTax(){//ข้อที่ 12
    	
        //alert(SelectElement('a12').value);
        //alert("goto compute tax");
        AJAX_Send_Request();        
    }
    
    /*function Add_tax(){//ok
        if(SelectElement('add_tax').checked == true){
        	SelectElement('a17').readOnly = false;
        }else{
        	SelectElement('a17').readOnly = true;
        }
    }*/

	function styleMNo(id){
		/*SelectElement('a02').value = (0).toFixed(2);
		SelectElement('a14').value = (0).toFixed(2);
		SelectElement('a15').value = (0).toFixed(2);
		SelectElement('a16').value = (0).toFixed(2);
		SelectElement('a19').value = (0).toFixed(2);
		SelectElement('b07').value = (0).toFixed(2);
		SelectElement('c02').value = (0).toFixed(2);
		SelectElement('c04_05').value = (0).toFixed(2);
		SelectElement('c04_10').value = (0).toFixed(2);
		SelectElement('c03_02').value = (0).toFixed(2);
		SelectElement('c03_04').value = (0).toFixed(2);*/
		//SelectElement('a17').disabled=true;
		if(id !=2)
		{
			mainForm.SPOUSE_NO.disabled=true;
			mainForm.SPOUSE_TAX_ID.disabled=true;
			mainForm.SPOUSE_BIRTHDAY.disabled=true;
			mainForm.SPOUSE_NAME.disabled=true;
			
			mainForm.SPOUSE_TYPE[0].disabled=true;
	    	mainForm.SPOUSE_TYPE[1].disabled=true;
	    	mainForm.SPOUSE_TYPE[2].disabled=true;
	    	mainForm.SPOUSE_TYPE[3].disabled=true;
	
			mainForm.SPOUSE_TYPE1[0].disabled=true;
	    	mainForm.SPOUSE_TYPE1[1].disabled=true;
	    	mainForm.SPOUSE_TYPE1[2].disabled=true;
	    	//จำนวนบุตร
	    	mainForm.c03_01.disabled=true;
	    	mainForm.c03_03.disabled=true;
	    	//เงินได้และเบี้ยประกันสุขภาพ
			mainForm.c04_06.disabled=true;
			mainForm.c04_07.disabled=true;
			mainForm.c06_06.disabled=true;
			mainForm.c06_07.disabled=true;
			//คู่สมรสอายุตั้งแต่ 65 ปีขึ้นไปและมีเงินได้รวมคำนวณ 190,000 บาท
			mainForm.b05.disabled=true;
		}
		else
		{
			SelectMaritalStatus(id);
		}
		//button
    	//mainForm.bt_save.disabled=true;
    	//mainForm.bt_print.disabled=true;	

	}

	function styleMYes(){
		/*SelectElement('c02_01').value = 30000.00;
		
		SelectElement('a01_02').readOnly = false;
		
		SelectElement('b01_02').readOnly = false;
		SelectElement('b02_02').readOnly = false;
		SelectElement('b03_02').readOnly = false;
		SelectElement('b04_02').readOnly = false;
		SelectElement('b05_02').readOnly = false;	

		SelectElement('c04_06').disabled = false;
		SelectElement('c04_07').disabled = false;
		SelectElement('c05_06').disabled = false;
		SelectElement('c05_07').disabled = false;

		SelectElement('c06_02').readOnly = false;
		SelectElement('c07_02').readOnly = false;
		SelectElement('c08_02').readOnly = false;
		SelectElement('c09_02').readOnly = false;
		SelectElement('c11_02').readOnly = false;	*/		
	}

	function DetailOfChange(id){
	/*	//var a = SelectElement('MaritalStatus').value;
		//alert(a);
		
		var s = SelectElement('SelectStatus').value;	
		if(s=='5'){
			styleMYes();
		}else{
			styleMNo(id);
		}

		//------		
		JsC12();
		Js01();	*/	
	}  
    function JsOnLoad(id){
       // SelectElement('c01').value = CONTS_C01; 
        //SelectElement('a01').value = 0;       
        styleMNo(id);
        //JsC12();
        //Add_tax();
		    

    }
	//---- ให้คีย์ได้แต่ตัวเลข
	function IsNumericKeyPress()
	{
		if(event.keyCode ==46)
		{
			event.returnValue = true;
		}
		else if (((event.keyCode < 48) || (event.keyCode > 57)))
			{
				event.returnValue = false;
			}
	}
    //Ajax functions
    function AJAX_Send_Request() {
     	var target = "../../ProcessTexComputeSvrl?mDr=" +  SelectElement('a11').value ;
     	//alert("target="+target);
     	AJAX_Request(target, AJAX_Handle_Response);
    }
    
    function AJAX_Handle_Response() {              
        if (AJAX_IsComplete()) {
        	//alert("test 11");
            var xmlDoc = AJAX.responseXML;                    
            if (xmlDoc.getElementsByTagName("SUCCESS")[0] == null) {
                alert("Exception in update process");
                return;
            }

            //alert("current row loop : " + currentRowID);
            if (xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue == "0") {
            	//SelectElement('a12').value = xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue;
            	alert('Compute error!');
            }
            else {
           // alert("OKKKKKKKK");
            	SelectElement('a12').value = xmlDoc.getElementsByTagName("SUCCESS")[0].firstChild.nodeValue;
            	//SelectElement('a14').value = SelectElement('a12').value - SelectElement('a13').value;
            	//alert("12="+SelectElement('a12').value);
            	Js15();
            }                    
        }
        else{
        //alert("no complete");
        }
    }
</script>        
    <style type="text/css">
<!--
.style1 {
	font-size: xx-large;
	font-family: Arial, Helvetica, sans-serif;
	font-weight: bold;
}
.style3 {font-size: xx-small}
.style4 {
	font-size: x-large;
	font-weight: bold;
}
-->
    </style>
    </head><!--onload="JsOnLoad();"-->
 <%
 	String java_load="";
 	 if(MODE==DBMgr.MODE_UPDATE)
	 {
	 	java_load=DBMgr.getRecordValue(tax91Rec,"DOCTOR_STATUS");
	 	
	 }
	 
    %>
	<body onLoad="JsOnLoad('<%=java_load%>'); Js01(); ">
        <form id="mainForm" name="mainForm" method="post" >
		<input type="hidden" name="MODE" id="MODE" value="<%=MODE %>">
		<input type="hidden" id="REPORT_DISPLAY" name="REPORT_DISPLAY"/>
        <input type="hidden" id="REPORT_MODULE" name="REPORT_MODULE" value="tax_doctor_report"/>
        <input type="hidden" id="REPORT_FILE_NAME" name="REPORT_FILE_NAME" value="tax91_52">
        <input type="hidden" id="YYYY" name="YYYY" value="2009"/>
        <input type="hidden" id="a14_01_h" name="a14_01_h" />
        <input type="hidden" id="a18_01_h" name="a18_01_h" />
        <input type="hidden" id="a20_01_h" name="a20_01_h" />
        <table width="950" border="0" cellpadding="0" cellspacing="1" bgcolor="#FFFFFF" class="main_data_free_width">
            <tr>
                <td colspan="2">
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
                            <tr>
                                <td width="20%" rowspan="2" align="center" bgcolor="#CCCCCC">
                                    <span class="style4">ปีภาษี 2552</span></td>
                                <td width="60%" height="25" align="center" bgcolor="#CCCCCC" class="dpDayHighlight">แบบแสดงรายการภาษีเงินได้บุคคลธรรมดา</td>
                                <td width="20%" rowspan="2" align="center" bgcolor="#CCCCCC"><p class="style1">ภ.ง.ด. 91 </p></td>
                            </tr>
                            <tr>
                                <td height="25" align="center" bgcolor="#CCCCCC" class="msg">สำหรับผู้มีเงินได้จากการจ้างแรงงาน ตามมาตรา 40(1) แห่งประมวลรัษฎากร ประเภทเดียว </td>
                            </tr>
                            <tr>
                              <td height="25" colspan="3" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                  <td height="32" colspan="2">Doctor Code 
                                    <span class="input">
                                    <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" maxlength="20" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" readonly="readonly" onKeyPress="return CODE_KeyPress(event);" />
                                    <input id="SEARCH_CODE" name="SEARCH_CODE" type="image" class="image_button" src="../../images/search_button_profile.png" alt="Search" onClick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&BEINSIDEHOSPITAL=1&TARGET=DOCTOR_CODE&HANDLE=AJAX_Refresh_DOCTOR'); return false;" />
                                  </span><input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onClick="window.location = 'taxform.jsp?DOCTOR_CODE=' + document.mainForm.DOCTOR_CODE.value; return false;" />                        </td>
                                </tr>
                                <tr>
                                  <td width="46%" valign="top"><table width="100%" border="1" cellspacing="1" cellpadding="1">
                                    <tr>
                                      <td colspan="2" align="center" bordercolor="#FFFFFF" bgcolor="#FFFF99" class="dpDayHighlightTD"><strong>ผู้มีเงินได้</strong></td>
                                    </tr>
                                    <tr>
                                      <td width="37%" bordercolor="#FFFFFF" class="data">เลขประจำตัวประชาชน</td>
                                      <td width="63%" bordercolor="#FFFFFF"><input name="PEOPLE_NO" type="text" id="PEOPLE_NO" size="15" maxlength="13" value="<%= people_no%>" readonly="readonly" /></td>
                                    </tr>
                                    <tr>
                                      <td bordercolor="#FFFFFF" class="data">เลขประจำตัวผู้เสียภาษีอากร</td>
                                      <td bordercolor="#FFFFFF"><input name="PEOPLE_TAX_ID" type="text" id="PEOPLE_TAX_ID" size="15" maxlength="10" value="<%= people_tax_id%>" readonly="readonly" />
                                          <br />
                                          <span class="style3">(กรอกเฉพาะกรณีเป็นผู้ไม่มีเลขประจำตัวประชาชน) </span></td>
                                    </tr>
                                    <tr>
                                      <td bordercolor="#FFFFFF" class="data">วันเดือนปีเกิด</td>
                                      <td bordercolor="#FFFFFF"><input name="PEOPLE_BIRTHDAY" type="text" class="short" id="PEOPLE_BIRTHDAY" maxlength="10" value="<%=JDate.showDate(DBMgr.getRecordValue(doctorProfileRec, "BIRTH_DATE"))%>"  readonly="readonly"/></td>
                                    </tr>
                                    <tr>
                                      <td bordercolor="#FFFFFF" class="data">ชื่อ - นามสกุล </td>
                                      <td bordercolor="#FFFFFF"><input name="DOCTOR_NAME" type="text" id="DOCTOR_NAME"  readonly="readonly" value="<%=DBMgr.getRecordValue(doctorRec, "NAME_THAI")%>"/></td>
                                    </tr>
                                    <tr>
                                      <td bordercolor="#FFFFFF" class="data">ที่อยู่</td>
                                      <td bordercolor="#FFFFFF"><input name="ADDRESS1" type="text" id="ADDRESS1" size="30" readonly="readonly" value="<%=DBMgr.getRecordValue(doctorRec, "ADDRESS1")%>"/></td>
                                    </tr>
                                    <tr>
                                      <td bordercolor="#FFFFFF" class="data">&nbsp;</td>
                                      <td bordercolor="#FFFFFF"><input name="ADDRESS2" type="text" id="ADDRESS2" size="30" readonly="readonly" value="<%=DBMgr.getRecordValue(doctorRec, "ADDRESS2")%>"/></td>
                                    </tr>
                                    <tr>
                                      <td bordercolor="#FFFFFF" class="data">&nbsp;</td>
                                      <td bordercolor="#FFFFFF"><input name="ADDRESS3" type="text" id="ADDRESS3" size="30" readonly="readonly" value="<%=DBMgr.getRecordValue(doctorRec, "ADDRESS3")%>"/></td>
                                    </tr>
                                    <tr>
                                      <td bordercolor="#FFFFFF" class="data">รหัสไปรษณีย์</td>
                                      <td bordercolor="#FFFFFF"><input name="ZIP" type="text" id="ZIP" size="10" readonly="readonly" value="<%=DBMgr.getRecordValue(doctorRec, "ZIP")%>"/></td>
                                    </tr>
                                    <tr>
                                      <td bordercolor="#FFFFFF" class="data">โทรศัพท์ : ที่บ้าน </td>
                                      <td bordercolor="#FFFFFF"><input name="DOCTOR_TEL_HOME" type="text" id="DOCTOR_TEL_HOME" value="<%=DBMgr.getRecordValue(tax91Rec, "DOCTOR_TEL_HOME")%>"/></td>
                                    </tr>
                                    <tr>
                                      <td bordercolor="#FFFFFF" class="data">โทรศัพท์ : ที่ทำงาน</td>
                                      <td bordercolor="#FFFFFF"><input name="DOCTOR_TEL_OFFICE" type="text" id="DOCTOR_TEL_OFFICE"  value="<%=DBMgr.getRecordValue(tax91Rec, "DOCTOR_TEL_OFFICE")%>"/></td>
                                    </tr>
                                    <tr>
                                      <td bordercolor="#FFFFFF" class="data">สถานภาพ</td>
                                      <% String doctor_status=DBMgr.getRecordValue(tax91Rec, "DOCTOR_STATUS");
									  	//out.println("doctor_status="+doctor_status);
									  	String status1="", status2="", status3="", status4="";
									  	
									  if(doctor_status.equals("1"))  		{  		status1="checked";		  }
									  else if(doctor_status.equals("2"))	{  		status2="checked";		  }
									  else if(doctor_status.equals("3"))	{  		status3="checked";		  }
									  else if(doctor_status.equals("4"))	{  		status4="checked";		  }
									  %>
                                      <td bordercolor="#FFFFFF"><input type="radio" name="DOCTOR_STATUS" id="DOCTOR_STATUS" value="1" onClick="SelectMaritalStatus(1)" <%=status1%>/>
                                          <span class="form"> โสด &nbsp;&nbsp;&nbsp;
                                          <input type="radio" name="DOCTOR_STATUS" id="DOCTOR_STATUS"  value="2" onClick="SelectMaritalStatus(2)" <%=status2%>/>
                                            สมรส &nbsp;&nbsp;&nbsp;<br />
                                            <input type="radio" name="DOCTOR_STATUS" id="DOCTOR_STATUS"  value="3" onClick="SelectMaritalStatus(3)" <%=status3%>/>
                                            หม้าย &nbsp;&nbsp;&nbsp;
                                            <input type="radio" name="DOCTOR_STATUS"  id="DOCTOR_STATUS" value="4" onClick="SelectMaritalStatus(4)" <%=status4%>/>
                                            ตายระหว่างปีภาษี </span></td>
                                    </tr>
                                  </table></td>
                                  <td width="54%" valign="top"><table width="100%" height="100%" border="1" cellpadding="1" cellspacing="1">
                                    <tr>
                                      <td colspan="4" align="center" bordercolor="#FFFFFF" bgcolor="#FFFF99" class="dpDayHighlightTD"><strong>คู่สมรส</strong></td>
                                    </tr>
                                    <tr>
                                      <td colspan="2" bordercolor="#FFFFFF">เลขประจำตัวประชาชน</td>
                                      <td colspan="2" bordercolor="#FFFFFF"><input name="SPOUSE_NO" type="text" id="SPOUSE_NO" size="15" maxlength="13" value="<%=DBMgr.getRecordValue(tax91Rec, "SPOUSE_NO")%>"  onkeypress="IsNumericKeyPress();"/></td>
                                    </tr>
                                    <tr>
                                      <td colspan="2" bordercolor="#FFFFFF">เลขประจำตัวผู้เสียภาษีอากร</td>
                                      <td colspan="2" bordercolor="#FFFFFF"><input name="SPOUSE_TAX_ID" type="text" id="SPOUSE_TAX_ID" size="15" maxlength="10" value="<%=DBMgr.getRecordValue(tax91Rec, "SPOUSE_TAX_NO")%>"  onkeypress="IsNumericKeyPress();">
                                          <br />
                                          <span class="style3">(กรอกเฉพาะกรณีเป็นผู้ไม่มีเลขประจำตัวประชาชน)</span> </td>
                                    </tr>
                                    <tr>
                                      <td colspan="2" bordercolor="#FFFFFF">วันเดือนปีเกิด</td>
                                      <td colspan="2" bordercolor="#FFFFFF"><input name="SPOUSE_BIRTHDAY" type="text" class="short" id="SPOUSE_BIRTHDAY" maxlength="10" value="<%=JDate.showDate(DBMgr.getRecordValue(tax91Rec, "SPOUSE_BIRTHDAY"))%>"  />
                                      <input name="image2" type="image" class="image_button" onClick="displayDatePicker('SPOUSE_BIRTHDAY'); return false;" src="../../images/calendar_button.png" alt=""/></td>
                                    </tr>
                                    <tr>
                                      <td colspan="2" bordercolor="#FFFFFF">ชื่อ - นามสกุล </td>
                                      <td colspan="2" bordercolor="#FFFFFF"><input name="SPOUSE_NAME" type="text" id="SPOUSE_NAME" value="<%=DBMgr.getRecordValue(tax91Rec, "SPOUSE_NAME")%>"/></td>
                                    </tr>
                                    <%
                                          String st011="",st012="",st013="",st014="";
                                    	  String st021="",st022="",st023="";
                                    	  String st01=DBMgr.getRecordValue(tax91Rec, "SPOUSE_TYPE");
                                          String st02=DBMgr.getRecordValue(tax91Rec, "SPOUSE_TYPE1");
                                          if(st01.equals("1")) 
                                          { 
                                        	  st011="checked";
                                        	  if(st02.equals("1")) {st021="checked"; }
                                        	  else if(st02.equals("2")) {st022="checked";}
                                        	  else if(st02.equals("3")) {st023="checked";}
                                          }
                                          else if(st01.equals("2")) {st012="checked"; }
                                          else if(st01.equals("3")) {st013="checked"; }
                                          else if(st01.equals("4")) {st014="checked"; }
                                     %>
                                    <tr>
                                      <td colspan="2" bordercolor="#FFFFFF"><input name="SPOUSE_TYPE" type="radio" value="1" id="SPOUSE_TYPE" onClick="ChType(1)" <%=st011 %> />
                                        (1) มีเงินได้ แต่ </td>
                                      <td width="7%" bordercolor="#FFFFFF">&nbsp;</td>
                                      <td width="57%" bordercolor="#FFFFFF"><input name="SPOUSE_TYPE" type="radio" value="2"  id="SPOUSE_TYPE" onClick="ChType(2)" <%=st012 %> />
                                        (2) มีเงินได้รวมคำนวณภาษี </td>
                                    </tr>
                                    <tr>
                                      <td width="10%" bordercolor="#FFFFFF">&nbsp;</td>
                                      <td colspan="2" bordercolor="#FFFFFF"><input name="SPOUSE_TYPE1" type="radio" value="1"  id="SPOUSE_TYPE1" <%=st021 %>/>
                                        สมรสระหว่างปีภาษี</td>
                                      <td bordercolor="#FFFFFF"><input name="SPOUSE_TYPE" type="radio" value="3"  id="SPOUSE_TYPE" onClick="ChType(3)" <%=st013 %> />
                                        (3) มีเงินได้แยกยื่นแบบฯ </td>
                                    </tr>
                                    <tr>
                                      <td bordercolor="#FFFFFF">&nbsp;</td>
                                      <td colspan="2" bordercolor="#FFFFFF"><input name="SPOUSE_TYPE1" type="radio" value="2"  id="SPOUSE_TYPE1" <%=st022 %>/>
                                        หย่าระหว่างปีภาษี</td>
                                      <td bordercolor="#FFFFFF"><input name="SPOUSE_TYPE" type="radio" value="4"  id="SPOUSE_TYPE" onClick="ChType(4)" <%=st014 %> />
                                        (4) ไม่มีเงินได้ </td>
                                    </tr>
                                    <tr>
                                      <td bordercolor="#FFFFFF">&nbsp;</td>
                                      <td colspan="2" bordercolor="#FFFFFF"><input name="SPOUSE_TYPE1" type="radio" value="3"  id="SPOUSE_TYPE1" <%=st023 %>/>
                                        ตายระหว่างปีภาษี </td>
                                      <td bordercolor="#FFFFFF">&nbsp;</td>
                                    </tr>
                                    <tr>
                                      <td bordercolor="#FFFFFF">&nbsp;</td>
                                      <td colspan="2" bordercolor="#FFFFFF">&nbsp;</td>
                                      <td bordercolor="#FFFFFF">&nbsp;</td>
                                    </tr>
                                    <tr>
                                      <td colspan="4" bordercolor="#FFFFFF"><table width="100%" border="1" cellspacing="1" cellpadding="1">
                                          <tr>
                                            <td width="74%" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFCC"><strong>การแสดงเจตนาบริจาคภาษีที่ชำระให้พรรคการเมือง</strong></td>
                                            <td width="26%" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFCC"><strong>รหัสพรรคการเมือง</strong></td>
                                          </tr>
                                          <%
                                          String ph_011="",ph_012="",ph_021="",ph_022="";
                                          String ph_01=DBMgr.getRecordValue(tax91Rec, "PHALANX_01");
                                          String ph_02=DBMgr.getRecordValue(tax91Rec, "PHALANX_01");
                                          if(ph_01.equals("1")) {ph_011="checked"; }
                                          else if(ph_01.equals("2")){ph_012="checked"; }
                                          if(ph_02.equals("1")) {ph_021="checked"; }
                                          else if(ph_02.equals("2")){ph_022="checked"; }
                                          %>
                                          <tr>
                                            <td bordercolor="#FFFFFF"><input name="PHALANX_01" type="radio"  id="PHALANX_01" value="1" <%=ph_011 %>/>
                                              ไม่ประสงค์บริจาค
                                              <input name="PHALANX_01" type="radio"  id="PHALANX_01" value="2" <%=ph_012 %>/>
                                              ประสงค์บริจาคภาษี 100 บาทให้ </td>
                                            <td align="center" bordercolor="#FFFFFF"><input name="PHALANX_01_01" type="text"  id="PHALANX_01_01" size="5" maxlength="3" value="<%=DBMgr.getRecordValue(tax91Rec, "PHALANX_01_01")%>"   onkeypress="IsNumericKeyPress();"/></td>
                                          </tr>
                                          <tr>
                                            <td bordercolor="#FFFFFF"><input name="PHALANX_02" type="radio"  id="PHALANX_02" value="1" <%=ph_021 %>/>
                                              ไม่ประสงค์บริจาค
                                              <input name="PHALANX_02" type="radio"  id="PHALANX_02" value="2" <%=ph_022 %>/>
                                              ประสงค์บริจาคภาษี 100 บาทให้ </td>
                                            <td align="center" bordercolor="#FFFFFF"><input name="PHALANX_02_01" type="text"  id="PHALANX_02_01"size="5" maxlength="3" value="<%=DBMgr.getRecordValue(tax91Rec, "PHALANX_02_01")%>"   onkeypress="IsNumericKeyPress();"/></td>
                                          </tr>
                                      </table></td>
                                    </tr>
                                  </table></td>
                                </tr>
                                
                                <tr>
                                  <td>&nbsp;</td>
                                  <td>&nbsp;</td>
                                </tr>
                              </table></td>
                            </tr>
                        </table>                </td>
            </tr>
            <tr>
                <td width="50%" valign="top"  bgcolor="#FFFFFF">
                        <table width="100%" border="0" bgcolor="#FFFFFF">
                            <tr>
                                <th height="30" colspan="2" align="left" bgcolor="#99CCFF">ก.การคำนวณภาษี</th>
                            </tr>
                            
                            <tr>
                                <td width="78%">1.<strong>เงินเดือน</strong> ค่าจ้าง บำนาญ ฯลฯ <span class="style3">(รวมเงินได้ที่ได้รับการยกเว้นตาม ข.6) </span></td>
                                <td width="22%">
                              <input type="text" name="a01" id="a01" value="<%=DBMgr.getRecordValue(sumTax402Rec, "SUM_NORMAL_TAX_AMT")%>" class="short alignRight" onBlur="Js01()" readonly="readonly"/>                                </td>
                            </tr>
                            <tr>
                                <td width="78%">2.<strong>หัก</strong> เงินได้ที่ได้รับยกเว้น(ยกมาจาก ข.7)</td>
                              <td width="22%"><input type="text" name="a02" id="a02" value="<%=DBMgr.getRecordValue(tax91Rec, "A02")%>" class="short alignRight" readonly="readonly"/>                                </td>
                            </tr>
                            <tr>
                                <td width="78%">3.<strong>คงเหลือ (1.-2.)</strong></td>
                              <td width="22%"><input type="text" name="a03" id="a03" value="<%=DBMgr.getRecordValue(tax91Rec, "A03")%>" class="short alignRight" readonly="readonly"/>                                </td>
                            </tr>
                            <tr>
                                <td width="78%">4.<strong>หัก</strong> ค่าใช้จ่าย(ร้อยละ 40 ของ 3. แต่ไม่เกินที่กฎหมายกำหนด)</td>
                              <td width="22%"><input type="text" name="a04"  id="a04" value="<%=DBMgr.getRecordValue(tax91Rec, "A04")%>" class="short alignRight" readonly="readonly"/>                                </td>
                            </tr>
                            <tr>
                                <td width="78%">5.<strong>คงเหลือ(3.-4.)</strong></td>
                              <td width="22%"><input type="text" name="a05"  id="a05" value="<%=DBMgr.getRecordValue(tax91Rec, "A05")%>" class="short alignRight" readonly="readonly"/>                                </td>
                            </tr> 
                            <tr>
                                <td width="78%">6.<strong>หัก</strong> ค่าลดหย่อนฯ (ยกมาจาก ค 14.)</td>
                              <td width="22%"><input type="text" name="a06" id="a06" value="<%=showA06 %>" class="short alignRight" readonly="readonly"/>                                </td>
                            </tr> 
                            <tr>
                                <td width="78%">7.<strong>คงเหลือ(5.-6.)</strong></td>
                              <td width="22%"><input type="text" name="a07" id="a07" value="<%=DBMgr.getRecordValue(tax91Rec, "A07")%>" class="short alignRight" readonly="readonly"/>                                </td>
                            </tr>
                            <tr>
                                <td width="78%">
                                    8.<strong>หัก</strong> เงินบริจาคสนับสนุนทางการศึกษา</td>
                              <td width="22%"><input type="text" name="a08_01"  id="a08_01" value="" class="short alignRight" onBlur="Js08()"  onkeypress="IsNumericKeyPress();"/></td>
                            </tr>
                            <tr>
                              <td>(2 เท่าของจำนวนเงินที่ได้จ่ายไปจริง แต่ไม่เกินร้อยละ 10 ของ 7.) </td>
                              <td><input type="text" name="a08_02"  id="a08_02" value="<%=DBMgr.getRecordValue(tax91Rec, "A08")%>" class="short alignRight" readonly="readonly"/></td>
                            </tr>
                            <tr>
                                <td width="78%">
                                    9.<strong>คงเหลือ(7.-8.)                                </strong></td>
                              <td width="22%"><input name="a09" type="text" class="short alignRight" id="a09" value="<%=DBMgr.getRecordValue(tax91Rec, "A09")%>" readonly="readonly"/></td>
                            </tr>
                            <tr>
                                <td width="78%">
                                    10.<strong>หัก</strong> เงินบริจาค(ไม่เกินร้อยละ 10 ของ 9.)                                </td>
                                <td width="22%">
                              <input name="a10" type="text" class="short alignRight" id="a10" onBlur="Js11();" value="<%=DBMgr.getRecordValue(tax91Rec, "A10")%>"  onkeypress="IsNumericKeyPress();"/>                                </td>
                            </tr>
                            <tr>
                                <td width="78%">
                                    11.<strong>เงินได้สุทธิ(9.-10.)                                </strong></td>
                                <td width="22%">
                              <input name="a11" type="text" class="short alignRight" id="a11" value="<%=DBMgr.getRecordValue(tax91Rec, "A11")%>" readonly/>                                </td>
                            </tr>
                            <tr>
                                <td width="78%">
                                    12.<strong>ภาษีคำนวณจากเงินได้สุทธิตาม 11.</strong> </td>
                                <td width="22%">
                              <input name="a12" type="text" class="short alignRight" id="a12" value="<%=DBMgr.getRecordValue(tax91Rec, "A12")%>" readonly/>                                </td>
                            </tr>
                            <tr>
                                <td width="78%">
                                    13.<strong>หัก</strong> ภาษีเงินได้หัก ณ ที่จ่าย                                </td>
                                <td width="22%">
                              <input name="a13" type="text" class="short alignRight" id="a13" onBlur="Js14();" value="<%=DBMgr.getRecordValue(sumTax402Rec, "NET_TAX_MONTH")%>" readonly="readonly"/>                                </td>
                            </tr>
                            <tr>
                                <td width="78%">
								<%
								String get14=DBMgr.getRecordValue(tax91Rec, "A14_01");
								String show14_01="",show14_02="";
								if(get14.equals("1")) { show14_01="checked"; show14_02="";}
								else if(get14.equals("2")) {show14_01=""; show14_02="checked";}
								%>
                                    14.<strong>คงเหลือ</strong> ภาษีที่
                                        <input type="checkbox" name="a14_01" id="a14_01" value="1" disabled <%=show14_01%>>ชำระเพิ่มเติม
                                        <input type="checkbox" name="a14_02" id="a14_02" value="2" disabled <%=show14_02%>>
                              ชำระเกินไว้ &nbsp;</td>
                                <td width="22%">
                              <input name="a14" type="text" class="short alignRight" id="a14" value="<%=DBMgr.getRecordValue(tax91Rec, "A14_03")%>" readonly/>                                </td>
                            </tr>
                            <tr>
                              <td colspan="2">(หลักฐานแนบ 8., 10. และ 13. รวม <input name="a14_03" type="text" class="alignRight" id="a14_03" value="<%=DBMgr.getRecordValue(tax91Rec, "A14_02")%>" size="3" maxlength="3"   onkeypress="IsNumericKeyPress();"/>  ฉบับ) </td>
                            </tr>
							<%
							String show144=DBMgr.getRecordValue(tax91Rec, "A14_04");
							String show1441="", show1442="", show1443="";
							if(show144.equals("1")) { show1441="checked";}
							else if(show144.equals("2")) { show1442="checked";}
							else if(show144.equals("3")) { show1443="checked";}
							%>
                            <tr>
                              <td colspan="2"> <strong>กรณี</strong>&nbsp;<input name="a14_04" type="radio"  id="a14_04" value="1"  onclick="Ch14(1);" <%=show1441%>/>
                                <strong>มีใบแนบ</strong>&nbsp;<input name="a14_04" type="radio" value="2"   id="a14_04"  onclick="Ch14(2);" <%=show1442%>/>
                                <strong>ยื่นแบบฯ เพิ่มเติม</strong> &nbsp;<input name="a14_04" type="radio" value="3"   id="a14_04"  onclick="Ch14(3);" <%=show1443%>/>
                                <strong>ยื่นแบบฯ เกินกำหนดเวลา</strong> </td>
                          </tr>
                            <tr>
                                <td width="78%">
                                    15.<strong>บวก</strong> ภาษีที่ชำระเพิ่มเติม(ยกมาจาก ค.6. ของในแนบ (ถ้ามี))                                </td>
                                <td width="22%">
                              <input name="a15" type="text" class="short alignRight" id="a15" value="<%=DBMgr.getRecordValue(tax91Rec, "A15")%>" readonly/>                                </td>
                            </tr>
                            <tr>
                                <td width="78%">
                                    16.<strong>หัก</strong> ภาษีที่ชำระไว้เกิน(ยกมาจาก ค.7. ของในแนบ (ถ้ามี))                                </td>
                                <td width="22%">
                              <input name="a16" type="text" class="short alignRight" id="a16" value="<%=DBMgr.getRecordValue(tax91Rec, "A16")%>" readonly/>                                </td>
                            </tr>
                            <tr>
                                <td width="78%">
                                    17.<strong>หัก</strong> ภาษีที่ชำระไว้ตามแบบ ภ.ง.ด.91(กรณียื่นเพิ่มเติม)                                </td>
                                <td width="22%">
                              <input name="a17" type="text" class="short alignRight" id="a17" onBlur="Js18()" value="<%=DBMgr.getRecordValue(tax91Rec, "A17")%>"/>                                </td>
                            </tr>
							<%
								String get18=DBMgr.getRecordValue(tax91Rec, "A18_01");
								String show18_01="",show18_02="";
								if(get18.equals("1")) { show18_01="checked"; show18_02="";}
								else if(get18.equals("2")){show18_01=""; show18_02="checked";}
								%>
                            <tr>
                                <td width="78%">
                                    18.ภาษีที่ 
                                      <input type="checkbox" name="a18_01"  id="a18_01"  value="1" disabled  <%=show18_01%>>
                                      <strong>ชำระเพิ่มเติม</strong>
                                  <input type="checkbox" name="a18_02"   id="a18_02" value="2" disabled <%=show18_02%>>
                              <strong>ชำระไว้เกิน</strong> </td>
                                <td width="22%">
                              <input name="a18" type="text" class="short alignRight" id="a18" value="<%=DBMgr.getRecordValue(tax91Rec, "A18_02")%>" readonly/>                                </td>
                            </tr>
                            <tr>
                                <td width="78%">
                                    19.<strong>บวก</strong> เงินเพิ่ม(ถ้ามี)                                </td>
                                <td width="22%">
                              <input name="a19" type="text" class="short alignRight" id="a19" value="<%=DBMgr.getRecordValue(tax91Rec,"A19")%>" readonly/>                                </td>
                            </tr>
							<%
								String get20=DBMgr.getRecordValue(tax91Rec, "A20_01");
								String show20_01="",show20_02="";
								if(get20.equals("1")) { show20_01="checked"; show20_02="";}
								else if(get20.equals("2")) {show20_01=""; show20_02="checked";}
								%>
                            <tr>
                                <td width="78%">
                                    20.<strong>รวม</strong> ภาษีที่ 
                                    <input type="checkbox" name="a20_01"  id="a20_01"  value="1" disabled <%=show20_01%>>
                                    <strong>ชำระเพิ่มเติม</strong>
                                  <input type="checkbox" name="a20_02"   id="a20_02"  value="2" disabled <%=show20_02%>>
                              <strong>ชำระไว้เกิน</strong> </td>
                                <td width="22%">
                              <input name="a20" type="text" class="short alignRight" id="a20"  value="<%=DBMgr.getRecordValue(tax91Rec,"A20_02")%>" readonly/>                                </td>
                            </tr>
                        </table>                </td>
                <td width="50%" rowspan="2" valign="top"><table width="100%" border="0" bgcolor="#FFFFFF">
                  <tr>
                    <th height="30" colspan="2" align="left" bgcolor="#99CCFF">ค.รายการลดหย่อนและยกเว้นหลังจากหักค่าใช้จ่าย</th>
                  </tr>
                  <tr>
                    <td width="77%">1.ผู้มีเงินได้</td>
                    <td width="23%"><input name="c01" type="text" class="short alignRight" id="c01" value="<%=showC01 %>" readonly="readonly"/>                    </td>
                  </tr>
                  <tr>
                    <td>2.คู่สมรส(30,000 บาท กรณีมีเงินได้รวมคำนวณภาษีหรือไม่มีเงินได้)</td>
                    <td><input name="c02" type="text" class="short alignRight" id="c02" value="<%=DBMgr.getRecordValue(tax91Rec,"C02")%>" readonly="readonly"/>                    </td>
                  </tr>
                  <tr>
                    <td>3.บุตรคนละ 15,000 บาท
                      <input type="text" name="c03_01"  id="c03_01" value="<%=DBMgr.getRecordValue(tax91Rec,"C03_01")%>" class="alignRight" size="3" onBlur="JsC03_01()" onKeyPress="IsNumericKeyPress();"  onkeyup="JsC03_01();"/>
                      คน</td>
                    <td><input name="c03_02" type="text" class="short alignRight" id="c03_02" value="<%=DBMgr.getRecordValue(tax91Rec,"C03_02")%>" readonly="readonly"/>                    </td>
                  </tr>
                  <tr>
                    <td>&nbsp;&nbsp;บุตรคนละ 17,000 บาท
                      <input name="c03_03" type="text" class="alignRight" id="c03_03" onBlur="JsC03_03()" value="<%=DBMgr.getRecordValue(tax91Rec,"C03_03")%>" size="3" onKeyUp="JsC03_03();"  onkeypress="IsNumericKeyPress();"/>
                      คน</td>
                    <td><input name="c03_04" type="text" class="short alignRight" id="c03_04" value="<%=DBMgr.getRecordValue(tax91Rec,"C03_04")%>" readonly="readonly"/>                    </td>
                  </tr>
				  <%
				  double c04031=0,c04041=0,c04081=0,c04091=0;
				  String c0403		=DBMgr.getRecordValue(tax91Rec,"C04_01");
				  if(DBMgr.getRecordValue(tax91Rec,"C04_02") !="")
				  {
				  	c04031		=Double.parseDouble(DBMgr.getRecordValue(tax91Rec,"C04_02"));
				  }
				  if(DBMgr.getRecordValue(tax91Rec,"C04_04") !="")
				  {
					  c04041	=Double.parseDouble(DBMgr.getRecordValue(tax91Rec,"C04_04"));
				  }
				  if(DBMgr.getRecordValue(tax91Rec,"C04_06") !="")
				  {
					  c04081	=Double.parseDouble(DBMgr.getRecordValue(tax91Rec,"C04_06"));
				  }
				  if(DBMgr.getRecordValue(tax91Rec,"C04_08") !="")
				  {
					  c04091	=Double.parseDouble(DBMgr.getRecordValue(tax91Rec,"C04_08"));
				  }
				  String c0404		=DBMgr.getRecordValue(tax91Rec,"C04_03");
				  String c0408		=DBMgr.getRecordValue(tax91Rec,"C04_05");
				  String c0409		=DBMgr.getRecordValue(tax91Rec,"C04_07");
				  
				  
				  String c0401="", c0402="", c0406="", c0407="";
				  double c0405=0, c0410=0;
				  if(!c0403.equals("")) { c0401="checked";}
				  if(!c0404.equals("")) { c0402="checked";}
				  if(!c0408.equals("")) { c0406="checked";}
				  if(!c0409.equals("")) { c0407="checked";}
					c0405=c04031+c04041;
					c0410= c04081+c04091;
				  %>
                  <tr>
                    <td height="25"> 4.
                        <input type="checkbox" name="c04_01"   id="c04_01"  value="" onClick="JsC04_01();" <%=c0401%>/>
                      บิดา
                      <input type="checkbox" name="c04_02" id="c04_02" value="" onClick="JsC04_02();" <%=c0402%>/>
                      มารดา ผู้มีเงินได้(ให้กรอกเลขประจำตัวประชาชน) </td>
                    <td height="25">&nbsp;</td>
                  </tr>
                  <tr>
                    <td>&nbsp;&nbsp;&nbsp;
                        <input name="c04_03" type="text"   id="c04_03" class="short alignRight" value="<%=c0403%>" maxlength="13" readonly="readonly"   onkeypress="IsNumericKeyPress();"/>
                        <input name="c04_04" type="text"   id="c04_04" class="short alignRight" value="<%=c0404%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/>                    </td>
                    <td><input type="text" name="c04_05"   id="c04_05" value="<%=c0405%>" class="short alignRight" readonly="readonly"/></td>
                  </tr>
                  <tr>
                    <td height="25">&nbsp;&nbsp;
                        <input type="checkbox" name="c04_06" id="c04_06" value=""  onclick="JsC04_06();" <%=c0406%>/>
                      บิดา
                      <input type="checkbox" name="c04_07" id="c04_07" value=""  onclick="JsC04_07();" <%=c0407%>/>
                      มารดา คู่สมรส(ให้กรอกเลขประจำตัวประชาชน) </td>
                    <td height="25">&nbsp;</td>
                  </tr>
                  <tr>
                    <td>&nbsp;&nbsp;&nbsp;
                        <input name="c04_08" type="text"   id="c04_08" class="short alignRight" value="<%=c0408%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/>
                        <input name="c04_09" type="text"   id="c04_09" class="short alignRight" value="<%=c0409%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/>                    </td>
                    <td><input type="text" name="c04_10"   id="c04_10"  value="<%=c0410%>" class="short alignRight" readonly="readonly"/></td>
                  </tr>
                  <tr>
                    <td height="25">5. อุปการะเลี้ยงดูคนพิการหรือคนทุพพลภาพ (ยกมาจากแบบ ล.ย. 04) </td>
                    <td height="25"><input name="c05" type="text" class="short alignRight" id="c05" onBlur="JsC12();" value="<%=DBMgr.getRecordValue(tax91Rec,"C05")%>"  onkeypress="IsNumericKeyPress();"/></td>
                  </tr>
				  <%
				  String c0603=DBMgr.getRecordValue(tax91Rec,"C06_01");
				  String c0604=DBMgr.getRecordValue(tax91Rec,"C06_02");
				  String c0608=DBMgr.getRecordValue(tax91Rec,"C06_03");
				  String c0609=DBMgr.getRecordValue(tax91Rec,"C06_04");
				  String c0601="", c0602="", c0605="", c0606="";
				  if(!c0603.equals("")) { c0601="checked";}
				  if(!c0604.equals("")) { c0602="checked";}
				  if(!c0608.equals("")) { c0605="checked";}
				  if(!c0609.equals("")) { c0606="checked";}
					 
				  %>
                  <tr>
                    <td height="25"> 6.
                      <input name="c06_01" type="checkbox" id="c06_01" onClick="JsC05_01();" value="" <%=c0601%>/>บิดา
                      <input name="c06_02" type="checkbox" id="c06_02" onClick="JsC05_02();" value="" <%=c0602%>/>มารดา เบี้ยประกันสุขภาพบิดามารดาของผู้มีเงินได้ </td>
                    <td height="25">&nbsp;</td>
                  </tr>
                  <tr>
                    <td>&nbsp;&nbsp;&nbsp;
                        <input name="c06_03" type="text" class="short alignRight" id="c06_03" value="<%=c0603%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/>
                        <input name="c06_04" type="text" class="short alignRight" id="c06_04" value="<%=c0604%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/>                    </td>
                    <td rowspan="3"><input type="text" name="c06_10"   id="c06_10"  value="<%=DBMgr.getRecordValue(tax91Rec,"C06_05")%>" class="short alignRight" onBlur="JsC12();"  onkeypress="IsNumericKeyPress();"/></td>
                  </tr>
                  <tr>
                    <td height="25">&nbsp;&nbsp;
                        <input name="c06_06" type="checkbox" id="c06_06" onClick="JsC05_06();" value="" <%=c0605%>/>                        บิดา
                        <input name="c06_07" type="checkbox" id="c06_07" onClick="JsC05_07();" value="" <%=c0606%>/> 
                      มารดา เบี้ยประกันสุขภาพบิดามารดาของคู่สมรส </td>
                  </tr>
                  <tr>
                    <td>&nbsp;&nbsp;&nbsp;
                        <input name="c06_08" type="text" class="short alignRight" id="c06_08" value="<%=c0608%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/>
                        <input name="c06_09" type="text" class="short alignRight" id="c06_09" value="<%=c0609%>" maxlength="13" readonly="readonly"  onkeypress="IsNumericKeyPress();"/>                    </td>
                  </tr>
                  <tr>
                    <td>7.เบี้ยประกันชีวิต</td>
                    <td><input name="c07" type="text" class="short alignRight" id="c07" onBlur="JsC12();" value="<%=DBMgr.getRecordValue(tax91Rec,"C07")%>"  onkeypress="IsNumericKeyPress();"/>                    </td>
                  </tr>
                  <tr>
                    <td>8.เงินสะสมกองทุนสำรองเลี้ยงชีพ</td>
                    <td><input name="c08" type="text" class="short alignRight" id="c08" onBlur="JsC12();" value="<%=DBMgr.getRecordValue(tax91Rec,"C08")%>"  onkeypress="IsNumericKeyPress();"/>                    </td>
                  </tr>
                  <tr>
                    <td>9.ค่าซื้อหน่วยลงทุน ฯ สำรองเลี้ยงชีพ</td>
                    <td><input name="c09" type="text" class="short alignRight" id="c09" onBlur="JsC12();" value="<%=DBMgr.getRecordValue(tax91Rec,"C09")%>"  onkeypress="IsNumericKeyPress();"/>                    </td>
                  </tr>
                  <tr>
                    <td>10.ค่าซื้อหน่วยลงทุน ฯ หุ้นระยะยาว</td>
                    <td><input name="c10" type="text" class="short alignRight" id="c10" onBlur="JsC12();" value="<%=DBMgr.getRecordValue(tax91Rec,"C10")%>"  onkeypress="IsNumericKeyPress();"/>                    </td>
                  </tr>
                  <tr>
                    <td>11.ดอกเบี้ยประกันเงินกู้ยืม ฯ</td>
                    <td><input name="c11" type="text" class="short alignRight" id="c11" onBlur="JsC12();" value="<%=DBMgr.getRecordValue(tax91Rec,"C11")%>"  onkeypress="IsNumericKeyPress();"/>                    </td>
                  </tr>
                  <tr>
                    <td>12. ค่าซื้ออาคารฯ </td>
                    <td><input name="c12" type="text" class="short alignRight" id="c12" onBlur="JsC12();" value="<%=DBMgr.getRecordValue(tax91Rec,"C12")%>"  onkeypress="IsNumericKeyPress();"/></td>
                  </tr>
                  <tr>
                    <td>13.เงินสมทบกองทุนประกันสังคม</td>
                    <td><input name="c13" type="text" class="short alignRight" id="c13" onBlur="JsC12();" value="<%=DBMgr.getRecordValue(tax91Rec,"C13")%>"  onkeypress="IsNumericKeyPress();"/>                    </td>
                  </tr>
                  <tr>
                    <td>14.รวม(1. ถึง 13.)ยกไปกรอก ก.6</td>
                    <td><input name="c14" type="text" class="short alignRight" id="c14" value="<%=showC14 %>" readonly="readonly"/>                    </td>
                  </tr>
                  <tr>
                    <td colspan="2">(หลักฐานแนบ ข. 1 ถึง 6 และ ค. 4 ถึง 13 รวม
                      <input name="c14_02" type="text" class="alignRight" id="c14_02" value="<%=DBMgr.getRecordValue(tax91Rec,"C14_02")%>" size="3" maxlength="3"   onkeypress="IsNumericKeyPress();"/>
                      ฉบับ) </td>
                  </tr>
                </table></td>
            </tr>
            <tr>
              <td valign="top"  bgcolor="#FFFFFF"><table width="100%" border="0" bgcolor="#FFFFFF">
                <tr>
                  <th height="30" colspan="2" align="left" bgcolor="#99CCFF">ข.รายการเงินได้ที่ได้รับยกเว้น</th>
                </tr>

                <tr>
                  <td width="78%">1.เงินสะสม<strong>กองทุนสำรองเลี้ยงชีพ</strong> (ส่วนที่เิกิน 10,000 บาท ) </td>
                  <td width="22%"><input name="b01" type="text" class="short alignRight" id="b01" onBlur="JsB06()" value="<%=DBMgr.getRecordValue(tax91Rec,"B01")%>"   onkeypress="IsNumericKeyPress();"/>                  </td>
                </tr>
                <tr>
                  <td>2.เงินสะสม <strong>กบข.</strong></td>
                  <td><input name="b02" type="text" class="short alignRight" id="b02" onBlur="JsB06()" value="<%=DBMgr.getRecordValue(tax91Rec,"B02")%>"   onkeypress="IsNumericKeyPress();"/>                  </td>
                </tr>
                <tr>
                  <td>3.เงินสะสม<strong>กองทุนสงเคราะห์ครูโรงเรียนเอกชน</strong></td>
                  <td><input name="b03" type="text" class="short alignRight" id="b03" onBlur="JsB06()" value="<%=DBMgr.getRecordValue(tax91Rec,"B03")%>"   onkeypress="IsNumericKeyPress();"/>                  </td>
                </tr>
                <tr>
                  <td>4. <strong>ผู้มีเงินได้อายุตั้งแต่ 65 ปีขึ้นไป 190,000  บาท </strong></td>
                  <td><input name="b04" type="text" class="short alignRight" id="b04" onBlur="JsB06()" value="<%=DBMgr.getRecordValue(tax91Rec,"B04")%>"   onkeypress="IsNumericKeyPress();"/>                  </td>
                </tr>
                <tr>
                  <td>5. <strong>คู่สมรสอายุตั้งแต่ 65 ปีขึ้นไปและมีเงินได้รวมคำนวณ 190,000 บาท  </strong></td>
                  <td><input name="b05" type="text" class="short alignRight" id="b05" onBlur="JsB06()" value="<%=DBMgr.getRecordValue(tax91Rec,"B05")%>"  onkeypress="IsNumericKeyPress();" /></td>
                </tr>
                <tr>
                  <td>6.เงินค่าชดเชยที่ได้รับตามกฏหมายแรงงาน<br />
                    <span class="style3">(กรณีนำมารวมคำนวณภาษี)</span></td>
                  <td><input name="b06" type="text" class="short alignRight" id="b06" onBlur="JsB06()" value="<%=DBMgr.getRecordValue(tax91Rec,"B06")%>"  onkeypress="IsNumericKeyPress();" />                  </td>
                </tr>
                <tr>
                  <td>7.รวม(1. ถึง 6.) ยกไปกรอกใน ก 2. </td>
                  <td><input name="b07" type="text" class="short alignRight" id="b07" value="<%=DBMgr.getRecordValue(tax91Rec,"B07")%>" readonly="readonly"/>                  </td>
                </tr>
              </table></td>
            </tr>
            <tr>
                <td colspan="2" align="center" height="28" bgcolor="#FFFFFF">
                
                 	<input type="button" name="bt_save" id="bt_save" value="บันทึกข้อมูลการคำนวณภาษี" onClick="SAVE_Click()" <%= MODE == DBMgr.MODE_INSERT ? " disabled" : ""%>/>
                
                    <input type="button" name="bt_print" id="bt_print" value="พิมพ์ผลการคำนวณภาษี" onClick="Report_View();" <%= MODE == DBMgr.MODE_INSERT ? " disabled" : ""%>/>
                    <input type="reset" name="bt_reset" id="bt_reset" value="ล้างข้อความ"/>
                    <input type="button" name="bt_close" id="bt_close" value="ออกจากโปรแกรม" onClick="window.location='../process/ProcessFlow.jsp'"/>                </td>
            </tr>
        </table>

        </form>
	</body>
</html>