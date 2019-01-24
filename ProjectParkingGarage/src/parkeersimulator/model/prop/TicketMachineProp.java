package parkeersimulator.model.prop;

import java.awt.Color;

public class TicketMachineProp extends Prop {

	public TicketMachineProp() {
		super(2f);
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
