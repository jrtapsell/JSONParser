package json.types;

import json.JsonTestBase;
import json.parser.Json;
import json.utils.ContentType;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import org.testng.annotations.Test;

/**
 * @author James Tapsell
 */
public class JsonStringTest extends JsonTestBase {
  @Test
  public void testSimpleString() throws LocatedJSONException {
    assertOutput("\"Hello\"", new Partition(0, 7, ContentType.STRING));
  }

  @Test
  public void testUnendedString() throws LocatedJSONException {
    assertErrorIndex(() -> Json.parseString("\""), 0);
  }
}
