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
  private static final ContentType TYPE1 = ContentType.OBJECT;
  private static final int END1 = 12;
  private static final int START1 = 0;
  private static final Partition TEST1 = new Partition(START1, END1, TYPE1);


  private static final ContentType TYPE2 = ContentType.ARRAY;
  private static final int END2 = 6;
  private static final int START2 = 5;
  private static final Partition TEST2 = new Partition(START2, END2, TYPE2);

  @Test
  public void testStart() {
    Assert.assertEquals(TEST1.getStart(), START1, "Bad start");
    Assert.assertEquals(TEST2.getStart(), START2, "Bad start");
  }

  @Test
  public void testEnd() {
    Assert.assertEquals(TEST1.getEnd(), END1, "Bad end");
    Assert.assertEquals(TEST2.getEnd(), END2, "Bad end");
  }

  @Test
  public void testType() {
    Assert.assertEquals(TEST1.getType(), TYPE1, "Bad type");
    Assert.assertEquals(TEST2.getType(), TYPE2, "Bad type");
  }

  @Test
  public void testName() {
    Assert.assertEquals(TEST1.getName(), TYPE1.name(), "Bad name");
    Assert.assertEquals(TEST2.getName(), TYPE2.name(), "Bad name");
  }

  @Test
  public void testEquals() {
    Assert.assertNotEquals(TEST1, TEST2, "Dissimilar partitions are equal");
    Assert.assertNotEquals("NULL", TEST1, "Partition equal to String");
    Assert.assertNotEquals(null, TEST1, "Partition equal to null");
    Assert.assertEquals(TEST1, TEST1, "Partition not equal to itself");
  }

  @Test
  public void testHashCode() {
    Assert.assertNotEquals(
        TEST1.hashCode(),
        TEST2.hashCode(),
        "Test partitions have the same hashcode");
  }
}
