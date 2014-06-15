package distalg.websockets;

/**
 * Created by home on 26.05.14.
 */

import distalg.ejb.EJBService;
import distalg.ejb.PeerData;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint("/websocket")
public class WebSocketTest {

    @Inject
    EJBService ejbService;

    @OnMessage
    public void onMessage(String message, Session session)
            throws IOException, InterruptedException {
       System.out.println("onMessage 2 - " + message);

        long time_returned = System.currentTimeMillis();

        if(message != null && message.equals("start"))
             session.getAsyncRemote().sendText("{\"callback\":"+"\"start\""+"}");

        else{
        JSONObject obj = new JSONObject(message);

//        Long time = (Long)(obj.get("time"));
//        Long time_start = (Long)(obj.get("time_start"));

            Long time = Long.valueOf(""+obj.get("time"));
            Long time_start = Long.valueOf(""+obj.get("time_start"));

            Long all_time = time_returned - time_start;
            System.out.println("onMessage time =" + time);
            System.out.println("onMessage time_start =" + time_start);
            System.out.println("onMessage all_time =" + all_time);

            JsonObject answer = Json.createObjectBuilder()
                    .add("time", time)
                    .add("all_time", all_time)
                    .build();

        session.getAsyncRemote().sendText(answer.toString());

//        List<String> list = new ArrayList<String>();
//        JSONArray array = obj.getJSONArray("interests");
//        for(int i = 0 ; i < array.length() ; i++){
//            list.add(array.getJSONObject(i).getString("interestKey"));
//        }

        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Client connected");
        ejbService.addPeer(System.nanoTime(), session);
        ejbService.addPeerData(session, new PeerData());
        session.getAsyncRemote().sendText("web socket on open server");
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed");
        ejbService.removePeer(session);
    }
}
