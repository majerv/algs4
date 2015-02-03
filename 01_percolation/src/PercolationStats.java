public class PercolationStats {

  private final double[] percolationThresholds;
  private final int experimentCount;

  /**
   * Performs T independent experiments on an N-by-N grid.
   * 
   * @param dimension
   * @param experimentCount
   */
  public PercolationStats(int dimension, int experimentCount) {
    checkArgument(dimension > 0, "Dimension of the grid must be positive");
    checkArgument(experimentCount > 0, "Number of experiments must be positive");

    this.experimentCount = experimentCount;
    percolationThresholds = new double[experimentCount];

    for (int i = 0; i < experimentCount; i++) {
      percolationThresholds[i] = executeMonteCarloSimulation(dimension);
    }
  }

  /**
   * Sample mean of percolation threshold.
   * 
   * @return
   */
  public double mean() {
    return StdStats.mean(percolationThresholds);
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(percolationThresholds);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - 1.96 * stddev() / Math.sqrt(experimentCount);
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + 1.96 * stddev() / Math.sqrt(experimentCount);
  }

  private void checkArgument(final boolean condition, final String message) {
    if (!condition) {
      throw new IllegalArgumentException(message);
    }
  }

  // test client (described below)
  public static void main(String[] args) {
    // final Stopwatch watch = new Stopwatch();

    if (args.length != 2) {
      throw new IllegalArgumentException("Wrong number of command line arguments");
    }

    final int N = Integer.parseInt(args[0]);
    final int T = Integer.parseInt(args[1]);

    final PercolationStats percolationStats = new PercolationStats(N, T);
    StdOut.printf("%-23s = %f\n", "mean", percolationStats.mean());
    StdOut.printf("%-23s = %f\n", "stddev", percolationStats.stddev());
    StdOut.printf("95%% confidence interval = %f, %f\n", percolationStats.confidenceLo(),
        percolationStats.confidenceHi());

    // StdOut.printf("\nElapsed time: %f sec", watch.elapsedTime());
  }

  private double executeMonteCarloSimulation(final int dimension) {
    final Percolation percolation = new Percolation(dimension);
    int openSites = 0;

    while (openSites < dimension || !percolation.percolates()) {
      // choose a site (row i, column j) uniformly at random among all blocked sites

      int randomI = 1;
      int randomJ = 1;
      boolean blockedSitefound = false;

      while (!blockedSitefound) {
        randomI = StdRandom.uniform(dimension) + 1;
        randomJ = StdRandom.uniform(dimension) + 1;

        blockedSitefound = !percolation.isOpen(randomI, randomJ);
      }

      // open the site (row i, column j)
      percolation.open(randomI, randomJ);
      ++openSites;
    }

    return (double) openSites / (dimension * dimension);
  }

}
