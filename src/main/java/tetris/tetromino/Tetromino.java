package tetris.tetromino;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import static tetris.GameConstants.DEFAULT_BLOCK_COLOR;
import tetris.utility.IllegalArgs;

/**
 * Represents a Tetromino in the Tetris game. Each Tetromino consists of a grid
 * of blocks. This class provides methods for moving and rotating the Tetromino,
 * as well as getting and setting its properties.
 * <p>
 * The Tetromino is represented as a 2D array of {@link Block} objects, which
 * define its shape. The class ensures that all blocks within a Tetromino have
 * the same dimensions.
 * <p>
 * The class supports basic operations such as moving the Tetromino in different
 * directions ({@link Direction}) and rotating it either clockwise or
 * counter-clockwise ({@link Rotation}).
 * <p>
 * Usage example:
 * <pre>
 * Block[][] shape = ...
 * Tetromino tetromino = new Tetromino(shape);
 * tetromino.move(Direction.LEFT);
 * tetromino.rotate(Rotation.CLOCKWISE);
 * </pre>
 * </p>
 * <p>
 * This class also contains a custom exception
 * {@link IllegalTetrominoShapeException} to handle invalid Tetromino shapes,
 * ensuring that all blocks have the same width and height.
 * </p>
 * <p>
 * Note: The Tetromino class uses {@link IllegalArgs} for input validation,
 * ensuring that arguments passed to its methods and constructors are valid.
 * </p>
 *
 * @see Block
 * @see Direction
 * @see Rotation
 * @see IllegalTetrominoShapeException
 * @see IllegalArgs
 *
 * @version 1.0
 * @since 1.0
 *
 * @author Kheagen Haskins
 */
public class Tetromino {

    // ------------------------------ Static -------------------------------- //
    /**
     * Enumeration to define valid directions for moving the Tetromino.
     * <p>
     * The directions are defined as follows:
     * <ul>
     * <li>{@link #LEFT}: Move the Tetromino to the left.</li>
     * <li>{@link #RIGHT}: Move the Tetromino to the right.</li>
     * <li>{@link #DOWN}: Move the Tetromino downward.</li>
     * </ul>
     */
    public static enum Direction {
        LEFT, RIGHT, DOWN
    }

    /**
     * Enumeration to define valid rotations for rotating the Tetromino.
     * <p>
     * The rotations are defined as follows:
     * <ul>
     * <li>{@link #CLOCKWISE}: Rotate the Tetromino 90 degrees clockwise.</li>
     * <li>{@link #COUNTER_CLOCKWISE}: Rotate the Tetromino 90 degrees
     * counter-clockwise.</li>
     * </ul>
     */
    public static enum Rotation {
        CLOCKWISE, COUNTER_CLOCKWISE
    }

    public static enum Type {
        I, O, T, S, Z, J, L, F, P, N, U, X, Y, W, Z_LARGE, T_LARGE, SINGLE
    }

    /**
     * Exception to represent an invalid Tetromino shape.
     * <p>
     * This exception is thrown when the shape of the Tetromino is invalid. An
     * invalid shape occurs when not all blocks within the Tetromino have the
     * same height and width, which is a requirement for a valid Tetromino
     * shape.
     * </p>
     */
    public static class IllegalTetrominoShapeException extends IllegalArgumentException {

        /**
         * Constructs an IllegalTetrominoShapeException with a default message
         * indicating that the Tetromino shape is invalid because all blocks
         * must have the same height and width.
         */
        public IllegalTetrominoShapeException() {
            super("Illegal Tetromino shape; all blocks must have the same height and width");
        }

        /**
         * Constructs an IllegalTetrominoShapeException with a specified detail
         * message.
         *
         * @param s the detail message explaining why the Tetromino shape is
         * invalid
         */
        public IllegalTetrominoShapeException(String s) {
            super(s);
        }
    }

    // ------------------------------ Fields -------------------------------- //
    private Block[][] shape;
    private final int blockSize;
    private int x, y;
    private int vSpeed, hSpeed; // horizontal and vertical speeds
    private Color color; // if the blocks do not color themselves

    /**
     * Constructs a Tetromino with a specified shape using the default color.
     *
     * @param shape a 2D array representing the shape of the Tetromino. Each
     * element in the array should be a {@link Block} object, where each block
     * has the same width and height. Blocks can be null if that position in the
     * Tetromino is empty.
     * @throws IllegalArgumentException if the blocks array is null or empty.
     * @throws Tetromino.IllegalTetrominoShapeException if the blocks do not
     * have the same dimensions or the array is not rectangular.
     */
    Tetromino(Block[][] shape) {
        IllegalArgs.throwEmpty("Tetromino 2D Blocks array", shape);

        this.shape = shape;
        this.color = DEFAULT_BLOCK_COLOR;
        this.blockSize = shape[0][0].getWidth(); // Arbitrarily goes off width
        init();
    }

