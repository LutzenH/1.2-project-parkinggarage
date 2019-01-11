package parkeersimulator.controller;

import javax.swing.JPanel;

import parkeersimulator.model.AbstractModel;
import parkeersimulator.model.ParkingGarageModel;

public abstract class AbstractController extends JPanel {
	private static final long serialVersionUID = 4941730006940737729L;
	protected ParkingGarageModel model;
	
	public AbstractController(ParkingGarageModel model) {
		this.model=model;
	}
}
