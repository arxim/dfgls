<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
	<script language="javascript">
	    function load(){
	 	   location.href = "http://localhost:8082/tax/main/<%=session.getAttribute("HOSPITAL_CODE").toString() %>";
	    }
	</script>        
    </head><!--onload="JsOnLoad();"-->
	<body onload="load();">
	</body>
</html>