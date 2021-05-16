import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientUI {
    String NAME = "Default"; //使用者預設名稱
    String allMessage = "";
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JPanel messagePanel = new JPanel();
    JTextPane messages = new JTextPane(); //聊天室訊息
    JTextField inputMessage = new JTextField(); //使用者輸入文字
    JButton sendMessage = new JButton("Send Message"); //傳送輸入文字給聊天室
    JMenuBar menu = new JMenuBar();
    JMenu nameMenu = new JMenu("name");
    JMenuItem setName = new JMenuItem("setName"); // 開始對話窗來更改使用者名字

    public void setFromServer (JSONObject message) throws JSONException {
        String combine = String.format("%s %s : %s \n", message.get("time"), message.get("username"), message.get("message"));
        allMessage += combine; //加入新字串（combine）更新現有字串（allMessage）
        messages.setText(allMessage);//更新全部訊息字串
        frame.repaint();//更新聊天視窗視窗內容
    }

    public void setInit() {
        menu.add(nameMenu);
        nameMenu.add(setName);
        setName.addActionListener(new ActionListener() { //檢查使用者使否有要更改名字
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
