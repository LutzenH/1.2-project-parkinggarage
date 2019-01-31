package parkeersimulator.controller;

import java.util.HashMap;

import parkeersimulator.model.AbstractModel;

/**
 * Abstract class of the controller.
 */
public abstract class AbstractController {
	///Declaration of the model this controller should be controlling.
	protected AbstractModel model;
	
	///The type of event that should be performed when a certain button or field has been triggered. 
	public enum ActionType {
		TIME_TICKPAUSEAMOUNT, TIME_TICKAMOUNT,
		TIME_TICKSTART, TIME_TICKSTOP, 
		PARKINGGARAGE_BASELINE_AMOUNT,
		PARKINGGARAGE_ADHOCWEEK_AMOUNT, PARKINGGARAGE_ADHOCWEEKEND_AMOUNT, PARKINGGARAGE_ADHOCEVENT_AMOUNT,
		PARKINGGARAGE_PASSWEEK_AMOUNT, PARKINGGARAGE_PASSWEEKEND_AMOUNT, PARKINGGARAGE_PASSEVENTWEEK_AMOUNT,
		PARKINGGARAGE_RESERVATIONWEEK_AMOUNT, PARKINGGARAGE_RESERVATIONWEEKEND_AMOUNT, PARKINGGARAGE_RESERVATIONEVENT_AMOUNT,
		PARKINGGARAGE_CLICK_PROP,
		PARKINGGARAGE_OPEN_GARAGE,
		PARKINGGARAGE_SET_DRAWCHEAP,
		FINANCE_PAYMENTTIMEFRAME_AMOUNT, FINANCE_COSTPERTIMEFRAME_AMOUNT, FINANCE_MAINTENANCECOSTS_AMOUNT
		};
	
	/**
	 * The constructor of AbstractController
	 * 
	 * @param model The model this controller should be controlling.
	 */
	public AbstractController(AbstractModel model) {
		this.model=model;
	}
	
	/**
	 * The event that should be performed when a certain button or field has been triggered.
	 * 
	 * @param actionType The type of event that should be performed when a certain button or field has been triggered. 
	 * @param data A HashMap of data that could be needed by a certain type of event.
	 * @return returns false if the right action could not be found.
	 */
	public abstract boolean performAction(ActionType action, HashMap<String, Object> data);

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
	
	public enum ControllerType {PARKINGGARAGE, FINANCE, TIME}
	public abstract ControllerType getControllerType();
}
