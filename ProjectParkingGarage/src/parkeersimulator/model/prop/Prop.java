package parkeersimulator.model.prop;

import java.awt.Color;

import parkeersimulator.model.location.Coordinate;

public abstract class Prop {
	
	public enum PropType { PROP_ENTRANCE, PROP_EXIT, PROP_TICKETMACHINE };
	
	private Coordinate position;
	
	public Prop(Coordinate position) {
		this.position = position;
	}
	
	public abstract Color getColor();
	public abstract String getName();
	public abstract PropType getType();

	/**
	 * @return the location of the Prop
	 */
	public Coordinate getPosition() {
		return position;
	}
	
	/**
	 * Calculate the coordinate using an index.
	 * @param index the index of the prop
	 * @param numberOfPlaces the length of the numberOfPlaces
	 * @return the calculated coordinate of the prop.
	 */
	public static Coordinate calculateCoordinate(int index, int numberOfPlaces) {	
		int x = index;
		int y = (index % 2 == 0) ? -1 : numberOfPlaces + 1;
		
		return new Coordinate(x, y);
	}
	
}
