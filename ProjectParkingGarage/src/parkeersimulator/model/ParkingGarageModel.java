package parkeersimulator.model;

import java.util.ArrayList;
import java.util.Random;

import parkeersimulator.model.car.AdHocCar;
import parkeersimulator.model.car.Car;
import parkeersimulator.model.car.ParkingPassCar;
import parkeersimulator.model.location.Location;
import parkeersimulator.model.queue.CarQueue;

/**
 * The model of the parking garage simulation.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 * 
 */
public class ParkingGarageModel extends AbstractModel implements Runnable {

	///boolean that is used for when threads need to stop running.
	private boolean run;
	
	//TODO Replace these different types with an Enum.
	///Id of the different types of cars.
	public enum carType { AD_HOC, PASS }
	
	///Declaration of the different queues in the simulation.
	private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    
    private int eventChancePercentage = 10;

    //TODO Replace this with a more robust system.
    ///Declaration of the time of the week.
    private int day = 0;
    private int hour = 0;
    private int minute = 0;

    ///The amount of time the thread should wait before executing the next tick().
    private int tickPause = 100;

    ///The baseline number of cars arriving in an hour.
    int standardArrivals = 100;
    int adHocArrivals;
    int passArrivals;
    
    ///The multipliers of cars arriving in an hour.
    float adHocArrivals_week = 1f;
    float adHocArrivals_weekend = 1.7f;
    float adHocArrivals_event = 2.1f;
    
    float passArrivals_week = 0.5f;
    float passArrivals_weekend = 0.05f;
    float passArrivals_eventWeek = 0.3f;
  
    /// number of cars that can enter per minute
    int enterSpeed = 3; 
    /// number of cars that can pay per minute
    int paymentSpeed = 7;
    /// number of cars that can leave per minute
    int exitSpeed = 5;
    
    ///Declaration of Multi-dimensional array of Car, format: [numberOfFloors][numberOfRows][numberOfPlaces]
    private Car[][][] cars;

    ///Declaration of values needed to generate the parking garage.
	private int numberOfFloors = 3;
	private int numberOfRows = 8;
	private int numberOfPlaces = 30;
	private int numberOfOpenSpots;

	/**
	 * Constructor of ParkingGarageModel
	 */
    public ParkingGarageModel() {
    	///Instantiation of the queue's
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();

        this.numberOfOpenSpots = numberOfFloors * numberOfRows * numberOfPlaces;
        
        ///Instantiation of the all possible car positions.
        cars = new Car[numberOfFloors][numberOfRows][numberOfPlaces];
    }

    /**
     * Starts the simulation on a different thread.
     */
	public void start() {
		if(!run)
			new Thread(this).start();
	}
	
	/**
	 * Stops the currently running simulation on a different thread.
	 */
	public void stop() {
		run=false;
	}
	
	/**
	 * Simulation that runs on a different thread when started.
	 */
	@Override
	public void run() {
		run=true;
		while(run) {
            tick();
			try {
				Thread.sleep(tickPause);
			} 
			catch (Exception e) {
			}
		}
	}

	/**
	 * Responsible for what happens every iteration in the simulation.
	 */
    public void tick() {
    	advanceTime();
    	tickCars();
    	handleExit();
    	notifyViews();
    	setArrivals();
    	handleEntrance();
    }
    
    /**
     * Runs a certain amount of simulation iterations
     * 
     * @param amount The amount of ticks that should run.
     */
    public void tick(int amount) {
    	for(int i = 0; i < amount; i++)
    	{
    		tick();
    	}
    }

    /**
     * A method that is used for incrementing the current time.
     */
    private void advanceTime(){
        // Advance the time by one minute.
        minute++;
        while (minute > 59) {
            minute -= 60;
            hour++;
        }
        while (hour > 23) {
            hour -= 24;
            day++;
            checkForEvent();
        }
        while (day > 6) {
            day -= 7;
        }

    }

    /**
     * Handles all methods related to entrance of the cars.
     */
    private void handleEntrance(){
    	carsArriving();
    	carsEntering(entrancePassQueue);
    	carsEntering(entranceCarQueue);  	
    }
    
