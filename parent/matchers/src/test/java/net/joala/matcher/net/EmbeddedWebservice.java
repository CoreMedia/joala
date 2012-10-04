package net.joala.matcher.net;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

import static java.net.InetAddress.getByName;

/**
* @since 10/4/12
*/
public class EmbeddedWebservice {
  private static final Logger LOG = LoggerFactory.getLogger(EmbeddedWebservice.class);

  private volatile HttpServer server;
  private final TestHandler testHandler;
  private final URI clientUri;

  public EmbeddedWebservice(final String context, final int port) throws IOException {
    final InetSocketAddress address = new InetSocketAddress(port);
    server = HttpServer.create(address, 0);
    testHandler = new TestHandler();
    server.createContext(context, testHandler);
    server.setExecutor(null);
    server.start();
    clientUri = URI.create(String.format("http://%s:%d%s", getByName(null).getHostName(), port, context));
    LOG.debug("Started embedded webservice at port {} with context {}.", port, context);
  }

  public URI getClientUri() {
    return clientUri;
  }

  public void stop() throws Exception {
    server.stop(1);
    testHandler.clearResponses();
    server = null;
    LOG.debug("Stopped embedded webservice");
  }

  public TestHandler getTestHandler() {
    return testHandler;
  }
}
