package json.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.util.Pair;
import json.utils.ContentType;
import json.utils.JSONElementFactory;
import json.utils.JSONTreeElement;
import json.utils.LocatedJSONException;
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

  private static final JSONElementFactory[] FACTORIES = {
      JsonKeywordFactory.getInstance(),
      JsonNumberFactory.getInstance(),
      JsonStringFactory.getInstance(),
      JsonObjectFactory.getInstance(),
      JsonArrayFactory.getInstance()
  };
  private Json() {}

  @Contract("_ -> !null")
  public static Pair<List<Partition>, JSONTreeElement> parseString(final @NotNull String s) throws LocatedJSONException {
    final List<Partition> partitions = new ArrayList<>();
    final StringStack ss = new StringStack(s);
    consumeWhitespace(partitions, ss);
    final JSONTreeElement root = new JSONTreeElement(null, 0);
    parseAny(partitions, ss, root);
    consumeWhitespace(partitions,ss);
    if (ss.isAvailable()) {
      throw new LocatedJSONException("Bad character after Json", ss);
    }
    return new Pair<>(partitions, root);
  }


  static void parseAny(final @NotNull List<Partition> partitions, final @NotNull StringStack ss, final @NotNull JSONTreeElement root) throws LocatedJSONException {
    for (final JSONElementFactory factory : FACTORIES) {
      if (factory.isNext(ss)) {
        factory.read(partitions, ss, root);
        return;
      }
    }
    final String message = String.format(
        "Unknown character: %c",
        ss.peek());
    throw new LocatedJSONException(message, ss);
  }

  private static void consumeWhitespace(final @NotNull Collection<Partition> partitions, final StringStack ss) {
    if (ss.isAvailable() && Character.isWhitespace(ss.peek())) {
      final int startIndex = ss.getIndex();
      ss.seekWhitespace();
      if (ss.getIndex() != startIndex) {
        partitions.add(new Partition(startIndex, ss.getIndex(), ContentType.SPACE));
      }
    }
  }
}
