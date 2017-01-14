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
 * Factory for JSONElements of types Number (int or float).
 *
 * @author James Tapsell
 */
public final class JsonNumberFactory implements JsonElementFactory {
  private static final JsonNumberFactory INSTANCE = new JsonNumberFactory();

  public static JsonElementFactory getInstance() {
    return INSTANCE;
  }

  private JsonNumberFactory() {}

  @Override
  public void read(
      final @NotNull List<Partition> partitions,
      final @NotNull StringStack stack,
      final @NotNull JsonTreeElement parent) throws LocatedJsonException {
    boolean decimal = false; // Only allow a decimal once
    final int startIndex = stack.getIndex();
    if (stack.peek() == '-') {
      stack.pop();
    }
    int read = 0;
    while (stack.isAvailable()
        && (
            Character.isDigit(stack.peek())
            || stack.peek() == '.')) {
      read++;
      final char c = stack.pop();
      if (c == '.') {
        if (decimal) {
          stack.unpop();
          throw new LocatedJsonException("Float with 2 dots", stack);
        } else {
          decimal = true;
        }
      }
    }
    if (read == 0) {
      throw new LocatedJsonException("Only a -", stack);
    }
    final int endIndex = stack.getIndex();
    JsonTreeElement jte = new JsonTreeElement(ContentType.NUMBER, startIndex);
    jte.finalise(endIndex, stack.getText(startIndex, endIndex));
    parent.addChild(jte);
    partitions.add(new Partition(startIndex, endIndex, ContentType.NUMBER));
  }

  @Override
  public boolean isNext(final @NotNull StringStack stack) {
    return Character.isDigit(stack.peek()) || stack.peek() == '.' || stack.peek() == '-';
  }
}
