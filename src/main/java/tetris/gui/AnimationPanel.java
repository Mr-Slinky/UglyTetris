package tetris.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import tetris.grid.GameLoop;
import tetris.grid.GameMatrix;
import tetris.grid.InputHandler;

/**
 *
 * @author Kheagen Haskins
 */
public class AnimationPanel extends JPanel {

    private GameMatrix matrix;
    private GameLoop gameLoop;

    public AnimationPanel() {
        super(null, true);
        int rows = 35;
        int cols = 20;
        
        matrix = new GameMatrix(rows, cols);
        gameLoop = new GameLoop(5, matrix, this, ScoreBoard.getInstance());
        addKeyListener(new InputHandler(this, matrix));

        int width, height;
        width = cols * matrix.getBlockSize();
        height = rows * matrix.getBlockSize();
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
        // For the attached key listener
        setFocusable(true);
        requestFocusInWindow();

        gameLoop.start();
    }

    @Override
    protected void paintComponent(Graphics og) {
        super.paintComponent(og);
        Graphics2D g = (Graphics2D) og;
        matrix.paint(g);
    }

}