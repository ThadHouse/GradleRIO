package edu.wpi.first.gradlerio.test

import edu.wpi.first.gradlerio.ExternalLaunchTask
import edu.wpi.first.gradlerio.test.TestPlugin
import groovy.transform.CompileStatic
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.nativeplatform.NativeExecutableBinarySpec
import org.gradle.nativeplatform.tasks.InstallExecutable

@CompileStatic
class NativeSimulationTask extends ExternalLaunchTask {
    @Internal
    NativeExecutableBinarySpec binary

    @TaskAction
    void run() {
        def installTask = binary.tasks.withType(InstallExecutable).first()
        def dir = new File(installTask.installDirectory.asFile.get(), "lib")

        environment.set(TestPlugin.getSimLaunchEnv(project, dir.absolutePath))

        workingDir.set(dir)
        launch("\"${installTask.runScriptFile.get().asFile.absolutePath}\"")
    }

}
