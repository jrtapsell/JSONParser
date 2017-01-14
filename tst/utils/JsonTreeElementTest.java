package utils;

import java.util.Collections;
import json.utils.ContentType;
import json.utils.JsonTreeElement;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests {@link JsonTreeElement}.
 *
 * @author James Tapsell
 */
public class JsonTreeElementTest {
  @Test
  public void simpleTreeElement() {
    JsonTreeElement jte = new JsonTreeElement(ContentType.ARRAY, 0);
    jte.finalise(3, "ABC");
    Assert.assertEquals(jte.getType(), ContentType.ARRAY);
    Assert.assertEquals(jte.getChildren(), Collections.emptyList());
    Assert.assertEquals(jte.getText(), "ABC");
    Assert.assertEquals(jte.getStartIndex(), 0);
    Assert.assertEquals(jte.getEndIndex(), 3);
  }
}
