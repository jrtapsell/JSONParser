import json.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author James Tapsell
 */
public class ContentTypeTest {
  @Test
  public void testNames() {
    Assert.assertEquals(ContentType.names(), new String[]{
        ContentType.OBJECT.name(),
        ContentType.ARRAY.name(),
        ContentType.STRING.name(),
        ContentType.NUMBER.name(),
        ContentType.BOOLEAN.name(),
        ContentType.NULL.name(),
        ContentType.SPACE.name()
    }, "Returned names was incorrect");
  }
}
