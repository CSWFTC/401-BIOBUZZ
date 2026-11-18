package org.firstinspires.ftc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

// Test OpMode: intake + transfer + outtake on ONE button.
// Tap A: all three ON. Tap A again: all three OFF. Repeat.
@TeleOp(name = "Inout", group = "Test")
public class Inout extends LinearOpMode {

    public static DcMotor intake;
    public static DcMotor transfer;
    public static DcMotor outtake;

    @Override
    public void runOpMode() {

        intake   = hardwareMap.get(DcMotor.class, "intake");
        transfer = hardwareMap.get(DcMotor.class, "transfer");
        outtake  = hardwareMap.get(DcMotor.class, "outtake");


        intake.setDirection(DcMotorSimple.Direction.REVERSE);
        transfer.setDirection(DcMotorSimple.Direction.REVERSE);
        outtake.setDirection(DcMotorSimple.Direction.REVERSE);

        boolean running = false;
        boolean lastA = false;

        telemetry.addData("Status", "Ready. Tap A to turn all three ON / OFF.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            boolean aNow = gamepad1.a;
            if (aNow && !lastA) {
                running = !running;
            }
            lastA = aNow;

            double power = running ? 1.0 : 0.0;
            double power2 = running ? 0.65 : 0.0;
            intake.setPower(power);
            transfer.setPower(power);
            outtake.setPower(power2);

            telemetry.addData("Motors", running ? "ON" : "OFF");
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