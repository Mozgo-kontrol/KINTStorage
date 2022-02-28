import java.util.HashSet;
import java.util.Set;

public class Tasks {
    private Set<RequestNumber> _taskSet;
    private int lastRequestNumber;

    public Tasks() {
        _taskSet = new HashSet<>();
        lastRequestNumber = 0;
    }

    public Set<RequestNumber> getTaskSet() {
        return _taskSet;
    }

    public int getLastRequestNumber() {
        return lastRequestNumber;
    }

    public void setLastRequestNumber(int lastRequestNumber) {
        this.lastRequestNumber = lastRequestNumber;
    }

    public void addRequestNumber(int a){
        RequestNumber newRequestNumber = new RequestNumber(a);
        _taskSet.add(newRequestNumber);
    }
}
