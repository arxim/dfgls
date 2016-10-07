<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>

<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.DataRecord"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.obj.util.Utils "%>

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
            labelMap.add("TITLE_MAIN", "Setup Df Tranfer", "โอนรายได้แพทย์");
            labelMap.add("DOCTOR_FROM", "Doctor From", "Doctor From");
            labelMap.add("DOCTOR_TO", "Doctor To", "Doctor To");
            labelMap.add("ADMISSION_TYPE", "Admission type", "Admission type");
            request.setAttribute("labelMap", labelMap.getHashMap());           
            request.setCharacterEncoding("UTF-8");
          
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
</head>    
    <body>
        <form id="mainForm" name="mainForm" method="post" action="#">
           <center>
                <table width="800" border="0">
                    <tr>
	                    <td align="left">
	                        <b><font color='#003399'><%=Utils.getInfoPage("stp_df_tranfer_add.jsp", labelMap.getFieldLangSuffix(), new DBConnection(""+session.getAttribute("HOSPITAL_CODE")))%></font></b>
	                    </td>
                   </tr>
			</table>
            </center>
            <table class="form">
                <tr>
                    <th colspan="4">
				  		<div style="float: left;">${labelMap.TITLE_MAIN}</div>
					</th>
                </tr>
                <tr>
                    <td class="label"><label for="CODE"><span class="style1">${labelMap.DOCTOR_FROM}*</span></label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="doctorFrom" name="doctorFrom" class="short" value="" onkeypress="return DOCTOR_CODE_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR();" />
	                    <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="doctorOnSearchDoctorFrom(); return false;" />
	                    <input type="text" id="DOCTOR_NAME_SEARCH" name="DOCTOR_NAME_SEARCH" class="mediumMax" readonly="readonly" value="" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION"><span class="style1">${labelMap.DOCTOR_TO}*</span></label></td>
                    <td colspan="3" class="input">
                         <input type="text" id="doctorTo" name="doctorTo" class="short" value="" onkeypress="return DOCTOR_CODE_TO_KeyPress(event);" onblur="AJAX_Refresh_DOCTOR_TO();" />
	                    <input id="SEARCH_DOCTOR_CODE" name="SEARCH_DOCTOR_CODE" type="image" class="image_button" src="../../images/search_button.png" alt="Search" onclick="doctorOnSearchDoctorTo(); return false;" />
	                    <input type="text" id="DOCTOR_NAME_SEARCH_TO" name="DOCTOR_NAME_SEARCH_TO" class="mediumMax" readonly="readonly" value="" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="HOSPITAL_UNIT_CODE"><span class="style2">${labelMap.ADMISSION_TYPE}</span></label></td>
                    <td colspan="3" class="input">
                     	
                     	<select id="admissionType" name="admissionType">
<!--                      	  	<option value="ALL">ALL OPD/IPD</option> -->
                     	  	<option value="O">OPD</option>
                     	  	<option value="I">IPD</option>
                     	</select>
                     </td>
                </tr>
              
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" onclick="return resetData()" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" />
                    </th>
                </tr>
            </table>
        </form>
        
        
        <script type="text/javascript" src="../../javascript/jquery-1.6.2.min.js"></script>
        <script type="text/javascript" src="../../javascript/data_table.js"></script>
        <script type="text/javascript" src="../../javascript/ajax.js"></script>
        <script type="text/javascript" src="../../javascript/util.js"></script>
        <script type="text/javascript" src="../../javascript/search_form.js"></script>
        
        <script type="text/javascript">
	    
        	$(function(){ 

        		 $("#CLOSE").click(function(){ 
        			 window.history.back();
        		 });
        		 
        		 $("#SAVE").click(function() { 
        			 
        			  	if( $("#doctorFrom").val()  == "")
        			  	{
        			  		 alert("Please Enter Doctor Code.");
        			  		 $("#doctorFrom").focus();
        			  		 return false;
        			  	}
        			  	
        			  	if($("#doctorTo").val() == "") 
        			  	{
        			  		alert("Please Enter Doctor Code.");
        			  		$("#doctorTo").focus();
        			  		return false;
        			  	}
        			 
        			   $.ajax({
                           url: '../../ProcessDfTranferRevenueController',
                           type: 'POST',
                           data: {
                        	   ControllerAction :  "actionAdd" , 
                        	   doctorFrom : $("#doctorFrom").val() , 
                           	   doctorTo : $("#doctorTo").val() ,  
                           	   doctorFromName : $("#DOCTOR_NAME_SEARCH").val() , 
                           	   doctorToName : $("#DOCTOR_NAME_SEARCH_TO").val(), 
                           	   admissionType :  $("#admissionType option:selected").val()
                           }, 
                           success: function(data) {
	                          	if(data.status == "SUCCESS") {
	                          		window.location = "stp_df_tranfer.jsp";
	                		    } else {
                          			alert("Can't save data. Please try again.");
	                		    }
                           } , 
                           error: function (){ 
                          	 $(this).ajaxSuccess(function(){
            		         	alert("เกิดข้อผิดพลาด กรุณาตรวจสอบข้อมูลอีกครั้ง");
                          	 });
                           }
                       });
          		 	});
        	
    	        function resetData(){
    	        	
    	        }
    	        
        	});

        	function DOCTOR_CODE_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.doctorFrom.blur();
                    return false;
                } else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR() {
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.doctorFrom.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR);
            }
            
            function AJAX_Handle_Refresh_DOCTOR() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.doctorFrom.value = "";
                        document.mainForm.DOCTOR_NAME_SEARCH.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_NAME_SEARCH.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }   

            function doctorOnSearchDoctorFrom(){
        		openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=doctorFrom&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR');	
        	}
            
            function DOCTOR_CODE_TO_KeyPress(e) {
                var key = window.event ? window.event.keyCode : e.which;    // ? IE : Firefox

                if (key == 13) {
                    document.mainForm.doctorTo.blur();
                    return false;
                }
                else {
                    return true;
                }
            }

            function AJAX_Refresh_DOCTOR_TO() {
                var target = "../../RetrieveData?TABLE=DOCTOR&COND=CODE='" + document.mainForm.doctorTo.value +"' AND HOSPITAL_CODE='<%=session.getAttribute("HOSPITAL_CODE")%>'";
                AJAX_Request(target, AJAX_Handle_Refresh_DOCTOR_TO);
            }
            
            function AJAX_Handle_Refresh_DOCTOR_TO() {
                if (AJAX_IsComplete()) {
                    var xmlDoc = AJAX.responseXML;

                    if (!isXMLNodeExist(xmlDoc, "CODE")) {
                        document.mainForm.doctorTo.value = "";
                        document.mainForm.DOCTOR_NAME_SEARCH_TO.value = "";
                        return;
                    }
                    
                    // Data found
                    document.mainForm.DOCTOR_NAME_SEARCH_TO.value = getXMLNodeValue(xmlDoc, "NAME_" + "<%= labelMap.getFieldLangSuffix() %>");
                }
            }    
            
            function doctorOnSearchDoctorTo(){
        		openSearchForm('../search.jsp?TABLE=DOCTOR&DISPLAY_FIELD=NAME_<%= labelMap.getFieldLangSuffix() %>&TARGET=doctorTo&BEINSIDEHOSPITAL=1&BEACTIVE=1&HANDLE=AJAX_Refresh_DOCTOR_TO');	
        	}
            
        </script>
         
    </body>
</html>