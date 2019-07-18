package edu.wpi.first.gradlerio;

import java.io.IOException;
import java.util.Arrays;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;

public class JsonMergeTask extends DefaultTask implements SingletonTask {
    private final DirectoryProperty folder = getProject().getObjects().directoryProperty();
    private final RegularFileProperty out = getProject().getObjects().fileProperty();
    private final Property<String> singletonName = getProject().getObjects().property(String.class);

    public JsonMergeTask() {
        singletonName.set("jsonMerge");
    }

    @Internal
    @Override
    public Property<String> getSingletonName() {
        return singletonName;
    }
    
    @Internal
    public DirectoryProperty getFolder() {
        return folder;
    }

    @Internal
    public RegularFileProperty getOut() {
        return out;
    }

    @TaskAction
    public void merge() throws IOException {
        var containerFolder = folder.get().getAsFile();
        var outFile = out.get().getAsFile();

        if (containerFolder.exists()) {
            var files = containerFolder.listFiles((path) -> {
                return path.isFile() && path.getName().endsWith(".json") && !path.getAbsolutePath().equals(outFile.getAbsolutePath());
            });
            JsonUtil.mergeArrays(Arrays.asList(files), outFile);
        }
    }
}