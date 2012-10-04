package net.joala.expression.net;

import net.joala.expression.AbstractExpression;
import net.joala.expression.ExpressionEvaluationException;
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

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;
import static net.joala.matcher.net.KnownHost.knownHost;

/**
 * <p>
 * Matcher to validate response code received from given URI.
 * </p>
 *
 * @since 10/2/12
 */
public class UriStatusCodeExpression extends AbstractExpression<Integer> {
  /**
   * Default timeout to use if unspecified.
   */
  private static final Timeout DEFAULT_TIMEOUT = new TimeoutImpl(10, TimeUnit.SECONDS);

  /**
   * Timeout for connection.
   */
  private final Timeout timeout;
  private final URI uri;

  /**
   * <p>
   * Constructor with target URI. Default timeout is used.
   * </p>
   *
   * @param uri URI to get the status code from
   */
  public UriStatusCodeExpression(@Nonnull final URI uri) {
    this(DEFAULT_TIMEOUT, uri);
  }

  /**
   * <p>
   * Constructor with target URI and connection timeout.
   * </p>
   *
   * @param timeout the connection timeout
   * @param uri     URI to get the status code from
   */
  public UriStatusCodeExpression(@Nonnull final Timeout timeout, @Nonnull final URI uri) {
    super(format("Retrieve status code from URI %s", uri));
    this.timeout = timeout;
    this.uri = uri;
  }

  @Override
  public Integer get() {
    final String host = uri.getHost();
    checkState(knownHost().matches(host), "Host %s from URI %s is unknown.", host, uri);
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
        return statusCode;
      } catch (IOException e) {
        throw new ExpressionEvaluationException(format("Failure reading from URI %s.", uri), e);
      }
    } finally {
      httpClient.getConnectionManager().shutdown();
    }
  }

}
