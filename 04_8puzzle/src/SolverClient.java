public class SolverClient {

  public static void main(String[] args) {

    // create initial board from file
    final In in = new In(args[0]);
    final int N = in.readInt();
    final int[][] blocks = new int[N][N];

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        blocks[i][j] = in.readInt();
      }
    }

    final Board initial = new Board(blocks);

    // solve the puzzle
    final Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution())
        StdOut.println(board);
    }
  }

}
