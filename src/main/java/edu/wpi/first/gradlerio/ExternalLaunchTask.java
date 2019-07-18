package edu.wpi.first.gradlerio;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.internal.os.OperatingSystem;

public class ExternalLaunchTask extends DefaultTask {
    private final Property<Boolean> scriptOnly = getProject().getObjects().property(Boolean.class);

    private final MapProperty<String, String> environment = getProject().getObjects().mapProperty(String.class,
            String.class);
    private final RegularFileProperty workingDir = getProject().getObjects().fileProperty();

    @Internal
    public MapProperty<String, String> getEnvironment() {
        return environment;
    }

    @Internal
    public Property<Boolean> getScriptOnly() {
        return scriptOnly;
    }

    @Internal
    public RegularFileProperty getWorkingDir() {
        return workingDir;
    }

    public Process launch(String... cmd) throws IOException {
        return this.launch(Arrays.asList(cmd));
    }

    public Process launch(List<String> cmd) throws IOException {
        StringBuilder fileContent = new StringBuilder();

        if (OperatingSystem.current().isWindows()) {
            fileContent.append("@echo off\nsetlocal\n");
        } else {
            fileContent.append("#!/bin/bash\n\n");
        }

        environment.finalizeValue();
        workingDir.finalizeValue();
        scriptOnly.finalizeValue();

        for (var entry : environment.get().entrySet()) {
            if (OperatingSystem.current().isWindows()) {
                fileContent.append("set ");
            } else {
                fileContent.append("export ");
            }
            fileContent.append(entry.getKey()).append('=');
            fileContent.append(entry.getValue()).append('\n');
        }

        if (workingDir.isPresent()) {
            var workingFile = workingDir.get().getAsFile();
            workingFile.mkdirs();
            fileContent.append("pushd ").append(workingFile.getAbsolutePath()).append('\n');
        }

        fileContent.append(String.join(" ", cmd)).append("\n");

        if (workingDir.isPresent()) {
            fileContent.append("popd\n");
        }

        if (OperatingSystem.current().isWindows()) {
            fileContent.append("endlocal\n");
        }

        File file = new File(getProject().getBuildDir(), "gradlerio_" + getName() + (OperatingSystem.current().isWindows() ? ".bat" : ".sh"));

        getProject().getBuildDir().mkdirs();
        
        Files.writeString(file.toPath(), fileContent);
        
        if (OperatingSystem.current().isUnix()) {
            getProject().exec(spec -> {
                spec.commandLine("chmod");
                spec.args("0775", file.getAbsolutePath());
            });
        }

        if (scriptOnly.get() || getProject().hasProperty("headless")) {
            System.out.println("Commandswritten to " + file.getAbsolutePath() + "! Run this file.");
            return null;
        } else {
            File stdoutFile = new File(getProject().getBuildDir(), "stdout/" + getName() + ".log");
            ProcessBuilder builder;
            if (OperatingSystem.current().isWindows()) {
                builder = new ProcessBuilder("cmd", "/c", "start", file.getAbsolutePath());
            } else {
                builder = new ProcessBuilder(file.getAbsolutePath());
                stdoutFile.getParentFile().mkdirs();
                builder.redirectOutput(stdoutFile);
                System.out.println("Program Output logFile: " + stdoutFile.getAbsolutePath());
            }

            Process process = builder.start();
            long pid = process.pid();
            File pidFile = new File(getProject().getBuildDir(), "pids/" + getName() + ".pid");
            pidFile.getParentFile().mkdirs();
            Files.writeString(pidFile.toPath(), Long.toString(pid));
            return process;
        }
    }
}