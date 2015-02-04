import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A linear collection that supports element insertion and removal at both ends. The name
 * <i>deque</i> is short for "double ended queue" and is usually pronounced "deck".
 * 
 * @param <E> the type of the contained items
 */
public class Deque<E> implements Iterable<E> {

  private Node<E> first;
  private Node<E> last;

  private int size;
  private int modCount;

  /**
   * Constructs an empty deque.
   */
  public Deque() {}

  /**
   * Indicates if the deque is empty.
   * 
   * @return true, iff the deque is empty
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns the size of the deque.
   * 
   * @return the number of items on the deque
   */
  public int size() {
    return size;
  }

  /**
   * Inserts the given item at the front.
   * 
   * @param item
   */
  public void addFirst(final E item) {
    checkItemNotNull(item);

    final Node<E> prevFirst = first;
    final Node<E> newNode = new Node<>(null, item, prevFirst);

    first = newNode;

    if (prevFirst == null) {
      last = newNode;
    } else {
      prevFirst.prev = newNode;
    }

    ++size;
    ++modCount;
  }

  /**
   * Inserts an item at the end
   * 
   * @param item the item to be inserted
   * @throws NullPointerException if item is null
   */
  public void addLast(final E item) {
    checkItemNotNull(item);

    final Node<E> prevLast = last;
    final Node<E> newNode = new Node<>(prevLast, item, null);

    last = newNode;

    if (prevLast == null) {
      first = newNode;
    } else {
      prevLast.next = newNode;
    }

    ++size;
    ++modCount;
  }

  /**
   * Removes and returns the item at the front.
   * 
   * @return
   */
  public E removeFirst() {
    if (first == null) {
      throw new NoSuchElementException("Cannot remove first item from an empty deque");
    }

    final Node<E> oldFirst = first;
    final E item = oldFirst.item;
    final Node<E> next = oldFirst.next;

    first = next;
    if (next == null) {
      last = null;
    } else {
      next.prev = null;
    }

    // help GC
    oldFirst.item = null;
    oldFirst.next = null;

    --size;
    ++modCount;

    return item;
  }

  /**
   * Removes and returns the item at the end.
   * 
   * @return
   */
  public E removeLast() {
    if (last == null) {
      throw new NoSuchElementException("Cannot remove last item from an empty deque");
    }

    final Node<E> oldLast = last;
    final E item = oldLast.item;
    final Node<E> prev = oldLast.prev;

    last = prev;
    if (prev == null) {
      first = null;
    } else {
      prev.next = null;
    }

    // help GC
    oldLast.item = null;
    oldLast.prev = null;

    --size;
    ++modCount;

    return item;
  }

  /**
   * Creates an iterator over items in order from front to end.
   */
  @Override
  public Iterator<E> iterator() {
    return new DequeIterator();
  }

  private void checkItemNotNull(final E item) {
    if (item == null) {
      throw new NullPointerException("Cannot add a null item to the deque");
    }
  }

  private class DequeIterator implements Iterator<E> {

    private final int expectedModCount = modCount;

    private Node<E> cursor = first;

    @Override
    public boolean hasNext() {
      return cursor != null;
    }

    @Override
    public E next() {
      checkForComodification();

      if (!hasNext()) {
        throw new NoSuchElementException("There is no next element to iterate over");
      }

      final E item = cursor.item;
      cursor = cursor.next;
      return item;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("Removal through the iterator is not supported");
    }

    private void checkForComodification() {
      if (expectedModCount != modCount) {
        throw new ConcurrentModificationException();
      }
    }

  }

  private static class Node<E> {

    private Node<E> prev;
    private E item;
    private Node<E> next;

    Node(final Node<E> prev, final E item, final Node<E> next) {
      this.prev = prev;
      this.item = item;
      this.next = next;
    }

  }

}
