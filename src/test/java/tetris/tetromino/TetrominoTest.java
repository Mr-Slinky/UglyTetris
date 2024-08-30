package tetris.tetromino;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.Color;
import java.awt.Point;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.EnumSource;
import static tetris.GameConstants.DEFAULT_BLOCK_COLOR;
import tetris.tetromino.Tetromino.Rotation;
import static tetris.GameConstants.BLOCK_SIZE;

/**
 *
 * @author Kheagen Haskins
 */
public class TetrominoTest {

    // -------------------------------------------------------------- Set-Up -------------------------------------------------------------- //
    private Block[][] validShape;
    private Block validBlock;

    private static Block getBasicBlock() {
        int size = 10;
        return new Block(0, 0, size, size);
    } // Used below

    @BeforeEach
    public void setUp() {
        int size = 10;
        validBlock = new Block(0, 0, size, size);
        validShape = new Block[][]{
            {getBasicBlock(), getBasicBlock()},
            {getBasicBlock(), getBasicBlock()}
        };
    }

    // ------------------------------------------------------------ Constructors ------------------------------------------------------------ //
    @Test
    public void constructor_shouldThrowExceptionWhenShapeIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Tetromino(null),
                "Constructor must throw IllegalArgumentException if the shape array is null.");
    }

    @Test
    public void constructor_shouldThrowExceptionWhenColorIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Tetromino(null, validShape),
                "Constructor must throw IllegalArgumentException if the shape array is null.");
    }

    private static Stream<Arguments> provideIllegalShapes() {
        int blockSize = 20;
        Color defaultColor = Color.BLUE;
        // Valid block for comparison
        Block block = new Block(0, 0, blockSize, blockSize, defaultColor);
        // Block with different size
        Block blockDifferentSize = new Block(0, 0, 30, 30, defaultColor);
        // Duplicate blocks for testing unique instance requirement
        Block duplicateBlock = block;

        return Stream.of(
                Arguments.of(
                        new Block[][]{
                            {block, block},
                            {block, block}
                        }, "Tetromino blocks must all be unique Block instances"),
                Arguments.of(
                        new Block[][]{
                            {block, blockDifferentSize},
                            {getBasicBlock(), getBasicBlock()}
                        },
                        "All blocks must be of the same size."),
                Arguments.of(
                        new Block[][]{
                            {getBasicBlock(), null},
                            {getBasicBlock(), block}
                        },
                        "All blocks must be non-null.")
        );
    } // Used below

    @ParameterizedTest
    @MethodSource("provideIllegalShapes")
    public void constructor_shouldThrowIllegalTetrominoShapeExceptionForInvalidShapes(Block[][] illegalShape, String message) {
        assertThrows(
                Tetromino.IllegalTetrominoShapeException.class,
                () -> new Tetromino(illegalShape),
                message);
    }

    @Test
    public void constructor_shouldInitialiseProperlyWithValidInput() {
        Tetromino tetromino = new Tetromino(validShape);
        assertAll("Constructor should properly initialise fields with valid input",
                () -> assertEquals(0, tetromino.getX(), "X coordinate should match the first block's X."),
                () -> assertEquals(0, tetromino.getY(), "Y coordinate should match the first block's Y."),
                () -> assertEquals(validShape[0].length, tetromino.getHBlockCount(), "Width should match the number of columns in the shape."),
                () -> assertEquals(validShape.length, tetromino.getVBlockCount(), "Height should match the number of rows in the shape.")
        );
    }

    @Test
    public void constructor_withColor_shouldInitialiseProperlyWithValidInput() {
        Tetromino tetromino = new Tetromino(Color.RED, validShape);
        assertAll("Constructor with color should properly initialise fields with valid input",
                () -> assertEquals(Color.RED, tetromino.getColor(), "Tetromino color should be initialised correctly."),
                () -> assertEquals(0, tetromino.getX(), "X coordinate should match the first block's X."),
                () -> assertEquals(0, tetromino.getY(), "Y coordinate should match the first block's Y."),
                () -> assertEquals(validShape[0].length, tetromino.getHBlockCount(), "Width should match the number of columns in the shape."),
                () -> assertEquals(validShape.length, tetromino.getVBlockCount(), "Height should match the number of rows in the shape.")
        );
    }

    // ------------------------------------------------------------ Getters ------------------------------------------------------------ //
    @Test
    public void getX_shouldReturnInitialXCoordinate() {
        Block[][] shape = createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE);
        Tetromino tetromino = new Tetromino(shape);
        assertEquals(shape[0][0].getX(), tetromino.getX(), "getX should return the initial X coordinate of the Tetromino based on the first block.");
    }

    @Test
    public void getY_shouldReturnInitialYCoordinate() {
        Block[][] shape = createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE);
        Tetromino tetromino = new Tetromino(shape);
        assertEquals(shape[0][0].getY(), tetromino.getY(), "getY should return the initial Y coordinate of the Tetromino based on the first block.");
    }

    @Test
    public void getWidth_shouldReturnNumberOfColumnsInShape() {
        Block[][] shape = createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE); // 2 rows, 3 columns
        Tetromino tetromino = new Tetromino(shape);
        assertEquals(3, tetromino.getHBlockCount(), "getWidth should return the number of columns in the Tetromino shape.");
    }

    @Test
    public void getHeight_shouldReturnNumberOfRowsInShape() {
        Block[][] shape = createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE); // 2 rows, 3 columns
        Tetromino tetromino = new Tetromino(shape);
        assertEquals(2, tetromino.getVBlockCount(), "getHeight should return the number of rows in the Tetromino shape.");
    }

    @Test
    public void getColor_shouldReturnDefaultColorIfNotSpecified() {
        Block[][] shape = createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE);
        Tetromino tetromino = new Tetromino(shape);
        assertEquals(DEFAULT_BLOCK_COLOR, tetromino.getColor(), "getColor should return the default color if no specific color is set.");
    }

    @Test
    public void getColor_shouldReturnSpecifiedColor() {
        Block[][] shape = createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE);
        Color specifiedColor = Color.RED;
        Tetromino tetromino = new Tetromino(specifiedColor, shape);
        assertEquals(specifiedColor, tetromino.getColor(), "getColor should return the color specified at construction.");
    }

    @Test
    public void getHorizontalSpeed_ShouldReturnDefaultHorizontalSpeed() {
        Block[][] shape = createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE);
        Tetromino tetromino = new Tetromino(shape);
        assertEquals(tetromino.getBlockAt(0, 0).getWidth(), tetromino.getHorizontalSpeed(), "getHorizontalSpeed should return the default horizontal speed.");
    }

    @Test
    public void getVerticalSpeed_ShouldReturnDefaultVerticalSpeed() {
        Block[][] shape = createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE);
        Tetromino tetromino = new Tetromino(shape);
        assertEquals(tetromino.getBlockAt(0, 0).getHeight(), tetromino.getVerticalSpeed(), "getVerticalSpeed should return the default vertical speed.");
    }

    public void getBlockSize_ShouldReturnCorrectly() {
        Block[][] shape = createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE);
    }

    // ------------------------------------------------------------ Row and Column Retrieval ------------------------------------------------------------ //
    @Test
    public void getRow_ShouldReturnCorrectRow() {
        Block[][] shape = createValidShape(); // 3 rows, 2 columns
        Tetromino tetromino = new Tetromino(shape);
        Block[] row = tetromino.getRow(1);
        assertAll("getRow should return the correct row blocks",
                () -> assertEquals(shape[1][0], row[0], "Block at row 1, col 0 should match"),
                () -> assertEquals(shape[1][1], row[1], "Block at row 1, col 1 should match")
        );
    }

    @Test
    public void getColumn_ShouldReturnCorrectColumn() {
        Block[][] shape = createValidShape(3, 2); // 3 rows, 2 columns
        Tetromino tetromino = new Tetromino(shape);
        Block[] column = tetromino.getColumn(1);
        assertAll("getColumn should return the correct column blocks",
                () -> assertEquals(shape[0][1], column[0], "Block at row 0, col 1 should match"),
                () -> assertEquals(shape[1][1], column[1], "Block at row 1, col 1 should match"),
                () -> assertEquals(shape[2][1], column[2], "Block at row 2, col 1 should match")
        );
    }

    @Test
    public void getLeftColumn_ShouldReturnFirstColumn() {
        Block[][] shape = createValidShape(3, 2); // 3 rows, 2 columns
        Tetromino tetromino = new Tetromino(shape);
        Block[] column = tetromino.getLeftColumn();
        assertAll("getLeftColumn should return the first column",
                () -> assertEquals(shape[0][0], column[0], "First block in left column should match"),
                () -> assertEquals(shape[1][0], column[1], "Second block in left column should match"),
                () -> assertEquals(shape[2][0], column[2], "Third block in left column should match")
        );
    }

    @Test
    public void getRightColumn_ShouldReturnLastColumn() {
        Block[][] shape = createValidShape(3, 2); // 3 rows, 2 columns
        Tetromino tetromino = new Tetromino(shape);
        Block[] column = tetromino.getRightColumn();
        assertAll("getRightColumn should return the last column",
                () -> assertEquals(shape[0][1], column[0], "First block in right column should match"),
                () -> assertEquals(shape[1][1], column[1], "Second block in right column should match"),
                () -> assertEquals(shape[2][1], column[2], "Third block in right column should match")
        );
    }

    @Test
    public void getBottomRow_ShouldReturnLastRow() {
        Block[][] shape = createValidShape(3, 2); // 3 rows, 2 columns
        Tetromino tetromino = new Tetromino(shape);
        Block[] row = tetromino.getBottomRow();
        assertAll("getBottomRow should return the last row",
                () -> assertEquals(shape[2][0], row[0], "First block in bottom row should match"),
                () -> assertEquals(shape[2][1], row[1], "Second block in bottom row should match")
        );
    }

    @Test
    public void getRow_ShouldThrowExceptionWhenRowOutOfRange() {
        Tetromino tetromino = new Tetromino(createValidShape(2, 2, 10, 10));
        assertThrows(IllegalArgumentException.class, () -> tetromino.getRow(2), "getRow should throw IllegalArgumentException when row is out of range.");
    }

    @Test
    public void getColumn_ShouldThrowExceptionWhenColumnOutOfRange() {
        Tetromino tetromino = new Tetromino(createValidShape(2, 2, 10, 10));
        assertThrows(IllegalArgumentException.class, () -> tetromino.getColumn(2), "getColumn should throw IllegalArgumentException when column is out of range.");
    }

    // ------------------------------------------------------------ GetBlockAt functionality ------------------------------------------------------------ //
    @Test
    public void getBlockAt_ShouldReturnCorrectBlockWhenIndicesAreValid() {
        Block[][] shape = createValidShape(2, 3, 10, 10); // 2 rows, 3 columns
        Tetromino tetromino = new Tetromino(shape);

        // Testing valid accesses
        assertAll("Ensure getBlockAt returns the correct blocks for valid indices",
                () -> assertEquals(shape[0][0], tetromino.getBlockAt(0, 0), "Block at (0,0) should match the initially set block."),
                () -> assertEquals(shape[0][1], tetromino.getBlockAt(0, 1), "Block at (0,1) should match the initially set block."),
                () -> assertEquals(shape[0][2], tetromino.getBlockAt(0, 2), "Block at (0,2) should match the initially set block."),
                () -> assertEquals(shape[1][0], tetromino.getBlockAt(1, 0), "Block at (1,0) should match the initially set block."),
                () -> assertEquals(shape[1][1], tetromino.getBlockAt(1, 1), "Block at (1,1) should match the initially set block."),
                () -> assertEquals(shape[1][2], tetromino.getBlockAt(1, 2), "Block at (1,2) should match the initially set block.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideOutOfRangeIndices")
    public void getBlockAt_ShouldThrowIllegalArgumentExceptionWhenIndicesAreOutOfRange(int row, int col, String message) {
        Block[][] shape = createValidShape(2, 3, 10, 10); // 2 rows, 3 columns
        Tetromino tetromino = new Tetromino(shape);

        // Test index out of range
        assertThrows(IllegalArgumentException.class,
                () -> tetromino.getBlockAt(row, col),
                message);
    }

// Method to provide indices and messages for out-of-range tests
    static Stream<Arguments> provideOutOfRangeIndices() {
        return Stream.of(
                Arguments.of(-1, 1, "getBlockAt should throw IllegalArgumentException when row index is negative."),
                Arguments.of(2, 1, "getBlockAt should throw IllegalArgumentException when row index exceeds shape boundaries."),
                Arguments.of(1, -1, "getBlockAt should throw IllegalArgumentException when column index is negative."),
                Arguments.of(1, 3, "getBlockAt should throw IllegalArgumentException when column index exceeds shape boundaries."),
                Arguments.of(-1, -1, "getBlockAt should throw IllegalArgumentException when both row and column indices are negative."),
                Arguments.of(2, 3, "getBlockAt should throw IllegalArgumentException when both row and column indices exceed shape boundaries.")
        );
    }

    // ------------------------------------------------------------ Setters ------------------------------------------------------------ //
    @Test
    public void setHorizontalSpeed_ShouldCorrectlyUpdateHorizontalSpeed() {
        Block[][] shape = createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE);
        Tetromino tetromino = new Tetromino(shape);
        tetromino.setHorizontalSpeed(5);
        assertEquals(5, tetromino.getHorizontalSpeed(), "setHorizontalSpeed should correctly update the Tetromino's horizontal speed.");
    }

    @Test
    public void setVerticalSpeed_ShouldCorrectlyUpdateVerticalSpeed() {
        Block[][] shape = createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE);
        Tetromino tetromino = new Tetromino(shape);
        tetromino.setVerticalSpeed(5);
        assertEquals(5, tetromino.getVerticalSpeed(), "setVerticalSpeed should correctly update the Tetromino's vertical speed.");
    }

    @ParameterizedTest
    @MethodSource("provideColorsForTetromino")
    public void setColor_shouldSetAndThrowProperly(Color color, boolean shouldThrow) {
        Block[][] shape = createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE);
        Tetromino tetromino = new Tetromino(shape);
        if (shouldThrow) {
            assertThrows(IllegalArgumentException.class, () -> tetromino.setColor(color), "setColor should throw IllegalArgumentException for null color.");
        } else {
            tetromino.setColor(color);
            assertEquals(color, tetromino.getColor(), "setColor should correctly set the color of the Tetromino.");
        }
    }

    // Helper method to provide colors for tests
    private static Stream<Arguments> provideColorsForTetromino() {
        return Stream.of(
                Arguments.of(Color.RED, false),
                Arguments.of(Color.BLUE, false),
                Arguments.of(null, true) // Expecting an exception
        );
    }

    // ------------------------------------------------------------ Move functionality  ------------------------------------------------------------ //
    @ParameterizedTest
    @MethodSource("provideMoveDirectionsSpeedsAndChanges")
    public void move_shouldUpdateCoordinatesAccordingToDirectionAndSpeed(Tetromino.Direction direction, int hSpeed, int vSpeed, int expectedXChange, int expectedYChange) {
        Block[][] shape = createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE);
        Tetromino tetromino = new Tetromino(shape);
        tetromino.setHorizontalSpeed(hSpeed);  // Setting horizontal speed for this test iteration
        tetromino.setVerticalSpeed(vSpeed);    // Setting vertical speed for this test iteration
        int initialX = tetromino.getX();
        int initialY = tetromino.getY();

        tetromino.move(direction);

        assertAll(
                () -> assertEquals(initialX + expectedXChange, tetromino.getX(), "X coordinate should update correctly when moving " + direction),
                () -> assertEquals(initialY + expectedYChange, tetromino.getY(), "Y coordinate should update correctly when moving " + direction)
        );
    }

    static Stream<Arguments> provideMoveDirectionsSpeedsAndChanges() {
        return Stream.of(
                Arguments.of(Tetromino.Direction.LEFT, 1, 2, -1, 0),
                Arguments.of(Tetromino.Direction.RIGHT, 1, 2, 1, 0),
                Arguments.of(Tetromino.Direction.DOWN, 1, 2, 0, 2),
                // Adding more diverse speed values for comprehensive testing
                Arguments.of(Tetromino.Direction.LEFT, 3, 5, -3, 0),
                Arguments.of(Tetromino.Direction.RIGHT, 3, 5, 3, 0),
                Arguments.of(Tetromino.Direction.DOWN, 3, 5, 0, 5)
        );
    }

    // ------------------------------------------------------------ Rotate functionality  ------------------------------------------------------------ //
    @Test
    public void rotate_ShouldThrowIllegalArgumentExceptionWhenRotationIsNull() {
        Block[][] shape = createValidShape(2, 2, 20, 20); // Creating a 2x2 matrix for simplicity
        Tetromino tetromino = new Tetromino(shape);
        assertThrows(IllegalArgumentException.class, () -> tetromino.rotate(null),
                "rotate should throw IllegalArgumentException when rotation direction is null.");
    }

    @ParameterizedTest
    @EnumSource(Rotation.class)
    public void rotateShouldMaintainPositionalIntegrity(Rotation r) {
        // Create a specific shape (e.g., 2x2 for simplicity)
        Block[][] shape = new Block[][]{
            {new Block(0, 0, 10, 10), new Block(10, 0, 10, 10)},
            {new Block(0, 10, 10, 10), new Block(10, 10, 10, 10)}
        };
        Tetromino tetromino = new Tetromino(shape);

        // Get initial positions
        Point[][] initialPositions = new Point[shape.length][shape[0].length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                initialPositions[i][j] = new Point(shape[i][j].getX(), shape[i][j].getY());
            }
        }

        // Rotate the shape
        tetromino.rotate(r);

        // Check that positions have not changed
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                Block blockAfterRotation = tetromino.getBlockAt(i, j);
                Point currentPosition = new Point(blockAfterRotation.getX(), blockAfterRotation.getY());
                assertEquals(initialPositions[i][j], currentPosition, "Block positions should remain consistent after rotation.");
            }
        }
    }

    @Test
    public void rotateClockwise_ShouldCorrectlyRotateShape() {
        // Create a 2x3 shape
        Block[][] originalShape = createValidShape();
        Tetromino tetromino = new Tetromino(originalShape);

        // Expected positions after one clockwise rotation (3x2 shape)
        Block[][] expectedShape = new Block[3][2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                expectedShape[i][j] = originalShape[2 - 1 - j][i]; // Calculating expected rotated positions
            }
        }

        tetromino.rotate(Tetromino.Rotation.CLOCKWISE);

        // Validate the rotated shape
        assertAll("Check each block matches the expected rotated position",
                () -> assertEquals(expectedShape[0][0], tetromino.getBlockAt(0, 0)),
                () -> assertEquals(expectedShape[1][0], tetromino.getBlockAt(1, 0)),
                () -> assertEquals(expectedShape[2][0], tetromino.getBlockAt(2, 0)),
                () -> assertEquals(expectedShape[0][1], tetromino.getBlockAt(0, 1)),
                () -> assertEquals(expectedShape[1][1], tetromino.getBlockAt(1, 1)),
                () -> assertEquals(expectedShape[2][1], tetromino.getBlockAt(2, 1))
        );
    }

    @Test
    public void rotateCounterClockwise_ShouldCorrectlyRotateShape() {
        // Create a 2x3 shape
        Block[][] originalShape = createValidShape(2, 3, 10, 10);
        Tetromino tetromino = new Tetromino(originalShape);

        // Expected positions after one counter-clockwise rotation (3x2 shape)
        Block[][] expectedShape = new Block[3][2];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                expectedShape[i][j] = originalShape[j][3 - 1 - i]; // Calculating expected rotated positions
            }
        }

        tetromino.rotate(Tetromino.Rotation.COUNTER_CLOCKWISE);

        // Validate the rotated shape
        assertAll("Check each block matches the expected rotated position",
                () -> assertEquals(expectedShape[0][0], tetromino.getBlockAt(0, 0)),
                () -> assertEquals(expectedShape[1][0], tetromino.getBlockAt(1, 0)),
                () -> assertEquals(expectedShape[2][0], tetromino.getBlockAt(2, 0)),
                () -> assertEquals(expectedShape[0][1], tetromino.getBlockAt(0, 1)),
                () -> assertEquals(expectedShape[1][1], tetromino.getBlockAt(1, 1)),
                () -> assertEquals(expectedShape[2][1], tetromino.getBlockAt(2, 1))
        );
    }

    @ParameterizedTest
    @EnumSource(Rotation.class)
    public void rotateMultipleTimes_ShouldReturnToOriginalOrientation(Rotation d) {
        Block[][] originalShape = createValidShape();
        Tetromino tetromino = new Tetromino(originalShape);

        // Rotate 4 times clockwise
        for (int i = 0; i < 4; i++) {
            tetromino.rotate(d);
        }

        // Check that the final shape matches the original shape
        assertAll("Shape should return to original after four rotations",
                () -> assertEquals(originalShape[0][0], tetromino.getBlockAt(0, 0)),
                () -> assertEquals(originalShape[0][1], tetromino.getBlockAt(0, 1)),
                () -> assertEquals(originalShape[0][2], tetromino.getBlockAt(0, 2)),
                () -> assertEquals(originalShape[1][0], tetromino.getBlockAt(1, 0)),
                () -> assertEquals(originalShape[1][1], tetromino.getBlockAt(1, 1)),
                () -> assertEquals(originalShape[1][2], tetromino.getBlockAt(1, 2))
        );
    }

    // -------------------------------------------------------------- Universal Helpers -------------------------------------------------------------- //
    // Helper method to create a valid shape with specified dimensions and block size
    private Block[][] createValidShape(int rows, int cols, int width, int height) {
        Block[][] shape = new Block[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                shape[i][j] = new Block(0, 0, width, height, Color.BLUE); // Arbitrary initial positions and color
            }
        }
        return shape;
    }

    private Block[][] createValidShape(int rows, int cols) {
        Block[][] shape = new Block[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                shape[i][j] = new Block(0, 0, BLOCK_SIZE, BLOCK_SIZE, Color.BLUE); // Arbitrary initial positions and color
            }
        }
        return shape;
    }

    private Block[][] createValidShape() {
        return createValidShape(2, 3, BLOCK_SIZE, BLOCK_SIZE);
    }
}
