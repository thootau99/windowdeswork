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
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;

public class ClientUI{
    String NAME = "Default"; //使用者預設名稱
    String allMessage = "";
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JPanel messagePanel = new JPanel(); // Panel -> inputMessage + sendMessage
    JPanel buttonPanel = new JPanel();
    JPanel battleAndOnlineUsers = new JPanel();
    JPanel allMessageAndBattleAndOnlineUsers = new JPanel(); // Panel -> message + allOnlineUsers
    JTextPane messages = new JTextPane(); //聊天室訊息
    DefaultListModel onlineUsersModel = new DefaultListModel(); // The model that the JList should show
    JList allOnlineUsers = new JList(onlineUsersModel); // The list that show all of the online users.

    DefaultListModel battleModel = new DefaultListModel();
    JList allBattles = new JList(battleModel);

    JTextField inputMessage = new JTextField(); //使用者輸入文字
    JButton sendMessage = new JButton("Send Message"); //傳送輸入文字給聊天室
    JButton sendImage = new JButton("Send Image"); //傳送輸入文字給聊天室
    JButton playSong = new JButton("Play Song");
    JMenuBar menu = new JMenuBar();
    JMenu nameMenu = new JMenu("name");
    JMenuItem setName = new JMenuItem("setName"); // 開始對話窗來更改使用者名字
    JMenu musicMenu = new JMenu("music");
    JMenuItem muPlay = new JMenuItem("play");
    JMenuItem muStop = new JMenuItem("stop");
    JMenuItem muRestart = new JMenuItem("restart");    
    Clip myClip;
	AudioInputStream input;
    int nerverPlay = 0;

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
    public void insertMessage(JSONObject messageJson) throws JSONException {
        if(NAME != (String)messageJson.get("username")){
            Toolkit.getDefaultToolkit().beep();
        }
        String str = String.format("%s %s : %s", messageJson.get("time"), messageJson.get("username"), messageJson.get("message"));
        Document docs = messages.getDocument();// 利用getDocument()方法取得JTextPane的Document
                                                // instance.0
        //str = str + "\n";
        try {			
            //docs.insertString(docs.getLength(), str, attrset);
            docs.insertString(docs.getLength(), str, null);
            docs.insertString(docs.getLength(), "\n", null);
            messages.setCaretPosition(docs.getLength()); //自動捲動到底部
        } catch (BadLocationException ble) {
            System.out.println("BadLocationException:" + ble);
        }
    }
    public void setFromServer (JSONObject messageJson) throws JSONException {
        //String name = messageJson.get("username");
        String combine = String.format("%s %s : %s \n", messageJson.get("time"), messageJson.get("username"), messageJson.get("message"));
        allMessage += combine; //加入新字串（combine）更新現有字串（allMessage）
        messages.setText(allMessage);//更新全部訊息字串
        frame.repaint();//更新聊天視窗視窗內容
    }

    public void insertImage(JSONObject messageJson) throws JSONException { //插入圖片
        if(NAME != (String)messageJson.get("username")){
            Toolkit.getDefaultToolkit().beep();
        }
        ClassLoader classLoader = getClass().getClassLoader();
        ImageIcon pic1=new ImageIcon(classLoader.getResource("images/no.jpeg").getFile());
        ImageIcon pic2=new ImageIcon(classLoader.getResource("images/anrgy.jpeg").getFile());
        ImageIcon pic3=new ImageIcon(classLoader.getResource("images/happy.jpeg").getFile());
        ImageIcon pic4=new ImageIcon(classLoader.getResource("images/sad.jpeg").getFile());
        ImageIcon pic5=new ImageIcon(classLoader.getResource("images/sorry.jpeg").getFile());
        ImageIcon pic6=new ImageIcon(classLoader.getResource("images/thank.jpeg").getFile());
	ImageIcon pic7=new ImageIcon(classLoader.getResource("images/cat.jpeg").getFile());
	ImageIcon pic8=new ImageIcon(classLoader.getResource("images/duck.jpeg").getFile());
	ImageIcon pic9=new ImageIcon(classLoader.getResource("images/fat.jpeg").getFile());
	ImageIcon pic10=new ImageIcon(classLoader.getResource("images/isthatit.jpeg").getFile());
	ImageIcon pic11=new ImageIcon(classLoader.getResource("images/lag.jpeg").getFile());
	ImageIcon pic12=new ImageIcon(classLoader.getResource("images/new.jpeg").getFile());
	ImageIcon pic13=new ImageIcon(classLoader.getResource("images/saint.jpeg").getFile());
	ImageIcon pic14=new ImageIcon(classLoader.getResource("images/sb.jpeg").getFile());
	ImageIcon pic15=new ImageIcon(classLoader.getResource("images/tired.jpeg").getFile());
	ImageIcon pic16=new ImageIcon(classLoader.getResource("images/wah.jpeg").getFile());
	ImageIcon pic17=new ImageIcon(classLoader.getResource("images/woman.jpeg").getFile());
        ImageIcon pic18=new ImageIcon(classLoader.getResource("images/midwind.gif").getFile());   
        String str = String.format("%s %s : ", messageJson.get("time"), messageJson.get("username"));
        Document doc = messages.getDocument();
        Object[] picOptions = { pic1, pic2, pic3, pic4, pic5,pic6 };
        int id = Integer.parseInt(messageJson.get("pic").toString());
        ImageIcon ImageIconChoose =  (ImageIcon) (picOptions[id]);
        try {			
            doc.insertString(doc.getLength(), str, null);
            messages.setCaretPosition(doc.getLength());
            messages.insertIcon(ImageIconChoose);
            doc.insertString(doc.getLength(), "\n", null);
            messages.setCaretPosition(doc.getLength());
        } catch (BadLocationException ble) {
            System.out.println("BadLocationException:" + ble);
        }

    }

