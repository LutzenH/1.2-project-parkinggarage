package parkeersimulator.controller;

import java.util.HashMap;

import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.model.handler.ModelHandler;
/**
 * The controller used in the simulation of the parking garage.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 */
public class ParkingGarageController extends AbstractController {
	ModelHandler modelHandler; //TODO: LUTZEN NOT FINT NETJUS
	ParkingGarageModel parkingGarageModel;
	
	///The type of event that should be performed when a certain button or field has been triggered. 
	public enum ActionType {
		EVENT_TICKPAUSEAMOUNT, EVENT_TICKAMOUNT, 
		EVENT_ADHOCWEEK_AMOUNT, EVENT_ADHOCWEEKEND_AMOUNT, EVENT_ADHOCEVENT_AMOUNT,
		EVENT_PASSWEEK_AMOUNT, EVENT_PASSWEEKEND_AMOUNT, EVENT_PASSEVENTWEEK_AMOUNT,
		EVENT_TICKSTART, EVENT_TICKSTOP,
		EVENT_CLICK_PROP,
		EVENT_OPEN_GARAGE
		};
	
	/**
	 * The constructor of ParkingGarageController
	 * 
	 * @param model The model this controller should be controlling.
	 */
	public ParkingGarageController(ModelHandler modelHandler, ParkingGarageModel parkingGarageModel) {
		super(parkingGarageModel);
		this.modelHandler = modelHandler;
		this.parkingGarageModel = parkingGarageModel;
	}
	
	/**
	 * The event that should be performed when a certain button or field has been triggered.
	 * 
	 * @param actionType The type of event that should be performed when a certain button or field has been triggered. 
	 * @param data A HashMap of data that could be needed by a certain type of event.
	 * @return returns false if the right action could not be found.
	 */
	public boolean performAction(ActionType actionType, HashMap<String, Object> data) {
		switch(actionType)
		{
			case EVENT_TICKPAUSEAMOUNT:
				modelHandler.setTickPause((Integer)data.get("amount"));
				return true;
			case EVENT_TICKAMOUNT:
				modelHandler.tick((Integer)data.get("amount"));
				return true;
				
			case EVENT_ADHOCWEEK_AMOUNT:
				parkingGarageModel.setAdHocArrivals_week((Integer)data.get("amount"));
				return true;
			case EVENT_ADHOCWEEKEND_AMOUNT:
				parkingGarageModel.setAdHocArrivals_weekend((Integer)data.get("amount"));
				return true;
			case EVENT_ADHOCEVENT_AMOUNT:
				parkingGarageModel.setAdHocArrivals_event((Integer)data.get("amount"));
				return true;
				
			case EVENT_PASSWEEK_AMOUNT:
				parkingGarageModel.setPassArrivals_week((Integer)data.get("amount"));
				return true;
			case EVENT_PASSWEEKEND_AMOUNT:
				parkingGarageModel.setPassArrivals_weekend((Integer)data.get("amount"));
				return true;
			case EVENT_PASSEVENTWEEK_AMOUNT:
				parkingGarageModel.setPassArrivals_eventWeek((Integer)data.get("amount"));
				return true;
				
			case EVENT_TICKSTART:
				modelHandler.start();
				return true;
			case EVENT_TICKSTOP:
				modelHandler.stop();
				return true;
				
			case EVENT_CLICK_PROP:
				parkingGarageModel.setProp((Integer)data.get("index"));
				return true;
		
			case EVENT_OPEN_GARAGE:
				parkingGarageModel.openGarage();
				return true;
			
			default:
				break;
		}
		return false;
	}

	/**
	 * The event that should be performed when a certain button or field has been triggered.
	 * 
	 * @param actionType The type of event that should be performed when a certain button or field has been triggered. 
	 * @return returns false if the right action could not be found.
	 */
	public boolean performAction(ActionType actionType)
	{
		return performAction(actionType, null);
	}
}
