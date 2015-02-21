import java.util.Arrays;

public class Brute {

  private Brute() {
    // no instances
  }

  public static void main(final String[] args) {
    if (args.length < 1) {
      throw new IllegalArgumentException("Not enough command-line arguments");
    }

    final String filename = args[0];
    final In input = new In(filename);

    final int size = input.readInt();

    if (size <= 0) {
      return;
    }

    final Point[] points = new Point[size];

    for (int i = 0; i < size; i++) {
      points[i] = new Point(input.readInt(), input.readInt());
      // StdOut.println(points[i]);
    }

    for (int i = 0; i < size - 3; i++) {
      for (int j = i + 1; j < size - 2; j++) {
        for (int k = j + 1; k < size - 1; k++) {
          for (int m = k + 1; m < size; m++) {
            if (isCollinear(points, i, j, k, m)) {
              print(points, i, j, k, m);
            }
          }
        }
      }
    }
  }

  private static void print(final Point[] points, final int i, final int j, final int k, final int m) {
    final Point p = points[i];
    final Point q = points[j];
    final Point r = points[k];
    final Point s = points[m];

    final Point[] collinearPoints = new Point[] {p, q, r, s};
    Arrays.sort(collinearPoints, p.SLOPE_ORDER);

    for (int n = 0; n < collinearPoints.length; n++) {
      StdOut.print(collinearPoints[n]);
      if (n < collinearPoints.length - 1) {
        StdOut.print(" -> ");
      } else {
        StdOut.print("\n");
      }
    }
  }

  private static boolean isCollinear(final Point[] points, final int i, final int j, final int k,
      final int m) {
    final Point p = points[i];
    final Point q = points[j];
    final Point r = points[k];
    final Point s = points[m];

    final boolean result =
        Double.compare(p.slopeTo(q), p.slopeTo(r)) == 0
            && Double.compare(p.slopeTo(r), p.slopeTo(s)) == 0;

    return result;
  }

}
