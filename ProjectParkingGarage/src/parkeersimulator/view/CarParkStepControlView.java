package parkeersimulator.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;

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
		
		ChangeListener changeListener;
		ActionListener actionListener;

		//Tick pause amount
		JSlider tickPauseAmountSlider = buildComponent_slider(100, 0, 1000);
		changeListener = e -> controller.performAction(ActionType.EVENT_TICKPAUSEAMOUNT, new HashMap<String, Object>() {{ put("amount", tickPauseAmountSlider.getValue()); }});
		JPanel tickPauseAmountController = buildController_slider("Tick pause amount", tickPauseAmountSlider, changeListener);
		
		//Tick jump amount
		JSpinner tickJumpAmountSpinner = buildComponent_spinner(100, 1, 10000, 1);
		actionListener = e -> controller.performAction(ActionType.EVENT_TICKAMOUNT, new HashMap<String, Object>() {{ put("amount", tickJumpAmountSpinner.getValue()); }});
		JPanel tickJumpAmountController = buildController_spinner("Tick jump amount", "Fast forward", tickJumpAmountSpinner, false, "", actionListener);
		
		
		//AdHocArrival amounts
		JSpinner adHocWeekArrivalsAmountSpinner = buildComponent_spinner(model.getAdHocArrivalsPercent_week(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.EVENT_ADHOCWEEK_AMOUNT, new HashMap<String, Object>() {{ put("amount", adHocWeekArrivalsAmountSpinner.getValue()); }});
		JPanel adHocWeekArrivalsAmountController = buildController_spinner("AdHoc week arrivals", "Set", adHocWeekArrivalsAmountSpinner, true, "in %", actionListener);
		
		JSpinner adHocWeekendArrivalsAmountSpinner = buildComponent_spinner(model.getAdHocArrivals_weekend(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.EVENT_ADHOCWEEKEND_AMOUNT, new HashMap<String, Object>() {{ put("amount", adHocWeekendArrivalsAmountSpinner.getValue()); }});
		JPanel adHocWeekendArrivalsAmountController = buildController_spinner("AdHoc weekend arrivals", "Set", adHocWeekendArrivalsAmountSpinner, true, "in %", actionListener);
		
		JSpinner adHocEventArrivalsAmountSpinner = buildComponent_spinner(model.getAdHocArrivals_event(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.EVENT_ADHOCEVENT_AMOUNT, new HashMap<String, Object>() {{ put("amount", adHocEventArrivalsAmountSpinner.getValue()); }});
		JPanel adHocEventdArrivalsAmountController = buildController_spinner("AdHoc event arrivals", "Set", adHocEventArrivalsAmountSpinner, true, "in %", actionListener);
		
		//PassArrival amounts
		JSpinner passWeekArrivalsAmountSpinner = buildComponent_spinner(model.getPassArrivals_week(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.EVENT_PASSWEEK_AMOUNT, new HashMap<String, Object>() {{ put("amount", passWeekArrivalsAmountSpinner.getValue()); }});
		JPanel passWeekArrivalsAmountController = buildController_spinner("Pass week arrivals", "Set", passWeekArrivalsAmountSpinner, true, "in %", actionListener);
		
		JSpinner passWeekendArrivalsAmountSpinner = buildComponent_spinner(model.getPassArrivals_weekend(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.EVENT_PASSWEEKEND_AMOUNT, new HashMap<String, Object>() {{ put("amount", passWeekendArrivalsAmountSpinner.getValue()); }});
		JPanel passWeekendArrivalsAmountController = buildController_spinner("Pass weekend arrivals", "Set", passWeekendArrivalsAmountSpinner, true, "in %", actionListener);
		
		JSpinner passEventWeekArrivalsAmountSpinner = buildComponent_spinner(model.getPassArrivals_eventWeek(), 0, 300, 1);
		actionListener = e -> controller.performAction(ActionType.EVENT_PASSEVENTWEEK_AMOUNT, new HashMap<String, Object>() {{ put("amount", passEventWeekArrivalsAmountSpinner.getValue()); }});
		JPanel passEventWeekArrivalsAmountController = buildController_spinner("Pass week event arrivals", "Set", passEventWeekArrivalsAmountSpinner, true, "in %", actionListener);
		
		
		//Tick start and stop
		tickStartButton = new JButton("Start");
		tickStartButton.addActionListener(e -> controller.performAction(ActionType.EVENT_TICKSTART));
		tickStopButton = new JButton("Stop");
		tickStopButton.addActionListener(e -> controller.performAction(ActionType.EVENT_TICKSTOP));
		
		this.setLayout(new FlowLayout());
		
		
		add(tickPauseAmountController);
		add(tickJumpAmountController);
		
		add(adHocWeekArrivalsAmountController);
		add(adHocWeekendArrivalsAmountController);
		add(adHocEventdArrivalsAmountController);
		
		add(passWeekArrivalsAmountController);
		add(passWeekendArrivalsAmountController);
		add(passEventWeekArrivalsAmountController);
		
		add(tickStartButton);
		add(tickStopButton);

		
		tickStartButton.setBounds(229, 10, 70, 30);
		tickStopButton.setBounds(319, 10, 70, 30);
		
		this.setBorder(BorderFactory.createLineBorder(Color.black));

		setVisible(true);
	}
	
	private JSlider buildComponent_slider(int value, int min, int max) {
		JSlider slider = new JSlider();
		slider.setBorder(null);
		slider.setValue(value);
		slider.setMinimum(min);
		slider.setMaximum(max);
		
		return slider;
	}
	private JPanel buildController_slider(String controllerName, JSlider slider, ChangeListener listener) {
		JPanel panel = new JPanel();
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{241, 0};
		gbl_panel.rowHeights = new int[]{18, 38, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblNewLabel_1 = new JLabel(controllerName);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_1.insets = new Insets(0, 5, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		panel.add(panel_1, gbc_panel_1);

		slider.addChangeListener(listener);
		panel_1.add(slider);
		
		JLabel lblNewLabel = new JLabel("Value");
		lblNewLabel.setText(Integer.toString(slider.getValue()));
		slider.addChangeListener(e -> lblNewLabel.setText(Integer.toString(slider.getValue())));
		panel_1.add(lblNewLabel);
		
		return panel;
	}

	private JSpinner buildComponent_spinner(Integer value, Integer min, Integer max, Integer stepSize) {
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(value, min, max, stepSize));
		
		return spinner;
	}
	private JPanel buildController_spinner(String controllerName, String buttonName, JSpinner spinner, Boolean createLabel, String labelText, ActionListener listener) {
		JPanel panel_6 = new JPanel();
		add(panel_6);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[]{151, 0};
		gbl_panel_6.rowHeights = new int[]{17, 38, 0};
		gbl_panel_6.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_6.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_6.setLayout(gbl_panel_6);
		
		JLabel label_2 = new JLabel(controllerName);
		label_2.setHorizontalAlignment(SwingConstants.LEFT);
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.fill = GridBagConstraints.BOTH;
		gbc_label_2.insets = new Insets(0, 5, 5, 0);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 0;
		panel_6.add(label_2, gbc_label_2);
		
		JPanel panel_7 = new JPanel();
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.fill = GridBagConstraints.BOTH;
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 1;
		panel_6.add(panel_7, gbc_panel_7);

		panel_7.add(spinner);
		
		if(createLabel == true)
		{
			JLabel lblNewLabel = new JLabel(labelText);
			panel_7.add(lblNewLabel);
		}
		
		JButton btnNewButton = new JButton(buttonName);
		btnNewButton.addActionListener(listener);
		panel_7.add(btnNewButton);
		
		return panel_6;
	}
}
