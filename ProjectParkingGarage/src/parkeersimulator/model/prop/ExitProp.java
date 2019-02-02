package parkeersimulator.model.prop;

import java.awt.Color;

import parkeersimulator.model.location.Coordinate;

/**
 * A Prop that is used as an exit.
 * @author LutzenH
 *
 */
public class ExitProp extends Prop {

	/**
	 * Constructor for ExitProp
	 * @param position the position of this Prop (this value is used for the likeability of a Place)
	 */
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
