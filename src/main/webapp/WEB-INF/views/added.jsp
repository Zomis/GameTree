<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ include file="header.jsp"%>
<title><fmt:message key="create.title" /></title>
</head>
<body role="document">
Node added: <c:out value="${node}"></c:out>
</body>
</html>