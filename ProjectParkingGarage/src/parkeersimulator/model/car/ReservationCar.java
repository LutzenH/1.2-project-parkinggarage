package parkeersimulator.model.car;

import java.util.Random;

import java.awt.*;

/**
 * The model of a car without a subscription.
 */
public class ReservationCar extends Car {
	///The color this type of car should have.
	private static final Color COLOR=Color.yellow;
	
	private int timeBeforeArrival;
	
	/**
	 * The constructor for ReservationCar
	 */
    public ReservationCar() {
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.timeBeforeArrival = random.nextInt(30) + 5;
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

	/**
	 * @return the timeBeforeArrival
	 */
	public int getTimeBeforeArrival() {
		return timeBeforeArrival;
	}

	public void tickArrivalTime() {
		timeBeforeArrival--;
	}
}
