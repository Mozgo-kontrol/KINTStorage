import java.util.HashSet;
import java.util.Set;

public class Tasks {
    private Set<RequestNumber> taskSet;
    private int lastRequestNumber;

    public Tasks() {
        Set<RequestNumber> taskSet = new HashSet<>();
        lastRequestNumber = 0;
    }

    public Set<RequestNumber> getTaskSet() {
        return taskSet;
    }

    public int getLastRequestNumber() {
        return lastRequestNumber;
    }

    public void setLastRequestNumber(int lastRequestNumber) {
        this.lastRequestNumber = lastRequestNumber;
    }

    public void addRequestNumber(int a){
        RequestNumber newRequestNumber = new RequestNumber(a);
        taskSet.add(newRequestNumber);
    }
}
