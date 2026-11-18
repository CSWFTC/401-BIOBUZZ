package org.firstinspires.ftc;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "AutonTest", group = "Auton")
public class AutonTest extends LinearOpMode {
    DcMotor frontLeft, backLeft, frontRight, backRight;

    // Tune these on the field
    static final double DRIVE_POWER = 0.5;
    static final long   DRIVE_MS    = 1000;

    @Override
    public void runOpMode() {
        // Names must match the Driver Station config (same as DriveTrain)
        frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight  = hardwareMap.get(DcMotor.class, "backRight");

        // Same directions as DriveTrain
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // Stop quickly instead of coasting
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Ready - press START");
        telemetry.update();

        waitForStart();

        // --- Autonomous routine ---
        if (opModeIsActive()) {
            driveForward(DRIVE_POWER, DRIVE_MS);
        }

        stopDrive();
    }

    // ---- Movement helpers (signs copied from DriveTrain TeleOp) ----

    // power > 0 = forward, power < 0 = backward
    private void driveForward(double power, long ms) {
        setDrive(-power, -power, power, power);
        waitMs(ms);
        stopDrive();
    }

    // power > 0 = right, power < 0 = left
    private void strafeRight(double power, long ms) {
        setDrive(-power, power, -power, power);
        waitMs(ms);
        stopDrive();
    }

    // power > 0 = turn right, power < 0 = turn left
    private void turnRight(double power, long ms) {
        setDrive(-power, -power, -power, -power);
        waitMs(ms);
        stopDrive();
    }

    private void setDrive(double fl, double bl, double fr, double br) {
        frontLeft.setPower(fl);
        backLeft.setPower(bl);
        frontRight.setPower(fr);
        backRight.setPower(br);
    }

    private void stopDrive() {
        setDrive(0, 0, 0, 0);
    }

    // Like sleep(), but exits early if STOP is pressed
    private void waitMs(long ms) {
        long end = System.currentTimeMillis() + ms;
        while (opModeIsActive() && System.currentTimeMillis() < end) {
            idle();
        }
    }
}