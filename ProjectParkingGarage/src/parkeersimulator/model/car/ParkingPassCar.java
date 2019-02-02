package parkeersimulator.model.car;

import java.awt.*;

/**
 * The model of a car with a subscription pass. 
 */
public class ParkingPassCar extends Car {
	public static final Color COLOR=Color.blue;
	
	/**
	 * The constructor for ParkingPassCar
	 * @param stayMinutes the amount of minutes this car should stay in the garage.
	 */
    public ParkingPassCar(int stayMinutes) {    	
        this.setMinutesLeft(stayMinutes);
    }
    
    /**
     * Returns the color of the car used in the simulation.
     */
    public Color getColor(){
    	return COLOR;
    }
    
	@Override
	public CarType getCarType() {
		return CarType.PASS;
	}

	@Override
	public boolean getHasToPay() {
		return false;
	}
}
