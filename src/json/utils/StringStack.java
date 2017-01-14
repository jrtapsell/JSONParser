package json.utils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * A string with a stacklike interface.
 *
 * @author James Tapsell
 */
public class StringStack {
  private final String text;
  private int index;
  private final List<Integer> lines;
  private static final String LINE_SEPARATOR = System.lineSeparator();

  public StringStack(final @NotNull String text) {
    this(text, 0);
  }

  private StringStack(final @NotNull String text, final int i) {
    this.text = text;
    index = i;
    lines = calculateLines(text);
  }

  /**
   * Pops a character off the stack.
   *
   * @return
   *  The character that was popped.
   */
  public char pop() {
    final char result = text.charAt(index);
    index++;
    return result;
  }

  public int getIndex() {
    return index;
  }

  public char peek() {
    return text.charAt(index);
  }

  /**
   * Checks if the string is the next characters in the stack.
   *
   * @param text
   *  The text to check against
   * @return
   *  true iff the next part of the string is equal to text.
   */
  public boolean isNext(final @NotNull CharSequence text) {
    final int length = text.length();
    if (length + index > this.text.length()) {
      return false;
    }
    final String substring = this.text.substring(index, length + index);
    return available() >= length && substring.equals(text);
  }

  private int available() {
    return text.length() - index;
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
    return text;
  }

  public void unpop() {
    index--;
  }

  /**
   * Pops all the whitespace characters until the next non-whitespace character.
   */
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
    if (position < 0 || position >= text.length()) {
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

  /**
   * Gets the text of the line including the given line number (0 indexed).
   *
   * @param position
   *  The position to get for
   * @return
   *  The text of the line, with the ending newline stripped off
   */
  public String getLine(final int position) {
    final int lineIndex = getLineIndex(position);
    final boolean last = lineIndex == (lines.size() - 1);
    final int end = last ? text.length() : lines.get(lineIndex + 1);
    final int start = lines.get(lineIndex);

    final String substring = text.substring(start, end);
    return substring.endsWith(LINE_SEPARATOR)
        ? substring.substring(0, substring.length() - LINE_SEPARATOR.length())
        : substring;
  }

  public String getText(int startIndex, int index) {
    return text.substring(startIndex, index);
  }
}
