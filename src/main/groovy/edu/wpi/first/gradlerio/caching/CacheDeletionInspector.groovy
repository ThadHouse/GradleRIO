package edu.wpi.first.gradlerio.caching

import groovy.transform.CompileStatic
import jaci.gradle.log.ETLogger
import org.gradle.initialization.GradleUserHomeDirProvider
import org.gradle.util.GUtil

import java.util.Properties

@CompileStatic
class CacheDeletionInspector {
  public static final String CACHE_CLEANUP_PROPERTY = "org.gradle.cache.cleanup"

  private GradleUserHomeDirProvider userHomeDirProvider

  public CacheDeletionInspector(GradleUserHomeDirProvider userHomeDirProvider) {
    this.userHomeDirProvider = userHomeDirProvider
  }

  public void run(ETLogger log) {
    File gradleUserHomeDirectory = userHomeDirProvider.getGradleUserHomeDirectory()
    File gradleProperties = new File(gradleUserHomeDirectory, "gradle.properties")
    if (gradleProperties.isFile()) {
        Properties properties = GUtil.loadProperties(gradleProperties)
        String cleanup = properties.getProperty(CACHE_CLEANUP_PROPERTY);
        if (cleanup != null && cleanup.equals("false")) {
            return;
        }
        properties.setProperty(CACHE_CLEANUP_PROPERTY, "false")
        GUtil.saveProperties(properties, gradleProperties)
    }
  }
}
