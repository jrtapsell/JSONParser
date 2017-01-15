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
 * Superclass for container types [Array and Object].
 *
 * @author James Tapsell
 */
public abstract class JsonContainerFactory implements JsonElementFactory {

  @Override
  public void read(
      final @NotNull List<Partition> partitions,
      final @NotNull StringStack stack,
      final @NotNull JsonTreeElement parent) throws LocatedJsonException {
    int startIndex = stack.getIndex();
    final JsonTreeElement jte = new JsonTreeElement(getType(), startIndex);
    final int origin = startIndex;
    stack.pop();
    while (stack.isAvailable()) {
      stack.seekWhitespace();
      if (stack.peek() == getEnd()) {
        finalise(partitions, stack, startIndex, jte);
        return;
      }
      partitions.add(new Partition(startIndex, stack.getIndex(), getType()));
      readRow(partitions, stack, jte);
      startIndex = stack.getIndex();
      stack.seekWhitespace();
      if (stack.peek() != getEnd() && stack.pop() != ',') {
        throw new LocatedJsonException("Missing , ", stack , stack.getIndex() - 1);
      }
    }
    throw new LocatedJsonException("Unterminated " + getName(), stack, origin);
  }


  /**
   * Reads a single row [pair for objectm element for array.
   *
   * @param partitions
   *  The partitions to add contained elements to
   * @param stack
   *  The stack to read from
   * @param jte
   *  The tree to build on
   * @throws LocatedJsonException
   *  If the JSON is invalid
   */
  public abstract void readRow(
      final @NotNull List<Partition> partitions,
      final @NotNull StringStack stack,
      final @NotNull JsonTreeElement jte) throws LocatedJsonException;

  public  abstract @NotNull ContentType getType();

  private void finalise(
      final @NotNull Collection<Partition> partitions,
      final @NotNull StringStack stack,
      final int startIndex,
      final JsonTreeElement jte) {
    stack.pop();
    partitions.add(new Partition(startIndex, stack.getIndex(), getType()));
    jte.finalise(stack.getIndex(), stack.getText(jte.getStartIndex(), stack.getIndex()));
  }

  @Override
  public boolean isNext(final @NotNull StringStack stack) {
    return stack.peek() == getStart();
  }

  public abstract char getStart();

  public abstract char getEnd();

  public abstract @NotNull String getName();
}
