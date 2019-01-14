package parkeersimulator.view;

import parkeersimulator.controller.AbstractController;
import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.ParkingGarageModel;

public abstract class AbstractControllableView extends AbstractView{
	ParkingGarageController controller;
	
	public AbstractControllableView(ParkingGarageModel model, ParkingGarageController controller) {
		super(model);
		this.controller = controller;
	}

	public AbstractController getController() {
		return controller;
	}
}
