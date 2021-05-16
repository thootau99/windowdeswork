import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import javax.swing.*;

public class SocketClient {

    static URI uri = URI.create("http://localhost:3000");//�إ�URI��LocalHost,port:3000
    static IO.Options options = IO.Options.builder()
            .setQuery("x=42")
            .build();
    static Socket socket = IO.socket(uri, options);
    //�إ�socket
    static ClientUI ui = new ClientUI();//�ϥ�ClientUI�w�q�n������

    public static void main(String[] args) {
        ui.sendMessage.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ui.inputMessage.getText().length() == 0){ //�ϥΨS����J�ɡA���Ϳ��~��ܮ�
                    JOptionPane.showMessageDialog(null,"�п�J��r","error",JOptionPane.ERROR_MESSAGE); 
                }
                else if (ui.inputMessage.getText().length() != 0){  //�ϥΪ̦���J��
                    JSONObject messageWillSend = new JSONObject();  //�إߤ@��JSON����Ӷǻ����
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");//���ͤ���r��
                    LocalDateTime now = LocalDateTime.now();//�^���ثe�ɶ��H�Τ���]���Ҽ{�ɰϡ^
                    try {
                        messageWillSend.put("username", ui.NAME);//���o��J���ϥΪ̦W��
                        messageWillSend.put("message", ui.inputMessage.getText());//���oClientUI�ϥΪ̿�J���T��
                        messageWillSend.put("time", dtf.format(now));//���o��J�ɶ�
                        ui.setFromServer(messageWillSend);//�I�ssetFromServer��smessages�]JTextPane�^���e
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                    socket.emit("sendMessage", messageWillSend.toString());//���e�s�u��client�o�e�@��"sendMessage"�ƥ�
                    ui.inputMessage.setText("");//�ǰe����NJTextField�ª����e�M��
                }
            }
        });
        socket.on("getMessage", new Emitter.Listener() { //��ť�Ӧ�server��"getMessage"�ƥ�
            @Override
            public void call(Object... args) { // args[0]�O�Ӧۨ�LClient�ݪ��T��
                try {
                    JSONObject message = new JSONObject(args[0].toString());//�إߤ@��JSON����Ӷǻ����
                    System.out.println(message);
                    System.out.println(args[0]);
                    ui.setFromServer(message);//�I�ssetFromServer��smessages�]JTextPane�^���e
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        ui.setInit();
        socket.connect();
    }
}
