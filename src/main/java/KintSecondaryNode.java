import org.drasyl.identity.DrasylAddress;
import org.drasyl.node.DrasylConfig;
import org.drasyl.node.DrasylException;
import org.drasyl.node.event.Event;
import org.drasyl.node.event.MessageEvent;
import org.drasyl.node.event.NodeDownEvent;
import org.drasyl.node.event.NodeOnlineEvent;
import org.json.simple.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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

    }

    @Override public void onEvent(Event event)
    {
        System.out.println("Event received: " + event);
        if (event instanceof NodeOnlineEvent) {
            System.out.println("register bei superNode");
            _online = true;
            registerBeiSuper(3000L);
        }

        else if (event instanceof NodeDownEvent)
        {
            System.exit(0);
            turnOff();
        }

        else if (event instanceof MessageEvent) {

            MessageEvent e = (MessageEvent) event;
            String payload = e.getPayload().toString();

            switch (payload) {
                case ("registerpeer") -> {

                }

                case (Common.HEARTBEAT) -> {

                    send(e.getSender(), "HeartbeatReceived");

                    System.out.println("Heartbeat gesendet von: " + e.getSender());
                }

                case (Common.NODEREGISTERED) -> {
                    _isRegisteredBeiSuperNode = true;

                    System.out.println("Node registered");
                }

                case ("SuperShutdown") -> {
                    System.out.println("Super node gone offline");
                    shutdown();
                }

                default -> {
                    JSONObject j = Utility.parseJSON(payload);

                   // long checksum = (long) j.get("checksum");

                    assert j != null;
                    MessageRequest message = (MessageRequest) j.get("message");
                        //GET, POST, UPDATE, REMOVE
                    String result;

                        switch (message.getRequest())
                        {
                        case GET:
                            //   update();
                            break;
                        case  UPDATE:
                            //   update();
                            break;
                        case POST:

                             result = _localeStorage.create(
                                      message.getContentKey(),
                                      message.getContent());

                            System.out.println("Post request von:"+ e.getSender()+"result:" + result);

                            MessageEvent messageEvent = MessageResponseEvent.of(identity.getAddress(), result);
                            send(e.getSender(), messageEvent);

                            break;
                        case REMOVE:
                            //   delete();
                            break;
                        default:
                            break;
                        }
                    }


                   /* long newChecksum = Utility.getCRC32Checksum(message.getBytes(
                            StandardCharsets.UTF_8));
                    if (checksum != newChecksum) {
                        send(_superNode, "ChecksumFailure");
                    } else {
                        send(_superNode, "Success");
                        System.out.println(message);
                    }*/

                }
            }

    }
    public void registerBeiSuper(long intervall) {
        System.out.println("Register bei Super Start");
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Register bei Super Send");
                send(_superNode, Common.REGISTERNODE).exceptionally(e -> {
                    throw new RuntimeException(
                            "Unable to process message.", e);
                });

                if(_isRegisteredBeiSuperNode){
                    timer.cancel();
                    System.out.println("Node ist registered bei Super");
                    System.out.println("Timer cancel");
                }
            }
        }, 0, intervall);
    }


}
