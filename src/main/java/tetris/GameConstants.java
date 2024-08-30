package tetris;

import java.awt.Color;

/**
 *
 * @author Kheagen Haskins
 */
public class GameConstants {

    // ------------------------------ Fields -------------------------------- //
    /**
     * Represents the standard size of each block in a tetromino and in the game
     * grid. This size is used to ensure uniformity across all tetrominos,
     * maintaining consistency in the visual representation and behavior in the
     * game grid.
     */
    public static final int BLOCK_SIZE = 20;
    
    /**
     * The default color of the block if no color is specified.
     */
    public static final Color DEFAULT_BLOCK_COLOR = Color.RED;
    
    /**
     * To prevent instantiation
     */
    private GameConstants() {}
    
}