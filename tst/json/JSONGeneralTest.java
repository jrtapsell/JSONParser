package json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import json.parser.JSON;
import json.utils.LocatedJSONException;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author James Tapsell
 */
public class JSONGeneralTest extends JSONTestBase {

  @Test
  public void testNotJSON() {
    assertErrorIndex(() -> JSON.parseString("NOT JSON"), 0);
  }

  @Test
  public void big() throws IOException, LocatedJSONException {
    final String s = getText("big.json");
    JSON.parseString(s);
  }

  @Test
  public void example() throws IOException, LocatedJSONException {
    final String s = getText("example.json");
    JSON.parseString(s);
  }

  private String getText(final String first) throws IOException {
    return String.join(System.lineSeparator(), Files.readAllLines(Paths.get(first)));
  }
}