package edu.jhu.teamundecided.clueless.gameBoard;

import edu.jhu.teamundecided.clueless.player.Player;

import java.util.ArrayList;

public class Room
{

   private String _roomName;
   private ArrayList<Room> _adjacentRooms;
   private boolean _isHall;
   private ArrayList<Player> _occupants;


   public Room(String roomName, boolean isHall)
   {

      this._roomName = roomName;
      this._isHall = isHall;
      _adjacentRooms = new ArrayList<Room>();
      _occupants = new ArrayList<Player>();

   }


   public ArrayList<Room> getPossibleMoves()
   {

      ArrayList<Room> possibleMoves = new ArrayList<Room>();

      for (Room room : _adjacentRooms)
      {
         if (room.getIsHall())
         {
            if (room.getOccupants().size() > 0)
            {
               continue;
            }
         }
         possibleMoves.add(room);
      }

      return possibleMoves;
   }


   public boolean getIsHall()
   {

      return _isHall;
   }


   public ArrayList<Player> getOccupants()
   {

      return _occupants;
   }


   public void set_occupants(ArrayList<Player> _occupants)
   {

      this._occupants = _occupants;
   }


   public String getRoomName()
   {

      return _roomName;
   }


   public ArrayList<Room> getAdjacentRooms()
   {

      return _adjacentRooms;
   }


   public void setAdjacentRooms(ArrayList<Room> adjacentRooms)
   {

      this._adjacentRooms = adjacentRooms;
   }


   public void removeOccupant(Player player)
   {

      _occupants.remove(player);
   }


   public void addOccupant(Player player)
   {

      _occupants.add(player);
   }


}
