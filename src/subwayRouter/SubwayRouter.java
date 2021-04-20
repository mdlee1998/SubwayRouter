package subwayRouter;

import java.io.*;
import java.util.Scanner;

public class SubwayRouter {
	
	public static void main(String args[]) throws IOException {
		Scanner in = new Scanner(System.in);
		String file = getFile(in);
		Router r = new Router(file);
		findRoutes(in,r);
	}
			
		
	
	
	public static String getFile(Scanner in) {
		boolean fileMade = false;
		String file = "";
		while(!fileMade) {
				System.out.println("Choose a CSV file to use for your subway system.\n"
						+ "Enter stop to end the program\n");
				file = in.nextLine();
				if(file.equalsIgnoreCase("Stop")) {
					System.exit(0);
				}
				else {
					fileMade = true;
				}
		}
		return file;
	}
	
	public static void findRoutes(Scanner in,Router r) {
		boolean ask = true;
		while(ask) {
			System.out.println("Where are you starting?\n"
					+ "Enter stop to end the program\n");
			String from = in.nextLine();
			if(from.equalsIgnoreCase("stop")) {
				ask = false;
				System.exit(0);
			}
			System.out.println("Where are you going?\n"
					+ "Enter stop to end the program.\n");
			String to = in.nextLine();
			if(to.equalsIgnoreCase("stop")){
				ask = false;
				System.exit(0);
			}
			System.out.println(r.findRoute(from, to));
		}
	}
}
