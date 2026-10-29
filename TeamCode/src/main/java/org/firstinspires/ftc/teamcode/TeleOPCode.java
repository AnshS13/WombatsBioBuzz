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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.annotations.DigitalIoDeviceType;
//This is a test comment in the local file
//Now I am dangerously changing in the remote.

@TeleOp(name = "Tele-Op Team Code")
public class TeleOPCode extends LinearOpMode
{
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    //ColorSensor colorSensor;

    DcMotor intake;

    //DcMotorEx shooter;
    Servo flowerRamp;
    String color = "";

    double shooterP = 3000;
    double shooterF = 0.00047619;
    double ShootingVelocity = 0;

    double highVelo = 1500;
    double lowVelo = 1000;

    double currentTarget = highVelo;
    double [] stepSizes = {10.0, 1.0, 0.1, 0.01, 0.001, 0.0001};

    int stepIndex = 1;

    double motorSpeed = 1;
    DcMotor turretMotor;
    DcMotorEx flywheel;


    @Override
    public void runOpMode()
    {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        turretMotor = hardwareMap.get(DcMotor.class, "turretMotor"); // changed this
        // this could be a servo. Khaian said it was a motor but double-check.
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //colorSensor = hardwareMap.get(ColorSensor.class, "color");
        

        intake = hardwareMap.get(DcMotor.class, "intake");

        flowerRamp = hardwareMap.get(Servo.class, "flowerRamp");

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(shooterP, 0 , 0, shooterF);
        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
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
                telemetry.addData("Flywheel Speed", flywheel.getVelocity());

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

                double currVelo = flywheel.getVelocity();
                double error = currentTarget - currVelo;

                pidfCoefficients = new PIDFCoefficients(shooterP, 0 , 0, shooterF);
                flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

                telemetry.addData("Target Velo", currentTarget);
                telemetry.addData("Current Velo", currVelo);
                telemetry.addData("Error", "%.2f", error);
                telemetry.addLine("--------------------------------");
                telemetry.addData("Tuning P", "%.10f", shooterP);
                telemetry.addData("Tuning F", "%.10f", shooterF);
                telemetry.addData("Step Size", "%.10f", stepSizes[stepIndex]);
                telemetry.addData("Ramp Servo Position" , flowerRamp.getPosition());


                //BallColor();
                Omnimovement();
                Intakes();
                rampMovement();
                // both of these classes are untested as of 10/22/2026 at 2:21pm
                Turret();
                Flywheel();
                //Shooting();
            }
    }



//        motorSpeed = moveSpeeds[speedIndex];





    public void Omnimovement()
    {
        if (Math.abs(gamepad1.left_stick_y) > 0.25  || Math.abs(gamepad1.left_stick_x) >0.25||
            Math.abs(gamepad1.right_stick_x) > 0.2 ){
            //checks to see if the thumbstick is moved at least a quarter in either direction.

            double frontLeft_Vector =  -gamepad1.left_stick_y + gamepad1.left_stick_x
                    + gamepad1.right_stick_x;
                /*
                vector_add first takes the left stick y coordinate and fixes its idiosyncrasy (In
                that forward is -1.0 and backward is +1.0) by multiplying it by -1. Then through a
                process known as vector addition, we arrive at the final vector needed for the motor
                speed.

                !!!!See https://www.youtube.com/watch?v=D5pQCy5dvlc for more information about
                vector addition in linear algebra!!!!!

                 */
            double backRight_Vector = (-gamepad1.left_stick_y) + gamepad1.left_stick_x - gamepad1.right_stick_x;

            double frontRight_Vector = -gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;
                /*
                vector_sub follows the same rules prior except for one minor detail: while certain
                motors are facing the correct direction, the motors on the opposite side are facing
                subsequently facing the opposite direction and need specialized inputs to account
                for that issue. This fixes the opposite direction issue by subtracting instead of
                adding (essentially addition but multiplying the x coordinate by -1 [thereby making
                the x-vector face in the opposite direction]) to account for this very issue.
                 */
            double backLeft_Vector = -gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x;

            double ratioControl = Math.max(Math.abs(frontLeft_Vector),Math.abs(frontRight_Vector));
            ratioControl = Math.max(ratioControl, Math.abs(backLeft_Vector));
            ratioControl = Math.max(ratioControl,Math.abs(backRight_Vector));
            ratioControl = Math.max(ratioControl, 1.0);
            /*
            ratioControl effectively handles the data loss; this is vector normalization.
            whereas cutting off each vector such that each vector was individually checked for
            if they were outside the bounds of the setPower function, this uses a ratio so that
            they are still within bounds. The issue with the previous fix was that ratios were
            lost when they were cut off, such as vectors 1.4 and 1.8 (information is lost, where
            the fact that the 0.4 and 0.8 are necessary for diagonals). Instead, you take the
            largest value and divide each vector by that largest value to keep those ratios but
            keep them within the bounds. if no vector has a magnitude has a value over 1.0, then
            the 1.0 is the base case so that slow movement can be achieved.
            */

            frontLeft.setPower(frontLeft_Vector / ratioControl * 0.75);
            frontRight.setPower(frontRight_Vector / ratioControl * 0.75);
            backLeft.setPower(backLeft_Vector / ratioControl * 0.75);
            backRight.setPower(backRight_Vector / ratioControl * 0.75);

        }
        else{
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
            //sets motors to zero if joystick is not moved.
        }

        /*
        if(Math.abs(gamepad1.right_stick_x) > 0.5)
        {
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


         */

    }
    
    // this is untested as of 10/22/2026 at 2:21pm
    public void Turret() // changed this
    {
        if(gamepad2.right_bumper)
        {
            turretMotor.setPower(0.3);
        }
        else if(gamepad2.left_bumper)
        {
            turretMotor.setPower(-0.3);
        }
        else
        {
            turretMotor.setPower(0);
        }
    }

    public void Flywheel()
    {
        if(gamepad2.right_trigger >= 0.5)
        {
            flywheel.setVelocity(currentTarget);
        }
        else if(gamepad2.right_trigger <= 0.2)
        {
            flywheel.setVelocity(0);
        }
    }
    public void Intakes()
    {
        if(gamepad1.rightTriggerWasPressed())
        {
            intake.setPower(-1);
        }
        else if(gamepad1.rightTriggerWasReleased())
        {
            intake.setPower(0);
        }
        else if(gamepad1.leftTriggerWasPressed())
        {
            intake.setPower(0.3)                                                                                                                                                                                                 ;
        }
        else if(gamepad1.leftTriggerWasReleased())
        {
            intake.setPower(0);
        }
    }

    public void rampMovement()
    {
        if(gamepad1.crossWasPressed())
        {
            flowerRamp.setPosition(0.08);
        }
        else if (gamepad1.triangleWasPressed())
        {
            flowerRamp.setPosition(1);
        }
    }





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

