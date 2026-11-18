package org.firstinspires.ftc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

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

        // Same directions as Inout
        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        transfer.setDirection(DcMotorSimple.Direction.REVERSE);
        outtake.setDirection(DcMotorSimple.Direction.REVERSE);

        // Stop quickly when the sticks are released instead of coasting
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Ignore tiny stick movements so the robot doesn't creep
        double deadzone = 0.1;

        // Inout: tap A to turn intake + transfer + outtake ON, tap A again for OFF
        boolean running = false;
        boolean lastA = false;

        waitForStart();

        while (opModeIsActive()) {
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

            // Inout: tap A to toggle all three motors ON / OFF
            boolean aNow = gamepad1.a;
            if (aNow && !lastA) {
                running = !running;
            }
            lastA = aNow;

            double power  = running ? 1.0 : 0.0;   // intake + transfer
            double power2 = running ? 0.65 : 0.0;  // outtake
            intake.setPower(power);
            transfer.setPower(power);
            outtake.setPower(power2);

            telemetry.addData("Left Y (drive)", y);
            telemetry.addData("Left X (strafe)", x);
            telemetry.addData("Right X (turn)", rx);
            telemetry.addData("FL / FR", "%.2f / %.2f", fl, fr);
            telemetry.addData("BL / BR", "%.2f / %.2f", bl, br);
            telemetry.addData("Inout Motors", running ? "ON" : "OFF");
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