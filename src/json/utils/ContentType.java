package json.utils;

import org.jetbrains.annotations.Contract;

/**
 * @author James Tapsell
 */
public enum ContentType {
  OBJECT, ARRAY, STRING, NUMBER, BOOLEAN, NULL, SPACE;

  @Contract(" ->  !null")
  public static String[] names() {
    final ContentType[] values = values();
    final int length = values.length;
    final String[] names = new String[length];

    for (int i = 0; i < length; i++) {
      names[i] = values[i].name();
    }

    return names;
  }
}
