package utils;

import json.utils.ContentType;
import json.utils.Partition;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test JSON partitions.
 *
 * @author James Tapsell
 */
public class PartitionTest {
  private static final Partition TEST1A = new Partition(0, 12, ContentType.OBJECT);
  private static final Partition TEST1B = new Partition(0, 12, ContentType.OBJECT);
  private static final Partition TEST2A = new Partition(5, 6, ContentType.ARRAY);
  private static final Partition TEST2B = new Partition(5, 8, ContentType.ARRAY);
  private static final Partition TEST2C = new Partition(5, 6, ContentType.OBJECT);

  @Test
  public void testStart() {
    Assert.assertEquals(TEST1A.getStart(), 0, "Bad start");
    Assert.assertEquals(TEST2A.getStart(), 5, "Bad start");
  }

  @Test
  public void testEnd() {
    Assert.assertEquals(TEST1A.getEnd(), 12, "Bad end");
    Assert.assertEquals(TEST2A.getEnd(), 6, "Bad end");
  }

  @Test
  public void testType() {
    Assert.assertEquals(TEST1A.getType(), ContentType.OBJECT, "Bad type");
    Assert.assertEquals(TEST2A.getType(), ContentType.ARRAY, "Bad type");
  }

  @Test
  public void testName() {
    Assert.assertEquals(TEST1A.getName(), ContentType.OBJECT.name(), "Bad name");
    Assert.assertEquals(TEST2A.getName(), ContentType.ARRAY.name(), "Bad name");
  }

  @Test
  public void testEquals() {
    Assert.assertNotEquals(TEST1A, TEST2A, "Dissimilar partitions are equal");
    Assert.assertNotEquals("NULL", TEST1A, "Partition equal to String");
    Assert.assertNotEquals(null, TEST1A, "Partition equal to null");
    Assert.assertEquals(TEST1A, TEST1A, "Partition not equal to itself");
    Assert.assertEquals(TEST1A, TEST1B);
    Assert.assertNotEquals(TEST2A, TEST2B);
  }

  @Test
  public void testHashCode() {
    Assert.assertNotEquals(
        TEST1A.hashCode(),
        TEST2A.hashCode(),
        "Test partitions have the same hashcode");
    Assert.assertEquals(TEST1A.hashCode(), TEST1B.hashCode());
    Assert.assertNotEquals(TEST2A, TEST2C);
  }
}
