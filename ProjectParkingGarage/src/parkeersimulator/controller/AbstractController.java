package parkeersimulator.controller;

import parkeersimulator.model.ParkingGarageModel;

public abstract class AbstractController {
	protected ParkingGarageModel model;
	
	public AbstractController(ParkingGarageModel model) {
		this.model=model;
	}
}
