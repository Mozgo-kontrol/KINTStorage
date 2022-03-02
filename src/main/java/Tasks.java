import java.util.HashSet;
import java.util.Set;

public class Tasks {
    private Set<RequestNumber> _taskSet;
    private int _lastRequestNumber;

    public Tasks() {
        _taskSet = new HashSet<>();
        _lastRequestNumber = 0;
    }

    public Set<RequestNumber> getTaskSet() {
        return _taskSet;
    }

    public int getLastRequestNumber() {
        return _lastRequestNumber;
    }

    public void setLastRequestNumber(int lastRequestNumber) {
        this._lastRequestNumber = lastRequestNumber;
    }

    public void addRequestNumber(RequestNumber newRequestNumber){
        _taskSet.add(newRequestNumber);
        _lastRequestNumber++;
    }

    public void removeRequestNumber(RequestNumber oldRequestNumber){
        _taskSet.remove(oldRequestNumber);
    }

  /*  public void addRequestNumber(){
        RequestNumber newRequestNumber = new RequestNumber(_lastRequestNumber+1);
        _taskSet.add(newRequestNumber);
        _lastRequestNumber++;
    }*/

}
