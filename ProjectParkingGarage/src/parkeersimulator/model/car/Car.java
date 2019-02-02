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

    /**
     * @return the location of this car
     */
    public Location getLocation() { return location; }
    
    /**
     * Sets the location of this car
     * @param location the location the car should be set to
     */
    public void setLocation(Location location) { this.location = location; }

    /**
     * @return the amount of minutes this car has left before it will leave.
     */
    public int getMinutesLeft() { return minutesLeft; }
    
    /**
     * Sets the amount of minutes this car has left before it will leave.
     * @param minutesLeft the amount of minutes this car has left before it will leave.
     */
    public void setMinutesLeft(int minutesLeft) { this.minutesLeft = minutesLeft; }
    
    /**
     * @return the amount of time this car has stayed in the garage.
     */
    public int getMinutesStayed() { return minutesStayed; }
    
    /**
     * @return true if the car is paying
     */
    public boolean getIsPaying() { return isPaying; }
    
    /**
     * set the car to be paying or not.
     * @param isPaying set the car to be paying or not
     */
    public void setIsPaying(boolean isPaying) { this.isPaying = isPaying; }

    /**
     * @return returns true if the car has to pay for his visit in the garage.
     */
    public abstract boolean getHasToPay();

    /**
     * every tick this car does changes until it has to leave
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