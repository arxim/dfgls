<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="../error.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<script type="text/javascript">
		function load(){
			location.href = "http://localhost:8082/tax/initProcess/<%=session.getAttribute("HOSPITAL_CODE").toString() %>";
		}
	</script>
    <body onload="load();"></body>
</html>