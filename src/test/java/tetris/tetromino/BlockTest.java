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
import java.util.stream.Stream;
import static tetris.GameConstants.DEFAULT_BLOCK_COLOR;

/**
 * Unit Test for the Block class
 *
 * @author Kheagen
 */
public class BlockTest {

    // ------------------------------ Set-Up ------------------------------- //
    private static int validX = 10;
    private static int validY = 10;
    private static int validWidth = 10;
    private static int validHeight = 10;
    private Block block;

    @BeforeEach
    public void initialiseBlock() {
        block = new Block(validX, validY, validWidth, validHeight, Color.RED);
    }

    // --------------------------- Constructors ----------------------------- //
    @ParameterizedTest
    @MethodSource("provideValidBlockParameters")
    public void constructor_shouldInitialiseWithGivenValues(int x, int y, int width, int height, Color c) {

        Block testBlock = new Block(x, y, width, height, c);

        assertAll("Constructor should correctly initialise all fields",
                () -> assertEquals(x, testBlock.getX()),
                () -> assertEquals(y, testBlock.getY()),
                () -> assertEquals(width, testBlock.getWidth()),
                () -> assertEquals(height, testBlock.getHeight()),
                () -> assertEquals(c, testBlock.getColor())
        );
    }

    private static Stream<Arguments> provideValidBlockParameters() {
        return Stream.of(
                Arguments.of(1, 1, 1, 1, Color.RED),
                Arguments.of(10, 10, 10, 10, Color.GREEN),
                Arguments.of(0, 0, 0, 0, new Color(0xFF00FF)),
                Arguments.of(100, 200, 300, 400, Color.WHITE),
                Arguments.of(validX, validY, validWidth, validHeight, Color.BLACK),
                Arguments.of(
                        Integer.MAX_VALUE, Integer.MAX_VALUE,
                        Integer.MAX_VALUE, Integer.MAX_VALUE, Color.BLACK
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidBlockParameters")
    public void constructor_shouldThrowExceptionForInvalidArguments(int x, int y, int width, int height) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Block(x, y, width, height),
                "Constructor should throw an exception if any dimension or coordinate is negative.");
    }

    private static Stream<Arguments> provideInvalidBlockParameters() {
        return Stream.of(
                Arguments.of(-1, 20, 30, 40),
                Arguments.of(10, -20, 30, 40),
                Arguments.of(10, 20, -30, 40),
                Arguments.of(10, 20, 30, -40),
                Arguments.of(-1, -20, 30, 40),
                Arguments.of(10, -20, -30, 40),
                Arguments.of(-1, 20, -30, -40),
                Arguments.of(-1, -20, -30, -40)
        );
    }

    @Test
    public void constructor_shouldThrowExceptionForNullColor() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Block(10, 20, 30, 40, null)
        );
    }

    @Test
    public void constructor_withoutColor_shouldUseDefaultColor() {
        Block defaultColorBlock = new Block(10, 20, 30, 40);
        assertEquals(DEFAULT_BLOCK_COLOR,
                defaultColorBlock.getColor(),
                "Block should use the default color when none is specified."
        );
    }

    // ------------------------------ Getters ------------------------------- //
    @Test
    public void getX_shouldReturnCorrectXCoordinate() {
        assertEquals(validX, block.getX());
    }

    @Test
    public void getY_shouldReturnCorrectYCoordinate() {
        assertEquals(validY, block.getY());
    }

    @Test
    public void getWidth_shouldReturnCorrectWidth() {
        assertEquals(validWidth, block.getWidth());
    }

    @Test
    public void getHeight_shouldReturnCorrectHeight() {
        assertEquals(validHeight, block.getHeight());
    }

    @Test
    public void getBlockColor_shouldReturnCorrectColor() {
        assertEquals(Color.RED, block.getColor());
    }

    // ------------------------------ Setters ------------------------------- //
    @ParameterizedTest
    @MethodSource("provideValidDimensions")
    public void setProperties_shouldSetAndCheckPropertiesCorrectly(int x, int y, int width, int height) {
        assertAll("Testing setting and getting properties",
                () -> {
                    block.setX(x);
                    assertEquals(x, block.getX(), "setX should set x correctly when positive.");
                },
                () -> {
                    block.setY(y);
                    assertEquals(y, block.getY(), "setY should set y correctly when positive.");
                },
                () -> {
                    block.setWidth(width);
                    assertEquals(width, block.getWidth(), "setWidth should set width correctly when positive.");
                },
                () -> {
                    block.setHeight(height);
                    assertEquals(height, block.getHeight(), "setHeight should set height correctly when positive.");
                }
        );
    }

    private static Stream<Arguments> provideValidDimensions() {
        return Stream.of(
                Arguments.of(validX, validY, validWidth, validHeight),
                Arguments.of(1, 1, 1, 1),
                Arguments.of(100, 200, 300, 400),
                Arguments.of(-100, -200, 300, 400), // negative position
                Arguments.of(0, 0, 0, 0) // Assuming 0 is a valid value for these dimensions
        );
    }

    @Test
    public void setWidth_shouldThrowExceptionWhenNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> block.setWidth(-1)
        );
    }

    @Test
    public void setHeight_shouldThrowExceptionWhenNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> block.setHeight(-1)
        );
    }

    @Test
    public void setX_andGetY_shouldBeIndependent() {
        block.setX(100);
        block.setY(200);
        assertAll(
                () -> assertEquals(100, block.getX(), "X should be updated to 100."),
                () -> assertEquals(200, block.getY(), "Y should remain 200 after setting X.")
        );
    }

}
