package parkeersimulator.model.prop;

import java.awt.Color;

import parkeersimulator.model.location.Coordinate;

public class TicketMachineProp extends Prop {

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
