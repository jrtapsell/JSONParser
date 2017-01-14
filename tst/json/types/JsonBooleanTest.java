package json.types;

import json.JsonTestBase;
import json.utils.ContentType;
import json.utils.LocatedJsonException;
import json.utils.Partition;
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

  @Test(dataProvider = "goodBooleans")
  public void testSimpleBoolean(final String test) throws LocatedJsonException {
    assertOutput(test, new Partition(0, test.length(), ContentType.BOOLEAN));
  }
}
