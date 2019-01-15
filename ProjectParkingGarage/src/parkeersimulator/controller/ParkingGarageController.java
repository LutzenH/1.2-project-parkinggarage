package parkeersimulator.controller;

import java.util.HashMap;

import parkeersimulator.model.ParkingGarageModel;
/**
 * The controller used in the simulation of the parking garage.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 */
public class ParkingGarageController extends AbstractController {
	
	///The type of event that should be performed when a certain button or field has been triggered. 
	public enum ActionType {EVENT_TICKPAUSEAMOUNT, EVENT_TICKAMOUNT, EVENT_TICKSTART, EVENT_TICKSTOP};
	
	/**
	 * The constructor of ParkingGarageController
	 * 
	 * @param model The model this controller should be controlling.
	 */
	public ParkingGarageController(ParkingGarageModel model) {
		super(model);
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
				model.setTickPause((Integer)data.get("amount"));
				return true;
			case EVENT_TICKAMOUNT:
				model.tick((Integer)data.get("amount"));
				return true;
			case EVENT_TICKSTART:
				model.start();
				return true;
			case EVENT_TICKSTOP:
				model.stop();
				return true;
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
