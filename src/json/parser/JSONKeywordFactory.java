package json.parser;

import java.util.Collection;
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
  public void read(final @NotNull List<Partition> partitions, final @NotNull StringStack stack, final @NotNull JSONTreeElement parent) throws LocatedJSONException{
    if (checkKeyword(partitions, stack, "true", ContentType.BOOLEAN, parent)) {
      return;
    }
    if (checkKeyword(partitions, stack, "null", ContentType.NULL, parent)) {
      return;
    }
    if (checkKeyword(partitions, stack, "false", ContentType.BOOLEAN, parent)) {
      return;
    }
    throw new LocatedJSONException("Unknown keyword", stack);
  }

  private static boolean checkKeyword(
      final @NotNull Collection<Partition> partitions,
      final @NotNull StringStack ss,
      final @NotNull CharSequence keyword,
      final @NotNull ContentType type,
      final @NotNull JSONTreeElement root) {
    if (ss.isNext(keyword)) {
      final int start = ss.getIndex();
      final int end = start + keyword.length();
      JSONTreeElement jte = new JSONTreeElement(type, start);
      jte.finalise(end, ss.getText(start, end));
      partitions.add(new Partition(start, end, type));
      root.addChild(jte);
      ss.consume(keyword);
      return true;
    }
    return false;
  }
}
