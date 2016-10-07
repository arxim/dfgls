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
            
            //ProcessUtil proUtil = new ProcessUtil();                    
            //Batch batch = new Batch();                                 
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Flow Process", "ระบบ Flow");
            labelMap.add("PAY_DATE", "Pay Date", "วันที่จ่ายเงิน");
            labelMap.add("COL_0", " - ", " - ");
            labelMap.add("COL_1", "Process Name", "ชื่อโปรเซส");
            labelMap.add("COL_2", "Count", "จำนวนโปรเซส");
            labelMap.add("COL_3", " - ", " - ");
            labelMap.add("COL_4", " - ", " - ");

            labelMap.add("INTERFACE", "Interface", "นำเข้ารายการทั่วไป");
            labelMap.add("PROCESS_01", "Interface Bill", "นำเข้ารายการรักษา");
            labelMap.add("PROCESS_02", "Interface Verify", "นำเข้ารายการอ่านผล");
            labelMap.add("PROCESS_03", "Interface AR Receipt", "นำเข้ารายการรับชำระ");
            labelMap.add("PROCESS_04", "Import Transaction", "นำรายการรักษาเข้าระบบ");
            labelMap.add("PROCESS_05", "Compute daily", "คำนวณรายวัน");
            labelMap.add("PROCESS_06", "Process Receipt", "ประมวลผลรายการรับเงิน");
            labelMap.add("PROCESS_06_01", "Process Receipt by Cash", "ประมวลผลรายการรับเงิน(เงินสด)");
            labelMap.add("PROCESS_06_02", "Process Receipt by AR", "ประมวลผลรายการรับเงิน(รับชำระหนี้)");
            labelMap.add("PROCESS_06_03", "Process Receipt by Payor Office", "ประมวลผลรายการรับเงิน(คู่สัญญา)");
            labelMap.add("PROCESS_06_04", "Process Receipt by Doctor", "ประมวลผลรายการรับเงิน(แพทย์)");
            labelMap.add("PROCESS_07", "Compute monthly", "คำนวณรายการรักษารายเดือน");
            labelMap.add("PROCESS_08", "Payment monthly", "คำนวณรายการทำจ่ายรายเดือน");
            labelMap.add("PROCESS_09", "Export bank", "ประมวลผลรายการโอนแบงค์");
            labelMap.add("PROCESS_10", "Compute summary tax 40(6)", "คำนวณรายการภาษี 40(6)");
            labelMap.add("PROCESS_11", "Compute payment monthly for Salary", "คำนวณรายการจ่ายเงินเดือนแพทย์");
            
            labelMap.add("VALUEBUTTONPROCESS","Process","Process");
            labelMap.add("NAMEBUTTONPROCESS","Process","คำนวณ");
            
            labelMap.add("MM", "Month", "เดือน");
            labelMap.add("YYYY", "Year", "ปี");
            
            labelMap.add("1","January","มกราคม");
            labelMap.add("2","February","กุมภาพันธ์");
            labelMap.add("3","March","มีนาคม");
            labelMap.add("4","April","เมษายน");
            labelMap.add("5","May","พฤษภาคม");
            labelMap.add("6","June","มิถุนายน");
            labelMap.add("7","July","กรกฎาคม");
            labelMap.add("8","August","สิงหาคม");
            labelMap.add("9","September","กันยายน");
            labelMap.add("10","October","ตุลาคม");
            labelMap.add("11","November","พฤศจิกายน");
            labelMap.add("12","December","ธันวาคม");
            labelMap.add("SELECT","Display","แสดงผล");
            
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
          <center>
			<input type="button" id="SELECT" name="SELECT" value="${labelMap.SELECT}" onclick="main('06');" />
			<p>
                <table id="dataTable" name="dataTable" width="400">
					<tr>
                        <td class="row0 alignCenter">
                            <div id="ImgPro01">
                                <img src="../../images/start_icon.png" border="0">
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <img src="../../images/flow_arrow_gray.png">
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <b><%=labelMap.get("INTERFACE")%></b>
                            <div id="ImgPro01">
                                <a href="javascript:void(0)" onclick="OpenWindow('ProcessFlowPopupInterface.jsp');"><img src="../../images/pending_icon.png" border="0"></a>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <img src="../../images/flow_arrow_gray.png">
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <b><%=labelMap.get("PROCESS_04")%></b>
                            <div id="ImgPro04">
                                <a href="javascript:void(0)" onclick="OpenWindow('ProcessFlowPopupImportBill.jsp');"><img src="../../images/pending_icon.png" border="0"></a>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <img src="../../images/flow_arrow_gray.png">
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <b><%=labelMap.get("PROCESS_05")%></b>
                            <div id="ImgPro05">
                                <a href="javascript:void(0)" onclick="OpenWindow('ProcessFlowPopupComputedaily.jsp');"><img src="../../images/pending_icon.png" border="0"></a>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <img src="../../images/flow_arrow_gray.png">
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <b><%=labelMap.get("PROCESS_06")%></b>
                            <div id="ImgPro06">
                                <a href="javascript:void(0)" onclick="OpenWindow('ProcessFlowPopupReceipt.jsp');"><img src="../../images/pending_icon.png" border="0"></a>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <img src="../../images/flow_arrow_gray.png">
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <%=labelMap.get("PROCESS_07")%>
                            <div id="ImgPro07"><img src="../../images/pending_icon.png" border="0"></div>
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <img src="../../images/flow_arrow_gray.png">
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <%=labelMap.get("PROCESS_08")%>
                            <div id="ImgPro08"><img src="../../images/pending_icon.png" border="0"></div>
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <img src="../../images/flow_arrow_gray.png">
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <%=labelMap.get("PROCESS_09")%>
                            <div id="ImgPro09"><img src="../../images/pending_icon.png" border="0"></div>
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <img src="../../images/flow_arrow_gray.png">
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <%=labelMap.get("PROCESS_10")%>
                            <div id="ImgPro10"><img src="../../images/pending_icon.png" border="0"></div>
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <img src="../../images/flow_arrow_gray.png">
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <%=labelMap.get("PROCESS_11")%>
                            <div id="ImgPro11"><img src="../../images/pending_icon.png" border="0"></div>
                        </td>
                    </tr>
					<tr>
                        <td class="row0 alignCenter">
                            <img src="../../images/flow_arrow_gray.png">
                        </td>
                    </tr>
                    <tr>
                        <td class="row0 alignCenter">
                            <div id="ImgPro11"><img src="../../images/end_icon.png" border="0"></div>
                        </td>
                    </tr>
                </table>
            </center>
        </form>

        <center>
            <iframe name="iFrameSubmit" width="0" height="0"></iframe>
            <input type="hidden" name="goToURL_07" value="ProcessMonthly.jsp">
            <input type="hidden" name="goToURL_08" value="forms/process/ProcessMonthly.jsp">
            <input type="hidden" name="goToURL_09" value="http://www.google.com">
            <input type="hidden" name="goToURL_10" value="http://www.google.com">
            <input type="hidden" name="goToURL_11" value="http://www.google.com">
			<input type="hidden" id="MM" name="MM" value="<%=JDate.getMonth() %>" />
			<input type="hidden" id="YY" name="YY" value="<%=JDate.getYear() %>" />
        </center>
    </body>
