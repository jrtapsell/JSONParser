

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import json.ContentType;
import json.JSON;
import json.JsonException;
import json.LocatedJSONException;
import json.Partition;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author James Tapsell
 */
public class JSONPartitionerTest {

  @Test
  public void testSimpleBoolean() throws JsonException {
    assertOutput("true", new Partition(0, 4, ContentType.BOOLEAN));
  }

  @Test
  public void testSimpleInteger() throws JsonException {
    assertOutput("1234", new Partition(0, 4, ContentType.NUMBER));
  }

  @Test
  public void testSimpleNull() throws JsonException {
    assertOutput("null", new Partition(0, 4, ContentType.NULL));
  }

  @Test
  public void testSimpleFloat() throws JsonException {
    assertOutput("1234.5", new Partition(0, 6, ContentType.NUMBER));
  }

  @Test
  public void testSimpleString() throws JsonException {
    assertOutput("\"Hello\"", new Partition(0, 7, ContentType.STRING));
  }

  @Test
  public void testSimpleObject() throws JsonException {
    assertOutput(
        "{\"a\":\"b\"}",
        new Partition(0, 1, ContentType.OBJECT),
        new Partition(1, 4, ContentType.STRING),
        new Partition(4, 5, ContentType.OBJECT),
        new Partition(5, 8, ContentType.STRING),
        new Partition(8, 9, ContentType.OBJECT));
  }

  @Test(expectedExceptions = {JsonException.class})
  public void testUnendedString() throws JsonException {
    JSON.getPartitions("\"");
  }

  @Test(expectedExceptions = {JsonException.class})
  public void testUnendedObject() throws JsonException {
    JSON.getPartitions("{");
  }

  @Test(expectedExceptions = {JsonException.class})
  public void testUnendedArray() throws JsonException {
    JSON.getPartitions("[");
  }

  @Test(expectedExceptions = {JsonException.class})
  public void testMalformedArray() throws JsonException {
    JSON.getPartitions("[1 2]");
  }

  @Test(expectedExceptions = {JsonException.class})
  public void testMalformedObject() throws JsonException {
    JSON.getPartitions("{\"a\" \"b\"}");
  }

  @Test(expectedExceptions = {JsonException.class})
  public void testMalformedObject2() throws JsonException {
    JSON.getPartitions("{\"a\":\"b\" \"a\":\"b\"}");
  }

  @Test(expectedExceptions = {JsonException.class})
  public void testNotJSON() throws JsonException {
    JSON.getPartitions("z");
  }

  @Test()
  public void testEmptyArray() throws JsonException {
    assertOutput("[]",
        new Partition(0, 2, ContentType.ARRAY));
  }

  @Test()
  public void testEmptyObject() throws JsonException {
    assertOutput("{}",
        new Partition(0, 2, ContentType.OBJECT));
  }

  @Test
  public void testSimpleSpacedObject() throws JsonException {
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
  public void testSimpleArray() throws JsonException {
    assertOutput(
        "[1,2]",
        new Partition(0, 1, ContentType.ARRAY),
        new Partition(1, 2, ContentType.NUMBER),
        new Partition(2, 3, ContentType.ARRAY),
        new Partition(3, 4, ContentType.NUMBER),
        new Partition(4, 5, ContentType.ARRAY));
  }

  @Test
  public void testSpacedArray() throws JsonException {
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

  @Test(expectedExceptions = {LocatedJSONException.class}, expectedExceptionsMessageRegExp = "Exception \\{'Float with 2 dots' at 6 of '1234.5.'\\}")
  public void testBadFloat() throws JsonException {
    JSON.getPartitions("1234.5.");
  }

  @Test(expectedExceptions = {LocatedJSONException.class}, expectedExceptionsMessageRegExp = "Exception \\{'Only a -' at 1 of '-'\\}")
  public void testOnlyNegitive() throws JsonException {
    JSON.getPartitions("-");
  }



  @Test(expectedExceptions = {LocatedJSONException.class}, expectedExceptionsMessageRegExp = "Exception \\{'Only a -' at 1 of '--'\\}")
  public void testDoubleNegitive() throws JsonException {
    JSON.getPartitions("--");
  }

  private void assertOutput(final String input, final Partition... output) throws JsonException {
    final List<Partition> partitions = JSON.getPartitions(input);
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
  public void bigOne() throws IOException, JsonException {
    String s = String.join(System.lineSeparator(), Files.readAllLines(Paths.get("big.json")));
    JSON.getPartitions(s);
  }
}