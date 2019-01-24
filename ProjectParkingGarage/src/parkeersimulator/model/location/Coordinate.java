package parkeersimulator.model.location;

public class Coordinate {
	private int x;
	private int y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

    /**
     * Implement content equality.
     */
    public boolean equals(Object obj) {
        if(obj instanceof Coordinate) {
            Coordinate other = (Coordinate) obj;
            return x == other.getX() && y == other.getY();
        }
        else {
            return false;
        }
    }
    
    /**
     * Return a string of the form floor,row,place.
     * @return A string representation of the location.
     */
    public String toString() {
        return x + "," + y;
    }
	
	/**
	 * @return the x-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y-coordinate
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Calculates the distance between two coordinates
	 * @param coord1 the first Coordinate
	 * @param coord2 the second Coordinate
	 * @return the distance between coord1 and coord2
	 */
	public static double calculateDistance(Coordinate coord1, Coordinate coord2) {
		return Math.sqrt(Math.pow((coord2.getX() - coord1.getX()), 2) + Math.pow((coord2.getY() - coord1.getY()), 2));
	}
	
}
