EventBus = {
		
	SUBSCRIBE_EVENT: '__subscribe_event',	
	
	listeners: {},	
	
	componentId: null,
	
	
	init: function(componentId) {
		if (this.componentId != null) { 
			throw "Illegal State: Event Bus already initialized" 
		}
		this.componentId = componentId;
	},
		
	publish: function(eventType, message) {
		this._post(
			{
				eventId: eventType,
				componentId: this.componentId,
				body: message
			} 
		);
	},
	
	subscribe: function(eventType, listener) {
		if (!this.listeners[eventType]) {
			this.listeners[eventType] = [];
		}

		this.listeners[eventType].push(listener);
		
		this._post(
			{
				eventId: this.SUBSCRIBE_EVENT,
				componentId: this.componentId,
				eventType: eventType
			} 
		);
	},
	
	_messageListener: function(event) {
		var eventType = event.data.eventId;

		if (!eventType || !this.listeners[eventType]) {
			return;
		}
		
		if (!this.listeners[eventType]) {
			return;
		}
		
		this.listeners[eventType].forEach(function(listener) {
			listener(event.data.body);
	    });
	},
	
	_post: function(message) {
		window.parent.postMessage(message, "*");
	}
		
};


function eventBusMessageListener(event) {
	EventBus._messageListener(event);
} 


(function(){
	
	if (window.addEventListener) {
		window.addEventListener('message', eventBusMessageListener);
	} else {
		// IE8
		window.attachEvent('onmessage', eventBusMessageListener);
	}
	
})();

