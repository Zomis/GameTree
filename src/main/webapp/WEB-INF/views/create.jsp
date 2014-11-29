<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ include file="header.jsp"%>
<title><fmt:message key="create.title" /></title>
</head>
<body role="document">

	<form:form action="create" method="post" modelAttribute="tree">
		<form:label path="name">
			<fmt:message key="create.name" />
		</form:label>
		<form:input type="text" path="name" value="Treename" />
		<input type="submit" />
	</form:form>
</body>
</html>