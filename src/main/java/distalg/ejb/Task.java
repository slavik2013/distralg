package distalg.ejb;

/**
 * Created by home on 27.05.14.
 */
public class Task {

    public String command;
    public String algorithm;
    public Object tasks;

    public Task() {
    }

    public Task(String command, String algorithm, Object tasks) {
        this.command = command;
        this.algorithm = algorithm;
        this.tasks = tasks;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Object getTasks() {
        return tasks;
    }

    public void setTask(Object tasks) {
        this.tasks = tasks;
    }
}
