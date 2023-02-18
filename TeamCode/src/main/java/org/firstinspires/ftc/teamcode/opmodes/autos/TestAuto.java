package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commandgroups.HighGoal;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;
import org.firstinspires.ftc.teamcode.util.CurrentOpmode;

@Autonomous
public class TestAuto extends CommandOpMode {
    @Override
    public void initialize() {
        // Subsystems
        LimitSwitch extensionLeftLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "extensionLSW");
        LimitSwitch extensionRightLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "extensionRSW");
        LimitSwitch rotationBottomLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "rotationBSW");
        LimitSwitch rotationTopLimitSwitch = new LimitSwitch(hardwareMap, telemetry, "rotationTSW");
        Extension extension = new Extension(hardwareMap, telemetry);
        Rotation rotation = new Rotation(hardwareMap, telemetry);
        Claw claw = new Claw(hardwareMap, telemetry);

        register(extensionLeftLimitSwitch, extension, claw, rotation, rotationBottomLimitSwitch, rotationTopLimitSwitch);

        CurrentOpmode.setCurrentOpmode(CurrentOpmode.OpMode.AUTO);

        waitForStart();

        schedule(
                new SequentialCommandGroup(
                        new WaitCommand(500),
                        new HighGoal(rotation, rotationBottomLimitSwitch, rotationTopLimitSwitch, extension,  extensionLeftLimitSwitch, extensionRightLimitSwitch, claw)
                        //new MediumGoal(rotation, rotationSW, claw),
                        //new WaitCommand(2000),
                        //new RotateHome(rotation, rotationSW)
                )
        );
    }
}
