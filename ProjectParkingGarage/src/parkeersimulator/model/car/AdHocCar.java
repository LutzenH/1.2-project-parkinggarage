package parkeersimulator.model.car;

import java.awt.*;

/**
 * The model of a car without a subscription.
 */
public class AdHocCar extends Car {
	///The color this type of car should have.
	public static final Color COLOR=Color.red;
	/**
	 * The constructor for AdHocCar
	 * @param stayMinutes the amount of minutes this car should stay in the garage
	 */
    public AdHocCar(int stayMinutes) {    	
        this.setMinutesLeft(stayMinutes);
    }
    /**
     * @return the color of the car used in the simulation.
     */
    public Color getColor(){
    	return COLOR;
    }
    
	@Override
	public CarType getCarType() {
		return CarType.AD_HOC;
	}
	@Override
	public boolean getHasToPay() {
		return true;
	}
}
