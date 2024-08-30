package tetris.grid;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JOptionPane;
import tetris.tetromino.Block;
import tetris.tetromino.Tetromino;
import tetris.tetromino.Tetromino.Direction;
import static tetris.tetromino.Tetromino.Direction.DOWN;
import static tetris.tetromino.Tetromino.Direction.LEFT;
import static tetris.tetromino.Tetromino.Direction.RIGHT;
import tetris.utility.IllegalArgs;
import static tetris.GameConstants.BLOCK_SIZE;
import tetris.tetromino.Tetromino.Rotation;
import static tetris.tetromino.Tetromino.Rotation.CLOCKWISE;

/**
 *
 * @author Kheagen Haskins
 */
public class GameMatrix {

    // ------------------------------ Fields -------------------------------- //
    private boolean drawGridLines = true;
    private int score = 0;
    private int rows;
    private int cols;
    private int blockSize;
    private int scoreMultiplier;

    private Block[][] matrix;
    private Tetromino activeTet;
    private Rotation rotation = CLOCKWISE; // Rotation the Tetromino will turn
    private Color blockColor = Color.MAGENTA;
    private Color gridColor = Color.BLACK;

    // --------------------------- Constructors ----------------------------- //
    public GameMatrix(int rowCount, int colCount) {
        rows = rowCount;
        cols = colCount;
        blockSize = BLOCK_SIZE;
        matrix = new Block[rowCount][colCount];
        initGrid();
    }

    // ------------------------------ Getters ------------------------------- //
    public boolean hasActiveTetromino() {
        return activeTet != null;
    } // Might not need this

    public int getBlockSize() {
        return blockSize;
    }

    public Block[] getRow(int r) {
        IllegalArgs.throwOutOfRange("Grid row number", r, 0, rows);
        return matrix[r];
    }

    public Block[] getColumn(int c) {
        IllegalArgs.throwOutOfRange("Grid row number", c, 0, cols);
        Block[] col = new Block[rows];
        for (int ri = 0; ri < rows; ri++) {
            col[ri] = matrix[ri][c];
        }
        return col;
    }

    public boolean gridLinesVisible() {
        return drawGridLines;
    }
    
    public int getScore() {
        return score;
    }
    
    // ------------------------------ Setters ------------------------------- //
    /**
     *
     * @param tetro
     */
    public void setTetronimo(Tetromino tetro) {
        activeTet = tetro;
        // Place shape in a random horizontal placement
        activeTet.setX((cols * blockSize / 2) - ((activeTet.getHBlockCount() + 1) * blockSize));
        activeTet.updateBlockPositions();
        if (checkGameOver(tetro)) {
            System.out.println("GAME OVER");
            System.exit(0);
        }
    }

