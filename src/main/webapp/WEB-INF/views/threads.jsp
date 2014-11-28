<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.zomis.net/tld" prefix="z" %>
<%@ page session="false" %>

<%@ include file="header.jsp" %>
<title><fmt:message key="home.title" /></title>
</head>
<body>
<table>
<c:forEach items="${threads.entrySet()}" var="thread" >
	<tr>
		<td>${thread.getKey().getName()}</td>
		<td>
			<table>
			<c:forEach items="${thread.getValue()}" var="trace" >
				<tr>
					<td>${trace}</td>
				</tr>
			</c:forEach>
			</table>
		</td>
	</tr>
</c:forEach>
</table>
