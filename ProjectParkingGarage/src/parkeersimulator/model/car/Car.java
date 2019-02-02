package parkeersimulator.model.car;

import java.awt.*;

import parkeersimulator.model.location.Location;

/**
 * Abstract class of the cars used in the simulation.
 */
public abstract class Car {

    private Location location;
    
	///Id of the different types of cars.
	public enum CarType { AD_HOC, PASS, RESERVERATION_CAR }

    private int minutesLeft;
    private int minutesStayed;
    private boolean isPaying;

    /**
     * Constructor for objects of class Car
     */
    public Car() { }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public int getMinutesLeft() { return minutesLeft; }
    public void setMinutesLeft(int minutesLeft) { this.minutesLeft = minutesLeft; }
    
    public int getMinutesStayed() { return minutesStayed; }
    
    public boolean getIsPaying() { return isPaying; }
    public void setIsPaying(boolean isPaying) { this.isPaying = isPaying; }

    public abstract boolean getHasToPay();

    /**
     * every tick this car  does changes its to leave.
     */
    public void tick() {
        minutesLeft--;
        minutesStayed++;
    }
    
    /**
     * @return The color of a car.
     */
    public abstract Color getColor();
    
    /**
     * @return the type of car this is.
     */
    public abstract CarType getCarType();
}