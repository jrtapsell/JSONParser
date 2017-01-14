package json.parser;

import java.util.List;
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

  @Override
  public void read(
      final @NotNull List<Partition> partitions,
      final @NotNull StringStack stack,
      final @NotNull JsonTreeElement tree) throws LocatedJsonException {
    final int startIndex = stack.getIndex();
    stack.pop();
    while (stack.isAvailable()) {
      final char c = stack.pop();
      if (c == '\\') {
        char escaped = stack.pop();
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
