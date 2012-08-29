package net.joala.condition;

/**
 * <p>
 *   Conditions to be used during testing. A condition knows how to get the state of an element to verify
 *   and can perform verifications on this element if a requested state is reached.
 * </p>
 * <p>
 *   The central interface is {@link net.joala.condition.Condition}. All class implementing this interface should also implement
 *   {@link net.joala.condition.FailSafeCondition}. This interface is hidden at first
 *   glance to represent a small API to the condition users. Typically those building conditions will need
 *   the interface {@link net.joala.condition.FailSafeCondition} to configure the
 *   behavior of the condition.
 * </p>
 * <p>
 *   Another concept you can find here is the concept of timeouts. The conditions use a {@link net.joala.condition.old.TimeoutProvider}
 *   to retrieve the timeout which this way can be configured per test. In contrast to adjust fixed timeouts for
 *   some conditions you can adjust the factor to wait for a condition. Thus you can say that it will take twice as
 *   much time for a condition to be fulfilled. This way your test implementation does not need to know for example
 *   how slow connection is and such.
 * </p>
 */