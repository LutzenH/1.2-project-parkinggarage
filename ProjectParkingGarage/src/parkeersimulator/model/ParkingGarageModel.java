package parkeersimulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import parkeersimulator.handler.ModelHandler;
import parkeersimulator.model.AbstractModel.ModelType;
import parkeersimulator.model.car.AdHocCar;
import parkeersimulator.model.car.Car;
import parkeersimulator.model.car.Car.CarType;
import parkeersimulator.model.car.ParkingPassCar;
import parkeersimulator.model.car.ReservationCar;
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
	
	private FinanceModel financeModel;
	private TimeModel timeModel;

	///Check if the garage is opened
	private boolean isGarageOpen;
	
	///Declaration of the different queues in the simulation.
	private CarQueue entranceCarQueue;
    private CarQueue entrancePassQueue;
    private CarQueue paymentCarQueue;
    private CarQueue exitCarQueue;
    
    //Arrivals and stay times
    private int baseLineArrivals = 100; ///The baseline number of cars arriving in an hour.
    private int adHocArrivals;
    private int passArrivals;
    private int reservationArrivals;
    
    ///The multipliers of cars arriving in an hour.
    private float adHocArrivals_week = 1f;
    private float adHocArrivals_weekend = 1.7f;
    private float adHocArrivals_event = 2.1f;
    
    private float passArrivals_week = 0.5f;
    private float passArrivals_weekend = 0.05f;
    private float passArrivals_event = 0.3f;
    
    private float reservationArrivals_week = 0.5f;
    private float reservationArrivals_weekend = 1f;
    private float reservationArrivals_event = 0f;
    
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
	
	private int numberOfAllowedPassHolders;
	
    ///Amount of spots for Pass holders
    private int passHolderPlaceAmount = 100;
    
    ///Amount of cars leaving the entrance queue
    private int amountOfLeavingCars = 0;
    
    ///total amount of ticks
    private int ticks = 0;

	/**
	 * Constructor of ParkingGarageModel
	 */
    public ParkingGarageModel(ModelHandler handler) {
    	super(handler);
    	
    	this.financeModel = (FinanceModel) handler.getModel(ModelType.FINANCE);
    	this.timeModel = (TimeModel) handler.getModel(ModelType.TIME);
    	
    	///Instantiation of the queue's
        entranceCarQueue = new CarQueue();
        entrancePassQueue = new CarQueue();
        paymentCarQueue = new CarQueue();
        exitCarQueue = new CarQueue();

        this.numberOfOpenDefaultSpots = (numberOfFloors * numberOfRows * numberOfPlaces) - passHolderPlaceAmount;
        this.numberOfOpenPassHolderSpots = passHolderPlaceAmount;
        this.numberOfAllowedPassHolders = numberOfOpenPassHolderSpots;
        
        this.isGarageOpen = false;
        
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
    	tickCars();
    	handleExit();
    	notifyViews();
    	setArrivalProperties();
    	eventManager();
    	handleEntrance();
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
        	if(queue.getFrontCarType() != null) {
            	switch(queue.getFrontCarType()) {
    				case RESERVERATION_CAR:
    		        	while (queue.carsInQueue()>0 && getNumberOfOpenDefaultSpots() > 0 && i<enterSpeed) {
    		        		Car car = queue.removeCar();
    		                Location freeLocation = getFirstFreeLocation(car.getCarType());
    		                
    		                setCarAt(freeLocation, car);
    		                i++;
    		        	}
    					break;
    				case PASS:
    		        	while (queue.carsInQueue()>0 && getNumberOfOpenPassHolderSpots() > 0 && i<enterSpeed) {
    		                Car car = queue.removeCar();
    		                Location freeLocation = getFirstFreeLocation(car.getCarType());
    		                
    		                setCarAt(freeLocation, car);
    		                i++;
    		        	}
    					break;
    				default:
    					break;
            	}
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
	            financeModel.collectMoney(car);
	            paymentCarQueue.addCar(car);
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
    		Car car = exitCarQueue.removeCar();
    		if(car.getCarType() == CarType.PASS)
    			numberOfAllowedPassHolders++;
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
    	ticks++;
    	switch(type) {
	    	case AD_HOC:
	            for (int i = 0; i < numberOfCars; i++) {
	            	int queueLengthBeforeLeving = new Random().nextInt(11) + 15;
	            	if(getEntranceCarQueue().carsInQueue() <= queueLengthBeforeLeving) {
	            		entranceCarQueue.addCar(new AdHocCar(stayMinutes));
	            		amountOfLeavingCars++;
	            	}
	            }
	            break;
	    	case PASS:
	            for (int i = 0; i < numberOfCars; i++) {
	            	if(numberOfAllowedPassHolders > 0) {
		            	entrancePassQueue.addCar(new ParkingPassCar(stayMinutes));
		            	numberOfAllowedPassHolders--;
	            	}
	            }
	            break;
	        //If the car is a RESERVERATION_CAR check for an open spot for a AD_HOC car and set it to reserved
	    	case RESERVERATION_CAR:
	            for (int i = 0; i < numberOfCars; i++) {	            	
	            	Location location = getFirstFreeLocation(CarType.AD_HOC);
	            	
	            	if (location != null) {
		            	entrancePassQueue.addCar(new ReservationCar());
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
     * @return The baseline for all the cars arriving.
     */
    public int getBaseLineArrivals() { return baseLineArrivals; }
    /**
     * Sets the baseline for all the cars arriving.
     */
    public void setBaseLineArrivals(int amount) { baseLineArrivals = amount; }
    
    
    /**
     * @return The current adHocArrivals_week in percent.
     */
    public Integer getAdHocArrivalsPercent_week() { return Math.round( adHocArrivals_week * 100f); }
    /**
     * Sets the multiplier for adHoc cars arriving during standard weeks.
     */
    public void setAdHocArrivals_week(float amount) { adHocArrivals_week = amount / 100f; }
    
    /**
     * @return The current adHocArrivals_weekend in percent.
     */
    public Integer getAdHocArrivals_weekend() { return Math.round(adHocArrivals_weekend * 100f); }
    /**
     * Sets the multiplier for adHoc cars arriving during weekends.
     */
    public void setAdHocArrivals_weekend(float amount) { adHocArrivals_weekend = amount / 100f; }
    
    /**
     * @return The current adHocArrivals_event in percent.
     */
    public Integer getAdHocArrivals_event() { return Math.round(adHocArrivals_event * 100f); }
    /**
     * Sets the multiplier for adHoc cars arriving at events.
     */
    public void setAdHocArrivals_event(float amount) { adHocArrivals_event = amount / 100f; }
    
    
    /**
     * @return The current passArrivals_week in percent.
     */
    public Integer getPassArrivals_week() { return Math.round( passArrivals_week * 100f); }
    /**
     * Sets the multiplier for pass cars arriving during standard weeks.
     */
    public void setPassArrivals_week(float amount) { passArrivals_week = amount / 100f; }
    
    /**
     * @return The current passArrivals_weekend in percent.
     */
    public Integer getPassArrivals_weekend() { return Math.round(passArrivals_weekend * 100f); }
    /**
     * Sets the multiplier for pass cars arriving during weekends.
     */
    public void setPassArrivals_weekend(float amount) { passArrivals_weekend = amount / 100f; }
    
    /**
     * @return The current passArrivals_eventWeek in percent.
     */
    public Integer getPassArrivals_eventWeek() { return Math.round(passArrivals_event * 100f); }
    /**
     * Sets the multiplier for pass cars arriving at events during standard weeks.
     */
    public void setPassArrivals_eventWeek(float amount) { passArrivals_event = amount / 100f; }

    /**
     * @return The current reservationArrivals_week in percent.
     */
    public Integer getReservationArrivals_week() { return Math.round( reservationArrivals_week * 100f); }
    /**
     * Sets the multiplier for reservation cars arriving during standard weeks.
     */
    public void setReservationArrivals_week(float amount) { reservationArrivals_week = amount / 100f; }
    
    /**
     * @return The current reservationArrivals_weekend in percent.
     */
    public Integer getReservationArrivals_weekend() { return Math.round(reservationArrivals_weekend * 100f); }
    /**
     * Sets the multiplier for reservation cars arriving during weekends.
     */
    public void setReservationArrivals_weekend(float amount) { reservationArrivals_weekend = amount / 100f; }
    
    /**
     * @return The current reservationArrivals_event in percent.
     */
    public Integer getReservationArrivals_event() { return Math.round(reservationArrivals_event * 100f); }
    /**
     * Sets the multiplier for reservation cars arriving at events.
     */
    public void setReservationArrivals_event(float amount) { reservationArrivals_event = amount / 100f; }
    
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

    /**
     * @return a String with advice about the cars leavening the entranceCarQueue.
     */
    public String adviceCarsLeavingEntranceCarQueueu() {
    	String advice = "";
    	double amount = (amountOfLeavingCars / (ticks / 60d / 24d));
		int averageCarsLeavingEntrenceCarQueueu = (int) Math.round(amount);
    	if(averageCarsLeavingEntrenceCarQueueu <= 25) {
    		advice = "Average amount of cars leaving enterence queue every day: " + averageCarsLeavingEntrenceCarQueueu +  "r/n/ Advice: There are enough entrances.";
    	} else if(averageCarsLeavingEntrenceCarQueueu <= 75) {
    		advice = "Average amount of cars leaving enterence queue every day: " + averageCarsLeavingEntrenceCarQueueu +  "r/n/ Advice: one extra entrance could reduce the number of passing cars";
    	} else if(averageCarsLeavingEntrenceCarQueueu > 75) {
    		advice = "Average amount of cars leaving enterence queue every day: " + averageCarsLeavingEntrenceCarQueueu+  "r/n/ Advice: There are not enough entries.";
    	}
    	return advice;
    }
    
    /**
     * @return A String with advice about the PaymentCarQueue.
     */
    public String advicePaymentCarQueue() {
    	String advice = "";
    	double averagePaymentCarQueue = 0d;
    	averagePaymentCarQueue += (paymentCarQueue.carsInQueue() / ticks);
    	int average = (int) Math.round(averagePaymentCarQueue);
    	if (averagePaymentCarQueue < 25) {
    		advice = "Average payment queue: " + average +  "r/n/ Advice: There are enough payment terminals.";
    	} else if (averagePaymentCarQueue <= 75) {
    		advice = "Average payment queue: " + average +  "r/n/ Advice: one extra payment terminal could reduce te mumber of cars waiting in the payment queue.";
    	} else if (averagePaymentCarQueue > 75) {
    		advice = "Average payment queue: " + average +  "r/n/ Advice: There are not enough payment terminals";
    	}	
    	return advice;
    }
    
    /**
     * @return A String with advice about the exitCarQueue.
     */
    public String adviceExitCarQueue() {
    	String advice = "";
    	double averageExitCarQueue = 0d;
    	averageExitCarQueue += (exitCarQueue.carsInQueue() / ticks);
    	int average = (int) Math.round(averageExitCarQueue);
    	if (averageExitCarQueue <= 25) {
    		advice ="Average exit queue: " + average +  "r/n/ Advice: There are enough exits.";
    	} else if (averageExitCarQueue <= 75) {
    		advice = "Average exit queue: " + average +  "r/n/ Advice: One extra exit could reduce te mumber of cars waiting in the exit queue.";
    	} else if (averageExitCarQueue > 75) {
    		advice = "Average exit queue: " + average +  "r/n/ Advice: There are not enough exits";
    	}	
    	return advice;
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
    	int[] time = timeModel.getTime(); //Minute, hour, day
    	Random random = new Random();
    	
    	//Check if an event has started or not
    	if(!isEventStart && time[0] == 0) {
    		//Check if we can spawn the start of an event
    		if(time[2] == 3 && time[1] == eventStartingHour_ThursdayMarket)
    			isEventStart = true;
    		else if(time[2] == 5 && time[1] == eventStartingHour_SaturdayConcert)
    			isEventStart = true;
    		else if(time[2] == 6 && time[1] == eventStartingHour_SundayConcert)
    			isEventStart = true;
    		
    		if(isEventStart == true) {
    			eventDuration = random.nextInt((4 - 1) + 1) + 1; //Randomly set a duration for the event
    			eventDuration *= 60; //Translate hours to minutes
    		}
    	}
    	else if(isEventStart && time[0] == 0){ //People have 2 hours to show up, this is the + 2
    		//Check if we can despawn the start of an event
    		if(time[2] == 3 && time[1] == eventStartingHour_ThursdayMarket + 2)
    			isEventStart = false;
    		else if(time[2] == 5 && time[1] == eventStartingHour_SaturdayConcert + 2)
    			isEventStart = false;
    		else if(time[2] == 6 && time[1] == eventStartingHour_SundayConcert + 2)
    			isEventStart = false;
    	}
    }
    
    /**
     * Sets the arrival properties of the cars entering the car park. Such as: The amount arriving and the time they stay.
     * We do this based on day, time and events.
     */
    private void setArrivalProperties() {
    	int[] time = timeModel.getTime(); //Minute, hour, day
    	
    	if(time[2] <= 4) {
    		if(!isEventStart) {
    			adHocArrivals = (int)Math.floor(baseLineArrivals * adHocArrivals_week);
            	passArrivals = (int)Math.floor(baseLineArrivals * passArrivals_week);
            	reservationArrivals = (int)Math.floor(baseLineArrivals * reservationArrivals_week);
    		}
    		else if(isEventStart) {
    			adHocArrivals = (int)Math.floor(baseLineArrivals * adHocArrivals_event);
        		passArrivals = (int)Math.floor(baseLineArrivals * passArrivals_event);
        		reservationArrivals = (int)Math.floor(baseLineArrivals * reservationArrivals_event);
    		}
    	}
    	else if(time[2] > 4) {
    		if(!isEventStart) {
    			adHocArrivals = (int)Math.floor(baseLineArrivals * adHocArrivals_weekend);
    			reservationArrivals = (int)Math.floor(baseLineArrivals * reservationArrivals_weekend);
    		}
    		else if(isEventStart) {
    			adHocArrivals = (int)Math.floor(baseLineArrivals * adHocArrivals_event);
    			reservationArrivals = (int)Math.floor(baseLineArrivals * reservationArrivals_event);
    		}
    		
    		passArrivals = (int)Math.floor(baseLineArrivals * passArrivals_weekend);
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
    	
    	int[] time = timeModel.getTime(); //Minute, hour, day
    	
    	if(time[2] <= 3) {
    		timeStamps = timeStamps_week;
    		factors = arrivalFactors_week;
    	}
    	if(time[2] == 4) {
    		timeStamps = timeStamps_friday;
    		factors = arrivalFactors_friday;
    	}
    	else if(time[2] > 4) {
    		timeStamps = timeStamps_weekend;
    		factors = arrivalFactors_weekend;
    	}
    	
    	//Create a variable holding the hours and minutes
    	float hours = time[1];
    	hours += time[0] / 60f;
    	
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
	
	@Override
	public ModelType getModelType() {
		return ModelType.PARKINGGARAGE;
	}
}
