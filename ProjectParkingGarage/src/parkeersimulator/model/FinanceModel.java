package parkeersimulator.model;

import parkeersimulator.handler.ModelHandler;
import parkeersimulator.model.car.Car;
import parkeersimulator.model.car.Car.CarType;

public class FinanceModel extends AbstractModel {

	///Other Models required for this model to work
	private TimeModel timeModel;
	
	//Total revenue of the garage
	private float moneyTotal = 0f;
	//Total revenue of the past month
	private float moneyMonth = 0f;
	private float moneyLastMonth = 0f;
	//Total revenue of the past day
	private float moneyDay = 0f;
	//Total revenue the parking garage missed out on
	private float moneyMissedOutOn = 0;
	
	private int carsLeftQueue = 0;
	
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
	
	//Values that reset each month and are used for the financial report
	private float monthlyIncomeAdHocCars = 0;
	private float monthlyIncomeParkingPassCar = 0;
	private float monthlyIncomeReservationCar = 0;
	private float monthlyMaintenance = 0;
	private float monthlyTaxes = 0;
	
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

	/**
	 * Adds money to the total monthly budget for every car leaving and paying
	 * @param car the car where the money should be collected from
	 */
	public void collectMoney(Car car) {
        int totalFramesStayed = (int) Math.ceil(car.getMinutesStayed() / paymentTimeframe);
        
		switch(car.getCarType()) {
			case AD_HOC:
				moneyDay += totalFramesStayed * costPerTimeFrame_adHocCar;
				monthlyIncomeAdHocCars += totalFramesStayed * costPerTimeFrame_adHocCar;
				break;
			case RESERVERATION_CAR:
				moneyDay += totalFramesStayed * costPerTimeFrame_ReservationCar;
				monthlyIncomeReservationCar += totalFramesStayed * costPerTimeFrame_ReservationCar;
				break;
				
			default:
				break;
		}
	}
	
	/**
	 * Adds money to the amount of money the garage misses out on
	 * @param stayMinutes The amount of minutes the car was supposed to stay
	 */
	public void addMoneyMissedOutOn(int stayMinutes) {
		int totalFramesStayed = (int) Math.ceil(stayMinutes / paymentTimeframe);
		moneyMissedOutOn += totalFramesStayed * costPerTimeFrame_adHocCar;
		carsLeftQueue++;
	}
	
	/*
	 * Checks if we have to pay or make reports yet
	 */
	private void manageMoney() {
		//End of day
		if(timeModel.getHour() == 0 && timeModel.getMinute() == 0) {
			moneyMonth += moneyDay;
			moneyDay = 0;
		}
		//End of month
		if(timeModel.getIsFirstDayOfMonth() && timeModel.getHour() == 8 && timeModel.getMinute() == 30) {
			addPassHolderMoney();
			payMoney();
			
			moneyLastMonth = moneyMonth;
			moneyTotal += moneyMonth;
			
			//Reset our values
			moneyMonth = 0;
			moneyMissedOutOn = 0;
			carsLeftQueue = 0;
		}
	}
	
	/**
	 * Adds money to the total monthly budget by letting pass holders pay
	 */
	private void addPassHolderMoney() {
		//Let pass holders pay
		moneyMonth += passHolderPlaceAmount * cost_passHolderCar;
		monthlyIncomeParkingPassCar += passHolderPlaceAmount * cost_passHolderCar;
	}
	
	/**
	 * Initiated the taxes and maintenance
	 */
	private void payMoney() {
		//Pay taxes
		moneyMonth = payMoney_taxes(moneyMonth);
		//Pay maintenance
		moneyMonth = payMoney_maintenance(moneyMonth);
	}
	/**
	 * Pays for taxes
	 * @param amount The amount we have before taxes has been reducted
	 * @return Returns the amount we have after taxes has been reducted
	 */
	private float payMoney_taxes(float amount) {
		float costs = amount;
		
		//We pay taxes at 8.30 in the morning
		if(amount > 0 && amount <= 20384)
			costs *= (1f - 0.3665f);
		if(amount > 20384 && amount <= 68507)
			costs *= (1f - 0.3810f);
		if(amount > 68507)
			costs *= (1f - 0.5175f);
		
		monthlyTaxes = costs - amount;
		
		amount = costs;
		System.out.println("taxes: " + amount);
		return amount;
	}
	/**
	 * Pays money for maintenance
	 * @param amount The amount we have before maintenance is being paid for
	 * @return Returns the amount we have after maintenance is being paid for
	 */
	private float payMoney_maintenance(float amount) {
		float costs = 0;
		
		//Pay maintenance costs
		costs -= numberOfEntrances * entranceCosts;
		costs -= numberOfTicketMachines * ticketMachineCosts;
		costs -= numberOfExits * exitCosts;
		
		costs -= numberOfPlaces * placeCosts;
		costs -= numberOfRows * rowCosts;
		costs -= numberOfFloors * floorCosts;
		
		costs -= maintenanceCosts;
		
		monthlyMaintenance = costs;
		
		amount += costs;
		System.out.println("maintenance: " + amount);
		return amount;
	}
	
