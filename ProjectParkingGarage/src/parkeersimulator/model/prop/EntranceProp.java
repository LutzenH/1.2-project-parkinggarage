package parkeersimulator.model.prop;

import java.awt.Color;

public class EntranceProp extends Prop {

	public EntranceProp() {
		super(1f);
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
