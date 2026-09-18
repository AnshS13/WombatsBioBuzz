package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name = "Testing2")
public class TeleOPCode extends LinearOpMode {
    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
//        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        waitForStart();
        telemetry.addData("Telemetry", "Called");



        while (opModeIsActive()) {
            telemetry.addData("X: ", gamepad1.left_stick_x);
            telemetry.addData("Y: ", gamepad1.left_stick_y);
            telemetry.update();

            Omnimovement();
        }


    }
    public void Omnimovement()
    {
        //Uses a Y-Vector and X-Vector, adds them to get
        if (Math.abs(gamepad1.left_stick_y) > 0.25  || Math.abs(gamepad1.left_stick_x) >0.25) {
            double vector_Add =  (-1 * gamepad1.left_stick_y) + gamepad1.left_stick_x;
            double vector_Sub =  (-1 * gamepad1.left_stick_y) - gamepad1.left_stick_x;
            int negative_Add = vector_Add<0?-1:1;
            int negative_sub = vector_Sub<0?-1:1;
            double f_Vector_add = Math.abs(vector_Add)>1? negative_Add : vector_Add;
            double f_Vector_sub = Math.abs(vector_Sub)>1? negative_sub : vector_Sub;


            frontLeft.setPower(f_Vector_add);
            frontRight.setPower(f_Vector_sub);
            backLeft.setPower(f_Vector_sub);
            backRight.setPower(f_Vector_add);

            if(gamepad1.right_stick_x > 0.25)
            {
                //turn clock wise
                frontLeft.setPower(1);
                frontRight.setPower(-1);
                backLeft.setPower(1);
                backRight.setPower(-1);
            }
            if(gamepad1.right_stick_x < -0.25)
            {
                //turn counter clock wise
                frontLeft.setPower(-1);
                frontRight.setPower(1);
                backLeft.setPower(-1);
                backRight.setPower(1);
            }
        }
        else{
            frontLeft.setPower(0);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);
        }
    }
}

