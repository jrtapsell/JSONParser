package json.parser;

import java.util.List;
import json.utils.ContentType;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import json.utils.StringStack;

/**
 * @author James Tapsell
 */
public class JSONObject {
  static void parseObject(final List<Partition> partitions, final StringStack ss) throws LocatedJSONException {
    int startIndex = ss.getIndex();
    final int origin = startIndex;
    ss.pop();
    while (ss.isAvailable()) {
      ss.seekWhitespace();
      if (ss.peek() == '}') {
        ss.pop();
        partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.OBJECT));
        return;
      }
      partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.OBJECT));
      if (ss.peek() != '"') {
        throw new LocatedJSONException("Key must be a string", ss);
      }
      JSONString.readString(partitions, ss);
      startIndex = ss.getIndex();
      ss.seekWhitespace();
      if (ss.pop() != ':') {
        throw new LocatedJSONException("Missing : ", ss, ss.getIndex() - 1);
      }
      ss.seekWhitespace();
      partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.OBJECT));
      JSON.parseAny(partitions, ss);
      startIndex = ss.getIndex();
      ss.seekWhitespace();
      if (ss.peek() == '}') {
        continue;
      }
      if (ss.pop() != ',') {
        throw new LocatedJSONException("Missing , ", ss);
      }
    }
    throw new LocatedJSONException("Unterminated JSONObject",ss, origin);
  }
}
