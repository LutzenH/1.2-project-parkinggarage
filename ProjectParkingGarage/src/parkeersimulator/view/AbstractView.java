package parkeersimulator.view;

import javax.swing.JPanel;

import parkeersimulator.model.AbstractModel;
import parkeersimulator.model.ParkingGarageModel;

public abstract class AbstractView extends JPanel {
	private static final long serialVersionUID = -2767764579227738552L;
	protected ParkingGarageModel model;

	public AbstractView(ParkingGarageModel model) {
		this.model=model;
		model.addView(this);
	}
	
	public AbstractModel getModel() {
		return model;
	}
	
	public void updateView() {
		repaint();
	}
}
