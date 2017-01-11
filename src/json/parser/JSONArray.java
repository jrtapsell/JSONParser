package json.parser;

import java.util.List;
import json.utils.ContentType;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import json.utils.StringStack;

/**
 * @author James Tapsell
 */
public class JSONArray {
  static void parseArray(final List<Partition> partitions, final StringStack ss) throws LocatedJSONException {
    int startIndex = ss.getIndex();
    final int origin = startIndex;
    ss.pop();
    while (ss.isAvailable()) {
      ss.seekWhitespace();
      if (ss.peek() == ']') {
        ss.pop();
        partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.ARRAY));
        return;
      }
      partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.ARRAY));
      JSON.parseAny(partitions, ss);
      startIndex = ss.getIndex();
      ss.seekWhitespace();
      if (ss.peek() == ']') {
        continue;
      }
      if (ss.pop() != ',') {
        throw new LocatedJSONException("Missing comma", ss, ss.getIndex() - 1);
      }
    }
    throw new LocatedJSONException("Unterminated array", ss, origin);
  }
}
