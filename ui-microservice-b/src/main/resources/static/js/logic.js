(function(){
	EventBus.init('microservice_b');
	EventBus.subscribe('a_message', aServiceListener);
})();


function aServiceListener(eventData) {
	var textArea = document.getElementById('txt_area');
	textArea.value = textArea.value + eventData + '\n';
}

function broadcastMessage() {
	var msgText = document.getElementById('msg').value;
	EventBus.publish("b_message", msgText);
}

