package json.types;

import json.JsonTestBase;
import json.utils.ContentType;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import org.testng.annotations.Test;

/**
 * @author James Tapsell
 */
public class JsonNullTest extends JsonTestBase {
  @Test
  public void testSimpleNull() throws LocatedJSONException {
    assertOutput("null", new Partition(0, 4, ContentType.NULL));
  }
}
