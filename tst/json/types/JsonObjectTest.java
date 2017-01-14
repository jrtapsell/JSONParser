package json.types;

import json.JsonTestBase;
import json.parser.Json;
import json.utils.ContentType;
import json.utils.LocatedJsonException;
import json.utils.Partition;
import org.testng.annotations.Test;

/**
 * Tests JSON Objects.
 *
 * @author James Tapsell
 */
public class JsonObjectTest extends JsonTestBase {

  @Test
  public void testSimpleObject() throws LocatedJsonException {
    assertOutput(
        "{\"a\":\"b\"}",
        new Partition(0, 1, ContentType.OBJECT),
        new Partition(1, 4, ContentType.STRING),
        new Partition(4, 5, ContentType.OBJECT),
        new Partition(5, 8, ContentType.STRING),
        new Partition(8, 9, ContentType.OBJECT));
  }

  @Test
  public void testUnendedObject() throws LocatedJsonException {
    assertErrorIndex(() -> Json.parseString("{"), 0);
  }

  @Test
  public void testMalformedObject() throws LocatedJsonException {
    assertErrorIndex(() -> Json.parseString("{\"a\" \"b\"}"), 5);
  }

  @Test (expectedExceptions = LocatedJsonException.class)
  public void testMalformedObject2() throws LocatedJsonException {
    Json.parseString("{\"a\":\"b\" \"a\":\"b\"}");
  }

  @Test
  public void testEmptyObject() throws LocatedJsonException {
    assertOutput("{}",
        new Partition(0, 2, ContentType.OBJECT));
  }

  @Test
  public void testSimpleSpacedObject() throws LocatedJsonException {
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
