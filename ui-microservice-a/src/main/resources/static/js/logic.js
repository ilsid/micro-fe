(function(){
	EventBus.init('microservice_a');
	EventBus.subscribe('b_message', bServiceListener);
	EventBus.subscribe('zz_message', stubbedListener);
})();


function bServiceListener(eventData) {
	var textArea = document.getElementById("txt_area");
	textArea.value = textArea.value + eventData + '\n';
}

function stubbedListener(eventData) {
}

function broadcastMessage() {
	var msgText = document.getElementById('msg').value;
	EventBus.publish("a_message", msgText);
}