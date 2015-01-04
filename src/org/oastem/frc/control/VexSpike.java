package org.oastem.frc.control;

import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.Relay;
/**
 * Wrapper class for a Vex Spike (which really is just a relay)
 * @author ThePotatoGuy
 * 
 * Notes:
 * 2014:
 *  + creation of VexSpike class and methods
 * 2015-01-04:
 *  + added commenting and patch notes for better understanding
 */
public class VexSpike{
    
    // A vex spike is just a relay
    private Relay vexRelay;
    
    /**
     * Constructor for vex spike using its connected pin
     * @param channel - the pin this spike is connected to
     */
    public VexSpike(int channel){
        vexRelay = new Relay(channel); //7, 8
    }
    
    /**
     * If I remember right, this did absolutely nothing
     */
    public void activate(){
        vexRelay.set(Relay.Value.kOn);
    }
    
    /**
     * This will activate the vex spike, turning the light green
     */
    public void goForward(){
        vexRelay.set(Relay.Value.kForward);
    }
    
    /**
     * This will activate the vex spike, turning the light red
     * (using this and the goForward method allows you to connect the vex spike to other stuff to do stuff)
     */
    public void goBackward() {
        vexRelay.set(Relay.Value.kReverse);
    }

    /**
     * not sure what this does
     */
    public void free(){
        vexRelay.free();
    }
    
    /**
     * This basically turns off the vex spike. Use this after calling goForward or goBackward
     * Also use this in e-stop situations
     */
    public void deactivate(){
        vexRelay.set(Relay.Value.kOff);
    }
}
