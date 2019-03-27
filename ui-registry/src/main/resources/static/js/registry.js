(function() {
	
	uiRegistry = {
		OP_REGISTER: 'register',
		OP_REMOVE: 'remove',
		OP_GET_ALL_COMPONENTS: 'getAllComponents',
		OP_REFRESH_ALL_COMPONENTS: 'refreshAllComponents',
		OP_STORE_SUBSCRIBED_EVENT: 'storeSubscribedEvent',
		OP_STORE_PUBLISHED_EVENT: 'storePublishedEvent',
		
		socket: null,
		adminModel: [],
		dependencyGraph: GraphUtil.emptyGraph()
	};
	
	initWebsocketConnection();
	
	window.onbeforeunload = function() {
		uiRegistry.socket.onclose = function () {};
		uiRegistry.socket.close();
	};
	
	EventRouter.addSubscriptionListener(storeSubscriptionEventInRegistry);
	EventRouter.addPublishingListener(storePublishingEventInRegistry);

	var getAllRequest = {
		operation: uiRegistry.OP_GET_ALL_COMPONENTS	
	}; 
	
	sendMessageWhenConnectionIsReady(uiRegistry.socket, JSON.stringify(getAllRequest));
	
})();


function initWebsocketConnection() {
	uiRegistry.socket = new WebSocket(getSocketUrl());
	uiRegistry.socket.onmessage = componentHandler;
	uiRegistry.socket.onclose = function() {
		setTimeout(initWebsocketConnection, 1000);
		console.log("Websocket connection is restored");
	};
}

function getSocketUrl() {		
	var socketPort = window.location.port || '80';
	
	var pathName = '/' + StdUtil.trimSlashes(window.location.pathname);
	pathName = (pathName == '/') ? '' : pathName;
	
	var socketUrl = 'ws://' + StdUtil.trimTrailingSlash(window.location.hostname) 
		+ ':' + socketPort + pathName + '/registryTopic';
	
	return socketUrl;
}

function componentHandler(event) {
	var data = JSON.parse(event.data);

	if (data.operation == uiRegistry.OP_REGISTER) {
		createIframe(data);
	}
	else if (data.operation == uiRegistry.OP_REMOVE) {
		StdUtil.removeByPropertyValue(uiRegistry.adminModel, 'name', data.componentId);
		GraphUtil.removeNodeAndLinks(uiRegistry.dependencyGraph, data.componentId);
		var iframe = document.getElementById(data.componentId);
		iframe.parentNode.removeChild(iframe);
	}
	else if (data.operation == uiRegistry.OP_GET_ALL_COMPONENTS) {
		data.payload.forEach(function(item) {
			createIframe(item);
		});
		populateAdminModel(data.payload);
	} else if (data.operation == uiRegistry.OP_REFRESH_ALL_COMPONENTS) {
		populateAdminModel(data.payload);
	}
}

function createIframe(item) {
	if (!uiConfig[item.componentId]) {
		return;
	}
	
	var iframe = document.createElement('iframe');
	iframe.setAttribute('id', item.componentId);
	iframe.setAttribute('src', item.uri);
	iframe.setAttribute('scrolling', 'no');
	iframe.setAttribute('frameBorder', '0');
	var width = uiConfig[item.componentId].width;
	var height = uiConfig[item.componentId].height;
	iframe.setAttribute('width', width);
	iframe.setAttribute('height', height);
	var containerId = uiConfig[item.componentId].containerId;
	var container = document.getElementById(containerId);
	container.appendChild(iframe);
	
	return iframe;
}

function waitForSocketConnection(socket, callback, attempts){
    var cnt = attempts || 0;
    
    if (cnt === 20) {
    	console.log('Websocket connection failed after ' + cnt + ' attempts');
    	return;
    }
    
	setTimeout(
        function () {
            if (socket.readyState === 1) {
                console.log('Websocket connection is established');
                callback();
                return;

            } else {
                console.log('Waiting for Websocket connection...');
                waitForSocketConnection(socket, callback, ++cnt);
            }

        }, 500); 
}

function sendMessageWhenConnectionIsReady(socket, msg) {
    waitForSocketConnection(socket, function() {
    	socket.send(msg);
    });
}


function populateAdminModel(registryItems) {
	uiRegistry.adminModel = [];
	uiRegistry.dependencyGraph = GraphUtil.emptyGraph();
	var graph = uiRegistry.dependencyGraph;
	var subscribers = {};
	var publishers = {};
	
	registryItems.forEach(function(item) {
		var adminModelItem = {
			name: item.componentId,
			url: item.uri,
			subscribedEvents: '',
			publishedEvents: ''
		};
		
		graph.nodes.push({
			id: item.id,
			name: item.componentId
		});
		
		item.subscribedEvents.forEach(function(evt) {
			var comma = adminModelItem.subscribedEvents.length == 0 ? '' : ', ';
			adminModelItem.subscribedEvents += comma + evt.typeName;
			
			if (!subscribers[evt.typeName]) {
				subscribers[evt.typeName] = [];
			}
			subscribers[evt.typeName].push({ id: item.id });
		});
		
		item.publishedEvents.forEach(function(evt) {
			var comma = adminModelItem.publishedEvents.length == 0 ? '' : ', ';
			adminModelItem.publishedEvents += comma + evt.typeName;
			
			if (!publishers[evt.typeName]) {
				publishers[evt.typeName] = [];
			}
			publishers[evt.typeName].push({ id: item.id });
		});
		
		uiRegistry.adminModel.push(adminModelItem);
	});
	
	for (prop in publishers) {
		if (publishers.hasOwnProperty(prop)) {
	        var evtName = prop;
	        publishers[evtName].forEach(function(pub) {
	        	if (subscribers[evtName]) {
					subscribers[evtName].forEach(function(sub) {
						graph.links.push({
							source: pub.id,
							target: sub.id,
							message: evtName
						});
					});
				}
	        });
	    }
	}
	
	window.tabContainer.items = uiRegistry.adminModel;
	GraphUtil.drawGraph(graph);
}

function storeSubscriptionEventInRegistry(data) {
	var storeEventRequest = {
		operation: uiRegistry.OP_STORE_SUBSCRIBED_EVENT,	
		componentId: data.componentId,
		eventType: data.eventType
	};
	
	uiRegistry.socket.send(JSON.stringify(storeEventRequest));
};

function storePublishingEventInRegistry(data) {
	var storeEventRequest = {
		operation: uiRegistry.OP_STORE_PUBLISHED_EVENT,	
		componentId: data.componentId,
		eventType: data.eventType
	};
	
	uiRegistry.socket.send(JSON.stringify(storeEventRequest));
};
