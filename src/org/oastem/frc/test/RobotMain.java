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
    private Joystick js, js2;
    
    private DigitalInput lim;
    
    private Jaguar motor1;
    
    String[] debug = new String[6];
    
    private final int LEFT_DRIVE_PORT = 1;
    private final int RIGHT_DRIVE_PORT = 2;
    
    private final int FIRST_JOYSTICK = 1;
    private final int SECOND_JOYSTICK = 2;
    
    private final int JOYSTICK_BUTTON = 2; 
    
    private final double AUTO_SPEED = 0.5;
    
    private final int LIMIT_SWITCH_PORT = 1;
    private final int JAGUAR_PORT = 1;
    
    private final int RUN_TIME = 5000;
    
    private Encoder encoder;
    private ADW22307Gyro gyro;
    
    private final int ENCODER_PORT_A = 1;
    private final int ENCODER_PORT_B = 2;
    private final int GYRO_PORT = 3;
    
    
    public void robotInit(){
        //ds = DriveSystem.getInstance();
        //ds.initializeDrive(LEFT_DRIVE_PORT, RIGHT_DRIVE_PORT);
        
        js = new Joystick(FIRST_JOYSTICK);
        encoder = new Encoder(ENCODER_PORT_A, ENCODER_PORT_B);
        gyro = new ADW22307Gyro(GYRO_PORT);
        
        
       
        
        motor1 = new Jaguar(JAGUAR_PORT);
        
        Debug.clear();
        Debug.log(1, 1, "Robot initialized.");
    }
    
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        long currentTime;
        long startTime = 0;
        boolean motorStart = false;
        while(isEnabled() && isOperatorControl()){
            currentTime = System.currentTimeMillis();
            /*debug[1] = "Drive Speed: " + js.getY() + ", " + js2.getY();
            ds.tankDrive(js.getY(), js2.getY());
            
            if (js.getRawButton(JOYSTICK_BUTTON) || motorStart){
                if(!motorStart){
                    motorStart = true;
                    startTime = currentTime;
                }
                motor1.set(0.1);
                if (!lim.get() || currentTime - startTime >= RUN_TIME){
                    motor1.set(0.0);
                    debug[2] = "Limit engaged, stopping motor at " + currentTime;
                    motorStart = false;
                } else {
                    debug[2] = "Still going at time " + currentTime;
                }
            }
            */
            
            motor1.set(js.getY());
            debug[0] = "Rate: " + encoder.getRate();
            debug[1] = "Angle: " + gyro.getAngle();
            debug[2] = "how to do";
            Debug.log(debug);
        }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
}
