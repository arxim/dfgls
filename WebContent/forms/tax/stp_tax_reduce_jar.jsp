<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
	<script language="javascript">
	var hosp = document.HOSPITAL_CODE.value;
	console.log(hosp)
	    function load(){
			if("<%=session.getAttribute("HOSPITAL_CODE").toString()%>"=="CHC00"){
				location.href = "http://192.168.1.24:8882/tax/tax_reduce/<%=session.getAttribute("HOSPITAL_CODE").toString()%>";
			}else{
				location.href = "http://localhost:8882/tax/tax_reduce/<%=session.getAttribute("HOSPITAL_CODE").toString()%>";
			}
	    }
	</script>        
    </head><!--onload="JsOnLoad();"-->
	<body onload="load();">
		<input type="hidden" id="HOSPITAL_CODE" name=""HOSPITAL_CODE"" value=<%=session.getAttribute("HOSPITAL_CODE")%> />
	</body>
</html>