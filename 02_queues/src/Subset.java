public class Subset {

  public static void main(String[] args) {
    final RandomizedQueue<String> queue = new RandomizedQueue<String>();

    if (args.length < 1) {
      throw new IllegalArgumentException("Not enough command-line arguments");
    }

    final int selectedItemCount = Integer.parseInt(args[0]);
    if (selectedItemCount <= 0) {
      return;
    }

    while (!StdIn.isEmpty()) {
      queue.enqueue(StdIn.readString());
    }

    for (int i = 0; i < selectedItemCount; i++) {
      StdOut.println(queue.dequeue());
    }
  }

}
