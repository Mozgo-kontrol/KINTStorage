
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

    private JPanel mainGUI;
    private JButton turnOffButton;
    private JRadioButton node0RadioButton;
    private JRadioButton node1RadioButton;
    private JRadioButton node2RadioButton;
    private KintMainNode kintMainNode;

    private Timer timer;

    public KintGUI(){

        Delete.addActionListener(e -> {
            try
            {
                kintMainNode.remove(Integer.parseInt(Key.getText()));
            }
            catch (JsonProcessingException jsonProcessingException)
            {
                jsonProcessingException.printStackTrace();
            }
        });

        Read.addActionListener(e -> {
            try
            {
                kintMainNode.read(Integer.parseInt(Key.getText()));
            }
            catch (JsonProcessingException jsonProcessingException)
            {
                jsonProcessingException.printStackTrace();
            }
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

        turnOffButton.addActionListener(e -> {        });

        node0RadioButton.addActionListener(e -> {});

        node1RadioButton.addActionListener(e -> {});

        node2RadioButton.addActionListener(e -> {});

        HeartBeatOffButton.addActionListener(e -> {
            kintMainNode.turnOffSendHeartbeat();
        });

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                AntwortText.setText(kintMainNode.getResponse());
            }
        }, 5000, 3000L);

    }
   }
