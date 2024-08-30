package tetris.tetromino;

import static tetris.tetromino.Tetromino.Type.*;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import static java.util.concurrent.ThreadLocalRandom.current;
import tetris.GameConstants;
import static tetris.GameConstants.DEFAULT_BLOCK_COLOR;
import tetris.tetromino.Tetromino.Type;
import static tetris.GameConstants.BLOCK_SIZE;

/**
 * Provides a centralised factory for creating various types of
 * {@link Tetromino} objects in a Tetris game. This class uses a factory pattern
 * to encapsulate the creation logic of different tetromino shapes, making it
 * easy to manage and extend the shape creation process. The factory supports
 * the creation of standard Tetris blocks like I, O, T, S, and Z, as well as
 * larger and more complex forms such as T_LARGE and Z_LARGE.
 *
 * <p>
 * Each tetromino type is associated with a specific method through a static
 * map, allowing dynamic retrieval and instantiation of tetromino objects based
 * on their type. This approach simplifies modifications and additions to the
 * types of tetrominos available without altering the client code. It enhances
 * maintainability by centralising the creation logic and reducing dependencies
 * in the client classes.</p>
 *
 * <p>
 * The class also provides utility methods for creating a single tetromino, a
 * default-colored tetromino, and an array of tetrominos representing one of
 * each type defined. These methods ensure that all tetrominos created are
 * consistent in terms of construction and initialisation, adhering to the
 * game's rules and mechanics.</p>
 *
 * <p>
 * Note: This class is intended to be used statically, hence it cannot be
 * instantiated.</p>
 *
 * @author Kheagen
 * @see Tetromino
 * @see Block
 * @see GameConstants
 * @see Color
 *
 */
public class TetroFactory {

    // ------------------------------ Static -------------------------------- //
    /**
     * A mapping of {@link Type} enums to their respective
     * {@link TetrominoCreator} functional interfaces. This map serves as the
     * central mechanism for the factory pattern, allowing the dynamic creation
     * of different tetromino types based on their predefined methods. Each
     * tetromino type defined in the {@link Type} enum is associated with a
     * specific method that constructs its unique shape and configuration.
     *
     * <p>
     * Initialisation occurs statically, with each tetromino type and its
     * creation logic bound at the time of class loading. This ensures that the
     * mapping is available and ready to use as soon as the class is referenced,
     * without needing additional runtime setup.</p>
     */
    private static final Map<Type, TetrominoCreator> TET_CREATORS = new HashMap<>();

    static {
        TET_CREATORS.put(F, TetroFactory::createF);
        TET_CREATORS.put(I, TetroFactory::createI);
        TET_CREATORS.put(J, TetroFactory::createJ);
        TET_CREATORS.put(L, TetroFactory::createL);
        TET_CREATORS.put(N, TetroFactory::createN);
        TET_CREATORS.put(O, TetroFactory::createO);
        TET_CREATORS.put(P, TetroFactory::createP);
        TET_CREATORS.put(S, TetroFactory::createS);
        TET_CREATORS.put(T, TetroFactory::createT);
        TET_CREATORS.put(U, TetroFactory::createU);
        TET_CREATORS.put(W, TetroFactory::createW);
        TET_CREATORS.put(X, TetroFactory::createX);
        TET_CREATORS.put(Y, TetroFactory::createY);
        TET_CREATORS.put(Z, TetroFactory::createZ);
        TET_CREATORS.put(Z_LARGE, TetroFactory::createZLarge);
        TET_CREATORS.put(T_LARGE, TetroFactory::createTLarge);
        TET_CREATORS.put(SINGLE, (color) -> new Tetromino(new Block[][]{{block(BLOCK_SIZE)}}));
    }

    /**
     * An array containing all possible types of tetrominos defined in the
     * {@link Type} enum. This array is used to facilitate operations that
     * require iterating over all possible tetromino types, such as generating a
     * sample set of each type for display or testing purposes.
     */
    private static final Type[] types = Tetromino.Type.values();

    /**
     * Private constructor to prevent instantiation of this utility class. The
     * class is intended to be used statically, hence it is designed to not have
     * any instances created.
     */
    private TetroFactory() {
    }

    // ---------------------------- API Methods ----------------------------- //
    /**
     * Creates a new {@link Tetromino} of the specified type and color. This
     * method serves as the primary way to obtain a tetromino with custom
     * specifications.
     *
     * @param type The type of tetromino to create, as defined by the
     * {@link Type} enum.
     * @param color The {@link Color} to apply to the tetromino blocks.
     * @return A new {@link Tetromino} instance of the specified type and color.
     * @throws IllegalArgumentException if the specified tetromino type is not
     * handled by the factory.
     */
    public static Tetromino createNewTetromino(Type type, Color color) {
        TetrominoCreator creator = TET_CREATORS.get(type);
        if (creator != null) {
            return creator.create(color);
        } else {
            throw new IllegalArgumentException("Unhandled tetromino type: " + type);
        }
    }

