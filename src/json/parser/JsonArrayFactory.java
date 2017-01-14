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
public final class JsonArrayFactory implements JSONElementFactory {
  private static final JsonArrayFactory INSTANCE = new JsonArrayFactory();

  static JSONElementFactory getInstance() {
    return INSTANCE;
  }
  private JsonArrayFactory() {}

  @Override
  public void read(final @NotNull List<Partition> partitions, final @NotNull StringStack stack, final @NotNull JSONTreeElement parent) throws LocatedJSONException {
    int startIndex = stack.getIndex();
    final JSONTreeElement jte = new JSONTreeElement(ContentType.ARRAY, startIndex);
    parent.addChild(jte);
    final int origin = startIndex;
    stack.pop();
    while (stack.isAvailable()) {
      stack.seekWhitespace();
      if (stack.peek() == ']') {
        stack.pop();
        partitions.add(new Partition(startIndex, stack.getIndex(), ContentType.ARRAY));
        jte.finalise(stack.getIndex(), stack.getText(jte.getStartIndex(), stack.getIndex()));
        return;
      }
      partitions.add(new Partition(startIndex, stack.getIndex(), ContentType.ARRAY));
      Json.parseAny(partitions, stack, jte);
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
