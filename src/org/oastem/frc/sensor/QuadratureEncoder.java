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
    private double pulsesPerRevolution;
    
    public QuadratureEncoder(int channelA, int channelB)
    {
        enc = new Encoder(channelA, channelB);
        enc.start();
    }
    
    public QuadratureEncoder(int channelA, int channelB, double pulsesPerRev)
    {
        enc = new Encoder(channelA, channelB);
        pulsesPerRevolution = pulsesPerRev;
        enc.start();
    }
    
    public QuadratureEncoder(int channelA, int channelB, boolean isReversed, double pulsesPerRev)
    {
        enc = new Encoder(channelA, channelB, isReversed);
        pulsesPerRevolution = pulsesPerRev;
        enc.start();
    }
    
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
