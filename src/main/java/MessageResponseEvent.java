import jdk.jfr.Event;
import org.drasyl.identity.DrasylAddress;
import org.drasyl.node.event.MessageEvent;

class MessageResponseEvent extends MessageEvent
{
    private DrasylAddress _sender;
    private Object _payload;

    //TODO responce Enum erstellen
    //200 = OK
    //300 = REPEAT
    //400 = FAULT CLIENT
    //500 = FAULT SERVER

    public MessageResponseEvent(DrasylAddress sender, Object payload)
    {
        this._sender = sender;
        this._payload = payload;
    }

    public MessageResponseEvent()
    {
        super();
    }
    @Override
    public DrasylAddress getSender()
    {
        return _sender;
    }

    @Override
    public Object getPayload()
    {
        return _payload;
    }
}
