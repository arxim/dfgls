<%@page contentType="text/html" pageEncoding="UTF-8" import="df.jsp.LabelMap" errorPage="../error.jsp"%>

<%    if (session.getAttribute("LANG_CODE") == null)
        session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
    
    LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
labelMap.add("TITLE_MAIN", "Location Type", "Location Type");
    labelMap.add("CODE", "Code", "รหัส");
    labelMap.add("DESCRIPTION", "Description", "ชื่อ");
    //labelMap.add("ACTIVE", "Status", "สถานะ");
    //labelMap.add("ACTIVE_0", "Inactive", "ไม่ใช้งาน");
    //labelMap.add("ACTIVE_1", "Active", "ใช้งาน");
    request.setAttribute("labelMap", labelMap.getHashMap());
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <link rel="stylesheet" type="text/css" href="../../css/share.css" media="all" />
        <script type="text/javascript">
            function validateData()
            {
/*                if (document.mainForm.aText.value.length <= 0)
                    alert("กรุณาป้อนข้อมูล");
                else*/
                    document.mainForm.submit();
            }
        </script>
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
                    <td class="label"><label for="CODE">${labelMap.CODE}</label></td>
                    <td colspan="3" class="input">
                        <input type="text" id="CODE" name="CODE" class="short" />
                        <input type="image" class="image_button" src="../../images/search_button.png" alt="" onclick="alert('Under construction')" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="DESCRIPTION">${labelMap.DESCRIPTION}</label></td>
                    <td colspan="3" class="input">
                    <input type="text" id="DESCRIPTION" name="DESCRIPTION" class="long" />
                    </td>
                </tr>
                <tr>
                    <td class="label"><label for="ACTIVE_1">${labelMap.ACTIVE}</label></td>
                    <td colspan="3" class="input">
                        <input type="radio" id="ACTIVE_1" name="ACTIVE" value="1" checked="checked" />
                        <label for="ACTIVE_1">${labelMap.ACTIVE_1}</label>
                        <input type="radio" id="ACTIVE_0" name="ACTIVE" value="0" />
                        <label for="ACTIVE_0">${labelMap.ACTIVE_0}</label>
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
