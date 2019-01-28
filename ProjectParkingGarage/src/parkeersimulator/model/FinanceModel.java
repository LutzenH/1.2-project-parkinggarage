package parkeersimulator.model;

import parkeersimulator.model.car.Car;
import parkeersimulator.model.handler.ModelHandler;
import parkeersimulator.model.queue.CarQueue;

public class FinanceModel extends AbstractModel {

	private ParkingGarageModel parkingGarageModel;
	
	//Total revenue of the garage
	private float moneyTotal = 0f;
	//Total revenue of the past day
	private float moneyMonth = 0f;
	//The amount of time  a car stays before having to pay another costPerTimeFrame
	private int timeFrame = 20;
	//The cost for staying in the car park every time frame
	private float costPerTimeFrame = 1f;
	
	private float maintenanceCosts = 40000;
	
	
	public FinanceModel(ModelHandler handler, ParkingGarageModel parkingGarageModel) {
		super(handler);
		
		this.parkingGarageModel = parkingGarageModel;
	}
	

	public void tick() {
		collectMoney();
		payMoney();
	}

	private void collectMoney() {
		CarQueue paymentCarQueue = parkingGarageModel.getPaymentCarQueue_entireTick();

		int i=0;
    	while (paymentCarQueue.carsInQueue() > 0 && i < parkingGarageModel.getPaymentSpeed()){
            Car car = paymentCarQueue.removeCar();
            
            int minutesStayed = car.getMinutesStayed();

            int totalFramesStayed = (int) Math.ceil(car.getMinutesStayed() / timeFrame);
            
            moneyMonth += totalFramesStayed * costPerTimeFrame;
            
            i++;
    	}
	}
	
	private void payMoney() {
		if(parkingGarageModel.getIsFirstDayOfMonth() && parkingGarageModel.getHour() == 8 && parkingGarageModel.getMinute() == 30) {
			moneyMonth = payMoney_taxes(moneyMonth);
			moneyMonth = payMoney_maintenance(moneyMonth);
			
			moneyTotal += moneyMonth;
			moneyMonth = 0;
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
	
	
	public float getMoneyTotal() { return moneyTotal; }
	
	public float getMoneyMonth() { return moneyMonth; }
	
	public void addMoney(float amount) { moneyTotal += amount; }
	
	public void retractMoney(float amount) { moneyTotal -= amount; }
}
