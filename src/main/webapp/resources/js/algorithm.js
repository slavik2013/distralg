/**
 * Created by home on 15.06.14.
 */
var time_start = (new Date()).getTime();
var parsedJson = JSON.parse(data);
var count = 0;
var alphabetic = 'abcdefghijklmnopqrstuvwxyz';
var synbol_array =[];
var json_answer ={};
var object_array = [];
var object = {};
for(var k = 0; k < parsedJson.tasks.length; k++){
    object = {};
    object['id'] = parsedJson.tasks[k].id;
    object['result'] ='';
    var data2 = parsedJson.tasks[k].data;
    for(var i = 0; i < alphabetic.length; i++){count = 0;for (var j = 0; j < data2.length;j++){if(data2.charAt(j).toLowerCase() == alphabetic[i])count++;}object['result'] += alphabetic[i] + ':' + count + ' , ';}object_array.push(object);}
var time_of_data_processing = (new Date()).getTime() - time_start;
json_answer['results'] = object_array;
json_answer['time'] = time_of_data_processing;
json_answer['time_start'] = parsedJson.time_start;
json_answer['data_size'] = parsedJson.data_size;
var returnJson = JSON.stringify(json_answer);
webSocket.send(returnJson);