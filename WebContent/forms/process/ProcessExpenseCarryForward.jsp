<%-- 
    Document   : ProcessExpense
    Created on : 
    Author     : 
--%>

<%@page import="df.bean.process.ProcessExpenseBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.HashMap" %>
<%@page import="java.sql.*"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.conn.DBConn"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="java.sql.*"%>
<%@include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_PROCESS_PREPARE_GUARANTEE)) {
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
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Expense Carry Forward", "คำนวณยกยอด Expense ที่ไม่พอจ่าย ");
            labelMap.add("LABEL_SHOWDATE", "Month/Year", "เดือน/ปี");
            labelMap.add("LABEL_GROUP", "Group Code", "รหัสกลุ่ม");
            labelMap.add("COL_0", "No.", "ลำดับ");
            labelMap.add("COL_1", "Process", "ขบวนการ");
            labelMap.add("COL_2", "Month", "เดือน");
            labelMap.add("COL_3", "Status", "สถานะ");
            labelMap.add("YYYY", "Year", "ปี");
            labelMap.add("MM", "Month", "เดือน");
            labelMap.add("EXP_CODE", "EXP_CODE", "รหัส Expense");
            labelMap.add("EXP_DESP", "EXP_DESP", "Description");
            labelMap.add("AMOUNT", "Amount", "จำนวน");
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            DBConnection con;
            con = new DBConnection();
            con.connectToLocal();
            //con.connectToServer();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), con);
            con.Close();
            //con.freeConnection();
            String BATCH_DATE = b.getYyyy() + b.getMm() + "00";
            
            String startDateStr = JDate.showDate(JDate.getDate());
            String endDateStr = JDate.showDate(JDate.getDate());
			String currentMonth = JDate.getDate().substring(0, 4);
			
			String defaultMM=JDate.getMonth();
			String defaultYYYY=JDate.getYear();
			String showDate=b.getMm()+"/"+b.getYyyy();
			
			
			//
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
			ProcessUtil util = new ProcessUtil();
            DBConnection c = new DBConnection();
            c.connectToLocal();
            Batch bb = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), c);
            String mouth = "";
            String year = "";
            c.Close();
			
            if(request.getParameter("MM") == null){
            	mouth = bb.getMm();
            	year = bb.getYyyy();
            }else{
            	mouth = request.getParameter("MM");
            	year = request.getParameter("YYYY");
            }
            ArrayList<HashMap<String,String>> listData = new ArrayList<HashMap<String,String>>();
            //ArrayList<HashMap<String,String>> listData =null;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
        <script type="text/javascript">
            //SELECT_Click
             function SELECT_Click() {
                document.mainForm.TYPE.value='Exp';
                document.mainForm.MM.value= document.getElementById("MONTH").value;
                document.mainForm.YYYY.value= document.getElementById("YEAR").value;
                document.mainForm.submit();
            }
            //SAVE_Click
            function save_data() {
            	document.subForm.TYPe.value='UpdateExp';
            	document.subForm.Mm.value= document.getElementById("MONTH").value;
                document.subForm.YYYy.value= document.getElementById("YEAR").value;
            	document.subForm.submit();
            }
            
            //disables SAVE
            document.mainForm.UPDATE.disabled = true;
            // Check data on table SUMMARY_MONTHLY : 2009-07-01 By Nop
            function AJAX_Verify_Check_Guarantee() {
                var MM = document.mainForm.MONTH.value;
                var YYYY = document.mainForm.YEAR.value;
                var target = "../../CheckSummaryGuaranteeSrvl?MM="+MM+"&YYYY="+YYYY+"&FORM=''";
                //alert(target);
                AJAX_Request(target, AJAX_Handle_Verify_Check_Guarantee);
            }  

            function AJAX_Handle_Verify_Check_Guarantee(){
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;
                   // alert(getXMLNodeValue(xmlDoc, "STATUS"));
                    if (getXMLNodeValue(xmlDoc, "STATUS")=='YES') {
                    	 document.mainForm.UPDATE.disabled = false;
                    }else{
                    	document.mainForm.UPDATE.disabled = true;
                    }
                }				
           	}
            
        </script>
    </head>
    <body onload="AJAX_Verify_Check_Guarantee();">
        <form id="mainForm" name="mainForm" method="post" action="">
        <input type="hidden" name="MM" id="MM" />
        <input type="hidden" name="YYYY" id="YYYY" />
        <input type="hidden" name="TYPE" id="TYPE" >
        <center>
        	<table width="800" border="0">
            	<tr><td align="left">
                	<b><font color='#003399'><%=Utils.getInfoPage("ProcessExpenseCarryForward.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                </td></tr>
        	</table>
        </center>
            <table class="form">
                <tr>
                    <th colspan="4">
				  	<div style="float: left;">${labelMap.TITLE_MAIN}</div></th>
                </tr>
				<tr>
                    <td class="label">
                        <label for="aText">${labelMap.MM}</label>
                    </td>
                    <td class="input">
                     <input type="text" class="short"  value="<%=b.getMm()%>" name="MONTH" id="MONTH" readonly/>
                     </td>
                    <td class="label">
                         <label>${labelMap.YYYY}</label>
					</td>
					<td class="input">
                     <input type="text" class="short"  value="<%=b.getYyyy() %>" name="YEAR" id="YEAR" readonly/>
                     </td>
                </tr> 
                <tr>
                    <th colspan="4" class="buttonBar"> 
                  	 	<input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="SELECT_Click();" />  
                  	 	 <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='ProcessExpense.jsp'" />                     
                        <%-- <input type="button" id="RUN" name="RUN" class="button" value="${labelMap.RUN}" onclick="RUN_Click();" disabled="disabled" />
                        <input type="button" id="STOP" name="STOP" class="button" value="Stop" onclick="STOP_Click()" disabled="disabled" />--%>
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" /> 					
                     </th>
                </tr>
            </table>
            <hr />
             <table class="data" id="dataTable">
                <tr>
                    <th colspan="9" class="alignLeft">${labelMap.TITLE_DATA}</th>
                </tr>
                <tr>
                	<td class="sub_head"><%=labelMap.get("YYYY")%></td>
                    <td class="sub_head"><%=labelMap.get("MM")%></td>
                	<td class="sub_head"><%=labelMap.get("DOCTOR_CODE")%></td>
                    <td class="sub_head"><%=labelMap.get("EXP_CODE")%></td>
                    <td class="sub_head"><%=labelMap.get("EXP_DESP")%></td>
                    <td class="sub_head"><%=labelMap.get("AMOUNT")%></td>
                    <td class="sub_head"><%=labelMap.get(LabelMap.ACTIVE)%></td>
                </tr>
                <%
               		DBConnection cn = new DBConnection();
           			cn.connectToLocal();
               	    DBConn cdb = new DBConn(cn.getConnection());
               	    String type = request.getParameter("TYPE");
            		String month_ = request.getParameter("MM");
            		String year_ = request.getParameter("YYYY");
            		String hospital_code_ = ""+session.getAttribute("HOSPITAL_CODE"); 
            		listData = new ArrayList<HashMap<String,String>>();
        			ProcessExpenseBean exp = new ProcessExpenseBean(cdb);

            		if(type != null){
            			listData = exp.CalculateExpenseCarryForward(month_, year_, hospital_code_);
            			String  batch_ = b.getYyyy() + b.getMm();
            			if(listData.size()>0){}
            		%>
            		<script type="text/javascript">
            		  if(<%=listData.size()>0%>){
            			 //var element= document.getElementById("UPDATE");
            			/*  var element =document.getElementById("UPDATE")("UPDATE");
            			 element.removeAttribute("disabled"); */
            		  }
            		</script>
            		<% 	
            			for(int i=0;i<listData.size();i++){
            				String  yyyyMm = listData.get(i).get("YYYY")+listData.get(i).get("MM"); 
            				String status = listData.get(i).get("ACT").equals("0") ? "ยกเลิก" : yyyyMm.equals(batch_) ? "หักในเดือน":"ยกยอด";	
            		%>
						<tr>
							<td class="row<%=i%> alignCenter"><%=listData.get(i).get("YYYY")%></td>
							<td class="row<%=i%> alignCenter"><%=listData.get(i).get("MM")%></td>
							<td class="row<%=i%> alignCenter"><%=listData.get(i).get("DOCTOR_CODE")%></td>
							<td class="row<%=i%> alignCenter"><%=listData.get(i).get("EXP_CODE")%></td>
							<td class="row<%=i%> alignCenter"><%=listData.get(i).get("EXP_DESP")%></td>
							<td class="row<%=i%> alignCenter"><%=listData.get(i).get("AMT")%></td>
							<td class="row<%=i%> alignCenter"><%=status%></td>
						</tr>
				<%
					}//loop
				  }
            		
				%>	
				<tr>
                    <th colspan="7" class="buttonBar">                        
                        <input type="button" id="UPDATE" name="UPDATE" class="button" value="${labelMap.SAVE}" onclick="save_data();" />
                    </th>
                </tr>
            </table> 
        </form>
         <form id="subForm" name="subForm" method="post" action="">
       	  <input type="hidden" name="TYPe" id="TYPe" >
       	  <input type="hidden" name="Mm" id="Mm" />
        	<input type="hidden" name="YYYy" id="YYYy" />
         	<% 
	       	    String typeUp = request.getParameter("TYPe");
	    		String monthUp = request.getParameter("Mm");
	    		String yearUp = request.getParameter("YYYy");
	    		String hospital_codeUp = ""+session.getAttribute("HOSPITAL_CODE"); 
         		if(typeUp != null){
         			if(typeUp.equals("UpdateExp")|| typeUp=="UpdateExp"){
            			ProcessExpenseBean expUp = new ProcessExpenseBean(cdb);
            			System.out.println(monthUp+"  "+yearUp);
             			exp.updateExpCarryForward(monthUp,yearUp,hospital_codeUp); 
         			}
         		}
         	%>
         </form>
    </body>
</html>