	/**
	 * @return Returns an array of monthly revenue and costs
	 */
	public float[] getMonthlyReport() {
		float[] report = new float[] { monthlyIncomeAdHocCars, monthlyIncomeParkingPassCar, monthlyIncomeReservationCar, monthlyMaintenance, monthlyTaxes };
		
		monthlyIncomeAdHocCars = 0;
		monthlyIncomeParkingPassCar = 0;
		monthlyIncomeReservationCar = 0;
		monthlyMaintenance = 0;
		monthlyTaxes = 0;
		
		return report;
	}
	
	/**
	 * @return Gets the money the garage has in total
	 */
	public float getMoneyTotal() { return moneyTotal; }
	/**
	 * @return Gets the money the garage has earned this month
	 */
	public float getMoneyMonth() { return moneyMonth; }
	/**
	 * @return Gets the money the garage has earned this day
	 */
	public float getMoneyDay() { return moneyDay; }
	
	/**
	 * Adds money to the total budget
	 * @param amount the amount of money that should be added to the budget
	 */
	public void addMoney(float amount) { moneyTotal += amount; }
	/**
	 * Removes money to the total budget
	 * @param amount the amount of money that should be removed from the budget.
	 */
	public void retractMoney(float amount) { moneyTotal -= amount; }
	
	/**
	 * @return Gets the time when a car has to pay more money for their stay
	 */
	public int getPaymentTimeframe() { return paymentTimeframe; }
	/**
	 * Sets the time when a car has to pay more money for their stay
	 * @param amount the amount of money that should be asked for a certain timeframe
	 */
	public void setPaymentTimeframe(int amount) { paymentTimeframe = amount; }
	
	/**
	 * @return Gets the amount an adHoc car has to pay for staying in the garage every timeFrame
	 */
	public float getCostPerTimeFrame_adHocCar() { return costPerTimeFrame_adHocCar; }
	/**
	 * Sets the amount an adHoc car has to pay for staying in the garage every timeFrame
	 * @param amount the amount of money an adHocCar has to pay for staying in the garage every timeFrame
	 */
	public void setCostPerTimeFrame_adHocCar(float amount) { costPerTimeFrame_adHocCar = amount; }
	
	/**
	 * @return Gets the amount a reservation car has to pay for staying in the garage every timeFrame
	 */
	public float getCostPerTimeFrame_reservationCar() { return costPerTimeFrame_ReservationCar; }
	/**
	 * Sets the amount a reservation car has to pay for staying in the garage every timeFrame
	 * @param amount the amount a ReservationCar has to pay for staying in the garage every timeFrame.
	 */
	public void setCostPerTimeFrame_reservationCar(float amount) { costPerTimeFrame_ReservationCar = amount; }

	/**
	 * @return Gets the amount a pass holder car has to pay every month
	 */
	public float getCostPerMonth_passHolderCar() { return cost_passHolderCar; }
	/**
	 * Sets the amount a pass holder car has to pay every month
	 * @param amount the amount a pass holder car has to pay every month.
	 */
	public void setCostPerMonth_passHolderCar(float amount) { cost_passHolderCar = amount; }
	
	/**
	 * @return Gets the amount the garage has to pay for maintenance
	 */
	public int getMaintenanceCosts() { return maintenanceCosts; }
	/**
	 * Sets the amount the garage has to pay for maintenance
	 * @param amount the amount of default maintenance cost.
	 */
	public void setMaintenanceCosts(int amount) { maintenanceCosts = amount; }
	
	/**
	 * @return Gets the number of entrances to the carpark
	 */
	public int getNumberOfEntrances() { return numberOfEntrances; }
	/**
	 * Sets the number of entrances to the carpark (this number is not dependent on the actual amount of entrances)
	 * @param amount sets the number of entrances in this model
	 */
	public void setNumberOfEntrances(int amount) { numberOfEntrances = amount; }

	/**
	 * @return Gets the number of ticket machines in the carpark 
	 */
	public int getNumberOfTicketMachines() { return numberOfTicketMachines; }
	/**
	 * Sets the number of ticket machines in the carpark  (this number is not dependent on the actual amount of ticketmachines)
	 * @param amount Sets the number of ticket machines in the carpark
	 */
	public void setNumberOfTicketMachines(int amount) { numberOfTicketMachines = amount; }

