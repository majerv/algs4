import java.util.Comparator;

/**
 * An immutable data type for points in the plane.
 */
public class Point implements Comparable<Point> {

  // compare points by slope
  public final Comparator<Point> SLOPE_ORDER = new Comparator<Point>() {
    @Override
    public int compare(final Point point, final Point point2) {
      final double slope1 = slopeTo(point);
      final double slope2 = slopeTo(point2);

      int returnValue;
      if (slope1 == slope2) {
        returnValue = 0;
      } else if (slope1 < slope2) {
        returnValue = -1;
      } else {
        returnValue = 1;
      }

      return returnValue;
    }
  };

  private final int x; // x coordinate
  private final int y; // y coordinate

  /**
   * Creates the point (x, y).
   */
  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Plots this point to standard drawing.
   */
  public void draw() {
    StdDraw.point(x, y);
  }

  /**
   * Draws line between this point and that point to standard drawing.
   */
  public void drawTo(Point that) {
    StdDraw.line(this.x, this.y, that.x, that.y);
  }

  /**
   * Calculates slope between this point and that point.
   */
  public double slopeTo(final Point that) {
    if (x == that.x && y == that.y) {
      return Double.NEGATIVE_INFINITY;
    } else if (y == that.y) {
      return 0;
    } else if (x == that.x) {
      return Double.POSITIVE_INFINITY;
    } else {
      return (that.y - y) / (double) (that.x - x);
    }
  }

  /**
   * Indicates whether this point lexicographically smaller than that one.
   * <p>
   * Compares y-coordinates and breaking ties by x-coordinates: the invoking point (x0, y0) is less
   * than the argument point (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1
   */
  @Override
  public int compareTo(final Point that) {
    if (this == that) {
      return 0;
    }

    final int diff = y - that.y;

    if (diff == 0) {
      return x - that.x;
    } else {
      return diff;
    }
  }

  /**
   * Returns the string representation of this point.
   */
  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

}
