<%@ page contentType="text/html;charset=windows-874"%>
<%@ page import ="javazoom.upload.*,java.util.*"%>
<jsp:useBean id="up" scope="page" class="javazoom.upload.UploadBean" >
<jsp:setProperty name="up" property="folderstore" value="C:\Upload" />
</jsp:useBean>
<html>
<body>

<%
if (MultipartFormDataRequest.isMultipartFormData(request)) {
    MultipartFormDataRequest mrequest = new MultipartFormDataRequest(request);
    String submit = mrequest.getParameter("submit");
    String name = mrequest.getParameter("name");
    String address = mrequest.getParameter("address");
    //if(submit != null){
        //Hashtable files = mrequest.getFiles();
        //UploadFile file = (UploadFile) files.get("upload");
        /*
        out.println("<b>Data</b>");
        out.println("<br>name : "+name);
        out.println("<br>address : "+address);
        out.println("<br><b>File</b>");
        out.println("<br>fileName : "+file.getFileName());
        out.println("<br>fileSize : "+file.getFileSize());
        out.println("<br>contentType : "+file.getContentType());
        */
        up.store(mrequest, "upload");
    //}
}
%>
<form method="post" action="" enctype="multipart/form-data">
<table>
<!--
<tr><td>name</td><td><input type="text" name="name"></td></tr>
<tr><td>address</td><td><textarea name="address" rows="3" cols="30"></textarea></td></tr>
-->
<tr><td>file</td><td><input type="file" name="upload"></td></tr>
<tr><td colspan="2"><input type="submit" name="submit" value="Upload"></td></tr>
</table>
</form>
</body>
</html>

