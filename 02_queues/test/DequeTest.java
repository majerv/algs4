import static com.google.common.truth.Truth.ASSERT;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;


public class DequeTest {

  private static final String MAKKA_PAKKA = "MakkaPakka";
  private static final String UPSY_DAISY = "UpsyDaisy";
  private static final String IGGLE_PIGGLE = "IgglePiggle";
  private static final String TOMBLIBOOS = "Tombliboos";

  // unit under test
  private Deque<String> deque;

  @Before
  public void setUpDeque() {
    deque = new Deque<String>();
  }

  @Test
  public void dequeIsEmptyAfterCreation() {
    // given -- deque initialized

    // then
    verifyEmpty();
  }

  @Test(expected = NullPointerException.class)
  public void cannotAddFirstNullItem() {
    // given -- deque initialized

    // when
    deque.addFirst(null);
  }

  @Test(expected = NullPointerException.class)
  public void cannotAddLastNullItem() {
    // given -- deque initialized

    // when
    deque.addLast(null);
  }

  @Test(expected = NoSuchElementException.class)
  public void cannotRemoveFirstIfEmpty() {
    // given -- deque initialized

    // when
    deque.removeFirst();
  }

  @Test(expected = NoSuchElementException.class)
  public void cannotRemoveLastIfEmpty() {
    // given -- deque initialized

    // when
    deque.removeLast();
  }

  @Test
  public void dequeIsNotEmptyIfItemAddedFirst() {
    // given
    deque.addFirst(MAKKA_PAKKA);

    // then
    verifyNotEmpty();
    verifySize(1);
  }

  @Test
  public void dequeIsNotEmptyIfItemAddedLast() {
    // given
    deque.addLast(MAKKA_PAKKA);

    // then
    verifyNotEmpty();
    verifySize(1);
  }

  @Test
  public void addFirst_RemoveFirst_Sequence() {
    // given
    deque.addFirst(MAKKA_PAKKA);

    // when
    final String item = deque.removeFirst();

    // then
    assertEquals(MAKKA_PAKKA, item);
    verifyEmpty();
  }

  @Test
  public void addFirst_RemoveLast_Sequence() {
    // given
    deque.addFirst(MAKKA_PAKKA);

    // when
    final String item = deque.removeLast();

    // then
    assertEquals(MAKKA_PAKKA, item);
    verifyEmpty();
  }

  @Test
  public void addLast_RemoveLast_Sequence() {
    // given
    deque.addLast(MAKKA_PAKKA);

    // when
    final String item = deque.removeLast();

    // then
    assertEquals(MAKKA_PAKKA, item);
    verifyEmpty();
  }

  @Test
  public void addLast_RemoveFirst_Sequence() {
    // given
    deque.addLast(MAKKA_PAKKA);

    // when
    final String item = deque.removeFirst();

    // then
    assertEquals(MAKKA_PAKKA, item);
    verifyEmpty();
  }

  @Test
  public void removeFirstFromMultipleItems() {
    // given
    deque.addLast(MAKKA_PAKKA);
    deque.addLast(IGGLE_PIGGLE);
    deque.addLast(UPSY_DAISY);

    // when
    final String item = deque.removeFirst();

    // then
    assertEquals(MAKKA_PAKKA, item);
    verifyNotEmpty();
    verifySize(2);
  }

  @Test
  public void removeLastFromMultipleItems() {
    // given
    deque.addLast(MAKKA_PAKKA);
    deque.addLast(IGGLE_PIGGLE);
    deque.addLast(UPSY_DAISY);

    // when
    final String item = deque.removeLast();

    // then
    assertEquals(UPSY_DAISY, item);
    verifyNotEmpty();
    verifySize(2);
  }

  @Test
  public void testIteratorViaForLoop() {
    // given
    deque.addFirst(MAKKA_PAKKA);
    deque.addFirst(UPSY_DAISY);
    deque.addFirst(IGGLE_PIGGLE);

    // when
    final List<String> results = new ArrayList<>();

    for (String item : deque) {
      results.add(item);
    }

    // then
    ASSERT.that(results).containsExactly(IGGLE_PIGGLE, UPSY_DAISY, MAKKA_PAKKA).inOrder();
  }

  @Test
  public void testIterator() {
    // given
    deque.addFirst(MAKKA_PAKKA);
    deque.addLast(UPSY_DAISY);
    deque.addFirst(IGGLE_PIGGLE);
    deque.addLast(TOMBLIBOOS);

    // when
    final List<String> results = new ArrayList<>();

    for (Iterator<String> iterator = deque.iterator(); iterator.hasNext();) {
      results.add(iterator.next());
    }

    // then
    ASSERT.that(results).containsExactly(IGGLE_PIGGLE, MAKKA_PAKKA, UPSY_DAISY, TOMBLIBOOS)
        .inOrder();
  }

  @Test(expected = ConcurrentModificationException.class)
  public void concurrentModificationIsDetected() {
    // given
    deque.addFirst(MAKKA_PAKKA);
    deque.addFirst(UPSY_DAISY);

    // when
    for (String item : deque) {
      deque.addFirst(item);
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void removeNotAllowedThroughIterator() {
    // given
    deque.addFirst(MAKKA_PAKKA);

    // when
    for (Iterator<String> iterator = deque.iterator(); iterator.hasNext();) {
      iterator.remove();
    }
  }

  @Test(expected = NoSuchElementException.class)
  public void fetchNextNotAllowedThroughEmptyIterator() {
    // given

    // when
    final Iterator<String> iterator = deque.iterator();

    // then
    iterator.next();
  }

  private void verifyEmpty() {
    assertTrue("Deque must be empty", deque.isEmpty());
    verifySize(0);
  }

  private void verifyNotEmpty() {
    assertFalse("Deque should not be empty", deque.isEmpty());
  }

  private void verifySize(final int expectedSize) {
    assertEquals("Size must be different", expectedSize, deque.size());
  }

}
