package parkeersimulator.controller;

import java.util.HashMap;

import parkeersimulator.model.AbstractModel;
import parkeersimulator.model.TimeModel;

public class TimeController extends AbstractController{
	private TimeModel timeModel;
	
	public TimeController(TimeModel timeModel) {
		super(timeModel);
		this.timeModel = timeModel;
	}

	@Override
	public boolean performAction(ActionType action, HashMap<String, Object> data) {
		switch(action) {
			case TIME_TICKSTART:
				timeModel.start();
			return true;
			case TIME_TICKSTOP:
				timeModel.stop();
			return true;
			
			case TIME_TICKPAUSEAMOUNT:
				timeModel.setTickPause((Integer)data.get("amount"));
			return true;
			case TIME_TICKAMOUNT:
				timeModel.tick((Integer)data.get("amount"));
			return true;
		}
		
		return false;
	}

	@Override
	public ControllerType getControllerType() {
		return ControllerType.TIME;
	}

}
