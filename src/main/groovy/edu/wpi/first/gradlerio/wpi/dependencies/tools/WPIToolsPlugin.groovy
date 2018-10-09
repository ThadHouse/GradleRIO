package edu.wpi.first.gradlerio.wpi.dependencies.tools

import com.google.gson.GsonBuilder
import edu.wpi.first.gradlerio.tooling.GradleRIOToolingExtension
import edu.wpi.first.gradlerio.wpi.WPIExtension
import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

@CompileStatic
class WPIToolsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.configurations.maybeCreate("wpiTools")

        def wpi = project.extensions.getByType(WPIExtension)
        project.afterEvaluate {
            List<WPITool> tools = project.extensions.getByType(GradleRIOToolingExtension).tools

            def frcHome = wpi.frcHome
            String toolFolder = new File(frcHome, 'tools').toString()

            ToolInstallTask.toolsFolder = toolFolder
            tools << new WPITool(project, "SmartDashboard", wpi.smartDashboardVersion, "edu.wpi.first.wpilib:SmartDashboard:${wpi.smartDashboardVersion}", false)
            tools << new WPITool(project, "ShuffleBoard", wpi.shuffleboardVersion, "edu.wpi.first.shuffleboard:shuffleboard:${wpi.shuffleboardVersion}", true)
            tools << new WPITool(project, "OutlineViewer", wpi.outlineViewerVersion, "edu.wpi.first.wpilib:OutlineViewer:${wpi.outlineViewerVersion}", true)
            tools << new WPITool(project, "RobotBuilder", wpi.robotBuilderVersion, "edu.wpi.first.wpilib:RobotBuilder:${wpi.robotBuilderVersion}", false)
            tools << new WPITool(project, "PathWeaver", wpi.pathWeaverVersion, "edu.wpi.first.wpilib:PathWeaver:${wpi.pathWeaverVersion}", true)

            project.tasks.register("InstallAllTools") { Task task->
                task.group = 'GradleRIO'
                task.description = 'Install All Tools'

                tools.each { WPITool tool->
                    task.dependsOn tool.toolInstallTask
                }
            }
        }


    }
}
