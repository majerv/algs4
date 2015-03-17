public class Board {

  /**
   * Constructs a board from an N-by-N array of blocks (where blocks[i][j] = block in row i, column
   * j)
   * 
   * @param blocks
   */
  public Board(int[][] blocks) {

  }

  // board dimension N
  public int dimension() {
    return 0;
  }

  // number of blocks out of place
  public int hamming() {
    return 0;
  }

  // sum of Manhattan distances between blocks and goal
  public int manhattan() {
    return 0;
  }

  // is this board the goal board?
  public boolean isGoal() {
    return false;
  }

  // a boadr that is obtained by exchanging two adjacent blocks in the same row
  public Board twin() {
    return null;
  }

  // does this board equal y?
  @Override
  public boolean equals(Object y) {
    return false;
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {
    return null;
  }

  // string representation of this board (in the output format specified below)
  @Override
  public String toString() {
    return null;
  }

}
