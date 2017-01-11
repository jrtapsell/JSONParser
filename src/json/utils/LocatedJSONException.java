package json.utils;

/**
 * @author James Tapsell
 */
public class LocatedJSONException extends Exception {
  private final int position;

  public LocatedJSONException(final String s, final StringStack stack) {
    this(s, stack, stack.getIndex());
  }
  public LocatedJSONException(final String s, final StringStack stack, final int position) {
    super(s + System.lineSeparator() + getLineDisplay(stack, position));
    final StringStack ss = stack;
    this.position = position;
  }

  public int getPosition() {
    return position;
  }

  private static String getLineDisplay(final StringStack ss, final int position) {
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
