import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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
    private JCheckBox node1CheckBox;
    private JCheckBox node2CheckBox;
    private JCheckBox node3CheckBox;

    public KintGUI(JCheckBox node1CheckBox, JCheckBox node3CheckBox, JCheckBox node2CheckBox, JCheckBox node2CheckBox1){
        this.node2CheckBox = node2CheckBox1;


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

        HeartBeatOffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });



    }

   }
