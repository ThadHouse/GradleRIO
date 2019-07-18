package edu.wpi.first.gradlerio;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
    public static void mergeArrays(List<File> files, File outFile) throws IOException {
        Gson gson = new Gson();
        List merged = new ArrayList();

        for (File f : files) {
            try (FileReader reader = new FileReader(f)) {
                Object[] items = gson.fromJson(reader, Object[].class);
                merged.addAll(Arrays.asList(items));
            }
        }

        GsonBuilder gbuilder = new GsonBuilder();
        gbuilder.setPrettyPrinting();
        String json = gbuilder.create().toJson(merged);
         
        outFile.getParentFile().mkdirs();

        Files.writeString(outFile.toPath(), json);

    }
}