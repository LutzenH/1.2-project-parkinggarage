package parkeersimulator.view.frame;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import parkeersimulator.controller.ParkingGarageController;
import parkeersimulator.model.ParkingGarageModel;
import parkeersimulator.view.GarageCustomisationView;
import parkeersimulator.view.OpenGarageView;

public class GarageDesignFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public GarageDesignFrame(ParkingGarageModel model, ParkingGarageController controller) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new GarageCustomisationView(model, controller);
		contentPane.add(panel, BorderLayout.CENTER);
		
		JPanel panel_1 = new OpenGarageView(model, controller);
		contentPane.add(panel_1, BorderLayout.SOUTH);
	}

}
