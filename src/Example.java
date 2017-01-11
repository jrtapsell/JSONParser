import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import json.JSON;
import json.JsonException;
import json.Partition;

/**
 * @author James Tapsell
 */
public class Example {
  public static void main(final String... args) throws JsonException, IOException {
    String test = String.join("\n", Files.readAllLines(Paths.get("big.json")));
    List<Partition> paa = part(test);
    try (PrintStream ps = new PrintStream(new FileOutputStream("out.html"))) {
      for (Partition p : paa) {
        String color;
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

  private static List<Partition> part(String test) throws JsonException {
    long n = System.nanoTime();
    final List<Partition> partitions = JSON.getPartitions(test);
    final long x = System.nanoTime() - n;
    System.err.printf("%d.%dms%n", x % 1000000, x / 10000000);
    return partitions;
  }

  private static String getText(String test, Partition p) {
    String substring = test.substring(p.getStart(), p.getEnd());
    substring = substring.replaceAll("\n", "<br>");
    substring = substring.replaceAll(" ", "&nbsp;");
    return substring;
  }
}
