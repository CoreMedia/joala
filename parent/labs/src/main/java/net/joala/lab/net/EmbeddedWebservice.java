package net.joala.lab.net;

import com.google.common.base.Objects;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

import static java.net.InetAddress.getByName;
import static net.joala.lab.net.PortUtils.freePort;

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

  /**
   * Server to bind to port.
   */
  private volatile HttpServer server;
  /**
   * Http Handler which will provide prepared responses on each request.
   */
  private final PreparedResponsesHttpHandler preparedResponsesHttpHandler;
  /**
   * The URI you can send requests to, which will reach this webservice.
   */
  private final URI clientUri;
  /**
   * Context the service is bound to.
   */
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
  public EmbeddedWebservice(final String context) throws IOException {
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
  public EmbeddedWebservice(final String context, final int port) throws IOException {
    this.context = context;
    this.port = port;
    final InetSocketAddress address = new InetSocketAddress(port);
    server = HttpServer.create(address, 0);
    preparedResponsesHttpHandler = new PreparedResponsesHttpHandlerImpl();
    server.createContext(this.context, preparedResponsesHttpHandler);
    server.setExecutor(null);
    clientUri = URI.create(String.format("http://%s:%d%s", getByName(null).getHostName(), this.port, context));
  }

  /**
   * <p>
   * Get the URI which where to send requests to, to reach this service.
   * </p>
   *
   * @return client URI
   */
  public URI getClientUri() {
    return clientUri;
  }

  /**
   * Start the webservice.
   */
  public void start() {
    server.start();
    LOG.debug("Started embedded webservice at port {} with context {}.", port, context);
  }

  /**
   * Stop the webservice.
   */
  public void stop() {
    server.stop(1);
    preparedResponsesHttpHandler.clearResponses();
    server = null;
    LOG.debug("Stopped embedded webservice");
  }

  /**
   * <p>
   * Get handler to control response flow.
   * </p>
   *
   * @return handler to provide responses
   */
  public PreparedResponsesHttpHandler getHttpHandler() {
    return preparedResponsesHttpHandler;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
            .add("clientUri", clientUri)
            .add("server", server)
            .add("preparedResponsesHttpHandler", preparedResponsesHttpHandler)
            .add("context", context)
            .add("port", port)
            .toString();
  }
}
