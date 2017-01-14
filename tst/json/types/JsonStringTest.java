package json.types;

import json.JsonTestBase;
import json.parser.Json;
import json.utils.ContentType;
import json.utils.LocatedJsonException;
import json.utils.Partition;
import org.testng.annotations.Test;

/**
 * Tests JSON Strings.
 *
 * @author James Tapsell
 */
public class JsonStringTest extends JsonTestBase {
  @Test
  public void testSimpleString() throws LocatedJsonException {
    assertOutput("\"Hello\"", new Partition(0, 7, ContentType.STRING));
  }

  @Test
  public void testUnendedString() throws LocatedJsonException {
    assertErrorIndex(() -> Json.parseString("\""), 0);
  }
}
