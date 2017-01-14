package utils;

import json.utils.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests {@link json.utils.ContentType}.
 *
 * <p>Unnecessary, but makes coveralls happier.</p>
 *
 * @author James Tapsell
 */
public class ContentTypeTest {
  @Test
  public void valueOf() {
    Assert.assertEquals(ContentType.valueOf("OBJECT"), ContentType.OBJECT);
  }
}
