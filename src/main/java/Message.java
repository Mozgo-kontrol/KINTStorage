public class Message
{
    final private String messageKey;
    final private String messageType; //Int, String, Boolean
    final private String messageValue;

    public Message(String messageKey, String messageType, String messageValue)
    {
        this.messageKey = messageKey;
        this.messageType = messageType;
        this.messageValue = messageValue;
    }

    public String getMessageKey()
    {
        return messageKey;
    }
    public String getMessageType()
    {
        return messageType;
    }

    public String getMessageValue()
    {
        return messageValue;
    }
}
