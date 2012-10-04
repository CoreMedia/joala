package net.joala.matcher.net;

import net.joala.lab.net.EmbeddedWebservice;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URI;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.net.InetAddress.getByName;
import static net.joala.lab.net.DelayedResponse.delay;
import static net.joala.lab.net.StatusCodeResponse.statusCode;
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
  private static EmbeddedWebservice webservice;
  private static final int SOME_PORT = 12345;
  private static final long TIMED_OUT_RESPONSE_DELAY_MILLIS = 5L;
  private static final Timeout FAST_TIMEOUT = new TimeoutImpl(1L, TimeUnit.MILLISECONDS);

  @BeforeClass
  public static void setUpClass() throws Exception {
    webservice = new EmbeddedWebservice();
    webservice.start();
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    webservice.stop();
  }

  @Before
  public void setUp() throws Exception {
    webservice.getHttpHandler().clearResponses();
  }

  @After
  public void tearDown() throws Exception {
    webservice.getHttpHandler().clearResponses();
  }

  @Test
  public void unknownHosts_should_fail_without_request() throws Exception {
    final URI clientUri = URI.create(String.format("http://%s:%d/", randomAlphabetic(10), SOME_PORT));
    assertThat(clientUri, not(UriHttpStatusCode.httpStatusCodeSuccess()));
  }

  @Test
  public void httpStatusCodeSuccess_should_work_for_HTTP_OK() throws Exception {
    webservice.getHttpHandler().feedResponses(statusCode(HttpURLConnection.HTTP_OK));
    assertThat(webservice.getClientUri(), UriHttpStatusCode.httpStatusCodeSuccess());
    assertEquals("All fed responses should have been read.", 0, webservice.getHttpHandler().availableResponses());
  }

  @Test
  public void httpStatusCodeSuccess_should_fail_for_HTTP_NOT_FOUND() throws Exception {
    webservice.getHttpHandler().feedResponses(statusCode(HttpURLConnection.HTTP_NOT_FOUND));
    assertThat(webservice.getClientUri(), not(UriHttpStatusCode.httpStatusCodeSuccess()));
    assertEquals("All fed responses should have been read.", 0, webservice.getHttpHandler().availableResponses());
  }

  @Test
  public void httpStatusCodeIn_should_work_for_response_match() throws Exception {
    webservice.getHttpHandler().feedResponses(statusCode(HttpURLConnection.HTTP_OK));
    assertThat(webservice.getClientUri(), UriHttpStatusCode.httpStatusCodeIn(HttpURLConnection.HTTP_OK));
    assertEquals("All fed responses should have been read.", 0, webservice.getHttpHandler().availableResponses());
  }

  @Test
  public void httpStatusCodeIn_should_work_for_one_of_responses_match() throws Exception {
    webservice.getHttpHandler().feedResponses(statusCode(HttpURLConnection.HTTP_OK));
    assertThat(webservice.getClientUri(), UriHttpStatusCode.httpStatusCodeIn(HttpURLConnection.HTTP_ACCEPTED, HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_NO_CONTENT));
    assertEquals("All fed responses should have been read.", 0, webservice.getHttpHandler().availableResponses());
  }

  @Test
  public void httpStatusCodeIn_should_fail_for_response_mismatch() throws Exception {
    webservice.getHttpHandler().feedResponses(statusCode(HttpURLConnection.HTTP_NOT_FOUND));
    assertThat(webservice.getClientUri(), not(UriHttpStatusCode.httpStatusCodeIn(HttpURLConnection.HTTP_OK)));
    assertEquals("All fed responses should have been read.", 0, webservice.getHttpHandler().availableResponses());
  }

  @Test
  public void ping_should_fail_for_unbound_port() throws Exception {
    final URI clientUri = URI.create(String.format("http://%s:%d/", getByName(null).getHostName(), getFreePort()));
    assertThat(clientUri, not(UriHttpStatusCode.httpStatusCodeIn(HttpURLConnection.HTTP_OK)));
  }

  @Test
  public void httpStatusCodeIn_ping_should_fail_for_response_not_provided_within_timeout() throws Exception {
    webservice.getHttpHandler().feedResponses(delay(TIMED_OUT_RESPONSE_DELAY_MILLIS, statusCode(HttpURLConnection.HTTP_OK)));
    assertThat(webservice.getClientUri(), not(UriHttpStatusCode.httpStatusCodeIn(FAST_TIMEOUT, HttpURLConnection.HTTP_OK)));
  }

  @Test
  public void httpStatusCodeSuccess_ping_should_fail_for_response_not_provided_within_timeout() throws Exception {
    webservice.getHttpHandler().feedResponses(delay(TIMED_OUT_RESPONSE_DELAY_MILLIS, statusCode(HttpURLConnection.HTTP_OK)));
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

}
