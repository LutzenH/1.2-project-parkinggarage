package parkeersimulator.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

import parkeersimulator.model.ParkingGarageModel;

public class QueueCountView extends AbstractView {

	private JLabel entranceCarQueueLabel;
	private JLabel entrancePassQueueLabel;
	private JLabel paymentCarQueueLabel;
	private JLabel exitCarQueueLabel;
	
	/**
	 * Create the panel.
	 */
	public QueueCountView(ParkingGarageModel model) {
		super(model);
		
		setLayout(new GridLayout(4, 1, 0, 0));
		
		entranceCarQueueLabel = new JLabel("entranceCarQueue: ");
		add(entranceCarQueueLabel);
		
		entrancePassQueueLabel = new JLabel("entrancePassQueue:");
		add(entrancePassQueueLabel);
		
		paymentCarQueueLabel = new JLabel("paymentCarQueue:");
		add(paymentCarQueueLabel);
		
		exitCarQueueLabel = new JLabel("exitCarQueue: ");
		add(exitCarQueueLabel);

	}
	
    @Override
    public void updateView() {
    	entranceCarQueueLabel.setText("entranceCarQueue: " + model.getEntranceCarQueue().carsInQueue());
    	entrancePassQueueLabel.setText("entrancePassQueue: " + model.getEntrancePassQueue().carsInQueue());
    	paymentCarQueueLabel.setText("paymentCarQueue: " + model.getPaymentCarQueue().carsInQueue());
    	exitCarQueueLabel.setText("exitCarQueue: " + model.getExitCarQueue().carsInQueue());
    	
    	repaint();
    }

}
