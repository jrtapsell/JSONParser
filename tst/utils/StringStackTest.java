package utils;

import json.utils.StringStack;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests the {@link StringStack}.
 *
 * @author James Tapsell
 */
public class StringStackTest {

  @Test
  public void testToString() {
    final String testString = "abc";
    final StringStack stringStack = new StringStack(testString);
    final String toString = stringStack.toString();
    Assert.assertTrue(toString.contains(testString));
  }

  /**
   * Provides locations to test {@link StringStack#getLine(int)}.
   *
   * @return
   *    An array of [String text, int index, String line]
   */
  @DataProvider
  public Object[][] locationProvider() {
    return new Object[][] {
        {"a", 0, "a"},
        {"a\nb", 2, "b"},
        {"a\nb\nc", 2, "b"}
    };
  }

  @Test
  public void testNoSeek() {
    StringStack ss = new StringStack("sss");
    ss.seekWhitespace();
    Assert.assertEquals(
        ss.getIndex(),
        0,
        "seekWhitespace seeked through non whitespace ");
  }

  @Test
  public void testRaw() {
    final String example = "Example";
    StringStack ss = new StringStack(example);
    Assert.assertEquals(ss.getRaw(), example, "getRaw returned wrong value");
  }

  @Test(expectedExceptions = AssertionError.class)
  public void testBadLocationHigh() {
    StringStack ss = new StringStack("a");
    ss.getLine(9);
  }

  @Test(expectedExceptions = AssertionError.class)
  public void testBadLocationLow() {
    StringStack ss = new StringStack("a");
    ss.getLine(-1);
  }

  @Test(dataProvider = "locationProvider")
  public void testLocation(String text, int index, String expectedLine) {
    StringStack ss = new StringStack(text);
    Assert.assertEquals(
        ss.getLine(index),
        expectedLine,
        "Bad line number"
    );
  }
}
