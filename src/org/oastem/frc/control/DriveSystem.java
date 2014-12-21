package org.oastem.frc.control;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Victor;
import java.util.Hashtable;

/**
 * Handles the drive train for the FRC robot. Plugs into all the motor
 * controllers used for driving.
 *
 * @author KTOmega
 */
public class DriveSystem {
	// Use a singleton pattern because we only have one drive system
	// per robot.
    protected static DriveSystem instance;
    
    // The WPILibJ class that handles robot driving natively.
    protected RobotDrive drive;
    
    // All the victors we will be using.
    protected Victor[] raw;
    
    // Has secondary drive?
    protected boolean hasSecondary = false;
    
    // Used only for secondary drive system.
    protected RobotDrive drive2;
    
    protected DriveSystem() {
        raw = new Victor[12];
    }
    
    public static DriveSystem getInstance() {
        if (instance == null) {
            instance = new DriveSystem();
        }
        
        return instance;
    }
    
    public void initializeDrive(int leftFront, int leftRear, int rightFront, int rightRear) {
        drive = new RobotDrive(leftFront, leftRear, rightFront, rightRear);
    }
    
    public void initializeDrive(int left, int right){
        drive = new RobotDrive(left, right);
    }
    
    public void setDrive(RobotDrive rd) {
        drive = rd;
    }
    
    public void initializeSecondaryDrive(int l2, int r2) {
        drive2 = new RobotDrive(l2, r2);
        hasSecondary = true;
    }
    
    public void setSecondaryDriver(RobotDrive rd) {
        drive2 = rd;
        hasSecondary = true;
    }
    
    public void arcadeDrive(Joystick joystick){
        drive.arcadeDrive(joystick);
    }
    
    public void arcadeDrive(double forward, double turn) {
        drive.arcadeDrive(forward, turn);
        if (hasSecondary) drive2.arcadeDrive(forward, turn);
    }
    
    public void tankDrive(double x, double y) {
        drive.tankDrive(x, y);
        if (hasSecondary) drive2.tankDrive(x, y);
    }
    
    public void addVictor(int port) {
        raw[port] = new Victor(port);
    }
    
    public void set(int vic, double power) {
        raw[vic].set(power);
    }
    
    public double getPwm(int vic) {
        return raw[vic].get();
    }
    
    public Victor getVictor(int vic) {
        return raw[vic];
    }
    
    public void setSafety(boolean b){
        drive.setSafetyEnabled(false);
        if (hasSecondary) drive2.setSafetyEnabled(false);
    }
}
