package parkeersimulator.model.car;

import java.util.Random;

import parkeersimulator.model.location.Location;

import java.awt.*;

/**
 * The model of a car without a subscription.
 */
public class ReservationCar extends Car {
	///The color this type of car should have.
	public static final Color COLOR=Color.yellow;
	
	private int timeBeforeArrival;
	private int stayMinutes;
	
	private Location reservedLocation;
	
	/**
	 * The constructor for ReservationCar
	 */
    public ReservationCar(Location reservedLocation) {
    	Random random = new Random();
    	stayMinutes = (int) (60 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.timeBeforeArrival = random.nextInt(30) + 10;
        this.reservedLocation = reservedLocation;
    }
    
    /**
     * @return the color of the car used in the simulation.
     */
    public Color getColor(){
    	return COLOR;
    }
    
	@Override
	public CarType getCarType() {
		return CarType.RESERVERATION_CAR;
	}

	@Override
	public boolean getHasToPay() {
		return true;
	}

	public Location getReservedLocation() {
		return reservedLocation;
	}
	
	/**
	 * @return the timeBeforeArrival
	 */
	public int getTimeBeforeArrival() {
		return timeBeforeArrival;
	}

	public int getStayMinutes() {
		return stayMinutes;
	}
	
	public void tickArrivalTime() {
		timeBeforeArrival--;
	}
}
