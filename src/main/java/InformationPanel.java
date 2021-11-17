import javax.swing.*;
import java.awt.*;

public class InformationPanel extends JPanel  {

    public InformationPanel() {
        super();
        this.setBackground(new Color(164, 164, 255));
        for (int i = 5; --i> 0;) {
            add(new Button(String.valueOf(i)));
        }
    }

}
