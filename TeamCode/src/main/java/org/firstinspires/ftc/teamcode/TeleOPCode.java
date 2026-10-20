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

    String color = "";

    double shooterP = 0;
    double shooterF = 0;
    double ShootingVelocity = 0;

    double highVelo = 1500;
    double lowVelo = 1000;

    double currentTarget = highVelo;
    double [] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};

    int stepIndex = 1;

    double motorSpeed = 1;



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

//                if(gamepad2.triangleWasPressed())
//                {
//                    if(currentTarget == highVelo)
//                    {
//                        currentTarget = lowVelo;
//                    }
//                    else
//                    {
//                        currentTarget = highVelo;
//                    }
//                }
//
//                if(gamepad2.crossWasPressed())
//                {
//                    stepIndex = (stepIndex + 1) % stepSizes.length;
//                }
//
//                if(gamepad2.squareWasPressed())
//                {
//                   shooterF -= stepSizes[stepIndex];
//                }
//                if (gamepad2.circleWasPressed())
//                {
//                    shooterF += stepSizes[stepIndex];
//                }
//
//                if(gamepad2.dpadUpWasPressed())
//                {
//                    shooterP -= stepSizes[stepIndex];
//                }
//                if (gamepad2.dpadDownWasPressed())
//                {
//                    shooterP += stepSizes[stepIndex];
//                }

//                pidfCoefficients = new PIDFCoefficients(shooterP, 0, 0, shooterF);
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



//        motorSpeed = moveSpeeds[speedIndex];





    public void Omnimovement()
    {
        if (Math.abs(gamepad1.left_stick_y) > 0.25  || Math.abs(gamepad1.left_stick_x) >0.25) {
            //checks to see if the thumbstick is moved at least a quarter in either direction.

            double vector_Add = (-gamepad1.left_stick_y) + gamepad1.left_stick_x;
                /*
                vector_add first takes the left stick y coordinate and fixes its idiosyncrasy (In
                that forward is -1.0 and backward is +1.0) by multiplying it by -1. Then through a
                process known as vector addition, we arrive at the final vector needed for the motor
                speed.

                !!!!See https://www.youtube.com/watch?v=D5pQCy5dvlc for more information about
                vector addition in linear algebra!!!!!

                 */
            double vector_Sub = (-gamepad1.left_stick_y) - gamepad1.left_stick_x;
                /*
                vector_sub follows the same rules prior except for one minor detail: while certain
                motors are facing the correct direction, the motors on the opposite side are facing
                subsequently facing the opposite direction and need specialized inputs to account
                for that issue. This fixes the opposite direction issue by subtracting instead of
                adding (essentially addition but multiplying the x coordinate by -1 [thereby making
                the x-vector face in the opposite direction]) to account for this very issue.
                 */

            int negative_Add = vector_Add<0?-1:1;
            int negative_Sub = vector_Sub<0?-1:1;
                /*
                Just checks whether the vectors point in a positive direction and stores its sign.
                For instance, if negative_Add is greater than 0, then it is positive and gets 1.
                This is done for when the vector addition results in a number that is greater than
                the bounds of the setPower function (that being from -1 to 1). This becomes
                important when pushing the vector information into the set Power function

                -----------------------------------------------------------------------------------
                IMPORTANT!

                The '?' character used is called a ternary operator. This operator follows the
                format of "(Boolean) ? (Option 1) : (Option 2)". You can think of this as a quick
                and short if and else statement--as that is what is is. The colon(':') essentially
                acts like an else (Otherwise an OR colloquial language). It First asks the question
                "Is this boolean statement true?" if it is true, then Option 1 is returned; if not,
                then Option 2 is returned. For clarification, the boolean statement can be anything
                as long as it results as a boolean, such as a comparative operator (<,>,==, ect.),
                functions, and primitives.
                -----------------------------------------------------------------------------------
                 */


            frontLeft.setPower(Math.abs(vector_Add)>1? negative_Add : vector_Add);
            frontRight.setPower(Math.abs(vector_Sub)>1? negative_Sub : vector_Sub);
            backLeft.setPower(Math.abs(vector_Sub)>1? negative_Sub : vector_Sub);
            backRight.setPower(Math.abs(vector_Add)>1? negative_Add : vector_Add);
                /*
                Here the ternary operator is used to check if the absolute value of the vector is
                greater than 1. For instance, if the function setPower represented a circle with a
                radius of 1 centered around the origin (0,0), then this comparison just checks if
                the vector ends at a point outside the circle. If in the case that it is greater
                than 1, then it defaults to the default value saved as negative_Add in order to stay
                Within bounds. No important information is lost in this process as the ONLY CASE IN
                WHICH A VECTOR HAS A MAGNITUDE GREATER THAN 1 IS IN EITHER ADDITION OR SUBTRACTION,
                NEVER BOTH. The exact same process is followed for both vector_Add and vector_Sub,
                the only difference being that each is speciallized for its own vector information.
                 */
        }
        else{
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
            //sets motors to zero if joystick is not moved.
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
            intake.setPower(1)                                                                                                                                                                                                 ;
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

