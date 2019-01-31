package parkeersimulator.view;

import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.ParkingGarageModel;

/**
 * Class of a view that can start and stop the simulation.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 * 
 */
public class CarParkLayoutControlView extends AbstractControllableView{
	/**
	 * Constructor of CarParkLayoutControlView
	 * @param model The model this view should be getting information from.
	 * @param controller The controller this view should send instruction to.
	 */ 
	public CarParkLayoutControlView(ParkingGarageModel model, ParkingGarageController controller) {
		super(model, controller);
	}
}
