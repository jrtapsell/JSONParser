package json.parser;

import java.util.List;
import json.utils.ContentType;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import json.utils.StringStack;

/**
 * @author James Tapsell
 */
public class JSONNumber {
  static void readNumber(final List<Partition> partitions, final StringStack ss) throws LocatedJSONException {
    boolean decimal = false; // Only allow a decimal once
    final int startIndex = ss.getIndex();
    if (ss.peek() == '-') {
      ss.pop();
    }
    int read = 0;
    while (ss.isAvailable() && (Character.isDigit(ss.peek()) || ss.peek() == '.')) {
      read++;
      final char c = ss.pop();
      if (c == '.') {
        if (decimal) {
          ss.unpop();
          throw new LocatedJSONException("Float with 2 dots", ss);
        } else {
          decimal = true;
        }
      }
    }
    if (read == 0) {
      throw new LocatedJSONException("Only a -", ss);
    }
    final int endIndex = ss.getIndex();
    partitions.add(new Partition(startIndex, endIndex, ContentType.NUMBER));
  }

  static boolean isNumber(StringStack ss) {
    return Character.isDigit(ss.peek()) || ss.peek() == '.' || ss.peek() == '-';
  }
}
