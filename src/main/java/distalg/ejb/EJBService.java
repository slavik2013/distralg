package distalg.ejb;

import distalg.model.Algorithm;
import distalg.model.Data;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonWriter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.websocket.Session;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Startup
@Singleton
public class EJBService {

    public  Map<Session,Long> peers = Collections.synchronizedMap(new HashMap<Session, Long>());

    public Map<Session, PeerData> peerData = Collections.synchronizedMap(new HashMap<Session, PeerData>());

    public Map<Session, LinkedList<Task>> peersTaskQueue = Collections.synchronizedMap(new HashMap<Session, LinkedList<Task>>());

    @PersistenceContext(name = "distralgunit")
    private EntityManager entityManager;

    public EJBService() {

    }

    public void clearData(){
        peers.clear();
        peerData.clear();
        peersTaskQueue.clear();
    }

    public List<Algorithm> getAlgorithm(){

        TypedQuery<Algorithm> typedQuery = entityManager.createQuery("SELECT alg FROM Algorithm alg", Algorithm.class);

        return typedQuery.getResultList() ;

    }

    public Map<Session, PeerData> getPeerData() {
        return peerData;
    }

    public PeerData getPeerDataBySession(Session session) {
        if(peerData != null && !peerData.isEmpty()){
            return peerData.get(session);
        }
            return null;
    }

    public void addPeerData(Session session, PeerData peerData1){
        peerData.put(session, peerData1);
        peersTaskQueue.put(session, new LinkedList<Task>());
    }

    public  Map<Session, Long> getPeersWithId() {
        return peers;
    }

    public Set<Session> getPeersSet(){
        return peers.keySet();
    }

    public Map<Session,Long> getPeers(){
        return peers;
    }

    public void setPeers(Map<Session, Long> peers) {
        this.peers = peers;
    }


    public  void removePeer(Session session){
        peers.remove(session);
    }

    public  void addPeer(Long id, Session session){
        peers.put(session,id);
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

       return getJson(list);
    }

    public String getAllDataJson(){
        TypedQuery<Data> typedQuery = entityManager.createQuery("SELECT data FROM Data data", Data.class);

        List<Data> list = typedQuery.getResultList();



        return getJson(list);
    }

    public String getJson( Object object){
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(out, object);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final byte[] data = out.toByteArray();
        return new String(data);
    }

    public String filterString(String str){
        return str.replace("\\\"","\"").replace("\"[","[").replace("]\"","]");
    }

    public void makeMeasure(int limit){
        Task task = new Task();
        TypedQuery<Data> typedQueryData;
        if(limit > 0)
          typedQueryData = entityManager.createQuery("SELECT data FROM Data data", Data.class).setMaxResults(limit);
        else
          typedQueryData = entityManager.createQuery("SELECT data FROM Data data", Data.class);


        List<Data> list = typedQueryData.getResultList();

        int size =  0;
        for (int i = 0; i < list.size(); i++) {
            System.out.println("size vidn = " + list.get(i).size_vidnosn);
            size += list.get(i).getSize();
        }
        task.data_size = Long.valueOf(size);
        System.out.println("size all = " + task.data_size);
        task.setTask(filterString(getJson(list)));

        TypedQuery<Algorithm> typedQuery = entityManager.createQuery("SELECT alg FROM Algorithm alg", Algorithm.class);

        task.setAlgorithm(filterString(getJson(typedQuery.getSingleResult())));
        task.setCommand("process");
        task.setTimeStart(System.currentTimeMillis());

        sendTask(getJson(task));
    }



    public boolean ckechForReady(){
        for(Map.Entry<Session, PeerData> entry: peerData.entrySet()) {
            if(entry.getValue().speed  <= 0)
                return false;
        }
        return true;
    }


    public void prepareTask(){
//        if(!ckechForReady()){
//             makeMeasure();
//        }
        Task task = new Task();
        TypedQuery<Data> typedQueryData = entityManager.createQuery("SELECT data FROM Data data", Data.class);

        List<Data> list = typedQueryData.getResultList();

        Collections.sort(list);

        int max_size = list.get(0).getSize();

        for (int i = 0; i < list.size(); i++) {
            list.get(i).size_vidnosn = 100 * list.get(i).getSize()/max_size;
        }

        List<PeerData> peerDataList = new ArrayList<>();
        for(Map.Entry<Session, PeerData> entry: peerData.entrySet()) {
            peerDataList.add(entry.getValue());
        }

        Collections.sort(peerDataList);

        for (int i = 0; i < peerDataList.size(); i++) {
            System.out.println("peerDataList = " + peerDataList.get(i).general_speed);
        }

        int size =  0;
        for (int i = 0; i < list.size(); i++) {
            System.out.println("size vidn = " + list.get(i).size_vidnosn);
            size += list.get(i).getSize();
        }

        TypedQuery<Algorithm> typedQuery = entityManager.createQuery("SELECT alg FROM Algorithm alg", Algorithm.class);

        String algorithmJson = filterString(getJson(typedQuery.getSingleResult()));

        ListIterator<Data> dataListIterator = list.listIterator();

        while(dataListIterator.hasNext()){
            for (int i = 0; i < peerDataList.size(); i++) {
                if(dataListIterator.hasNext()){
                    PeerData peerData1 = peerDataList.get(i);
                    Session session = peerData1.getSession();
                    if(peersTaskQueue.get(session).isEmpty()){
                        Task task2 = new Task();
                        task2.setAlgorithm(algorithmJson);
                        task2.setCommand("process");
                        task.setTimeStart(System.currentTimeMillis());
                        peersTaskQueue.get(session).add(task2);
                    }
                    Task task1 = peersTaskQueue.get(session).getFirst();
                    task1.getTasks_list().add(dataListIterator.next());
                }

            }
        }

        for (int i = 0; i < peerDataList.size(); i++) {
            PeerData peerData1 = peerDataList.get(i);
            Session session = peerData1.getSession();
            Task task1 = peersTaskQueue.get(session).getFirst();
            List<Data> datas = task1.getTasks_list();
            task1.setTask(filterString(getJson(datas)));
        }

        sendTask();

    }


    public String filterTaskString(String string){
        string = string.replace("\\\"","\"");
        string = string.replace("\"{","{");
        string = string.replace("\"[","[");
        string = string.replace("]\"","]");
        string = string.replace("}\"","}");
        string = string.replace("\\\\r"," ");
        string = string.replace("\\\\n"," ");

        return string;
    }

    public void sendTask(String task){
        time_send = System.currentTimeMillis();
        String taskFiltered = filterTaskString(task);

        for (Session session : peers.keySet()) {
           session.getAsyncRemote().sendText(taskFiltered);
        }
    }

    long time_send = 0;

    public long getTime_send() {
        return time_send;
    }

    public void setTime_send(long time_send) {
        this.time_send = time_send;
    }

    public void sendTask(){
        time_send = System.currentTimeMillis();
        System.out.println("time send = " + System.currentTimeMillis());
        for(Map.Entry<Session, LinkedList<Task>> entry: peersTaskQueue.entrySet()) {
            Task task = entry.getValue().getFirst();
            Session session = entry.getKey();
            session.getAsyncRemote().sendText(filterTaskString(getJson(task)));
        }
    }

}
