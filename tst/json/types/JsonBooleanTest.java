package json.types;

import java.util.ArrayList;
import java.util.List;
import json.JsonTestBase;
import json.parser.Json;
import json.parser.JsonKeywordFactory;
import json.utils.ContentType;
import json.utils.JsonTreeElement;
import json.utils.LocatedJsonException;
import json.utils.Partition;
import json.utils.StringStack;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests JSON boolean values.
 *
 * @author James Tapsell
 */
public class JsonBooleanTest extends JsonTestBase {
  @DataProvider
  private static Object[][] goodBooleans() {
    return new Object[][]{
        {"true"},
        {"false"}
    };
  }

  @DataProvider
  private static Object[][] badBooleans() {
    return new Object[][]{
        {"True"},
        {"False"}
    };
  }

  @Test(dataProvider = "goodBooleans")
  public void testSimpleBoolean(final String test) throws LocatedJsonException {
    assertOutput(test, new Partition(0, test.length(), ContentType.BOOLEAN));
  }

  @Test(dataProvider = "badBooleans")
  public void testBadBooleans(final String test) throws LocatedJsonException {
    assertErrorIndex(() -> Json.parseString(test), 0);
  }

  @Test(expectedExceptions = LocatedJsonException.class)
  public void badKeyword() throws LocatedJsonException {
    StringStack ss = new StringStack("Keyword");
    List<Partition> parts = new ArrayList<>();
    JsonKeywordFactory.getInstance().read(parts, ss, new JsonTreeElement(ContentType.OBJECT,0));
  }
}
