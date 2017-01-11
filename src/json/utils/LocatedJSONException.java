package json.utils;

import json.utils.StringStack;

/**
 * @author James Tapsell
 */
public class LocatedJSONException extends Exception {
  private final int position;
  private final StringStack ss;
  public LocatedJSONException(final String s, final StringStack stack) {
    super(s);
    ss = stack;
    position = stack.getIndex();
  }
  public LocatedJSONException(final String s, final StringStack stack, int position) {
    super(s);
    ss =stack;
    this.position = position;
  }

  public int getPosition() {
    return position;
  }
}
