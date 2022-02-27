import org.drasyl.identity.DrasylAddress;

import java.lang.reflect.Array;


public class MessageRequest
{
    private final Request messageRequest;  //GET, POST, PATCH, REMOVE
    private final Array metadata;
    private final Message content;
    private final DrasylAddress fromClient;


    public MessageRequest(Request messageRequest, Array metadata,
                          Message content, DrasylAddress fromClient)
    {
        this.messageRequest = messageRequest;
        this.metadata = metadata;
        this.content = content;
        this.fromClient = fromClient;

    }

    public Request getRequest() {
        return messageRequest;
    }

    public Message getContent() {
        return content;
    }

    public Array getMetadata() {
        return metadata;
    }

    public DrasylAddress fromClient() {
        return fromClient;
    }

}
