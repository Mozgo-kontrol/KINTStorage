import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class IsNodeOnline {

    private final int CAPACITY = 6; //6 -> wenn 30 sek (5*6) keine Heartbeats, dann ist _isNodeOnline = false
    private Queue<Integer> _lastHeartbeatList;
    private boolean _isNodeOnline = false;

    public IsNodeOnline() {
        _lastHeartbeatList = new ArrayBlockingQueue<>(CAPACITY);
        for (int i=0; i<CAPACITY; i++)
        {
            _lastHeartbeatList.offer(0);
        }
    }

    public boolean getIsNodeOnline(){
        return _isNodeOnline;
    }

    public void addHeartbeat(){
        _lastHeartbeatList.poll(); //take head element from queue
        _lastHeartbeatList.offer(1);
        updateIsNodeOnline();
    }

    public void addMissingHeartbeat(){
        _lastHeartbeatList.poll(); //take head element from queue
        _lastHeartbeatList.offer(0);
        updateIsNodeOnline();
    }

    /*
    sets _isNodeOnline false if all entries of _lastHeartbeatList are 0. Sets _isNodeOnline true if one or
    more entries of _lastHeartbeatList are 1.
     */
    private void updateIsNodeOnline(){
        int sum = 0;
        int headOfQueue;
        for (int i=0; i<CAPACITY; i++)
        {
            headOfQueue = _lastHeartbeatList.poll();//take head element from queue
            _lastHeartbeatList.offer(headOfQueue);//add element to tail of queue
            sum = sum + headOfQueue;
        }
        _isNodeOnline = sum != 0;
        }
}