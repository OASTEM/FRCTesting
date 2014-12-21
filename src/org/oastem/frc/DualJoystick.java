package org.oastem.frc;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.parsing.IInputOutput;

/**
 * Allows you to use either joystick to perform the same commands on
 * the robot.
 *
 * @author KTOmega
 */
public class DualJoystick extends GenericHID implements IInputOutput {
    private Joystick left;
    private Joystick right;
    
    // The preferred hand, left or right.
    private Hand pref;
    
    // By default that hand is the right hand.
    private static final Hand DEFAULT_HAND = Hand.kRight;
    
    public DualJoystick(int left, int right) {
		this(left, right, DEFAULT_HAND);
	}
	
	public DualJoystick(Joystick left, Joystick right) {
		this(left, right, DEFAULT_HAND);
	}

    public DualJoystick(int left, int right, Hand pref) {
        this(new Joystick(left), new Joystick(right), pref);
    }
    
    public DualJoystick(Joystick left, Joystick right, Hand pref) {
        this.left = left;
        this.right = right;
        this.pref = pref;
    }
    
    public void setPreference(Hand pref) {
        this.pref = pref;
    }
    
    public Hand getPreference() {
        return pref;
    }

	// Returns whichever one is non-zero, or whichever one is preferred.
    private double nonZero(double leftVal, double rightVal) {
        if (leftVal == 0 || rightVal == 0) {
            return Math.max(leftVal, rightVal);
        } else {
            if (getPreference() == Hand.kLeft) {
                return leftVal;
            } else if (getPreference() == Hand.kRight) {
                return rightVal;
            } else {
                return Math.max(leftVal, rightVal);
            }
        }
    }

	// Gets whether a button is pushed on either of the joysticks.
    public boolean getRawButton(int button) {
        return left.getRawButton(button) || right.getRawButton(button);
    }

	// Joysticks don't have bumpers.
	// This method is only here to fully implement GenericHID.
    public boolean getBumper(Hand hand) {
        return false;
    }

    public boolean getTop(Hand hand) {
        return left.getTop() || right.getTop();
    }

    public double getRawAxis(int axis) {
        double leftAxis = left.getRawAxis(axis);
        double rightAxis = right.getRawAxis(axis);
        return nonZero(leftAxis, rightAxis);
    }

    public boolean getTrigger(Hand hand) {
        return left.getTrigger() || right.getTrigger();
    }

    public double getThrottle() {
        double leftVal = left.getThrottle();
        double rightVal = right.getThrottle();
        return nonZero(leftVal, rightVal);
    }

    public double getTwist() {
        double leftVal = left.getTwist();
        double rightVal = right.getTwist();
        return nonZero(leftVal, rightVal);
    }

    public double getX(Hand hand) {
        double leftVal = left.getX();
        double rightVal = right.getX();
        return nonZero(leftVal, rightVal);
    }

    public double getY(Hand hand) {
        double leftVal = left.getY();
        double rightVal = right.getY();
        return nonZero(leftVal, rightVal);
    }

    public double getZ(Hand hand) {
        double leftVal = left.getZ();
        double rightVal = right.getZ();
        return nonZero(leftVal, rightVal);
    }
}
