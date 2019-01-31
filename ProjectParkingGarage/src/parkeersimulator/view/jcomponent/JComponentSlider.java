package parkeersimulator.view.jcomponent;

import javax.swing.JSlider;

public class JComponentSlider extends JSlider{
	int[] stepValues;
	
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
