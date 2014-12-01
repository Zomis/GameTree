<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ include file="header.jsp"%>
	<title><fmt:message key="view.title" /></title>
	<style>
	
	/** ELEMENTS **/
.window { 
	border:0.1em dotted #d4e06b; 	
	width: 180px;
	height: 50px; 	
	line-height: 1em;
}

/** HOVER EFFECTS **/
.chart-demo .window:hover, .chart-demo .window._jsPlumb_source_hover, .chart-demo .window._jsPlumb_target_hover {
    border:1px solid orange;
    color:orange;
}

/** ELEMENT POSITIONS **/
<c:forEach items="${nodePositions}" var="node">
	#chartWindow${node.id} {
		left: ${node.x}px;
		top: ${node.y}px;
	}
</c:forEach>

.node-edit, .node-details {
	display: none;
}
	</style>
</head>
<body role="document">
<%@ include file="navbar.jsp" %>

	<div class="container theme-home" role="main">
	
	</div>


	<div id="main">
		<!-- demo -->
		<div class="demo chart-demo" id="game-tree">
			<c:forEach items="${nodes}" var="node">
				<div class="window" data-tags="${node.tagNames()}" data-node="${node.id}" id="chartWindow${node.getId()}">
					<div class="node-info">
						<span class="node-name">${node}</span>
					</div>
					<div class="node-details">
						<c:if test="${editmode}">
							<span class="details-edit"><a href="#" onclick="nodeEdit('${node.id}')">Edit</a></span>
						</c:if>
						<c:if test="${not editmode}">
							<div class="node-tags">(tags)</div>
						</c:if>
					</div>
					<c:if test="${editmode}">
						<div class="node-edit">
							<form method="post" action="<c:url value="/edit/node" />">
								<input class="form-name" name="name" type="text" placeholder="Name" value="${node.name}" />
								<input class="form-tags" name="tags" type="text" placeholder="Tags" value="${node.tagNames()}" />
								<div class="edit-submit">
									<input type="button" value="Save" onclick="saveNode(this)" />
									<input type="button" value="Cancel" onclick="cancelEditNode()" />
								</div>
							</form>
						</div>
					</c:if>
				</div>
			</c:forEach>
        </div>
        <!-- /demo -->
	</div>
	
	<div id="node-details" style="display: none;">
		<p id="node-description">Details</p>
		<div id="node-tags"></div>
	</div>
	


	<script>
	<c:if test="${editmode}">
	function saveNode(obj) {
		var node = $(obj).parents(".window");
		var nodeId = node.data('node');
		var nodeName = node.find('.form-name').val();
		var nodeTags = node.find('.form-tags').val();
		alert("save " + nodeId + " - " + nodeName + " - " + nodeTags);
		$.ajax({
			type: "POST",
			url: "<c:url value="/edit/node" />",
			data: { tree: ${treeId}, node: nodeId, name: nodeName, tags: nodeTags  }
		})
		.done(function( msg ) {
			alert( "Node saved: " + msg );
		})
		.fail(function(jqXHR, textStatus) {
			alert("Error saving: " + jqXHR + ", " + textStatus);
		});
	}
	
	function cancelEditNode() {
		$(".node-edit").hide();
		$(".window").animate({
			width: "180px",
			height: "50px"
		}, 500);
		$(".node-info").show();
		
	}
	
	function nodeEdit(id) {
		$(".node-edit").hide();
		$(".node-info").show();
		var editNode = $(".window[data-node=" + id + "]");
		$(".window").not(editNode).animate({
			width: "180px",
			height: "50px"
		}, 500);
		editNode.find(".node-edit").show();
		editNode.find(".node-info").hide();
		$(editNode).animate({
			width: "250px",
			height: "100px"
		}, 500);
	}
	
	function detatchConnection(plumb, conn) {
		var sourceId = $("#" + conn.sourceId).data('node');
		var targetId = $("#" + conn.targetId).data('node');
		$.ajax({
			type: "POST",
			url: "<c:url value="/edit/connection/remove" />",
			data: { tree: ${treeId}, from: sourceId, to: targetId }
		})
		.done(function( msg ) {
			alert( "Removed connection: " + msg );
		})
		.fail(function(jqXHR, textStatus) {
			alert("Error removing connection: " + jqXHR + ", " + textStatus);
		});
		plumb.detach(conn);
	}
	
	function attachConnection(conn) {
        conn.bind("click", function(conn) {
        	detatchConnection(instance, conn);
        });
		var sourceId = $("#" + conn.sourceId).data('node');
		var targetId = $("#" + conn.targetId).data('node');
		$.ajax({
			type: "POST",
			url: "<c:url value="/edit/connection/add" />",
			data: { tree: ${treeId}, from: sourceId, to: targetId }
		})
		.done(function( msg ) {
			alert( "Added connection: " + msg );
		})
		.fail(function(jqXHR, textStatus) {
			alert("Error adding connection: " + jqXHR + ", " + textStatus);
		});
	}
	
	</c:if>
	
	
	jsPlumb.ready(function() {			

		var color = "gray";

	    var arrowCommon = { foldback:0.7, fillStyle:color, width:14 };
		var instance = jsPlumb.getInstance({
			// notice the 'curviness' argument to this Bezier curve.  the curves on this page are far smoother
			// than the curves on the first demo, which use the default curviness value.			
	        Connector : [ "Straight", {  } ],
			DragOptions : { cursor: "pointer", zIndex:2000 },
			PaintStyle : { strokeStyle:color, lineWidth:2 },
			EndpointStyle : { radius:9, fillStyle:color },
			HoverPaintStyle : {strokeStyle:"#ec9f2e" },
			EndpointHoverStyle : {fillStyle:"#ec9f2e" },
			Container:"game-tree",
	        ConnectionOverlays : [ ["Arrow", { location: 0.5 }, arrowCommon ] ]
		});
			
		// suspend drawing and initialise.
		instance.doWhileSuspended(function() {		
			// add endpoints, giving them a UUID.
			// you DO NOT NEED to use this method. You can use your library's selector method.
			// the jsPlumb demos use it so that the code can be shared between all three libraries.
			var windows = jsPlumb.getSelector(".chart-demo .window");
			for (var i = 0; i < windows.length; i++) {
				<c:if test="${editmode}">
				instance.addEndpoint(windows[i], {
					isSource: true,
					uuid:windows[i].getAttribute("id") + "s",
					anchor:"Bottom",
					maxConnections:-1
				});
				instance.addEndpoint(windows[i], {
					isTarget: true,
					uuid:windows[i].getAttribute("id") + "e",
					anchor:"Top",
					maxConnections:-1
				});
				</c:if>
				<c:if test="${not editmode}">
				instance.addEndpoint(windows[i], {
					uuid:windows[i].getAttribute("id") + "e",
					anchor:"Center",
					maxConnections:-1
				});
				</c:if>
			}
		
			<c:forEach items="${connections}" var="connection" varStatus="itstatus">
				<c:if test="${editmode}">
					instance.connect({uuids:["chartWindow${connection.getFrom()}s", "chartWindow${connection.getTo()}e" ]}).bind("click", function(conn) {
						detatchConnection(instance, conn);
					});
				</c:if>
				<c:if test="${not editmode}">
					instance.connect({uuids:["chartWindow${connection.getFrom()}e", "chartWindow${connection.getTo()}e" ]});
				</c:if>
			</c:forEach>
			instance.draggable(windows);		
			
	        instance.bind("connection", function(info) {
	        	attachConnection(info.connection);
	        });
		});

		jsPlumb.fire("jsPlumbDemoLoaded", instance);
	});
	$(".window" ).click(function() {
		$(".node-details").hide();
		$(this).find(".node-details").show();

/*		$("#node-details").dialog({
			title: $(this).html(),
			buttons: {
				Ok: function() {
					$(this).dialog( "close" );
				}
			}
		});
		$("#node-description").html($(this).html());*/
		var tags = $(this).data('tags').split(" ");
		var tagHtml = "";
		for (var i = 0; i < tags.length; i++) {
			tagHtml += "<span class=\"node-tag\">" + tags[i] + "</span>";
		}
		$(this).find(".node-tags").html(tagHtml);
	});
	</script>
</body>
</html>
