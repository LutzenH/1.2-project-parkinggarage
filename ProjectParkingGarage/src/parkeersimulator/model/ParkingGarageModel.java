package parkeersimulator.model;

import java.util.ArrayList;
import java.util.Random;

import parkeersimulator.model.car.AdHocCar;
import parkeersimulator.model.car.Car;
import parkeersimulator.model.car.ParkingPassCar;
import parkeersimulator.model.car.ReservationCar;
import parkeersimulator.model.location.Location;
import parkeersimulator.model.location.Place;
import parkeersimulator.model.queue.CarQueue;

/**
 * The model of the parking garage simulation.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 * @author shand
 * 
 */
public class ParkingGarageModel extends AbstractModel implements Runnable {

	///boolean that is used for when threads need to stop running.
	private boolean run;
	
	//TODO Replace these different types with an Enum.
	///Id of the different types of cars.
	public enum CarType { AD_HOC, PASS, RESERVERATION_CAR }
	
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
    private int week = 0;

    ///The amount of time the thread should wait before executing the next tick().
    private int tickPause = 100;

    //Arrivals and stay times
    ///The baseline number of cars arriving in an hour.
    int standardArrivals = 100;
    int adHocArrivals;
    int passArrivals;
    int reservationArrivals;
    
    ///The multipliers of cars arriving in an hour.
    float adHocArrivals_week = 1f;
    float adHocArrivals_weekend = 1.7f;
    float adHocArrivals_event = 2.1f;
    
    float passArrivals_week = 0.5f;
    float passArrivals_weekend = 0.05f;
    float passArrivals_event = 0.3f;
    
    float reservationArrivals_week = 0.5f;
    float reservationArrivals_weekend = 1f;
    float reservationArrivals_event = 0f;
    
    //The amount of time a car can stay in the car park;
    private int stayMinutes;
    
    //Events
    //Boolean that intitializes an event
    boolean isEventStart = false;
    
    //Integer that holds the duration of an event
    int eventDuration;
    
    //Integers that hold the starting time of certain events
    int eventStartingHour_ThursdayMarket = 18;
	int eventStartingHour_SaturdayConcert = 19;
	int eventStartingHour_SundayConcert = 15;
    
	
	//Time arrival
	//The double that holds the factor that gets applied to the number of arriving cars for simulating busy times
    private double timeArrivalFactor;
    
    //The arraylists used for changing the timeArrivalFactor multiplier
    ArrayList<Float> timeStamps_week = new ArrayList<Float>() {{ add(0f); add(4.5f); add(8f); add(11.75f); add(15.5f); add(17f); add(24f); }};
    ArrayList<Float> arrivalFactors_week = new ArrayList<Float>() {{ add(0.2f); add(0.1f); add(1f); add(0.3f); add(0.4f); add(0.5f); add(0.2f); }};
  
    ArrayList<Float> timeStamps_friday = new ArrayList<Float>() {{ add(0f); add(4.5f); add(8f); add(11.75f); add(15.5f); add(17f); add(24f); }};
    ArrayList<Float> arrivalFactors_friday = new ArrayList<Float>() {{ add(0.1f); add(0.15f); add(1f); add(0.3f); add(0.4f); add(0.8f); add(0.5f); }};
    
    ArrayList<Float> timeStamps_weekend = new ArrayList<Float>() {{ add(0f); add(4.5f); add(12f); add(14.5f); add(15.5f); add(16.75f); add(17.0f); add(24f); }};
    ArrayList<Float> arrivalFactors_weekend = new ArrayList<Float>() {{ add(0.6f); add(0f); add(1f); add(0.6f); add(0.3f); add(0.2f); add(1f); add(0.6f); }};

    
    /// number of cars that can enter per minute
    int enterSpeed = 3; 
    /// number of cars that can pay per minute
    int paymentSpeed = 7;
    /// number of cars that can leave per minute
    int exitSpeed = 5;
    
    ///Declaration of Multi-dimensional array of places, format: [numberOfFloors][numberOfRows][numberOfPlacesInRow]
    private Place[][][] places;

