<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.*" %>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.table.Batch"%>

<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_RECEIPT)) {
                response.sendRedirect("../message.jsp");
                return;
            }
			
			//
            // Initial LabelMap
            //
            
			DBConnection con;
			con = new DBConnection();
			con.connectToLocal();
			//con.connectToServer();
			
			DBConn conData = new DBConn();
            conData.setStatement();
            String showButtonSave="";
            
			Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), con);
			//con.Close();
			//con.freeConnection();
			String batch_year = b.getYyyy();
			String batch_month = b.getMm();
			
			String sqlGurantee="SELECT DISTINCT YYYY, MM FROM SUMMARY_GUARANTEE "
			+" WHERE YYYY='"+batch_year+"' AND MM='"+batch_month+"' AND HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"'";
			System.out.println("sqlData= "+sqlGurantee);
			String [][]arrBatch = conData.query(sqlGurantee);
			if(arrBatch.length != 0)
			{
				showButtonSave="disabled";
			}
			
			
            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            String dt = JDate.showDate(JDate.getDate());
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Edit Old Transaction", "จัดการข้อมูลใบแจ้งหนี้/ใบเสร็จ");
            labelMap.add("INVOICE_NO", "Invoice No", "Invoice No");
            labelMap.add("INVOICE_DATE", "Invoice Date", "Invoice Date");
			labelMap.add("RECEIPT_DATE", "Receipt Date", "วันที่รับชำระ");
            labelMap.add("PAYOR_OFFICE_CODE", "Payor Office", "Payor Office");
            labelMap.add("DOCTOR_CODE", "Doctor Code", "Doctor Code");
            labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "Order Item Code");
            labelMap.add("HN_NO", "HN No", "HN No");
            labelMap.add("Status", "Status", "สถานะ");
            labelMap.add("LINE_NO", "Line No", "Line No");
            labelMap.add("AMOUNT_OF_DISCOUNT", "Amount of Discount", "Amount of Discount");
            labelMap.add("NOTE", "Note", "Note");
            labelMap.add("ROLLBACK", "Rollback", "Rollback");
            
            labelMap.add("PATIENT_NAME", "Patient Name", "ชื่อผู้ป่วย");
            labelMap.add("DETAIL", "Detail", "Detail");
            labelMap.add("M_RECEIPT", "Receipt", "รับชำระ");
            labelMap.add("TITLE_DATA", "Data List", "รายการข้อมูล");
            
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            DataRecord payorOfficeRec = null, doctorRec = null, hnNoRec = null, orderRec = null;
            String query = "";
            //int i=0;
            int table_row=0;
            if (request.getParameter("PAYOR_OFFICE_CODE") != null && !request.getParameter("PAYOR_OFFICE_CODE").equalsIgnoreCase("")) {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM PAYOR_OFFICE WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + request.getParameter("PAYOR_OFFICE_CODE") + "'";
                payorOfficeRec = DBMgr.getRecord(query);
            }
            if (request.getParameter("DOCTOR_CODE") != null) {
                query = "SELECT CODE, NAME_" + labelMap.getFieldLangSuffix() + " AS NAME FROM DOCTOR WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + request.getParameter("DOCTOR_CODE") + "'";
                doctorRec = DBMgr.getRecord(query);
            }
            if (request.getParameter("HN_NO") != null && !request.getParameter("HN_NO").equalsIgnoreCase("")) {
                query = "SELECT HN_NO, PATIENT_NAME FROM TRN_DAILY WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND HN_NO = '" + request.getParameter("HN_NO") + "'";
                hnNoRec = DBMgr.getRecord(query);
            }
            if (request.getParameter("ORDER_ITEM_CODE") != null && !request.getParameter("ORDER_ITEM_CODE").equalsIgnoreCase("")) {
                query = "SELECT CODE, DESCRIPTION_"+labelMap.getFieldLangSuffix()+" AS NAME FROM ORDER_ITEM WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND CODE = '" + request.getParameter("ORDER_ITEM_CODE") + "'";
                orderRec = DBMgr.getRecord(query);
            }
            else
            {
            	System.out.println("order_item_code="+request.getParameter("ORDER_ITEM_CODE"));
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
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript">
            //-------All------สำหรับ popup หน้าที่กำหนด----------------------------------------------------------
			function SetNewSize(new_location,height,width)
			{
				var resolution_height = screen.availHeight; 
				var resolution_width  = screen.availWidth; 
				var win_height = height;
				var win_width =  width;
				var xpos  = ( resolution_width  - win_width  ) / 2;
				var ypos  = ( resolution_height - win_height ) / 2;
			
				//new_location = "organize.php?release="+$release;
				//กำหนดขนาดของหน้าจอ login และให้อยู่ตรงกลาง
				windows_parameter = 'resizable=no,scrollbars=yes,width='+win_width+', height='+win_height+',left='+xpos.toFixed(0)+',top='+ypos.toFixed(0);
				windows = window.open(new_location,'', windows_parameter);
			}
            function INVOICE_NO_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.INVOICE_NO.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_INVOICE() {
                var target = "../../RetrieveData?TABLE=TRN_DAILY&COND=INVOICE_NO='" + document.mainForm.INVOICE_NO.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_INVOICE);
            }
            
            function AJAX_Handle_Refresh_INVOICE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "INVOICE_NO")) {
                        document.mainForm.INVOICE_NO.value = "";
                        return;
                    }
                    
                    // Data found
                }
            }
            
            function PAYOR_OFFICE_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.PAYOR_OFFICE_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_PAYOR_OFFICE() {
                var target = "../../RetrieveData?TABLE=PAYOR_OFFICE&COND=CODE='" + document.mainForm.PAYOR_OFFICE_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_PAYOR_OFFICE);
            }
            
            function AJAX_Handle_Refresh_PAYOR_OFFICE() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.PAYOR_OFFICE_CODE.value = "";
                        document.mainForm.PAYOR_OFFICE_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.PAYOR_OFFICE_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
            
            function DOCTOR_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.DOCTOR_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR() {
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
            }
            
            function AJAX_Handle_Refresh_DOCTOR() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.DOCTOR_CODE.value = "";
                        document.mainForm.DOCTOR_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_NAME.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }
            function AJAX_Refresh_DOCTOR_ARR() {
            	//alert("ok");
                //var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.dataForm.DOCTOR_CODE_ARR[id].value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                //AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR(id));
            }
            
            
            function HN_NO_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.HN_NO.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_HN_NO() {
                var target = "../../RetrieveData?TABLE=TRN_DAILY&COND=HN_NO='" + document.mainForm.HN_NO.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_HN_NO);
            }
            
            function AJAX_Handle_Refresh_HN_NO() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "HN_NO")) {
                        document.mainForm.HN_NO.value = "";
                        document.mainForm.PATIENT_NAME.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.PATIENT_NAME.value = getXMLNodeValue(xmlDoc, "PATIENT_NAME");
                }
            }
