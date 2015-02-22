import java.util.Arrays;

/**
 * Requirements:<br>
 * =============
 * <p>
 * Brute force. Write a program Brute.java that examines 4 points at a time and checks whether they
 * all lie on the same line segment, printing out any such line segments to standard output and
 * drawing them using standard drawing. To check whether the 4 points p, q, r, and s are collinear,
 * check whether the slopes between p and q, between p and r, and between p and s are all equal.
 * <p>
 * The order of growth of the running time of your program should be N4 in the worst case and it
 * should use space proportional to N.
 * <p>
 * Output format: Print to standard output the line segments that your program discovers, one per
 * line. Print each line segment as an ordered sequence of its constituent points, separated by
 * " -> ".
 */
public class Brute {

  private static final double PEN_RADIUS_FOR_LINES = 0.002;
  private static final double PEN_RADIUS_FOR_POINTS = 0.007;

  private Brute() {
    // no instances
  }

  public static void main(final String[] args) {
    if (args.length < 1) {
      throw new IllegalArgumentException("Not enough command-line arguments");
    }

    final String filename = args[0];
    final In input = new In(filename);

    try {
      executeCollinearPointDetection(input);
    } finally {
      input.close();
    }
  }

  private static void executeCollinearPointDetection(final In input) {
    final int size = input.readInt();

    if (size <= 0) {
      throw new IllegalArgumentException("Number of points must be positive");
    }

    final Point[] points = initPoints(input, size);
    detectCollinearPoints(points);
  }

  private static Point[] initPoints(final In input, final int size) {
    final Point[] points = new Point[size];

    setupDrawing();

    for (int i = 0; i < size; i++) {
      points[i] = new Point(input.readInt(), input.readInt());
      drawPoint(points[i]);
    }

    Arrays.sort(points, points[0].SLOPE_ORDER);
    return points;
  }

  // uses brute force to check all 4-tuple
  private static void detectCollinearPoints(final Point[] points) {
    final int size = points.length;

    // check collinear points for all (size choose 4) tuples
    final Point[] tuple = new Point[4];

    for (int i = 0; i < size - 3; i++) {
      for (int j = i + 1; j < size - 2; j++) {
        for (int k = j + 1; k < size - 1; k++) {
          for (int m = k + 1; m < size; m++) {
            resetTuple(tuple, points, i, j, k, m);

            if (isCollinear(tuple)) {
              print(tuple);
            }
          }
        }
      }
    }
  }

  // looks weird... varargs could be used, but we want to spare array this time
  private static void resetTuple(final Point[] tuple, final Point[] points, int i, int j, int k,
      int m) {
    tuple[0] = points[i];
    tuple[1] = points[j];
    tuple[2] = points[k];
    tuple[3] = points[m];

    Arrays.sort(tuple, tuple[0].SLOPE_ORDER);
  }

  private static boolean isCollinear(final Point[] tuple) {
    final Point p = tuple[0];
    final Point q = tuple[1];
    final Point r = tuple[2];
    final Point s = tuple[3];

    return Double.compare(p.slopeTo(q), p.slopeTo(r)) == 0
        && Double.compare(p.slopeTo(r), p.slopeTo(s)) == 0;
  }

  private static void print(final Point[] tuple) {
    final int size = tuple.length;

    for (int n = 0; n < size; n++) {
      StdOut.print(tuple[n]);

      if (n < size - 1) {
        StdOut.print(" -> ");
      } else {
        StdOut.print("\n");
      }
    }

    drawLine(tuple[0], tuple[size - 1]);
  }

  private static void setupDrawing() {
    StdDraw.setXscale(0, Short.MAX_VALUE + 1);
    StdDraw.setYscale(0, Short.MAX_VALUE + 1);
  }

  private static void drawPoint(final Point point) {
    if (StdDraw.getPenRadius() != PEN_RADIUS_FOR_POINTS) {
      StdDraw.setPenRadius(PEN_RADIUS_FOR_POINTS);
    }

    point.draw();
  }

  private static void drawLine(final Point pointFrom, final Point pointTo) {
    if (StdDraw.getPenRadius() != PEN_RADIUS_FOR_LINES) {
      StdDraw.setPenRadius(PEN_RADIUS_FOR_LINES);
    }

    pointFrom.drawTo(pointTo);
  }

}
