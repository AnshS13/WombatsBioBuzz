package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.annotations.DigitalIoDeviceType;
//This is a test comment in the local file
//Now I am dangerously changing in the remote.

@Autonomous(name = "Standard Auton Code", group = "Autonomous")
public class StandardAuto extends LinearOpMode {
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    //ColorSensor colorSensor;

    DcMotor intake;

   // DcMotorEx shooter;

    String color = "";

    double shooterP = 0;
    double shooterF = 0;
    double ShootingVelocity = 0;

    double highVelo = 1500;
    double lowVelo = 1000;

    double currentTarget = highVelo;
    double[] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};

    int stepIndex = 1;

    double motorSpeed = 1;


    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        //colorSensor = hardwareMap.get(ColorSensor.class, "color");

        intake = hardwareMap.get(DcMotor.class, "intake");
        //shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        //shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(shooterP, 0, 0, shooterF);
        //shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        waitForStart();
        telemetry.addData("Telemetry", "Called");

        if (opModeIsActive())
        {
            //shooter.setPower(1267);
            intake.setPower(1267);
            sleep(3000);
            //shooter.setPower(0);
            turnLeft(1000);

        }
    }
    public void setMotorsPower(double fLSpeed,double fRSpeed,double rLSpeed,double rRSpeed)//function to set all motors to the same speed
    {
        //set the powers to drive the robot
        frontLeft.setPower(fLSpeed);
        frontRight.setPower(fRSpeed);
        backLeft.setPower(-rLSpeed);
        backRight.setPower(-rRSpeed);
    }
    public void turnLeft(int time)
    {
        setMotorsPower(0,0.5,0.5,0);
        sleep(time);
        setMotorsPower(0,0,0,0);
    }

}



