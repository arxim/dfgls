<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="df.bean.obj.util.ReadProperties"%>
<%  
	ReadProperties rp = new ReadProperties();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <script type="text/javascript">
           function load(){
        	   
       			location.href =  "http://<%= rp.getPropertiesData("config.properties", "interface.","ip").get("ip") %>:8882/tax/tax_reduce/<%=session.getAttribute("HOSPITAL_CODE").toString()%>";	 
       		
           }
	    </script>
    </head>    
    <body onload="load();">
    </body>
</html>


