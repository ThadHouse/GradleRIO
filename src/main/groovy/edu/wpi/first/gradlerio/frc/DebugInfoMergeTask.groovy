package edu.wpi.first.gradlerio.frc

import edu.wpi.first.gradlerio.JsonMergeTask
import groovy.transform.CompileStatic

@CompileStatic
class DebugInfoMergeTask extends JsonMergeTask {

    public static final String CONTAINER_FOLDER = "debug/riopartial"
    public static final String OUTPUT_FILE = "debug/debuginfo.json"

    DebugInfoMergeTask() {
        this.out.set(new File(project.rootProject.buildDir, OUTPUT_FILE))
        this.folder.set(new File(project.rootProject.buildDir, CONTAINER_FOLDER))
        this.singletonName.set("mergeDebugInfo")
    }
}
