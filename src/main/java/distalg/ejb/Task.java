package distalg.ejb;

/**
 * Created by home on 27.05.14.
 */
public class Task {

    public String command;
    public String algorithm;
    public Object tasks;
    public Long time_start;
    public Long data_size;

    public Task() {

    }

    public Task(String command, String algorithm, Object tasks, Long time_start, Long data_size) {
        this.command = command;
        this.algorithm = algorithm;
        this.tasks = tasks;
        this.time_start = time_start;
        this.data_size = data_size;
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

    public Long getTimeStart() {
        return time_start;
    }

    public void setTimeStart(Long time_start) {
        this.time_start = time_start;
    }

    public Long getData_size() {
        return data_size;
    }

    public void setData_size(Long data_size) {
        this.data_size = data_size;
    }
}
