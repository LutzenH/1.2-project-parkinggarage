package parkeersimulator.view;

import parkeersimulator.model.ParkingGarageModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * A view that returns the current time of the week.
 * 
 * @author LutzenH
 * @author ThowV
 * @author b-kuiper
 */
public class TimeView extends AbstractView {
	///Declaration of the label used to display the time.
	private JLabel timeLabel;

	/**
	 * Constructor of TimeView, Creates the panel for this view.
	 */
	public TimeView(ParkingGarageModel model) {
		super(model);
		
		setLayout(new GridLayout(1, 0, 0, 0));
		this.setPreferredSize(new Dimension(130, 30));
		
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		
		timeLabel = new JLabel("d: 0, h: 0, m: 0");
		timeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		add(timeLabel);

	}
	
	/**
	 * Repaints the information diplayed in this view.
	 */
    @Override
    public void updateView() {
    	ParkingGarageModel parkingGarageModel = (ParkingGarageModel) model;
    	
    	timeLabel.setText("d: " + parkingGarageModel.getDay() + ", h: " + parkingGarageModel.getHour() + ", m: " + parkingGarageModel.getMinute());
    	
    	repaint();
    }

}
