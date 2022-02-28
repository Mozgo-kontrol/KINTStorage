import org.drasyl.node.DrasylConfig;
import org.drasyl.node.DrasylConfigException;
import org.drasyl.node.DrasylException;
import org.drasyl.node.DrasylNode;
import org.drasyl.node.event.Event;
import org.drasyl.node.event.MessageEvent;
import org.drasyl.node.event.NodeOnlineEvent;

import java.util.HashMap;
import java.util.Set;

//import org.json.simple.JSONObject;

public class MasterNode extends DrasylNode {

    private boolean online = false;
    private HashMap<Integer, String> _storage = new HashMap<>();
    private Tasks requestTasks;

    public MasterNode() throws DrasylException {
        super();
        requestTasks = new Tasks();
    }

    public MasterNode(DrasylConfig config) throws DrasylConfigException, DrasylException {
        super(config);
        requestTasks = new Tasks();
    }

    @Override
    public void onEvent(Event event) { //event-listener
        if (event instanceof NodeOnlineEvent) {
            online = true;
        }

        if (event instanceof MessageEvent)
        {
            Object payload = ((MessageEvent) event).getPayload();
            MessageRequest messagePayload = (MessageRequest) payload;
            //GET, POST, UPDATE, REMOVE
            switch (messagePayload.getRequest())
            {
            case GET:
              //  read();
                break;
            case  UPDATE:
             //   update();
                break;
            case POST:
             //   create();
                break;
            case REMOVE:
             //   delete();
                break;
            default:
                break;
            }
        }
    }

    //Returns the value to which argument key is mapped in _storage, or null
    //if _storage contains no mapping for argument key
    private String read(int key)  {
        return _storage.get(key);
    }

    //If _storage previously contained a mapping for argument key, the old value is replaced by argument value.
    //If _storage previously did not contain a mapping for argument key, maps argument key to argument value.
    private void update(int key, String value) {
        _storage.put(key, value);
    }

    //If _storage previously did not contain a mapping for argument key, maps argument key to argument value.
    //If _storage previously contained a mapping for argument key, the old value is replaced by argument value.
    private void create(int key, String value) {
        _storage.put(key, value);
    }

    //Removes the mapping for argument key from _storage, if mapping exists.
    private void delete(int key) {
        _storage.remove(key);
    }

}
