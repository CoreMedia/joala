package net.joala.matcher.net;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.joala.time.Timeout;
import net.joala.time.TimeoutImpl;
import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkState;
import static java.net.InetAddress.getByName;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * <p>
 * Tests {@link UriHttpStatusCode}.
 * </p>
 *
 * @since 10/2/12
 */
public class UriHttpStatusCodeTest {
  private static final Logger LOG = LoggerFactory.getLogger(UriHttpStatusCodeTest.class);

  private static EmbeddedWebservice webservice;
  private static final int SOME_PORT = 12345;
  private static final long TIMED_OUT_RESPONSE_DELAY_MILLIS = 5L;
  private static final Timeout FAST_TIMEOUT = new TimeoutImpl(1L, TimeUnit.MILLISECONDS);

  @BeforeClass
  public static void setUpClass() throws Exception {
    webservice = new EmbeddedWebservice("/" + randomAlphabetic(5), getFreePort());
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    webservice.stop();
  }

  @Before
  public void setUp() throws Exception {
    webservice.getTestHandler().clearResponses();
  }

  @After
  public void tearDown() throws Exception {
    webservice.getTestHandler().clearResponses();
  }

  @Test
  public void unknownHosts_should_fail_without_request() throws Exception {
    final URI clientUri = URI.create(String.format("http://%s:%d/", randomAlphabetic(10), SOME_PORT));
    assertThat(clientUri, not(UriHttpStatusCode.httpStatusCodeSuccess()));
  }

  @Test
  public void httpStatusCodeSuccess_should_work_for_HTTP_OK() throws Exception {
    webservice.getTestHandler().feedResponses(new TestResponse(HttpURLConnection.HTTP_OK, randomAlphabetic(10)));
    assertThat(webservice.getClientUri(), UriHttpStatusCode.httpStatusCodeSuccess());
    assertEquals("All fed responses should have been read.", 0, webservice.getTestHandler().availableResponses());
  }

  @Test
  public void httpStatusCodeSuccess_should_fail_for_HTTP_NOT_FOUND() throws Exception {
    webservice.getTestHandler().feedResponses(new TestResponse(HttpURLConnection.HTTP_NOT_FOUND, randomAlphabetic(10)));
    assertThat(webservice.getClientUri(), not(UriHttpStatusCode.httpStatusCodeSuccess()));
    assertEquals("All fed responses should have been read.", 0, webservice.getTestHandler().availableResponses());
  }

  @Test
  public void httpStatusCodeIn_should_work_for_response_match() throws Exception {
    webservice.getTestHandler().feedResponses(new TestResponse(HttpURLConnection.HTTP_OK, randomAlphabetic(10)));
    assertThat(webservice.getClientUri(), UriHttpStatusCode.httpStatusCodeIn(HttpURLConnection.HTTP_OK));
    assertEquals("All fed responses should have been read.", 0, webservice.getTestHandler().availableResponses());
  }

  @Test
  public void httpStatusCodeIn_should_work_for_one_of_responses_match() throws Exception {
    webservice.getTestHandler().feedResponses(new TestResponse(HttpURLConnection.HTTP_OK, randomAlphabetic(10)));
    assertThat(webservice.getClientUri(), UriHttpStatusCode.httpStatusCodeIn(HttpURLConnection.HTTP_ACCEPTED, HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_NO_CONTENT));
    assertEquals("All fed responses should have been read.", 0, webservice.getTestHandler().availableResponses());
  }

  @Test
  public void httpStatusCodeIn_should_fail_for_response_mismatch() throws Exception {
    webservice.getTestHandler().feedResponses(new TestResponse(HttpURLConnection.HTTP_NOT_FOUND, randomAlphabetic(10)));
    assertThat(webservice.getClientUri(), not(UriHttpStatusCode.httpStatusCodeIn(HttpURLConnection.HTTP_OK)));
    assertEquals("All fed responses should have been read.", 0, webservice.getTestHandler().availableResponses());
  }

