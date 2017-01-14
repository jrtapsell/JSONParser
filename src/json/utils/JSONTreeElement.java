package json.utils;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * @author James Tapsell
 */
public class JSONTreeElement {
  private final ContentType type;
  private final List<JSONTreeElement> children;
  private final int startIndex;
  private int endIndex;
  private String text;

  public JSONTreeElement(ContentType type, int startIndex) {
    this.type = type;
    this.startIndex = startIndex;
    children = new ArrayList<>();
  }

  public ContentType getType() {
    return type;
  }

  public List<JSONTreeElement> getChildren() {
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

  public void addChild(@NotNull JSONTreeElement element) {
    children.add(element);
  }

  public void finalise(int endIndex, String text) {
    this.endIndex = endIndex;
    this.text = text;
  }
}
