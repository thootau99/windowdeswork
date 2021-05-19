import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

public class SocketClient {

    static URI uri = URI.create("http://thootau.synology.me:48763");//建立URI為LocalHost,port:3000
    static IO.Options options = IO.Options.builder()
            .build();
    static Socket socket = IO.socket(uri, options);
    //建立socket
    static ClientUI ui = new ClientUI();//使用ClientUI定義好的視窗
    static String socketId = "";
    public static void main(String[] args) {
        ui.sendMessage.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ui.inputMessage.getText().length() == 0){ //使用沒有輸入時，產生錯誤對話框
                    JOptionPane.showMessageDialog(null,"請輸入文字","error",JOptionPane.ERROR_MESSAGE); 
                }
                else if (ui.inputMessage.getText().length() != 0){  //使用者有輸入時
                    JSONObject messageWillSend = new JSONObject();  //建立一個JSON物件來傳遞資料
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");//產生日期字串
                    LocalDateTime now = LocalDateTime.now();//擷取目前時間以及日期（不考慮時區）
                    try {
                        messageWillSend.put("username", ui.NAME);//取得輸入的使用者名稱
                        messageWillSend.put("message", ui.inputMessage.getText());//取得ClientUI使用者輸入的訊息
                        messageWillSend.put("time", dtf.format(now));//取得輸入時間
                        ui.setFromServer(messageWillSend);//呼叫setFromServer更新messages（JTextPane）內容
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                    socket.emit("sendMessage", messageWillSend.toString());//對當前連線的client發送一個"sendMessage"事件
                    ui.inputMessage.setText("");//傳送完後將JTextField舊的內容清空
                }
            }
        });
        ui.setName.addActionListener(new ActionListener() { //檢查使用者使否有要更改名字
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = (String) JOptionPane.showInputDialog(
                        ui.frame,
                        "What is your name?",
                        "Name set",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        ui.NAME

                );
                ui.NAME = name;
                socket.emit("changeName", name); // emit changeName to server, notify other clients that my name changed.
            }
        });

        ui.allOnlineUsers.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String chosenUser = (String) ui.allOnlineUsers.getSelectedValue();
                    chosenUser = chosenUser.split(",")[1];
                    String number = (String) JOptionPane.showInputDialog(
                            ui.frame,
                            "Which number is that you want to another user guess? \n 4 numbers",
                            "Guess number set",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            null,
                            "0000"
                    );
                    JSONObject numberWillSend = new JSONObject();
                    numberWillSend.put("id", chosenUser);
                    numberWillSend.put("number", number);
                    socket.emit("guessNumber", numberWillSend.toString());
                }
            }
        });


        socket.on("getMessage", new Emitter.Listener() { //監聽來自server的"getMessage"事件
            @Override
            public void call(Object... args) { // args[0]是來自其他Client端的訊息
                try {
                    /**
                     * Shape of message Object
                     * time String - The time that the message has been sent.
                     * username String - The username that send the message.
                     * message String - The content of message.
                     * */
                    JSONObject message = new JSONObject(args[0].toString());//建立一個JSON物件來傳遞資料
                    ui.setFromServer(message);//呼叫setFromServer更新messages（JTextPane）內容
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        socket.on("receiveOnlineUserList", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                /**
                 * Shape of user Object
                 * name String
                 * id String
                 * */
                JSONArray users = new JSONArray(args[0].toString()); // make the JSON string to JSONArray.
                ui.setOnlineUser(users, socketId);
            }
        });

        socket.on("guessNumber", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                ui.setGuessNumber((String) args[0]);
            }
        });

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socketId = socket.id();
            }
        });


        System.out.println();
        ui.setInit();
        socket.connect();
    }
}