    /**
     * Constructs a Tetromino with a specified color and shape.
     *
     * @param color the color of the Tetromino. If the blocks do not define
     * their own color, this color will be used.
     * @param shape a 2D array representing the shape of the Tetromino. Each
     * element in the array should be a {@link Block} object, where each block
     * has the same width and height. Blocks can be null if that position in the
     * Tetromino is empty.
     * @throws IllegalArgumentException if the color is null or the blocks array
     * is null or empty.
     * @throws Tetromino.IllegalTetrominoShapeException if the blocks do not
     * have the same dimensions or the array is not rectangular.
     */
    Tetromino(Color color, Block[][] shape) {
        IllegalArgs.throwNull("Tetromino color", color);
        IllegalArgs.throwEmpty("Tetromino 2D Blocks array", shape);

        this.color = color;
        this.shape = shape;
        this.blockSize = shape[0][0].getWidth(); // Arbitrarily goes off width
        init();
    }

    // ------------------------------ Getters ------------------------------- //
    /**
     * Returns the x-coordinate of the Tetromino's position.
     *
     * @return the x-coordinate of the Tetromino.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the Tetromino's position.
     *
     * @return the y-coordinate of the Tetromino.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the width of the Tetromino in terms of the number of blocks.
     *
     * @return the horizontal block count of the Tetromino.
     */
    public int getHBlockCount() {
        return shape[0].length;
    }

    /**
     * Returns the height of the Tetromino in terms of the number of blocks.
     *
     * @return the vertical block count of the Tetromino.
     */
    public int getVBlockCount() {
        return shape.length;
    }

    /**
     * Retrieves a single row of blocks from the Tetromino's shape.
     *
     * @param rowNum the row index of the blocks to retrieve. Must be a
     * non-negative integer and less than the height of the Tetromino.
     * @return an array of {@link Block} objects representing the specified row
     * in the Tetromino's shape.
     * @throws IllegalArgumentException if the row index is out of range. The
     * method uses {@link IllegalArgs#throwOutOfRange} to validate that the
     * index is within the bounds of the Tetromino's shape dimensions.
     */
    public Block[] getRow(int rowNum) {
        IllegalArgs.throwOutOfRange("row index", rowNum, 0, getVBlockCount());
        return shape[rowNum];
    }

    /**
     * Retrieves a single column of blocks from the Tetromino's shape. This
     * method ensures that the specified column index is within the valid range
     * of the Tetromino's dimensions.
     *
     * @param colNum the column index of the blocks to retrieve. Must be a
     * non-negative integer and less than the width of the Tetromino.
     * @return an array of {@link Block} objects representing the specified
     * column in the Tetromino's shape. Blocks can be null if that position in
     * the Tetromino is empty.
     * @throws IllegalArgumentException if the column index is out of range. The
     * method uses {@link IllegalArgs#throwOutOfRange} to validate that the
     * index is within the bounds of the Tetromino's shape dimensions.
     */
    public Block[] getColumn(int colNum) {
        IllegalArgs.throwOutOfRange("column index", colNum, 0, getHBlockCount());
        Block[] col = new Block[getVBlockCount()];
        for (int ri = 0; ri < getVBlockCount(); ri++) {
            col[ri] = shape[ri][colNum];
        }
        return col;
    }

    /**
     * Retrieves the leftmost column of blocks from the Tetromino's shape.
     *
     * @return an array of {@link Block} objects representing the leftmost
     * column of the Tetromino. This is a convenience method that calls
     * {@link #getColumn(int)} with a column index of 0.
     */
    public Block[] getLeftColumn() {
        return getColumn(0);
    }

    /**
     * Retrieves the rightmost column of blocks from the Tetromino's shape.
     *
     * @return an array of {@link Block} objects representing the rightmost
     * column of the Tetromino. This is a convenience method that calls
     * {@link #getColumn(int)} with a column index equal to the width of the
     * Tetromino minus one.
     */
    public Block[] getRightColumn() {
        return getColumn(getHBlockCount() - 1);
    }

    /**
     * Retrieves the bottom row of blocks from the Tetromino's shape. This is a
     * convenience method for retrieving the last row of the shape array.
     *
     * @return an array of {@link Block} objects representing the bottom row of
     * the Tetromino. This method calls {@link #getRow(int)} with a row index
     * equal to the height of the Tetromino minus one.
     */
    public Block[] getBottomRow() {
        return getRow(getVBlockCount() - 1);
    }

    /**
     * The uniform size of each block in this shape.
     *
     * @return the uniform size of each block in this shape.
     */
    public int getBlockSize() {
        return blockSize;
    }

