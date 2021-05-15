import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientUI {
    String NAME = "Mark"; // The name of user
    String allMessage = "";
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JPanel messagePanel = new JPanel(); //
    JTextPane messages = new JTextPane(); // All messages go to here
    JTextField inputMessage = new JTextField(); // The place that uses to type message
    JButton sendMessage = new JButton("Send Message"); // The button that sends message
    JMenuBar menu = new JMenuBar();
    JMenu nameMenu = new JMenu("name");
    JMenuItem setName = new JMenuItem("setName"); // The menu button that open a dialog to set name

    public void setFromServer (JSONObject message) throws JSONException {
        String combine = String.format("%s %s : %s \n", message.get("time"), message.get("username"), message.get("message"));
        allMessage += combine;
        messages.setText(allMessage);
        frame.repaint();
    }

    public void setInit() {
        menu.add(nameMenu);
        nameMenu.add(setName);
        setName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = (String) JOptionPane.showInputDialog(
                        frame,
                        "What is your name?",
                        "Name set",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        NAME

                );
                NAME = name;
                System.out.println(NAME);
            }
        });
        messagePanel.setLayout(new GridLayout(1, 2));
        messagePanel.add(inputMessage);
        messagePanel.add(sendMessage);
        panel.add(messages);
        panel.add(messagePanel);
        panel.setLayout(new GridLayout(2,1));
        frame.add(panel);
        frame.setJMenuBar(menu);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

}
