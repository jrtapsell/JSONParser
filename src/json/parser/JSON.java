package json.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import json.utils.ContentType;
import json.utils.JSONElementFactory;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import json.utils.StringStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author James Tapsell
 */
public final class JSON {

  private static final JSONElementFactory[] FACTORIES = {
      JSONKeywordFactory.getInstance(),
      JSONNumberFactory.getInstance(),
      JSONStringFactory.getInstance(),
      JSONObjectFactory.getInstance(),
      JSONArrayFactory.getInstance()
  };
  private JSON() {}

  @Contract("_ -> !null")
  public static List<Partition> parseString(final @NotNull String s) throws LocatedJSONException {
    final List<Partition> partitions = new ArrayList<>();
    final StringStack ss = new StringStack(s);
    consumeWhitespace(partitions, ss);
    parseAny(partitions, ss);
    consumeWhitespace(partitions,ss);
    if (ss.isAvailable()) {
      throw new LocatedJSONException("Bad character after JSON", ss);
    }
    return partitions;
  }


  static void parseAny(final @NotNull List<Partition> partitions, final @NotNull StringStack ss) throws LocatedJSONException {
    for (final JSONElementFactory factory : FACTORIES) {
      if (factory.isNext(ss)) {
        factory.read(partitions, ss);
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
