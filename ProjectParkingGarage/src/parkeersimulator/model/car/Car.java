package parkeersimulator.model.car;

import java.awt.*;

import parkeersimulator.model.location.Location;

/**
 * Abstract class of the cars used in the simulation.
 */
public abstract class Car {

    private Location location;
    private int minutesLeft;
    private boolean isPaying;
    private boolean hasToPay;

    /**
     * Constructor for objects of class Car
     */
    public Car() {

    }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public int getMinutesLeft() { return minutesLeft; }
    public void setMinutesLeft(int minutesLeft) { this.minutesLeft = minutesLeft; }
    
    public boolean getIsPaying() { return isPaying; }
    public void setIsPaying(boolean isPaying) { this.isPaying = isPaying; }

    public boolean getHasToPay() { return hasToPay; }
    public void setHasToPay(boolean hasToPay) { this.hasToPay = hasToPay; }

    public void tick() {
        minutesLeft--;
    }
    
    /**
     * @return The color of a car.
     */
    public abstract Color getColor();
}