	/**
	 * @return Gets the number of exits from the carpark
	 */
	public int getNumberOfExits() { return numberOfExits; }
	/**
	 * Sets the number of exits from the carpark (this number is not dependent on the actual amount of exits)
	 * @param amount the number of exits in the carpark
	 */
	public void setNumberOfExits(int amount) { numberOfExits = amount; }

	/**
	 * @return Gets the amount of costs for an entrance in the carpark
	 */
	public int getEntranceCosts() { return entranceCosts; }
	/**
	 * Sets the amount of costs for an entrance in the carpark
	 * @param amount the amount of costs for an entrance in the carpark
	 */
	public void setEntranceCosts(int amount) { entranceCosts = amount; }

	/**
	 * @return Gets the amount of costs for a ticket machine in the carpark
	 */
	public int getTicketMachineCosts() { return ticketMachineCosts; }
	/**
	 * Sets the amount of costs for a ticket machine in the carpark
	 * @param amount the amount of costs for a ticket machine in the carpark
	 */
	public void setTicketMachineCosts(int amount) { ticketMachineCosts = amount; }

	/**
	 * @return Gets the amount of costs for an exit machine in the carpark
	 */
	public int getExitCosts() { return exitCosts; }
	/**
	 * Sets the amount of costs for an exit machine in the carpark
	 * @param amount the amount of costs for an exit machine in the carpark
	 */
	public void setExitCosts(int amount) { exitCosts = amount; }
	
	/**
	 * @return Gets the amount places in the carpark
	 */
	public int getNumberOfPlaces() { return numberOfPlaces; }
	/**
	 * Sets the amount places in the carpark (this number is not dependent on the actual amount of places in the ParkingGarageModel)
	 * @param amount the amount places in the carpark 
	 */
	public void setNumberOfPlaces(int amount) { numberOfPlaces = amount; }
	
	/**
	 * @return Gets the amount rows in the carpark
	 */
	public int getNumberOfRows() { return numberOfRows; }
	/**
	 * Sets the amount rows in the carpark (this number is not dependent on the actual amount of rows in the ParkingGarageModel)
	 * @param amount the amount rows in the carpark 
	 */
	public void setNumberOfRows(int amount) { numberOfRows = amount; }

	/**
	 * @return Gets the amount floors in the carpark
	 */
	public int getNumberOfFloors() { return numberOfFloors; }
	/**
	 * Sets the amount floors in the carpark (this number is not dependent on the actual amount of rows in the ParkingGarageModel)
	 * @param amount the amount floors in the carpark
	 */
	public void setNumberOfFloors(int amount) { numberOfFloors = amount; }
	
	/**
	 * Sets the amount places, rows and floors all at once in the carpark
	 * @param num the amount places [0], rows [1] and floors [2] all at once in the carpark
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
	 * @param amount the amount a place costs for maintenance
	 */
	public void setPlaceCosts(float amount) { placeCosts = amount; }

	/**
	 * @return Gets the amount a row costs for maintenance
	 */
	public int getRowCosts() { return rowCosts; }
	/**
	 * Sets the amount a row costs for maintenance
	 * @param amount the amount a row costs for maintenance
	 */
	public void setRowCosts(int amount) { rowCosts = amount; }
	
	/**
	 * @return Gets the amount a floor costs for maintenance
	 */
	public int getFloorCosts() { return floorCosts; }
	/**
	 * Sets the amount a floor costs for maintenance
	 * @param amount the amount a floor costs for maintenance
	 */
	public void setFloorCosts(int amount) { floorCosts = amount; }
	
	/**
	 * @return Gets the amount of passHolder spots the parking garage has
	 */
	public int getPassHolderPlaceAmount() { return passHolderPlaceAmount; }
	/**
	 * Sets the amount of passHolder spots the parking garage has
	 * @param amount the amount of passHolder spots the parking garage has
	 */
	public void setPassHolderPlaceAmount(int amount) { passHolderPlaceAmount = amount; }
	
	/**
	 * @return Gets the amount of cars the parking garage misses out on on a monthly basis
	 */
	public int getCarsLeftQueue() { return carsLeftQueue; }
	/**
	 * Sets the amount of cars the parking garage misses out on on a monthly basis
	 * @param amount the amount of cars the parking garage misses out on on a monthly basis
	 */
	public void setCarsLeftQueue(int amount) { carsLeftQueue = amount; }
	/**
	 * Adds to the amount of cars the parking garage misses out on on a monthly basis
	 * @param amount the amount of cars the parking garage misses out on on a monthly basis
	 */
	public void addCarsLeftQueue(int amount) { carsLeftQueue += amount; }
	
	/**
	 * @return Gets the total amount of money the garage has missed out on in the current month
	 */
	public float getMoneyMissedOutOn() { return moneyMissedOutOn; }

	/**
	 * @return Gets the money the parkingGarage has earned last month for later use
	 */
	public float getMoneyLastMonth() { return moneyLastMonth; }
}
