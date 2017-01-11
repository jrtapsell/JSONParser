package json.types;

import json.JSONTestBase;
import json.parser.JSON;
import json.utils.ContentType;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import org.testng.annotations.Test;

/**
 * @author James Tapsell
 */
public class JSONStringTest extends JSONTestBase {
  @Test
  public void testSimpleString() throws LocatedJSONException {
    assertOutput("\"Hello\"", new Partition(0, 7, ContentType.STRING));
  }

  @Test
  public void testUnendedString() throws LocatedJSONException {
    assertErrorIndex(() -> JSON.parseString("\""), 0);
  }
}
