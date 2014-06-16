package distalg.ejb;

import javax.websocket.Session;


public class PeerData implements Comparable<PeerData>{

    long timeConnection;
    long errorNumber;
    long speed;
    public Session session;
    public double general_speed;

    public PeerData() {
    }

    public PeerData(long time, long errorNumber, long speed, Session session) {
        this.timeConnection = time;
        this.errorNumber = errorNumber;
        this.speed = speed;
        this.session = session;
    }

    public long getTimeConnection() {
        return timeConnection;
    }

    public void setTimeConnection(long timeConnection) {
        this.timeConnection = timeConnection;
    }

    public long getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(long errorNumber) {
        this.errorNumber = errorNumber;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void calculateGeneralSpeed(){
        this.general_speed = (1/(double)this.timeConnection) + (1/(double)this.speed);
    }

    @Override
    public int compareTo(PeerData peerData) {

        if(this.general_speed > peerData.general_speed)
            return -1;
        else if(this.general_speed < peerData.general_speed)
            return 1;
        return 0;
    }
}
