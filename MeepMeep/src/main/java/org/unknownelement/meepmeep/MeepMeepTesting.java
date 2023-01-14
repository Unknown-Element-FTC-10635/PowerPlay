package org.unknownelement.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueLight;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedLight;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
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
                .setConstraints(40, 40, Math.toRadians(361.40679297895304), Math.toRadians(180), 17)

                .setDimensions(16.8, 16)

                // Creates a trajectory
                .followTrajectorySequence(driveShim ->
                        driveShim.trajectorySequenceBuilder(new Pose2d(-36.0, 64, Math.toRadians(270)))
                                .splineTo(new Vector2d(-30, 37), Math.toRadians(-54))
                                .forward(4)
                                .setReversed(true)
                                .splineTo(new Vector2d(-35, 50), Math.toRadians(90))
                                .lineTo(new Vector2d(-35, 4))
                                .lineTo(new Vector2d(-35, 14))
                                .turn(Math.toRadians(-92))
                                .lineTo(new Vector2d(-57, 12))
                                .setReversed(true)
                                .lineToLinearHeading(new Pose2d(-40, 14, Math.toRadians(40)))
                                .lineTo(new Vector2d(-38, 17))
                                .forward(8)
                                .lineToLinearHeading(new Pose2d(-38, 14, -3))
                                .lineTo(new Vector2d(-11.5, 14))
                                .strafeRight(5)

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