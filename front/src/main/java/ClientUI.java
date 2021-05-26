import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ClientUI{
    String NAME = "Default"; //使用者預設名稱
    String allMessage = "";
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JPanel messagePanel = new JPanel(); // Panel -> inputMessage + sendMessage
    
    JPanel allMessageAndOnlineUsers = new JPanel(); // Panel -> message + allOnlineUsers
    JTextPane messages = new JTextPane(); //聊天室訊息
    DefaultListModel onlineUsersModel = new DefaultListModel(); // The model that the JList should show
    JList allOnlineUsers = new JList(onlineUsersModel); // The list that show all of the online users.
    JTextField inputMessage = new JTextField(); //使用者輸入文字
    JButton sendMessage = new JButton("Send Message"); //傳送輸入文字給聊天室
    JMenuBar menu = new JMenuBar();
    JMenu nameMenu = new JMenu("name");
    JMenuItem setName = new JMenuItem("setName"); // 開始對話窗來更改使用者名字
    /*public ClientUI(){
        //this.addKeyListener(new MyKeyListener()); 
        this.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == 13){
                    System.out.println("你按了空白鍵");
                }  
            }
        });
    }*/
    
    public void setGuessNumber (String number) {
        int A = 0;
        int B = 0;
        while (A != 4) {
            String numberSplit[] = number.split("");
            String guessNumber = (String) JOptionPane.showInputDialog(
                    frame,
                    String.format("Current State %s A %s B, please enter the 4 digits number to guess.", A, B),
                    "Number guessing",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "0000"
            );
            A = 0;
            B = 0;
            String guessNumberSplit[] = guessNumber.split("");
            int index = 0;
            int innerIndex = 0;
            for (String c : guessNumberSplit) {
                for (String innerC : numberSplit) {
                    if (index == innerIndex && c.equals(innerC)){
                        ++A;
                        break;
                    }
                    if (index != innerIndex && c.equals(innerC)){
                        ++B;
                        break;
                    }
                    innerIndex ++;
                }
                index ++;
                innerIndex = index;
            }
        }

    }


    public void setFromServer (JSONObject message) throws JSONException {
        String combine = String.format("%s %s : %s \n", message.get("time"), message.get("username"), message.get("message"));
        allMessage += combine; //加入新字串（combine）更新現有字串（allMessage）
        messages.setText(allMessage);//更新全部訊息字串
        frame.repaint();//更新聊天視窗視窗內容
    }

    public void setOnlineUser(JSONArray users, String socketId) {
        onlineUsersModel.clear(); // Clear the all element in the JList
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i); // Get the i th element of the JSONArray
            if (user.get("id").equals(socketId)) {
                onlineUsersModel.addElement(String.format("Name: %s (self)", user.get("name")));
                continue;
            }
            onlineUsersModel.addElement(String.format("Name: %s,%s", user.get("name"), user.get("id"))); // "Name: name, id: id"
        }
        allOnlineUsers.repaint(); // Repaint the JList
    }

    public void setInit() {
        menu.add(nameMenu);
        nameMenu.add(setName);
        allMessageAndOnlineUsers.setLayout(new GridLayout(1, 2));
        allMessageAndOnlineUsers.add(new JScrollPane(messages,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
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
    /*class MyKeyListener extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode() == 13){
                System.out.println("你按了空白鍵");
            }  
        }
    }*/

}
