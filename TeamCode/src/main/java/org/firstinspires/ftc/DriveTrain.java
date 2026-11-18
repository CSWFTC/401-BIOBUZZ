package org.firstinspires.ftc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

// CONTROLS
//   Gamepad 1 (driver):
//     Left stick up/down    = drive forward / backward
//     Left stick left/right = strafe
//     Right stick left/right = turn
//   Gamepad 2 (operator):
//     A          = toggle INTAKE + TRANSFER on / off
//     B          = toggle TRANSFER + OUTTAKE on / off
//     Outtake speed presets (D-pad):
//       D-pad down  = 0.50
//       D-pad left  = 0.65 (default)
//       D-pad right = 0.80
//       D-pad up    = 1.00
//     Outtake fine tune:
//       Y = speed +0.05
//       X = speed -0.05
@TeleOp(name = "DriveTrain", group = "Drive")
public class DriveTrain extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Robot configuration (must match the Driver Station config):
        //   Port 0 = "frontLeft"
        //   Port 1 = "backLeft"
        //   Port 2 = "frontRight"
        //   Port 3 = "backRight"
        //   plus "intake", "transfer", "outtake"
        DcMotor frontLeft  = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backLeft   = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRight  = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor intake     = hardwareMap.get(DcMotor.class, "intake");
        DcMotor transfer   = hardwareMap.get(DcMotor.class, "transfer");
        DcMotor outtake    = hardwareMap.get(DcMotor.class, "outtake");

        // Same directions as before (these work for forward and turning)
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        transfer.setDirection(DcMotorSimple.Direction.REVERSE);
        outtake.setDirection(DcMotorSimple.Direction.REVERSE);

        // FASTER: run drive motors without the encoder speed limit,
        // so power 1.0 gives the motor's true full speed.
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Stop quickly when the sticks are released instead of coasting
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Ignore tiny stick movements so the robot doesn't creep
        double deadzone = 0.1;

        // Gamepad 2 toggles (separate)
        boolean intakeOn  = false;   // A: intake + transfer
        boolean outtakeOn = false;   // B: transfer + outtake
        boolean lastA = false;
        boolean lastB = false;

        // Outtake speed: D-pad presets, X / Y fine tune
        double outtakeSpeed = 0.65;   // default
        double speedStep = 0.05;
        boolean lastX = false;
        boolean lastY = false;

        waitForStart();

        while (opModeIsActive()) {
            // ===== DRIVE (gamepad 1) - unchanged movement logic =====
            double y  = -gamepad1.left_stick_y;  // left stick up = positive
            double x  = gamepad1.left_stick_x;   // left stick right = positive
            double rx = gamepad1.right_stick_x;  // right stick right = positive

            double fl, bl, fr, br;

            if (Math.abs(rx) > deadzone) {
                // TURN (right stick left/right)
                fl = -rx;
                bl = -rx;
                fr = -rx;
                br = -rx;
            } else if (Math.abs(y) > deadzone && Math.abs(y) >= Math.abs(x)) {
                // DRIVE FORWARD / BACKWARD (left stick up/down)
                fl = -y;
                bl = -y;
                fr = y;
                br = y;
            } else if (Math.abs(x) > deadzone) {
                // STRAFE LEFT / RIGHT (left stick left/right)
                fl = -x;
                bl = x;
                fr = -x;
                br = x;
            } else {
                // STOP (no stick pushed)
                fl = 0;
                bl = 0;
                fr = 0;
                br = 0;
            }

            frontLeft.setPower(fl);
            backLeft.setPower(bl);
            frontRight.setPower(fr);
            backRight.setPower(br);

            // ===== MECHANISMS (gamepad 2) =====

            // A: toggle intake + transfer
            boolean aNow = gamepad2.a;
            if (aNow && !lastA) {
                intakeOn = !intakeOn;
            }
            lastA = aNow;

            // B: toggle transfer + outtake
            boolean bNow = gamepad2.b;
            if (bNow && !lastB) {
                outtakeOn = !outtakeOn;
            }
            lastB = bNow;

            // D-pad: jump to a preset outtake speed
            if (gamepad2.dpad_down) {
                outtakeSpeed = 0.50;
            } else if (gamepad2.dpad_left) {
                outtakeSpeed = 0.65;
            } else if (gamepad2.dpad_right) {
                outtakeSpeed = 0.80;
            } else if (gamepad2.dpad_up) {
                outtakeSpeed = 1.00;
            }

            // Y / X: fine tune outtake speed (one step per press)
            boolean yNow = gamepad2.y;
            boolean xNow = gamepad2.x;
            if (yNow && !lastY) {
                outtakeSpeed += speedStep;
            }
            if (xNow && !lastX) {
                outtakeSpeed -= speedStep;
            }
            lastY = yNow;
            lastX = xNow;
            outtakeSpeed = Math.max(0.0, Math.min(1.0, outtakeSpeed));  // keep 0 to 1

            // Transfer runs if EITHER A or B mode is on
            intake.setPower(intakeOn ? 1.0 : 0.0);
            transfer.setPower((intakeOn || outtakeOn) ? 1.0 : 0.0);
            outtake.setPower(outtakeOn ? outtakeSpeed : 0.0);

            // ===== TELEMETRY =====
            telemetry.addData("Left Y (drive)", y);
            telemetry.addData("Left X (strafe)", x);
            telemetry.addData("Right X (turn)", rx);
            telemetry.addData("FL / FR", "%.2f / %.2f", fl, fr);
            telemetry.addData("BL / BR", "%.2f / %.2f", bl, br);
            telemetry.addData("Intake (GP2 A)", intakeOn ? "ON" : "OFF");
            telemetry.addData("Outtake (GP2 B)", outtakeOn ? "ON" : "OFF");
            telemetry.addData("Outtake Speed (D-pad / X Y)", "%.2f", outtakeSpeed);
            telemetry.addData("Intake Power", intake.getPower());
            telemetry.addData("Transfer Power", transfer.getPower());
            telemetry.addData("Outtake Power", outtake.getPower());
            telemetry.update();
        }

        intake.setPower(0);
        transfer.setPower(0);
        outtake.setPower(0);
    }
}