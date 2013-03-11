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

package net.joala.dns.story;

import com.google.common.net.InetAddresses;
import net.joala.bdd.reference.Reference;
import net.joala.condition.ConditionFactory;
import net.joala.dns.NameStoreTestWatcher;
import net.joala.expression.Expression;
import net.joala.expression.library.net.UriStatusCodeExpression;
import net.joala.net.EmbeddedWebservice;
import net.joala.net.Response;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Options;

import javax.annotation.Nullable;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static net.joala.bdd.reference.References.ref;
import static net.joala.dns.NameStore.nameStore;
import static net.joala.net.PortUtils.freePort;
import static net.joala.net.StatusCodeResponse.statusCode;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

/**
 * <pre>
 * In order to add customized resolving of hostnames in my tests
 * As tester
 * I want to provide a custom NameService to my JVM.
 * </pre>
 *
 * @since 10/6/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
public class DNSStoryTest extends StoryBaseTest {
  @Rule
  public final TestWatcher nameStoreTestWatcher = new NameStoreTestWatcher();

  private static final Logger LOG = LoggerFactory.getLogger(DNSStoryTest.class);

  @Test
  public void scenario_redirect_uri_access_to_local_webservice() throws Exception {
    final Reference<URI> U = ref("U");
    final Reference<EmbeddedWebservice> S = ref("S");
    final Reference<Integer> statusCode = ref();

    _.given_an_URI_U_with_unknown_host(U);
    _.given_a_local_webservice_S_prepared_to_answer_URI_U(S, U);
    _.when_I_redirected_calls_to_host_of_URI_U_to_webservice_S(U, S);
    _.when_I_connect_to_URI_U(U, statusCode);
    _.then_webservice_S_will_respond(S, statusCode);
  }

  @Test
  public void scenario_resolve_to_real_address_for_unmapped() throws Exception {
    final Reference<URI> U = ref("U");
    final Reference<EmbeddedWebservice> S = ref("S");
    final Reference<Integer> statusCode = ref();
    _.given_an_URI_U_with_known_unmapped_host_served_by_service_S(U, S);
    _.when_I_connect_to_URI_U(U, statusCode);
    _.then_webservice_S_will_respond(S, statusCode);
  }

  @Test
  public void scenario_resolve_to_real_address_after_mapping_removed() throws Exception {
    final Reference<String> H = ref("H");
    final Reference<String> IO = ref("IO");
    final Reference<String> I = ref("I");

    _.given_host_H_is_normally_resolved_to_IP_IO(H, IO);
    _.given_host_H_was_mapped_and_resolved_to_IP_I(H, I);
    _.when_mapping_for_host_H_is_removed(H);
    _.then_host_H_will_be_resolved_to_original_IP_IO_again(H, IO);
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Enabling DNS verbose logging because Debug logging is enabled.");
      Options.set("verbose");
      Options.set("verbosemsg");
      Options.set("verbosecompression");
      Options.set("verbosesec");
      Options.set("verbosecache");
    } else {
      LOG.info("Enable debug logging for {} in order to make DNS lookup verbose.", LOG.getName());
    }
  }

  @Inject
  private Steps _;

  @Named
  @Singleton
  public static class Steps {
    // This constant replaces a random value which actually might be a
    // matching host by random. Thus to provide repeatable results the
    // hostname is fixed here. If for some reason this name is ever
    // registered, replace by a different host.
    private static final String UNKNOWN_HOST_1 = "vcpoisadfjh.cghq.fdao";
    private static final String UNKNOWN_HOST_2 = "bizoxy.roueipqw.vuf";

    private static final String P_HOST = "host";
    private static final String P_PORT = "port";
    private static final String P_CONTEXT = "context";
    private static final String P_RESPONSE = "response";
    private static final String P_ADDRESS = "address";

    @Inject
    private ConditionFactory conditionFactory;

    @Nullable
    private EmbeddedWebservice webservice;

    @PreDestroy
    private void stopService() {
      if (webservice != null) {
        webservice.stop();
      }
    }

    public void given_an_URI_U_with_unknown_host(final Reference<URI> u) throws IOException {
      final String host = UNKNOWN_HOST_1;
      final int port = freePort();
      final String context = "/";
      final URI uri = URI.create(String.format("http://%s:%d%s", host, port, context));
      u.set(uri);
      u.setProperty(P_HOST, host);
      u.setProperty(P_PORT, port);
      u.setProperty(P_CONTEXT, context);
    }

    @SuppressWarnings("ConstantConditions")
    public void given_a_local_webservice_S_prepared_to_answer_URI_U(final Reference<EmbeddedWebservice> s, final Reference<URI> u) throws IOException {
      stopService();
      webservice = new EmbeddedWebservice(u.getProperty(P_CONTEXT, String.class), u.getProperty(P_PORT, Integer.class));
      webservice.start();
      final int statusCode = HTTP_NO_CONTENT;
      final Response response = statusCode(statusCode);
      webservice.getHttpHandler().feedResponses(response);
      s.set(webservice);
      s.setProperty(P_RESPONSE, statusCode);
    }

    @SuppressWarnings("ConstantConditions")
    public void when_I_connect_to_URI_U(final Reference<URI> u, final Reference<Integer> statusCode) {
      final Expression<Integer> expression = new UriStatusCodeExpression(u.get());
      statusCode.set(conditionFactory.condition(expression).await());
    }

    public void then_webservice_S_will_respond(final Reference<EmbeddedWebservice> s, final Reference<Integer> statusCode) {
      final Integer expectedStatusCode = s.getProperty(P_RESPONSE, Integer.class);
      final Integer actualStatusCode = statusCode.get();
      assertEquals("Should have received expected status code.", expectedStatusCode, actualStatusCode);
      assertEquals("Webservice should have sent all responses.", 0, s.get().getHttpHandler().availableResponses());
    }

    public void when_I_redirected_calls_to_host_of_URI_U_to_webservice_S(final Reference<URI> u, final Reference<EmbeddedWebservice> s) throws UnknownHostException {
      final String unknownHost = u.getProperty(P_HOST, String.class);
      final String localHost = s.get().getClientUri().getHost();
      assert unknownHost != null;
      nameStore().register(unknownHost, InetAddress.getByName(localHost));
    }

    public void given_an_URI_U_with_known_unmapped_host_served_by_service_S(final Reference<URI> u, final Reference<EmbeddedWebservice> s) throws IOException {
      stopService();
      webservice = new EmbeddedWebservice();
      webservice.start();
      final int statusCode = HTTP_NO_CONTENT;
      final Response response = statusCode(statusCode);
      webservice.getHttpHandler().feedResponses(response);
      s.set(webservice);
      s.setProperty(P_RESPONSE, statusCode);
      u.set(webservice.getClientUri());
    }

    public void given_host_H_is_normally_resolved_to_IP_IO(final Reference<String> h, final Reference<String> io) throws UnknownHostException {
      final String hostName = UNKNOWN_HOST_2;
      final InetAddress normalIP = InetAddresses.increment(InetAddress.getLocalHost());
      h.set(hostName);
      h.setProperty(P_ADDRESS, normalIP);
      io.set(normalIP.getHostAddress());
      nameStore().register(hostName, normalIP);
      assumeThat(InetAddress.getByName(hostName), equalTo(normalIP));
      nameStore().clear();
    }

    public void given_host_H_was_mapped_and_resolved_to_IP_I(final Reference<String> h, final Reference<String> i) throws UnknownHostException {
      final InetAddress previousAddress = (InetAddress) h.getProperty(P_ADDRESS);
      final InetAddress inetAddress = InetAddresses.increment(previousAddress);
      i.set(inetAddress.getHostAddress());
      nameStore().register(h.get(), inetAddress);
      final InetAddress byName = InetAddress.getByName(h.get());
      assumeThat(byName, equalTo(inetAddress));
    }

    @SuppressWarnings("ConstantConditions")
    public void when_mapping_for_host_H_is_removed(final Reference<String> h) {
      nameStore().unregister(h.get());
      // Actually this only tests the disabled cache; this is because we cannot
      // rely on any external address for this test.
      nameStore().register(h.get(), h.getProperty(P_ADDRESS, InetAddress.class));
    }

    public void then_host_H_will_be_resolved_to_original_IP_IO_again(final Reference<String> h, final Reference<String> io) throws UnknownHostException {
      final InetAddress actualAddress = InetAddress.getByName(h.get());
      assertEquals(io.get(), actualAddress.getHostAddress());
    }
  }
}
