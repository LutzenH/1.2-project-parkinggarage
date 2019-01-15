package parkeersimulator.main;

import java.awt.FlowLayout;

import javax.swing.JFrame;

import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.view.CarParkStepControlView;
import parkeersimulator.view.CarParkView;
import parkeersimulator.view.GraphView;
import parkeersimulator.view.QueueCountView;
import parkeersimulator.view.TimeView;

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
	private GraphView graphview;

	private JFrame screen;
	
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
		graphview = new GraphView(model);
		
		///Layout and instantiation of the JFrame.
		screen = new JFrame("Parking Garage Simulator");
		screen.setLayout(new FlowLayout());
		
		screen.getContentPane().add(queuecountview);
		screen.getContentPane().add(timeview);
		screen.getContentPane().add(carparkview);
		screen.getContentPane().add(carparkstepcontrolview);
		screen.getContentPane().add(graphview);
		
		screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		screen.pack();
		screen.setSize(800, 800);
		
		///the views will be notified before the simulation runs in order to display an empty CarParkView.
		model.notifyViews();
		
		///Makes this JFrame visible.
		screen.setVisible(true);
	}
	
	/**
	 * The Entry-point for this program to start running.
	 */
	public static void main(String[] args) {
		Main main = new Main();
	}

}
