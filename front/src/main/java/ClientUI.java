import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class ClientUI {
    String NAME = "Default"; //使用者預設名稱
    String allMessage = "";
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JPanel messagePanel = new JPanel();
    JPanel allMessageAndOnlineUsers = new JPanel();
    JTextPane messages = new JTextPane(); //聊天室訊息
    DefaultListModel onlineUsersModel = new DefaultListModel();
    JList allOnlineUsers = new JList(onlineUsersModel);
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

    public void setOnlineUser(JSONArray users) {
        onlineUsersModel.clear(); // Clear the all element in the JList
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i); // Get the i th element of the JSONArray
            onlineUsersModel.addElement(String.format("Name: %s, %s \n", user.get("name"), user.get("id"))); // "Name: name, id: id"
        }
        allOnlineUsers.repaint(); // Repaint the JList
    }

    public void setInit() {
        menu.add(nameMenu);
        nameMenu.add(setName);

        allMessageAndOnlineUsers.setLayout(new GridLayout(1, 2));
        allMessageAndOnlineUsers.add(messages);
        allMessageAndOnlineUsers.add(allOnlineUsers);

        messagePanel.setLayout(new GridLayout(1, 2));
        messagePanel.add(inputMessage);
        messagePanel.add(sendMessage);
        panel.add(allMessageAndOnlineUsers);
        panel.add(messagePanel);
        panel.setLayout(new GridLayout(2,1));
        frame.add(panel);
        frame.setJMenuBar(menu);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

}
