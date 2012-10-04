/*
 * Copyright 2012 CoreMedia AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.joala.bdd.reference;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * <p>
 * Test for {@link ReferenceImpl}.
 * </p>
 *
 * @since 6/5/12
 */
public class ReferenceImplTest {
  @Test
  public void should_hold_value() throws Exception {
    final Reference<Map<Object, Object>> reference = new ReferenceImpl<Map<Object, Object>>();
    final Map<Object, Object> referenceValue = Collections.emptyMap();
    reference.set(referenceValue);
    assertSame("Reference value retrieved should hold the original value set.", referenceValue, reference.get());
  }

  @Test(expected = ReferenceAlreadyBoundException.class)
  public void should_deny_to_set_value_twice() throws Exception {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.set("Lorem");
    reference.set("Ipsum");
  }

  @Test(expected = ReferenceNotBoundException.class)
  public void should_deny_to_read_from_unbound_reference() throws Exception {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.get();
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_for_reference_value_null() throws Exception {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.set(null);
  }

  @Test
  public void should_hold_property_value() throws Exception {
    final String propertyKey = "lorem";
    final Map<Object, Object> propertyValue = Collections.emptyMap();
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.setProperty(propertyKey, propertyValue);
    assertSame("Uncasted property value should be same.", propertyValue, reference.getProperty(propertyKey));
    assertSame("Casted property value should be same.", propertyValue, reference.getProperty(propertyKey, propertyValue.getClass()));
  }

  @Test
  public void should_hold_null_property_value() throws Exception {
    final String propertyKey = "lorem";
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.setProperty(propertyKey, null);
    assertNull("Uncasted property value should be same.", reference.getProperty(propertyKey));
    assertNull("Casted property value should be same.", reference.getProperty(propertyKey, String.class));
  }

  @Test(expected = PropertyNotSetException.class)
  public void should_fail_reading_unset_property() throws Exception {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.getProperty("lorem");
  }

  @Test(expected = PropertyAlreadySetException.class)
  public void should_fail_setting_property_which_got_already_set() throws Exception {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.setProperty("lorem", null);
    reference.setProperty("lorem", null);
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_for_set_with_property_key_null() throws Exception {
    final Reference<String> reference = new ReferenceImpl<String>();
    reference.setProperty(null, "value");
  }

}
