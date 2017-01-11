package json.utils;

import java.util.List;

/**
 * @author James Tapsell
 */
public abstract class JSONElement {
  public abstract boolean is(StringStack stack);
  public abstract boolean read(List<Partition> partitions, StringStack stack);
}
