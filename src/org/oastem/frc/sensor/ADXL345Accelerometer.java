package org.oastem.frc.sensor;

import edu.wpi.first.wpilibj.ADXL345_I2C;

/**
 * Custom class built that encloses the Accelerometer class in order to 
 * accomodate for drifting/out of range values.
 * 
 * @author KTOmega
 */
public class ADXL345Accelerometer {
	private static final int AXES = 3;
	private static final double NORMAL_REACT = 0.02;
	
	// The wrapped accelerometer class.
	// Depends on the model of accelerometer used.
    private ADXL345_I2C accel;
    
    // The initial readings to be used as a base.
    private double[] initialReadings;
    
    // The delta value which, if the change in any axis is greater than
    // this value, will indicate that that axis has moved.
    private double reactValue;
    
    public ADXL345Accelerometer(int port) {
        this(port, NORMAL_REACT);
    }
    
    public ADXL345Accelerometer(int port, double react) {
        accel = new ADXL345_I2C(port, ADXL345_I2C.DataFormat_Range.k8G);
        initialReadings = new double[AXES];
        reactValue = react;
        
        initialize();
    }
    
    public void initialize() {
        // Get initial readings
        
        initialReadings[0] = accel.getAcceleration(ADXL345_I2C.Axes.kX);
        initialReadings[1] = accel.getAcceleration(ADXL345_I2C.Axes.kY);
        initialReadings[2] = accel.getAcceleration(ADXL345_I2C.Axes.kZ);
    }
    
    public double getX() {
        return calculateAngle(accel.getAcceleration(ADXL345_I2C.Axes.kX) - initialReadings[0]);
    }
    
    public double getY() {
        //System.out.println(accel.getAcceleration(ADXL345_I2C.Axes.kY));
        //double temp = accel.getAcceleration(ADXL345_I2C.Axes.kY) - initialReadings[1];
        //System.out.println(temp + ":"+initialReadings[1]);
        return calculateAngle(accel.getAcceleration(ADXL345_I2C.Axes.kY) - initialReadings[1]);
    }
    
    public double getZ() {
        return calculateAngle(accel.getAcceleration(ADXL345_I2C.Axes.kZ) - initialReadings[2]);
    }
    
    public boolean isXOffset() {
        return Math.abs(getX()) > reactValue;
    }
    
    public boolean isYOffset() {
        return Math.abs(getY()) > reactValue;
    }
    
    public boolean isZOffset() {
        return Math.abs(getZ()) > reactValue;
    }
    
    /**
     * lol so it returns it in radians
     * @param radianValue
     * @return angle in degrees
     */
    private double calculateAngle(double radianValue){
        return radianValue*57.296;
    }
}
