<!-- 
*
* AMOS-SS16-PROJ9
*
* Released under the AGPL license
* Date: 2016-07-08
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cache</title>
</head>
<body>
	<textarea name="cache" form="cacheForm" style="width: 600px; height: 400px;"></textarea>
	<form id="cacheForm" action="${pageContext.request.contextPath}/submitCache" method="POST">
	  <input type="submit" /> 
	</form>
</body>
</html>