    ///Declaration of values needed to generate the parking garage.
	private int numberOfFloors = 3;
	private int numberOfRows = 8;
	private int numberOfPlaces = 30;
	
	//Number of open spots.
	private int numberOfOpenDefaultSpots;
	private int numberOfOpenPassHolderSpots;
	
    //Amount of spots for Pass holders
    private int passHolderPlaceAmount = 100;

	/**
	 * Constructor of ParkingGarageModel
	 */
    public ParkingGarageModel() {
    	///Instantiation of the queue's
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();

        this.numberOfOpenDefaultSpots = (numberOfFloors * numberOfRows * numberOfPlaces) - passHolderPlaceAmount;
        this.numberOfOpenPassHolderSpots = passHolderPlaceAmount;
        
        ///Instantiation of the all possible car positions.
        places = new Place[numberOfFloors][numberOfRows][numberOfPlaces];
        populatePlaces();
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
    	setArrivalProperties();
    	eventManager();
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
        }
        while (day > 6) {
            day -= 7;
            week++;
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
		addArrivingCars(numberOfCars, CarType.AD_HOC); 
    	numberOfCars=getNumberOfCars(passArrivals);
        addArrivingCars(numberOfCars, CarType.PASS); 
        numberOfCars=getNumberOfCars(reservationArrivals);
        addArrivingCars(numberOfCars, CarType.RESERVERATION_CAR);
    }

