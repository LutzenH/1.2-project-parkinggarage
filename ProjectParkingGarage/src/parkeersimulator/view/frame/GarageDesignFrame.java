package parkeersimulator.view.frame;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.view.GarageCustomisationView;
import parkeersimulator.view.OpenGarageView;

/**
 * The JPanel for the customisation panel.
 * @author LutzenH
 *
 */
public class GarageDesignFrame extends JPanel {

	/**
	 * Create the frame.
	 * @param model The ParkingGarageModel this view uses to display the garage
	 * @param controller the ParkingGarageController this view uses to send instructions to.
	 */
	public GarageDesignFrame(ParkingGarageModel model, ParkingGarageController controller) {
		setBounds(100, 100, 450, 300);
		
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
		this.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new GarageCustomisationView(model, controller);
		this.add(panel, BorderLayout.CENTER);
		
		JPanel panel_1 = new OpenGarageView(model, controller);
		this.add(panel_1, BorderLayout.NORTH);
	}

}
