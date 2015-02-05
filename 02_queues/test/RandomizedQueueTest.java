import static com.google.common.truth.Truth.ASSERT;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link RandomizedQueue}.
 */
public class RandomizedQueueTest {

  private static final int LOAD_TEST_ITEM_COUNT = 1_000_000;

  // unit under test
  private RandomizedQueue<String> queue;

  @Before
  public void setUpQueue() {
    queue = new RandomizedQueue<String>();
  }

  @Test
  public void emptyAfterCreated() {
    // given -- queue is initialized

    // then
    verifyEmpty();
  }

  @Test(expected = NullPointerException.class)
  public void cannotEnqueueNull() {
    // given -- queue is initialized

    // when
    queue.enqueue(null);
  }

  @Test(expected = NoSuchElementException.class)
  public void cannotSampleFromEmptyQueue() {
    // given -- queue is initialized

    // when
    queue.sample();
  }

  @Test(expected = NoSuchElementException.class)
  public void cannotDequeueFromEmptyQueue() {
    // given -- queue is initialized

    // when
    queue.dequeue();
  }

  @Test
  public void notEmptyAfterItemEnqueued() {
    // given -- queue is initialized

    // when
    queue.enqueue(TestData.MAKKA_PAKKA);

    // then
    verifyNotEmpty();
    verifySize(1);
  }

  @Test
  public void sampleEualsToSoleItem() {
    // given
    queue.enqueue(TestData.MAKKA_PAKKA);

    // when
    final String item = queue.sample();

    // then
    verifyNotEmpty();
    assertEquals(TestData.MAKKA_PAKKA, item);
  }

  @Test
  public void emptyAfterItemEnqueuedAndDequed() {
    // given
    queue.enqueue(TestData.MAKKA_PAKKA);

    // when
    final String item = queue.dequeue();

    // then
    verifyEmpty();
    assertEquals(TestData.MAKKA_PAKKA, item);
  }

  @Test
  public void loadTestEnqueue() {
    // given -- queue is initialized

    // when
    for (int i = 0; i < LOAD_TEST_ITEM_COUNT; i++) {
      queue.enqueue(String.valueOf(i));
    }

    // then
    verifyNotEmpty();
    verifySize(LOAD_TEST_ITEM_COUNT);
  }

  @Test
  public void loadTestDequeue() {
    // given -- queue is initialized

    // when
    for (int i = 0; i < LOAD_TEST_ITEM_COUNT; i++) {
      queue.enqueue(String.valueOf(i));
    }

    for (int i = 0; i < LOAD_TEST_ITEM_COUNT; i++) {
      queue.dequeue();
    }

    // then
    verifyEmpty();
  }

  @Test
  public void loadTestMixed() {
    // given -- queue is initialized
    int removals = 0;

    for (int i = 0; i < LOAD_TEST_ITEM_COUNT; i++) {
      queue.enqueue(String.valueOf(i));
    }

    // when
    for (int i = 0; i < LOAD_TEST_ITEM_COUNT; i++) {
      if (StdRandom.uniform(2) == 0) {
        queue.dequeue();
        ++removals;
      } else {
        queue.enqueue(String.valueOf(i));
      }
    }

    // then
    verifySize(2 * LOAD_TEST_ITEM_COUNT - 2 * removals);
  }

  @Test
  public void testIterator() {
    // given
    queue.enqueue(TestData.MAKKA_PAKKA);
    queue.enqueue(TestData.IGGLE_PIGGLE);
    queue.enqueue(TestData.UPSY_DAISY);
    queue.enqueue(TestData.TOMBLIBOOS);

    // when
    final List<String> result = new ArrayList<>();
    for (Iterator<String> iterator = queue.iterator(); iterator.hasNext();) {
      result.add(iterator.next());
    }

    // then
    ASSERT.that(result).containsExactly(TestData.MAKKA_PAKKA, TestData.IGGLE_PIGGLE,
        TestData.UPSY_DAISY, TestData.TOMBLIBOOS);
  }

  private void verifyEmpty() {
    assertTrue("Queue must be empty", queue.isEmpty());
    verifySize(0);
  }

  private void verifyNotEmpty() {
    assertFalse("Queue shouldn't be empty", queue.isEmpty());
  }

  private void verifySize(final int expectedSize) {
    assertEquals("Queue has different size than expected", expectedSize, queue.size());
  }

}
