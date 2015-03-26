import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

  private static final int EMPTY_ITEM = 0;

  private final int dimension;
  private final int[][] blocks;

  private final int rowOfEmpty;
  private final int columnOfEmpty;

  /**
   * Constructs a board from an N-by-N array of blocks (where blocks[i][j] = block in row i, column
   * j)
   * 
   * @param blocks
   */
  public Board(int[][] blocks) {
    final int width = blocks.length;
    final int count = width * width;

    if (width < 2) {
      throw new IllegalArgumentException("The size of the puzzle is too low.");
    }

    if (count > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("The size of the puzzle is too large.");
    }

    this.dimension = width;
    this.blocks = copyOf(blocks);

    // set the indices of empty position
    final int position = findElement(EMPTY_ITEM);
    rowOfEmpty = position / dimension;
    columnOfEmpty = position % dimension;
  }

  // board dimension N
  public int dimension() {
    return dimension;
  }

  // number of blocks out of place
  public int hamming() {
    int wrongItems = 0;

    int position;
    int value;

    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        position = i * dimension + j + 1;
        value = blocks[i][j];
        if (value != EMPTY_ITEM && value != position) {
          ++wrongItems;
        }
      }
    }

    return wrongItems;
  }

  // sum of Manhattan distances between blocks and goal
  public int manhattan() {
    int distances = 0;

    int goalRow;
    int goalColumn;
    int value;
    int position;

    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        value = blocks[i][j];
        position = i * dimension + j + 1;
        if (value != EMPTY_ITEM && value != position) {
          goalRow = (value - 1) / dimension;
          goalColumn = (value - 1) % dimension;

          distances += Math.abs(i - goalRow) + Math.abs(j - goalColumn);
        }
      }
    }

    return distances;
  }

  // is this board the goal board?
  public boolean isGoal() {
    return 0 == manhattan();
  }

  // a board that is obtained by exchanging two adjacent blocks in the same row
  public Board twin() {
    final int[][] copiedBlocks = copyOf(blocks);

    changeTwoBlocks(copiedBlocks);

    // return twin
    return new Board(copiedBlocks);
  }

  // does this board equal y?
  @Override
  public boolean equals(Object y) {
    if (this == y) {
      return true;
    }

    if (!(y instanceof Board)) {
      return false;
    }

    final Board other = (Board) y;
    if (dimension != other.dimension) {
      return false;
    }

    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        if (blocks[i][j] != other.blocks[i][j]) {
          return false;
        }
      }
    }

    return true;
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {
    final List<Board> neighbors = new ArrayList<>();

    final int[][] copiedBlocks = copyOf(blocks);

    // create neighbor on the left
    if (columnOfEmpty > 0) {
      changeBlocks(copiedBlocks, rowOfEmpty, columnOfEmpty, rowOfEmpty, columnOfEmpty - 1);
      neighbors.add(new Board(copiedBlocks));
      changeBlocks(copiedBlocks, rowOfEmpty, columnOfEmpty, rowOfEmpty, columnOfEmpty - 1);
    }

    // create neighbor on the right
    if (columnOfEmpty < dimension - 1) {
      changeBlocks(copiedBlocks, rowOfEmpty, columnOfEmpty, rowOfEmpty, columnOfEmpty + 1);
      neighbors.add(new Board(copiedBlocks));
      changeBlocks(copiedBlocks, rowOfEmpty, columnOfEmpty, rowOfEmpty, columnOfEmpty + 1);
    }

    // create neighbor above
    if (rowOfEmpty > 0) {
      changeBlocks(copiedBlocks, rowOfEmpty, columnOfEmpty, rowOfEmpty - 1, columnOfEmpty);
      neighbors.add(new Board(copiedBlocks));
      changeBlocks(copiedBlocks, rowOfEmpty, columnOfEmpty, rowOfEmpty - 1, columnOfEmpty);
    }

    // create neighbor below
    if (rowOfEmpty < dimension - 1) {
      changeBlocks(copiedBlocks, rowOfEmpty, columnOfEmpty, rowOfEmpty + 1, columnOfEmpty);
      neighbors.add(new Board(copiedBlocks));
      changeBlocks(copiedBlocks, rowOfEmpty, columnOfEmpty, rowOfEmpty + 1, columnOfEmpty);
    }

    return neighbors;
  }

  // string representation of this board (in the output format specified below)
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(dimension + "\n");
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        s.append(String.format("%2d ", blocks[i][j]));
      }
      s.append("\n");
    }
    return s.toString();
  }

  // changes two non-empty blocks
  private void changeTwoBlocks(final int[][] copiedBlocks) {
    int row = 0;
    // if empty block is in the first row, then make the change in the 2nd
    if (copiedBlocks[0][0] == 0 || copiedBlocks[0][1] == 0) {
      row = 1;
    }

    int tmp;
    tmp = copiedBlocks[row][0];
    copiedBlocks[row][0] = copiedBlocks[row][1];
    copiedBlocks[row][1] = tmp;
  }

  private int[][] copyOf(int[][] blocks) {
    return Arrays.copyOf(blocks, blocks.length);
  }

  private void changeBlocks(int[][] copiedBlocks, int row, int column, int row2, int column2) {
    int tmp = copiedBlocks[row][column];
    copiedBlocks[row][column] = copiedBlocks[row2][column2];
    copiedBlocks[row2][column2] = tmp;
  }

  private int findElement(int valueToFind) {
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        if (blocks[i][j] == valueToFind) {
          return i * dimension + j;
        }
      }
    }

    throw new IllegalArgumentException("No such element on the board: " + valueToFind);
  }

}
