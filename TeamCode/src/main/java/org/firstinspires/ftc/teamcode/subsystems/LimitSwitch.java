package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LimitSwitch extends SubsystemBase {
    private final DigitalChannel limitSwitch;

    private final Telemetry telemetry;

    private final String name;

    public LimitSwitch(HardwareMap hardwareMap, Telemetry telemetry, String name) {
        this.limitSwitch = hardwareMap.get(DigitalChannel.class , name);
        this.telemetry = telemetry;
        this.name = name;
    }

    public boolean isPressed() {
        return limitSwitch.getState();
    }

    @Override
    public void periodic() {
        telemetry.addData(name + " Status:", limitSwitch.getState());
    }
}
