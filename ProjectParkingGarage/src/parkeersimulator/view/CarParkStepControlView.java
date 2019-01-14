package parkeersimulator.view;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.controller.ParkingGarageController.ActionType;
import parkeersimulator.model.ParkingGarageModel;

public class CarParkStepControlView extends AbstractControllableView{
	private JButton plusone;
	private JButton plushundred;
	private JButton start;
	private JButton stop;
	
	public CarParkStepControlView(ParkingGarageModel model, ParkingGarageController controller) {
		super(model, controller);
		
		setSize(450, 50);
		plusone=new JButton("+1");
		plusone.addActionListener(e -> controller.performAction(ActionType.EVENT_PLUSONE));
		
		plushundred=new JButton("+100");
		plushundred.addActionListener(e -> controller.performAction(ActionType.EVENT_PLUSHUNDRED));
		
		start=new JButton("Start");
		start.addActionListener(e -> controller.performAction(ActionType.EVENT_START));
		stop=new JButton("Stop");
		stop.addActionListener(e -> controller.performAction(ActionType.EVENT_STOP));
		
		this.setLayout(new FlowLayout());
		add(plusone);
		add(plushundred);
		add(start);
		add(stop);
		plusone.setBounds(140, 10, 70, 30);
		start.setBounds(229, 10, 70, 30);
		stop.setBounds(319, 10, 70, 30);
		
		this.setBorder(BorderFactory.createLineBorder(Color.black));

		setVisible(true);
	}
}
