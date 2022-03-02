import com.fasterxml.jackson.core.JsonProcessingException;
import org.drasyl.identity.DrasylAddress;
import org.drasyl.node.DrasylConfig;
import org.drasyl.node.DrasylException;
import org.drasyl.node.event.*;
import org.json.simple.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class KintMainNode extends ApplicationNode
{
    private final Storage _localeStorage = new Storage();
    private boolean _online = false;
    private String _response ="";
    private Tasks _requestTasks = new Tasks();
    private HashMap <Integer, DrasylAddress> _addressHashMap = new HashMap<>();
    private Timer timer;
    private Timer checkHeartbeats = new Timer();




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
        timer.cancel();
        shutdown();

        System.exit(0);
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

                for (Map.Entry<Integer, DrasylAddress> entry : _addressHashMap.entrySet())
                {
                    if(entry.getKey()!=0){

                    }
                }


            }
        }, 0, intervall);
    }

    public void turnOffSendHeartbeat(){
        if(timer!=null){
            timer.cancel();
        }
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

    public void remove(int key) throws JsonProcessingException {

        int keyOfRemoveNode = calculateHashSum(key);
        System.out.println("SpeicherOrt :" + keyOfRemoveNode );
        String result;
        if(keyOfRemoveNode==0){
            System.out.println("remove in local");
            _response = removeInLocalStorage(key);
        }
        else {
            System.out.println("remove in remote");
            _response = removeRemoteLocalStorage(keyOfRemoveNode, key);
        }
    }

    private String removeInLocalStorage(int key){
        return _localeStorage.delete(key);
    }

    private String removeRemoteLocalStorage(int receiverAddress, int key)
            throws JsonProcessingException {

        RequestNumber requestNumber = new RequestNumber(_requestTasks.getLastRequestNumber()+1);
        MessageRequest messageRequest = new MessageRequest(Request.REMOVE, requestNumber,
                key);
        sendMessage(messageRequest, requestNumber, receiverAddress);
        return Common.OK;
    }

    // Hilfsmethode
    private void sendMessage(MessageRequest messageRequest, RequestNumber requestNumber, int receiverAddress)
            throws JsonProcessingException {

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
            _response = Common.UNABLETOEXECUTE;
        }
    }

    public void read(int key) throws JsonProcessingException {

        int keyOfGetNode = calculateHashSum(key);
        System.out.println("SpeicherOrt :" + keyOfGetNode );
        if(keyOfGetNode==0){
            System.out.println("get in local");
            _response = getInLocalStorage(key);
        }
        else {
           _response = getRemoteLocalStorage(keyOfGetNode, key);
        }
    }

    private String getInLocalStorage(int key) {

        return _localeStorage.read(key);
    }

    private String getRemoteLocalStorage(
        int receiverAddress, int key)
        throws JsonProcessingException {

        RequestNumber requestNumber = new RequestNumber(_requestTasks.getLastRequestNumber()+1);
        MessageRequest messageRequest = new MessageRequest(Request.GET, requestNumber,
                key);
        sendMessage(messageRequest, requestNumber, receiverAddress);
        return Common.OK;
    }

    public void create(int key, String value)
            throws JsonProcessingException {

        int keyOfSaveNode = calculateHashSum(key);
        System.out.println("SpeicherOrt :" + keyOfSaveNode );

        String result;
        if(keyOfSaveNode==0) {
            System.out.println("create Local");
            result = createInLocalStorage(key, value);
            if(result.equals(Common.OK)){
               result = result +" : in key : "+key+" value : "+ value;
                _response = result;
            }
        }
        else {
            result = createRemoteLocalStorage(keyOfSaveNode, key,value);
            _response = result;
        }
    }

    private String createInLocalStorage(
            int key,
            String value) {

        return _localeStorage.create(key, value);
    }

    private String createRemoteLocalStorage(
            int receiverAddress,
            int key,
            String value) throws JsonProcessingException {

        RequestNumber requestNumber = new RequestNumber(_requestTasks.getLastRequestNumber()+1);
        MessageRequest messageRequest = new MessageRequest(Request.POST, requestNumber,
                key, value);
        sendMessage(messageRequest, requestNumber, receiverAddress);
        return Common.WAITINGONRESPONSE;
    }

    private int calculateHashSum(int key) {
        return key % (Common.SUMOFNODE);  //hash funktion
    }


    @Override public void onEvent(Event event)
    {
        if (event instanceof NodeOnlineEvent) {

            _addressHashMap.put(0, (
                    (NodeOnlineEvent) event)
                    .getNode()
                    .getIdentity()
                    .getAddress());
            _online = true;

        }

        if (event instanceof MessageEvent) {

            MessageEvent msgEvent = (MessageEvent) event;
            DrasylAddress sender = msgEvent.getSender();
            String message = msgEvent.getPayload().toString();


            if (event instanceof MessageResponseEvent) {

                _response = message;
                System.out.println("Response became: " + message);
            }

            else if (message.equals(Common.REGISTERNODE)) {

                       if(!_addressHashMap.containsValue(sender)) {
                           _addressHashMap.put(getAddressHashMapSize(), sender);
                           System.out.println(
                                   "Node is registered and is in the list with key "
                                           + (getAddressHashMapSize() - 1) + " and Address "
                                           + _addressHashMap.get(
                                           getAddressHashMapSize() -1));

                           send(sender, Common.NODEREGISTERED);
                           showMeNodes();
                       }
                       else{
                           System.out.println(
                                   "Node is registered and is in the list!");
                       }
            }

            else if (message.equals(Common.HEARTBEAT)) {


                System.out.println(" Heartbeat received von Node" + sender);
                //
            }



            else if (message.equals("NodeShutdown"))
            {
                removeAddressFromHashMap(sender);
                System.out.println("NodeShutdown Node mit Address " + sender);
            }

            else if (message.contains("value: ")) {
                _response = message;
                System.out.println("Response became: " + message);
            }

            else {
                _response = message;
                System.out.println("Response became: " + message);
            }
        }
        System.out.println("Event received: " + event);
    }

    private void removeRequestNumberFromRequestTasks(MessageRequest messageRequest){
        _requestTasks.removeRequestNumber(messageRequest.get_metadata());
    }


    private void showMeNodes(){
        System.out.println(" In the List: ");

        for (Map.Entry<Integer, DrasylAddress> entry : _addressHashMap.entrySet())
        {
            System.out.println("Node with key :" + entry.getKey()+"< "+entry.getValue()+" >");
        }
        System.out.println(" In the List: ");
    }

    private void removeAddressFromHashMap(DrasylAddress address){

        System.out.println(" removeAddressFromHashMap: " + address);

        if(_addressHashMap.containsValue(address))
        {
            for (Map.Entry<Integer, DrasylAddress> entry : _addressHashMap.entrySet())
            {
                if (entry.getValue().equals(address))
                {
                    _addressHashMap.remove(entry.getKey());
                }
                // do what you have to do here
                // In your case, another loop.
            }
        }

    }

}
