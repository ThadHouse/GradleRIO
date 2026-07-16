package first.team0000.robot

import org.wpilib.framework.TimedRobot
import org.wpilib.smartdashboard.SendableChooser
import org.wpilib.smartdashboard.SmartDashboard

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
class Robot : TimedRobot() {
    private var autoSelected = kDefaultAuto
    private val chooser = SendableChooser<String>()

    init {
        chooser.setDefaultOption("Default Auto", kDefaultAuto)
        chooser.addOption("My Auto", kCustomAuto)
        SmartDashboard.putData("Auto choices", chooser)
    }

    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     *
     * This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    override fun robotPeriodic() {
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString line to get the auto name from the text box below the Gyro
     *
     * You can add additional auto modes by adding additional comparisons to
     * the switch structure below with additional strings. If using the
     * SendableChooser make sure to add them to the chooser code above as well.
     */
    override fun autonomousInit() {
        autoSelected = chooser.selected ?: kDefaultAuto
        // autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto)
        println("Auto selected: $autoSelected")
    }

    /**
     * This function is called periodically during autonomous.
     */
    override fun autonomousPeriodic() {
        when (autoSelected) {
            kCustomAuto -> {
                // Put custom auto code here
            }
            kDefaultAuto -> {
                // Put default auto code here
            }
            else -> {
                // Put default auto code here
            }
        }
    }

    /**
     * This function is called periodically during operator control.
     */
    override fun teleopPeriodic() {
    }

    /**
     * This function is called periodically during utility mode.
     */
    override fun utilityPeriodic() {
    }

    private companion object {
        const val kDefaultAuto = "Default"
        const val kCustomAuto = "My Auto"
    }
}
