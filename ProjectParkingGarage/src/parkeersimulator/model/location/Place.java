package parkeersimulator.model.location;

import parkeersimulator.model.car.Car;
import parkeersimulator.model.car.Car.CarType;

/**
 * The place a car will stay when inside the garage.
 * @author LutzenH
 */
public class Place implements Comparable<Place> {
	private Car car;
	private CarType carType;
	private Location location;
	
	private int timeReserved;
	
	///Declaration of the preferenceFactor that is used to sort all the places based on preference in the garage
	private float preferenceFactor;

	/**
	 * Default Constructor for Place
	 */
	public Place(Location location) {
		this(null, null, location);
	}
	
	/**
	 * Constructor for place.
	 * @param car The car located at this Place.
	 * @param cartypes The type of cars allowed at this Place.
	 */
	public Place(Car car, CarType carType, Location location) {
		this.car = car;
		this.carType = carType;
		this.location = location;
		this.timeReserved = 0;
	}
	
	/**
	 * Constructor for place with CarType input variable.

	 * @param carType The type of car allowed at this Place.
	 */
	public Place(CarType carType, Location location) {
		this(null, carType, location);
	}
	
	/**
	 * Will lower  timeReserved if it is greater than 0
	 */
	public void tick() {
		if(timeReserved > 0)
			timeReserved--;
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
	public boolean getReserved() { return timeReserved > 0 ? true : false; }

	/**
	 * @param set the place time to be reserved.
	 */
	public void setTimeReserved(int minutes) { this.timeReserved = minutes; }

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
