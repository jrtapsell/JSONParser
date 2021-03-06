package json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import json.parser.Json;
import json.utils.LocatedJsonException;
import json.utils.StringStack;
import org.testng.annotations.Test;

/**
 * Tests general JSON strings.
 *
 * @author James Tapsell
 */
public class JsonGeneralTest extends JsonTestBase {

  @Test
  public void testNotJson() {
    assertErrorIndex(() -> Json.parseString("NOT Json"), 0);
  }

  @Test
  public void big() throws IOException, LocatedJsonException {
    final String s = getText("big.json");
    Json.parseString(s);
  }

  @Test
  public void example() throws IOException, LocatedJsonException {
    final String s = getText("example.json");
    Json.parseString(s);
  }

  private String getText(final String first) throws IOException {
    return String.join(System.lineSeparator(), Files.readAllLines(Paths.get(first)));
  }

  @Test
  public void testBadTail() throws LocatedJsonException {
    assertErrorIndex(() -> Json.parseString("\"a\"z"), 3);
  }
}