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
//import javax.swing.text.html.HTMLDocument;
//import javax.swing.text.html.HTMLEditorKit;
//import java.io.IOException;
//import com.vdurmont.emoji.EmojiParser;
//import com.vdurmont.emoji.EmojiManager;
//import java.awt.Color;
//import javax.swing.text.html.HTML;
//import java.nio.charset.Charset;
//import java.awt.Font;

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
    JButton sendImage = new JButton("Send Image"); //傳送輸入文字給聊天室
    JMenuBar menu = new JMenuBar();
    JMenu nameMenu = new JMenu("name");
    JMenuItem setName = new JMenuItem("setName"); // 開始對話窗來更改使用者名字
    //HTMLEditorKit htmledit = new HTMLEditorKit();
    //HTMLDocument text_html = (HTMLDocument) htmledit.createDefaultDocument();
    
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
    //public void insertText(String str, AttributeSet attrset) {
    public void insertMessage(JSONObject messageJson) throws JSONException {
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
        String combine = String.format("%s %s : %s \n", messageJson.get("time"), messageJson.get("username"), messageJson.get("message"));
        allMessage += combine; //加入新字串（combine）更新現有字串（allMessage）
        messages.setText(allMessage);//更新全部訊息字串
        frame.repaint();//更新聊天視窗視窗內容
    }
    /*public void setEmoji (){ //測試emoji直接輸出到jtextpane
        //String combine = String.format("%s %s : %s \n", messageJson.get("time"), messageJson.get("username"), messageJson.get("message"));
        //String str = "An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!";
        String result = "\uD83D\uDC7D";//EmojiParser.parseToUnicode(str);
        //byte[] emojiBytes = new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x98, (byte)0x81};
        //String emojiAsString = new String(emojiBytes, Charset.forName("UTF-8"));
        allMessage += result; //加入新字串（combine）更新現有字串（allMessage）
        messages.setText(allMessage);//更新全部訊息字串
        frame.repaint();//更新聊天視窗視窗內容
        //int[] surrogates = {0xD83D, 0xDC7D};
        //String alienEmojiString = new String(surrogates, 0, surrogates.length);
        //System.out.println(alienEmojiString);
        System.out.println("\uD83D\uDC7D"); 
    }*/

    public void insertImage(JSONObject messageJson) throws JSONException { //插入圖片
        String str = String.format("%s %s : ", messageJson.get("time"), messageJson.get("username"));
        //messages.insertIcon(new ImageIcon("/Users/tasiyusiang/Downloads/windowdeswork/front/src/main/java/Shit.png"));
        Document doc = messages.getDocument();
        try {			
            //docs.insertString(docs.getLength(), str, attrset);
            doc.insertString(doc.getLength(), str, null);
            messages.setCaretPosition(doc.getLength());
            messages.insertIcon(new ImageIcon("/Users/tasiyusiang/Downloads/windowdeswork/front/src/main/java/Shit.png"));
            doc.insertString(doc.getLength(), "\n", null);
            messages.setCaretPosition(doc.getLength());
        } catch (BadLocationException ble) {
            System.out.println("BadLocationException:" + ble);
        }
        //setImage();

    }
    /*public void setImage(){//(JSONObject messageJson) throws JSONException { //插入圖片
        //String str = String.format("%s %s : ", messageJson.get("time"), messageJson.get("username"));
        //messages.insertIcon(new ImageIcon("/Users/tasiyusiang/Downloads/windowdeswork/front/src/main/java/Shit.png"));
        try {
            Document doc = messages.getDocument();
            //doc.insertString(doc.getLength(), str, null);
            
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }

    }*/

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
        //messages.setFont(new java.awt.Font("Arial Unicode MS",1,20));
        //messages.setEditorKit(htmledit);
        //messages.setContentType("text/html");
        //messages.setDocument(text_html);
        //insertHTML(HTMLDocument doc, int offset, String html, int popDepth, int pushDepth, HTML.Tag insertTag);
        menu.add(nameMenu);
        nameMenu.add(setName);
        allMessageAndBattleAndOnlineUsers.setLayout(new GridLayout(1, 2));
        allMessageAndBattleAndOnlineUsers.add(new JScrollPane(messages,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        battleAndOnlineUsers.setLayout(new GridLayout(2, 1));
        battleAndOnlineUsers.add(allOnlineUsers);
        battleAndOnlineUsers.add(allBattles);

        allMessageAndBattleAndOnlineUsers.add(battleAndOnlineUsers);
        messagePanel.setLayout(new GridLayout(1, 3));
        messagePanel.add(inputMessage);
        messagePanel.add(sendMessage);
        messagePanel.add(sendImage);
        panel.add(allMessageAndBattleAndOnlineUsers);
        panel.add(messagePanel);
        panel.setLayout(new GridLayout(2,1));
        frame.add(panel);
        frame.setJMenuBar(menu);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
    /*public void insert(String str, AttributeSet attrset) {
		Document docs = messages.getDocument();// 利用getDocument()方法取得JTextPane的Document
												// instance.0
		str = str + "\n";
		try {			
			docs.insertString(docs.getLength(), str, attrset);
			
			messages.setCaretPosition(docs.getLength()); //自動捲動到底部
		} catch (BadLocationException ble) {
			System.out.println("BadLocationException:" + ble);
		}
	}*/
    /*public void display(String str) throws BadLocationException, IOException {//使用HTML方式顯示
		String str_sour = EmojiParser.parseToUnicode(str); //将原串中的表情别名转换为Unicode编码
		for (int j = 0; j < str_sour.length(); j++) { //遍历转换后的字符串
			int codepoint = str_sour.codePointAt(j); //暂存码点
			char[] code = Character.toChars(codepoint);  //将码点转换为Charater
			String str1 = new String(code);   //再次转换为字符串
			if (EmojiManager.isEmoji(EmojiParser.parseToUnicode(str1))) { //如果此码点为表情的Unicode
				htmledit.insertHTML(text_html, messages.getDocument().getLength(),
						"<span style=\"color:'" + "blue" + "';font-family:'Apple Color Emoji'\">" + str1 + "</span>", 0, 0,
						HTML.Tag.SPAN); //插入html文档 字体为“Segoe UI Emoji'以正常显示表情
                System.out.println(str1);
				j += 1;  //由于表情一个码点是两个代码单元，故每次循环偏移两个单位
			} else {
				htmledit.insertHTML(text_html, messages.getDocument().getLength(),
						"<span style=\"color:'" + "blue" + "'\">" + str1 + "</span>", 0, 0, HTML.Tag.SPAN);  //非emoji字符则正常显示，color用于颜色控制，在笔者的工程代码中，在此处可忽略
			}
		}
		htmledit.insertHTML(text_html, messages.getDocument().getLength(), "<br />", 0, 0, HTML.Tag.BR); //最后加以换行

	}*/

}
