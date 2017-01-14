package json.types;

import json.JsonTestBase;
import json.parser.Json;
import json.utils.ContentType;
import json.utils.LocatedJsonException;
import json.utils.Partition;
import org.testng.annotations.Test;

/**
 * Tests JSON Arrays.
 *
 * @author James Tapsell
 */
public class JsonArrayTest extends JsonTestBase {

  @Test
  public void testUnendedArray() throws LocatedJsonException {
    assertErrorIndex(() -> Json.parseString("["), 0);
  }

  @Test
  public void testMalformedArray() throws LocatedJsonException {
    assertErrorIndex(() -> Json.parseString("[1 2]"), 3);
  }

  @Test
  public void testSimpleArray() throws LocatedJsonException {
    assertOutput(
        "[1,2]",
        new Partition(0, 1, ContentType.ARRAY),
        new Partition(1, 2, ContentType.NUMBER),
        new Partition(2, 3, ContentType.ARRAY),
        new Partition(3, 4, ContentType.NUMBER),
        new Partition(4, 5, ContentType.ARRAY));
  }

  @Test
  public void testSpacedArray() throws LocatedJsonException {
    assertOutput(
        " [ 1 , 2 ] ",
        new Partition(0, 1, ContentType.SPACE),
        new Partition(1, 3, ContentType.ARRAY),
        new Partition(3, 4, ContentType.NUMBER),
        new Partition(4, 7, ContentType.ARRAY),
        new Partition(7, 8, ContentType.NUMBER),
        new Partition(8, 10, ContentType.ARRAY),
        new Partition(10, 11, ContentType.SPACE));
  }

  @Test
  public void testEmptyArray() throws LocatedJsonException {
    assertOutput("[]",
        new Partition(0, 2, ContentType.ARRAY));
  }
}