    /**
     * Creates a new {@link Tetromino} of the specified type using the default
     * block color. This method facilitates quick creation of tetrominos without
     * specifying a color.
     *
     * @param type The type of tetromino to create, as defined by the
     * {@link Type} enum.
     * @return A new {@link Tetromino} instance of the specified type, colored
     * with {@link Block#DEFAULT_BLOCK_COLOR}.
     */
    public static Tetromino createNewTetromino(Type type) {
        return createNewTetromino(type, DEFAULT_BLOCK_COLOR);
    }

    /**
     * Generates an array of {@link Tetromino} objects, each representing one of
     * the types defined in the {@link Type} enum. This method is useful for
     * generating a complete set of different tetromino types, typically for
     * testing, display, or game initialisation purposes.
     *
     * @return An array of {@link Tetromino} objects, with one tetromino for
     * each type defined in the Type enum.
     */
    public static Tetromino[] createOneOfEach() {
        Tetromino[] tArr = new Tetromino[types.length];
        for (int i = 0; i < tArr.length; i++) {
            tArr[i] = createNewTetromino(types[i]);
        }
        return tArr;
    }

    /**
     * Randomly selects a {@link Type} from the available tetromino types.
     *
     * @return A randomly selected {@link Type}.
     */
    public static Type randomType() {
        int randomIndex = current().nextInt(types.length);
        return types[randomIndex];
    }

    public static Type[] getSimpleTypesOnly() {
        return new Type[]{I, O, J, L, S, Z, T};
    }

    // -------------------------- Creation Methods ---------------------------- //
    /**
     * Creates an "I" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "I" shaped Tetromino.
     */
    private static Tetromino createI(Color color) {
        Block[][] shape = shape(4, 1);
        return new Tetromino(color, shape);
    }

    /**
     * Creates a larger "Z" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new larger "Z" shaped Tetromino.
     */
    private static Tetromino createZLarge(Color color) {
        Block[][] shape = shape(3, 3);
        switchOffAt(shape, new int[][]{{0, 2}, {1, 0}, {1, 2}, {2, 0}});
        return new Tetromino(color, shape);
    }

    /**
     * Creates a larger "T" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new larger "T" shaped Tetromino.
     */
    private static Tetromino createTLarge(Color color) {
        Block[][] shape = shape(3, 3);
        switchOffAt(shape, new int[][]{{1, 0}, {1, 2}, {2, 0}, {2, 2}});
        return new Tetromino(color, shape);
    }

    /**
     * Creates an "F" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "F" shaped Tetromino.
     */
    private static Tetromino createF(Color color) {
        Block[][] shape = shape(3, 3);
        switchOffAt(shape, new int[][]{{0, 0}, {1, 2}, {2, 0}, {2, 2}});
        return new Tetromino(color, shape);
    }

    /**
     * Creates a "W" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "W" shaped Tetromino.
     */
    private static Tetromino createW(Color color) {
        Block[][] shape = shape(3, 3);
        switchOffAt(shape, new int[][]{{0, 1}, {0, 2}, {1, 2}, {2, 0}});
        return new Tetromino(color, shape);
    }

    /**
     * Creates an "X" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "X" shaped Tetromino.
     */
    private static Tetromino createX(Color color) {
        Block[][] shape = shape(3, 3);
        switchOffAt(shape, new int[][]{{0, 0}, {0, 2}, {2, 0}, {2, 2}});
        return new Tetromino(color, shape);
    }

    /**
     * Creates a "U" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "U" shaped Tetromino.
     */
    private static Tetromino createU(Color color) {
        Block[][] shape = shape(2, 3);
        shape[0][1].setVisible(false);
        return new Tetromino(color, shape);
    }

    /**
     * Creates a "Y" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "Y" shaped Tetromino.
     */
    private static Tetromino createY(Color color) {
        Block[][] shape = shape(4, 2);
        switchOffAt(shape, new int[][]{{0, 1}, {2, 1}, {3, 1}});
        return new Tetromino(color, shape);
    }

    /**
     * Creates an "N" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "N" shaped Tetromino.
     */
    private static Tetromino createN(Color color) {
        Block[][] shape = shape(4, 2);
        switchOffAt(shape, new int[][]{{0, 0}, {1, 0}, {3, 1}});
        return new Tetromino(color, shape);
    }

