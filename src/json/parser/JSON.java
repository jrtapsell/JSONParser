package json.parser;

import java.util.ArrayList;
import java.util.List;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import json.utils.StringStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author James Tapsell
 */
public class JSON {
  private JSON() {}

  @Contract("_ -> !null")
  public static List<Partition> parse(final @NotNull String s) throws LocatedJSONException {
    final List<Partition> partitions = new ArrayList<>();
    final StringStack ss = new StringStack(s);
    parseAny(partitions, ss);
    Utils.consumeWhitespace(partitions,ss);
    if (ss.isAvailable()) {
      throw new LocatedJSONException("Bad character after JSON", ss);
    }
    return partitions;
  }


  static void parseAny(final List<Partition> partitions, final StringStack ss) throws LocatedJSONException {
    Utils.consumeWhitespace(partitions, ss);
    if (JSONKeyword.isKeyword(ss)) {
      JSONKeyword.readKeyword(partitions, ss);
      return;
    }
    if (JSONNumber.isNumber(ss)) {
      JSONNumber.readNumber(partitions, ss);
      return;
    }
    if (JSONString.isString(ss)) {
      JSONString.readString(partitions, ss);
      return;
    }
    if (JSONObject.isObject(ss)) {
      JSONObject.parseObject(partitions, ss);
      return;
    }
    if (JSONArray.isArray(ss)) {
      JSONArray.parseArray(partitions, ss);
      return;
    }
    throw new LocatedJSONException("Unknown character: " + ss.peek(), ss);
  }

}
