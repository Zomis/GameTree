<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<%@ include file="header.jsp" %>
<title><fmt:message key="create.title" /></title>
</head>
<body role="document">
Tree created! <c:out value="${tree}"></c:out>
</body>
</html>