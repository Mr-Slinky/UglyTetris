package tetris.grid;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import static tetris.tetromino.Tetromino.Direction.DOWN;
import static tetris.tetromino.Tetromino.Direction.LEFT;
import static tetris.tetromino.Tetromino.Direction.RIGHT;

/**
 *
 * @author Kheagen Haskins
 */
public class InputHandler extends KeyAdapter {

    // ------------------------------ Fields -------------------------------- //
    private GameMatrix matrix;
    private JComponent container;

    // --------------------------- Constructors ----------------------------- //
    public InputHandler(JComponent container, GameMatrix matrix) {
        this.matrix = matrix;
        this.container = container;
    }

    // ---------------------------- API Methods ----------------------------- //
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                matrix.rotateTetromino();
                break;
            case KeyEvent.VK_LEFT:
                matrix.moveTetromino(LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                matrix.moveTetromino(RIGHT);
                break;
            case KeyEvent.VK_DOWN:
                matrix.moveTetromino(DOWN);
                break;
            default:
                return;
        }
        container.repaint();
    }

    // -------------------------- Helper Methods ---------------------------- //
}
