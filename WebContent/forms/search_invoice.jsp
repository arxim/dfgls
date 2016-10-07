<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="error.jsp"%>

<%@page import="df.jsp.LabelMap"%>
<%@page import="df.bean.db.conn.DBConnection"%>
<%@page import="df.bean.db.DBMgr"%>
<%@page import="df.bean.db.table.Batch"%>
<%@page import="df.jsp.Util"%>
<%@page import="java.sql.*"%>

<%
            final int NUM_CONDITION = 3;

            //
            // Initial LabelMap
            //
            
            if (session.getAttribute("LANG_CODE") == null) {
                session.setAttribute("LANG_CODE", LabelMap.LANG_EN);
            }
            LabelMap labelMap = new LabelMap(session.getAttribute("LANG_CODE").toString());
            labelMap.add("TITLE_MAIN", "Search", "ค้นหา");
            labelMap.add("KEYWORD", "Keyword", "คำค้นหา");
            labelMap.add("DISPLAY_FIELD", "View Column", "แสดงคอลัมน์");
            labelMap.add("ORDER_BY_FIELD", "Order By Column", "เรียงลำดับตามคอลัมน์");
            labelMap.add("ASC", "Ascending", "จากน้อยไปมาก");
            labelMap.add("DESC", "Descending", "จากมากไปน้อย");

            request.setAttribute("labelMap", labelMap.getHashMap());

           

            //
            // Process request
            //
            
            request.setCharacterEncoding("UTF-8");
            String condition_include = request.getParameter("COND");
            String returnField = "";

            if (request.getParameter("RETURN_FIELD") == null) {
                returnField = "CODE";
            }
            else {
                returnField = request.getParameter("RETURN_FIELD");
            }

            String displaysubfiled = "";
            if(request.getParameter("DISPLAY_SUB_CODE")==null){
                displaysubfiled = "";
            }else{
                displaysubfiled = request.getParameter("DISPLAY_SUB_CODE");
            }

            if (request.getParameter("TABLE") == null) {
                throw new Exception("Table name is not specified");
            }
            String table = request.getParameter("TABLE");

            boolean beActive = request.getParameter("BEACTIVE") == null ? false : true;
            boolean beInsideHospital = request.getParameter("BEINSIDEHOSPITAL") == null ? false : true;

            if (request.getParameter("TARGET") == null) {
                throw new Exception("Target input object is not specified");
            }
            String target = request.getParameter("TARGET");

            if (request.getParameter("HANDLE") == null) {
                throw new Exception("Handle function is not specified");
            }
            String handle = request.getParameter("HANDLE");
            
            String[] searchFields = request.getParameterValues("SEARCH_FIELDS") == null ? null : request.getParameterValues("SEARCH_FIELDS");
            String[] keywords = request.getParameterValues("KEYWORDS") == null ? null : request.getParameterValues("KEYWORDS");

            String displayField = request.getParameter("DISPLAY_FIELD") == null ? null : request.getParameter("DISPLAY_FIELD").toString();
            String displayField_second = request.getParameter("displayField_second") == null ? null : request.getParameter("displayField_second").toString();

            String orderByField = request.getParameter("ORDER_BY_FIELD") == null ? "INVOICE_DATE" : request.getParameter("ORDER_BY_FIELD").toString();
            String orderByDir = request.getParameter("ORDER_BY_DIR") == null ? "ASC" : request.getParameter("ORDER_BY_DIR").toString();

            DBConnection con = new DBConnection();
            con.connectToLocal();
            DBConnection c = new DBConnection();
            Batch b = new Batch(session.getAttribute("HOSPITAL_CODE").toString(), con);

            String[] allListFields = con.getColumnNames(table);
            String[] listFields = allListFields;
            
            // Remove field ACTIVE
            if (beActive) {
                listFields = new String[allListFields.length - 1];
                int j = 0;
                for (int i = 0; i < allListFields.length; i++) {
                    if (!allListFields[i].equalsIgnoreCase("ACTIVE")) {
                        listFields[j++] = allListFields[i];
                    }
                }
            }
            
            // Remove field HOSPITAL_CODE
            if (beInsideHospital) {
                allListFields = listFields;
                listFields = new String[allListFields.length - 1];
                int j = 0;
                for (int i = 0; i < allListFields.length; i++) {
                    if (!allListFields[i].equalsIgnoreCase("HOSPITAL_CODE")) {
                        listFields[j++] = allListFields[i];
                    }
                }
            }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>${labelMap.TITLE_MAIN}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../css/share.css" media="all" />
        <link rel="stylesheet" type="text/css" href="../css/search.css" media="all" />
        <script type="text/javascript">
            function DATA_Click(code) {
                parent.mainFrame.document.getElementById("<%=target%>").value = code;
                parent.mainFrame.<%=handle%>();
                parent.CloseSearch()
            }
        </script>
    </head>
    <body>
        <center>
        <br>
        <form id="mainForm" name="mainForm" method="post" action="search_invoice.jsp">
            <input type="hidden" id="TABLE" name="TABLE" value="<%=table%>" />
            <input type="hidden" id="RETURN_FIELD" name="RETURN_FIELD" value="<%=returnField%>" />
            <input type="hidden" id="DISPLAY_SUB_CODE" name="DISPLAY_SUB_CODE" value="<%=displaysubfiled%>" />

            <%=beActive ? "<input type=\"hidden\" id=\"BEACTIVE\" name=\"BEACTIVE\" value=\"1\" />" : ""%>
            <%=beInsideHospital ? "<input type=\"hidden\" id=\"BEINSIDEHOSPITAL\" name=\"BEINSIDEHOSPITAL\" value=\"1\" />" : ""%>
            <input type="hidden" id="TARGET" name="TARGET" value="<%=target%>" />
            <input type="hidden" id="HANDLE" name="HANDLE" value="<%=handle%>" />
            <input type="hidden" id="COND" name="COND" value="<%=condition_include%>" />
            <table class="form">
                <tr>
                    <th colspan="2">${labelMap.TITLE_MAIN}</th>
                </tr>
                <%
            for (int i = 0; i < NUM_CONDITION; i++) {
                %>
                <tr>
                    <td class="label">${labelMap.KEYWORD}</td>
                    <td class="input alignLeft">
                        <%=DBMgr.generateDropDownList("SEARCH_FIELDS", "medium", listFields, listFields, searchFields == null ? returnField : searchFields[i])%>                            
                        <input type="text" id="KEYWORDS" name="KEYWORDS" class="medium" value="<%=keywords == null ? "" : keywords[i]%>" />
                    </td>
                </tr>
                <%
            }
                %>
                <tr>
                    <td class="label">${labelMap.ORDER_BY_FIELD}</td>
                    <td class="input alignLeft">
                        <%=DBMgr.generateDropDownList("ORDER_BY_FIELD", "medium", listFields, listFields, orderByField == null ? returnField : orderByField)%>
                        <select id="ORDER_BY_DIR" name="ORDER_BY_DIR" class="medium">
                            <option value="ASC"<%= orderByDir.equalsIgnoreCase("ASC") ? " selected=\"selected\"" : "" %>>${labelMap.ASC}</option>
                            <option value="DESC"<%= orderByDir.equalsIgnoreCase("DESC") ? " selected=\"selected\"" : "" %>>${labelMap.DESC}</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th colspan="2" class="buttonBar">
                        <input type="submit" id="SEARCH" name="SEARCH" class="button" value="<%=labelMap.get(LabelMap.SEARCH)%>" />
                        <input type="button" id="CLOSE" name="CLOSE" class="button" value="${labelMap.CLOSE}" onclick="parent.CloseSearch()" />
                    </th>
                </tr>
            </table>
            <%

            ResultSet rs = null;
            String query = "";
            
            if (keywords != null && searchFields != null) {
                /*
                query = "SELECT TOP 300 INVOICE_NO, ADMISSION_TYPE_CODE, INVOICE_DATE, HN_NO, PATIENT_NAME, PAYOR_OFFICE_NAME, " +
                        "CASE WHEN SUM(AMOUNT_AFT_DISCOUNT)= 0 THEN 'CANCEL' ELSE 'PAID' END AS REMARK  FROM TRN_DAILY WHERE ";
                */
                query = "SELECT TOP 300 INVOICE_NO, ADMISSION_TYPE_CODE, INVOICE_DATE, HN_NO, PATIENT_NAME, PAYOR_OFFICE_NAME, " +
                        "CASE WHEN SUM(AMOUNT_AFT_DISCOUNT)= 0 THEN 'CANCEL' ELSE "+
                        "CASE WHEN BATCH_NO <> '' THEN 'PAID' ELSE "+
                        "CASE WHEN INVOICE_DATE LIKE '"+b.getBatchNo()+"%' THEN '' ELSE 'HOLD' END "+
                        "END "+
                        "END AS REMARK  FROM TRN_DAILY WHERE ";
                //out.println(query);
                if (beActive) {
                    query += " ACTIVE='1' AND";
                }
                if (beInsideHospital) {
                    query += " HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE") + "' AND";
                }
                query += " ( 1 = 1 AND HOSPITAL_CODE = '"+ session.getAttribute("HOSPITAL_CODE").toString() +"'";
                for (int i = 0; i < NUM_CONDITION; i++) {
                    if (keywords[i] != "") {
                        query += " AND" + String.format(" %1$s LIKE '%2$s'", DBMgr.toSQLString(searchFields[i]), DBMgr.toSQLString(keywords[i]));
                    }
                }
                query += ")";
                query += " AND HOSPITAL_CODE='" + session.getAttribute("HOSPITAL_CODE") +"' ";
                query += " GROUP BY INVOICE_NO, ADMISSION_TYPE_CODE, INVOICE_DATE, HN_NO, PATIENT_NAME, PAYOR_OFFICE_NAME, BATCH_NO ";

                if (orderByField != null) {
                    query += " ORDER BY " + orderByField + " " + orderByDir;
                }
                //out.println(query);
                try{
                    System.out.println(query);
                    rs = con.executeQuery(query);
                }catch(Exception err){
                    out.println(query);
                }
            }
            if (rs != null) {
                
            %>
            <table class="data" id="dataTable" name="dataTable">
                <tr>
                    <th width="20%">Invoice No.</th>
                    <th width="10%">I/O</th>
                    <th width="15%">Invoice Date</th>
                    <th width="20%">Hn.</th>
                    <th width="20%">Patient Name</th>
                    <th width="20%">Contract Code</th>
                    <th width="10%">Remark</th>
                </tr>
                <%
                int i = 0;
                while (rs.next()) {
                %>
                <tr>
                    <td class="row<%=i % 2%>"><a href="javascript:DATA_Click('<%=rs.getString(returnField)%>')" title="${labelMap.SELECT}"><%=Util.formatHTMLString(rs.getString("INVOICE_NO"), true)%></a></td>
                    <td class="row<%=i % 2%>"><a href="javascript:DATA_Click('<%=rs.getString(returnField)%>')" title="${labelMap.SELECT}"><%=Util.formatHTMLString(rs.getString("ADMISSION_TYPE_CODE"), true)%></a></td>
                    <td class="row<%=i % 2%> alignLeft"><a href="javascript:DATA_Click('<%=rs.getString(returnField)%>')" title="${labelMap.SELECT}"><%=Util.formatHTMLString(rs.getString("INVOICE_DATE"), true)%></a></td>
                    <td class="row<%=i % 2%> alignLeft"><a href="javascript:DATA_Click('<%=rs.getString(returnField)%>')" title="${labelMap.SELECT}"><%=Util.formatHTMLString(rs.getString("HN_NO"), true)%></a></td>
                    <td class="row<%=i % 2%> alignLeft"><a href="javascript:DATA_Click('<%=rs.getString(returnField)%>')" title="${labelMap.SELECT}"><%=Util.formatHTMLString(rs.getString("PATIENT_NAME"), true)%></a></td>
                    <td class="row<%=i % 2%> alignLeft"><a href="javascript:DATA_Click('<%=rs.getString(returnField)%>')" title="${labelMap.SELECT}"><%=Util.formatHTMLString(rs.getString("PAYOR_OFFICE_NAME"), true)%></a>&nbsp;</td>
                    <td class="row<%=i % 2%> alignLeft"><a href="javascript:DATA_Click('<%=rs.getString(returnField)%>')" title="${labelMap.SELECT}"><%=Util.formatHTMLString(rs.getString("REMARK"), true)%></a></td>
                </tr>
                <%
                    i++;
                }
                if (i == 0) {
                %>
                <tr><td colspan="2" class="row0">${labelMap.SEARCH_NOT_FOUND}</td></tr>
                <%
                    }
                %>                
            </table>
            <%
            }
            %>                    
        </form>
        </center>
        <br/><br/>
    </body>
</html>
<%
            if (rs != null) {
                rs.close();
            }
            con.Close();
%>
