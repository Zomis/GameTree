<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ include file="header.jsp"%>
<title><fmt:message key="create.title" /></title>
</head>
<body role="document">
<%@ include file="navbar.jsp" %>
<div class="container theme-home" role="main">
	<h1><fmt:message key="create.header" /></h1>
	<form:form action="create" method="post" modelAttribute="tree">
		<form:label path="name" cssClass="sr-only">
			<fmt:message key="create.name" />
		</form:label>
		<form:input type="text" path="name" cssClass="form-control" placeholder="Name" value="Treename" />
		<input type="submit" />
	</form:form>
</div>
</body>
</html>