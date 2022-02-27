import java.lang.reflect.Array;


public class MessageRequest
{
    private final String messageRequest;  //GET, POST, PATCH, REMOVE
    private final Array metadata;
    private final Message content;


    public MessageRequest(String messageRequest, Array metadata,
                          Message content)
    {
        this.messageRequest = messageRequest;
        this.metadata = metadata;
        this.content = content;

    }

    public String getRequest() {
        return messageRequest;
    }

}
