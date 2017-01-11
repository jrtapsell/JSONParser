package json.parser;

import java.util.List;
import json.utils.ContentType;
import json.utils.JSONElementFactory;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import json.utils.StringStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author James Tapsell
 */
public final class JSONArrayFactory implements JSONElementFactory {
  private static final JSONArrayFactory INSTANCE = new JSONArrayFactory();

  static JSONElementFactory getInstance() {
    return INSTANCE;
  }
  private JSONArrayFactory() {}

  @Override
  public void read(final @NotNull List<Partition> partitions, final @NotNull StringStack stack) throws LocatedJSONException {
    int startIndex = stack.getIndex();
    final int origin = startIndex;
    stack.pop();
    while (stack.isAvailable()) {
      stack.seekWhitespace();
      if (stack.peek() == ']') {
        stack.pop();
        partitions.add(new Partition(startIndex, stack.getIndex(), ContentType.ARRAY));
        return;
      }
      partitions.add(new Partition(startIndex, stack.getIndex(), ContentType.ARRAY));
      JSON.parseAny(partitions, stack);
      startIndex = stack.getIndex();
      stack.seekWhitespace();
      if (stack.peek() != ']' && stack.pop() != ',') {
        throw new LocatedJSONException("Missing comma", stack, stack.getIndex() - 1);
      }
    }
    throw new LocatedJSONException("Unterminated Array", stack, origin);
  }

  @Override
  public boolean isNext(final @NotNull StringStack stack) {
    return stack.peek() == '[';
  }
}
