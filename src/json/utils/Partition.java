package json.utils;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An element in a JSON string, flattened.
 *
 * @author James Tapsell
 */
public class Partition {
  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }

  public String getName() {
    return name;
  }

  private final int start;
  private final int end;
  private final String name;
  private final ContentType type;

  /**
   * Creates a new partition.
   * @param start
   *  The start position (inclusive)
   * @param end
   *  The end position (exclusive)
   * @param type
   *  The type
   */
  public Partition(final int start, final int end, final @NotNull ContentType type) {
    this.start = start;
    this.end = end;
    name = type.name();
    this.type = type;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("json.utils.Partition{");
    sb.append("start=").append(start);
    sb.append(", end=").append(end);
    sb.append(", name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(final @Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Partition)) {
      return false;
    }
    final Partition partition = (Partition) obj;
    return (start == partition.getStart())
        && (end == partition.getEnd())
        && Objects.equals(name, partition.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end, name);
  }

  public ContentType getType() {
    return type;
  }
}
