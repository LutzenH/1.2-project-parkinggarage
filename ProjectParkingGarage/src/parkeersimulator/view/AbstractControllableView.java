package parkeersimulator.view;

import parkeersimulator.controller.AbstractController;
import parkeersimulator.model.AbstractModel;

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
	AbstractController controller;
	
	/**
	 * Constructor of the AbstractControllableView.
	 * @param model the model of the parking garage.
	 * @param controller the controller of the parking garage.
	 */
	public AbstractControllableView(AbstractModel model, AbstractController controller) {
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
