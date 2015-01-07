/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.oastem.frc.assist2;


import edu.wpi.first.wpilibj.*;
import org.oastem.frc.control.*;
import org.oastem.frc.Debug;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends SimpleRobot {
    
    private Gyro gyro;
    private DriveSystem ds;
    private Joystick js;
    
    private DigitalInput lim;
    
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
    
    private final int JOYSTICK_BUTTON = 2; 
    
    private final double AUTO_SPEED = 0.5;
    
    private final int LF_MOTOR = 1;
    private final int LB_MOTOR = 2;
    private final int RF_MOTOR = 3;
    private final int RB_MOTOR = 4;
    
    private final int GYRO = 1;
    
    private final int RUN_TIME = 5000;
    
    
    public void robotInit(){
        ds = DriveSystem.getInstance();
        ds.initializeDrive(LEFT_FRONT_DRIVE_PORT, LEFT_BACK_DRIVE_PORT, 
                RIGHT_FRONT_DRIVE_PORT, RIGHT_BACK_DRIVE_PORT);
        
        js = new Joystick(JOYSTICK);
        
        motor1 = new Jaguar(LF_MOTOR);
        motor2 = new Jaguar(LB_MOTOR);
        motor3 = new Jaguar(RF_MOTOR);
        motor4 = new Jaguar(RB_MOTOR);
        
        gyro = new Gyro(GYRO);
        
        Debug.clear();
        Debug.log(1, 1, "Robot initialized.");
    }
    
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        long currentTime;
        long startTime = 0;
        boolean motorStart = false;
        while(isEnabled() && isOperatorControl()){
            currentTime = System.currentTimeMillis();
            debug[1] = "Drive Speed: " + js.getY();
            ds.mecanumDrive(js.getX(), js.getY(), js.getZ(), gyro.getAngle());
            
            if (js.getRawButton(JOYSTICK_BUTTON) || motorStart){
                if(!motorStart){
                    motorStart = true;
                    startTime = currentTime;
                }
                
            }
            Debug.log(debug);
        }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
    
    }
}
