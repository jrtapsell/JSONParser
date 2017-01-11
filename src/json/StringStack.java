package json;

import java.util.function.Predicate;

/**
 * @author James Tapsell
 */
public class StringStack {
  private String s;
  private int index;
  public StringStack(String s) {
    this(s, 0);
  }

  public StringStack(String s, int i) {
    this.s = s;
    this.index = i;
  }

  public char pop() {
    return s.charAt(index++);
  }

  public int getIndex() {
    return index;
  }

  public char peek() {
    return s.charAt(index);
  }

  public boolean nextIs(String text) {
    final int length = text.length();
    if (length + index > s.length()) {
      return false;
    }
    final String substring = s.substring(index, length + index);
    return available() >= length && substring.equals(text);
  }

  public int available() {
    return s.length() - index;
  }

  public boolean isComplete() {
    return available() == 0;
  }

  public void consume(String text) {
    consume(text.length());
  }

  private void consume(int length) {
    index += length;
  }

  boolean isAvailable() {
    return !isComplete();
  }

  public String getRaw() {
    return s;
  }

  public void unpop() {
    index--;
  }
}
