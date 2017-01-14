package json.utils;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * @author James Tapsell
 */
public class StringStack {
  private final String s;
  private int index;
  private final List<Integer> lines;
  private static final String LINE_SEPARATOR = System.lineSeparator();

  public StringStack(final @NotNull String s) {
    this(s, 0);
  }

  private StringStack(final @NotNull String s, final int i) {
    this.s = s;
    index = i;
    lines = calculateLines(s);
  }

  public char pop() {
    final char result = s.charAt(index);
    index++;
    return result;
  }

  public int getIndex() {
    return index;
  }

  public char peek() {
    return s.charAt(index);
  }

  public boolean isNext(final @NotNull CharSequence text) {
    final int length = text.length();
    if (length + index > s.length()) {
      return false;
    }
    final String substring = s.substring(index, length + index);
    return available() >= length && substring.equals(text);
  }

  private int available() {
    return s.length() - index;
  }

  private boolean isComplete() {
    return available() == 0;
  }

  public void consume(final @NotNull CharSequence text) {
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

  private static List<Integer> calculateLines(final @NotNull String text) {
    final List<Integer> newLines = new ArrayList<>();
    newLines.add(0);
    for (int i = 0; i + LINE_SEPARATOR.length() < text.length(); i++) {
      final String candidateNewline = text.substring(i, i + LINE_SEPARATOR.length());
      if (LINE_SEPARATOR.equals(candidateNewline)) {
        newLines.add(i + LINE_SEPARATOR.length());
      }
    }
    return newLines;
  }

  public int lineStart(final int position) {
    final int lineIndex = getLineIndex(position);
    return lines.get(lineIndex);
  }

  private int getLineIndex(final int position) {
    if (position < 0 || position >= s.length()) {
      throw new AssertionError("Bad Location");
    }
    for (int i = 0; i < lines.size() - 1; i++) {
      final int lineStart = lines.get(i + 1);
      if (lineStart > position) {
        return i;
      }
    }
    return lines.size() - 1;
  }

  public String getLine(final int position) {
    final int lineIndex = getLineIndex(position);
    final boolean last = lineIndex == (lines.size() - 1);
    final int end = last ? s.length() : lines.get(lineIndex + 1);
    final int start = lines.get(lineIndex);

    final String substring = s.substring(start, end);
    return substring.endsWith(LINE_SEPARATOR) ?
        substring.substring(0, substring.length() - LINE_SEPARATOR.length()) :
        substring;
  }

  public String getText(int startIndex, int index) {
    return s.substring(startIndex, index);
  }
}
