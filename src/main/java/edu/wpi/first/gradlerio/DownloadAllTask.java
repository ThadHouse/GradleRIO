package edu.wpi.first.gradlerio;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import jaci.gradle.log.ETLoggerFactory;

public class DownloadAllTask extends DefaultTask {

    @TaskAction
    public void downloadAll() {
        var logger = ETLoggerFactory.INSTANCE.create("DownloadAll");

        for (var conf : getProject().getConfigurations()) {
            // Skip configurations that cannot be resolved
            if (conf.isCanBeResolved()) {
                System.out.println("Resolving: " + conf.getName());
                for (var art : conf.getResolvedConfiguration().getResolvedArtifacts()) {
                    art.getFile(); // Needed to trigger download
                }
            } else {
                logger.info("Can't resolve " + conf.getName());
            }
        }
    }
}