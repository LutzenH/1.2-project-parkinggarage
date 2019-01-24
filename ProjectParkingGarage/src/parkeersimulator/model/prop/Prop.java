package parkeersimulator.model.prop;

import java.awt.Color;

public abstract class Prop {
	
	public enum PropType { PROP_ENTRANCE, PROP_EXIT, PROP_TICKETMACHINE };
	
	public float preferenceAmount;
	
	public Prop(float preferenceAmount) {
		this.preferenceAmount = preferenceAmount;
	}
	
	public abstract Color getColor();
	public abstract String getName();
	public abstract PropType getType();
	
	public float getPreferenceAmount() { return preferenceAmount; }
	public void setPreferenceAmount(float preferenceAmount) { this.preferenceAmount = preferenceAmount; }
	
}
