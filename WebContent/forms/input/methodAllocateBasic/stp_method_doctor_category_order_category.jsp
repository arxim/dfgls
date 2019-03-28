<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.jsp.Util"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="java.sql.*"%>
<%@ include file="../../../_global.jsp" %>
<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_INPUT_METHOD_ALLOC_ITEM_MAIN)) {
                response.sendRedirect("../../message.jsp");
                return;
            }

            //
            // Initial LabelMap
            //

            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Doctor Category & Order Item Category Method Allocate Basic", "Doctor Category & Order Item Category Method Allocate Basic");
            labelMap.add("TITLE_DATA", "Doctor Category & Order Item Category Method Allocate Basic", "Doctor Category & Order Item Category Allocate Basic");
            labelMap.add("DOCTOR_CATEGORY_CODE", "Doctor Category", "Doctor Category");
            labelMap.add("ORDER_ITEM_CODE", "Order Item", "Order Item");
            labelMap.add("PAYOR_CATEGORY_CODE" , "PAYOR Office Code"  , "PAYOR Office Code");
            labelMap.add("ORDER_ITEM_CATEGORY_CODE", "Order Item Category Code" , "Order Item Category Code ");
            labelMap.add("ORDER_ITEM_DESCRIPTION", "Description", "ชื่อ");
            labelMap.add("ADMISSION_TYPE_CODE", "I / O", "Admission Type");
            labelMap.add("PRICE", "Price", "ราคา");
            labelMap.add("NORMAL_ALLOCATE_PCT", "%", "%");
            labelMap.add("NORMAL_ALLOCATE_AMT", "Amount", "จำนวน");
            labelMap.add("IS_PROCEDURE", "PRC", "หัตถการ");
            
            request.setAttribute("labelMap", labelMap.getHashMap());
            
            //
            // Process request
            //

            request.setCharacterEncoding("UTF-8");
            DataRecord doctorCategoryRec = null;
            String arr = "";
            if (request.getParameter("DOCTOR_CATEGORY_CODE") != null) {
                doctorCategoryRec = DBMgr.getRecord("SELECT * FROM DOCTOR_CATEGORY WHERE HOSPITAL_CODE = '"+session.getAttribute("HOSPITAL_CODE")+"' AND  CODE = '" + request.getParameter("DOCTOR_CATEGORY_CODE") + "'");
                if(doctorCategoryRec==null)
                {
                    arr +="<script language='javascript'>";
                    arr +="alert('"+ labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)+"')";  
                    arr +="</script>";
                }
            }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <link rel="stylesheet" type="text/css" href="../../../css/share.css" media="all" />
        <script type="text/javascript" src="../../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../../javascript/util.js"></script>
        <script type="text/javascript" src="../../../javascript/search_form.js"></script>
         <script type="text/javascript" src="../../../javascript/jquery-1.6.min.js"></script>
        <script type="text/javascript">
            function DOCTOR_CATEGORY_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox
                if (key == 13) {
                    if(document.mainForm.DOCTOR_CATEGORY_CODE.value != ""){
                        AJAX_Refresh_DOCTOR_CATEGORY();                    
                    }
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_CATEGORY() {
                var target = "../../../RetrieveData?TABLE=DOCTOR_CATEGORY&COND=CODE='" + document.mainForm.DOCTOR_CATEGORY_CODE.value + "' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_CATEGORY);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_CATEGORY() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    // Data not found
                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        alert("<%=labelMap.get(LabelMap.ALERT_DATA_NOT_FOUND)%>");
                        document.mainForm.DOCTOR_CATEGORY_CODE.value = "";
                        document.mainForm.DOCTOR_CATEGORY_DESCRIPTION.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_CATEGORY_DESCRIPTION.value = getXMLNodeValue(xmlDoc, "DESCRIPTION");
                }
            }
            
            function btnNew(){
            	window.location = "stp_method_doctor_category_order_category_form.jsp?DOCTOR_CATEGORY_CODE='" + $("#DOCTOR_CATEGORY_CODE").val() +"'";
            }
            
            function getAttVal($id) { 
            	if($("#" + $id).val() == "" ||  $("#" + $id).val() == undefined ){
            		 return 0;
            	} else  {
            		return $("#" + $id).val();
            	}
            }
            
            function getAttValStr($id){
            	if($("#" + $id).val() == "" ||  $("#" + $id).val() == undefined ){
           		    return "''";
	           	} else  {
	           		return "'" + $("#" + $id).val() + "'";
	           	}	
            }
        </script>
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="">
            <center>
                <table width="800" border="0">
                    <tr><td align="left">
                            <b><font color='#003399'><%=Utils.getInfoPage("stp_method_doctor_category_order_category.jsp", labelMap.getFieldLangSuffix(),  new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
                    </td></tr>
		</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>
                <tr>
                    <td class="label">
                        <label for="DOCTOR_CATEGORY_CODE">${labelMap.DOCTOR_CATEGORY_CODE}</label>
                    </td>
                    <td class="input" colspan="3">
                        <input name="DOCTOR_CATEGORY_CODE" type="text" class="short" id="DOCTOR_CATEGORY_CODE" maxlength="20" value="<%= DBMgr.getRecordValue(doctorCategoryRec, "CODE")%>" onkeypress="return DOCTOR_CATEGORY_CODE_KeyPress(event);" />
                        <input type="image" class="image_button" src="../../../images/search_button.png" alt="" onclick="openSearchForm('../search.jsp?TABLE=DOCTOR_CATEGORY&DISPLAY_FIELD=DESCRIPTION&BEINSIDEHOSPITAL=1&BEACTIVE=1&TARGET=DOCTOR_CATEGORY_CODE&HANDLE=AJAX_Refresh_DOCTOR_CATEGORY'); return false;" />
                        <input name="DOCTOR_CATEGORY_DESCRIPTION" type="text" class="long" id="DOCTOR_CATEGORY_DESCRIPTION" readonly="readonly" value="<%= DBMgr.getRecordValue(doctorCategoryRec, "DESCRIPTION")%>" maxlength="255" />                    
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">                        
                        <input type="button" id="SELECT" name="SELECT" class="button" value="${labelMap.SELECT}" onclick="window.location = 'stp_method_doctor_category_order_category.jsp?DOCTOR_CATEGORY_CODE=' + document.mainForm.DOCTOR_CATEGORY_CODE.value; return false;" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="window.location='stp_method_doctor_category_order_category.jsp'" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../../process/ProcessFlow.jsp'" />
                    </th>
                </tr>
            </table>
            <hr />
            <table class="data" id="dataTable">
                <tr>
                    <th colspan="9" class="alignLeft">${labelMap.TITLE_DATA}</th>
                </tr>
                <tr>
                	<td class="sub_head"><%=labelMap.get("ORDER_ITEM_CATEGORY_CODE")%></td>
                	<td class="sub_head"><%=labelMap.get("ADMISSION_TYPE_CODE")%></td>
                    <td class="sub_head"><%=labelMap.get("PRICE")%></td>
                    <td class="sub_head"><%=labelMap.get("NORMAL_ALLOCATE_PCT")%></td>
                    <td class="sub_head"><%=labelMap.get("NORMAL_ALLOCATE_AMT")%></td>
                     <td class="sub_head"><%=labelMap.get("IS_PROCEDURE")%></td>
                    <td class="sub_head"><%=labelMap.get(LabelMap.ACTIVE)%></td>
                    <td class="sub_head"><%=labelMap.get(LabelMap.EDIT)%></td>
                </tr>
                <%
		            DBConnection con = new DBConnection();
		            con.connectToLocal();
		            
		            String query = "SELECT * FROM STP_METHOD_ALLOCATE WHERE METHOD_SEQUENCE = '2' AND HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() +  "'";
		            
		            if(request.getParameter("DOCTOR_CATEGORY_CODE") == null){ 
		            	 query += " AND 1 < 0 ";
		            } else  { 
		            	 query += " AND DOCTOR_CATEGORY_CODE =  '" + request.getParameter("DOCTOR_CATEGORY_CODE") + "'";
		            }
		            ResultSet rs = con.executeQuery(query);
		            int i = 0;
		            String activeIcon, linkEdit;
		            while (rs != null && rs.next()) {
	                activeIcon = "<img src=\"../../../images/" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? "" : "in") + "active_icon.png\" alt=\"" + (rs.getString("ACTIVE") != null && rs.getString("ACTIVE").equalsIgnoreCase("1") ? labelMap.get(LabelMap.ACTIVE_1) : labelMap.get(LabelMap.ACTIVE_0)) + "\" />";
	                linkEdit = "stp_method_doctor_category_order_category_form.jsp?METHOD_SEQUENCE="+rs.getString("METHOD_SEQUENCE")+"&DOCTOR_CATEGORY_CODE='"+rs.getString("DOCTOR_CATEGORY_CODE")+"'&DOCTOR_CODE='"+rs.getString("DOCTOR_CODE")+"'&ORDER_ITEM_CATEGORY_CODE='"+rs.getString("ORDER_ITEM_CATEGORY_CODE")+"'&ORDER_ITEM_CODE='"+rs.getString("ORDER_ITEM_CODE")+"'&PAYOR_OFFICE_CATEGORY_CODE='"+rs.getString("PAYOR_CATEGORY_CODE")+"'&PAYOR_CODE='"+rs.getString("PAYOR_CODE")+"'&ADMISSION_TYPE_CODE='"+rs.getString("ADMISSION_TYPE_CODE")+"'&DOCTOR_TREATMENT_CODE='"+rs.getString("DOCTOR_TREATMENT_CODE")+"'&IS_PROCEDURE='"+rs.getString("IS_PROCEDURE")+"'&PRIVATE_DOCTOR='"+rs.getString("PRIVATE_DOCTOR")+"'&AMOUNT_START="+rs.getString("AMOUNT_START")+"&AMOUNT_END="+rs.getString("AMOUNT_END")+"&NORMAL_ALLOCATE_PCT="+rs.getString("NORMAL_ALLOCATE_PCT")+"&NORMAL_ALLOCATE_AMT="+rs.getString("NORMAL_ALLOCATE_AMT")+"";
                %>                
                <tr>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("ORDER_ITEM_CATEGORY_CODE"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("ADMISSION_TYPE_CODE"), true)%></td>
                    <td class="row<%=i % 2%> alignRight"><%= Util.formatHTMLString(rs.getString("AMOUNT_START"), true)%></td>
                    <td class="row<%=i % 2%> alignRight"><%= Util.formatHTMLString(rs.getString("NORMAL_ALLOCATE_PCT"), true)%></td>
                    <td class="row<%=i % 2%> alignRight"><%= Util.formatHTMLString(rs.getString("NORMAL_ALLOCATE_AMT"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= Util.formatHTMLString(rs.getString("IS_PROCEDURE"), true)%></td>
                    <td class="row<%=i % 2%> alignCenter"><%= activeIcon %></td>
                    <td class="row<%=i % 2%> alignCenter">
                    	<a href="<%=linkEdit%>">
                    		<img src="../../../images/edit_button.png" alt="<%=labelMap.get(LabelMap.EDIT)%>" />
                    	</a>
                    </td>
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
                    <th colspan="9" class="buttonBar">                        
                        <input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}" onclick="btnNew()" />
                    </th>
                </tr>
            </table>
        </form>
    </body>
</html>
<%=arr%>