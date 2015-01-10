/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oastem.frc.sensor;

import edu.wpi.first.wpilibj.Gyro;

/**
 * Enclosure class for the Gyro class, customized to deal with the constant
 * drifting of the gyroscope.
 * 
 * @author KTOmega
 */
public class ADW22307Gyro {
    //create a new gyro object
    private Gyro g;
    
    //gives the value of compensation for the constant increase of angle over time
    private static final double DRIFT_PER_SECOND = 2.2348147200282*60;
    //private static final double DRIFT_PER_SECOND = 0.048;
    
    //creates the value lastUpdateTime
    private long lastUpdateTime = 0;
    
    public ADW22307Gyro(int port) {
        // initiates the gyro
        g = new Gyro(port);
        
        //sets the value lastUpdateTime
        lastUpdateTime = System.currentTimeMillis();
        //g.setSensitivity(7000);
        
        //goes to method initialize
        initialize();
    }
    
    public void initialize() {
        // sets the gyro to zero
        g.reset();
        //g.setSensitivity(0.07);
        
        // updates lastUpdateTime again for accurate readings
        lastUpdateTime = System.currentTimeMillis();
    }
    
    /*public double getAngle()
    {
        return g.getAngle();
    }//*/
    
    public double getAngle() {
        // gets the current time
        long currentTime = System.currentTimeMillis();
        
        // returns the value, recalibrated with compensation for drift
        // time is measured in milliseconds
        double value = g.getAngle() - DRIFT_PER_SECOND * (currentTime - lastUpdateTime)/1000.0;
        
        // updates currenttime
        lastUpdateTime = currentTime;
        
        // returns the angle
        return value;
    }//*/
}