    public void playMusic(JSONObject messageJson) throws JSONException {
        if(nerverPlay == 0){
            nerverPlay = 1;
        }else if(nerverPlay == 1){
            myClip.stop();
        }
        Object[] songNames = { "Astronomia", "NyanCat", "What'sGoingOn", "Yeee"};
        int id = Integer.parseInt(messageJson.get("music").toString());
        String str = String.format("%s %s set background music to %s", messageJson.get("time"), messageJson.get("username"), songNames[id]);
        Document docs = messages.getDocument();
        ClassLoader classLoader = getClass().getClassLoader();
        File music1 = new File(classLoader.getResource("audio/Astronomia.wav").getFile());
        File music2 = new File(classLoader.getResource("audio/NyanCat.wav").getFile());
        File music3 = new File(classLoader.getResource("audio/What'sGoingOn.wav").getFile());
        File music4 = new File(classLoader.getResource("audio/Yeee.wav").getFile());
        Object[] possibleValues = { music1, music2, music3, music4};
        try {			
            docs.insertString(docs.getLength(), str, null);
            docs.insertString(docs.getLength(), "\n", null);
            messages.setCaretPosition(docs.getLength()); //自動捲動到底部
        } catch (BadLocationException ble) {
            System.out.println("BadLocationException:" + ble);
        }
        try {
            input = AudioSystem.getAudioInputStream(
                (File)(possibleValues[id]));
            DataLine.Info info
                = new DataLine.Info(Clip.class, input.getFormat());
            if (AudioSystem.isLineSupported(info))
                myClip = (Clip) AudioSystem.getLine(info);
                myClip.open(input);
        }
        catch (UnsupportedAudioFileException ee1) {}
        catch (IOException ee2) {}
        catch (LineUnavailableException ee3) {};

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
        menu.add(musicMenu);
        musicMenu.add(muPlay);
        musicMenu.add(muStop);
        musicMenu.add(muRestart);
        allMessageAndBattleAndOnlineUsers.setLayout(new GridLayout(1, 2));
        allMessageAndBattleAndOnlineUsers.add(new JScrollPane(messages,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        battleAndOnlineUsers.setLayout(new GridLayout(2, 1));
        battleAndOnlineUsers.add(allOnlineUsers);
        battleAndOnlineUsers.add(allBattles);

        allMessageAndBattleAndOnlineUsers.add(battleAndOnlineUsers);
        messagePanel.setLayout(new GridLayout(1, 2));
        messagePanel.add(inputMessage);
        buttonPanel.setLayout(new GridLayout(3, 1));
        buttonPanel.add(sendMessage);
        buttonPanel.add(sendImage);
        buttonPanel.add(playSong);
        messagePanel.add(buttonPanel);
        panel.add(allMessageAndBattleAndOnlineUsers);
        panel.add(messagePanel);
        panel.setLayout(new GridLayout(2,1));
        frame.add(panel);
        frame.setJMenuBar(menu);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}
