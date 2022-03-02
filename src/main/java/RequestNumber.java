import lombok.Getter;
import lombok.Setter;

//all new
@Setter
@Getter
public class RequestNumber {

    private int requestNumber;

    RequestNumber(int i) {
        requestNumber = i;
    }

    public RequestNumber()
    {
    }
    public int getRequestNumber() {
        return requestNumber;
    }
}
