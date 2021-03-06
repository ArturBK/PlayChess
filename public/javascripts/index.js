
var mapSocket = null;
var senderUid = null;

$(document).ready(function() {

	var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
    mapSocket = new WS($("body").data("ws-url"))

    mapSocket.onopen = function(){
	    mapSocket.send(JSON.stringify({msg: "gg"}))
    }

	mapSocket.onmessage = function(event) {
		var message = JSON.parse(event.data)
		senderUid = message.uid;

		if (message.newObj == null){

	        $("tbody").append("<tr><td class='chat-line'>" + message.uid + "</td><td class='chat-line'>" + message.msg + "</td></tr>")
    		$("#msgtext").val("")
		}
		else{
			$("#fenFromSocket").text(message.fen)
			loadFen(message.oldObj, message.newObj, $("#uid").text(), senderUid)
		}
    }

	var onalert = function(event) {
        //$(".alert").removeClass("hide")
    }
	mapSocket.onerror = onalert
	mapSocket.onclose = onalert

    $( "#msgform" ).submit(function( event ) {
	    event.preventDefault()
    	mapSocket.send(JSON.stringify({msg: $("#msgtext").val()}))
	});
})

function sendFen(oldPos, newPos, fen) {
	if ($("#uid").text() == 1 && fen.split(" ")[1] == "b") {
		mapSocket.send(JSON.stringify({oldPos: oldPos, newPos: newPos}))
	}
	else if ($("#uid").text() == 2 && fen.split(" ")[1] == "w") {
		mapSocket.send(JSON.stringify({oldPos: oldPos, newPos: newPos}))
	}
};