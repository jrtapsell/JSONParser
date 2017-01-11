package json;

/**
 * @author James Tapsell
 */
public class LocatedJSONException extends JsonException {
  int position;
  public LocatedJSONException(String s, StringStack stack) {
    super(String.format("Exception {'%s' at %d of '%s'}", s, stack.getIndex(), stack.getRaw()));
    this.position = position;
  }
}
