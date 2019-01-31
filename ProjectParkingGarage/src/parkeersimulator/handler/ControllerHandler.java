package parkeersimulator.handler;

import java.util.ArrayList;

import parkeersimulator.controller.AbstractController;
import parkeersimulator.controller.AbstractController.ControllerType;

public class ControllerHandler{
private ArrayList<AbstractController> controllerList;

	
	public ControllerHandler() {
		controllerList = new ArrayList<>();
	}

	public void addController(AbstractController controller) {
		controllerList.add(controller);
	}

    public AbstractController getController(ControllerType type) {
    	for(AbstractController controller : controllerList) {
    		if(controller.getControllerType() == type)
    			return controller;
    	}
    	
    	return null;
    }
}
