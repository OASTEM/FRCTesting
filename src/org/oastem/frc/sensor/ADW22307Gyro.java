package org.oastem.frc.sensor;

import edu.wpi.first.wpilibj.Gyro;

/**
 * Enclosure class for the WPILibJ Gyro class, customized to deal with 
 * the constant drifting of the gyroscope.
 * 
 * @author KTOmega
 */
public class ADW22307Gyro {
	// The native Gyro class.
    private Gyro g;
    
    // The experimentally determined drift rate.
    private static final double DRIFT_PER_SECOND = 0.004578491443051003;
    
    // The last time this was updated.
    private long lastUpdateTime = 0;
    
    public ADW22307Gyro(int port) {
        g = new Gyro(port);
        lastUpdateTime = System.currentTimeMillis();
        
        initialize();
    }
    
    public void initialize() {
        g.reset();
        //g.setSensitivity(0.07);
        lastUpdateTime = System.currentTimeMillis();
    }
    
    public double getAngle() {
        long currentTime = System.currentTimeMillis();
        
        double value = g.getAngle() - DRIFT_PER_SECOND * (currentTime - lastUpdateTime)/1000.0;
        
        lastUpdateTime = currentTime;
        
        return value;
    }
}
