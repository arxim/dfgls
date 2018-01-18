<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>
<%@page import="java.sql.*"%>
<%@page import="df.jsp.Guard"%>
<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.table.TRN_Error"%>
<%@page import="df.bean.obj.util.JDate"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.bean.process.ProcessUtil"%>
<%@ include file="../../_global.jsp" %>

<%
            //
            // Verify permission
            //

            if (!Guard.checkPermission(session, Guard.PAGE_PROCESS_DEMO)) {
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
            DBConnection db = new DBConnection();
            db.connectToLocal();
            Batch batch = new Batch(session.getAttribute("HOSPITAL_CODE").toString(),db);
            db.Close();
            
            String startDateStr01 = JDate.showDate(JDate.getDate());
            String startDateStr02 = JDate.showDate(JDate.getDate());
            String startDateStr03 = JDate.showDate(JDate.getDate());
            String startDateStr04 = JDate.showDate(JDate.getDate());
            String startDateStr05 = JDate.showDate(JDate.getDate());
            String startDateStr07 = JDate.showDate(JDate.getDate());
            String startDateStr08 = JDate.showDate(JDate.getDate());//Receipt Transaction
            
            String endDateStr01 = JDate.showDate(JDate.getDate());
            String endDateStr02 = JDate.showDate(JDate.getDate());
            String endDateStr03 = JDate.showDate(JDate.getDate());
            String endDateStr04 = JDate.showDate(JDate.getDate());
            String endDateStr05 = JDate.showDate(JDate.getDate());
            String endDateStr08 = JDate.showDate(JDate.getDate());//Receipt Transaction         
                                             
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "RollBack", "ระบบ RollBack");
            labelMap.add("PAY_DATE", "Pay Date", "วันที่จ่ายเงิน");
            labelMap.add("COL_0", "  ", "  ");
            labelMap.add("COL_1", "Process Name", "ชื่อโปรเซส");
            labelMap.add("COL_2", "Status", "สถานะ");
            labelMap.add("COL_3", "", "");
            labelMap.add("COL_4", "  ", "  ");

            labelMap.add("PROCESS_00", "Interface Discharge Summary" , "นำเข้าไฟล์  Discharge");
            labelMap.add("PROCESS_01", "Interface DF Transaction", "นำเข้าไฟล์ค่าแพทย์");
            labelMap.add("PROCESS_02", "Interface DF Result", "นำเข้าไฟล์อ่านผล");
            labelMap.add("PROCESS_03", "Interface AR Transaction", "นำเข้าไฟล์รับชำระ");
            labelMap.add("PROCESS_04", "Import Transaction", "นำเข้ารายการค่าแพทย์");
            labelMap.add("PROCESS_05", "Daily Calculate", "คำนวณรายวัน");
            labelMap.add("PROCESS_06", "Receipt Process", "ขั้นตอนการทำรับชำระ");
            labelMap.add("PROCESS_06_01", "Cash", "เงินสด");
            labelMap.add("PROCESS_06_02", "Receipt Transaction", "รับชำระหนี้");
            labelMap.add("PROCESS_06_03", "Receipt By Payor", "ทำจ่ายด้วยเงื่อนไขคู่สัญญา");
            labelMap.add("PROCESS_06_04", "Receipt By Doctor", "ทำจ่ายด้วยเงื่อนไขแพทย์");
            labelMap.add("PROCESS_06_05", "Guarantee Process", "ขั้นตอนการันตี");
            labelMap.add("PROCESS_06_06", "Tax 40(2) Process", "ขั้นตอนคำนวณภาษี 40(2)");
            labelMap.add("PROCESS_07", "Monthly Calculate", "คำนวนรายเดือน");
            labelMap.add("PROCESS_08", "DF Payment", "คำนวณจ่ายแพทย์");
            labelMap.add("PROCESS_09", "Export to Bank", "นำเข้าข้อมูลเตรียมส่งแบงค์");
            labelMap.add("PROCESS_10", "Summary Tax 40(6)", "คำนวณภาษี 40(6)");
            labelMap.add("PROCESS_11", "Payment Salary Monthly", "คำนวณเงินเดือนแพทย์");
            labelMap.add("PROCESS_12", "Daily Process", "ขั้นตอนการทำงานรายวัน");
            labelMap.add("PROCESS_13", "Interface Import", "ขั้นตอนการนำเข้าข้อมูลรายวัน");
            labelMap.add("PROCESS_14", "Monthly Process", "ขั้นตอนการทำงานรายเดือน");
			labelMap.add("PROCESS_15", "Write Off", "ขั้นตอนตัดหนี้สูญ");
            labelMap.add("PROCESS_16", "Holiday Calculate", "Holiday Calculate");
            labelMap.add("PROCESS_17", "Discharge Calculate", "Discharge Calculate");
            labelMap.add("PAYMENT_DATE", "Payment Date", "วันที่จ่ายแพทย์");
            
            labelMap.add("VALUEBUTTONPROCESS","Process","Process");
            labelMap.add("NAMEBUTTONPROCESS","Process","คำนวณ");
            
            labelMap.add("MM", "Month", "เดือน");
            labelMap.add("YYYY", "Year", "ปี");
            
            request.setAttribute("labelMap", labelMap.getHashMap());
            
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
    </head>
    <body>
        <form id="mainForm" name="mainForm" method="post" action="ProcessPaymentMonthly.jsp">
            <table class="data" id="dataTable" name="dataTable">
                <tr>
                    <th colspan="5" class="alignLeft">
                        <div style="float: left;">${labelMap.TITLE_MAIN}</div>
                        <div style="float: right;" id="PROGRESS" name="PROGRESS"></div>                    </th>
                </tr>
                <tr>
                    <td class="sub_head"><%=labelMap.get("COL_0")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_1")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_4")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_3")%></td>
                    <td class="sub_head"><%=labelMap.get("COL_2")%></td>
                </tr>
				<tr>
                    <td class="row1 alignLeft" colspan="5">
                        <b><%=labelMap.get("PROCESS_14")%></b>                    </td>
                </tr>
                <tr>
                    <td class="row0 alignCenter"><div id="ImgPro10"><img src="../../images/begin_rollback.png" ></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_10")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>    
                                    <label for="aText">${labelMap.MM}</label>                                </td>                                
                                <td>
                                    <%=proUtil.selectMM406 ("", "MM10",batch.getMm())%>
                                <td>
                                    <label>${labelMap.YYYY}</label>                                </td>  
                                <td>
                                    <%=proUtil.selectYY("YY10", batch.getYyyy())%>                                </td>                                
                            </tr>
                        </table>                    </td>                     
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processMMYY('10')"></td>
                    <td class="row0 alignLeft"><div id="countPro10">..</div></td>
                </tr>
                <!-- 
				<tr>
                    <td class="row0 alignCenter"><div id="ImgPro09"><img src="../../images/begin_rollback.png" ></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_09")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>    
                                    <label for="aText">${labelMap.MM}</label>                                </td>                                
                                <td>
                                    <%=proUtil.selectMM("", "MM09",batch.getMm())%>                                </td>
                                <td>
                                    <label>${labelMap.YYYY}</label>                                </td>  
                                <td>
                                    <%=proUtil.selectYY("YY09", batch.getYyyy())%>                                </td>                                
                            </tr>
                        </table>
                    </td>                    
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processMMYY('09')"></td>
                    <td class="row0 alignLeft"><div id="countPro09">..</div></td>
                </tr>
				<tr>
                    <td class="row0 alignCenter"><div id="ImgPro11"><img src="../../images/begin_rollback.png"></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_11")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>    
                                    <label for="aText">${labelMap.MM}</label>                                </td>                                
                                <td>
                                    <%=proUtil.selectMM("", "MM11",batch.getMm())%>                                </td>
                                <td>
                                    <label>${labelMap.YYYY}</label>                                </td>  
                                <td>
                                    <%=proUtil.selectYY("YY11", batch.getYyyy())%>                                </td>                                
                            </tr>
                        </table>                    </td> 
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processMMYY('11')"></td>
                    <td class="row0 alignLeft"><div id="countPro11">..</div></td>
                </tr>
                <tr>
                    <td class="row0 alignCenter"><div id="ImgPro08"><img src="../../images/begin_rollback.png" ></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_08")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>    
                                    <label for="aText">${labelMap.MM}</label>                                </td>                                
                                <td>
                                    <%=proUtil.selectMM("", "MM08",batch.getMm())%>                                </td>
                                <td>
                                    <label>${labelMap.YYYY}</label>                                </td>  
                                <td>
                                    <%=proUtil.selectYY("YY08", batch.getYyyy())%>                                </td>                                
                            </tr>
                        </table>                    </td>                     
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processMMYY('08')"></td>
                    <td class="row0 alignLeft"><div id="countPro08">..</div></td>
                </tr>
                -->
                <tr>
                    <td class="row0 alignCenter"><div id="ImgPro06_06"><img src="../../images/begin_rollback.png" ></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_06_06")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>    
                                    <label for="aText">${labelMap.MM}</label>                                </td>                                
                                <td>
                                    <%=proUtil.selectMM("", "MM06_06",batch.getMm())%>                                </td>
                                <td>
                                    <label>${labelMap.YYYY}</label>                                </td>  
                                <td>
                                    <%=proUtil.selectYY("YY06_06", batch.getYyyy())%>                                </td>                                
                            </tr>
                        </table>                    </td>                
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processMMYY('06_06')" ></td>
                    <td class="row0 alignLeft"><div id="countPro06_06">..</div></td>
                </tr>
                <tr>
                    <td class="row0 alignCenter"><div id="ImgPro07"><img src="../../images/begin_rollback.png" ></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_07")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                             	<td>    
                                    <label for="aText">${labelMap.PAYMENT_DATE}</label>
                                </td> 
                                <td>
                                    <input type="text" value="<%=startDateStr07%>" id="START_DATE07" name="START_DATE07" class="short" value="<%=request.getParameter("START_DATE07") == null ? "" : request.getParameter("START_DATE07")%>" />
                                    <input name="image1" type="image" class="image_button" onclick="displayDatePicker('START_DATE07'); return false;" src="../../images/calendar_button.png" alt="" />                                
                                    <input type="hidden" value="<%=startDateStr07%>" id="END_DATE07" name="END_DATE07" value="<%=request.getParameter("START_DATE07") == null ? "" : request.getParameter("START_DATE07")%>" />
								</td>
                            </tr>
                        </table>
                    </td>
                    <td class="row1 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="process('07')"></td>
                    <td class="row0 alignLeft"><div id="countPro07">..</div></td>                    
                </tr>
                <!-- 
				<tr>
                    <td class="row0 alignCenter"><div id="ImgPro07"><img src="../../images/begin_rollback.png" ></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_07")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>    
                                    <label for="aText">${labelMap.MM}</label>                                </td>                                
                                <td>
                                    <%=proUtil.selectMM("", "MM07",batch.getMm())%>                                </td>
                                <td>
                                    <label>${labelMap.YYYY}</label>                                </td>  
                                <td>
                                    <%=proUtil.selectYY("YY07", batch.getYyyy())%>                                </td>                                
                            </tr>
                        </table>                    </td>                     
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processMMYY('07')"></td>
                    <td class="row0 alignLeft"><div id="countPro07">..</div></td>
                </tr>
                -->
                <tr>
                    <td class="row0 alignCenter"><div id="ImgPro17"><img src="../../images/begin_rollback.png" ></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_17")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>    
                                    <label for="aText">${labelMap.MM}</label>                                </td>                                
                                <td>
                                    <%=proUtil.selectMM("", "MM17",batch.getMm())%>                                </td>
                                <td>
                                    <label>${labelMap.YYYY}</label>                                </td>  
                                <td>
                                    <%=proUtil.selectYY("YY17", batch.getYyyy())%>                                </td>                                
                            </tr>
                        </table>                    </td>                     
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processMMYY('17')"></td>
                    <td class="row0 alignLeft"><div id="countPro17">..</div></td>
                </tr>
				<tr>
                    <td class="row0 alignCenter"><div id="ImgPro06_05"><img src="../../images/begin_rollback.png" ></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_06_05")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>    
                                    <label for="aText">${labelMap.MM}</label>                                </td>                                
                                <td>
                                    <%=proUtil.selectMM("", "MM06_05",batch.getMm())%>                                </td>
                                <td>
                                    <label>${labelMap.YYYY}</label>                                </td>  
                                <td>
                                    <%=proUtil.selectYY("YY06_05", batch.getYyyy())%>                                </td>                                
                            </tr>
                        </table>                    </td>                
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processMMYY('06_05')" ></td>
                    <td class="row0 alignLeft"><div id="countPro06_05">..</div></td>
                </tr>
                <tr>
                    <td class="row0 alignCenter"><div id="ImgPro16"><img src="../../images/begin_rollback.png" ></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_16")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>    
                                    <label for="aText">${labelMap.MM}</label>                                </td>                                
                                <td>
                                    <%=proUtil.selectMM("", "MM16",batch.getMm())%>                                </td>
                                <td>
                                    <label>${labelMap.YYYY}</label>                                </td>  
                                <td>
                                    <%=proUtil.selectYY("YY16", batch.getYyyy())%>                                </td>                                
                            </tr>
                        </table>                    </td>                     
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processMMYY('16')"></td>
                    <td class="row0 alignLeft"><div id="countPro16">..</div></td>
                </tr>
				<tr>
                    <td class="row1 alignLeft" colspan="5">
                        <b><%=labelMap.get("PROCESS_06")%></b>                    </td>
                </tr>
				<tr>
                    <td class="row0 alignCenter"><div id="ImgPro15"><img src="../../images/begin_rollback.png" ></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_15")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>    
                                    <label for="aText">${labelMap.MM}</label>                                </td>                                
                                <td>
                                    <%=proUtil.selectMM("", "MM15",batch.getMm())%>                                </td>
                                <td>
                                    <label>${labelMap.YYYY}</label>                                </td>  
                                <td>
                                    <%=proUtil.selectYY("YY15", batch.getYyyy())%>                                </td>                                
                            </tr>
                        </table>                    </td>                
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" id="<%=labelMap.get("NAMEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processMMYY('15')" ></td>
                    <td class="row0 alignLeft"><div id="countPro15">..</div></td>
                </tr>
                
                
                
                <tr>
                    <td class="row0 alignCenter"><div id="ImgPro06_02"><img src="../../images/begin_rollback.png" ></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_06_02")%></td>
                    <td class="row0 alignCenter">  
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" value="<%=startDateStr08%>" id="START_DATE06_02" name="START_DATE06_02" class="short" value="<%=request.getParameter("START_DATE06_02") == null ? "" : request.getParameter("START_DATE06_02")%>">
                                    <input name="image1" type="image" class="image_button" onclick="displayDatePicker('START_DATE06_02'); return false;" src="../../images/calendar_button.png" alt="" />                                </td>
                                <td>
                                    <input type="text" value="<%=endDateStr08%>" id="END_DATE06_02" name="END_DATE06_02" class="short" value="<%=request.getParameter("END_DATE06_02") == null ? "" : request.getParameter("END_DATE06_02")%>" />
                                    <input name="image1" type="image" class="image_button" onclick="displayDatePicker('END_DATE06_02'); return false;" src="../../images/calendar_button.png" alt="" />                                </td>
                            </tr>
                        </table>               
                   </td>                
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="process('06_02')" ></td>
                    <td class="row0 alignLeft"><div id="countPro06_02">..</div></td>
                </tr>
                
                
                
                
                
                <tr>
                    <td class="row0 alignCenter"><div id="ImgPro06_03"><img src="../../images/begin_rollback.png"  /></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_06_03")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>    
                                    <label for="aText">${labelMap.MM}</label>                                </td>                                
                                <td>
                                    <%=proUtil.selectMM("", "MM06_03",batch.getMm())%>                                </td>
                                <td>
                                    <label>${labelMap.YYYY}</label>                                </td>  
                                <td>
                                    <%=proUtil.selectYY("YY06_03", batch.getYyyy())%>                                </td>                                
                            </tr>
                        </table>                    </td>                    
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processMMYY('06_03')"></td>
                    <td class="row0 alignLeft"><div id="countPro06_03">..</div></td>
                </tr>
                <tr>
                    <td class="row0 alignCenter"><div id="ImgPro06_04"><img src="../../images/begin_rollback.png" ></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_06_04")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>    
                                    <label for="aText">${labelMap.MM}</label>                                </td>                                
                                <td>
                                    <%=proUtil.selectMM("", "MM06_04",batch.getMm())%>                                </td>
                                <td>
                                    <label>${labelMap.YYYY}</label>                                </td>  
                                <td>
                                    <%=proUtil.selectYY("YY06_04", batch.getYyyy())%>                                </td>                                
                            </tr>
                        </table>                    </td>                     
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processMMYY('06_04')"></td>
                    <td class="row0 alignLeft"><div id="countPro06_04">..</div></td>
                </tr>
                <tr>
                    <td class="row1 alignLeft" colspan="5"><b><%=labelMap.get("PROCESS_12")%></b></td>
                </tr>
				<tr>
                    <td class="row0 alignCenter"><div id="ImgPro05"><img src="../../images/begin_rollback.png" ></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_05")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" value="<%=startDateStr05%>" id="START_DATE05" name="START_DATE05" class="short" value="<%=request.getParameter("START_DATE05") == null ? "" : request.getParameter("START_DATE05")%>" />
                                    <input name="image1" type="image" class="image_button" onclick="displayDatePicker('START_DATE05'); return false;" src="../../images/calendar_button.png" alt="" />                                
								</td>
                                <td>
                                    <input type="text" value="<%=endDateStr05%>" id="END_DATE05" name="END_DATE05" class="short" value="<%=request.getParameter("END_DATE05") == null ? "" : request.getParameter("END_DATE05")%>" />
                                    <input name="image1" type="image" class="image_button" onclick="displayDatePicker('END_DATE05'); return false;" src="../../images/calendar_button.png" alt="" />                                </td>
                            </tr>
                        </table>                    </td>
                    <td class="row1 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="process('05')"></td>
                    <td class="row0 alignLeft"><div id="countPro05">..</div></td>                    
                </tr>
				<tr>
                    <td class="row0 alignCenter"><div id="ImgPro04"><img src="../../images/begin_rollback.png"></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_04")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" value="<%=startDateStr04%>" id="START_DATE04" name="START_DATE04" class="short" value="<%=request.getParameter("START_DATE04") == null ? "" : request.getParameter("START_DATE04")%>" />
                                    <input name="image1" type="image" class="image_button" onclick="displayDatePicker('START_DATE04'); return false;" src="../../images/calendar_button.png" alt="" />                                </td>
                                <td>
                                    <input type="text" value="<%=endDateStr04%>" id="END_DATE04" name="END_DATE04" class="short" value="<%=request.getParameter("END_DATE04") == null ? "" : request.getParameter("END_DATE04")%>" />
                                    <input name="image1" type="image" class="image_button" onclick="displayDatePicker('END_DATE04'); return false;" src="../../images/calendar_button.png" alt="" />                                </td>
                            </tr>
                        </table>                    </td>
                    <td class="row1 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="process('04')"></td>
                    <td class="row0 alignLeft"><div id="countPro04">..</div></td>                    
                </tr>
                
				<tr>
                    <td class="row1 alignLeft" colspan="5"><b><%=labelMap.get("PROCESS_13")%></b></td>
                </tr>
                
                <tr>
                    <td class="row0 alignCenter"><div id="ImgPro01"><img src="../../images/begin_rollback.png"></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_01")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" value="<%=startDateStr01%>" id="START_DATE01" name="START_DATE01" class="short" value="<%=request.getParameter("START_DATE01") == null ? "" : request.getParameter("START_DATE01")%>" />
                                    <input name="image1" type="image" class="image_button" onclick="displayDatePicker('START_DATE01'); return false;" src="../../images/calendar_button.png" alt="" />                                </td>
                                <td>
                                    <input type="text" value="<%=endDateStr01%>" id="END_DATE01" name="END_DATE01" class="short" value="<%=request.getParameter("END_DATE01") == null ? "" : request.getParameter("END_DATE01")%>" />
                                    <input name="image1" type="image" class="image_button" onclick="displayDatePicker('END_DATE01'); return false;" src="../../images/calendar_button.png" alt="" />                                </td>
                            </tr>
                        </table>                    </td>
                    <td class="row1 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="process('01')"></td> 
                    <td class="row0 alignLeft"><div id="countPro01">..</div></td> 
                </tr>
                <tr>
                    <td class="row0 alignCenter"><div id="ImgPro02"><img src="../../images/begin_rollback.png"></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_02")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" value="<%=startDateStr02%>" id="START_DATE02" name="START_DATE02" class="short" value="<%=request.getParameter("START_DATE02") == null ? "" : request.getParameter("START_DATE02")%>" />
                                    <input name="image1" type="image" class="image_button" onclick="displayDatePicker('START_DATE02'); return false;" src="../../images/calendar_button.png" alt="" />                                </td>
                                <td>
                                    <input type="text" value="<%=endDateStr02%>" id="END_DATE02" name="END_DATE02" class="short" value="<%=request.getParameter("END_DATE02") == null ? "" : request.getParameter("END_DATE02")%>" />
                                    <input name="image1" type="image" class="image_button" onclick="displayDatePicker('END_DATE02'); return false;" src="../../images/calendar_button.png" alt="" />                                </td>
                            </tr>                            
                        </table>                    </td>
                    <td class="row1 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="process('02')"></td>
                    <td class="row0 alignLeft"><div id="countPro02">..</div></td> 
                </tr>
                <tr>
                    <td class="row0 alignCenter"><div id="ImgPro03"><img src="../../images/begin_rollback.png"></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_03")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>
                                    <input type="text" value="<%=startDateStr03%>" id="START_DATE03" name="START_DATE03" class="short" value="<%=request.getParameter("START_DATE03") == null ? "" : request.getParameter("START_DATE03")%>">
                                    <input name="image1" type="image" class="image_button" onclick="displayDatePicker('START_DATE03'); return false;" src="../../images/calendar_button.png" alt="" />                                </td>
                                <td>
                                    <input type="text" value="<%=endDateStr03%>" id="END_DATE03" name="END_DATE03" class="short" value="<%=request.getParameter("END_DATE03") == null ? "" : request.getParameter("END_DATE03")%>" />
                                    <input name="image1" type="image" class="image_button" onclick="displayDatePicker('END_DATE03'); return false;" src="../../images/calendar_button.png" alt="" />                                </td>
                            </tr>
                        </table>                    </td>
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="process('03')"></td>
                    <td class="row0 alignLeft"><div id="countPro03">..</div></td>
                </tr>
                
                <!--  Roll Back Discharge Summary Data -->
                <tr>
                    <td class="row0 alignCenter"><div id="ImgPro00"><img src="../../images/begin_rollback.png"></div></td>
                    <td class="row1 alignLeft"><%=labelMap.get("PROCESS_00")%></td>
                    <td class="row0 alignCenter">
                        <table cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td>    
                                    <label for="aText">${labelMap.MM}</label>                                
                                </td>                                
                                <td>
                                    <%=proUtil.selectMM("", "MM00",batch.getMm())%>                                
                                </td>
                                <td>
                                    <label>${labelMap.YYYY}</label>                                
                                </td>  
                                <td>
                                    <%=proUtil.selectYY("YY00", batch.getYyyy())%>                                
                                </td>                                
                            </tr>
                        </table>              
                        </td>
                    <td class="row0 alignCenter"><input type="button" value="<%=labelMap.get("VALUEBUTTONPROCESS")%>" name="<%=labelMap.get("NAMEBUTTONPROCESS")%>" onclick="processMMYY('00')"></td>
                    <td class="row0 alignLeft"><div id="countPro00">..</div></td>
                </tr>
                
                
                <tr>
                    <th colspan="5" class="buttonBar">                        
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../process/ProcessFlow.jsp'" />					</th>
                </tr>
                
            </table>
        </form>

        <center>
            <iframe id="iFrameSubmit" name="iFrameSubmit" width="0" height="0"></iframe>
        </center>
    </body>
</html>
<script language="javascript">
    function process(idProcess){
    	if(confirm('Confirm Rollback?')){       
	        var nDateST = "START_DATE" + idProcess; 
	        var nDateFI = "END_DATE" + idProcess;
	        var valueDataST = document.getElementById(nDateST).value;
	        var valueDataFI = document.getElementById(nDateFI).value;
	        
	        var url = "../../ProcessRollbackSrvl?idProcess=" + idProcess + "&START_DATE=" + valueDataST + "&END_DATE=" + valueDataFI;
	        var iFrame = document.getElementById("iFrameSubmit");      
	        iFrame.src = url;
    	}      
    }
    
    function processMMYY(idProcess){    
    	if(confirm('Confirm Rollback?')){       
	        var m = "MM" + idProcess; 
	        var y = "YY" + idProcess;
	        var mm = document.getElementById(m).value;
	        var yy = document.getElementById(y).value;
	        var url = "../../ProcessRollbackSrvl?idProcess=" + idProcess + "&mm=" + mm + "&yy=" + yy;
	        var iFrame = document.getElementById("iFrameSubmit");      
	        iFrame.src = url;
    	}
    }  
    
    function changStatus(id,value1){
        //alert(value1);
        var nameDiv = "ImgPro" + id;
        var e = document.getElementById(nameDiv);
        if(value1=='true'){
            //alert('1-1');
            e.innerHTML = '<img src="../../images/complete_rollback.png">';
        }else{
           // alert('1-2');
            e.innerHTML = '<img src="../../images/error_rollback.png">'; 
        }

    }
    function changValue(id,value1){
        var nameDiv = "countPro" + id;
        var e = document.getElementById(nameDiv);
        if(value1=='true'){
            //alert("1");            
            e.innerHTML = 'success'; 
            //alert("2");
        }else{
            e.innerHTML = 'error';
        }
    }
    function status(idProcessRet,valueResult_1){
        changStatus(idProcessRet,valueResult_1);
        changValue(idProcessRet,valueResult_1);
    }
    
</script>