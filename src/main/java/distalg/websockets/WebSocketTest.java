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

       System.out.println("onMessage  - " + message);

        System.out.println("message peers " + ejbService.getPeers());

        if(ejbService.getPeers() != null && !ejbService.getPeers().isEmpty())
            System.out.println("message from client # " + ejbService.getPeers().get(session));

        long time_returned = System.currentTimeMillis();

        if(message != null && message.equals("start"))
             session.getAsyncRemote().sendText("{\"callback\":"+"\"start\""+"}");

        else{
            JSONObject obj = new JSONObject(message);


            Long time = Long.valueOf(""+obj.get("time"));
            Long time_start = Long.valueOf(""+obj.get("time_start"));

            Long all_time = time_returned - time_start;
            Long data_size = Long.valueOf("" + obj.get("data_size"));

            System.out.println("onMessage time =" + time);
            System.out.println("onMessage time_start =" + time_start);
            System.out.println("onMessage all_time =" + all_time);

            PeerData peerData = ejbService.getPeerDataBySession(session);
            System.out.println("peerData = " + peerData);
            if(peerData != null){
                peerData.setTimeConnection(data_size/(all_time - time));
                peerData.setSpeed(1000*data_size/time);
                peerData.calculateGeneralSpeed();
            }

            JsonObject answer = Json.createObjectBuilder()
                    .add("time", time)
                    .add("all_time", all_time)
                    .add("speed",1000*data_size/time)
                    .add("connection_speed",data_size/(all_time - time))
                    .add("data_size",data_size)
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
        PeerData peerData = new PeerData();
        peerData.setSession(session);
        ejbService.addPeer(System.nanoTime(), session);
        ejbService.addPeerData(session, peerData);

        session.getAsyncRemote().sendText("web socket on open server");
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed");
        ejbService.removePeer(session);
    }
}
