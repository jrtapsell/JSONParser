package json.types;

import json.JsonTestBase;
import json.utils.ContentType;
import json.utils.LocatedJsonException;
import json.utils.Partition;
import org.testng.annotations.Test;

/**
 * Tests JSON null values.
 *
 * @author James Tapsell
 */
public class JsonNullTest extends JsonTestBase {
  @Test
  public void testSimpleNull() throws LocatedJsonException {
    assertOutput("null", new Partition(0, 4, ContentType.NULL));
  }
}
