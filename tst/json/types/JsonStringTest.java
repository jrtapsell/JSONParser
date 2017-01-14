package json.types;

import json.JsonTestBase;
import json.parser.Json;
import json.utils.ContentType;
import json.utils.LocatedJsonException;
import json.utils.Partition;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests JSON Strings.
 *
 * @author James Tapsell
 */
public class JsonStringTest extends JsonTestBase {

  /**
   * Provides valid JSON characters to be escaped.
   *
   * @return
   *  Data provider for a single char argument test
   */
  @DataProvider
  public Object[][] validEscapes() {
    return new Object[][] {
        {"\\"},{ "b"},{ "f"},{ "n"},{"r"},{ "t"}, {"u1234"}, {"u0000"}, {"u000a"}
    };
  }

  /**
   * Provides invalid JSON escapes.
   *
   * @return
   *  Data provider for a single String argument test
   */
  @DataProvider
  public Object[][] invalidEscapes() {
    return new Object[][] {
        {"z"},
        {"u"},
        {"uaaaz"},
        {"u000z"}
    };
  }

  @Test
  public void testSimpleString() throws LocatedJsonException {
    assertOutput("\"Hello\"", new Partition(0, 7, ContentType.STRING));
  }

  @Test
  public void testUnendedString() throws LocatedJsonException {
    assertErrorIndex(() -> Json.parseString("\""), 0);
  }

  @Test
  public void testNestedString() throws LocatedJsonException {
    assertOutput("\"a \\\"b\\\" \"", new Partition(0, 10, ContentType.STRING));
  }

  @Test(dataProvider = "validEscapes")
  public void testValidEscapes(String letter) throws LocatedJsonException {
    final String format = String.format("%c\\%s%c", '"', letter, '"');
    assertOutput(
        format,
        new Partition(0, format.length(), ContentType.STRING));
  }


  @Test(dataProvider = "invalidEscapes")
  public void testInvalidEscape(String text) throws LocatedJsonException {
    assertErrorIndex(() -> {
      final String z = String.format("%c\\%s%c", '"', text, '"');
      Json.parseString(
          z);
    }, 1);
  }

}
