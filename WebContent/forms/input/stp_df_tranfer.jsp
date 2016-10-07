<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.*"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.obj.util.Utils"%>
<%@page import="df.bean.db.table.Batch"%>
<%@ include file="../../_global.jsp" %>

<%
	//
	// Verify permission
	//

	//if (!Guard.checkPermission(session,
	//		Guard.PAGE_INPUT_METHOD_ALLOC_ITEM_MAIN)) {
	//	response.sendRedirect("../message.jsp");
	//	return;
	//}

	//
	// Initial LabelMap
	//

	if (session.getAttribute("LANG_CODE") == null) {
		session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
	}
	
	LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
	
	labelMap.add("TITLE_MAIN", "Set Up DF Tranfer revenues","กำหนดการโยกรายได้รายได้แพทย์");
	
	labelMap.add("TITLE_DATA" , "Data detail setup." , "รายละเอียดการกำหนดเงือนไข");
	
	request.setAttribute("labelMap", labelMap.getHashMap());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
<link type="text/css" href="../../css/themes/custom-theme/jquery.ui.all.css" rel="stylesheet" media="screen"/>
<link rel="stylesheet" type="text/css" href="../../tablejTP/jTPS.css"/>
<style type="text/css">
	
  				body {
  					font-family: Tahoma;
                    font-size: 9pt;
                    background-color: #dde4e8;
                }
                
                #demoTable thead th {
                        white-space: nowrap;
                        overflow-x:hidden;
                        padding: 3px;
                }
                
                #demoTable tbody td {
                        padding: 3px;
                }
                
                #demoTable{
                	 background-color: #fff; 
                }
				                
				table.data, table.form {
				    border: 1px solid #999;
				    background-color: #dde4e8;
				    /*background-color: #eee;*/
				    margin: 0px auto;
				    width: 800px;
				}
				
				table.data th {
				    background-color: #666;
				    color: #fff;
				    padding: 10px;
				    font-size: 110%;
				}
				
				table.data td.sub_head {
				    background-color: #888;
				    color: #eee;
				    padding: 10px;
				    font-size: 105%;
				    font-weight: bold;
				    text-align: center;
				}
				
				table.data td, table.data td.row0 {
				    background-color: #eee;
				    color: 444;
				    border: 1px solid #d5d5d5;
				    padding: 5px 10px;
				}
				
				table.data td.row1 {
				    background-color: #e5e5e5;
				    color: 333;
				    border: 1px solid #cecece;
				}
				
				table.data_free_width, table.form {
				    border: 1px solid #999;
				    background-color: #dde4e8;
				    margin: 0px auto;
				}
				
				table.data_free_width th {
				    background-color: #666;
				    color: #fff;
				    padding: 10px;
				    font-size: 110%;
				}
				
				table.data_free_width td.sub_head {
				    background-color: #888;
				    color: #eee;
				    padding: 10px;
				    font-size: 105%;
				    font-weight: bold;
				    text-align: center;
				}
				
				table.data_free_width td, table.data td.row0 {
				    background-color: #eee;
				    color: 444;
				    border: 1px solid #d5d5d5;
				    padding: 5px 10px;
				}
				
				table.data_free_width td.row1 {
				    background-color: #e5e5e5;
				    color: 333;
				    border: 1px solid #cecece;
				}
				
				table.form {
				    width: 800px;
				}
				
				table.form th  , table.form thead tr  ,  table.form tfoot tr {
				    background-color: #666;
				    color: #fff;
				    padding: 10px;
				    text-align: left;
				    font-weight: bold;
				}
				
				table.form th.buttonBar, table.data th.buttonBar {
				    padding: 5px;
				    text-align: right;
				}
				
				table.form td {
				    padding: 5px 10px;
				}
				
				table.form td.label , table.form tbody tr td {
				    background-color: #ccc;
				    color: #333;
				    border: 1px solid #b8b8b8;
				    text-align: right;
				    padding: 2px 8px 2px 5px;
				    width: 170px;
				}
				
				table.form td.labelRequest {
				    background-color: #ccc;
				    color:#003399;
				    border: 1px solid #b8b8b8;
				    text-align: right;
				    padding: 2px 8px 2px 5px;
				    width: 170px;
				}
				
				table.form td.input ,  table.form tbody tr td  {
				    background-color: #eee;
				    color: 444;
				    border: 1px solid #c8c8c8;
				}
				
				table.form label {
				    font-weight: bold;
				}
