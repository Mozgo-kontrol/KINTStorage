import org.drasyl.node.DrasylConfig;
import org.drasyl.node.DrasylConfigException;
import org.drasyl.node.DrasylException;
import org.drasyl.node.DrasylNode;
import org.drasyl.node.event.Event;
import org.drasyl.node.event.MessageEvent;
import org.drasyl.node.event.NodeOnlineEvent;

import java.util.HashMap;

//import org.json.simple.JSONObject;

public class MasterNode extends DrasylNode {

    private boolean online = false;

    private HashMap<Integer, String> _storage = new HashMap<>();

    public MasterNode() throws DrasylException {
        super();
    }

    public MasterNode(DrasylConfig config) throws DrasylConfigException, DrasylException {
        super(config);
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


    private String read(int key)  {

        return "";
    }

    private void update(int key, String value) {

    }

    private void create(int key, String value) {

    }

    private void delete(int key) {

    }

}
