import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Fast {

  private static final double PEN_RADIUS_FOR_LINES = 0.002;
  private static final double PEN_RADIUS_FOR_POINTS = 0.007;

  private Fast() {
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
    if (3 < points.length) {
      detectCollinearPoints(points);
    }
  }

  private static Point[] initPoints(final In input, final int size) {
    final Point[] points = new Point[size];

    setupDrawing();

    for (int i = 0; i < size; i++) {
      points[i] = new Point(input.readInt(), input.readInt());
      drawPoint(points[i]);
    }

    return points;
  }

  private static void detectCollinearPoints(final Point[] points) {
    final int size = points.length;

    final Point[] referencePoints = Arrays.copyOf(points, size);
    final Map<Point, Set<Double>> discoveredSegments = new HashMap<>();
    final Set<Point> collinearPoints = new HashSet<>();

    for (int i = 0; i < referencePoints.length; i++) {
      // if (!discoveredPoints.contains(referencePoints[i])) {
      Arrays.sort(points, referencePoints[i].SLOPE_ORDER);

      final Point startPoint = points[0];

      collinearPoints.clear();
      collinearPoints.add(startPoint);

      double lastSavedSlope = startPoint.slopeTo(points[1]);

      double tmpSlope;
      for (int j = 1; j < points.length; j++) {
        tmpSlope = startPoint.slopeTo(points[j]);

        if (Double.compare(tmpSlope, lastSavedSlope) == 0) {
          collinearPoints.add(points[j]);
        } else {
          printNewCollinearSegment(startPoint, discoveredSegments, collinearPoints, lastSavedSlope);

          collinearPoints.clear();
          collinearPoints.add(startPoint);
          collinearPoints.add(points[j]);
          lastSavedSlope = tmpSlope;
        }
      }

      printNewCollinearSegment(startPoint, discoveredSegments, collinearPoints, lastSavedSlope);
    }

  }

  private static void printNewCollinearSegment(final Point startPoint,
      final Map<Point, Set<Double>> discoveredSegments, final Set<Point> collinearPoints,
      double lastSavedSlope) {
    final Set<Double> slopesForPoint = discoveredSegments.get(startPoint);
    if (3 < collinearPoints.size()
        && (slopesForPoint == null || !slopesForPoint.contains(lastSavedSlope))) {
      print(collinearPoints.toArray(new Point[collinearPoints.size()]));
      registerSegment(lastSavedSlope, collinearPoints, discoveredSegments);
    }
  }

  private static void registerSegment(final double slope, final Set<Point> collinearPoints,
      final Map<Point, Set<Double>> discoveredSegments) {
    for (Point point : collinearPoints) {
      if (discoveredSegments.containsKey(point)) {
        discoveredSegments.get(point).add(slope);
      } else {
        final Set<Double> slopes = new HashSet<>();
        slopes.add(slope);
        discoveredSegments.put(point, slopes);
      }
    }
  }

  private static void print(final Point[] tuple) {
    final int size = tuple.length;

    Arrays.sort(tuple);

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
