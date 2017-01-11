package json.utils;

/**
 * @author James Tapsell
 */
public class LocatedJSONException extends Exception {
  private final int position;
  private final StringStack ss;
  public LocatedJSONException(final String s, final StringStack stack) {
    this(s, stack, stack.getIndex());
  }
  public LocatedJSONException(final String s, final StringStack stack, int position) {
    super(s + System.lineSeparator() + getLineDisplay(stack, position));
    ss =stack;
    this.position = position;
  }

  public int getPosition() {
    return position;
  }

  static public String getLineDisplay(StringStack ss, int position) {
    int lineStart = ss.lineStart(position);
    String line = ss.getLine(position);
    int offset = position - lineStart;
    StringBuilder sb = new StringBuilder();
    sb.append(line);
    sb.append(System.lineSeparator());
    for (int i = 0; i < offset; i++) {
      sb.append("-");
    }
    sb.append("^");
    return sb.toString();
  }
}
