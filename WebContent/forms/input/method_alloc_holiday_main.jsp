<%@page import="df.bean.interfacefile.InterfaceData"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@page import="java.sql.*"%>

<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_METHOD_ALLOC_ITEM_MAIN)) {
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
            labelMap.add("TITLE_MAIN", "Long Weekend Allocate", "ส่วนแบ่งวันหยุดนักขัตฤกษ์");
            labelMap.add("YYYY", "Year", "ปี");
            labelMap.add("MM", "Month", "เดือน");
            labelMap.add("TITLE_DATA", "Long Weekend Allocate", "ส่วนแบ่งวันหยุดนักขัตฤกษ์");
            labelMap.add("DOCTOR_CATEGORY_CODE", "Doctor Category", "กลุ่มแพทย์");
            labelMap.add("ORDER_ITEM_CATEGORY_CODE", "Order Item Category", "กลุ่มรายการรักษา");
            labelMap.add("DOCTOR_CODE", "Doctor Code", "รหัสแพทย์");
            labelMap.add("ORDER_ITEM_CODE", "Order Item", "รายการรักษา");
            labelMap.add("DATE_HOLIDAY", "Date", "วันหยุดนักขัตฤกษ์");
            labelMap.add("INCLUDE", "Include", "คำนวน");
            labelMap.add("PATIENT_DEPARTMENT_CODE", "Patient Department", "แผนก");
            labelMap.add("ORDER_ITEM_DESCRIPTION", "Description", "ชื่อ");
            labelMap.add("ADMISSION_TYPE_CODE", "Admission Type", "แผนกรับผู้ป่วย");
            labelMap.add("PRICE", "Price", "ราคา");
            labelMap.add("NORMAL_ALLOCATE_PCT", "%", "%");
            labelMap.add("NORMAL_ALLOCATE_AMT", "Amount", "จำนวน");
            
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request+
            //

            request.setCharacterEncoding("UTF-8");
            DataRecord doctorCategoryRec = null;
            String arr = "";
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
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
        <script type="text/javascript">
            
            function DOCTOR_CATEGORY_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    if(document.mainForm.YYYY.value != ""&&document.mainForm.MM.value != ""){
                        AJAX_Refresh_DOCTOR_CATEGORY();                    
                    }
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_CATEGORY() {
                var target = "../../RetrieveData?TABLE=DOCTOR_CATEGORY&COND=CODE='" + document.mainForm.DOCTOR_CATEGORY_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_CATEGORY);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_CATEGORY() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        alert("<%=labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)%>");
                        //document.mainForm.DOCTOR_CATEGORY_CODE.value = "";
                        document.mainForm.DOCTOR_CATEGORY_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_CATEGORY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }

        </script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="">
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                            <b><font color='#003399'><%=Utils.getInfoPage("method_alloc_holiday_main.jsp", labelMap.getFieldLangSuffix(),  new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
		</table>
            </center>
            <table class="form">
                <tr> 
                    <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>
                <tr>
                    <td class="label">
                        <label>${labelMap.MM}</label>
                    </td>
                    <td class="input"><%=proUtil.selectMM(session.getAttribute("LANG_CODE").toString(), "MM", b.getMm())%></td>
                    <td class="label">
                         <label>${labelMap.YYYY}</label>
					</td>
                    <td class="input"><%=proUtil.selectYY("YYYY", b.getYyyy())%></td>
                </tr>                
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="window.location = 'method_alloc_holiday_main.jsp?YYYY=' + document.mainForm.YYYY.value+'&MM='+document.mainForm.MM.value; return false;" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='method_alloc_holiday_main.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
            <hr />
            <table class="data" id="dataTable" name="dataTable">
                <tr>
                    <th colspan="10" class="alignLeft">${labelMap.TITLE_DATA}</th>
                </tr>
                <tr>
                 	<td class="sub_head"><%=labelMap.get("DATE_HOLIDAY")%></td>
                    <td class="sub_head"><%=labelMap.get("ADMISSION_TYPE_CODE")%></td>
                    <td class="sub_head"><%=labelMap.get("NORMAL_ALLOCATE_PCT")%></td>
                    <td class="sub_head"><%=labelMap.get("PATIENT_DEPARTMENT_CODE")%></td>
                    <td class="sub_head"><%=labelMap.get("DOCTOR_CATEGORY_CODE")%></td>
                    <td class="sub_head"><%=labelMap.get("ORDER_ITEM_CATEGORY_CODE")%></td>
                    <td class="sub_head"><%=labelMap.get("DOCTOR_CODE")%></td>
                    <td class="sub_head"><%=labelMap.get("ORDER_ITEM_CODE")%></td>                                     
                    <td class="sub_head"><%=labelMap.get(LabelMap.ACTIVE)%></td>
                    <td class="sub_head"><%=labelMap.get(LabelMap.EDIT)%></td>
                </tr>
                <%
		            DBConnection con = new DBConnection();
		            con.connectToLocal();
		            String query="";
					if(request.getParameter("YYYY") != null&&request.getParameter("MM") != null){
						 query="SELECT  * FROM [STP_HOLIDAY] WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' AND YYYY='"+request.getParameter("YYYY")+"' AND MM='"+request.getParameter("MM")+"' ORDER BY ACTIVE DESC,DOCTOR_CATEGORY_CODE ASC,ORDER_ITEM_CATEGORY_CODE ASC,DOCTOR_CODE ASC,ORDER_ITEM_CODE ASC";
				         
					}else{
						 query="SELECT  * FROM [STP_HOLIDAY]  WHERE HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "' ORDER BY ACTIVE DESC,DOCTOR_CATEGORY_CODE ASC,ORDER_ITEM_CATEGORY_CODE ASC,DOCTOR_CODE ASC,ORDER_ITEM_CODE ASC";
				            
					}
					 System.out.print("Main >"+query);
		            ResultSet rs = con.executeQuery(query);
		            int i = 0;
		            String activeIcon, linkEdit;
		            while (rs != null && rs.next()) {
	                activeIcon = "<img src=\"../../images/" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? "" : "in") + "active_icon.png\" alt=\"" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? labelMap.get(LabelMap.ACTIVE_1) : labelMap.get(LabelMap.ACTIVE_0)) + "\" />";
	                linkEdit = "<a href=\"method_alloc_holiday_detail.jsp?MODEUPDATE=2&HOSPITAL_CODE=" + rs.getString("HOSPITAL_CODE") + "&DOCTOR_CATEGORY_CODE=" + rs.getString("DOCTOR_CATEGORY_CODE") + "&ORDER_ITEM_CATEGORY_CODE=" + rs.getString("ORDER_ITEM_CATEGORY_CODE") + "&DOCTOR_CODE=" + rs.getString("DOCTOR_CODE") + "&ORDER_ITEM_CODE=" + rs.getString("ORDER_ITEM_CODE") + "&DD=" + rs.getString("DD") + "&PATIENT_DEPARTMENT_CODE=" + rs.getString("PATIENT_DEPARTMENT_CODE") + "&MM=" + rs.getString("MM") + "&YYYY=" + rs.getString("YYYY")+ "&ACTIVE=" + rs.getString("ACTIVE")+ "&ADMISSION_TYPE_CODE=" + rs.getString("ADMISSION_TYPE_CODE")+ "&NOR_ALLOCATE_PCT=" + rs.getString("NOR_ALLOCATE_PCT") + "\" title=\"" + labelMap.get(LabelMap.EDIT) + "\"><img src=\"../../images/edit_button.png\" alt=\"" + labelMap.get(LabelMap.EDIT) + "\" /></a>";
                %>                
                <tr>
               		<td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("DD"), true)+"/"+Util.formatHTMLString(rs.getString("MM"), true)+"/"+Util.formatHTMLString(rs.getString("YYYY"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("ADMISSION_TYPE_CODE"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("NOR_ALLOCATE_PCT"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("PATIENT_DEPARTMENT_CODE"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("DOCTOR_CATEGORY_CODE"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("ORDER_ITEM_CATEGORY_CODE"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("DOCTOR_CODE"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("ORDER_ITEM_CODE"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= activeIcon %></td>
                    <td class="row<%=i % 2%> alignCenter"><%= linkEdit%></td>
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
                    <th colspan="10" class="buttonBar">                        
                        <input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}" onclick="window.location = 'method_alloc_holiday_detail.jsp'" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
<%=arr%>