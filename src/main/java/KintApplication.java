import org.drasyl.node.DrasylConfig;
import org.drasyl.node.DrasylException;

import java.nio.file.Path;

public class KintApplication
{

    protected static ApplicationNode node;


    public static void main(String[] args)
    {
       // Runtime.getRuntime().addShutdownHook(new Thread(() -> node.turnOff()));

        if (args.length > 1)
        {
            DrasylConfig config = DrasylConfig.newBuilder().identityPath(Path.of(args[1])).build();

            switch (args[0])
            {
            case(Common.MAIN):
            {
                createMainNode(config);
                return;
            }
            case(Common.SECONDARY):
            {
                try
                {
                    createSecondaryNode(config, args[2]);
                }
                catch (Exception e)
                {
                    if (e instanceof IndexOutOfBoundsException)
                    {
                        e.printStackTrace();
                        System.out.println("Help placeholder");
                    }
                    else
                    {
                        e.printStackTrace();
                    }
                }
            }
            }

        }
        else
        {
            System.out.println("Help placeholder");
        }

    }


    public static void createMainNode(DrasylConfig config)
    {
        try {
            node = new KintMainNode(config);
           node.start();
        } catch (DrasylException e) {
            e.printStackTrace();
        }
       //GUI started
    }

    public static void createSecondaryNode(DrasylConfig config, String superAddress)
    {
        try {
            node = new KintSecondaryNode(config, superAddress);
            node.start();
        } catch (DrasylException e) {
            e.printStackTrace();
        }
    }
}
