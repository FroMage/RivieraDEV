package util;

public class StringUtils {

  public static boolean isNotBlank(String str) {
    return str != null && str.trim().length() > 0;
  }

}
