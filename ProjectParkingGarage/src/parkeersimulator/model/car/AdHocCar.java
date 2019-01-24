package parkeersimulator.model.car;

import java.util.Random;

import parkeersimulator.model.ParkingGarageModel.CarType;

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
    public AdHocCar(int stayMinutes) {    	
        this.setMinutesLeft(stayMinutes);
        this.setHasToPay(true);
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
}
