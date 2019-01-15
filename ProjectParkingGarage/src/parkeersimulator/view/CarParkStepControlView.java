package parkeersimulator.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.controller.ParkingGarageController.ActionType;
import parkeersimulator.model.ParkingGarageModel;

/**
 * Class of a view that can start and stop the simulation.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 * 
 */
public class CarParkStepControlView extends AbstractControllableView{
	///Declaration of the buttons in this view.
	private JSlider tickPauseAmountSlider;
	private JButton tickPauseAmountButton;
	
	private JSpinner tickAmountInput;
	private JButton tickAmountButton;
	
	private JButton tickStartButton;
	private JButton tickStopButton;
	
	/**
	 * Constructor of CarParkStepControlView
	 * @param model The model this view should be getting information from.
	 * @param controller The controller this view should send instruction to.
	 */ 
	public CarParkStepControlView(ParkingGarageModel model, ParkingGarageController controller) {
		super(model, controller);
		
		setSize(450, 50);

		//Tick pause amount
		tickPauseAmountSlider = new JSlider();
		tickPauseAmountSlider.setValue(100);
		tickPauseAmountSlider.setMinimum(1);
		tickPauseAmountSlider.setMaximum(1000);
		tickPauseAmountButton = new JButton("Set");
		tickPauseAmountButton.addActionListener(e -> controller.performAction(ActionType.EVENT_TICKPAUSEAMOUNT, new HashMap<String, Object>() {{ put("amount", tickPauseAmountSlider.getValue()); }}));
		
		//Tick amount
		tickAmountInput = new JSpinner();
		tickAmountInput.setModel(new SpinnerNumberModel(100, 1, 1000, 1));
		tickAmountButton = new JButton("Step");
		
		tickAmountButton.addActionListener(e -> controller.performAction(ActionType.EVENT_TICKAMOUNT, new HashMap<String, Object>() {{ put("amount", tickAmountInput.getValue()); }}));
		
		//Tick start and stop
		tickStartButton = new JButton("Start");
		tickStartButton.addActionListener(e -> controller.performAction(ActionType.EVENT_TICKSTART));
		tickStopButton = new JButton("Stop");
		tickStopButton.addActionListener(e -> controller.performAction(ActionType.EVENT_TICKSTOP));
		
		this.setLayout(new FlowLayout());
		
		add(tickPauseAmountSlider);
		add(tickPauseAmountButton);
		add(tickAmountInput);
		add(tickAmountButton);
		add(tickStartButton);
		add(tickStopButton);
		
		tickPauseAmountSlider.setBounds(140, 10, 70, 30);
		tickPauseAmountButton.setBounds(140, 10, 70, 30);
		tickAmountInput.setBounds(140, 10, 70, 30);
		tickAmountButton.setBounds(140, 10, 70, 30);
		tickStartButton.setBounds(229, 10, 70, 30);
		tickStopButton.setBounds(319, 10, 70, 30);
		
		this.setBorder(BorderFactory.createLineBorder(Color.black));

		setVisible(true);
	}
}
