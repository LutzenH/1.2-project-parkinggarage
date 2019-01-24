package parkeersimulator.model.prop;

import java.awt.Color;

public class ExitProp extends Prop {

	public ExitProp() {
		super(0.8f);
	}

	@Override
	public Color getColor() { return Color.RED; }

	@Override
	public String getName() { return "Exit"; }

	@Override
	public PropType getType() {
		return PropType.PROP_EXIT;
	}
	
}
