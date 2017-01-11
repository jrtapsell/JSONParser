package json.parser;

import java.util.ArrayList;
import java.util.List;
import json.utils.ContentType;
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


  private static void parseAny(final List<Partition> partitions, final StringStack ss) throws LocatedJSONException {
    Utils.consumeWhitespace(partitions, ss);
    if (parseKeyable(partitions, ss)) {
      return;
    }
    if (parseUnkeyable(partitions, ss)) {
      return;
    }
    throw new LocatedJSONException("Unknown character: " + ss.peek(), ss);
  }

  private static boolean parseUnkeyable(final List<Partition> partitions, final StringStack ss) throws LocatedJSONException {
    if (ss.peek() == '{') {
      parseObject(partitions, ss);
      return true;
    }
    if (ss.peek() == '[') {
      parseArray(partitions, ss);
      return true;
    }
    return false;
  }

  private static void parseArray(final List<Partition> partitions, final StringStack ss) throws LocatedJSONException{
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
      parseAny(partitions, ss);
      startIndex = ss.getIndex();
      ss.seekWhitespace();
      if (ss.peek() == ']') {
        continue;
      }
      if (ss.pop() != ',') {
        throw new LocatedJSONException("Missing comma", ss);
      }
    }
    throw new LocatedJSONException("Unterminated array", ss, origin);
  }

  private static void parseObject(final List<Partition> partitions, final StringStack ss) throws LocatedJSONException {
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
      readString(partitions, ss);
      startIndex = ss.getIndex();
      ss.seekWhitespace();
      if (ss.pop() != ':') {
        throw new LocatedJSONException("Missing : ", ss);
      }
      ss.seekWhitespace();
      partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.OBJECT));
      parseAny(partitions, ss);
      startIndex = ss.getIndex();
      ss.seekWhitespace();
      if (ss.peek() == '}') {
        continue;
      }
      if (ss.pop() != ',') {
        throw new LocatedJSONException("Missing , ", ss);
      }
    }
    throw new LocatedJSONException("Unterminated Object",ss, origin);
  }

  private static boolean parseKeyable(final List<Partition> partitions, final StringStack ss) throws LocatedJSONException {
    if (checkKeyword(partitions, ss, "true", ContentType.BOOLEAN)) {
      return true;
    }
    if (checkKeyword(partitions, ss, "null", ContentType.NULL)) {
      return true;
    }
    if (checkKeyword(partitions, ss, "false", ContentType.BOOLEAN)) {
      return true;
    }
    if (Character.isDigit(ss.peek()) || ss.peek() == '.' || ss.peek() == '-') {
      readNumber(partitions, ss);
      return true;
    }
    if (ss.peek() == '"') {
      readString(partitions, ss);
      return true;
    }
    return false;
  }

  private static boolean readString(List<Partition> partitions, StringStack ss) throws LocatedJSONException {
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

  private static void readNumber(final List<Partition> partitions, final StringStack ss) throws LocatedJSONException {
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

  private static boolean checkKeyword(final List<Partition> partitions, final StringStack ss, final String keyword, final ContentType type) {
    if (ss.nextIs(keyword)) {
      partitions.add(new Partition(ss.getIndex(), ss.getIndex() + keyword.length(), type));
      ss.consume(keyword);
      return true;
    }
    return false;
  }
}
