import javax.swing.*;
import java.awt.*;

public class MainFrame {
    private JFrame mainFrame;

    MainFrame() {
        initializeFrame();
        GamePanel gamePanel = new GamePanel();
        gamePanel.setBackground(Color.DARK_GRAY);
        mainFrame.add(gamePanel);
        mainFrame.setVisible(true);
    }

    private void initializeFrame() {
        mainFrame = new JFrame("Snake_Game");
        mainFrame.setBounds(10, 10, 905, 700);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        mainFrame.getContentPane().setLayout(null);
        mainFrame.setLocation(400,100);
        mainFrame.setResizable(false);
    }
}
