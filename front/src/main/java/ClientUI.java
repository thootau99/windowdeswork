import javax.swing.*;
import java.awt.*;

public class ClientUI {
    JLabel fromServer = new JLabel("server");
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();

    public void setFromServer (String message) {
        fromServer.setText(message);
        frame.repaint();
    }

    public void setInit() {
        frame.add(fromServer);
        frame.setVisible(true);
    }

}
