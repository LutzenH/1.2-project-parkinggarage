package parkeersimulator.model.prop;

import java.awt.Color;

import parkeersimulator.model.location.Coordinate;

public class EntranceProp extends Prop {

	public EntranceProp(Coordinate position) {
		super(0.3f, position);
	}

	@Override
	public Color getColor() { return Color.GREEN; }

	@Override
	public String getName() { return "Entrance"; }

	@Override
	public PropType getType() {
		return PropType.PROP_ENTRANCE;
	}

}
