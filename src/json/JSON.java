package json;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author James Tapsell
 */
public class JSON {
  private JSON() {}

  public static List<Partition> getPartitions(String s) throws JsonException {
    final Stack<ContentType> stack = new Stack<>();
    final List<Partition> partitions = new ArrayList<>();
    final StringStack ss = new StringStack(s);
    parseAny(partitions, ss);
    consumeWhitespace(partitions,ss);
    return partitions;
  }

  private static void parseAny(List<Partition> partitions, StringStack ss) throws JsonException {
    consumeWhitespace(partitions, ss);
    if (parseKeyable(partitions, ss)) {
      return;
    }
    if (parseUnkeyable(partitions, ss)) {
      return;
    }
    throw new LocatedJSONException("Unknown character: " + ss.peek(), ss);
  }

  private static void consumeWhitespace(List<Partition> partitions, StringStack ss) {
    if (ss.isAvailable() && Character.isWhitespace(ss.peek())) {
      int startIndex = ss.getIndex();
      seekWhitespace(ss);
      if (ss.getIndex() != startIndex) {
        partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.SPACE));
      }
    }
  }

  private static void seekWhitespace(StringStack ss) {
    while (ss.isAvailable() && Character.isWhitespace(ss.peek())) {
      ss.pop();
    }
  }

  private static boolean parseUnkeyable(List<Partition> partitions, StringStack ss) throws JsonException {
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

  private static void parseArray(List<Partition> partitions, StringStack ss) throws JsonException{
    int startIndex = ss.getIndex();
    ss.pop();
    while (ss.isAvailable()) {
      seekWhitespace(ss);
      if (ss.peek() == ']') {
        ss.pop();
        partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.ARRAY));
        return;
      }
      partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.ARRAY));
      parseAny(partitions, ss);
      startIndex = ss.getIndex();
      seekWhitespace(ss);
      if (ss.peek() == ']') {
        continue;
      }
      if (ss.pop() != ',') {
        throw new LocatedJSONException("Missing comms", ss);
      }
    }
    throw new JsonException("Unterminated array");
  }

  private static void parseObject(List<Partition> partitions, StringStack ss) throws JsonException {
    int startIndex = ss.getIndex();
    ss.pop();
    while (ss.isAvailable()) {
      seekWhitespace(ss);
      if (ss.peek() == '}') {
        ss.pop();
        partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.OBJECT));
        return;
      }
      partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.OBJECT));
      parseKeyable(partitions, ss);
      startIndex = ss.getIndex();
      seekWhitespace(ss);
      if (ss.pop() != ':') {
        throw new LocatedJSONException("Missing : ", ss);
      }
      seekWhitespace(ss);
      partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.OBJECT));
      parseAny(partitions, ss);
      startIndex = ss.getIndex();
      seekWhitespace(ss);
      if (ss.peek() == '}') {
        continue;
      }
      if (ss.pop() != ',') {
        throw new LocatedJSONException("Missing , ", ss);
      }
    }
    throw new JsonException("UnterminatedObject");
  }

  private static boolean parseKeyable(List<Partition> partitions, StringStack ss) throws JsonException {
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
      int startIndex = ss.getIndex();
      ss.pop();
      boolean escaped = false;
      while (ss.isAvailable()) {
        char c = ss.pop();
        if (c == '\\') {
          escaped = !escaped;
        } else if (c == '"' && !escaped) {
          partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.STRING));
          return true;
        }
      }
      throw new JsonException("Unterminated String");
    }
    return false;
  }

  private static void readNumber(List<Partition> partitions, StringStack ss) throws JsonException {
    boolean decimal = false; // Only allow a decimal once
    final int startIndex = ss.getIndex();
    if (ss.peek() == '-') {
      ss.pop();
    }
    int read = 0;
    while (ss.isAvailable() && (Character.isDigit(ss.peek()) || ss.peek() == '.')) {
      read++;
      char c = ss.pop();
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

  private static boolean checkKeyword(List<Partition> partitions, StringStack ss, String keyword, ContentType type) {
    if (ss.nextIs(keyword)) {
      partitions.add(new Partition(ss.getIndex(), ss.getIndex() + keyword.length(), type));
      ss.consume(keyword);
      return true;
    }
    return false;
  }
}
