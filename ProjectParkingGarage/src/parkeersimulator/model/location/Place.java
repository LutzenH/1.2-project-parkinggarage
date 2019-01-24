package parkeersimulator.model.location;

import parkeersimulator.model.ParkingGarageModel.carType;
import parkeersimulator.model.car.Car;

public class Place {
	private Car car;
	private carType[] carTypes;
	
	private boolean isReserved;

	/**
	 * Default Constructor for Place
	 */
	public Place() {
		this.car = null;
		this.carTypes = null;
		this.isReserved = false;
	}
	
	/**
	 * Constructor for place.
	 * @param car The car located at this Place.
	 * @param cartypes The type of cars allowed at this Place.
	 */
	public Place(Car car, carType[] carTypes) {
		this.car = car;
		this.carTypes = carTypes;
		this.isReserved = false;
	}
	
	/**
	 * @return the car
	 */
	public Car getCar() {
		return car;
	}

	/**
	 * @param car the car to set
	 */
	public void setCar(Car car) { this.car = car; }

	/**
	 * @return check if the Place has been reserved.
	 */
	public boolean getReserved() { return isReserved; }

	/**
	 * @param set the place to be reserved or not.
	 */
	public void setReserved(boolean isReserved) { this.isReserved = isReserved; }

	/**
	 * @return the allowed carTypes
	 */
	public carType[] getCarTypes() { return carTypes; }

	/**
	 * @param carTypes set the allowed carTypes
	 */
	public void setCarTypes(carType[] carTypes) { this.carTypes = carTypes; }
	
	/**
	 * Check if this Place is empty
	 * @return true if this Place is empty
	 */
	public boolean isEmpty() {
		return (car == null) ? true : false;
	}
}
