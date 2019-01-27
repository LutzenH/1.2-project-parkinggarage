package parkeersimulator.main;

import javax.swing.JPanel;

import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.view.CarParkStepControlView;
import parkeersimulator.view.frame.MainFrame;
import parkeersimulator.view.frame.GarageDesignFrame;
import parkeersimulator.view.graph.CarCountGraphView;
import parkeersimulator.view.graph.QueueGraphView;

/**
 * The Entry point for launching the program.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 *
 */
public class Main {
	
	///Declaration of the model this program should use.
	private ParkingGarageModel model;
	
	///Declaration of the controllers this program uses.
	private ParkingGarageController controller;
	
	///Declaration of the views this program uses.
	private GarageDesignFrame carparkview;
	private CarParkStepControlView carparkstepcontrolview;
	private JPanel[] tabbedviews;

	private MainFrame screen;
	
	/**
	 * Constructor of the class Main.
	 */
	public Main() {
		///Instantiation of the ParkingGarageModel.
		model = new ParkingGarageModel();
		
		///Instantiation of this programs' controllers.
		controller = new ParkingGarageController(model);
		
		///Instantiation of this programs' views.
		carparkview = new GarageDesignFrame(model, controller);
		carparkstepcontrolview = new CarParkStepControlView(model, controller);
		
		tabbedviews = new JPanel[2];
		tabbedviews[0] = new QueueGraphView(model);
		tabbedviews[0].setName("Cars in queue");
		tabbedviews[1] = new CarCountGraphView(model);
		tabbedviews[1].setName("Car Count");
		
		///Layout and instantiation of the JFrame.
		screen = new MainFrame("Parking Garage Simulator", carparkstepcontrolview, tabbedviews, carparkview);
		
		///Makes this JFrame visible.
		screen.setVisible(true);
		
		///the views will be notified before the simulation runs in order to display an empty CarParkView.
		model.notifyViews();
	}
	
	/**
	 * The Entry-point for this program to start running.
	 */
	public static void main(String[] args) {
		Main main = new Main();
	}

}