    /**
     * Handles all methods related to leaving of cars.
     */
    private void handleExit(){
        carsReadyToLeave();
        carsPaying();
        carsLeaving();
    }
    
    /**
     * Puts a certain amount of cars in the queue depending on there average values / type.
     */
    private void carsArriving(){
    	int numberOfCars=getNumberOfCars(adHocArrivals);  
		addArrivingCars(numberOfCars, carType.AD_HOC); 
    	numberOfCars=getNumberOfCars(passArrivals);
        addArrivingCars(numberOfCars, carType.PASS);   
    }

    /**
     * Removes cars from the front of the queue and assign to a parking space depending on the enterSpeed.
     * 
     * @param queue the queue that the cars should be taken from.
     */
    private void carsEntering(CarQueue queue){
        int i=0;      
    	while (queue.carsInQueue()>0 && 
    			getNumberOfOpenSpots()>0 && 
    			i<enterSpeed) {
            Car car = queue.removeCar();
            Location freeLocation = getFirstFreeLocation();
            setCarAt(freeLocation, car);
            i++;
        }
    }
    
    /**
     * Adds leaving cars to the payment queue.
     */
    private void carsReadyToLeave(){
        Car car = getFirstLeavingCar();
        while (car!=null) {
        	if (car.getHasToPay()){
	            car.setIsPaying(true);
	            paymentCarQueue.addCar(car);
        	}
        	else {
        		carLeavesSpot(car);
        	}
            car = getFirstLeavingCar();
        }
    }

    // TODO Handle payment.
    /**
     * Let cars pay.
     */
    private void carsPaying(){
    	int i=0;
    	while (paymentCarQueue.carsInQueue()>0 && i < paymentSpeed){
            Car car = paymentCarQueue.removeCar();
            carLeavesSpot(car);
            i++;
    	}
    }
    
    /**
     * Lets cars leave.
     */
    private void carsLeaving(){
    	int i=0;
    	while (exitCarQueue.carsInQueue()>0 && i < exitSpeed){
            exitCarQueue.removeCar();
            i++;
    	}	
    }
    
    /**
     * Returns the number of cars that arrive per minute depending on the entered values.
     * 
     * @param weekDay The average amount of cars that should enter the parking garage on a weekday per hour.
     * @param weekend The average amount of cars that should enter the parking garage in the weekend per hour.
     * @return The number of cars that arrive per minute.
     */
    private int getNumberOfCars(int arrivalNum){
        Random random = new Random();

        // Get the average number of cars that arrive per hour.
        int averageNumberOfCarsPerHour = arrivalNum;

        // Calculate the number of cars that arrive this minute.
        double standardDeviation = averageNumberOfCarsPerHour * 0.3;
        double numberOfCarsPerHour = averageNumberOfCarsPerHour + random.nextGaussian() * standardDeviation;
        return (int)Math.round(numberOfCarsPerHour / 60);	
    }
    
    /**
     * Adds a certain number and type of cars to the back of the queue.
     * 
     * @param numberOfCars Amount of cars that should be added to the queue.
     * @param type Type of car that should be added to the queue.
     */
    private void addArrivingCars(int numberOfCars, carType type){
    	switch(type) {
    	case AD_HOC: 
            for (int i = 0; i < numberOfCars; i++) {
            	entranceCarQueue.addCar(new AdHocCar());
            }
            break;
    	case PASS:
            for (int i = 0; i < numberOfCars; i++) {
            	entrancePassQueue.addCar(new ParkingPassCar());
            }
            break;	            
    	}
    }
    
    /**
     * Removes a car from its location and places it the exitCarQueue
     * 
     * @param car The car that should leave its location.
     */
    private void carLeavesSpot(Car car){
    	removeCarAt(car.getLocation());
        exitCarQueue.addCar(car);
    }
    
    
    /**
     * @return The current adHocArrivals_week in percent.
     */
    public Integer getAdHocArrivalsPercent_week() { return Math.round(adHocArrivals_week * 100f); }
    /**
     * Sets the multiplier for adHoc cars arriving during standard weeks.
     */
    public void setAdHocArrivals_week(float amount) {adHocArrivals_week = amount / 100f; }
    
