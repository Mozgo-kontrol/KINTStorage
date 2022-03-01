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


 /*   public static MessageRequest fromString(String s){

        String[] splits = s.split(limiter);
        //kommt was sinnvolles raus?
        //wenn nein, return null
        //wenn ja:
        Request createdMessageRequest = Request.GET; //nicht nur get, was auch immer split rausgibt
        RequestNumber createdRequestNumber = new RequestNumber(36); //nicht 36, was auch immer split ausgibt
       // Message createdContent = new Message(1, "",""); //was auch immer split ausgibt
        return new MessageRequest(createdMessageRequest, createdRequestNumber, 12, "");//
    }

    @Override
    public String toString(){
        //... (messageRequest to String, metadata + to String ..., content to String)
        return limiter + limiter;
    }*/



