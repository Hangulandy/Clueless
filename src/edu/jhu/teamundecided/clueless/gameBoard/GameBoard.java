package edu.jhu.teamundecided.clueless.gameBoard;

import edu.jhu.teamundecided.clueless.player.Player;

import java.util.*;

public class GameBoard
{

   public Room blank;
   public Room study;
   public Room hall;
   public Room lounge;
   public Room library;
   public Room billiard_room;
   public Room dining_room;
   public Room conservatory;
   public Room ballroom;
   public Room kitchen;
   public Room hallway_1;
   public Room hallway_2;
   public Room hallway_3;
   public Room hallway_4;
   public Room hallway_5;
   public Room hallway_6;
   public Room hallway_7;
   public Room hallway_8;
   public Room hallway_9;
   public Room hallway_10;
   public Room hallway_11;
   public Room hallway_12;

   public ArrayList<Room> rooms;


   public GameBoard()
   {

      blank = new Room("", false);
      study = new Room("study", false);
      hall = new Room("hall", false);
      lounge = new Room("lounge", false);
      library = new Room("library", false);
      billiard_room = new Room("billiard_room", false);
      dining_room = new Room("dining_room", false);
      conservatory = new Room("conservatory", false);
      ballroom = new Room("ballroom", false);
      kitchen = new Room("kitchen", false);
      hallway_1 = new Room("hallway_1", true);
      hallway_2 = new Room("hallway_2", true);
      hallway_3 = new Room("hallway_3", true);
      hallway_4 = new Room("hallway_4", true);
      hallway_5 = new Room("hallway_5", true);
      hallway_6 = new Room("hallway_6", true);
      hallway_7 = new Room("hallway_7", true);
      hallway_8 = new Room("hallway_8", true);
      hallway_9 = new Room("hallway_9", true);
      hallway_10 = new Room("hallway_10", true);
      hallway_11 = new Room("hallway_11", true);
      hallway_12 = new Room("hallway_12", true);

      study.setAdjacentRooms(new ArrayList<>(Arrays.asList(billiard_room, hallway_1, hallway_3)));
      hall.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_1, hallway_2, hallway_4)));
      lounge.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_2, hallway_5, billiard_room)));
      library.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_3, hallway_6, hallway_8)));
      billiard_room.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_4, hallway_6, hallway_7, hallway_9)));
      dining_room.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_5, hallway_7, hallway_10)));
      conservatory.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_8, hallway_11, billiard_room)));
      ballroom.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_9, hallway_11, hallway_12)));
      kitchen.setAdjacentRooms(new ArrayList<>(Arrays.asList(hallway_10, hallway_12, billiard_room)));
      hallway_1.setAdjacentRooms(new ArrayList<>(Arrays.asList(study, hall)));
      hallway_2.setAdjacentRooms(new ArrayList<>(Arrays.asList(hall, lounge)));
      hallway_3.setAdjacentRooms(new ArrayList<>(Arrays.asList(study, library)));
      hallway_4.setAdjacentRooms(new ArrayList<>(Arrays.asList(hall, billiard_room)));
      hallway_5.setAdjacentRooms(new ArrayList<>(Arrays.asList(lounge, dining_room)));
      hallway_6.setAdjacentRooms(new ArrayList<>(Arrays.asList(library, billiard_room)));
      hallway_7.setAdjacentRooms(new ArrayList<>(Arrays.asList(billiard_room, dining_room)));
      hallway_8.setAdjacentRooms(new ArrayList<>(Arrays.asList(library, conservatory)));
      hallway_9.setAdjacentRooms(new ArrayList<>(Arrays.asList(billiard_room, ballroom)));
      hallway_10.setAdjacentRooms(new ArrayList<>(Arrays.asList(dining_room, kitchen)));
      hallway_11.setAdjacentRooms(new ArrayList<>(Arrays.asList(conservatory, ballroom)));
      hallway_12.setAdjacentRooms(new ArrayList<>(Arrays.asList(ballroom, kitchen)));

      rooms = new ArrayList<>(Arrays.asList(study, hallway_1, hall, hallway_2, lounge, hallway_3
              , blank, hallway_4, blank, hallway_5, library, hallway_6, billiard_room, hallway_7, dining_room, hallway_8, blank, hallway_9, blank, hallway_10, conservatory, hallway_11, ballroom, hallway_12, kitchen));

   }


   public String render(ArrayList<Player> players)
   {

      String renderedBoard = "";
      String roomRow = "";
      String playerRow = "";
      int maxWidth = 25;

      String divider = "";
      while (divider.length() < maxWidth * 5)
      {
         divider += "_";
      }

      for (int i = 0; i < rooms.size(); i++)
      {
         Room room = rooms.get(i);
         String roomName = room.getRoomName();
         int length = roomName.length();
         String playersInRoom = "";

         for (int k = 0; k < players.size(); k++)
         {
            Player player = players.get(k);

            if (player.getLocation() == room)
            {
               if (playersInRoom == "")
                  playersInRoom = player.getCharacterName();
               else if (playersInRoom != "")
                  playersInRoom = playersInRoom + ", " + player.getCharacterName();
            }
         }

         while (playersInRoom.length() < maxWidth)
         {
            playersInRoom += " ";
         }

         playerRow = playerRow + playersInRoom;

         while (roomName.length() < maxWidth)
         {
            roomName += " ";
         }

         roomRow = roomRow + roomName;

         if ((i + 1) % 5 == 0 && i != 0)
         {
            // Commented out because of string return instead of print.
            // System.out.println(divider);
            // System.out.println(roomRow + "\n");
            // System.out.println(playerRow + "\n\n\n");

            renderedBoard += divider + "\n" + roomRow + "\n" + playerRow + "\n\n\n";
            roomRow = "";
            playerRow = "";
         }
      }
      return renderedBoard;
   }


   public void displayGameBoard(String[] tokens)
   {

      HashMap<String, String> playerLocations = new HashMap<String, String>();

      for (int m = 0; m < tokens.length - 1; m++)
      {
         String player = tokens[m];
         String location = tokens[m + 1];

         playerLocations.put(player, location);
      }

      String roomRow = "";
      String playerRow = "";
      int maxWidth = 25;

      String divider = "";
      while (divider.length() < maxWidth * 5)
      {
         divider += "_";
      }

      for (int i = 0; i < rooms.size(); i++)
      {
         Room room = rooms.get(i);

         String roomName = room.getRoomName();

         String playersInRoom = "";

         for (String player : playerLocations.keySet())
         {
            String test = playerLocations.get(player);
            if (test.equals(roomName))
            {
               if (playersInRoom == "")
               {
                  playersInRoom = player;
               }
               else if (playersInRoom != "")
               {
                  playersInRoom = playersInRoom + ", " + player;
               }
            }
         }

         while (playersInRoom.length() < maxWidth)
         {
            playersInRoom += " ";
         }

         playerRow = playerRow + playersInRoom;

         while (roomName.length() < maxWidth)
         {
            roomName += " ";
         }

         roomRow += roomName;

         if ((i + 1) % 5 == 0 && i != 0)
         {
            System.out.println(divider);
            System.out.println(roomRow + "\n");
            System.out.println(playerRow + "\n\n\n");

            roomRow = "";
            playerRow = "";
         }
      }
   }

   public String getGameBoardData(ArrayList<Player> players)
   {

      StringBuilder msg = new StringBuilder("updateGameBoard");

      for (Player player : players)
      {
         msg.append(" ");
         msg.append(player.getCharacterName());
         msg.append(" ");
         if (player.getLocation() == null)
         {
            msg.append("blank");
         } else
         {
            msg.append(player.getLocation().getRoomName());
         }
      }

      return msg.toString();
   }


   public void move(Player player, Room room, ArrayList<Player> players, GameBoard gb)
   {

      player.setLocation(room);
      render(players);
   }


   public boolean movePlayer(Player player, String roomName)
   {

      // remove player from room only if player already exists in a room
      if (player.getLocation() != null)
      {
         player.getLocation().removeOccupant(player);
      }

      Room room = findRoom(roomName);

      room.addOccupant(player);

      player.setLocation(room);

      System.out.println(player.getCharacterName() + " moved to " + room.getRoomName());

      return true;
   }


   private Room findRoom(String roomName)
   {

      for (Room room : rooms)
      {
         if (room.getRoomName().equalsIgnoreCase(roomName))
         {
            System.out.println("Found room : " + roomName);
            return room;
         }
      }
      System.out.println("Did not find room : " + roomName);
      return null;
   }


}