    /**
     * @return The current adHocArrivals_weekend in percent.
     */
    public Integer getAdHocArrivals_weekend() { return Math.round(adHocArrivals_weekend * 100f); }
    /**
     * Sets the multiplier for adHoc cars arriving during weekends.
     */
    public void setAdHocArrivals_weekend(float amount) {adHocArrivals_weekend = amount / 100f; }
    
    /**
     * @return The current adHocArrivals_event in percent.
     */
    public Integer getAdHocArrivals_event() { return Math.round(adHocArrivals_event * 100f); }
    /**
     * Sets the multiplier for adHoc cars arriving at events.
     */
    public void setAdHocArrivals_event(float amount) {adHocArrivals_event = amount / 100f; }
    
    
    /**
     * @return The current passArrivals_week in percent.
     */
    public Integer getPassArrivals_week() { return Math.round(passArrivals_week * 100f); }
    /**
     * Sets the multiplier for pass cars arriving during standard weeks.
     */
    public void setPassArrivals_week(float amount) {passArrivals_week = amount / 100f; }
    
    /**
     * @return The current passArrivals_weekend in percent.
     */
    public Integer getPassArrivals_weekend() { return Math.round(passArrivals_weekend * 100f); }
    /**
     * Sets the multiplier for pass cars arriving during weekends.
     */
    public void setPassArrivals_weekend(float amount) {passArrivals_weekend = amount / 100f; }
    
    /**
     * @return The current passArrivals_eventWeek in percent.
     */
    public Integer getPassArrivals_eventWeek() { return Math.round(passArrivals_eventWeek * 100f); }
    /**
     * Sets the multiplier for pass cars arriving at events during standard weeks.
     */
    public void setPassArrivals_eventWeek(float amount) {passArrivals_eventWeek = amount / 100f; }
    
    
    /**
     * @return The current pause between ticks.
     */
    public int getTickPause() { return tickPause; }
    /**
     * Sets the pause between ticks.
     */
    public void setTickPause(int amount) { tickPause = amount; }
    
    /**
     * @return The current day of the week.
     */
    public int getDay() { return day; }
    
    /**
     * @return The current hour of the day.
     */
    public int getHour() { return hour; }
    
    /**
     * @return The current minute of the hour.
     */
    public int getMinute() { return minute; }
    
    /**
     * @return The current time in the format: int[day][hour][minute].
     */
    public int[] getTime() { return new int[] { day, hour, minute }; }
    
    /**
     * @return The number of floors in the parking garage.
     */
	public int getNumberOfFloors() { return numberOfFloors; }
	
	/**
	 * @return The number of rows in the parking garage per floor.
	 */
    public int getNumberOfRows() { return numberOfRows; }
    
	/**
	 * @return The number of places in the parking garage per row.
	 */
    public int getNumberOfPlaces() { return numberOfPlaces; }
    
    /**
     * @return The total amount of open spots in the parking garage.
     */
    public int getNumberOfOpenSpots() { return numberOfOpenSpots; }
    
    /**
     * @return A multi-dimensional array of all cars in the parking garage, format: car[floor][row][place]
     */
    public Car[][][] getCars() { return cars; }
    
    //TODO Optimize this method.
    /**
     * Get the total car count of a certain car type.
     * @param type The type of car that should be used to calculate the car count.
     * @return amount of cars of this type currently in the parking garage.
     */
    public int getCarCount(carType type) {
    	int count = 0;
    	
    	for(Car[][] floor : cars) {
    		for(Car[] row : floor) {
        		for(Car car : row) {
        			switch(type) {
	        			case AD_HOC:
	        				if(car instanceof AdHocCar)
	        					count++;
	        				break;
	        			case PASS:
	        				if(car instanceof ParkingPassCar)
	        					count++;
	        				break;
        			}
            	}
        	}
    	}
    	
    	return count;
    }
    
    /**
     * @return The entranceCarQueue.
     */
	public CarQueue getEntranceCarQueue() { return entranceCarQueue; }
	
