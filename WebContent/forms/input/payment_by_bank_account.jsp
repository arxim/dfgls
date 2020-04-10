<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@page import="java.sql.*"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.obj.util.Utils "%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.process.ProcessUtil"%>
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
            ProcessUtil proUtil = new ProcessUtil();
            DBConnection c = new DBConnection();
            c.connectToLocal();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
            c.Close();
			
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Doctor Bank Account", "บัญชีแพทย์");
            labelMap.add("TITLE_DETAIL", "Bank Account", "บัญชี");
            labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("NAME_THAI", "Name (Thai)", "ชื่อ (ไทย)");
            labelMap.add("NAME_ENG", "Name (Eng)", "ชื่อ (อังกฤษ)");
            labelMap.add("DOCTOR_NAME","Doctor Name","ชื่อแพทย์");
            labelMap.add("BANK_ACCOUNT_NO", "Bank Account No.", "เลขที่บัญชีธนาคาร");
            labelMap.add("BANK_ACCOUNT_NAME", "Bank Account Name.", "ชื่อบัญชีธนาคาร");
            labelMap.add("AMOUNT", "Amount", "จำนวนเงิน");
            labelMap.add("MM", "Month", "เดือน");
            labelMap.add("YYYY", "Year", "ปี");
            labelMap.add("BALANCE", "Balance", "คงเหลือ");

            request.setAttribute("labelMap", labelMap.getHashMap());
            
			//
			// Process request
			//

            request.setCharacterEncoding("UTF-8");
            DataRecord doctorRec = null, doctorBankAccountRec = null,summaryPaymentRec = null, trnPaymentByBankRec = null ;
            byte MODE = DBMgr.MODE_INSERT;
            String readonlyCheck = "";
            boolean status = false;
            
            if (request.getParameter("MODE") != null) {
            	
            	String bankAccountNo[] = request.getParameterValues("BANK_ACCOUNT_NO");
                String paidAmount[] = request.getParameterValues("PAID_AMOUNT");
                MODE = Byte.parseByte(request.getParameter("MODE"));
                
                if (MODE == DBMgr.MODE_INSERT) {
                	DBConnection con = new DBConnection();
        			con.connectToLocal();
        			String sqlDelete = "DELETE FROM TRN_PAYMENT_BY_BANK WHERE HOSPITAL_CODE='"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND YYYY='"+request.getParameter("YYYY")+"' AND MM='"+request.getParameter("MM")+"' AND DOCTOR_CODE='"+request.getParameter("DOCTOR_CODE")+"'";
        			DBConn conDoctor = new DBConn();
        			conDoctor.setStatement();
        			
        			status = con.executeUpdate(sqlDelete) < 0 ? false : true;
        			con.Close();
                    
        			if(status){
        				for(int i=0; i<bankAccountNo.length; i++){
        					if(Double.parseDouble(paidAmount[i]) > 0){
        						trnPaymentByBankRec = new DataRecord("TRN_PAYMENT_BY_BANK");
                                trnPaymentByBankRec.addField("HOSPITAL_CODE", Types.VARCHAR, session.getAttribute("HOSPITAL_CODE").toString(), true);
                                trnPaymentByBankRec.addField("DOCTOR_CODE", Types.VARCHAR, request.getParameter("DOCTOR_CODE"), true);
                                trnPaymentByBankRec.addField("BANK_ACCOUNT_NO", Types.VARCHAR, bankAccountNo[i], true);
                                trnPaymentByBankRec.addField("PAID_AMOUNT", Types.VARCHAR, paidAmount[i]);
                                trnPaymentByBankRec.addField("YYYY", Types.VARCHAR, request.getParameter("YYYY"), true);
                                trnPaymentByBankRec.addField("MM", Types.VARCHAR, request.getParameter("MM"), true);
                                trnPaymentByBankRec.addField("UPDATE_DATE", Types.VARCHAR, JDate.getDate());
                                trnPaymentByBankRec.addField("UPDATE_TIME", Types.VARCHAR, JDate.getTime());
                            	trnPaymentByBankRec.addField("USER_ID", Types.VARCHAR, session.getAttribute("USER_ID").toString());
                            	
                            	if (DBMgr.insertRecord(trnPaymentByBankRec)) {
                                    session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_SUCCESS).replace("[HREF]", "input/payment_by_bank_account.jsp?DOCTOR_CODE=" + request.getParameter("DOCTOR_CODE")));
                                } else {
                                    session.setAttribute("MSG", labelMap.get(LabelMap.MSG_SAVE_FAIL));
                                }
        					}
        				}
        			}
                }else if (MODE == DBMgr.MODE_QUERY) {
                	doctorRec = DBMgr.getRecord("SELECT * FROM DOCTOR WHERE CODE = '" + request.getParameter("DOCTOR_CODE") +  "' AND HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"'");
                	doctorBankAccountRec = DBMgr.getRecord("SELECT * FROM DOCTOR_BANK_ACCOUNT WHERE DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") +  "' AND HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND ACTIVE='1' ");
                	summaryPaymentRec = DBMgr.getRecord("SELECT * FROM SUMMARY_PAYMENT WHERE DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") +  "' AND HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND YYYY='"+b.getYyyy()+"' AND MM='"+b.getMm()+"'");
                	MODE = DBMgr.MODE_INSERT;
                }
                
                response.sendRedirect("../message.jsp");
                return;
            }
            else if (request.getParameter("DOCTOR_CODE") != null) {
            	doctorRec = DBMgr.getRecord("SELECT * FROM DOCTOR WHERE CODE = '" + request.getParameter("DOCTOR_CODE") +  "' AND HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"'");
            	doctorBankAccountRec = DBMgr.getRecord("SELECT * FROM DOCTOR_BANK_ACCOUNT WHERE DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") +  "' AND HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND ACTIVE='1' ");
            	summaryPaymentRec = DBMgr.getRecord("SELECT * FROM SUMMARY_PAYMENT WHERE DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") +  "' AND HOSPITAL_CODE='"+ session.getAttribute("HOSPITAL_CODE").toString() +"' AND YYYY='"+b.getYyyy()+"' AND MM='"+b.getMm()+"'");
            	
            	MODE = DBMgr.MODE_INSERT;
               
            }else{
            	MODE = DBMgr.MODE_QUERY;
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
        <script type="text/javascript" src="../../javascript/jquery-1.6.min.js"></script>
        <script type="text/javascript">
        
        $( document ).ready(function() {
        	doCalAmount();
        });
            
        function DOCTOR_CODE_KeyPress(e) {
            var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

            if (key == 13) {
                //document.mainForm.DOCTOR_CODE.blur();
                Refresh_DOCTOR();
                return false;
            }
            else {
                return true;
            }
        }
        
        function Refresh_DOCTOR() {
            var to = document.location.pathname.lastIndexOf('?');
            if (to < 0) {
                window.location = document.location.pathname + '?DOCTOR_CODE=' + document.mainForm.DOCTOR_CODE.value;
            }
            else {
                window.location = document.location.pathname.substr(0, to) + '?DOCTOR_CODE=' + document.mainForm.DOCTOR_CODE.value;
            }
        }
        
        function AJAX_Refresh_DOCTOR() {
            var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.DOCTOR_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>' ";
            AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
        }
        
        function AJAX_Handle_Refresh_DOCTOR() {
            if (AJAX_IsComplete()) {
                var xmlDoc = AJAX.responseXML;

                // Data not found
                if (!isXMLNodeExist(xmlDoc, "CODE")) {
                    document.mainForm.DOCTOR_DESCRIPTION.value = "";
                    return;
                }
                // Data found
                document.mainForm.DOCTOR_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "NAME_<%=labelMap.getFieldLangSuffix()%>");

                if(document.mainForm.DOCTOR_CODE.value != ""){
                	AJAX_Refresh_SUMMARY_PAYMENT();
                }
            }
        }
       
        function AJAX_Refresh_SUMMARY_PAYMENT() {
            var target = "../../RetrieveData?TABLE=SUMMARY_PAYMENT&COND=DOCTOR_CODE='" + document.mainForm.DOCTOR_CODE.value + "' AND YYYY='<%=b.getYyyy()%>' AND MM='<%=b.getMm()%>' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
            AJAX_Request(target, AJAX_Handle_Refresh_SUMMARY_PAYMENT);
        }    
            
            
        function AJAX_Handle_Refresh_SUMMARY_PAYMENT() {
            if (AJAX_IsComplete()) {
                var xmlDoc = AJAX.responseXML;
                    // Data not found
            	if (!isXMLNodeExist(xmlDoc, "DOCTOR_CODE")) {
                	document.mainForm.DR_NET_PAID_AMT.value = "";
                	return;
                } 
                // Data found
				document.mainForm.DR_NET_PAID_AMT.value = getXMLNodeValue(xmlDoc, "DR_NET_PAID_AMT");
			}
		}
        
        function doReset(){
        	document.mainForm.reset();
        	document.mainForm.MODE.value= "<%=DBMgr.MODE_QUERY%>";
        	
        }
        
        function doCalAmount(){
        	var balance = 0;
        	
        	
        	for(var i=0; i<document.getElementsByName("PAID_AMOUNT").length; i++){
        		if(document.getElementsByName("PAID_AMOUNT")[i].value == '' || !isObjectValidNumber(document.getElementsByName("PAID_AMOUNT")[i], '<%=labelMap.get(LabelMap.ALERT_INVALID_NUMBER)%>')){
        			document.getElementsByName("PAID_AMOUNT")[i].value = '0.00';
        		}
        		balance += parseFloat(document.getElementsByName("PAID_AMOUNT")[i].value);
        	}
        	
        	if(document.mainForm.DR_NET_PAID_AMT.value != ''){
        		
        		document.mainForm.BALANCE.value = parseFloat(document.mainForm.DR_NET_PAID_AMT.value) - balance;
            	if(document.mainForm.BALANCE.value == 0){
            		document.mainForm.BALANCE.style.color = '#000000';
            	}else{
            		document.mainForm.BALANCE.style.color = '#ff0000';
            	}
        	}
        	
        	
        }
            
        function SAVE_Click() {
            if(!isObjectEmptyString(document.mainForm.DOCTOR_CODE, '<%=labelMap.get(LabelMap.ALERT_REQUIRED_FIELD)%>') &&
            		parseFloat(document.mainForm.BALANCE.value) == 0 ){
            	if (confirm("<%=labelMap.get("CONFIRM_REPLACE_DATA")%>")) {
                    document.mainForm.MODE.value= "<%=DBMgr.MODE_INSERT%>";
                    document.mainForm.submit();
                }
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
        <form id="mainForm" name="mainForm" method="post" action="payment_by_bank_account.jsp">
            <input type="hidden" id="MODE" name="MODE" value="<%= MODE%>" />
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                        <b><font color='#003399'><%=Utils.getInfoPage("payment_by_bank_account.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
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
                    <label for="DOCTOR_CODE"><span class="style1">${labelMap.DOCTOR_CODE}*</span></label></td>
                    <td class="input" colspan="3">
                        <input name="DOCTOR_CODE" type="text" class="short" id="DOCTOR_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(doctorRec, "CODE")%>" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR();" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%=labelMap.getFieldLangSuffix()%>&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=DOCTOR_CODE&HANDLE=Refresh_DOCTOR'); return false;" />
                        <input name="DOCTOR_DESCRIPTION" type="text" class="long" id="DOCTOR_DESCRIPTION" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorRec, "NAME_"+labelMap.getFieldLangSuffix() )%>" maxlength="255" />                    </td>
                </tr>
                <tr>
                    <td class="label"><label>${labelMap.MM}</label></td>
                    <td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "MM", b.getMm())%></td>
                    <td class="label">
                         <label>${labelMap.YYYY}</label>
					</td>
                    <td class="input"><%=proUtil.selectYY("YYYY", b.getYyyy())%></td>
                </tr>
                <tr>
                    <td class="label"><label for="DR_NET_PAID_AMT">${labelMap.AMOUNT}</label></td>
                    <td class="input">
                        <input type="text" id="DR_NET_PAID_AMT" name="DR_NET_PAID_AMT" class="short alignRight" readonly="readonly" maxlength="20" value="<%= DBMgr.getRecordValue(summaryPaymentRec, "DR_NET_PAID_AMT") %>" />                 
                    </td>
                    <td class="label"><label for="BALANCE">${labelMap.BALANCE}</label></td>
                    <td class="input">
                        <input type="text" id="BALANCE" name="BALANCE" class="short alignRight" readonly="readonly" maxlength="20" value="" />                 
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SELECT}" onclick="AJAX_Refresh_DOCTOR()" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='payment_by_bank_account.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
            <hr />
            <table class="data">
                <tr>
                    <th colspan="7" class="alignLeft">${labelMap.TITLE_DETAIL}</th>
                </tr>
                <tr>
                    <td class="sub_head">${labelMap.BANK_ACCOUNT_NO}</td>
                    <td class="sub_head">${labelMap.BANK_ACCOUNT_NAME}</td>
                    <td class="sub_head">${labelMap.AMOUNT}</td>
                </tr>
                <%
			if(request.getParameter("DOCTOR_CODE") != null){
		
            DBConnection con = new DBConnection();
            con.connectToLocal();
            List<String> bankAccountNo = new ArrayList();
            List<String> bankAccountName = new ArrayList();
            
            ResultSet rs = con.executeQuery("SELECT BANK_ACCOUNT_NO,BANK_ACCOUNT_NAME  FROM DOCTOR_BANK_ACCOUNT WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND DOCTOR_CODE = '" + request.getParameter("DOCTOR_CODE") + "' AND ACTIVE='1' ORDER BY BANK_ACCOUNT_NO ASC");
            int i = 0;
            while (rs.next()) {
            	bankAccountNo.add(i, rs.getString("BANK_ACCOUNT_NO") == null ? "" : rs.getString("BANK_ACCOUNT_NO"));
            	bankAccountName.add(i, rs.getString("BANK_ACCOUNT_NAME") == null ? "" : rs.getString("BANK_ACCOUNT_NAME"));
                i++;
            }
            if (rs != null) {
                rs.close();
            }
            
            for(int j=0;j<bankAccountNo.size(); j++){
            	List<String> paidAmount = new ArrayList();
            	String sql = "SELECT PAID_AMOUNT FROM TRN_PAYMENT_BY_BANK WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE").toString()+"' AND YYYY+MM='"+b.getYyyy()+b.getMm()+"' AND DOCTOR_CODE = '" + DBMgr.getRecordValue(doctorBankAccountRec, "DOCTOR_CODE") + "' AND BANK_ACCOUNT_NO='"+bankAccountNo.get(j)+"' ";
            	ResultSet ck = con.executeQuery(sql);
            	while (ck.next()) {
            		paidAmount.add(0, ck.getString("PAID_AMOUNT"));
            	}
            	if (ck != null) {
            		ck.close();
                }
            	
            	%>                
                <tr>
                    <td class="row<%=j % 2%>" align="center"><input type="hidden" name="BANK_ACCOUNT_NO" size="10" value="<%=bankAccountNo.get(j)%>"><%= Util.formatHTMLString(bankAccountNo.get(j), true)%></td>
                    <td class="row<%=j % 2%>"><%= Util.formatHTMLString(bankAccountName.get(j), true)%></td>
                    <td class="row<%=j % 2%>" align="center"><input type="textbox" name="PAID_AMOUNT" class="short alignRight" size="10" value="<%=paidAmount.size() == 0 ? "0.00" : paidAmount.get(0)%>" onblur="doCalAmount()"></td>
                    
                </tr>
                <%
            	
            }
            con.Close();
			}
                %>  
                <tr>
                    <th colspan="3" class="buttonBar">                        
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="SAVE_Click()" />
                    </th>
                </tr>  
            </table>
        </form>
      </body>
</html>
