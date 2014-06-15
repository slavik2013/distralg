package distalg.ejb;

import distalg.model.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 27.05.14.
 */
public class Task implements Comparable<Task>{

    public String command;
    public String algorithm;
    public Object tasks;
    public Long time_start;
    public Long data_size;

    @JsonIgnore
    public List<Data> tasks_list = new ArrayList<Data>();

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

    @Override
    public int compareTo(Task task) {
        return 0;
    }

    public List<Data> getTasks_list() {
        return tasks_list;
    }

    public void setTasks_list(List<Data> tasks_list) {
        this.tasks_list = tasks_list;
    }
}
