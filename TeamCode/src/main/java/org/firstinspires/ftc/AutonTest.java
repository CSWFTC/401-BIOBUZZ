package org.firstinspires.ftc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

public class AutonTest extends LinearOpMode {
    DcMotor leftDrive;
    DcMotor rightDrive;

    public void runOpMode() {
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        waitForStart(); // Wait until the match starts

        // Drive forward
        leftDrive.setPower(0.5);
        rightDrive.setPower(0.5);
        sleep(1000); // move for 1 second
        stopMotors();
    }

    private void stopMotors() {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
    }
}
