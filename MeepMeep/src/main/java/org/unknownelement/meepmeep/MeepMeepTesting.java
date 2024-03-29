package org.unknownelement.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueLight;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedLight;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.SampleMecanumDrive;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        // TODO: If you experience poor performance, enable this flag
        //System.setProperty("sun.java2d.opengl", "true");

        MeepMeep meepMeep = new MeepMeep(800, 60);

        RoadRunnerBotEntity rightAuto = new DefaultBotBuilder(meepMeep)
                // Set Color Scheme (BlueLight, BlueDark, RedLight, RedDark)
                .setColorScheme(new ColorSchemeBlueLight())

                // Set constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(361.40679297895304), Math.toRadians(180), 15.15)

                .setDimensions(16.8, 16)

                // Creates a trajectory
                .followTrajectorySequence(driveShim ->
                        driveShim.trajectorySequenceBuilder(new Pose2d(-33.0, -64, Math.toRadians(90)))
                                // Preload
                                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, 15.15))
                                .splineTo(new Vector2d(-36, -40), Math.toRadians(90))
                                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(100, 100, 15.15))
                                .lineTo(new Vector2d(-36, -5))
                                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, 15.15))
                                .back(8)
                                .lineToSplineHeading(new Pose2d(-37, -15.75, Math.toRadians(225)))
                                .back(11.5)

                                .waitSeconds(0.25)

                                // Pick up
                                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(50, 50, 15.15))
                                .splineTo(new Vector2d(-40, -12), Math.toRadians(180))
                                .lineTo(new Vector2d(-60, -12))

                                // Pole
                                .setVelConstraint(SampleMecanumDrive.getVelocityConstraint(40, 40, 15.15))
                                .setReversed(true)
                                .lineTo(new Vector2d(-40, -12))
                                .splineTo(new Vector2d(-29, -7), Math.toRadians(30))

                                .build()
                );



        meepMeep
                // Set field image
                .setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)

                // Set Color Scheme (BlueLight, BlueDark, RedLight, RedDark)
                .setTheme(new ColorSchemeBlueLight())

                // Background opacity from 0-1
                .setBackgroundAlpha(0.95f)

                // Adds the bot
                .addEntity(rightAuto)
                .start();
    }
}