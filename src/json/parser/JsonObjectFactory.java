package json.parser;

import java.util.Collection;
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
public final class JsonObjectFactory extends JsonContainerFactory {

  private static final JsonObjectFactory INSTANCE = new JsonObjectFactory();
  private static final JsonStringFactory STRING_FACTORY = JsonStringFactory.getInstance();

  public static JsonElementFactory getInstance() {
    return INSTANCE;
  }

  private JsonObjectFactory() {}

  @Override
  public char getStart() {
    return '{';
  }

  @Override
  public char getEnd() {
    return '}';
  }


  @Override
  public @NotNull String getName() {
    return "Object";
  }

  @Override
  public void readRow(
      final @NotNull List<Partition> partitions,
      final @NotNull StringStack stack,
      final @NotNull JsonTreeElement jte) throws LocatedJsonException {
    int startIndex;
    if (stack.peek() != '"') {
      throw new LocatedJsonException("Key must be a string", stack);
    }
    JsonStringFactory.getInstance().read(partitions, stack, jte);
    startIndex = stack.getIndex();
    stack.seekWhitespace();
    if (stack.pop() != ':') {
      throw new LocatedJsonException("Missing : ", stack, stack.getIndex() - 1);
    }
    stack.seekWhitespace();
    partitions.add(new Partition(startIndex, stack.getIndex(), getType()));
    Json.parseAny(partitions, stack, jte);
  }

  @Override
  public @NotNull ContentType getType() {
    return ContentType.OBJECT;
  }
}
