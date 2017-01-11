package json.utils;

import java.util.function.Predicate;

/**
 * @author James Tapsell
 */
public class StringStack {
  private String s;
  private int index;
  public StringStack(final String s) {
    this(s, 0);
  }

  public StringStack(final String s, final int i) {
    this.s = s;
    this.index = i;
  }

  public char pop() {
    char result = s.charAt(index);
    index++;
    return result;
  }

  public int getIndex() {
    return index;
  }

  public char peek() {
    return s.charAt(index);
  }

  public boolean nextIs(final String text) {
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

  public void consume(final String text) {
    consume(text.length());
  }

  private void consume(final int length) {
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

  void seekWhitespace() {
    while (isAvailable() && Character.isWhitespace(peek())) {
      pop();
    }
  }
}