//ORDER ITEM CODE
             function ORDER_ITEM_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.ORDER_ITEM_CODE.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_ORDER_ITEM() {
                var target = "../../RetrieveData?TABLE=ORDER_ITEM&COND=CODE='" + document.mainForm.ORDER_ITEM_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_ORDER_ITEM);
            }
            
            function AJAX_Handle_ORDER_ITEM() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        // Data not found
                        document.mainForm.ORDER_ITEM_CODE.value = "";
                        document.mainForm.ORDER_ITEM_NAME.value = "";
                    }
                    else {
                        // Data found
                        document.mainForm.ORDER_ITEM_NAME.value = getXMLNodeValue(xmlDoc, "DESCRIPTION_<%=labelMap.getFieldLangSuffix()%>");
                    }
                }
            }
               function updateAllCheckBox() 
            { 
            	var dis_check = document.dataForm.elements['DIS[]'];
            	//alert("dis_check.length="+dis_check.length);
            	if(dataForm.allCheckBox.checked==true)
            	{
            		if(dis_check.length==undefined)
            		{
            			dis_check.checked=true;
            		}
            		else
            		{
		            	for (var i = 0; i < dis_check.length; i++) 
		            	{ 
		                   dis_check[i].checked=true;
		                }
		            }
	             }
	             else
	             {
	             	if(dis_check.length==undefined)
            		{
            			dis_check.checked=false;
            		}
            		else
            		{
		             	for (var i = 0; i < dis_check.length; i++) 
		            	{ 
		                   dis_check[i].checked=false;
		                }
		            }
	             }
                /*
                for (var i = 0; i < document.mainForm.elements.length; i++) { 
                    var x = document.mainForm.elements[i]; 
                    //alert("x="+x);
                    if (x.type == 'checkbox') { 
                        x.checked = document.mainForm.allCheckBox.checked; 
                    } 
                } */
            } 
            /*function updateAllCheckBox() 
            { 
                for (var i = 0; i < document.dataForm.elements.length; i++) { 
                    var x = document.dataForm.elements[i]; 
                    if (x.type == 'checkbox') { 
                        x.checked = document.dataForm.allCheckBox.checked; 
                    } 
                } 
            } */
			
			function save_data()
			{
				//alert("ok save_data");
				//alert("dataForm.ChProcess_inactive.checked="+dataForm.ChProcess_inactive.checked);
				//alert("dataForm.ChProcess_discount.checked="+dataForm.ChProcess_discount.checked);
				//alert("dataForm.ChProcess_change.checked="+dataForm.ChProcess_change.checked);
				if(dataForm.ChProcess_inactive.checked==false && dataForm.ChProcess_discount.checked==false && dataForm.ChProcess_change.checked==false)
				{
					alert("Please, Select Process");
					return false;
				}
				//else if(dataForm.ChProcess_inactive.checked==true)
				//{
					
				//}
				if(dataForm.ChProcess_discount.checked==true)
				{
					//alert("ChProcess_discount.checked==true");
					if(dataForm.RdType[0].checked==false && dataForm.RdType[1].checked==false)
					{
						alert("Please, Select Type for Discount");
						return false;
					}
					else if(dataForm.RdType[1].checked==true)
					{
						if(dataForm.TxtTypePercent.value=="")
						{
							alert("Please, fill Percent %");
							return false;
						}
						else 
						{
							var total_percent=parseFloat(dataForm.TxtTypePercent.value);
							if(total_percent<0 || total_percent>100)
							{
								alert("Percent % incorrect");
								return false;
							}
						}
					}
					
				}
				//else if(mainForm.ChProcess_change.checked==true)
				//{
				//}
				var num=chCheckBox();
				//alert("num="+num);
				if(num)
				{
					//chCheckBoxData();
					//alert("box="+chCheckBoxData());
					//alert("data="+chDataCheckBox());
					
					if(chCheckBoxData()&& chDataCheckBox())
					{
						if(dataForm.RdType[0].checked==true)
						{
							if(chCheckValueData())
							{
								//dataForm.INVOICE_NUMBER.value=mainForm.INVOICE_NO.value;
								document.dataForm.submit();
							}
						}
						else
						{
							//alert("Sava OK");
							//alert(dataForm.INVOICE_NUMBER.value);
							//alert(mainForm.INVOICE_NO.value);
							//dataForm.INVOICE_NUMBER.value=mainForm.INVOICE_NO.value;
							document.dataForm.submit();
						}
					}
					
				}
				else
				{
					alert("Please, Click Checkbox");
					return false;
				}
              ///////////document.dataForm.submit();
				
			}
			// check ว่ามีการ click ที่ Checkbox หรือไม่
            function chCheckBox()
            {
            	var n=0;
            	var dis=document.dataForm.elements['DIS[]'];
            	//alert("dis="+dis.length);
            	//alert("value="+dis.value);
            	//alert("checked="+dis.checked);
            	//var line=document.dataForm.elements['LINE_NO_ARR[]'];
            	if(!(dataForm.ChProcess_discount.checked==true  && dataForm.RdType[1].checked==true))
            	{
            		if(dis.length==undefined)
            		{
            			//alert("ok");
            			if(dis.checked==true)
            			{
            				n++;
            			}
            		}
            		else
            		{
		            	for (var i = 0; i < dis.length; i++) 
		            	{ 
		            		//var name_dis=line[i];
		                    if(dis[i].checked==true)
		                   	{
		                   		n++;
		                   	}
		                   	else
		                   	{
		                   		//don't use
		                   	} 
		                } 
		            }
		            //alert("n="+n);
	                if(n>0)
	                {
	                	//alert("return true");
	                	return true;
	                }
	                else
	                {
	                	//alert("return false");
	                	return false;
	                }
	            }
	            else
	            {
	            	return true;
	            }
            }
			//check ว่า ค่าที่ใส่นั้นมีการ checkbox หรือไม่
            function chDataCheckBox()
            {
            	//alert("chDataCheckBox");
            	var dis_check = document.dataForm.elements['DIS[]'];
            	var line = document.dataForm.elements['LINE_NO_ARR[]'];
            	var dis_doctor;
                var dis_amount;
                var num_check=0;
                var num_peramount=0;
                var status_check;
                //if(dataForm.ChProcess_inactive.checked==true)
               	//{
	               	//if(!chCheckBox())
	               	//{
	               		//return false;
	               	//}
               //	}
               //else 
               if(dataForm.ChProcess_discount.checked==true && dataForm.ChProcess_change.checked==true  && dataForm.RdType[0].checked==true)
               	{
	               	
	                dis_amount = document.dataForm.elements['AMOUNT_OF_DISCOUNT_ARR[]'];
	                
	                for (var i = 0; i < line.length; i++) 
            		{ 
            			dis_doctor = document.dataForm.elements['DOCTOR_CODE_ARR['+i+']'];
	                    if((dis_doctor.value !="" || dis_amount[i].value !="") && dis_check[i].checked==false)
                    	{
                    		alert("Please, click Checkbox");
                    		dis_check[i].focus();
                    		return false;
                    	}
                	} 
	                
               	}
               	else if(dataForm.ChProcess_discount.checked==true && dataForm.RdType[0].checked==true)
               	{
               		//alert("dddddddd");
               		var dis_amount = document.dataForm.elements['AMOUNT_OF_DISCOUNT_ARR[]'];
                	
                	for (var i = 0; i < line.length; i++) 
            		{ 
	                    if(dis_amount[i].value !="" && dis_check[i].checked==false)
                    	{
                    		alert("Please, click Checkbox");
                    		dis_check[i].focus();
                    		return false;
                    	}
                	} 
               	}
               	else if(dataForm.ChProcess_change.checked==true)
               	{
               		//alert("line.length="+line.length);
               		if(line == undefined)
               		{
               			dis_doctor = document.dataForm.elements['DOCTOR_CODE_ARR[0]'];
	                    if(dis_doctor.value !="" && dis_check[i].checked==false)
                    	{
                    		alert("Please, click Checkbox");
                    		dis_check[0].focus();
                    		return false;
                    	}
               		}
               		for (var i = 0; i < line.length; i++) 
            		{ 
            			dis_doctor = document.dataForm.elements['DOCTOR_CODE_ARR['+i+']'];
	                    if(dis_doctor.value !="" && dis_check[i].checked==false)
                    	{
                    		alert("Please, click Checkbox");
                    		dis_check[i].focus();
                    		return false;
                    	}
                	} 
	                
               	}
            	return true;
               
            }
            //check ว่า checkbox ที่เลือกนั้นใส่ข้อมูลแล้วหรือไม่
            function chCheckBoxData()
            {
            	//alert("chCheckBoxData");
            	var dis_check = document.dataForm.elements['DIS[]'];
            	//var dis_doctor = document.dataForm.elements['DOCTOR_CODE_ARR[]'];
	            var dis_amount = document.dataForm.elements['AMOUNT_OF_DISCOUNT_ARR[]'];
	            var num_check=0;
                var num_peramount=0;
                //alert("dis_doctor="+dis_doctor);
                if(dis_check.length==undefined)
                {
                		var dis_doctor = document.dataForm.elements['DOCTOR_CODE_ARR[0]'];
                		//alert("dis_doctor="+dis_doctor.value);
	                    if(dis_check.checked==true)
	                    {
	                    	//alert("ok1");
	                    	num_check++;
	                    	if(dataForm.ChProcess_discount.checked==true && dataForm.ChProcess_change.checked==true && (dis_doctor.value=="" && dis_amount.value=="") && dataForm.RdType[0].checked==true)
	                    	{
	                    		alert("Please, fill Amount Discount or Select Doctor Code");
	                    		return false;
	                    	}
	                    	else if(dataForm.ChProcess_discount.checked==true && dataForm.ChProcess_change.checked==false && dis_amount.value==""  && dataForm.RdType[0].checked==true)
	                    	{
	                    		alert("Please, fill Amount Discount");
	                    		dis_amount.focus();
	                    		return false;
	                    	}
	                    	else if(dataForm.ChProcess_discount.checked==false && dataForm.ChProcess_change.checked==true && dis_doctor.value=="")
	                    	{
	                    		alert("Please, select Doctor Code");
	                    		//dis_doctor.focus();
	                    		return false;
	                    	}
	                    	else
	                    	{
	                    		//alert("ok2");
	                    		num_peramount++;
	                    	}
	                    }
	                    
                }
                else
                {
	            	for (var i = 0; i < dis_check.length; i++) 
	            	{ 
	                    var dis_doctor = document.dataForm.elements['DOCTOR_CODE_ARR['+i+']'];
	                    if(dis_check[i].checked==true)
	                    {
	                    	num_check++;
	                    	if(dataForm.ChProcess_discount.checked==true && dataForm.ChProcess_change.checked==true && (dis_doctor.value=="" && dis_amount[i].value=="") && dataForm.RdType[0].checked==true)
	                    	{
	                    		alert("Please, fill Amount Discount or Select Doctor Code");
	                    		return false;
	                    	}
	                    	else if(dataForm.ChProcess_discount.checked==true && dataForm.ChProcess_change.checked==false && dis_amount[i].value==""  && dataForm.RdType[0].checked==true)
	                    	{
	                    		alert("Please, fill Amount Discount");
	                    		dis_amount[i].focus();
	                    		return false;
	                    	}
	                    	else if(dataForm.ChProcess_discount.checked==false && dataForm.ChProcess_change.checked==true && dis_doctor.value=="")
	                    	{
	                    		alert("Please, select Doctor Code");
	                    		dis_doctor[i].focus();
	                    		return false;
	                    	}
	                    	else
	                    	{
	                    		num_peramount++;
	                    	}
	                    }
	                    
	                } 
	            }
                //alert("num_check="+num_check);
                //alert("num_peramount="+num_peramount);
                if(num_check==num_peramount)
                {
                	return true;
                }
                
            }
            function chCheckValueData()
            {
            	//alert("chCheckValueData");
            	var dis_check = document.dataForm.elements['DIS[]'];
            	//var dis_doctor = document.dataForm.elements['DOCTOR_CODE_ARR[]'];
            	var bef_amount =document.dataForm.elements['AMT_BEF_DIS[]'];
	            var dis_amount = document.dataForm.elements['AMOUNT_OF_DISCOUNT_ARR[]'];
	            var num_check=0;
                var num_peramount=0;
                if(dis_check.length==undefined)
                {
                	var bef_amount_value=bef_amount.value;
                    var dis_amount_value=dis_amount.value;
                    var dis_doctor = document.dataForm.elements['DOCTOR_CODE_ARR[]'];
                    
                    if(dis_check.checked==true)
                    {
                    	num_check++;
                    	if(parseFloat(bef_amount.value) ==0)
                    	{
                    		alert("Amount Before Discount isn't value");
                    		return false;
                    	}
                    	else if((parseFloat(bef_amount.value) < parseFloat(dis_amount.value)) && (parseFloat(bef_amount.value) != parseFloat(dis_amount.value)))
                    	{
                    		alert("Please, fill Amount of Discount < Amount Before Discount");
                    		return false;
                    	}
                    	else
                    	{
                    		num_peramount++;
                    	}
                    }
                }
                else
                {
	            	for (var i = 0; i < dis_check.length; i++) 
	            	{ 
	                    var bef_amount_value=bef_amount[i].value;
	                    var dis_amount_value=dis_amount[i].value;
	                    var dis_doctor = document.dataForm.elements['DOCTOR_CODE_ARR['+i+']'];
	                    
	                    if(dis_check[i].checked==true)
	                    {
	                    	num_check++;
	                    	if(parseFloat(bef_amount[i].value) ==0)
	                    	{
	                    		alert("Amount Before Discount isn't value");
	                    		return false;
	                    	}
	                    	else if((parseFloat(bef_amount[i].value) < parseFloat(dis_amount[i].value)) && (parseFloat(bef_amount[i].value) != parseFloat(dis_amount[i].value)))
	                    	{
	                    		alert("Please, fill Amount of Discount < Amount Before Discount");
	                    		return false;
	                    	}
	                    	else
	                    	{
	                    		//alert("ssssssss");
	                    		num_peramount++;
	                    	}
	                    }
	                    
	                } 
	            }
                if(num_check==num_peramount)
                {
                	return true;
                }
                
            }
            function load_data()
            {
            	//alert("load_data="+<%=table_row%>);
            	//alert("load_num="+num);
            	dataForm.TxtTypePercent.disabled=true;
            	dataForm.TxtareaNoteAll.disabled=true;
            	dataForm.RdType[0].disabled=true;
            	dataForm.RdType[1].disabled=true;
            	dataForm.RdType[0].checked=false;
            	dataForm.RdType[1].checked=false;
            	dataForm.allCheckBox.disabled=true;
            	
            	var dis_check = document.dataForm.elements['DIS[]'];
            	if(dis_check==undefined){//if(dis_check.length==undefined){
            		//alert("ok");
            		document.dataForm.ChProcess_inactive.disabled=true;
            		document.dataForm.ChProcess_discount.disabled=true;
            		document.dataForm.ChProcess_change.disabled=true;
            		/*
            		dis_check.disabled=true;
            		var dis_button = document.dataForm.elements['SEARCH_DOCTOR_CODE_ARR[]'];
	            	dis_button.disabled=true;
			        var dis_text = document.dataForm.elements['AMOUNT_OF_DISCOUNT_ARR[]'];
	            	dis_text.disabled=true;
			        var dis_note = document.dataForm.elements['NOTE_ARR[]'];
	            	dis_note.disabled=true;
	            	*/
	            }else{
	            	//alert("dis_check.length="+dis_check.length);
	            	for (var i = 0; i < dis_check.length; i++) 
	            	{ 
	                   dis_check[i].disabled=true;
	                }
	            	//button browse doctor code of transaction detail
		            var dis_button = document.dataForm.elements['SEARCH_DOCTOR_CODE_ARR[]'];
	            	//alert("ds_button.length="+dis_button.length);
			        for (var i = 0; i < dis_button.length; i++) 
			        { 
			            dis_button[i].disabled=true;
			        }
		            var dis_text = document.dataForm.elements['AMOUNT_OF_DISCOUNT_ARR[]'];
	            	//alert("dis_text.length="+dis_text.length);
			        for (var i = 0; i < dis_text.length; i++) 
			        { 
			            dis_text[i].disabled=true;
			        }
			        
	                var dis_note = document.dataForm.elements['NOTE_ARR[]'];
	            	//alert("dis_note.length="+dis_note.length);
	            	for (var i = 0; i < dis_note.length; i++) 
	            	{ 
	                   dis_note[i].disabled=true;
	                }
	           }
            }
            function Click_Process(id)
            {
            	//alert("id="+id);
            	if(id==1)//inactive
            	{
            		if(dataForm.ChProcess_inactive.checked==true)
            		{
	            		dataForm.ChProcess_discount.disabled=true;
	            		dataForm.ChProcess_change.disabled=true;
	            		dataForm.RdType[0].disabled=true;
            			dataForm.RdType[1].disabled=true;
            			dataForm.RdType[0].checked=false;
            			dataForm.RdType[1].checked=false;
	            		dataForm.TxtTypePercent.disabled=true;
	            		//button browse doctor code of transaction detail
	            		dataForm.allCheckBox.disabled=false;
	            		var dis_button = document.dataForm.elements['SEARCH_DOCTOR_CODE_ARR[]'];
	            		var dis_text = document.dataForm.elements['AMOUNT_OF_DISCOUNT_ARR[]'];
	            		var dis_check = document.dataForm.elements['DIS[]'];
	            		var dis_note = document.dataForm.elements['NOTE_ARR[]'];
	            		
	            		if(dis_button.length==undefined)
	            		{
	            			//alert("OK");
	            			dis_button.disabled=true;
	            			dis_text.disabled=true;
	            			dis_check.disabled=false;
	            			dis_note.disabled=false;
	            		}
	            		else
	            		{
	            			//alert("dis_button.length="+dis_button.length);
			            	for (var i = 0; i < dis_button.length; i++) 
			            	{ 
			                   dis_button[i].disabled=true;
			                }
		            		var dis_text = document.dataForm.elements['AMOUNT_OF_DISCOUNT_ARR[]'];
	            			//alert("dis_text.length="+dis_text.length);
			            	for (var i = 0; i < dis_text.length; i++) 
			            	{ 
			                   dis_text[i].disabled=true;
			                }
			                var dis_check = document.dataForm.elements['DIS[]'];
	            			//alert("dis_check.length="+dis_check.length);
	            			for (var i = 0; i < dis_check.length; i++) 
	            			{ 
	                   			dis_check[i].disabled=false;
	                		}
			                var dis_note = document.dataForm.elements['NOTE_ARR[]'];
	            			//alert("dis_note.length="+dis_note.length);
			            	for (var i = 0; i < dis_note.length; i++) 
			            	{ 
			                   dis_note[i].disabled=false;
			                }
			             }
	            	}
	            	else
	            	{
	            		dataForm.ChProcess_discount.disabled=false;
		            	dataForm.ChProcess_change.disabled=false;
		            	dataForm.allCheckBox.disabled=true;
		            	var dis_check = document.dataForm.elements['DIS[]'];
		            	var dis_note = document.dataForm.elements['NOTE_ARR[]'];
	            		if(dis_button.length==undefined)
	            		{
	            			dis_check.disabled=true;
	            			dis_note.disabled=true;
	            		}
	            		else
	            		{
		            		//alert("dis_check.length="+dis_check.length);
	            			for (var i = 0; i < dis_check.length; i++) 
	            			{ 
	                   			dis_check[i].disabled=true;
	                		}
		            		//alert("dis_note.length="+dis_note.length);
			            	for (var i = 0; i < dis_note.length; i++) 
			            	{ 
			                   dis_note[i].disabled=true;
			                }
			            }
	            	}
            	}
            	else if(id==2)//discount,change
            	{
            		var dis_button = document.dataForm.elements['SEARCH_DOCTOR_CODE_ARR[]'];
	            	var dis_check = document.dataForm.elements['DIS[]'];
	            	var dis_note = document.dataForm.elements['NOTE_ARR[]'];
	            	
            		if(dataForm.ChProcess_discount.checked==true && dataForm.ChProcess_change.checked==true)
            		{
            			dataForm.ChProcess_inactive.disabled=true;
            			dataForm.ChProcess_change.disabled=false;
            			dataForm.RdType[0].disabled=false;
            			dataForm.RdType[1].disabled=false;
            			dataForm.RdType[0].checked=false;
            			dataForm.RdType[1].checked=false;
            			dataForm.TxtTypePercent.value="";
						//button browse doctor code of transaction detail
						dataForm.allCheckBox.disabled=false;
	            		
            			//alert("dis_button.length="+dis_button.length);
            			if(dis_button.length==undefined)
            			{
            				dis_button.disabled=false;
            				dis_check.disabled=false;
            				dis_note.disabled=false;
            			}
            			else
            			{
			            	for (var i = 0; i < dis_button.length; i++) 
			            	{ 
			                   dis_button[i].disabled=false;
			                }
			                //alert("dis_check.length="+dis_check.length);
	            			for (var i = 0; i < dis_check.length; i++) 
	            			{ 
	                   			dis_check[i].disabled=false;
	                		}
		            		//alert("dis_note.length="+dis_note.length);
			            	for (var i = 0; i < dis_note.length; i++) 
			            	{ 
			                   dis_note[i].disabled=false;
			                }
			            }
	            	}
	            	else if(dataForm.ChProcess_discount.checked==true && dataForm.ChProcess_change.checked==false)
            		{
            			dataForm.ChProcess_inactive.disabled=true;
            			if(dataForm.RdType[1].checked==true)
            			{
	            			dataForm.ChProcess_change.disabled=true;
	            			dataForm.ChProcess_change.checked=true;
	            		}
	            		else if(dataForm.RdType[0].checked==true)
	            		{
	            			dataForm.ChProcess_change.disabled=false;
	            			dataForm.ChProcess_change.checked=false;
	            		}
            			else
            			{
	            			dataForm.RdType[0].disabled=false;
	            			dataForm.RdType[1].disabled=false;
	            			dataForm.RdType[0].checked=false;
	            			dataForm.RdType[1].checked=false;
	            			dataForm.TxtTypePercent.value="";
	            		}
            			//button browse doctor code of transaction detail
            			dataForm.allCheckBox.disabled=true;
	            		//var dis_button = document.dataForm.elements['SEARCH_DOCTOR_CODE_ARR[]'];
	            		//var dis_check = document.dataForm.elements['DIS[]'];
	            		//var dis_note = document.dataForm.elements['NOTE_ARR[]'];
            			//alert("dis_button.length="+dis_button.length);
            			if(dis_button.length==undefined)
            			{
            				dis_button.disabled=true;
            				dis_check.disabled=true;
            				dis_note.disabled=true;
            			}
            			else
            			{
			            	for (var i = 0; i < dis_button.length; i++) 
			            	{ 
			                   dis_button[i].disabled=true;
			                }
			                //alert("dis_check.length="+dis_check.length);
	            			for (var i = 0; i < dis_check.length; i++) 
	            			{ 
	                   			dis_check[i].disabled=true;
	                		}
	                		//alert("dis_note.length="+dis_note.length);
			            	for (var i = 0; i < dis_note.length; i++) 
			            	{ 
			                   dis_note[i].disabled=true;
			                }
			             }
	            	}
            		else if(dataForm.ChProcess_discount.checked==false && dataForm.ChProcess_change.checked==true)
            		{
            			dataForm.ChProcess_inactive.disabled=true;
            			dataForm.RdType[0].disabled=true;
            			dataForm.RdType[1].disabled=true;
            			dataForm.RdType[0].checked=false;
            			dataForm.RdType[1].checked=false;
	            		dataForm.TxtTypePercent.disabled=true;
	            		dataForm.TxtTypePercent.value="";
	            		//button browse doctor code of transaction detail
	            		dataForm.allCheckBox.disabled=false;
	            		//var dis_button = document.dataForm.elements['SEARCH_DOCTOR_CODE_ARR[]'];
            			//alert("dis_button.length="+dis_button.length);
            			if(dis_button.length==undefined)
            			{
            				dis_button.disabled=false;
            				dis_check.disabled=false;
            				dis_note.disabled=false;
            			}
            			else
            			{
			            	for (var i = 0; i < dis_button.length; i++) 
			            	{ 
			                   dis_button[i].disabled=false;
			                }
			                //alert("dis_check.length="+dis_check.length);
	            			for (var i = 0; i < dis_check.length; i++) 
	            			{ 
	                   			dis_check[i].disabled=false;
	                		}
	                		//alert("dis_note.length="+dis_note.length);
			            	for (var i = 0; i < dis_note.length; i++) 
			            	{ 
			                   dis_note[i].disabled=false;
			                }
			            }
	            	}
            		else if(dataForm.ChProcess_discount.checked==false && dataForm.ChProcess_change.checked==false)
            		{
            			dataForm.ChProcess_inactive.disabled=false;
            			dataForm.ChProcess_change.disabled=false;
            			dataForm.RdType[0].disabled=true;
            			dataForm.RdType[1].disabled=true;
            			dataForm.RdType[0].checked=false;
            			dataForm.RdType[1].checked=false;
	            		dataForm.TxtTypePercent.disabled=true;
	            		dataForm.TxtTypePercent.value="";
	            		
	            		//button browse doctor code of transaction detail
	            		dataForm.allCheckBox.disabled=true;
	            		//var dis_button = document.dataForm.elements['SEARCH_DOCTOR_CODE_ARR[]'];
	            		//var dis_check = document.dataForm.elements['DIS[]'];
	            		//var dis_note = document.dataForm.elements['NOTE_ARR[]'];
	            				
            			//alert("dis_button.length="+dis_button.length);
		            	if(dis_button.length==undefined)
            			{
            				dis_button.disabled=true;
            				dis_check.disabled=true;
            				dis_note.disabled=true;
            			}
            			else
            			{
	            			for (var i = 0; i < dis_button.length; i++) 
			            	{ 
			                   dis_button[i].disabled=true;
			                }
			                //alert("dis_check.length="+dis_check.length);
	            			for (var i = 0; i < dis_check.length; i++) 
	            			{ 
	                   			dis_check[i].disabled=true;
	                		}
	                		//alert("dis_note.length="+dis_note.length);
			            	for (var i = 0; i < dis_note.length; i++) 
			            	{ 
			                   dis_note[i].disabled=true;
			                }
			            }
	            	}
            		var dis_text = document.dataForm.elements['AMOUNT_OF_DISCOUNT_ARR[]'];
            		//alert("dis_text.length="+dis_text.length);
            		if(dis_text.length==undefined)
            		{
            			dis_text.disabled=true;
            		}
            		else
            		{
			            for (var i = 0; i < dis_text.length; i++) 
			            { 
			               dis_text[i].disabled=true;
			            }
			        }
            		
            	}
            }
            function Click_Type(id)
            {
            	if(id==1)//amount
            	{
            		dataForm.TxtTypePercent.disabled=true;
            		dataForm.TxtTypePercent.value="";
            		dataForm.allCheckBox.disabled=false;
            		var dis_text = document.dataForm.elements['AMOUNT_OF_DISCOUNT_ARR[]'];
            		var dis_check = document.dataForm.elements['DIS[]'];
            		var dis_note = document.dataForm.elements['NOTE_ARR[]'];
            		//alert("dis_text.length="+dis_text.length);
            		if(dis_text.length==undefined)
            		{
            			dis_text.disabled=false;
            			dis_check.disabled=false;
            			dis_note.disabled=false;
            		}
            		else
            		{
			            for (var i = 0; i < dis_text.length; i++) 
			            { 
			               dis_text[i].disabled=false;
			            }
			            //alert("dis_check.length="+dis_check.length);
	            		for (var i = 0; i < dis_check.length; i++) 
	            		{ 
	                   		dis_check[i].disabled=false;
	                	}
	                	//alert("dis_note.length="+dis_note.length);
			            for (var i = 0; i < dis_note.length; i++) 
			            { 
			                dis_note[i].disabled=false;
			            }
			        }
		            if(dataForm.ChProcess_change.checked==true)
		            {
			            var dis_button = document.dataForm.elements['SEARCH_DOCTOR_CODE_ARR[]'];
	            		//alert("dis_button.length="+dis_button.length);
	            		if(dis_button.length==undefined)
	            		{
	            			dis_button.disabled=false;
	            		}
	            		else
	            		{
				           	for (var i = 0; i < dis_button.length; i++) 
				           	{ 
				                dis_button[i].disabled=false;
				            }
				        }
			        }
			        else
			        {
				        dataForm.ChProcess_change.disabled=false;
			            dataForm.ChProcess_change.checked=false;
			        }
            	}
            	else//%
            	{
            		if(mainForm.INVOICE_NO.value=="")
            		{
            			alert("Please, Select Invoice No.");
            		}
            		else
            		{
	            		dataForm.TxtTypePercent.disabled=false;
	            		dataForm.TxtTypePercent.focus();
	            		var dis_text = document.dataForm.elements['AMOUNT_OF_DISCOUNT_ARR[]'];
	            		var dis_check = document.dataForm.elements['DIS[]'];
            			//alert("dis_text.length="+dis_text.length);
            			if(dis_text.length==undefined)
            			{
            				dis_text.disabled=true;
            				dis_check.disabled=true;
            			}
            			else
            			{
			            	for (var i = 0; i < dis_text.length; i++) 
			            	{ 
			               		dis_text[i].disabled=true;
			            	}
			            	//alert("dis_check.length="+dis_check.length);
	            			for (var i = 0; i < dis_check.length; i++) 
	            			{ 
	                   			dis_check[i].disabled=true;
	                		}
	               		}
	            	}
            		dataForm.allCheckBox.disabled=true;
            		dataForm.ChProcess_change.disabled=true;
            		dataForm.ChProcess_change.checked=false;
            		var dis_note = document.dataForm.elements['NOTE_ARR[]'];
            		var dis_button = document.dataForm.elements['SEARCH_DOCTOR_CODE_ARR[]'];
            		//alert("dis_note.length="+dis_note.length);
            		if(dis_button.length==undefined)
            		{
            			dis_note.disabled=true;
            			dis_button.disabled=true;
            		}
            		else
            		{
			            for (var i = 0; i < dis_note.length; i++) 
			            { 
			               dis_note[i].disabled=true;
			            }
		            	//alert("dis_button.length="+dis_button.length);
		            	for (var i = 0; i < dis_button.length; i++) 
		            	{ 
		                   dis_button[i].disabled=true;
		                }
		            }
            	}
            }
            function Click_Note()
            {
            	if(dataForm.ChAllNote.checked==true)
            	{
            		dataForm.TxtareaNoteAll.disabled=false;
            		dataForm.TxtareaNoteAll.focus();
            	}
            	else
            	{
            		dataForm.TxtareaNoteAll.disabled=true;
            		dataForm.TxtareaNoteAll.value="";
            		
            	}
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
        </script>
	<style type="text/css">
		<!--
		.style1 {color: #003399}
		.style2 {color: #033}
		-->
	</style>
    </head>
    <body onload="load_data();">
        <form id="mainForm" name="mainForm" method="post" action="transaction_edit_dis.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%=DBMgr.MODE_INSERT%>" />
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                    <b><font color='#003399'><%=Utils.getInfoPage("transaction_edit_dis.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
				</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>
                <!-- 01-CI10003377 -->
                <tr>
                    <td class="label"><label for="INVOICE_NO">${labelMap.INVOICE_NO} *</label></td>
                    <td class="input">
                        <input type="text" id="INVOICE_NO" name="INVOICE_NO" class="short" maxlength="20" value="<%= request.getParameter("INVOICE_NO") != null ? request.getParameter("INVOICE_NO") : "" %>" onkeypress="return INVOICE_NO_KeyPress(event);" onblur="AJAX_Refresh_INVOICE();" />
                        <input id="SEARCH_INVOICE_NO" name="SEARCH_INVOICE_NO" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=TRN_DAILY&RETURN_FIELD=INVOICE_NO&DISPLAY_FIELD=INVOICE_DATE&BEINSIDEHOSPITAL=1&TARGET=INVOICE_NO&HANDLE=AJAX_Refresh_INVOICE'); return false;" />
                    </td>
                    <td class="label">
                        <label for="INVOICE_DATE">${labelMap.INVOICE_DATE}</label>
                    </td>
                    <td class="input">
                        <input name="INVOICE_DATE" type="text" class="short" id="INVOICE_DATE" maxlength="10" value="<%= request.getParameter("INVOICE_DATE") != null ? request.getParameter("INVOICE_DATE") : "" %>" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('INVOICE_DATE'); return false;" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_CODE">${labelMap.DOCTOR_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="DOCTOR_CODE" name="DOCTOR_CODE" class="short" value="<%= DBMgr.getRecordValue(doctorRec, "CODE") %>" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR();" />
                        <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR'); return false;" />
                        <input type="text" id="DOCTOR_NAME" name="DOCTOR_NAME" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="PAYOR_OFFICE_CODE">${labelMap.PAYOR_OFFICE_CODE}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input name="PAYOR_OFFICE_CODE" type="text" class="short" id="PAYOR_OFFICE_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(payorOfficeRec, "CODE") %>" onkeypress="return PAYOR_OFFICE_CODE_KeyPress(event);" onblur="AJAX_Refresh_PAYOR_OFFICE();" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=PAYOR_OFFICE&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=PAYOR_OFFICE_CODE&HANDLE=AJAX_Refresh_PAYOR_OFFICE'); return false;" />
                        <input name="PAYOR_OFFICE_NAME" type="text" class="long" id="PAYOR_OFFICE_NAME" readonly="readonly" value="<%= DBMgr.getRecordValue(payorOfficeRec, "NAME") %>" maxlength="255" />                    
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ORDER_ITEM_CODE">${labelMap.ORDER_ITEM_CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="ORDER_ITEM_CODE" name="ORDER_ITEM_CODE" class="short" value="<%= DBMgr.getRecordValue(orderRec, "CODE") %>" onkeypress="return ORDER_ITEM_CODE_KeyPress(event);" onblur="AJAX_Refresh_ORDER_ITEM();" />
                        <input id="SEARCH_ORDER_ITEM_CODE" name="SEARCH_ORDER_ITEM_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=ORDER_ITEM&DISPLAY_FIELD=DESCRIPTION_<%= labelMap.getFieldLangSuffix() %>&TARGET=ORDER_ITEM_CODE&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_ORDER_ITEM'); return false;" />
                        <input type="text" id="ORDER_ITEM_NAME" name="ORDER_ITEM_NAME" class="long" readonly="readonly" value="<%= DBMgr.getRecordValue(orderRec, "NAME") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="HN_NO">${labelMap.HN_NO}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="HN_NO" name="HN_NO" class="short" value="<%= DBMgr.getRecordValue(hnNoRec, "HN_NO") %>" onkeypress="return HN_NO_KeyPress(event);" onblur="AJAX_Refresh_HN_NO();" />
                        <input id="SEARCH_HN_NO" name="SEARCH_HN_NO" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="openSearchForm('../search.jsp?TABLE=TRN_DAILY&RETURN_FIELD=HN_NO&DISPLAY_FIELD=PATIENT_NAME&BEINSIDEHOSPITAL=1&TARGET=HN_NO&HANDLE=AJAX_Refresh_HN_NO'); return false;" />
                        <input type="text" id="PATIENT_NAME" name="PATIENT_NAME" value="<%= DBMgr.getRecordValue(hnNoRec, "PATIENT_NAME") %>" class="long" readonly="readonly" />
                    </td>
                </tr>
                <tr>
                    <th colspan="6" class="buttonBar">                        
                        <input type="submit" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" />
                        <input type="button" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='receipt_by_detail.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
        </form>
        <hr/>
        <form id="dataForm" name="dataForm" method="post" action="transaction_edit_dis_update.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= DBMgr.MODE_QUERY %>" />
            
      <table class="data">
      <tr>
        <th colspan="2" bgcolor="#999999" align="left">Condition</th>
        </tr>
      <tr>
        <!-- <td valign="top" bgcolor="#CCCCCC"><strong>Process</strong></td> -->
        <td valign="top" bgcolor="#CCCCCC">Process</td>
        
        <td bgcolor="#EEEEEE">
		<input type="checkbox" name="ChProcess_inactive" id="ChProcess_inactive" value="1" onclick="Click_Process(1);"/> Inactive<br />
        <input type="checkbox" name="ChProcess_discount" id="ChProcess_discount"  value="1" onclick="Click_Process(2);"/> Discount<br />
		<input type="checkbox" name="ChProcess_change" id="ChProcess_change" value="1" onclick="Click_Process(2);"/> Change Dr .Code</td>
      </tr>
      <tr>
        <td width="34%" bgcolor="#CCCCCC"><strong class="style4"> Type Discount</strong></td>
        <td width="66%" bgcolor="#EEEEEE">
		<input name="RdType" id="RdType" type="radio" value="1" onclick="Click_Type(1);"/>Amount
  		<input name="RdType"  id="RdType" type="radio" value="2" onclick="Click_Type(2);"/><input name="TxtTypePercent" type="text" size="10" onkeypress="IsNumericKeyPress();"/>%
  		</td>
      </tr>
<tr>
        <td width="34%" bgcolor="#CCCCCC"><strong class="style4"> Note All Invoice</strong></td>
        <td width="66%" valign="top" bgcolor="#EEEEEE"><input name="ChAllNote" id="ChAllNote" type="checkbox" value="1" onclick="Click_Note();" /><textarea name="TxtareaNoteAll" cols="50" rows="3" id="TxtareaNoteAll"></textarea>  </td>
      </tr>
      
    </table>
    
            <table class="data">
                <tr>
                    <!--<th colspan="8" class="alignLeft">${labelMap.TITLE_DATA}</th>-->
					<th colspan="13" class="buttonBar"><input type="button" id="UPDATE" name="UPDATE" class="button" value="${labelMap.SAVE}" onclick="save_data();" <%=showButtonSave %>/></th>
                </tr>
                <tr>
                    <td class="sub_head">                    
                    <input type="checkbox" id="allCheckBox" name="allCheckBox" onclick="updateAllCheckBox()" /></td>
                    <td class="sub_head">${labelMap.INVOICE_NO}</td>
                    <td class="sub_head">${labelMap.INVOICE_DATE}</td>
                    <td class="sub_head">${labelMap.PAYOR_OFFICE_CODE}</td>
                    <td class="sub_head">${labelMap.ORDER_ITEM_CODE}</td>
                    <td class="sub_head">${labelMap.HN_NO}</td>
                    <td class="sub_head">${labelMap.ACTIVE}</td>
                    <td class="sub_head">${labelMap.LINE_NO}</td>
                    <td class="sub_head">${labelMap.DOCTOR_CODE}</td>
                    <td class="sub_head">${labelMap.AMOUNT_OF_DISCOUNT}</td>
                    <td class="sub_head">${labelMap.NOTE}</td>
                    <td class="sub_head">${labelMap.DETAIL}</td>
                    <!-- td class="sub_head">${labelMap.ROLLBACK}</td-->
                </tr>
                <%
                if (request.getParameter("MODE") != null) {

                    String cond = "";
                    if (request.getParameter("INVOICE_NO") != null && !request.getParameter("INVOICE_NO").equalsIgnoreCase("")) {
                        cond += " AND INVOICE_NO = '" + request.getParameter("INVOICE_NO") + "'";
                    }
                    if (request.getParameter("INVOICE_DATE") != null && !request.getParameter("INVOICE_DATE").equalsIgnoreCase("")) {
                        cond += " AND INVOICE_DATE = '" + JDate.saveDate(request.getParameter("INVOICE_DATE")) + "'";
                    }
                    if (request.getParameter("PAYOR_OFFICE_CODE") != null && !request.getParameter("PAYOR_OFFICE_CODE").equalsIgnoreCase("")) {
                        cond += " AND PAYOR_OFFICE_CODE = '" + request.getParameter("PAYOR_OFFICE_CODE") + "'";
                    }
                    if (request.getParameter("DOCTOR_CODE") != null && !request.getParameter("DOCTOR_CODE").equalsIgnoreCase("")) {
                        cond += " AND DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") + "'";
                    }
                    if (request.getParameter("HN_NO") != null && !request.getParameter("HN_NO").equalsIgnoreCase("")) {
                        cond += " AND HN_NO = '" + request.getParameter("HN_NO") + "'";
                    }
                    if (request.getParameter("ORDER_ITEM_CODE") != null && !request.getParameter("ORDER_ITEM_CODE").equalsIgnoreCase("")) {
                        cond += " AND ORDER_ITEM_CODE = '" + request.getParameter("ORDER_ITEM_CODE") + "'";
                    }
                    //DBConnection con = new DBConnection();
                    //con.connectToServer();
					//con.connectToLocal();
                    query = "SELECT INVOICE_NO, INVOICE_DATE, LINE_NO, PAYOR_OFFICE_CODE, DOCTOR_CODE, "
                    +" ORDER_ITEM_CODE, HN_NO, PATIENT_NAME, AMOUNT_BEF_DISCOUNT, AMOUNT_OF_DISCOUNT, AMOUNT_AFT_DISCOUNT, ACTIVE "
                    +" FROM TRN_DAILY "
                    +" WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' "
                    +" AND ACTIVE = '1' "
                    +" AND (BATCH_NO IS NULL OR BATCH_NO = '')" 
                    +" AND (GUARANTEE_NOTE IS NULL OR GUARANTEE_NOTE= '')"
                    +" AND IS_WRITE_OFF='N' "
                    + cond;
                   
                    ResultSet rs = con.executeQuery(query);
                    
                    int i = 0;
                    DBConn cdb = new DBConn();
                	cdb.setStatement();
                	String [][]TrnArr = cdb.query(query);
                    int num_trn=0;
                    num_trn=TrnArr.length;
                    if(num_trn==0)
					{
						showButtonSave="disabled";
					}
                	//rs.getMetaData().g
                    while (rs.next()) {
                    	String note_show="";
                    	String sql_note="SELECT NOTE FROM MA_TRN_DAILY WHERE LINE_NO='"+rs.getString("LINE_NO")+"'"
                    	+" AND INVOICE_NO='"+rs.getString("INVOICE_NO")+"'";
                        String [][]NoteArr = cdb.query(sql_note);
                        if(NoteArr.length !=0)
                        {
                        	note_show=NoteArr[0][0];
                        }
                       // i++;
                		//table_row++;
                		%>
                <input type="hidden" name="AMT_BEF_DIS[]" id="AMT_BEF_DIS[]" value="<%= Util.formatHTMLString(rs.getString("AMOUNT_BEF_DISCOUNT"), true)%>">
				<input type="hidden" name="INVOICE_NUMBER[]" id="INVOICE_NUMBER[]" value="<%= Util.formatHTMLString(rs.getString("INVOICE_NO"), true)%>">
                <tr>
                    <td class="row<%=i % 2%> alignCenter"><input type="checkbox" id="DIS[]" name="DIS[]" value="<%=rs.getString("LINE_NO")%>" /></td>
                    <td class="row<%=i % 2%>"><%=Util.formatHTMLString(rs.getString("INVOICE_NO"), true)%></td>
                    <td class="row<%=i % 2%>"><%=JDate.showDate(rs.getString("INVOICE_DATE"))%></td>
                    <td class="row<%=i % 2%>"><%=Util.formatHTMLString(rs.getString("PAYOR_OFFICE_CODE"), true)%></td>
                    <td class="row<%=i % 2%>"><%=Util.formatHTMLString(rs.getString("ORDER_ITEM_CODE"), true)%></td>
                    <td class="row<%=i % 2%>"><%=Util.formatHTMLString(rs.getString("HN_NO"), true)%></td>
                    <td class="row<%=i % 2%>"><%=rs.getString("ACTIVE").equals("1") ? "Active" : "Inactive"%></td>
                    <td class="row<%=i % 2%>"><input type="text" size="10" name="LINE_NO_ARR[]" id="LINE_NO_ARR[]" value="<%=rs.getString("LINE_NO")%>" readonly></td>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("DOCTOR_CODE"), true)%><br><input type="text" size="10" name="DOCTOR_CODE_ARR[<%=i %>]" id="DOCTOR_CODE_ARR[<%=i %>]" readonly>
                    <input id="SEARCH_DOCTOR_CODE_ARR[]" name="SEARCH_DOCTOR_CODE_ARR[]" type="button" value="..." class="image_button" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=DOCTOR_CODE_ARR[<%=i %>]&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_ARR'); return false;" /></td>
                    <td class="row<%=i % 2%>">bef: <%=JNumber.getShowMoney(Double.parseDouble(rs.getString("AMOUNT_BEF_DISCOUNT")))%><br>dis: <%=JNumber.getShowMoney(Double.parseDouble(rs.getString("AMOUNT_OF_DISCOUNT")))%><br><input type="text" size="10" name="AMOUNT_OF_DISCOUNT_ARR[]" id="AMOUNT_OF_DISCOUNT_ARR[]" onkeypress="IsNumericKeyPress();"></td>
                    <td class="row<%=i % 2%>"><textarea name="NOTE_ARR[]" id="NOTE_ARR[]"><%=note_show%></textarea></td>
                    <td class="row<%=i % 2%>"><a href='#' onclick="SetNewSize('transaction_detail.jsp?LINE_NO=<%=Util.formatHTMLString(rs.getString("LINE_NO"), true) %>&INVOICE_NO=<%= Util.formatHTMLString(rs.getString("INVOICE_NO"), true)%>','600','900');">Detail</a></td>
                    <!-- td class="row<%=i % 2%>">Rollback</td-->

                <%
                       i++;
                	   table_row++;
                		
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    con.Close();
                    //con.freeConnection();
                }
                %>                
                <tr>
                    <th colspan="13" class="buttonBar">                        
                        <input type="button" id="UPDATE" name="UPDATE" class="button" value="${labelMap.SAVE}" onclick="save_data();" <%=showButtonSave %>/>
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>