    /**
     * @return The entrancePassQueue.
     */
    public CarQueue getEntrancePassQueue() { return entrancePassQueue; }
    
    /**
     * @return The paymentCarQueue.
     */
    public CarQueue getPaymentCarQueue() { return paymentCarQueue; }
    
    /**
     * @return The exitCarQueue.
     */
    public CarQueue getExitCarQueue() { return exitCarQueue; }
    
    /**
     * Returns the car at a certain location.
     * 
     * @param location the location where a car could be.
     * @return A car at a certain location.
     */
    public Car getCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        return cars[location.getFloor()][location.getRow()][location.getPlace()];
    }

    /**
     * Sets the car at a certain location.
     * 
     * @param location The location where the car should be set.
     * @param car The car that will be placed at the location.
     * @return returns true if there aren't any cars at this location.
     */
    public boolean setCarAt(Location location, Car car) {
        if (!locationIsValid(location)) {
            return false;
        }
        Car oldCar = getCarAt(location);
        if (oldCar == null) {
            cars[location.getFloor()][location.getRow()][location.getPlace()] = car;
            car.setLocation(location);
            numberOfOpenSpots--;
            return true;
        }
        return false;
    }

    /**
     * Removes a car from a certain location.
     * 
     * @param location The location the car should be removed from.
     * @return The car that is removed from the specified location.
     */
    public Car removeCarAt(Location location) {
        if (!locationIsValid(location)) {
            return null;
        }
        Car car = getCarAt(location);
        if (car == null) {
            return null;
        }
        cars[location.getFloor()][location.getRow()][location.getPlace()] = null;
        car.setLocation(null);
        numberOfOpenSpots++;
        return car;
    }

    /**
     * @return The first free location in the parking garage.
     */
    public Location getFirstFreeLocation() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    if (getCarAt(location) == null) {
                        return location;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return The first car ready to leave its parking spot.
     */
    public Car getFirstLeavingCar() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null && car.getMinutesLeft() <= 0 && !car.getIsPaying()) {
                        return car;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Ticks all the cars to remove a minute from the minutes they have left.
     */
    public void tickCars() {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
                    Location location = new Location(floor, row, place);
                    Car car = getCarAt(location);
                    if (car != null) {
                        car.tick();
                    }
                }
            }
        }
    }

    /**
     * Checks if a location is valid, depending on the size of the garage.
     * @param location the location that needs to be checked.
     * @return returns true if the specified location is valid.
     */
    private boolean locationIsValid(Location location) {
        int floor = location.getFloor();
        int row = location.getRow();
        int place = location.getPlace();
        if (floor < 0 || floor >= numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces) {
            return false;
        }
        return true;
    }
    
    
    boolean isEvent = false;
    
    private void checkForEvent() {
    	double chance = Math.random();
    	if(chance < eventChancePercentage * 0.01) {//50%
    		isEvent = true;
    		System.out.println("Event has started");
    	}
    	else {
    		isEvent = false;
    		System.out.println("Event has stopped");
    	}
    }

    private void setArrivals() {
    	//Handle normal days
    	if(day <= 5) {
    		adHocArrivals = (int)Math.floor(standardArrivals * adHocArrivals_week);
        	passArrivals = (int)Math.floor(standardArrivals * passArrivals_week);
    	}
    	else if(day > 5) {
    		adHocArrivals = (int)Math.floor(standardArrivals * adHocArrivals_weekend);
        	passArrivals = (int)Math.floor(standardArrivals * passArrivals_weekend);
    	}
    	
    	//Handle events
    	if(isEvent && day <= 5) { //In the week there should be a few pass holders
    		adHocArrivals = (int)Math.floor(standardArrivals * adHocArrivals_weekend);
    		passArrivals = (int)Math.floor(standardArrivals * passArrivals_eventWeek);
    	}
    	else if(isEvent && day > 5) { //In the weekend there should be no pass holders
    		adHocArrivals = (int)Math.floor(standardArrivals * adHocArrivals_weekend);
    		passArrivals = (int)Math.floor(standardArrivals * passArrivals_weekend);
    	}
    	
    	System.out.println("adHocArrivals_week: " + adHocArrivals_week);
    }
}
