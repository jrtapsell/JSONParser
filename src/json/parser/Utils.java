package json.parser;

import java.util.List;
import json.utils.ContentType;
import json.utils.Partition;
import json.utils.StringStack;

/**
 * @author James Tapsell
 */
public class Utils {
  public static void consumeWhitespace(final List<Partition> partitions, final StringStack ss) {
    if (ss.isAvailable() && Character.isWhitespace(ss.peek())) {
      final int startIndex = ss.getIndex();
      ss.seekWhitespace();
      if (ss.getIndex() != startIndex) {
        partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.SPACE));
      }
    }
  }
}
