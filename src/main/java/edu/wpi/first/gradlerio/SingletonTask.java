package edu.wpi.first.gradlerio;

import org.gradle.api.provider.Property;

public interface SingletonTask {
    public Property<String> getSingletonName();
}