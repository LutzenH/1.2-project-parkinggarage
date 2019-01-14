package parkeersimulator.model.car;

import java.util.Random;
import java.awt.*;

/**
 * The model of a car without a subscription.
 */
public class AdHocCar extends Car {
	///The color this type of car should have.
	private static final Color COLOR=Color.red;
	
	/**
	 * The constructor for AdHocCar
	 */
    public AdHocCar() {
    	Random random = new Random();
    	int stayMinutes = (int) (15 + random.nextFloat() * 3 * 60);
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(true);
    }
    
    /**
     * @return the color of the car used in the simulation.
     */
    public Color getColor(){
    	return COLOR;
    }
}
