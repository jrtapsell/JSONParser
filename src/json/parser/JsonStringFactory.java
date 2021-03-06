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
  public static final int HEX = 16;

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
      final int escapeStart = stack.getIndex();
      final char c = stack.pop();
      if (c == '\\') {
        validateEscape(stack, escapeStart);
      } else if (c == '"') {
        partitions.add(new Partition(startIndex, stack.getIndex(), ContentType.STRING));
        return;
      }
    }
    throw new LocatedJsonException("Unterminated String", stack, startIndex);
  }

  private void validateEscape(
      final @NotNull StringStack stack,
      final int escapeStart) throws LocatedJsonException {
    switch (stack.pop()) {
      case '\\':
      case 'b':
      case 'f':
      case 'n':
      case 'r':
      case 't':
      case '"':
        // Do nothing, it's valid
        break;
      case 'u':
        validateUnicode(stack, escapeStart);
        break;
      default:
        throw new LocatedJsonException("Invalid escape", stack, escapeStart);
    }
  }

  private void validateUnicode(
      final @NotNull StringStack stack,
      final int escapeStart) throws LocatedJsonException {
    if (stack.available() < 4) {
      throw new LocatedJsonException("Unicode escape needs 4 digits", stack, escapeStart);
    }
    final String s = stack.getNext(4);
    try {
      Long.parseLong(s, HEX);
    } catch (final NumberFormatException ignored) {
      throw new LocatedJsonException("Invalid unicode escape", stack, escapeStart);
    }
  }

  @Override
  public boolean isNext(final @NotNull StringStack stack) {
    return stack.peek() == '"';
  }
}
