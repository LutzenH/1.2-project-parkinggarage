package parkeersimulator.model.prop;

import java.awt.Color;

import parkeersimulator.model.location.Coordinate;

public class ExitProp extends Prop {

	public ExitProp(Coordinate position) {
		super(position);
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
