<%-- 
    Document   : ProcessTax402
    Created on : 5 ม.ค. 2553, 11:15:52
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="java.sql.*"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="df.bean.db.table.SummaryTax402"%>
<%@page import="df.bean.tax.CalculateTax90Bean"%>
<%@page import="df.bean.obj.util.Utils "%>
<%@include file="../../_global.jsp" %>

<%
			String iconSuccess="";
			
			if(request.getParameter("status_run") != null)
			{
				String statusRun=request.getParameter("status_run");
				
				//out.println("statusRun="+statusRun+"<br>");
			
				if(statusRun.equalsIgnoreCase("1"))
				{
					//out.println("ok");
					// Compute Summary Tax402
					DBConnection conn = new DBConnection();
					conn.connectToLocal();
		            CalculateTax90Bean pt = new CalculateTax90Bean(session.getAttribute("HOSPITAL_CODE").toString(), request.getParameter("YYYY"));

		            if (pt.processTax(session.getAttribute("HOSPITAL_CODE").toString(), request.getParameter("YYYY"))) 
		            {
						iconSuccess="1";//success
		            	//out.println("<script>table.rows[currentRowID].cells[2].innerHTML = '<img src=\"../../images/succeed_icon.gif\" alt=\"\" />';</script>");
			        }
		            else 
		            {
		            	iconSuccess="2";//failed
		            	//out.println("<script>table.rows[currentRowID].cells[2].innerHTML = '<img src=\"../../images/failed_icon.gif\" alt=\"\" />';</script>");
		            }
		            	//DialogBox.ShowInfo("Fail!!!");
		            pt = null;

	
				}
				else
				{
					//out.println("no");
				}
				
			}// if(staus_run != null)
			else
			{
				//out.println("noooooooooooooooooooo");
			}
				
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
            labelMap.add("TITLE_MAIN", "Process Summary Tax 90 (Year)", "คำนวณภาษี ภงด.90 (รายปี)");
            labelMap.add("START_DATE", "Start Date", "วันที่เริ่มต้น");
            labelMap.add("END_DATE", "End Date", "วันที่สิ้นสุด");
            labelMap.add("YYYY", "Year", "ปี");
            labelMap.add("COL_0", "No.", "ลำดับ");
            labelMap.add("COL_1", "Process", "ขบวนการ");
            labelMap.add("COL_2", "Month", "เดือน");
            labelMap.add("COL_3", "Status", "สถานะ");
            request.setAttribute("labelMap", labelMap.getHashMap());
            String startDateStr = JDate.showDate(JDate.getDate());
            String endDateStr = JDate.showDate(JDate.getDate());
			String currentMonth = JDate.getDate().substring(0, 4);

			
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
        <script type="text/javascript">
        
			//RUN_Click
            function RUN_Click() {
               table = document.getElementById("dataTable"); 
                fromRowID = currentRowID = 2;
                toRowID = document.getElementById("dataTable").rows.length - 1;
             	document.mainForm.RUN.disabled = true;
                document.mainForm.STOP.disabled = false;
                document.mainForm.CLOSE.disabled = true;
                table.rows[currentRowID].cells[2].innerHTML = '<img src="../../images/processing_icon.gif" alt="" />'
                document.mainForm.status_run.value="1";
                document.mainForm.submit();
            }
            
            function STOP_Click() {
                document.mainForm.RUN.disabled = false;
                document.mainForm.STOP.disabled = true;
                document.mainForm.CLOSE.disabled = false;
                // Send Roll back message via AJAX here 
            }
        </script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="">
        <input type="hidden" name="status_run">
        <center>
			<table width="800" border="0">
				<tr><td align="left">
					<b><font color='#003399'><%=Utils.getInfoPage("ProcessTax90.jsp", labelMap.getFieldLangSuffix(), new DBConnection()) %></font></b>
				</td></tr>
			</table>
		</center>
        
            <table class="form">
                <tr>
                    <th colspan="2">
				  	<div style="float: left;">${labelMap.TITLE_MAIN}</div>				 	</th>
                </tr>
				<tr>
                    <td class="label">
                    <label>${labelMap.YYYY}</label>					</td>
                    <td class="input">
                    <% 
                    //out.println("iconSuccess="+iconSuccess);
                    
                    		if(!iconSuccess.equalsIgnoreCase("")) 
                    		{
                    			out.println(proUtil.selectYY("YYYY", request.getParameter("YYYY")));
                    		}
                    		else 
                    		{
                    			out.println(proUtil.selectYY("YYYY", JDate.getYear()));
                    		}
                    %>
                    </td>
                </tr>
                <tr>
                    <th colspan="2" class="buttonBar">                        
                        <input type="button" id="RUN" name="RUN" class="button" value="${labelMap.RUN}" onclick="RUN_Click();" disabled="disabled" />
                        <input type="button" id="STOP" name="STOP" class="button" value="Stop" onclick="STOP_Click()" disabled="disabled" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />					</th>
                </tr>
            </table>
            <hr />
            <table class="data" id="dataTable" name="dataTable">
                <tr>
                    <th colspan="3" class="alignLeft">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
                                        </th>
                </tr>
                <tr>
                    <td class="sub_head"><%=labelMap.get("COL_0")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_1")%></td>
<%--                    <td class="sub_head"><%=labelMap.get("COL_2")%></td>    --%>
                    <td class="sub_head"><%=labelMap.get("COL_3")%></td>
                </tr>
                
<%
            int i = 2; //SHOW IN TITLE TABLE SUCH AS 0/3
%>
                <tr>
                    <td class="row<%=i % 2%> alignCenter"><%="1"%></td>
                    <td class="row<%=i % 2%> alignCenter"><%="Tax 402"%></td>
                    <td class="row<%=i % 2%> alignCenter">
                    <% if(iconSuccess.equalsIgnoreCase("1"))
                    	{
                    		out.println("<img src=\"../../images/succeed_icon.gif\" alt=\"\" />");
                    	}
                    	else if(iconSuccess.equalsIgnoreCase("2"))
                    	{
                    		out.println("<img src=\"../../images/failed_icon.gif\" alt=\"\" />");
                    	}
                    	else
                    	{
                    		out.println("<img src=\"../../images/waiting_icon.gif\" alt=\"\" />");
                    	}%></td>
                </tr>
<%
                i ++;
%>
<script type="text/javascript">
                    numRow = <%=i - 1%>;
</script>
            </table>
<%
            if (i > 1) {
                out.print("<script type=\"text/javascript\">document.mainForm.RUN.disabled = false;</script>");
            }

%>
        </form>
    </body>
</html>
