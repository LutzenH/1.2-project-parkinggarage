package parkeersimulator.view;

import javax.swing.JPanel;

import parkeersimulator.model.AbstractModel;
import parkeersimulator.model.ParkingGarageModel;

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
}
