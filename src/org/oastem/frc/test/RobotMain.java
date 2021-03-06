/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.oastem.frc.test;


import edu.wpi.first.wpilibj.*;
import org.oastem.frc.control.*;
import org.oastem.frc.Debug;
import org.oastem.frc.sensor.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends SimpleRobot {
    
    private DriveSystem ds;
    private Joystick js;
    
    private Jaguar motor1;
    private Jaguar motor2;
    private Jaguar motor3;
    private Jaguar motor4;
    
    String[] debug = new String[6];
    
    private final int LEFT_FRONT_DRIVE_PORT = 1;
    private final int LEFT_BACK_DRIVE_PORT = 2;
    private final int RIGHT_FRONT_DRIVE_PORT = 3;
    private final int RIGHT_BACK_DRIVE_PORT = 4;
    
    private final int JOYSTICK = 1; 
    
    private final int WHEEL_CIRCUMFERENCE = 7;
    private final int ENCODER_CH_A = 14;
    private final int ENCODER_CH_B = 13;
    private final int ENC_JAG_PORT = 2;
    private final int CHAIN_UP_BUTTON = 11;
    private final int CHAIN_DOWN_BUTTON = 10;

    
    private QuadratureEncoder encoder;
    private ADW22307Gyro gyro;
    /*
    private Compressor compress;
    private DoubleSolenoid solen;
    private Relay rel;
    //*/
    
    private final int GYRO_PORT = 1;
    /*
    private final int PRESSURE_SWITCH_CHANNEL = 3;
    private final int COMP_RELAY_CHANNEL = 1;
    private final int SOLEN_FORWARD_CHANNEL = 1;  // FIGURE OUT THE ACTUAL NUMBER
    private final int SOLEN_BACKWARD_CHANNEL = 2; // FIGURE OUT THE ACTUAL NUMBER
    
    private final int SOL_FORWARD_BUTTON = 4;
    private final int SOL_REVERSE_BUTTON = 5;
    //*/
    
    public void robotInit(){
        /*ds = DriveSystem.getInstance();
        ds.initializeDrive(LEFT_FRONT_DRIVE_PORT, LEFT_BACK_DRIVE_PORT, 
                RIGHT_FRONT_DRIVE_PORT, RIGHT_BACK_DRIVE_PORT);
        
        motor1 = new Jaguar(LEFT_FRONT_DRIVE_PORT);
        motor2 = new Jaguar(LEFT_BACK_DRIVE_PORT);
        motor3 = new Jaguar(RIGHT_FRONT_DRIVE_PORT);
        motor4 = new Jaguar(RIGHT_BACK_DRIVE_PORT);
        */
        js = new Joystick(JOYSTICK);

        motor1 = new Jaguar(ENC_JAG_PORT);

        
        //encoder = new Encoder(ENCODER_CH_A, ENCODER_CH_B);
        encoder = new QuadratureEncoder(ENCODER_CH_A, ENCODER_CH_B, true, 4, 479);
        encoder.setDistancePerPulse(WHEEL_CIRCUMFERENCE);
        
        gyro = new ADW22307Gyro(GYRO_PORT);
        /*
        compress = new Compressor(PRESSURE_SWITCH_CHANNEL, COMP_RELAY_CHANNEL);
        //compress.start();
        solen = new DoubleSolenoid(SOLEN_FORWARD_CHANNEL, SOLEN_BACKWARD_CHANNEL);
        //*/
        
        Debug.clear();
        Debug.log(1, 1, "Robot initialized.");
    }
    
    
    

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        long currentTime;
        long startTime = 0;
        boolean motorStart = false;
        encoder.reset();
        double wantedDistance = 0;
        boolean canPress = true;
        Debug.clear();
        while(isEnabled() && isOperatorControl()){
            Debug.clear();
            currentTime = System.currentTimeMillis();
            //debug[0] = "Drive Speed: " + js.getY();
            //ds.mecanumDrive(js.getX(), js.getY(), js.getZ(), gyro.getAngle());
            
            if(encoder.getDistance() < wantedDistance - 2)
                motor1.set(-50);
            else if (wantedDistance + 2 < encoder.getDistance())
                motor1.set(50);
            else
                motor1.set(0);
            if (!js.getRawButton(CHAIN_UP_BUTTON) && !js.getRawButton(CHAIN_DOWN_BUTTON))
                canPress = true;
            
            if (encoder.isStopped() && js.getRawButton(CHAIN_UP_BUTTON) && canPress)
            {
                wantedDistance += 15;
                canPress = false;
            }
            
            else if (encoder.isStopped() && js.getRawButton(CHAIN_DOWN_BUTTON) && canPress)
            {
                wantedDistance -= 15;
                canPress = false;
            }
            //motor1.set(js.getY());
            
            debug[0] = "Distance: " + encoder.getDistance();
            debug[1] = "Wanted: " + wantedDistance;
            
            
            
            /*
            if (js.getRawButton(SOL_FORWARD_BUTTON))
            {
                solen.set(DoubleSolenoid.Value.kForward);
                debug[1] = "solen FORWARD";
            }
            else if (js.getRawButton(SOL_REVERSE_BUTTON))
            {
                solen.set(DoubleSolenoid.Value.kReverse);
                debug[1] = "solen REVERSE";
            }
            else
            {
                solen.set(DoubleSolenoid.Value.kOff);
                debug[1] = "solen OFF";
            }
            //*/

            //debug[1] = "Gyro: " + gyro.getAngle();
            Debug.log(debug);
        }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
    
    public void disabled()
    {
        
    }
}