</html>
<script language="javascript">
    function process(idProcess){        
        var nDateST = "START_DATE" + idProcess; 
        var nDateFI = "END_DATE" + idProcess;
        var valueDataST = document.getElementById(nDateST).value;
        var valueDataFI = document.getElementById(nDateFI).value;
        
        var url = "../../ProcessFlowSrvl?idProcess=" + idProcess + "&START_DATE=" + valueDataST + "&END_DATE=" + valueDataFI;
        var iFrame = document.getElementById("iFrameSubmit");      
        iFrame.src = url;         
    }

    function main(cntNext){
        //alert(document.getElementById('MM').value);
        var countProcess = parseInt(cntNext,10);
        countProcess++;
        var m = "MM";
        var y = "YY";
        var mm = document.getElementById(m).value;
        var yy = document.getElementById(y).value;
        
        var idProcess = "";
        if(countProcess < 12){
            if(countProcess < 10){
                idProcess = "0" + countProcess;
            }else{
                idProcess = countProcess;
            }
            processMMYY(idProcess,mm,yy);
        }
    }

    //function

    function processMMYY(idProcess,m,y){
        //alert("processMMYY = " + idProcess);
        var mm = m;
        var yy = y;
        
        var url = "../../ProcessFlowSrvl?idProcess=" + idProcess + "&mm=" + mm + "&yy=" + yy;
        var iFrame = document.getElementById("iFrameSubmit");      
        iFrame.src = url;
    }  
    
    function processOnce(idProcess){
        var nDateST = "START_DATE" + idProcess;
        var valueDataST = document.getElementById(nDateST).value;
        var url = "../../ProcessFlowSrvl?idProcess=" + idProcess + "&START_DATE=" + valueDataST + "&END_DATE=no";
        var iFrame = document.getElementById("iFrameSubmit");       
        iFrame.src = url;  
    }
    
    function changStatus(id,value1,value2){
        var nameDiv = "ImgPro" + id;
        var nameUrl = "goToURL_" + id;
        var e = document.getElementById(nameDiv);
        var url = document.getElementById(nameUrl);
        
        var e = document.getElementById(nameDiv);
        if(value2==''){
            if(value1 > 0){
                //alert('1-1');
                e.innerHTML = '<img src="../../images/complete_icon.png"  border="0">';
            }else{
                //alert('1-2');
                e.innerHTML = '<a href="'+ url.value +'"><img src="../../images/pending_icon.png"  border="0"></a>';
            }
        }else{
            //alert('2');
            if(value1 == value2){
                //alert('2-1');
                e.innerHTML = '<img src="../../images/complete_icon.png"  border="0">';
            }else{
                //alert('2-2');
                e.innerHTML = '<a href="'+ url.value +'"><img src="../../images/pending_icon.png"  border="0"></a>';
            }
        }

        // for next process;
        main(id);
    }
    function changValue(id,value1,value2){
        var nameDiv = "countPro" + id;
        var e = document.getElementById(nameDiv);
        if(value1=='')value1 = '0';
        if(value2==''){
            //alert("1");            
            e.innerHTML = value1; 
            //alert("2");
        }else{
            e.innerHTML = value1 + '/' + value2; 
        } 
    }
    function status(idProcessRet,valueResult_1,valueResult_2){
        //alert(valueResult_1);
        changStatus(idProcessRet,valueResult_1,valueResult_2);
        //changValue(idProcessRet,valueResult_1,valueResult_2);
    }
    
</script>
<script language="javascript">
    function OpenWindow(url,temp){
        var mm = document.getElementById("MM");
        var yy = document.getElementById("YY");
        //alert(mm.value);
        url = url + "?mm=" + mm.value + "&yy=" + yy.value;
        if (temp=='') temp ='temp'
        temp415=window.open(url, temp, 'scrollbars=yes,statusbars=no,toolbar=no,location=no,status=no,menubar=no,resizable=yes,dependent=no');
        temp415.focus();
    }

</script>
