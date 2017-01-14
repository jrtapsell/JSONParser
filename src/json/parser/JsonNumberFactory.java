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
public final class JsonNumberFactory implements JSONElementFactory {
  private static final JsonNumberFactory INSTANCE = new JsonNumberFactory();

  public static JSONElementFactory getInstance() {
    return INSTANCE;
  }
  private JsonNumberFactory() {}

  @Override
  public void read(final @NotNull List<Partition> partitions, final @NotNull StringStack stack, final @NotNull JSONTreeElement parent) throws LocatedJSONException {
    boolean decimal = false; // Only allow a decimal once
    final int startIndex = stack.getIndex();
    if (stack.peek() == '-') {
      stack.pop();
    }
    int read = 0;
    while (stack.isAvailable() && (Character.isDigit(stack.peek()) || stack.peek() == '.')) {
      read++;
      final char c = stack.pop();
      if (c == '.') {
        if (decimal) {
          stack.unpop();
          throw new LocatedJSONException("Float with 2 dots", stack);
        } else {
          decimal = true;
        }
      }
    }
    if (read == 0) {
      throw new LocatedJSONException("Only a -", stack);
    }
    final int endIndex = stack.getIndex();
    JSONTreeElement jte = new JSONTreeElement(ContentType.NUMBER, startIndex);
    jte.finalise(endIndex, stack.getText(startIndex, endIndex));
    parent.addChild(jte);
    partitions.add(new Partition(startIndex, endIndex, ContentType.NUMBER));
  }

  @Override
  public boolean isNext(final @NotNull StringStack stack) {
    return Character.isDigit(stack.peek()) || stack.peek() == '.' || stack.peek() == '-';
  }
}
