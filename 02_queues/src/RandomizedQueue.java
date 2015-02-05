import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A randomized queue that is similar to a stack or queue, except that the item removed is chosen
 * uniformly at random from items in the data structure.
 * 
 * @param <E> the type of the elements stored in the queue
 */
public class RandomizedQueue<E> implements Iterable<E> {

  private static final int DEFAULT_CAPACITY = 2;
  private static final float SHRINK_FACTOR = 0.25f;

  private E[] items;
  private int size;
  private int capacity;

  private int back;

  // helps to avoid comodifications
  private int modCount;

  /**
   * Constructs an empty randomized queue.
   */
  public RandomizedQueue() {
    resize(DEFAULT_CAPACITY);
  }

  /**
   * Indicates whether the queue is empty.
   * 
   * @return true, iff the queue is empty
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns the number of items on the queue.
   * 
   * @return the number of contained items
   */
  public int size() {
    return size;
  }

  /**
   * Adds an item to the back of the queue.
   * 
   * @param item the element to insert
   */
  public void enqueue(final E item) {
    if (item == null) {
      throw new NullPointerException("Cannot add null to the queue");
    }

    if (size == capacity) {
      resize(capacity * 2);
    }

    items[back++] = item;

    ++size;
    ++modCount;
  }

  /**
   * Deletes and returns a random item.
   * 
   * @return a random item that is being removed from the queue
   */
  public E dequeue() {
    checkEmpty();

    final int removedIndex = getRandomItemIndex();
    final E removedItem = items[removedIndex];

    final int lastIndex = --back;

    if (removedIndex != lastIndex) { // delete didn't happen at the end
      items[removedIndex] = items[lastIndex];
      items[lastIndex] = null; // avoid loitering
    }

    --size;
    ++modCount;

    final int shrinkThreshold = Math.round(capacity * SHRINK_FACTOR);
    if (size <= shrinkThreshold) {
      resize(shrinkThreshold);
    }

    return removedItem;
  }

  public E sample() {
    checkEmpty();
    return items[getRandomItemIndex()];
  }

  // return an independent iterator over items in random order
  @Override
  public Iterator<E> iterator() {
    return new RandomizedQueueIterator();
  }

  private void checkEmpty() {
    if (isEmpty()) {
      throw new NoSuchElementException("Queue is empty");
    }
  }

  private int getRandomItemIndex() {
    return StdRandom.uniform(size);
  }

  private void resize(final int newCapacity) {
    if (items == null) {
      items = createEmptyArray();
    }

    capacity = newCapacity;
    items = Arrays.copyOf(items, newCapacity);

    back = size;
  }

  @SuppressWarnings("unchecked")
  private E[] createEmptyArray() {
    return (E[]) new Object[] {};
  }

  private class RandomizedQueueIterator implements Iterator<E> {

    private final int expectedModCount = modCount;
    private final int[] itemIndices = new int[size()];

    private int cursor;

    public RandomizedQueueIterator() {
      for (int i = 0; i < itemIndices.length; i++) {
        itemIndices[i] = i;
      }

      StdRandom.shuffle(itemIndices);
    }

    @Override
    public boolean hasNext() {
      return cursor != itemIndices.length;
    }

    @Override
    public E next() {
      if (expectedModCount != modCount) {
        throw new ConcurrentModificationException();
      }

      if (!hasNext()) {
        throw new NoSuchElementException("No more element to iterate over");
      }

      final int randomIndex = itemIndices[cursor++];
      return items[randomIndex];
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("Removal through iterator is not supported");
    }

  }

}
