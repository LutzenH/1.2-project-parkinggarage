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

import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.ParkingGarageModel;

/**
 * Class of a view that can start and stop the simulation.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 * 
 */
public class CarParkLayoutControlView extends AbstractControllableView{
	/**
	 * Constructor of CarParkLayoutControlView
	 * @param model The model this view should be getting information from.
	 * @param controller The controller this view should send instruction to.
	 */ 
	public CarParkLayoutControlView(ParkingGarageModel model, ParkingGarageController controller) {
		super(model, controller);
	}
}
