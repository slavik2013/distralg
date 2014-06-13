package distalg.ejb;

import distalg.model.Algorithm;
import distalg.model.Data;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.websocket.Session;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by home on 26.05.14.
 */
@Startup
@Singleton
public class EJBService {

    public  Map<Session,Long> peers = Collections.synchronizedMap(new HashMap<Session, Long>());

    public Map<Session, PeerData> peerData = Collections.synchronizedMap(new HashMap<Session, PeerData>());


    @PersistenceContext(name = "distralgunit")
    private EntityManager entityManager;

    public EJBService() {

    }

    public List<Algorithm> getAlgorithm(){

        TypedQuery<Algorithm> typedQuery = entityManager.createQuery("SELECT alg FROM Algorithm alg", Algorithm.class);

        return typedQuery.getResultList() ;

    }

    public Map<Session, PeerData> getPeerData() {
        return peerData;
    }

    public void addPeerData(Session session, PeerData peerData1){
        peerData.put(session, peerData1);
    }

    public  Map<Session, Long> getPeersWithId() {
        return peers;
    }

    public Set<Session> getPeers(){
        return peers.keySet();
    }

    public void setPeers(Map<Session, Long> peers) {
        this.peers = peers;
    }

    public  void addPeer(Long id, Session session){
        peers.put(session,id);
    }

    public  void removePeer(Session session){
        peers.remove(session);
    }

    public  Set<Long> getPeersId(){
       Set<Map.Entry<Session, Long>> entrySet =  peers.entrySet();
        Set<Long> peersId = null;
        for (Map.Entry<Session, Long> sessionLongEntry : entrySet) {
            peersId.add(sessionLongEntry.getValue());
        }
        return peersId;
    }


    public void makeTask(Task task){
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(task);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Session session : peers.keySet()) {
            try {
                session.getAsyncRemote().sendText(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getAllAlgorithmJson(){
        TypedQuery<Algorithm> typedQuery = entityManager.createQuery("SELECT alg FROM Algorithm alg", Algorithm.class);

        List<Algorithm> list = typedQuery.getResultList() ;

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(out, list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final byte[] data = out.toByteArray();
        return new String(data);
    }

    public String getAllDataJson(){
        TypedQuery<Data> typedQuery = entityManager.createQuery("SELECT data FROM Data data", Data.class);

        List<Data> list = typedQuery.getResultList() ;

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(out, list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final byte[] data = out.toByteArray();
        return new String(data);
    }


    public String prepareTask(){
        Task task = new Task();
        task.setTask(getAllDataJson());

        TypedQuery<Algorithm> typedQuery = entityManager.createQuery("SELECT alg FROM Algorithm alg", Algorithm.class);

        List<Algorithm> list = typedQuery.getResultList() ;
        Algorithm singleAlgorithm = list.get(0);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String algorithmJson = "";
        try {
            algorithmJson = ow.writeValueAsString(singleAlgorithm);
        } catch (IOException e) {
            e.printStackTrace();
        }
        task.setAlgorithm(algorithmJson);
        task.setCommand("process");

        ObjectWriter ow2 = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String taskJson = "";
        try {
            taskJson = ow.writeValueAsString(task);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return taskJson;
    }


    public void sendTask(String task){

        for (Session session : peers.keySet()) {
            try {
                session.getAsyncRemote().sendText(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
