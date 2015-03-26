import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {

  private static class SearchNode implements Comparable<SearchNode> {
    private final Board board;
    private final SearchNode previousNode;
    private final int moves;
    private final int priority;

    public SearchNode(final Board board, final int moves, final SearchNode previousNode) {
      this.board = board;
      this.previousNode = previousNode;
      this.moves = moves;
      this.priority = moves + board.manhattan();
    }

    @Override
    public int compareTo(final SearchNode other) {
      return priority - other.priority;
    }
  }

  private boolean solvable;
  private final List<Board> solution;

  // find a solution to the initial board (using the A* algorithm)
  public Solver(final Board initial) {
    solution = new ArrayList<>();

    final SearchNode initialNode = new SearchNode(initial, 0, null);
    final SearchNode initialTwinNode = new SearchNode(initial.twin(), 0, null);

    solve(initialNode, initialTwinNode);
  }

  // is the initial board solvable?
  public boolean isSolvable() {
    return solvable;
  }

  // minimum number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    if (solvable) {
      return solution.size() - 1;
    } else {
      return -1;
    }
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    if (solvable) {
      return solution;
    } else {
      return null;
    }
  }

  // solve a slider puzzle
  public static void main(final String[] args) {
    // create initial board from file
    final In in = new In(args[0]);

    final int N = in.readInt();
    int[][] blocks = new int[N][N];

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

  private void solve(final SearchNode initialNode, final SearchNode initialTwinNode) {
    final MinPQ<SearchNode> nodes = new MinPQ<>();
    nodes.insert(initialNode);

    final MinPQ<SearchNode> twinNodes = new MinPQ<>();
    twinNodes.insert(initialTwinNode);

    SearchNode currentNode = initialNode;
    SearchNode currentTwinNode = initialTwinNode;

    while (!currentNode.board.isGoal() && !currentTwinNode.board.isGoal()) {
      currentNode = nodes.delMin();
      currentTwinNode = twinNodes.delMin();

      nextMove(nodes, currentNode);
      nextMove(twinNodes, currentTwinNode);
    }

    solvable = currentNode.board.isGoal();

    if (solvable) {
      updateSolution(currentNode);
    }
  }

  private void nextMove(final MinPQ<SearchNode> nodes, final SearchNode currentNode) {
    final Iterable<Board> neighbors = currentNode.board.neighbors();
    for (Board neighbor : neighbors) {
      if (currentNode.previousNode == null || !neighbor.equals(currentNode.previousNode.board)) {
        nodes.insert(new SearchNode(neighbor, currentNode.moves + 1, currentNode));
      }
    }
  }

  private void updateSolution(final SearchNode goalNode) {
    SearchNode currentNode = goalNode;
    while (currentNode != null) {
      solution.add(currentNode.board);
      currentNode = currentNode.previousNode;
    }
    Collections.reverse(solution);
  }

}
