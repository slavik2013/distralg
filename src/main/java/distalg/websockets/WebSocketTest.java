package distalg.websockets;

/**
 * Created by home on 26.05.14.
 */

import distalg.ejb.EJBService;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/websocket")
public class WebSocketTest {

    @Inject
    EJBService ejbService;

    @OnMessage
    public void onMessage(String message, Session session)
            throws IOException, InterruptedException {
        System.out.println("onMessage");
        session.getAsyncRemote().sendText("alert('OK');");
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Client connected");
        ejbService.addPeer(System.nanoTime(),session);
        session.getAsyncRemote().sendText("web socket on open server");
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed");
        ejbService.removePeer(session);
    }
}
