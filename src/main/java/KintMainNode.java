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

    private Tasks _requestTasks = new Tasks();


    //Address allen Knoten im Netz

    private HashMap <Integer, DrasylAddress> _addressHashMap = new HashMap<>();

    //private Set<DrasylAddress> _addressSet = new HashSet<>();



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

    private Integer getAddressHashMapSize(){
       return _addressHashMap.size();
    }

    @Override
    public void turnOff()
    {

        for (Map.Entry<Integer, DrasylAddress> entry : _addressHashMap.entrySet())
        {
            send(entry.getValue(),"SuperShutdown").exceptionally(e -> {
                throw new RuntimeException(
                        "Unable to process message.", e);
            });
            // do what you have to do here
            // In your case, another loop.
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

                for (Map.Entry<Integer, DrasylAddress> entry : _addressHashMap.entrySet())
                {
                    if(entry.getKey()!=null){
                        String payload = "Heartbeat";
                        send(entry.getValue(), payload).exceptionally(e -> {
                            throw new RuntimeException(
                                    "Unable to process message.", e);
                        });
                        // do what you have to do here
                        // In your case, another loop.
                        System.out.println("Gesendet an: " + entry.getValue() + " Payload: " + payload);
                    }
                }

            }
        }, 0, intervall);
    }

    public void sendToAllNodes(String message) {

        long checksum = Utility.getCRC32Checksum(message.getBytes(
                StandardCharsets.UTF_8));

        JSONObject json = new JSONObject();

        json.putAll(Map.of("checksum", checksum, "message", message));

        for (Map.Entry<Integer, DrasylAddress> entry : _addressHashMap.entrySet())
        {
                send(entry.getValue(), json.toJSONString()).exceptionally(e -> {
                    throw new RuntimeException(
                            "Unable to process message.", e);
                });
            // do what you have to do here
            // In your case, another loop.
        }
    }

    public void create(Integer key, String value){

        //TODO pruefen ob key integer ist
        int keyOfSaveNode = calculateHashSum(key);

        if(keyOfSaveNode==0){
            createInLocalStorage();
        }
        else {
            createRemoteLocalStorage(keyOfSaveNode, key,value);
        }

    }

    private String createInLocalStorage(){
        //TODO create implementieren
        return "";
    }

    private String createRemoteLocalStorage(int receiverAddress, Integer key,
            String value){

        //TODO erstellen request number aus Task
        RequestNumber requestNumber = new RequestNumber(_requestTasks.getLastRequestNumber()+1);

        MessageRequest messageRequest = new MessageRequest(Request.POST, requestNumber,
                key, value);


      /*  long checksum = Utility.getCRC32Checksum(value.getBytes(
               StandardCharsets.UTF_8));
*/
        JSONObject json = new JSONObject();
        //json.putAll(Map.of("checksum", checksum, "message",  messageRequest));

        json.putAll(Map.of( "message",  messageRequest));

        _requestTasks.addRequestNumber(requestNumber);

        try
        {
            send(_addressHashMap.get(receiverAddress), json.toJSONString())
                    .exceptionally(e -> {
                        throw new RuntimeException(
                                "Unable to process message.", e);

                    });
        }
        catch (RuntimeException e){
            _requestTasks.removeRequestNumber(requestNumber);
        }

        return "";
    }





    private Integer calculateHashSum(int key){
        return key % Common.SUMOFNODE-1;  //hash funktion
    }


    @Override public void onEvent(Event event)
    {
        if (event instanceof NodeOnlineEvent) {

            _addressHashMap.put(0, ((NodeOnlineEvent) event).getNode().getIdentity().getAddress());
            _online = true;

        }

        if (event instanceof MessageEvent) {


            MessageEvent msgEvent = (MessageEvent) event;
            String message = msgEvent.getPayload().toString();

            if (event instanceof MessageResponseEvent) {

                System.out.println("Response bekam: " + message);
            }

            if (message.equals("registernode")) {

                _addressHashMap.put(getAddressHashMapSize(), msgEvent.getSender());

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
                _addressHashMap.remove(msgEvent.getSender());

            }

        }
        System.out.println("Event received: " + event);
    }

}
