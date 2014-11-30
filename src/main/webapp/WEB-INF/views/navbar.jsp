<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page session="false" %>
    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="<c:url value="/" />">Game Tree</a>
        </div>
        
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="${pageContext.request.servletPath == '/WEB-INF/views/home.jsp' ? 'active' : ''}">
            	<a href="<c:url value="/" />">Home</a>
            </li>
            <li class="${pageContext.request.servletPath == '/WEB-INF/views/create.jsp' ? 'active' : ''}">
            	<a href="<c:url value="/create" />">Create</a>
            </li>
            <li class="${pageContext.request.servletPath == '/WEB-INF/views/view.jsp' ? 'active' : ''}">
            	<a href="<c:url value="/view" />">View</a>
            </li>
            <li class="${pageContext.request.servletPath == '/WEB-INF/views/edit.jsp' ? 'active' : ''}">
            	<a href="<c:url value="/edit" />">Edit</a>
            </li>
            <li class="${pageContext.request.servletPath == '/WEB-INF/views/add.jsp' ? 'active' : ''}">
            	<a href="<c:url value="/add" />">Add</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
