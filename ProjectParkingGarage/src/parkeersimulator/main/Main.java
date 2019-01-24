package parkeersimulator.main;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.view.CarParkStepControlView;
import parkeersimulator.view.CarParkView;
import parkeersimulator.view.QueueCountView;
import parkeersimulator.view.TimeView;
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
	private TimeView timeview;
	private CarParkView carparkview;
	private QueueCountView queuecountview;
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
		carparkview = new CarParkView(model);
		queuecountview = new QueueCountView(model);
		timeview = new TimeView(model);
		carparkstepcontrolview = new CarParkStepControlView(model, controller);
		
		tabbedviews = new JPanel[2];
		tabbedviews[0] = new QueueGraphView(model);
		tabbedviews[0].setName("Cars in queue");
		tabbedviews[1] = new CarCountGraphView(model);
		tabbedviews[1].setName("Car Count");
		
		///Layout and instantiation of the JFrame.
		screen = new MainFrame("Parking Garage Simulator", carparkstepcontrolview, tabbedviews, carparkview);

		
		//screen.getContentPane().add(queuecountview);
		//screen.getContentPane().add(timeview);
		
		///the views will be notified before the simulation runs in order to display an empty CarParkView.
		model.notifyViews();
		
		///Makes this JFrame visible.
		screen.setVisible(true);
		
		//screen.addResizeProperty(controller);
		
		GarageDesignFrame temp = new GarageDesignFrame(model, controller);
		temp.setVisible(true);
		temp.setSize(new Dimension(732, 410));
	}
	
	/**
	 * The Entry-point for this program to start running.
	 */
	public static void main(String[] args) {
		Main main = new Main();
	}

}
