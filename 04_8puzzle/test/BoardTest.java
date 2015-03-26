import static com.google.common.truth.Truth.ASSERT;
import static junitparams.JUnitParamsRunner.$;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit test for {@link Board}.
 */
@RunWith(JUnitParamsRunner.class)
public class BoardTest {

  // unit under test
  private Board board;

  @Test
  @Parameters(method = "provideBoardItems")
  public void boardCreation(final int[][] blocks) {
    // given
    board = createFrom(blocks);

    // then
    System.out.println("Board: " + board.toString());
  }

  @Test
  @Parameters(method = "provideBoardItems")
  public void twinCreation(final int[][] blocks) {
    // given
    board = createFrom(blocks);

    // then
    System.out.println("Board: " + board);
    System.out.println("Twin: " + board.twin());
  }

  @Test
  @Parameters(method = "provideBoardItemsForHammingTest")
  public void hammingHeuristic(final int[][] blocks, final int expectedHamming) {
    // given
    board = createFrom(blocks);

    // then
    ASSERT.that(board.hamming()).isEqualTo(expectedHamming);
  }

  @Test
  @Parameters(method = "provideBoardItemsForManhattanTest")
  public void manhattanHeuristic(final int[][] blocks, final int expectedManhattan) {
    // given
    board = createFrom(blocks);

    // then
    ASSERT.that(board.manhattan()).isEqualTo(expectedManhattan);
  }

  @Test
  @Parameters(method = "provideBoardItems")
  public void neighborCalculation(final int[][] blocks) {
    // given
    board = createFrom(blocks);

    final Iterable<Board> neighbors = board.neighbors();

    // then
    System.out.println("Original: " + board);
    System.out.println("Neighbors: " + neighbors);
  }

  private static Board createFrom(final int[][] blocks) {
    return new Board(blocks);
  }

  public static Object[] provideBoardItems() {
    return $(//
        new int[][] { {1, 2}, {3, 0}}, //
        new int[][] { {1, 2}, {0, 3}},//
        new int[][] { {4, 1, 3}, {0, 2, 6}, {7, 5, 8}}//
    );
  }

  public static Object[] provideBoardItemsForHammingTest() {
    return $(//
        $(new int[][] { {1, 2}, {3, 0}}, 0), //
        $(new int[][] { {1, 2}, {0, 3}}, 1),//
        $(new int[][] { {4, 1, 3}, {0, 2, 6}, {7, 5, 8}}, 5),//
        $(new int[][] { {2, 0, 3, 4}, {1, 10, 6, 8}, {5, 9, 7, 12}, {13, 14, 11, 15}}, 9),//
        $(new int[][] { {2, 9, 3, 5}, {8, 11, 12, 7}, {15, 4, 0, 13}, {6, 1, 10, 14}}, 14)//

    );
  }

  public static Object[] provideBoardItemsForManhattanTest() {
    return $(//
        $(new int[][] { {1, 2}, {3, 0}}, 0), //
        $(new int[][] { {1, 2}, {0, 3}}, 1),//
        $(new int[][] { {4, 1, 3}, {0, 2, 6}, {7, 5, 8}}, 5),//
        $(new int[][] { {2, 0, 3, 4}, {1, 10, 6, 8}, {5, 9, 7, 12}, {13, 14, 11, 15}}, 9),//
        $(new int[][] { {2, 9, 3, 5}, {8, 11, 12, 7}, {15, 4, 0, 13}, {6, 1, 10, 14}}, 38)//
    );
  }

}
