import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private String _response ="";

   // private HashMap<Integer, String> _storage = _localeStorage.getStorage();

    private Tasks _requestTasks = new Tasks();


    //Address allen Knoten im Netz

    private HashMap <Integer, DrasylAddress> _addressHashMap = new HashMap<>();

    //private Set<DrasylAddress> _addressSet = new HashSet<>();



    private Timer timer;


    public String getResponse(){
       return  _response;
    }

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

    private Integer getNaechstefreieStelle(){
        return _addressHashMap.size()-1;
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
        //Utility.saveHashmapToFile(_storage);

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
                    if(entry.getKey()!=0){

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

    public String create(Integer key, String value)
            throws JsonProcessingException
    {

        //TODO pruefen ob key integer ist
        int keyOfSaveNode = calculateHashSum(key);
        System.out.println("SpeicherOrt :" + keyOfSaveNode );
        String result;
        if(keyOfSaveNode==0){

            System.out.println("create Local");
            result = createInLocalStorage(key, value);
            if(result.equals(Common.OK)){
               result = result +" : in key : "+key+" value : "+ _localeStorage.read(key);
            }
        }
        else {
            result = createRemoteLocalStorage(keyOfSaveNode, key,value);
        }
        return result;
    }

    private String createInLocalStorage(
            Integer key,
            String value){
        _localeStorage.create(key, value);
        return _localeStorage.create(key, value);
    }

    private String createRemoteLocalStorage(
            int receiverAddress,
            Integer key,
            String value) throws JsonProcessingException
    {

        //TODO erstellen request number aus Task

        RequestNumber requestNumber = new RequestNumber(_requestTasks.getLastRequestNumber()+1);

        MessageRequest messageRequest = new MessageRequest(Request.POST, requestNumber,
                key, value);


      /*  long checksum = Utility.getCRC32Checksum(value.getBytes(
               StandardCharsets.UTF_8));
*/
       // JSONObject json = new JSONObject();
        //json.putAll(Map.of("checksum", checksum, "message",  messageRequest));

       String message = Utility.parseObjectToJSON(messageRequest);

        _requestTasks.addRequestNumber(requestNumber);

        try
        {
            send(_addressHashMap.get(receiverAddress), message)
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
        return key % (Common.SUMOFNODE-1);  //hash funktion
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

                _response = message;
                System.out.println("Response became: " + message);
            }

            else if (message.equals(Common.REGISTERNODE)) {


                _addressHashMap.put(getAddressHashMapSize(), msgEvent.getSender());

                System.out.println("Node is registered and is in the list with key "
                        + getAddressHashMapSize()+ "and Address "
                        + _addressHashMap.get(getAddressHashMapSize()-1));

                send(msgEvent.getSender(), Common.NODEREGISTERED);
            }


            else if (message.equals(Common.HEARTBEAT)) {

                DrasylAddress sender = msgEvent.getSender();
                send(sender, "Heartbeat received");
                System.out.println(msgEvent.getSender());

            }

            else if (message.equals("NodeShutdown"))
            {
                _addressHashMap.remove(msgEvent.getSender());

            }
            else {

                _response = message;
                System.out.println("Response became: " + message);
            }

        }
        System.out.println("Event received: " + event);
    }

}
