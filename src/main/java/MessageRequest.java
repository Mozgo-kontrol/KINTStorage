import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest
{

    private  Request _messageRequest;  //GET, POST, PATCH, REMOVE
    private  RequestNumber _metadata;
    private  Integer _contentKey;
    private  String _content;


    public MessageRequest(Request messageRequest, RequestNumber metadata, Integer contentKey, String content)
    {
        this._messageRequest = messageRequest;
        this._metadata = metadata;
        this._contentKey = contentKey;
        this._content = content;
    }

    public MessageRequest(Request messageRequest, RequestNumber metadata, Integer contentKey)
    {
        this._messageRequest = messageRequest;
        this._metadata = metadata;
        this._contentKey = contentKey;
        this._content = "";
    }
    public MessageRequest(){}
}


