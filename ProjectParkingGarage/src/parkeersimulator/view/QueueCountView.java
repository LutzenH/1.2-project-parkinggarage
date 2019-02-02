package parkeersimulator.view;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.GridLayout;

import parkeersimulator.model.ParkingGarageModel;

/**
 * A view that displays the number of cars in the four queues.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 *
 */
public class QueueCountView extends AbstractView {
	//Declaration of the JLabels used in this view.
	private JLabel entranceCarQueueLabel;
	private JLabel entrancePassQueueLabel;
	private JLabel paymentCarQueueLabel;
	private JLabel exitCarQueueLabel;
	
	/**
	 * Constructor of QueueCountView.
	 * @param model the ParkingGarageModel this view should get data from.
	 */
	public QueueCountView(ParkingGarageModel model) {
		super(model);
		
		setLayout(new GridLayout(4, 1, 0, 0));
		
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		
		entranceCarQueueLabel = new JLabel("entranceCarQueue: ");
		add(entranceCarQueueLabel);
		
		entrancePassQueueLabel = new JLabel("entrancePassQueue:");
		add(entrancePassQueueLabel);
		
		paymentCarQueueLabel = new JLabel("paymentCarQueue:");
		add(paymentCarQueueLabel);
		
		exitCarQueueLabel = new JLabel("exitCarQueue: ");
		add(exitCarQueueLabel);

	}
	
	/**
	 * repaints the information displayed in this view.
	 */
    @Override
    public void updateView() {
    	ParkingGarageModel parkingGarageModel = (ParkingGarageModel) model;
    	
    	entranceCarQueueLabel.setText("entranceCarQueue: " + parkingGarageModel.getEntranceCarQueue().carsInQueue());
    	entrancePassQueueLabel.setText("entrancePassQueue: " + parkingGarageModel.getEntrancePassQueue().carsInQueue());
    	paymentCarQueueLabel.setText("paymentCarQueue: " + parkingGarageModel.getPaymentCarQueue().carsInQueue());
    	exitCarQueueLabel.setText("exitCarQueue: " + parkingGarageModel.getExitCarQueue().carsInQueue());
    	
    	repaint();
    }

}
