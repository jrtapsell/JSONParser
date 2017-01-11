package json;

import java.util.Objects;

/**
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

  public Partition(int start, int end, ContentType name) {
    this.start = start;
    this.end = end;
    this.name = name.name();
    this.type = name;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("json.Partition{");
    sb.append("start=").append(start);
    sb.append(", end=").append(end);
    sb.append(", name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Partition)) {
      return false;
    }
    Partition partition = (Partition) o;
    return start == partition.start &&
        end == partition.end &&
        Objects.equals(name, partition.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end, name);
  }

  public ContentType getType() {
    return type;
  }
}
