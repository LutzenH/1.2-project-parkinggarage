package parkeersimulator.controller;

import java.util.HashMap;

import parkeersimulator.model.AbstractModel.ModelType;
import parkeersimulator.handler.ModelHandler;
import parkeersimulator.model.FinanceModel;
import parkeersimulator.model.ParkingGarageModel;
/**
 * The controller used in the simulation of the parking garage.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 */
public class ParkingGarageController extends AbstractController {
	ParkingGarageModel parkingGarageModel;

	/**
	 * The constructor of ParkingGarageController
	 * 
	 * @param model The model this controller should be controlling.
	 */
	public ParkingGarageController(ParkingGarageModel parkingGarageModel) {
		super(parkingGarageModel);
		this.parkingGarageModel = parkingGarageModel;
	}
	
	@Override
	public boolean performAction(ActionType actionType, HashMap<String, Object> data) {
		switch(actionType)
		{	
			case PARKINGGARAGE_BASELINE_AMOUNT:
				parkingGarageModel.setBaseLineArrivals((Integer)data.get("amount"));
				return true;
			
			case PARKINGGARAGE_ADHOCWEEK_AMOUNT:
				parkingGarageModel.setAdHocArrivals_week((Integer)data.get("amount"));
				return true;
			case PARKINGGARAGE_ADHOCWEEKEND_AMOUNT:
				parkingGarageModel.setAdHocArrivals_weekend((Integer)data.get("amount"));
				return true;
			case PARKINGGARAGE_ADHOCEVENT_AMOUNT:
				parkingGarageModel.setAdHocArrivals_event((Integer)data.get("amount"));
				return true;
				
			case PARKINGGARAGE_PASSWEEK_AMOUNT:
				parkingGarageModel.setPassArrivals_week((Integer)data.get("amount"));
				return true;
			case PARKINGGARAGE_PASSWEEKEND_AMOUNT:
				parkingGarageModel.setPassArrivals_weekend((Integer)data.get("amount"));
				return true;
			case PARKINGGARAGE_PASSEVENTWEEK_AMOUNT:
				parkingGarageModel.setPassArrivals_eventWeek((Integer)data.get("amount"));
				return true;
				
			case PARKINGGARAGE_RESERVATIONWEEK_AMOUNT:
				parkingGarageModel.setReservationArrivals_week((Integer)data.get("amount"));
				return true;
			case PARKINGGARAGE_RESERVATIONWEEKEND_AMOUNT:
				parkingGarageModel.setReservationArrivals_weekend((Integer)data.get("amount"));
				return true;
			case PARKINGGARAGE_RESERVATIONEVENT_AMOUNT:
				parkingGarageModel.setReservationArrivals_event((Integer)data.get("amount"));
				return true;
				
			case PARKINGGARAGE_CLICK_PROP:
				parkingGarageModel.setProp((Integer)data.get("index"));
				return true;
		
			case PARKINGGARAGE_OPEN_GARAGE:
				parkingGarageModel.openGarage();
				return true;
				
			default:
				break;
		}
		return false;
	}

	@Override
	public ControllerType getControllerType() {
		return ControllerType.PARKINGGARAGE;
	}
}
