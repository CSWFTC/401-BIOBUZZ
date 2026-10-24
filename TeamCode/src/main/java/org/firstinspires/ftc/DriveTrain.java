package org.firstinspires.ftc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.Helper.Hardware;

@TeleOp(name = "Drivetrain Motor Test")
public class DriveTrain extends LinearOpMode {
    @Override
    public void runOpMode() {
        Hardware.init(hardwareMap);
        Hardware.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Hardware.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Hardware.rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Hardware.rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Hardware.outtake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        waitForStart();
        Gamepad old = new Gamepad();
        boolean fl = false, fr = false, rl = false, rr = false;
        while (opModeIsActive()) {
            if (gamepad1.a && !old.a)
                rl = !rl;
            if (gamepad1.b && !old.b)
                rr = !rr;
            if (gamepad1.x && !old.x)
                fl = !fl;
            if (gamepad1.y && !old.y)
                fr = !fr;
            gamepad1.copy(old);
            Hardware.frontLeft.setPower(fl ? 1 : 0);
            Hardware.frontRight.setPower(fr ? 1 : 0);
            Hardware.rearLeft.setPower(rl ? 1 : 0);
            Hardware.rearRight.setPower(rr ? 1 : 0);
            /*Hardware.outtakeTop.setPower(fl ? 1 : 0);
            Hardware.outtakeMiddle.setPower(fr ? 1 : 0);
            Hardware.outtakeBottom.setPower(rl ? 1 : 0);
            Hardware.intake.setPower(rr ? 1 : 0);*/
            telemetry.addData("Rear Left (A)", rl);
            telemetry.addLine();
            telemetry.addData("Rear Right (B)", rr);
            telemetry.addLine();
            telemetry.addData("Front Left (X)", fl);
            telemetry.addLine();
            telemetry.addData("Front Right (Y)", fr);
            telemetry.addLine();
            telemetry.update();
        }
    }
}
