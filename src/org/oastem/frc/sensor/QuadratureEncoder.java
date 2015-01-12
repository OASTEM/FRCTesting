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
    
    public QuadratureEncoder(int channelA, int channelB)
    {
        enc = new Encoder(channelA, channelB);
    }
    
    public QuadratureEncoder(int channelA, int channelB, boolean isReversed)
    {
        enc = new Encoder(channelA, channelB, isReversed);
    }
    
    public QuadratureEncoder(int channelA, int channelB, int scaleValue)
    {
        if (scaleValue == 1)
            enc = new Encoder(channelA, channelB, false, CounterBase.EncodingType.k1X);
        else if (scaleValue == 2)
            enc = new Encoder(channelA, channelB, false, CounterBase.EncodingType.k2X);
        else if (scaleValue == 4)
            enc = new Encoder(channelA, channelB, false, CounterBase.EncodingType.k4X);
        else
            enc = new Encoder(channelA, channelB, false, CounterBase.EncodingType.k4X);
    }
    
    public QuadratureEncoder(int channelA, int channelB, boolean isReversed, int scaleValue)
    {
        if (scaleValue == 1)
            enc = new Encoder(channelA, channelB, isReversed, CounterBase.EncodingType.k1X);
        else if (scaleValue == 2)
            enc = new Encoder(channelA, channelB, isReversed, CounterBase.EncodingType.k2X);
        else if (scaleValue == 4)
            enc = new Encoder(channelA, channelB, isReversed, CounterBase.EncodingType.k4X);
        else
            enc = new Encoder(channelA, channelB, isReversed, CounterBase.EncodingType.k4X);
    }
    
    
    
    
}
