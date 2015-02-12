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
    
    //private DriveSystem ds;
    private Joystick js, js2;
    
    //private DigitalInput lim;
    
    //private Jaguar motor1;
    
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
    
    //private Encoder encoder;
    //private ADW22307Gyro gyro;
    private Compressor compress;
    private DoubleSolenoid solen;
    private Relay rel;
    
    private final int ENCODER_PORT_A = 1;
    private final int ENCODER_PORT_B = 2;
    private final int GYRO_PORT = 3;
    private final int PRESSURE_SWITCH_CHANNEL = 3;
    private final int COMP_RELAY_CHANNEL = 1;
    private final int SOLEN_FORWARD_CHANNEL = 1;  // FIGURE OUT THE ACTUAL NUMBER
    private final int SOLEN_BACKWARD_CHANNEL = 2; // FIGURE OUT THE ACTUAL NUMBER
    
    private final int SOL_FORWARD_BUTTON = 4;
    private final int SOL_REVERSE_BUTTON = 5;
    private final int SOL_OFF_BUTTON = 6;
    
    
    public void robotInit(){
        //ds = DriveSystem.getInstance();
        //ds.initializeDrive(LEFT_DRIVE_PORT, RIGHT_DRIVE_PORT);
        
        js = new Joystick(FIRST_JOYSTICK);
        //encoder = new Encoder(ENCODER_PORT_A, ENCODER_PORT_B);
        //gyro = new ADW22307Gyro(GYRO_PORT);
        compress = new Compressor(PRESSURE_SWITCH_CHANNEL, COMP_RELAY_CHANNEL);
        compress.start();
        solen = new DoubleSolenoid(SOLEN_FORWARD_CHANNEL, SOLEN_BACKWARD_CHANNEL);
        
        
       
        
        //motor1 = new Jaguar(JAGUAR_PORT);
        
        Debug.clear();
        Debug.log(1, 1, "Robot initialized.");
    }
    
    /*
	*This is autonomous pseudocode and it should hopefully logically work <3
	*

1:	init start state
2:	init resetCount
3:	switch time comparison from > to  <=
	> implies the if branches hit false if it takes too long, never moving to the next state
4:	the else block in UPLIFT case implies rapid switch to RESET state, since the the if statement will most likely
	return false in the first few milliseconds 
5:	the else block in MOVETO_AUTO case implies rapid switch to READY state, 
	since again, the if statement will most likely return false in the first few milliseconds
	
The biggest issues (in order of issue importance) are 3, 4, 5.
Issue 5 will only skip the release state, but overall is not as destructive as the other two issues. 
Issue 4 will cause rapid hook activation and deactivation, but only 3 times, since the reset count times out. However, this will leave the robot stuck in UPLIFT state. 
Issue 3 will cause the robot to possibly get stuck in every case that involves timeouts. Remember, currTime - triggerStart = how much time has passed since we started this method, and the > comparison will hit true if the time passed is OVER the timeout. If it hits true, then the false gets returned, and the robot will be in its current state for all eternity. 

Fixes:
Issue 5 can be fixed by removing the else block or adding an additional if statement for a timeout to switch to READY state. 
Issue 4 can be fixed by again, removing the else block or adding an additional if statement timeout to switch to RESET state. (Although, this state itself seems pointless)
Issue 3 can be fixed be replacing all > in the methods with <= and replacing all || with &&, or by swapping the true and false returns in each method. 

	* 
	* 
	* 
	* 
	* 
	* 
	* 
	* 
	
	public static final int START = 0;
	public static final int GOTO_TOTE = 1;
	public static final int UPLIFT = 2;
	public static final int RESET = 3;
	public static final int MOVETO_AUTO = 4;
	public static final int RELEASE = 5;
	//public static final int READY = 6;
	
	private int state;
	private int resetCount;
	private long currTime = 0L;
	private long triggerStart = 0L;
	
	public void autonomous() {
		debug[0] = "Autonomous mode enabled";
		while(isAutonomous() && isEnabled()) {
			imageProcessing();
			currTime = System.currentTimeMillis();
			joytonomousStates(currTime); //not sure if this should be here
		}
		Debug.log(debug);
	}
	
	private void joytonomousStates(long currTime) {
		switch(state) {
			case START:
				//anything we need to go beforehand
				break;
			case GOTO_TOTE:
				if(moveForward(currTime, triggerStart)) {
					triggerStart = currTime;
					state = UPLIFT;
				}
				break;
			case UPLIFT:
				if(resetCount < 3) {
					if(hookUp(currTime, triggerStart)) {
						triggerStart = currTime;
						state = MOVETO_AUTO;
					} 
					if (currTime - triggerStart > 1750L) {
						triggerStart = currTime;
						state = RESET; //count attempts
						resetCount++;
					}
				}
				state = MOVETO_AUTO;
				break;
			case RESET:
				if(redo(currTime, triggerStart)) {
					triggerStart = currTime;
					state = UPLIFT;
				}
				break;
			case MOVETO_AUTO;
				if(moveAuto(currTime, triggerStart)) {
					triggerStart = currTime;
					state = RELEASE;
				} 
				if (currTime - triggerStart > 2500L) {
					triggerStart = currTime;
					state = READY;
				}
				break;
			case RELEASE:
				if(releaseTote(currTime, triggerStart)) {
					triggerStart = 0;
					state = READY;
				}
			case READY:
				//how we want to get ready for operator control
			default: 
				//what to do if something fails
				break; //ihy
		}
	}
	
	private boolean moveForward(long currTime, long triggerStart) {
		if(!robot.drive(distanceToTote) && currTime - triggerStart <= 1500L) { //lol this isn't a method but should return T/F 
			return false;
		} else {
			return true;
		}
	}
	
	private boolean hookUp(long currTime, long triggerStart) {
		hook.upToTote(); //lol I wish this were already a method
		if(!checkHooked() && currTime - triggerStart <= 2000L ) { //however long it takes to hook the tote
			return false;
		} else {
			return true;
}
	}
	
	private boolean redo(long currTime, long triggerStart) {
		hook.downToUnhook();
		if (!robot.drive(justSmallDistanceToReposition) && currTime - triggerStart <= 1000L) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean moveAuto(long currTime, long triggerStart) {
		if(!robot.drive(distanceToAuto) && !checkHooked() && currTime-triggerStart <= 5000L) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean releaseTote(long currTime, long triggerStart) {
		hook.downToUnhook();
		if(checkHooked() && currTime - triggerStart <= 1500L) {
			return false;
		} else {
			robot.reverse(justSmallDistanceToReposition);
			return true;
		}
	}
	
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
            
            
            if (js.getRawButton(SOL_FORWARD_BUTTON))
            {
                solen.set(DoubleSolenoid.Value.kForward);
                debug[4] = "solen FORWARD";
            }
            else if (js.getRawButton(SOL_REVERSE_BUTTON))
            {
                solen.set(DoubleSolenoid.Value.kReverse);
                debug[4] = "solen REVERSE";
            }
            else
            {
                solen.set(DoubleSolenoid.Value.kOff);
                debug[4] = "solen OFF";
            }
            
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
            
            //motor1.set(js.getY());
            //debug[0] = "Rate: " + encoder.getRate();
            //debug[1] = "Angle: " + gyro.getAngle();
            debug[2] = "how to do";
            System.out.println(debug[4]);
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