/*
 * Copyright 2012 CoreMedia AG
 *
 * This file is part of Joala.
 *
 * Joala is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Joala is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Joala.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.joala.expression.library.net;

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
 * @deprecated Will be removed soon.
 */
@SuppressWarnings({"deprecation", "IOResourceOpenedButNotSafelyClosed"})
@Deprecated
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
  @Nonnull
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
