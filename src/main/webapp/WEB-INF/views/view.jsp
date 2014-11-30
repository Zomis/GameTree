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
#chartWindow1 { left:20em; top:6em;}
#chartWindow2 { left:10em;top:18em;}
#chartWindow3 { left:40em;top:18em;}
#chartWindow4 { left:4em;top:30em;}
#chartWindow5 { left:22em;top:30em;}
#chartWindow6 { left:47em;top:30em;}
#chartWindow7 { top:18em;left:46em;}
#chartWindow8 { left:63em;top:38em;}
	</style>
</head>
<body role="document">
	<div id="main">
		<!-- demo -->
		<div class="demo chart-demo" id="game-tree">
			<div class="window" id="chartWindow1">window one</div>
            <div class="window" id="chartWindow2">window two</div>
            <div class="window" id="chartWindow3">window three</div>
            <div class="window" id="chartWindow4">window four</div>
            <div class="window" id="chartWindow5">window five</div>
            <div class="window" id="chartWindow6">window six</div>
        </div>
        <!-- /demo -->
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
		
			instance.connect({uuids:["chartWindow3e", "chartWindow6e" ], overlays:overlays, detachable:true, reattach:true});
			instance.connect({uuids:["chartWindow1e", "chartWindow2e" ], overlays:overlays});
			instance.connect({uuids:["chartWindow1e", "chartWindow3e" ], overlays:overlays});
			instance.connect({uuids:["chartWindow2e", "chartWindow4e" ], overlays:overlays});
			instance.connect({uuids:["chartWindow2e", "chartWindow5e" ], overlays:overlays});
					
			instance.draggable(windows);		
		});

		jsPlumb.fire("jsPlumbDemoLoaded", instance);
	});
	</script>
Tree name is ${tree.name}<br />
Tree id is ${tree.id}<br />
${tree}
</body>
</html>
