/*
 * Copyright 2013 CoreMedia AG
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

package net.joala.image;

import com.google.common.base.Preconditions;
import net.joala.bdd.reference.Reference;
import net.joala.image.config.ImageBuilderConfig;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static net.joala.bdd.reference.References.ref;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

/**
 * <p>
 * In order to get rid of explicit implementations of Image Builders<br/>
 * As a tester<br/>
 * I want to use dependency injection to retrieve Image Builders.
 * </p>
 *
 * @since 2013-02-25
 */
public class StoryConfigurationViaSpringTest extends SpringImageTestCase {
  /* ==========[ SCENARIOS ]========== */

  @Test
  public void scenario_use_spring_configured_configuration() throws Exception {
    final Reference<ApplicationContext> refA = ref("A");
    final Reference<ImageBuilderConfig> refC = ref("C");

    _.given_a_spring_application_context_$0(refA);
    _.when_a_configuration_$0_is_contained_in_application_context_$1(refC, refA);
    _.then_this_configuration_$0_is_used_for_defaults_in_image_builders(refC);
  }

  /* ==========[ STEPS ]========== */

  @Inject
  private Steps _;

  @SuppressWarnings("SpringJavaAutowiringInspection")
  @Named
  @Singleton
  public static class Steps {
    private static final Logger LOG = LoggerFactory.getLogger(Steps.class);

    @Inject
    private ApplicationContext applicationContext;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Inject
    private List<? extends ImageBuilder<?>> contextImageBuilders;

    public void given_a_spring_application_context_$0(final Reference<ApplicationContext> refA) {
      refA.set(applicationContext);
    }

    public void when_a_configuration_$0_is_contained_in_application_context_$1(final Reference<ImageBuilderConfig> refC, final Reference<ApplicationContext> refA) {
      final ApplicationContext context = refA.get();
      final ImageBuilderConfig bean = context.getBean(ImageBuilderConfig.class);
      refC.set(bean);
    }

    public void then_this_configuration_$0_is_used_for_defaults_in_image_builders(final Reference<ImageBuilderConfig> refC) throws IOException {
      Preconditions.checkState(!contextImageBuilders.isEmpty(), "At least one ImageBuilder should be available through Application-Context.");
      final ImageBuilderConfig imageBuilderConfig = refC.get();
      for (final ImageBuilder<?> imageBuilder : contextImageBuilders) {
        LOG.debug("Using ImageBuilder {}", imageBuilder);
        possiblySetRequiredDefaults(imageBuilder);
        imageBuilder.build();
        verify(imageBuilderConfig, atLeastOnce()).getDefaultHeight();
        verify(imageBuilderConfig, atLeastOnce()).getDefaultWidth();
        reset(imageBuilderConfig);
      }
    }

    private void possiblySetRequiredDefaults(final ImageBuilder<?> imageBuilder) throws IOException {
      if (imageBuilder instanceof OutputImageBuilder) {
        final OutputImageBuilder builder = (OutputImageBuilder) imageBuilder;
        final File tempFile = File.createTempFile(StoryConfigurationViaSpringTest.class.getName(), ".someimg");
        tempFile.deleteOnExit();
        builder.output(tempFile);
      }
    }
  }
}
