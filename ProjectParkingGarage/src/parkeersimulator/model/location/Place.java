package parkeersimulator.model.location;

import parkeersimulator.model.ParkingGarageModel.CarType;
import parkeersimulator.model.car.Car;

public class Place {
	private Car car;
	private CarType[] carTypes;
	
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
	public Place(Car car, CarType[] carTypes) {
		this.car = car;
		this.carTypes = carTypes;
		this.isReserved = false;
	}
	
	/**
	 * Constructor for place with CarType input variable.

	 * @param carType The type of car allowed at this Place.
	 */
	public Place(CarType carType) {
		this.car = null;
		this.carTypes = new CarType[1];
		this.carTypes[0] = carType;
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
	public CarType[] getCarTypes() { return carTypes; }

	/**
	 * @param carTypes set the allowed carTypes
	 */
	public void setCarTypes(CarType[] carTypes) { this.carTypes = carTypes; }
	
	/**
	 * Check if this Place is empty
	 * @return true if this Place is empty
	 */
	public boolean isEmpty() {
		return (car == null) ? true : false;
	}
}