    /**
     * Removes cars from the front of the queue and assign to a parking space depending on the enterSpeed.
     * 
     * @param queue the queue that the cars should be taken from.
     */
    private void carsEntering(CarQueue queue){
        int i=0;
        
        if(queue == entranceCarQueue )
        {
        	while (queue.carsInQueue()>0 && 
        			getNumberOfOpenDefaultSpots()>0 && 
        			i<enterSpeed) {
                Car car = queue.removeCar();
                Location freeLocation = getFirstFreeLocation(car);
                setCarAt(freeLocation, car);
                i++;
            }
        }
        else if (queue == entrancePassQueue)
        {
        	while (queue.carsInQueue()>0 && 
        			getNumberOfOpenPassHolderSpots()>0 && 
        			i<enterSpeed) {
                Car car = queue.removeCar();
                Location freeLocation = getFirstFreeLocation(car);
                setCarAt(freeLocation, car);
                i++;
            }
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
    private void addArrivingCars(int numberOfCars, CarType type){
    	switch(type) {
    	case AD_HOC: 
            for (int i = 0; i < numberOfCars; i++) {
            	entranceCarQueue.addCar(new AdHocCar(stayMinutes));
            }
            break;
    	case PASS:
            for (int i = 0; i < numberOfCars; i++) {
            	entrancePassQueue.addCar(new ParkingPassCar(stayMinutes));
            }
            break;	     
    	case RESERVERATION_CAR:
            for (int i = 0; i < numberOfCars; i++) {
            	entranceCarQueue.addCar(new ReservationCar());
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
    public Integer getPassArrivals_eventWeek() { return Math.round(passArrivals_event * 100f); }
    /**
     * Sets the multiplier for pass cars arriving at events during standard weeks.
     */
    public void setPassArrivals_eventWeek(float amount) {passArrivals_event = amount / 100f; }
    
    
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
     * @return The total amount of default open spots in the parking garage.
     */
    public int getNumberOfOpenDefaultSpots() { return numberOfOpenDefaultSpots; }
    
    /**
     * @return The total amount of open passholder spots in the parking garage.
     */
    public int getNumberOfOpenPassHolderSpots() { return numberOfOpenPassHolderSpots; }
    
    /**
     * @return A multi-dimensional array of all cars in the parking garage, format: car[floor][row][place]
     */
    public Place[][][] getPlaces() { return places; }
    
    //TODO Optimize this method.
    /**
     * Get the total car count of a certain car type.
     * @param type The type of car that should be used to calculate the car count.
     * @return amount of cars of this type currently in the parking garage.
     */
    public int getCarCount(CarType type) {
    	int count = 0;
    	
    	for(Place[][] floor : places) {
    		for(Place[] row : floor) {
        		for(Place place : row) {
        			switch(type) {
	        			case AD_HOC:
	        				if(place.getCar() instanceof AdHocCar)
	        					count++;
	        				break;
	        			case PASS:
	        				if(place.getCar() instanceof ParkingPassCar)
	        					count++;
	        				break;
	        			case RESERVERATION_CAR:
	        				if(place.getCar() instanceof ReservationCar)
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
        return places[location.getFloor()][location.getRow()][location.getPlace()].getCar();
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
            places[location.getFloor()][location.getRow()][location.getPlace()].setCar(car);
            car.setLocation(location);
            if(car.getCarType() == CarType.PASS)
            	numberOfOpenPassHolderSpots--;
            else
            	numberOfOpenDefaultSpots--;
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
        places[location.getFloor()][location.getRow()][location.getPlace()].setCar(null);
        car.setLocation(null);
        if(car.getCarType() == CarType.PASS)
        	numberOfOpenPassHolderSpots++;
        else
            numberOfOpenDefaultSpots++;

        return car;
    }

    /**
     * @return The first free location in the parking garage.
     */
    public Location getFirstFreeLocation(Car car) {
        for (int floor = 0; floor < getNumberOfFloors(); floor++) {
            for (int row = 0; row < getNumberOfRows(); row++) {
                for (int place = 0; place < getNumberOfPlaces(); place++) {
               
                	CarType[] allowedTypes = places[floor][row][place].getCarTypes();
                    Location location = new Location(floor, row, place);
                	
                	if(allowedTypes == null) {
                        if (getCarAt(location) == null) {
                            return location;
                        }
                	}
                	else {
                    	for(CarType type : allowedTypes) {
                    		if(car.getCarType() == type && getCarAt(location) == null) {
                    			return location;
                    		}
                    	}
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
    
    
    /**
     * Checks if we can spawn or despawn an event.
     */
    private void eventManager() {
    	Random random = new Random();
    	
    	//Check if an event has started or not
    	if(!isEventStart && minute == 0) {
    		//Check if we can spawn the start of an event
    		if(day == 3 && hour == eventStartingHour_ThursdayMarket)
    			isEventStart = true;
    		else if(day == 5 && hour == eventStartingHour_SaturdayConcert)
    			isEventStart = true;
    		else if(day == 6 && hour == eventStartingHour_SundayConcert)
    			isEventStart = true;
    		
    		if(isEventStart == true) {
    			eventDuration = random.nextInt((4 - 1) + 1) + 1; //Randomly set a duration for the event
    			eventDuration *= 60; //Translate hours to minutes
    		}
    	}
    	else if(isEventStart && minute == 0){ //People have 2 hours to show up, this is the + 2
    		//Check if we can despawn the start of an event
    		if(day == 3 && hour == eventStartingHour_ThursdayMarket + 2)
    			isEventStart = false;
    		else if(day == 5 && hour == eventStartingHour_SaturdayConcert + 2)
    			isEventStart = false;
    		else if(day == 6 && hour == eventStartingHour_SundayConcert + 2)
    			isEventStart = false;
    	}
    }
    
    /**
     * Sets the arrival properties of the cars entering the car park. Such as: The amount arriving and the time they stay.
     * We do this based on day, time and events.
     */

    private void setArrivalProperties() {
    	if(day <= 4) {
    		if(!isEventStart) {
    			adHocArrivals = (int)Math.floor(standardArrivals * adHocArrivals_week);
            	passArrivals = (int)Math.floor(standardArrivals * passArrivals_week);
            	reservationArrivals = (int)Math.floor(standardArrivals * reservationArrivals_week);
    		}
    		else if(isEventStart) {
    			adHocArrivals = (int)Math.floor(standardArrivals * adHocArrivals_event);
        		passArrivals = (int)Math.floor(standardArrivals * passArrivals_event);
        		reservationArrivals = (int)Math.floor(standardArrivals * reservationArrivals_event);
    		}
    	}
    	else if(day > 4) {
    		if(!isEventStart) {
    			adHocArrivals = (int)Math.floor(standardArrivals * adHocArrivals_weekend);
    			reservationArrivals = (int)Math.floor(standardArrivals * reservationArrivals_weekend);
    		}
    		else if(isEventStart) {
    			adHocArrivals = (int)Math.floor(standardArrivals * adHocArrivals_event);
    			reservationArrivals = (int)Math.floor(standardArrivals * reservationArrivals_event);
    		}
    		
    		passArrivals = (int)Math.floor(standardArrivals * passArrivals_weekend);
    	}
    	
    	
    	//Change the amount of cars coming into the car park
    	updateTimeArrivalFactor(); //Update the time arrival factor    	
    	if(!isEventStart) { //Apply the time arrival factor but only when there is not an event starting
			adHocArrivals = (int)Math.floor(adHocArrivals * timeArrivalFactor);
        	passArrivals = (int)Math.floor(passArrivals * timeArrivalFactor);
        	reservationArrivals = (int)Math.floor(reservationArrivals * timeArrivalFactor);
    	}
    	

    	//Change the time cars should stay in the car park
    	updateCarStayMinutes();
    }
    
    
    
    /**
     * Changes the amount of cars entering the car park
     */
    private void updateTimeArrivalFactor() {
    	//Choose what arrayList to use based on the day of the week
    	ArrayList<Float> timeStamps = new ArrayList<>();
    	ArrayList<Float> factors = new ArrayList<>();
    	
    	if(day <= 3) {
    		timeStamps = timeStamps_week;
    		factors = arrivalFactors_week;
    	}
    	if(day == 4) {
    		timeStamps = timeStamps_friday;
    		factors = arrivalFactors_friday;
    	}
    	else if(day > 4) {
    		timeStamps = timeStamps_weekend;
    		factors = arrivalFactors_weekend;
    	}
    	
    	//Create a variable holding the hours and minutes
    	float hours = hour;
    	hours += minute / 60f;
    	
    	//Create variables to hold the timeStamps and factors to use in our cosine wave 
    	float currentStamp;
    	float currentFactor;
    	float nextStamp;
    	float nextFactor;
		
		//Random factor for variation
    	float leftLimit = 0.8f;
    	float randomFactor = leftLimit + new Random().nextFloat() * (1f - leftLimit);
		
		//Create our cosine wave and set our final factor
    	for (int i = 0; i < timeStamps.size(); i++) {
       		//Set our variables
    		currentStamp = timeStamps.get(i);
    		currentFactor = factors.get(i);
    		
    		if(i != timeStamps.size() - 1) {
    			nextStamp = timeStamps.get(i + 1);
    			nextFactor = factors.get(i + 1);
    		}
    		else {
    			nextStamp = timeStamps.get(0);
    			nextFactor = factors.get(0);
    		}
    		
    		//Create a factor
    		if(hours >= currentStamp && hours < nextStamp) {
    			timeArrivalFactor = (currentFactor - nextFactor) / 2f * Math.cos((2f * Math.PI) / ((nextStamp - currentStamp) * 2f) * (hours - currentStamp)) + (currentFactor + nextFactor) / 2f;
    		}
    	}
    	
    	timeArrivalFactor *= randomFactor;
    }

    /**
     * Changes the duration of cars staying in the car park
     */
    private void updateCarStayMinutes() {
    	Random random = new Random();

    	if(isEventStart)
    		stayMinutes = eventDuration;
    	else
    		stayMinutes = (int) Math.floor(random.nextFloat() * (540 * Math.pow(timeArrivalFactor, 2)) + 15);
    }
    
    /**
     * Populates places[][][] with Place's
     */
    private void populatePlaces() {
    	int count = 0;
    	
    	for(int floor = 0; floor < numberOfFloors; floor++) {
    		for(int rows = 0; rows < numberOfRows; rows++) {
    			for(int place = 0; place < numberOfPlaces; place++) {
    				if(count < passHolderPlaceAmount)
    					places[floor][rows][place] = new Place(CarType.PASS);
    				else
    					places[floor][rows][place] = new Place();
    				
    				count++;
    			}
    		}
    	}
    }
    
}
