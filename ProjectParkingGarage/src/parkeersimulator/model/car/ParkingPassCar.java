package parkeersimulator.model.car;

import java.util.Random;
import java.awt.*;

/**
 * The model of a car with a subscription pass. 
 */
public class ParkingPassCar extends Car {
	private static final Color COLOR=Color.blue;
	
	/**
	 * The constructor for ParkingPassCar
	 */
    public ParkingPassCar(int stayMinutes) {    	
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(false);
    }
    
    /**
     * Returns the color of the car used in the simulation.
     */
    public Color getColor(){
    	return COLOR;
    }
}
