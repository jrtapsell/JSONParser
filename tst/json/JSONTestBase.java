package json;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import json.parser.JSON;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import org.testng.Assert;

/**
 * @author James Tapsell
 */
public class JSONTestBase {
  protected void assertErrorIndex(final ThrowingRunnable<LocatedJSONException> o, final int expected) {
    try {
      o.run();
      Assert.fail("Test threw no exception");
    } catch (final LocatedJSONException ex) {
      final int actual = ex.getPosition();
      if (actual == expected) {
        return;
      }
      final String message = String.format(
          "Exception has wrong index [Expected: %d, Actual: %d]%n%s",
          expected,
          actual,
          ex.getLocalizedMessage());
      final AssertionError assertionError = new AssertionError(message);
      assertionError.setStackTrace(ex.getStackTrace());
      throw assertionError;
    }
  }

  protected void assertOutput(final String input, final Partition... output) throws LocatedJSONException {
    final List<Partition> partitions = JSON.parseString(input).getKey();
    final List<Partition> expected = Arrays.asList(output);
    if (!expected.equals(partitions)) {
      System.out.println("Expected:");
      System.out.println(stringify(expected));
      System.out.println("Actual:");
      System.out.println(stringify(partitions));
    }
    Assert.assertEquals(partitions, expected, "Bad result");
  }

  private static String stringify(final Collection<?> partitions) {
    final List<String> collect = ((Collection<?>) partitions).stream()
        .map(p -> p.toString())
        .collect(Collectors.toList());
    return String.join(System.lineSeparator(), collect);
  }

  protected interface ThrowingRunnable<T extends Throwable> {
    void run() throws T;
  }
}
