/**
 * Represents a percolation system.
 */
public class Percolation {

  private final int dimension;
  private final int sitesCount;

  private final boolean[] grid;

  // used to store the connections among nodes, plus a virtual-top node
  private final WeightedQuickUnionUF unionFindWithTopVirtualNode;

  // used to store the connections among nodes, plus virtual-top & virtual-bottom nodes
  private final WeightedQuickUnionUF unionFindWithTopBottomVirtualNodes;

  /**
   * Creates an N-by-N grid, with all sites blocked (N = dimension)
   * 
   * @param dimension the dimension of the grid
   */
  public Percolation(final int dimension) {
    if (dimension <= 0) {
      throw new IllegalArgumentException("Dimension of the grid must be positive");
    }

    this.dimension = dimension;
    this.sitesCount = dimension * dimension + 2; // add two virtual nodes

    this.grid = new boolean[sitesCount];
    this.unionFindWithTopVirtualNode = new WeightedQuickUnionUF(sitesCount - 1);
    this.unionFindWithTopBottomVirtualNodes = new WeightedQuickUnionUF(sitesCount);

    // union first row with virtual-top node
    for (int j = 0; j < dimension; j++) {
      union(getVirtualStartPoint(), j + 1);
    }

    // union last row with virtual-bottom node, only in reference UF
    for (int j = 0; j < dimension; j++) {
      unionFindWithTopBottomVirtualNodes.union(getVirtualEndPoint(), getSideId(dimension, j + 1));
    }
  }

  /**
   * Opens site (row i, column j) if it is not open already
   * 
   * @param row
   * @param column
   */
  public void open(final int row, int column) {
    checkIndices(row, column);

    // open the site
    final int sideId = getSideId(row, column);
    grid[sideId] = true;

    // connect it with its neighbours
    // left
    unionIfPossible(sideId, row, column - 1);

    // right
    unionIfPossible(sideId, row, column + 1);

    // top
    unionIfPossible(sideId, row - 1, column);

    // bottom
    unionIfPossible(sideId, row + 1, column);
  }

  /**
   * Checks whether the site (row i, column j) is open?
   * 
   * @param row
   * @param column
   * @return true, iff the site is already open
   */
  public boolean isOpen(final int row, final int column) {
    checkIndices(row, column);
    return grid[getSideId(row, column)];
  }

  /**
   * Determines if site(row, column) full or not.
   * 
   * @param row
   * @param column
   * @return true, iff the site is full (connected to the virtual-top node)
   */
  public boolean isFull(final int row, final int column) {
    checkIndices(row, column);

    // check if site is open and is connected to virtual-top node
    return isOpen(row, column)
        && unionFindWithTopVirtualNode.connected(getVirtualStartPoint(), getSideId(row, column));
  }

  /**
   * Indicates whether the system percolates.
   * 
   * @return true, iff the system percolates
   */
  public boolean percolates() {
    // edge case: 1-by-1 grid
    if (dimension == 1) {
      return isOpen(1, 1);
    }

    // check if the two virtual nodes are connected
    return unionFindWithTopBottomVirtualNodes.connected(getVirtualStartPoint(),
        getVirtualEndPoint());
  }

  private void checkIndices(final int row, final int column) {
    checkIndex(row);
    checkIndex(column);
  }

  private void checkIndex(final int index) {
    if (!isValidIndex(index)) {
      throw new IndexOutOfBoundsException(
          String.format("Index must be between 1 and %s", dimension));
    }
  }

  private boolean isValidIndex(final int index) {
    // condition 1 <= index <= dimension must hold
    return index >= 1 && index <= dimension;
  }

  private int getSideId(final int row, final int column) {
    return (row - 1) * dimension + column;
  }

  private int getVirtualStartPoint() {
    return 0;
  }

  private int getVirtualEndPoint() {
    return sitesCount - 1;
  }

  private void union(final int siteId, final int site2Id) {
    unionFindWithTopVirtualNode.union(siteId, site2Id);
    unionFindWithTopBottomVirtualNodes.union(siteId, site2Id);
  }

  private void unionIfPossible(final int sideId, final int row, final int column) {
    if (isValidIndex(row) && isValidIndex(column) && isOpen(row, column)) {
      union(sideId, getSideId(row, column));
    }
  }

}
