package parkeersimulator.model.prop;

import java.awt.Color;

import parkeersimulator.model.location.Coordinate;

/**
 * A Prop that is used as an entrance.
 * @author LutzenH
 *
 */
public class EntranceProp extends Prop {

	/**
	 * Constructor for EntranceProp
	 * @param position the position of this Prop (this value is used for the likeability of a Place)
	 */
	public EntranceProp(Coordinate position) {
		super(position);
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
