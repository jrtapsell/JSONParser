package json.parser;

import java.util.List;
import json.utils.ContentType;
import json.utils.JSONElementFactory;
import json.utils.JSONTreeElement;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import json.utils.StringStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author James Tapsell
 */
public final class JsonStringFactory implements JSONElementFactory {
  private static final JsonStringFactory INSTANCE = new JsonStringFactory();

  public static JsonStringFactory getInstance() {
    return INSTANCE;
  }
  private JsonStringFactory() {}
  @Override
  public void read(final @NotNull List<Partition> partitions, final @NotNull StringStack stack, final @NotNull JSONTreeElement tree) throws LocatedJSONException {
    final int startIndex = stack.getIndex();
    stack.pop();
    boolean escaped = false;
    while (stack.isAvailable()) {
      final char c = stack.pop();
      if (c == '\\') {
        escaped = !escaped;
      } else if (c == '"' && !escaped) {
        partitions.add(new Partition(startIndex, stack.getIndex(), ContentType.STRING));
        return;
      }
    }
    throw new LocatedJSONException("Unterminated String", stack, startIndex);
  }

  @Override
  public boolean isNext(final @NotNull StringStack stack) {
    return stack.peek() == '"';
  }
}
