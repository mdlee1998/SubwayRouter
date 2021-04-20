package subwayRouter;

import java.io.*;
import java.util.*;

/**
 * Router creates a map of different lists of links, with keys that are the name of a station.
 * Each key accesses the list of stations that can be directly traveled to from the key
 *  station.  Router can then be used to find the route from one station to another with
 *  the shortest number of stops, then print that route in an easy to read and user-
 *  friendly manner.
 * 
 * @author Matthew Lee
 *
 */
public class Router {

	//FIELDS//
	
	//HashMap of of possible station to station combinations
	private HashMap<String,ArrayList<Link>> map;
	
	//Constants//
	
	public final int COLORLOC = 3;
	
	/**
	 * Creates a Router object using a filename that reads a file.  The file must be a
	 * CSV file of stations and colors of line, where each line has two stations that 
	 * are connected.  It maps them out into the map member.  It uses both the from 
	 * and the to as keys, making the other the destination.  
	 * @param fileName The String name of the file	
	 * @throws IOException If an error is thrown with the file name
	 */
	protected Router(String fileName) throws IOException {
		try {
			map = new HashMap<String,ArrayList<Link>>();
				BufferedReader br = new BufferedReader(new FileReader(fileName));
				String line;
				while((line = br.readLine()) != null) {
					//Splits lines of csv file into from, to and color values
					String[] splitLine = line.split(",");
					String from = splitLine[0];
					String to = splitLine[1];
					String color = splitLine[COLORLOC]; 
					//Uses both from and to as the key,
					//with the opposite at a link in the list value
					putInMap(from,to,color);
					putInMap(to,from,color);
				}
				br.close();
		}
		catch(IOException ex) {
			System.out.println("File Input Error");
		}
	}
	
	/**
	 * Gets the map field
	 * @return the map
	 */
	protected HashMap<String,ArrayList<Link>> getMap() {
		return map;
	}
	
	/**
	 * Makes a link with the given parameter. Then makes from the key of the
	 * map, with a value of an ArrayList of Links.  If the key is already
	 * present, it takes the list that the key correlates to, adds the new 
	 * destination to that list, then replaces the value at that key with the
	 * new list.
	 * @param from The starting point of the link, which becomes the key
	 * @param to The destination of the link
	 * @param color The color of the line the two stations are on.
	 */
	private void putInMap(String from,String to, String color) {
		ArrayList<Link> list;
		Link link = new Link(from,to,color);
		//If the key is already present
		if(map.containsKey(from)) {
			//Gets list already at that key
			list = map.get(from);
		} else {
			list = new ArrayList<Link>();
		}
		//If the list does not contain the new 
		//destination, add it to the list
		if(!list.contains(link)) {
			list.add(link);
		}
		
		map.put(from, list);
	}
	
	/**
	 * Starting at the starting location, find all the possible destinations from
	 * that location.  Then, finds all possible destinations from that location.  Continues
	 * until the end location is found, and displays the shortest number of stops to the destination
	 * @param start Starting location
	 * @param end Final Location
	 * @return The directions of the route that is the quickest
	 * @throws NoSuchElementException  In case the user inputs an element that does not exist
	 */
	protected String findRoute(String start,String end) throws NoSuchElementException {
		//Links that have been identified to be connected to the beginning link
		ArrayDeque<Link> identified = new ArrayDeque<Link>();
		//The set of visited String keys
		HashSet<String> visited = new HashSet<String>();
		//A stack of links that are being used
		Stack<Link> usedLinks = new Stack<Link>();
		//The destination location
		String station;
		
		try {
			//Throws exception if the start and stop are not in the map
			if(!(map.containsKey(start) || map.containsKey(end))) {
				throw new NoSuchElementException();
			}
			//Sets current station to the start
			station = start;
			//Gets the first link in the arraylist at the key of the current station
			Link link = map.get(station).get(0);
			identified.add(link);
			while(!identified.isEmpty()) {
				Link tempLink = identified.poll();
				//Sets current station to the link 
				station = tempLink.getTo();
				ArrayList<Link> tempList = map.get(station);
				//adds each of the links to the identified deque to be tested later
				for(Link thisLink:tempList) {
					if(!visited.contains(thisLink.getTo())) {
						identified.offer(thisLink);
					}
				}
				visited.add(station);
				usedLinks.push(tempLink);
				//if the current station is the last station
				if(station.equals(end)) {
					//Makes a stack of links for the path
					Stack<Link> path = makePath(usedLinks,start);
					return directions(path);
				}
			}
			return "The search has failed";
		}
		catch(NoSuchElementException ex) {
			return "You must enter valid station names for the start and end location";
		}
		catch(NullPointerException ex) {
			return "You must enter valid station names for the start and end location";
		}
	}
	
	/**
	 * Takes the stack of links that findRoute had gone through to get
	 * to the destination, then finds the relevant links in that stack
	 * that connect the from to the to.
	 * @param usedLinks The stack of links that were checked
	 * @param start The original starting point that must be found in the stack of links
	 * @return The stack path of correct links from the start to finish
	 */
	private Stack<Link> makePath(Stack<Link> usedLinks,String start) {
		Stack<Link> truePath = new Stack<Link>();
		Link current = usedLinks.pop();
		truePath.push(current);
		while(!current.getFrom().equals(start)) {
			Link previous = usedLinks.pop();
			//Goes through stack of visited links and finds the ones
			//that belong in the route.
			if(current.getFrom().equals(previous.getTo())) {
				current = previous;
				truePath.push(current);
			}
		}
		return truePath;
	}
	
	/**
	 * Creates a string of the directions from the stack path given. 
	 * String tells what station to enter at, what line to get on,
	 * how many stops to go, and where to exit and switch lines if
	 * that is necessary.  
	 * @param path The stack of links that go from the start to finish.
	 * @return A string of directions.
	 */
	private String directions(Stack<Link> path) {
		String directions = "";
		int lines = 0;
		if(!path.empty()) {
			Link first = path.pop();
			directions +="Enter at "+first.getFrom()+" on the "+first.getColor()+"line.\n";
		}
		ArrayDeque<Link> colorPath = new ArrayDeque<Link>();
		while(!path.isEmpty()) {
			Link current = path.pop();
			String color = current.getColor();
			colorPath.offer(current);
			if(path.isEmpty() || !color.equals(path.peek().getColor())) {
				if(!colorPath.isEmpty()) {
					int count = colorPath.size();
					Link first = colorPath.poll();
					Link last = first;
					while(!colorPath.isEmpty()) {
						last = colorPath.poll();
					}
					if(lines > 0) {
						directions+="Switch to the "+color+" line. ";
					}
					if(count == 1) {
						directions+="Ride for "+count+" stop\n"
								+ "until "+last.getTo()+"\n";
					}
					else {
						directions+="Ride for "+count+" stops\n"
									+ "until "+last.getTo()+".\n";
					}
					lines++;
				}
			}
		}
		return directions;
	}
	
}
