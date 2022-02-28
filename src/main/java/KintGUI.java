import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
@Setter
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


        Read.addActionListener(e -> {

        });

        Write.addActionListener(e -> {

        });

        Update.addActionListener(e -> {

        });

        HeartBeatOnButton.addActionListener(e -> {

        });

        Key.addActionListener(e -> {

        });

        Value.addActionListener(e -> {

        });

        AntwortText.addActionListener(e -> {

        });

        HeartBeatOffButton.addActionListener(e -> {

        });
    }
   }