    /**
     * Returns the color of the Tetromino.
     *
     * @return the color of the Tetromino.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Retrieve the speed that this shape travels horizontally
     *
     * @return the speed that this shape travels horizontally
     */
    public int getHorizontalSpeed() {
        return hSpeed;
    }

    /**
     * Retrieve the speed that this shape travels vertically
     *
     * @return the speed that this shape travels vertically
     */
    public int getVerticalSpeed() {
        return vSpeed;
    }

    /**
     * Retrieves the {@link Block} at the specified row and column in the
     * Tetromino's shape. This method ensures that the specified indices are
     * within the valid range of the Tetromino's dimensions. It throws an
     * exception if either the row or column index is out of the Tetromino's
     * bounds.
     *
     * @param row the row index of the block to retrieve.
     * @param col the column index of the block to retrieve.
     * @return the {@link Block} at the specified row and column.
     * @throws IllegalArgumentException if the row or column index is out of
     * range. The method uses {@link IllegalArgs#throwOutOfRange} to validate
     * that both indices are within the bounds of the Tetromino's shape
     * dimensions.
     */
    public Block getBlockAt(int row, int col) {
        IllegalArgs.throwOutOfRange("row index", row, 0, getVBlockCount());
        IllegalArgs.throwOutOfRange("col index", col, 0, getHBlockCount());

        return shape[row][col];
    }

    // ------------------------------ Setters ------------------------------- //
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets the color of the Tetromino.
     *
     * @param color the new color of the Tetromino.
     * @throws IllegalArgumentException if the color is null.
     */
    public void setColor(Color color) {
        IllegalArgs.throwNull("Tetromino color", color);
        this.color = color;
    }

    /**
     * Sets the speed that this shape travels horizontally
     *
     * @param hSpeed the new speed that this shape travels horizontally
     */
    public void setHorizontalSpeed(int hSpeed) {
        this.hSpeed = hSpeed;
    }

