package parkeersimulator.model;

import parkeersimulator.handler.ModelHandler;
import parkeersimulator.model.car.AdHocCar;
import parkeersimulator.model.car.Car;
import parkeersimulator.model.queue.CarQueue;

public class FinanceModel extends AbstractModel {

	private TimeModel timeModel;
	private ParkingGarageModel parkingGarageModel;
	
	//Total revenue of the garage
	private float moneyTotal = 0f;
	//Total revenue of the past day
	private float moneyMonth = 0f;
	//The amount of time  a car stays before having to pay another costPerTimeFrame
	private int paymentTimeframe = 20;
	//The cost for staying in the car park every time frame
	private float costPerTimeFrame = 1f;
	//The cost for salary and upkeep
	private int maintenanceCosts = 40000;
	
	public FinanceModel(ModelHandler handler) {
		super(handler);
		
		this.timeModel = (TimeModel) handler.getModel(ModelType.TIME);
	}
	
	@Override
	public ModelType getModelType() {
		return ModelType.FINANCE;
	}

	public void tick() {
		payMoney();
	}

	public void collectMoney(Car car) {
		switch(car.getCarType()) {
			case AD_HOC:
				int minutesStayed = car.getMinutesStayed();
	            int totalFramesStayed = (int) Math.ceil(car.getMinutesStayed() / paymentTimeframe);
	            moneyMonth += totalFramesStayed * costPerTimeFrame;
				break;
				
			case PASS:
				break;
				
			case RESERVERATION_CAR:
				break;
				
			default:
				break;
		}
	}
	
	private void payMoney() {
		if(timeModel.getIsFirstDayOfMonth() && timeModel.getHour() == 8 && timeModel.getMinute() == 30) {
			moneyMonth = payMoney_taxes(moneyMonth);
			moneyMonth = payMoney_maintenance(moneyMonth);
			
			moneyTotal += moneyMonth;
			moneyMonth = 0;
			
			//parkingGarageModel = (ParkingGarageModel) getModelHandler().getModel(ModelType.PARKINGGARAGE);
			//System.out.println(parkingGarageModel.getBaseLineArrivals());
		}
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
	 * @return Gets the amount a car has to pay for staying in the garage every timeFrame
	 */
	public float getCostPerTimeFrame() { return costPerTimeFrame; }
	/**
	 * Sets the amount a car has to pay for staying in the garage every timeFrame
	 */
	public void setCostPerTimeFrame(float amount) { costPerTimeFrame = amount; }
	
	/**
	 * @return Gets the amount the garage has to pay for maintenance
	 */
	public int getMaintenanceCosts() { return maintenanceCosts; }
	/**
	 * Sets the amount the garage has to pay for maintenance
	 */
	public void setMaintenanceCosts(int amount) { maintenanceCosts = amount; }
}
