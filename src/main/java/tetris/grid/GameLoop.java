package tetris.grid;

import java.awt.event.ActionEvent;
import static java.util.concurrent.ThreadLocalRandom.current;
import javax.swing.JComponent;
import javax.swing.Timer;
import tetris.gui.ScoreBoard;
import tetris.tetromino.TetroFactory;
import tetris.tetromino.Tetromino;
import static tetris.tetromino.Tetromino.Direction.DOWN;
import tetris.tetromino.Tetromino.Type;

/**
 *
 * @author Kheagen
 */
public class GameLoop {

    // ------------------------------ Fields -------------------------------- //
    private int score = 0;
    private ScoreBoard scoreBoard;
    private Timer t;
    private Tetromino nextShape;
    private GameMatrix matrix;
    private JComponent container;
    private Type[] types;

    // --------------------------- Constructors ----------------------------- //
    public GameLoop(float updatesPerSecond, GameMatrix matrix, JComponent container, ScoreBoard scoreBoard) {
        this.matrix = matrix;
        this.types = TetroFactory.getSimpleTypesOnly();
        this.container = container;
        this.nextShape = getNextShape();
        this.scoreBoard = scoreBoard;
        
        int millis = (int) (1000 / updatesPerSecond);
        this.t = new Timer(millis, (ActionEvent ev) -> {
            doUpdate();
        });
    }

    // ---------------------------- API Methods ----------------------------- //
    public void stop() {
        t.stop();
    }

    public void start() {
        t.start();
    }

    // -------------------------- Helper Methods ---------------------------- //
    private void doUpdate() {
        matrix.moveTetromino(DOWN);
        if (!matrix.hasActiveTetromino()) {
            matrix.setTetronimo(nextShape);
        }
        container.repaint();

        scoreBoard.setScore(matrix.getScore());
        nextShape = getNextShape();
    }

    private int typeCursor = 0;

    private Type getNextType() {
        Type type = types[typeCursor];
        typeCursor = (typeCursor + 1) % types.length;
        return type;
    }

    private Type getRandomType() {
        return types[current().nextInt(types.length)];
    }

    private Tetromino getNextShape() {
        return TetroFactory.createNewTetromino(TetroFactory.randomType());
    }
}
