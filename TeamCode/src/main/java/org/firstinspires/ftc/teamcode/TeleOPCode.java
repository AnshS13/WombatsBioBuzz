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

    double motorSpeed;



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
                ChangeMotorPowerSpeed();
            }
    }


    public void ChangeMotorPowerSpeed()//changes MOTOR MOVEMENT Speed using M1 and M2
    {
        telemetry.addData("setPowerSpeed", "called");
        if (gamepad1.dpadUpWasPressed())//M1
        {
            telemetry.addData("dpad_up", "called");
            //If motor speed is less then 1 then increase by .1
//            speedIndex += speedIndex < moveSpeeds.length - 1 ? 1 : 0;
            if ((motorSpeed < 1))
            {
                motorSpeed += 0.2;
                telemetry.addData("Motor Speed is : ", motorSpeed);
            }
            if(motorSpeed > 0.5)
            {
                motorSpeed = 1;
            }
        }
        if (gamepad1.dpadDownWasPressed())//M2
        {
            telemetry.addData("dpad_down", "called");
            //If motor speed is greater then -1 then decrease by .1
//            speedIndex += speedIndex > 0 ? -1 : 0;
            if(motorSpeed > 0.3)
            {
                motorSpeed-=0.2;
            }
            if(motorSpeed > 0.5)
            {
                motorSpeed = 0.5;
            }
            telemetry.addData("Motor Speed is : ", motorSpeed);
        }

//        motorSpeed = moveSpeeds[speedIndex];
    }
    public void setMotorsPower(double fLSpeed, double fRSpeed, double bLSpeed, double bRSpeed)//function to set all motors to the same speed
    {
        frontLeft.setPower(fLSpeed);
        frontRight.setPower(fRSpeed);
        backLeft.setPower(bLSpeed);
        backRight.setPower(bRSpeed);
    }


    public void Omnimovement() {
        telemetry.addData("joystick X: ", gamepad1.left_stick_x);
        telemetry.addData("joystick Y: ", gamepad1.left_stick_y);
        /* Checking if controller is going right
        Checks if x is on the right side (x is greater than 0)
        Checks if y is on the y-axis (y is between 0.5 and -0.5)
        */
        if (gamepad1.left_stick_x > 0 && (gamepad1.left_stick_y > -0.5 && gamepad1.left_stick_y < 0.5)) {
            setMotorsPower(-motorSpeed, motorSpeed, motorSpeed, -motorSpeed);
            telemetry.addData("Direction: ", "Right");
        }

        /* Checking if controller is going left
        Checks if x is on the left side (x is less than 0)
        Checks if y is on the y-axis (y is between 0.5 and -0.5)
        */
        else if (gamepad1.left_stick_x < 0 && (gamepad1.left_stick_y > -0.5 && gamepad1.left_stick_y < 0.5)) {
            setMotorsPower(motorSpeed, -motorSpeed, -motorSpeed, motorSpeed);
            telemetry.addData("Direction: ", "Left");
        }
        /* Checking if controller is going up
        Checks if x is on the x-axis (x is between 0.5 and -0.5)
        Checks if y is on the up side (y is less than 0)
        */
        else if (gamepad1.left_stick_y < 0 && (gamepad1.left_stick_x > -0.5 && gamepad1.left_stick_x < 0.5)) {
            setMotorsPower(-motorSpeed, -motorSpeed, -motorSpeed, -motorSpeed);
            telemetry.addData("Direction: ", "Up");
        }
        /* Checking if controller is going down
        Checks if x is on the x-axis (x is between 0.5 and -0.5)
        Checks if y is on the bottom side (y is greater than 0)
        */
        else if (gamepad1.left_stick_y > 0 && (gamepad1.left_stick_x > -0.5 && gamepad1.left_stick_x < 0.5)) {
            setMotorsPower(motorSpeed, motorSpeed, motorSpeed, motorSpeed);
            telemetry.addData("Direction: ", "Down");
        }
        /* Checking if controller is going up-right
        Checks if x is on the right side (x is greater than 0.5)
        Checks if y is on the top side (y is less than -0.5)
        */
        else if (gamepad1.left_stick_y < -0.5 && gamepad1.left_stick_x > 0.5) {
            setMotorsPower(-motorSpeed, 0, 0, motorSpeed);
            telemetry.addData("Direction: ", "Up-Right");
        }
        /* Checking if controller is going up-left
        Checks if x is on the left side (x is less than -0.5)
        Checks if y is on the top side (y is less than -0.5)
        */
        else if (gamepad1.left_stick_y < -0.5 && gamepad1.left_stick_x < -0.5) {
            setMotorsPower(0, -motorSpeed, -motorSpeed, 0);
            telemetry.addData("Direction: ", "Up-Left");
        }
        /* Checking if controller is going down-right
        Checks if x is on the right side (x is greater than 0.5)
        Checks if y is on the bottom side (y is greater than 0.5)
        */
        else if (gamepad1.left_stick_y > 0.5 && gamepad1.left_stick_x > 0.5) {
            setMotorsPower(0, motorSpeed, motorSpeed, 0);
            telemetry.addData("Direction: ", "Down-Right");
        }
        /* Checking if controller is going down-left
        Checks if x is on the left side (x is less than -0.5)
        Checks if y is on the bottom side (y is greater than 0.5)
        */
        else if (gamepad1.left_stick_y > 0.5 && gamepad1.left_stick_x < -0.5) {
            setMotorsPower(motorSpeed, 0, 0, motorSpeed);
            telemetry.addData("Direction: ", "Down-Left");
        }
        /* If the controller is going nowhere else, it stops the robot
        Sets the power to all wheels to 0
        */
        else
        {
            setMotorsPower(0,0,0,0);
        }
        if(Math.abs(gamepad1.right_stick_x) > 0.25)
        {
            //turns the robot
            setMotorsPower(gamepad1.right_stick_x,-gamepad1.right_stick_x,gamepad1.right_stick_x,-gamepad1.right_stick_x);
        }
        else
        {
            setMotorsPower(0,0,0,0);
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

