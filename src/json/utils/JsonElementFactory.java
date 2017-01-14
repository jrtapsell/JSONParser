package json.utils;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for JSON elements.
 *
 * @author James Tapsell
 */
public interface JsonElementFactory {
  boolean isNext(@NotNull StringStack stack);

  void read(@NotNull List<Partition> partitions,
            @NotNull StringStack stack,
            @NotNull JsonTreeElement parent) throws LocatedJsonException;
}
