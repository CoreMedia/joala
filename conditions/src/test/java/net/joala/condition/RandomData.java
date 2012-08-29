package net.joala.condition;

import org.apache.commons.lang3.RandomStringUtils;

import static java.lang.String.format;

/**
 * @since 8/25/12
 */
public final class RandomData {
  private static final int DEFAULT_MAXIMUM_INT = 255;
  private static final int DEFAULT_MINIMUM_INT = 1;

  private RandomData() {
  }

  public static String randomString() {
    return RandomStringUtils.random(randomPositiveInt());
  }

  public static String randomString(final String appendix) {
    return format("%s_%s", RandomStringUtils.random(3), appendix);
  }

  public static int randomPositiveInt() {
    return (int) (Math.random() * (DEFAULT_MAXIMUM_INT - DEFAULT_MINIMUM_INT) + DEFAULT_MINIMUM_INT);
  }

  public static double randomPositiveDouble() {
    return Math.random() * (DEFAULT_MAXIMUM_INT - DEFAULT_MINIMUM_INT) + DEFAULT_MINIMUM_INT;
  }

}
