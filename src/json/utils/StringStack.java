package json.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author James Tapsell
 */
public class StringStack {
  private String s;
  private int index;
  private final List<Integer> lines;
  public static final String NL = System.lineSeparator();

  public StringStack(final String s) {
    this(s, 0);
  }

  public StringStack(final String s, final int i) {
    this.s = s;
    this.index = i;
    lines =calculateLines(s);
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

  public boolean isAvailable() {
    return !isComplete();
  }

  public String getRaw() {
    return s;
  }

  public void unpop() {
    index--;
  }

  public void seekWhitespace() {
    while (isAvailable() && Character.isWhitespace(peek())) {
      pop();
    }
  }

  public static List<Integer> calculateLines(String text) {
    List<Integer> newLines = new ArrayList<>();
    newLines.add(0);
    int line = 0;
    for (int i = 0; (i + NL.length()) < text.length(); i++) {
      final String candidateNewline = text.substring(i, i + NL.length());
      if (NL.equals(candidateNewline)) {
        newLines.add(i + NL.length());
      }
    }
    return newLines;
  }

  public int lineStart(int position) {
    int index = getLineIndex(position);
    return lines.get(index);
  }

  private int getLineIndex(int position) {
    if (position < 0 || position >= s.length()) {
      throw new AssertionError("Bad Location");
    }
    for (int i = 0; i < lines.size() - 1; i++) {
      int lineStart = lines.get(i + 1);
      if (lineStart > position) {
        return i;
      }
    }
    return lines.size() - 1;
  }

  public String getLine(int position) {
    int index = getLineIndex(position);
    int end;
    if (index == lines.size() - 1) {
      end = s.length();
    } else {
      end = lines.get(index + 1);
    }
    int start = lines.get(index);

    final String substring = s.substring(start, end);
    return substring.endsWith(NL) ? substring.substring(0, substring.length() - NL.length()) : substring;
  }
}
