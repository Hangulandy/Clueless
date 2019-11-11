package edu.jhu.teamundecided.clueless.gameBoard;

import edu.jhu.teamundecided.clueless.player.Player;

import java.util.*;

public class GameBoard{

	public Room blank = new Room("");
	public Room study = new Room("study");
	public Room hall = new Room("hall");
	public Room lounge = new Room("lounge");
	public Room library = new Room("library");
	public Room billiard_room = new Room("billiard_room");
	public Room dining_room = new Room("dining_room");
	public Room conservatory = new Room("conservatory");
	public Room ballroom = new Room("ballroom");
	public Room kitchen = new Room("kitchen");
	public Room hallway_1 = new Room("hallway_1");
	public Room hallway_2 = new Room("hallway_2");
	public Room hallway_3 = new Room("hallway_3");
	public Room hallway_4 = new Room("hallway_4");
	public Room hallway_5 = new Room("hallway_5");
	public Room hallway_6 = new Room("hallway_6");
	public Room hallway_7 = new Room("hallway_7");
	public Room hallway_8 = new Room("hallway_8");
	public Room hallway_9 = new Room("hallway_9");
	public Room hallway_10 = new Room("hallway_10");
	public Room hallway_11 = new Room("hallway_11");
	public Room hallway_12 = new Room("hallway_12");

	public ArrayList<Room> rooms = new ArrayList<>(Arrays.asList(study, hallway_1, hall, hallway_2, lounge, hallway_3, blank, hallway_4, blank, hallway_5, library, hallway_6, billiard_room, hallway_7, dining_room, hallway_8, blank, hallway_9, blank, hallway_10, conservatory, hallway_11, ballroom, hallway_12, kitchen));

	public static void main(String[] args){
		GameBoard gb = new GameBoard();

		//study
		ArrayList<Room> adjacentRooms = new ArrayList<>(Arrays.asList(gb.billiard_room, gb.hallway_1, gb.hallway_3));
		gb.study.setAdjacentRooms(adjacentRooms);
		
		//hall
		adjacentRooms = new ArrayList<>(Arrays.asList(gb.hallway_1, gb.hallway_2, gb.hallway_4));
		gb.hall.setAdjacentRooms(adjacentRooms);

		//lounge
		adjacentRooms = new ArrayList<>(Arrays.asList(gb.hallway_2, gb.hallway_5, gb.billiard_room));
		gb.lounge.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.hallway_3, gb.hallway_6, gb.hallway_8));
		gb.library.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.hallway_4, gb.hallway_6, gb.hallway_7, gb.hallway_9));
		gb.billiard_room.setAdjacentRooms(adjacentRooms);
	
		adjacentRooms = new ArrayList<>(Arrays.asList(gb.hallway_5, gb.hallway_7, gb.hallway_10));
		gb.dining_room.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.hallway_8, gb.hallway_11, gb.billiard_room));
		gb.conservatory.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.hallway_9, gb.hallway_11, gb.hallway_12));
		gb.ballroom.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.hallway_10, gb.hallway_12, gb.billiard_room));
		gb.kitchen.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.study, gb.hall));
		gb.hallway_1.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.hall, gb.lounge));
		gb.hallway_2.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.study, gb.library));
		gb.hallway_3.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.hall , gb.billiard_room));
		gb.hallway_4.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.lounge , gb.dining_room));
		gb.hallway_5.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.library , gb.billiard_room));
		gb.hallway_6.setAdjacentRooms(adjacentRooms);
	
		adjacentRooms = new ArrayList<>(Arrays.asList(gb.billiard_room , gb.dining_room));
		gb.hallway_7.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.library , gb.conservatory));
		gb.hallway_8.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.billiard_room , gb.ballroom));
		gb.hallway_9.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.dining_room , gb.kitchen));
		gb.hallway_10.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.conservatory , gb.ballroom));
		gb.hallway_11.setAdjacentRooms(adjacentRooms);

		adjacentRooms = new ArrayList<>(Arrays.asList(gb.ballroom , gb.kitchen));
		gb.hallway_12.setAdjacentRooms(adjacentRooms);

		//create players here for the meantime
		ArrayList<Player> players = new ArrayList<Player>();
		Player scarlet = new Player("Miss_Scarlet", gb.hallway_2);
		Player mustard = new Player("Col._Mustard", gb.hallway_5);
		Player white = new Player("Mrs._White", gb.hallway_12);
		Player green = new Player("Mr._Green", gb.hallway_11);
		Player peacock = new Player("Mrs._Peacock", gb.hallway_8);
		Player plum = new Player("Prof._Plum", gb.hallway_3);
		
		players.add(scarlet);
		players.add(mustard);
		players.add(white);
		players.add(green);
		players.add(peacock);
		players.add(plum);

		System.out.println("\nRender board:");
		render(gb, players);

		System.out.println("\nGet possible moves for Mrs. Peacock:");
		ArrayList<Room> moves = getPossibleMoves(peacock, players, gb);

		// Print out mrs.peacock's moves
		for(Room r : moves){
			System.out.println(r.getRoomName());
		}

		//move mrs peacock
		System.out.println("\nMove Mrs. Peacock to Conservatory");
		move(peacock, gb.conservatory, players, gb);


		System.out.println("\nGet possible moves for Mrs. Peacock:");
		moves = getPossibleMoves(peacock, players, gb);

		// Print out mrs.peacock's moves after her player moves
		for(Room r : moves){
			System.out.println(r.getRoomName());
		}
	}

	public static void render(GameBoard gb, ArrayList<Player> players){
		String roomRow = "";
		String playerRow = "";
		int maxWidth = 25;

		String divider = "";
		while(divider.length() < maxWidth*5){
			divider += "_";
		}

		for(int i = 0; i < gb.rooms.size(); i++){
			Room room = gb.rooms.get(i);
			String roomName = room.getRoomName();		
			int length = roomName.length();
			String playersInRoom = "";
			
			for(int k = 0; k < players.size(); k++){
				Player player = players.get(k); 
			
				if(player.getLocation() == room){
					if(playersInRoom == "")
						playersInRoom = player.getName();
					else if (playersInRoom != "")
						playersInRoom = playersInRoom + ", " + player.getName();
				}
			}

			while(playersInRoom.length() < maxWidth){
				playersInRoom += " ";
			}

			playerRow = playerRow + playersInRoom;
			
			while(roomName.length() < maxWidth){
				roomName += " ";
			}

			roomRow = roomRow + roomName;

			if((i + 1)%5==0 && i != 0){
				System.out.println(divider);
				System.out.println(roomRow + "\n");
				System.out.println(playerRow + "\n\n\n");
				roomRow = "";
				playerRow = "";
			}
		}	
	}

	public static ArrayList<Room> getPossibleMoves(Player player, ArrayList<Player> players, GameBoard gb){

		ArrayList<Room> adjacentRooms = player.getLocation().getAdjacentRooms();

		for(int i = 0; i < adjacentRooms.size(); i++){
			Room r = adjacentRooms.get(i);

			//can't move to a hallway if another person is in it
			if(r.getRoomName().startsWith("hallway")){
				
				for(int j = 0; j < players.size(); j++){
					Player p = players.get(j);

					if(p.getLocation() == r){
						adjacentRooms.remove(r);
						i = i - 1;
					}
				}
			}
		}

		return adjacentRooms;
	}

	public static void move(Player player, Room room, ArrayList<Player> players, GameBoard gb){
		player.setLocation(room);
		render(gb, players);
	}
}