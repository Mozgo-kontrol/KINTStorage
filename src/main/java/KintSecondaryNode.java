import org.drasyl.node.DrasylConfig;
import org.drasyl.node.DrasylException;
import org.drasyl.node.event.Event;
import org.drasyl.node.event.MessageEvent;
import org.drasyl.node.event.NodeDownEvent;
import org.drasyl.node.event.NodeOnlineEvent;
import org.json.simple.JSONObject;

import java.nio.charset.StandardCharsets;

public class KintSecondaryNode extends ApplicationNode
{
    private boolean _online;

    private String _superNode;

    public KintSecondaryNode(DrasylConfig config, String superNode) throws DrasylException {
        super(config);
        _superNode = superNode;
    }

    @Override public void turnOff()
    {

    }

    @Override public void onEvent(Event event)
    {
        if (event instanceof NodeOnlineEvent) {
            _online = true;
            send(_superNode, "registernode");
        }

        else if (event instanceof NodeDownEvent)
        {
            System.exit(0);
        }

        else if (event instanceof MessageEvent) {

            MessageEvent e = (MessageEvent) event;
            String payload = e.getPayload().toString();

            switch (payload) {
            case ("registerpeer"): {
                return;
            }
            case ("Heartbeat"): {

                send(e.getSender(), "HeartbeatRecieved");
                System.out.println("Heartbeat gesendet von: " + e.getSender());
                return;
            }

            case ("NodeRegistered"): {
                return;
            }

            case ("SuperShutdown"):
            {
                System.out.println("Super node gone offline");
                shutdown();
                return;
            }

            default: {
                JSONObject j = Utility.parseJSON(payload);
                if (j == null)
                {
                    return;
                }
                long checksum = (long) j.get("checksum");
                String message = (String) j.get("message");

                long newChecksum = Utility.getCRC32Checksum(message.getBytes(
                        StandardCharsets.UTF_8));
                if (checksum != newChecksum)
                {
                    send(_superNode, "ChecksumFailure");
                }
                else
                {
                    send(_superNode, "Success");
                    System.out.println(message);
                }

            }
            }

        }

    }
}
