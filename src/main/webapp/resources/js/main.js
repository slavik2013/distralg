/**
 * Created by home on 27.05.14.
 */
var webSocket;
var port =":8000";

var url  = 'ws://localhost:8080/distralg/websocket';
//url = 'ws://testapp-slavik2013.rhcloud.com:8000/distralg/websocket';

webSocket =  new WebSocket(url);

webSocket.onerror = function(event) {
    onError(event)
};

webSocket.onopen = function(event) {
    onOpen(event)
};

webSocket.onmessage = function(event) {
    onMessage(event)
};

var data;
var parseData;
function onMessage(event) {
    data = event.data;
    parseData = JSON.parse(data);
    document.getElementById('messages').innerHTML += '<br />' + data;
//    document.getElementById('messages').innerHTML
//        += '<br />' + data;
//    document.getElementById('messages').innerHTML
//        += '<br />' + parseData;
//    document.getElementById('messages').innerHTML
//        += '<br />' + parseData.command;
//    document.getElementById('messages').innerHTML
//        += '<br />' + parseData.algorithm.algorithm;
//    document.getElementById('messages').innerHTML
//        += '<br />' + parseData.data;

    if(parseData.hasOwnProperty('command') && parseData.command == 'process')
        eval(parseData.algorithm.algorithm);

    else
        document.getElementById('messages').innerHTML += '<br />' + data;

}

function onOpen(event) {
    document.getElementById('messages').innerHTML = 'Connection established' ;
    //alert(url);
}

function onError(event) {
    alert(event.data);
}

function start() {
    webSocket.send('start');
    return false;
}


