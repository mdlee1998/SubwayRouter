package subwayRouter;

/**
 * Link contains connections of subway stations, with a from station and a to station.
 * These two stations are connected, and can each be reached from each other.  Link
 * also contains the color identifier of the line that connects the two stations.
 * @author Matthew Lee
 *
 */
public class Link {

	//Fields//
	
	//One of the stations
	private String from;
	
	//The station connected to from
	private String to;
	
	//The color identifier of the line
	private String color;
	
	
	//Constructors//
	/**
	 * Constructs a link, by setting the from, to and color fields
	 * @param from
	 * @param to
	 * @param color
	 */
	protected Link(String from, String to, String color) {
		this.from = from;
		this.to=to;
		this.color=color;
	} 
	
	/**
	 * Creates an empty link
	 */
	protected Link() {
		this.from = null;
		this.to= null;
		this.color = null;
	}
	
	/**
	 * Gets the from station of the link
	 * @return from field
	 */
	protected String getFrom() {
		return from;
	}
	
	/**
	 * Gets the to station of the link
	 * @return to field
	 */
	protected String getTo() {
		return to;
	}
	
	/**
	 * Gets the color identifier of the line
	 * @return color field
	 */
	protected String getColor() {
		return color;
	}
	
	/**
	 * Returns a string of the link, in brackets, with the 
	 * from appearing first, the the to, the the color, all separated
	 * by commas.
	 */
	@Override
	public String toString() {
		return "["+from+","+to+","+color+"]";
		
	}
	
	
}
