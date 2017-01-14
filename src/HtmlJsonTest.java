import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import json.parser.Json;
import json.utils.LocatedJsonException;
import json.utils.Partition;

/**
 * Tests rendering a Json file to HTML.
 *
 * @author James Tapsell
 */
final class HtmlJsonTest {

  private static final String LINE_SEPARATOR = System.lineSeparator();
  private static final long NS_IN_MS = (long) 1.0e6f;

  private HtmlJsonTest() {
  }

  public static void main(final String... args) throws LocatedJsonException, IOException {
    final String test = String.join(LINE_SEPARATOR, Files.readAllLines(Paths.get("big.json")));
    final List<Partition> paa = part(test);
    try (PrintStream ps = new PrintStream(new FileOutputStream("out.html"))) {
      for (final Partition p : paa) {
        ps.printf("<span style='color:%s'>%s</span>", getColour(p), getText(test, p));
      }
    }
  }

  private static String getColour(final Partition partition) {
    switch (partition.getType()) {
      case ARRAY:
        return "red";
      case BOOLEAN:
        return "blue";
      case NULL:
        return "brown";
      case NUMBER:
        return "purple";
      case OBJECT:
        return "orange";
      case STRING:
        return "green";
      case SPACE:
        return "black";
      default:
        return "pink";
    }
  }

  private static List<Partition> part(final String test) throws LocatedJsonException {
    final long n = System.nanoTime();
    final List<Partition> partitions = Json.parseString(test).getKey();
    final long x = System.nanoTime() - n;
    System.err.printf("Parsing took %d.%dms%n", x / NS_IN_MS, x % NS_IN_MS);
    return partitions;
  }

  private static String getText(final String test, final Partition p) {
    String substring = test.substring(p.getStart(), p.getEnd());
    substring = substring.replaceAll(LINE_SEPARATOR, "<br>");
    substring = substring.replaceAll(" ", "&nbsp;");
    return substring;
  }
}
