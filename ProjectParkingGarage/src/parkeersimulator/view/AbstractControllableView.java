package parkeersimulator.view;

import parkeersimulator.controller.AbstractController;
import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.ParkingGarageModel;

/**
 * Abstract class of a controllable view.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 * 
 */
public abstract class AbstractControllableView extends AbstractView{
	///Declaration of the ParkingGarageController this view should be sending instructions/information to.
	ParkingGarageController controller;
	
	/**
	 * Constructor of the AbstractControllableView.
	 * @param model the model of the parking garage.
	 * @param controller the controller of the parking garage.
	 */
	public AbstractControllableView(ParkingGarageModel model, ParkingGarageController controller) {
		super(model);
		this.controller = controller;
	}

	/**
	 * @return The controller this view sends instructions/information to.
	 */
	public AbstractController getController() {
		return controller;
	}
}
