import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class Graph {
    JLabel jl;
    JFrame jf;
    JScrollPane jsp;
    BufferedImage img = null;
    /**
     * Create the application.
     */
    public Graph() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() { 
        try {
            img = ImageIO.read(new File("big.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        jl = new JLabel(new ImageIcon(img));
        jl.setToolTipText("КРАСНЫЕ линии - ОСИ графика\r\nЧЕРНЫЕ линии - ОГРАНИЧЕНИЯ\r\nСИНЯЯ линия - ЛИНИЯ УРОВНЯ\r\n");
         jsp = new JScrollPane(jl);
        // создаем фрейм, ложим в центр созданный JScrollPane
         jf = new JFrame("JScroll Window");
        jf.setSize(700, 700);
        jf.getContentPane().add(jsp);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setVisible(true);
    }
}
