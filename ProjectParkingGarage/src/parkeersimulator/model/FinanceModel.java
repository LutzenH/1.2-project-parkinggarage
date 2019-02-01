package parkeersimulator.model;

import parkeersimulator.handler.ModelHandler;
import parkeersimulator.model.car.Car;

public class FinanceModel extends AbstractModel {

	private TimeModel timeModel;
	private ParkingGarageModel parkingGarageModel;
	
	//Total revenue of the garage
	private float moneyTotal = 0f;
	//Total revenue of the past month
	private float moneyMonth = 0f;
	//Total revenue of the past day
	private float moneyDay = 0f;
	
	//The amount of time an adHocCar car stays before having to pay another costPerTimeFrame
	private int paymentTimeframe = 20;
	
	//The cost for an adHoc staying for every time frame
	private float costPerTimeFrame_adHocCar = 1f;
	//The cost for an adHoc staying for every time frame
	private float costPerTimeFrame_ReservationCar = 1.2f;
	//The cost for a passHolder in a month
	private float cost_passHolderCar = 25f;
	
	//The amount of entrances, ticket machines and exits the carpark currently has
	private int numberOfEntrances;
	private int numberOfTicketMachines;
	private int numberOfExits;
	//The amount an entrance, ticket machine and exit costs
	private int entranceCosts = 6000;
	private int ticketMachineCosts = 3000;
	private int exitCosts = 5000;
	
	//The amount of places, floors and rows
	private int numberOfPlaces;
	private int numberOfRows;
	private int numberOfFloors;
	//The amount an entrance, ticket machine and exit costs
	private float placeCosts = 2.1f;
	private int rowCosts = 500;
	private int floorCosts = 2000;
	
	private int passHolderPlaceAmount;
	
	//The cost for salary and upkeep
	private int maintenanceCosts = 60000;
	
	public FinanceModel(ModelHandler modelHandler) {
		super(modelHandler);
		
		this.timeModel = (TimeModel) modelHandler.getModel(ModelType.TIME);
	}
	
	@Override
	public ModelType getModelType() {
		return ModelType.FINANCE;
	}

	public void tick() {
		manageMoney();
		notifyViews();
	}

	public void collectMoney(Car car) {
        int minutesStayed = car.getMinutesStayed();
        int totalFramesStayed = (int) Math.ceil(car.getMinutesStayed() / paymentTimeframe);
        
		switch(car.getCarType()) {
			case AD_HOC:
				moneyDay += totalFramesStayed * costPerTimeFrame_adHocCar;
				break;
			case RESERVERATION_CAR:
				moneyDay += totalFramesStayed * costPerTimeFrame_ReservationCar;
				break;
			case PASS:
				break; //Passholders pay at the end of the month
				
			default:
				break;
		}
	}
	
	private void manageMoney() {
		if(timeModel.getHour() == 0 && timeModel.getMinute() == 0) {
			moneyMonth += moneyDay;
			moneyDay = 0;
		}
		
		if(timeModel.getIsFirstDayOfMonth() && timeModel.getHour() == 8 && timeModel.getMinute() == 30) {
			getMoney();
			payMoney();
			
			moneyTotal += moneyMonth;
			moneyMonth = 0;
		}
	}
	
	private void getMoney() {
		//Let pass holders pay
		moneyMonth += passHolderPlaceAmount * cost_passHolderCar;
	}
	
	private void payMoney() {
		//Pay taxes
		moneyMonth = payMoney_taxes(moneyMonth);
		//Pay maintenance
		moneyMonth = payMoney_maintenance(moneyMonth);
	}
	private float payMoney_taxes(float amount) {
		//We pay taxes at 8.30 in the morning
		if(amount > 0 && amount <= 20384)
			amount *= (1f - 0.3665f);
		if(amount > 20384 && amount <= 68507)
			amount *= (1f - 0.3810f);
		if(amount > 68507)
			amount *= (1f - 0.5175f);
		
		return amount;
	}
	private float payMoney_maintenance(float amount) {
		//Pay maintenance costs
		amount -= numberOfEntrances * entranceCosts;
		amount -= numberOfTicketMachines * ticketMachineCosts;
		amount -= numberOfExits * exitCosts;
		
		amount -= numberOfPlaces * placeCosts;
		amount -= numberOfRows * rowCosts;
		amount -= numberOfFloors * floorCosts;
		
		amount -= maintenanceCosts;
		
		return amount;
	}
	
	
	/**
	 * Gets the money the garage has in total
	 */
	public float getMoneyTotal() { return moneyTotal; }
	/**
	 * Gets the money the garage has earned this month
	 */
	public float getMoneyMonth() { return moneyMonth; }
	/**
	 * Gets the money the garage has earned this day
	 */
	public float getMoneyDay() { return moneyDay; }
	
	/**
	 * Adds money to the toal budget
	 */
	public void addMoney(float amount) { moneyTotal += amount; }
	/**
	 * Removes money to the total budget
	 */
	public void retractMoney(float amount) { moneyTotal -= amount; }
	
	/**
	 * @return Gets the time when a car has to pay more money for their stay
	 */
	public int getPaymentTimeframe() { return paymentTimeframe; }
	/**
	 * Sets the time when a car has to pay more money for their stay
	 */
	public void setPaymentTimeframe(int amount) { paymentTimeframe = amount; }
	
