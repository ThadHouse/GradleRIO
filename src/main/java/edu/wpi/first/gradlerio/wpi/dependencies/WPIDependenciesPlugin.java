package edu.wpi.first.gradlerio.wpi.dependencies;

import java.util.ArrayList;
import java.util.List;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.jvm.tasks.Jar;

import edu.wpi.first.deployutils.deploy.DeployExtension;
import edu.wpi.first.deployutils.deploy.artifact.Artifact;
import edu.wpi.first.deployutils.deploy.target.RemoteTarget;
import edu.wpi.first.deployutils.log.ETLogger;
import edu.wpi.first.deployutils.log.ETLoggerFactory;
import edu.wpi.first.gradlerio.PreemptiveDownloadTask;
import edu.wpi.first.gradlerio.deploy.roborio.FRCJREArtifact;
import edu.wpi.first.gradlerio.deploy.roborio.FRCJavaArtifact;
import edu.wpi.first.gradlerio.wpi.WPIExtension;
import groovy.transform.CompileStatic;

public class WPIDependenciesPlugin implements Plugin<Project> {

    @CompileStatic
    public static class MissingJniDependencyException extends RuntimeException {
        private static final long serialVersionUID = -3526743142145446835L;
        private final String dependencyName;
        private final String classifier;
        private final WPIVendorDepsExtension.JniArtifact artifact;

        public String getDependencyName() {
            return dependencyName;
        }

        public String getClassifier() {
            return classifier;
        }

        public WPIVendorDepsExtension.JniArtifact getArtifact() {
            return artifact;
        }

        public MissingJniDependencyException(String name, String classifier, WPIVendorDepsExtension.JniArtifact artifact) {
            super("Cannot find jni dependency: " + name + " for classifier: " + classifier);
            this.dependencyName = name;
            this.classifier = classifier;
            this.artifact = artifact;
        }
    }

    @Override
    public void apply(Project project) {
        ETLogger logger = ETLoggerFactory.INSTANCE.create("WPIDeps");
        WPIExtension wpi = project.getExtensions().getByType(WPIExtension.class);
        wpi.getVendor().loadAll();

        // // We need to register our own task for this, since .doFirst on compileJava (or any Jar task), won"t work
        // // if it"s up-to-date
        // TaskProvider<Task> lazyPreempt = project.getTasks().register("downloadDepsPreemptively", t -> {
        //     t.doFirst(new Action<Task>() {
        //         @Override
        //         public void execute(Task t) {
        //             // On build, download all libs that will be needed for deploy to lessen the cases where the user has to
        //             // run an online deploy dry or downloadAll task.
        //             downloadDepsPreemptively(project, logger);
        //         }
        //     });
        // });

        TaskProvider<PreemptiveDownloadTask> lazyPreempt = project.getTasks().register("downloadDepsPreemptively", PreemptiveDownloadTask.class);

        project.getTasks().register("vendordep", VendorDepTask.class, task -> {
            task.setGroup("GradleRIO");
            task.setDescription("Install vendordep JSON file from URL or local wpilib folder");
        });

        project.getTasks().withType(Jar.class, jarTask -> {
            jarTask.dependsOn(lazyPreempt);
        });
    }
}
