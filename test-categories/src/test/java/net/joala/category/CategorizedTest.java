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

package net.joala.category;

import net.joala.category.clearance.browser.AnyBrowser;
import net.joala.category.clearance.browser.Firefox;
import net.joala.category.clearance.os.AIX;
import net.joala.category.clearance.os.Linux;
import net.joala.category.clearance.os.Windows;
import net.joala.category.confidence.Stable;
import net.joala.category.level.Integration;
import net.joala.category.level.Unit;
import net.joala.category.requirement.WebGUI;
import net.joala.category.time.WithinMilliseconds;
import net.joala.category.time.WithinSeconds;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * @since 8/30/12
 */
public class CategorizedTest {
  @Test
  @Category({Unit.class, WithinMilliseconds.class})
  public void very_fast_unit_test() throws Exception {
  }

  @Test
  @Category({Integration.class, WithinMilliseconds.class})
  public void very_fast_integration_test() throws Exception {
  }

  @Test
  @Category({Integration.class, Linux.class, AIX.class, WithinSeconds.class})
  public void fast_unix_integration_test() {
  }

  @Test
  @Category({WebGUI.class, AnyBrowser.class, Windows.class, WithinSeconds.class})
  public void fast_ui_test_for_any_browser_on_windows() {
  }

  @Test
  @Category({Unit.class, WithinSeconds.class})
  public void fast_unit_test_in_seconds() {
  }

  @Test
  @Category({WebGUI.class, Firefox.class, Windows.class, WithinSeconds.class})
  public void fast_ui_test_for_firefox_on_windows() {
  }

  @Test
  @Category({WebGUI.class, Firefox.class, Linux.class, WithinSeconds.class})
  public void fast_ui_test_for_firefox_on_linux() {
  }

  @Test
  @Category({WebGUI.class, Windows.class, StableFastAnyBrowser.class})
  public void fast_ui_test_for_all_browsers_on_windows() {
  }

  @Test
  @Category({WebGUI.class, Windows.class, Stable.class, AnyBrowser.class, WithinSeconds.class})
  public void fast_ui_test_for_all_browsers_on_windows_2() {
  }

  @Test
  @Category({WebGUI.class, Windows.class, Stable.class, AnyBrowser.class, WithinMilliseconds.class})
  public void very_fast_ui_test_for_all_browsers_on_windows_2() {
  }

  public interface StableFastAnyBrowser extends Stable, AnyBrowser, WithinSeconds {
  }
}
