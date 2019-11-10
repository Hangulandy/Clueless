package edu.jhu.teamundecided.clueless.gameBoard;//By: Trey Hoffman, thoffm10@jhu.edu
//Description: This program draws out a minimal text based game board for "Clueless". 
//Use the menu to move players around and see the game board update.  

import java.util.*;


public class GameBoard{

	public GameBoard()
	{
	}

	//dictionary to hold player locations
	public static HashMap<String, String> playersMap = new HashMap<String, String>();
	
	//array of arrays to hold the game locations
	public static String[][] board = 
		{
			{"   ", "R01", "H01", "R02", "H02", "R03", "   "},
			{"S06", "H03", "T01", "H04", "T02", "H05", "S01"},
			{"   ", "R04", "H06", "R05", "H07", "R06", "   "},
			{"S05", "H08", "T03", "H09", "T04", "H10", "S02"},
			{"   ", "R07", "H11", "R08", "H12", "R09", "   "},
			{"   ", "   ", "S04", "   ", "S03", "   ", "   "}
		};

		// Labeled game board, formatting is not complete
		// {
		// 	{"       ", "Kitchen     ", "Hallway 1     ", "Ballroom   ", "Hallway 2     ", "Study     ", "       "},
		// 	{"Start 6", "Hallway 3   ", "Secret Passage", "Hallway 4  ", "Secret Passage", "Hallway 5 ", "Start 1"},
		// 	{"       ", "Conservatory", "Hallway 6	   ", "Dining room", "Hallway 7     ", "Billiard  ", "       "},
		// 	{"Start 5", "Hallway 8   ", "Secret Passage", "Hallway 9  ", "Secret Passage", "Hallway 10", "Start 2"},
		// 	{"       ", "Lounge      ", "Hallway 11    ", "Hall       ", "Hallway 12    ", "Library   ", "       "},
		// 	{"       ", "            ", "Start 4       ", "           ", "Start 3       ", "          ", "       "}
		// };

	static void drawBoard(String[][] board, HashMap<String, String> playersMap, String previousMove) {
		//Print out the banner
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		System.out.println("###################################################################################");
		System.out.println("################################ Clueless Game Board ##############################");
		System.out.println("###################################################################################");
		System.out.println("Board Key:");
		System.out.println("R: Room");
		System.out.println("S: Starting Position");
		System.out.println("H: Hallway");
		System.out.println("T: Secret Tunnel");	

		//no previous move for the first run. 
		if(previousMove != ""){
			System.out.println("\n                    Previous Move: " + previousMove);
		}
		
		System.out.println("###################################################################################");
		System.out.println("___________________________________________________________________________________\n");

		//iterate over board to print
		for(int i = 0; i <= 5; i++){
			for(int j = 0; j <= 6; j++){
				if(board[i][j] == "   "){
					System.out.print("            ");
				}
				else{
					System.out.print(board[i][j] + "         ");
				}
			}
			
			System.out.print("\n");

			//print out the players if they are present in any of this row's locations
			for(int j = 0; j <= 6; j++){
				String location = String.valueOf(i)+','+String.valueOf(j);
				
				if(playersMap.containsValue(location)){
					List<String> playersAtLocation = getAllKeysForValue(playersMap, location);
					int length = playersAtLocation.size();
					length = 3*length;
					String blank = "";
				
					//create whitespace for formatting	
					if(length < 12){
						int whiteSpace = 12 - length	;
						for(int k = 0; k < whiteSpace; k++){
							blank = blank + " ";
						}
					}
					System.out.print(playersAtLocation + blank);
				}
				else{
					System.out.print("            ");
				}
			}
			System.out.println("\n\n\n___________________________________________________________________________________\n");
		}
	}

	public static void movePlayer(String player, String position){
		position = position.trim();

		//convert the position string to a string showing index location
		for(int i = 0; i <= 5; i++){
			for(int j = 0; j <= 6; j++){
				if(board[i][j].trim().equals(position)){
					position = String.valueOf(i) + "," + String.valueOf(j);
					break;
				}
			}
		}

		//get the player's last location
		String from = playersMap.get(player);
		int row = Integer.parseInt(from.split(",")[0]);
		int col = Integer.parseInt(from.split(",")[1]);
		from = board[row][col];
		
		//update the players location
		playersMap.put(player, position);

		//get the location the player just moved to 
		String to = playersMap.get(player);
		row = Integer.parseInt(to.split(",")[0]);
		col = Integer.parseInt(to.split(",")[1]);
		to = board[row][col];

		//parse string for what the previous move was and redraw the game board
		String previousMove = "Player "+ player +" moved from " + from + " to " + to;
		drawBoard(board, playersMap, previousMove);
	}

	static void initGame(){

		//initialize the game board, placing all players in their respective locations
		playersMap.put("1", "1,6");
		playersMap.put("2", "3,6");
		playersMap.put("3", "5,4");
		playersMap.put("4", "5,2");
		playersMap.put("5", "3,0");
		playersMap.put("6", "1,0");

		//draw the initial gameboard
		drawBoard(board, playersMap, "");
	}

	//helper method to get all of the players (keys) at a location (value) in the playersMap
	static <K, V> List<K> getAllKeysForValue(HashMap<K, V> mapOfWords, V value) {
		List<K> listOfKeys = null; 
		if(mapOfWords.containsValue(value)){
			listOfKeys = new ArrayList<>();				
			for (HashMap.Entry<K, V> entry : mapOfWords.entrySet()){
				if (entry.getValue().equals(value)){
					listOfKeys.add(entry.getKey());
				}
			}
		}
		return listOfKeys;	
	}

	public static void main(String[] args) {
		initGame();
		String input = null;
			
		do{
			//option menu for moving or quitting the program
			System.out.println("###################################################################################");
			System.out.println("######################################## Menu #####################################");
			System.out.println("###################################################################################");
			System.out.println("m: move a player");
			System.out.println("q: quit");
			System.out.print("Select an option: ");

			//get console input 
			Scanner scan = new Scanner(System.in);	
			input = scan.nextLine();
			
			switch (input) {

			case "m":
				//prompt user for player selection
				String player = null;
				System.out.print("Enter the player you would like to move (1-6): ");
				player = scan.nextLine();
				
				//prompt user for location selection
				String position = null; 
				System.out.print("Enter the location you want move player " + player + " to: ");
				position = scan.nextLine();
				
				movePlayer(player, position);
				break;

			case "q":
				break;
			} 
		} while (!input.equals("q")); 
	}

	//TODO method getAdjacentRooms
}