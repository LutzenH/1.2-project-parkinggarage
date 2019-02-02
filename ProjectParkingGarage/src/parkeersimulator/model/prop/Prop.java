package parkeersimulator.model.prop;

import java.awt.Color;

import parkeersimulator.model.location.Coordinate;

/**
 * A prop inside of the garage
 * @author LutzenH
 *
 */
public abstract class Prop {
	
	//Declaration of the different type of props as an enum.
	public enum PropType { PROP_ENTRANCE, PROP_EXIT, PROP_TICKETMACHINE };
	
	//Declaration of the coordinate this prop is at relative to the position of places.
	private Coordinate position;
	
	/**
	 * Constructor for Prop
	 * @param position the position this prop is at.
	 */
	public Prop(Coordinate position) {
		this.position = position;
	}
	
	/**
	 * @return the color of this prop displayed in the garage.
	 */
	public abstract Color getColor();
	
	/**
	 * @return the name of this prop.
	 */
	public abstract String getName();
	
	/**
	 * @return the type of prop this is.
	 */
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
