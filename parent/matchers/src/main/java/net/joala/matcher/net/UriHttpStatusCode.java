package net.joala.matcher.net;

import net.joala.time.Timeout;
import net.joala.time.TimeoutImpl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static net.joala.matcher.net.KnownHost.knownHost;

/**
 * <p>
 * Matcher to validate response code received from given URI.
 * </p>
 *
 * @since 10/2/12
 */
public class UriHttpStatusCode extends TypeSafeMatcher<URI> {
  /**
   * Log used for debugging hints. Should not be used above debug level.
   */
  private static final Logger LOG = LoggerFactory.getLogger(UriHttpStatusCode.class);
  /**
   * Default timeout to use if unspecified.
   */
  private static final Timeout DEFAULT_TIMEOUT = new TimeoutImpl(10, TimeUnit.SECONDS);
  /**
   * List of status codes which are regarded as successful (matching 2xx).
   */
  private static final int[] SUCCESS_STATUS_CODES = new int[]{HttpURLConnection.HTTP_OK,
          HttpURLConnection.HTTP_CREATED,
          HttpURLConnection.HTTP_ACCEPTED,
          HttpURLConnection.HTTP_NOT_AUTHORITATIVE,
          HttpURLConnection.HTTP_NO_CONTENT,
          HttpURLConnection.HTTP_RESET,
          HttpURLConnection.HTTP_PARTIAL};

  /**
   * Timeout for connection.
   */
  private final Timeout timeout;
  /**
   * List of expected status codes.
   */
  private final int[] validStatusCodes;

  /**
   * <p>
   * Constructor with specified status code(s). Default timeout is used.
   * </p>
   *
   * @param validStatusCodes list of valid status codes
   */
  public UriHttpStatusCode(final int... validStatusCodes) {
    this(DEFAULT_TIMEOUT, validStatusCodes);
  }

  /**
   * <p>
   * Constructor with specified status code(s) and connection timeout.
   * </p>
   *
   * @param timeout          the connection timeout
   * @param validStatusCodes list of valid status codes
   */
  public UriHttpStatusCode(final Timeout timeout, final int... validStatusCodes) {
    this.timeout = timeout;
    this.validStatusCodes = validStatusCodes;
  }

  @Override
  protected boolean matchesSafely(final URI uri) {
    final String host = uri.getHost();
    if (!knownHost().matches(host)) {
      LOG.debug("Skipping ping to URI {}. Host is unknown.", uri);
      return false;
    }
    final DefaultHttpClient httpClient = new DefaultHttpClient();
    try {
      final HttpParams httpParams = httpClient.getParams();
      HttpConnectionParams.setConnectionTimeout(httpParams, (int) timeout.in(TimeUnit.MILLISECONDS));
      HttpConnectionParams.setSoTimeout(httpParams, (int) timeout.in(TimeUnit.MILLISECONDS));
      final HttpUriRequest httpHead = new HttpHead(uri);
      try {
        final HttpResponse response = httpClient.execute(httpHead);
        final HttpEntity httpEntity = response.getEntity();
        final StatusLine statusLine = response.getStatusLine();
        final int statusCode = statusLine.getStatusCode();
        if (httpEntity != null) {
          EntityUtils.consume(httpEntity);
        }
        return Arrays.binarySearch(validStatusCodes, statusCode) >= 0;
      } catch (IOException e) {
        LOG.debug("Failure reading from URI {}. Assuming that it is not reachable.", uri, e);
        return false;
      }
    } finally {
      httpClient.getConnectionManager().shutdown();
    }
  }

  @Override
  public void describeTo(final Description description) {
    description.appendText("a response with status code in ");
    description.appendValue(validStatusCodes);
  }

  /**
   * <p>
   * Matcher expecting specified status code(s). Default timeout is used.
   * </p>
   *
   * @param validStatusCodes list of valid status codes
   * @return matcher
   */
  @Factory
  public static Matcher<URI> httpStatusCodeIn(final int... validStatusCodes) {
    return new UriHttpStatusCode(validStatusCodes);
  }

  /**
   * <p>
   * Matcher expecting specified status code(s) and using specified connection timeout.
   * </p>
   *
   * @param timeout          the connection timeout
   * @param validStatusCodes list of valid status codes
   * @return matcher
   */
  @Factory
  public static Matcher<URI> httpStatusCodeIn(final Timeout timeout, final int... validStatusCodes) {
    return new UriHttpStatusCode(timeout, validStatusCodes);
  }

  /**
   * <p>
   * Matcher to validate if status code is regarded as successful (status codes matching 2xx). Default timeout is used.
   * </p>
   *
   * @return matcher
   */
  @Factory
  public static Matcher<URI> httpStatusCodeSuccess() {
    return new UriHttpStatusCode(SUCCESS_STATUS_CODES);
  }

  /**
   * <p>
   * Matcher to validate if status code is regarded as successful (status codes matching 2xx).
   * </p>
   *
   * @param timeout the connection timeout
   * @return matcher
   */
  @Factory
  public static Matcher<URI> httpStatusCodeSuccess(final Timeout timeout) {
    return new UriHttpStatusCode(timeout, SUCCESS_STATUS_CODES);
  }
}
