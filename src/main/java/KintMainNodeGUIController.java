import javax.swing.*;

public class KintMainNodeGUIController
{
    public KintMainNodeGUIController(KintMainNode kintMainNode)
    {
        KintGUI kintGUI = new KintGUI();
        JFrame jFrame = new JFrame("Message Sender");
        jFrame.setContentPane(kintGUI.getMainGUI());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        kintGUI.setKintMainNode(kintMainNode);

    }
}
