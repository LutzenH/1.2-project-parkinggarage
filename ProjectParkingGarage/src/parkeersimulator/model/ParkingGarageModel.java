package parkeersimulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import parkeersimulator.model.car.AdHocCar;
import parkeersimulator.model.car.Car;
import parkeersimulator.model.car.Car.CarType;
import parkeersimulator.model.car.ParkingPassCar;
import parkeersimulator.model.car.ReservationCar;
import parkeersimulator.model.handler.ModelHandler;
import parkeersimulator.model.location.Coordinate;
import parkeersimulator.model.location.Location;
import parkeersimulator.model.location.Place;
import parkeersimulator.model.prop.EntranceProp;
import parkeersimulator.model.prop.ExitProp;
import parkeersimulator.model.prop.Prop;
import parkeersimulator.model.prop.TicketMachineProp;
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
public class ParkingGarageModel extends AbstractModel {
	
	private boolean drawCheap;
	
	///Check if the garage is opened
	private boolean isGarageOpen;
	
	///Declaration of the different queues in the simulation.
	private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue paymentCarQueue_entireTick; //Holds all the cars that were in the paymentCarQueue in a tick
    private CarQueue exitCarQueue;
    
    private int eventChancePercentage = 10;

    ///Declaration of the time.
    private int year = 0;
    private int month = 0;
    private int week = 0;
    private int day = 0;
    private int hour = 0;
    private int minute = 0;

    ///total amount of ticks
    private int ticks = 0;

    ///The amount of time the thread should wait before executing the next tick().
    private int tickPause = 100;
    
    //Tells us if it's the first day of the month
    private boolean isFirstDayOfMonth;
    
    //Keeps track of the current day in the month
    private int currentDayInMonth;
    
