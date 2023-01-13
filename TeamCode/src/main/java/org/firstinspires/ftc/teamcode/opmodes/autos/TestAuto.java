package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commandgroups.MediumGoal;
import org.firstinspires.ftc.teamcode.commandgroups.RotateHome;
import org.firstinspires.ftc.teamcode.commands.Rotate;
import org.firstinspires.ftc.teamcode.subsystems.Claw;
import org.firstinspires.ftc.teamcode.subsystems.Extension;
import org.firstinspires.ftc.teamcode.subsystems.LimitSwitch;
import org.firstinspires.ftc.teamcode.subsystems.Rotation;

@Autonomous
public class TestAuto extends CommandOpMode {
    @Override
    public void initialize() {
        // Subsystems
        Extension extension = new Extension(hardwareMap, telemetry);
        Rotation rotation = new Rotation(hardwareMap, telemetry);
        LimitSwitch limitSwitch = new LimitSwitch(hardwareMap, telemetry, "primarySwitch");
        Claw claw = new Claw(hardwareMap, telemetry);

        register(limitSwitch, extension, claw, rotation);

        waitForStart();

        schedule(
                new SequentialCommandGroup(
                        new MediumGoal(rotation, claw),
                        new WaitCommand(2000),
                        new RotateHome(rotation)
                )
        );
    }
}
