import org.drasyl.node.DrasylConfig;
import org.drasyl.node.DrasylException;
import org.drasyl.node.DrasylNode;

import java.util.HashMap;

public abstract class ApplicationNode extends DrasylNode
{

    protected ApplicationNode(DrasylConfig config) throws DrasylException
    {
        super(config);
    }

    protected ApplicationNode() throws DrasylException {
        super();
    }

    public abstract void turnOff();

}
