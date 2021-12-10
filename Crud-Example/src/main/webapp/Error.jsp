<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<style>
	div{text-align: center;}
</style>
<title>Error</title>
</head>
<body>

	<div>
	
		<h1>Error</h1>
		<h2> <%= exception.getMessage() %>   </h2>
	</div>
</body>
</html>