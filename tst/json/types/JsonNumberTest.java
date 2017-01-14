package json.types;

import json.JsonTestBase;
import json.parser.Json;
import json.utils.ContentType;
import json.utils.LocatedJsonException;
import json.utils.Partition;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests JSON Numbers.
 *
 * @author James Tapsell
 */
public class JsonNumberTest extends JsonTestBase {

  private static final Object[][] BAD_FLOATS = {
      {"-1.1.", 4},
      {"--99", 1},
      {"99-", 2},
      {"1.1.1", 3},
      {"-1-1", 2}
  };
  private static final Object[][] GOOD_FLOATS = {
      {"1.000"},
      {"-1.000"},
      {".999"},
      {"-.999"}
  };
  private static final Object[][] GOOD_INTS = {
      {1},
      {999},
      {10002},
      {-9}
  };

  @DataProvider
  public static Object[][] badFloats() {
    return BAD_FLOATS;
  }

  @DataProvider
  public static Object[][] goodFloats() {
    return GOOD_FLOATS;
  }

  @DataProvider
  public static Object[][] goodInts() {
    return GOOD_INTS;
  }

  @Test (dataProvider = "goodInts")
  public void testSimpleInteger(final int value) throws LocatedJsonException {
    final String input = String.valueOf(value);
    assertOutput(input, new Partition(0, input.length(), ContentType.NUMBER));
  }

  @Test(dataProvider = "goodFloats")
  public void testSimpleFloats(final String in) throws LocatedJsonException {
    assertOutput(in, new Partition(0, in.length(), ContentType.NUMBER));
  }

  @Test (dataProvider = "badFloats")
  public void testBadFloats(final String value, final int failOffset) throws LocatedJsonException {
    assertErrorIndex(() -> Json.parseString(value), failOffset);
  }
}
