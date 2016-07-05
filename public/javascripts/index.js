$(document).ready(function() {
	// hideable alert thanks to Twitter Bootstrap
	//$(".alert").alert()
    console.log("asdasdsad")
	// map initialization
	// open a WebSocket
	var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket
    var mapSocket = new WS($("body").data("ws-url"))
	mapSocket.onmessage = function(event) {
		console.log("data")
		console.log(event.data)
		$('#resposta').text(event.data)

		var message = JSON.parse(event.data)


        $("tbody").append("<tr><td class='chat-line'>" + message.uid + "</td><td class='chat-line'>" + message.msg + "</td></tr>")
    	$("#msgtext").val("")

    }

	var onalert = function(event) {
        //$(".alert").removeClass("hide")
    }
	mapSocket.onerror = onalert
	mapSocket.onclose = onalert

    $( "#msgform" ).submit(function( event ) {
	    event.preventDefault()
	    console.log($("#msgtext").val())
    	mapSocket.send(JSON.stringify({msg: $("#msgtext").val()}))
	});

})