    //Declaration of the amount of days in each month
    private int daysInFeb = 28;
    private int[] dayAmountMonth = new int[] {31, daysInFeb, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

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
    
	public enum CustomiseErrorMessages { ERROR_CUSTOMISE_NOTEMPTY, ERROR_CUSTOMISE_NOTCLOSED, ERROR_CUSTOMISE_NOTREADY }
	
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

    /// number of cars that can enter per minute per entrance
    int baseEnterSpeed = 3;
    /// number of cars that can pay per minute per ticketmachine
    int basePaymentSpeed = 2;
    /// number of cars that can leave per minute exits
    int baseExitSpeed = 5;
    
    // Declaration of variable queueSpeeds based on the amount of entrances, exits and paymentspots.
    int enterSpeed = 0; 
    int paymentSpeed = 0;
    int exitSpeed = 0;
    
    ///Declaration of Multi-dimensional array of places, format: [numberOfFloors][numberOfRows][numberOfPlacesInRow]
    private Place[][][] places;
    
    //Declaration of sortable places 
    private ArrayList<Place> preferredPlaces;
    
    ///Declaration of array of Props;
    private Prop[] props;

    ///Declaration of values needed to generate the parking garage.
	private int numberOfFloors = 3;
	private int numberOfRows = 8;
	private int numberOfPlaces = 30;
	
	///Number of open spots.
	private int numberOfOpenDefaultSpots;
	private int numberOfOpenPassHolderSpots;
	
    ///Amount of spots for Pass holders
    private int passHolderPlaceAmount = 100;
    
    ///Amount of leaving cars
    private int amountOfLeavingCars = 0;

	/**
	 * Constructor of ParkingGarageModel
	 */
    public ParkingGarageModel(ModelHandler handler) {
    	super(handler);
    	
    	///Instantiation of the queue's
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        paymentCarQueue_entireTick = new CarQueue();
        exitCarQueue = new CarQueue();

        this.numberOfOpenDefaultSpots = (numberOfFloors * numberOfRows * numberOfPlaces) - passHolderPlaceAmount;
        this.numberOfOpenPassHolderSpots = passHolderPlaceAmount;
        
        this.isGarageOpen = false;
        this.drawCheap = false;
        
        ///Instantiation of the all possible car positions.
        places = new Place[numberOfFloors][numberOfRows][numberOfPlaces];
        preferredPlaces = new ArrayList<Place>();
        populatePlaces();
        
        props = new Prop[numberOfRows * numberOfFloors];
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
     * A method that is used for incrementing the current time.
     */
    private void advanceTime(){
        // Advance the time by one minute.
        minute++;
        ticks++;
        
        while (minute > 59) { //Reset minutes and set hours
            minute -= minute;
            hour++;
        }
        while (hour > 23) { //Reset hours and set days
            hour -= hour;
            day++;
            isFirstDayOfMonth = false;
        }
        while (day > 6) { //Reset days and set weeks
            day -= day;
            week++;
        }
        
        currentDayInMonth = (week * 7) + day;
        while(currentDayInMonth > dayAmountMonth[month] - 1) { //Reset weeks and set months
        	currentDayInMonth = day;
        	week -= week;
        	month++;
        	isFirstDayOfMonth = true;
        }
        
        while(month > 11) { //Reset months and set years
        	month -= month;
        	year++;
        	
        	if((year + 1) % 4 == 0) //leap year
        		daysInFeb = 29;
        	else
        		daysInFeb = 28;
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
    	if(isGarageOpen) {
            int numberOfCars=getNumberOfCars(reservationArrivals);
            addArrivingCars(numberOfCars, CarType.RESERVERATION_CAR);
        	numberOfCars=getNumberOfCars(adHocArrivals);  
    		addArrivingCars(numberOfCars, CarType.AD_HOC); 
        	numberOfCars=getNumberOfCars(passArrivals);
            addArrivingCars(numberOfCars, CarType.PASS); 
    	}
    }

    /**
     * Removes cars from the front of the queue and assign to a parking space depending on the enterSpeed.
     * 
     * @param queue the queue that the cars should be taken from.
     */
    private void carsEntering(CarQueue queue){
        int i=0;
        
        if(queue == entrancePassQueue) {
        	while (queue.carsInQueue()>0 && 
        			getNumberOfOpenPassHolderSpots()>0 && 
        			i<enterSpeed) {
                Car car = queue.removeCar();
                Location freeLocation = getFirstFreeLocation(car.getCarType());
                
                setCarAt(freeLocation, car);
                i++;
        	}
        }
        else if (queue == entranceCarQueue ) {
        	while (queue.carsInQueue()>0 && getNumberOfOpenDefaultSpots() > 0 && i<enterSpeed) {
        		Car car = queue.removeCar();
                Location freeLocation = getFirstFreeLocation(car.getCarType());
                
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
	            paymentCarQueue_entireTick.addCar(car);
        	}
        	else {
        		carLeavesSpot(car);
        	}
            car = getFirstLeavingCar();
        }
    }

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
	            	int queueLengthBeforeLeving = new Random().nextInt(11) + 15;
	            	if(getEntranceCarQueue().carsInQueue() <= queueLengthBeforeLeving) {
	            		entranceCarQueue.addCar(new AdHocCar(stayMinutes));
	            		amountOfLeavingCars ++;
	            	}
	            }
	            break;
	    	case PASS:
	            for (int i = 0; i < numberOfCars; i++) {
	            	entrancePassQueue.addCar(new ParkingPassCar(stayMinutes));
	            }
	            break;
	        //If the car is a RESERVERATION_CAR check for an open spot for a AD_HOC car and set it to reserved
	    	case RESERVERATION_CAR:
	            for (int i = 0; i < numberOfCars; i++) {	            	
	            	Location location = getFirstFreeLocation(CarType.AD_HOC);
	            	
	            	if (location != null) {
		            	entranceCarQueue.addCar(new ReservationCar());
		            	places [location.getFloor()][location.getRow()][location.getPlace()].setReserved(true);
		            	numberOfOpenDefaultSpots--; 
	            	}            	
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
    	places [car.getLocation().getFloor()][car.getLocation().getRow()][car.getLocation().getPlace()].setReserved(false);
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
     * @return The current year.
     */
    public int getYear() { return year; }

    /**
     * @return The current month of a year.
     */
    public int getMonth() { return month; }
    
    /**
     * @return The current week of a month.
     */
    public int getWeek() { return week; }
    
    /**
     * @return The total amount of ticks
     */
    public int getTicks() { return ticks; }
    
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
     * @return The amount of cars that has leaved the entranceCarqueue.
     */
    public int getAmountOfLeavingCars() {return amountOfLeavingCars;}

    /**
     * @return The bool that tells the program if it is the first day of the month
     */
    public boolean getIsFirstDayOfMonth() { return isFirstDayOfMonth; }
    
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
    
    /**
     * @return An array of all props
     */
    public Prop[] getProps() { return props; }
    
    //TODO: add illegalArgumentsException above propsSize
    /**
     * Sets the prop to the next prop depending on its current state
     * @param index the index of the prop in props
     */
    public void setProp(int index) {   	
    	if(!isGarageOpen && isGarageEmpty()) {
    		Coordinate position = Prop.calculateCoordinate(index, getNumberOfPlaces());
    		
        	if(props[index] != null) {
            	switch(props[index].getType()) {
        			case PROP_ENTRANCE:
        				props[index] = new ExitProp(position);
        				break;
        			case PROP_EXIT:
        				props[index] = new TicketMachineProp(position);
        				break;
        			case PROP_TICKETMACHINE:
        				props[index] = null;
        				break;
        			default:
        				props[index] = new EntranceProp(position);
        				break;
            	}	
        	}
        	else {
        		props[index] = new EntranceProp(position);
        	}
        	
        	updatePlacePreferences();
    	}
    }
    
    private void updatePlacePreferences() {
		for (int floor = 0; floor < getNumberOfFloors(); floor++) {
			for (int row = 0; row < getNumberOfRows(); row++) {
				for (int place = 0; place < getNumberOfPlaces(); place++) {
					Location location = new Location(floor, row, place);
					
					Coordinate position = Location.convertToCoordinate(location, getNumberOfRows());
					
					float preferenceFactor = Float.MAX_VALUE;
					
					paymentSpeed = 0;
					exitSpeed = 0;
					enterSpeed = 0;
					
					for(Prop prop : props) {
						if(prop != null) {
							switch(prop.getType()) {
								case PROP_ENTRANCE:
									enterSpeed += baseEnterSpeed;
									break;
								case PROP_EXIT:
									exitSpeed += baseExitSpeed;
									break;
								case PROP_TICKETMACHINE:
									paymentSpeed += basePaymentSpeed;
									break;
								default:
									break;
							}
							
							float distance = Coordinate.calculateDistance(position, prop.getPosition());
							
							if(distance < preferenceFactor)
								preferenceFactor = distance;
						}
					}
					
					places[floor][row][place].setPreferenceFactor(preferenceFactor);
				}
			}
		}
		
		Collections.sort(preferredPlaces);
		
		int count = 0;
		for(Place place : preferredPlaces) {
			if(count < passHolderPlaceAmount) {
				place.setCarType(CarType.PASS);
			}
			else {
				place.setCarType(null);
			}
			count++;
		}
	}

	/**
	 * @return the drawCheap
	 */
	public boolean getDrawCheap() {
		return drawCheap;
	}

	/**
	 * @param drawCheap the drawCheap to set
	 */
	public void setDrawCheap() {
		this.drawCheap = this.drawCheap ? false : true;
	}
    
	//TODO Optimize this method.
    /**
     * Get the total car count of a certain car type.
     * @param type The type of car that should be used to calculate the car count.
     * @return amount of cars of this type currently in the parking garage.
     */
    public int getCarCount(Car.CarType type) {
    	int count = 0;
    	
    	for(Place[][] floor : places) {
    		for(Place[] row : floor) {
        		for(Place place : row) {
        			if(place.getCar() != null) {
            			if(place.getCar().getCarType() == type)
            				count++;
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
     * @return The paymentCarQueue for the entire tick 
     */
    public CarQueue getPaymentCarQueue_entireTick() { return paymentCarQueue_entireTick; }
    
    /**
     * @return The exitCarQueue.
     */
    public CarQueue getExitCarQueue() { return exitCarQueue; }
    
    /**
     * 
     */
    public int getPaymentSpeed() { return paymentSpeed; }
    
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
            if(car.getCarType() == Car.CarType.PASS)
            	numberOfOpenPassHolderSpots--;
            else if(car.getCarType() == Car.CarType.AD_HOC) {
            	numberOfOpenDefaultSpots--;
            	}
            
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
        if(car.getCarType() == Car.CarType.PASS)
        	numberOfOpenPassHolderSpots++;
        else
            numberOfOpenDefaultSpots++;

        return car;
    }

    /**
     * @return The first free location in the parking garage.
     */
    public Location getFirstFreeLocation(CarType carType) {
    	for(Place place : preferredPlaces) {
            Location location = place.getLocation();
            
            //Check if the location contains a car.
            if(getCarAt(location) == null) {
            	CarType allowedType = place.getCarType();
            	
            	//Check if the arrived car is allowed to stand at this spot.
                if(allowedType != null) {
            		if(carType == allowedType) {        			
            			return location;
            		}
                }
                //If any type of car is allowed at this spot: 
                else {
                	//If the arrived car is a reservation car check if the spot is reserved or not.
                	if(carType == CarType.RESERVERATION_CAR) {
                		if(place.getReserved())
                			return location;
                	}
                	//If the arrived car is not a reservation car check if spot is unreserved.
                	else {
                		if(!place.getReserved())
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
    
    public int leaveTheEntranceCarQueueOnAverageEveryDay() {
    		double amount = (amountOfLeavingCars / (ticks / 60d / 24d));
    		return (int) Math.round(amount);
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
        
        if (floor < 0 || floor > numberOfFloors || row < 0 || row > numberOfRows || place < 0 || place > numberOfPlaces) {
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
    					places[floor][rows][place] = new Place(Car.CarType.PASS, new Location(floor, rows, place));
    				else
    					places[floor][rows][place] = new Place(new Location(floor, rows, place));
    				
    				preferredPlaces.add(places[floor][rows][place]);
    				
    				count++;
    			}
    		}
    	}
    }

	/**
	 * @return the isGarageOpen
	 */
	public boolean isGarageOpen() {
		return isGarageOpen;
	}
	
	/**
	 * Opens the garage when closed, closes the garage when open
	 */
	public void openGarage() {
		if(getCustomisationErrorMessages() == null || getCustomisationErrorMessages() == CustomiseErrorMessages.ERROR_CUSTOMISE_NOTCLOSED)
		{
			isGarageOpen = isGarageOpen ? false : true;
		}
	}
    
	public boolean isGarageEmpty() {
    	for(int floor = 0; floor < numberOfFloors; floor++) {
    		for(int rows = 0; rows < numberOfRows; rows++) {
    			for(int place = 0; place < numberOfPlaces; place++) {
    				if(places[floor][rows][place].getCar() != null)
    					return false;
    			}
    		}
    	}
    	return true;
	}

	/**
	 * @return the customisationErrorMessages
	 */
	public CustomiseErrorMessages getCustomisationErrorMessages() {
		if(!hasRequiredProps())
			return CustomiseErrorMessages.ERROR_CUSTOMISE_NOTREADY;
		else if(isGarageOpen)
			return CustomiseErrorMessages.ERROR_CUSTOMISE_NOTCLOSED;
		else if(!isGarageEmpty())
			return CustomiseErrorMessages.ERROR_CUSTOMISE_NOTEMPTY;
		else
			return null;
	}

	/**
	 * Checks if the garage has the required props inorder to be opened.
	 * @return true if it has the required props
	 */
	private boolean hasRequiredProps() {
		boolean hasEntrance = false;
		boolean hasExit = false;
		boolean hasTicketMachine = false;
		
		for(Prop prop : props) {
			if(prop != null) {
				switch(prop.getType()) {
					case PROP_ENTRANCE:
						hasEntrance = true;
						break;
					case PROP_EXIT:
						hasExit = true;
						break;
					case PROP_TICKETMACHINE:
						hasTicketMachine = true;
						break;
						
					default:
						break;
				}
			}
		}
		return (hasEntrance && hasExit && hasTicketMachine);
	}
	
}
