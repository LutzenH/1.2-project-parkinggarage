package parkeersimulator.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import parkeersimulator.controller.AbstractController.ActionType;
import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.controller.TimeController;
import parkeersimulator.handler.ModelHandler;
import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.model.TimeModel;

/**
 * Class of a view that can start and stop the simulation.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 * 
 */
public class CarParkSimulationControlView extends AbstractControllableView{
	/**
	 * Constructor of CarParkSimulationControlView
	 * @param model The model this view should be getting information from.
	 * @param controller The controller this view should send instruction to.
	 */ 
	public CarParkSimulationControlView(TimeModel timeModel, TimeController timeController) {
		super(timeModel, timeController);

		ChangeListener changeListener;
		ActionListener actionListener;

		//Tick pause amount
		JSlider tickPauseAmountSlider = buildComponent_slider(timeModel.getTickPause(), new int[] {0, 1, 2, 3, 4, 5, 10, 20, 50, 100});
		changeListener = e -> controller.performAction(ActionType.TIME_TICKPAUSEAMOUNT, new HashMap<String, Object>() {{ put("amount", tickPauseAmountSlider.getValue()); }});
		JPanel tickPauseAmountController = buildController_slider("Tick pause amount", tickPauseAmountSlider, changeListener);
		
		//Tick jump amount
		JSpinner tickJumpAmountSpinner = buildComponent_spinner(100, 1, 10000, 1);
		actionListener = e -> controller.performAction(ActionType.TIME_TICKAMOUNT, new HashMap<String, Object>() {{ put("amount", tickJumpAmountSpinner.getValue()); }});
		JPanel tickJumpAmountController = buildController_spinner("Tick jump amount", "Fast forward", tickJumpAmountSpinner, actionListener);
		
		//Tick start and stop
		JButton tickStartButton = new JButton("Start");
		tickStartButton.setBounds(229, 10, 70, 30);
		tickStartButton.addActionListener(e -> controller.performAction(ActionType.TIME_TICKSTART));
		JButton tickStopButton = new JButton("Stop");
		tickStopButton.setBounds(319, 10, 70, 30);
		tickStopButton.addActionListener(e -> controller.performAction(ActionType.TIME_TICKSTOP));
		
		JPanel simulationStartStopController = buildController_groupedButtons(new JButton[] {tickStartButton, tickStopButton});
		
		//Set the layoutManager of the JPanel that all these controls sit on
		this.setLayout(new FlowLayout());
		
		//Add the components to the panel
		add(tickPauseAmountController);
		add(tickJumpAmountController);
		add(simulationStartStopController);

		setVisible(true);
	}
}