    /**
     * Sets the speed that this shape travels vertically
     *
     * @param vSpeed the new speed that this shape travels vertically
     */
    public void setVerticalSpeed(int vSpeed) {
        this.vSpeed = vSpeed;
    }

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Moves the Tetromino in the specified direction.
     *
     * @param d the direction to move the Tetromino. Valid directions are
     * {@link Direction#LEFT}, {@link Direction#RIGHT}, and
     * {@link Direction#DOWN}.
     * @throws IllegalArgumentException if the direction is null.
     */
    public void move(Direction d) {
        IllegalArgs.throwNull("Direction", d);

        switch (d) {
            case LEFT:
                x -= hSpeed;
                break;
            case RIGHT:
                x += hSpeed;
                break;
            case DOWN:
                y += vSpeed;
                break;
        }

        updateBlockPositions();
    }

    /**
     * Rotates the Tetromino in the specified direction.
     *
     * @param r the rotation direction. Valid rotations are
     * {@link Rotation#CLOCKWISE} and {@link Rotation#COUNTER_CLOCKWISE}.
     * @throws IllegalArgumentException if the rotation is null.
     */
    public void rotate(Rotation r) {
        IllegalArgs.throwNull("Rotation", r);
        shape = rotate(shape, r);
        updateBlockPositions();
    }

    /**
     * Draws the Tetromino on the given {@link Graphics2D} context. This method
     * iterates over each block within the Tetromino's shape, checking if the
     * block is visible. If a block is visible, it is drawn on the specified
     * {@link Graphics2D} context using the block's dimensions and position.
     * <p>
     * This method is typically called within a game's rendering loop to
     * visually represent the Tetromino on the screen. Each block's position and
     * size are used to determine where and how large the filled rectangle
     * representing the block should be.
     * </p>
     *
     * @param g the {@link Graphics2D} context on which the Tetromino is drawn.
     * It cannot be null.
     * @throws NullPointerException if the {@link Graphics2D} context provided
     * is null.
     */
    /**
     * Paints the Tetromino onto the specified {@link Graphics2D} graphics
     * context. This method first sets the fill color for the Tetromino, fills
     * visible blocks, and then draws black borders around visible blocks where
     * necessary.
     *
     * @param g the {@link Graphics2D} graphics context on which to paint the
     * Tetromino.
     */
    public void paint(Graphics2D g) {
        // Set the fill color for the Tetromino blocks
        g.setColor(color);

        // Draw filled rectangles for visible blocks
        for (Block[] row : shape) {
            for (Block b : row) {
                if (b.isVisible()) {
                    g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
                }
            }
        }

        // Draw borders for visible blocks
        g.setColor(Color.BLACK);
        drawBorders(g);
    }

    /**
     * Updates the positions of the blocks within the Tetromino. This method is
     * called internally within the class but can be called externally when
     * updates need to be made by a controlling class.
     * <p>
     * This method iterates over the 2D array of blocks and updates each block's
     * position based on the Tetromino's current x and y coordinates. The new
     * position of each block is calculated by adding the product of its index
     * and the block size to the Tetromino's x and y coordinates.
     * </p>
     */
    public final void updateBlockPositions() {
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {
                shape[row][col].setX(x + col * blockSize);
                shape[row][col].setY(y + row * blockSize);
            }
        }
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Initializes the Tetromino by validating its shape and setting its initial
     * position.
     * <p>
     * This method checks if the shape of the Tetromino is valid using
     * {@link #validateShape()}. If the shape is not valid, it throws an
     * {@link IllegalTetrominoShapeException}. It also sets the initial x and y
     * coordinates of the Tetromino based on the first block in the shape array.
     * </p>
     *
     * @throws IllegalTetrominoShapeException if the shape of the Tetromino is
     * invalid.
     */
    private void init() {
        validateShape();

        this.x = shape[0][0].getX();
        this.y = shape[0][0].getY();
        this.hSpeed = blockSize;
        this.vSpeed = blockSize;

        updateBlockPositions();
    }

    /**
     * Checks if the shape of the Tetromino is valid.
     * <p>
     * A valid shape means all blocks in the shape are non-null and have the
     * same width and height as the {@link #blockSize}. This method iterates
     * over all the blocks in the shape and compares their dimensions to the
     * blockSize.
     * </p>
     *
     * @return {@code true} if the shape is valid, {@code false} otherwise.
     */
    private void validateShape() {
        // validate size of each block
        for (Block[] blockRow : shape) {
            for (Block block : blockRow) {
                if (block == null) {
                    throw new IllegalTetrominoShapeException("Tetronimo shape cannot contain null element");
                }

                if (block.getWidth() != blockSize || block.getHeight() != blockSize) {
                    throw new IllegalTetrominoShapeException("Tetronimo block sizes are not all equal");
                }
            }
        }

        Set<Block> uniqueBlocks = new HashSet<>(); // can only contain unique elements
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (!uniqueBlocks.add(shape[i][j])) { // returns false if the element to add is a duplicate
                    throw new IllegalTetrominoShapeException("Tetromino blocks must all be unique Block instances");
                }
            }
        }
    }

    /**
     * Rotates the given 2D array of blocks.
     * <p>
     * This method rotates the 2D array of blocks either clockwise or
     * counterclockwise depending on the specified rotation. For a clockwise
     * rotation, each block is moved to its new position in the rotated array
     * accordingly. For a counterclockwise rotation, each block is moved to its
     * new position in the rotated array accordingly.
     * </p>
     *
     * @param matrix the 2D array of blocks to rotate.
     * @param r the rotation direction, either {@link Rotation#CLOCKWISE} or
     * {@link Rotation#COUNTER_CLOCKWISE}.
     * @return the rotated 2D array of blocks.
     */
    private Block[][] rotate(Block[][] matrix, Rotation r) {
        int rows = getVBlockCount();
        int cols = getHBlockCount();
        Block[][] rotated = new Block[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (r == Rotation.CLOCKWISE) {
                    rotated[j][rows - 1 - i] = matrix[i][j]; // clockwise
                } else {
                    rotated[cols - 1 - j][i] = matrix[i][j]; // counterclockwise
                }
            }
        }
        return rotated;
    }

    /**
     * Helper method to draw borders around the visible blocks of the Tetromino.
     *
     * @param g the {@link Graphics2D} graphics context on which to draw the
     * borders.
     */
    private void drawBorders(Graphics2D g) {
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                Block b = shape[row][col];
                if (b.isVisible()) {
                    drawBlockBorders(g, b, row, col);
                }
            }
        }
    }

    /**
     * Draws borders around a single block if adjacent blocks are not visible.
     *
     * @param g the {@link Graphics2D} graphics context.
     * @param b the block for which borders are to be drawn.
     * @param row the row index of the block within the Tetromino shape.
     * @param col the column index of the block within the Tetromino shape.
     */
    private void drawBlockBorders(Graphics2D g, Block b, int row, int col) {
        int bx = b.getX();
        int by = b.getY();
        int width = b.getWidth();
        int height = b.getHeight();

        // Top border
        if (row == 0 || !shape[row - 1][col].isVisible()) {
            g.drawLine(bx, by, bx + width, by);
        }

        // Bottom border
        if (row + 1 == shape.length || !shape[row + 1][col].isVisible()) {
            g.drawLine(bx, by + height, bx + width, by + height);
        }

        // Left border
        if (col == 0 || !shape[row][col - 1].isVisible()) {
            g.drawLine(bx, by, bx, by + height);
        }

        // Right border
        if (col + 1 == shape[row].length || !shape[row][col + 1].isVisible()) {
            g.drawLine(bx + width, by, bx + width, by + height);
        }
    }

}
