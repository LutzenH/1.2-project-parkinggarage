package parkeersimulator.view.jcomponent;

import javax.swing.JSlider;

/**
 * A custom slider that uses non-linear amounts
 * @author ThowV
 *
 */
public class JComponentSlider extends JSlider{
	int[] stepValues;
	
	/**
	 * Constructor for this JSlider
	 * @param stepValues the different values this slider should use.
	 */
	public JComponentSlider(int[] stepValues) {
		this.stepValues = stepValues;
	}
	
	@Override
	public int getValue(){
		int currentValue = super.getValue();
		int newValue = 0;

		if(stepValues != null)
			newValue = stepValues[currentValue];
		
		return newValue;
	}

}