    public void setGridLinesVisible(boolean drawGridLines) {
        this.drawGridLines = drawGridLines;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    public void setBlockColor(Color blockColor) {
        this.blockColor = blockColor;
    }

    // ---------------------------- API Methods ----------------------------- //
    public void rotateTetromino() {
        int nextX = activeTet.getX() + (activeTet.getVBlockCount() * activeTet.getBlockSize());
        if (nextX > cols * blockSize) {
            return; // Not sure how to handle this
        }
        activeTet.rotate(rotation);
    }

    public void moveTetromino(Direction dir) {
        if (activeTet == null) {
            return;
        }

        if (isCollisions(activeTet, dir)) {
            if (dir == DOWN) {
                incorporate(activeTet);
                updateScore();
            }
            return;
        }

        activeTet.move(dir);
    }

    public void paint(Graphics2D g) {
        Block b;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                b = matrix[i][j];
                if (b.isVisible()) {
                    g.setColor(blockColor);
                } else {
                    g.setColor(gridColor);
                }

                g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());

                if (drawGridLines) {
                    g.setColor(Color.BLACK);
                    g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
                }
            }
        }

        if (activeTet != null) {
            activeTet.paint(g);
        }
    }

    Block[] getColumnNextTo(Block[] tetroCol, Direction d) {
        if (!(d == LEFT || d == RIGHT)) {
            throw new IllegalArgumentException("Invalid horizontal direction: " + d);
        }

        int colNum = (tetroCol[0].getX() / blockSize);
        switch (d) {
            case LEFT:
                colNum--;
                break;
            case RIGHT:
                colNum++;
                break;
        }

        if (colNum < 0 || colNum >= cols) {
            return null;
        }

        return getColumn(colNum);
    }

    /**
     *
     * @param tetBottomRow
     * @return the row beneath the given tetromino, or {@code null} if there is
     * none
     */
    Block[] getRowBeneathTet(Block[] tetBottomRow) {
        int rowNum = ((tetBottomRow[0].getY()) / blockSize) + 1;

        if (rowNum >= rows) { // tet bottom row == grid bottom row
            return null;
        }

        return matrix[rowNum];
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Called when a new Tetromino is placed on the game matrix and instantly
     * has a collision.
     *
     * @param tetro
     */
    private boolean checkGameOver(Tetromino tetro) {
        int gr = tetro.getY() / blockSize;
        int gc = tetro.getX() / blockSize;

        Block gb, tb;
        for (int tr = 0; tr < tetro.getVBlockCount(); tr++, gr++) {
            for (int tc = 0; tc < tetro.getHBlockCount(); tc++, gc++) {
                gb = matrix[gr][gc];
                tb = tetro.getBlockAt(tr, tc);
                if (gb.isVisible() && tb.isVisible()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Merges the tetromino into the 2D array of blocks, removing it as the
     * active tetromino in the grid
     *
     * @param tetro
     */
    private void incorporate(Tetromino tetro) {
        int startingRow = tetro.getY() / blockSize;
        if (startingRow < 0) {
            triggerGameOver();
        }

        int startingColumn = tetro.getX() / blockSize;

        boolean rowOutOfBounds = startingRow + tetro.getVBlockCount() > rows;
        boolean colOutOfBounds = startingColumn + tetro.getHBlockCount() > cols;

        if (rowOutOfBounds || colOutOfBounds) {
            throw new IllegalStateException("Cannot incorporate tetronimo when it lies outside of the grid");
        }

        Block bti; // block to incorporate
        for (int r = startingRow, tr = 0; r < startingRow + tetro.getVBlockCount(); r++, tr++) {
            int tc = 0;
            for (int c = startingColumn; c < startingColumn + tetro.getHBlockCount(); c++, tc++) {
                bti = tetro.getBlockAt(tr, tc);
                if (bti.isVisible()) {
                    matrix[r][c] = bti;
                }
            }
        }

        activeTet = null;
    }

    private void updateScore() {
        scoreMultiplier = 1;
        
        for (int r = rows - 1; r >= 0; r--) {
            if (isRowFull(r)) {
                score += cols * scoreMultiplier;
                scoreMultiplier++;
                shiftDown(r);
                r++; // to recheck the row
            }
        }
    }

    private boolean isCollisions(Tetromino tetro, Direction d) {
        boolean vertical = d == DOWN;
        int outerEnd = vertical ? tetro.getVBlockCount() : tetro.getHBlockCount();
        for (int i = 0; i < outerEnd; i++) {
            Block[] tArr = vertical ? tetro.getRow(i) : tetro.getColumn(i);
            Block[] gArr = vertical ? getRowBeneathTet(tArr) : getColumnNextTo(tArr, d);

            if (gArr == null) {
                return true;
            }

            int gStart = vertical ? tArr[0].getX() : tArr[0].getY();
            gStart /= blockSize;
            int tEnd = gStart;
            tEnd += vertical ? tetro.getHBlockCount() : tetro.getVBlockCount();
            for (int gi = gStart, ti = 0; gi < tEnd; gi++, ti++) {
                Block adjBlock = gArr[gi];
                if (adjBlock.isVisible() && tArr[ti].isVisible()) {
                    return true;
                }
            }
        }

        return false;
    }

    private void shiftDown(int rowToDelete) {
        for (int r = rowToDelete; r >= 0; r--) {
            Block above;
            Block below;
            for (int c = 0; c < cols; c++) {
                above = matrix[r - 1][c];
                below = matrix[r][c];
                below.setVisible(above.isVisible());
            }

            if (isRowEmpty(r - 1)) {
                break;
            }
        }

    }

    private boolean isRowFull(int rowNum) {
        for (int c = 0; c < cols; c++) {
            if (!matrix[rowNum][c].isVisible()) {
                return false;
            }
        }

        return true;
    }

    private boolean isRowEmpty(int rowNum) {
        for (int c = 0; c < cols; c++) {
            if (matrix[rowNum][c].isVisible()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Initialises the grid to an array of invisible blocks;
     */
    private void initGrid() {
        int y = 0;
        for (int r = 0; r < rows; r++) {
            int x = 0;
            for (int c = 0; c < cols; c++) {
                matrix[r][c] = new Block(x, y, blockSize, blockSize);
                matrix[r][c].setVisible(false);
                x += blockSize;
            }

            y += blockSize;
        }
    }

    private void triggerGameOver() {
        JOptionPane.showMessageDialog(null, "GAME OVER!");
        System.exit(0);
    }

}