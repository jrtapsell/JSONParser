package json;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import json.parser.Json;
import json.utils.LocatedJsonException;
import json.utils.Partition;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;

/**
 * Base for JSON test classes.
 *
 * @author James Tapsell
 */
public class JsonTestBase {
  protected void assertErrorIndex(
      final @NotNull ThrowingRunnable<LocatedJsonException> o,
      final int expected) {
    try {
      o.run();
      Assert.fail("Test threw no exception, expected a located exception");
    } catch (final LocatedJsonException ex) {
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

  protected void assertOutput(
      final @NotNull String input,
      final @NotNull Partition... output) throws LocatedJsonException {
    final List<Partition> partitions = Json.parseString(input).getKey();
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
