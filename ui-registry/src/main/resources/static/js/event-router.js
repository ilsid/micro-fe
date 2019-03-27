EventRouter = {
	
	SUBSCRIBE_EVENT: '__subscribe_event',
	
	subscribers: {},
	
	publishers: {},
	
	subscriptionListeners: [],
	
	publishingListeners: [],
	
	addSubscriptionListener: function(listener) {
		this.subscriptionListeners.push(listener);
	},
	
	addPublishingListener: function(listener) {
		this.publishingListeners.push(listener);
	},
	
	_messageListener: function(event) {
		if (!event.data.eventId) {
			return;
		}
		
		if (event.data.eventId === this.SUBSCRIBE_EVENT) {
			var eventType = event.data.eventType;
			var componentId = event.data.componentId;
			
			if (!this.subscribers[eventType]) {
				this.subscribers[eventType] = [];
			}
			
			this.subscribers[eventType].push(componentId);
			var iframe = document.getElementById(componentId);
			var url = iframe.src;
			
			var listenerData = {
				componentId: componentId,
				url: url,
				eventType: eventType
			};
			
			this.subscriptionListeners.forEach(function(listener) {
				listener(listenerData);
			});
			
		} else {
			var eventType = event.data.eventId;
			var componentId = event.data.componentId;
			
			if (!this.publishers[eventType]) {
				this.publishers[eventType] = [];
			}
			
			var publisherRegistered = this.publishers[eventType].some(function(p) {
				return p.id == componentId;
			});
			
			if (!publisherRegistered) {
				this.publishers[eventType].push({'id': componentId });
				
				var listenerData = {
					componentId: componentId,
					eventType: eventType
				};
				
				this.publishingListeners.forEach(function(listener) {
					listener(listenerData);
				});
			}

			var iframes = [].slice.call(document.getElementsByTagName("iframe"));
			var subscribers = this.subscribers[eventType];
			
			if (subscribers) {
				subscribers.forEach(function(componentId) {
					function targetIframe(e) {
					    return e.id == componentId;
					}
					
					var iframe = iframes.filter(targetIframe)[0];
					iframe.contentWindow.postMessage( 
						{
							eventId: eventType,
							componentId: componentId,
							body: event.data.body
						}, "*"
					);
				});
			}
		}


	}
		
};


function eventRouterMessageListener(event) {
	EventRouter._messageListener(event);
} 


(function() {
	
	if (window.addEventListener) {
		window.addEventListener('message', eventRouterMessageListener);
	} else {
		// IE8
		window.attachEvent('onmessage', eventRouterMessageListener);
	}
	
})();