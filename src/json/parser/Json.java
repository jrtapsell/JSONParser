package json.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.util.Pair;
import json.utils.ContentType;
import json.utils.JsonElementFactory;
import json.utils.JsonTreeElement;
import json.utils.LocatedJsonException;
import json.utils.Partition;
import json.utils.StringStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The Json Parser.
 *
 * @author James Tapsell
 */
public final class Json {

  private static final JsonElementFactory[] FACTORIES = {
      JsonKeywordFactory.getInstance(),
      JsonNumberFactory.getInstance(),
      JsonStringFactory.getInstance(),
      JsonObjectFactory.getInstance(),
      JsonArrayFactory.getInstance()
  };

  private Json() {}

  /**
   * Parses a JSON String.
   *
   * @param text
   *  The string to parse
   * @return
   *  The list of sections
   * @throws LocatedJsonException
   *  Thrown if the JSON is invalid
   */
  @Contract("_ -> !null")
  public static Pair<List<Partition>, JsonTreeElement> parseString(
      final @NotNull String text) throws LocatedJsonException {
    final List<Partition> partitions = new ArrayList<>();
    final StringStack ss = new StringStack(text);
    consumeWhitespace(partitions, ss);
    final JsonTreeElement root = new JsonTreeElement(null, 0);
    parseAny(partitions, ss, root);
    consumeWhitespace(partitions,ss);
    if (ss.isAvailable()) {
      throw new LocatedJsonException("Bad character after Json", ss);
    }
    return new Pair<>(partitions, root);
  }


  static void parseAny(
      final @NotNull List<Partition> partitions,
      final @NotNull StringStack ss,
      final @NotNull JsonTreeElement root) throws LocatedJsonException {
    for (final JsonElementFactory factory : FACTORIES) {
      if (factory.isNext(ss)) {
        factory.read(partitions, ss, root);
        return;
      }
    }
    final String message = String.format(
        "Unknown character: %c",
        ss.peek());
    throw new LocatedJsonException(message, ss);
  }

  private static void consumeWhitespace(
      final @NotNull Collection<Partition> partitions,
      final @NotNull StringStack ss) {
    if (ss.isAvailable() && Character.isWhitespace(ss.peek())) {
      final int startIndex = ss.getIndex();
      ss.seekWhitespace();
      if (ss.getIndex() != startIndex) {
        partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.SPACE));
      }
    }
  }
}
