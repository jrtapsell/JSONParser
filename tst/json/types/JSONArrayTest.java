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
public class JSONArrayTest extends JSONTestBase {

  @Test
  public void testUnendedArray() throws LocatedJSONException {
    assertErrorIndex(() -> JSON.parseString("["), 0);
  }

  @Test
  public void testMalformedArray() throws LocatedJSONException {
    assertErrorIndex(() -> JSON.parseString("[1 2]"), 3);
  }

  @Test
  public void testSimpleArray() throws LocatedJSONException {
    assertOutput(
        "[1,2]",
        new Partition(0, 1, ContentType.ARRAY),
        new Partition(1, 2, ContentType.NUMBER),
        new Partition(2, 3, ContentType.ARRAY),
        new Partition(3, 4, ContentType.NUMBER),
        new Partition(4, 5, ContentType.ARRAY));
  }

  @Test
  public void testSpacedArray() throws LocatedJSONException {
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
  public void testEmptyArray() throws LocatedJSONException {
    assertOutput("[]",
        new Partition(0, 2, ContentType.ARRAY));
  }
}
