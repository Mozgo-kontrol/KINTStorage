public class MessageRequest
{
    private final Request messageRequest;  //GET, POST, PATCH, REMOVE
    private final RequestNumber metadata;
    private final Integer contentKey;
   // private final Message content;
   private final String content;
    private static final String limiter = ";";


    public MessageRequest(Request messageRequest, RequestNumber metadata, Integer contentKey, String content)
    {
        this.messageRequest = messageRequest;
        this.metadata = metadata;
        this.contentKey = contentKey;
        this.content = content;
    }

    public Integer getContentKey()
    {
        return contentKey;
    }

    public Request getRequest() {
        return messageRequest;
    }

    public String getContent() {
        return content;
    }

    public RequestNumber getMetadata() {
        return metadata;
}


    public static MessageRequest fromString(String s){

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
    }

}

