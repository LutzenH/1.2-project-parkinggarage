package parkeersimulator.model.prop;

import java.awt.Color;

import parkeersimulator.model.location.Coordinate;

/**
 * A Prop that is used as a TicketMachine.
 * @author LutzenH
 *
 */
public class TicketMachineProp extends Prop {

	/**
	 * Constructor for TicketMachineProp
	 * @param position the position of this Prop (this value is used for the likeability of a Place)
	 */
	public TicketMachineProp(Coordinate position) {
		super(position);
	}

	@Override
	public Color getColor() { return Color.ORANGE; }

	@Override
	public String getName() { return "Ticket Machine"; }

	@Override
	public PropType getType() {
		return PropType.PROP_TICKETMACHINE;
	}
	
}
