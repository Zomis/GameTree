<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="content-type" content="text/html;charset=utf-8" />
	<link rel="stylesheet" href="<c:url value="/resources/css/server.css" />" type="text/css" media="all" />
	<link rel="stylesheet" href="<c:url value="/resources/css/blue/style.css" />" type="text/css" media="all" />
	<link rel="stylesheet" href="<c:url value="/resources/css/jquery-ui-1.9.2.custom.min.css" />" type="text/css" media="all" />
	
    <link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet" />
 	<link href="<c:url value="/resources/css/bootstrap-theme.min.css" />" rel="stylesheet" />
	<link href="<c:url value="/resources/css/theme.css" />" rel="stylesheet" />
	<link href="<c:url value="/resources/css/jsplumb.css" />" rel="stylesheet" />
	<link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">
 
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
	
    <script src="<c:url value="/resources/js/jquery-1.9.0-min.js" /> "></script>
    <script src="<c:url value="/resources/js/jquery-ui-1.9.2.min.js" /> "></script>
    <script src="<c:url value="/resources/js/jquery.ui.touch-punch-0.2.2.min.js" /> "></script>
    <script src="<c:url value="/resources/js/jquery.jsPlumb-1.7.2-min.js" /> "></script>
    <script src="<c:url value="/resources/js/bootstrap.min.js" /> "></script>
    <script src="<c:url value="/resources/js/jquery.tablesorter.min.js" /> "></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$("table.sortable").tablesorter({ widgets: ['zebra'] });
		});
	</script>
	
	
        
	
