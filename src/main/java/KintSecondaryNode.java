import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.drasyl.identity.DrasylAddress;
import org.drasyl.node.DrasylConfig;
import org.drasyl.node.DrasylException;
import org.drasyl.node.event.*;
import org.json.simple.JSONObject;

import java.io.DataInput;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.*;

public class KintSecondaryNode extends ApplicationNode
{

    private Storage _localeStorage = new Storage();
    private Timer timer;


    private boolean _online = false;

    private boolean _isRegisteredBeiSuperNode = false;

    private final String _superNode;

    public KintSecondaryNode(DrasylConfig config, String superNode) throws DrasylException {
        super(config);
        _superNode = superNode;
    }

    public KintSecondaryNode(String superNode) throws DrasylException {
        super();
        _superNode = superNode;
    }


    @Override public void turnOff()
    {
       //send(_superNode, "NodeShutdown");
    }


    @Override @SneakyThrows public void onEvent(Event event)
    {
        System.out.println("Event received: " + event);
        if (event instanceof NodeOnlineEvent) {

            System.out.println("register bei superNode");
            _online = true;
            registerBeiSuper(10000L);

        }

        if (event instanceof PeerRelayEvent) {

            _isRegisteredBeiSuperNode = false;
            System.out.println("Connection with Master was fault superNode");
            registerBeiSuper(10000L);
            System.out.println("Register bei superNode");

        }

        else if (event instanceof MessageEvent) {

            MessageEvent e = (MessageEvent) event;
            String payload = e.getPayload().toString();

            switch (payload) {

                case ("registerpeer") -> {

                }

                case (Common.HEARTBEAT) -> {
                    String nodeName ="";
                    if(e.getSender().toString().equals(_superNode)){ nodeName = " Master ";}
                    System.out.println("Heartbeat become von: "
                            +nodeName
                            +e.getSender());

                    send(e.getSender(), Common.HEARTBEAT);
                }

                case (Common.NODEREGISTERED) -> {
                    _isRegisteredBeiSuperNode = true;
                    System.out.println("Node registered");

                }

                case (Common.SUPERSHUTDOWN) -> {

                    System.out.println("Super node SuperShutdown");
                    shutdown();
                }

                default -> {

                   // System.out.println(payload);

                    MessageRequest message = Utility.parseJSONToMessageRequest(payload);

                        //GET, POST, UPDATE, REMOVE
                        String result;
                        switch (message.get_messageRequest())
                        {
                        case GET:
                            result = _localeStorage.read(message.get_contentKey());
                            System.out.println("Read request von:"+ e.getSender()+"result:" + result);
                            send(e.getSender(), "value: " + result);
                            break;
                        case POST, UPDATE:
                            result = _localeStorage.create(
                                message.get_contentKey(),
                                message.get_content());
                            System.out.println("Post request von:"+ e.getSender()+"result:" + result);
                            send(e.getSender(),"Result: " + result);
                            break;
                        case REMOVE:
                            result = _localeStorage.delete(message.get_contentKey());
                            System.out.println("Remove request von:"+ e.getSender()+"result:" + result);
                            send(e.getSender(),"Result:" + result);
                            break;
                        default:
                            break;
                        }
                    }
                }
            }

    }
    public void registerBeiSuper(long intervall) {
        System.out.println("Register bei Super Start");
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if(_isRegisteredBeiSuperNode){
                    timer.cancel();
                    System.out.println("Node ist registered bei Super");
                    System.out.println("Timer cancel");
                }
                else {

                    System.out.println("Register bei Super Send");
                    send(_superNode, Common.REGISTERNODE).exceptionally(e -> {
                        throw new RuntimeException(
                                "Unable to process message.", e);
                    });
                }

            }
        }, 0, intervall);
    }


}
