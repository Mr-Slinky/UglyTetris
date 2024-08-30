package tetris.tetromino;

import java.awt.Color;
import static tetris.GameConstants.DEFAULT_BLOCK_COLOR;
import tetris.utility.IllegalArgs;

/**
 * Represents a single block in the Tetris game. This class provides methods for
 * getting and setting the block's properties such as its position, size, and
 * color.
 * <p>
 * Each block is defined by its x and y coordinates, width, height, and color.
 * Blocks can be created with a default color or a specified color.
 * </p>
 * <p>
 * Usage example:
 * <pre>
 Block block1 = new Block(0, 0, 20, 20);
 Block block2 = new Block(0, 0, 20, 20, Color.RED);
 block1.setX(10);
 block2.setColor(Color.BLUE);
 </pre>
 * </p>
 *
 * @author Kheagen Haskins
 */
public class Block {

    // ------------------------------ Fields -------------------------------- //
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean visible;
    private Color blockColor;

    // --------------------------- Constructors ----------------------------- //
    /**
     * Constructs a Block with the specified position, size, and color. This
     * block is visible by default
     *
     * @param x the x-coordinate of the block.
     * @param y the y-coordinate of the block.
     * @param width the width of the block.
     * @param height the height of the block.
     * @param blockColor the color of the block.
     * @throws IllegalArgumentException if x, y, width, or height are negative,
     * or if blockColor is null.
     */
    public Block(int x, int y, int width, int height, Color blockColor) {
        IllegalArgs.throwNegative("x", x);
        IllegalArgs.throwNegative("y", y);
        IllegalArgs.throwNegative("width", width);
        IllegalArgs.throwNegative("height", height);
        IllegalArgs.throwNull("Block color", blockColor);

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.blockColor = blockColor;
        this.visible = true;
    }

    /**
     * Constructs a Block with the specified position and size, using the
     * default color. This block is visible by default
     *
     * @param x the x-coordinate of the block.
     * @param y the y-coordinate of the block.
     * @param width the width of the block.
     * @param height the height of the block.
     * @throws IllegalArgumentException if x, y, width, or height are negative.
     */
    public Block(int x, int y, int width, int height) {
        this(x, y, width, height, DEFAULT_BLOCK_COLOR);
    }

    /**
     * Constructs a Block with the specified size, using the default color. This
     * block is visible by default, and the position is set to (0, 0).
     *
     * @param width the width of the block.
     * @param height the height of the block.
     * @throws IllegalArgumentException if x, y, width, or height are negative.
     */
    public Block(int width, int height) {
        this(0, 0, width, height, DEFAULT_BLOCK_COLOR);
    }

    /**
     * Constructs a Block with the specified size and color. This block is
     * visible by default, and the position is set to (0, 0).
     *
     * @param width the width of the block.
     * @param height the height of the block.
     * @param color the color of the block.
     * @throws IllegalArgumentException if x, y, width, or height are negative,
     * or if blockColor is null.
     */
    public Block(int width, int height, Color color) {
        this(0, 0, width, height, color);
    }

    // ------------------------------ Getters ------------------------------- //
    /**
     * Returns the x-coordinate of the block.
     *
     * @return the x-coordinate of the block.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the block.
     *
     * @return the y-coordinate of the block.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the width of the block.
     *
     * @return the width of the block.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the block.
     *
     * @return the height of the block.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the visibility status of the block. A visible block will be
     * rendered in the game, whereas an invisible block will not.
     *
     * @return true if the block is visible, false otherwise.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Returns the color of the block.
     *
     * @return the color of the block.
     */
    public Color getColor() {
        return blockColor;
    }

    // ------------------------------ Setters ------------------------------- //
    /**
     * Sets the x-coordinate of the block.
     *
     * @param x the new x-coordinate of the block.
     * @throws IllegalArgumentException if x is negative.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the block.
     *
     * @param y the new y-coordinate of the block.
     * @throws IllegalArgumentException if y is negative.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Sets the width of the block.
     *
     * @param width the new width of the block.
     * @throws IllegalArgumentException if width is negative.
     */
    public void setWidth(int width) {
        IllegalArgs.throwNegative("width", width);
        this.width = width;
    }

    /**
     * Sets the height of the block.
     *
     * @param height the new height of the block.
     * @throws IllegalArgumentException if height is negative.
     */
    public void setHeight(int height) {
        IllegalArgs.throwNegative("height", height);
        this.height = height;
    }

    /**
     * Sets the visibility of the block. This allows for controlling whether the
     * block should be rendered in the Tetris game interface.
     *
     * @param visible the visibility status to set for this block.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Sets the color of the block.
     *
     * @param color the new color of the block.
     * @throws IllegalArgumentException if color is null.
     */
    public void setColor(Color color) {
        IllegalArgs.throwNull("Block color", color);
        this.blockColor = color;
    }

    /**
     * Returns a string representation of the block for debugging purposes.
     *
     * @return a string representation of the block, including its position,
     * size, and color.
     */
    @Override
    public String toString() {
        StringBuilder outp = new StringBuilder();

        outp.append(super.toString()).append(":\n");
        outp.append("x:\t").append(x).append("\n");
        outp.append("y:\t").append(y).append("\n");
        outp.append("width:\t").append(width).append("\n");
        outp.append("height:\t").append(height);

        return outp.toString();
    }

}
