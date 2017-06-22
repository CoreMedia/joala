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

package net.joala.net;

import com.google.common.base.MoreObjects;
import com.sun.net.httpserver.HttpServer;
import net.joala.condition.Condition;
import net.joala.condition.DefaultConditionFactory;
import net.joala.expression.AbstractExpression;
import net.joala.expression.ExpressionEvaluationException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;
import static java.net.InetAddress.getByName;
import static net.joala.net.PortUtils.freePort;
import static net.joala.net.StatusCodeResponse.statusCode;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * <p>
 * An webservice meant to be used in tests to simulate a HTTP Server. You can feed
 * it with prepared responses which will then be sent on each request.
 * </p>
 *
 * @since 10/4/12
 */
public class EmbeddedWebservice {
  /**
   * Logging instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(EmbeddedWebservice.class);
  private static final TimeoutImpl SERVER_STARTUP_TIMEOUT = new TimeoutImpl(5L, TimeUnit.SECONDS);

  /**
   * Server to bind to port.
   */
  private volatile HttpServer server;
  /**
   * Http Handler which will provide prepared responses on each request.
   */
  @Nonnull
  private final PreparedResponsesHttpHandler preparedResponsesHttpHandler;
  /**
   * The URI you can send requests to, which will reach this webservice.
   */
  @Nonnull
  private final URI clientUri;
  /**
   * Context the service is bound to.
   */
  @Nonnull
  private final String context;
  /**
   * Port the service is bound to.
   */
  private final int port;

  /**
   * <p>
   * Creates a webservice responding to a random free port on root context.
   * </p>
   *
   * @throws IOException on failure
   */
  public EmbeddedWebservice() throws IOException {
    this("/");
  }

  /**
   * <p>
   * Creates a webservice responding to a random free port on specified context.
   * </p>
   *
   * @param context context, for example {@code /lorem} for lorem-context
   * @throws IOException on failure
   */
  public EmbeddedWebservice(@Nonnull final String context) throws IOException {
    this(context, freePort());
  }

  /**
   * <p>
   * Creates a webservice responding to given port on specified context.
   * </p>
   *
   * @param context context, for example {@code /lorem} for lorem-context
   * @param port    port to bind the service to
   * @throws IOException on failure
   */
  public EmbeddedWebservice(@Nonnull final String context, final int port) throws IOException {
    checkNotNull(context, "Context must not be null. For root context use '/'.");
    try {
      this.context = context;
      this.port = port;
      final InetSocketAddress address = new InetSocketAddress(port);
      server = HttpServer.create(address, 0);
      preparedResponsesHttpHandler = new PreparedResponsesHttpHandlerImpl();
      server.createContext(this.context, preparedResponsesHttpHandler);
      server.setExecutor(null);
      clientUri = URI.create(format("http://%s:%d%s", getByName(null).getHostName(), this.port, context));
    } catch (IOException e) {
      throw new IOException("Failed to prepare embedded webservice for " + context + ':' + port, e);
    }
    LOG.info("Embedded Webservice ready to start for " + context + ':' + port);
  }

  /**
   * <p>
   * Get the URI which where to send requests to, to reach this service.
   * </p>
   *
   * @return client URI
   */
  @Nonnull
  public URI getClientUri() {
    return clientUri;
  }

  /**
   * Start the webservice.
   */
  public void start() {
    checkState(server != null, "Server cannot be restarted.");
    server.start();
    preparedResponsesHttpHandler.feedResponses(statusCode(HttpURLConnection.HTTP_OK));
    final DefaultHttpClient httpClient = getStartupHttpClient();
    try {
      getStartupCondition(httpClient).assumeThat(equalTo(HttpURLConnection.HTTP_OK));
    } finally {
      httpClient.getConnectionManager().shutdown();
    }
    LOG.info("Started embedded webservice at port {} with context {}.", port, context);
  }

  private Condition<Integer> getStartupCondition(final DefaultHttpClient httpClient) {
    return new DefaultConditionFactory(SERVER_STARTUP_TIMEOUT).condition(new ServerStartupStatusCodeExpression(httpClient));
  }

  private DefaultHttpClient getStartupHttpClient() {
    final DefaultHttpClient httpClient = new DefaultHttpClient();
    final HttpParams httpParams = httpClient.getParams();
    HttpConnectionParams.setConnectionTimeout(httpParams, (int) SERVER_STARTUP_TIMEOUT.in(TimeUnit.MILLISECONDS));
    HttpConnectionParams.setSoTimeout(httpParams, (int) SERVER_STARTUP_TIMEOUT.in(TimeUnit.MILLISECONDS));
    return httpClient;
  }

  /**
   * Stop the webservice.
   */
  public void stop() {
    server.stop(1);
    preparedResponsesHttpHandler.clearResponses();
    server = null;
    LOG.info("Stopped embedded webservice");
  }

  /**
   * <p>
   * Get handler to control response flow.
   * </p>
   *
   * @return handler to provide responses
   */
  @Nonnull
  public PreparedResponsesHttpHandler getHttpHandler() {
    return preparedResponsesHttpHandler;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
            .add("clientUri", clientUri)
            .add("server", server)
            .add("preparedResponsesHttpHandler", preparedResponsesHttpHandler)
            .add("context", context)
            .add("port", port)
            .toString();
  }

  private class ServerStartupStatusCodeExpression extends AbstractExpression<Integer> {
    private final DefaultHttpClient httpClient;

    private ServerStartupStatusCodeExpression(final DefaultHttpClient httpClient) {
      this.httpClient = httpClient;
    }

    @Override
    public Integer get() {
      final HttpUriRequest httpHead = new HttpHead(clientUri);
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
        throw new ExpressionEvaluationException(format("Failure reading from URI %s.", clientUri), e);
      }
    }
  }
}
