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
    private Encoder enc;
    private double pulsesPerRevolution;
    
    public QuadratureEncoder(int channelA, int channelB)
    {
        enc = new Encoder(channelA, channelB);
    }
    
    public QuadratureEncoder(int channelA, int channelB, double pulsesPerRev)
    {
        enc = new Encoder(channelA, channelB);
        pulsesPerRevolution = pulsesPerRev;
    }
    
    public QuadratureEncoder(int channelA, int channelB, boolean isReversed, double pulsesPerRev)
    {
        enc = new Encoder(channelA, channelB, isReversed);
        pulsesPerRevolution = pulsesPerRev;
    }
    
    public QuadratureEncoder(int channelA, int channelB, int scaleValue, double pulsesPerRev)
    {
        if (scaleValue == 1)
            enc = new Encoder(channelA, channelB, false, CounterBase.EncodingType.k1X);
        else if (scaleValue == 2)
            enc = new Encoder(channelA, channelB, false, CounterBase.EncodingType.k2X);
        else if (scaleValue == 4)
            enc = new Encoder(channelA, channelB, false, CounterBase.EncodingType.k4X);
        else
            enc = new Encoder(channelA, channelB, false, CounterBase.EncodingType.k4X);
        
        pulsesPerRevolution = pulsesPerRev;
    }
    
    public QuadratureEncoder(int channelA, int channelB, boolean isReversed,
                            int scaleValue, double pulsesPerRev)
    {
        if (scaleValue == 1)
            enc = new Encoder(channelA, channelB, isReversed, CounterBase.EncodingType.k1X);
        else if (scaleValue == 2)
            enc = new Encoder(channelA, channelB, isReversed, CounterBase.EncodingType.k2X);
        else if (scaleValue == 4)
            enc = new Encoder(channelA, channelB, isReversed, CounterBase.EncodingType.k4X);
        else
            enc = new Encoder(channelA, channelB, isReversed, CounterBase.EncodingType.k4X);
        
        pulsesPerRevolution = pulsesPerRev;
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
    
    public void setDistancePerPulse(int wheelCircumference)
    {
        enc.setDistancePerPulse(wheelCircumference / pulsesPerRevolution);
    }
    
    public void setDistanceWithRatio(int distancePerPulse, double gearRatio)
    {
        enc.setDistancePerPulse(distancePerPulse * gearRatio);
    }
    
}
