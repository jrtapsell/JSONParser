import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import json.parser.JSON;
import json.utils.LocatedJSONException;
import json.utils.Partition;

/**
 * @author James Tapsell
 */
final class HtmlJsonTest {

  public static final String LINE_SEPARATOR = System.lineSeparator();

  private HtmlJsonTest() {
  }

  public static void main(final String... args) throws LocatedJSONException, IOException {
    final String test = String.join(LINE_SEPARATOR, Files.readAllLines(Paths.get("big.json")));
    final List<Partition> paa = part(test);
    try (PrintStream ps = new PrintStream(new FileOutputStream("out.html"))) {
      for (final Partition p : paa) {
        final String color;
        switch (p.getType()) {
          case ARRAY:
            color = "red";
            break;
          case BOOLEAN:
            color = "blue";
            break;
          case NULL:
            color = "brown";
            break;
          case NUMBER:
            color = "purple";
            break;
          case OBJECT:
            color = "orange";
            break;
          case STRING:
            color = "green";
            break;
          case SPACE:
            color = "black";
            break;
          default:
            color = "pink";
            break;
        }
        ps.printf("<span style='color:%s'>%s</span>", color, getText(test, p));
      }
    }
  }

  private static List<Partition> part(final String test) throws LocatedJSONException {
    final long n = System.nanoTime();
    final List<Partition> partitions = JSON.parseString(test);
    final long x = System.nanoTime() - n;
    System.err.printf("Parsing took %d.%dms%n", x / 1000000L, x % 10000000L);
    return partitions;
  }

  private static String getText(final String test, final Partition p) {
    String substring = test.substring(p.getStart(), p.getEnd());
    substring = substring.replaceAll(LINE_SEPARATOR, "<br>");
    substring = substring.replaceAll(" ", "&nbsp;");
    return substring;
  }
}
