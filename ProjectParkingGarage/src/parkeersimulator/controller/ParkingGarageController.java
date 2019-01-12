package parkeersimulator.controller;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import parkeersimulator.model.ParkingGarageModel;

public class ParkingGarageController extends AbstractController implements ActionListener {

	private JButton plusone;
	private JButton plushunderd;
	private JButton start;
	private JButton stop;
	
	public ParkingGarageController(ParkingGarageModel model) {
		super(model);
		
		setSize(450, 50);
		plusone=new JButton("+1");
		plusone.addActionListener(this);
		
		plushunderd=new JButton("+100");
		plushunderd.addActionListener(this);
		
		start=new JButton("Start");
		start.addActionListener(this);
		stop=new JButton("Stop");
		stop.addActionListener(this);
		
		this.setLayout(new FlowLayout());
		add(plusone);
		add(plushunderd);
		add(start);
		add(stop);
		plusone.setBounds(140, 10, 70, 30);
		start.setBounds(229, 10, 70, 30);
		stop.setBounds(319, 10, 70, 30);
		
		this.setBorder(BorderFactory.createLineBorder(Color.black));

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {	
		if (e.getSource()==plusone) {
			model.tick();
		}
		
		if (e.getSource()==plushunderd) {
			for(int i = 0; i < 100; i++)
				model.tick();
		}
		
		if (e.getSource()==start) {
			model.start();
		}
		
		if (e.getSource()==stop) {
			model.stop();
		}
	}

}
