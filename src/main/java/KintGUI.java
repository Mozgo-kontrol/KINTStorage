
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.Setter;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

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

    private Timer timer;

    public KintGUI(){

        Delete.addActionListener(e -> {

        });

        Read.addActionListener(e -> {
           // AntwortText.setText(kintMainNode);
        });

        Write.addActionListener(e -> {

            try
            {
                kintMainNode.create(Integer.parseInt(Key.getText()),Value.getText());
            }
            catch (JsonProcessingException jsonProcessingException)
            {
                jsonProcessingException.printStackTrace();
            }
        });

        Update.addActionListener(e -> {
            try
            {
                kintMainNode.create(Integer.parseInt(Key.getText()),Value.getText());
            }
            catch (JsonProcessingException jsonProcessingException)
            {
                jsonProcessingException.printStackTrace();
            }

        });

        HeartBeatOnButton.addActionListener(e -> {
            kintMainNode.sendHeartbeat(5000L);
        });


        HeartBeatOffButton.addActionListener(e -> {
            kintMainNode.turnOffSendHeartbeat();
        });

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                AntwortText.setText(kintMainNode.getResponse());
            }
        }, 0, 3000L);

    }
   }
