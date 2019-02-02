package parkeersimulator.view;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import parkeersimulator.model.ParkingGarageModel;

public class AdviceView extends AbstractView {
	//Declaration of the JLabels used in this view.
	private JLabel adviceCarsLeavingTheEntrance;
	private JLabel adviceEntrance;
	private JLabel adviceExit;
	private JLabel advicePaymentCarQueue;
	private JLabel text;
	private JLabel adviceLocationProps;
	
	//Declaration of the models required in this view.
	private ParkingGarageModel parkingGarageModel = (ParkingGarageModel) model;
	
	/**
	 * Constructor of QueueCountView.
	 */
	public AdviceView(ParkingGarageModel model) {
		super(model);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		text = new JLabel("<html>The advice will become visible after one month.<br><br></html>");
		add(text);
		
		adviceCarsLeavingTheEntrance = new JLabel(parkingGarageModel.adviceCarsLeavingEntranceCarQueueu());
		add(adviceCarsLeavingTheEntrance);
		
		adviceEntrance = new JLabel(parkingGarageModel.adviceEntranceCarQueue());
		add(adviceEntrance);
		
		adviceExit = new JLabel(parkingGarageModel.adviceExitCarQueue());
		add(adviceExit);
		
		advicePaymentCarQueue = new JLabel(parkingGarageModel.advicePaymentCarQueue());
		add(advicePaymentCarQueue);
		
		adviceLocationProps = new JLabel(parkingGarageModel.adviceLocationProps());
		add(adviceLocationProps);
	}
	
	/**
	 * repaints the information displayed in this view.
	 */
    @Override
    public void updateView() {
    	adviceCarsLeavingTheEntrance.setText(parkingGarageModel.adviceCarsLeavingEntranceCarQueueu());
    	adviceEntrance.setText(parkingGarageModel.adviceEntranceCarQueue());
    	adviceExit.setText(parkingGarageModel.adviceExitCarQueue());
    	advicePaymentCarQueue.setText(parkingGarageModel.advicePaymentCarQueue());
    	adviceLocationProps.setText(parkingGarageModel.adviceLocationProps());
    	repaint();
    }
}
