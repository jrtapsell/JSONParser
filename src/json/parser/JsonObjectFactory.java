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
 * Factory for JSONElements of type Object.
 *
 * @author James Tapsell
 */
public final class JsonObjectFactory implements JsonElementFactory {

  private static final JsonObjectFactory INSTANCE = new JsonObjectFactory();
  private static final JsonStringFactory STRING_FACTORY = JsonStringFactory.getInstance();

  public static JsonElementFactory getInstance() {
    return INSTANCE;
  }

  private JsonObjectFactory() {}

  @Override
  public void read(
      final @NotNull List<Partition> partitions,
      final @NotNull StringStack stack,
      final @NotNull JsonTreeElement parent) throws LocatedJsonException {
    int startIndex = stack.getIndex();
    JsonTreeElement jte = new JsonTreeElement(ContentType.OBJECT, startIndex);
    final int origin = startIndex;
    stack.pop();
    while (stack.isAvailable()) {
      stack.seekWhitespace();
      if (stack.peek() == '}') {
        stack.pop();
        partitions.add(new Partition(startIndex, stack.getIndex(), ContentType.OBJECT));
        jte.finalise(stack.getIndex(), stack.getText(jte.getStartIndex(), stack.getIndex()));
        return;
      }
      partitions.add(new Partition(startIndex, stack.getIndex(), ContentType.OBJECT));
      if (stack.peek() != '"') {
        throw new LocatedJsonException("Key must be a string", stack);
      }
      STRING_FACTORY.read(partitions, stack, jte);
      startIndex = stack.getIndex();
      stack.seekWhitespace();
      if (stack.pop() != ':') {
        throw new LocatedJsonException("Missing : ", stack, stack.getIndex() - 1);
      }
      stack.seekWhitespace();
      partitions.add(new Partition(startIndex, stack.getIndex(), ContentType.OBJECT));
      Json.parseAny(partitions, stack, jte);
      startIndex = stack.getIndex();
      stack.seekWhitespace();
      if (stack.peek() != '}' && stack.pop() != ',') {
        throw new LocatedJsonException("Missing , ", stack);
      }
    }
    throw new LocatedJsonException("Unterminated Object", stack, origin);
  }

  @Override
  public boolean isNext(final @NotNull StringStack stack) {
    return stack.peek() == '{';
  }
}
