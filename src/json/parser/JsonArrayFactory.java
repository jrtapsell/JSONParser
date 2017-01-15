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
 * Factory for JSONElements of type Array.
 *
 * @author James Tapsell
 */
public final class JsonArrayFactory extends JsonContainerFactory {
  private static final JsonArrayFactory INSTANCE = new JsonArrayFactory();

  static JsonElementFactory getInstance() {
    return INSTANCE;
  }

  private JsonArrayFactory() {}

  @Override
  public void readRow(
      final @NotNull List<Partition> partitions,
      final @NotNull StringStack stack,
      final @NotNull JsonTreeElement jte) throws LocatedJsonException {
    Json.parseAny(partitions, stack, jte);
  }

  @Override
  public @NotNull ContentType getType() {
    return ContentType.ARRAY;
  }

  @Override
  public char getStart() {
    return '[';
  }

  @Override
  public char getEnd() {
    return ']';
  }

  @Override
  public @NotNull String getName() {
    return "Array";
  }
}