  @Test
  public void ping_should_fail_for_unbound_port() throws Exception {
    final URI clientUri = URI.create(String.format("http://%s:%d/", getByName(null).getHostName(), getFreePort()));
    assertThat(clientUri, not(UriHttpStatusCode.httpStatusCodeIn(HttpURLConnection.HTTP_OK)));
  }

  @Test
  public void httpStatusCodeIn_ping_should_fail_for_response_not_provided_within_timeout() throws Exception {
    webservice.getTestHandler().feedResponses(new DelayedTestResponse(TIMED_OUT_RESPONSE_DELAY_MILLIS, HttpURLConnection.HTTP_OK, randomAlphabetic(10)));
    assertThat(webservice.getClientUri(), not(UriHttpStatusCode.httpStatusCodeIn(FAST_TIMEOUT, HttpURLConnection.HTTP_OK)));
  }

  @Test
  public void httpStatusCodeSuccess_ping_should_fail_for_response_not_provided_within_timeout() throws Exception {
    webservice.getTestHandler().feedResponses(new DelayedTestResponse(TIMED_OUT_RESPONSE_DELAY_MILLIS, HttpURLConnection.HTTP_OK, randomAlphabetic(10)));
    assertThat(webservice.getClientUri(), not(UriHttpStatusCode.httpStatusCodeSuccess(FAST_TIMEOUT)));
  }

  @Test
  public void description_should_contain_valid_response_codes() throws Exception {
    final Random random = new Random();
    final int[] codes = {random.nextInt(), random.nextInt()};
    final SelfDescribing matcher = new UriHttpStatusCode(codes);
    final Description description = new StringDescription();
    matcher.describeTo(description);
    assertThat(description.toString(), allOf(containsString(String.valueOf(codes[0])), containsString(String.valueOf(codes[1]))));
  }

  private static int getFreePort() throws IOException {
    final ServerSocket server = new ServerSocket(0);
    try {
      return server.getLocalPort();
    } finally {
      server.close();
    }
  }

  private static class EmbeddedWebservice {
    private volatile HttpServer server;
    private final TestHandler testHandler;
    private final URI clientUri;

    private EmbeddedWebservice(final String context, final int port) throws IOException {
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

    private void stop() throws Exception {
      server.stop(1);
      testHandler.clearResponses();
      server = null;
      LOG.debug("Stopped embedded webservice");
    }

    public TestHandler getTestHandler() {
      return testHandler;
    }
  }

  private static class TestHandler implements HttpHandler {
    private final Deque<TestResponse> responses = new LinkedList<TestResponse>();

    private void feedResponses(final TestResponse... responses) {
      this.responses.addAll(Arrays.asList(responses));
    }

    private void clearResponses() {
      this.responses.clear();
    }

    private int availableResponses() {
      return responses.size();
    }

    @Override
    public void handle(final HttpExchange exchange) throws IOException {
      checkState(!responses.isEmpty(), "No responses available anymore.");
      responses.pollFirst().write(exchange);
    }
  }

  private static class TestResponse {
    private final int responseCode;
    private final String responseBody;

    private TestResponse(final int responseCode, final String responseBody) {
      this.responseCode = responseCode;
      this.responseBody = responseBody;
    }

    protected void write(final HttpExchange exchange) throws IOException {
      exchange.sendResponseHeaders(responseCode, responseBody.length());

      // need GET params here

      final OutputStream os = exchange.getResponseBody();
      try {
        os.write(responseBody.getBytes());
      } finally {
        os.close();
      }
    }
  }

  private static class DelayedTestResponse extends TestResponse {
    private final long delayMillis;

    private DelayedTestResponse(final long delayMillis, final int responseCode, final String responseBody) {
      super(responseCode, responseBody);
      this.delayMillis = delayMillis;
    }

    @Override
    protected void write(final HttpExchange exchange) throws IOException {
      try {
        Thread.sleep(delayMillis);
      } catch (InterruptedException ignored) {
      }
      super.write(exchange);
    }
  }
}
