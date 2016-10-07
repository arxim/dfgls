<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>

<%    if (session.getAttribute("LANG_CODE") == null)
        session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
    
    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
labelMap.add("TITLE_MAIN", "Daily Report", "รายงานประจำวัน");
    labelMap.add("REPORT_TYPE", "Report Type", "ประเภทรายงาน");
    labelMap.add("START_DATE", "Start Date", "เริ่มวันที่");
    labelMap.add("START_TIME", "Start Time", "เริ่มเวลา");
    labelMap.add("DOCTOR_TYPE_CODE", "Doctor Type", "Doctor Type");
    labelMap.add("LOCATION_CODE", "Location Code", "Location Code");
    labelMap.add("ORDER_ITEM_CODE", "Order Item Code", "Order Item Code");
    labelMap.add("ADMISSION_TYPE_CODE", "Admission Type", "Admission Type");
    labelMap.add("MODULE", "Module", "Module");
    labelMap.add("STATUS", "DF Payment Status", "DF Payment Status");
    labelMap.add("STATUS_PAID", "Paid", "ชำระแล้ว");
    labelMap.add("STATUS_UNPAID", "Unpaid", "ค้างชำระ");
    request.setAttribute("labelMap", labelMap.getHashMap());
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../../css/calendar.css" />
        <script type="text/javascript" src="../../javascript/calendar.js"></script>
    </head>    <body>
        <p>
            <a href="../../switch_lang.jsp?lang=<%=LabelMap.LANG_TH%>">ไทย</a> | <a href="../../switch_lang.jsp?lang=<%=LabelMap.LANG_EN%>">Eng</a>
        </p>
        <form id="mainForm" name="mainForm" method="post" action="BankSrvl">
<table class="form">
                <tr>
                  <th colspan="4">${labelMap.TITLE_MAIN}</th>
                </tr>
                <tr>
                    <td class="label">
                        <label for="REPORT_TYPE">${labelMap.REPORT_TYPE}</label>                    </td>
                    <td colspan="3" class="input">
                    <label>
                    <select id="REPORT_TYPE" name="REPORT_TYPE" class="medium">
                      <option value="" selected="selected">-- Select --</option>
                      <option value="">รายงานน้ำเข้าประจำวัน</option>
                      <option value="">รายงานการคำนวณรายวัน</option>
                      <option value="">รายงานการตั้งหนี้</option>
                      <option>รายงานการรับชำระหนี้</option>
                                                                                </select>
                    </label></td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="START_DATE">${labelMap.START_DATE}</label>
                    </td>
                    <td class="input">
                        <input type="text" id="START_DATE" name="START_DATE" class="short" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('START_DATE'); return false;" />
                    </td>
                    <td class="label">
                        <label for="END_DATE">${labelMap.END_DATE}</label>
                    </td>
                    <td class="input">
                        <input type="text" id="END_DATE" name="END_DATE" class="short" />
                        <input type="image" class="image_button" src="../../images/calendar_button.png" alt="" onclick="displayDatePicker('END_DATE'); return false;" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DOCTOR_TYPE_CODE">${labelMap.DOCTOR_TYPE_CODE}</label></td>
                    <td class="input" colspan="3"><input type="text" id="DOCTOR_TYPE_CODE" name="DOCTOR_TYPE_CODE" class="short" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="alert('Under construction')" />
                        <input type="text" id="DOCTOR_TYPE_DESCRIPTION" name="DOCTOR_TYPE_DESCRIPTION" class="long" />
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="LOCATION_CODE">${labelMap.LOCATION_CODE}</label>                    
                    </td>
                    <td class="input" colspan="3">
                        <input type="text" id="LOCATION_CODE" name="LOCATION_CODE" class="short" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="alert('Under construction')" />
                        <input type="text" id="LOCATION_DESCRIPTION" name="LOCATION_DESCRIPTION" class="long" />                    
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="ORDER_ITEM_CODE">${labelMap.ORDER_ITEM_CODE}</label>                    
                    </td>
                    <td class="input" colspan="3">
                        <input type="text" id="ORDER_ITEM_CODE" name="ORDER_ITEM_CODE" class="short" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="alert('Under construction')" />
                        <input type="text" id="ORDER_ITEM_DESCRIPTION" name="ORDER_ITEM_DESCRIPTION" class="long" />                    
                    </td>
                </tr>
                <tr>
                    <td class="label">
                        <label for="ADMISSION_TYPE_CODE">${labelMap.ADMISSION_TYPE_CODE}</label>                    
                    </td>
                    <td class="input">
                        <select id="ADMISSION_TYPE_CODE" name="ADMISSION_TYPE_CODE" class="medium">
                            <option value="">-- Select --</option>
                            <option value="">IPD</option>
                            <option value="">OPD</option>
                            <option value="">All</option>
                        </select>                    
                    </td>
                    <td class="label"><label for="MODULE">${labelMap.MODULE}</label></td>
                    <td class="input">
                        <select id="MODULE" name="MODULE" class="medium">
                            <option value="">-- Select --</option>
                            <option value="">CASH</option>
                            <option value="">AR</option>
                            <option>All</option>
                        </select>                    
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="STATUS_PAID">${labelMap.STATUS}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="STATUS_PAID" name="STATUS" value="1" checked="checked" />
                        <label for="STATUS_PAID">${labelMap.STATUS_PAID}</label>
                        <input type="radio" id="STATUS_UNPAID" name="STATUS" value="0" />
                        <label for="STATUS_UNPAID">${labelMap.STATUS_UNPAID}</label>
                    </td>
                </tr>
                <tr>
                    <th colspan="4" class="buttonBar">
                        <input type="button" id="SAVE" name="SAVE" class="button" value="${labelMap.SAVE}" onclick="validateData();" />
                        <input type="reset" id="RESET" name="RESET" class="button" value="${labelMap.RESET}" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="window.location='../../process_flow_page.jsp'" />

                    </th>
                </tr>
            </table>
    </form>
    </body>
</html>
