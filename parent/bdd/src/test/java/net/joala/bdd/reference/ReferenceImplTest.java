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

package net.joala.bdd.reference;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * Test for {@link ReferenceImpl}.
 * </p>
 *
 * @since 6/5/12
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
public class ReferenceImplTest {
  @Test
  public void should_hold_value() {
    final Reference<Map<Object, Object>> reference = new ReferenceImpl<Map<Object, Object>>();
    final Map<Object, Object> referenceValue = Collections.emptyMap();
    reference.set(referenceValue);
    assertSame("Reference value retrieved should hold the original value set.", referenceValue, reference.get());
  }

  @Test(expected = ReferenceAlreadyBoundException.class)
  public void should_deny_to_set_value_twice() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.set("Lorem");
    reference.set("Ipsum");
  }

  @Test(expected = ReferenceNotBoundException.class)
  public void should_deny_to_read_from_unbound_reference() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.get();
  }

  @Test
  public void should_be_able_to_hold_reference_value_null() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.set(null);
    assertNull("Value null should be returned when retrieving value.", reference.get());
  }

  @Test
  public void should_hold_property_value() {
    final String propertyKey = "lorem";
    final Map<Object, Object> propertyValue = Collections.emptyMap();
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.setProperty(propertyKey, propertyValue);
    assertSame("Uncasted property value should be same.", propertyValue, reference.getProperty(propertyKey));
    assertSame("Casted property value should be same.", propertyValue, reference.getProperty(propertyKey, propertyValue.getClass()));
  }

  @Test
  public void should_hold_null_property_value() {
    final String propertyKey = "lorem";
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.setProperty(propertyKey, null);
    assertNull("Uncasted property value should be same.", reference.getProperty(propertyKey));
    assertNull("Casted property value should be same.", reference.getProperty(propertyKey, String.class));
  }

  @Test(expected = PropertyNotSetException.class)
  public void should_fail_reading_unset_property() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.getProperty("lorem");
  }

  @Test(expected = PropertyAlreadySetException.class)
  public void should_fail_setting_property_which_got_already_set() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.setProperty("lorem", null);
    reference.setProperty("lorem", null);
  }

  @SuppressWarnings("ConstantConditions")
  @Test(expected = NullPointerException.class)
  public void should_fail_for_set_with_property_key_null() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.setProperty(null, "value");
  }

  @SuppressWarnings("ConstantConditions")
  @Test(expected = NullPointerException.class)
  public void should_fail_for_asking_a_null_key() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.hasProperty(null);
  }

  @Test
  public void should_acknowledge_existing_key() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.setProperty("foo", "bar");
    assertTrue("Expected \"foo\" to be there.", reference.hasProperty("foo"));
  }

  @Test
  public void should_deny_existence_of_property() {
    final Reference<String> reference = new ReferenceImpl<String>();
    assertFalse("\"foo\" must not be set.", reference.hasProperty("foo"));
  }

  @SuppressWarnings("ConstantConditions")
  @Test(expected = NullPointerException.class)
  public void should_fail_for_removing_property_with_key_null() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.removeProperty(null, String.class);
  }

  @SuppressWarnings("ConstantConditions")
  @Test(expected = NullPointerException.class)
  public void should_fail_for_removing_property_with_expected_class_null() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.removeProperty("foo", null);
  }

  @Test(expected = PropertyNotSetException.class)
  public void should_fail_when_trying_to_remove_unset_property() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.removeProperty("foo", String.class);
  }

  @Test
  public void remove_property_should_return_former_property_value() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.setProperty("foo", "bar");
    assertEquals("\"foo\" must be set to \"bar\".", "bar", reference.removeProperty("foo", String.class));
  }

  @Test
  public void remove_property_should_actually_remove_the_property() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.setProperty("foo", "bar");
    reference.removeProperty("foo", String.class);
    assertFalse("\"foo\" must not be set anymore.", reference.hasProperty("foo"));
  }

  @Test
  public void should_be_possible_to_query_if_reference_value_is_set_if_set() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.set("Lorem");
    assertTrue("Reference should signal to carry a value.", reference.hasValue());
  }

  @Test
  public void hasValue_should_signal_value_set_for_reference_value_null() {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.set(null);
    assertTrue("Reference should signal to carry a value.", reference.hasValue());
  }

  @Test
  public void should_be_possible_to_query_if_reference_value_is_set_if_unset() {
    final Reference<String> reference = new ReferenceImpl<String>();
    assertFalse("Reference should signal not to carry a value.", reference.hasValue());
  }
}