    /**
     * Creates a "P" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "P" shaped Tetromino.
     */
    private static Tetromino createP(Color color) {
        Block[][] shape = shape(3, 2);
        shape[2][1].setVisible(false);
        return new Tetromino(color, shape);
    }

    /**
     * Creates an "O" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "O" shaped Tetromino.
     */
    private static Tetromino createO(Color color) {
        Block[][] shape = shape(2, 2);
        return new Tetromino(color, shape);
    }

    /**
     * Creates a "T" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "T" shaped Tetromino.
     */
    private static Tetromino createT(Color color) {
        Block[][] shape = shape(2, 3);
        switchOffAt(shape, new int[][]{{1, 0}, {1, 2}});
        return new Tetromino(color, shape);
    }

    /**
     * Creates an "S" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "S" shaped Tetromino.
     */
    private static Tetromino createS(Color color) {
        Block[][] shape = shape(2, 3);
        switchOffAt(shape, new int[][]{{0, 0}, {1, 2}});
        return new Tetromino(color, shape);
    }

    /**
     * Creates a "Z" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "Z" shaped Tetromino.
     */
    private static Tetromino createZ(Color color) {
        Block[][] shape = shape(2, 3);
        switchOffAt(shape, new int[][]{{0, 2}, {1, 0}});
        return new Tetromino(color, shape);
    }

    /**
     * Creates an "L" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "L" shaped Tetromino.
     */
    private static Tetromino createL(Color color) {
        Block[][] shape = shape(3, 2);
        switchOffAt(shape, new int[][]{{0, 1}, {1, 1}});
        return new Tetromino(color, shape);
    }

    /**
     * Creates a "J" shaped tetromino.
     *
     * @param color The color to apply to the tetromino.
     * @return A new "J" shaped Tetromino.
     */
    private static Tetromino createJ(Color color) {
        Block[][] shape = shape(3, 2);
        switchOffAt(shape, new int[][]{{0, 0}, {1, 0}});
        return new Tetromino(color, shape);
    }

    // -------------------------- Helper Methods ---------------------------- //
    /**
     * Creates a single {@link Block} with specified dimensions. This method
     * centranises the creation of blocks, ensuring consistency in the size of
     * blocks across all tetrominos created by the factory.
     *
     * @param size The side length for the square block to be created. All
     * blocks in the tetrominos are square, maintaining uniformity in appearance
     * and collision detection.
     * @return A new {@link Block} instance with both width and height set to
     * the specified size.
     */
    private static Block block(int size) {
        return new Block(size, size);
    }

    /**
     * Constructs a 2D array of {@link Block} objects representing the shape of
     * a tetromino. This method initianises each block within the array, setting
     * them to a standard block size defined by {@link #BLOCK_SIZE}. The method
     * ensures that the shape array is filled completely with new blocks, ready
     * for further modification such as setting visibility or color.
     *
     * @param rows The number of rows in the tetromino shape array.
     * @param cols The number of columns in the tetromino shape array.
     * @return A 2D array of {@link Block} objects, representing the initial
     * state of a tetromino's shape.
     */
    private static Block[][] shape(int rows, int cols) {
        Block[][] shape = new Block[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                shape[r][c] = block(BLOCK_SIZE);
            }
        }
        return shape;
    }

    /**
     * Alters the visibility of specific blocks within a tetromino's shape
     * array. This method is used to configure complex tetromino shapes by
     * selectively disabling blocks, thereby defining the unique pattern of each
     * tetromino type. It is a key component in customizing the standard grid of
     * blocks to represent different tetromino shapes.
     *
     * @param shape The 2D array of {@link Block} objects representing a
     * tetromino's shape.
     * @param indices An array of integer pairs, where each pair contains the
     * row and column indices of a block whose visibility should be set to
     * false. This method directly modifies the visibility state of the blocks
     * at the specified indices.
     */
    private static void switchOffAt(Block[][] shape, int[][] indices) {
        for (int i = 0; i < indices.length; i++) {
            int r = indices[i][0];
            int c = indices[i][1];
            shape[r][c].setVisible(false);
        }
    }

    /**
     * Functional interface for creating tetrominos. This interface provides a
     * single abstract method for creating a tetromino of a specified color,
     * allowing the implementation to define the precise details of tetromino
     * construction. This design facilitates the mapping of tetromino types to
     * specific creation logic within the factory class, supporting the addition
     * of new types without modifying existing code.
     *
     * @param color The {@link Color} to be applied to the new tetromino.
     * @return A new {@link Tetromino} object, customized with the specified
     * color.
     */
    @FunctionalInterface
    private interface TetrominoCreator {

        Tetromino create(Color color);
    }

}
