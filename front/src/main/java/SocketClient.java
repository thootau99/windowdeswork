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

public class SocketClient {

    static URI uri = URI.create("http://localhost:3000");
    static IO.Options options = IO.Options.builder()
            .setQuery("x=42")
            .build();
    static Socket socket = IO.socket(uri, options);
    static ClientUI ui = new ClientUI();

    public static void main(String[] args) {
        ui.sendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ui.inputMessage.getText() == null) return;
                JSONObject messageWillSend = new JSONObject();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                try {
                    messageWillSend.put("username", ui.NAME);
                    messageWillSend.put("message", ui.inputMessage.getText());
                    messageWillSend.put("time", dtf.format(now));
                    ui.setFromServer(messageWillSend);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
                socket.emit("sendMessage", messageWillSend.toString());
                ui.inputMessage.setText("");
            }
        });
        socket.on("getMessage", new Emitter.Listener() {
            @Override
            public void call(Object... args) { // args[0] here will be the message from other clients.
                try {
                    JSONObject message = new JSONObject(args[0].toString());
                    System.out.println(message);
                    System.out.println(args[0]);
                    ui.setFromServer(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        ui.setInit();
        socket.connect();
    }
}
