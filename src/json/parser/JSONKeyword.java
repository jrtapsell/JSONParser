package json.parser;

import java.util.List;
import json.utils.ContentType;
import json.utils.Partition;
import json.utils.StringStack;

/**
 * @author James Tapsell
 */
public class JSONKeyword {
  static boolean isKeyword(StringStack ss) {
    return ss.nextIs("true") || ss.nextIs("false") || ss.nextIs("null");
  }

  static boolean readKeyword(List<Partition> partitions, StringStack ss) {
    if (checkKeyword(partitions, ss, "true", ContentType.BOOLEAN)) {
      return true;
    }
    if (checkKeyword(partitions, ss, "null", ContentType.NULL)) {
      return true;
    }
    if (checkKeyword(partitions, ss, "false", ContentType.BOOLEAN)) {
      return true;
    }
    return false;
  }

  private static boolean checkKeyword(final List<Partition> partitions, final StringStack ss, final String keyword, final ContentType type) {
    if (ss.nextIs(keyword)) {
      partitions.add(new Partition(ss.getIndex(), ss.getIndex() + keyword.length(), type));
      ss.consume(keyword);
      return true;
    }
    return false;
  }
}
