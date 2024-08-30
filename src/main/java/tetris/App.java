package tetris;

import java.awt.BorderLayout;
import java.awt.Dimension;
import tetris.gui.AnimationPanel;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import tetris.gui.ScoreBoard;

/**
 *
 * @author Kheagen Haskins
 */
public class App extends JFrame {
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new App().launch());
    }
    
    public App() throws HeadlessException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel contentPane = configureContentPane();
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Launches the application
     */
    private void launch() {
        System.setProperty("sun.java2d.opengl", "true");
        setVisible(true);
    }
    
    private JPanel configureContentPane() {
        JPanel contentPane = createContentPane();
        AnimationPanel animationPanel = new AnimationPanel();
        int scoreBoardWidth = 100;
        
        contentPane.add(animationPanel, BorderLayout.CENTER);
        contentPane.add(ScoreBoard.getInstance(), BorderLayout.EAST);
        contentPane.setPreferredSize(new Dimension(
                animationPanel.getPreferredSize().width + scoreBoardWidth,
                animationPanel.getPreferredSize().height
        ));
        
        return contentPane;
    }
    
    private JPanel createContentPane() {
        JPanel pnl = new JPanel();
        pnl.setDoubleBuffered(true);
        pnl.setLayout(new BorderLayout());
        return pnl;
    }
    
}