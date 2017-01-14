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
public class JsonObjectTest extends JsonTestBase {

  @Test
  public void testSimpleObject() throws LocatedJSONException {
    assertOutput(
        "{\"a\":\"b\"}",
        new Partition(0, 1, ContentType.OBJECT),
        new Partition(1, 4, ContentType.STRING),
        new Partition(4, 5, ContentType.OBJECT),
        new Partition(5, 8, ContentType.STRING),
        new Partition(8, 9, ContentType.OBJECT));
  }

  @Test
  public void testUnendedObject() throws LocatedJSONException {
    assertErrorIndex(() -> Json.parseString("{"), 0);
  }

  @Test
  public void testMalformedObject() throws LocatedJSONException {
    assertErrorIndex(() -> Json.parseString("{\"a\" \"b\"}"), 5);
  }
  @Test (expectedExceptions = LocatedJSONException.class)
  public void testMalformedObject2() throws LocatedJSONException {
    Json.parseString("{\"a\":\"b\" \"a\":\"b\"}");
  }

  @Test
  public void testEmptyObject() throws LocatedJSONException {
    assertOutput("{}",
        new Partition(0, 2, ContentType.OBJECT));
  }

  @Test
  public void testSimpleSpacedObject() throws LocatedJSONException {
    assertOutput(
        " { \"a\" : \"b\" } ",
        new Partition(0, 1, ContentType.SPACE),
        new Partition(1, 3, ContentType.OBJECT),
        new Partition(3, 6, ContentType.STRING),
        new Partition(6, 9, ContentType.OBJECT),
        new Partition(9, 12, ContentType.STRING),
        new Partition(12, 14, ContentType.OBJECT),
        new Partition(14, 15, ContentType.SPACE));
  }


}
