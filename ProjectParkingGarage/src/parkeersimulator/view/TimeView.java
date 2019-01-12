package parkeersimulator.view;

import parkeersimulator.model.ParkingGarageModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TimeView extends AbstractView {
	
	private JLabel timeLabel;

	/**
	 * Create the panel.
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
	
    @Override
    public void updateView() {
    	
    	timeLabel.setText("d: " + model.getDay() + ", h: " + model.getHour() + ", m: " + model.getMinute());
    	
    	repaint();
    }

}
