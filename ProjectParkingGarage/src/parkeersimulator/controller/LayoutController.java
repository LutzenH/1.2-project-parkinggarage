package parkeersimulator.controller;

import java.util.HashMap;

import parkeersimulator.model.LayoutModel;

public class LayoutController extends AbstractController {
	LayoutModel layoutModel;
	
	public LayoutController(LayoutModel layoutModel) {
		super(layoutModel);
		this.layoutModel = layoutModel;
	}

	@Override
	public boolean performAction(ActionType action, HashMap<String, Object> data) {
		switch(action) {
		case LAYOUT_ADD_FLOOR:
			layoutModel.setNumberOfFloors((Integer)data.get("amount"));
			return true;
		case LAYOUT_ADD_ROW:
			layoutModel.setNumberOfRows((Integer)data.get("amount"));
			return true;
		case LAYOUT_ADD_PLACE:
			layoutModel.setNumberOfPlaces((Integer)data.get("amount"));
			return true;
	}
	return false;
	}

	@Override
	public ControllerType getControllerType() {
		return ControllerType.LAYOUT;
	}

}
