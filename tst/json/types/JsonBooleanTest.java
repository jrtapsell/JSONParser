package json.types;

import json.JsonTestBase;
import json.utils.ContentType;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author James Tapsell
 */
public class JsonBooleanTest extends JsonTestBase {
  @DataProvider
  public static Object[][] goodBooleans() {
    return new Object[][]{
        {"true"},
        {"false"}
    };
  }

  @Test(dataProvider = "goodBooleans")
  public void testSimpleBoolean(final String test) throws LocatedJSONException {
    assertOutput(test, new Partition(0, test.length(), ContentType.BOOLEAN));
  }
}
