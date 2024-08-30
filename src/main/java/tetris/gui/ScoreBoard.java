package tetris.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Kheagen Haskins
 */
public class ScoreBoard extends JPanel {
    
    private JLabel lblScore;
    
    private static ScoreBoard board = new ScoreBoard(100);
    
    public static ScoreBoard getInstance() {
        return board;
    }
    
    /**
     * Main constructor. All other constructors call this one.
     *
     * @param width the width of the Panel
     */
    private ScoreBoard(int width) {
        super(true);
        
        setPreferredSize(new Dimension(width, Short.MAX_VALUE));
        setMaximumSize(getPreferredSize());

        setBackground(new Color(0x4C0827));
        initComponents();
    }
        
    public int getScore() {
        return Integer.parseInt(lblScore.getText());
    }
    
    public void setScore(int score) {
        lblScore.setText(String.valueOf(score));
    }
    
    private void initComponents() {
        lblScore = new JLabel();
        
        lblScore.setFont(new Font("Courier Primer", Font.PLAIN, 25));
        lblScore.setForeground(Color.WHITE);
        lblScore.setVerticalAlignment(JLabel.CENTER);
        lblScore.setHorizontalAlignment(JLabel.CENTER);
        
        lblScore.setText("0");
        
        add(lblScore);
    }
    
}