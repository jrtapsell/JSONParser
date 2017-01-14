package json.utils;

import org.jetbrains.annotations.NotNull;

/**
 * An error in a JSON string, with the index that it occured.
 *
 * @author James Tapsell
 */
public class LocatedJsonException extends Exception {
  private final int position;

  public LocatedJsonException(
      final @NotNull String text,
      final @NotNull StringStack stack) {
    this(text, stack, stack.getIndex());
  }

  /**
   * Creates a new exception.
   *
   * @param test
   *  The exception message.
   * @param stack
   *  The stack that the error happened in.
   * @param position
   *  The position of the error.
   */
  public LocatedJsonException(
      final @NotNull String test,
      final @NotNull StringStack stack,
      final int position) {
    super(String.format(
        "%s%s%s",
        test,
        System.lineSeparator(),
        getLineDisplay(stack, position)));
    final StringStack ss = stack;
    this.position = position;
  }

  public int getPosition() {
    return position;
  }

  private static String getLineDisplay(final @NotNull StringStack ss, final int position) {
    final int lineStart = ss.lineStart(position);
    final String line = ss.getLine(position);
    final int offset = position - lineStart;
    final StringBuilder sb = new StringBuilder();
    sb.append(line);
    sb.append(System.lineSeparator());
    for (int i = 0; i < offset; i++) {
      sb.append("-");
    }
    sb.append("^");
    return sb.toString();
  }
}
