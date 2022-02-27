import org.drasyl.identity.DrasylAddress;
import org.drasyl.node.DrasylConfig;
import org.drasyl.node.DrasylException;
import org.drasyl.node.event.Event;
import org.drasyl.node.event.MessageEvent;
import org.drasyl.node.event.NodeOnlineEvent;
import org.json.simple.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.*;



public class KintMainNode extends ApplicationNode
{

    private final Storage _localeStorage = new Storage();
    private HashMap<Integer, String> _storage = _localeStorage.getStorage();

    private boolean _online = false;



    //Address allen Knoten im Netz
    private Set<DrasylAddress> addressSet = new HashSet<>();



    private Timer timer;

    protected KintMainNode(DrasylConfig config) throws DrasylException
    {
        super(config);
        sendHeartbeat(5000);

    }
    protected KintMainNode() throws DrasylException {
        super();
        sendHeartbeat(5000);
    }

    @Override public void turnOff()
    {
        for (DrasylAddress address : addressSet) {
            send(address, "SuperShutdown");
        }

        System.out.println("Turning off");
        shutdown();
    }

    public void sendHeartbeat(long intervall) {

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (DrasylAddress address : addressSet) {
                    String payload = "Heartbeat";

                    send(address, payload);

                    System.out.println("Gesendet an: " + address + " Payload: " + payload);
                }
            }
        }, 0, intervall);
    }

    public void sendToAllNodes(String message) {

        long checksum = Utility.getCRC32Checksum(message.getBytes(
                StandardCharsets.UTF_8));

        JSONObject json = new JSONObject();
        json.putAll(Map.of("checksum", checksum, "message", message));
        for (DrasylAddress address : addressSet) {
            send(address, json.toJSONString());
        }
    }




    @Override public void onEvent(Event event)
    {
        if (event instanceof NodeOnlineEvent) {
            _online = true;
        }

        if (event instanceof MessageEvent) {

            MessageEvent msgEvent = (MessageEvent) event;

            String message = msgEvent.getPayload().toString();


            if (message.equals("registernode")) {
                addressSet.add(msgEvent.getSender());
                System.out.println(msgEvent.getSender().toString());
                send(msgEvent.getSender(), "NodeRegistered");
            }


            else if (message.equals("Heartbeat")) {
                DrasylAddress sender = msgEvent.getSender();

                send(sender, "Heartbeat received");
                System.out.println(msgEvent.getSender());

            }

            else if (message.equals("NodeShutdown"))
            {
                addressSet.remove(msgEvent.getSender());

            }
        }
        System.out.println("Event received: " + event);
    }

}
