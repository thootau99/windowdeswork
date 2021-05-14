import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URI;

public class SocketClient {

    static URI uri = URI.create("http://localhost:3000");
    static IO.Options options = IO.Options.builder()
            .setQuery("x=42")
            .build();
    static Socket socket = IO.socket(uri, options);
    static ClientUI ui = new ClientUI();
    public static void main(String[] args) {
        ui.setInit();
        socket.connect();
        socket.on("hello", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println(args[0]);
            }
        });
        socket.on("test", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                ui.setFromServer((String) args[0]);
                System.out.println(args[0]);
            }
        });
    }
}
