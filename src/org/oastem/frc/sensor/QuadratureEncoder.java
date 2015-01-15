/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oastem.frc.sensor;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;

/**
 *
 * @author mduong15
 */
public class QuadratureEncoder {
    private final Encoder enc;
    
    //This is different depending on encoder and motors (account for gear ratio).
    //This must be figured out by using docs and specs.
    //(If desperate, just have the motor spin one rotation and see how many pulses it is.)
    private double pulsesPerRevolution;
    
    
    /**
     * Creates an encoder using the two SIG inputs (A and B) 
     * Default to use to just get encoder readings and not use getDistance().
     * @param channelA I/0 SIG A 
     * @param channelB I/0 SIG B
     */
    public QuadratureEncoder(int channelA, int channelB)
    {
        enc = new Encoder(channelA, channelB);
        enc.start();
    }
    
    /**
     * Creates an encoder using the two SIG inputs (A and B) 
     * pulsesPerRev is used for getDistance() methods
     * @param channelA I/0 SIG A 
     * @param channelB I/O SIG B
     * @param pulsesPerRev Number of pulses for a revolution of the motor (look at instance variable)
     */
    public QuadratureEncoder(int channelA, int channelB, double pulsesPerRev)
    {
        enc = new Encoder(channelA, channelB);
        pulsesPerRevolution = pulsesPerRev;
        enc.start();
    }
    
    /**
     * Creates an encoder using the two SIG inputs (A and B) 
     * Also allows the encoder to be reversed
     * pulsesPerRev is used for getDistance() methods
     * @param channelA I/0 SIG A 
     * @param channelB I/O SIG B
     * @param isReversed When true, returned values are inverted
     * @param pulsesPerRev Number of pulses for a revolution of the motor (look at instance variable)
     */
    public QuadratureEncoder(int channelA, int channelB, boolean isReversed, double pulsesPerRev)
    {
        enc = new Encoder(channelA, channelB, isReversed);
        pulsesPerRevolution = pulsesPerRev;
        enc.start();
    }
    
    /**
     * Creates an encoder using the two SIG inputs (A and B) 
     * Also allows the encoder to be reversed
     * Can also control the difference between getRaw and get values
     * pulsesPerRev is used for getDistance() methods
     * @param channelA I/0 SIG A 
     * @param channelB I/O SIG B
     * @param isReversed When true, returned values are inverted
     * @param scaleValue getRaw() values are divided by multiples of 1, 2, or 4 to increase accuracy
     * @param pulsesPerRev Number of pulses for a revolution of the motor (look at instance variable)
     */
    public QuadratureEncoder(int channelA, int channelB, boolean isReversed,
                            int scaleValue, double pulsesPerRev)
    {
        CounterBase.EncodingType encType = CounterBase.EncodingType.k4X;
        
        if (scaleValue == 1)
            encType = CounterBase.EncodingType.k1X;
        else if (scaleValue == 2)
            encType = CounterBase.EncodingType.k2X;
        else if (scaleValue == 4)
            encType = CounterBase.EncodingType.k4X;
        
        enc = new Encoder(channelA, channelB, isReversed, encType);
        
        pulsesPerRevolution = pulsesPerRev;
        enc.start();
    }
    
    /**
     * Creates an encoder using the two SIG inputs (A and B) 
     * Can also control the difference between getRaw and get values
     * pulsesPerRev is used for getDistance() methods
     * @param channelA I/0 SIG A 
     * @param channelB I/O SIG B
     * @param scaleValue getRaw() values are divided by multiples of 1, 2, or 4 to increase accuracy
     * @param pulsesPerRev Number of pulses for a revolution of the motor (look at instance variable)
     */
    public QuadratureEncoder(int channelA, int channelB, int scaleValue, double pulsesPerRev)
    {
        this(channelA, channelB, false, scaleValue, pulsesPerRev);
    }
    
    public void free()
    {
        enc.free();
    }
    
    public int get()
    {
        return enc.get();
    }
    
    public double getDistance()
    {
        return enc.getDistance();
    }
    
    public double getRate()
    {
        return enc.getRate();
    }
    
    public int getRaw()
    {
        return enc.getRaw();
    }
    
    public boolean isGoingForward()
    {
        return enc.getDirection();
    }
    
    public boolean isStopped()
    {
        return enc.getStopped();
    }
    
    public void reset()
    {
        enc.reset();
    }
    
    public void setDistancePerPulse(int distancePerDriverRevolution)
    {
        enc.setDistancePerPulse(distancePerDriverRevolution / pulsesPerRevolution);
    }
    
}
