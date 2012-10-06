package net.joala.dns;

import sun.net.spi.nameservice.NameService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.String.format;

/**
 * @since 10/6/12
 */
public class FallbackInetAddressNameService implements NameService {
  private static final SystemLogger LOG = SystemLogger.getLogger(FallbackInetAddressNameService.class);

  private static FallbackInetAddressNameService ourInstance;

  @SuppressWarnings("NonThreadSafeLazyInitialization")
  public static FallbackInetAddressNameService fallbackNameService() throws InstantiationException {
    if (ourInstance == null) {
      ourInstance = new FallbackInetAddressNameService();
    }
    return ourInstance;
  }

  private FallbackInetAddressNameService() throws InstantiationException {
    try {
      final Class<?> factory = Class.forName(JAVA_NET_INET_ADDRESS_IMPL_FACTORY, true, InetAddress.class.getClassLoader());
      final Method method = factory.getDeclaredMethod("create");
      method.setAccessible(true);
      wrapped = method.invoke(null);
      final Class<?> wrappedClass = wrapped.getClass();
      lookupAllHostAddrMethod = wrappedClass.getMethod(LOOKUP_ALL_HOST_ADDR, String.class);
      getHostByAddrMethod = wrappedClass.getMethod(GET_HOST_BY_ADDR, byte[].class);
      lookupAllHostAddrMethod.setAccessible(true);
      getHostByAddrMethod.setAccessible(true);
    } catch (Exception e) {
      final InstantiationException exception = new InstantiationException("Unable to access JVM private API as it seems.");
      exception.initCause(e);
      throw exception;
    }
  }

  private final Object wrapped;
  private final Method lookupAllHostAddrMethod;
  private final Method getHostByAddrMethod;

  private static final String JAVA_NET_INET_ADDRESS_IMPL_FACTORY = "java.net.InetAddressImplFactory";
  private static final String LOOKUP_ALL_HOST_ADDR = "lookupAllHostAddr";
  private static final String GET_HOST_BY_ADDR = "getHostByAddr";

  @Override
  public InetAddress[] lookupAllHostAddr(final String host) throws UnknownHostException {
    try {
      LOG.info("Redirecting lookupAllHostAddr() to " + wrapped.getClass().getName());
      return (InetAddress[]) lookupAllHostAddrMethod.invoke(wrapped, host);
    } catch (IllegalAccessException e) {
      throw createForwardException("lookupAllHostAddr", e);
    } catch (InvocationTargetException e) {
      throw createForwardException("lookupAllHostAddr", e);
    }
  }

  @Override
  public String getHostByAddr(final byte[] addr) throws UnknownHostException {
    try {
      LOG.info("Redirecting getHostByAddr() to " + wrapped.getClass().getName());
      return (String) getHostByAddrMethod.invoke(wrapped, new Object[]{addr});
    } catch (IllegalAccessException e) {
      throw createForwardException("lookupAllHostAddr", e);
    } catch (InvocationTargetException e) {
      throw createForwardException("lookupAllHostAddr", e);
    }
  }

  private UnknownHostException createForwardException(final String methodName, final Throwable cause) {
    final UnknownHostException exception = new UnknownHostException(format("Unable to forward request to %s", methodName));
    exception.initCause(cause);
    return exception;
  }

}