	/**
	 * @return Gets the amount an adHoc car has to pay for staying in the garage every timeFrame
	 */
	public float getCostPerTimeFrame_adHocCar() { return costPerTimeFrame_adHocCar; }
	/**
	 * Sets the amount an adHoc car has to pay for staying in the garage every timeFrame
	 */
	public void setCostPerTimeFrame_adHocCar(float amount) { costPerTimeFrame_adHocCar = amount; }
	
	/**
	 * @return Gets the amount a reservation car has to pay for staying in the garage every timeFrame
	 */
	public float getCostPerTimeFrame_reservationCar() { return costPerTimeFrame_ReservationCar; }
	/**
	 * Sets the amount a reservation car has to pay for staying in the garage every timeFrame
	 */
	public void setCostPerTimeFrame_reservationCar(float amount) { costPerTimeFrame_ReservationCar = amount; }

	/**
	 * @return Gets the amount a pass holder car has to pay every month
	 */
	public float getCostPerMonth_passHolderCar() { return cost_passHolderCar; }
	/**
	 * Sets the amount a pass holder car has to pay every month
	 */
	public void setCostPerMonth_passHolderCar(float amount) { cost_passHolderCar = amount; }
	
	/**
	 * @return Gets the amount the garage has to pay for maintenance
	 */
	public int getMaintenanceCosts() { return maintenanceCosts; }
	/**
	 * Sets the amount the garage has to pay for maintenance
	 */
	public void setMaintenanceCosts(int amount) { maintenanceCosts = amount; }
	
	/**
	 * @return Gets the number of entrances to the carpark
	 */
	public int getNumberOfEntrances() { return numberOfEntrances; }
	/**
	 * Sets the number of entrances to the carpark
	 */
	public void setNumberOfEntrances(int amount) { numberOfEntrances = amount; }

	/**
	 * @return Gets the number of ticket machines in the carpark
	 */
	public int getNumberOfTicketMachines() { return numberOfTicketMachines; }
	/**
	 * Sets the number of ticket machines in the carpark
	 */
	public void setNumberOfTicketMachines(int amount) { numberOfTicketMachines = amount; }

	/**
	 * @return Gets the number of exits from the carpark
	 */
	public int getNumberOfExits() { return numberOfExits; }
	/**
	 * Sets the number of exits from the carpark
	 */
	public void setNumberOfExits(int amount) { numberOfExits = amount; }

	/**
	 * @return Gets the amount of costs for an entrance in the carpark
	 */
	public int getEntranceCosts() { return entranceCosts; }
	/**
	 * Sets the amount of costs for an entrance in the carpark
	 */
	public void setEntranceCosts(int amount) { entranceCosts = amount; }

	/**
	 * @return Gets the amount of costs for a ticket machine in the carpark
	 */
	public int getTicketMachineCosts() { return ticketMachineCosts; }
	/**
	 * Sets the amount of costs for a ticket machine in the carpark
	 */
	public void setTicketMachineCosts(int amount) { ticketMachineCosts = amount; }

	/**
	 * @return Gets the amount of costs for an exit machine in the carpark
	 */
	public int getExitCosts() { return exitCosts; }
	/**
	 * Sets the amount of costs for an exit machine in the carpark
	 */
	public void setExitCosts(int amount) { exitCosts = amount; }
	
	/**
	 * @return Gets the amount places in the carpark
	 */
	public int getNumberOfPlaces() { return numberOfPlaces; }
	/**
	 * Sets the amount places in the carpark
	 */
	public void setNumberOfPlaces(int amount) { numberOfPlaces = amount; }
	
	/**
	 * @return Gets the amount rows in the carpark
	 */
	public int getNumberOfRows() { return numberOfRows; }
	/**
	 * Sets the amount rows in the carpark
	 */
	public void setNumberOfRows(int amount) { numberOfRows = amount; }

	/**
	 * @return Gets the amount floors in the carpark
	 */
	public int getNumberOfFloors() { return numberOfFloors; }
	/**
	 * Sets the amount floors in the carpark
	 */
	public void setNumberOfFloors(int amount) { numberOfFloors = amount; }
	
	/**
	 * Sets the amount places, rows and floors all at once in the carpark
	 */
	public void setNumberOff_RowFloorPlaces(int[] num) {
		numberOfPlaces = num[0] * num[1] * num[2];
		numberOfRows = num[1] * num[2];
		numberOfFloors = num[2];
	}
	
	/**
	 * @return Gets the amount a place costs for maintenance
	 */
	public float getPlaceCosts() { return placeCosts; }
	/**
	 * Sets the amount a place costs for maintenance
	 */
	public void setPlaceCosts(float amount) { placeCosts = amount; }

	/**
	 * @return Gets the amount a row costs for maintenance
	 */
	public int getRowCosts() { return rowCosts; }
	/**
	 * Sets the amount a row costs for maintenance
	 */
	public void setRowCosts(int amount) { rowCosts = amount; }
	
	/**
	 * @return Gets the amount a floor costs for maintenance
	 */
	public int getFloorCosts() { return floorCosts; }
	/**
	 * Sets the amount a floor costs for maintenance
	 */
	public void setFloorCosts(int amount) { floorCosts = amount; }
	
	/**
	 * @return Gets the amount of passHolder spots the parking garage has
	 */
	public int getPassHolderPlaceAmount() { return passHolderPlaceAmount; }
	/**
	 * Sets the amount of passHolder spots the parking garage has
	 */
	public void setPassHolderPlaceAmount(int amount) { passHolderPlaceAmount = amount; }
}
