package parkeersimulator.main;

import java.awt.FlowLayout;

import javax.swing.JFrame;

import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.view.CarParkStepControlView;
import parkeersimulator.view.CarParkView;
import parkeersimulator.view.QueueCountView;
import parkeersimulator.view.TimeView;

public class Main {
	private ParkingGarageModel model;
	
	private ParkingGarageController controller;
	private TimeView timeview;
	private CarParkView carparkview;
	private QueueCountView queuecountview;
	private CarParkStepControlView carparkstepcontrolview;

	private JFrame screen;
	
	public Main() {
		model = new ParkingGarageModel();
		controller = new ParkingGarageController(model);
		
		carparkview = new CarParkView(model);
		queuecountview = new QueueCountView(model);
		timeview = new TimeView(model);
		carparkstepcontrolview = new CarParkStepControlView(model, controller);
		
		screen = new JFrame("Parking Garage Simulator");
		screen.setLayout(new FlowLayout());
				
		screen.getContentPane().add(queuecountview);
		screen.getContentPane().add(timeview);
		screen.getContentPane().add(carparkview);
		screen.getContentPane().add(carparkstepcontrolview);
		
		screen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		screen.pack();
		screen.setSize(800, 800);
		
		model.notifyViews();
		
		screen.setVisible(true);
	}
	
	public static void main(String[] args) {
		Main main = new Main();
	}

}
