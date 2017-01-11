import org.jetbrains.annotations.Contract;
import org.testng.annotations.DataProvider;

/**
 * @author James Tapsell
 */
public class Providers {

  @Contract (" -> !null")
  @DataProvider
  public static Object[][] floats() {
    return new Object[][]{
        {"1.000"},
        {"-1.000"},
        {".999"},
        {"-.999"}
    };
  }

  @Contract (" -> !null")
  @DataProvider
  public static Object[][] ints() {
    return new Object[][]{
        {1},
        {999},
        {10002},
        {-9}
    };
  }

  @Contract (" -> !null")
  @DataProvider
  public static Object[][] badFloats() {
    return new Object[][]{
        {"-1.1.", 4},
        {"--99", 1},
        {"99-", 2},
        {"1.1.1", 3}
    };
  }
}
