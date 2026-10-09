package org.firstinspires.ftc.teamcode;

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

@TeleOp(name = "Testing")
public class TeleOPCode extends LinearOpMode
{
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    //ColorSensor colorSensor;

    DcMotor intake;

    //DcMotorEx shooter;

    String color = "";

    double shooterP = 0;
    double shooterF = 0;
    double ShootingVelocity = 0;

    double highVelo = 1500;
    double lowVelo = 1000;

    double currentTarget = highVelo;
    double [] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};

    int stepIndex = 1;



    @Override
    public void runOpMode()
    {
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

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(shooterP, 0 , 0, shooterF);
        //shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        waitForStart();
        telemetry.addData("Telemetry", "Called");




            while (opModeIsActive())
            {
                telemetry.addData("X: ", gamepad1.left_stick_x);
                telemetry.addData("Y: ", gamepad1.left_stick_y);
//                telemetry.addData("Color Red", colorSensor.red());
//                telemetry.addData("Color Blue", colorSensor.blue());
//                telemetry.addData("Color Green", colorSensor.green());
                telemetry.addData("Ball Color", color);

                telemetry.update();

                if(gamepad2.triangleWasPressed())
                {
                    if(currentTarget == highVelo)
                    {
                        currentTarget = lowVelo;
                    }
                    else
                    {
                        currentTarget = highVelo;
                    }
                }

                if(gamepad2.crossWasPressed())
                {
                    stepIndex = (stepIndex + 1) % stepSizes.length;
                }

                if(gamepad2.squareWasPressed())
                {
                   shooterF -= stepSizes[stepIndex];
                }
                if (gamepad2.circleWasPressed())
                {
                    shooterF += stepSizes[stepIndex];
                }

                if(gamepad2.dpadUpWasPressed())
                {
                    shooterP -= stepSizes[stepIndex];
                }
                if (gamepad2.dpadDownWasPressed())
                {
                    shooterP += stepSizes[stepIndex];
                }

                pidfCoefficients = new PIDFCoefficients(shooterP, 0, 0, shooterF);
//                shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
//                shooter.setVelocity(currentTarget);
//
//                double currVelo = shooter.getVelocity();
//                double error = currentTarget - currVelo;

//                telemetry.addData("Target Velo", currentTarget);
//                telemetry.addData("Current Velo", currVelo);
//                telemetry.addData("Error", "%.2f", error);
                telemetry.addLine("--------------------------------");
                telemetry.addData("Tuning P", "%.4f", shooterP);
                telemetry.addData("Tuning F", "%.4f", shooterF);
                telemetry.addData("Step Size", "%.4f", stepSizes[stepIndex]);


                //BallColor();
                Omnimovement();
                Intakes();
                //Shooting();
            }
    }




    public void Omnimovement()
    {
        //Uses a Y-Vector and X-Vector, adds them to get
        // forward
        if (gamepad1.left_stick_y > 0.5 )
        {
            frontLeft.setPower(-1);
            frontRight.setPower(-1);
            backLeft.setPower(-1);
            backRight.setPower(-1);
        }
        // backward
        else if (gamepad1.left_stick_y < -0.5 )
        {
            frontLeft.setPower(1);
            frontRight.setPower(1);
            backLeft.setPower(1);
            backRight.setPower(1);
        }
        // strafe right
        else if (gamepad1.left_stick_x > 0.5 )
        {
            frontLeft.setPower(-1);
            frontRight.setPower(1);
            backLeft.setPower(1);
            backRight.setPower(-1);
        }
        // strafe left
        else if (gamepad1.left_stick_x < -0.5 )
        {
            frontLeft.setPower(1);
            frontRight.setPower(-1);
            backLeft.setPower(-1);
            backRight.setPower(1);
        }
        else
        {
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }
        if(Math.abs(gamepad1.right_stick_x) > 0.25)
        {
            //turn clock wise
            frontLeft.setPower(gamepad1.right_stick_x);
            frontRight.setPower(-gamepad1.right_stick_x);
            backLeft.setPower(gamepad1.right_stick_x);
            backRight.setPower(-gamepad1.right_stick_x);
        }
        else
        {
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }

    }

    public void Intakes()
    {
        if(gamepad1.rightTriggerWasPressed())
        {
            intake.setPower(1);
        }
        else if(gamepad1.rightTriggerWasReleased())
        {
            intake.setPower(0);
        }
        else if(gamepad1.leftTriggerWasPressed())
        {
            intake.setPower(-1);
        }
        else if(gamepad1.leftTriggerWasReleased())
        {
            intake.setPower(0);
        }
    }

//    public void Shooting()
//    {
//        if(gamepad2.rightTriggerWasPressed())
//        {
//            shooter.setPower(ShootingVelocity);// a number that will work after you test it
//        }
//        else if(gamepad2.rightTriggerWasReleased())
//        {
//            shooter.setPower(0);
//        }
//
//
//    }

//
//    public void BallColor()
//    {
//        if (colorSensor.red() > 200 || colorSensor.blue() > 200 || colorSensor.green() > 200)
//        {
//            if (colorSensor.red() > colorSensor.blue() && colorSensor.red() > colorSensor.green())
//            {
//                color = "Red";
//            }
//            else if (colorSensor.blue() > colorSensor.red() && colorSensor.blue() > colorSensor.green())
//            {
//
//                color = "Blue";
//            }
//            else if (colorSensor.green() > colorSensor.red() && colorSensor.green() > colorSensor.blue())
//            {
//                color = "Yellow";
//            }
//        }
//        else
//        {
//            color = "No Ball";
//        }
//    }

}

