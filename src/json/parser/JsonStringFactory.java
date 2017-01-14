package json.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import json.utils.ContentType;
import json.utils.JsonElementFactory;
import json.utils.JsonTreeElement;
import json.utils.LocatedJsonException;
import json.utils.Partition;
import json.utils.StringStack;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for JSONElements of type String.
 *
 * @author James Tapsell
 */
public final class JsonStringFactory implements JsonElementFactory {
  private static final JsonStringFactory INSTANCE = new JsonStringFactory();

  public static JsonStringFactory getInstance() {
    return INSTANCE;
  }

  private JsonStringFactory() {}

  private static final List<Character> singleEscapes = Arrays.asList(
      '\\','b','f','n','r','t', '"'
  );

  @Override
  public void read(
      final @NotNull List<Partition> partitions,
      final @NotNull StringStack stack,
      final @NotNull JsonTreeElement tree) throws LocatedJsonException {
    final int startIndex = stack.getIndex();
    stack.pop();
    while (stack.isAvailable()) {
      final int escapeStart = stack.getIndex();
      final char c = stack.pop();
      if (c == '\\') {
        char escaped = stack.pop();
        if (singleEscapes.contains(escaped)) {
          continue;
        } else if (escaped == 'u') {
          if (stack.available() < 4) {
            throw new LocatedJsonException("Unicode escape needs 4 digits", stack, escapeStart);
          }
          String escape = "";
          for (int i = 0; i < 4; i++) {
            escape += stack.pop();
          }
          try {
            Long.parseLong(escape, 16);
          } catch (NumberFormatException ex) {
            throw new LocatedJsonException("Invalid unicode escape", stack, escapeStart);
          }
        } else {
          throw new LocatedJsonException("Invalid escape", stack, escapeStart);
        }
      } else if (c == '"') {
        partitions.add(new Partition(startIndex, stack.getIndex(), ContentType.STRING));
        return;
      }
    }
    throw new LocatedJsonException("Unterminated String", stack, startIndex);
  }

  @Override
  public boolean isNext(final @NotNull StringStack stack) {
    return stack.peek() == '"';
  }
}
