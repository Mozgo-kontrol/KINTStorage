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
    private boolean _online = false;

    private final Storage _localeStorage = new Storage();
    private HashMap<Integer, String> _storage = _localeStorage.getStorage();



    //Address allen Knoten im Netz
    //private HashMap <Integer, String> _addressSet = new HashMap<>();

    private Set<DrasylAddress> _addressSet = new HashSet<>();



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

    @Override
    public void turnOff()
    {
        for (DrasylAddress address : _addressSet) {
            send(address, "SuperShutdown");
        }
        // Save to file
        Utility.saveHashmapToFile(_storage);

        System.out.println("Turning off");
        shutdown();
    }

    public void sendHeartbeat(long intervall) {

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (DrasylAddress address : _addressSet) {
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


        for (DrasylAddress address : _addressSet) {

            send(address, json.toJSONString());
        }
    }

    public void create(Integer key, String value){

        //TODO pruefen ob key integer ist
        int keyOfSaveNode = calculateHashSum(key);

        if(keyOfSaveNode==0){
            createInLocalStorage();
        }
        else {
            createRemoteLocalStorage(keyOfSaveNode, value);
        }

    }

    private String createInLocalStorage(){
        //TODO create implementieren
        return "";
    }

    private String createRemoteLocalStorage(int receiverAddress, String value){

        //TODO erstellen request number aus Task
        RequestNumber requestNumber = new RequestNumber(10);

        MessageRequest messageRequest = new MessageRequest(Request.POST, requestNumber,  value);

       // long checksum = Utility.getCRC32Checksum(value.getBytes(
       //         StandardCharsets.UTF_8));

        JSONObject json = new JSONObject();


        //json.putAll(Map.of("checksum", checksum, "message",  messageRequest));

        //TODO create implementieren

        return "";
    }





    private Integer calculateHashSum(int key){
        return key % Common.SUMOFNODE-1;  //hash funktion
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

                _addressSet.add(msgEvent.getSender());
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
                _addressSet.remove(msgEvent.getSender());

            }
        }
        System.out.println("Event received: " + event);
    }

}
