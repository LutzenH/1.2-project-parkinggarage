package parkeersimulator.main;

import javax.swing.JPanel;

import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.FinanceModel;
import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.model.handler.ModelHandler;
import parkeersimulator.view.CarParkStepControlView;
import parkeersimulator.view.CarParkView;
import parkeersimulator.view.frame.MainFrame;
import parkeersimulator.view.frame.GarageDesignFrame;
import parkeersimulator.view.graph.CarCountGraphView;
import parkeersimulator.view.graph.QueueGraphView;
import parkeersimulator.view.graph.FinanceGraphView;

/**
 * The Entry point for launching the program.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 *
 */
public class Main {
	
	///Declaration of the main handler that initiates the models this program should use.
	private ModelHandler modelHandler;
	private ParkingGarageModel parkingGarageModel;
	private FinanceModel financeModel;
	
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
		//Instantiation of this programs' models
		modelHandler = new ModelHandler();
		parkingGarageModel = new ParkingGarageModel(modelHandler);
		financeModel = new FinanceModel(modelHandler, parkingGarageModel);
		
		///Instantiation of this programs' controllers.
		controller = new ParkingGarageController(modelHandler, parkingGarageModel);
		
		///Instantiation of this programs' views.
		carparkview = new GarageDesignFrame(parkingGarageModel, controller);
		carparkstepcontrolview = new CarParkStepControlView(parkingGarageModel, controller);
		
		tabbedviews = new JPanel[3];
		tabbedviews[0] = new QueueGraphView(parkingGarageModel);
		tabbedviews[0].setName("Cars in queue");
		tabbedviews[1] = new CarCountGraphView(parkingGarageModel);
		tabbedviews[1].setName("Car Count");
		tabbedviews[2] = new FinanceGraphView(parkingGarageModel, financeModel);
		tabbedviews[2].setName("Finance");
		
		///Layout and instantiation of the JFrame.
		screen = new MainFrame("Parking Garage Simulator", carparkstepcontrolview, tabbedviews, carparkview);
		
		///Makes this JFrame visible.
		screen.setVisible(true);
		
		///the views will be notified before the simulation runs in order to display an empty CarParkView.
		parkingGarageModel.notifyViews();
	}
	
	/**
	 * The Entry-point for this program to start running.
	 */
	public static void main(String[] args) {
		Main main = new Main();
	}

}
