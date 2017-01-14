package json.utils;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * @author James Tapsell
 */
public interface JSONElementFactory {
  boolean isNext(@NotNull StringStack stack);
  void read(@NotNull List<Partition> partitions, @NotNull StringStack stack, @NotNull JSONTreeElement parent) throws LocatedJSONException;
}
