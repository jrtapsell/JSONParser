package json.parser;

import java.util.Collection;
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
public final class JSONKeywordFactory implements JSONElementFactory {
  private static final JSONKeywordFactory INSTANCE = new JSONKeywordFactory();

  public static JSONElementFactory getInstance() {
    return INSTANCE;
  }
  private JSONKeywordFactory() {}

  @Override
  public boolean isNext(final @NotNull StringStack stack) {
    return stack.isNext("true") || stack.isNext("false") || stack.isNext("null");
  }

  @Override
  public void read(final @NotNull List<Partition> partitions, final @NotNull StringStack stack) throws LocatedJSONException{
    if (checkKeyword(partitions, stack, "true", ContentType.BOOLEAN)) {
      return;
    }
    if (checkKeyword(partitions, stack, "null", ContentType.NULL)) {
      return;
    }
    if (checkKeyword(partitions, stack, "false", ContentType.BOOLEAN)) {
      return;
    }
    throw new LocatedJSONException("Unknown keyword", stack);
  }

  private static boolean checkKeyword(final Collection<Partition> partitions, final StringStack ss, final CharSequence keyword, final ContentType type) {
    if (ss.isNext(keyword)) {
      partitions.add(new Partition(ss.getIndex(), ss.getIndex() + keyword.length(), type));
      ss.consume(keyword);
      return true;
    }
    return false;
  }
}
