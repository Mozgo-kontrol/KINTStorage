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
    private JPanel mainGUI;
    private JButton turnOffButton;
    private JRadioButton node1RadioButton;
    private JRadioButton node2RadioButton;
    private JRadioButton node4RadioButton;
    private JRadioButton node3RadioButton;
    private KintMainNode kintMainNode;

    public KintGUI(){

        Delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        Read.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        Write.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        Update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        HeartBeatOnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        Key.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        Value.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        AntwortText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        turnOffButton.addActionListener(e -> {});

        node1RadioButton.addActionListener(e -> {});

        node2RadioButton.addActionListener(e -> {});

        node3RadioButton.addActionListener(e -> {});

        node4RadioButton.addActionListener(e -> {});

        HeartBeatOffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
   }
