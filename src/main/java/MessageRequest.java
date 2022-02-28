import org.drasyl.identity.DrasylAddress;

import java.lang.reflect.Array;


public class MessageRequest
{
    private final Request messageRequest;  //GET, POST, PATCH, REMOVE
    private final Array metadata;
    private final Message content;


    public MessageRequest(Request messageRequest, Array metadata,
                          Message content)
    {
        this.messageRequest = messageRequest;
        this.metadata = metadata;
        this.content = content;

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
}