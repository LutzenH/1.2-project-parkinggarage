package parkeersimulator.controller;

import parkeersimulator.model.AbstractModel;
import parkeersimulator.model.ParkingGarageModel;

/**
 * Abstract class of the controller.
 */
public abstract class AbstractController {
	///Declaration of the model this controller should be controlling.
	protected AbstractModel model;
	
	/**
	 * The constructor of AbstractController
	 * 
	 * @param model The model this controller should be controlling.
	 */
	public AbstractController(ParkingGarageModel model) {
		this.model=model;
	}
}
