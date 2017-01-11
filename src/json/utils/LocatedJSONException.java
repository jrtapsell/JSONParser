package json.utils;

import org.jetbrains.annotations.NotNull;

/**
 * @author James Tapsell
 */
public class LocatedJSONException extends Exception {
  private final int position;

  public LocatedJSONException(
      final @NotNull String s,
      final @NotNull StringStack stack) {
    this(s, stack, stack.getIndex());
  }
  public LocatedJSONException(
      final @NotNull String s,
      final @NotNull StringStack stack,
      final int position) {
    super(String.format(
        "%s%s%s",
        s,
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
