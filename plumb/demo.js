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
			instance.addEndpoint(windows[i], {
				isTarget: true,
				uuid:windows[i].getAttribute("id") + "e",
				anchor:"Top",
				maxConnections:-1
			});
			instance.addEndpoint(windows[i], {
				isSource: true,
				uuid:windows[i].getAttribute("id") + "s",
				anchor:"Bottom",
				maxConnections:-1
			});
		}
	
		instance.connect({uuids:["chartWindow3s", "chartWindow6e" ], detachable:true, reattach:true})
			.bind("click", function(conn) {
				instance.detach(conn);
			});
		instance.connect({uuids:["chartWindow1s", "chartWindow2e" ]});
		instance.connect({uuids:["chartWindow1s", "chartWindow3e" ]});
		instance.connect({uuids:["chartWindow2s", "chartWindow4e" ]});
		instance.connect({uuids:["chartWindow2s", "chartWindow5e" ]});
				
		instance.draggable(windows);		
	});

	jsPlumb.fire("jsPlumbDemoLoaded", instance);
});
	$(".window" ).click(function() {
		$("#node-details" ).dialog({
			title: $(this).html(),
			buttons: {
				Ok: function() {
					$( this ).dialog( "close" );
				}
			}
		});
		$("#node-description").html($(this).html());
	});

