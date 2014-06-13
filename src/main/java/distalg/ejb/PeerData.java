package distalg.ejb;

/**
 * Created by home on 27.05.14.
 */
public class PeerData {

    long timeConnection;
    long errorNumber;
    long speed;

    public PeerData() {
    }

    public PeerData(long time, long errorNumber, long speed) {
        this.timeConnection = time;
        this.errorNumber = errorNumber;
        this.speed = speed;
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
}
