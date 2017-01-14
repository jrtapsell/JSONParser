package json.utils;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An element in a parsed JSON string, with tree structure intact.
 *
 * @author James Tapsell
 */
public class JsonTreeElement {
  private final ContentType type;
  private final List<JsonTreeElement> children;
  private final int startIndex;
  private int endIndex;
  private String text;

  /**
   * Constructs a new tree element.
   *
   * @param type
   *  The type of the new element
   * @param startIndex
   *  The start index of the tree element
   */
  public JsonTreeElement(
      final @Nullable ContentType type,
      final int startIndex) {
    this.type = type;
    this.startIndex = startIndex;
    children = new ArrayList<>();
  }

  public ContentType getType() {
    return type;
  }

  public List<JsonTreeElement> getChildren() {
    return children;
  }

  public int getStartIndex() {
    return startIndex;
  }

  public int getEndIndex() {
    return endIndex;
  }

  public String getText() {
    return text;
  }

  public void addChild(@NotNull JsonTreeElement element) {
    children.add(element);
  }

  public void finalise(int endIndex, String text) {
    this.endIndex = endIndex;
    this.text = text;
  }
}
