package parkeersimulator.controller;

import java.util.HashMap;

import parkeersimulator.model.ParkingGarageModel;

public class ParkingGarageController extends AbstractController {
	public enum ActionType {EVENT_PLUSONE, EVENT_PLUSHUNDRED, EVENT_START, EVENT_STOP};
	
	public ParkingGarageController(ParkingGarageModel model) {
		super(model);
	}
	
	public boolean performAction(ActionType actionType, HashMap<String, Object> data) {
		switch(actionType)
		{
			case EVENT_PLUSONE:
				model.tick();
				return true;
			case EVENT_PLUSHUNDRED:
				model.tick(100);
				return true;
			case EVENT_START:
				model.start();
				return true;
			case EVENT_STOP:
				model.stop();
				return true;
		}
		return false;
	}

	public boolean performAction(ActionType actionType)
	{
		return performAction(actionType, null);
	}
}
