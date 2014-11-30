<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<%@ include file="header.jsp"%>
	<title><fmt:message key="view.title" /></title>
	<style>
	
	/** ELEMENTS **/
.chart-demo .window { 
	border:0.1em dotted #d4e06b; 	
	width:14em; height:4em; 	
	line-height:4em;	
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
	</style>
</head>
<body role="document">
	<div class="container theme-home" role="main">
	
	</div>


	<div id="main">
		<!-- demo -->
		<div class="demo chart-demo" id="game-tree">
			<c:forEach items="${nodes}" var="node">
				<div class="window" data-tags="${node.tagNames()}" id="chartWindow${node.getId()}">${node}</div>
			</c:forEach>
        </div>
        <!-- /demo -->
	</div>
	
	<div id="node-details" style="display: none;">
		<p id="node-description">Details</p>
		<div id="node-tags"></div>
	</div>
	

	<script>
	jsPlumb.ready(function() {			

		var color = "gray";

		var instance = jsPlumb.getInstance({
			// notice the 'curviness' argument to this Bezier curve.  the curves on this page are far smoother
			// than the curves on the first demo, which use the default curviness value.			
			Connector : [ "Bezier", { curviness:50 } ],
			DragOptions : { cursor: "pointer", zIndex:2000 },
			PaintStyle : { strokeStyle:color, lineWidth:2 },
			EndpointStyle : { radius:9, fillStyle:color },
			HoverPaintStyle : {strokeStyle:"#ec9f2e" },
			EndpointHoverStyle : {fillStyle:"#ec9f2e" },
			Container:"game-tree"
		});
			
		// suspend drawing and initialise.
		instance.doWhileSuspended(function() {		
			// declare some common values:
			var arrowCommon = { foldback:0.7, fillStyle:color, width:14 },
				// use three-arg spec to create two different arrows with the common values:
				overlays = [
					[ "Arrow", { location:0.7 }, arrowCommon ]
				];

			// add endpoints, giving them a UUID.
			// you DO NOT NEED to use this method. You can use your library's selector method.
			// the jsPlumb demos use it so that the code can be shared between all three libraries.
			var windows = jsPlumb.getSelector(".chart-demo .window");
			for (var i = 0; i < windows.length; i++) {
				instance.addEndpoint(windows[i], {
					uuid:windows[i].getAttribute("id") + "e",
					anchor:"Center",
					maxConnections:-1
				});
			}
		
			<c:forEach items="${connections}" var="connection" varStatus="itstatus">
				instance.connect({uuids:["chartWindow${connection.getFrom()}e", "chartWindow${connection.getTo()}e" ], overlays:overlays});
			</c:forEach>
			instance.draggable(windows);		
		});

		jsPlumb.fire("jsPlumbDemoLoaded", instance);
	});
	$(".window" ).click(function() {
		$("#node-details").dialog({
			title: $(this).html(),
			buttons: {
				Ok: function() {
					$(this).dialog( "close" );
				}
			}
		});
		$("#node-description").html($(this).html());
		var tags = $(this).data('tags').split(" ");
		var tagHtml = "";
		for (var i = 0; i < tags.length; i++) {
			tagHtml += "<span class=\"node-tag\">" + tags[i] + "</span>";
		}
		$("#node-tags").html(tagHtml);
	});
	</script>
</body>
</html>
