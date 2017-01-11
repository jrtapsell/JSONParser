package json.parser;

import java.util.List;
import json.utils.ContentType;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import json.utils.StringStack;

/**
 * @author James Tapsell
 */
public class JSONString {
  static boolean readString(List<Partition> partitions, StringStack ss) throws LocatedJSONException {
    final int startIndex = ss.getIndex();
    ss.pop();
    boolean escaped = false;
    while (ss.isAvailable()) {
      final char c = ss.pop();
      if (c == '\\') {
        escaped = !escaped;
      } else if (c == '"' && !escaped) {
        partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.STRING));
        return true;
      }
    }

    throw new LocatedJSONException("Unterminated String", ss, startIndex);
  }

  static boolean isString(StringStack ss) {
    return ss.peek() == '"';
  }
}
