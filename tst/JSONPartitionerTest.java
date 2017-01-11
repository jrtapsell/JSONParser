

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import json.utils.ContentType;
import json.parser.JSON;
import json.utils.LocatedJSONException;
import json.utils.Partition;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author James Tapsell
 */
public class JSONPartitionerTest {

  @Test
  public void testSimpleBoolean() throws LocatedJSONException {
    assertOutput("true", new Partition(0, 4, ContentType.BOOLEAN));
  }

  @Test(dataProvider = "ints", dataProviderClass = Providers.class)
  public void testSimpleInteger(int value) throws LocatedJSONException {
    final String input = String.valueOf(value);
    assertOutput(input, new Partition(0, input.length(), ContentType.NUMBER));
  }

  @Test
  public void testSimpleNull() throws LocatedJSONException {
    assertOutput("null", new Partition(0, 4, ContentType.NULL));
  }

  @Test(dataProvider = "floats", dataProviderClass = Providers.class)
  public void testSimpleFloats(String in) throws LocatedJSONException {
    assertOutput(in, new Partition(0, in.length(), ContentType.NUMBER));
  }

  @Test
  public void testSimpleString() throws LocatedJSONException {
    assertOutput("\"Hello\"", new Partition(0, 7, ContentType.STRING));
  }

  @Test
  public void testSimpleObject() throws LocatedJSONException {
    assertOutput(
        "{\"a\":\"b\"}",
        new Partition(0, 1, ContentType.OBJECT),
        new Partition(1, 4, ContentType.STRING),
        new Partition(4, 5, ContentType.OBJECT),
        new Partition(5, 8, ContentType.STRING),
        new Partition(8, 9, ContentType.OBJECT));
  }

  @Test
  public void testUnendedString() throws LocatedJSONException {
    assertErrorIndex(() -> JSON.parse("\""), 0);
  }

  @Test
  public void testUnendedObject() throws LocatedJSONException {
    assertErrorIndex(() -> JSON.parse("{"), 0);
  }

  @Test
  public void testUnendedArray() throws LocatedJSONException {
    assertErrorIndex(() -> JSON.parse("["), 0);
  }

  @Test
  public void testMalformedArray() throws LocatedJSONException {
    assertErrorIndex(() -> JSON.parse("[1 2]"), 3);
  }

  @Test
  public void testMalformedObject() throws LocatedJSONException {
    assertErrorIndex(() -> JSON.parse("{\"a\" \"b\"}"), 5);
  }

  @Test (expectedExceptions = LocatedJSONException.class)
  public void testMalformedObject2() throws LocatedJSONException {
    JSON.parse("{\"a\":\"b\" \"a\":\"b\"}");
  }

  @Test (expectedExceptions = LocatedJSONException.class)
  public void testNotJSON() throws LocatedJSONException {
    JSON.parse("z");
  }

  @Test
  public void testEmptyArray() throws LocatedJSONException {
    assertOutput("[]",
        new Partition(0, 2, ContentType.ARRAY));
  }

  @Test
  public void testEmptyObject() throws LocatedJSONException {
    assertOutput("{}",
        new Partition(0, 2, ContentType.OBJECT));
  }

  @Test
  public void testSimpleSpacedObject() throws LocatedJSONException {
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

  @Test
  public void testSimpleArray() throws LocatedJSONException {
    assertOutput(
        "[1,2]",
        new Partition(0, 1, ContentType.ARRAY),
        new Partition(1, 2, ContentType.NUMBER),
        new Partition(2, 3, ContentType.ARRAY),
        new Partition(3, 4, ContentType.NUMBER),
        new Partition(4, 5, ContentType.ARRAY));
  }

  @Test
  public void testSpacedArray() throws LocatedJSONException {
    assertOutput(
        " [ 1 , 2 ] ",
        new Partition(0, 1, ContentType.SPACE),
        new Partition(1, 3, ContentType.ARRAY),
        new Partition(3, 4, ContentType.NUMBER),
        new Partition(4, 7, ContentType.ARRAY),
        new Partition(7, 8, ContentType.NUMBER),
        new Partition(8, 10, ContentType.ARRAY),
        new Partition(10, 11, ContentType.SPACE));
  }

  @Test (
  dataProviderClass = Providers.class,
  dataProvider = "badFloats")
  public void testBadFloats(String value, int failOffset) throws LocatedJSONException {
    assertErrorIndex(() -> JSON.parse(value), failOffset);
  }

  private void assertErrorIndex(ThrowingRunnable<LocatedJSONException> o, int expected) {
    try {
      o.run();
      Assert.fail("Test threw no exception");
    } catch (LocatedJSONException ex) {
      final int actual = ex.getPosition();
      if (actual == expected) {
        return;
      }
      final String message = String.format("Exception has wrong index [Expected: %d, Actual: %d]%n%s", expected, actual, ex.getLocalizedMessage());
      final AssertionError assertionError = new AssertionError(message);
      assertionError.setStackTrace(ex.getStackTrace());
      throw assertionError;
    }
  }

  private void assertOutput(final String input, final Partition... output) throws LocatedJSONException {
    final List<Partition> partitions = JSON.parse(input);
    final List<Partition> expected = Arrays.asList(output);
    if (!expected.equals(partitions)) {
      System.out.println("Expected:");
      System.out.println(String.join("\n", expected.stream().map(p -> p.toString()).collect(Collectors.toList())));
      System.out.println("Actual:");
      System.out.println(String.join("\n", partitions.stream().map(p -> p.toString()).collect(Collectors.toList())));
    }
    Assert.assertEquals(partitions, expected, "Bad result");
  }

  @Test
  public void bigOne() throws IOException, LocatedJSONException {
    final String s = String.join(System.lineSeparator(), Files.readAllLines(Paths.get("big.json")));
    JSON.parse(s);
  }

  private interface ThrowingRunnable<T extends Throwable> {
    public void run() throws T;
  }
}