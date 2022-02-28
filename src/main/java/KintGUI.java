
import lombok.Getter;
import lombok.Setter;
import javax.swing.*;

@Setter
@Getter
public class KintGUI
{
    private JButton Read;
    private JTextField Key;
    private JTextField Value;
    private JTextField AntwortText;
    private JButton Delete;
    private JButton Write;
    private JButton Update;
    private JButton HeartBeatOnButton;
    private JButton HeartBeatOffButton;
    private JCheckBox CheckBox;

    private JPanel mainGUI;
    private KintMainNode kintMainNode;

    public KintGUI(){

        Delete.addActionListener(e -> {

        });

        //AntwortText.setText(kintMainNode.getResponse()+"");

        Read.addActionListener(e -> {
           // AntwortText.setText(kintMainNode);
        });

        Write.addActionListener(e -> {
               AntwortText.setText(kintMainNode.create(Integer.parseInt(Key.getText()),Value.getText()));
        });

        Update.addActionListener(e -> {

        });
        HeartBeatOnButton.addActionListener(e -> {

        });


        HeartBeatOffButton.addActionListener(e -> {

        });
    }
   }
