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
    JPanel battleAndOnlineUsers = new JPanel();
    JPanel allMessageAndBattleAndOnlineUsers = new JPanel(); // Panel -> message + allOnlineUsers
    JTextPane messages = new JTextPane(); //聊天室訊息
    DefaultListModel onlineUsersModel = new DefaultListModel(); // The model that the JList should show
    JList allOnlineUsers = new JList(onlineUsersModel); // The list that show all of the online users.

    DefaultListModel battleModel = new DefaultListModel();
    JList allBattles = new JList(battleModel);

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
    
    public boolean setGuessNumber (String number) {
        int A = 0;
        int B = 0;
        int count = -1;
        while (A != 4) {
            if (count > 5) return false;
            count ++;
            String tempNumber = number;
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
            for (int i = 0; i < guessNumberSplit.length; i++) {
                int position = tempNumber.indexOf(guessNumberSplit[i]);
                if (position >= 0) { // guess number contain in number
                    if (position + A == i) {
                        StringBuilder result = new StringBuilder(tempNumber);
                        tempNumber = result.deleteCharAt(position).toString();
                        A ++;
                    }
                    else if (position + A != i) B ++;
                }
            }
        }
        if (A >= 4) return true;
        else return false;
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

    public void setBattles(JSONArray battles) {
        battleModel.clear();
        for (int i = 0; i < battles.length(); i++) {
            JSONObject battle = battles.getJSONObject(i);
            if ((boolean) battle.get("finished") == false) {
                battleModel.addElement(String.format("Battle is between %s and %s !", battle.get("p1"), battle.get("p2")));
            } else {
                // true -> p2 win, false -> p1 win
                boolean result = (boolean) battle.get("winner");
                if (result) {
                    battleModel.addElement(String.format("The legendary battle overed, %s wins!", battle.get("p2")));
                } else {
                    battleModel.addElement(String.format("%s has torn %s into pieces!", battle.get("p1"), battle.get("p2")));
                }
            }
        }
        allBattles.repaint();
    }

    public void setInit() {
        menu.add(nameMenu);
        nameMenu.add(setName);
        allMessageAndBattleAndOnlineUsers.setLayout(new GridLayout(1, 2));
        allMessageAndBattleAndOnlineUsers.add(new JScrollPane(messages,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        battleAndOnlineUsers.setLayout(new GridLayout(2, 1));
        battleAndOnlineUsers.add(allOnlineUsers);
        battleAndOnlineUsers.add(allBattles);

        allMessageAndBattleAndOnlineUsers.add(battleAndOnlineUsers);
        messagePanel.setLayout(new GridLayout(1, 2));
        messagePanel.add(inputMessage);
        messagePanel.add(sendMessage);
        panel.add(allMessageAndBattleAndOnlineUsers);
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
