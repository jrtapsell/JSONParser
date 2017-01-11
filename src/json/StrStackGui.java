package json;

import json.utils.LocatedJSONException;
import json.utils.StringStack;

/**
 * @author James Tapsell
 */
public class StrStackGui {
  public static void main(final String... args) {
    StringStack s = new StringStack("ABCDEF\nEFG");
    for (int i = 0; i < s.available(); i++) {
      LocatedJSONException ex = new LocatedJSONException("Bad", s, i);
      System.out.println(ex.getLocalizedMessage());
    }
  }
}
