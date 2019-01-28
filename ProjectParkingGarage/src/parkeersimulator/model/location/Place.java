package parkeersimulator.model.location;

import parkeersimulator.model.car.Car;
import parkeersimulator.model.car.Car.CarType;

public class Place implements Comparable<Place> {
	private Car car;
	private CarType carType;
	private Location location;
	
	private boolean isReserved;
	
	private float preferenceFactor;

	/**
	 * Default Constructor for Place
	 */
	public Place(Location location) {
		this.car = null;
		this.carType = null;
		this.isReserved = false;
		this.setPreferenceFactor(0f);
		this.location = location;
	}
	
	/**
	 * Constructor for place.
	 * @param car The car located at this Place.
	 * @param cartypes The type of cars allowed at this Place.
	 */
	public Place(Car car, CarType carType, Location location) {
		this.car = car;
		this.carType = carType;
		this.isReserved = false;
		this.location = location;
	}
	
	/**
	 * Constructor for place with CarType input variable.

	 * @param carType The type of car allowed at this Place.
	 */
	public Place(CarType carType, Location location) {
		this.car = null;
		this.carType = carType;
		this.isReserved = false;
		this.location = location;
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
	 * @return the allowed carType
	 */
	public CarType getCarType() { return carType; }

	/**
	 * @param carTypes set the allowed carType
	 */
	public void setCarType(CarType carType) { this.carType = carType; }
	
	/**
	 * Check if this Place is empty
	 * @return true if this Place is empty
	 */
	public boolean isEmpty() {
		return (car == null) ? true : false;
	}

	/**
	 * @return its preference factor
	 */
	public float getPreferenceFactor() {
		return preferenceFactor;
	}

	/**
	 * @param preferenceFactor the preferenceFactor
	 */
	public void setPreferenceFactor(float preferenceFactor) {
		this.preferenceFactor = preferenceFactor;
	}

	@Override
	public int compareTo(Place otherPlace) {
		return Float.compare(this.preferenceFactor, otherPlace.getPreferenceFactor());
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
}
