import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientUI {
    String NAME = "Default"; //�ϥΪ̹w�]�W��
    String allMessage = "";
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JPanel messagePanel = new JPanel();
    JTextPane messages = new JTextPane(); //��ѫǰT��
    JTextField inputMessage = new JTextField(); //�ϥΪ̿�J��r
    JButton sendMessage = new JButton("Send Message"); //�ǰe��J��r����ѫ�
    JMenuBar menu = new JMenuBar();
    JMenu nameMenu = new JMenu("name");
    JMenuItem setName = new JMenuItem("setName"); // �}�l��ܵ��ӧ��ϥΪ̦W�r

    public void setFromServer (JSONObject message) throws JSONException {
        String combine = String.format("%s %s : %s \n", message.get("time"), message.get("username"), message.get("message"));
        allMessage += combine; //�[�J�s�r��]combine�^��s�{���r��]allMessage�^
        messages.setText(allMessage);//��s�����T���r��
        frame.repaint();//��s��ѵ����������e
    }

    public void setInit() {
        menu.add(nameMenu);
        nameMenu.add(setName);
        setName.addActionListener(new ActionListener() { //�ˬd�ϥΪ̨ϧ_���n���W�r
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
