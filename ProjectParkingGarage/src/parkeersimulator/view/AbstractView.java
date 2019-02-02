package parkeersimulator.view;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;

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

import parkeersimulator.model.AbstractModel;
import parkeersimulator.view.jcomponent.JComponentSlider;

/**
 * Abstract class of a view without control elements.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 * 
 */
public abstract class AbstractView extends JPanel {
	///Declaration of the model this view should be retrieving information from.
	protected AbstractModel model;

	/**
	 * The constructor of AbstractView
	 * @param model The model this view should be retrieving information from.
	 */
	public AbstractView(AbstractModel model) {
		this.model=model;
		model.addView(this);
	}
	
	/**
	 * @return The model this view should be retrieving information from.
	 */
	public AbstractModel getModel() {
		return model;
	}
	
	/**
	 * Repaints this view.
	 */
	public void updateView() {
		repaint();
	}

	/**
	 * Builds the slider for the slider controller
	 * @param value The amount the slider is currently set to
	 * @param min The minimum amount the slider can slide to
	 * @param max The maximum amount the slider can slide to
	 * @param stepValues The values of each step in the slider
	 * @return Returns a slider with the specified properties
	 */
	public static JSlider buildComponent_slider(int value, int min, int max, int[] stepValues) {
		JSlider slider;
		
		if(stepValues != null)
			slider = new JComponentSlider(stepValues);
		else
			slider = new JSlider();
		
		slider.setValue(value);
		slider.setMinimum(min);
		slider.setMaximum(max);
		slider.setBorder(null);
		
		return slider;
	}
	
	public static JSlider buildComponent_slider(int value, int min, int max) {
		return buildComponent_slider(value, min, max, null);
	}
	
	public static JSlider buildComponent_slider(int value, int[] stepValues) {
		return buildComponent_slider(value, 0, stepValues.length - 1, stepValues);
	}

	/**
	 * Builds the slider component for use in the views
	 * @param controllerName Label text above the slider
	 * @param slider The slider component for use in this controller
	 * @param listener The listener that listens to a change of values
	 * @return Returns a slider controller
	 */
	public static JPanel buildController_slider(String controllerName, JSlider slider, ChangeListener listener) {
		JPanel panel = buildController_header(controllerName);
		
		JPanel panel_controlContent = new JPanel();
		panel.add(panel_controlContent);

		slider.addChangeListener(listener);
		panel_controlContent.add(slider);
		
		JLabel label_controlValue = new JLabel("Value");
		label_controlValue.setText(Integer.toString(slider.getValue()));
		slider.addChangeListener(e -> label_controlValue.setText(Integer.toString(slider.getValue())));
		panel_controlContent.add(label_controlValue);
		
		return panel;
	}

	/**
	 * Builds the spinner for the spinner controller
	 * @param value The amount the slider is currently set to
	 * @param min The minimum amount the slider can slide to
	 * @param max The maximum amount the slider can slide to
	 * @param stepSize The amount the spinner skips every step
	 * @return Returns a spinner with the specified properties
	 */
	public static JSpinner buildComponent_spinner(Number value, Comparable min, Comparable max, Number stepSize) {
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(value, min, max, stepSize));
		
		return spinner;
	}
	
	/**
	 * Builds the spinner component for use in the views
	 * @param controllerName Label text above the slider
	 * @param buttonName The text inside the button next to the spinner
	 * @param spinner The spinner component for use in this controller
	 * @param createLabel Bool that tells the function if this spinner should have a label next to it
	 * @param labelText The text that sits inside the label next to the spinner if specified
	 * @param listener The listener that listens to a change of values
	 * @return Returns a spinner controller
	 */
	public static JPanel buildController_spinner(String controllerName, String buttonName, JSpinner spinner, Boolean createLabel, String labelText, ActionListener listener) {
		JPanel panel = buildController_header(controllerName);
		
		JPanel panel_controlContent = new JPanel();
		panel.add(panel_controlContent);

		panel_controlContent.add(spinner);
		
		if(createLabel == true)
		{
			JLabel lblNewLabel = new JLabel(labelText);
			panel_controlContent.add(lblNewLabel);
		}
		
		JButton btnNewButton = new JButton(buttonName);
		btnNewButton.addActionListener(listener);
		panel_controlContent.add(btnNewButton);
		
		return panel;
	}
	
	public static JPanel buildController_spinner(String controllerName, String buttonName, JSpinner spinner, ActionListener listener) {
		return buildController_spinner(controllerName, buttonName, spinner, false, "", listener);
	}
	
	public static JPanel buildController_spinner(String controllerName, String buttonName, JSpinner spinner, String labelText, ActionListener listener) {
		return buildController_spinner(controllerName, buttonName, spinner, true, labelText, listener);
	}
	
	/**
	 * Builds the grouped buttons component for use in the views
	 * @param buttons The buttons inside the button group
	 * @return Returns a grouped buttons controller
	 */
	public static JPanel buildController_groupedButtons(JButton[] buttons) {
		JPanel panel = buildController_header();
		
		JPanel panel_controlContent = new JPanel();
		panel.add(panel_controlContent);
		
		for(JButton button : buttons) {
			panel_controlContent.add(button);
		}
		
		return panel;
	}

	/**
	 * Builds the wrapper/header for use in each controller to sit above the set of controls
	 * @param createHeaderLabel Bool that tells the function if it should build a header label above the set of controls
	 * @param controllerName The text that sits inside the string above the set of controls
	 * @return Returns a header
	 */
	public static JPanel buildController_header(boolean createHeaderLabel, String controllerName) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(new EmptyBorder(5, 5, 5, 15));
		
		if(createHeaderLabel) {
			JLabel label_controlHeader = new JLabel(controllerName);
			label_controlHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
			label_controlHeader.setHorizontalAlignment(SwingConstants.LEFT);
			label_controlHeader.setFont(new Font("Tahoma", Font.PLAIN, 14));
			panel.add(label_controlHeader);
		}

		return panel;
	}
	
	public static JPanel buildController_header(String controllerName) {
		return buildController_header(true, controllerName);
	}
	
	public static JPanel buildController_header() {
		return buildController_header(false, "");
	}
}