</style>
<title>
	${labelMap.TITLE_MAIN}
</title>
</head>
<body>
	 	<div class="blockMe">
        <center>
		   <table class="form" width="800px;">
		   	  <thead>
		   	  	 <tr>
		   	  	 	 <td colspan="2">
		   	  	 	  This Process Change Doctor Code.
		   	  	 	</td>
		   	  	 </tr>
		   	  </thead>
		   	  <tbody>
		   	  	  	<tr>
		   	  	  	  <td width="300px"> 
		   	  	  		  form date : 
		   	  	  	  </td>
		   	  	  	  <td style="text-align: left;">
		   	  	  	  	  <input type="text" name="startDate" id="startDate"/>
		   	  	  	  </td>
		   	  	  	</tr>
		   	  	  	<tr>
		   	  	  	  <td>
		   	  	  		  form To : 
		   	  	  	  </td>
		   	  	  	  <td  style="text-align: left;">
		   	  	  	  	  <input type="text" name="endDate" id="endDate"/>
		   	  	  	  </td>
		   	  	  	</tr>
		   	  </tbody>
		   	  <tfoot>
		   	  	<tr>
		   	  		<td colspan="2" style="text-align: right;">
		   	  			 <button type="button" name="btnProcess" id="btnProcess"> Process </button>
		   	  			 <button type="button" name="btnCancel" id="btnCancel" >Cancel</button>
		   	  			 <button type="button" name="btnInfo" id="btnInfo">Info</button>
		   	  		</td>
		   	  	</tr>
		   	  </tfoot>
		   </table>
 	 		
             <hr />
             
             <table style="background-color: #666666; width: 800px;">
                <thead>
                	 <tr>
                	 	 <td style="margin: 0px; padding: 0px;">
                	 	 	
                	 	 	<table style="width: 100%; padding: 0px; margin: 0px; font-weight: bold;">
                	 	 		<tr>	
                	 	 			<td>
                	 	 				${labelMap.TITLE_MAIN}
                	 	 			</td>
                	 	 			<td style="text-align: right;">
                	 	 				 <input type="text" name="keySearch"  id="keySearch" value="${param.keySearch}"/>
                	 	 				 <button id="btnSearch"  name="btnSearch"> Search </button>
	                	 	 			 <input type="button" id="NEW" name="NEW" class="button" value="${labelMap.NEW}" />
		                        		 <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />
                	 	 			</td>
                	 	 		</tr>
                	 	 	</table>
                	 	 </td>
                	 </tr>
                </thead>
                
            </table>
            <table class="data" id="demoTable" style="border: 1px solid #ccc;" cellspacing="0" width="800">
                <thead>
	                <tr>
	                    <td sort="index" align="center" style="background-color: #888888;"><strong>INDEX</strong></td>
	                    <td sort="doctor_from" style="background-color: #888888;"><strong>DOCTOR_FROM</strong></td>
	                    <td sort="doctor" style="background-color: #888888;"><strong>DOCTOR_TO</strong></td>
	                    <td sort="admission" align="center" style="background-color: #888888;"><strong>OPD/IPD</strong></td>
	                    <td sort="action" align="center" style="background-color: #888888;"><strong>ACTION</strong></td>
	                </tr>
                </thead>
                <tbody style="background-color: #fff;">
                 <%
		            DBConnection con = new DBConnection();
		            con.connectToLocal();
		            String strCondition = "";
		            
		            try{ 
			            if(request.getParameter("keySearch") != null || !request.getParameter("keySearch").equals("")) { 
			            	strCondition = " AND ( DOCTOR_FROM LIKE '%" + request.getParameter("keySearch").toString()  + "%'  OR  DOCTOR_TO LIKE '%" + request.getParameter("keySearch").toString()  + "%')";
			            } 
		            } catch (Exception ex){ 
		            	
		            }
		            
		            String query = "SELECT * FROM  STP_TRANFER_DF  WHERE " + 
		            			   " HOSPITAL_CODE = '" + session.getAttribute("HOSPITAL_CODE").toString() + "'"+
		            			    strCondition +  
		            				" ORDER BY DOCTOR_FROM";
		            
		            ResultSet rs = con.executeQuery(query);
		            int i = 1;
		            String activeIcon, linkEdit;
		            while (rs != null && rs.next()) {
	            %>                
                <tr>
                    <td align="center"><%=i%></td>
                    <td align="left">	
                    	<%=rs.getString("DOCTOR_FROM") %>  ( 
                    	<%=rs.getString("DOCTOR_FROM_NAME") %> )
                    </td>
                    <td align="left">
                    	<%=rs.getString("DOCTOR_TO") %> (
                    	<%=rs.getString("DOCTOR_TO_NAME") %> )
                    </td>
                    <td align="center">
                    	<%
	                    	if(rs.getString("ADMISSION_TYPE").equals("ALL")){ 
	                    			out.print("ALL OPD/IPD");
	                    	} else if(rs.getString("ADMISSION_TYPE").equals("O")){ 
	                    		 	out.print("OPD");
	                    	} else if(rs.getString("ADMISSION_TYPE").equals("I")){ 
	                    			out.print("IPD");	
	                    	}
                    	%>
                    	</td>
                    <td align="center"> 
                    	<a href="#" onclick="actionEdite('<%=rs.getString("DOCTOR_FROM").toString().trim() %>' , '<%=rs.getString("DOCTOR_TO").toString().trim()%>' ,  '<%=rs.getString("ADMISSION_TYPE").toString().trim() %>')">Edite</a> / 
                    	<a href="#" onclick="actionDelete('<%=rs.getString("DOCTOR_FROM").toString().trim() %>' , '<%=rs.getString("DOCTOR_TO").toString().trim()%>' ,  '<%=rs.getString("ADMISSION_TYPE").toString().trim() %>')">Del</a>
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
                </tbody>
                 <tfoot class="nav" style="font-weight: bold;">
	                <tr>
	                        <td colspan=7 style="background-color: #888888;">
	                                <div class="pagination"></div>
	                                <div class="paginationTitle">Page</div>
	                                <div class="selectPerPage"></div>
	                                <div class="status"></div>
	                        </td>
	                </tr>
        	</tfoot>     
            </table>
            </center>
        </div>
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/tablejTP/jquery10.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/data_table.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery-1.6.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/jquery-ui-1.8.16.custom.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/javascript/calendar.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/tablejTP/jTPS.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/tablejTP/jquery.blockUI.js" ></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/tablejTP/jquery-ui.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/tablejTP/jquery_002.js" ></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/tablejTP/urchin.js"></script>

	<script type="text/javascript">
	
        $(document).ready(function () {
        
        	// Event Key 
        	
        	$("#startDate").datepicker({
				changeMonth: true,
				changeYear: true,
				buttonImageOnly: true,
				dateFormat: 'dd/mm/yy'
			}); 
		    
			$("#endDate").datepicker({
				changeMonth: true,
				changeYear: true,
				buttonImageOnly: true,
				dateFormat: 'dd/mm/yy'
			});				
			
			$("#btnInfo").click(function(){
					 alert("ระบบจะไม่ทำงาน  กรณีที่คำนวนส่วนแบ่งเบื้องต้นแล้ว");
					 return false;
			});
				  
		        	$('#demoTable').jTPS( {perPages:[5,12,15,50,'ALL'],scrollStep:1,scrollDelay:30,
			        		  clickCallback:function () {     
			                           // target table selector
			                           var table = '#demoTable';
			                           // store pagination + sort in cookie 
			                           document.cookie = 'jTPS=sortasc:' + $(table + ' .sortableHeader').index($(table + ' .sortAsc')) + ',' +
			                                   'sortdesc:' + $(table + ' .sortableHeader').index($(table + ' .sortDesc')) + ',' +
			                                   'page:' + $(table + ' .pageSelector').index($(table + ' .hilightPageSelector')) + ';';
			                   }
			        
		        	});

		           // reinstate sort and pagination if cookie exists
		           var cookies = document.cookie.split(';');
		           for (var ci = 0, cie = cookies.length; ci < cie; ci++) {
		                   var cookie = cookies[ci].split('=');
		                   if (cookie[0] == 'jTPS') {
		                           var commands = cookie[1].split(',');
		                           for (var cm = 0, cme = commands.length; cm < cme; cm++) {
		                                   var command = commands[cm].split(':');
		                                   if (command[0] == 'sortasc' && parseInt(command[1]) >= 0) {
		                                           $('#demoTable .sortableHeader:eq(' + parseInt(command[1]) + ')').click();
		                                   } else if (command[0] == 'sortdesc' && parseInt(command[1]) >= 0) {
		                                           $('#demoTable .sortableHeader:eq(' + parseInt(command[1]) + ')').click().click();
		                                   } else if (command[0] == 'page' && parseInt(command[1]) >= 0) {
		                                           $('#demoTable .pageSelector:eq(' + parseInt(command[1]) + ')').click();
		                                   }
		                           }
		                   }
		           }

		           // bind mouseover for each tbody row and change cell (td) hover style
		           $('#demoTable tbody tr:not(.stubCell)').bind('mouseover mouseout',
		                   function (e) {
		                           // hilight the row
		                           e.type == 'mouseover' ? $(this).children('td').addClass('hilightRow') : $(this).children('td').removeClass('hilightRow');
		                   }
		           );
		           
		           $("#keySearch").keypress(function(e){
		        	  
		           		if(e.which == '13')
		           		{
		           			var keySearch  =  $("#keySearch").val();
				            window.location = "stp_df_tranfer.jsp?keySearch="+ keySearch ;
				            $("#keySearch").focus();
		           		}
		           		
		           });
		           
		           $("#btnSearch").click(function(){
		        	   var keySearch  =  $("#keySearch").val();
		        	   window.location = "stp_df_tranfer.jsp?keySearch="+ keySearch ;
			       });
	
		           $("#NEW").click(function(){ 
		        		window.location = "stp_df_tranfer_add.jsp";
		           });
	 
		           // Button Action Process 
		           $("#btnProcess").click(function(){
		        	   
		        	    if($("#startDate").val() == ""){ 
		        	    	alert("Plese enter date start.!!!");
		        	    	$("#startDate").focus();
		        	    	return false;
		        	    }
		        	    
		        	    if($("#endDate").val() == ""){ 
		        	    	alert("Plese enter date end.!!!");
		        	    	$("#entDate").focus();
		        	    	return false;
		        	    }
		        	    
		        	    // Block Modal 
		        	    $('div.blockMe').block({ 
		                     message: '<br/><img  src="../../images/processing_icon.gif" width="20" height="20"/> <h1>Processing</h1>', 
		                     css: { border: '3px solid #a00' } 
		                }); 
		        	    
		        	    $.ajax({
			                 url: '../../ProcessDfTranferRevenueController',
			                 type: 'POST',
			                 data: {
			   	           	   		ControllerAction :  "actionProcess" , 
			   		           	    startDate : $("#startDate").val(), 
			   	              	    endDate : $("#endDate").val() 
			   	              }, 
			   	              success: function(data) {
			   	                 if(data.status == "SUCCESS") {
				   	                $('div.blockMe').unblock(); 
						         } else {
			   	             		alert("Can't save data. Please try again.");
			   	       		     }
			   	              } , 
			   	              error: function (){ 
			   	             	 	alert("เกิดข้อผิดพลาด กรุณาตรวจสอบข้อมูลอีกครั้ง");
			   	              }
			   	          });
		           });
        });
        
       function actionEdite( doctorFrom , doctorTo  , admisstionType ) {
     	  window.location.href = "../../ProcessDfTranferRevenueController?ControllerAction=actionEdite&doctorFrom=" + doctorFrom + "&doctorTo=" + doctorTo +  "&admissionType=" + admisstionType;
       }
        
       function actionDelete(  doctorFrom , doctorTo  , admisstionType ){
    	  
    		if(confirm("Delete data ?")){
	    		 
    			$.ajax({
	                 url: '../../ProcessDfTranferRevenueController',
	                 type: 'POST',
	                 data: {
	   	           	   		ControllerAction :  "actionDelete" , 
	   		           	    doctorFrom : doctorFrom , 
	   	              	    doctorTo : doctorTo ,  
	   	              	    admissionType :  admisstionType
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
	    	} 
    	}
    </script>
	</body>
</html>