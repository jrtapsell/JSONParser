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
public final class JSONObjectFactory implements JSONElementFactory {

  private static final JSONObjectFactory INSTANCE = new JSONObjectFactory();
  private static final JSONStringFactory STRING_FACTORY = JSONStringFactory.getInstance();

  public static JSONElementFactory getInstance() {
    return INSTANCE;
  }
  private JSONObjectFactory() {}

  @Override
  public void read(final @NotNull List<Partition> partitions, final @NotNull StringStack stack, final @NotNull JSONTreeElement parent) throws LocatedJSONException {
    int startIndex = stack.getIndex();
    JSONTreeElement jte = new JSONTreeElement(ContentType.OBJECT, startIndex);
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
        throw new LocatedJSONException("Key must be a string", stack);
      }
      STRING_FACTORY.read(partitions, stack, jte);
      startIndex = stack.getIndex();
      stack.seekWhitespace();
      if (stack.pop() != ':') {
        throw new LocatedJSONException("Missing : ", stack, stack.getIndex() - 1);
      }
      stack.seekWhitespace();
      partitions.add(new Partition(startIndex, stack.getIndex(), ContentType.OBJECT));
      Json.parseAny(partitions, stack, jte);
      startIndex = stack.getIndex();
      stack.seekWhitespace();
      if (stack.peek() != '}' && stack.pop() != ',') {
        throw new LocatedJSONException("Missing , ", stack);
      }
    }
    throw new LocatedJSONException("Unterminated Object", stack, origin);
  }

  @Override
  public boolean isNext(final @NotNull StringStack stack) {
    return stack.peek() == '{';
  }
}
